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
-- Table structure for table `achievement_template`
--

DROP TABLE IF EXISTS `achievement_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `achievement_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `info1` text NOT NULL,
  `info2` text NOT NULL,
  `money` int(11) NOT NULL,
  `max_count` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `achievement_template`
--

LOCK TABLES `achievement_template` WRITE;
/*!40000 ALTER TABLE `achievement_template` DISABLE KEYS */;
INSERT INTO `achievement_template` VALUES (1,'Gia nhập Vệ Binh','Đạt cấp Vệ Binh',1000,340000),(2,'Sức mạnh siêu cấp','Đạt cấp %1',5000,1500000),(3,'Nông dân chăm chỉ','Cây đậu thần đạt cấp 5',2000,5),(4,'Trăm trận trăm thắng','Thắng 100 người khác nhau',2000,100),(5,'Nội công cao cường','Chưởng 2.000 phát',1000,2000),(6,'Khinh công thành thạo','Bay 20.000 mét',1000,20000),(7,'Thợ săn thiện xạ','Hạ 1.000 quái trên không',1000,1000),(8,'Tập luyện bài bản','Hạ 1.000 người rơm',1000,1000),(9,'Hoạt động chăm chỉ','Chơi hơn 120 giờ',2000,120),(10,'Hỗ trợ đồng đội','Cho 10.000 đậu thần',5000,10000),(11,'Trùm nhặt ve chai','Bán cho %2 200 món đồ',1000,200),(12,'Lần đầu nạp ngọc','Nạp ít nhất 150 ngọc',5000,150),(13,'Đánh bại siêu quái','Hạ 100 siêu quái',1000,100),(14,'Thánh hồi sinh','Hồi sinh tại chỗ 200 lần',5000,200),(15,'Kỹ năng thành thạo','Dùng chiêu đặc biệt 1000 lần',2000,1000),(16,'Trùm nhặt ngọc','Nhặt 1000 ngọc',2000,1000),(17,'Đạt 15 triệu sức mạnh','Dành cho tân thủ từ 19/7/2023',10000,15000000),(18,'Tuyệt kỹ thành thạo','Dùng tuyệt kỹ (skill thứ 9) 7749 lần',5000,49),(19,'Chăm sóc đặc biệt','Được Namếc hồi sinh 2K lần',1500,2000),(20,'Trùm kết liễu Boss','Đánh đòn cuối hạ Boss 2K lần',2000,2000),(21,'Trùm kết liễu Boss','Đánh đòn cuối hạ Boss 20K lần',20000,20000);
/*!40000 ALTER TABLE `achievement_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 14:02:53
