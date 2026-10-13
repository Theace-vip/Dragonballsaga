-- ============================================================
--  SET ĐỒ BÁN TRONG SHOP (Phúc Lợi tab 4/5/8) — thêm chỉ số
--  File mới 2026-10-13. Chạy trên DB hondaodragon:
--
--    cd /c/xampp/mysql/bin
--    ./mysql.exe --default-character-set=utf8mb4 -u root hondaodragon < <repo>/BeMeoGaming/sql/shop_set_option.sql
--
--  BẮT BUỘC có --default-character-set=utf8mb4 (không có cờ này, tiếng Việt
--  trong file bị ghi sai mã hoá, xem sự cố 13/10 ở BAO_CAO_TRIEN_KHAI_PHUC_LOI.md mục 8).
--
--  Phạm vi: CHỈ UPDATE cột list_item của 5 mốc shop:
--     id 19  (tab 4) SET Thanh Long        — 100.000 Lượng Bạc
--     id 30  (tab 5) SET Hắc Ám            — 100.000 Lượng Vàng
--     id 31  (tab 5) SET Thần Long         —  10.000 Lượng Vàng
--     id 18  (tab 8) SET Hắc Ám Loạn Lưu   —   5.000 Cỏ 4 Lá
--     id 28  (tab 8) SET Hỗn Độn Vô Cực    —  10.000 Cỏ 4 Lá
--  KHÔNG tạo item/option mới, không đụng bảng khác
--  => không cần build lại jar, không cần restart server.
--  Sau khi chạy: Panel admin -> Reload Phúc Lợi (PhucLoi.reload() đọc lại DB).
--  Người chơi chỉ cần mở lại bảng Phúc Lợi (option được gửi khi mở bảng).
--
--  BẬC THANG ĐÃ CHỐT (option 50/77/103: param = % thật × 10, giữ hệ số 10)
--  ------------------------------------------------------------------
--  Set                      Nơi bán            param  %/món  Tổng 5/5 (SĐ/HP/KI)
--  Thanh Long               Lượng Bạc           200    20%    80% / 60% / 60%
--  Hắc Ám                   Lượng Vàng          850    85%   340% / 255% / 255%
--  Thần Long                Lượng Vàng         1700   170%   680% / 510% / 510%
--  Hắc Ám Loạn Lưu          Cỏ 4 Lá (5.000)    2200   220%   880% / 660% / 660%
--  Hỗn Độn Vô Cực           Cỏ 4 Lá (10.000)   3000   300%  1200% / 900% / 900%
--
--  Tham chiếu mốc nạp đang chạy (phuc_loi_tab id 53/90/92/93):
--  T1 Hắc Ám 25%/món (100/75/75)  T2 Nguyên Thủy 50%  T3 Tư Tượng 90%  T4 Ngũ Hành 150%
--  => Thanh Long (20%) thấp hơn cả T1; Hắc Ám shop (85%) "ít hơn 1 xíu" T3 (90%);
--     Thần Long (170%) trên T4 (150%) = set mạnh nhất shop; 2 set Cỏ 4 Lá trên tất cả.
--
--  Marker 5/5 (cần đủ 5/5 món mới kích hoạt):
--    Thanh Long                 -> option 66  (Full Sét Dragon, +30% SĐ) như T1/T2
--    Hắc Ám / Thần Long / 2 set Cỏ -> option 149-152 (nhóm Tư Tượng trong set_config:
--                                    +500% KI, +500% HP, +1000%+3×200% SĐ, +500% Giáp)
--    Lý do dùng 149-152 cho cả 3 set cao: 2 set Cỏ 4 Lá phải "vượt trội hơn toàn bộ",
--    mà nhóm 149-152 là nhóm bonus mạnh nhất còn slot (option đã chạm trần 255).
--  Không dùng marker cho set thấp (Thanh Long) vì sẽ thành mạnh nhất bảng.
-- ============================================================

-- ---------- 1) TAB 4 — SHOP LƯỢNG BẠC: SET Thanh Long (1679-1683) ----------
UPDATE phuc_loi_tab SET list_item = '[{"id":1679,"quantity":1,"options":[{"id":50,"param":200},{"id":77,"param":200},{"id":94,"param":20},{"id":47,"param":8000},{"id":66,"param":0},{"id":30,"param":1}]},{"id":1680,"quantity":1,"options":[{"id":77,"param":200},{"id":103,"param":200},{"id":6,"param":200000},{"id":8,"param":4},{"id":66,"param":0},{"id":30,"param":1}]},{"id":1681,"quantity":1,"options":[{"id":50,"param":200},{"id":103,"param":200},{"id":0,"param":10000},{"id":14,"param":2},{"id":66,"param":0},{"id":30,"param":1}]},{"id":1682,"quantity":1,"options":[{"id":50,"param":200},{"id":77,"param":200},{"id":18,"param":15},{"id":16,"param":15},{"id":66,"param":0},{"id":30,"param":1}]},{"id":1683,"quantity":1,"options":[{"id":103,"param":200},{"id":50,"param":200},{"id":5,"param":8},{"id":19,"param":15},{"id":66,"param":0},{"id":30,"param":1}]}]'
 WHERE id = 19 AND tab_id = 4 AND list_item LIKE '%"options":[]%';

