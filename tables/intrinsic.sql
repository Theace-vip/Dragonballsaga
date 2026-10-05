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
-- Table structure for table `intrinsic`
--

DROP TABLE IF EXISTS `intrinsic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intrinsic` (
  `id` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `param_from_1` int(11) NOT NULL DEFAULT 0,
  `param_to_1` int(11) NOT NULL DEFAULT 0,
  `param_from_2` int(11) NOT NULL DEFAULT 0,
  `param_to_2` int(11) NOT NULL DEFAULT 0,
  `icon` int(11) NOT NULL DEFAULT 0,
  `gender` smallint(6) NOT NULL DEFAULT 3,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `intrinsic`
--

LOCK TABLES `intrinsic` WRITE;
/*!40000 ALTER TABLE `intrinsic` DISABLE KEYS */;
INSERT INTO `intrinsic` VALUES (0,'Chưa kích hoạt nội tại\nBấm vào để xem chi tiết',0,0,0,0,5223,3),(1,'Chiêu đấm Dragon +p0% đến p1% sát thương',5,100,0,0,5223,0),(2,'Chiêu Kamejoko +p0% đến p1% sát thương',5,200,0,0,5223,0),(3,'Thái Dương Hạ San +p0% đến p1% tốc độ -p2% đến p3% KI',10,35,10,35,5223,0),(4,'Quả cầu kênh khi +p0% đến p1% tốc độ hồi phục',15,55,0,0,5223,0),(5,'Khiên năng lượng +p0% đến p1% tốc độ hồi phục',15,55,0,0,5223,0),(6,'Dịch chuyển tức thời +p0% đến p1% sát thương đòn kế',1,500,0,0,5223,0),(7,'Thôi miên +p0% đến p1% sát thương đòn kế',1,500,0,0,5223,0),(8,'Chiêu đấm Demon +p0% đến p1% sát thương',5,500,0,0,5223,1),(9,'Chiêu Masenko +p0% đến p1% sát thương',2,150,0,0,5223,1),(10,'Trị thương +p0% đến p1% tốc độ hồi phục',15,65,0,0,5223,1),(11,'Makankosappo +p0% đến p1% tốc độ hồi phục',15,55,0,0,5223,1),(12,'Đẻ trứng +p0% đến p1% tốc độ hồi phục',15,65,0,0,5223,1),(13,'Liên hoàn +p0% đến p1% sát thương',5,25,0,0,5223,1),(14,'Biến Sôcôla +p0% đến p1% sát thương đòn kế',50,500,0,0,5223,1),(15,'Khiên năng lượng +p0% đến p1% tốc độ hồi phục',15,55,0,0,5223,1),(16,'Chiêu đấm Galick +p0% đến p1% sát thương',5,80,0,0,5223,2),(17,'Chiêu Antomic +p0% đến p1% sát thương',5,160,0,0,5223,2),(18,'Biến hình +p0% đến p1% sát thương',5,55,0,0,5223,2),(19,'Tự phát nổ +p0% đến p1% tốc độ hồi phục',15,65,0,0,5223,2),(20,'Khiên năng lượng +p0% đến p1% tốc độ hồi phục',15,55,0,0,5223,2),(21,'Huýt sáo +p0% đến p1% tốc độ hồi phục',15,65,0,0,5223,2),(22,'Trói +p0% đến p1% sát thương đòn kế',1,500,0,0,5223,2),(23,'Vàng rơi từ quái +p0% đến p1%',25,300,0,0,5223,3),(24,'Sức mạnh và tiềm năng khi đánh quái +p0% đến p1%',5,500,0,0,5223,3),(25,'Chí mạng liên tục khi HP dưới p0% đến p1%',20,50,0,0,5223,3),(26,'Kaioken tăng sát thương từ p0% đến p1%',1,50,0,0,5223,0);
/*!40000 ALTER TABLE `intrinsic` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 13:16:37
