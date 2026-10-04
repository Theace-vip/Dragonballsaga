-- =====================================================================
-- SU KIEN TRUNG THU - VAT PHAM MOI: Ruong Trung Thu (ID 1914)
-- Model theo Hop Trung Thu (1512), dung lai anh icon cua 1512 (icon_id = 11717)
--
-- Chay 1 lan tren DB hondaodragon. KHONG sua hondaodragon.sql.
-- =====================================================================

INSERT INTO `item_template`
    (`id`, `TYPE`, `gender`, `NAME`, `description`, `level`, `icon_id`, `part`,
     `is_up_to_up`, `power_require`, `gold`, `gem`, `head`, `body`, `leg`,
     `is_up_to_up_over_99`, `can_trade`, `comment`, `ruby`)
VALUES
    (1914, 27, 3,
     'Ruong Trung Thu',
     'Su kien Trung Thu - Mo ruong nhan qua su kien, ty le re cai trang 5%',
     0, 11717, 0, 1, 0, 0, 0, -1, -1, -1, 1, 0, NULL, 0);
