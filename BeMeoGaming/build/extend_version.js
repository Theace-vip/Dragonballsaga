// Extend smallimage_version_data header 32745 -> 32781 (them 36 slot -1), moi id cu giu nguyen
const fs = require('fs');
const NEW = 32781;
for (let z = 1; z <= 4; z++) {
  const p = `data/smallimage_version/x${z}/smallimage_version_data`;
  const d = fs.readFileSync(p);
  const header = (d[0] << 8) | d[1];
  if (header >= NEW) { console.log(`x${z}: header=${header} >= ${NEW}, bo qua`); continue; }
  const out = Buffer.alloc(2 + NEW, 0xff);
  d.copy(out, 0); // giu toan bo cu (header cu + byte id cu)
  out[0] = (NEW >> 8) & 0xff; out[1] = NEW & 0xff;
  fs.writeFileSync(p, out);
  console.log(`x${z}: header ${header} -> ${NEW} (+${NEW - header} slot)`);
}
