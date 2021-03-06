CREATE TABLE `otc_account_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `customId` BIGINT(20) DEFAULT NULL COMMENT '账户id',
  `accountId` BIGINT(20) DEFAULT NULL COMMENT '币账户id',
  `money` DECIMAL(20,10) NULL COMMENT '操作数据',
  `monteyType` INT(1) NOT NULL COMMENT '1热账户，2，冷账号',
  `acccountType` INT(1) NOT NULL COMMENT '0资金账号，1币账户',
  `transactionNum` VARCHAR(250) DEFAULT NULL COMMENT '单号', 
  `remarks` VARCHAR(250) DEFAULT NULL COMMENT '备注',
  `saasId` VARCHAR(255) COLLATE utf8_bin DEFAULT '' COMMENT 'saasId',
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',  
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=109188 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='otc账号操作记录'
