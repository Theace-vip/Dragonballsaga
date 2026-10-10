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
-- Table structure for table `phuc_loi_tab`
--

DROP TABLE IF EXISTS `phuc_loi_tab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phuc_loi_tab` (
  `id` int(11) NOT NULL,
  `tab_id` int(11) NOT NULL DEFAULT 0,
  `name` text NOT NULL,
  `max_count` int(11) NOT NULL DEFAULT 0,
  `active` tinyint(4) NOT NULL DEFAULT 0,
  `list_item` longtext DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_phuc_loi_tab_tab_id` (`tab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phuc_loi_tab`
--

LOCK TABLES `phuc_loi_tab` WRITE;
/*!40000 ALTER TABLE `phuc_loi_tab` DISABLE KEYS */;
INSERT INTO `phuc_loi_tab` VALUES (0,0,'Online 1 phút',1,0,'[{\"quantity\":10,\"options\":[],\"id\":1271},{\"quantity\":5,\"options\":[],\"id\":1270}]'),(1,0,'Online 5 phút',5,0,'[{\"quantity\":10,\"options\":[],\"id\":1271},{\"quantity\":5,\"options\":[],\"id\":1270},{\"quantity\":1,\"options\":[{\"param\":100,\"id\":5},{\"param\":100,\"id\":6},{\"param\":100,\"id\":7},{\"param\":10,\"id\":14},{\"param\":1,\"id\":63}],\"id\":591}]'),(2,1,'Điểm danh Thứ 2',2,0,'[{\"quantity\":1,\"options\":[],\"id\":16}]'),(3,1,'Điểm danh Thứ 3',3,0,'[{\"quantity\":1,\"options\":[],\"id\":17}]'),(4,1,'Điểm danh Thứ 4',4,0,'[{\"quantity\":1,\"options\":[],\"id\":18}]'),(5,1,'Điểm danh Thứ 5',5,0,'[{\"quantity\":1,\"options\":[],\"id\":457}]'),(6,1,'Điểm danh Thứ 6',6,0,'[{\"quantity\":1,\"options\":[],\"id\":934}]'),(7,1,'Điểm danh Thứ 7',7,0,'[{\"quantity\":1,\"options\":[],\"id\":935}]'),(8,1,'Điểm danh Chủ nhật',1,0,'[{\"quantity\":1,\"options\":[],\"id\":936}]'),(12,0,'Online 10 phút',10,0,'[{\"quantity\":10,\"options\":[],\"id\":1271},{\"quantity\":5,\"options\":[],\"id\":1270},{\"quantity\":1,\"options\":[{\"param\":100,\"id\":5},{\"param\":100,\"id\":6},{\"param\":100,\"id\":7},{\"param\":1,\"id\":63}],\"id\":1810}]'),(13,0,'Online 20 phút',20,0,'[{\"quantity\":10,\"options\":[],\"id\":1271},{\"quantity\":5,\"options\":[],\"id\":1270},{\"quantity\":5,\"options\":[],\"id\":1150},{\"quantity\":1,\"options\":[{\"param\":100,\"id\":5},{\"param\":100,\"id\":6},{\"param\":100,\"id\":7},{\"param\":1,\"id\":63}],\"id\":1772}]'),(18,8,'SET Hắc Ám Loạn Lưu',5000,1,'[{\"quantity\":1,\"options\":[],\"id\":1919},{\"quantity\":1,\"options\":[],\"id\":1920},{\"quantity\":1,\"options\":[],\"id\":1921},{\"quantity\":1,\"options\":[],\"id\":1922},{\"quantity\":1,\"options\":[],\"id\":1923}]'),(19,4,'SET Thanh Long',100000,1,'[{\"quantity\":1,\"options\":[{\"param\":1000,\"id\":94},{\"param\":10000,\"id\":2},{\"param\":10000,\"id\":109}],\"id\":1679},{\"quantity\":1,\"options\":[],\"id\":1680},{\"quantity\":1,\"options\":[],\"id\":1681},{\"quantity\":1,\"options\":[],\"id\":1682},{\"quantity\":1,\"options\":[],\"id\":1683}]'),(20,5,'Thẻ x2 điểm farm',5,1,'[{\"quantity\":1,\"options\":[],\"id\":1916}]'),(21,5,'Thẻ x3 điểm farm',7,1,'[{\"quantity\":1,\"options\":[],\"id\":1917}]'),(22,5,'Thẻ x5 điểm farm',10,1,'[{\"quantity\":1,\"options\":[],\"id\":1918}]'),(23,5,'Thẻ giao dịch điểm farm',15,1,'[{\"quantity\":1,\"options\":[],\"id\":1251}]'),(24,5,'Thẻ next nv',5,1,'[{\"quantity\":1,\"options\":[],\"id\":1108}]'),(26,5,'Thẻ tu tiên',7,1,'[{\"quantity\":1,\"options\":[],\"id\":1132}]'),(28,8,'SET Hỗn Độn Vô Cực',10000,1,'[{\"quantity\":1,\"options\":[],\"id\":1924},{\"quantity\":1,\"options\":[],\"id\":1925},{\"quantity\":1,\"options\":[],\"id\":1926},{\"quantity\":1,\"options\":[],\"id\":1927},{\"quantity\":1,\"options\":[],\"id\":1928}]'),(29,4,'Lệnh Đồ Sát',100,1,'[{\"quantity\":1,\"options\":[],\"id\":1194}]'),(30,5,'SET Hắc Ám',100000,1,'[{\"quantity\":1,\"options\":[],\"id\":1174},{\"quantity\":1,\"options\":[],\"id\":1175},{\"quantity\":1,\"options\":[],\"id\":1176},{\"quantity\":1,\"options\":[],\"id\":1177},{\"quantity\":1,\"options\":[],\"id\":1178}]'),(31,5,'SET Thần Long',10000,1,'[{\"quantity\":1,\"options\":[],\"id\":1868},{\"quantity\":1,\"options\":[],\"id\":1869},{\"quantity\":1,\"options\":[],\"id\":1870},{\"quantity\":1,\"options\":[],\"id\":1871},{\"quantity\":1,\"options\":[],\"id\":1872}]'),(32,5,'Key vàng',5,1,'[{\"quantity\":1,\"options\":[],\"id\":1874}]'),(33,4,'Thẻ đổi tên',100,1,'[{\"quantity\":1,\"options\":[],\"id\":1665}]'),(34,5,'Thiên đạo',5,1,'[{\"quantity\":1,\"options\":[],\"id\":1224}]'),(35,5,'Địa đạo',1,1,'[{\"quantity\":1,\"options\":[],\"id\":1225}]'),(36,4,'Check thông tin',1000,1,'[{\"quantity\":1,\"options\":[],\"id\":1660}]'),(37,0,'Online 30 phút',30,0,'[]'),(38,0,'Online 1 tiếng',60,0,'[]'),(39,0,'Online 2 tiếng',120,0,'[]'),(40,0,'Online 5 tiếng',300,0,'[]'),(41,0,'Online 7 tiếng',420,0,'[]'),(42,0,'Online 10 tiếng',600,0,'[]'),(43,0,'Online 12 tiếng',720,0,'[]'),(44,2,'20.000',20000,0,'[]'),(45,2,'30.000',30000,0,'[]'),(46,2,'50.000',50000,0,'[]'),(47,2,'100.000',100000,0,'[]'),(48,2,'500.000',500000,0,'[]'),(49,2,'1.000.000',1000000,0,'[]'),(50,2,'1.500.000',1500000,0,'[]'),(51,2,'2.000.000',2000000,0,'[]'),(52,2,'3.000.000',3000000,0,'[]'),(53,2,'5.000.000',5000000,0,'[]'),(54,3,'50.000',50000,1,'[]'),(55,3,'50.000',50000,1,'[]'),(56,3,'100.000',100000,1,'[]'),(57,3,'100.000',100000,1,'[]'),(58,3,'150.000',150000,1,'[]'),(59,3,'200.000',200000,1,'[]'),(60,3,'300.000',300000,1,'[]'),(61,3,'500.000',500000,1,'[]'),(62,3,'1.000.000',1000000,1,'[]'),(63,4,'Đệ kid fide',50000,1,'[{\"quantity\":1,\"options\":[],\"id\":1300}]'),(64,4,'Kid uub',55000,1,'[{\"quantity\":1,\"options\":[],\"id\":1301}]'),(65,4,'Kid xên',60000,1,'[{\"quantity\":1,\"options\":[],\"id\":1302}]'),(66,4,'Kid bư',65000,1,'[{\"quantity\":1,\"options\":[],\"id\":1691}]'),(67,4,'Black thường',70000,1,'[{\"quantity\":1,\"options\":[],\"id\":1158}]'),(68,5,'Đệ Gohan',100,1,'[{\"quantity\":1,\"options\":[],\"id\":1639}]'),(69,5,'Đệ Gohan',110,1,'[{\"quantity\":1,\"options\":[],\"id\":1640}]'),(70,5,'Đệ Gohan',120,1,'[{\"quantity\":1,\"options\":[],\"id\":1641}]'),(71,8,'Đệ Gohan',100,1,'[{\"quantity\":1,\"options\":[],\"id\":1642}]'),(72,8,'Đệ Gohan',110,1,'[{\"quantity\":1,\"options\":[],\"id\":1643}]'),(73,8,'Đệ Gohan',120,1,'[{\"quantity\":1,\"options\":[],\"id\":1644}]'),(74,6,'Vật phẩm cơ bản',1000,1,'[{\"quantity\":10,\"options\":[],\"id\":381},{\"quantity\":10,\"options\":[],\"id\":382},{\"quantity\":10,\"options\":[],\"id\":383},{\"quantity\":10,\"options\":[],\"id\":384}]'),(75,6,'Vật phẩm siêu cấp',10000,1,'[{\"quantity\":10,\"options\":[],\"id\":1099},{\"quantity\":10,\"options\":[],\"id\":1100},{\"quantity\":10,\"options\":[],\"id\":1101},{\"quantity\":10,\"options\":[],\"id\":1102}]'),(76,7,'Giáp tập luyện cấp 1',1000,1,'[{\"quantity\":1,\"options\":[],\"id\":529}]'),(77,7,'Giáp tập luyện cấp 2',2000,1,'[{\"quantity\":1,\"options\":[],\"id\":530}]'),(78,7,'Giáp tập luyện cấp 3',3000,1,'[{\"quantity\":1,\"options\":[],\"id\":531}]'),(79,7,'Bông tai',1000,1,'[{\"quantity\":1,\"options\":[],\"id\":454}]'),(80,7,'Mảnh hồn/Mảnh vỡ',1000,1,'[{\"quantity\":1000,\"options\":[],\"id\":933},{\"quantity\":1000,\"options\":[],\"id\":934}]'),(81,8,'Cải trang mở chỉ số',100,1,'[{\"quantity\":1,\"options\":[{\"param\":0,\"id\":241}],\"id\":1507}]'),(82,8,'Cải trang mở chỉ số',100,1,'[{\"quantity\":1,\"options\":[{\"param\":0,\"id\":241}],\"id\":932}]'),(83,8,'Cải trang mở chỉ số',100,1,'[{\"quantity\":1,\"options\":[{\"param\":0,\"id\":241}],\"id\":1093}]'),(84,8,'Cải trang mở chỉ số',100,1,'[{\"quantity\":1,\"options\":[{\"param\":0,\"id\":241}],\"id\":879}]'),(85,7,'Capsu',1000,1,'[{\"quantity\":1,\"options\":[],\"id\":194}]'),(86,7,'Tàu bay',1,1,'[{\"quantity\":1,\"options\":[],\"id\":795}]');
/*!40000 ALTER TABLE `phuc_loi_tab` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-10 19:29:26
