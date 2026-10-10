// fix_smallimage_range.js — ghi byte trung gian trong data/smallimage_version/x{1..4}/smallimage_version_data
// cho dải id mới (icon nằm GIỮA file nên extendSmallImageVersion không với tới → byte cũ = -1).
// Byte tại offset (2 + id) = len(icon_botnet/x{z}/{id}.png) % 127, thiếu file = 255 (-1).
// Giữ nguyên header (2 byte BE) và mọi byte khác. Backup .bak trước khi ghi.
//
// Dùng: node tools/fix_smallimage_range.js <idStart> <idEnd>   (vd: node tools/fix_smallimage_range.js 26512 26547)

const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '..');
const SV = path.join(ROOT, 'data', 'smallimage_version');
const ICON = path.join(ROOT, 'data', 'icon_botnet');

const idStart = parseInt(process.argv[2], 10);
const idEnd = parseInt(process.argv[3], 10);
if (!Number.isInteger(idStart) || !Number.isInteger(idEnd) || idEnd < idStart) {
  console.error('Dung: node tools/fix_smallimage_range.js <idStart> <idEnd>');
  process.exit(1);
}

let total = 0;
for (const z of [1, 2, 3, 4]) {
  const f = path.join(SV, `x${z}`, 'smallimage_version_data');
  const buf = fs.readFileSync(f);
  const header = (buf[0] << 8) | buf[1];
  if (idEnd >= header) {
    console.error(`x${z}: id ${idEnd} >= header ${header} → không có byte, bỏ qua (cần extend trước)`);
    continue;
  }
  const bak = f + '.bak';
  if (!fs.existsSync(bak)) fs.copyFileSync(bak === f ? f : f, bak);

  let changed = 0;
  for (let id = idStart; id <= idEnd; id++) {
    const png = path.join(ICON, `x${z}`, `${id}.png`);
    let val = 255; // -1 = thiếu
    if (fs.existsSync(png)) val = fs.statSync(png).size % 127;
    const off = 2 + id;
    if (buf[off] !== val) { buf[off] = val; changed++; }
  }
  fs.writeFileSync(f, buf);
  total += changed;
  console.log(`x${z}: header=${header} patched ${changed} byte (${idStart}..${idEnd})`);
}
console.log('Tong byte da ghi:', total);
