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
-- Table structure for table `phuc_loi`
--

DROP TABLE IF EXISTS `phuc_loi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phuc_loi` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `max_tab` int(11) NOT NULL DEFAULT 0,
  `id_tab` int(11) NOT NULL DEFAULT 0,
  `info_phucloi` text DEFAULT NULL,
  `action` int(11) NOT NULL DEFAULT 0,
  `tich_luy` varchar(255) DEFAULT NULL,
  `currency_item` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phuc_loi`
--

LOCK TABLES `phuc_loi` WRITE;
/*!40000 ALTER TABLE `phuc_loi` DISABLE KEYS */;
INSERT INTO `phuc_loi` VALUES (0,'Quà Online',2,0,'Online đủ số phút là nhận được quà',1,'phút',0),(1,'Điểm danh tuần',7,1,'Điểm danh đúng ngày trong tuần',2,'ngày',0),(2,'Tích nạp',1,2,'Nạp đủ mốc là nhận được quà',1,'đã nạp',0),(3,'Quà Coin',2,3,'Dùng Coin để đổi quà',1,'Coin',0),(4,'Shop lượng bạc',1,4,'Dùng Coin mua Lượng Bạc',1,'Lượng Bạc',1271),(5,'Shop lượng vàng',1,5,'Dùng Coin mua Lượng Vàng',1,'Lượng Vàng',1270),(6,'Shop thỏi vàng',1,6,'Dùng Coin mua Thỏi Vàng',1,'Thỏi Vàng',457),(7,'Shop ngọc xanh',1,7,'Dùng Coin mua Ngọc Xanh',1,'Ngọc Xanh',-1),(8,'Shop Cỏ 4 lá',1,8,'Dùng Coin mua Cỏ 4 Lá',1,'Cỏ 4 Lá',1150);
/*!40000 ALTER TABLE `phuc_loi` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:40:55
