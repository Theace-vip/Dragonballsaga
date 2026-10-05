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
-- Table structure for table `kho_web`
--

DROP TABLE IF EXISTS `kho_web`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kho_web` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `template_id` int(11) DEFAULT 1238,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kho_web`
--

LOCK TABLES `kho_web` WRITE;
/*!40000 ALTER TABLE `kho_web` DISABLE KEYS */;
INSERT INTO `kho_web` VALUES (25,'admin','🎁 Hộp quà VIP Đền Bù',1,'2025-08-15 23:24:02',1238),(26,'admin','🎁 Hộp quà VIP  Đền Bù',1,'2025-08-17 01:45:50',1238),(27,'ngocquyenn','🎁 Hộp quà VIP  Đền Bù',1,'2025-08-17 08:14:36',1238),(28,'ericvan97','🎁 Hộp quà VIP  Đền Bù',0,'2025-09-05 14:08:53',1238),(29,'ericvan97','🎁 Hộp quà VIP  Đền Bù',0,'2025-09-05 14:16:52',1238),(30,'ericvan95','🎁 Hộp quà VIP  Đền Bù',1,'2025-09-05 17:21:52',1238),(31,'tuandung','🎁 Hộp quà VIP  Đền Bù',1,'2025-09-09 15:47:56',1238),(32,'truongbeo','🎁 Hộp quà VIP  Đền Bù',0,'2025-09-12 11:45:47',1238),(33,'sslsvn123456','🎁 Hộp quà VIP  Đền Bù',0,'2025-09-14 19:08:47',1238),(34,'Chinhneji','🎁 Hộp quà VIP  Đền Bù',1,'2025-09-17 11:47:53',1238),(35,'huyhuy12','🎁 Hộp quà VIP',1,'2025-09-19 01:33:39',1238);
/*!40000 ALTER TABLE `kho_web` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 14:02:58
