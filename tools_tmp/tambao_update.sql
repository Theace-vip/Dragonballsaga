-- Cập nhật Vòng quay Tầm Bảo: 14 ô mới + 14 mốc mới + cột reset cho nhân vật
-- Chạy 1 lần: mysql --default-character-set=utf8mb4 hondaodragon < tambao_update.sql

-- 1. Cột đếm số lần reset vòng quay (mốc 2000)
ALTER TABLE player ADD COLUMN IF NOT EXISTS reset_vong_quay INT NOT NULL DEFAULT 0;

-- 2. Tỉ lệ cho phép thập phân (0.5%, 1.5%)
ALTER TABLE tambao_items MODIFY COLUMN tile_trung_thuong DECIMAL(7,2) NOT NULL DEFAULT 0;

-- 3. Xoá trạng thái vòng quay cũ của toàn bộ nhân vật (bộ mốc đổi hoàn toàn)
UPDATE player SET diem_quay = 0, active_vong_quay = '[]', reset_vong_quay = 0;

-- 4. Bộ 14 ô mới cho pool 1874 (Key vàng) - tổng tỉ lệ đúng 100%
DELETE FROM tambao_items WHERE key_item_id = 1874;
INSERT INTO tambao_items(key_item_id,item_id,quantity,item_options,tile_trung_thuong,des,enabled) VALUES
(1874,224,100,'',12.00,'Đá thạch anh tím x100',1),
(1874,220,100,'',12.00,'Đá lục bảo x100',1),
(1874,221,100,'',11.00,'Đá Saphia x100',1),
(1874,222,100,'',11.00,'Đá Ruby x100',1),
(1874,223,100,'',10.00,'Đá Titan x100',1),
(1874,457,1000,'',9.00,'Thỏi vàng x1000',1),
(1874,1225,10,'',8.00,'Đá Địa Đạo x10',1),
(1874,1224,1,'',7.00,'Đá Thiên Đạo x1',1),
(1874,1913,2,'',6.00,'Ngọc Tinh Đồ x2',1),
(1874,1588,1,'42-50',5.00,'Sách Ép Premium 50%',1),
(1874,1588,1,'42-60',4.00,'Sách Ép Premium 60%',1),
(1874,1588,1,'42-70',3.00,'Sách Ép Premium 70%',1),
(1874,1588,1,'42-80',1.50,'Sách Ép Premium 80%',1),
(1874,1588,1,'42-100',0.50,'Sách Ép Premium 100%',1);

-- 5. Bộ 14 mốc mới (quà 1000-1700 kèm option 77=HP%, 103=KI%, 50=Sức đánh% - reset +2000%/lần)
DELETE FROM moc_vong_quay;
INSERT INTO moc_vong_quay(item_id,quantity,max_value,item_options) VALUES
(457,10000,10,'[]'),
(1271,10000,50,'[]'),
(1270,5,100,'[]'),
(1270,10,200,'[]'),
(1270,50,500,'[]'),
(1150,10,600,'[]'),
(1150,20,700,'[]'),
(1150,30,800,'[]'),
(1150,50,900,'[]'),
(884,1,1000,'[{"id":77,"param":15000},{"id":103,"param":15000},{"id":50,"param":15000}]'),
(1810,1,1100,'[{"id":77,"param":15000},{"id":103,"param":15000},{"id":50,"param":15000}]'),
(531,1,1500,'[{"id":77,"param":15000},{"id":103,"param":15000},{"id":50,"param":15000}]'),
(1753,1,1700,'[{"id":77,"param":15000},{"id":103,"param":15000},{"id":50,"param":15000}]'),
(1558,1,2000,'[]');
