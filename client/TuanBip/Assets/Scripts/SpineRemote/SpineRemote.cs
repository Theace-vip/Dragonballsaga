// SpineRemote - tai spine (.skel/.atlas/.png) tu GitHub CDN va ve cho nhan vat
// dang mac cai trang da cau hinh, KHONG can bake frame, KHONG can build lai khi
// them set moi chi can push Git.
//
// Cach dung (goi tu Char.paintCharBody, ho tro ca Game1/2/3):
//   bool daVe = SpineRemote.TryPaint(this, cx, cy, cdir, cf, head, body, leg,
//       g.getTranslateX(), g.getTranslateY() - mGraphics.addYWhenOpenKeyBoard, mGraphics.zoomLevel);
//   if (!daVe) { ... ve part bitmap cu ... }
//   -> Tra ve false khi dang tai / loi / khong phai cai trang spine -> fallback part.
//
// Yeu cau: set phai la spine 4.1 (runtime spine-csharp 4.1 kem trong SpineRemote/),
// moi set chi 1 atlas page, ten anim co "stand"/"run"/"attack"/"skill" (co the them -1/-2).
// Toa do: ve trong cung khong gian voi mGraphics.drawRegion (x2px).
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.Rendering;
using Spine;

public static class SpineRemote
{
    // ============================== CAU HINH ==============================
    // CDN: raw.githubusercontent hoac jsdelivr (jsdelivr co cache, nhanh hon)
    public const string BASE = "https://cdn.jsdelivr.net/gh/Theace-vip/Dragonballsaga@spine-src/spine_export_remote/";

    // item_id -> (set spine, scale = x2px / don vi the gioi — lay tu log bake GenCaitrang)
    class Cfg
    {
        public string set;
        public float s;
    }
    static readonly Dictionary<int, Cfg> table = new Dictionary<int, Cfg>
    {
        // Hau Tho (item 1939, part head 2155) — bake log: scale = 0.24797 x2px/world
        { 2155, new Cfg { set = "g13_houtu2", s = 0.24797f } },
        // Thieu Nu Tinh Khong (item 1940, part head 2158) — bake log: scale = 0.28805 x2px/world
        { 2158, new Cfg { set = "g13_xingkong_nv", s = 0.28805f } },
    };

    const int RT = 512;          // kich thuoc RenderTexture
    const int MARGIN = 48;       // px duoi trong RT de dat chan (cho vung hieu ung bay len)
    const float CHAR_PX = 400f;  // nhan cao ~400 RT px (phan duoc 512 - MARGIN)
    const int LAYER = 31;        // layer rieng cho spine (camera chi nhin layer nay)
    const int HOLD_FRAMES = 600; // xoa actor sau ~10s khong ve nua
    // Neu nhan bi dao dau/nguoc khi test in-game thi doi thanh true:
    public const bool FLIP_Y = false;

    // cf (0..32) -> trang thai anim (theo MAP cua GenCaitrang)
    static readonly string[] CF2CAT = new string[]
    {
        "stand", "stand",                             // 0,1 dung
        "run", "run", "run", "run", "run",            // 2..6 di bo
        "run", "run",                                 // 7,8 roi/nhay
        "attack", "attack",                           // 9,10 danh
        "run",                                        // 11 nhay ngang
        "attack",                                     // 12 charge
        "skill", "skill", "skill",                    // 13..15 skill
        "stand", "stand",                             // 16,17
        "skill", "skill", "skill", "skill",           // 18..21 skill bay
        "attack", "attack",                           // 22,23 (bi trung don)
        "skill",                                      // 24
        "run",                                        // 25 dich chuyen
        "skill", "skill", "skill",                    // 26..28
        "stand",                                      // 29
        "skill", "skill",                             // 30,31
        "attack"                                      // 32
    };
    static readonly string[] STAND_CAND = { "stand", "stand-1", "stand1-0", "idle", "normal" };
    static readonly string[] RUN_CAND = { "run", "run-1", "run1-0", "walk", "Chay1-0", "Chay" };
    static readonly string[] ATTACK_CAND = { "attack", "attack-1", "attack-2", "attack1-0", "danh", "Combo1" };
    static readonly string[] SKILL_CAND = { "skill", "skill-1", "skill-2", "skill1-0" };
    static readonly string[] DEAD_CAND = { "dead", "die", "death", "death1-0" };

