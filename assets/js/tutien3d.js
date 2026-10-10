/* ==========================================================================
   NROKuRA — TUTIEN3D.JS  (canh 3D WebGL: huyet nguyet, nui troi, vong phu van)
   ES module: import Three.js tu CDN (co thu nhieu nguon). Neu khong tai duoc
   hoac may khong co WebGL thi bo qua im lang -> hieu ung CSS/canvas 2D van chay.
   ========================================================================== */

const NGUON = [
    "https://cdn.jsdelivr.net/npm/three@0.160.0/build/three.module.js",
    "https://unpkg.com/three@0.160.0/build/three.module.js",
    "https://cdn.skypack.dev/three@0.160.0"
];

window.__bm3d = window.__bm3d || { ok: false, ly_do: "chua chay", fps: 0, objects: 0 };

const buocHieuUng = (() => {
    try {
        return /[?&]bm=on(&|$|=)/.test(location.search) || window.localStorage.getItem("bmMotion") === "on";
    } catch (e) { return false; }
})();

const giam = (() => {
    if (buocHieuUng) { return false; }
    try { return window.matchMedia("(prefers-reduced-motion: reduce)").matches; } catch (e) { return false; }
})();
const mobile = window.innerWidth <= 768 || (navigator.maxTouchPoints > 0 && window.innerWidth <= 900);

async function tai_three() {
    let loi = null;
    for (const url of NGUON) {
        try {
            const mod = await import(/* @vite-ignore */ url);
            return mod;
        } catch (e) { loi = e; }
    }
    throw loi || new Error("khong tai duoc three.js");
}

function tao_texture_quang(ctxSize) {
    const c = document.createElement("canvas");
    c.width = c.height = ctxSize;
    const g = c.getContext("2d");
    const grd = g.createRadialGradient(ctxSize / 2, ctxSize / 2, 0, ctxSize / 2, ctxSize / 2, ctxSize / 2);
    grd.addColorStop(0, "rgba(255,225,220,1)");
    grd.addColorStop(0.25, "rgba(255,120,120,0.75)");
    grd.addColorStop(0.55, "rgba(200,30,45,0.28)");
    grd.addColorStop(1, "rgba(0,0,0,0)");
    g.fillStyle = grd;
    g.fillRect(0, 0, ctxSize, ctxSize);
    return c;
}

