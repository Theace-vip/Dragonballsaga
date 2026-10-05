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
-- Table structure for table `data_badges`
--

DROP TABLE IF EXISTS `data_badges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_badges` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idEffect` int(11) NOT NULL,
  `idItem` int(11) NOT NULL,
  `NAME` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Options` text NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_badges`
--

LOCK TABLES `data_badges` WRITE;
/*!40000 ALTER TABLE `data_badges` DISABLE KEYS */;
INSERT INTO `data_badges` VALUES (1,218,1289,'Đại gia mới nhú','[{\"param\":15,\"id\":50}]'),(2,219,1290,'Trùm ước rồng','[{\"param\":6,\"id\":77}]'),(3,220,1291,'Trùm săn boss','[{\"param\":5,\"id\":50}]'),(4,221,1292,'Thánh đập đồ +7','[{\"param\":10,\"id\":77},{\"param\":10,\"id\":103}]'),(5,222,1293,'Cao thủ siêu hạng','[{\"param\":8,\"id\":77}]'),(6,223,1294,'Nông dân chăm chỉ','[{\"param\":5,\"id\":77},{\"param\":5,\"id\":103}]'),(7,224,1295,'Ông thần ve chai','[{\"param\":3,\"id\":108}]'),(8,225,1296,'Bị móc sạch túi','[{\"param\":5,\"id\":108},{\"param\":5,\"id\":77},{\"param\":5,\"id\":103}]'),(9,228,1299,'Fan cứng','[{\"param\":3,\"id\":50},{\"param\":3,\"id\":77},{\"param\":3,\"id\":103}]'),(10,242,1392,'Gõ đầu trẻ','[{\"param\":10,\"id\":77},{\"param\":10,\"id\":103},{\"param\":10,\"id\":5}]'),(11,243,1393,'Gõ đầu trẻ','[{\"param\":10,\"id\":77},{\"param\":10,\"id\":103},{\"param\":10,\"id\":5}]'),(12,240,1394,'Gõ đầu trẻ','[{\"param\":10,\"id\":77},{\"param\":10,\"id\":103},{\"param\":10,\"id\":5}]'),(13,247,1457,'X-mas','[{\"param\":12,\"id\":50},{\"param\":12,\"id\":77},{\"param\":12,\"id\":103}]'),(14,253,1514,'Em xinh, em đẹp','[{\"param\":11,\"id\":50},{\"param\":11,\"id\":77},{\"param\":11,\"id\":103},{\"param\":5,\"id\":117}]');
/*!40000 ALTER TABLE `data_badges` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 13:51:18