    // ============================== TRANG THAI ==============================
    class SetData
    {
        public string name;
        public Cfg cfg;
        public int state;              // 0=dang tai, 1=ready, 2=failed
        public SkeletonData skeletonData;
        public AnimationStateData animData;
        public Texture2D texture;
        public Material material;
        public Dictionary<string, string> anim = new Dictionary<string, string>(); // cat -> ten anim
        public float ax, ay;           // tam x + day y (world) cua pose stand t=0
        public float F;                // RT px / world
        public bool rendered;          // da render RT lan dau chua
    }

    class Actor
    {
        public object owner;
        public SetData sd;
        public Skeleton skeleton;
        public Spine.AnimationState state;
        public string curCat = "";
        public GameObject go;
        public Mesh mesh;
        public RenderTexture rt;
        public int lastFrame;
        public bool rtOk;
        // buffer
        public float[] worldBuf = new float[1024];
        public Vector3[] verts = new Vector3[512];
        public Vector2[] uvs = new Vector2[512];
        public int[] tris = new int[3072];
    }

    static SpineRemoteHost host;
    static Camera cam;
    static GameObject camGo;
    static readonly Dictionary<string, SetData> sets = new Dictionary<string, SetData>();
    static readonly Dictionary<object, Actor> actors = new Dictionary<object, Actor>();
    static readonly Dictionary<string, Material> mats = new Dictionary<string, Material>();

    static string CacheDir(string set)
    {
        return Path.Combine(Application.persistentDataPath, "spine", set);
    }

    // ============================== GOI TU CHAR.CS ==============================
    // tx = g.getTranslateX(); ty = g.getTranslateY() - mGraphics.addYWhenOpenKeyBoard;
    // zoom = mGraphics.zoomLevel
    public static bool TryPaint(object owner, int cx, int cy, int cdir, int cf,
        int headId, int bodyId, int legId, int tx, int ty, int zoom)
    {
        Cfg cfg;
        if (!table.TryGetValue(headId, out cfg)) return false; // khong phai cai trang spine
        try
        {
            EnsureHost();
            SetData sd = GetSet(cfg);
            if (sd == null || sd.state != 1) return false;   // dang tai / loi -> fallback part

            Actor a = GetActor(owner, sd);
            if (a == null || !a.rtOk) return false;          // chua render RT lan dau -> fallback phan hoi
            a.lastFrame = Time.frameCount;

            // doi anim neu cf chuyen trang thai
            int idx = Mathf.Clamp(cf, 0, CF2CAT.Length - 1);
            string cat = CF2CAT[idx];
            string want;
            if (!sd.anim.TryGetValue(cat, out want)) sd.anim.TryGetValue("stand", out want);
            if (want != null && want != a.curCat)
            {
                a.state.SetAnimation(0, want, true);
                a.curCat = want;
            }

            // toa do cuoi cung (cung khong gian mGraphics.drawRegion: (cx + tx) * zoom)
            float anchorX = (cx + tx) * (float)zoom;
            float anchorY = (cy + ty) * (float)zoom;

            float S = cfg.s;                       // x2px / world
            float rect = RT * S / sd.F;            // kich thuoc toan RT tren man hinh
            float offB = MARGIN * S / sd.F;        // chan trong RT -> day man hinh
            float top = anchorY + offB - rect;
            float left = anchorX - rect * 0.5f;
            bool mirror = cdir != 1;
            // dao bang dest width/height AM — cung cach mGraphics.__drawRegion (transform 1/2) da chay tot
            float dx = mirror ? left + rect : left;
            float dy = FLIP_Y ? top + rect : top;
            float dw = mirror ? -rect : rect;
            float dh = FLIP_Y ? -rect : rect;
            Graphics.DrawTexture(new Rect(dx, dy, dw, dh), a.rt,
                new Rect(0f, 0f, 1f, 1f), 0, 0, 0, 0);
            return true;
        }
        catch (Exception e)
        {
            Debug.LogWarning("[SpineRemote] TryPaint loi: " + e.Message);
            return false; // bat ky loi gi -> fallback part
        }
    }

