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
-- Table structure for table `panel_option_dict`
--

DROP TABLE IF EXISTS `panel_option_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `panel_option_dict` (
  `option_id` int(11) NOT NULL,
  `ten_viet` varchar(255) NOT NULL DEFAULT '',
  `goi_y_param` varchar(255) NOT NULL DEFAULT '',
  `min_param` bigint(20) NOT NULL DEFAULT 0,
  `max_param` bigint(20) NOT NULL DEFAULT 999999999,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `panel_option_dict`
--

LOCK TABLES `panel_option_dict` WRITE;
/*!40000 ALTER TABLE `panel_option_dict` DISABLE KEYS */;
INSERT INTO `panel_option_dict` VALUES (0,'Suc danh goc','Do cong, de 500-20000',0,1000000,'2026-09-20 16:18:32'),(5,'% Suc danh','Ti le %, 1-20',0,100,'2026-09-20 16:18:32'),(6,'HP goc','Mau cong them, 1000-50000',0,5000000,'2026-09-20 16:18:32'),(7,'KI goc','KI cong them',0,5000000,'2026-09-20 16:18:32'),(14,'Chi mang','Ti le chi mang %, 1-25',0,100,'2026-09-20 16:18:32'),(21,'Yeu cau SM','Dieu kien suc manh',0,9999999999,'2026-09-20 16:18:32'),(22,'HP','Mau, 100-5000',0,100000,'2026-09-20 16:18:32'),(23,'KI','KI, 100-5000',0,100000,'2026-09-20 16:18:32'),(30,'Hoi HP','Tu dong hoi',0,100000,'2026-09-20 16:18:32'),(47,'Giap','Giam sat thuong, 100-5000',0,100000,'2026-09-20 16:18:32'),(50,'Suc danh','Cong dame, 100-5000',0,100000,'2026-09-20 16:18:32'),(77,'HP toi da','% hoac so, tuy ban',0,1000000,'2026-09-20 16:18:32'),(93,'Toc do hoi','tuy ban',0,100000,'2026-09-20 16:18:32'),(103,'KI toi da','tuy ban',0,1000000,'2026-09-20 16:18:32'),(107,'Phan don / dac biet','Tuy ban, de 1-20',0,1000,'2026-09-20 16:18:32');
/*!40000 ALTER TABLE `panel_option_dict` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:13:04