function chay(THREE) {
    const fx = document.getElementById("bm-fx");
    if (!fx) { throw new Error("khong tim thay lop #bm-fx"); }

    let renderer;
    try {
        renderer = new THREE.WebGLRenderer({ alpha: true, antialias: !mobile, powerPreference: "high-performance" });
    } catch (e) {
        window.__bm3d.ly_do = "khong tao duoc WebGL";
        return;
    }
    /* ---- May KHONG co GPU that (WebGL phan mem: SwiftShader/llvmpipe...):
       dung han 3D ngay tu dau, tra lai lop CSS + canvas 2D cho nhe may. ---- */
    let ten_gpu = "";
    try {
        const gl = renderer.getContext();
        const ext = gl.getExtension("WEBGL_debug_renderer_info");
        ten_gpu = ext ? String(gl.getParameter(ext.UNMASKED_RENDERER_WEBGL)) : "";
    } catch (e) { /* bo qua */ }
    window.__bm3d.gpu = ten_gpu;
    if (/swiftshader|software|llvmpipe|basic render|mesa offscreen/i.test(ten_gpu)) {
        try { renderer.dispose(); } catch (e) { /* bo qua */ }
        window.__bm3d.ok = false;
        window.__bm3d.ly_do = "tat vi GPU phan mem (" + ten_gpu.slice(0, 60) + ")";
        window.__bm3d.cap = 3;
        window.__bm3d.che_do = "tat (giu nen CSS tinh cho may yeu)";
        document.body.classList.add("bm-nhe");
        document.dispatchEvent(new CustomEvent("bm-giam-tai", { detail: { cap: 3, ly_do: "gpu-phan-mem" } }));
        return;
    }

    renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, mobile ? 1.15 : 1.6));
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.setClearColor(0x000000, 0);
    const canvas = renderer.domElement;
    canvas.id = "bm-3d";
    canvas.style.position = "absolute";
    canvas.style.inset = "0";
    fx.insertBefore(canvas, fx.querySelector("#bm-2d"));

    const scene = new THREE.Scene();
    scene.fog = new THREE.FogExp2(0x12030a, 0.017);

    const camera = new THREE.PerspectiveCamera(56, window.innerWidth / window.innerHeight, 0.1, 400);
    camera.position.set(0, 3.2, 26);

    /* ---- Huyet nguyet ---- */
    const moon = new THREE.Group();
    const mat_moon = new THREE.MeshBasicMaterial({ color: 0xff6a63 });
    const cau = new THREE.Mesh(new THREE.SphereGeometry(4.4, 48, 48), mat_moon);
    moon.add(cau);

    const tex_quang = new THREE.CanvasTexture(tao_texture_quang(256));
    const quang = new THREE.Sprite(new THREE.SpriteMaterial({
        map: tex_quang, color: 0xff4a4a, transparent: true, opacity: 0.85,
        blending: THREE.AdditiveBlending, depthWrite: false
    }));
    quang.scale.set(30, 30, 1);
    moon.add(quang);
    moon.position.set(15, 9.5, -34);
    scene.add(moon);

    const anh_sang = new THREE.PointLight(0xff5a5a, 2.4, 120, 1.6);
    anh_sang.position.copy(moon.position);
    scene.add(anh_sang);

    /* ---- Nui troi nhieu lop (parallax) ---- */
    function tao_nui(soNui, z, ban_kinh, mau, cao) {
        const grp = new THREE.Group();
        const mat = new THREE.MeshStandardMaterial({ color: mau, roughness: 1, metalness: 0, flatShading: true });
        for (let i = 0; i < soNui; i++) {
            const h = cao * (0.55 + Math.random() * 0.8);
            const cone = new THREE.Mesh(new THREE.ConeGeometry(ban_kinh * (0.6 + Math.random() * 0.8), h, 4 + Math.floor(Math.random() * 3)), mat);
            cone.position.set((Math.random() - 0.5) * 150, -16 + h / 2, z + (Math.random() - 0.5) * 18);
            cone.rotation.y = Math.random() * Math.PI;
            grp.add(cone);
        }
        scene.add(grp);
        return grp;
    }

    const nui_xa = tao_nui(mobile ? 12 : 20, -70, 12, 0x1b0810, 26);
    const nui_giua = tao_nui(mobile ? 9 : 15, -48, 9, 0x240a13, 20);
    const nui_gan = tao_nui(mobile ? 7 : 11, -26, 7, 0x2c0c16, 15);

    /* ---- Vong phu van kim xoay 3D ---- */
    function tao_vong(ban_kinh, day, mau, so_van, nghieng) {
        const grp = new THREE.Group();
        const mat = new THREE.MeshBasicMaterial({ color: mau, transparent: true, opacity: 0.9 });
        const vong = new THREE.Mesh(new THREE.TorusGeometry(ban_kinh, day, 6, 96), mat);
        grp.add(vong);
        const mat_van = new THREE.MeshBasicMaterial({ color: mau, transparent: true, opacity: 0.75 });
        for (let i = 0; i < so_van; i++) {
            const a = (i / so_van) * Math.PI * 2;
            const van = new THREE.Mesh(new THREE.BoxGeometry(0.16, 0.16, 1.05), mat_van);
            van.position.set(Math.cos(a) * ban_kinh, Math.sin(a) * ban_kinh, 0);
            van.rotation.z = a;
            grp.add(van);
        }
        grp.rotation.x = nghieng;
        return grp;
    }

    const vong_grp = new THREE.Group();
    vong_grp.add(tao_vong(6.6, 0.06, 0xe0b458, mobile ? 22 : 40, 0));
    vong_grp.add(tao_vong(7.6, 0.045, 0xd81f2a, mobile ? 16 : 30, 0.35));
    vong_grp.position.set(0, 4.4, -18);
    vong_grp.rotation.x = -0.28;
    scene.add(vong_grp);

    /* ---- Da troi (da bay) ---- */
    const mats = [];
    for (let i = 0; i < (mobile ? 6 : 11); i++) {
        const r = 0.35 + Math.random() * 0.75;
        const da = new THREE.Mesh(
            new THREE.IcosahedronGeometry(r, 0),
            new THREE.MeshStandardMaterial({ color: 0x3a0f1a, roughness: 1, flatShading: true })
        );
        da.position.set((Math.random() - 0.5) * 46, 1 + Math.random() * 11, -30 + Math.random() * 16);
        da.userData = { y0: da.position.y, bien: 0.5 + Math.random() * 1.4, pha: Math.random() * Math.PI * 2 };
        scene.add(da);
        mats.push(da);
    }

    /* ---- Tro / tan lua 3D ---- */
    const soTro = mobile ? 260 : 900;
    const vt = new Float32Array(soTro * 3);
    const mau = new Float32Array(soTro * 3);
    const toc = new Float32Array(soTro);
    const c1 = new THREE.Color(0xff8a5a), c2 = new THREE.Color(0xe0b458), c3 = new THREE.Color(0xd81f2a);
    for (let i = 0; i < soTro; i++) {
        vt[i * 3] = (Math.random() - 0.5) * 130;
        vt[i * 3 + 1] = Math.random() * 46 - 12;
        vt[i * 3 + 2] = -60 + Math.random() * 70;
        const c = Math.random() > 0.7 ? c2 : (Math.random() > 0.45 ? c1 : c3);
        mau[i * 3] = c.r; mau[i * 3 + 1] = c.g; mau[i * 3 + 2] = c.b;
        toc[i] = 0.6 + Math.random() * 2.1;
    }
    const geo_tro = new THREE.BufferGeometry();
    geo_tro.setAttribute("position", new THREE.BufferAttribute(vt, 3));
    geo_tro.setAttribute("color", new THREE.BufferAttribute(mau, 3));
    const tex_tro = new THREE.CanvasTexture(tao_texture_quang(64));
    const tro = new THREE.Points(geo_tro, new THREE.PointsMaterial({
        size: mobile ? 0.55 : 0.42, map: tex_tro, vertexColors: true, transparent: true,
        opacity: 0.9, blending: THREE.AdditiveBlending, depthWrite: false, sizeAttenuation: true
    }));
    scene.add(tro);

    /* ---- Tuong tac chuot / cuon ---- */
    const chuot = { x: 0, y: 0, tx: 0, ty: 0 };
    window.addEventListener("pointermove", (e) => {
        chuot.tx = (e.clientX / window.innerWidth - 0.5) * 2;
        chuot.ty = (e.clientY / window.innerHeight - 0.5) * 2;
    }, { passive: true });

    let cuon = window.scrollY || 0;
    window.addEventListener("scroll", () => { cuon = window.scrollY || 0; }, { passive: true });

    /* ---- Vong lap ---- */
    const dong_ho = new THREE.Clock();
    let khung = null;
    let dem = 0, moc = 0;

    /* ---- Tu giam chat luong cho may yeu (do FPS thuc te roi ha dan) ---- */
    window.__bm3d.lich_su = [];
    let cap = 0;

    function ha_chat_luong(fps_do_duoc) {
        cap++;
        window.__bm3d.cap = cap;
        window.__bm3d.lich_su.push("cap " + cap + " (" + Math.round(fps_do_duoc) + " fps)");
        if (cap === 1) {
            renderer.setPixelRatio(1);
            doi_kich_thuoc();
            geo_tro.setDrawRange(0, Math.floor(soTro * 0.55));
        } else if (cap === 2) {
            nui_xa.visible = false;
            geo_tro.setDrawRange(0, Math.floor(soTro * 0.3));
            quang.scale.set(22, 22, 1);
            document.dispatchEvent(new CustomEvent("bm-giam-tai", { detail: { cap: cap } }));
        } else if (cap >= 3) {
            // Dung han vong lap 3D, giu lai khung hinh cuoi cung lam nen tinh
            if (khung) { cancelAnimationFrame(khung); khung = null; }
            renderer.render(scene, camera);
            window.__bm3d.che_do = "tinh (da dung de giu may yeu chay muot)";
            document.dispatchEvent(new CustomEvent("bm-giam-tai", { detail: { cap: cap } }));
            return true;
        }
        return false;
    }

    function ve() {
        const dt = Math.min(dong_ho.getDelta(), 0.05);
        const t = dong_ho.elapsedTime;

        chuot.x += (chuot.tx - chuot.x) * 0.045;
        chuot.y += (chuot.ty - chuot.y) * 0.045;

        camera.position.x = chuot.x * 2.6;
        camera.position.y = 3.2 - chuot.y * 1.5 - cuon * 0.004;
        camera.position.z = 26 - Math.min(cuon * 0.006, 3.2);
        camera.lookAt(0, 3 + chuot.y * 0.4, -14);

        moon.rotation.y += dt * 0.05;
        quang.material.opacity = 0.72 + 0.16 * Math.sin(t * 0.9);
        mat_moon.color.setHSL(0.005, 0.78, 0.55 + 0.05 * Math.sin(t * 0.9));

        nui_xa.position.x = -chuot.x * 1.2 - cuon * 0.002;
        nui_giua.position.x = -chuot.x * 2.1 - cuon * 0.004;
        nui_gan.position.x = -chuot.x * 3.4 - cuon * 0.007;

        vong_grp.rotation.z += dt * 0.22;
        vong_grp.rotation.x = -0.28 + Math.sin(t * 0.35) * 0.07;
        vong_grp.children[1].rotation.z -= dt * 0.16;

        for (const da of mats) {
            da.position.y = da.userData.y0 + Math.sin(t * 0.5 * da.userData.bien + da.userData.pha) * 1.1;
            da.rotation.x += dt * 0.22;
            da.rotation.y += dt * 0.17;
        }

        const pos = geo_tro.attributes.position;
        for (let i = 0; i < soTro; i++) {
            let y = pos.array[i * 3 + 1] + toc[i] * dt * 1.5;
            if (y > 36) { y = -14; pos.array[i * 3] = (Math.random() - 0.5) * 130; }
            pos.array[i * 3 + 1] = y;
        }
        pos.needsUpdate = true;

        renderer.render(scene, camera);

        dem++;
        if (t - moc >= 0.8) {
            const fps = dem / (t - moc);
            window.__bm3d.fps = Math.round(fps);
            dem = 0;
            moc = t;
            window.__bm3d.so_lan_do = (window.__bm3d.so_lan_do || 0) + 1;
            // Ngay lan do dau tien da qua te (<20fps) thi dung han, khong ha tung buoc
            if (window.__bm3d.so_lan_do === 1 && fps < 20) {
                window.__bm3d.cap = 3;
                window.__bm3d.che_do = "tinh (may qua yeu, da dung 3D) " + Math.round(fps) + "fps";
                window.__bm3d.lich_su.push("dung ngay (" + Math.round(fps) + " fps)");
                try { document.body.classList.add("bm-nhe"); } catch (e) { /* bo qua */ }
                document.dispatchEvent(new CustomEvent("bm-giam-tai", { detail: { cap: 3 } }));
                renderer.render(scene, camera);
                if (khung) { cancelAnimationFrame(khung); khung = null; }
                return;
            }
            if (window.__bm3d.so_lan_do > 2) {
                if (cap === 0 && fps < 26) { ha_chat_luong(fps); }
                else if (cap === 1 && fps < 20) { ha_chat_luong(fps); }
                else if (cap === 2 && fps < 14) { if (ha_chat_luong(fps)) { return; } }
            }
        }

        khung = requestAnimationFrame(ve);
    }

    function doi_kich_thuoc() {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
    }
    window.addEventListener("resize", () => { try { doi_kich_thuoc(); } catch (e) { /* bo qua */ } });

    document.addEventListener("visibilitychange", () => {
        if (document.hidden) {
            if (khung) { cancelAnimationFrame(khung); khung = null; }
        } else if (!khung && !giam) {
            khung = requestAnimationFrame(ve);
        }
    });

    window.__bm3d.ok = true;
    window.__bm3d.ly_do = "dang chay";
    window.__bm3d.objects = scene.children.length;
    window.__bm3d.cap = 0;
    window.__bm3d.che_do = mobile ? "3D (chat luong di dong)" : "3D (chat luong cao)";

    if (giam) {
        renderer.render(scene, camera);
    } else {
        khung = requestAnimationFrame(ve);
    }
}

(async () => {
    try {
        if (!document.getElementById("bm-fx")) { throw new Error("lop #bm-fx chua san sang"); }
        const THREE = await tai_three();
        chay(THREE);
    } catch (e) {
        window.__bm3d.ok = false;
        window.__bm3d.ly_do = "loi: " + (e && e.message ? e.message : String(e));
    }
})();
