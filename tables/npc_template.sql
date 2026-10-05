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
-- Table structure for table `npc_template`
--

DROP TABLE IF EXISTS `npc_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `npc_template` (
  `id` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `head` int(11) NOT NULL,
  `body` int(11) NOT NULL,
  `leg` int(11) NOT NULL,
  `avatar` int(11) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `npc_template`
--

LOCK TABLES `npc_template` WRITE;
/*!40000 ALTER TABLE `npc_template` DISABLE KEYS */;
INSERT INTO `npc_template` VALUES (0,'Ông Gôhan',18,19,20,349),(1,'Ông Paragus',24,25,26,348),(2,'Ông Moori',21,22,23,347),(3,'Rương đồ',74,75,265,0),(4,'Đậu thần',84,51,84,0),(5,'Con mèo',75,-1,-1,0),(6,'Khu vực',-1,-1,-1,0),(7,'Bunma',42,43,44,562),(8,'Dende',45,46,47,350),(9,'Appule',3,4,5,565),(10,'Dr. Brief',784,785,786,7184),(11,'Cargo',54,55,56,641),(12,'Cui',48,49,50,639),(13,'Quy Lão Kame',33,34,35,564),(14,'Trưỡng lão guru',39,40,41,566),(15,'Vua Vegeta',36,37,38,563),(16,'Uron',61,62,63,728),(17,'Bò mộng',80,81,82,1142),(18,'Thần Mèo Karin',89,90,91,1209),(19,'Thượng Đế',86,87,88,1356),(20,'Thần Vũ Trụ',98,99,100,1357),(21,'Bà Hạt Mít',117,118,119,1410),(22,'Trọng tài',114,115,116,1411),(23,'Ghi danh',120,121,122,1415),(24,'Rồng Thiêng',103,104,105,0),(25,'Lính canh',132,133,134,1468),(26,'Độc Nhãn',144,145,146,1571),(27,'Rôgng Thiêng Namec',0,0,0,0),(28,'Cửa hàng kí gửi',120,121,122,1415),(29,'Rồng Omega',204,205,206,2332),(30,'Rồng 2 sao',207,208,209,2333),(31,'Rồng 3 sao',210,211,212,2334),(32,'Rồng 4 sao',213,214,215,2335),(33,'Rồng 5 sao',216,217,218,2336),(34,'Rồng 6 sao',219,220,221,2337),(35,'Rồng 7 sao',222,223,224,2338),(36,'Rồng 1 sao',225,226,227,2344),(37,'Bunma',267,268,269,2752),(38,'Ca Lích',270,271,272,1364),(39,'Santa',300,301,302,2993),(40,'Mabư mập',297,298,299,0),(41,'Trung thu',120,121,122,0),(42,'Quốc Vương',442,443,444,4335),(43,'Tổ Sư Kaio',448,449,450,4389),(44,'Ôsin',433,434,435,4390),(45,'Kibit',436,437,438,4391),(46,'Babiđây',430,431,432,4388),(47,'Giu-ma Đầu Bò',445,446,447,0),(48,'Ngộ Không',462,470,471,0),(49,'Đường Tăng',467,468,469,4544),(50,'Quả trứng',-1,-1,-1,0),(51,'Dưa hấu',-1,-1,-1,0),(52,'Hùng Vương',484,485,486,0),(53,'Tapion',481,482,483,4668),(54,'Lý Tiểu Nương',487,488,489,3049),(55,'Bill',508,509,510,5067),(56,'Whis',505,506,507,5073),(57,'Champa',511,512,513,0),(58,'Vados',530,531,532,5074),(59,'Trọng tài',533,534,535,0),(60,'Goku SSJ',101,57,66,1359),(61,'Goku SSJ',0,523,524,516),(62,'Potage',621,622,623,5828),(63,'Jaco',624,625,626,5833),(64,'Cađíc',645,646,647,5073),(65,'Yarirobe',77,78,79,0),(66,'Nồi bánh',766,767,768,7084),(67,'Mr Popo',83,84,85,2132),(68,'Panchy',787,788,789,0),(69,'Thỏ Đại Ca',403,404,405,0),(70,'Bardock',1012,1013,1014,9075),(71,'Berry',1015,1016,1017,9076),(72,'ToriBot',1143,1144,1145,10477),(73,'Sự Kiện',1173,1174,1175,9493),(74,'Fide',1062,1063,1064,10477),(75,'Bảng Xếp Hạng',645,646,647,0),(76,'Bí Kiếp Thuật',409,410,411,0),(77,'',703,704,705,6578),(78,'Naruto',1752,1753,1754,32200),(79,'Haatsu',1431,1432,1433,0),(80,'Kaio Shin',433,434,435,4390),(81,'Cẩu Thần Thú',1900,1901,1902,0),(82,'',2045,2046,2047,0),(83,'Tháp Bí Cảnh',1910,1911,1912,0),(84,'NPC Tân Thủ',297,298,299,0),(85,'Thiên Đạo',1506,1507,1508,14096),(86,'Thiên Sứ Girl',1098,1099,1100,10586),(87,'',2045,2046,2047,0),(88,'Đạo Lữ',1897,1898,1899,0),(89,'Đường Tăng',467,468,469,0),(90,'Sự Kiện Mabu',1894,1895,1896,0),(91,'Luffy Shop',582,583,584,0),(92,'Trung thu 2025',120,121,122,0),(93,'MC Kết Hôn',693,694,695,0),(94,'Tu Tiên & Chuyển Sinh',787,788,789,0),(95,'Nông Dân AFK',582,583,584,0),(96,'Thợ Mỏ Pro',1252,1253,1254,-1),(97,'',-1,-1,-1,-1),(98,'Ông già Noel',657,658,659,0),(99,'Cây thông Noel',2003,2004,2005,0),(100,'PhucNeAE',391,392,393,0),(101,'Rồng Băng',0,0,0,0),(102,'Sổ Xứ Mệnh',850,851,852,0),(103,'Tạp Hóa Gói',1458,1459,1460,0),(104,'',-1,-1,-1,-1),(105,'',-1,-1,-1,-1),(106,'',-1,-1,-1,-1),(107,'',-1,-1,-1,-1),(108,'Heart',2109,2110,2111,16165),(109,'Bulma Bunny',409,410,411,4119),(110,'Bunma Rực Rỡ',2123,2124,2125,16269),(111,'Đua top',645,646,647,0);
/*!40000 ALTER TABLE `npc_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:13:01
