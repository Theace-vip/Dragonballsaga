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
-- Table structure for table `giftcode`
--

DROP TABLE IF EXISTS `giftcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `giftcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` text NOT NULL,
  `count_left` int(11) NOT NULL,
  `detail` text NOT NULL,
  `datecreate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `expired` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giftcode`
--

LOCK TABLES `giftcode` WRITE;
/*!40000 ALTER TABLE `giftcode` DISABLE KEYS */;
INSERT INTO `giftcode` VALUES (1,'open7',99917,'[{\"quantity\":\"7\",\"temp_id\":\"14\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"15\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"16\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"17\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"18\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"19\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"20\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]}]','2026-10-11 10:42:20','2029-09-05 17:00:00'),(2,'open6',99924,'[{\"quantity\":\"50\",\"temp_id\":\"1233\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"50\",\"temp_id\":\"1234\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"50\",\"temp_id\":\"1235\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"50\",\"temp_id\":\"1236\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]}]','2026-10-11 10:42:07','2029-09-05 17:00:00'),(3,'open5',99917,'[{\"quantity\":\"2\",\"temp_id\":\"1025\",\"options\":[{\"param\":\"10\",\"id\":\"30\"}]}]','2026-10-11 10:41:58','2027-10-06 17:00:00'),(4,'open4',99918,'[{\"quantity\":\"1000000\",\"temp_id\":\"1825\",\"options\":[{\"param\":\"16\",\"id\":\"30\"}]}]','2026-10-11 10:42:32','2029-07-11 17:00:00'),(5,'open3',99918,'[{\"quantity\":\"9999\",\"temp_id\":\"1881\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"100000\",\"temp_id\":\"1271\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]}]','2026-10-11 10:41:49','2029-07-11 17:00:00'),(6,'open2',99921,'[{\"quantity\":\"1\",\"temp_id\":\"461\",\"options\":[{\"param\":\"999\",\"id\":\"50\"},{\"param\":\"999\",\"id\":\"77\"},{\"param\":\"999\",\"id\":\"103\"},{\"param\":\"50\",\"id\":\"101\"},{\"param\":\"8\",\"id\":\"93\"}]},{\"quantity\":\"1\",\"temp_id\":\"421\",\"options\":[{\"param\":\"999\",\"id\":\"50\"},{\"param\":\"999\",\"id\":\"77\"},{\"param\":\"999\",\"id\":\"103\"},{\"param\":\"10\",\"id\":\"110\"},{\"param\":\"8\",\"id\":\"93\"}]}]','2026-10-11 10:41:46','2029-07-11 17:00:00'),(7,'open1',99922,'[{\"quantity\":\"100\",\"temp_id\":\"1884\",\"options\":[{\"param\":\"100\",\"id\":\"30\"}]}]','2026-10-11 10:41:42','2029-07-11 17:00:00'),(8,'open',99923,'[{\"quantity\":\"50\",\"temp_id\":\"447\",\"options\":[{\"param\":\"100\",\"id\":\"101\"}]}]','2026-10-11 10:41:34','2029-07-11 17:00:00'),(9,'openkura',99914,'[{\"quantity\":\"1000000\",\"temp_id\":\"1174\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"5000\",\"id\":\"47\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1175\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"500000\",\"id\":\"6\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1176\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"5000\",\"id\":\"0\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1177\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"500000\",\"id\":\"7\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1178\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"5\",\"id\":\"14\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]}]','2026-10-11 10:41:24','2029-07-11 17:00:00');
/*!40000 ALTER TABLE `giftcode` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-13 21:08:31
