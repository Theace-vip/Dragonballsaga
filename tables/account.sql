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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` text DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `update_time` timestamp NULL DEFAULT current_timestamp(),
  `ban` smallint(6) NOT NULL DEFAULT 0,
  `point_post` int(11) NOT NULL DEFAULT 0,
  `last_post` int(11) NOT NULL DEFAULT 0,
  `role` int(11) NOT NULL DEFAULT -1,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `last_time_login` timestamp NOT NULL DEFAULT '2002-07-30 17:00:00',
  `last_time_logout` timestamp NOT NULL DEFAULT '2002-07-30 17:00:00',
  `ip_address` varchar(50) DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 0,
  `thoi_vang` int(11) NOT NULL DEFAULT 0,
  `server_login` int(11) NOT NULL DEFAULT -1,
  `bd_player` double DEFAULT 1,
  `is_gift_box` tinyint(1) DEFAULT 0,
  `gift_time` varchar(255) DEFAULT '0',
  `reward` longtext DEFAULT NULL,
  `vnd` int(225) NOT NULL DEFAULT 0,
  `tongnap` int(225) NOT NULL DEFAULT 0,
  `token` text DEFAULT NULL,
  `xsrf_token` text DEFAULT NULL,
  `newpass` text DEFAULT NULL,
  `luotquay` int(11) NOT NULL DEFAULT 0,
  `vang` bigint(20) NOT NULL DEFAULT 0,
  `event_point` int(11) NOT NULL DEFAULT 0,
  `vip` int(11) NOT NULL DEFAULT 0,
  `admin` int(11) NOT NULL,
  `temp_vnd` int(225) NOT NULL DEFAULT 0,
  `mabaove` int(11) NOT NULL,
  `anh_web` text NOT NULL,
  `gioithieu` int(11) NOT NULL,
  `gmail` text NOT NULL,
  `cash` int(11) NOT NULL DEFAULT 0,
  `danap` int(11) NOT NULL DEFAULT 0,
  `mkc2` int(11) NOT NULL DEFAULT 0,
  `tichdiem` int(11) NOT NULL DEFAULT 0,
  `daptrung_count` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin','pbkdf2-sha256$100000$be7a71d515ec85466435fcbf51c2ecda$528f3ba6b0dfd9746d0b50376b1751ca5055ca233fd1b9cc3305b69219981f4e',NULL,'2025-11-19 09:57:08','2025-11-19 09:57:08',0,0,0,-1,1,'2026-10-13 13:51:20','2026-10-13 13:46:18','171.250.164.159',1,0,-1,1,0,'0',NULL,1378020351,2000000000,'58b73a71d5f64b83b39daafc10e17d3ebe756894cdec96df01d619d0239ccc51',NULL,NULL,0,0,0,0,0,0,0,'',0,'',0,0,0,0,0),(2,'theace','pbkdf2-sha256$100000$a7a5f9094a642dd1c5c40f2768e1d9aa$7028d6a040eeb8a5303b7cbddbf30b8c2a1c60e50073c3419724a8afa8f1b526',NULL,'2026-10-11 10:46:47','2026-10-11 10:46:47',0,0,0,-1,0,'2026-10-11 10:53:36','2026-10-11 10:54:53','171.250.164.159',0,0,-1,1,0,'0',NULL,0,0,NULL,NULL,NULL,0,0,0,0,0,0,0,'',0,'',0,0,0,0,0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-13 21:08:27
