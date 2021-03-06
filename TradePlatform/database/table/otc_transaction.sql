CREATE TABLE `otc_transaction` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionNum` VARCHAR(100) COLLATE utf8_bin DEFAULT NULL COMMENT '交易订单号',
  `userName` VARCHAR(100) COLLATE utf8_bin DEFAULT NULL COMMENT '用户登录名',
  `customerId` BIGINT(20) NOT NULL COMMENT '用户id',
  `accountId` BIGINT(20) NOT NULL COMMENT '虚拟账户id',
  `transactionType` SMALLINT(2) DEFAULT NULL COMMENT '交易类型(1买,2卖)',
  `transactionMoney` DECIMAL(20,10) DEFAULT NULL COMMENT '交易金额',
  `transactionCount` DECIMAL(20,10) DEFAULT NULL COMMENT '交易数量',
  `transactionPrice` DECIMAL(20,10) DEFAULT NULL COMMENT '交易单价',
  `dealQuantity` DECIMAL(20,10) DEFAULT NULL COMMENT '成交量',
  `businessQuantity` DECIMAL(20,10) DEFAULT NULL COMMENT '交易量',
  `surplusQuantity` DECIMAL(20,10) DEFAULT NULL COMMENT '剩余量',
  `frozenQuantity` DECIMAL(20,10) DEFAULT NULL COMMENT '因数量不足剩余数量',
  `returnQuantity` DECIMAL(20,10) DEFAULT NULL COMMENT '因数量不足剩余数量',
  `businessFlag` VARCHAR(20) COLLATE utf8_bin DEFAULT 'Y' COMMENT '交易标识',
  `status` SMALLINT(2) DEFAULT NULL COMMENT '1进行中 2已完成 3已取消,4 部分交易,5 全部交易 6部分完成',
  `finishTime` DATETIME DEFAULT NULL COMMENT '完成时间',
  `remark` VARCHAR(20) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `coinCode` VARCHAR(20) COLLATE utf8_bin DEFAULT '' COMMENT '币种',
  `poundage_type` VARCHAR(20) COLLATE utf8_bin DEFAULT '' COMMENT '手续费类型',
  `poundage` DECIMAL(20,10) DEFAULT '0.0000000000' COMMENT '手续费比例或固定手续费',
  `fee` DECIMAL(20,10) DEFAULT '0.0000000000' COMMENT '手续费',
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `createBy` BIGINT(20) NOT NULL COMMENT '创建人',
  `modified` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `saasId` VARCHAR(255) COLLATE utf8_bin NOT NULL COMMENT 'saasId',
  `businessmanId` BIGINT(20) DEFAULT NULL,
  `businessmanBankId` BIGINT(20) DEFAULT NULL,
  `randomNum` VARCHAR(20) COLLATE utf8_bin DEFAULT '',
  `customerBankId` BIGINT(20) DEFAULT NULL,
  `transactionId` BIGINT(20) DEFAULT NULL,
  `status2` SMALLINT(2) DEFAULT NULL,
  `cancelFlag` VARCHAR(255) COLLATE utf8_bin  COMMENT '取消标识',
  `cancelBy` BIGINT(20)  COMMENT '取消人',
  `cancelDate` DATETIME DEFAULT NULL  COMMENT '取消日期',
  `businessman` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `attribute1` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL,
  `attribute2` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL,
  `attribute3` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL,
  `attribute4` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL,
  `attribute5` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='otc平台交易订单表'
