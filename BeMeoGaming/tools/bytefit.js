// Tinh max f (ty le chieu cao) de dx/dy part nam trong byte (-128..127)
// Doc: CharInfo tu Char.cs, dx/dy tu build/caitrang_part.txt, cf dai dien tu report.
const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const charCs = fs.readFileSync('C:/Users/Administrator/Downloads/TuanBip/Assets/Scripts/Game1/Char.cs', 'utf8');
const st = charCs.indexOf('CharInfo = new int[33]');
const en = charCs.indexOf('\n        };', st);
const body = charCs.slice(st, en < 0 ? st + 40000 : en);
const re = /new\s+int\s*\[3\]\s*\{\s*(-?\d+)\s*,\s*(-?\d+)\s*,\s*(-?\d+)\s*\}|new\s+int\s*\[3\](?!\s*\{)/g;
const rows = [];
let m;
while ((m = re.exec(body))) rows.push(m[1] !== undefined ? [+m[1], +m[2], +m[3]] : [0, 0, 0]);

const txt = fs.readFileSync(path.join(root, 'build/caitrang_part.txt'), 'utf8');
const parts = {};
for (const s of ['head', 'leg', 'body']) {
  const line = txt.split('\n').find(l => l.includes(s + '=['));
  const key = s + '=[';
  parts[s] = JSON.parse(line.slice(line.indexOf(key) + key.length - 1));
}
const rep = {};
const report = fs.readFileSync(path.join(root, 'build/caitrang_report.txt'), 'utf8');
for (const mm of report.matchAll(/slot=(\d+) idx=(\d+) id=(\d+) cf=(\d+)/g)) rep[+mm[3]] = { slot: +mm[1], cf: +mm[4] };

// slot: 0=head,1=leg,2=body  <->  CharInfo col: 0=head,1=leg,2=body
const items = [];
for (const s of ['head', 'leg', 'body']) {
  for (const p of parts[s]) {
    const r = rep[p[0]];
    const ci = rows[r.cf * 4 + r.slot];
    const ci_x = ci[1], ci_y = ci[2];
    const lx = p[1] + ci_x, ly = p[2] - ci_y;   // dx = lx - ci_x, dy = ly + ci_y
    items.push({ id: p[0], slot: s, cf: r.cf, ci_x, ci_y, lx, ly, dx: p[1], dy: p[2] });
  }
}
let f = 1, worst = null;
for (const it of items) {
  const c = [];
  if (it.lx < 0) c.push([(128 + it.ci_x) / (-it.lx), 'dx<-128 id=' + it.id + ' (lx=' + it.lx.toFixed(1) + ' ci_x=' + it.ci_x + ')']);
  else if (it.lx > 0) c.push([(127 + it.ci_x) / it.lx, 'dx>127 id=' + it.id]);
  if (it.ly < 0) c.push([(128 + it.ci_y) / (-it.ly), 'dy<-128 id=' + it.id + ' (ly=' + it.ly.toFixed(1) + ' ci_y=' + it.ci_y + ')']);
  else if (it.ly > 0) c.push([(127 + it.ci_y) / it.ly, 'dy>127 id=' + it.id]);
  for (const [v, w] of c) if (v < f) { f = v; worst = w; }
}
const bad = items.filter(i => Math.abs(i.dx) > 127 || Math.abs(i.dy) > 127);
console.log('part vuot byte: ' + bad.length + '/' + items.length);
console.log('dx range: ' + Math.min(...items.map(i => i.dx)) + ' .. ' + Math.max(...items.map(i => i.dx)));
console.log('dy range: ' + Math.min(...items.map(i => i.dy)) + ' .. ' + Math.max(...items.map(i => i.dy)));
console.log('max f = ' + f.toFixed(4) + '  -> ' + worst);
console.log('chieu cao toi da = ' + Math.round(446 * f) + ' px x2 (hien tai 446)');
console.log('head reps: ' + items.filter(i => i.slot === 'head').map(i => 'cf' + i.cf + '(ci_y=' + i.ci_y + ')').join(' '));
