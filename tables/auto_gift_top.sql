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
-- Table structure for table `auto_gift_top`
--

DROP TABLE IF EXISTS `auto_gift_top`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auto_gift_top` (
  `id_top` int(11) NOT NULL DEFAULT 0,
  `name_top` varchar(50) NOT NULL DEFAULT '',
  `date_time` datetime NOT NULL,
  `user_receive` text NOT NULL,
  `is_receive` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auto_gift_top`
--

LOCK TABLES `auto_gift_top` WRITE;
/*!40000 ALTER TABLE `auto_gift_top` DISABLE KEYS */;
INSERT INTO `auto_gift_top` VALUES (0,'Top Săn Vĩ Thú','2025-05-02 18:30:22','[]',1),(1,'Top Săn Boss','2025-05-02 18:30:22','[]',1),(2,'Top Vĩ Thú','2025-05-02 18:30:22','[]',1),(3,'Top Leo Tháp','2025-05-02 18:30:22','[]',1),(4,'Top Sức Mạnh','2025-05-10 11:36:46','[]',1),(5,'Top Nạp','2025-09-20 18:31:02','[]',1),(6,'Top Fam','2025-09-20 18:31:02','[]',1),(7,'Top Phòng Tập','2025-05-21 19:15:08','[]',1),(8,'Top Săn Boss','2025-06-20 13:15:08','[]',1),(9,'Top Săn mabu','2025-05-23 13:15:08','[]',1),(10,'Top Đập Đồ','2025-08-03 01:32:56','[]',1),(11,'Top Rương Thần Bí','2025-09-20 18:31:02','[]',1),(12,'Top Tầm Bảo','2025-09-20 18:31:02','[]',1),(13,'Top Câu Cá','2025-09-20 18:31:02','[]',1);
/*!40000 ALTER TABLE `auto_gift_top` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-13 21:08:28
