package com.mz.remote.fund;

import com.mz.core.mvc.model.page.PageResult;
import java.util.HashMap;

public interface RemoteAppAccountRecordService {

    PageResult findAppColdAccountRecordList(HashMap<String, String> parameterMap);

    PageResult findAppHotAccountRecordList(HashMap<String, String> parameterMap);

    PageResult findExDmColdAccountRecordList(HashMap<String, String> parameterMap);

    PageResult findExDmHotAccountRecordList(HashMap<String, String> parameterMap);
}
