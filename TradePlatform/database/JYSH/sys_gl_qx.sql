CREATE TABLE `sys_gl_qx` (
  `GL_ID` varchar(100) NOT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `FX_QX` int(10) DEFAULT NULL,
  `FW_QX` int(10) DEFAULT NULL,
  `QX1` int(10) DEFAULT NULL,
  `QX2` int(10) DEFAULT NULL,
  `QX3` int(10) DEFAULT NULL,
  `QX4` int(10) DEFAULT NULL,
  PRIMARY KEY (`GL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_gl_qx` */

insert  into `sys_gl_qx`(`GL_ID`,`ROLE_ID`,`FX_QX`,`FW_QX`,`QX1`,`QX2`,`QX3`,`QX4`) values ('03b00e767e4440cdb67ea3d4e6bacb79','3e9f1a20f4274d9d8cb06bc421cad45c',0,0,0,0,0,0),('1','2',1,1,1,1,1,1),('2','1',1,1,1,1,1,1),('3aa9dca97417445d8948cef0554f5a95','1',0,0,0,0,0,0),('3e9f1a20f4274d9d8cb06bc421cad45c','0',0,0,0,0,0,0),('5918bc7a61af4c718a0372354ed91943','7a71af7455944680b889b1aa4ed1d349',0,0,0,0,0,0),('7791f7b751214649b1e2eda35bf0dd12','0',0,0,0,0,0,0),('7a71af7455944680b889b1aa4ed1d349','0',0,0,0,0,0,0);


