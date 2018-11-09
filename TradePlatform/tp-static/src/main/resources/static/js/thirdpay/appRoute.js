/**
 * 注册third路由服务
 */
define(['app'], function (app) {
	 
	var thirdRouteService = function(){
		this.$get = function() {
				return {
					state:'thirdpay',
					url:'/thirdpay',
					path:['js/thirdpay/thirdpay'],
					controller:'thirdpayCtrl',
					
				 routes:[{
				//配置管理
					state:'thirdpay.pay_appthirdPayconfig',
					url: '/pay/appthirdpayconfig/:page/anon',
					path: ['js/thirdpay/pay/AppThirdPayConfig'],
					controller: 'thirdpay_pay_appthirdpayconfig'
				},{
				//配置管理
					state:'thirdpay.coin_coin',
					url: '/coin/coin/:page/anon',
					path: ['js/thirdpay/coin/Coin'],
					controller: 'thirdpay_coin_coin'
				},{
				//配置管理
					state:'thirdpay.pay_applogthirdpay',
					url: '/pay/applogthirdpay/:page/anon',
					path: ['js/thirdpay/pay/AppLogThirdPay'],
					controller: 'thirdpay_pay_AppLogThirdpay'
				}]
			};
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("thirdRoute",thirdRouteService);
	return app;
	
})