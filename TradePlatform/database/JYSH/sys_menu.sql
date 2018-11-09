/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.7.20-log : Database - change
*********************************************************************
*/
CREATE TABLE `sys_menu` (
  `MENU_ID` int(11) NOT NULL,
  `MENU_NAME` varchar(255) DEFAULT NULL,
  `MENU_URL` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(100) DEFAULT NULL,
  `MENU_ORDER` varchar(100) DEFAULT NULL,
  `MENU_ICON` varchar(30) DEFAULT NULL,
  `MENU_TYPE` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`MENU_ID`,`MENU_NAME`,`MENU_URL`,`PARENT_ID`,`MENU_ORDER`,`MENU_ICON`,`MENU_TYPE`) values (1,'系统管理','#','0','1','icon-book','2'),(2,'组织管理','role.do','1','2',NULL,'2'),(4,'会员管理','happuser/listUsers.do','1','4',NULL,'2'),(5,'系统用户','user/listUsers.do','1','3',NULL,'2'),(20,'在线管理','onlinemanager/list.do','1','5',NULL,'2'),(87,'注册会员管理','#','0','4','icon-heart','2'),(88,'会员信息管理','app_customer/list.do','87','5',NULL,'2'),(89,'系统工具','#','0','2',NULL,''),(90,'性能监控','druid/index.html','89','1',NULL,''),(91,'接口测试','tool/interfaceTest.do','89','2',NULL,''),(92,'发送邮件','tool/goSendEmail.do','89','3',NULL,''),(93,'置二维码','tool/goTwoDimensionCode.do','89','4',NULL,''),(94,'微信管理','#','0','3',NULL,''),(95,'关注回复','textmsg/goSubscribe.do','94','1',NULL,''),(96,'文本回复','textmsg/list.do','94','2',NULL,''),(97,'图文回复','imgmsg/list.do','94','3',NULL,''),(98,'应用命令','command/list.do','94','4',NULL,''),(99,'提币地址管理','ex_dm_customer_publickey/list.do','87','4',NULL,'2'),(100,'银行卡管理','app_bank_card/list.do','87','3',NULL,'2'),(101,'资金账户管理','app_account/list.do','87','2',NULL,'2'),(102,'货币账户管理','ex_digitalmoney_account/list.do','87','1',NULL,'2');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
