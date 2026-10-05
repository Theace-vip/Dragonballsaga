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
-- Table structure for table `worldboss_dame`
--

DROP TABLE IF EXISTS `worldboss_dame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `worldboss_dame` (
  `season_id` varchar(32) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  `player_name` varchar(64) DEFAULT NULL,
  `dame` double DEFAULT 0,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`season_id`,`player_id`),
  KEY `idx_season_dame` (`season_id`,`dame`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worldboss_dame`
--

LOCK TABLES `worldboss_dame` WRITE;
/*!40000 ALTER TABLE `worldboss_dame` DISABLE KEYS */;
INSERT INTO `worldboss_dame` VALUES ('20260924_2200',88,'brojp',9e18,'2026-09-24 15:00:35'),('20260924_2200',90,'kandz',9e18,'2026-09-24 15:04:36'),('20260924_2200',91,'bomthue',9e18,'2026-09-24 15:08:37'),('20260924_2200',98,'atula',9e18,'2026-09-24 15:00:35'),('20260924_2212',88,'brojp',9e18,'2026-09-24 15:12:59'),('20260924_2212',91,'bomthue',9e18,'2026-09-24 15:12:59'),('20260924_2213',91,'bomthue',9e18,'2026-09-24 15:13:59'),('20260924_2213',98,'atula',9e18,'2026-09-24 15:13:59'),('20260924_2214',91,'bomthue',9e18,'2026-09-24 15:15:04'),('20260924_2214',98,'atula',9e18,'2026-09-24 15:15:04'),('20260924_2215',88,'brojp',9e18,'2026-09-24 15:15:51'),('20260924_2215',90,'kandz',9e18,'2026-09-24 15:15:51'),('20260924_2215',91,'bomthue',9e18,'2026-09-24 15:15:51'),('20260924_2215',98,'atula',9e18,'2026-09-24 15:19:52'),('20260924_2306',90,'kandz',9e18,'2026-09-24 16:07:39'),('20260924_2306',91,'bomthue',9e18,'2026-09-24 16:11:03'),('20260925_1253',1,'admin',2.852577792349306e24,'2026-09-25 05:54:39');
/*!40000 ALTER TABLE `worldboss_dame` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 13:16:48
