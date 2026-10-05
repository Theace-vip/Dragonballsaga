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
-- Table structure for table `img_by_name`
--

DROP TABLE IF EXISTS `img_by_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `img_by_name` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(55) NOT NULL,
  `n_frame` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=257 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `img_by_name`
--

LOCK TABLES `img_by_name` WRITE;
/*!40000 ALTER TABLE `img_by_name` DISABLE KEYS */;
INSERT INTO `img_by_name` VALUES (1,'aura_0_0',4),(2,'aura_0_1',4),(3,'aura_1_0',4),(4,'aura_2_0',4),(5,'aura_3_0',5),(6,'aura_4_0',4),(7,'aura_5_0',4),(8,'aura_6_0',4),(9,'aura_6_1',4),(10,'aura_7_0',6),(11,'aura_7_1',4),(12,'aura_8_0',6),(13,'aura_8_1',4),(14,'aura_11_0',6),(15,'aura_13_0',6),(16,'aura_14_0',6),(17,'aura_15_0',6),(18,'aura_17_0',4),(19,'aura_20_0',4),(20,'aura_20_1',4),(21,'aura_21_0',4),(22,'aura_21_1',4),(23,'aura_22_0',4),(24,'aura_22_1',4),(25,'aura_23_1',4),(26,'aura_24_0',4),(27,'aura_24_1',4),(28,'aura_25_0',4),(29,'aura_25_1',4),(30,'aura_26_0',4),(31,'aura_26_1',4),(32,'aura_27_0',4),(33,'aura_27_1',4),(34,'mount_1_1',3),(35,'mount_2_0',3),(36,'mount_2_1',3),(37,'mount_3_1',3),(38,'mount_4_1',3),(39,'mount_5_1',3),(40,'mount_6_1',3),(41,'mount_7_1',3),(42,'mount_8_1',3),(43,'mount_9_0',4),(44,'mount_10_1',3),(45,'mount_11_1',3),(46,'mount_12_1',4),(47,'mount_13_0',4),(48,'mount_13_1',4),(49,'mount_14_1',8),(50,'mount_15_0',4),(51,'mount_15_1',4),(52,'mount_16_1',5),(53,'mount_17_0',4),(54,'mount_17_1',4),(55,'aura_23_0',4),(56,'aura_99_0',4),(57,'aura_98_0',4),(58,'aura_55_0',4),(117,'Skills_24_3_0',7),(118,'Skills_24_3_1',6),(119,'Skills_24_3_2',4),(120,'Skills_24_3_3',4),(121,'Skills_24_3_4',4),(122,'Skills_25_3_0',3),(123,'Skills_25_3_1',3),(124,'Skills_25_3_2',4),(125,'Skills_25_3_3',6),(126,'Skills_25_3_4',2),(127,'Skills_25_3_5',6),(128,'Skills_25_3_6',6),(129,'Skills_24_2_0',7),(130,'Skills_24_2_1',6),(131,'Skills_24_2_2',4),(132,'Skills_24_2_3',4),(133,'Skills_24_2_4',4),(134,'Skills_25_2_0',3),(135,'Skills_25_2_1',3),(136,'Skills_25_2_2',4),(137,'Skills_25_2_3',6),(138,'Skills_25_2_4',2),(139,'Skills_25_2_5',6),(140,'Skills_25_2_6',6),(141,'Skills_24_0_0',7),(142,'Skills_24_0_1',6),(143,'Skills_24_0_2',4),(144,'Skills_24_0_3',4),(145,'Skills_24_0_4',4),(146,'Skills_25_0_0',3),(147,'Skills_25_0_1',3),(148,'Skills_25_0_2',4),(149,'Skills_25_0_3',6),(150,'Skills_25_0_4',2),(151,'Skills_25_0_5',6),(152,'Skills_25_0_6',6),(153,'Skills_26_1_0',3),(154,'Skills_26_1_1',4),(155,'Skills_26_1_2',4),(156,'Skills_26_1_3',6),(157,'Skills_26_1_4',5),(158,'Skills_26_1_5',5),(159,'Skills_26_1_6',5),(160,'Skills_26_1_7',6),(161,'Skills_26_0_0',3),(162,'Skills_26_0_1',4),(163,'Skills_26_0_2',4),(164,'Skills_26_0_3',6),(165,'Skills_26_0_4',5),(166,'Skills_26_0_5',5),(167,'Skills_26_0_6',5),(168,'Skills_26_0_7',6),(169,'Skills_26_3_0',3),(170,'Skills_26_3_1',4),(171,'Skills_26_3_2',4),(172,'Skills_26_3_3',6),(173,'Skills_26_3_6',5),(174,'Skills_26_3_7',6),(175,'aura_36_0',4),(178,'mount_18_0',4),(179,'mount_18_1',4),(180,'mount_19_0',4),(181,'mount_19_1',4),(182,'mount_20_0',2),(183,'mount_20_1',2),(184,'mount_23_0',4),(185,'mount_23_1',4),(186,'mount_24_0',4),(187,'mount_24_1',4),(188,'mount_26_0',4),(189,'mount_26_1',4),(190,'mount_32_0',4),(191,'mount_32_1',4),(192,'aura_38_0',4),(193,'mount_53_0',8),(194,'mount_53_1',8),(195,'mount_54_0',8),(196,'mount_54_1',8),(197,'mount_33_0',4),(198,'mount_33_1',4),(199,'mount_28_0',6),(200,'mount_28_1',6),(201,'mount_29_0',6),(202,'mount_29_1',6),(203,'mount_30_0',6),(204,'mount_30_1',6),(205,'mount_31_0',6),(206,'mount_31_1',6),(207,'aura_34_0',4),(208,'aura_39_0',4),(209,'aura_40_0',4),(210,'set_eff_8_0',2),(211,'set_eff_8_1',6),(212,'mount_37_0',6),(213,'mount_37_1',6),(214,'mount_38_0',4),(215,'mount_38_1',4),(216,'mount_39_0',6),(217,'mount_39_1',6),(218,'mount_40_0',6),(219,'mount_40_1',6),(221,'Skills_26_2_0',3),(222,'Skills_26_2_1',4),(223,'Skills_26_2_2',4),(224,'Skills_26_2_3',6),(225,'Skills_26_2_6',5),(226,'Skills_26_2_7',6),(227,'mount_41_0',2),(228,'mount_41_1',2),(229,'mount_42_0',6),(230,'mount_42_1',6),(231,'aura_18_0',6),(232,'aura_29_0',6),(233,'aura_9_0',6),(234,'aura_10_0',6),(235,'aura_12_0',6),(236,'aura_31_0',6),(237,'mount_55_0',3),(238,'mount_55_1',3),(239,'mount_56_0',4),(240,'mount_56_1',4),(241,'mount_57_0',4),(242,'mount_57_1',4),(243,'mount_58_0',4),(244,'mount_58_1',4),(245,'aura_19_0',10),(246,'aura_32_0',4),(247,'aura_41_0',8),(248,'aura_42_0',8),(249,'mount_62_0',4),(250,'mount_62_1',4),(251,'mount_63_0',4),(252,'mount_63_1',4),(253,'mount_64_0',4),(254,'mount_64_1',4),(255,'mount_65_0',4),(256,'mount_65_1',4);
/*!40000 ALTER TABLE `img_by_name` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:40:50
