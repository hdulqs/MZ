CREATE TABLE `sys_user` (
  `USER_ID` varchar(100) NOT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `LAST_LOGIN` varchar(255) DEFAULT NULL,
  `IP` varchar(100) DEFAULT NULL,
  `STATUS` varchar(32) DEFAULT NULL,
  `BZ` varchar(255) DEFAULT NULL,
  `SKIN` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(32) DEFAULT NULL,
  `NUMBER` varchar(100) DEFAULT NULL,
  `PHONE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user` */

insert  into `sys_user`(`USER_ID`,`USERNAME`,`PASSWORD`,`NAME`,`RIGHTS`,`ROLE_ID`,`LAST_LOGIN`,`IP`,`STATUS`,`BZ`,`SKIN`,`EMAIL`,`NUMBER`,`PHONE`) values ('0d0b5c90e66c4b75860045df1a6cdd3e','dfl','7e0f0e3cfab70a36da3823563c8fc846350cfb80','邓芳羚','','3aa9dca97417445d8948cef0554f5a95','2018-04-19 13:33:59','27.38.32.62','0','','default','dfl@wld.com','dfl','13189078469'),('1','admin','de41b7fb99201d8334c23c014db35ecd92df81bc','系统管理员','1133671055321055258374707980945218933803269864762743594642571294','1','2018-06-28 14:42:34','47.75.200.109','0','','skin-3','admin@main.com','admin','18872693583'),('398eac75540c45d48f53dda61d19eacd','wld','b738f7c262cca7adeaea9ab5d64ee833acc3466f','系统管理员','','3aa9dca97417445d8948cef0554f5a95','2018-05-01 10:47:51','223.104.63.247','0','dfs','default','sdad@ew.com','system','13686086237'),('3e456e420a5a4afe9fd11b6cb82a9cde','wld1','fad7907a891496272f47165209196219cf706a3c','陶思','','3aa9dca97417445d8948cef0554f5a95','2018-05-02 08:56:44','14.156.32.240','0','','default','123dfg@wld.com','wld1','18688632380'),('7dc62ef7aa2c475fa87bcfa72a93db1e','alddgly','00046678d78cf722ce55bb0fb0b9043acb0256c4','喂来店','','5918bc7a61af4c718a0372354ed91943','2018-02-07 13:34:34','0:0:0:0:0:0:0:1','0','','default','101@qq.com','002','13530352950'),('ba96624adca44eaba39cd0ed8c729706','大鹏','46497b5fd6331e84f4678f1c457bc8c017a7ea14','韩','','3aa9dca97417445d8948cef0554f5a95','2018-06-28 14:44:02','47.75.200.109','0','000','default','123@qq.com','007','18813929463');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
