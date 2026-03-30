-- MySQL dump 10.13  Distrib 9.6.0, for Linux (aarch64)
--
-- Host: 127.0.0.1    Database: ryvue
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_JOB_DETAILS`
--

LOCK TABLES `QRTZ_JOB_DETAILS` WRITE;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `QRTZ_JOB_DETAILS` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='触发器详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_TRIGGERS`
--

LOCK TABLES `QRTZ_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Blob类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_BLOB_TRIGGERS`
--

LOCK TABLES `QRTZ_BLOB_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Cron类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CRON_TRIGGERS`
--

LOCK TABLES `QRTZ_CRON_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='简单触发器的信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPLE_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPLE_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='同步机制的行锁表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPROP_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPROP_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日历信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CALENDARS`
--

LOCK TABLES `QRTZ_CALENDARS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已触发的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_FIRED_TRIGGERS`
--

LOCK TABLES `QRTZ_FIRED_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='暂停的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

LOCK TABLES `QRTZ_PAUSED_TRIGGER_GRPS` WRITE;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度器状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SCHEDULER_STATE`
--

LOCK TABLES `QRTZ_SCHEDULER_STATE` WRITE;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_LOCKS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储的悲观锁信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_LOCKS`
--

LOCK TABLES `QRTZ_LOCKS` WRITE;
/*!40000 ALTER TABLE `QRTZ_LOCKS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_salon`
--

DROP TABLE IF EXISTS `activity_salon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_salon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '活动标题',
  `describe` text NOT NULL COMMENT '活动描述',
  `url` varchar(255) DEFAULT '' COMMENT '活动详情链接',
  `cover_image` varchar(255) DEFAULT '' COMMENT '活动封面图链接',
  `start_time` datetime NOT NULL COMMENT '活动开始时间',
  `end_time` datetime NOT NULL COMMENT '活动结束时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '活动状态: 0-未开始, 1-进行中, 2-已结束',
  `max_participants` int DEFAULT '0' COMMENT '报名人数上限 (0表示不限制)',
  `sort_order` int DEFAULT '0' COMMENT '排序权重 (数值越大越靠前)',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除: 0-正常, 1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主题沙龙活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_salon`
--

LOCK TABLES `activity_salon` WRITE;
/*!40000 ALTER TABLE `activity_salon` DISABLE KEYS */;
INSERT INTO `activity_salon` (`id`, `title`, `describe`, `url`, `cover_image`, `start_time`, `end_time`, `status`, `max_participants`, `sort_order`, `is_deleted`, `create_time`, `update_time`) VALUES (1,'【亲密关系】主题沙龙','读懂爱与被爱，化解冲突，让关系回归温暖与幸福','https://example.com/salon/1','','2026-03-10 14:00:00','2026-03-10 17:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(2,'【亲子成长】主题沙龙','提升亲子关系，用理解与陪伴，滋养彼此共同成长','https://example.com/salon/2','','2026-03-12 09:30:00','2026-03-12 11:30:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(3,'【身心健康】主题沙龙','关照情绪与身体，释放压力，活出平衡轻盈的人生','https://example.com/salon/3','','2026-03-15 19:00:00','2026-03-15 21:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(4,'【事业财富】主题沙龙','经营优质人脉，拓展事业格局，收获顺境与丰盛','https://example.com/salon/4','','2026-03-18 14:00:00','2026-03-18 17:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(5,'【快乐绽放】主题沙龙','放松释放，自在做自己，活出轻松喜悦、光芒绽放','https://example.com/salon/5','','2026-03-20 15:00:00','2026-03-20 18:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(6,'【智慧观影】主题沙龙','借光影悟人生，在故事中照见自己，沉淀成长智慧','https://example.com/salon/6','','2026-03-22 18:30:00','2026-03-22 21:30:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(7,'【知行合一】主题沙龙','明心见性，知行合一，活出清醒、笃定、智慧人生','https://example.com/salon/7','','2026-03-25 14:00:00','2026-03-25 17:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(8,'【幸福之道】主题沙龙','修习健全人格，收获幸福文化传承，把幸福变成能力','https://example.com/salon/8','','2026-03-28 09:00:00','2026-03-28 12:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02'),(9,'【人格解析】主题沙龙','透过案例实训，读懂人格差异，收获予人幸福的能力。','https://example.com/salon/9','','2026-03-30 14:00:00','2026-03-30 17:00:00',0,0,0,0,'2026-03-06 15:49:02','2026-03-06 15:49:02');
/*!40000 ALTER TABLE `activity_salon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_jobs`
--

DROP TABLE IF EXISTS `daily_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `daily_jobs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日结工作主键ID',
  `title` varchar(50) NOT NULL COMMENT '工作标题（如：初中数学日结兼职）',
  `category` tinyint DEFAULT '0' COMMENT '分类：0-家教, 1-助教, 2-派发, 3-其他',
  `salary_day` decimal(10,2) NOT NULL COMMENT '日结薪水（元/日）',
  `work_date` date NOT NULL COMMENT '工作具体日期',
  `work_time` varchar(50) DEFAULT NULL COMMENT '具体时间段（如：14:00-16:00）',
  `location` varchar(100) NOT NULL COMMENT '工作详细地址',
  `district_id` varchar(10) DEFAULT NULL COMMENT '区域区号（如：320115）',
  `contacts` varchar(20) DEFAULT NULL COMMENT '联系人姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `description` text COMMENT '工作具体要求内容',
  `status` tinyint DEFAULT '0' COMMENT '状态：0-招募中, 1-已满员, 2-已结束',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='兼职日结工作表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_jobs`
--

