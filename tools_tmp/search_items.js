// Tạm: tìm item_template theo từ khoá (đọc items_dump.tsv UTF-8)
const fs = require('fs');
const lines = fs.readFileSync('items_dump.tsv', 'utf8').split('\n');
const rows = lines.map(l => l.split('\t')).filter(c => c.length >= 2).map(c => ({ id: c[0], name: c[1], type: c[2] || '', gender: c[3] || '', level: c[4] || '' }));
const keys = ["thạch anh", "lục bảo", "saphia", "ruby", "titan", "thỏi", "địa đạo", "thiên đạo", "ngọc tinh", "tinh đồ", "sách", "ép", "cỏ", "lượng", "cải trang", "hit", "chân mệnh", "chân", "tập luyện", "super dragon", "hộp quà", "quà", "đạo", "hồn", "vé"];
for (const k of keys) {
  const hit = rows.filter(r => r.name.toLowerCase().includes(k.toLowerCase()));
  console.log(`=== [${k}] ${hit.length} ===`);
  for (const r of hit.slice(0, 60)) console.log(`${r.id}\t${r.name}\ttype=${r.type}\tgender=${r.gender}\tlv=${r.level}`);
}
