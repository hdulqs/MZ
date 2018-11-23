package com.mz.trade.entrust.service;

import com.mz.trade.model.AccountResultEnum;
import com.mz.trade.redis.model.Accountadd;
import com.mz.trade.redis.model.EntrustTrade;

import java.util.List;

/**
 * Created By lingyang on 11/17/18
 * Project: tp-trade-api
 */
public interface RedisAccountService {

    /**
     * 账户资金的改变，如果negateCheck为false，不进行资金变化后的资金变化为负数检查
     * @param accountaddList 资金变化的记录
     * @param negateCheck 资金变化的负数检查
     * @return 成功为 {@link AccountResultEnum#SUCCESS} ，其他是失败{@link AccountResultEnum}
     */
    public AccountResultEnum accountChange(List<Accountadd> accountaddList, final boolean negateCheck);

    /**
     * 根据新的委托冻结资金
     * @param entrustTrade 新的委托
     * @return true表示委托成功，false表示委托失败
     */
    public boolean forzenByEntrust(EntrustTrade entrustTrade);
}