    // ============================== HOST ==============================
    static void EnsureHost()
    {
        if (host != null) return;
        GameObject go = new GameObject("SpineRemoteHost");
        UnityEngine.Object.DontDestroyOnLoad(go);
        host = go.AddComponent<SpineRemoteHost>();

        camGo = new GameObject("SpineRemoteCam");
        camGo.transform.SetParent(go.transform, false);
        camGo.layer = LAYER;
        cam = camGo.AddComponent<Camera>();
        cam.orthographic = true;
        cam.enabled = false;
        cam.useOcclusionCulling = false;
        cam.clearFlags = CameraClearFlags.SolidColor;
        cam.backgroundColor = new Color(0f, 0f, 0f, 0f);
        cam.cullingMask = 1 << LAYER;
        cam.nearClipPlane = 0.01f;
        cam.farClipPlane = 500f;
        cam.transform.position = new Vector3(0f, 0f, -50f);
    }

    // ============================== TAI SET ==============================
    static SetData GetSet(Cfg cfg)
    {
        SetData sd;
        if (sets.TryGetValue(cfg.set, out sd)) return sd;
        sd = new SetData { name = cfg.set, cfg = cfg, state = 0 };
        sets[cfg.set] = sd;
        host.StartCoroutine(LoadSet(sd));
        return sd;
    }

    static IEnumerator FetchCached(string set, string file, string url, Action<byte[]> done)
    {
        string local = Path.Combine(CacheDir(set), file);
        try
        {
            if (File.Exists(local))
            {
                done(File.ReadAllBytes(local));
                yield break;
            }
        }
        catch (Exception) { /* doc loi -> tai network */ }
        using (UnityWebRequest req = UnityWebRequest.Get(url))
        {
            yield return req.SendWebRequest();
            if (req.result != UnityWebRequest.Result.Success)
            {
                done(null);
                yield break;
            }
            try
            {
                Directory.CreateDirectory(CacheDir(set));
                File.WriteAllBytes(local, req.downloadHandler.data);
            }
            catch (Exception) { /* khong ghi duoc cache van dung duoc */ }
            done(req.downloadHandler.data);
        }
    }

