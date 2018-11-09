ALTER TABLE c2c_transaction
 ADD (cardName VARCHAR(255) NULL COMMENT "银行卡持有人", cardNumber VARCHAR(255) NULL  COMMENT "银行卡号",cardBank  VARCHAR(255) NULL  COMMENT "开户银行");
 
 ALTER TABLE  c2c_transaction
 ADD (subBank VARCHAR(255) NULL COMMENT "卖家开户支行");
 
 ALTER TABLE c2c_transaction
 ADD (BusinessmanBankName VARCHAR(255) NULL COMMENT "商家银行", BusinessmanBankcard VARCHAR(255) NULL  COMMENT "商家银行卡号",BusinessmanBankowner  VARCHAR(255) NULL  COMMENT "商家");