/**
 * 注册ico路由服务
 */
define(['app'], function (app) {
	 
	var icoRouteService = function(){
		this.$get = function(){
			return {
			state : 'ico',
			url:'/ico',
			path : ['js/ico/ico'],
			controller : 'icoCtrl',
			routes : [ {
							state : 'ico.project',
							url : '/project/appicoproject/:page/:id',
							path : [ 'js/ico/project/AppIcoProject' ],
							controller : 'ico_project_appicoproject'
					   },{
							state : 'ico.coin',
							url : '/coin/appicocoin/:page/:id',
							path : [ 'js/ico/coin/AppIcoCoin' ],
							controller : 'ico_coin_appicocoin'
						},{
							state : 'ico.coinAccount',
							url : '/coinAccount/appicocoinaccount/:page/:id',
							path : [ 'js/ico/coinAccount/AppIcoCoinAccount' ],
							controller : 'ico_coinAccount_appicocoinAccount'
						},{
							state : 'ico.coinAccountHotRecord',
							url : '/coinAccountHotRecord/appicocoinaccounthotrecord/:page/:id',
							path : [ 'js/ico/coinAccountHotRecord/AppIcoCoinAccountHotRecord' ],
							controller : 'ico_coinAccountHotRecord_appicocoinaccounthotrecord'
						},{
							state : 'ico.coinAccountColdRecord',
							url : '/coinAccountColdRecord/appicocoinaccountcoldrecord/:page/:id',
							path : [ 'js/ico/coinAccountColdRecord/AppIcoCoinAccountColdRecord' ],
							controller : 'ico_coinAccountColdRecord_appicocoinaccountcoldrecord'
						},{
							state : 'ico.coinTransaction',
							url : '/coinTransaction/appicocointransaction/:page/:id',
							path : [ 'js/ico/coinTransaction/AppIcoCoinTransaction'],
							controller : 'ico_coinTransaction_appicocointransaction'
						}]
			};
		};
	};
	// 在模块中登记--使用provider用于config中调用
	app.provider("icoRoute",icoRouteService);
	return app;
	
})