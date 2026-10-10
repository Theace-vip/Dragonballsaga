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
-- Table structure for table `panel_audit`
--

DROP TABLE IF EXISTS `panel_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `panel_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actor` varchar(64) NOT NULL DEFAULT 'panel',
  `action` varchar(64) NOT NULL,
  `detail` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `panel_audit`
--

LOCK TABLES `panel_audit` WRITE;
/*!40000 ALTER TABLE `panel_audit` DISABLE KEYS */;
INSERT INTO `panel_audit` VALUES (1,'panel','nap_rate','rate=x3 enabled=1 note=test tu dong','2026-10-09 18:25:11'),(2,'panel','nap_rate','rate=x50 enabled=1 note=thu tran','2026-10-09 18:25:12'),(3,'panel','nap_rate','rate=x1 enabled=0','2026-10-09 18:25:12'),(4,'panel','nap_rate','rate=x3 enabled=1 note=test tu dong','2026-10-09 18:26:23'),(5,'panel','nap_rate','rate=x50 enabled=1 note=thu tran','2026-10-09 18:26:23'),(6,'panel','nap_rate','rate=x1 enabled=0','2026-10-09 18:26:23'),(7,'panel','nap_rate','rate=x10 enabled=1 until=2026-10-11','2026-10-09 18:47:15'),(8,'panel','nap_rate','rate=x1 enabled=0','2026-10-09 18:48:19'),(9,'panel','nap_rate','rate=x1 enabled=1','2026-10-09 18:48:24'),(10,'panel','createGiftcode','test001 count=100 hetHan=30ngay','2026-10-09 21:02:36'),(11,'panel','createGiftcode','test0001 count=100 hetHan=30ngay','2026-10-09 21:04:36');
/*!40000 ALTER TABLE `panel_audit` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-10 19:29:24
