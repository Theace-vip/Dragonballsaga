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
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giftcode`
--

LOCK TABLES `giftcode` WRITE;
/*!40000 ALTER TABLE `giftcode` DISABLE KEYS */;
INSERT INTO `giftcode` VALUES (1,'test4',99917,'[{\"quantity\":\"7\",\"temp_id\":\"14\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"9999999\",\"temp_id\":\"15\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"16\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"17\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"18\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"19\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"7\",\"temp_id\":\"20\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]}]','2026-10-04 15:39:14','2029-09-05 17:00:00'),(2,'test5',99924,'[{\"quantity\":\"50\",\"temp_id\":\"1233\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"50\",\"temp_id\":\"1234\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"50\",\"temp_id\":\"1235\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"50\",\"temp_id\":\"1236\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]}]','2026-10-04 15:39:20','2029-09-05 17:00:00'),(3,'test6',99917,'[{\"quantity\":\"2\",\"temp_id\":\"1025\",\"options\":[{\"param\":\"10\",\"id\":\"30\"}]}]','2026-10-04 15:43:43','2027-10-06 17:00:00'),(4,'test7',99918,'[{\"quantity\":\"100000\",\"temp_id\":\"1825\",\"options\":[{\"param\":\"16\",\"id\":\"30\"}]}]','2026-10-04 15:43:47','2029-07-11 17:00:00'),(5,'test8',99918,'[{\"quantity\":\"9999\",\"temp_id\":\"1881\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]},{\"quantity\":\"100000\",\"temp_id\":\"1271\",\"options\":[{\"param\":\"0\",\"id\":\"30\"}]}]','2026-10-04 15:43:52','2029-07-11 17:00:00'),(6,'test9',99921,'[{\"quantity\":\"1\",\"temp_id\":\"461\",\"options\":[{\"param\":\"999\",\"id\":\"50\"},{\"param\":\"999\",\"id\":\"77\"},{\"param\":\"999\",\"id\":\"103\"},{\"param\":\"50\",\"id\":\"101\"},{\"param\":\"8\",\"id\":\"93\"}]},{\"quantity\":\"1\",\"temp_id\":\"421\",\"options\":[{\"param\":\"999\",\"id\":\"50\"},{\"param\":\"999\",\"id\":\"77\"},{\"param\":\"999\",\"id\":\"103\"},{\"param\":\"10\",\"id\":\"110\"},{\"param\":\"8\",\"id\":\"93\"}]}]','2026-10-04 15:43:56','2029-07-11 17:00:00'),(7,'tsest10',99922,'[{\"quantity\":\"100\",\"temp_id\":\"1884\",\"options\":[{\"param\":\"100\",\"id\":\"30\"}]}]','2026-10-04 15:45:01','2029-07-11 17:00:00'),(8,'test11',99923,'[{\"quantity\":\"50\",\"temp_id\":\"447\",\"options\":[{\"param\":\"100\",\"id\":\"101\"}]}]','2026-10-03 15:49:26','2029-07-11 17:00:00'),(9,'test12',99914,'[{\"quantity\":\"1000000\",\"temp_id\":\"1174\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"5000\",\"id\":\"47\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1175\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"500000\",\"id\":\"6\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1176\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"5000\",\"id\":\"0\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1177\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"500000\",\"id\":\"7\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]},{\"quantity\":\"1\",\"temp_id\":\"1178\",\"options\":[{\"param\":\"0\",\"id\":\"158\"},{\"param\":\"5\",\"id\":\"14\"},{\"param\":\"0\",\"id\":\"75\"},{\"param\":\"0\",\"id\":\"55\"}]}]','2026-10-04 15:44:16','2029-07-11 17:00:00'),(32,'test2',54,'[{\"quantity\":\"100000\",\"temp_id\":\"1825\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"628\",\"options\":[{\"param\":\"500000\",\"id\":\"0\"},{\"param\":\"10\",\"id\":\"14\"},{\"param\":\"500000\",\"id\":\"19\"},{\"param\":\"0\",\"id\":\"117\"},{\"param\":\"0\",\"id\":\"0\"}]}]','2026-10-04 15:38:25','2026-10-22 12:44:16'),(33,'cungmenh',55,'[{\"quantity\":\"100000\",\"temp_id\":\"1912\",\"options\":[]},{\"quantity\":\"10000\",\"temp_id\":\"1913\",\"options\":[]}]','2026-10-04 15:38:05','2026-10-23 15:44:08'),(35,'test',64,'[{\"quantity\":\"10000\",\"temp_id\":\"1270\",\"options\":[]},{\"quantity\":\"10000\",\"temp_id\":\"1271\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"441\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"442\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"443\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"444\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"445\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"446\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"447\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"964\",\"options\":[]},{\"quantity\":\"100\",\"temp_id\":\"965\",\"options\":[]}]','2026-10-04 15:38:17','2026-10-24 11:46:04'),(36,'test1',56,'[{\"quantity\":\"1\",\"temp_id\":\"1818\",\"options\":[{\"param\":\"10000\",\"id\":\"5\"},{\"param\":\"10000\",\"id\":\"117\"}]}]','2026-10-03 15:48:20','2026-10-24 11:48:16'),(37,'test3',55,'[{\"quantity\":\"1\",\"temp_id\":\"1748\",\"options\":[{\"param\":\"10000\",\"id\":\"5\"},{\"param\":\"10000\",\"id\":\"77\"},{\"param\":\"10\",\"id\":\"78\"}]},{\"quantity\":\"1\",\"temp_id\":\"1211\",\"options\":[{\"param\":\"10000\",\"id\":\"5\"}]},{\"quantity\":\"1\",\"temp_id\":\"1171\",\"options\":[{\"param\":\"10000\",\"id\":\"5\"},{\"param\":\"100\",\"id\":\"14\"},{\"param\":\"1000000\",\"id\":\"101\"}]},{\"quantity\":\"1\",\"temp_id\":\"966\",\"options\":[{\"param\":\"10000\",\"id\":\"5\"},{\"param\":\"10000\",\"id\":\"101\"},{\"param\":\"10000\",\"id\":\"117\"},{\"param\":\"10000\",\"id\":\"147\"}]},{\"quantity\":\"1\",\"temp_id\":\"1474\",\"options\":[{\"param\":\"10000\",\"id\":\"5\"},{\"param\":\"10000\",\"id\":\"103\"},{\"param\":\"1\",\"id\":\"116\"},{\"param\":\"10000\",\"id\":\"163\"}]}]','2026-10-04 15:39:08','2026-10-24 12:41:26'),(38,'test13',67,'[{\"quantity\":\"9999999\",\"temp_id\":\"1270\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1271\",\"options\":[]}]','2026-10-03 15:49:39','2026-10-24 13:19:42'),(42,'test14',62,'[{\"quantity\":\"1000000\",\"temp_id\":\"1271\",\"options\":[]}]','2026-10-04 15:44:23','2026-10-24 13:49:13'),(43,'test20',61,'[{\"quantity\":\"10000000\",\"temp_id\":\"1271\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1406\",\"options\":[]}]','2026-10-04 15:44:36','2026-10-24 13:50:21'),(44,'test21',60,'[{\"quantity\":\"1\",\"temp_id\":\"454\",\"options\":[{\"param\":\"10000000\",\"id\":\"5\"}]}]','2026-10-04 15:45:27','2026-10-24 14:25:54'),(46,'truonggiang',0,'[{\"quantity\":\"99999999\",\"temp_id\":\"1270\",\"options\":[]}]','2026-09-25 19:22:28','2026-09-26 19:17:11'),(47,'truonggiang1',0,'[{\"quantity\":\"10000000\",\"temp_id\":\"1588\",\"options\":[{\"param\":\"0\",\"id\":\"102\"},{\"param\":\"1000\",\"id\":\"239\"}]}]','2026-09-25 19:32:21','2026-09-26 19:27:43'),(48,'test22',93,'[{\"quantity\":\"100000\",\"temp_id\":\"1874\",\"options\":[]}]','2026-10-03 15:59:53','2026-10-26 08:25:54'),(49,'test01',95,'[{\"quantity\":\"1\",\"temp_id\":\"1538\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1930\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1929\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1931\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1932\",\"options\":[]},{\"quantity\":\"1\",\"temp_id\":\"1933\",\"options\":[]}]','2026-10-01 10:50:56','2026-10-31 06:14:03');
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

-- Dump completed on 2026-10-05 20:40:49
