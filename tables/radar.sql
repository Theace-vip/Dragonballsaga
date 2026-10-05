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
-- Table structure for table `radar`
--

DROP TABLE IF EXISTS `radar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radar` (
  `id` int(11) NOT NULL,
  `iconId` int(11) DEFAULT 0,
  `rank` tinyint(4) DEFAULT 0,
  `max` int(11) DEFAULT 60,
  `type` int(11) DEFAULT 0,
  `mob_id` int(11) DEFAULT 1,
  `body` varchar(500) DEFAULT '[]',
  `name` varchar(500) DEFAULT '',
  `info` varchar(2000) DEFAULT '',
  `options` varchar(2000) DEFAULT '[]',
  `aura_id` smallint(6) DEFAULT -1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radar`
--

LOCK TABLES `radar` WRITE;
/*!40000 ALTER TABLE `radar` DISABLE KEYS */;
INSERT INTO `radar` VALUES (828,7467,0,120,0,1,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Khủng long','Hai chi trước của Khủng long rất ngắn nên chúng không thể cầm thức ăn được','[{\"id\": 6, \"param\": 1000, \"activeCard\": 0},\n{\"id\": 6, \"param\": 2000, \"activeCard\": 1},\n{\"id\": 6, \"param\": 3000, \"activeCard\": 2}]',1),(829,7468,0,120,0,2,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Lợn lòi','Lợn lòi có sở thích mài răng nanh dưới đất, vô tình tạo ra những rãnh đất để người Namec trồng trọt','[{\"id\":7,\"param\":1000,\"activeCard\":0},{\"id\":7,\"param\":2000,\"activeCard\":1},{\"id\":7,\"param\":3000,\"activeCard\":2}]',-1),(830,7469,0,120,0,3,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Quỷ đất','Bản tính của Qủy đất khá nhút nhát, chúng thường núp vào cây khi gặp người lạ','[{\"id\":0,\"param\":10,\"activeCard\":0},{\"id\":0,\"param\":20,\"activeCard\":1},{\"id\":0,\"param\":30,\"activeCard\":2}]',-1),(831,7470,1,100,0,4,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Khủng long mẹ','Hai chi trước của Khủng long rất ngắn nên chúng không thể cầm thức ăn được','[{\"id\": 6, \"param\": 10000, \"activeCard\": 0},\n{\"id\": 6, \"param\": 20000, \"activeCard\": 1},\n{\"id\": 6, \"param\": 30000, \"activeCard\": 2}]',-1),(832,7471,1,100,0,5,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Lợn lòi mẹ','Lợn lòi có sở thích mài răng nanh dưới đất, vô tình tạo ra những rãnh đất để người Namec trồng trọt','[{\"id\":7,\"param\": 10000,\"activeCard\":0},{\"id\":7,\"param\": 20000,\"activeCard\":1},{\"id\":7,\"param\": 30000,\"activeCard\":2}]',-1),(833,7472,1,100,0,6,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Quỷ đất mẹ',' Bản tính của Qủy đất khá nhút nhát, chúng thường núp vào cây khi gặp người lạ','[{\"id\":0,\"param\":100,\"activeCard\":0},{\"id\":0,\"param\":200,\"activeCard\":1},{\"id\":0,\"param\":300,\"activeCard\":2}]',-1),(834,7473,2,80,0,7,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Thằn lằn bay','Thằn lằn bay dùng cái mỏ dài và cứng để tấn công kẻ thù, đôi khi chúng vẫn dùng để gõ hạt óc chó ăn','[{\"id\":80,\"param\":2,\"activeCard\":0},{\"id\":80,\"param\":3,\"activeCard\":1},{\"id\":80,\"param\":5,\"activeCard\":2}]',-1),(835,7474,2,80,0,8,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Phi long','Dùng tốc độ cực nhanh để tiếp cận và hạ gục mục tiêu, Phi long cũng có sở thích hơi lạ là khi bay luôn mở to miệng để đón gió','[{\"id\":81,\"param\":2,\"activeCard\":0},{\"id\":81,\"param\":3,\"activeCard\":1},{\"id\":81,\"param\":5,\"activeCard\":2}]',-1),(836,7475,2,80,0,9,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Quỷ bay','Sở trường tấn công chớp choáng kẻ thù, nhưng đâu ai ngờ Quỷ bay lại có bệnh sợ độ cao','[{\"id\": 47, \"param\": 50, \"activeCard\": 0}, {\"id\": 47, \"param\": 100, \"activeCard\": 1}, {\"id\": 47, \"param\": 150, \"activeCard\": 2}]',-1),(837,7476,3,60,0,34,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ Lính độc nhãn','Người ta nuôi quân lính dùng trong 1 giờ, Lính độc nhãn thì không dùng được 5 phút','[{\"id\": 6, \"param\": 10000, \"activeCard\": 0},\n{\"id\": 6, \"param\": 20000, \"activeCard\": 1},\n{\"id\": 6, \"param\": 30000, \"activeCard\": 2}]',-1),(838,7477,3,60,0,35,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ lính độc nhãn','Người ta nuôi quân lính dùng trong 1 giờ, Lính độc nhãn thì không dùng được 5 phút','[{\"id\":7,\"param\": 10000,\"activeCard\":0},{\"id\":7,\"param\": 20000,\"activeCard\":1},{\"id\":7,\"param\": 30000,\"activeCard\":2}]',-1),(839,7478,3,60,0,36,'[{\"head\":1, \"body\":1, \"leg\":1, \"bag\":-1}]','Thẻ sói xám','Được lính độc nhãn thuần hóa và cho giữ nhà như cún con, sở thích của chúng là được chơi trò nhặt bóng','[{\"id\":0,\"param\":500,\"activeCard\":0},{\"id\":0,\"param\":700,\"activeCard\":1},{\"id\":0,\"param\":900,\"activeCard\":2}]',-1),(840,7480,4,40,1,1,'[{\"head\":141, \"body\":142, \"leg\":143, \"bag\":-1}]','Thẻ trung úy trắng','Thân hình hơi béo so với tiêu chuẩn chung, luôn đeo chiếc khăn quàng đỏ, thường hay mơ mộng làm thơ','[{\"id\": 94, \"param\": 5, \"activeCard\": 0},\r\n{\"id\": 77, \"param\": 5, \"activeCard\": 1},\r\n{\"id\": 103, \"param\": 5, \"activeCard\": 2}]',-1),(841,7481,4,40,1,1,'[{\"head\":123, \"body\":124, \"leg\":125, \"bag\":-1}]','Thẻ ninja tím','Ninja với nhiều tài năng để trở thành sát thủ, nhưng kỹ thuật ẩn thân lại không có, nên không thể trở thành sát thủ','[{\"id\": 94, \"param\": 5, \"activeCard\": 0},\r\n{\"id\": 77, \"param\": 5, \"activeCard\": 1},\r\n{\"id\": 103, \"param\": 5, \"activeCard\": 2}]',-1),(842,7479,4,40,1,1,'[{\"head\":135, \"body\":136, \"leg\":137, \"bag\":-1}]','Thẻ trung úy xanh lơ','Có siêu năng lực thôi miên nhưng cực kỳ sợ chuột','[{\"id\": 94, \"param\": 5, \"activeCard\": 0},\r\n{\"id\": 77, \"param\": 5, \"activeCard\": 1},\r\n{\"id\": 103, \"param\": 5, \"activeCard\": 2}]',-1),(859,1568,4,40,1,1,'[{\"head\":144, \"body\":145, \"leg\":146, \"bag\":-1}]','Thẻ Độc Nhãn','Đầu não của Red Ribbon. Bị chột một mắt, Lúc nhỏ bị mọi người chê là \'thằng lùn\'','[{\"id\": 94, \"param\": 5, \"activeCard\": 0},\r\n{\"id\": 77, \"param\": 5, \"activeCard\": 1},\r\n{\"id\": 103, \"param\": 5, \"activeCard\": 2}]',-1),(956,8935,4,50,1,1,'[{\"head\":994, \"body\":995, \"leg\":996, \"bag\":-1}]','Thẻ Đội Trưởng Vàng','Đội trưởng vàng là 1 con hổ hình người chắc nịch cơ thể được bao phủ bởi bộ lông vàng, Goku đã đấm hắn ra khỏi máy bay của mình khi đang bay ở giữa không trung.','[{\"id\": 94, \"param\": 5, \"activeCard\": 0},\r\n{\"id\": 77, \"param\": 5, \"activeCard\": 1},\r\n{\"id\": 103, \"param\": 5, \"activeCard\": 2}]',0),(1142,11048,4,75,1,1,'[{\"head\":1236, \"body\":1237, \"leg\":1238, \"bag\":-1}]','Thẻ Rồng Thần Namec','Rồng thần của Namếc do trưởng lão hành tinh này tạo ra. Khác với rồng thiêng của trái đất, Rồng thần Namếc có thân hình màu xanh to lớn, đồ sộ và đáng sợ hơn. Có thể thực hiện 3 điều ước.','[{\"id\": 14, \"param\": 10, \"activeCard\": 0},\n{\"id\": 49, \"param\": 10, \"activeCard\": 1},\n{\"id\": 49, \"param\": 20, \"activeCard\": 2}]',1);
/*!40000 ALTER TABLE `radar` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:13:08
