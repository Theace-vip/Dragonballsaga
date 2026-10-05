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
-- Table structure for table `task_sub_template`
--

DROP TABLE IF EXISTS `task_sub_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_sub_template` (
  `task_main_id` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `max_count` int(11) NOT NULL DEFAULT -1,
  `notify` varchar(255) NOT NULL DEFAULT '',
  `npc_id` int(11) NOT NULL DEFAULT -1,
  `map` int(11) NOT NULL,
  `ducvupro` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ducvupro`) USING BTREE,
  KEY `task_main_id` (`task_main_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_sub_template`
--

LOCK TABLES `task_sub_template` WRITE;
/*!40000 ALTER TABLE `task_sub_template` DISABLE KEYS */;
INSERT INTO `task_sub_template` VALUES (0,'Di chuyển tới mũi tên chỉ dẫn',1,'',-1,-1,1),(0,'Hãy đi đến nhà %2 ở bên phải',1,'',-2,-2,2),(0,'Nói chuyện với %2',1,'',-2,-2,3),(0,'Mở rương đồ',1,'',3,-2,4),(0,'Thu hoạch đậu thần',1,'',4,-2,5),(0,'Báo cáo với %2',1,'',-2,-2,6),(1,'Đánh ngã 5 mộc nhân',5,'Đánh ngã 5 mộc nhân',-1,-1,7),(1,'Báo cáo với %2',1,'Chúc mừng bạn, giờ hãy quay về báo cáo với %2',-2,-2,8),(2,'Thu thập 10 đùi gà',10,'Thu thập 10 đùi gà',-1,-3,9),(2,'Báo cáo với %2',1,'Chúc mừng bạn, giờ hãy quay về báo cáo với %2',-2,-2,10),(3,'Sử dụng tiềm năng',1,'',-1,-1,11),(3,'Đi khám phá vật thể lạ',1,'',-1,-4,12),(3,'Báo cáo với %2',1,'Chúc mừng bạn, giờ hãy quay về báo cáo với %2',-2,-2,13),(4,'Đánh 3 con khủng long mẹ',3,'',-1,-5,14),(4,'Đánh 3 con lợn lòi mẹ',3,'Tiếp theo là Lợn lòi mẹ tại Namếc',-1,-5,15),(4,'Đánh 3 con quỷ đất mẹ',3,'Tiếp theo là Quỷ đất mẹ tại Xayda',-1,-5,16),(4,'Báo cáo với %2',1,'Chúc mừng bạn, giờ hãy quay về báo cáo với %2',-2,-2,17),(5,'Đánh 3 con lợn lòi mẹ',3,'',-1,-5,18),(5,'Đánh 3 con khủng long mẹ',3,'Tiếp theo là Khủng long mẹ tại Trái đất',-1,-5,19),(5,'Đánh 3 con quỷ đất mẹ',3,'Tiếp theo là Quỷ đất mẹ tại Xayda',-1,-5,20),(5,'Báo cáo với %2',1,'Chúc mừng bạn, giờ hãy quay về báo cáo với %2',-2,-2,21),(6,'Đánh 3 con quỷ đất mẹ',3,'',-1,-5,22),(6,'Đánh 3 con khủng long mẹ',3,'Tiếp theo là Khủng long mẹ tại Trái đất',-1,-5,23),(6,'Đánh 3 con lợn lòi mẹ',3,'Tiếp theo là Lợn lòi mẹ tại Namếc',-1,-5,24),(6,'Báo cáo với %2',1,'Chúc mừng bạn, giờ hãy quay về báo cáo với %2',-2,-2,25),(7,'Đạt 1 Tỉ  sức mạnh',1,'',-1,-1,26),(7,'Đánh bại 20 con %9',20,'',-1,-7,27),(7,'Nói chuyện với %8',1,'Hãy mau về làng nói chuyện với %8 nào',-4,-8,28),(7,'Báo cáo với %2',1,'',-2,-2,29),(8,'Đạt 4 Tỉ sức mạnh',1,'Đạt 4 Tỉ sức mạnh',-1,-1,30),(8,'Tìm viên ngọc rồng 7 sao',1,'Tìm viên ngọc rồng 7 sao',-1,-3,31),(8,'Đem ngọc về cho %2',1,'Tìm thấy viên ngọc rồng 7 sao rồi, đem về cho %2 nào!',-2,-2,32),(9,'Nói chuyện với Bò Mộng',1,'',17,47,161),(9,'Đụng độ Tàu Pảy Pảy',1,'',-1,47,162),(9,'Bỏ chạy lên tháp Karin',1,'',-1,46,163),(9,'Nói chuyện với Thần Mèo',1,'',18,46,164),(10,'Đánh thắng Thần mèo',1,'',18,46,165),(10,'Tiêu diệt Tàu Pảy Pảy',1,'',-1,47,166),(10,'Nói chuyện với Bò Mộng',1,'',17,47,167),(10,'Báo cáo với %2',1,'',-2,-2,168),(11,'Tìm %10 tại %11',1,'',-5,-9,169),(11,'Báo cáo với %2',1,'',-2,-2,170),(12,'Vào 1 bang hội',1,'',-1,-1,171),(12,'Báo cáo với %10',1,'',-5,-9,172),(13,'Tiêu diệt Heo rừng',30,'',-1,27,173),(13,'Tiêu diệt Heo da xanh',30,'',-1,31,174),(13,'Tiêu diệt Heo xayda',30,'',-1,36,175),(13,'Báo cáo với %10',1,'',-5,-9,176),(14,'Đạt 20 Tỉ sức mạnh',1,'',-1,-1,177),(14,'Đánh bọn %12 lấy truyện',1,'',-1,-9,178),(14,'Báo cáo với %10',1,'',-5,-9,179),(15,'Đạt 50 Tỉ sức mạnh',1,'',-1,-1,180),(15,'Tiêu diệt Bulon',30,'',-1,30,181),(15,'Tiêu diệt Ukulele',30,'',-1,34,182),(15,'Tiêu diệt Quỷ mập',30,'',-1,38,183),(15,'Báo cáo với %10',-1,'',-5,-9,184),(16,'Thách đấu thắng 10 người',10,'',-1,-1,185),(16,'Báo cáo với %10',1,'',-5,-9,186),(17,'Đạt 150 Tỉ sức mạnh',1,'',-1,-1,187),(17,'Tiêu diệt Akkuman',1,'',-1,19,188),(17,'Tiêu diệt Tamborine',1,'',-1,6,189),(17,'Tiêu diệt Drum',1,'',-1,10,190),(17,'Báo cáo với %10',1,'',-5,-9,191),(18,'Đạt 500 Tỉ sức mạnh',1,'',-1,-1,192),(18,'Thắng vòng 2 đại hội võ thuật',1,'',-1,52,193),(18,'Báo cáo với %10',1,'',-5,-9,194),(19,'Đạt 1500 Tỉ sức mạnh',1,'',-1,-1,195),(19,'Diệt Trung Úy Trắng(Trại Độc Nhãn)',1,'',25,27,196),(19,'Báo cáo với %10',1,'',-5,-9,197),(20,'Đạt 5000 Tỉ sức mạnh',1,'',-1,-1,198),(20,'Tiêu diệt Nappa',700,'',-1,68,199),(20,'Tiêu diệt Soldier',600,'',-1,69,200),(20,'Tiêu diệt Appule',500,'',-1,70,201),(20,'Tiêu diệt Raspberry',400,'',-1,71,202),(20,'Tiêu diệt Thằn lằn xanh',300,'',-1,72,203),(20,'Báo cáo với %10',1,'',-5,-9,204),(21,'Tiêu diệt Kuku',1,'',-1,-1,205),(21,'Tiêu diệt Mập Đầu Đinh',1,'',-1,-1,206),(21,'Tiêu diệt Rambo',1,'',-1,-1,207),(21,'Báo cáo với %10',1,'',-5,-9,208),(22,'Tiêu diệt Số 4',1,'',-1,-1,209),(22,'Tiêu diệt Số 3',1,'',-1,-1,210),(22,'Tiêu diệt Số 1',1,'',-1,-1,211),(22,'Tiêu diệt Tiểu Đội Trưởng',1,'',-1,-1,212),(22,'Báo cáo với %10',1,'',-5,-9,213),(23,'Tiêu diệt Fide cấp 1',1,'',-1,-1,214),(23,'Tiêu diệt Fide cấp 2',1,'',-1,-1,215),(23,'Tiêu diệt Fide cấp 3',1,'',-1,-1,216),(23,'Báo cáo với %10',1,'',-5,-9,217),(24,'Báo cáo với ông %2',1,'',-2,-2,218),(24,'Đi tìm vị khách lạ',1,'',-1,47,219),(24,'Đưa thuốc trợ tim cho Quy Lão',1,'',13,5,220),(24,'Đến tương lai gặp Bunma',1,'',37,102,221),(24,'Diệt Xên con cấp 1',1000,'',-1,92,222),(24,'Báo với Bunma tương lai',1,'',37,102,223),(25,'Đến điểm hẹn tìm Rôbốt Sát Thủ',1,'',-1,93,224),(25,'Tiêu diệt số 2 (Android 19)',5,'',-1,93,225),(25,'Tiêu diệt số 1 (Android 20)',5,'',-1,93,226),(25,'Diệt Xên con cấp 3',2000,'',-1,94,227),(25,'Báo với Bunma tương lai',1,'',37,102,228),(26,'Đến sân sau siêu thị',1,'',-1,104,229),(26,'Tiêu diệt Android 15',1,'',-1,104,230),(26,'Tiêu diệt Android 14',1,'',-1,104,231),(26,'Tiêu diệt Android 13',1,'',-1,104,232),(26,'Báo với Bunma tương lai',1,'',37,102,233),(27,'Đi tìm Píc Póc',1,'',-1,97,234),(27,'Tiêu diệt Píc',1,'',-1,97,235),(27,'Tiêu diệt Póc',1,'',-1,97,236),(27,'Tiêu diệt King Kong',1,'',-1,97,237),(27,'Diệt Xên con cấp 5',3000,'',-1,97,238),(27,'Báo với Bunma tương lai',1,'',37,102,239),(28,'Đến thị trấn Ginder',1,'',-1,100,240),(28,'Tiêu diệt Xên Bọ Hung cấp 1',1,'',-1,100,241),(28,'Tiêu diệt Xên Bọ Hung cấp 2',1,'',-1,100,242),(28,'Tiêu diệt Xên Bọ Hung hoàn thiện',1,'',-1,100,243),(28,'Diệt Xên con cấp 8',5000,'',-1,100,244),(28,'Báo với Bunma tương lai',1,'',37,102,245),(29,'Nâng sức đánh gốc lên 10K',1,'',18,47,246),(29,'Thu thập Capsule kì bí',5000,'',-1,100,247),(29,'Đến võ đài Xên Bọ Hung',1,'',-1,103,248),(29,'Tiêu diệt 7 đứa con của Xên',7,'',-1,103,249),(29,'Tiêu diệt Siêu Bọ Hung',1,'',-1,103,250),(29,'Báo với Bunma tương lai',1,'',37,102,251),(30,'Đi theo Ôsin',1,'',44,52,252),(30,'Hạ vua địa ngục Drabura',1,'',-1,114,253),(30,'Hạ Pui Pui',1,'',-1,115,254),(30,'Hạ Pui Pui lần 2',1,'',-1,116,255),(30,'Hạ Yacôn',1,'',-1,117,256),(30,'Hạ Drabura lần 2',1,'',-1,118,257),(30,'Hạ Mabư',1,'',-1,119,258),(30,'Báo cáo với Ôsin',1,'',44,52,259),(31,'Tìm nhẫn thời không từ Black Goku',1,'Tìm nhẫn thời không từ Black Goku',-1,-1,260),(31,'Sử dụng nhẫn thời không',1,'',-1,-1,261),(31,'Tìm người xayda đang bị thương',1,'',-1,-1,262),(31,'Hạ 5.000 Tobi và Cabira',5000,'',-1,-1,263),(31,'Nói chuyện với Bardock',1,'',-1,-1,264),(31,'Tìm kiếm Berry đi lạc',1,'',-1,-1,265),(31,'Mang Berry về hang cho Bardock',1,'',-1,-1,266),(31,'Tìm 99 thức ăn cho Bardock tại bìa rừng',99,'',-1,-1,267),(31,'Hạ 10.000 Tobi và Cabira',1000,'',-1,-1,268),(31,'Nói chuyện với Bardock',1,'',-1,-1,269),(32,'...',1,'',-1,-1,270);
/*!40000 ALTER TABLE `task_sub_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-05 13:16:47
