
/**
 * 注册customer路由服务
 */
define(['app'], function (app) {
	 
	var accountRouteService = function(){
		this.$get = function(){
			return{
			//账户
				state:'account',
				url : '/account',
				path:['js/account/account'],
				controller:	'accountCtrl',


				routes:
				[
		
					{
					//我方账户流水
						state:'account.fund_cord',
						url: '/fund/appaccountrecord/:page/:id',
						path: ['js/account/fund/AppAccountRecord'],
						controller: 'account_fund_appaccountrecord'
					},
					{
						//平台收取卖币佣金
						state:'account.fund_cord1',
						url: '/fund/appaccountrecord1/:page/:id',
						path: ['js/account/fund/AppAccountRecord1'],
						controller: 'account_fund_appaccountrecord'
					},
					{
					//交易订单
						state:'account.fund_transaction',
						url: '/fund/apptransaction/:page/:id',
						path: ['js/account/fund/AppTransaction'],
						controller: 'account_fund_apptransaction'
					},
					{
					//可用金额
						state:'account.fund_trecord',
						url: '/fund/apphotaccountrecord/:page/:id',
						path: ['js/account/fund/AppHotAccountRecord'],
						controller: 'account_fund_apphotaccountrecord'
					},
                    {
                        //冻结金额
                        state:'account.fund_cold',
                        url: '/fund/appcoldaccountrecord/:page/:id',
                        path: ['js/account/fund/AppColdAccountRecord'],
                        controller: 'account_fund_appcoldaccountrecord'
                    },
					{
					//银行卡管理
						state:'account.fund_bankcard',
						url: '/fund/appbankcard/:page/:id',
						path: ['js/account/fund/AppBankCard'],
						controller: 'account_fund_appbankcard'
					},
					{
					//我方主体银行卡管理
						state:'account.fund_account',
						url: '/fund/appouraccount/:page/:id',
						path:['js/account/fund/AppOurAccount'],
						controller: 'account_fund_appouraccount'
					},
					{
						//我方主体银行卡管理
						state:'account.fund_dmaccount',
						url: '/fund/appourdmaccount/:page/:id',
						path:['js/account/fund/AppOurDmAccount'],
						controller: 'account_fund_appourdmaccount'
					},
					
					//人民币账户管理
					{
						state:'account.fund_appacount',
						url: '/fund/appaccount/:page/:anon',
						path: ['js/account/fund/AppAccount'],
						controller: 'account_fund_appaccount'
					},
					
					//金科新加：注册或推荐奖励币的记录
					{
						state:'account.fund_appCoinRewardRecord',
						url: '/fund/appCoinRewardRecord/:page/:id',
						path: ['js/account/fund/AppCoinRewardRecord'],
						controller: 'account_fund_appCoinRewardRecord'
					},
					//卖币交易给推荐人返佣记录
					{
						state:'account.fund_appAgencyTranAwardRecord',
						url: '/fund/appAgencyTranAwardRecord/:page/:id',
						path: ['js/account/fund/AppAgencyTranAwardRecord'],
						controller: 'account_fund_appAgencyTranAwardRecord'
					},
					//卖币交易给推荐人返佣记录
					{
						state:'account.fund_agentList',
						url: '/fund/agentList',
						path: ['js/account/fund/AgentList'],
						controller: 'account_fund_appaccount'
					},
					//积分导入列表页面记录
					{
						state:'account.fund_integralImportRecordList',
						url: '/fund/IntegralImportRecordList',
						path: ['js/account/fund/IntegralImportRecordList'],
						controller: 'account_fund_integralImportRecord'
					},
					//手续费账户提现列表页面记录
					{
						state:'account.fund_feeWithdrawalsRecordList',
						url: '/fund/FeeWithdrawalsRecordList',
						path: ['js/account/fund/FeeWithdrawalsRecordList'],
						controller: 'account_fund_feeWithdrawalsRecord'
					}
					
					
					
					
					
					
					]

			};
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("accountRoute",accountRouteService);
	return app;
	
})