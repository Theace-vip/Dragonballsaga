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
-- Table structure for table `moc_vong_quay`
--

DROP TABLE IF EXISTS `moc_vong_quay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moc_vong_quay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL COMMENT 'item_template.id của phần thưởng',
  `quantity` int(11) NOT NULL DEFAULT 1,
  `max_value` int(11) NOT NULL DEFAULT 0 COMMENT 'số điểm quay cần có',
  `item_options` varchar(255) NOT NULL DEFAULT '[]' COMMENT 'JSON option, ví dụ [{"id":30,"param":1}]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moc_vong_quay`
--

LOCK TABLES `moc_vong_quay` WRITE;
/*!40000 ALTER TABLE `moc_vong_quay` DISABLE KEYS */;
INSERT INTO `moc_vong_quay` VALUES (7,457,10000,10,'[]'),(8,1271,10000,50,'[]'),(9,1270,5,100,'[]'),(10,1270,10,200,'[]'),(11,1270,50,500,'[]'),(12,1150,10,600,'[]'),(13,1150,20,700,'[]'),(14,1150,30,800,'[]'),(15,1150,50,900,'[]'),(16,884,1,1000,'[{\"id\":77,\"param\":15000},{\"id\":103,\"param\":15000},{\"id\":50,\"param\":15000}]'),(17,1810,1,1100,'[{\"id\":77,\"param\":15000},{\"id\":103,\"param\":15000},{\"id\":50,\"param\":15000}]'),(18,531,1,1500,'[{\"id\":77,\"param\":15000},{\"id\":103,\"param\":15000},{\"id\":50,\"param\":15000}]'),(19,1753,1,1700,'[{\"id\":77,\"param\":15000},{\"id\":103,\"param\":15000},{\"id\":50,\"param\":15000}]'),(20,1558,1,2000,'[]');
/*!40000 ALTER TABLE `moc_vong_quay` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-10 19:29:23
