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
-- Table structure for table `task_badges_template`
--

DROP TABLE IF EXISTS `task_badges_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_badges_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `maxCount` int(11) NOT NULL DEFAULT 0,
  `idBadgesReward` int(11) NOT NULL DEFAULT -1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_badges_template`
--

LOCK TABLES `task_badges_template` WRITE;
/*!40000 ALTER TABLE `task_badges_template` DISABLE KEYS */;
INSERT INTO `task_badges_template` VALUES (1,'Nạp Tích luỹ 1 Triệu Trong Ngày',1000000,218),(2,'Ước Rồng Thần 1 Sao X10 Lần',10,219),(3,'Hạ Gục Cumber, Black Goku, Cooler, Xên ( 30 Lần )',30,220),(4,'Đập 3 Trang Bị +7 Trong Ngày',3,221),(5,'Top 1 Đại Hội Võ Đài Siêu Hạng',1,222),(6,'Hoàn Thành 10 Nhiệm Vụ Siêu Khó Tại Bò Mộng',10,223),(7,'Đánh Bại, Hoặc Cho Xương Sói 20 Lần',20,-1),(8,'Hoàn Thành Nhiệm Vụ 5 Lần Cho Xinbato Nước',5,-1),(9,'Nhặt Đồ Trong Ngày 500 Lần',500,224),(10,'Tiêu diệt 30 Boss Ăn Trộm',30,225),(11,'Tiêu Diệt 30 Boss Ở Dơ',30,-1);
/*!40000 ALTER TABLE `task_badges_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 13:55:57
