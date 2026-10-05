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
-- Table structure for table `type_item`
--

DROP TABLE IF EXISTS `type_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `type_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL DEFAULT '',
  `index_body` int(11) NOT NULL DEFAULT -1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_item`
--

LOCK TABLES `type_item` WRITE;
/*!40000 ALTER TABLE `type_item` DISABLE KEYS */;
INSERT INTO `type_item` VALUES (1,'Quần',1),(2,'Găng',2),(3,'Giày',3),(4,'Rađa',4),(5,'Cải trang - avatar',5),(6,'Đậu thần',-1),(7,'Sách kỹ năng',-1),(8,'Vật phẩm nhiệm vụ',-1),(9,'Vàng',-1),(10,'Ngọc',-1),(11,'Vật phẩm đeo trên lưng',8),(12,'Ngọc rồng các loại',-1),(13,'Bùa',-1),(14,'Đá nâng cấp',-1),(15,'Mảnh đá vụn',-1),(16,'Bình nước phép',-1),(17,'.',-1),(18,'.',-1),(19,'.',-1),(20,'.',-1),(21,'.',-1),(22,'Vệ tinh',-1),(23,'Thú cưỡi mới',9),(24,'Thú cưỡi cũ',9),(25,'Rađa dò ngọc rồng Namếc',-1),(26,'.',-1),(27,'Tạp chủng',-1),(28,'Cờ pk',-1),(29,'Item time',-1),(30,'Sao pha lê',-1),(31,'Bánh trung thu, bánh tết',-1),(32,'Giáp tập luyện',6),(33,'Mảnh sưu tập',-1),(34,'Hồng ngọc',-1),(67,'Danh hieu tang 3',-1),(68,'Danh hiệu tầng 2',-1),(69,'Danh hiệu tầng 1 (-150)',12),(70,'Phụ kiện',11),(71,'Pet chạy sau lưng',7),(72,'Linh thú bay',10),(73,'Trứng linh thú',-1),(74,'Đá pha lê linh thú',-1),(75,'Girlkun75',-1),(80,'Vật phẩm đeo lưng đệ tử',7),(81,'Áo',0);
/*!40000 ALTER TABLE `type_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 14:24:44
