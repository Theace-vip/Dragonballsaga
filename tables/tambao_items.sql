-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: hondaodragon
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tambao_items`
--

DROP TABLE IF EXISTS `tambao_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tambao_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_item_id` int(11) NOT NULL DEFAULT 1778 COMMENT 'item_template.id của chìa quay',
  `item_id` int(11) NOT NULL COMMENT 'item_template.id của phần thưởng',
  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT 'số lượng trao cho người chơi',
  `item_options` varchar(255) NOT NULL DEFAULT '' COMMENT 'option compact, ví dụ 30-1,77-50',
  `tile_trung_thuong` decimal(7,2) NOT NULL DEFAULT 0.00,
  `des` varchar(255) DEFAULT NULL COMMENT 'ghi chú cho admin',
  `start_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_key_item` (`key_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tambao_items`
--

LOCK TABLES `tambao_items` WRITE;
/*!40000 ALTER TABLE `tambao_items` DISABLE KEYS */;
INSERT INTO `tambao_items` VALUES (34,1779,1432,1,'',20.00,'Thỏi vàng Khóa',NULL,NULL,1),(35,1779,821,1,'',10.00,'Vé quay ngọc vàng',NULL,NULL,1),(36,1779,220,2,'',10.00,'Đá lục bảo x2',NULL,NULL,1),(37,1779,222,2,'',10.00,'Đá Ruby x2',NULL,NULL,1),(38,1779,224,2,'',10.00,'Đá thạch anh tím x2',NULL,NULL,1),(39,1874,224,100,'',12.00,'Đá thạch anh tím x100',NULL,NULL,1),(40,1874,220,100,'',12.00,'Đá lục bảo x100',NULL,NULL,1),(41,1874,221,100,'',11.00,'Đá Saphia x100',NULL,NULL,1),(42,1874,222,100,'',11.00,'Đá Ruby x100',NULL,NULL,1),(43,1874,223,100,'',10.00,'Đá Titan x100',NULL,NULL,1),(44,1874,457,1000,'',9.00,'Thỏi vàng x1000',NULL,NULL,1),(45,1874,1225,10,'',8.00,'Đá Địa Đạo x10',NULL,NULL,1),(46,1874,1224,1,'',7.00,'Đá Thiên Đạo x1',NULL,NULL,1),(47,1874,1913,2,'',6.00,'Ngọc Tinh Đồ x2',NULL,NULL,1),(48,1874,1588,1,'42-50',5.00,'Sách Ép Premium 50%',NULL,NULL,1),(49,1874,1588,1,'42-60',4.00,'Sách Ép Premium 60%',NULL,NULL,1),(50,1874,1588,1,'42-70',3.00,'Sách Ép Premium 70%',NULL,NULL,1),(51,1874,1588,1,'42-80',1.50,'Sách Ép Premium 80%',NULL,NULL,1),(52,1874,1588,1,'42-100',0.50,'Sách Ép Premium 100%',NULL,NULL,1);
/*!40000 ALTER TABLE `tambao_items` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-10 19:29:29