    static IEnumerator LoadSet(SetData sd)
    {
        string root = BASE + sd.name + "/";
        // 1. atlas
        byte[] atlasBytes = null;
        yield return FetchCached(sd.name, sd.name + ".atlas", root + sd.name + ".atlas",
            b => atlasBytes = b);
        if (atlasBytes == null) { Fail(sd, "khong tai duoc atlas"); yield break; }
        string atlasText = System.Text.Encoding.UTF8.GetString(atlasBytes);

        // 2. ten page png (dong dau tien cua file atlas)
        string pageName = null;
        using (StringReader sr = new StringReader(atlasText))
        {
            string line;
            while ((line = sr.ReadLine()) != null)
            {
                line = line.Trim();
                if (line.EndsWith(".png")) { pageName = line; break; }
            }
        }
        if (string.IsNullOrEmpty(pageName)) { Fail(sd, "atlas khong co ten png"); yield break; }

        // 3. png
        byte[] pngBytes = null;
        yield return FetchCached(sd.name, pageName, root + pageName, b => pngBytes = b);
        if (pngBytes == null) { Fail(sd, "khong tai duoc png " + pageName); yield break; }

        // 4. skel (.skel truoc, 404 thi .json) — cache neu da co 1 trong 2
        byte[] skelBytes = null;
        bool isJson = false;
        string localSkel = Path.Combine(CacheDir(sd.name), sd.name + ".skel");
        string localJson = Path.Combine(CacheDir(sd.name), sd.name + ".json");
        if (File.Exists(localSkel)) skelBytes = File.ReadAllBytes(localSkel);
        else if (File.Exists(localJson)) { skelBytes = File.ReadAllBytes(localJson); isJson = true; }
        else
        {
            yield return FetchCached(sd.name, sd.name + ".skel", root + sd.name + ".skel",
                b => skelBytes = b);
            if (skelBytes == null)
            {
                isJson = true;
                yield return FetchCached(sd.name, sd.name + ".json", root + sd.name + ".json",
                    b => skelBytes = b);
            }
        }
        if (skelBytes == null) { Fail(sd, "khong tai duoc skel/json"); yield break; }

        // 5. build (parse loi -> ver 4.2/3.8 khac runtime 4.1 -> failed -> fallback part)
        try
        {
            Texture2D tex = new Texture2D(2, 2, TextureFormat.RGBA32, false);
            if (!tex.LoadImage(pngBytes)) { Fail(sd, "png khong doc duoc"); yield break; }
            tex.wrapMode = TextureWrapMode.Clamp;
            tex.filterMode = FilterMode.Bilinear;

            TextureLoaderFor loader = new TextureLoaderFor(tex);
            Atlas atlas = new Atlas(new StringReader(atlasText), "", loader);
            if (loader.extraPage || atlas.Pages.Count != 1)
            {
                Fail(sd, "set co nhieu hon 1 atlas page (chua ho tro)"); yield break;
            }

            AtlasAttachmentLoader aal = new AtlasAttachmentLoader(atlas);
            SkeletonData data;
            if (isJson)
            {
                SkeletonJson json = new SkeletonJson(aal);
                using (StringReader sr = new StringReader(System.Text.Encoding.UTF8.GetString(skelBytes)))
                    data = json.ReadSkeletonData(sr);
            }
            else
            {
                SkeletonBinary bin = new SkeletonBinary(aal);
                using (MemoryStream ms = new MemoryStream(skelBytes))
                    data = bin.ReadSkeletonData(ms);
            }

            sd.skeletonData = data;
            sd.animData = new AnimationStateData(data);
            sd.animData.DefaultMix = 0.12f;
            sd.texture = tex;

            // dat ten anim theo tung trang thai (thieu stand -> set khong dung duoc)
            ResolveAnim(data, STAND_CAND, "stand", sd.anim);
            if (!sd.anim.ContainsKey("stand")) { Fail(sd, "khong co anim stand"); yield break; }
            ResolveAnim(data, RUN_CAND, "run", sd.anim);
            ResolveAnim(data, ATTACK_CAND, "attack", sd.anim);
            ResolveAnim(data, SKILL_CAND, "skill", sd.anim);
            ResolveAnim(data, DEAD_CAND, "dead", sd.anim);
            if (!sd.anim.ContainsKey("run")) sd.anim["run"] = sd.anim["stand"];
            if (!sd.anim.ContainsKey("attack")) sd.anim["attack"] = sd.anim["stand"];
            if (!sd.anim.ContainsKey("skill")) sd.anim["skill"] = sd.anim["stand"];
            if (!sd.anim.ContainsKey("dead")) sd.anim["dead"] = sd.anim["stand"];

            // do bounds pose stand t=0 -> goc neo (chan giua) + chieu cao
            Skeleton sk = new Skeleton(data);
            Spine.AnimationState st = new Spine.AnimationState(sd.animData);
            st.SetAnimation(0, sd.anim["stand"], true);
            st.Update(0f);
            st.Apply(sk);
            sk.UpdateWorldTransform();
            float bx, by, bw, bh;
            float[] work = null;
            sk.GetBounds(out bx, out by, out bw, out bh, ref work);
            if (bw <= 0f || bh <= 0f) { Fail(sd, "bounds stand = 0"); yield break; }
            sd.ax = bx + bw * 0.5f;
            sd.ay = by;                 // day = chan
            sd.F = CHAR_PX / bh;        // RT px per world

            // material: PMA (mac dinh cua spine atlas) hoac straight alpha
            bool pma = atlasText.IndexOf("pma:true", StringComparison.OrdinalIgnoreCase) >= 0;
            Material m;
            if (!mats.TryGetValue(sd.name, out m))
            {
                Shader sh = Resources.Load<Shader>("SpineRemotePMA");
                if (sh == null) sh = Shader.Find("Hidden/SpineRemotePMA");
                if (sh == null) { Fail(sd, "khong tim thay shader SpineRemotePMA"); yield break; }
                m = new Material(sh);
                m.SetFloat("_SrcBlend", pma ? (float)UnityEngine.Rendering.BlendMode.One : (float)UnityEngine.Rendering.BlendMode.SrcAlpha);
                m.SetFloat("_DstBlend", (float)UnityEngine.Rendering.BlendMode.OneMinusSrcAlpha);
                m.SetTexture("_MainTex", tex);
                mats[sd.name] = m;
            }
            sd.material = m;
            sd.state = 1;
            Debug.Log("[SpineRemote] set " + sd.name + " ready: "
                + data.Animations.Items.Length + " anim, stand h=" + bh.ToString("F0")
                + " world, F=" + sd.F.ToString("F3"));
        }
        catch (Exception e)
        {
            Fail(sd, "parse/build loi: " + e.Message);
        }
    }

