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

let rows = 0, triples = 0, bad = 0, weird = 0;
let minDx = 128, maxDx = -128, minDy = 128, maxDy = -128;
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
    for (const raw of arr) {
        triples++;
        // 8 row legacy luu dang string: "[17,0,0]" → bung ra truoc khi check
        let t = raw;
        if (typeof t === 'string') {
            try { t = JSON.parse(t); } catch (e) { t = null; }
        }
        // hinh dang hop le: [so_nguyen, so_nguyen, so_nguyen]
        if (!Array.isArray(t) || t.length !== 3 || t.some(v => !Number.isInteger(v))) {
            weird++;
            console.log('SAO HINH DANG: part id=' + id + ' TYPE=' + type + ' -> ' + JSON.stringify(raw));
            continue;
        }
        const [icon, dx, dy] = t;
        minDx = Math.min(minDx, dx); maxDx = Math.max(maxDx, dx);
        minDy = Math.min(minDy, dy); maxDy = Math.max(maxDy, dy);
        if (dx < -128 || dx > 127 || dy < -128 || dy > 127) {
            bad++;
            console.log('VUOT BYTE: part id=' + id + ' TYPE=' + type + ' icon=' + icon + ' dx=' + dx + ' dy=' + dy);
        }
    }
}
console.log('part rows=' + rows + '  triples=' + triples + '  vuot_byte=' + bad + '  hinh_dang_le=' + weird);
if (triples > 0 && minDy <= maxDy) {
    console.log('dx range: ' + minDx + ' .. ' + maxDx + '   dy range: ' + minDy + ' .. ' + maxDy);
}
process.exit(bad || weird ? 1 : 0);
