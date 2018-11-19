/**
 * <p> TODO</p>
 *
 * @author: Gao Mimi
 * @Date :          2016年4月12日 下午4:45:50
 */
package com.mz.trade.entrust.service;

import com.mz.core.mvc.service.base.BaseService;
import com.mz.trade.entrust.model.ExEntrust;
import com.mz.trade.entrust.model.ExOrderInfo;
import com.mz.trade.redis.model.EntrustTrade;

import java.math.BigDecimal;

/**
 * <p> TODO</p>
 * @author: Gao Mimi
 * @Date :          2016年4月12日 下午4:45:50 
 */
public interface ExEntrustService extends BaseService<ExEntrust, Long> {

    /**
     *
     * <p>
     * 获得委托
     * </p>
     *
     * @author: Gao Mimi
     * @param: @param entrustNums
     * @return: void
     * @Date : 2016年4月19日 下午4:42:22
     * @throws:
     */
    public ExEntrust getExEntrustByentrustNum(String entrustNum);

    public void autoAddExEntrust();

    public void cancelAutoAddExEntrust();

    public boolean addExEntrust(String coinCode, String fixPriceCoinCode, Integer fixPriceType, Integer type, Integer entrustWay, Long customerId, BigDecimal price, String autoUsername, BigDecimal entrustCount, String currencyType, String website);


    public String getExEntrustChangeMarket(String coinCode, String fixPriceCoinCode, Integer n);

    public void tradeInit();

    public String getExEntrustChangeDephMarket(String coinCode, String fixPriceCoinCode, Integer n, BigDecimal depth);

    public void refreshReferencePrice();

}