    static void Fail(SetData sd, string msg)
    {
        sd.state = 2;
        Debug.LogWarning("[SpineRemote] set " + sd.name + " FAILED: " + msg + " -> dung part bitmap");
    }

    static void ResolveAnim(SkeletonData data, string[] cands, string cat,
        Dictionary<string, string> into)
    {
        foreach (string c in cands)
        {
            if (data.FindAnimation(c) != null) { into[cat] = c; return; }
        }
        // startsWith tuyet doi khong phan biet hoa
        foreach (Spine.Animation a in data.Animations.Items)
        {
            foreach (string c in cands)
            {
                if (a.Name.StartsWith(c, StringComparison.OrdinalIgnoreCase))
                {
                    into[cat] = a.Name;
                    return;
                }
            }
        }
    }

    // ============================== ACTOR ==============================
    static Actor GetActor(object owner, SetData sd)
    {
        Actor a;
        if (actors.TryGetValue(owner, out a))
        {
            if (a.sd == sd) return a;
            DestroyActor(a); // doi set -> tao lai
        }
        a = new Actor { owner = owner, sd = sd, lastFrame = Time.frameCount };
        a.skeleton = new Skeleton(sd.skeletonData);
        a.state = new Spine.AnimationState(sd.animData);
        a.state.SetAnimation(0, sd.anim["stand"], true);
        a.curCat = sd.anim["stand"];

        a.rt = new RenderTexture(RT, RT, 0, RenderTextureFormat.ARGB32);
        a.rt.filterMode = FilterMode.Bilinear;
        a.rt.Create();

        a.go = new GameObject("spineActor_" + sd.name);
        a.go.layer = LAYER;
        MeshFilter mf = a.go.AddComponent<MeshFilter>();
        MeshRenderer mr = a.go.AddComponent<MeshRenderer>();
        a.mesh = new Mesh();
        a.mesh.MarkDynamic();
        mf.sharedMesh = a.mesh;
        mr.sharedMaterial = sd.material;
        mr.shadowCastingMode = ShadowCastingMode.Off;
        mr.receiveShadows = false;
        mr.lightProbeUsage = LightProbeUsage.Off;
        mr.reflectionProbeUsage = ReflectionProbeUsage.Off;
        actors[owner] = a;
        return a;
    }

    static void DestroyActor(Actor a)
    {
        actors.Remove(a.owner);
        if (a.go != null) UnityEngine.Object.Destroy(a.go);
        if (a.rt != null) { a.rt.Release(); UnityEngine.Object.Destroy(a.rt); }
        if (a.mesh != null) UnityEngine.Object.Destroy(a.mesh);
    }

    // ============================== RENDER MOI FRAME ==============================
    internal static void Tick(float dt)
    {
        int now = Time.frameCount;
        List<object> dead = null;
        foreach (KeyValuePair<object, Actor> kv in actors)
        {
            Actor a = kv.Value;
            if (now - a.lastFrame > HOLD_FRAMES)
            {
                if (dead == null) dead = new List<object>();
                dead.Add(kv.Key);
                continue;
            }
            if (a.sd.state != 1) continue;
            try
            {
                a.state.Update(dt);
                a.state.Apply(a.skeleton);
                a.skeleton.UpdateWorldTransform();
                BuildMesh(a);
                // setup camera cho actor nay va render vao RT
                float half = RT * 0.5f / a.sd.F;
                cam.orthographicSize = half;
                cam.transform.position = new Vector3(a.sd.ax, a.sd.ay + (RT * 0.5f - MARGIN) / a.sd.F, -50f);
                cam.targetTexture = a.rt;
                cam.Render();
                cam.targetTexture = null;
                a.rtOk = true;
                if (!a.sd.rendered)
                {
                    a.sd.rendered = true;
                    Debug.Log("[SpineRemote] da render RT lan dau cho " + a.sd.name);
                }
            }
            catch (Exception e)
            {
                Debug.LogWarning("[SpineRemote] tick loi: " + e.Message);
                a.sd.state = 2; // loi render -> fallback part
            }
        }
        if (dead != null)
            foreach (object k in dead)
                if (actors.ContainsKey(k)) DestroyActor(actors[k]);
    }

    static readonly int[] REGION_TRI_BASE = { 0, 1, 2, 2, 3, 0 };

