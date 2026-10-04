-- ============================================================
-- PANEL "TAO RUONG (BOX)" - He thong ruong doc luu theo DB
-- Chuong trinh TU TAO 2 bang nay khi panel mo (ChestManager.ensureTables),
-- file nay chi de tham khao / tay tao neu can.
-- ============================================================
-- 1 ruong = 1 item_template MOI (id = max+1, copy TYPE/icon/gender/... tu
-- item mau mac dinh 1413 "Ruong Than Bi 1Sao") + 1 dong trong panel_chest.
-- Noi dung ruong = nhieu dong panel_chest_entry (vat pham + so luong + ty le).
-- Mo ruong trong game: UseItem -> ChestManager.open() -> roll ty le ->
-- nhan vat pham -> tru 1 ruong. Ap dung ngay, khong can restart.

CREATE TABLE IF NOT EXISTS panel_chest (
  item_id INT NOT NULL PRIMARY KEY,          -- = item_template id cua ruong
  name VARCHAR(64) NOT NULL,                 -- ten ruong
  mode TINYINT NOT NULL DEFAULT 0,           -- 0 = chon 1 mon theo trong so; 1 = moi mon roll doc lap theo %
  note VARCHAR(255) NOT NULL DEFAULT '',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS panel_chest_entry (
  id INT AUTO_INCREMENT PRIMARY KEY,
  chest_id INT NOT NULL,                     -- = panel_chest.item_id
  temp_id INT NOT NULL,                      -- vat pham nhan duoc (item_template.id)
  qty INT NOT NULL DEFAULT 1,                -- so luong
  rate DOUBLE NOT NULL DEFAULT 1,            -- mode0 = trong so (khong can tong 100); mode1 = % (0-100)
  KEY idx_chest (chest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- VD tay tao ruong mau (khong can thiet vi panel tao duoc):
-- INSERT INTO panel_chest(item_id, name, mode, note) VALUES (1915, 'Ruong Test', 0, '');
-- INSERT INTO panel_chest_entry(chest_id, temp_id, qty, rate) VALUES
--   (1915, 14, 1, 70),   -- Ngoc rong1 sao, trong so 70
--   (1915, 457, 10, 30); -- Thoi vang, trong so 30
