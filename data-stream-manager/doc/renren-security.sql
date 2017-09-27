/*
Navicat MySQL Data Transfer

Source Server         : 192.168.102.121
Source Server Version : 50718
Source Host           : 192.168.102.121:3306
Source Database       : renren-security

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-09-26 17:16:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for application_key
-- ----------------------------
DROP TABLE IF EXISTS `application_key`;
CREATE TABLE `application_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `private_key` varchar(3000) DEFAULT NULL,
  `public_key` varchar(2048) DEFAULT NULL,
  `passphrase` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of application_key
-- ----------------------------
INSERT INTO `application_key` VALUES ('2', '-----BEGIN RSA PRIVATE KEY-----\nProc-Type: 4,ENCRYPTED\nDEK-Info: DES-EDE3-CBC,BE6F9324E6973B2D\n\nhssRPdkeD6FS6/KyKFZhB1yyMKt7H/rseTqwQeDQ8U4hSlrQZ4KYolCCdEQTEFzc\nyL8Qw8/waz+Iy2T8JjWQKyxmtFJjFxXhWhv2aB1+UlO3rO/cbJb59cxrk3NyJNiU\nWG+brWCX6aasEtP2zBdV1jK9v5trXaRw45eVyV0ngcBtfWDChRELYLlYmsk69Hx9\n0QMsc2EezzOt5T+kdBWMU7V8iFNRhfjblqp839z1BqC7Z5DNcLSsCyTYL0tIvd1v\nwuBrwHqTcHcLsknkX0f8sdqDc4PxjX043aY2T+BJQkTjiQny3WpT/o7my1GJqKS/\nj6pkoIz3yI8Hra0qcLwwOaErg688xCYg1uJkoCRyruhNAt/8y7GplpOlPw5C3F2p\njFoWzlcdTfmY4j4Lez/Ntk1mDQ4tRY8KFDnaphsz4TpBCCZCk5RltygmeJOKEg2Z\n/Pzho7+1iK+yhZHtMRWamqfCFFk6CJqHe+3YFIpEygjTtC+Z1uo5tQAETFhtbDNi\n9A7kvqDN+2HCQzdOJu9L7HRxYuD5us97qOKjXnjIVSeMIyB2gUD12aqWfpogFgza\n0rUoiObKPkWNVwLM7vyY0ogooEfwINy1LbeKF2jwbXQUTqVwwVZKbKSyJufrBQR7\nMYaUUEg0zUVxpmT0fysq9NUfGNrhsTondl7X0w5qIJbR1nJbLOekA63piU/HnT25\ntrYWitmG4oHxhxN32g6KUkMjM56QxIXE/KaQr5ogAZG7GOZ6IwLyoPQVRktbXE+M\nIcwRwvxeAuHmFxUsnuxPK2/JRvAwi/yvs0WZa4XEikaFuef+Oo9DeTCFoG66bYTt\nKDOVS853LJVw0AGRuOWnhkchSEoEqGVA3+8e2z3uCm7JsqUqOm8vkTkCGyVbfS6j\nAE7/Mi1iXIOzSvFUaNdWegpdPwetZGB9y3yhxJzhoVG91cjx4z5MXMgr9z1mO2V+\n8e3wKXxnJK15X90GQoS92yU0oOE5jcPb99Rd8ojp8ZPusGhX7SfvEkaGCfyzAT8J\nKJzZ90CxVSmJTa8RUb6KlXfjPQf8tZd5BxxPSUCo0Y9jnKZSFDn+6endKZ9jtt/z\nLFdQtUO4PPnVn8Gv/aTFzmUGIS6ZDAR/vA3r9jRs6Er3H+Oe+Aosyc3IXeGevlpt\npxY+45mwgth5pLaE5hPL+0sZv4c+rCDVsYE3saCoc5qawMQJK6+pJ7uFGslwYe4q\nHKzgozem5QPdikybZcqR9yUGInWjHY9QFaQi3Af+pSov1FEldeoIN/AMmoTZjvm3\nOLTmB6MnKKvgB4xlZQ3CowkrsPxS9OVUpJRUw7WAJhtHK0E3qcMCm1ivEh9ihYs9\nM0wT8d0mTNdRmSXcC9hvIyWp0S4wR3Mam6boxX6ZgDQAX9xHoYNEcIuGTOCUpkQm\nYpOWHhldN7Bm/JTjvd0wOyl5uhw1996pdDQFAjVVHXNDiKVNIaD7efFEmRMdKIKR\nuj9GoSjR3mt59HTQgOGLlz5J47eGSlzVvHHs5FIBTEl1s3abzUMwRr9OzilZzLKT\n4wMbdc9HEvEAnKG/Xn61Gw9560+1TpGAQC+WqyQ9YTO24g+FTeawqzVHJyLUT5bz\n-----END RSA PRIVATE KEY-----', 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC5BavbU3BCOZpFYDuYCVaw/BtkrnJmVhCzK9i6RZpWOjnOLGpJPG/cgSRv5r0QbcPeTAXYnmScNhPntR30uWGGw7P+qqkQ+l0FSYH/S72Skj83Wzwd3M9X7ILDh1uD+130ySXLLINCjbEf+ECbFUdL42ad91D/FK25GtPGrOvUN0EIy8dgBfQCjMZGQv6cJbXE4iBCDJELJYx1pZCBZ2+GzHfuj3Ld77bEy4Bh0FZSwJiyYCXF1ADvqQOBJbh6vrVZqRHs4R/thkuBlkVdJv3vLo9EX2HAHDFlnwjJw+WktWLPz7wRB04TXnCRS524QZM9c9TXx0JFuVi4d6RnYcTf keybox@global_key', '4158bf50-2072-4850-83cb-3717fc2863ef');

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_CRON_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_18', 'DEFAULT', '0/1 * * * * ? *', 'America/New_York');

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('RenrenScheduler', 'TASK_18', 'DEFAULT', null, 'com.yss.quartz.ScheduleJob', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372001D636F6D2E7973732E656E746974792E537061726B4A6F62456E74697479000000000000000102000F4C0009636C617373417267737400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C0010647269766572446F636B6572547970657400134C6A6176612F6C616E672F496E74656765723B4C00126578656375746F72446F636B65725479706571007E000B4C000B6578656375746F724E756D71007E000B4C000B6A617246696C654E616D6571007E00094C000B6A6F62446573637269707471007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000E6C61737455706461746554696D6571007E000A4C00096D61696E436C61737371007E00094C0009737061726B436F6E6671007E00094C000673746174757371007E000B4C000675736572496471007E000C7870707372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000015E7ACEECD87874000F302F31202A202A202A202A203F202A737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000071007E00137371007E00110000000174001D2F726F6F742F737061726B2D7973732D6170702D73686164652E6A6172740006E6B58BE8AF957372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C75657871007E00120000000000000012740006E6B58BE8AF957371007E000E77080000015E7DFB2A7078740015636F6D2E7973732E7961726E2E5961726E546573747071007E00147371007E001700000000000000017800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `QRTZ_LOCKS` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------
INSERT INTO `QRTZ_SCHEDULER_STATE` VALUES ('RenrenScheduler', 'bogon1506047286558', '1506417355331', '15000');

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_TRIGGERS` VALUES ('RenrenScheduler', 'TASK_18', 'DEFAULT', 'TASK_18', 'DEFAULT', null, '1506047286000', '-1', '5', 'PAUSED', 'CRON', '1506047286000', '0', null, '2', '');

-- ----------------------------
-- Table structure for spark_job
-- ----------------------------
DROP TABLE IF EXISTS `spark_job`;
CREATE TABLE `spark_job` (
  `job_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(255) NOT NULL COMMENT 'task名称',
  `job_descript` varchar(255) DEFAULT NULL,
  `jar_file_name` varchar(255) NOT NULL,
  `main_class` varchar(255) NOT NULL,
  `class_args` varchar(255) DEFAULT NULL,
  `driver_docker_type` int(4) NOT NULL,
  `executor_docker_type` int(4) NOT NULL,
  `executor_num` int(11) NOT NULL,
  `spark_conf` varchar(255) DEFAULT NULL,
  `cron_expression` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spark_job
-- ----------------------------
INSERT INTO `spark_job` VALUES ('18', '测试', '测试', '/root/spark-yss-app-shade.jar', 'com.yss.yarn.YarnTest', null, '0', '0', '1', null, '0/1 * * * * ? *', '1', '1', '2017-09-13 06:35:35', '2017-09-13 21:22:46');

-- ----------------------------
-- Table structure for spark_job_log
-- ----------------------------
DROP TABLE IF EXISTS `spark_job_log`;
CREATE TABLE `spark_job_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL,
  `job_name` varchar(255) NOT NULL,
  `yarn_app_id` varchar(255) DEFAULT NULL,
  `app_name` varchar(255) DEFAULT NULL,
  `main_class` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `tracking_ui` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3116 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spark_job_log
-- ----------------------------
INSERT INTO `spark_job_log` VALUES ('3100', '18', '测试', 'application_1505715230071_0011', null, 'com.yss.yarn.YarnTest', null, 'KILLED', 'http://yarn2:4040', '2017-09-18 05:03:13', '2017-09-18 05:04:25', '2017-09-18 05:04:25');
INSERT INTO `spark_job_log` VALUES ('3101', '18', '测试', 'application_1505715230071_0012', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn1:4040', '2017-09-18 05:11:17', '2017-09-18 05:14:08', '2017-09-18 05:14:08');
INSERT INTO `spark_job_log` VALUES ('3102', '18', '测试', 'application_1505715230071_0013', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn1:4040', '2017-09-18 05:17:10', '2017-09-18 05:19:24', '2017-09-18 05:19:24');
INSERT INTO `spark_job_log` VALUES ('3103', '18', '测试', 'application_1505715230071_0014', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn1:4040', '2017-09-18 22:31:21', '2017-09-18 22:34:18', '2017-09-18 22:34:18');
INSERT INTO `spark_job_log` VALUES ('3104', '18', '测试', 'application_1505715230071_0015', null, 'com.yss.yarn.YarnTest', null, 'KILLED', 'http://yarn2:4040', '2017-09-18 22:35:35', '2017-09-18 22:37:10', '2017-09-18 22:37:10');
INSERT INTO `spark_job_log` VALUES ('3105', '18', '测试', 'application_1505715230071_0016', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn2:4040', '2017-09-18 22:40:36', '2017-09-18 22:43:12', '2017-09-18 22:43:12');
INSERT INTO `spark_job_log` VALUES ('3106', '18', '测试', 'application_1505715230071_0017', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://192.168.103.226:4040', '2017-09-18 23:16:49', '2017-09-18 23:19:22', '2017-09-18 23:19:21');
INSERT INTO `spark_job_log` VALUES ('3107', '18', '测试', 'application_1505715230071_0018', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn2:4040', '2017-09-19 01:25:51', '2017-09-19 01:28:59', '2017-09-19 01:28:59');
INSERT INTO `spark_job_log` VALUES ('3108', '18', '测试', 'application_1505715230071_0019', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn2:4040', '2017-09-19 04:13:32', '2017-09-19 04:16:46', '2017-09-19 04:16:46');
INSERT INTO `spark_job_log` VALUES ('3109', '18', '测试', 'application_1505715230071_0020', null, 'com.yss.yarn.YarnTest', null, 'KILLED', 'http://192.168.103.226:4040', '2017-09-19 04:21:30', '2017-09-19 04:23:53', '2017-09-19 04:23:52');
INSERT INTO `spark_job_log` VALUES ('3110', '18', '测试', 'application_1505715230071_0021', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn2:4040', '2017-09-19 21:16:29', '2017-09-19 21:20:01', '2017-09-19 21:20:01');
INSERT INTO `spark_job_log` VALUES ('3111', '18', '测试', 'application_1505715230071_0022', null, 'com.yss.yarn.YarnTest', null, 'KILLED', 'http://192.168.103.226:4040', '2017-09-19 23:33:51', '2017-09-19 23:36:49', '2017-09-19 23:36:48');
INSERT INTO `spark_job_log` VALUES ('3112', '18', '测试', 'application_1505715230071_0023', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://192.168.103.226:4040', '2017-09-20 22:26:39', '2017-09-20 22:29:43', '2017-09-20 22:29:43');
INSERT INTO `spark_job_log` VALUES ('3114', '18', '测试', 'application_1505715230071_0025', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://yarn2:4040', '2017-09-21 22:30:06', '2017-09-21 22:33:22', '2017-09-21 22:33:21');
INSERT INTO `spark_job_log` VALUES ('3115', '18', '测试', 'application_1505715230071_0026', null, 'com.yss.yarn.YarnTest', null, 'SUCCEEDED', 'http://192.168.103.226:4040', '2017-09-22 01:40:16', '2017-09-22 01:43:23', '2017-09-22 01:43:22');

-- ----------------------------
-- Table structure for status
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `status_cd` varchar(255) NOT NULL DEFAULT 'INITIAL',
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES ('22', '1', 'SUCCESS');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('79', 'admin', '保存菜单', 'com.yss.controller.SysMenuController.save()', '{\"name\":\"SSH\",\"orderNum\":0,\"parentId\":1,\"parentName\":\"系统管理\",\"type\":1,\"url\":\"view/system\"}', '0:0:0:0:0:0:0:1', '2017-09-05 13:59:39');
INSERT INTO `sys_log` VALUES ('80', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"menuId\":35,\"name\":\"SSH工具\",\"orderNum\":0,\"parentId\":1,\"parentName\":\"系统管理\",\"type\":1,\"url\":\"view/system\"}', '0:0:0:0:0:0:0:1', '2017-09-05 16:37:22');
INSERT INTO `sys_log` VALUES ('81', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"icon\":\"eye\",\"menuId\":35,\"name\":\"SSH工具\",\"orderNum\":0,\"parentId\":1,\"parentName\":\"系统管理\",\"type\":1,\"url\":\"view/system\"}', '0:0:0:0:0:0:0:1', '2017-09-07 15:02:21');
INSERT INTO `sys_log` VALUES ('82', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"icon\":\"fa fa-eye\",\"menuId\":35,\"name\":\"SSH工具\",\"orderNum\":0,\"parentId\":1,\"parentName\":\"系统管理\",\"type\":1,\"url\":\"view/system\"}', '0:0:0:0:0:0:0:1', '2017-09-07 15:03:15');
INSERT INTO `sys_log` VALUES ('83', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"icon\":\"fa fa-clock-o\",\"menuId\":32,\"name\":\"定时任务\",\"orderNum\":0,\"parentId\":31,\"parentName\":\"Spark\",\"perms\":\"sys:spark,sys.spark.list\",\"type\":1,\"url\":\"sys/schedule.html\"}', '0:0:0:0:0:0:0:1', '2017-09-07 15:04:13');
INSERT INTO `sys_log` VALUES ('84', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"icon\":\"fa fa-area-chart\",\"menuId\":34,\"name\":\"应用监控\",\"orderNum\":0,\"parentId\":31,\"parentName\":\"Spark\",\"type\":1,\"url\":\"/sys/yarn/apps\"}', '0:0:0:0:0:0:0:1', '2017-09-07 15:07:20');
INSERT INTO `sys_log` VALUES ('85', 'admin', '暂停定时任务', 'com.yss.controller.ScheduleTaskController.pause()', '[11]', '0:0:0:0:0:0:0:1', '2017-09-07 17:53:22');
INSERT INTO `sys_log` VALUES ('86', 'admin', '修改定时任务', 'com.yss.controller.ScheduleTaskController.update()', '{\"classArgs\":\"\",\"createTime\":1503542633000,\"cronExpression\":\"0 0 12 *\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":3,\"jarFileName\":\"spark-app.jar\",\"lastUpdateTime\":1504734800000,\"mainClass\":\"com.yss.yarn.Test\",\"status\":1,\"taskDescript\":\"测试2\",\"taskId\":11,\"taskName\":\"测试\",\"userId\":1}', '0:0:0:0:0:0:0:1', '2017-09-08 10:16:52');
INSERT INTO `sys_log` VALUES ('87', 'admin', '修改定时任务', 'com.yss.controller.ScheduleTaskController.update()', '{\"classArgs\":\"\",\"createTime\":1503542633000,\"cronExpression\":\"0/1 0/1 * * * ?\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":3,\"jarFileName\":\"spark-app.jar\",\"lastUpdateTime\":1504793810000,\"mainClass\":\"com.yss.yarn.Test\",\"status\":1,\"taskDescript\":\"测试2\",\"taskId\":11,\"taskName\":\"测试\",\"userId\":1}', '0:0:0:0:0:0:0:1', '2017-09-08 10:18:44');
INSERT INTO `sys_log` VALUES ('88', 'admin', '恢复定时任务', 'com.yss.controller.ScheduleTaskController.resume()', '[11]', '0:0:0:0:0:0:0:1', '2017-09-08 10:18:51');
INSERT INTO `sys_log` VALUES ('89', 'admin', '删除定时任务', 'com.yss.controller.ScheduleTaskController.delete()', '[11]', '0:0:0:0:0:0:0:1', '2017-09-08 10:19:21');
INSERT INTO `sys_log` VALUES ('90', 'admin', '保存spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"d\",\"cronExpression\":\"0/1 0/1 * * * ?\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"a\",\"mainClass\":\"c\",\"taskDescript\":\"测试1\",\"taskName\":\"测试\"}', '0:0:0:0:0:0:0:1', '2017-09-08 10:19:53');
INSERT INTO `sys_log` VALUES ('91', 'admin', '保存spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"e\",\"cronExpression\":\"0/1 0/1 * * * ?\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"e\",\"mainClass\":\"e\",\"taskDescript\":\"de\",\"taskName\":\"ce\"}', '0:0:0:0:0:0:0:1', '2017-09-08 10:35:04');
INSERT INTO `sys_log` VALUES ('92', 'admin', '保存spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"1\",\"cronExpression\":\"0/1 0/1 * * * ? \",\"driverDockerType\":1,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"1\",\"mainClass\":\"1\"}', '0:0:0:0:0:0:0:1', '2017-09-08 14:16:35');
INSERT INTO `sys_log` VALUES ('93', 'admin', '保存spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"1\",\"cronExpression\":\"0/1 0/1 * * * ?\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"1\",\"jobDescript\":\"1\",\"jobName\":\"w\",\"mainClass\":\"1\"}', '0:0:0:0:0:0:0:1', '2017-09-08 14:18:20');
INSERT INTO `sys_log` VALUES ('94', 'admin', '保存spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"ee\",\"cronExpression\":\"0/1 0/1 * * * ?\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"ee\",\"jobDescript\":\"ee\",\"jobName\":\"se\",\"mainClass\":\"ee\"}', '0:0:0:0:0:0:0:1', '2017-09-08 14:58:07');
INSERT INTO `sys_log` VALUES ('95', 'admin', '保存spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"sd\",\"cronExpression\":\"0/1 0/1 * * * ? \",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"sd\",\"jobDescript\":\"sd\",\"jobName\":\"sd\",\"mainClass\":\"sd\",\"sparkConf\":\"sd\"}', '0:0:0:0:0:0:0:1', '2017-09-08 15:05:04');
INSERT INTO `sys_log` VALUES ('96', 'admin', '暂停定时任务', 'com.yss.controller.ScheduleTaskController.pause()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:08:45');
INSERT INTO `sys_log` VALUES ('97', 'admin', '恢复定时任务', 'com.yss.controller.ScheduleTaskController.resume()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:15:50');
INSERT INTO `sys_log` VALUES ('98', 'admin', '暂停定时任务', 'com.yss.controller.ScheduleTaskController.pause()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:16:01');
INSERT INTO `sys_log` VALUES ('99', 'admin', '恢复定时任务', 'com.yss.controller.ScheduleTaskController.resume()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:16:23');
INSERT INTO `sys_log` VALUES ('100', 'admin', '暂停定时任务', 'com.yss.controller.ScheduleTaskController.pause()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:16:37');
INSERT INTO `sys_log` VALUES ('101', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:25:45');
INSERT INTO `sys_log` VALUES ('102', 'admin', '暂停定时任务', 'com.yss.controller.ScheduleTaskController.pause()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:25:53');
INSERT INTO `sys_log` VALUES ('103', 'admin', '恢复定时任务', 'com.yss.controller.ScheduleTaskController.resume()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:26:03');
INSERT INTO `sys_log` VALUES ('104', 'admin', '恢复定时任务', 'com.yss.controller.ScheduleTaskController.resume()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:26:12');
INSERT INTO `sys_log` VALUES ('105', 'admin', '删除定时任务', 'com.yss.controller.ScheduleTaskController.delete()', '[16]', '0:0:0:0:0:0:0:1', '2017-09-08 15:26:15');
INSERT INTO `sys_log` VALUES ('106', 'admin', '新增spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"classArgs\":\"d\",\"cronExpression\":\"0/1 0/1 * * * ?\",\"driverDockerType\":0,\"executorDockerType\":0,\"executorNum\":1,\"jarFileName\":\"d\",\"jobDescript\":\"d\",\"jobName\":\"d\",\"mainClass\":\"d\",\"sparkConf\":\"1\"}', '0:0:0:0:0:0:0:1', '2017-09-08 16:10:56');
INSERT INTO `sys_log` VALUES ('107', 'admin', '保存用户', 'com.yss.controller.SysUserController.save()', '{\"email\":\"lixj@ysstech.com\",\"mobile\":\"135\",\"roleIdList\":[1],\"status\":1,\"username\":\"lixj\"}', '0:0:0:0:0:0:0:1', '2017-09-11 10:00:36');
INSERT INTO `sys_log` VALUES ('108', 'admin', '修改角色', 'com.yss.controller.SysRoleController.update()', '{\"createTime\":1503044172000,\"createUserId\":1,\"menuIdList\":[1,35,2,15,16,17,18,3,19,20,21,22,4,23,24,25,26,30,29,28,7,31,8,9,10,11,12,13,14,33,34],\"roleId\":1,\"roleName\":\"admin\"}', '0:0:0:0:0:0:0:1', '2017-09-11 10:03:54');
INSERT INTO `sys_log` VALUES ('109', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"icon\":\"fa fa-clock-o\",\"menuId\":32,\"name\":\"定时任务\",\"orderNum\":0,\"parentId\":31,\"parentName\":\"Spark\",\"perms\":\"sys:spark\",\"type\":1,\"url\":\"sys/schedule.html\"}', '0:0:0:0:0:0:0:1', '2017-09-11 10:06:32');
INSERT INTO `sys_log` VALUES ('110', 'admin', '修改角色', 'com.yss.controller.SysRoleController.update()', '{\"createTime\":1503044172000,\"createUserId\":1,\"menuIdList\":[1,35,2,15,16,17,18,3,19,20,21,22,4,23,24,25,26,30,29,28,7,31,32,8,9,10,11,12,13,14,33,34],\"roleId\":1,\"roleName\":\"admin\"}', '0:0:0:0:0:0:0:1', '2017-09-11 10:16:33');
INSERT INTO `sys_log` VALUES ('111', 'admin', '新增spark定时任务', 'com.yss.controller.ScheduleTaskController.save()', '{\"cronExpression\":\"0/1 * * * * ? *\",\"driverDockerType\":0,\"executorCore\":\"1\",\"executorDockerType\":0,\"executorMemory\":\"2048M\",\"executorNum\":1,\"jarFileName\":\"/root/spark-yss-app-shade.jar\",\"jobDescript\":\"测试\",\"jobName\":\"测试\",\"mainClass\":\"com.yss.yarn.YarnTest\"}', '192.168.101.129', '2017-09-13 06:35:35');
INSERT INTO `sys_log` VALUES ('112', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-13 06:39:54');
INSERT INTO `sys_log` VALUES ('113', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-13 06:40:11');
INSERT INTO `sys_log` VALUES ('114', 'admin', '暂停定时任务', 'com.yss.controller.ScheduleTaskController.pause()', '[18]', '192.168.101.129', '2017-09-13 21:22:47');
INSERT INTO `sys_log` VALUES ('115', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 03:21:38');
INSERT INTO `sys_log` VALUES ('116', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 03:22:17');
INSERT INTO `sys_log` VALUES ('117', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 03:28:02');
INSERT INTO `sys_log` VALUES ('118', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 03:38:27');
INSERT INTO `sys_log` VALUES ('119', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 03:46:04');
INSERT INTO `sys_log` VALUES ('120', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 03:52:04');
INSERT INTO `sys_log` VALUES ('121', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 04:41:55');
INSERT INTO `sys_log` VALUES ('122', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 05:19:06');
INSERT INTO `sys_log` VALUES ('123', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 05:23:47');
INSERT INTO `sys_log` VALUES ('124', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 05:29:23');
INSERT INTO `sys_log` VALUES ('125', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 05:32:04');
INSERT INTO `sys_log` VALUES ('126', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 06:00:39');
INSERT INTO `sys_log` VALUES ('127', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 06:05:10');
INSERT INTO `sys_log` VALUES ('128', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 06:29:46');
INSERT INTO `sys_log` VALUES ('129', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 21:00:34');
INSERT INTO `sys_log` VALUES ('130', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 21:36:59');
INSERT INTO `sys_log` VALUES ('131', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 21:58:09');
INSERT INTO `sys_log` VALUES ('132', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-14 22:14:28');
INSERT INTO `sys_log` VALUES ('133', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-15 05:39:58');
INSERT INTO `sys_log` VALUES ('134', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-16 22:44:24');
INSERT INTO `sys_log` VALUES ('135', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 03:12:48');
INSERT INTO `sys_log` VALUES ('136', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 03:45:53');
INSERT INTO `sys_log` VALUES ('137', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 03:51:23');
INSERT INTO `sys_log` VALUES ('138', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 03:55:08');
INSERT INTO `sys_log` VALUES ('139', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 05:03:13');
INSERT INTO `sys_log` VALUES ('140', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 05:11:17');
INSERT INTO `sys_log` VALUES ('141', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 05:17:10');
INSERT INTO `sys_log` VALUES ('142', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 22:31:21');
INSERT INTO `sys_log` VALUES ('143', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 22:35:35');
INSERT INTO `sys_log` VALUES ('144', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 22:40:36');
INSERT INTO `sys_log` VALUES ('145', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-18 23:16:49');
INSERT INTO `sys_log` VALUES ('146', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-19 01:25:51');
INSERT INTO `sys_log` VALUES ('147', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-19 04:13:32');
INSERT INTO `sys_log` VALUES ('148', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-19 04:21:30');
INSERT INTO `sys_log` VALUES ('149', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-19 21:16:29');
INSERT INTO `sys_log` VALUES ('150', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-19 23:33:51');
INSERT INTO `sys_log` VALUES ('151', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-20 22:26:39');
INSERT INTO `sys_log` VALUES ('152', 'admin', '修改菜单', 'com.yss.controller.SysMenuController.update()', '{\"icon\":\"fa fa-fire\",\"menuId\":31,\"name\":\"Spark\",\"orderNum\":0,\"parentId\":0,\"parentName\":\"一级菜单\",\"type\":0}', '192.168.101.129', '2017-09-20 22:35:46');
INSERT INTO `sys_log` VALUES ('153', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-21 22:09:55');
INSERT INTO `sys_log` VALUES ('154', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-21 22:30:06');
INSERT INTO `sys_log` VALUES ('155', 'admin', '立即执行定时任务', 'com.yss.controller.ScheduleTaskController.run()', '[18]', '192.168.101.129', '2017-09-22 01:40:16');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', null, null, '0', 'fa fa-cog', '0');
INSERT INTO `sys_menu` VALUES ('2', '1', '用户列表', 'sys/user.html', null, '1', 'fa fa-user', '1');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'sys/role.html', null, '1', 'fa fa-user-secret', '2');
INSERT INTO `sys_menu` VALUES ('4', '1', '菜单管理', 'sys/menu.html', null, '1', 'fa fa-th-list', '3');
INSERT INTO `sys_menu` VALUES ('7', '6', '查看', null, 'sys:schedule:list,sys:schedule:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('8', '32', '新增', null, 'sys:spark:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('9', '32', '修改', null, 'sys:spark:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('10', '32', '删除', null, 'sys:spark:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('11', '32', '暂停', null, 'sys:spark:pause', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('12', '32', '恢复', null, 'sys:spark:resume', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('13', '32', '立即执行', null, 'sys:spark:run', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('14', '32', '日志列表', null, 'sys:spark:log', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('15', '2', '查看', null, 'sys:user:list,sys:user:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('16', '2', '新增', null, 'sys:user:save,sys:role:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('17', '2', '修改', null, 'sys:user:update,sys:role:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('18', '2', '删除', null, 'sys:user:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('19', '3', '查看', null, 'sys:role:list,sys:role:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('20', '3', '新增', null, 'sys:role:save,sys:menu:perms', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('21', '3', '修改', null, 'sys:role:update,sys:menu:perms', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('22', '3', '删除', null, 'sys:role:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('23', '4', '查看', null, 'sys:menu:list,sys:menu:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('24', '4', '新增', null, 'sys:menu:save,sys:menu:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('25', '4', '修改', null, 'sys:menu:update,sys:menu:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('26', '4', '删除', null, 'sys:menu:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('28', '1', '集群管理', 'sys/generator.html', 'sys:generator:list,sys:generator:code', '1', 'fa fa-rocket', '8');
INSERT INTO `sys_menu` VALUES ('29', '1', '系统日志', 'sys/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');
INSERT INTO `sys_menu` VALUES ('30', '1', '文件上传', 'sys/oss.html', 'sys:oss:all', '1', 'fa fa-file-image-o', '6');
INSERT INTO `sys_menu` VALUES ('31', '0', 'Spark', null, null, '0', 'fa fa-fire', '0');
INSERT INTO `sys_menu` VALUES ('32', '31', '定时任务', 'sys/schedule.html', 'sys:spark', '1', 'fa fa-clock-o', '0');
INSERT INTO `sys_menu` VALUES ('33', '31', '单次任务', '/sys/temptask.html', null, '1', null, '0');
INSERT INTO `sys_menu` VALUES ('34', '31', '应用监控', '/sys/yarn/apps', null, '1', 'fa fa-area-chart', '0');
INSERT INTO `sys_menu` VALUES ('35', '1', 'SSH工具', 'view/system', null, '1', 'fa fa-eye', '0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'admin', null, '1', '2017-08-18 16:16:12');
INSERT INTO `sys_role` VALUES ('2', 'spark', null, '1', '2017-08-18 16:18:07');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('31', '2', '1');
INSERT INTO `sys_role_menu` VALUES ('34', '2', '7');
INSERT INTO `sys_role_menu` VALUES ('35', '2', '8');
INSERT INTO `sys_role_menu` VALUES ('36', '2', '9');
INSERT INTO `sys_role_menu` VALUES ('37', '2', '10');
INSERT INTO `sys_role_menu` VALUES ('38', '2', '11');
INSERT INTO `sys_role_menu` VALUES ('39', '2', '12');
INSERT INTO `sys_role_menu` VALUES ('40', '2', '13');
INSERT INTO `sys_role_menu` VALUES ('41', '2', '14');
INSERT INTO `sys_role_menu` VALUES ('135', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('136', '1', '35');
INSERT INTO `sys_role_menu` VALUES ('137', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('138', '1', '15');
INSERT INTO `sys_role_menu` VALUES ('139', '1', '16');
INSERT INTO `sys_role_menu` VALUES ('140', '1', '17');
INSERT INTO `sys_role_menu` VALUES ('141', '1', '18');
INSERT INTO `sys_role_menu` VALUES ('142', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('143', '1', '19');
INSERT INTO `sys_role_menu` VALUES ('144', '1', '20');
INSERT INTO `sys_role_menu` VALUES ('145', '1', '21');
INSERT INTO `sys_role_menu` VALUES ('146', '1', '22');
INSERT INTO `sys_role_menu` VALUES ('147', '1', '4');
INSERT INTO `sys_role_menu` VALUES ('148', '1', '23');
INSERT INTO `sys_role_menu` VALUES ('149', '1', '24');
INSERT INTO `sys_role_menu` VALUES ('150', '1', '25');
INSERT INTO `sys_role_menu` VALUES ('151', '1', '26');
INSERT INTO `sys_role_menu` VALUES ('152', '1', '30');
INSERT INTO `sys_role_menu` VALUES ('153', '1', '29');
INSERT INTO `sys_role_menu` VALUES ('154', '1', '28');
INSERT INTO `sys_role_menu` VALUES ('155', '1', '7');
INSERT INTO `sys_role_menu` VALUES ('156', '1', '31');
INSERT INTO `sys_role_menu` VALUES ('157', '1', '32');
INSERT INTO `sys_role_menu` VALUES ('158', '1', '8');
INSERT INTO `sys_role_menu` VALUES ('159', '1', '9');
INSERT INTO `sys_role_menu` VALUES ('160', '1', '10');
INSERT INTO `sys_role_menu` VALUES ('161', '1', '11');
INSERT INTO `sys_role_menu` VALUES ('162', '1', '12');
INSERT INTO `sys_role_menu` VALUES ('163', '1', '13');
INSERT INTO `sys_role_menu` VALUES ('164', '1', '14');
INSERT INTO `sys_role_menu` VALUES ('165', '1', '33');
INSERT INTO `sys_role_menu` VALUES ('166', '1', '34');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'root@ysstech.com', '13612345678', '1', '1', '2016-11-11 11:11:11');
INSERT INTO `sys_user` VALUES ('2', 'lixj', 'd2b9f0d74c3d57c8fabb7c64bc81b1463c97652b1afb36b66c5ed9542f1f6cd1', 'lixj@ysstech.com', '135', '1', '1', '2017-09-11 10:00:36');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '2', '1');

-- ----------------------------
-- Table structure for system
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `display_nm` varchar(255) NOT NULL,
  `add_user_id` int(11) NOT NULL,
  `user` varchar(255) NOT NULL,
  `host` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `authorized_keys` varchar(255) NOT NULL,
  `status_cd` varchar(255) NOT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system
-- ----------------------------
INSERT INTO `system` VALUES ('22', 'yarn1', '1', 'root', '192.168.103.140', '22', '~/.ssh/authorized_keys', 'SUCCESS', '2017-09-18 05:05:46');
INSERT INTO `system` VALUES ('23', 'yarn2', '1', 'root', '192.168.103.166', '22', '~/.ssh/authorized_keys', 'SUCCESS', '2017-09-18 05:06:12');
INSERT INTO `system` VALUES ('24', 'yarn3', '1', 'root', '192.168.103.226', '22', '~/.ssh/authorized_keys', 'SUCCESS', '2017-09-21 22:22:42');
INSERT INTO `system` VALUES ('25', 'nexus', '1', 'root', '192.168.102.121', '22', '~/.ssh/authorized_keys', 'SUCCESS', '2017-09-18 05:07:44');
INSERT INTO `system` VALUES ('26', 'public', '1', 'root', '192.168.102.156', '22', '~/.ssh/authorized_keys', 'SUCCESS', '2017-09-18 05:08:04');
