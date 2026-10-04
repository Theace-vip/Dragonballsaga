#!/usr/bin/env node
/**
 * qsheet.js - tao contact sheet cho 1 bo spine thanh MOT anh PNG (base64) -> _sheet_frag.html
 * (preview server chi phuc file da dang ky nen anh phai inline base64)
 *
 * Cach dung: node qsheet.js --set g13_qiongqi_nan --frames 0,9 --cols 6
 * Output: data/spine_frames/_sheet_frag.html  (CharFrameSheet se ghep vao _match.html)
 */
const fs = require('fs');
const path = require('path');
const puppeteer = require('puppeteer-core');

const arg = (n, d) => { const i = process.argv.indexOf('--' + n); return i >= 0 ? process.argv[i + 1] : d; };
const SET = arg('set');
const FRAMES = arg('frames', '0,9').split(',').map((s) => s.trim().padStart(3, '0'));
const COLS = parseInt(arg('cols', '6'), 10);
const CHROME = arg('chrome', 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe');
const OUT = arg('out', path.resolve(__dirname, '..', '..', 'data', 'spine_frames', '_sheet_frag.html'));
const DIR = path.resolve(__dirname, '..', '..', 'data', 'spine_frames', SET);

if (!SET || !fs.existsSync(DIR)) { console.error('Thieu --set hoac khong thay thu muc: ' + DIR); process.exit(1); }

async function main() {
  const anims = fs.readdirSync(DIR).filter((d) => fs.statSync(path.join(DIR, d)).isDirectory()).sort();
  const cells = [];
  for (const a of anims) {
    for (const f of FRAMES) {
      const p = path.join(DIR, a, 'f' + f + '.png');
      if (!fs.existsSync(p)) continue;
      cells.push({ a, f, url: 'data:image/png;base64,' + fs.readFileSync(p).toString('base64') });
    }
  }
  const html = `<!doctype html><meta charset="utf-8"><style>
body{background:#1e2530;color:#eee;font-family:sans-serif;margin:6px}
h3{margin:4px 0 8px}
.g{display:grid;grid-template-columns:repeat(${COLS},1fr);gap:4px}
.c{background:#111;padding:2px;text-align:center;font-size:11px}
.c img{width:100%;height:150px;object-fit:contain;background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px}
</style><h3>${SET} - chon bien the anim (${cells.length} anh)</h3><div class="g">` +
    cells.map((c) => `<div class="c"><img src="${c.url}"><div>${c.a}/f${c.f}</div></div>`).join('') + '</div>';

  const browser = await puppeteer.launch({
    executablePath: CHROME, headless: true,
    args: ['--enable-unsafe-swiftshader', '--use-gl=angle', '--use-angle=swiftshader', '--no-sandbox', '--allow-file-access-from-files'],
  });
  const page = await browser.newPage();
  await page.setViewport({ width: 1100, height: 800, deviceScaleFactor: 1 });
  page.setDefaultNavigationTimeout(180000);
  await page.setContent(html, { waitUntil: 'domcontentloaded', timeout: 180000 });
  await page.evaluate(() => Promise.all([...document.images].map((i) => i.decode().catch(() => {}))));
  const shot = await page.screenshot({ fullPage: true, type: 'png' });
  await browser.close();

  const out = `<div><img src="data:image/png;base64,${shot.toString('base64')}" style="max-width:100%"></div>`;
  fs.writeFileSync(OUT, out);
  console.log(`${cells.length} anh -> ${OUT} (${Math.round(shot.length / 1024)} KB)`);
}
main().catch((e) => { console.error(e); process.exit(1); });
