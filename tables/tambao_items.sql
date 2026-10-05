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
  `tile_trung_thuong` int(11) NOT NULL DEFAULT 0 COMMENT 'tỉ lệ trúng theo %',
  `des` varchar(255) DEFAULT NULL COMMENT 'ghi chú cho admin',
  `start_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_key_item` (`key_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tambao_items`
--

LOCK TABLES `tambao_items` WRITE;
/*!40000 ALTER TABLE `tambao_items` DISABLE KEYS */;
INSERT INTO `tambao_items` VALUES (20,1874,76,1,'',25,'Vàng - số lượng random 1tr-20tr',NULL,NULL,1),(21,1874,220,1,'',8,'Đá lục bảo',NULL,NULL,1),(22,1874,221,1,'',8,'Đá Saphia',NULL,NULL,1),(23,1874,222,1,'',8,'Đá Ruby',NULL,NULL,1),(24,1874,223,1,'',8,'Đá Titan',NULL,NULL,1),(25,1874,224,1,'',8,'Đá thạch anh tím',NULL,NULL,1),(26,1874,381,1,'',6,'Cuồng nộ 10 phút',NULL,NULL,1),(27,1874,382,1,'',6,'Bổ huyết 10 phút',NULL,NULL,1),(28,1874,383,1,'',6,'Bổ khí 10 phút',NULL,NULL,1),(29,1874,384,1,'',6,'Giáp Xên bọ hung 10 phút',NULL,NULL,1),(30,1874,385,1,'',6,'Ẩn danh 10 phút',NULL,NULL,1),(31,1874,457,1,'',3,'Thỏi vàng',NULL,NULL,1),(32,1874,820,1,'',1,'Vé quay ngọc đen',NULL,NULL,1),(33,1874,821,1,'',1,'Vé quay ngọc vàng',NULL,NULL,1),(34,1779,1432,1,'',20,'Thỏi vàng Khóa',NULL,NULL,1),(35,1779,821,1,'',10,'Vé quay ngọc vàng',NULL,NULL,1),(36,1779,220,2,'',10,'Đá lục bảo x2',NULL,NULL,1),(37,1779,222,2,'',10,'Đá Ruby x2',NULL,NULL,1),(38,1779,224,2,'',10,'Đá thạch anh tím x2',NULL,NULL,1);
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

-- Dump completed on 2026-10-05 13:55:56
