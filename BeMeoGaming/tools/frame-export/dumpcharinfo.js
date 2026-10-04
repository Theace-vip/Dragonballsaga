// node dumpcharinfo.js <Char.cs>
// in bang CharInfo[33] = {headIdx, legIdx, bodyIdx} + nhom frame chia se
const fs = require('fs');
const src = fs.readFileSync(process.argv[2], 'utf8');
const st = src.indexOf('CharInfo = new int[33]');
const en = src.indexOf('\n        };', st);
const body = src.slice(st, en);
const cfs = [];
const re = /new int\[4\]\[\]\s*\{([\s\S]*?)\n        \}/g;
let m;
while ((m = re.exec(body))) {
  const rows = [];
  const rr = /new int\[3\]\s*\{\s*(-?\d+)\s*,\s*(-?\d+)\s*,\s*(-?\d+)\s*\}|new int\[3\](?!\s*\{)/g;
  let r;
  const chunk = m[1];
  let idx = 0;
  while (idx < chunk.length) {
    const sub = chunk.slice(idx);
    const a = /new\s+int\s*\[3\]\s*\{\s*(-?\d+)\s*,\s*(-?\d+)\s*,\s*(-?\d+)\s*\}/.exec(sub);
    const b = /new\s+int\s*\[3\](?!\s*\{)/.exec(sub);
    if (a && (!b || a.index <= b.index)) { rows.push([+a[1], +a[2], +a[3]]); idx += a.index + a[0].length; }
    else if (b) { rows.push([0, 0, 0]); idx += b.index + b[0].length; }
    else break;
  }
  cfs.push(rows);
}
console.log('cf count =', cfs.length);
const groups = (slot) => {
  const g = {};
  cfs.forEach((rows, cf) => { const k = rows[slot][0]; (g[k] = g[k] || []).push(cf); });
  return g;
};
const hg = groups(0), lg = groups(1), bg = groups(2);
console.log('\nHEAD groups:', JSON.stringify(hg));
console.log('\nLEG groups:');
for (const k of Object.keys(lg).sort((a, b) => a - b)) {
  const off = [...new Set(lg[k].map(cf => cfs[cf][1].slice(1).join(',')))];
  console.log('  idx ' + k + ': cf=' + lg[k].join(',') + '  offsets=' + off.join(' | '));
}
console.log('\nBODY groups:');
for (const k of Object.keys(bg).sort((a, b) => a - b)) {
  const off = [...new Set(bg[k].map(cf => cfs[cf][2].slice(1).join(',')))];
  console.log('  idx ' + k + ': cf=' + bg[k].join(',') + '  offsets=' + off.join(' | '));
}
console.log('\nFULL:');
cfs.forEach((rows, cf) => {
  console.log('cf' + String(cf).padStart(2) +
    '  head{' + rows[0].join(',') + '}  leg{' + rows[1].join(',') + '}  body{' + rows[2].join(',') + '}');
});
