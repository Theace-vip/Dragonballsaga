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
-- Table structure for table `task_main_template`
--

DROP TABLE IF EXISTS `task_main_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_main_template` (
  `id` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `detail` varchar(500) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_main_template`
--

LOCK TABLES `task_main_template` WRITE;
/*!40000 ALTER TABLE `task_main_template` DISABLE KEYS */;
INSERT INTO `task_main_template` VALUES (0,'Nhiệm vụ đầu tiên','Chi tiết nhiệm vụ'),(1,'Nhiệm vụ tập luyện','Mộc nhân được đặt nhiều tại %1, ngay trước nhà %2\r\nHãy đánh ngã 5 mộc nhân, \r\nsau đó quay về nhà báo cáo với ông %2\r\nĐể đánh, hãy chạm nhanh 2 lần vào đối tượng\r\nThưởng 500 sức mạnh\r\nThưởng 500 tiềm năng'),(2,'Nhiệm vụ tìm thức ăn','Tìm đến %3, tiêu diệt bọn quái %4 và nhặt về 10 đùi gà\r\nThưởng 1 k sức mạnh\r\nThưởng 1 k tiềm năng\r\nHọc được kỹ năng bay'),(3,'Nhiệm vụ sao băng','Đi khám phá xem vật thể lạ vừa rơi xuống hành tinh\r\nThưởng 2 k sức mạnh\r\nThưởng 2 k tiềm năng'),(4,'Nhiệm vụ thử thách','Khủng long mẹ sống tại Trái Đất\r\nLợn lòi mẹ sống tại Namếc\r\nQuỷ đất mẹ sống tại Xayda\r\nDùng tàu vũ trụ để di chuyển sang hành tinh khác\r\nThưởng 4 k sức mạnh\r\nThưởng 4 k tiềm năng'),(5,'Nhiệm vụ thử thách','Lợn lòi mẹ sống tại Namếc\r\nKhủng long mẹ sống tại Trái Đất\r\nQuỷ đất mẹ sống tại Xayda\r\nDùng tàu vũ trụ để di chuyển sang hành tinh khác\r\nThưởng 4 k sức mạnh\r\nThưởng 4 k tiềm năng'),(6,'Nhiệm vụ thử thách','Quỷ đất mẹ sống tại Xayda\r\nKhủng long mẹ sống tại Trái Đất\r\nLợn lòi mẹ sống tại Namếc\r\nDùng tàu vũ trụ để di chuyển sang hành tinh khác\r\nThưởng 4 k sức mạnh\r\nThưởng 4 k tiềm năng'),(7,'Nhiệm vụ giải cứu','Đến khu vực %13,\r\nHạ 20 con %9\r\nThưởng 8 k sức mạnh\r\nThưởng 8 k tiềm năng'),(8,'Nhiệm vụ tìm ngọc','Ngọc rồng 7 sao đang bị bọn\r\n%14 cướp đi.\r\nĐánh bại chúng để tìm lại.\r\nThưởng 15 k sức mạnh\r\nThưởng 15 k tiềm năng'),(9,'Nhiệm vụ tìm ngọc','Tìm đường đến Karin\r\nnói chuyện với Bò Mộng\r\nkhi đụng độ Tàu Pảy Pảy hãy mau chóng bay lên tháp karin'),(10,'Nhiệm vụ tìm ngọc','Học võ với Thần Mèo\r\nXuống rừng Karin tiêu diệt Tàu Pảy Pảy\r\nđem ngọc về cho ông %2\r\nThưởng 15 k sức mạnh\r\nThưởng 15 k tiềm năng'),(11,'Nhiệm vụ bái sư','Tìm đường tới %11, trò chuyện với %10 và xin làm đệ tử'),(12,'Nhiệm vụ gia nhập bang hội','Báo cáo với %2 khi bang của bạn\r\ncó từ 5 thành viên trở lên\r\nThưởng 20 k sức mạnh\r\nThưởng 20 k tiềm năng'),(13,'Nhiệm vụ bang hội đầu tiên','Cùng phối hợp với 1 người đồng đội lên đường làm nhiệm vụ\nGợi ý:\nHeo rừng xuất hiện tại rừng Bamboo\nHeo da xanh xuất \nhiện tại núi hoa vàng\nHeo xayda xuất hiện tại rừng cọ\nHãy tới trạm tàu vũ trụ để có thể di chuyển qua các map'),(14,'Nhiệm vụ bái sư','Đánh bọn %12 để lấy truyện\r\nDoremon tập 2\r\nThưởng 80 k sức mạnh\r\nThưởng 80 k tiềm năng\r\n'),(15,'Nhiệm vụ bang hội thứ 2','Cùng ít nhất 2 thành viên trong\r\nbang tiêu diệt\r\nBulon tại Đảo Bulông(Trái Đất)\r\nUkulele tại Đông Nam Guru(Namếc)\r\nQuỷ mập tại Bờ Vực Đen(Xayda)\r\nThưởng 150 k sức mạnh\r\nThưởng 150 k tiềm năng\r\n'),(16,'Nhiệm vụ thách đấu','Thách đấu và chiến thằng 10 người\r\nbất kì\r\nThưởng 150 k sức mạnh\r\nThưởng 150 k tiềm năng\r\n'),(17,'Nhiệm vụ tiêu diệt Boss Trùm','Đạt 1.500.000 sức mạnh để trở\r\nthành Siêu nhân\r\nTiêu diệt Akkuman tại Thành\r\nphố Vegeta, tiêu diệt Tamborine tại Đông\r\nKarin, tiêu diệt Drum tại Thung\r\nlũng Namếc\r\nThưởng 200 k sức mạnh\r\nThưởng 200 k tiềm năng\r\n'),(18,'Nhiệm vụ thử thách','Đạt 5 triệu sức mạnh\r\nTham gia và chiến thắng vòng 2 đại hội\r\nvõ thuật tại Vách núi Kakarot\r\nThưởng 500 k sức mạnh\r\nThưởng 500 k tiềm năng'),(19,'Nhiệm vụ cam go','Đạt 15 triệu sức mạnh\r\nVào doanh trại Độc Nhãn tìm diệt\r\nTrung Úy Trắng\r\nThưởng 5 Tr sức mạnh\r\nThưởng 5 Tr tiềm năng'),(20,'Nhiệm vụ bất khả thi','Đạt 50 triệu sức mạnh\r\nTiêu diệt bọn tay sai của Fide tại Xayda\r\nThưởng 50 Tr sức mạnh\r\nThưởng 50 Tr tiềm năng\r\n'),(21,'Nhiệm vụ tìm diệt đệ tử','Tiêu diệt bọn đệ tử Kuku, Mập Đầu Đinh,\r\nRambo của Fide đại ca tại Xayda\r\nCui có thể biết vị trí của chúng, nếu tìm\r\nkhông thấy hãy đến gặp Cui tại thành\r\nphố Vegeta\r\nThưởng 20 Tr sức mạnh\r\nThưởng 20 Tr tiềm năng\r\n'),(22,'Tiểu đội sát thủ','Tiêu diệt Tiểu Đội Sát Thủ do Fide đại\r\nca gọi đến tại Xayda\r\nThưởng 20 Tr sức mạnh\r\nThưởng 20 Tr tiềm năng\r\n'),(23,'Fide đại ca','Fide đã xuất hiện tại núi khỉ vàng\r\nThưởng 20 Tr sức mạnh\r\nThưởng 20 Tr tiềm năng\r\n'),(24,'Chú bé đến từ tương lai','Đến trái đất, rừng bamboo, rừng dương\r\nxỉ, nam Kamê tìm người lạ\r\nĐến đảo rùa đưa thuốc cho Quy Lão\r\nTheo Ca Lích đến tương lai\r\nGiúp họ diệt bọn bọ hung con\r\nThưởng 1 Tr sức mạnh\r\nThưởng 1 Tr tiềm năng\r\n'),(25,'Chạm trán Rôbốt Sát Thủ lần 1','Hãy đến thành phố phía nam\r\nđảo balê hoặc cao nguyên\r\nCùng 2 đồng bang diệt 900 Xên con cấp 3\r\nBáo với Bunma tương lai\r\nThưởng 1 Tr sức mạnh\r\nThưởng 1 Tr tiềm năng\r\n'),(26,'Chạm trán Rôbốt Sát Thủ lần 2','Trở về quá khứ, đến sân sau siêu thị\r\nTiêu diệt bọn Rôbốt sát thủ\r\nBáo với Bunma tương lai\r\nThưởng 1 Tr sức mạnh\r\nThưởng 1 Tr tiềm năng\r\n'),(27,'Chạm trán Rôbốt Sát Thủ lần 3','Đến thành phố, ngọn núi, thung lũng phía Bắc\r\nTiêu diệt bọn Rôbốt sát thủ\r\nCùng 2 đồng bang diệt 800 Xên con cấp 5\r\nBáo với Bunma tương lai\r\nThưởng 1 Tr sức mạnh\r\nThưởng 1 Tr tiềm năng\r\n'),(28,'Chạm trán Xên bọ hung','Đến thị trấn Ginder\r\nTiêu diệt Xên Bọ Hung cấp 1\r\nTiêu diệt Xên Bọ Hung cấp 2\r\nTiêu diệt Xên Bọ Hung hoàn thiện\r\nCùng 2 đồng bang diệt 700 Xên con cấp 8\r\nBáo với Bunma tương lai\r\nThưởng 1 Tr sức mạnh\r\nThưởng 1 Tr tiềm năng'),(29,'Cuộc dạo chơi của Xên','Nâng sức đánh gốc lên 10K, đến gặp thần\r\nmèo\r\nThu thập Capsule kì bí\r\nĐến võ đài Xên Bọ Hung\r\nTiêu diệt 7 đứa con của Xên\r\nTiêu diệt Siêu Bọ Hung\r\nBáo với Bunma tương lai\r\nThưởng 1 Tr sức mạnh\r\nThưởng 1 Tr tiềm năng\r\n'),(30,'Cuộc đối đầu không cân sức','Cẩn thận !!!\r\nNhững vị khách không mời mà tới\r\nthường tỏ ra nguy hiểm\r\n'),(31,'Chạm trán người ngoài hành tinh','Bảo vệ hành tinh thực vật, hạ những kẻ xâm lược.\r\nThưởng 10 Tr sức mạnh\r\nThưởng 10 Tr tiềm năng'),(32,'Vui lòng chờ nhiệm vụ mới','Vui lòng chờ nhiệm vụ mới');
/*!40000 ALTER TABLE `task_main_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 20:13:11
