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
-- Table structure for table `web_shop`
--

DROP TABLE IF EXISTS `web_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `web_shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(100) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `sold_quantity` int(11) NOT NULL DEFAULT 0,
  `price` bigint(20) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `template_id` int(11) DEFAULT 1150,
  `item_img` varchar(255) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `web_shop`
--

LOCK TABLES `web_shop` WRITE;
/*!40000 ALTER TABLE `web_shop` DISABLE KEYS */;
INSERT INTO `web_shop` VALUES (83,'Lệnh Đồ Sát',0,1099999,0,'2025-11-04 14:50:12',1194,'18232',''),(84,'Lượng Bạc',0,15000053,10,'2025-11-04 14:57:52',1271,'30443','Luoưng Bạc'),(85,'Đậu Thần Cấp 10',0,101,100,'2025-11-06 12:50:31',595,'241','đậu thần'),(86,'Đệ Xên ',0,1129,0,'2025-11-06 13:55:01',722,'6782','đệ free'),(87,'Hào Quang Hit',0,7,0,'2025-11-08 12:13:16',1481,'12745',''),(88,'Hào qua Kaioken Blue ',0,7,0,'2025-11-08 15:12:35',1482,'12750',''),(89,'Hào Quang jiren',0,7,0,'2025-11-08 15:13:15',1483,'12753',''),(90,'Cá Chà Bá',0,3144,0,'2025-11-08 15:13:53',1524,'10617',''),(91,'Đá Thần ngũ sắc',0,12100,10,'2025-11-08 15:19:16',674,'15109',''),(92,'Cân đẩu vân ngũ sắc',0,6,0,'2025-11-08 15:21:21',733,'6980',''),(93,'Cải trang Bill Bí Ngô',0,12,0,'2025-11-08 15:22:18',739,'6985',''),(94,'Thẻ Tăng 1 Cảnh Gioi Tu Tiên',0,9545,250000,'2025-11-08 15:24:11',1132,'6777',''),(95,'Thú Cưỡi Thanh Long',0,100007,10000,'2025-11-08 15:25:26',1750,'15862',''),(96,'Mảnh Thiên Sứ(Áo)',0,0,1,'2025-11-08 15:46:52',1066,'10197',''),(97,'Mảnh Thiên Sứ(Quần)',0,0,1,'2025-11-08 15:46:53',1067,'10198',''),(98,'Mảnh Thiên Sứ(Giày)',0,0,1,'2025-11-08 15:46:54',1068,'10199',''),(99,'Mảnh Thiên Sứ(Nhẫn)',0,0,1,'2025-11-08 15:46:54',1069,'10201',''),(100,'Mảnh Thiên Sứ(Găng)',0,0,1,'2025-11-08 15:46:55',1070,'10200',''),(101,'quà Top Điểm Fam',0,352,250000,'2025-11-08 15:47:05',1097,'32258',''),(102,'Ngọc Rồng 1 sao',0,99,500000,'2025-11-08 15:53:47',14,'419','Thu thập để ước rồng thần'),(103,'Ngọc Rồng 2 sao',0,0,80000,'2025-11-08 15:54:03',15,'420','Thu thập để ước rồng thần'),(104,'Ngọc Rồng 3 sao',0,0,60000,'2025-11-08 15:54:04',16,'421','Thu thập để ước rồng thần'),(105,'Ngọc Rồng 4 sao',0,0,10000,'2025-11-08 15:54:05',17,'422','Thu thập để ước rồng thần'),(106,'Ngọc Rồng 5 sao',0,0,2000,'2025-11-08 15:54:07',18,'423','Thu thập để ước rồng thần'),(107,'Ngọc Rồng 6 sao',0,0,500,'2025-11-08 15:54:08',19,'424','Thu thập để ước rồng thần'),(108,'Ngọc Rồng 7 sao',0,0,100,'2025-11-08 15:54:08',20,'425','Thu thập để ước rồng thần'),(109,'Thỏi Vàng',0,10000,100,'2025-11-08 16:03:15',457,'4028','Thỏi Vàng'),(110,'Tiến Hóa lv1',0,24,0,'2025-11-08 16:05:17',1904,'20951','Dùng để tiến hóa lên sức mạnh mới'),(111,'Phân thân lv1',0,22,0,'2025-11-08 16:05:18',1905,'20951','Dùng để học skill đặc biệt'),(112,'Phân thân lv2',0,7,200000,'2025-11-08 16:05:19',1906,'20951','Dùng để học skill đặc biệt'),(113,'Phân thân lv3',0,6,300000,'2025-11-08 16:05:20',1907,'20951','Dùng để học skill đặc biệt'),(114,'Phân thân lv4',0,6,400000,'2025-11-08 16:05:21',1908,'20951','Dùng để học skill đặc biệt'),(115,'Phân thân lv5',0,6,500000,'2025-11-08 16:05:23',1909,'20951','Dùng để học skill đặc biệt'),(116,'Phân thân lv6',0,6,600000,'2025-11-08 16:05:24',1910,'20951','Dùng để học skill đặc biệt'),(117,'Phân thân lv7',0,5,700000,'2025-11-08 16:05:25',1911,'20951','Dùng để học skill đặc biệt'),(118,'Super Goku lv 1',0,9,0,'2025-11-08 16:05:28',1886,'20986','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(119,'Super Goku lv 2',0,1,100000,'2025-11-08 16:07:49',1887,'20987','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(120,'Super Goku lv 3',0,1,200000,'2025-11-08 16:07:50',1888,'20988','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(121,'Super Goku lv 4',0,1,300000,'2025-11-08 16:07:51',1889,'20989','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(122,'Super Goku lv 5',0,1,400000,'2025-11-08 16:07:52',1890,'20990','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(123,'Super Goku lv 6',0,1,500000,'2025-11-08 16:07:53',1891,'20991','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(124,'Super Picolo lv 1',0,15,0,'2025-11-08 16:07:53',1892,'20998','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(125,'Super Picolo lv 2',0,6,100000,'2025-11-08 16:07:55',1893,'20999','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(126,'Super Picolo lv 3',0,6,200000,'2025-11-08 16:07:55',1894,'21000','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(127,'Super Picolo lv 4',0,5,300000,'2025-11-08 16:07:56',1895,'21001','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(128,'Super Picolo lv 5',0,5,400000,'2025-11-08 16:07:57',1896,'21002','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(129,'Super Picolo lv 6',0,5,500000,'2025-11-08 16:08:00',1897,'21003','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(130,'Super Vegita lv 1',0,7,0,'2025-11-08 16:08:01',1898,'20992','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(131,'Super Vegita lv 2',0,1,100000,'2025-11-08 16:08:02',1899,'20993','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(132,'Super Vegita lv 3',0,1,200000,'2025-11-08 16:08:04',1900,'20994','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(133,'Super Vegita lv 4',0,0,300000,'2025-11-08 16:08:13',1901,'20995','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(134,'Super Vegita lv 5',0,0,400000,'2025-11-08 16:08:14',1902,'20996','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(135,'Super Vegita lv 6',0,0,500000,'2025-11-08 16:08:16',1903,'20997','Dùng để học skill đặc biệt Tăng 50% SD,HP,KI,GIAP'),(136,'Hào quang',0,10,0,'2025-11-08 16:11:20',1876,'18711',''),(137,'Hào quang s2',0,5,0,'2025-11-08 16:11:21',1877,'18709',''),(138,'Hào quang s3',0,10,0,'2025-11-08 16:11:23',1878,'18708',''),(139,'máy dò cu boss',0,1152200,0,'2025-11-10 14:02:33',1881,'15457','');
/*!40000 ALTER TABLE `web_shop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 13:51:31
