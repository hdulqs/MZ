package com.mz.calculate.remote.fund;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mz.account.fund.model.AppColdAccountRecord;
import com.mz.account.fund.model.AppHotAccountRecord;
import com.mz.core.mvc.model.page.PageResult;
import com.mz.exchange.account.model.ExDmColdAccountRecord;
import com.mz.exchange.account.model.ExDmHotAccountRecord;
import com.mz.remote.fund.RemoteAppAccountRecordService;
import com.mz.util.QueryFilter;
import com.mz.account.fund.service.AppColdAccountRecordService;
import com.mz.account.fund.service.AppHotAccountRecordService;
import com.mz.exchange.account.service.ExDmColdAccountRecordService;
import com.mz.exchange.account.service.ExDmHotAccountRecordService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class RemoteAppAccountRecordServiceImpl implements RemoteAppAccountRecordService {

    @Resource
    private AppColdAccountRecordService appColdAccountRecordService;

    @Resource
    private AppHotAccountRecordService appHotAccountRecordService;

    @Resource
    private ExDmColdAccountRecordService exDmColdAccountRecordService;

    @Resource
    private ExDmHotAccountRecordService exDmHotAccountRecordService;

    @Override
    public PageResult findAppColdAccountRecordList(HashMap<String, String> map) {
        QueryFilter filter = new QueryFilter(AppColdAccountRecord.class,map);
        commonPage(map, filter);
        return appColdAccountRecordService.findPageResult(filter);
    }

    @Override
    public PageResult findAppHotAccountRecordList(HashMap<String, String> requestMap) {
        HashMap<String,String> ssm = new HashMap<>();
        QueryFilter filter = new QueryFilter(AppHotAccountRecord.class,requestMap);
        commonPage(requestMap, filter);
        return appHotAccountRecordService.findPageResult(filter);
    }

    @Override
    public PageResult findExDmColdAccountRecordList(HashMap<String, String> map) {
        QueryFilter filter = new QueryFilter(ExDmColdAccountRecord.class,map);
        commonPage(map, filter);
        return exDmColdAccountRecordService.findPageResult(filter);
    }

    private void commonPage(Map<String, String> map, QueryFilter filter) {
        if (StringUtils.isNotEmpty(map.get("recordType"))) {
            filter.addFilter("recordType=",map.get("recordType"));
        }
    }

    @Override
    public PageResult findExDmHotAccountRecordList(HashMap<String, String> map) {
        QueryFilter filter = new QueryFilter(ExDmHotAccountRecord.class,map);
        commonPage(map, filter);
        return exDmHotAccountRecordService.findPageResult(filter);
    }
}
