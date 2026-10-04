#!/usr/bin/env node
/**
 * export.js - Xuat frame PNG tu cac bo spine da export (qua spine-viewer dang chay).
 *
 * Co che: mo Chrome headless -> mo http://localhost:5750 (viewer serve/tools/spine-viewer),
 * goi loadSet() co san cua viewer (da gop skin, da fix UV/texture), seek tung moc thoi gian,
 * frame() ve vao canvas (preserveDrawingBuffer -> toDataURL duoc), luu PNG.
 *
 * Yeu cau: server viewer dang chay -> chay tools/spine-viewer/start-viewer.bat truoc.
 *
 * Cach dung:
 *   node export.js --list                          # liet ke ten cac bo
 *   node export.js --set Full_FX                   # xuat TAT CA anim, fps 15
 *   node export.js --set whis --anim idle,walk     # chi 2 anim
 *   node export.js --set-list goku,vegeta,whis --anim all --fps 12
 *   node export.js --set H21201 --anim all --max 90 --size 768
 *
 * Output: <out>/<set>/<anim>/f000.png ... + <out>/<set>/meta.json
 *   mac dinh out = BeMeoGaming/data/spine_frames
 */
const fs = require('fs');
const path = require('path');
const puppeteer = require('puppeteer-core');

const arg = (name, def) => { const i = process.argv.indexOf('--' + name); return i >= 0 ? process.argv[i + 1] : def; };
const has = (n) => process.argv.includes('--' + n);

