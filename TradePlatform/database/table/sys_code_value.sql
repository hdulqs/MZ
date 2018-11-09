CREATE TABLE sys_code_value(
  `code_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(255) COLLATE utf8_bin  NOT NULL,
  `code_name` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `code_description` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `value` TEXT COLLATE utf8_bin,
  `parent_code` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `parent_description` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `enable_flag` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `parent_enable_flag` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  `created` DATETIME DEFAULT NULL,
  `modified` DATETIME DEFAULT NULL,
  `saasId` VARCHAR(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`code_id`),
  UNIQUE (CODE)
) ENGINE=INNODB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT
