// Kiem tra toan bo bang part — co dx/dy nao vuot byte (-128..127) khong?
// Manager.java:402 Byte.parseByte → vuot byte la server khong khoi dong duoc.
//
// Tao file du lieu (giu UTF-8, khong dong them header):
//   mysql -h127.0.0.1 -uroot -D hondaodragon -N -B -e "SELECT id, TYPE, DATA FROM part" > /tmp/part.tsv
// Chay:
//   node tools/checkpart-byte.js /tmp/part.tsv
const fs = require('fs');

const file = process.argv[2] || '/tmp/part.tsv';
const lines = fs.readFileSync(file, 'utf8').split('\n').filter(Boolean);

let rows = 0, triples = 0, bad = 0, minDx = 128, maxDx = -128, minDy = 128, maxDy = -128;
for (const line of lines) {
    const [id, type, data] = line.split('\t');
    if (!data) continue;
    let arr;
    try { arr = JSON.parse(data); } catch (e) {
        console.log('LOI JSON: part id=' + id + ' — ' + e.message);
        bad++;
        continue;
    }
    rows++;
    for (const t of arr) {
        triples++;
        const [icon, dx, dy] = t;
        minDx = Math.min(minDx, dx); maxDx = Math.max(maxDx, dx);
        minDy = Math.min(minDy, dy); maxDy = Math.max(maxDy, dy);
        if (dx < -128 || dx > 127 || dy < -128 || dy > 127) {
            bad++;
            console.log('VUOT BYTE: part id=' + id + ' TYPE=' + type + ' icon=' + icon + ' dx=' + dx + ' dy=' + dy);
        }
    }
}
console.log('part rows=' + rows + '  triples=' + triples + '  vuot_byte=' + bad);
console.log('dx range: ' + minDx + ' .. ' + maxDx + '   dy range: ' + minDy + ' .. ' + maxDy);
process.exit(bad ? 1 : 0);
