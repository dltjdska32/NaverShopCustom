-- MySQL dump 10.13  Distrib 8.0.43, for macos15 (arm64)
--
-- Host: localhost    Database: munova1
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `brand_id` bigint NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(30) DEFAULT NULL,
  `president_name` varchar(10) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `brand_code` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_detail_id` bigint NOT NULL,
  `quantity` int DEFAULT NULL,
  `is_deleted` tinyint DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`cart_id`),
  KEY `fk_cart_user_id_idx` (`user_id`),
  KEY `fk_cart_product_detail_id_idx` (`product_detail_id`),
  CONSTRAINT `fk_cart_product_detail_id` FOREIGN KEY (`product_detail_id`) REFERENCES `product_detail` (`product_detail_id`),
  CONSTRAINT `fk_cart_user_id` FOREIGN KEY (`user_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `chat_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `status` enum('OPENED','CLOSED') DEFAULT NULL,
  `type` enum('GROUP','ONE_ON_ONE') DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `cur_participant` int DEFAULT NULL,
  `max_participant` int DEFAULT NULL,
  `last_message_content` varchar(30) DEFAULT NULL,
  `last_message_time` datetime DEFAULT NULL,
  PRIMARY KEY (`chat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat_member`
--

DROP TABLE IF EXISTS `chat_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_member` (
  `chat_member_id` bigint NOT NULL AUTO_INCREMENT,
  `chat_id` bigint DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `chat_member_type` enum('ADMIN','MEMBER','OWNER') DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`chat_member_id`),
  KEY `fk_chat_member_member_id_idx` (`member_id`),
  KEY `fk_chat_member_product_id_idx` (`product_id`),
  KEY `fk_chat_member_chat_id_idx` (`chat_id`),
  CONSTRAINT `fk_chat_member_chat_id` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`chat_id`),
  CONSTRAINT `fk_chat_member_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `fk_chat_member_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat_tag`
--

