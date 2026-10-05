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
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `Title` varchar(100) DEFAULT 'Nguyen Duc Kien',
  `Description` longtext DEFAULT NULL,
  `Keywords` longtext DEFAULT NULL,
  `SiteKey` varchar(100) DEFAULT NULL,
  `SecretKey` varchar(100) DEFAULT NULL,
  `ServerName` varchar(100) DEFAULT NULL,
  `Fanpage` varchar(100) DEFAULT NULL,
  `Group` varchar(100) DEFAULT NULL,
  `Zalo` varchar(100) DEFAULT NULL,
  `EmailSupport` varchar(50) DEFAULT NULL,
  `AccountBank` varchar(50) DEFAULT NULL,
  `PasswordBank` varchar(50) DEFAULT NULL,
  `NumberBank` int(11) DEFAULT NULL,
  `NameBank` varchar(50) DEFAULT NULL,
  `Android` varchar(50) DEFAULT NULL,
  `Windows` varchar(50) DEFAULT NULL,
  `IPhone` varchar(50) DEFAULT NULL,
  `Java` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES ('Nguyen Duc Kien','Ngọc Rồng Huyền Thoại, Game chiến thuật trên mobile đề tài Dragon ball với nhiều tính năng hấp dẫn, không online vẫn nhận quà và đầy đủ các nhân vật như Songoku, Goku SS4, Vegeta, Android 18, Bulma,....','Dragon ball, game dragon ball, songoku, Goku SS4, vegeta, quy lão tiên sinh, game dragon ball mobile, game chiến thuật','6LdT3ukpAAAAAJ-oX5n5s7fI7QO2Y2qLfyC6HyCE','6LdT3ukpAAAAAAZnJPWwen9UhWRVhV97Cv3zxXY-','NGUYEN DUC KIEN','1',NULL,NULL,NULL,'1','1',1,'1',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 14:24:42
