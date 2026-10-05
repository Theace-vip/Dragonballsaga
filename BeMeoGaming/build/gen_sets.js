// Sinh danh sach spine set cho viewer online (re-logic cua serve.ps1 Get-SpineList)
const fs = require('fs');
const path = require('path');

const base = 'C:/Users/Administrator/Downloads/ExportedProject/spine_export_remote';
const out = [];

for (const dir of fs.readdirSync(base)) {
  const p = path.join(base, dir);
  if (!fs.statSync(p).isDirectory()) continue;
  const files = fs.readdirSync(p);
  const skel = files.find((f) => f.endsWith('.skel') || f.endsWith('.json'));
  const atlas = files.find((f) => f.endsWith('.atlas'));
  if (!skel || !atlas) continue;

  let ver = '4.1';
  if (skel.endsWith('.json')) {
    const text = fs.readFileSync(path.join(p, skel), 'utf8').slice(0, 200000);
    const m = text.match(/"spine"\s*:\s*"(\d+\.\d+)/);
    if (m) ver = m[1];
  } else {
    const fd = fs.openSync(path.join(p, skel), 'r');
    const buf = Buffer.alloc(Math.min(65536, fs.fstatSync(fd).size));
    fs.readSync(fd, buf, 0, buf.length, 0);
    fs.closeSync(fd);
    const m = buf.toString('ascii').match(/(\d\.\d)\.\d+/);
    if (m) ver = m[1];
  }
  out.push({ name: dir, skel, atlas, ver });
}
out.sort((a, b) => a.name.localeCompare(b.name));
fs.writeFileSync(path.join(__dirname, 'spine_web', 'sets.json'), JSON.stringify(out));
console.log('sets:', out.length, '| versions:', JSON.stringify(out.reduce((m, s) => ((m[s.ver] = (m[s.ver] || 0) + 1), m), {})));
