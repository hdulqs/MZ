/**
 * 注册third路由服务
 */
define(['app'], function (app) {
	 
	var thirdRouteService = function(){
		this.$get = function() {
				return {
					state:'third',
					url:'/third',
					path:['js/third/third'],
					controller:'thirdCtrl',
					
				 routes:[{
				//配置管理
					state:'third.pay_appthirdPayconfig',
					url: '/pay/appthirdpayconfig/:page/anon',
					path: ['js/third/pay/AppThirdPayConfig'],
					controller: 'third_pay_appthirdpayconfig'
				}]
				
				
			};
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("thirdRoute",thirdRouteService);
	return app;
	
})