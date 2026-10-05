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
-- Table structure for table `cung_menh_config`
--

DROP TABLE IF EXISTS `cung_menh_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cung_menh_config` (
  `id` tinyint(4) NOT NULL,
  `max_level` int(11) NOT NULL DEFAULT 120,
  `hp_percent` double NOT NULL DEFAULT 0,
  `ki_percent` double NOT NULL DEFAULT 0,
  `dame_percent` double NOT NULL DEFAULT 0,
  `hp_flat` bigint(20) NOT NULL DEFAULT 2000,
  `ki_flat` bigint(20) NOT NULL DEFAULT 2000,
  `dame_flat` bigint(20) NOT NULL DEFAULT 200,
  `manh_base` int(11) NOT NULL DEFAULT 2,
  `manh_step` int(11) NOT NULL DEFAULT 2,
  `manh_extra` int(11) NOT NULL DEFAULT 1,
  `dot_pha_every` int(11) NOT NULL DEFAULT 10,
  `dot_pha_manh_base` int(11) NOT NULL DEFAULT 15,
  `dot_pha_manh_step` int(11) NOT NULL DEFAULT 5,
  `dot_pha_ngoc_base` int(11) NOT NULL DEFAULT 1,
  `dot_pha_ngoc_step` int(11) NOT NULL DEFAULT 1,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cung_menh_config`
--

LOCK TABLES `cung_menh_config` WRITE;
/*!40000 ALTER TABLE `cung_menh_config` DISABLE KEYS */;
INSERT INTO `cung_menh_config` VALUES (1,200,100,100,100,100000,1000000,1000,1,1,1,10,15,5,2,1,'2026-09-24 11:34:40');
/*!40000 ALTER TABLE `cung_menh_config` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 14:24:35
