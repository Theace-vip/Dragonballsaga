#!/usr/bin/env node
/**
 * verify.js - Kiem tra frame PNG da xuat co noi dung khong.
 * - Tinh % pixel co alpha > 0 va mau trung bình cua tung frame mau.
 * - Tao data/spine_frames/_sheet.html (contact sheet, anh nhung base64)
 *   de xem mat trong preview ma khong can server phuc thu muc.
 *
 * Cach dung: node verify.js [set]
 */
const fs = require('fs');
const path = require('path');
const puppeteer = require('puppeteer-core');

const ROOT = path.resolve(__dirname, '..', '..', 'data', 'spine_frames');
const CHROME = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe';

function sampleFrames(setName) {
  const setDir = path.join(ROOT, setName);
  if (!fs.existsSync(setDir)) return [];
  const out = [];
  for (const anim of fs.readdirSync(setDir)) {
    const aDir = path.join(setDir, anim);
    if (!fs.statSync(aDir).isDirectory()) continue;
    const files = fs.readdirSync(aDir).filter((f) => f.endsWith('.png')).sort();
    if (!files.length) continue;
    const pick = [];
    const N = Math.min(6, files.length);
    for (let i = 0; i < N; i++) pick.push(files[Math.floor(i * (files.length - 1) / Math.max(1, N - 1))]);
    out.push({ anim, dir: aDir, files, pick });
  }
  return out;
}

async function main() {
  const setName = process.argv[2] || 'Full_FX';
  const groups = sampleFrames(setName);
  if (!groups.length) { console.error('Khong thay frame nao o ' + setName); process.exit(1); }

  // Ghep anh mau thanh data URI
  const items = [];
  for (const g of groups) for (const f of g.pick) {
    const b64 = fs.readFileSync(path.join(g.dir, f)).toString('base64');
    items.push({ anim: g.anim, file: f, uri: 'data:image/png;base64,' + b64 });
  }

  const browser = await puppeteer.launch({
    executablePath: CHROME, headless: true,
    args: ['--no-sandbox', '--disable-dev-shm-usage', '--hide-scrollbars', '--mute-audio']
  });
  try {
    const page = await browser.newPage();
    await page.setViewport({ width: 1400, height: 900 });
    await page.goto('about:blank');
    const stats = await page.evaluate(async (items) => {
      const res = [];
      for (const it of items) {
        const img = new Image();
        await new Promise((ok, no) => { img.onload = ok; img.onerror = no; img.src = it.uri; });
        const c = document.createElement('canvas');
        c.width = img.width; c.height = img.height;
        const g = c.getContext('2d');
        g.drawImage(img, 0, 0);
        const d = g.getImageData(0, 0, c.width, c.height).data;
        let n = 0, solid = 0, r = 0, gg = 0, b = 0;
        for (let i = 0; i < d.length; i += 4) {
          n++;
          if (d[i + 3] > 10) { solid++; r += d[i]; gg += d[i + 1]; b += d[i + 2]; }
        }
        res.push({
          anim: it.anim, file: it.file, w: c.width, h: c.height,
          pct: +(100 * solid / n).toFixed(2),
          color: solid ? [r / solid, gg / solid, b / solid].map((v) => Math.round(v)) : null
        });
      }
      return res;
    }, items);

    // In bang
    const byAnim = new Map();
    for (const s of stats) {
      if (!byAnim.has(s.anim)) byAnim.set(s.anim, []);
      byAnim.get(s.anim).push(s);
    }
    let blank = 0;
    for (const [anim, arr] of byAnim) {
      const pcts = arr.map((s) => s.pct);
      const zero = pcts.filter((p) => p < 0.5).length;
      blank += zero;
      const w = arr[0].w + 'x' + arr[0].h;
      console.log(anim.padEnd(20) + w.padEnd(10) +
        'fill%=[' + pcts.join(', ') + ']' + (zero ? '  (zero:' + zero + ')' : ''));
    }
    console.log('\nTong mau: ' + stats.length + ' frame, trong: ' + blank);

    // Contact sheet base64 de xem mat
    const html = ['<!doctype html><html><head><meta charset="utf-8"><title>Sheet ' + setName + '</title>',
      '<style>body{background:#1e2530;color:#eee;font-family:Segoe UI;margin:14px}',
      'h2{font-size:15px;border-bottom:1px solid #444}',
      '.row{display:flex;flex-wrap:wrap;gap:6px;margin-bottom:12px}',
      '.cell{text-align:center}.cell img{width:110px;height:110px;object-fit:contain;',
      'background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px;border:1px solid #555}',
      '.cell div{font-size:11px;color:#9ab}</style></head><body>'];
    for (const g of groups) {
      html.push('<h2>' + setName + ' / ' + g.anim + '</h2><div class="row">');
      for (const f of g.pick) {
        const s = stats.find((x) => x.anim === g.anim && x.file === f);
        const b64 = fs.readFileSync(path.join(g.dir, f)).toString('base64');
        html.push('<div class="cell"><img src="data:image/png;base64,' + b64 + '"><div>' +
          f + ' ' + (s ? s.pct + '%' : '') + '</div></div>');
      }
      html.push('</div>');
    }
    html.push('</body></html>');
    fs.writeFileSync(path.join(ROOT, '_sheet_' + setName + '.html'), html.join('\n'));
    console.log('\nDa ghi ' + path.join(ROOT, '_sheet_' + setName + '.html'));
  } finally {
    await browser.close();
  }
}

main().catch((e) => { console.error(e); process.exit(1); });
