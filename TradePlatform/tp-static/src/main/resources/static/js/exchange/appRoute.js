/**
 * 注册exchange路由服务
 */
define(['app'], function (app) {
	 
	var exchangeRouteService = function(){
		this.$get = function(){
			return {
			state : 'exchange',
			url:'/exchange',
			path : ['js/exchange/exchange'],
			controller : 'exchangeCtrl',
			routes:[{
			state:'exchange.product_exp',
			url : '/product/exproduct/:page/:id',
			path : ['js/exchange/product/ExProduct'],
			controller : 'exchange_product_exproduct'
		},{	
			state:'exchange.product_params',
			url : '/product/productparameter/:page/:ing',
			path : ['js/exchange/product/ProductParameter'],
			controller : 'exchange_product_productparameter'
		},{	
			state:'exchange.product_cointocoin',
			url : '/product/excointocoin/:page/:ing',
			path : ['js/exchange/product/ExCointoCoin'],
			controller : 'exchange_product_excointocoin'
		},{	
			state:'exchange.entrust_trust',
			url : '/entrust/exentrust/:page/:id',
			path : ['js/exchange/entrust/ExEntrust'],
			controller : 'exchange_entrust_exentrust'
		},
		{
			state:'exchange.entrust_info',
			url : '/entrust/exorderinfo/:page/:id',
			path : ['js/exchange/entrust/ExOrderInfo'],
			controller : 'exchange_entrust_exorderinfo'
		},
		 {
			state:'exchange.entrust_summary',
			url : '/entrust/exsummary',
			path : ['js/exchange/entrust/Exsummary'],
			controller : 'exchange_entrust_exsummary'
		},
		
		{
			state:'exchange.account_app',
			url : '/account/appfundaccount/:page/:id',
			path : ['js/exchange/account/AppFundAccount'],
			controller : 'exchange_account_appfundaccount'
		},{
			state:'exchange.setting_appset',
			url : '/setting/appsetting/:page/:id',
			path : ['js/exchange/setting/AppSetting'],
			controller : 'exchange_setting_appsetting'
		},{
			state:'exchange.transaction_exdm',
			url : '/transaction/exdmtransaction/:page',
			path : ['js/exchange/transaction/ExDmTransaction'],
			controller : 'exchange_transaction_exdmtransaction'
		},{
			state:'exchange.transaction_hxbexdm',
			url : '/transaction/hxbexdmtransaction/:page',
			path : ['js/exchange/transaction/HxbexDmTransaction'],
			controller : 'exchange_transaction_hxbexdmtransaction'
		},{
			state:'exchange.transaction_custorm',
			url : '/transaction/exdmcustomerpublickey/:page/:id',
			path : ['js/exchange/transaction/ExDmCustomerPublicKey'],
			controller : 'exchange_transaction_exdmcustomerpublickey'
		},{
			state:'exchange.account_money',
			url : '/account/exdigitalmoneyaccount/:page/:id',
			path : ['js/exchange/account/ExDigitalmoneyAccount'],
			controller : 'exchange_account_exdigitalmoneyaccount'
		},{
			state:'exchange.account_cold',
			url : '/account/exdmcoldaccountrecord/:page/:id',
			path : ['js/exchange/account/ExDmColdAccountRecord'],
			controller : 'exchange_account_exdmcoldaccountrecord'
		},{
			state:'exchange.account_hot',
			url : '/account/exdmhotaccountrecord/:page/:id',
			path : ['js/exchange/account/ExDmHotAccountRecord'],
			controller : 'exchange_account_exdmhotaccountrecord'
		},{
			state:'exchange.product_view',
			url : '/product/productreview/:page/:ing',
			path : ['js/exchange/product/ProductReview'],
			controller : 'exchange_product_productparameter'
		},{
			state:'exchange.lend_lend',
			url : '/lend/exdmLend/:page',
			path : ['js/exchange/lend/ExDmLend'],
			controller : 'exchange_lend_exdmLend'
		},{
			state:'exchange.lend_intent',
			url : '/lend/exdmLendIntent/:page',
			path : ['js/exchange/lend/ExDmLendIntent'],
			controller : 'exchange_lend_exdmLendIntent'
		},{
			state:'exchange.lend_times',
			url : '/lend/exDmLendTimes/:page',
			path : ['js/exchange/lend/ExDmLendTimes'],
			controller : 'exchange_lend_exDmLendTimes'
		},{
			state:'exchange.lend_ping',
			url : '/lend/exDmLendPing/:page',
			path : ['js/exchange/lend/ExDmLendPing'],
			controller : 'exchange_lend_exDmLendPing'
		},{
			state:'exchange.procedure_proceduretrantroction',
			url : '/procedure/proceduretrantroction/:page/:id',
			path : ['js/exchange/procedure/Proceduretrantroction'],
			controller : 'exchange_procedure_proceduretrantroction'
		},{
			state:'exchange.purse_coin',
			url : '/purse/cointransaction/:page',
			path : ['js/exchange/purse/CoinTransaction'],
			controller : 'exchange_purse_cointransaction'
		},{
			state:'exchange.account_apiapply',
			url : '/account/exapiapply/:page',
			path : ['js/exchange/account/ExApiApply'],
			controller : 'exchange_account_exapiapply'
		},{
			state:'exchange.kline',
			url : '/kline/kline/:page/:id',
			path : ['js/exchange/kline/Kline'],
			controller : 'exchange_Kline'
		},{
			state:'exchange.subscription_plan',
			url : '/subscription/exsubscriptionplan/:page/:id',
			path : ['js/exchange/subscription/ExSubscriptionPlan'],
			controller : 'exchange_subscription_exsubscriptionplan'
		},{
			state:'exchange.subscription_record',
			url : '/subscription/exsubscriptionrecord/:page/:id',
			path : ['js/exchange/subscription/ExSubscriptionRecord'],
			controller : 'exchange_subscription_exsubscriptionrecord'
		},{
			state:'exchange.buyback_record',
			url : '/subscription/exbuybackrecord/:page/:id',
			path : ['js/exchange/subscription/ExBuybackRecord'],
			controller : 'exchange_subscription_exbuybackrecord'
		},{
			state:'exchange.account_accountdisable',
			url : '/account/appaccountdisable/:page/:id',
			path : ['js/exchange/account/AppAccountDisable'],
			controller : 'exchange_account_appsccountdisable'
		},{
			state:'exchange.cold_account',
			url : '/fund/appColdAccountRecord/:page',
			path : ['js/exchange/fund/AppColdAccountRecord'],
			controller : 'exchange_fund_appcoldaccountrecord'
		}



		
				]
			};
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("exchangeRoute",exchangeRouteService);
	return app;
	
})