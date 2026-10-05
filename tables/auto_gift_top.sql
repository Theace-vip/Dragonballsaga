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
-- Table structure for table `auto_gift_top`
--

DROP TABLE IF EXISTS `auto_gift_top`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auto_gift_top` (
  `id_top` int(11) NOT NULL DEFAULT 0,
  `name_top` varchar(50) NOT NULL DEFAULT '',
  `date_time` datetime NOT NULL,
  `user_receive` text NOT NULL,
  `is_receive` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auto_gift_top`
--

LOCK TABLES `auto_gift_top` WRITE;
/*!40000 ALTER TABLE `auto_gift_top` DISABLE KEYS */;
INSERT INTO `auto_gift_top` VALUES (0,'Top Săn Vĩ Thú','2025-05-02 18:30:22','[\"pId:1#tLog:00:00:10 (03-05-2025)\",\"pId:706#tLog:00:00:09 (03-05-2025)\",\"pId:658#tLog:00:00:10 (03-05-2025)\",\"pId:931#tLog:00:00:10 (03-05-2025)\",\"pId:20#tLog:00:00:10 (03-05-2025)\",\"pId:661#tLog:00:00:10 (03-05-2025)\",\"pId:776#tLog:00:00:09 (03-05-2025)\",\"pId:205#tLog:00:00:09 (03-05-2025)\",\"pId:1630#tLog:00:00:09 (03-05-2025)\",\"pId:1791#tLog:00:00:10 (03-05-2025)\"]',1),(1,'Top Săn Boss','2025-05-02 18:30:22','[\"pId:146#tLog:01:48:27 (03-08-2025)\",\"pId:179#tLog:01:48:27 (03-08-2025)\",\"pId:37#tLog:01:48:27 (03-08-2025)\",\"pId:168#tLog:01:48:27 (03-08-2025)\",\"pId:8#tLog:01:48:27 (03-08-2025)\",\"pId:186#tLog:01:48:27 (03-08-2025)\",\"pId:844#tLog:01:48:27 (03-08-2025)\",\"pId:29#tLog:01:48:27 (03-08-2025)\",\"pId:14#tLog:01:48:27 (03-08-2025)\",\"pId:127#tLog:01:48:27 (03-08-2025)\"]',1),(2,'Top Vĩ Thú','2025-05-02 18:30:22','',1),(3,'Top Leo Tháp','2025-05-02 18:30:22','',1),(4,'Top Sức Mạnh','2025-05-10 11:36:46','[\"pId:18#tLog:23:14:15 (23-07-2025)\",\"pId:146#tLog:23:14:15 (23-07-2025)\",\"pId:20#tLog:23:14:15 (23-07-2025)\",\"pId:8#tLog:23:14:15 (23-07-2025)\",\"pId:11#tLog:23:14:15 (23-07-2025)\",\"pId:29#tLog:23:14:15 (23-07-2025)\",\"pId:13#tLog:23:14:15 (23-07-2025)\",\"pId:14#tLog:23:14:15 (23-07-2025)\",\"pId:30#tLog:23:14:15 (23-07-2025)\",\"pId:127#tLog:23:14:15 (23-07-2025)\"]',1),(5,'Top Nạp','2025-09-20 18:31:02','[\"pId:240#tLog:00:02:16 (17-10-2025)\",\"pId:145#tLog:00:02:16 (17-10-2025)\",\"pId:610#tLog:00:02:16 (17-10-2025)\",\"pId:1411#tLog:00:02:16 (17-10-2025)\",\"pId:1428#tLog:00:02:16 (17-10-2025)\",\"pId:8#tLog:00:02:16 (17-10-2025)\",\"pId:954#tLog:00:02:16 (17-10-2025)\",\"pId:1499#tLog:00:02:16 (17-10-2025)\",\"pId:1119#tLog:00:02:15 (17-10-2025)\",\"pId:447#tLog:00:02:16 (17-10-2025)\"]',1),(6,'Top Fam','2025-09-20 18:31:02','[\"pId:241#tLog:00:04:35 (21-09-2025)\",\"pId:97#tLog:00:04:35 (21-09-2025)\",\"pId:131#tLog:00:04:35 (21-09-2025)\",\"pId:4#tLog:00:04:35 (21-09-2025)\",\"pId:89#tLog:00:04:35 (21-09-2025)\",\"pId:27#tLog:00:04:35 (21-09-2025)\",\"pId:60#tLog:00:04:35 (21-09-2025)\",\"pId:124#tLog:00:04:35 (21-09-2025)\",\"pId:317#tLog:00:04:35 (21-09-2025)\",\"pId:366#tLog:00:04:35 (21-09-2025)\"]',1),(7,'Top Phòng Tập','2025-05-21 19:15:08','[\"pId:48#tLog:20:21:50 (21-05-2025)\",\"pId:2626#tLog:20:21:50 (21-05-2025)\",\"pId:51#tLog:20:21:50 (21-05-2025)\",\"pId:53#tLog:20:21:49 (21-05-2025)\",\"pId:2121#tLog:20:21:50 (21-05-2025)\",\"pId:2473#tLog:20:21:50 (21-05-2025)\",\"pId:139#tLog:20:21:49 (21-05-2025)\",\"pId:2892#tLog:20:21:50 (21-05-2025)\",\"pId:77#tLog:20:21:50 (21-05-2025)\",\"pId:110#tLog:20:21:49 (21-05-2025)\"]',1),(8,'Top Săn Boss','2025-06-20 13:15:08','[\"pId:146#tLog:01:48:27 (03-08-2025)\",\"pId:179#tLog:01:48:27 (03-08-2025)\",\"pId:37#tLog:01:48:27 (03-08-2025)\",\"pId:168#tLog:01:48:27 (03-08-2025)\",\"pId:8#tLog:01:48:27 (03-08-2025)\",\"pId:186#tLog:01:48:27 (03-08-2025)\",\"pId:844#tLog:01:48:27 (03-08-2025)\",\"pId:29#tLog:01:48:27 (03-08-2025)\",\"pId:14#tLog:01:48:27 (03-08-2025)\",\"pId:127#tLog:01:48:27 (03-08-2025)\"]',1),(9,'Top Săn mabu','2025-05-23 13:15:08','[\"pId:114#tLog:11:12:51 (13-06-2025)\",\"pId:99#tLog:11:12:51 (13-06-2025)\",\"pId:51#tLog:11:12:51 (13-06-2025)\",\"pId:3332#tLog:11:12:51 (13-06-2025)\",\"pId:53#tLog:11:12:51 (13-06-2025)\",\"pId:954#tLog:11:12:51 (13-06-2025)\",\"pId:106#tLog:11:12:51 (13-06-2025)\",\"pId:76#tLog:11:12:51 (13-06-2025)\",\"pId:77#tLog:11:12:51 (13-06-2025)\",\"pId:1502#tLog:11:12:52 (13-06-2025)\"]',1),(10,'Top Đập Đồ','2025-08-03 01:32:56','[\"pId:448#tLog:01:36:51 (03-10-2025)\",\"pId:610#tLog:01:36:51 (03-10-2025)\",\"pId:258#tLog:01:36:52 (03-10-2025)\",\"pId:4#tLog:01:36:51 (03-10-2025)\",\"pId:5#tLog:01:36:52 (03-10-2025)\",\"pId:54#tLog:01:36:50 (03-10-2025)\",\"pId:8#tLog:01:36:50 (03-10-2025)\",\"pId:13#tLog:01:36:51 (03-10-2025)\",\"pId:413#tLog:01:36:52 (03-10-2025)\",\"pId:447#tLog:01:36:51 (03-10-2025)\"]',1),(11,'Top Rương Thần Bí','2025-09-20 18:31:02','[\"pId:131#tLog:00:04:35 (21-09-2025)\",\"pId:4#tLog:00:04:35 (21-09-2025)\",\"pId:116#tLog:00:04:35 (21-09-2025)\",\"pId:117#tLog:00:04:35 (21-09-2025)\",\"pId:54#tLog:00:04:35 (21-09-2025)\",\"pId:118#tLog:00:04:35 (21-09-2025)\",\"pId:27#tLog:00:04:35 (21-09-2025)\",\"pId:60#tLog:00:04:35 (21-09-2025)\",\"pId:124#tLog:00:04:35 (21-09-2025)\",\"pId:13#tLog:00:04:35 (21-09-2025)\"]',1),(12,'Top Tầm Bảo','2025-09-20 18:31:02','[\"pId:145#tLog:00:07:30 (17-10-2025)\",\"pId:610#tLog:00:07:30 (17-10-2025)\",\"pId:4#tLog:00:07:30 (17-10-2025)\",\"pId:54#tLog:00:07:30 (17-10-2025)\",\"pId:8#tLog:00:07:30 (17-10-2025)\",\"pId:954#tLog:00:07:30 (17-10-2025)\",\"pId:27#tLog:00:07:30 (17-10-2025)\",\"pId:12#tLog:00:07:30 (17-10-2025)\",\"pId:13#tLog:00:07:30 (17-10-2025)\",\"pId:447#tLog:00:07:29 (17-10-2025)\"]',1),(13,'Top Câu Cá','2025-09-20 18:31:02','[\"pId:4#tLog:00:02:16 (17-10-2025)\",\"pId:54#tLog:00:02:16 (17-10-2025)\",\"pId:39#tLog:00:02:16 (17-10-2025)\",\"pId:8#tLog:00:02:16 (17-10-2025)\",\"pId:10#tLog:00:02:16 (17-10-2025)\",\"pId:699#tLog:00:02:16 (17-10-2025)\",\"pId:27#tLog:00:02:16 (17-10-2025)\",\"pId:60#tLog:00:02:16 (17-10-2025)\",\"pId:13#tLog:00:02:16 (17-10-2025)\",\"pId:447#tLog:00:02:16 (17-10-2025)\"]',1);
/*!40000 ALTER TABLE `auto_gift_top` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 14:02:54