DROP TABLE IF EXISTS `chat_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_tag` (
  `chat_tag_id` bigint NOT NULL AUTO_INCREMENT,
  `chat_id` bigint DEFAULT NULL,
  `product_category_id` bigint DEFAULT NULL,
  `category_type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`chat_tag_id`),
  KEY `fk_chat_tag_chat_id_idx` (`chat_id`),
  KEY `fk_chat_tag_product_category_id_idx` (`product_category_id`),
  CONSTRAINT `fk_chat_tag_chat_id` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`chat_id`),
  CONSTRAINT `fk_chat_tag_product_category_id` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`product_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon` (
  `coupon_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `coupon_regist_id` bigint NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`coupon_id`),
  KEY `fk_coupon_user_id_idx` (`user_id`),
  KEY `fk_coupon_promotion_id_idx` (`coupon_regist_id`),
  CONSTRAINT `fk_coupon_promotion_id` FOREIGN KEY (`coupon_regist_id`) REFERENCES `coupon_regist` (`coupon_regist_id`),
  CONSTRAINT `fk_coupon_user_id` FOREIGN KEY (`user_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon_regist`
--

DROP TABLE IF EXISTS `coupon_regist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon_regist` (
  `coupon_regist_id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `coupon_type` enum('PERCENT','fix') DEFAULT NULL,
  `min_payment` bigint DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `admin_id` bigint DEFAULT NULL,
  `regist_at` datetime DEFAULT NULL,
  `expire_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`coupon_regist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(10) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member_click_log`
--

DROP TABLE IF EXISTS `member_click_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_click_log` (
  `member_click_log_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`member_click_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `message_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `chat_id` bigint NOT NULL,
  `content` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `type` enum('TEXT','IMAGE') DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`message_id`),
  KEY `fk_message_user_id_idx` (`user_id`),
  KEY `fk_message_chat_id_idx` (`chat_id`),
  CONSTRAINT `fk_message_chat_id` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`chat_id`),
  CONSTRAINT `fk_message_user_id` FOREIGN KEY (`user_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notification_id` bigint NOT NULL AUTO_INCREMENT,
  `noti_type` enum('ORDER_COMPLETE','COUPON') DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `quantity` int NOT NULL,
  `order_id` bigint NOT NULL,
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint NOT NULL,
  `product_detail_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `status` enum('CANCELED','CONFIRMED','CREATED','DELIVERED','EXCHANGED','EXCHANGE_REQUESTED','PAID','PAYMENT_FAILED','PAYMENT_PENDING','REFUNDED','RETURNED','RETURN_REQUESTED','SHIPPING','SHIPPING_READY') DEFAULT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `FKt4dc2r9nbvbujrljv3e23iibt` (`order_id`),
  KEY `FKgs4vv8bllp8nflsdt06axfuyd` (`product_detail_id`),
  CONSTRAINT `FKgs4vv8bllp8nflsdt06axfuyd` FOREIGN KEY (`product_detail_id`) REFERENCES `product_detail` (`product_detail_id`),
  CONSTRAINT `FKt4dc2r9nbvbujrljv3e23iibt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_product_log`
--

DROP TABLE IF EXISTS `order_product_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_product_log` (
  `quantity` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `member_id` bigint NOT NULL,
  `order_product_log_id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `order_status` enum('CANCELED','CONFIRMED','CREATED','DELIVERED','EXCHANGED','EXCHANGE_REQUESTED','PAID','PAYMENT_FAILED','PAYMENT_PENDING','REFUNDED','RETURNED','RETURN_REQUESTED','SHIPPING','SHIPPING_READY') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`order_product_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `discount_price` bigint DEFAULT NULL,
  `coupon_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `origin_price` bigint DEFAULT NULL,
  `total_price` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `order_num` varchar(255) NOT NULL,
  `user_request` varchar(255) DEFAULT NULL,
  `status` enum('CANCELED','CONFIRMED','CREATED','DELIVERED','EXCHANGED','EXCHANGE_REQUESTED','PAID','PAYMENT_FAILED','PAYMENT_PENDING','REFUNDED','RETURNED','RETURN_REQUESTED','SHIPPING','SHIPPING_READY') DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `UKo6e714ot0hclyvhlcte6vc4mr` (`order_num`),
  KEY `FK1b5nwx42sgj3ls1ltnx1csred` (`user_id`),
  KEY `FKa5ei0aklq6wrjl8vrr7ied3bx` (`coupon_id`),
  CONSTRAINT `FK1b5nwx42sgj3ls1ltnx1csred` FOREIGN KEY (`user_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `FKa5ei0aklq6wrjl8vrr7ied3bx` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `approved_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `requested_at` datetime(6) DEFAULT NULL,
  `total_amount` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `last_transaction_key` varchar(64) DEFAULT NULL,
  `receipt` varchar(255) DEFAULT NULL,
  `toss_payment_key` varchar(255) DEFAULT NULL,
  `method` enum('가상계좌','간편결제','게임문화상품권','계좌이체','도서문화상품권','문화상품권','카드','휴대폰') DEFAULT NULL,
  `payment_object` json DEFAULT NULL,
  `status` enum('ABORTED','CANCELED','DONE','EXPIRED','IN_PROGRESS','PARTIAL_CANCELED','READY','WAITING_FOR_DEPOSIT') DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `FKlouu98csyullos9k25tbpk4va` (`order_id`),
  CONSTRAINT `FKlouu98csyullos9k25tbpk4va` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `info` text,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `brand_id` bigint NOT NULL,
  `product_category_id` bigint DEFAULT NULL,
  `is_deleted` tinyint DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `sales_count` int DEFAULT NULL,
  `view_count` int DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `fk_product_user_id_idx` (`member_id`),
  KEY `fk_brand_id_idx` (`brand_id`),
  KEY `fk_product_category_id_idx` (`product_category_id`),
  CONSTRAINT `fk_product_brand_id` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`brand_id`),
  CONSTRAINT `fk_product_category_id` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`product_category_id`),
  CONSTRAINT `fk_product_user_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `product_category_id` bigint NOT NULL AUTO_INCREMENT,
  `ref_product_category_id` bigint DEFAULT NULL,
  `category_type` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `level` int DEFAULT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_category_id`),
  KEY `fk_product_category_ref_product_categ_idx` (`ref_product_category_id`),
  CONSTRAINT `fk_product_category_ref_product_categ` FOREIGN KEY (`ref_product_category_id`) REFERENCES `product_category` (`product_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_detail`
--

DROP TABLE IF EXISTS `product_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_detail` (
  `product_detail_id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `quantity` int DEFAULT NULL,
  `is_deleted` tinyint DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`product_detail_id`),
  KEY `fk_product_detail_product_id_idx` (`product_id`),
  CONSTRAINT `fk_product_detail_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `product_image_id` bigint NOT NULL AUTO_INCREMENT,
  `image_type` enum('MAIN','SIDE') DEFAULT NULL,
  `origin_name` varchar(100) DEFAULT NULL,
  `saved_name` varchar(100) DEFAULT NULL,
  `is_deleted` tinyint DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `fk_product_image_product_id_idx` (`product_id`),
  CONSTRAINT `fk_product_image_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_like`
--

DROP TABLE IF EXISTS `product_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_like` (
  `product_like_id` bigint NOT NULL AUTO_INCREMENT,
  `create_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  `is_deleted` tinyint DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`product_like_id`),
  KEY `fk_product_like_product_id_idx` (`product_id`),
  KEY `fk_product_like_user_id_idx` (`user_id`),
  CONSTRAINT `fk_product_like_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `fk_product_like_user_id` FOREIGN KEY (`user_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_option`
--

DROP TABLE IF EXISTS `product_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_option` (
  `product_option_id` bigint NOT NULL AUTO_INCREMENT,
  `option_type` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `option_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`product_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_option_mapping`
--

DROP TABLE IF EXISTS `product_option_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_option_mapping` (
  `product_option_mapping_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `option_id` bigint NOT NULL,
  `product_detail_id` bigint NOT NULL,
  PRIMARY KEY (`product_option_mapping_id`),
  KEY `fk_pom_option_id_idx` (`option_id`),
  KEY `fk_pom_product_detail_id_idx` (`product_detail_id`),
  CONSTRAINT `fk_pom_option_id` FOREIGN KEY (`option_id`) REFERENCES `product_option` (`product_option_id`),
  CONSTRAINT `fk_pom_product_detail_id` FOREIGN KEY (`product_detail_id`) REFERENCES `product_detail` (`product_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_purchase_log`
--

DROP TABLE IF EXISTS `product_purchase_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_purchase_log` (
  `product_purchase_log_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_purchase_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_recommend_log`
--

DROP TABLE IF EXISTS `product_recommend_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_recommend_log` (
  `product_recommend_log_id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `product_detail_id` bigint NOT NULL,
  `score` float DEFAULT NULL,
  `version` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`product_recommend_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_search_log`
--

DROP TABLE IF EXISTS `product_search_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_search_log` (
  `product_search_log_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint DEFAULT NULL,
  `search_detail` varchar(50) DEFAULT NULL,
  `search_category_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_search_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `refund`
--

DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund` (
  `refund_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cancel_amount` bigint DEFAULT NULL,
  `cancel_reason` varchar(255) DEFAULT NULL,
  `cancel_status` varchar(255) DEFAULT NULL,
  `canceled_at` datetime(6) DEFAULT NULL,
  `transaction_key` varchar(64) DEFAULT NULL,
  `order_item_id` bigint NOT NULL,
  `payment_id` bigint NOT NULL,
  PRIMARY KEY (`refund_id`),
  UNIQUE KEY `UKjosju2t4x8wmjbhop93b7iv29` (`order_item_id`),
  KEY `FKeoh1147brjy6m009cswl5lty4` (`payment_id`),
  CONSTRAINT `FKeoh1147brjy6m009cswl5lty4` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`),
  CONSTRAINT `FKhi94nmb6h56j863wjvd82b1dk` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`order_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_action_summary`
--

DROP TABLE IF EXISTS `user_action_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_action_summary` (
  `clicked` int DEFAULT NULL,
  `in_cart` bit(1) DEFAULT NULL,
  `liked` bit(1) DEFAULT NULL,
  `purchased` bit(1) DEFAULT NULL,
  `clicked_at` datetime(6) DEFAULT NULL,
  `in_cart_at` datetime(6) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `liked_at` datetime(6) DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `purchased_at` datetime(6) DEFAULT NULL,
  `user_action_summary_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_action_summary_id`),
  UNIQUE KEY `UKmemscggf1rrjm59ddilx5tdws` (`member_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_notification` (
  `user_notification_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `noti_id` bigint NOT NULL,
  `Field` text,
  `is_read` tinyint(1) DEFAULT NULL,
  `received_at` datetime DEFAULT NULL,
  `read_at` datetime DEFAULT NULL,
  PRIMARY KEY (`user_notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_purchase_log`
--

DROP TABLE IF EXISTS `user_purchase_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_purchase_log` (
  `user_purchase_log_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_purchase_log_id`),
  KEY `idx_user_purchase_member_id` (`member_id`),
  KEY `idx_user_purchase_product_id` (`product_id`),
  KEY `idx_user_purchase_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_recommend_log`
--

DROP TABLE IF EXISTS `user_recommend_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_recommend_log` (
  `user_recommend_log_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `cart_id` bigint NOT NULL,
  `score` float DEFAULT NULL,
  `version` varchar(30) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`user_recommend_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_recommendations`
--

DROP TABLE IF EXISTS `user_recommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_recommendations` (
  `score` double NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `member_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `user_recommendations_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_recommendations_id`),
  KEY `FK3xpiwphtqs38h2jy3ym90h0oh` (`member_id`),
  KEY `FKponkthg3fil64dir1tk1x8bm9` (`product_id`),
  CONSTRAINT `FK3xpiwphtqs38h2jy3ym90h0oh` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `FKponkthg3fil64dir1tk1x8bm9` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_search_log`
--

DROP TABLE IF EXISTS `user_search_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_search_log` (
  `user_search_log_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint DEFAULT NULL,
  `search_detail` varchar(50) DEFAULT NULL,
  `search_category_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_search_log_id`),
  KEY `idx_user_search_member_id` (`member_id`),
  KEY `idx_user_search_category_id` (`search_category_id`),
  KEY `idx_user_search_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-31 17:27:45
