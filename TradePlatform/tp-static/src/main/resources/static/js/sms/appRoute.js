
/**
 * 注册web路由服务
 */
define(['app'], function (app) {
	 
	var smsAppRouteService = function(){
		this.$get = function(){
			return{		state:'sms',
						url : '/sms',
						path : ['js/sms/sms'],
						controller : 'smsCtrl',
						
					routes:[{
						state:'sms.send',
						url : '/send/appsmssend/:page/:anon',
						path : ['js/sms/send/AppSmsSend'],
						controller : 'smssendCtrl'
					},
					{
						state:'sms.appjuhesend',
						url : '/send/appjuhesend/:page/:anon',
						path : ['js/sms/send/AppJuheSend'],
						controller : 'smsappjuhesendCtrl'
					}]
					}
				
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("smsRoute",smsAppRouteService);
	return app;
	
})