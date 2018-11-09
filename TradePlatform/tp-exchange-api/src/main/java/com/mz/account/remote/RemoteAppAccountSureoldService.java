/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      GaoMimi
 * @version:     V1.0 
 * @Date:        2016-10-11 14:37:42 
 */
package com.mz.account.remote;

import com.mz.account.fund.model.AppAccountSureold;
import java.util.List;

/**
 * <p> AppAccountSureoldService </p>
 * @author:         GaoMimi
 * @Date :          2016-10-11 14:37:42  
 */
public interface RemoteAppAccountSureoldService {

   public List<AppAccountSureold> getBycustomerId(Long customerId,
			String userName,String currencyType, String website);
   public AppAccountSureold getBycustomerIdAndcoinCode(Long customerId,String userName,String coinCode,String currencyType,String website);
   public void  updateAccount(AppAccountSureold appAccountSureold);
   public void  saveAccount(AppAccountSureold appAccountSureold);
}