-- ---------- 2) TAB 5 — SHOP LƯỢNG VÀNG: SET Hắc Ám (1174-1178) ----------
UPDATE phuc_loi_tab SET list_item = '[{"id":1174,"quantity":1,"options":[{"id":50,"param":850},{"id":77,"param":850},{"id":94,"param":90},{"id":95,"param":15},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1175,"quantity":1,"options":[{"id":77,"param":850},{"id":103,"param":850},{"id":173,"param":15},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1176,"quantity":1,"options":[{"id":50,"param":850},{"id":103,"param":850},{"id":5,"param":30},{"id":14,"param":8},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1177,"quantity":1,"options":[{"id":50,"param":850},{"id":77,"param":850},{"id":108,"param":15},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1178,"quantity":1,"options":[{"id":103,"param":850},{"id":50,"param":850},{"id":97,"param":25},{"id":105,"param":1},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]}]'
 WHERE id = 30 AND tab_id = 5 AND list_item LIKE '%"options":[]%';

-- ---------- 3) TAB 5 — SHOP LƯỢNG VÀNG: SET Thần Long (1868-1872) ----------
UPDATE phuc_loi_tab SET list_item = '[{"id":1868,"quantity":1,"options":[{"id":50,"param":1700},{"id":77,"param":1700},{"id":94,"param":170},{"id":95,"param":35},{"id":117,"param":45},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1869,"quantity":1,"options":[{"id":77,"param":1700},{"id":103,"param":1700},{"id":96,"param":35},{"id":117,"param":45},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1870,"quantity":1,"options":[{"id":50,"param":1700},{"id":103,"param":1700},{"id":5,"param":70},{"id":14,"param":16},{"id":117,"param":45},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1871,"quantity":1,"options":[{"id":50,"param":1700},{"id":77,"param":1700},{"id":97,"param":60},{"id":117,"param":45},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1872,"quantity":1,"options":[{"id":103,"param":1700},{"id":50,"param":1700},{"id":105,"param":1},{"id":98,"param":35},{"id":117,"param":45},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]}]'
 WHERE id = 31 AND tab_id = 5 AND list_item LIKE '%"options":[]%';

-- ---------- 4) TAB 8 — SHOP CỎ 4 LÁ: SET Hắc Ám Loạn Lưu (1919-1923) ----------
UPDATE phuc_loi_tab SET list_item = '[{"id":1919,"quantity":1,"options":[{"id":50,"param":2200},{"id":77,"param":2200},{"id":94,"param":220},{"id":95,"param":45},{"id":117,"param":60},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1920,"quantity":1,"options":[{"id":77,"param":2200},{"id":103,"param":2200},{"id":173,"param":45},{"id":117,"param":60},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1921,"quantity":1,"options":[{"id":50,"param":2200},{"id":103,"param":2200},{"id":5,"param":80},{"id":14,"param":20},{"id":117,"param":60},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1922,"quantity":1,"options":[{"id":50,"param":2200},{"id":77,"param":2200},{"id":108,"param":40},{"id":117,"param":60},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1923,"quantity":1,"options":[{"id":103,"param":2200},{"id":50,"param":2200},{"id":97,"param":60},{"id":105,"param":1},{"id":117,"param":60},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]}]'
 WHERE id = 18 AND tab_id = 8 AND list_item LIKE '%"options":[]%';

-- ---------- 5) TAB 8 — SHOP CỎ 4 LÁ: SET Hỗn Độn Vô Cực (1924-1928) ----------
UPDATE phuc_loi_tab SET list_item = '[{"id":1924,"quantity":1,"options":[{"id":50,"param":3000},{"id":77,"param":3000},{"id":94,"param":300},{"id":95,"param":60},{"id":117,"param":80},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1925,"quantity":1,"options":[{"id":77,"param":3000},{"id":103,"param":3000},{"id":96,"param":60},{"id":117,"param":80},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1926,"quantity":1,"options":[{"id":50,"param":3000},{"id":103,"param":3000},{"id":5,"param":120},{"id":14,"param":30},{"id":117,"param":80},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1927,"quantity":1,"options":[{"id":50,"param":3000},{"id":77,"param":3000},{"id":97,"param":100},{"id":117,"param":80},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]},{"id":1928,"quantity":1,"options":[{"id":103,"param":3000},{"id":50,"param":3000},{"id":105,"param":1},{"id":98,"param":60},{"id":117,"param":80},{"id":149,"param":0},{"id":150,"param":0},{"id":151,"param":0},{"id":152,"param":0},{"id":30,"param":1}]}]'
 WHERE id = 28 AND tab_id = 8 AND list_item LIKE '%"options":[]%';

-- ---------- 6) KIỂM TRA ----------
--  MariaDB 10.4 KHÔNG có JSON_TABLE (chỉ từ 10.6) -> ở đây chỉ kiểm được JSON hợp lệ,
--  số món và số món đã có option. Tổng % từng option (bậc thang) verify bằng script
--  node đọc thẳng JSON từ DB: tools_tmp/check_shop_set.mjs
SELECT id, tab_id, JSON_VALID(list_item) AS json_ok,
       JSON_LENGTH(list_item) AS so_mon,
       (CHAR_LENGTH(list_item) - CHAR_LENGTH(REPLACE(list_item, '"options":[]', ''))) / 12 AS so_mon_trong
  FROM phuc_loi_tab WHERE id IN (18, 19, 28, 30, 31) ORDER BY id;
