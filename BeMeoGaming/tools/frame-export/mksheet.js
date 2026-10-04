// node mksheet.js <set>
// tao fragment contact-sheet (khong co <html>) cho set frame da export,
// chay truoc tools/CharFrameSheet.java de ghep vao _match.html
const fs = require('fs');
const path = require('path');

const set = process.argv[2] || 'g13_juitianxuannv';
const root = path.join(__dirname, '..', '..', 'data', 'spine_frames', set);
const meta = JSON.parse(fs.readFileSync(path.join(root, 'meta.json'), 'utf8'));
const out = path.join(path.dirname(root), '_sheet_frag.html');

let html = `<style>
.spine h4{margin:14px 0 2px;color:#8cf}
.spine .row{display:flex;flex-wrap:wrap;gap:3px;margin-bottom:6px}
.spine .f{background:#000;text-align:center;padding:1px;border-radius:4px}
.spine .f img{display:block;image-rendering:pixelated;width:110px;height:146px;object-fit:contain;background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 50%) 50%/12px 12px}
.spine small{color:#8ab;font-size:10px;display:block}
</style><div class='spine'><h2>spine ${set}: ${meta.anims.length} anim, fps ${meta.fps}, ${meta.viewport.w}x${meta.viewport.h}</h2>`;

for (const a of meta.anims) {
  html += `<h4>${a.anim} (dur ${a.dur}s, ${a.count}f)</h4><div class='row'>`;
  for (let i = 0; i < a.count; i++) {
    const t = a.times[i].toFixed(2);
    html += `<div class='f'><img src='${set}/${a.anim}/f${String(i).padStart(3, '0')}.png'><small>${a.anim}<br>f${i} @${t}s</small></div>`;
  }
  html += `</div>`;
}
html += `</div>`;

fs.writeFileSync(out, html);
console.log('wrote', out, fs.statSync(out).size, 'bytes');
