// Sua loi: icon id > 32767 vuot Short -> xoa icon allocation loi, ve header 32745
const fs = require('fs');
let del = 0;
for (let id = 32745; id <= 32780; id++) {
  for (const z of [2, 3, 4]) {
    const p = `data/icon_botnet/x${z}/${id}.png`;
    if (fs.existsSync(p)) { fs.unlinkSync(p); del++; }
  }
}
console.log('da xoa', del, 'png loi (32745..32780)');
const OLD = 32745; // header goc truoc khi extend
for (let z = 1; z <= 4; z++) {
  const p = `data/smallimage_version/x${z}/smallimage_version_data`;
  const d = fs.readFileSync(p);
  const h = (d[0] << 8) | d[1];
  if (h <= OLD) { console.log(`x${z}: header=${h} already <= ${OLD}`); continue; }
  const out = Buffer.alloc(2 + OLD);
  d.copy(out, 0, 0, 2 + OLD); // lay 2 + OLD byte dau (byte id cu giu nguyen)
  out[0] = (OLD >> 8) & 0xff; out[1] = OLD & 0xff;
  fs.writeFileSync(p, out);
  console.log(`x${z}: header ${h} -> ${OLD} (truncate)`);
}
