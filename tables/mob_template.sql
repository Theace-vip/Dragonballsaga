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
-- Table structure for table `mob_template`
--

DROP TABLE IF EXISTS `mob_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mob_template` (
  `id` int(11) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `hp` double NOT NULL,
  `range_move` smallint(6) NOT NULL,
  `speed` smallint(6) NOT NULL,
  `dart_type` smallint(6) NOT NULL,
  `percent_dame` smallint(6) NOT NULL DEFAULT 5,
  `percent_tiem_nang` smallint(6) NOT NULL DEFAULT 50,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mob_template`
--

LOCK TABLES `mob_template` WRITE;
/*!40000 ALTER TABLE `mob_template` DISABLE KEYS */;
INSERT INTO `mob_template` VALUES (0,0,'Mộc nhân',20,0,1,25,5,10),(1,1,'Khủng long',200,33,1,25,5,10),(2,1,'Lợn lòi',200,33,1,9,5,10),(3,1,'Quỷ đất',200,33,1,5,5,10),(4,1,'Khủng long mẹ',500,33,2,26,5,10),(5,1,'Lợn lòi mẹ',500,33,1,12,5,10),(6,1,'Quỷ đất mẹ',500,33,2,27,5,10),(7,4,'Thằn lằn bay',600,33,1,26,5,10),(8,4,'Phi long',600,33,2,12,5,10),(9,4,'Quỷ bay',600,33,1,27,5,10),(10,4,'Thằn lằn mẹ',1000,33,2,28,5,10),(11,4,'Phi long mẹ',1000,33,1,28,5,10),(12,4,'Quỷ bay mẹ',1000,33,2,28,5,10),(13,1,'Ốc mượn hồn',3000,33,2,22,5,10),(14,1,'Ốc sên',3000,33,2,22,5,10),(15,1,'Heo Xayda mẹ',3000,33,2,22,5,10),(16,1,'Heo rừng',1500,33,1,13,5,10),(17,1,'Heo da xanh',1500,33,1,13,5,10),(18,1,'Heo Xayda',1500,33,1,13,5,10),(19,1,'Heo rừng mẹ',12000,33,2,16,5,10),(20,1,'Heo xanh mẹ',12000,33,2,16,5,10),(21,4,'Alien',12000,33,2,41,5,10),(22,1,'Bulon',6000,33,2,30,5,10),(23,1,'Ukulele',6000,33,2,23,5,10),(24,1,'Quỷ mập',6000,33,2,17,5,10),(25,4,'Tambourine',20000,33,2,34,5,10),(26,1,'Drum',20000,33,2,24,5,10),(27,1,'Akkuman',20000,33,2,29,5,10),(28,4,'Thằn lằn bay 2',1500,33,2,19,5,10),(29,4,'Phi long 2',1500,33,2,7,5,10),(30,4,'Quỷ bay 2',1500,33,2,20,5,10),(31,4,'Không tặc',3000,33,2,40,5,10),(32,4,'Quỷ đầu to',3000,33,2,15,5,10),(33,4,'Quỷ địa ngục',3000,33,2,17,5,10),(34,1,'Lính độc nhãn',30000,33,2,13,5,10),(35,1,'Lính độc nhãn',30000,33,2,13,5,10),(36,1,'Sói xám',30000,33,2,13,5,10),(37,4,'Robot bay',30000,33,2,13,5,10),(38,1,'Robot thép',30000,33,2,13,5,10),(39,1,'Nappa',40000,33,2,10,5,10),(40,1,'Soldier',50000,33,2,10,5,10),(41,1,'Appule',60000,33,2,42,5,10),(42,1,'Raspberry',70000,33,2,42,5,10),(43,4,'Thằn lằn xanh',80000,33,2,43,5,10),(44,1,'Quỷ đầu nhọn',90000,33,2,43,5,10),(45,1,'Quỷ đầu vàng',100000,33,2,44,5,10),(46,1,'Quỷ da tím',110000,33,2,44,5,10),(47,1,'Quỷ già',120000,33,2,45,5,10),(48,1,'Cá sấu',130000,33,2,45,5,10),(49,4,'Dơi da xanh',140000,33,2,46,5,10),(50,4,'Quỷ chim',180000,33,2,48,5,10),(51,1,'Lính đầu trọc',150000,33,1,46,5,10),(52,1,'Lính tai dài',160000,33,2,47,5,10),(53,1,'Lính vũ trụ',170000,33,2,47,5,10),(54,1,'Khỉ lông đen',300000,33,2,47,5,10),(55,1,'Khỉ giáp sắt',350000,33,2,47,5,10),(56,1,'Khỉ lông đỏ',400000,33,2,47,5,10),(57,1,'Khỉ lông vàng',450000,33,2,47,5,10),(58,1,'Xên con cấp 1',200000,33,3,47,5,10),(59,1,'Xên con cấp 2',250000,33,3,47,5,10),(60,1,'Xên con cấp 3',300000,33,3,47,5,10),(61,1,'Xên con cấp  4',350000,33,3,47,5,10),(62,1,'Xên con cấp  5',400000,33,3,47,5,10),(63,1,'Xên con cấp  6',450000,33,3,47,5,10),(64,1,'Xên con cấp  7',500000,33,3,47,5,10),(65,1,'Xên con cấp  8',550000,33,3,47,5,10),(66,1,'Tai tím',350000,33,2,45,5,10),(67,1,'Abo',400000,33,2,10,5,10),(68,1,'Kado',450000,33,2,46,5,10),(69,4,'Da xanh',500000,33,2,43,5,10),(70,1,'Hirudegarn',40000000,33,1,43,5,10),(71,1,'Vua Bạch Tuộc',1500000,33,1,43,5,10),(72,1,'Rôbốt bảo vệ',1000000,33,1,43,5,10),(73,1,'Kawazu',50000,33,2,30,5,10),(74,1,'Kinkarn',55000,33,2,30,5,10),(75,4,'Arbee',60000,33,2,30,5,10),(76,0,'Cỗ máy hủy diệt',80000,0,1,25,5,10),(77,1,'Gấu tướng cướp',2000000000,33,2,43,5,10),(78,1,'Khỉ lông xanh',2000000,33,2,47,5,10),(79,4,'Taburine Đỏ',3000000,33,3,34,5,10),(80,1,'Cabira',4000000,33,2,10,5,10),(81,1,'Tobi',5000000,33,2,47,5,10),(82,1,'Voi Chín Ngà',20000000,33,2,43,5,10),(83,1,'Gà Chín Cựa',12000000,33,2,43,5,10),(84,1,'Ngựa Chín Lmao',15000000,33,2,43,5,10),(85,1,'Piano',2000000000,33,1,43,5,10),(86,1,'Ếch mặt đỏ',4000000,33,2,62,5,10),(87,4,'Jinai',6000000,33,2,66,5,10),(88,1,'Quỷ đỏ',1000000,33,2,66,5,10),(89,1,'Quỷ xanh',1500000,33,2,62,5,10),(90,1,'Quỷ xanh lá',1000000,33,2,66,5,10),(91,1,'Quỷ vàng',1500000,33,2,62,5,10),(92,1,'',1,33,2,5,5,10),(93,1,'',1,33,2,5,5,10),(94,1,'Toppo',4000000,33,1,47,5,10),(95,1,'Thỏ con',4000000,33,1,47,5,10),(96,1,'Janemba',4000000,33,1,47,5,10),(97,1,'MEZ',4000000,33,1,47,5,10),(98,1,'GOZ',4000000,33,1,47,5,10),(99,1,'Đá đỏ',4000000,33,1,47,5,10),(100,1,'Đá vàng',4000000,33,1,47,5,10),(101,1,'Đá xanh',4000000,33,1,47,5,10),(102,1,'Thây ma',4000000,33,1,47,5,10),(103,0,'Bù nhìn ma quái',20,0,1,25,5,10),(104,4,'Phù thủy',4000000,33,1,47,5,10),(105,1,'Frostbite',500,33,2,30,5,10),(106,1,'Snowy Tangerine',550,33,2,30,5,10),(107,1,'Deinonychus',550,33,2,30,5,10),(108,1,'Snake',550,33,2,66,5,10),(109,1,'Blizzard bird',550,33,2,66,5,10),(110,1,'Snowman',55,33,2,30,5,10),(111,1,'Yeti',11,33,2,30,5,10),(112,1,'Grim Reaper',11,33,2,22,5,10),(113,1,'Demon 3',11,33,2,30,5,10),(114,1,'Golem',11,50,2,11,5,10),(115,1,'Dazing Stone',11,50,2,11,5,10),(116,1,'Demon 2',11,50,2,11,5,10);
/*!40000 ALTER TABLE `mob_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 17:46:49