LOCK TABLES `daily_jobs` WRITE;
/*!40000 ALTER TABLE `daily_jobs` DISABLE KEYS */;
INSERT INTO `daily_jobs` (`id`, `title`, `category`, `salary_day`, `work_date`, `work_time`, `location`, `district_id`, `contacts`, `phone`, `description`, `status`, `create_date`, `update_date`) VALUES (1,'高三数学考前突击',0,300.00,'2026-03-10','18:00-20:00','江宁区大学城某小区','320115','张家长','13800001111','急需一名数学优秀的教员，针对导数大题进行讲解。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(2,'少儿编程体验课助教',1,150.00,'2026-03-11','09:00-12:00','秦淮区新街口某机构','320104','李老师','13800002222','协助主讲老师维护课堂秩序，分发器材。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(3,'初中物理实验演示',0,200.00,'2026-03-10','14:00-16:00','鼓楼区北京西路','320106','王女士','13800003333','演示初二物理电路实验，要求操作规范。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(4,'教育展会宣传员',2,120.00,'2026-03-12','08:30-17:30','建邺区国际博览中心','320105','赵经理','13800004444','负责展台宣传单页派发，形象气质佳优先。',1,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(5,'钢琴陪练（日结）',0,180.00,'2026-03-10','19:00-20:00','雨花台区软件大道','320114','孙先生','13800005555','陪同7岁孩子练习钢琴，纠正手型。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(6,'英语口语陪练',0,260.00,'2026-03-13','15:00-17:00','栖霞区仙林大学城','320113','刘同学','13800006666','纯英文对话，练习雅思口语部分。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(7,'书法班代课老师',1,220.00,'2026-03-14','10:00-12:00','玄武区珠江路','320102','周校长','13800007777','要求有硬笔书法基础，代课一小节。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(8,'考研专业课答疑',0,400.00,'2026-03-10','20:00-22:00','线上辅导（南京本地优先）','320100','钱同学','13800008888','解答南大专业课真题，要求研究生在读。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(9,'周末户外写生助教',1,160.00,'2026-03-15','09:00-16:00','南京玄武湖公园','320102','吴老师','13800009999','协助照顾外出写生的小朋友，注意安全。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(10,'小学奥数临时辅导',0,200.00,'2026-03-11','16:30-18:30','六合区龙津路','320116','郑家长','13800000000','针对晚托班学生进行奥数难题点拨。',0,'2026-03-05 11:52:37','2026-03-05 11:52:37'),(11,'大学生日结服务员',3,200.00,'2026-03-13','6:30-12:00','双门楼','320123','吴','138123283473','洗盘子',0,'2026-03-05 12:53:31','2026-03-05 12:53:31');
/*!40000 ALTER TABLE `daily_jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT INTO `gen_table` (`table_id`, `table_name`, `table_comment`, `sub_table_name`, `sub_table_fk_name`, `class_name`, `tpl_category`, `tpl_web_type`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `gen_type`, `gen_path`, `options`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2,'tutors','大学生表/教员表',NULL,NULL,'Tutors','crud','element-ui','com.ruoyi.system','system','tutors','大学生/教员','ruoyi','0','/','{\"parentMenuId\":0}','admin','2026-03-03 06:54:08','','2026-03-04 09:47:29',NULL),(4,'parents','家教订单表',NULL,NULL,'Parents','crud','element-ui','com.ruoyi.system','system','parents','家教订单','ruoyi','0','/','{\"parentMenuId\":0}','admin','2026-03-03 07:14:32','','2026-03-04 10:22:41',NULL),(5,'lectures','课程活动/讲座表',NULL,NULL,'Lectures','crud','','com.ruoyi.system','system','lectures','课程活动/讲座','ruoyi','0','/',NULL,'admin','2026-03-05 09:50:05','',NULL,NULL),(6,'daily_jobs','兼职日结工作表',NULL,NULL,'DailyJobs','crud','','com.ruoyi.system','system','jobs','兼职日结工作','ruoyi','0','/',NULL,'admin','2026-03-05 11:56:54','',NULL,NULL),(7,'sign_in_record','讲座签到记录表',NULL,NULL,'SignInRecord','crud','element-ui','com.ruoyi.system','system','record','讲座签到记录','ruoyi','0','/','{}','admin','2026-03-06 14:21:53','','2026-03-06 14:23:30',NULL),(8,'user_realname_auth','用户实名认证表',NULL,NULL,'UserRealnameAuth','crud','element-ui','com.ruoyi.system','system','auth','用户实名认证','ruoyi','0','/','{}','admin','2026-03-06 14:22:13','','2026-03-06 14:22:58',NULL),(9,'lecture_material','资料中心数据表',NULL,NULL,'LectureMaterial','crud','','com.ruoyi.system','system','material','资料中心数据','ruoyi','0','/',NULL,'admin','2026-03-07 07:24:48','',NULL,NULL),(10,'lecturer_profile','讲师风采表',NULL,NULL,'LecturerProfile','crud','','com.ruoyi.system','system','profile','讲师风采','ruoyi','0','/',NULL,'admin','2026-03-07 07:24:48','',NULL,NULL),(11,'questionnaire','问卷调查配置表',NULL,NULL,'Questionnaire','crud','','com.ruoyi.system','system','questionnaire','问卷调查配置','ruoyi','0','/',NULL,'admin','2026-03-07 07:24:48','',NULL,NULL),(12,'student_enrollment','学籍信息表',NULL,NULL,'StudentEnrollment','crud','','com.ruoyi.system','system','enrollment','学籍信息','ruoyi','0','/',NULL,'admin','2026-03-07 07:24:48','',NULL,NULL),(13,'order','通用交易订单表（包含沙龙和讲座）',NULL,NULL,'Order','crud','','com.ruoyi.system','system','order','通用交易订单（包含沙龙和讲座）','ruoyi','0','/',NULL,'admin','2026-03-07 13:38:54','',NULL,NULL),(14,'salon_info','沙龙活动信息主表',NULL,NULL,'SalonInfo','crud','','com.ruoyi.system','system','info','沙龙活动信息主','ruoyi','0','/',NULL,'admin','2026-03-07 13:38:54','',NULL,NULL);
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT INTO `gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (13,2,'uid','关联user表的主键ID','int','Long','uid','1','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(14,2,'title','职称（如：教师、老师、大学生教员）','varchar(50)','String','title','0','0','1','1','1','1','1','EQ','input','',3,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(15,2,'certificates','证书图片url','varchar(255)','String','certificates','0','0','1','1','1','1','1','EQ','imageUpload','',4,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(16,2,'subjects','可授科目数组（使用索引0，1，2)','json','String','subjects','0','0','1','1','1','1','1','EQ','checkbox','sys_subject',5,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(17,2,'areas','可授区域数组（地区索引id）','json','String','areas','0','0','0','1','1','1','1','EQ',NULL,'',6,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(18,2,'methods','授课方式（网络辅导、线下）','varchar(50)','Long','methods','0','0','0','1','1','1','1','EQ','checkbox','sys_methods',7,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(19,2,'is_certified','审核状态（待审核、已通过、已拒绝）','varchar(20)','Long','isCertified','0','0','0','1','1','1','1','EQ','radio','sys_tutor_status',8,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(20,2,'salary','薪资要求（如：100元/小时）','varchar(50)','String','salary','0','0','0','1','1','1','1','EQ','input','',9,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(21,2,'experience','经历/履历','text','String','experience','0','0','0','1','1','1','1','EQ','textarea','',10,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(22,2,'major','专业','varchar(100)','String','major','0','0','1','1','1','1','1','EQ','input','',11,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(23,2,'school','就读/毕业院校','varchar(100)','String','school','0','0','1','1','1','1','1','EQ','input','',12,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(24,2,'degree','学历枚举：0-本科, 1-硕士, 2-博士','tinyint','Long','degree','0','0','0','1','1','1','1','EQ','radio','sys_degree',13,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(25,2,'create_date','创建时间','datetime','Date','createDate','0','0','0','1','1','1','1','EQ','datetime','',14,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(26,2,'update_date','更新时间','datetime','Date','updateDate','0','0','0','1','1','1','1','EQ','datetime','',15,'admin','2026-03-03 06:54:08','','2026-03-05 10:43:40'),(40,4,'uid','关联user表的主键ID','int','Long','uid','1','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(41,4,'location','地理位置文本（如：XX小区）','varchar(255)','String','location','0','0','0','1','1','1','1','EQ','input','',3,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(42,4,'geo','经纬度位置（如：118.82,32.04）','varchar(100)','String','geo','0','0','0','1','1','1','1','EQ','input','',4,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(43,4,'region','区域（如：玄武区）','varchar(50)','String','region','0','0','0','1','1','1','1','EQ','input','',5,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(44,4,'name','家教单的名称','varchar(150)','String','name','0','0','1','1','1','1','1','LIKE','input','',6,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(45,4,'grade','年级（如：一年级，初一）','varchar(50)','String','grade','0','0','0','1','1','1','1','EQ','radio','sys_class',7,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(46,4,'subject','科目','varchar(50)','String','subject','0','0','0','1','1','1','1','EQ','checkbox','sys_subject',8,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(47,4,'methods','辅导方式（网络辅导、线下）','varchar(50)','Long','methods','0','0','0','1','1','1','1','EQ','radio','sys_methods',9,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(48,4,'requirements','家长家教需求文本','text','String','requirements','0','0','0','1','1','1','1','EQ','textarea','',10,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(49,4,'brief','家长孩子情况简介','text','String','brief','0','0','0','1','1','1','1','EQ','textarea','',11,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(50,4,'create_date','创建时间','datetime','Date','createDate','0','0','0','1','1','1','1','EQ','datetime','',12,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(51,4,'update_date','更新时间','datetime','Date','updateDate','0','0','0','1','1','1','1','EQ','datetime','',13,'admin','2026-03-03 07:14:32','','2026-03-05 09:55:06'),(52,4,'id','教员表主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'','2026-03-04 07:21:56','','2026-03-05 09:55:06'),(53,4,'status','请家教订单状态','int','Long','status','0','0','1','1','1','1','1','EQ','radio','sys_parent_status',14,'','2026-03-04 08:41:56','','2026-03-05 09:55:06'),(54,2,'id','教员表主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'','2026-03-04 09:46:02','','2026-03-05 10:43:40'),(56,4,'day_of_week','每周几到几','varchar(20)','String','dayOfWeek','0','0','1','1','1','1','1','EQ','input','',15,'','2026-03-05 03:04:37','','2026-03-05 09:55:06'),(57,4,'start_time','开始时间','time','Date','startTime','0','0','1','1','1','1','1','EQ','datetime','',16,'','2026-03-05 03:04:37','','2026-03-05 09:55:06'),(58,4,'end_time','结束时间','time','Date','endTime','0','0','1','1','1','1','1','EQ','datetime','',17,'','2026-03-05 03:04:37','','2026-03-05 09:55:06'),(59,2,'real_name','实名认证真实姓名','varchar(10)','String','realName','0','0','0','1','1','1','1','LIKE','input','',16,'','2026-03-05 09:48:11','','2026-03-05 10:43:40'),(60,2,'id_card','身份证号码','varchar(20)','String','idCard','0','0','0','1','1','1','1','EQ','input','',17,'','2026-03-05 09:48:11','','2026-03-05 10:43:40'),(61,5,'id','唯一编码','int','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-05 09:50:05','',NULL),(62,5,'time','开讲时间','datetime','Date','time','0','0','1','1','1','1','1','EQ','datetime','',2,'admin','2026-03-05 09:50:05','',NULL),(63,5,'name','课程名称','varchar(255)','String','name','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2026-03-05 09:50:05','',NULL),(64,5,'speaker','讲师名称','varchar(30)','String','speaker','0','0','0','1','1','1','1','EQ','input','',4,'admin','2026-03-05 09:50:05','',NULL),(65,5,'location','详细地址','varchar(255)','String','location','0','0','0','1','1','1','1','EQ','input','',5,'admin','2026-03-05 09:50:05','',NULL),(66,5,'geo','经纬度信息 (如: 118.80,32.05)','varchar(50)','String','geo','0','0','0','1','1','1','1','EQ','input','',6,'admin','2026-03-05 09:50:05','',NULL),(67,5,'detail','活动详情','text','String','detail','0','0','0','1','1','1','1','EQ','textarea','',7,'admin','2026-03-05 09:50:05','',NULL),(68,5,'create_date','创建时间','datetime','Date','createDate','0','0','0','1','1','1','1','EQ','datetime','',8,'admin','2026-03-05 09:50:05','',NULL),(69,5,'update_date','更新时间','datetime','Date','updateDate','0','0','0','1','1','1','1','EQ','datetime','',9,'admin','2026-03-05 09:50:05','',NULL),(70,2,'self_judge','个人评价','text','String','selfJudge','0','0','0','1','1','1','1','EQ','textarea','',18,'','2026-03-05 10:43:40','',NULL),(71,2,'certificate','证书','text','String','certificate','0','0','0','1','1','1','1','EQ','textarea','',19,'','2026-03-05 10:43:40','',NULL),(72,2,'live','生活区域','varchar(10)','String','live','0','0','0','1','1','1','1','EQ','input','',20,'','2026-03-05 10:43:40','',NULL),(73,2,'work',NULL,'varchar(10)','String','work','0','0','0','1','1','1','1','EQ','input','',21,'','2026-03-05 10:43:40','',NULL),(74,6,'id','日结工作主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(75,6,'title','工作标题（如：初中数学日结兼职）','varchar(50)','String','title','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(76,6,'category','分类：0-家教, 1-助教, 2-派发, 3-其他','tinyint','Long','category','0','0','0','1','1','1','1','EQ','input','',3,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(77,6,'salary_day','日结薪水（元/日）','decimal(10,2)','BigDecimal','salaryDay','0','0','1','1','1','1','1','EQ','input','',4,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(78,6,'work_date','工作具体日期','date','Date','workDate','0','0','1','1','1','1','1','EQ','datetime','',5,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(79,6,'work_time','具体时间段（如：14:00-16:00）','varchar(50)','String','workTime','0','0','0','1','1','1','1','EQ','input','',6,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(80,6,'location','工作详细地址','varchar(100)','String','location','0','0','1','1','1','1','1','EQ','input','',7,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(81,6,'district_id','区域区号（如：320115）','varchar(10)','String','districtId','0','0','0','1','1','1','1','EQ','input','',8,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(82,6,'contacts','联系人姓名','varchar(20)','String','contacts','0','0','0','1','1','1','1','EQ','input','',9,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(83,6,'phone','联系电话','varchar(20)','String','phone','0','0','0','1','1','1','1','EQ','input','',10,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(84,6,'description','工作具体要求内容','text','String','description','0','0','0','1','1','1','1','EQ','textarea','',11,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(85,6,'status','状态：0-招募中, 1-已满员, 2-已结束','tinyint','Long','status','0','0','0','1','1','1','1','EQ','radio','',12,'admin','2026-03-05 11:56:54','','2026-03-05 12:14:20'),(88,6,'create_date',NULL,'datetime','Date','createDate','0','0','0','1','1','1','1','EQ','datetime','',13,'','2026-03-05 12:14:20','',NULL),(89,6,'update_date',NULL,'datetime','Date','updateDate','0','0','0','1','1','1','1','EQ','datetime','',14,'','2026-03-05 12:14:20','',NULL),(90,7,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-06 14:21:53','','2026-03-06 14:23:30'),(91,7,'uid','用户ID，关联sys_user表的user_id','bigint','Long','uid','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-06 14:21:53','','2026-03-06 14:23:30'),(92,7,'lecture_id','讲座ID，关联lectures表的id','bigint','Long','lectureId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2026-03-06 14:21:53','','2026-03-06 14:23:30'),(93,7,'sign_time','签到时间','datetime','Date','signTime','0','0','0','1','1','1','1','EQ','datetime','',4,'admin','2026-03-06 14:21:53','','2026-03-06 14:23:30'),(94,7,'sign_status','签到状态：1-正常，2-迟到，3-代签/异常','tinyint','Long','signStatus','0','0','0','1','1','1','1','EQ','radio','sys_tutor_status',5,'admin','2026-03-06 14:21:53','','2026-03-06 14:23:30'),(95,7,'device_info','签到设备或IP(可选，用于防作弊)','varchar(255)','String','deviceInfo','0','0','0','1','1','1','1','EQ','input','',6,'admin','2026-03-06 14:21:53','','2026-03-06 14:23:30'),(96,8,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(97,8,'uid','用户ID，关联sys_user表','bigint','Long','uid','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(98,8,'real_name','真实姓名','varchar(50)','String','realName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(99,8,'id_card','身份证号码','varchar(18)','String','idCard','0','0','1','1','1','1','1','EQ','input','',4,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(100,8,'auth_status','认证状态：0-待审核，1-已通过，2-已驳回','tinyint','Long','authStatus','0','0','0','1','1','1','1','EQ','radio','sys_tutor_status',5,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(101,8,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(102,8,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',7,'admin','2026-03-06 14:22:13','','2026-03-06 14:22:58'),(103,9,'id','主键ID','bigint','Long','id','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-07 07:24:48','',NULL),(104,9,'lecture_id','关联课程ID','bigint','Long','lectureId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-07 07:24:48','',NULL),(105,9,'url','资料/PDF文件OSS链接','varchar(512)','String','url','0','0','1','1','1','1','1','EQ','textarea','',3,'admin','2026-03-07 07:24:48','',NULL),(106,9,'status','状态: 0-隐藏, 1-展示 (由后端接口业务调整)','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',4,'admin','2026-03-07 07:24:48','',NULL),(107,9,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2026-03-07 07:24:48','',NULL),(108,9,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2026-03-07 07:24:48','',NULL),(109,9,'is_deleted','逻辑删除标识','tinyint','Long','isDeleted','0','0','0','1','1','1','1','EQ','input','',7,'admin','2026-03-07 07:24:48','',NULL),(110,10,'id','主键ID','bigint','Long','id','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-07 07:24:48','',NULL),(111,10,'name','讲师姓名','varchar(64)','String','name','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2026-03-07 07:24:48','',NULL),(112,10,'intro','讲师简介/职位介绍','text','String','intro','0','0','0','1','1','1','1','EQ','textarea','',3,'admin','2026-03-07 07:24:48','',NULL),(113,10,'avatar_url','头像URL','varchar(512)','String','avatarUrl','0','0','0','1','1','1','1','EQ','textarea','',4,'admin','2026-03-07 07:24:48','',NULL),(114,10,'poster_url','宣传海报URL','varchar(512)','String','posterUrl','0','0','0','1','1','1','1','EQ','textarea','',5,'admin','2026-03-07 07:24:48','',NULL),(115,10,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2026-03-07 07:24:48','',NULL),(116,10,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',7,'admin','2026-03-07 07:24:48','',NULL),(117,10,'is_deleted','逻辑删除标识','tinyint','Long','isDeleted','0','0','0','1','1','1','1','EQ','input','',8,'admin','2026-03-07 07:24:48','',NULL),(118,11,'id','主键ID (雪花算法)','bigint','Long','id','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-07 07:24:48','',NULL),(119,11,'lecture_id','关联课程ID','bigint','Long','lectureId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-07 07:24:48','',NULL),(120,11,'url','问卷星链接URL','varchar(512)','String','url','0','0','1','1','1','1','1','EQ','textarea','',3,'admin','2026-03-07 07:24:48','',NULL),(121,11,'status','状态: 0-失效, 1-有效 (配合Java定时任务控制开课7天内有效)','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',4,'admin','2026-03-07 07:24:48','',NULL),(122,11,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2026-03-07 07:24:48','',NULL),(123,11,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2026-03-07 07:24:48','',NULL),(124,11,'is_deleted','逻辑删除标识: 0-未删除, 1-已删除','tinyint','Long','isDeleted','0','0','0','1','1','1','1','EQ','input','',7,'admin','2026-03-07 07:24:48','',NULL),(125,12,'id','主键ID','bigint','Long','id','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-07 07:24:48','',NULL),(126,12,'uid','用户ID (关联小程序学员)','bigint','Long','uid','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-07 07:24:48','',NULL),(127,12,'lecture_id','关联课程ID','bigint','Long','lectureId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2026-03-07 07:24:48','',NULL),(128,12,'total','总学籍数/报名数','int','Long','total','0','0','1','1','1','1','1','EQ','input','',4,'admin','2026-03-07 07:24:48','',NULL),(129,12,'remain','剩余可用学籍数 (支持线下核销或赠送扣减)','int','Long','remain','0','0','1','1','1','1','1','EQ','input','',5,'admin','2026-03-07 07:24:48','',NULL),(130,12,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2026-03-07 07:24:48','',NULL),(131,12,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',7,'admin','2026-03-07 07:24:48','',NULL),(132,12,'is_deleted','逻辑删除标识','tinyint','Long','isDeleted','0','0','0','1','1','1','1','EQ','input','',8,'admin','2026-03-07 07:24:48','',NULL),(133,13,'id','内部订单ID，主键','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-07 13:38:54','',NULL),(134,13,'order_no','外部展示及支付网关使用的订单号（需唯一）','varchar(64)','String','orderNo','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-07 13:38:54','',NULL),(135,13,'user_id','购买用户的唯一标识','bigint','Long','userId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2026-03-07 13:38:54','',NULL),(136,13,'order_type','业务类型：1-沙龙(salon订单)，2-讲座(lecture订单)','tinyint','Long','orderType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2026-03-07 13:38:54','',NULL),(137,13,'salon_id','关联的沙龙ID（当order_type=1时有值）','bigint','Long','salonId','0','0','0','1','1','1','1','EQ','input','',5,'admin','2026-03-07 13:38:54','',NULL),(138,13,'lecture_id','关联的讲座ID（当order_type=2时有值）','bigint','Long','lectureId','0','0','0','1','1','1','1','EQ','input','',6,'admin','2026-03-07 13:38:54','',NULL),(139,13,'pay_amount','实际支付金额','decimal(10,2)','BigDecimal','payAmount','0','0','1','1','1','1','1','EQ','input','',7,'admin','2026-03-07 13:38:54','',NULL),(140,13,'pay_method','支付方式，如：wechat_pay, alipay, offline','varchar(32)','String','payMethod','0','0','0','1','1','1','1','EQ','input','',8,'admin','2026-03-07 13:38:54','',NULL),(141,13,'purpose','主要的用途/备注，如：报名听课、赞助商','varchar(128)','String','purpose','0','0','0','1','1','1','1','EQ','input','',9,'admin','2026-03-07 13:38:54','',NULL),(142,13,'pay_status','支付状态：0-待支付，1-已支付，2-已退款，3-已取消','tinyint','Long','payStatus','0','0','1','1','1','1','1','EQ','radio','',10,'admin','2026-03-07 13:38:54','',NULL),(143,13,'pay_time','实际完成支付的时间','datetime','Date','payTime','0','0','0','1','1','1','1','EQ','datetime','',11,'admin','2026-03-07 13:38:54','',NULL),(144,13,'create_time','订单创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',12,'admin','2026-03-07 13:38:54','',NULL),(145,13,'update_time','订单更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',13,'admin','2026-03-07 13:38:54','',NULL),(146,14,'id','沙龙ID，主键','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2026-03-07 13:38:55','',NULL),(147,14,'title','沙龙主标题，如：组局思维','varchar(128)','String','title','0','0','1','1','1','1','1','EQ','input','',2,'admin','2026-03-07 13:38:55','',NULL),(148,14,'subtitle','副标题或标签，如：沙龙/社群/KOL','varchar(128)','String','subtitle','0','0','0','1','1','1','1','EQ','input','',3,'admin','2026-03-07 13:38:55','',NULL),(149,14,'cover_img','封面图的URL','varchar(512)','String','coverImg','0','0','0','1','1','1','1','EQ','textarea','',4,'admin','2026-03-07 13:38:55','',NULL),(150,14,'description','详情页内容（富文本HTML或JSON）','text','String','description','0','0','0','1','1','1','1','EQ','textarea','',5,'admin','2026-03-07 13:38:55','',NULL),(151,14,'original_price','原价/划线价，如：768.00','decimal(10,2)','BigDecimal','originalPrice','0','0','0','1','1','1','1','EQ','input','',6,'admin','2026-03-07 13:38:55','',NULL),(152,14,'current_price','实际售卖价，如：128.00','decimal(10,2)','BigDecimal','currentPrice','0','0','1','1','1','1','1','EQ','input','',7,'admin','2026-03-07 13:38:55','',NULL),(153,14,'start_time','沙龙举办/开始时间','datetime','Date','startTime','0','0','0','1','1','1','1','EQ','datetime','',8,'admin','2026-03-07 13:38:55','',NULL),(154,14,'sales_volume','已售数量（用于页面展示）','int','Long','salesVolume','0','0','1','1','1','1','1','EQ','input','',9,'admin','2026-03-07 13:38:55','',NULL),(155,14,'status','状态：0-下架草稿，1-上架售卖中','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',10,'admin','2026-03-07 13:38:55','',NULL),(156,14,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2026-03-07 13:38:55','',NULL),(157,14,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',12,'admin','2026-03-07 13:38:55','',NULL);
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecture_material`
--

DROP TABLE IF EXISTS `lecture_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_material` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `lecture_id` bigint NOT NULL COMMENT '关联课程ID',
  `url` varchar(512) NOT NULL COMMENT '资料/PDF文件OSS链接',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0-隐藏, 1-展示 (由后端接口业务调整)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`),
  KEY `idx_lecture_id` (`lecture_id`) COMMENT '加速课程资料拉取'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资料中心数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecture_material`
--

LOCK TABLES `lecture_material` WRITE;
/*!40000 ALTER TABLE `lecture_material` DISABLE KEYS */;
INSERT INTO `lecture_material` (`id`, `lecture_id`, `url`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (1898700000000020,9901,'https://cdn.example.com/pdf/幸福解码思维导图.pdf',1,'2026-03-07 07:23:32','2026-03-07 07:23:32',0),(1898700000000021,9901,'https://cdn.example.com/pdf/家庭关系核心讲义.pdf',1,'2026-03-07 07:23:32','2026-03-07 07:23:32',0),(1898700000000022,9902,'https://cdn.example.com/pdf/往期沙龙回顾.pdf',0,'2026-03-07 07:23:32','2026-03-07 07:23:32',0);
/*!40000 ALTER TABLE `lecture_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturer_profile`
--

DROP TABLE IF EXISTS `lecturer_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturer_profile` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '讲师姓名',
  `intro` text COMMENT '讲师简介/职位介绍',
  `avatar_url` varchar(512) DEFAULT NULL COMMENT '头像URL',
  `poster_url` varchar(512) DEFAULT NULL COMMENT '宣传海报URL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='讲师风采表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturer_profile`
--

LOCK TABLES `lecturer_profile` WRITE;
/*!40000 ALTER TABLE `lecturer_profile` DISABLE KEYS */;
INSERT INTO `lecturer_profile` (`id`, `name`, `intro`, `avatar_url`, `poster_url`, `create_time`, `update_time`, `is_deleted`) VALUES (1898700000000001,'李老师','资深心理学导师，拥有10年家庭关系咨询经验。','https://cdn.example.com/avatar/li.png','<image>https://coral79.github.io/frankenmotion/images/Frankenstein_icon.png</image>资深心理学导师，拥有10年家庭关系咨询经验。','2026-03-07 07:23:32','2026-03-07 08:43:43',0),(1898700000000002,'王教授','亲子教育专家，主讲青少年潜能开发沙龙。','https://cdn.example.com/avatar/wang.png','<image>https://cdn.example.com/poster/wang_poster.jpg</image>','2026-03-07 07:23:32','2026-03-07 08:24:12',0);
/*!40000 ALTER TABLE `lecturer_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lectures`
--

DROP TABLE IF EXISTS `lectures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lectures` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一编码',
  `time` datetime NOT NULL COMMENT '开讲时间',
  `end_date` datetime DEFAULT NULL COMMENT '会议结束日期',
  `name` varchar(255) NOT NULL COMMENT '课程名称',
  `speaker` varchar(100) DEFAULT NULL COMMENT '讲师名称',
  `location` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `geo` varchar(100) DEFAULT NULL COMMENT '经纬度信息 (如: 118.80,32.05)',
  `detail` text COMMENT '活动详情',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cover` varchar(30) DEFAULT NULL COMMENT '活动封面图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程活动/讲座表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lectures`
--

LOCK TABLES `lectures` WRITE;
/*!40000 ALTER TABLE `lectures` DISABLE KEYS */;
INSERT INTO `lectures` (`id`, `time`, `end_date`, `name`, `speaker`, `location`, `geo`, `detail`, `create_date`, `update_date`, `cover`) VALUES (6,'2025-10-15 14:00:00','2025-10-17 23:00:00','中考数学压轴题专项拆解','1898700000000001','南京市玄武区图书馆一楼报告厅','118.800,32.050','针对历年中考数学最后两道大题的专项拆解与训练，适合初三学生。','2026-03-11 13:03:57','2026-03-11 13:25:48',NULL),(7,'2025-12-20 09:30:00','2025-12-20 23:30:00','青少年心理健康与家庭教育','1898700000000001','南京市鼓楼区文化活动中心','118.770,32.060','探讨青春期孩子的心理变化、叛逆期表现及家长的科学应对策略。','2026-03-11 13:03:57','2026-03-11 13:04:53',NULL),(8,'2026-02-28 15:00:00','2026-03-01 23:00:00','高效记忆法与英语词汇突破','1898700000000002','南京市建邺区青年创客空间','118.740,32.010','分享实用的记忆宫殿法与词根词缀记忆法，帮助学生快速扩大英语词汇量。','2026-03-11 13:03:57','2026-03-11 13:04:53',NULL),(9,'2026-03-08 19:00:00','2026-03-12 23:00:00','新高考志愿填报权威指南','1898700000000002','南京市秦淮区教育局大礼堂','118.790,32.025','全面解读最新高考政策与选科要求，指导高三学生及家长科学填报志愿，规避滑档风险。','2026-03-11 13:03:57','2026-03-11 13:25:52',NULL),(10,'2026-04-10 10:00:00','2026-04-14 23:00:00','少儿编程启蒙与人工智能','1898700000000001','南京市雨花台区软件谷科技展厅','118.760,31.980','面向小学生的 Scratch 编程基础体验课，结合简单的 AI 互动，激发科技创新兴趣。','2026-03-11 13:03:57','2026-03-11 13:25:48',NULL);
/*!40000 ALTER TABLE `lectures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parents`
--

DROP TABLE IF EXISTS `parents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parents` (
  `id` bigint NOT NULL COMMENT '教员表主键ID',
  `uid` int NOT NULL COMMENT '关联user表的主键ID',
  `location` varchar(255) DEFAULT NULL COMMENT '地理位置文本（如：XX小区）',
  `geo` varchar(100) DEFAULT NULL COMMENT '经纬度位置（如：118.82,32.04）',
  `region` varchar(50) DEFAULT NULL COMMENT '区域（如：玄武区）',
  `name` varchar(150) DEFAULT NULL COMMENT '家教单的名称',
  `grade` varchar(50) DEFAULT NULL COMMENT '年级（如：一年级，初一）',
  `subject` varchar(50) DEFAULT NULL COMMENT '科目',
  `methods` int DEFAULT NULL COMMENT '辅导方式（网络辅导、线下）',
  `requirements` text COMMENT '家长家教需求文本',
  `brief` text COMMENT '家长孩子情况简介',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int NOT NULL DEFAULT '0' COMMENT '请家教订单状态',
  `day_of_week` varchar(20) NOT NULL COMMENT '每周几到几',
  `start_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='家教订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parents`
--

LOCK TABLES `parents` WRITE;
/*!40000 ALTER TABLE `parents` DISABLE KEYS */;
INSERT INTO `parents` (`id`, `uid`, `location`, `geo`, `region`, `name`, `grade`, `subject`, `methods`, `requirements`, `brief`, `create_date`, `update_date`, `status`, `day_of_week`, `start_time`, `end_time`) VALUES (1,2001,'南京市玄武区中山门大街XX小区','118.823,32.045','320104','急招初二数学家教','8','8',0,'要求老师有耐心，能针对基础薄弱点进行讲解','孩子理科偏科，数学不及格，性格有点内向','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(2,2002,'南京市鼓楼区汉口路XX苑','118.780,32.055','320111','高一物理周末拔高','10','10',1,'希望是985高校理工科专业学生，逻辑严密','男生，基础不错，需要攻克压轴题','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(3,2003,'南京市建邺区江东中路XX号','118.730,32.001','320115','小学三年级英语口语陪练','3','7',0,'发音标准，能带孩子读英语绘本并互动','女孩，比较活泼好动，需要老师有趣味性','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(4,2004,'南京市秦淮区夫子庙街道XX大院','118.790,32.022','320116','初三理科周末冲刺','9','2',2,'需要有中考辅导经验的老师，能帮忙做学习计划','面临中考，学习压力大，需要心理疏导和方法指导','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(5,2005,'无明确地址','0,0','320115','高二化学线上答疑','11','11',1,'晚上9点到10点在线解答作业难题','孩子住校，只有晚上能用平板，主要问错题','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(6,2006,'南京市雨花台区软件大道XX小区','118.765,31.980','320114','五年级奥数启蒙','5','9',0,'有过竞赛经验优先，不要死记硬背公式','数学成绩优异，想提前接触拓展内容','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(7,2007,'南京市江宁区龙眠大道XX号','118.815,31.912','320113','初一语文阅读与写作辅导','7','6',0,'文科类专业，擅长引导孩子阅读和积累素材','作文经常跑题，阅读理解失分严重','2026-03-04 07:20:49','2026-03-11 02:03:25',0,'0','00:00:00','00:00:00'),(8,2008,'海外/异地','0,0','320104','高三英语听力口语突击','12','7',1,'英语专八或有留学背景，发音纯正','准备走中外合作办学，需要强化听说能力','2026-03-04 07:20:49','2026-03-11 02:03:25',0,'0','00:00:00','00:00:00'),(9,2009,'南京市浦口区珠江镇XX小区','118.630,32.060','320106','一年级拼音认字辅导','1','6',0,'幼师或者小学教育专业优先，亲和力强','刚上小学，跟不上进度，拼音总是搞混','2026-03-04 07:20:49','2026-03-11 02:03:25',0,'0','00:00:00','00:00:00'),(10,2010,'南京市六合区雄州街道XX苑','118.840,32.340','320123','初二物理入门辅导','8','10',0,'需要老师自带一些小实验道具，激发兴趣','刚接触物理，觉得比较抽象，需要培养物理思维','2026-03-04 07:20:49','2026-03-11 02:02:48',0,'0','00:00:00','00:00:00'),(3193680098412800,1,'江苏省南京市建邺区青奥北路与青奥路交叉口西北方向248米左右','118.703387,31.992043','320105','高中英语辅导','0','1',0,'阿芙蓉如风','阿多少分','2026-03-06 02:19:23','2026-03-11 02:03:25',0,'1,2,6','08:00:00','10:00:00');
/*!40000 ALTER TABLE `parents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire` (
  `id` bigint NOT NULL COMMENT '主键ID (雪花算法)',
  `lecture_id` bigint NOT NULL COMMENT '关联课程ID',
  `url` varchar(512) NOT NULL COMMENT '问卷星链接URL',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0-失效, 1-有效 (配合Java定时任务控制开课7天内有效)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除标识: 0-未删除, 1-已删除',
  `topic` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_lecture_status` (`lecture_id`,`status`) COMMENT '联合索引加速根据课程查询有效问卷'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问卷调查配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire`
--

LOCK TABLES `questionnaire` WRITE;
/*!40000 ALTER TABLE `questionnaire` DISABLE KEYS */;
INSERT INTO `questionnaire` (`id`, `lecture_id`, `url`, `status`, `create_time`, `update_time`, `is_deleted`, `topic`) VALUES (1898700000000010,6,'https://v.wjx.cn/vm/h9jERG2.aspx# ',1,'2026-03-07 07:23:32','2026-03-13 13:42:32',0,'学生问卷调查'),(1898700000000011,7,'https://www.wjx.cn/vm/exAmple2.aspx',0,'2026-03-07 07:23:32','2026-03-13 12:33:23',0,NULL);
/*!40000 ALTER TABLE `questionnaire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salon_info`
--

DROP TABLE IF EXISTS `salon_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salon_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '沙龙ID，主键',
  `title` varchar(128) NOT NULL COMMENT '沙龙主标题，如：组局思维',
  `subtitle` varchar(128) DEFAULT NULL COMMENT '副标题或标签，如：沙龙/社群/KOL',
  `cover_img` varchar(512) DEFAULT NULL COMMENT '封面图的URL',
  `description` text COMMENT '详情页内容（富文本HTML或JSON）',
  `original_price` decimal(10,2) DEFAULT '0.00' COMMENT '原价/划线价，如：768.00',
  `current_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际售卖价，如：128.00',
  `start_time` datetime DEFAULT NULL COMMENT '沙龙举办/开始时间',
  `sales_volume` int NOT NULL DEFAULT '0' COMMENT '已售数量（用于页面展示）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架草稿，1-上架售卖中',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='沙龙活动信息主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salon_info`
--

LOCK TABLES `salon_info` WRITE;
/*!40000 ALTER TABLE `salon_info` DISABLE KEYS */;
INSERT INTO `salon_info` (`id`, `title`, `subtitle`, `cover_img`, `description`, `original_price`, `current_price`, `start_time`, `sales_volume`, `status`, `create_time`, `update_time`) VALUES (1,'组局思维','沙龙/社群/KOL','https://demo.com/img1.jpg',NULL,768.00,128.00,'2026-03-06 14:00:00',9,1,'2026-03-07 13:58:15','2026-03-07 13:58:15'),(2,'岛上圆桌派·第33期','深度链接/头脑风暴','https://demo.com/img2.jpg',NULL,588.00,58.00,'2026-03-10 19:30:00',42,1,'2026-03-07 13:58:15','2026-03-07 13:58:15'),(3,'春节不打烊：10天让你朋友圈会说话','个人品牌建设','https://demo.com/img3.jpg',NULL,599.00,198.00,'2026-02-10 10:00:00',105,3,'2026-03-07 13:58:15','2026-03-07 13:58:15'),(4,'手碟音乐工作坊','你的第一支音乐MV','https://demo.com/img4.jpg',NULL,698.00,128.00,'2026-03-15 14:00:00',15,1,'2026-03-07 13:58:15','2026-03-07 13:58:15'),(5,'AIGC赋能职场效率沙龙','效率工具实战','https://demo.com/img5.jpg',NULL,299.00,99.00,'2026-03-20 14:00:00',0,0,'2026-03-07 13:58:15','2026-03-07 13:58:15');
/*!40000 ALTER TABLE `salon_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sign_in_record`
--

DROP TABLE IF EXISTS `sign_in_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sign_in_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint NOT NULL COMMENT '用户ID，关联sys_user表的user_id',
  `record_type` tinyint NOT NULL DEFAULT '1' COMMENT '记录类型: 1-讲座签到, 2-沙龙报名',
  `lecture_id` bigint DEFAULT NULL COMMENT '讲座ID，关联lectures表（record_type=1时有值）',
  `salon_id` bigint DEFAULT NULL COMMENT '沙龙ID，关联salon_info表（record_type=2时有值）',
  `sign_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到/报名时间',
  `sign_status` tinyint DEFAULT '0' COMMENT '状态: 0-已报名/待签到, 1-已签到/正常, 2-迟到, 3-已取消',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名（沙龙报名时填写）',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话（沙龙报名时填写）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `device_info` varchar(255) DEFAULT NULL COMMENT '签到设备或IP（可选，用于防作弊）',
  PRIMARY KEY (`id`),
  KEY `idx_uid_type` (`uid`,`record_type`) COMMENT '按用户和类型查询',
  KEY `idx_lecture_id` (`lecture_id`) COMMENT '按讲座查询签到',
  KEY `idx_salon_id` (`salon_id`) COMMENT '按沙龙查询报名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='签到与报名记录表（讲座签到/沙龙报名）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sign_in_record`
--

LOCK TABLES `sign_in_record` WRITE;
/*!40000 ALTER TABLE `sign_in_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `sign_in_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_enrollment`
--

DROP TABLE IF EXISTS `student_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_enrollment` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `uid` bigint NOT NULL COMMENT '用户ID (关联小程序学员)',
  `lecture_id` bigint NOT NULL COMMENT '关联课程ID',
  `total` int NOT NULL DEFAULT '0' COMMENT '总学籍数/报名数',
  `remain` int NOT NULL DEFAULT '0' COMMENT '剩余可用学籍数 (支持线下核销或赠送扣减)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_lecture` (`uid`,`lecture_id`) COMMENT '联合唯一约束：同一学员同一课程只保留一条汇总记录',
  KEY `idx_uid` (`uid`) COMMENT '加速查询"我的学籍"'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学籍信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_enrollment`
--

LOCK TABLES `student_enrollment` WRITE;
/*!40000 ALTER TABLE `student_enrollment` DISABLE KEYS */;
INSERT INTO `student_enrollment` (`id`, `uid`, `lecture_id`, `total`, `remain`, `create_time`, `update_time`, `is_deleted`) VALUES (1,1,1,5,5,'2026-03-07 12:19:53','2026-03-07 12:19:53',0),(1898700000000030,8801,9901,5,4,'2026-03-07 07:23:32','2026-03-07 07:23:32',0),(1898700000000031,8802,9901,1,1,'2026-03-07 07:23:32','2026-03-07 07:23:32',0),(1898700000000032,8801,9902,2,0,'2026-03-07 07:23:32','2026-03-07 07:23:32',0);
/*!40000 ALTER TABLE `student_enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2026-03-01 15:28:46','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2026-03-01 15:28:46','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2026-03-01 15:28:46','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2026-03-01 15:28:46','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2026-03-01 15:28:46','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2026-03-01 15:28:46','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (100,0,'0','全国总经理',0,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','admin','2026-03-07 05:47:42'),(101,100,'0,100','广东省负责人',1,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','admin','2026-03-07 05:48:31'),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-01 15:28:45','',NULL),(200,100,'0,100','江苏省负责人',0,NULL,NULL,NULL,'0','0','admin','2026-03-07 05:48:57','',NULL),(201,200,'0,100,200','南京市负责人',0,NULL,NULL,NULL,'0','0','admin','2026-03-07 05:49:14','',NULL),(202,200,'0,100,200','无锡市负责人',1,NULL,NULL,NULL,'0','0','admin','2026-03-07 05:49:44','',NULL),(203,201,'0,100,200,201','中层干部1',0,NULL,NULL,NULL,'0','2','admin','2026-03-07 05:50:03','admin','2026-03-07 05:50:30'),(204,201,'0,100,200,201','中层干部2',1,NULL,NULL,NULL,'0','2','admin','2026-03-07 05:50:39','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2026-03-01 15:28:46','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2026-03-01 15:28:46','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2026-03-01 15:28:46','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2026-03-01 15:28:46','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2026-03-01 15:28:46','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2026-03-01 15:28:46','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2026-03-01 15:28:46','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2026-03-01 15:28:46','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2026-03-01 15:28:46','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2026-03-01 15:28:46','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2026-03-01 15:28:46','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2026-03-01 15:28:46','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'关闭状态'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2026-03-01 15:28:46','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2026-03-01 15:28:46','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2026-03-01 15:28:46','',NULL,'修改操作'),(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'删除操作'),(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2026-03-01 15:28:46','',NULL,'授权操作'),(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2026-03-01 15:28:46','',NULL,'导出操作'),(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2026-03-01 15:28:46','',NULL,'导入操作'),(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'强退操作'),(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2026-03-01 15:28:46','',NULL,'生成操作'),(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2026-03-01 15:28:46','',NULL,'正常状态'),(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2026-03-01 15:28:46','',NULL,'停用状态'),(30,1,'幼儿学前','0','sys_subject','','','N','0','admin','2026-03-03 06:53:17','',NULL,'幼儿学前'),(31,2,'小学全科','1','sys_subject','','','N','0','admin','2026-03-03 06:53:17','',NULL,'小学全科'),(32,3,'初中理科','2','sys_subject','','','N','0','admin','2026-03-03 06:53:17','',NULL,'初中理科'),(33,4,'初中文科','3','sys_subject','','','N','0','admin','2026-03-03 06:53:17','',NULL,'初中文科'),(34,5,'高中理科','4','sys_subject','','','N','0','admin','2026-03-03 06:53:17','',NULL,'高中理科'),(35,6,'高中文科','5','sys_subject','','','N','0','admin','2026-03-03 06:53:17','',NULL,'高中文科'),(36,7,'语文','6','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'语文'),(37,8,'英语','7','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'英语'),(38,9,'数学','8','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'数学'),(39,10,'奥数','9','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'奥数'),(40,11,'物理','10','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'物理'),(41,12,'化学','11','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'化学'),(42,13,'生物','12','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'生物'),(43,14,'历史','13','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'历史'),(44,15,'地理','14','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'地理'),(45,16,'政治','15','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'政治'),(46,17,'钢琴','16','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'钢琴'),(47,18,'小提琴','17','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'小提琴'),(48,19,'古筝','18','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'古筝'),(49,20,'跳绳','19','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'跳绳'),(50,21,'篮球','20','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'篮球'),(51,22,'游泳','21','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'游泳'),(52,23,'围棋','22','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'围棋'),(53,24,'书法','23','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'书法'),(54,25,'美术','24','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'美术'),(55,26,'英语口语','25','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'英语口语'),(56,27,'四级','26','sys_subject','','','N','0','admin','2026-03-03 06:53:24','',NULL,'四级'),(60,1,'线下辅导','0','sys_methods','','primary','N','0','admin','2026-03-03 07:00:12','admin','2026-03-03 08:34:34','线下上门辅导'),(61,2,'网络辅导','1','sys_methods','','primary','N','0','admin','2026-03-03 07:00:12','admin','2026-03-03 08:34:45','线上远程辅导'),(62,3,'线上线下均可','2','sys_methods','','primary','N','0','admin','2026-03-03 07:00:12','admin','2026-03-03 08:34:56','可协商决定'),(70,1,'学前/幼儿园','0','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'学前阶段'),(71,2,'一年级','1','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'小学一年级'),(72,3,'二年级','2','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'小学二年级'),(73,4,'三年级','3','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'小学三年级'),(74,5,'四年级','4','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'小学四年级'),(75,6,'五年级','5','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'小学五年级'),(76,7,'六年级','6','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'小学六年级'),(77,8,'初一','7','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'初中一年级'),(78,9,'初二','8','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'初中二年级'),(79,10,'初三','9','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'初中三年级'),(80,11,'高一','10','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'高中一年级'),(81,12,'高二','11','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'高中二年级'),(82,13,'高三','12','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'高中三年级'),(83,14,'成人/其他','13','sys_class','','','N','0','admin','2026-03-03 07:00:12','',NULL,'成人教育或其他'),(90,1,'大专','0','sys_degree','','','N','0','admin','2026-03-03 07:00:12','',NULL,'专科学历'),(91,2,'本科','1','sys_degree','','','N','0','admin','2026-03-03 07:00:12','',NULL,'本科学历'),(92,3,'硕士','2','sys_degree','','','N','0','admin','2026-03-03 07:00:12','',NULL,'硕士研究生'),(93,4,'博士','3','sys_degree','','','N','0','admin','2026-03-03 07:00:12','',NULL,'博士研究生'),(94,5,'其他','4','sys_degree','','','N','0','admin','2026-03-03 07:00:12','',NULL,'其他学历'),(100,0,'招募中','0','sys_parent_status',NULL,'primary','N','0','admin','2026-03-04 09:42:58','',NULL,NULL),(101,0,'已完成','1','sys_parent_status',NULL,'success','N','0','admin','2026-03-04 09:43:17','',NULL,NULL),(102,0,'已取消','2','sys_parent_status',NULL,'default','N','0','admin','2026-03-04 09:43:36','',NULL,NULL),(103,0,'未提交','0','sys_tutor_status',NULL,'default','N','0','admin','2026-03-04 09:44:18','',NULL,NULL),(104,0,'审核中','1','sys_tutor_status',NULL,'info','N','0','admin','2026-03-04 09:44:42','admin','2026-03-05 08:37:39',NULL),(105,0,'审核通过','2','sys_tutor_status',NULL,'success','N','0','admin','2026-03-04 09:45:03','',NULL,NULL),(106,0,'审核不通过','3','sys_tutor_status',NULL,'danger','N','0','admin','2026-03-04 09:45:24','admin','2026-03-04 09:45:31',NULL),(107,0,'招募中','0','sys_day_work_status',NULL,'primary','N','0','admin','2026-03-05 12:19:32','',NULL,NULL),(108,0,'已满员','2','sys_day_work_status',NULL,'warning','N','0','admin','2026-03-05 12:19:51','',NULL,NULL),(109,0,'已结束','1','sys_day_work_status',NULL,'info','N','0','admin','2026-03-05 12:20:23','admin','2026-03-05 12:20:29',NULL),(110,0,'服务员','0','sys_daily_category',NULL,'default','N','0','admin','2026-03-05 12:24:19','',NULL,NULL),(111,3,'其他','10','sys_daily_category',NULL,'default','N','0','admin','2026-03-05 12:24:42','admin','2026-03-07 02:02:11',NULL),(112,0,'志愿者','1','sys_daily_category',NULL,'default','N','0','admin','2026-03-06 15:31:14','',NULL,NULL),(113,0,'群演','2','sys_daily_category',NULL,'default','N','0','admin','2026-03-06 15:31:37','',NULL,NULL),(114,0,'促销员','3','sys_daily_category',NULL,'default','N','0','admin','2026-03-06 15:31:50','',NULL,NULL),(115,0,'话务员','5','sys_daily_category',NULL,'default','N','0','admin','2026-03-06 15:32:01','',NULL,NULL),(116,0,'地推','4','sys_daily_category',NULL,'default','N','0','admin','2026-03-06 15:32:15','',NULL,NULL),(117,0,'活动充场','6','sys_daily_category',NULL,'default','N','0','admin','2026-03-06 15:32:29','',NULL,NULL);
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'用户性别','sys_user_sex','0','admin','2026-03-01 15:28:46','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2026-03-01 15:28:46','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2026-03-01 15:28:46','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2026-03-01 15:28:46','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2026-03-01 15:28:46','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2026-03-01 15:28:46','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2026-03-01 15:28:46','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2026-03-01 15:28:46','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2026-03-01 15:28:46','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2026-03-01 15:28:46','',NULL,'登录状态列表'),(11,'科目','sys_subject','0','admin','2026-03-03 06:53:17','',NULL,'科目列表'),(12,'辅导方式','sys_methods','0','admin','2026-03-03 07:01:03','',NULL,'辅导方式'),(13,'年级','sys_class','0','admin','2026-03-03 07:01:03','',NULL,'年级'),(14,'学历','sys_degree','0','admin','2026-03-03 07:01:03','',NULL,'学历'),(100,'订单状态','sys_parent_status','0','admin','2026-03-04 09:41:59','',NULL,'家长订单状态列表'),(101,'审核状态','sys_tutor_status','0','admin','2026-03-04 09:42:28','',NULL,'学生审核状态列表'),(102,'日结兼职状态','sys_day_work_status','0','admin','2026-03-05 12:19:11','',NULL,NULL),(103,'日结工作分类','sys_daily_category','0','admin','2026-03-05 12:23:44','',NULL,NULL);
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2026-03-01 15:28:46','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2026-03-01 15:28:46','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2026-03-01 15:28:46','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` (`info_id`, `user_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `msg`, `login_time`) VALUES (100,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-02 13:07:02'),(101,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 03:34:21'),(102,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-03 03:36:35'),(103,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码已失效','2026-03-03 03:43:44'),(104,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 03:43:49'),(105,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 03:55:43'),(106,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码错误','2026-03-03 03:56:15'),(107,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 03:56:23'),(108,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-03 03:56:37'),(109,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-03 06:24:10'),(110,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 08:02:58'),(111,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-03 08:33:25'),(112,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 12:56:52'),(113,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码错误','2026-03-03 13:34:27'),(114,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-03 13:34:32'),(115,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-04 03:41:20'),(116,'admin','192.168.31.194','内网IP','Apple WebKit','Mac OS X (iPhone)','0','登录成功','2026-03-04 08:33:28'),(117,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-05 03:04:21'),(118,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 06:03:20'),(119,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码错误','2026-03-07 06:05:17'),(120,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 06:05:21'),(121,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码已失效','2026-03-07 06:05:31'),(122,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 06:05:35'),(123,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 06:07:09'),(124,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 06:07:18'),(125,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 06:08:13'),(126,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码错误','2026-03-07 06:10:36'),(127,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 06:10:40'),(128,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 06:43:35'),(129,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','退出成功','2026-03-07 10:47:52'),(130,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-07 10:48:14'),(131,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:04:03'),(132,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码错误','2026-03-07 12:04:19'),(133,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:04:25'),(134,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:11:54'),(135,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:12:03'),(136,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:13:18'),(137,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:13:24'),(138,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:16:15'),(139,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','1','验证码错误','2026-03-07 12:16:22'),(140,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:16:28'),(141,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:17:09'),(142,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:17:17'),(143,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:31:31'),(144,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:37:11'),(145,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-07 12:38:43'),(146,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-07 12:38:57'),(147,'admin','127.0.0.1','内网IP','Chrome 14','Mac OS X','0','登录成功','2026-03-10 04:02:18'),(148,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-10 04:03:16'),(149,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-11 02:55:34'),(150,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-11 03:29:15'),(151,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-11 03:33:37'),(152,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','登录成功','2026-03-16 14:25:18'),(153,'admin','127.0.0.1','内网IP','Mobile Safari','Mac OS X (iPhone)','0','退出成功','2026-03-16 14:47:24');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2073 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'系统管理',0,1,'system',NULL,'','',1,0,'M','0','0','','system','admin','2026-03-01 15:28:45','',NULL,'系统管理目录'),(2,'系统监控',0,2,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2026-03-01 15:28:45','',NULL,'系统监控目录'),(3,'系统工具',0,3,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2026-03-01 15:28:45','',NULL,'系统工具目录'),(4,'若依官网',0,4,'http://ruoyi.vip',NULL,'','',0,0,'M','0','0','','guide','admin','2026-03-01 15:28:45','',NULL,'若依官网地址'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2026-03-01 15:28:45','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2026-03-01 15:28:45','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2026-03-01 15:28:45','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2026-03-01 15:28:45','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2026-03-01 15:28:45','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2026-03-01 15:28:45','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2026-03-01 15:28:45','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2026-03-01 15:28:45','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2026-03-01 15:28:45','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2026-03-01 15:28:45','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2026-03-01 15:28:45','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2026-03-01 15:28:45','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2026-03-01 15:28:45','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2026-03-01 15:28:45','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2026-03-01 15:28:45','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2026-03-01 15:28:45','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2026-03-01 15:28:45','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2026-03-01 15:28:45','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2026-03-01 15:28:45','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2026-03-01 15:28:45','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2026-03-01 15:28:45','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2026-03-01 15:28:45','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2026-03-01 15:28:45','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2026-03-01 15:28:45','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2026-03-01 15:28:45','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2026-03-01 15:28:45','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2026-03-01 15:28:45','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2026-03-01 15:28:45','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2026-03-01 15:28:45','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2026-03-01 15:28:45','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2026-03-01 15:28:45','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2026-03-01 15:28:45','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2026-03-01 15:28:45','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2026-03-01 15:28:45','',NULL,''),(2000,'业务管理',0,2,'bussiness',NULL,NULL,'bussiness',1,0,'M','0','0',NULL,'example','admin','2026-03-02 13:18:30','',NULL,''),(2001,'家教订单',2000,1,'parents','system/parents/index',NULL,'',1,0,'C','0','0','system:parents:list','documentation','admin','2026-03-03 07:07:00','admin','2026-03-03 07:19:19','家教订单菜单'),(2002,'家教订单查询',2001,1,'#','',NULL,'',1,0,'F','0','0','system:parents:query','#','admin','2026-03-03 07:07:00','',NULL,''),(2003,'家教订单新增',2001,2,'#','',NULL,'',1,0,'F','0','0','system:parents:add','#','admin','2026-03-03 07:07:00','',NULL,''),(2004,'家教订单修改',2001,3,'#','',NULL,'',1,0,'F','0','0','system:parents:edit','#','admin','2026-03-03 07:07:00','',NULL,''),(2005,'家教订单删除',2001,4,'#','',NULL,'',1,0,'F','0','0','system:parents:remove','#','admin','2026-03-03 07:07:00','',NULL,''),(2006,'家教订单导出',2001,5,'#','',NULL,'',1,0,'F','0','0','system:parents:export','#','admin','2026-03-03 07:07:00','',NULL,''),(2007,'大学生/教员',2000,1,'tutors','system/tutors/index',NULL,'',1,0,'C','0','0','system:tutors:list','people','admin','2026-03-03 07:29:08','admin','2026-03-03 07:35:19','大学生/教员菜单'),(2008,'大学生/教员查询',2007,1,'#','',NULL,'',1,0,'F','0','0','system:tutors:query','#','admin','2026-03-03 07:29:09','',NULL,''),(2009,'大学生/教员新增',2007,2,'#','',NULL,'',1,0,'F','0','0','system:tutors:add','#','admin','2026-03-03 07:29:09','',NULL,''),(2010,'大学生/教员修改',2007,3,'#','',NULL,'',1,0,'F','0','0','system:tutors:edit','#','admin','2026-03-03 07:29:09','',NULL,''),(2011,'大学生/教员删除',2007,4,'#','',NULL,'',1,0,'F','0','0','system:tutors:remove','#','admin','2026-03-03 07:29:09','',NULL,''),(2012,'大学生/教员导出',2007,5,'#','',NULL,'',1,0,'F','0','0','system:tutors:export','#','admin','2026-03-03 07:29:09','',NULL,''),(2013,'课程活动/讲座',2000,1,'lectures','system/lectures/index',NULL,'',1,0,'C','0','0','system:lectures:list','post','admin','2026-03-05 09:51:23','admin','2026-03-05 12:47:15','课程活动/讲座菜单'),(2014,'课程活动/讲座查询',2013,1,'#','',NULL,'',1,0,'F','0','0','system:lectures:query','#','admin','2026-03-05 09:51:23','',NULL,''),(2015,'课程活动/讲座新增',2013,2,'#','',NULL,'',1,0,'F','0','0','system:lectures:add','#','admin','2026-03-05 09:51:23','',NULL,''),(2016,'课程活动/讲座修改',2013,3,'#','',NULL,'',1,0,'F','0','0','system:lectures:edit','#','admin','2026-03-05 09:51:23','',NULL,''),(2017,'课程活动/讲座删除',2013,4,'#','',NULL,'',1,0,'F','0','0','system:lectures:remove','#','admin','2026-03-05 09:51:23','',NULL,''),(2018,'课程活动/讲座导出',2013,5,'#','',NULL,'',1,0,'F','0','0','system:lectures:export','#','admin','2026-03-05 09:51:23','',NULL,''),(2019,'兼职日结工作',2000,1,'jobs','system/jobs/index',NULL,'',1,0,'C','0','0','system:jobs:list','dashboard','admin','2026-03-05 11:57:18','admin','2026-03-05 12:46:36','兼职日结工作菜单'),(2020,'兼职日结工作查询',2019,1,'#','',NULL,'',1,0,'F','0','0','system:jobs:query','#','admin','2026-03-05 11:57:18','',NULL,''),(2021,'兼职日结工作新增',2019,2,'#','',NULL,'',1,0,'F','0','0','system:jobs:add','#','admin','2026-03-05 11:57:18','',NULL,''),(2022,'兼职日结工作修改',2019,3,'#','',NULL,'',1,0,'F','0','0','system:jobs:edit','#','admin','2026-03-05 11:57:18','',NULL,''),(2023,'兼职日结工作删除',2019,4,'#','',NULL,'',1,0,'F','0','0','system:jobs:remove','#','admin','2026-03-05 11:57:18','',NULL,''),(2024,'兼职日结工作导出',2019,5,'#','',NULL,'',1,0,'F','0','0','system:jobs:export','#','admin','2026-03-05 11:57:18','',NULL,''),(2025,'用户实名认证',2000,1,'auth','system/auth/index',NULL,'',1,0,'C','0','0','system:auth:list','#','admin','2026-03-06 14:24:06','admin','2026-03-07 07:29:54','用户实名认证菜单'),(2026,'用户实名认证查询',2025,1,'#','',NULL,'',1,0,'F','0','0','system:auth:query','#','admin','2026-03-06 14:24:07','',NULL,''),(2027,'用户实名认证新增',2025,2,'#','',NULL,'',1,0,'F','0','0','system:auth:add','#','admin','2026-03-06 14:24:07','',NULL,''),(2028,'用户实名认证修改',2025,3,'#','',NULL,'',1,0,'F','0','0','system:auth:edit','#','admin','2026-03-06 14:24:07','',NULL,''),(2029,'用户实名认证删除',2025,4,'#','',NULL,'',1,0,'F','0','0','system:auth:remove','#','admin','2026-03-06 14:24:07','',NULL,''),(2030,'用户实名认证导出',2025,5,'#','',NULL,'',1,0,'F','0','0','system:auth:export','#','admin','2026-03-06 14:24:07','',NULL,''),(2031,'讲座签到记录',2000,1,'record','system/record/index',NULL,'',1,0,'C','0','0','system:record:list','#','admin','2026-03-06 14:24:17','admin','2026-03-07 07:30:00','讲座签到记录菜单'),(2032,'讲座签到记录查询',2031,1,'#','',NULL,'',1,0,'F','0','0','system:record:query','#','admin','2026-03-06 14:24:18','',NULL,''),(2033,'讲座签到记录新增',2031,2,'#','',NULL,'',1,0,'F','0','0','system:record:add','#','admin','2026-03-06 14:24:18','',NULL,''),(2034,'讲座签到记录修改',2031,3,'#','',NULL,'',1,0,'F','0','0','system:record:edit','#','admin','2026-03-06 14:24:18','',NULL,''),(2035,'讲座签到记录删除',2031,4,'#','',NULL,'',1,0,'F','0','0','system:record:remove','#','admin','2026-03-06 14:24:18','',NULL,''),(2036,'讲座签到记录导出',2031,5,'#','',NULL,'',1,0,'F','0','0','system:record:export','#','admin','2026-03-06 14:24:18','',NULL,''),(2037,'学籍信息',2000,1,'enrollment','system/enrollment/index',NULL,'',1,0,'C','0','0','system:enrollment:list','#','admin','2026-03-07 07:25:30','admin','2026-03-07 07:30:06','学籍信息菜单'),(2038,'学籍信息查询',2037,1,'#','',NULL,'',1,0,'F','0','0','system:enrollment:query','#','admin','2026-03-07 07:25:30','',NULL,''),(2039,'学籍信息新增',2037,2,'#','',NULL,'',1,0,'F','0','0','system:enrollment:add','#','admin','2026-03-07 07:25:30','',NULL,''),(2040,'学籍信息修改',2037,3,'#','',NULL,'',1,0,'F','0','0','system:enrollment:edit','#','admin','2026-03-07 07:25:30','',NULL,''),(2041,'学籍信息删除',2037,4,'#','',NULL,'',1,0,'F','0','0','system:enrollment:remove','#','admin','2026-03-07 07:25:30','',NULL,''),(2042,'学籍信息导出',2037,5,'#','',NULL,'',1,0,'F','0','0','system:enrollment:export','#','admin','2026-03-07 07:25:30','',NULL,''),(2043,'问卷调查配置',2000,1,'questionnaire','system/questionnaire/index',NULL,'',1,0,'C','0','0','system:questionnaire:list','#','admin','2026-03-07 07:26:07','admin','2026-03-07 07:30:11','问卷调查配置菜单'),(2044,'问卷调查配置查询',2043,1,'#','',NULL,'',1,0,'F','0','0','system:questionnaire:query','#','admin','2026-03-07 07:26:08','',NULL,''),(2045,'问卷调查配置新增',2043,2,'#','',NULL,'',1,0,'F','0','0','system:questionnaire:add','#','admin','2026-03-07 07:26:08','',NULL,''),(2046,'问卷调查配置修改',2043,3,'#','',NULL,'',1,0,'F','0','0','system:questionnaire:edit','#','admin','2026-03-07 07:26:08','',NULL,''),(2047,'问卷调查配置删除',2043,4,'#','',NULL,'',1,0,'F','0','0','system:questionnaire:remove','#','admin','2026-03-07 07:26:08','',NULL,''),(2048,'问卷调查配置导出',2043,5,'#','',NULL,'',1,0,'F','0','0','system:questionnaire:export','#','admin','2026-03-07 07:26:08','',NULL,''),(2049,'讲师风采',2000,1,'profile','system/profile/index',NULL,'',1,0,'C','0','0','system:profile:list','#','admin','2026-03-07 07:26:15','admin','2026-03-07 07:30:16','讲师风采菜单'),(2050,'讲师风采查询',2049,1,'#','',NULL,'',1,0,'F','0','0','system:profile:query','#','admin','2026-03-07 07:26:15','',NULL,''),(2051,'讲师风采新增',2049,2,'#','',NULL,'',1,0,'F','0','0','system:profile:add','#','admin','2026-03-07 07:26:15','',NULL,''),(2052,'讲师风采修改',2049,3,'#','',NULL,'',1,0,'F','0','0','system:profile:edit','#','admin','2026-03-07 07:26:15','',NULL,''),(2053,'讲师风采删除',2049,4,'#','',NULL,'',1,0,'F','0','0','system:profile:remove','#','admin','2026-03-07 07:26:15','',NULL,''),(2054,'讲师风采导出',2049,5,'#','',NULL,'',1,0,'F','0','0','system:profile:export','#','admin','2026-03-07 07:26:15','',NULL,''),(2055,'资料中心数据',2000,1,'material','system/material/index',NULL,'',1,0,'C','0','0','system:material:list','#','admin','2026-03-07 07:26:24','admin','2026-03-07 07:30:20','资料中心数据菜单'),(2056,'资料中心数据查询',2055,1,'#','',NULL,'',1,0,'F','0','0','system:material:query','#','admin','2026-03-07 07:26:24','',NULL,''),(2057,'资料中心数据新增',2055,2,'#','',NULL,'',1,0,'F','0','0','system:material:add','#','admin','2026-03-07 07:26:24','',NULL,''),(2058,'资料中心数据修改',2055,3,'#','',NULL,'',1,0,'F','0','0','system:material:edit','#','admin','2026-03-07 07:26:24','',NULL,''),(2059,'资料中心数据删除',2055,4,'#','',NULL,'',1,0,'F','0','0','system:material:remove','#','admin','2026-03-07 07:26:24','',NULL,''),(2060,'资料中心数据导出',2055,5,'#','',NULL,'',1,0,'F','0','0','system:material:export','#','admin','2026-03-07 07:26:24','',NULL,''),(2061,'沙龙活动信息主',3,1,'info','system/info/index',NULL,'',1,0,'C','0','0','system:info:list','#','admin','2026-03-07 13:59:49','',NULL,'沙龙活动信息主菜单'),(2062,'沙龙活动信息主查询',2061,1,'#','',NULL,'',1,0,'F','0','0','system:info:query','#','admin','2026-03-07 13:59:49','',NULL,''),(2063,'沙龙活动信息主新增',2061,2,'#','',NULL,'',1,0,'F','0','0','system:info:add','#','admin','2026-03-07 13:59:49','',NULL,''),(2064,'沙龙活动信息主修改',2061,3,'#','',NULL,'',1,0,'F','0','0','system:info:edit','#','admin','2026-03-07 13:59:50','',NULL,''),(2065,'沙龙活动信息主删除',2061,4,'#','',NULL,'',1,0,'F','0','0','system:info:remove','#','admin','2026-03-07 13:59:50','',NULL,''),(2066,'沙龙活动信息主导出',2061,5,'#','',NULL,'',1,0,'F','0','0','system:info:export','#','admin','2026-03-07 13:59:50','',NULL,''),(2067,'通用交易订单（包含沙龙和讲座）',3,1,'order','system/order/index',NULL,'',1,0,'C','0','0','system:order:list','#','admin','2026-03-07 14:00:01','',NULL,'通用交易订单（包含沙龙和讲座）菜单'),(2068,'通用交易订单（包含沙龙和讲座）查询',2067,1,'#','',NULL,'',1,0,'F','0','0','system:order:query','#','admin','2026-03-07 14:00:02','',NULL,''),(2069,'通用交易订单（包含沙龙和讲座）新增',2067,2,'#','',NULL,'',1,0,'F','0','0','system:order:add','#','admin','2026-03-07 14:00:02','',NULL,''),(2070,'通用交易订单（包含沙龙和讲座）修改',2067,3,'#','',NULL,'',1,0,'F','0','0','system:order:edit','#','admin','2026-03-07 14:00:02','',NULL,''),(2071,'通用交易订单（包含沙龙和讲座）删除',2067,4,'#','',NULL,'',1,0,'F','0','0','system:order:remove','#','admin','2026-03-07 14:00:02','',NULL,''),(2072,'通用交易订单（包含沙龙和讲座）导出',2067,5,'#','',NULL,'',1,0,'F','0','0','system:order:export','#','admin','2026-03-07 14:00:02','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'温馨提醒：2018-07-01 若依新版本发布啦','2',_binary '新版本内容','0','admin','2026-03-01 15:28:46','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1',_binary '维护内容','0','admin','2026-03-01 15:28:46','',NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`, `cost_time`) VALUES (100,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"icon\":\"example\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"业务管理\",\"menuType\":\"M\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"bussiness\",\"routeName\":\"bussiness\",\"status\":\"0\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-02 13:18:30',32),(101,'用户头像',2,'com.ruoyi.web.controller.system.SysProfileController.avatar()','POST',1,'admin','研发部门','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2026/03/03/epR068O4HOp87b0aa240458cec4c58aae402c5771160_20260303113538A001.png\",\"code\":200}',0,NULL,'2026-03-03 03:35:38',44),(102,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE `tutors` (\\n  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT \'表主键\',\\n  `uid` INT NOT NULL COMMENT \'关联user对应的id\',\\n  `title` VARCHAR(50) DEFAULT NULL COMMENT \'职称（如：教师、老师、大学生教员）\',\\n  `certificates` VARCHAR(255) DEFAULT NULL COMMENT \'证书图片url\',\\n  `subjects` JSON DEFAULT NULL COMMENT \'可授科目数组（使用索引0，1，2)\',\\n  `areas` JSON DEFAULT NULL COMMENT \'可授区域数组（地区索引id）\',\\n  `methods` VARCHAR(50) DEFAULT NULL COMMENT \'授课方式（网络辅导、线下）\',\\n  `is_certified` VARCHAR(20) DEFAULT \'待审核\' COMMENT \'审核状态（待审核、已通过、已拒绝）\',\\n  `salary` VARCHAR(50) DEFAULT NULL COMMENT \'薪资要求（如：100元/小时）\',\\n  `experience` TEXT COMMENT \'经历/履历\',\\n  `major` VARCHAR(100) DEFAULT NULL COMMENT \'专业\',\\n  `school` VARCHAR(100) DEFAULT NULL COMMENT \'就读/毕业院校\',\\n  `degree` TINYINT DEFAULT 0 COMMENT \'学历枚举：0-本科, 1-硕士, 2-博士\',\\n  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\'\\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=\'大学生表/教员表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2026-03-03 06:37:01',10),(103,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"tutors,parents\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 06:54:08',169),(104,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":1,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":2,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":3,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"geo\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Region\",\"columnComment\":\"区域（如：玄武区）\",\"columnId\":4,\"columnName\":\"region\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequire','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:02:58',49),(105,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"tutors\",\"className\":\"Tutors\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":13,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"职称（如：教师、老师、大学生教员）\",\"columnId\":14,\"columnName\":\"title\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Certificates\",\"columnComment\":\"证书图片url\",\"columnId\":15,\"columnName\":\"certificates\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"certificates\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Subjects\",\"columnComment\":\"可授科目数组（使用索引0，1，2)\",\"columnId\":16,\"columnName\":\"subjects\",\"columnType\":\"json\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"sys_subject\",\"edit\":true,\"htmlType\":\"checkbox\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\"','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:04:44',55),(106,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":1,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:02:58\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":2,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:02:58\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":3,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"geo\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:02:58\",\"usableColumn\":false},{\"capJavaField\":\"Region\",\"columnComment\":\"区域（如：玄武区）\",\"columnId\":4,\"columnName\":\"region\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"in','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:05:23',45),(107,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":1,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:23\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":2,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:23\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":3,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"geo\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:23\",\"usableColumn\":false},{\"capJavaField\":\"Region\",\"columnComment\":\"区域（如：玄武区）\",\"columnId\":4,\"columnName\":\"region\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"in','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:05:36',61),(108,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"tutors\",\"className\":\"Tutors\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":13,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:04:44\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"职称（如：教师、老师、大学生教员）\",\"columnId\":14,\"columnName\":\"title\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:04:44\",\"usableColumn\":false},{\"capJavaField\":\"Certificates\",\"columnComment\":\"证书图片url\",\"columnId\":15,\"columnName\":\"certificates\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"certificates\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:04:44\",\"usableColumn\":false},{\"capJavaField\":\"Subjects\",\"columnComment\":\"可授科目数组（使用索引0，1，2)\",\"columnId\":16,\"columnName\":\"subjects\",\"columnType\":\"json\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"sys_subject\",\"edit\":true,\"htmlType\":\"checkb','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:05:50',55),(109,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":1,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:36\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":2,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:36\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":3,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"geo\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:36\",\"usableColumn\":false},{\"capJavaField\":\"Region\",\"columnComment\":\"区域（如：玄武区）\",\"columnId\":4,\"columnName\":\"region\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"in','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:05:57',59),(110,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-03 07:06:01',242),(111,'代码生成',3,'com.ruoyi.generator.controller.GenController.remove()','DELETE',1,'admin','研发部门','/tool/gen/1','127.0.0.1','内网IP','[1]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:13:04',43),(112,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"parents\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:13:07',77),(113,'代码生成',3,'com.ruoyi.generator.controller.GenController.remove()','DELETE',1,'admin','研发部门','/tool/gen/3','127.0.0.1','内网IP','[3]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:14:29',22),(114,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"parents\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:14:32',84),(115,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":40,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":41,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":42,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"geo\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Region\",\"columnComment\":\"区域（如：玄武区）\",\"columnId\":43,\"columnName\":\"region\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isReq','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:15:57',41),(116,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"tutors\",\"className\":\"Tutors\",\"columns\":[{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user对应的id\",\"columnId\":13,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":true,\"sort\":1,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:50\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"职称（如：教师、老师、大学生教员）\",\"columnId\":14,\"columnName\":\"title\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:50\",\"usableColumn\":false},{\"capJavaField\":\"Certificates\",\"columnComment\":\"证书图片url\",\"columnId\":15,\"columnName\":\"certificates\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"imageUpload\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"certificates\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-03 07:05:50\",\"usableColumn\":false},{\"capJavaField\":\"Subjects\",\"columnComment\":\"可授科目数组（使用索引0，1，2)\",\"columnId\":16,\"columnName\":\"subjects\",\"columnType\":\"json\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"sys_subject\",\"edit\":true,\"htmlType\":\"','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:16:11',54),(117,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-03 07:16:17',69),(118,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/parents/index\",\"createTime\":\"2026-03-03 07:07:00\",\"icon\":\"documentation\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"家教订单\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"parents\",\"perms\":\"system:parents:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:19:19',41),(119,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"tutors\"}',NULL,0,NULL,'2026-03-03 07:28:42',236),(120,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/tutors/index\",\"createTime\":\"2026-03-03 07:29:08\",\"icon\":\"people\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2007,\"menuName\":\"大学生/教员\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"tutors\",\"perms\":\"system:tutors:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 07:35:19',40),(121,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:00:12\",\"cssClass\":\"\",\"default\":false,\"dictCode\":60,\"dictLabel\":\"线下辅导\",\"dictSort\":1,\"dictType\":\"sys_methods\",\"dictValue\":\"0\",\"isDefault\":\"N\",\"listClass\":\"primary\",\"params\":{},\"remark\":\"线下上门辅导\",\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 08:34:34',23),(122,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:00:12\",\"cssClass\":\"\",\"default\":false,\"dictCode\":61,\"dictLabel\":\"网络辅导\",\"dictSort\":2,\"dictType\":\"sys_methods\",\"dictValue\":\"1\",\"isDefault\":\"N\",\"listClass\":\"primary\",\"params\":{},\"remark\":\"线上远程辅导\",\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 08:34:45',14),(123,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:00:12\",\"cssClass\":\"\",\"default\":false,\"dictCode\":62,\"dictLabel\":\"线上线下均可\",\"dictSort\":3,\"dictType\":\"sys_methods\",\"dictValue\":\"2\",\"isDefault\":\"N\",\"listClass\":\"primary\",\"params\":{},\"remark\":\"可协商决定\",\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-03 08:34:56',40),(124,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/parents','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 07:21:57',235),(125,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-04 07:22:59',563),(126,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/parents','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 08:41:56',199),(127,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"教员表主键ID\",\"columnId\":52,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"\",\"createTime\":\"2026-03-04 07:21:56\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 08:41:55\",\"usableColumn\":false},{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user表的主键ID\",\"columnId\":40,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"1\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":true,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 08:41:55\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":41,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 08:41:55\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":42,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 08:42:35',54),(128,'字典类型',1,'com.ruoyi.web.controller.system.SysDictTypeController.add()','POST',1,'admin','研发部门','/system/dict/type','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"dictName\":\"订单状态\",\"dictType\":\"sys_parent_status\",\"params\":{},\"remark\":\"家长订单状态列表\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:41:59',66),(129,'字典类型',1,'com.ruoyi.web.controller.system.SysDictTypeController.add()','POST',1,'admin','研发部门','/system/dict/type','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"dictName\":\"审核状态\",\"dictType\":\"sys_tutor_status\",\"params\":{},\"remark\":\"学生审核状态列表\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:42:28',49),(130,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"招募中\",\"dictSort\":0,\"dictType\":\"sys_parent_status\",\"dictValue\":\"0\",\"listClass\":\"primary\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:42:58',44),(131,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"已完成\",\"dictSort\":0,\"dictType\":\"sys_parent_status\",\"dictValue\":\"1\",\"listClass\":\"success\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:43:17',33),(132,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"已取消\",\"dictSort\":0,\"dictType\":\"sys_parent_status\",\"dictValue\":\"2\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:43:36',41),(133,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"未提交\",\"dictSort\":0,\"dictType\":\"sys_tutor_status\",\"dictValue\":\"0\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:44:18',45),(134,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"未审核\",\"dictSort\":0,\"dictType\":\"sys_tutor_status\",\"dictValue\":\"1\",\"listClass\":\"info\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:44:42',32),(135,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"审核通过\",\"dictSort\":0,\"dictType\":\"sys_tutor_status\",\"dictValue\":\"2\",\"listClass\":\"success\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:45:03',44),(136,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"审核不通过\",\"dictSort\":0,\"dictType\":\"sys_tutor_status\",\"dictValue\":\"3\",\"listClass\":\"warning\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:45:24',40),(137,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-04 09:45:24\",\"default\":false,\"dictCode\":106,\"dictLabel\":\"审核不通过\",\"dictSort\":0,\"dictType\":\"sys_tutor_status\",\"dictValue\":\"3\",\"isDefault\":\"N\",\"listClass\":\"danger\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:45:31',12),(138,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"教员表主键ID\",\"columnId\":52,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"\",\"createTime\":\"2026-03-04 07:21:56\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 08:42:35\",\"usableColumn\":false},{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user表的主键ID\",\"columnId\":40,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"1\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":true,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 08:42:35\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":41,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 08:42:35\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":42,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:45:58',86),(139,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/tutors','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:46:02',89),(140,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/tutors','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:46:49',96),(141,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"tutors\",\"className\":\"Tutors\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"教员表主键ID\",\"columnId\":54,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"\",\"createTime\":\"2026-03-04 09:46:02\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 09:46:49\",\"usableColumn\":false},{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user表的主键ID\",\"columnId\":13,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"1\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":true,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 09:46:49\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"职称（如：教师、老师、大学生教员）\",\"columnId\":14,\"columnName\":\"title\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":2,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 09:46:49\",\"usableColumn\":false},{\"capJavaField\":\"Certificates\",\"columnComment\":\"证书图片url\",\"columnId\":15,\"columnName\":\"certificates\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 06:54:08\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"imageUpload\",\"increment\":false,\"insert\":true,\"isEdit\":','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 09:47:29',47),(142,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-04 09:49:07',602),(143,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/parents','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 10:22:30',82),(144,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"parents\",\"className\":\"Parents\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"教员表主键ID\",\"columnId\":52,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"\",\"createTime\":\"2026-03-04 07:21:56\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 10:22:30\",\"usableColumn\":false},{\"capJavaField\":\"Uid\",\"columnComment\":\"关联user表的主键ID\",\"columnId\":40,\"columnName\":\"uid\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"1\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":true,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 10:22:30\",\"usableColumn\":false},{\"capJavaField\":\"Location\",\"columnComment\":\"地理位置文本（如：XX小区）\",\"columnId\":41,\"columnName\":\"location\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"location\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":4,\"updateBy\":\"\",\"updateTime\":\"2026-03-04 10:22:30\",\"usableColumn\":false},{\"capJavaField\":\"Geo\",\"columnComment\":\"经纬度位置（如：118.82,32.04）\",\"columnId\":42,\"columnName\":\"geo\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-03 07:14:32\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 10:22:41',44),(145,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-04 10:22:43',100),(146,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"0\",\"location\":\"南京\",\"methods\":0,\"name\":\"无语\",\"params\":{},\"subject\":\"1\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/ParentsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.ParentsMapper.insertParents-Inline\n### The error occurred while setting parameters\n### SQL: insert into parents          ( location,                                       name,             grade,             subject,             methods )           values ( ?,                                       ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n; Field \'uid\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'uid\' doesn\'t have a default value','2026-03-04 10:32:53',137),(147,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"1\",\"location\":\"阿水淀粉\",\"methods\":0,\"name\":\"史蒂夫\",\"params\":{},\"subject\":\"1\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/ParentsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.ParentsMapper.insertParents-Inline\n### The error occurred while setting parameters\n### SQL: insert into parents          ( location,                                       name,             grade,             subject,             methods )           values ( ?,                                       ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n; Field \'uid\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'uid\' doesn\'t have a default value','2026-03-04 10:41:03',17),(148,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"1\",\"location\":\"阿水淀粉\",\"methods\":0,\"name\":\"史蒂夫\",\"params\":{},\"subject\":\"1\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/ParentsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.ParentsMapper.insertParents-Inline\n### The error occurred while setting parameters\n### SQL: insert into parents          ( location,                                       name,             grade,             subject,             methods )           values ( ?,                                       ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n; Field \'uid\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'uid\' doesn\'t have a default value','2026-03-04 10:41:10',8),(149,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"2\",\"id\":11,\"location\":\"大发疯\",\"methods\":0,\"name\":\"阿多少分\",\"params\":{},\"subject\":\"2\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 10:42:22',26),(150,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"\",\"location\":\"\",\"name\":\"\",\"params\":{},\"subject\":\"\",\"uid\":1}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'1\' for key \'parents.uid\'\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/ParentsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.ParentsMapper.insertParents-Inline\n### The error occurred while setting parameters\n### SQL: insert into parents          ( uid,             location,                                                    grade,             subject )           values ( ?,             ?,                                                    ?,             ? )\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'1\' for key \'parents.uid\'\n; Duplicate entry \'1\' for key \'parents.uid\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'1\' for key \'parents.uid\'','2026-03-04 10:45:38',51),(151,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"0\",\"id\":816988308285558784,\"location\":\"阿道夫\",\"methods\":0,\"name\":\"阿水淀粉\",\"params\":{},\"subject\":\"0\",\"uid\":1}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'1\' for key \'parents.uid\'\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/ParentsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.ParentsMapper.insertParents-Inline\n### The error occurred while setting parameters\n### SQL: insert into parents          ( uid,             location,                                       name,             grade,             subject,             methods )           values ( ?,             ?,                                       ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'1\' for key \'parents.uid\'\n; Duplicate entry \'1\' for key \'parents.uid\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'1\' for key \'parents.uid\'','2026-03-04 10:59:51',133),(152,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"0\",\"id\":14,\"location\":\"阿道夫\",\"methods\":0,\"name\":\"阿水淀粉\",\"params\":{},\"subject\":\"0\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":816988722275946496}',0,NULL,'2026-03-04 11:01:30',39),(153,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"0\",\"id\":816989179438305280,\"location\":\"阿道夫\",\"methods\":0,\"name\":\"阿水淀粉\",\"params\":{},\"subject\":\"0\",\"uid\":1}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'id\' doesn\'t have a default value\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/ParentsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.ParentsMapper.insertParents-Inline\n### The error occurred while setting parameters\n### SQL: insert into parents          ( uid,             location,                                       name,             grade,             subject,             methods )           values ( ?,             ?,                                       ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Field \'id\' doesn\'t have a default value\n; Field \'id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'id\' doesn\'t have a default value','2026-03-04 11:03:19',12),(154,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/parents','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-04 11:04:48',171),(155,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"1\",\"id\":816990328295919616,\"location\":\"阿水淀粉\",\"methods\":0,\"name\":\"阿多少发\",\"params\":{},\"subject\":\"2\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":816990328295919616}',0,NULL,'2026-03-04 11:07:53',42),(156,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"1\",\"id\":816992034345848832,\"location\":\"阿水淀粉\",\"methods\":0,\"name\":\"阿多少发\",\"params\":{},\"subject\":\"2\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":816992034345848832}',0,NULL,'2026-03-04 11:14:40',30),(157,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"grade\":\"1\",\"id\":\"3191388629780736\",\"location\":\"第三方\",\"methods\":2,\"name\":\"阿水淀粉\",\"params\":{},\"subject\":\"1\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3191388629780736}',0,NULL,'2026-03-04 11:28:23',53),(158,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"geo\":\"116.325822,39.966431\",\"grade\":\"1\",\"id\":\"3192287102011648\",\"location\":\"北京市海淀区北三环西路38号\",\"methods\":1,\"name\":\"auf\",\"params\":{},\"region\":\"海淀区\",\"subject\":\"2\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3192287102011648}',0,NULL,'2026-03-05 02:42:22',119),(159,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"geo\":\"116.325822,39.966431\",\"grade\":\"1\",\"id\":\"3192287618369792\",\"location\":\"北京市海淀区北三环西路38号\",\"methods\":1,\"name\":\"auf\",\"params\":{},\"region\":\"海淀区\",\"subject\":\"2\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3192287618369792}',0,NULL,'2026-03-05 02:42:53',31),(160,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"geo\":\"116.325822,39.966431\",\"grade\":\"1\",\"id\":\"3192287884822784\",\"location\":\"北京市海淀区北三环西路38号\",\"methods\":1,\"name\":\"auf\",\"params\":{},\"region\":\"海淀区\",\"subject\":\"2\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3192287884822784}',0,NULL,'2026-03-05 02:43:09',36),(161,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/parents','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 03:04:37',165),(162,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','192.168.31.38','内网IP','{\"brief\":\"阿四大发疯啊\",\"dayOfWeek\":\"1,2,3,7\",\"endTime\":\"12:00\",\"geo\":\"116.291426,40.102976\",\"grade\":\"1\",\"id\":\"3192547823192320\",\"location\":\"北京市昌平区史各庄街道\",\"methods\":0,\"name\":\"刘女士\",\"params\":{},\"region\":\"昌平区\",\"startTime\":\"09:00\",\"subject\":\"1\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3192547823192320}',0,NULL,'2026-03-05 07:07:35',163),(163,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-04 09:44:42\",\"default\":false,\"dictCode\":104,\"dictLabel\":\"审核中\",\"dictSort\":0,\"dictType\":\"sys_tutor_status\",\"dictValue\":\"1\",\"isDefault\":\"N\",\"listClass\":\"info\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 08:37:39',63),(164,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/tutors','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 09:48:11',338),(165,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"lectures\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 09:50:06',69),(166,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"lectures\"}',NULL,0,NULL,'2026-03-05 09:50:27',87),(167,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/lectures/index\",\"createTime\":\"2026-03-05 09:51:23\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2013,\"menuName\":\"课程活动/讲座\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"lectures\",\"perms\":\"system:lectures:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 09:52:15',25),(168,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/parents','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 09:55:06',101),(169,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/tutors','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 09:55:08',84),(170,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-05 09:55:44',96),(171,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"tutors\"}',NULL,0,NULL,'2026-03-05 09:55:46',65),(172,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/tutors','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 10:43:40',183),(173,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"parents\"}',NULL,0,NULL,'2026-03-05 10:44:04',91),(174,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"tutors\"}',NULL,0,NULL,'2026-03-05 10:44:07',70),(175,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"lectures\"}',NULL,0,NULL,'2026-03-05 11:39:55',341),(176,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"daily_jobs\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 11:56:54',203),(177,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"daily_jobs\"}',NULL,0,NULL,'2026-03-05 11:57:01',76),(178,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/daily_jobs','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:14:20',207),(179,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"daily_jobs\"}',NULL,0,NULL,'2026-03-05 12:14:31',85),(180,'字典类型',1,'com.ruoyi.web.controller.system.SysDictTypeController.add()','POST',1,'admin','研发部门','/system/dict/type','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"dictName\":\"日结兼职状态\",\"dictType\":\"sys_day_work_status\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:19:11',49),(181,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"招募中\",\"dictSort\":0,\"dictType\":\"sys_day_work_status\",\"dictValue\":\"0\",\"listClass\":\"primary\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:19:32',22),(182,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"已满员\",\"dictSort\":0,\"dictType\":\"sys_day_work_status\",\"dictValue\":\"2\",\"listClass\":\"warning\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:19:51',29),(183,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"已结束\",\"dictSort\":0,\"dictType\":\"sys_day_work_status\",\"dictValue\":\"1\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:20:23',21),(184,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-05 12:20:23\",\"default\":false,\"dictCode\":109,\"dictLabel\":\"已结束\",\"dictSort\":0,\"dictType\":\"sys_day_work_status\",\"dictValue\":\"1\",\"isDefault\":\"N\",\"listClass\":\"info\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:20:29',20),(185,'字典类型',1,'com.ruoyi.web.controller.system.SysDictTypeController.add()','POST',1,'admin','研发部门','/system/dict/type','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"dictName\":\"日结工作分类\",\"dictType\":\"sys_daily_category\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:23:44',39),(186,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"服务员\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"0\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:24:19',38),(187,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"其他\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"1\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:24:42',32),(188,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/jobs/index\",\"createTime\":\"2026-03-05 11:57:18\",\"icon\":\"dashboard\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2019,\"menuName\":\"兼职日结工作\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"jobs\",\"perms\":\"system:jobs:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:46:36',59),(189,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/lectures/index\",\"createTime\":\"2026-03-05 09:51:23\",\"icon\":\"post\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2013,\"menuName\":\"课程活动/讲座\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"lectures\",\"perms\":\"system:lectures:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:47:15',38),(190,'兼职日结工作',1,'com.ruoyi.web.controller.bussiness.DailyJobsController.add()','POST',1,'admin','研发部门','/system/jobs','127.0.0.1','内网IP','{\"category\":3,\"contacts\":\"吴\",\"description\":\"洗盘子\",\"districtId\":\"320123\",\"id\":11,\"location\":\"双门楼\",\"params\":{},\"phone\":\"138123283473\",\"salaryDay\":200,\"title\":\"大学生日结服务员\",\"workDate\":\"2026-03-13\",\"workTime\":\"6:30-12:00\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 12:53:31',72),(191,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"高淳区,雨花台区\",\"certificate\":\"阿水淀粉\",\"degree\":2,\"experience\":\"阿四方\",\"idCard\":\"111111111111111111\",\"isCertified\":0,\"live\":\"320105\",\"major\":\"史蒂夫\",\"methods\":1,\"params\":{},\"realName\":\"为\",\"school\":\"而非\",\"selfJudge\":\"第三方\",\"subjects\":\"22,18,13\",\"work\":\"320104\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/TutorsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.TutorsMapper.insertTutors-Inline\n### The error occurred while setting parameters\n### SQL: insert into tutors          ( subjects,             areas,             methods,             is_certified,                          experience,             major,             school,             degree,                                       real_name,             id_card,             self_judge,             certificate,             live,             work )           values ( ?,             ?,             ?,             ?,                          ?,             ?,             ?,             ?,                                       ?,             ?,             ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n; Field \'uid\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'uid\' doesn\'t have a default value','2026-03-05 15:35:04',174),(192,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"高淳区,雨花台区\",\"certificate\":\"阿水淀粉\",\"degree\":2,\"experience\":\"阿四方\",\"idCard\":\"111111111111111111\",\"isCertified\":0,\"live\":\"320105\",\"major\":\"史蒂夫\",\"methods\":1,\"params\":{},\"realName\":\"为\",\"school\":\"而非\",\"selfJudge\":\"第三方\",\"subjects\":\"22,18,13\",\"work\":\"320104\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/TutorsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.TutorsMapper.insertTutors-Inline\n### The error occurred while setting parameters\n### SQL: insert into tutors          ( subjects,             areas,             methods,             is_certified,                          experience,             major,             school,             degree,                                       real_name,             id_card,             self_judge,             certificate,             live,             work )           values ( ?,             ?,             ?,             ?,                          ?,             ?,             ?,             ?,                                       ?,             ?,             ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Field \'uid\' doesn\'t have a default value\n; Field \'uid\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'uid\' doesn\'t have a default value','2026-03-05 15:35:17',21),(193,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"雨花台区,建邺区\",\"certificate\":\"阿水淀粉\",\"degree\":2,\"experience\":\"阿多少发\",\"id\":41,\"idCard\":\"111111111111111111\",\"isCertified\":0,\"live\":\"320102\",\"major\":\"第三方\",\"methods\":1,\"params\":{},\"realName\":\"阿多少发\",\"school\":\"第三方\",\"selfJudge\":\"阿道夫\",\"subjects\":\"19,14\",\"uid\":1,\"work\":\"\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-05 15:37:17',37),(194,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"320114,320105\",\"certificate\":\"阿多少发\",\"certificates\":\"http://localhost:8080/profile/upload/2026/03/06/rQrk7ZIH02hZ63a95cf6036e416bd003c35ca7a8532b_20260306095323A004.jpeg\",\"degree\":1,\"experience\":\"阿水淀粉\",\"id\":42,\"idCard\":\"111111111111111111\",\"isCertified\":0,\"live\":\"320104\",\"major\":\"阿道夫\",\"methods\":1,\"params\":{},\"realName\":\"阿道夫\",\"school\":\"阿水淀粉\",\"selfJudge\":\"阿水淀粉\",\"subjects\":\"18,13\",\"uid\":1,\"work\":\"320104\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 01:53:38',157),(195,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','127.0.0.1','内网IP','{\"brief\":\"学生成绩较差，不爱学习\",\"dayOfWeek\":\"1,2,3,4\",\"endTime\":\"10:00\",\"geo\":\"118.791477,32.059844\",\"grade\":\"5\",\"id\":\"3193664649365760\",\"location\":\"江苏省南京市玄武区鸡鸣寺路1号\",\"methods\":0,\"name\":\"王女士\",\"params\":{},\"region\":\"玄武区\",\"requirements\":\"需要能够提升能力，女，大学生，\",\"startTime\":\"08:00\",\"subject\":\"1\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3193664649365760}',0,NULL,'2026-03-06 02:03:40',82),(196,'家教订单',1,'com.ruoyi.web.controller.bussiness.ParentsController.add()','POST',1,'admin','研发部门','/system/parents','127.0.0.1','内网IP','{\"brief\":\"阿多少分\",\"dayOfWeek\":\"1,2,6\",\"endTime\":\"10:00\",\"geo\":\"118.703387,31.992043\",\"grade\":\"0\",\"id\":\"3193680098412800\",\"location\":\"江苏省南京市建邺区青奥北路与青奥路交叉口西北方向248米左右\",\"methods\":0,\"name\":\"高中英语辅导\",\"params\":{},\"region\":\"建邺区\",\"requirements\":\"阿芙蓉如风\",\"startTime\":\"08:00\",\"subject\":\"1\",\"uid\":1}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3193680098412800}',0,NULL,'2026-03-06 02:19:23',52),(197,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"320115,320114\",\"certificate\":\"大法额\",\"certificates\":\"http://localhost:8080/profile/upload/2026/03/06/jWd_Y9MCnx2l63a95cf6036e416bd003c35ca7a8532b_20260306102633A001.jpeg\",\"degree\":2,\"experience\":\"大法\",\"id\":3193687218309376,\"idCard\":\"123123123123123123\",\"isCertified\":0,\"live\":\"320104\",\"major\":\"大法\",\"methods\":0,\"params\":{},\"realName\":\"阿多少分\",\"school\":\"阿多少发发\",\"selfJudge\":\"阿水淀粉\",\"subjects\":\"22,18\",\"uid\":1,\"work\":\"320104\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Column count doesn\'t match value count at row 1\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/TutorsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.TutorsMapper.insertTutors-Inline\n### The error occurred while setting parameters\n### SQL: insert into tutors          ( id,             uid,                          certificates,             subjects,             areas,             methods,             is_certified,                          experience,             major,             school,             degree,                                       real_name,             id_card,             self_judge,             certificate,             live,             work )           values ( ?,                          ?,             ?,             ?,             ?,             ?,                          ?,             ?,             ?,             ?,                                       ?,             ?,             ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Column count doesn\'t match value count at row 1\n; bad SQL grammar []; nested exception is java.sql.SQLException: Column count doesn\'t match value count at row 1','2026-03-06 02:26:38',89),(198,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"320114\",\"certificate\":\"\",\"certificates\":\"http://localhost:8080/profile/upload/2026/03/06/UR6O-P32xRmb63a95cf6036e416bd003c35ca7a8532b_20260306102802A001.jpeg\",\"degree\":1,\"experience\":\"大法\",\"id\":3193688672438528,\"idCard\":\"123123123123111111\",\"isCertified\":0,\"live\":\"320102\",\"major\":\"阿道夫\",\"methods\":0,\"params\":{},\"realName\":\"大法\",\"school\":\"阿道夫\",\"selfJudge\":\"阿水淀粉\",\"subjects\":\"18\",\"uid\":1,\"work\":\"320105\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Column count doesn\'t match value count at row 1\n### The error may exist in file [/Users/wuyang/Documents/site/ruoyi-vue-wxmini/ruoyi-system/target/classes/mapper/system/TutorsMapper.xml]\n### The error may involve com.ruoyi.system.mapper.TutorsMapper.insertTutors-Inline\n### The error occurred while setting parameters\n### SQL: insert into tutors          ( id,             uid,                          certificates,             subjects,             areas,             methods,             is_certified,                          experience,             major,             school,             degree,                                       real_name,             id_card,             self_judge,             certificate,             live,             work )           values ( ?,                          ?,             ?,             ?,             ?,             ?,                          ?,             ?,             ?,             ?,                                       ?,             ?,             ?,             ?,             ?,             ? )\n### Cause: java.sql.SQLException: Column count doesn\'t match value count at row 1\n; bad SQL grammar []; nested exception is java.sql.SQLException: Column count doesn\'t match value count at row 1','2026-03-06 02:28:07',95),(199,'大学生/教员',1,'com.ruoyi.web.controller.bussiness.TutorsController.add()','POST',1,'admin','研发部门','/system/tutors','127.0.0.1','内网IP','{\"areas\":\"320114\",\"certificate\":\"\",\"certificates\":\"http://localhost:8080/profile/upload/2026/03/06/UR6O-P32xRmb63a95cf6036e416bd003c35ca7a8532b_20260306102802A001.jpeg\",\"degree\":1,\"experience\":\"大法\",\"id\":3193689978456320,\"idCard\":\"123123123123111111\",\"isCertified\":0,\"live\":\"320102\",\"major\":\"阿道夫\",\"methods\":0,\"params\":{},\"realName\":\"大法\",\"school\":\"阿道夫\",\"selfJudge\":\"阿水淀粉\",\"subjects\":\"18\",\"uid\":1,\"work\":\"320105\"}','{\"msg\":\"操作成功\",\"code\":200,\"data\":3193689978456320}',0,NULL,'2026-03-06 02:29:26',60),(200,'用户头像',2,'com.ruoyi.web.controller.system.SysProfileController.avatar()','POST',1,'admin','研发部门','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2026/03/06/kRFP27IosT4q57d253dadd7121bbd10c10bbe98e65bb_20260306112542A001.png\",\"code\":200}',0,NULL,'2026-03-06 03:25:42',174),(201,'用户头像',2,'com.ruoyi.web.controller.system.SysProfileController.avatar()','POST',1,'admin','研发部门','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2026/03/06/-t2tIbQv5v74580c3c3119d469920a02574f6ada64d1_20260306112604A002.png\",\"code\":200}',0,NULL,'2026-03-06 03:26:04',20),(202,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"sign_in_record\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 14:21:53',161),(203,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"user_realname_auth\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 14:22:13',66),(204,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"auth\",\"className\":\"UserRealnameAuth\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":96,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:22:13\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":8,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Uid\",\"columnComment\":\"用户ID，关联sys_user表\",\"columnId\":97,\"columnName\":\"uid\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:22:13\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":8,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"RealName\",\"columnComment\":\"真实姓名\",\"columnId\":98,\"columnName\":\"real_name\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:22:13\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"realName\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"LIKE\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":8,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"IdCard\",\"columnComment\":\"身份证号码\",\"columnId\":99,\"columnName\":\"id_card\",\"columnType\":\"varchar(18)\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:22:13\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"idC','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 14:22:58',61),(205,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"record\",\"className\":\"SignInRecord\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":90,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:21:53\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":7,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Uid\",\"columnComment\":\"用户ID，关联sys_user表的user_id\",\"columnId\":91,\"columnName\":\"uid\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:21:53\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"uid\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":7,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"LectureId\",\"columnComment\":\"讲座ID，关联lectures表的id\",\"columnId\":92,\"columnName\":\"lecture_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:21:53\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"lectureId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":7,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"SignTime\",\"columnComment\":\"签到时间\",\"columnId\":93,\"columnName\":\"sign_time\",\"columnType\":\"datetime\",\"createBy\":\"admin\",\"createTime\":\"2026-03-06 14:21:53\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"datetime\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 14:23:30',37),(206,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"user_realname_auth,sign_in_record\"}',NULL,0,NULL,'2026-03-06 14:23:36',280),(207,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"志愿者\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"1\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:31:14',40),(208,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-05 12:24:42\",\"default\":false,\"dictCode\":111,\"dictLabel\":\"其他\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"10\",\"isDefault\":\"N\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:31:23',20),(209,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"群演\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"2\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:31:37',31),(210,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"促销员\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"3\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:31:50',25),(211,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"话务员\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"5\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:32:01',35),(212,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"地推\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"4\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:32:15',24),(213,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"活动充场\",\"dictSort\":0,\"dictType\":\"sys_daily_category\",\"dictValue\":\"6\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-06 15:32:29',20),(214,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-03-05 12:24:42\",\"default\":false,\"dictCode\":111,\"dictLabel\":\"其他\",\"dictSort\":3,\"dictType\":\"sys_daily_category\",\"dictValue\":\"10\",\"isDefault\":\"N\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 02:02:11',55),(215,'部门管理',2,'com.ruoyi.web.controller.system.SysDeptController.edit()','PUT',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0\",\"children\":[],\"deptId\":100,\"deptName\":\"全国总经理\",\"email\":\"ry@qq.com\",\"leader\":\"若依\",\"orderNum\":0,\"params\":{},\"parentId\":0,\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:47:42',95),(216,'部门管理',2,'com.ruoyi.web.controller.system.SysDeptController.edit()','PUT',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100\",\"children\":[],\"deptId\":101,\"deptName\":\"广东省负责人\",\"email\":\"ry@qq.com\",\"leader\":\"若依\",\"orderNum\":1,\"params\":{},\"parentId\":100,\"parentName\":\"全国总经理\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:48:31',69),(217,'部门管理',1,'com.ruoyi.web.controller.system.SysDeptController.add()','POST',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100\",\"children\":[],\"createBy\":\"admin\",\"deptName\":\"江苏省负责人\",\"orderNum\":0,\"params\":{},\"parentId\":100,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:48:57',38),(218,'部门管理',1,'com.ruoyi.web.controller.system.SysDeptController.add()','POST',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100,200\",\"children\":[],\"createBy\":\"admin\",\"deptName\":\"南京市负责人\",\"orderNum\":0,\"params\":{},\"parentId\":200,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:49:14',22),(219,'部门管理',1,'com.ruoyi.web.controller.system.SysDeptController.add()','POST',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100,200\",\"children\":[],\"createBy\":\"admin\",\"deptName\":\"无锡市负责人\",\"orderNum\":1,\"params\":{},\"parentId\":200,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:49:44',31),(220,'部门管理',1,'com.ruoyi.web.controller.system.SysDeptController.add()','POST',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100,200,201\",\"children\":[],\"createBy\":\"admin\",\"deptName\":\"中层干部\",\"orderNum\":0,\"params\":{},\"parentId\":201,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:50:03',26),(221,'部门管理',2,'com.ruoyi.web.controller.system.SysDeptController.edit()','PUT',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100,200,201\",\"children\":[],\"deptId\":203,\"deptName\":\"中层干部1\",\"orderNum\":0,\"params\":{},\"parentId\":201,\"parentName\":\"南京市负责人\",\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:50:30',26),(222,'部门管理',1,'com.ruoyi.web.controller.system.SysDeptController.add()','POST',1,'admin','研发部门','/system/dept','127.0.0.1','内网IP','{\"ancestors\":\"0,100,200,201\",\"children\":[],\"createBy\":\"admin\",\"deptName\":\"中层干部2\",\"orderNum\":1,\"params\":{},\"parentId\":201,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:50:39',33),(223,'角色管理',1,'com.ruoyi.web.controller.system.SysRoleController.add()','POST',1,'admin','研发部门','/system/role','127.0.0.1','内网IP','{\"admin\":false,\"createBy\":\"admin\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[1,100,1000,1001,1002,1003,1004,1005,1006],\"params\":{},\"roleId\":100,\"roleKey\":\"low_permission\",\"roleName\":\"中层管理\",\"roleSort\":2,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 05:59:02',105),(224,'角色管理',1,'com.ruoyi.web.controller.system.SysRoleController.add()','POST',1,'admin','研发部门','/system/role','127.0.0.1','内网IP','{\"admin\":false,\"createBy\":\"admin\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[1,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,3,115,2025,2026,2027,2028,2029,2030,2031,2032,2033,2034,2035,2036,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"roleId\":101,\"roleKey\":\"full\",\"roleName\":\"全国管理员\",\"roleSort\":0,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:01:05',37),(225,'角色管理',1,'com.ruoyi.web.controller.system.SysRoleController.add()','POST',1,'admin','研发部门','/system/role','127.0.0.1','内网IP','{\"admin\":false,\"createBy\":\"admin\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[1,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,3,115,2025,2026,2027,2028,2029,2030,2031,2032,2033,2034,2035,2036,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"roleId\":102,\"roleKey\":\"province\",\"roleName\":\"省级管理员\",\"roleSort\":1,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:02:15',41),(226,'角色管理',1,'com.ruoyi.web.controller.system.SysRoleController.add()','POST',1,'admin','研发部门','/system/role','127.0.0.1','内网IP','{\"admin\":false,\"createBy\":\"admin\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[1,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,3,115,2025,2026,2027,2028,2029,2030,2031,2032,2033,2034,2035,2036,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"roleId\":103,\"roleKey\":\"city\",\"roleName\":\"市级管理员\",\"roleSort\":2,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:02:40',33),(227,'角色管理',2,'com.ruoyi.web.controller.system.SysRoleController.edit()','PUT',1,'admin','研发部门','/system/role','127.0.0.1','内网IP','{\"admin\":false,\"createTime\":\"2026-03-01 15:28:45\",\"dataScope\":\"2\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[3,1,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,115,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"remark\":\"普通角色\",\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":4,\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:02:55',71),(228,'角色管理',2,'com.ruoyi.web.controller.system.SysRoleController.edit()','PUT',1,'admin','研发部门','/system/role','127.0.0.1','内网IP','{\"admin\":false,\"createTime\":\"2026-03-07 05:59:02\",\"dataScope\":\"1\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[1,100,1000,1001,1002,1003,1004,1005,1006],\"params\":{},\"roleId\":100,\"roleKey\":\"low_permission\",\"roleName\":\"中层管理\",\"roleSort\":3,\"status\":\"0\",\"updateBy\":\"admin\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:03:02',24),(229,'部门管理',3,'com.ruoyi.web.controller.system.SysDeptController.remove()','DELETE',1,'admin','研发部门','/system/dept/203','127.0.0.1','内网IP','203','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:32:46',72),(230,'部门管理',3,'com.ruoyi.web.controller.system.SysDeptController.remove()','DELETE',1,'admin','研发部门','/system/dept/204','127.0.0.1','内网IP','204','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:32:48',13),(231,'角色管理',2,'com.ruoyi.web.controller.system.SysRoleController.dataScope()','PUT',1,'admin','研发部门','/system/role/dataScope','127.0.0.1','内网IP','{\"admin\":false,\"createTime\":\"2026-03-07 06:02:15\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"params\":{},\"roleId\":102,\"roleKey\":\"province\",\"roleName\":\"省级管理员\",\"roleSort\":1,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:46:21',25),(232,'角色管理',2,'com.ruoyi.web.controller.system.SysRoleController.dataScope()','PUT',1,'admin','研发部门','/system/role/dataScope','127.0.0.1','内网IP','{\"admin\":false,\"createTime\":\"2026-03-07 06:02:40\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"params\":{},\"roleId\":103,\"roleKey\":\"city\",\"roleName\":\"市级管理员\",\"roleSort\":2,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:46:29',21),(233,'角色管理',2,'com.ruoyi.web.controller.system.SysRoleController.dataScope()','PUT',1,'admin','研发部门','/system/role/dataScope','127.0.0.1','内网IP','{\"admin\":false,\"createTime\":\"2026-03-07 05:59:02\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"params\":{},\"roleId\":100,\"roleKey\":\"low_permission\",\"roleName\":\"中层管理\",\"roleSort\":3,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:46:37',27),(234,'角色管理',2,'com.ruoyi.web.controller.system.SysRoleController.dataScope()','PUT',1,'admin','研发部门','/system/role/dataScope','127.0.0.1','内网IP','{\"admin\":false,\"createTime\":\"2026-03-07 06:01:05\",\"dataScope\":\"1\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"params\":{},\"roleId\":101,\"roleKey\":\"full\",\"roleName\":\"全国管理员\",\"roleSort\":0,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 06:46:49',13),(235,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"student_enrollment,questionnaire,lecture_material,lecturer_profile\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:24:48',130),(236,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"lecture_material,lecturer_profile,questionnaire,student_enrollment\"}',NULL,0,NULL,'2026-03-07 07:24:55',204),(237,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/auth/index\",\"createTime\":\"2026-03-06 14:24:06\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2025,\"menuName\":\"用户实名认证\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"auth\",\"perms\":\"system:auth:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:29:54',43),(238,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/record/index\",\"createTime\":\"2026-03-06 14:24:17\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2031,\"menuName\":\"讲座签到记录\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"record\",\"perms\":\"system:record:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:30:00',28),(239,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/enrollment/index\",\"createTime\":\"2026-03-07 07:25:30\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2037,\"menuName\":\"学籍信息\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"enrollment\",\"perms\":\"system:enrollment:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:30:07',37),(240,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/questionnaire/index\",\"createTime\":\"2026-03-07 07:26:07\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2043,\"menuName\":\"问卷调查配置\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"questionnaire\",\"perms\":\"system:questionnaire:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:30:11',18),(241,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/profile/index\",\"createTime\":\"2026-03-07 07:26:15\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2049,\"menuName\":\"讲师风采\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"profile\",\"perms\":\"system:profile:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:30:16',20),(242,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/material/index\",\"createTime\":\"2026-03-07 07:26:24\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2055,\"menuName\":\"资料中心数据\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"material\",\"perms\":\"system:material:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 07:30:20',19),(243,'在线用户',7,'com.ruoyi.web.controller.monitor.SysUserOnlineController.forceLogout()','DELETE',1,'admin','研发部门','/monitor/online/4251402c-2acf-4583-91ea-92ac04add148','127.0.0.1','内网IP','\"4251402c-2acf-4583-91ea-92ac04add148\"','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 10:51:23',7),(244,'在线用户',7,'com.ruoyi.web.controller.monitor.SysUserOnlineController.forceLogout()','DELETE',1,'admin','研发部门','/monitor/online/15b0cf48-a8e3-4c3d-ad64-52024300175e','127.0.0.1','内网IP','\"15b0cf48-a8e3-4c3d-ad64-52024300175e\"','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 10:51:36',3),(245,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"salon_info,order\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-07 13:38:55',240),(246,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"salon_info,order\"}',NULL,0,NULL,'2026-03-07 13:58:54',356);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'ceo','董事长',1,'0','admin','2026-03-01 15:28:45','',NULL,''),(2,'se','项目经理',2,'0','admin','2026-03-01 15:28:45','',NULL,''),(3,'hr','人力资源',3,'0','admin','2026-03-01 15:28:45','',NULL,''),(4,'user','普通员工',4,'0','admin','2026-03-01 15:28:45','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2026-03-01 15:28:45','',NULL,'超级管理员'),(2,'普通角色','common',4,'2',1,1,'0','0','admin','2026-03-01 15:28:45','admin','2026-03-07 06:02:55','普通角色'),(100,'中层管理','low_permission',3,'4',1,1,'0','0','admin','2026-03-07 05:59:02','admin','2026-03-07 06:46:37',NULL),(101,'全国管理员','full',0,'1',1,1,'0','0','admin','2026-03-07 06:01:05','','2026-03-07 06:46:49',NULL),(102,'省级管理员','province',1,'4',1,1,'0','0','admin','2026-03-07 06:02:15','','2026-03-07 06:46:21',NULL),(103,'市级管理员','city',2,'4',1,1,'0','0','admin','2026-03-07 06:02:40','','2026-03-07 06:46:29',NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,117),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060),(100,1),(100,100),(100,1000),(100,1001),(100,1002),(100,1003),(100,1004),(100,1005),(100,1006),(101,1),(101,2),(101,3),(101,4),(101,100),(101,101),(101,102),(101,103),(101,104),(101,105),(101,106),(101,107),(101,108),(101,109),(101,110),(101,111),(101,112),(101,113),(101,114),(101,115),(101,116),(101,117),(101,500),(101,501),(101,1000),(101,1001),(101,1002),(101,1003),(101,1004),(101,1005),(101,1006),(101,1007),(101,1008),(101,1009),(101,1010),(101,1011),(101,1012),(101,1013),(101,1014),(101,1015),(101,1016),(101,1017),(101,1018),(101,1019),(101,1020),(101,1021),(101,1022),(101,1023),(101,1024),(101,1025),(101,1026),(101,1027),(101,1028),(101,1029),(101,1030),(101,1031),(101,1032),(101,1033),(101,1034),(101,1035),(101,1036),(101,1037),(101,1038),(101,1039),(101,1040),(101,1041),(101,1042),(101,1043),(101,1044),(101,1045),(101,1046),(101,1047),(101,1048),(101,1049),(101,1050),(101,1051),(101,1052),(101,1053),(101,1054),(101,1055),(101,1056),(101,1057),(101,1058),(101,1059),(101,1060),(101,2000),(101,2001),(101,2002),(101,2003),(101,2004),(101,2005),(101,2006),(101,2007),(101,2008),(101,2009),(101,2010),(101,2011),(101,2012),(101,2013),(101,2014),(101,2015),(101,2016),(101,2017),(101,2018),(101,2019),(101,2020),(101,2021),(101,2022),(101,2023),(101,2024),(101,2025),(101,2026),(101,2027),(101,2028),(101,2029),(101,2030),(101,2031),(101,2032),(101,2033),(101,2034),(101,2035),(101,2036),(102,1),(102,2),(102,3),(102,4),(102,100),(102,101),(102,102),(102,103),(102,104),(102,105),(102,106),(102,107),(102,108),(102,109),(102,110),(102,111),(102,112),(102,113),(102,114),(102,115),(102,116),(102,117),(102,500),(102,501),(102,1000),(102,1001),(102,1002),(102,1003),(102,1004),(102,1005),(102,1006),(102,1007),(102,1008),(102,1009),(102,1010),(102,1011),(102,1012),(102,1013),(102,1014),(102,1015),(102,1016),(102,1017),(102,1018),(102,1019),(102,1020),(102,1021),(102,1022),(102,1023),(102,1024),(102,1025),(102,1026),(102,1027),(102,1028),(102,1029),(102,1030),(102,1031),(102,1032),(102,1033),(102,1034),(102,1035),(102,1036),(102,1037),(102,1038),(102,1039),(102,1040),(102,1041),(102,1042),(102,1043),(102,1044),(102,1045),(102,1046),(102,1047),(102,1048),(102,1049),(102,1050),(102,1051),(102,1052),(102,1053),(102,1054),(102,1055),(102,1056),(102,1057),(102,1058),(102,1059),(102,1060),(102,2000),(102,2001),(102,2002),(102,2003),(102,2004),(102,2005),(102,2006),(102,2007),(102,2008),(102,2009),(102,2010),(102,2011),(102,2012),(102,2013),(102,2014),(102,2015),(102,2016),(102,2017),(102,2018),(102,2019),(102,2020),(102,2021),(102,2022),(102,2023),(102,2024),(102,2025),(102,2026),(102,2027),(102,2028),(102,2029),(102,2030),(102,2031),(102,2032),(102,2033),(102,2034),(102,2035),(102,2036),(103,1),(103,2),(103,3),(103,4),(103,100),(103,101),(103,102),(103,103),(103,104),(103,105),(103,106),(103,107),(103,108),(103,109),(103,110),(103,111),(103,112),(103,113),(103,114),(103,115),(103,116),(103,117),(103,500),(103,501),(103,1000),(103,1001),(103,1002),(103,1003),(103,1004),(103,1005),(103,1006),(103,1007),(103,1008),(103,1009),(103,1010),(103,1011),(103,1012),(103,1013),(103,1014),(103,1015),(103,1016),(103,1017),(103,1018),(103,1019),(103,1020),(103,1021),(103,1022),(103,1023),(103,1024),(103,1025),(103,1026),(103,1027),(103,1028),(103,1029),(103,1030),(103,1031),(103,1032),(103,1033),(103,1034),(103,1035),(103,1036),(103,1037),(103,1038),(103,1039),(103,1040),(103,1041),(103,1042),(103,1043),(103,1044),(103,1045),(103,1046),(103,1047),(103,1048),(103,1049),(103,1050),(103,1051),(103,1052),(103,1053),(103,1054),(103,1055),(103,1056),(103,1057),(103,1058),(103,1059),(103,1060),(103,2000),(103,2001),(103,2002),(103,2003),(103,2004),(103,2005),(103,2006),(103,2007),(103,2008),(103,2009),(103,2010),(103,2011),(103,2012),(103,2013),(103,2014),(103,2015),(103,2016),(103,2017),(103,2018),(103,2019),(103,2020),(103,2021),(103,2022),(103,2023),(103,2024),(103,2025),(103,2026),(103,2027),(103,2028),(103,2029),(103,2030),(103,2031),(103,2032),(103,2033),(103,2034),(103,2035),(103,2036);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `enrollment` int NOT NULL DEFAULT '0' COMMENT '学籍总数',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2011 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`user_id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `enrollment`) VALUES (1,103,'admin','若依','00','ry@163.com','15888888888','1','/profile/avatar/2026/03/06/-t2tIbQv5v74580c3c3119d469920a02574f6ada64d1_20260306112604A002.png','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2026-03-16 22:25:18','admin','2026-03-01 15:28:45','','2026-03-16 14:25:18','管理员',5),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2026-03-01 15:28:45','admin','2026-03-01 15:28:45','',NULL,'测试员',0),(1001,NULL,'tutor1001','张同学','00','zhang1001@example.com','13800001001','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'大学生教员',0),(1002,NULL,'tutor1002','李老师','00','li1002@example.com','13800001002','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'专职教师',0),(1003,NULL,'tutor1003','王同学','00','wang1003@example.com','13800001003','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'大学生教员',0),(1004,NULL,'tutor1004','赵同学','00','zhao1004@example.com','13800001004','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'大学生教员',0),(1005,NULL,'tutor1005','陈老师','00','chen1005@example.com','13800001005','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'专职教师',0),(1006,NULL,'tutor1006','刘同学','00','liu1006@example.com','13800001006','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'大学生教员',0),(1007,NULL,'tutor1007','孙老师','00','sun1007@example.com','13800001007','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'兼职老师',0),(1008,NULL,'tutor1008','周同学','00','zhou1008@example.com','13800001008','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'大学生教员',0),(1009,NULL,'tutor1009','吴老师','00','wu1009@example.com','13800001009','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'在职教师',0),(1010,NULL,'tutor1010','郑同学','00','zheng1010@example.com','13800001010','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'大学生教员',0),(2001,NULL,'parent2001','张先生','00','parent2001@example.com','13900002001','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找初二数学家教',0),(2002,NULL,'parent2002','李女士','00','parent2002@example.com','13900002002','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找高一物理家教',0),(2003,NULL,'parent2003','王女士','00','parent2003@example.com','13900002003','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找小学英语家教',0),(2004,NULL,'parent2004','赵先生','00','parent2004@example.com','13900002004','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找初三全科家教',0),(2005,NULL,'parent2005','陈先生','00','parent2005@example.com','13900002005','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找高二化学家教',0),(2006,NULL,'parent2006','刘女士','00','parent2006@example.com','13900002006','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找五年级奥数家教',0),(2007,NULL,'parent2007','孙先生','00','parent2007@example.com','13900002007','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找初一语文家教',0),(2008,NULL,'parent2008','周女士','00','parent2008@example.com','13900002008','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找高三英语家教',0),(2009,NULL,'parent2009','吴女士','00','parent2009@example.com','13900002009','1','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找一年级语文家教',0),(2010,NULL,'parent2010','郑先生','00','parent2010@example.com','13900002010','0','','e10adc3949ba59abbe56e057f20f883e','0','0','127.0.0.1','2026-03-03 06:53:39','admin','2026-03-03 06:53:39','',NULL,'找初二物理家教',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trade_order`
--

DROP TABLE IF EXISTS `trade_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trade_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '内部订单ID，主键',
  `order_no` varchar(64) NOT NULL COMMENT '外部展示及支付网关使用的订单号（需唯一）',
  `user_id` bigint NOT NULL COMMENT '购买用户的唯一标识',
  `order_type` tinyint NOT NULL COMMENT '业务类型：1-沙龙(salon订单)，2-讲座(lecture订单)',
  `salon_id` bigint DEFAULT NULL COMMENT '关联的沙龙ID（当order_type=1时有值）',
  `lecture_id` bigint DEFAULT NULL COMMENT '关联的讲座ID（当order_type=2时有值）',
  `pay_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',
  `pay_method` varchar(32) DEFAULT NULL COMMENT '支付方式，如：wechat_pay, alipay, offline',
  `purpose` varchar(128) DEFAULT NULL COMMENT '主要的用途/备注，如：报名听课、赞助商',
  `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付，1-已支付，2-已退款，3-已取消',
  `pay_time` datetime DEFAULT NULL COMMENT '实际完成支付的时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_salon_id` (`salon_id`),
  KEY `idx_lecture_id` (`lecture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通用交易订单表（包含沙龙和讲座）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade_order`
--

LOCK TABLES `trade_order` WRITE;
/*!40000 ALTER TABLE `trade_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `trade_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutors`
--

DROP TABLE IF EXISTS `tutors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutors` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '教员表主键ID',
  `uid` int NOT NULL COMMENT '关联user表的主键ID',
  `title` varchar(20) DEFAULT NULL COMMENT '职称（如：教师、老师、大学生教员）',
  `certificates` varchar(255) DEFAULT NULL COMMENT '证书图片url',
  `subjects` varchar(40) DEFAULT NULL COMMENT '可授科目数组（使用索引0，1，2)',
  `areas` varchar(100) DEFAULT NULL COMMENT '可授区域数组（地区索引id）',
  `methods` int DEFAULT NULL COMMENT '授课方式（网络辅导、线下）',
  `is_certified` int DEFAULT '0' COMMENT '审核状态（待审核、已通过、已拒绝）',
  `salary` varchar(50) DEFAULT NULL COMMENT '薪资要求（如：100元/小时）',
  `experience` text COMMENT '经历/履历',
  `major` varchar(15) DEFAULT NULL COMMENT '专业',
  `school` varchar(15) DEFAULT NULL COMMENT '就读/毕业院校',
  `degree` tinyint DEFAULT '0' COMMENT '学历枚举：0-本科, 1-硕士, 2-博士',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `real_name` varchar(10) DEFAULT NULL COMMENT '实名认证真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `self_judge` text COMMENT '个人评价',
  `certificate` text COMMENT '证书',
  `live` varchar(10) DEFAULT NULL COMMENT '生活区域',
  `work` varchar(10) DEFAULT NULL,
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3193689978456321 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='大学生表/教员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutors`
--

LOCK TABLES `tutors` WRITE;
/*!40000 ALTER TABLE `tutors` DISABLE KEYS */;
INSERT INTO `tutors` (`id`, `uid`, `title`, `certificates`, `subjects`, `areas`, `methods`, `is_certified`, `salary`, `experience`, `major`, `school`, `degree`, `create_date`, `update_date`, `real_name`, `id_card`, `self_judge`, `certificate`, `live`, `work`, `province`) VALUES (31,1001,'大学生教员','https://img.com/c1.jpg','0,1,2','320115,320114',1,1,'100-150元/小时','2023.09至今在高三进行数学提分辅导，学生成绩从85提升至112分；2022年曾带初中奥数班。','数学与应用数学','南京大学',0,'2026-03-05 11:06:19','2026-03-05 11:06:19','张三','320101199901011234','认真负责，擅长逻辑引导','英语六级','江宁区','雨花台区',NULL),(32,1002,'专业教师','https://img.com/c2.jpg','3,5','320102,320104',0,1,'200元/小时','10年高中物理教学经验，曾任职于南京某重点中学，带过三届高三毕业班，熟悉高考考点。','物理学','南京师范大学',1,'2026-03-05 11:06:19','2026-03-05 11:06:19','李四','320101199505052345','教学幽默，深受学生喜爱','高级教师资格证','玄武区','秦淮区',NULL),(33,1003,'大学生教员',NULL,'1,4','320113',1,0,'80-120元/小时','大二开始从事家教，辅导过3名小学生的语数英全科作业，有良好的沟通能力。','英语','南京航空航天大学',0,'2026-03-05 11:06:19','2026-03-05 11:06:19','王五','320101200208083456','发音标准，亲和力强','专八证书','栖霞区','栖霞区',NULL),(34,1004,'金牌教员','https://img.com/c4.jpg','0,8','320106,320105',1,1,'300元/小时','长期担任奥数竞赛教练，多名学员获得省级一等奖；精通高中数学难题拆解。','计算机科学','东南大学',2,'2026-03-05 11:06:19','2026-03-05 11:06:19','赵六','320101199012124567','逻辑极强，擅长难题拆解','博士学位证','鼓楼区','建邺区',NULL),(35,1005,'大学生教员',NULL,'10,11','320104',0,2,'100元/小时','钢琴十级，在南京艺术学院就读期间长期兼职钢琴陪练，负责纠正指法。','音乐表演','南京艺术学院',0,'2026-03-05 11:06:19','2026-03-05 11:06:19','孙七','320101200103035678','极具耐心，艺术气息浓厚','钢琴十级证书','秦淮区','秦淮区',NULL),(36,1006,'专业教员','https://img.com/c6.jpg','2,6','320111',1,1,'150元/小时','南京本地化学老师，擅长初三化学考前冲刺，能快速帮助学生建立知识体系。','应用化学','南京理工大学',1,'2026-03-05 11:06:19','2026-03-05 11:06:19','周八','320101199604046789','重点难点把握精准','教师资格证','浦口区','浦口区',NULL),(37,1007,'大学生教员',NULL,'15','320115',0,0,'120元/小时','研二学生，考研政治88分，熟悉考研政治大纲，有一套独特的背诵方法。','马克思主义理论','河海大学',1,'2026-03-05 11:06:19','2026-03-05 11:06:19','吴九','320101199811117890','政治理论扎实，经验丰富','优秀毕业生','江宁区','江宁区',NULL),(38,1008,'专业教师','https://img.com/c8.jpg','7,9','320116',1,1,'250元/小时','资深生物老师，整理有全套初中生物知识归纳笔记，尤其擅长实验题讲解。','生物科学','中国药科大学',1,'2026-03-05 11:06:19','2026-03-05 11:06:19','郑十','320101199402028901','严谨负责，注重知识联想','高级职称证','六合区','江宁区',NULL),(39,1009,'大学生教员',NULL,'20','320115',0,1,'90元/小时','擅长少儿编程机器人辅导，曾带队参加省青少年科技创新大赛。','软件工程','南京邮电大学',0,'2026-03-05 11:06:19','2026-03-05 11:06:19','钱十一','320101200306069012','思维活跃，互动性强','省赛一等奖','栖霞区','江宁区',NULL),(40,1010,'大学生教员','https://img.com/c10.jpg','4,1','320114',1,1,'180元/小时','英语专业八级，雅思8.0，曾在南京某知名培训机构兼职托福口语老师。','英语教育','南京师范大学',1,'2026-03-05 11:06:19','2026-03-05 11:06:19','Mike','320101199709091111','发音纯正，口语地道','TESOL证书','雨花台区','雨花台区',NULL);
/*!40000 ALTER TABLE `tutors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` varchar(64) NOT NULL COMMENT '平台用户id',
  `user_name` varchar(64) NOT NULL DEFAULT '微信用户' COMMENT '用户名',
  `user_type` varchar(64) NOT NULL DEFAULT '1' COMMENT '用户类型',
  `phone` varchar(64) DEFAULT NULL COMMENT '手机号',
  `open_id` varchar(128) DEFAULT NULL COMMENT '微信用户唯一标识',
  `union_id` varchar(128) DEFAULT NULL COMMENT '微信全平台用户唯一标识',
  `avatar_url` varchar(256) DEFAULT 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0' COMMENT '用户头像',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_info_index_user_id` (`user_id`) USING BTREE,
  KEY `user_info_index_open_id` (`open_id`) USING BTREE,
  KEY `user_info_index_phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` (`id`, `user_id`, `user_name`, `user_type`, `phone`, `open_id`, `union_id`, `avatar_url`, `create_time`, `update_time`) VALUES (1,'6bccb998-86f5-4c16-ae46-2b056360130a','微信用户','1','18913320708','oATZg13XM1hD-57qwckYK30Ug_k8',NULL,'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0','2026-03-11 10:57:58','2026-03-11 10:57:58');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_balance`
--

DROP TABLE IF EXISTS `user_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint NOT NULL COMMENT '关联 user_info 表的 id',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '余额，单位：元',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `user_balance_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户余额表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_balance`
--

LOCK TABLES `user_balance` WRITE;
/*!40000 ALTER TABLE `user_balance` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_realname_auth`
--

DROP TABLE IF EXISTS `user_realname_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_realname_auth` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint NOT NULL COMMENT '用户ID，关联sys_user表',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `id_card` varchar(18) NOT NULL COMMENT '身份证号码',
  `auth_status` tinyint DEFAULT '0' COMMENT '认证状态：0-待审核，1-已通过，2-已驳回',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid` (`uid`) COMMENT '保证一个用户只能有一条认证记录',
  UNIQUE KEY `uk_id_card` (`id_card`) COMMENT '保证一个身份证号只能认证一次',
  CONSTRAINT `fk_auth_sys_user` FOREIGN KEY (`uid`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户实名认证表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_realname_auth`
--

LOCK TABLES `user_realname_auth` WRITE;
/*!40000 ALTER TABLE `user_realname_auth` DISABLE KEYS */;
INSERT INTO `user_realname_auth` (`id`, `uid`, `real_name`, `id_card`, `auth_status`, `create_time`, `update_time`) VALUES (1,1001,'张同学','110105200501010011',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(2,1002,'李老师','110105199001020022',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(3,1003,'王同学','110105200501030033',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(4,1004,'赵同学','110105200501040044',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(5,1005,'陈老师','110105199001050055',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(6,1006,'刘同学','110105200501060066',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(7,1007,'孙老师','110105199001070077',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(8,1008,'周同学','110105200501080088',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(9,1009,'吴老师','110105199001090099',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(10,1010,'郑同学','11010520050110010X',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(11,2001,'张先生','110105198001010011',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(12,2002,'李女士','110105198001020022',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(13,2003,'王女士','110105198001030033',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(14,2004,'赵先生','110105198001040044',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(15,2005,'陈先生','110105198001050055',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(16,2006,'刘女士','110105198001060066',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(17,2007,'孙先生','110105198001070077',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(18,2008,'周女士','110105198001080088',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(19,2009,'吴女士','110105198001090099',1,'2026-03-06 03:46:02','2026-03-06 03:46:02'),(20,2010,'郑先生','11010519800110010X',1,'2026-03-06 03:46:02','2026-03-06 03:46:02');
/*!40000 ALTER TABLE `user_realname_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_wallet`
--

DROP TABLE IF EXISTS `user_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint NOT NULL COMMENT '用户ID，关联sys_user表',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '可用余额（元）',
  `frozen` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额（元，提现审核中）',
  `total_earned` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计收入（元）',
  `total_withdrawn` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计提现（元）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_wallet`
--

LOCK TABLES `user_wallet` WRITE;
/*!40000 ALTER TABLE `user_wallet` DISABLE KEYS */;
INSERT INTO `user_wallet` (`id`, `uid`, `balance`, `frozen`, `total_earned`, `total_withdrawn`, `create_time`, `update_time`) VALUES (1,1,0.00,0.00,0.00,0.00,'2026-03-11 02:05:48','2026-03-11 02:05:48');
/*!40000 ALTER TABLE `user_wallet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-18 15:05:10