const VIEWER = arg('viewer', 'http://localhost:5750');
const ANIM = arg('anim', 'all');
const FPS = Math.max(1, parseInt(arg('fps', '15'), 10) || 15);
const SIZE = Math.max(128, parseInt(arg('size', '1024'), 10) || 1024);
const MAX_FRAMES = Math.max(1, parseInt(arg('max', '60'), 10) || 60);
const OUT = arg('out', path.resolve(__dirname, '..', '..', 'data', 'spine_frames'));
const CHROME = arg('chrome', 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe');

async function main() {
  const list = await (await fetch(VIEWER + '/list')).json();

  if (has('list')) {
    list.forEach((s) => console.log(s.name + '\t' + s.ver + '\t' + (s.skel || '')));
    return;
  }

  let names = [];
  if (arg('set-list')) names = arg('set-list').split(',').map((s) => s.trim()).filter(Boolean);
  else if (arg('set')) names = arg('set').split(',').map((s) => s.trim()).filter(Boolean);
  if (!names.length) { console.error('Thieu --set <ten> hoac --set-list a,b,c (xem --list)'); process.exit(1); }

  const unknown = names.filter((n) => !list.find((s) => s.name === n));
  if (unknown.length) { console.error('Khong tim thay bo: ' + unknown.join(', ')); process.exit(1); }

  console.log('Chrome: ' + CHROME);
  const browser = await puppeteer.launch({
    executablePath: CHROME,
    headless: true,
    args: [
      '--enable-unsafe-swiftshader',
      '--use-gl=angle',
      '--use-angle=swiftshader',
      '--no-sandbox',
      '--disable-dev-shm-usage',
      '--hide-scrollbars',
      '--mute-audio'
    ]
  });

  try {
    const page = await browser.newPage();
    page.on('pageerror', (e) => console.error('[pageerror] ' + e.message));
    page.on('console', (m) => { if (m.type() === 'error') console.error('[console] ' + m.text()); });
    await page.setViewport({ width: SIZE, height: SIZE, deviceScaleFactor: 1 });
    await page.goto(VIEWER + '/', { waitUntil: 'domcontentloaded', timeout: 30000 });
    await page.waitForFunction('typeof sets !== "undefined" && sets.length > 0', { timeout: 20000 });

    let totalPng = 0;
    for (const setName of names) {
      const t0 = Date.now();
      const res = await page.evaluate(async (name, animSel, fps, maxFrames) => {
        const item = sets.find((s) => s.name === name);
        if (!item) return { error: 'khong thay bo trong /list' };
        try { await loadSet(item); } catch (e) { return { error: 'loadSet loi: ' + e.message }; }
        if (!skeleton || !animState) {
          return { error: 'khong parse duoc skeleton: ' + (document.getElementById('status').textContent || '') };
        }
        playing = false;      // dung RAF advance -> pose chi doi khi minh seek
        zoomFactor = 1;
        frame();              // dam bao canvas da co kich thuoc

        const all = skeleton.data.animations.slice();
        const withTl = all.filter((a) => a.timelines && a.timelines.length > 0);
        const anims = (animSel === 'all' ? withTl.map((a) => a.name) : animSel.split(',').map((s) => s.trim())
          .filter((n) => withTl.some((a) => a.name === n)));
        const missing = animSel === 'all' ? [] : animSel.split(',').map((s) => s.trim())
          .filter((n) => !withTl.some((a) => a.name === n));
        const out = {
          set: name, ver: item.ver, anims: [], missing,
          emptySkipped: all.filter((a) => !a.timelines || !a.timelines.length).map((a) => a.name),
          viewport: { w: canvas.width, h: canvas.height }
        };
        if (!anims.length) return out;

        const seek = (animName, t) => {
          animState.setAnimation(0, animName, true);
          animState.update(t);
          animState.apply(skeleton);
          skeleton.updateWorldTransform();
        };

        for (const animName of anims) {
          const an = all.find((a) => a.name === animName);
          if (!an) { out.anims.push({ anim: String(animName), skip: 'khong tim thay' }); continue; }
          const dur = an.duration || 0;
          if (dur <= 0) { out.anims.push({ anim: animName, skip: 'duration=0' }); continue; }

          // Fit camera theo union bbox qua nhieu moc cua CHINH anim nay (camera on dinh qua cac frame)
          const boxes = [];
          const nMarks = Math.min(11, Math.max(3, Math.ceil(dur * 10)));
          for (let i = 0; i < nMarks; i++) {
            seek(animName, dur * i / (nMarks - 1));
            boxes.push(...attachmentBoxes());
          }
          if (boxes.length) fitCamera(boxes);

          // Thoi diem xuat: buoc 1/fps, khong vuot dur
          const step = 1 / fps;
          const times = [];
          for (let t = 0; t < dur - 1e-4 && times.length < maxFrames; t += step) times.push(+t.toFixed(4));
          if (!times.length) times.push(0);

          const frames = [];
          for (const t of times) {
            seek(animName, t);
            frame();
            frames.push({ t, png: canvas.toDataURL('image/png') });
          }
          out.anims.push({
            anim: animName, dur: +dur.toFixed(4), frames,
            cam: { camX: +camX.toFixed(2), camY: +camY.toFixed(2), camH: +camH.toFixed(2),
                   w: canvas.width, h: canvas.height }
          });
        }
        return out;
      }, setName, ANIM, FPS, MAX_FRAMES);

      if (res.error) { console.error(setName + ': LOI - ' + res.error); continue; }

      const dir = path.join(OUT, setName);
      let pngCount = 0;
      const meta = {
        set: res.set, ver: res.ver, exportedAt: new Date().toISOString(),
        fps: FPS, viewport: res.viewport,
        source: VIEWER + ' (spine_export_remote/' + setName + ')',
        anims: []
      };
      for (const a of res.anims) {
        if (a.skip) { console.log('  [' + setName + '] bo qua ' + a.anim + ' (' + a.skip + ')'); continue; }
        const ad = path.join(dir, a.anim.replace(/[^\w.-]+/g, '_'));
        fs.mkdirSync(ad, { recursive: true });
        a.frames.forEach((f, i) => {
          const b64 = f.png.slice(f.png.indexOf(',') + 1);
          fs.writeFileSync(path.join(ad, 'f' + String(i).padStart(3, '0') + '.png'), Buffer.from(b64, 'base64'));
          pngCount++;
        });
        meta.anims.push({ anim: a.anim, dur: a.dur, count: a.frames.length,
                          times: a.frames.map((f) => f.t), cam: a.cam });
        console.log('  [' + setName + '] ' + a.anim + ': ' + a.frames.length + ' frame (dur=' + a.dur + 's)');
      }
      if (res.missing.length) console.log('  [' + setName + '] khong tim thay anim: ' + res.missing.join(', '));
      if (res.emptySkipped.length) console.log('  [' + setName + '] anim rong (0 timeline): ' + res.emptySkipped.join(', '));
      fs.mkdirSync(dir, { recursive: true });
      fs.writeFileSync(path.join(dir, 'meta.json'), JSON.stringify(meta, null, 1));
      totalPng += pngCount;
      console.log(setName + ': ' + pngCount + ' PNG, ' + res.anims.filter((a) => a.frames).length + ' anim, ' + ((Date.now() - t0) / 1000).toFixed(1) + 's');
    }
    console.log('XONG. Tong ' + totalPng + ' PNG -> ' + OUT);
  } finally {
    await browser.close().catch(() => {});
  }
}

main().catch((e) => { console.error(e); process.exit(1); });
