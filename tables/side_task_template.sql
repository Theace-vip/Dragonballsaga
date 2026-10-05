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
-- Table structure for table `side_task_template`
--

DROP TABLE IF EXISTS `side_task_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `side_task_template` (
  `id` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `max_count_lv1` varchar(255) NOT NULL,
  `max_count_lv2` varchar(255) NOT NULL,
  `max_count_lv3` varchar(255) NOT NULL,
  `max_count_lv4` varchar(255) NOT NULL,
  `max_count_lv5` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `side_task_template`
--

LOCK TABLES `side_task_template` WRITE;
/*!40000 ALTER TABLE `side_task_template` DISABLE KEYS */;
INSERT INTO `side_task_template` VALUES (0,'Tiêu diệt %1 khủng long','1-20','20-100','100-500','500-2000','2000-5000'),(1,'Tiêu diệt %1 lợn lòi','1-20','20-100','100-500','500-2000','2000-5000'),(2,'Tiêu diệt %1 quỷ đất','1-20','20-100','100-500','500-2000','2000-5000'),(3,'Tiêu diệt %1 khủng long mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(4,'Tiêu diệt %1 lợn lòi mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(5,'Tiêu diệt %1 quỷ đất mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(6,'Tiêu diệt %1 thằn lằn bay','1-20','20-100','100-500','500-2000','2000-5000'),(7,'Tiêu diệt %1 phi long','1-20','20-100','100-500','500-2000','2000-5000'),(8,'Tiêu diệt %1 quỷ bay','1-20','20-100','100-500','500-2000','2000-5000'),(9,'Tiêu diệt %1 thằn lằn mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(10,'Tiêu diệt %1 phi long mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(11,'Tiêu diệt %1 quỷ bay mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(12,'Tiêu diệt %1 heo rừng','1-20','20-100','100-500','500-2000','2000-5000'),(13,'Tiêu diệt %1 heo da xanh','1-20','20-100','100-500','500-2000','2000-5000'),(14,'Tiêu diệt %1 heo xayda','1-20','20-100','100-500','500-2000','2000-5000'),(15,'Tiêu diệt %1 ốc mượn hồn','1-20','20-100','100-500','500-2000','2000-5000'),(16,'Tiêu diệt %1 ốc sên','1-20','20-100','100-500','500-2000','2000-5000'),(17,'Tiêu diệt %1 heo xayda mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(18,'Tiêu diệt %1 không tặc','1-20','20-100','100-500','500-2000','2000-5000'),(19,'Tiêu diệt %1 quỷ đầu to','1-20','20-100','100-500','500-2000','2000-5000'),(20,'Tiêu diệt %1 quỷ địa ngục','1-20','20-100','100-500','500-2000','2000-5000'),(21,'Tiêu diệt %1 heo rừng mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(22,'Tiêu diệt %1 heo xanh mẹ','1-20','20-100','100-500','500-2000','2000-5000'),(23,'Tiêu diệt %1 alien','1-20','20-100','100-500','500-2000','2000-5000'),(24,'Tiêu diệt %1 tambourine','1-5','5-20','20-100','100-500','500-1000'),(25,'Tiêu diệt %1 drum','1-5','5-20','20-100','100-500','500-1000'),(26,'Tiêu diệt %1 akkuman','1-5','5-20','20-100','100-500','500-1000'),(27,'Tiêu diệt %1 nappa','1-5','5-20','20-100','100-500','500-1000'),(28,'Tiêu diệt %1 soldier','1-5','5-20','20-100','100-500','500-1000'),(29,'Tiêu diệt %1 appule','1-5','5-20','20-100','100-500','500-1000'),(30,'Tiêu diệt %1 raspberry','1-5','5-20','20-100','100-500','500-1000'),(31,'Tiêu diệt %1 thằn lằn xanh','1-5','5-20','20-100','100-500','500-1000'),(32,'Tiêu diệt %1 quỷ đầu nhọn','1-5','5-20','20-100','100-500','500-1000'),(33,'Tiêu diệt %1 quỷ đầu vàng','1-5','5-20','20-100','100-500','500-1000'),(34,'Tiêu diệt %1 quỷ da tím','1-5','5-20','20-100','100-500','500-1000'),(35,'Tiêu diệt %1 quỷ già','1-5','5-20','20-100','100-500','500-1000'),(36,'Tiêu diệt %1 cá sấu','1-5','5-20','20-100','100-500','500-1000'),(37,'Tiêu diệt %1 dơi da xanh','1-5','5-20','20-100','100-500','500-1000'),(38,'Tiêu diệt %1 quỷ chim','1-5','5-20','20-100','100-500','500-1000'),(39,'Tiêu diệt %1 lính đầu trọc','1-5','5-20','20-100','100-500','500-1000'),(40,'Tiêu diệt %1 lính tai dài','1-5','5-20','20-100','100-500','500-1000'),(41,'Tiêu diệt %1 lính vũ trụ','1-5','5-20','20-100','100-500','500-1000'),(42,'Tiêu diệt %1 khỉ lông đen','1-5','5-20','20-100','100-500','500-1000'),(43,'Tiêu diệt %1 khỉ giáp sắt','1-5','5-20','20-100','100-500','500-1000'),(44,'Tiêu diệt %1 khỉ lông đỏ','1-5','5-20','20-100','100-500','500-1000'),(45,'Tiêu diệt %1 khỉ lông vàng','1-5','5-20','20-100','100-500','500-1000'),(46,'Tiêu diệt %1 xên con cấp 1','1-5','5-20','20-100','100-500','500-1000'),(47,'Tiêu diệt %1 xên con cấp 2','1-5','5-20','20-100','100-500','500-1000'),(48,'Tiêu diệt %1 xên con cấp 3','1-5','5-20','20-100','100-500','500-1000'),(49,'Tiêu diệt %1 xên con cấp 4','1-5','5-20','20-100','100-500','500-1000'),(50,'Tiêu diệt %1 xên con cấp 5','1-5','5-20','20-100','100-500','500-1000'),(51,'Tiêu diệt %1 xên con cấp 6','1-5','5-20','20-100','100-500','500-1000'),(52,'Tiêu diệt %1 xên con cấp 7','1-5','5-20','20-100','100-500','500-1000'),(53,'Tiêu diệt %1 xên con cấp 8','1-5','5-20','20-100','100-500','500-1000'),(54,'Tiêu diệt %1 tai tím','1-5','5-20','20-100','100-500','500-1000'),(55,'Tiêu diệt %1 abo','1-5','5-20','20-100','100-500','500-1000'),(56,'Tiêu diệt %1 kado','1-5','5-20','20-100','100-500','500-1000'),(57,'Tiêu diệt %1 da xanh','1-5','5-20','20-100','100-500','500-1000'),(58,'Nhặt %1 vàng','1000-3000','3000-20000','20000-100000','100000-10000000','10000000-100000000');
/*!40000 ALTER TABLE `side_task_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 17:46:54
