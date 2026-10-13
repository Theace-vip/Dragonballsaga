// Verify chỉ số 5 set shop trong phuc_loi_tab (MariaDB 10.4 không có JSON_TABLE).
// Dùng: node tools_tmp/check_shop_set.mjs <duong_dan_tsv_hoac_db>
// Đọc TSV 2 cột: id<TAB>list_item (xuất bằng mysql -B -N -e "SELECT id, list_item ..."), in tổng % thật.
import { readFileSync } from 'node:fs';

const sep = '\t';
const text = readFileSync(process.argv[2], 'utf8');
const EXPECT = {
  19: { marker: '66', dame: 80, hp: 60, ki: 60, label: 'SET Thanh Long (luong bac)' },
  30: { marker: '149-152', dame: 340, hp: 255, ki: 255, label: 'SET Hac Am (luong vang)' },
  31: { marker: '149-152', dame: 680, hp: 510, ki: 510, label: 'SET Than Long (luong vang)' },
  18: { marker: '149-152', dame: 880, hp: 660, ki: 660, label: 'SET Hac Am Loan Luu (co 4 la)' },
  28: { marker: '149-152', dame: 1200, hp: 900, ki: 900, label: 'SET Hon Don Vo Cuc (co 4 la)' },
};

let fail = 0;
for (const line of text.split(/\r?\n/)) {
  if (!line.trim()) continue;
  const [idRaw, json] = line.split(sep);
  const id = Number(idRaw);
  const exp = EXPECT[id];
  if (!exp) continue;
  let items;
  try {
    items = JSON.parse(json);
  } catch (e) {
    console.log(`FAIL id=${id}: JSON khong parse duoc -> ${e.message}`);
    fail++;
    continue;
  }
  const sum = { 50: 0, 77: 0, 103: 0 };
  const markers = new Set();
  let empty = 0, lines = 0;
  for (const it of items) {
    if (!it.options || it.options.length === 0) empty++;
    for (const o of it.options || []) {
      lines++;
      if (sum[o.id] !== undefined) sum[o.id] += o.param;
      markers.add(o.id);
    }
    const hasLock = (it.options || []).some((o) => o.id === 30 && o.param === 1);
    if (!hasLock) { console.log(`FAIL id=${id} item ${it.id}: thieu khoa giao dich 30:1`); fail++; }
  }
  const got = { dame: sum[50] / 10, hp: sum[77] / 10, ki: sum[103] / 10 };
  const markerOk = exp.marker === '66' ? markers.has(66) : [149, 150, 151, 152].every((m) => markers.has(m));
  const ok = got.dame === exp.dame && got.hp === exp.hp && got.ki === exp.ki && empty === 0 && markerOk;
  console.log(
    `${ok ? 'OK  ' : 'FAIL'} id=${id} ${exp.label} | ${items.length} mon, ${lines} dong option, mon trong=${empty} | ` +
      `SĐ=${got.dame}% HP=${got.hp}% KI=${got.ki}% (mong doi ${exp.dame}/${exp.hp}/${exp.ki}) | marker ${exp.marker}=${markerOk}`
  );
  if (!ok) fail++;
}
console.log(fail === 0 ? '\n=> TAT CA OK' : `\n=> CO ${fail} DONG SAI`);
process.exit(fail === 0 ? 0 : 1);