    static void BuildMesh(Actor a)
    {
        int vc = 0, ic = 0;
        float[] wb = a.worldBuf;
        Vector3[] verts = a.verts;
        Vector2[] uvs = a.uvs;
        int[] tris = a.tris;
        ExposedList<Slot> drawOrder = a.skeleton.DrawOrder;
        Slot[] slots = drawOrder.Items;
        for (int si = 0; si < drawOrder.Count; si++)
        {
            Slot slot = slots[si];
            Attachment att = slot.Attachment;
            if (att == null) continue;
            int n;
            float[] auv;
            int baseV = vc;
            RegionAttachment ra = att as RegionAttachment;
            if (ra != null)
            {
                if (vc + 4 > verts.Length || ic + 6 > tris.Length) break;
                ra.ComputeWorldVertices(slot, wb, 0);
                n = 4;
                auv = ra.UVs;
                for (int i = 0; i < 4; i++)
                {
                    verts[vc + i] = new Vector3(wb[i * 2], wb[i * 2 + 1], 0f);
                    uvs[vc + i] = new Vector2(auv[i * 2], auv[i * 2 + 1]);
                }
                for (int i = 0; i < 6; i++) tris[ic + i] = baseV + REGION_TRI_BASE[i];
                ic += 6;
                vc += 4;
                continue;
            }
            VertexAttachment va = att as VertexAttachment;
            if (va == null) continue; // boundingbox/path/clip -> bo qua (clip set khong dung)
            n = va.WorldVerticesLength;
            if (n <= 0) continue;
            if (vc + n > verts.Length || ic + n * 3 > tris.Length) break;
            if (wb.Length < n * 2) Array.Resize(ref wb, n * 2);
            va.ComputeWorldVertices(slot, 0, n, wb, 0);
            MeshAttachment ma = att as MeshAttachment;
            if (ma == null) continue;
            float[] muv = ma.UVs;
            int[] mtris = ma.Triangles;
            for (int i = 0; i < n; i++)
            {
                verts[vc + i] = new Vector3(wb[i * 2], wb[i * 2 + 1], 0f);
                uvs[vc + i] = new Vector2(muv[i * 2], muv[i * 2 + 1]);
            }
            for (int i = 0; i < mtris.Length; i++) tris[ic + i] = baseV + mtris[i];
            ic += mtris.Length;
            vc += n;
        }
        Mesh m = a.mesh;
        m.Clear();
        if (vc == 0 || ic == 0) return;
        Vector3[] v2 = new Vector3[vc];
        Vector2[] uv2 = new Vector2[vc];
        int[] t2 = new int[ic];
        Array.Copy(verts, v2, vc);
        Array.Copy(uvs, uv2, vc);
        Array.Copy(tris, t2, ic);
        m.vertices = v2;
        m.uv = uv2;
        m.triangles = t2;
        m.bounds = new Bounds(new Vector3(a.sd.ax, a.sd.ay, 0f),
            new Vector3(10000f, 10000f, 100f)); // khong cho camera cull
    }

    // doc anh atlas cho toan bo page (chi page dau dung, page sau -> failed)
    class TextureLoaderFor : TextureLoader
    {
        public Texture2D tex;
        public bool extraPage;
        int pages;
        public TextureLoaderFor(Texture2D t) { tex = t; }
        public void Load(AtlasPage page, string path)
        {
            pages++;
            if (pages == 1) page.rendererObject = tex;
            else extraPage = true; // qua 1 page -> LoadSet se failed + fallback part
        }
        public void Unload(object texture)
        {
            // texture rieng per SetData, khong giai phong o day (giu den khi app thoat)
        }
    }
}

// Host rieng (top-level, khong long — AddComponent khong yeu cau ten file) chay tick render
// KHONG doi ten class nay: Beebyte RenameScriptableAssets se crash "could not be found"
// vi host chi tao runtime bang AddComponent, khong co trong scene/globalGameManagers.
// Dung System.Reflection.Obfuscation (Beebyte co ton trong) thay vi Beebyte.Obfuscator.Skip
// vi DLL cua Beebyte la Editor-only, game code khong reference duoc.
[System.Reflection.Obfuscation(Exclude = true, ApplyToMembers = true)]
public class SpineRemoteHost : MonoBehaviour
{
    private void LateUpdate()
    {
        SpineRemote.Tick(Time.deltaTime);
    }
}
