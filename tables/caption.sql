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
-- Table structure for table `caption`
--

DROP TABLE IF EXISTS `caption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caption` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `earth` text NOT NULL,
  `saiya` text NOT NULL,
  `namek` text NOT NULL,
  `power` double NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caption`
--

LOCK TABLES `caption` WRITE;
/*!40000 ALTER TABLE `caption` DISABLE KEYS */;
INSERT INTO `caption` VALUES (1,'Phàm Nhân','Phàm Nhân','Phàm Nhân',200000000),(2,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(3,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(4,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(5,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(6,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(7,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(8,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(9,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(10,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(11,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(12,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(13,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(14,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(15,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(16,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(17,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(18,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(19,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(20,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(21,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(22,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(23,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(24,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(25,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308),(26,'Phàm Nhân','Phàm Nhân','Phàm Nhân',1.7976931348623157e308);
/*!40000 ALTER TABLE `caption` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:40:48
