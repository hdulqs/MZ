package com.fh.util.push.android;

import com.fh.util.push.AndroidNotification;

public class AndroidBroadcast extends AndroidNotification {
	public AndroidBroadcast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "broadcast");	
	}
}
