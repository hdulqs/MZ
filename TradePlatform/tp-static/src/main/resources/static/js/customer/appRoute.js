
/**
 * 注册customer路由服务
 */
define(['app'], function (app) {
	 
	var customerRouteService = function(){
		this.$get = function(){
			return{	
					state:'customer',
					url:'/customer',
					path : ['js/customer/Customer'],
					controller:'customerCtrl',				
				
						
					routes:
						[{
							state:'customer.appcustmer',
							url : '/user/appcustomer/:page/:id',
							path : ['js/customer/user/AppCustomer'],
							controller : 'customer_user_appcustomer'
						},{// OtccCoin
							state:'customer.otccoin',
							url : '/businessman/otccoin/:page/:id',
							path : ['js/customer/businessman/OtcCoin'],
							controller : 'customer_businessman_otccoin'
						},{// Otc委托订单
                            state:'customer.otc',
                            url : '/businessman/otc/:page/:id',
                            path : ['js/customer/businessman/Otclist'],
                            controller : 'customer_businessman_otc'
                        },{// Otc交易订单
                            state:'customer.otcOrder',
                            url : '/businessman/otcOrder/:page/:id',
                            path : ['js/customer/businessman/OtcOrder'],
                            controller : 'customer_businessman_otcOrder'
                        },{// c2cCoin
							state:'customer.c2ccoin',
							url : '/businessman/c2ccoin/:page/:id',
							path : ['js/customer/businessman/C2cCoin'],
							controller : 'customer_businessman_c2ccoin'
						},{// 交易商户
							state:'customer.appbusinessman',
							url : '/businessman/appbusinessman/:page/:id',
							path : ['js/customer/businessman/AppBusinessman'],
							controller : 'customer_businessman_appBusinessman'
						},{// 交易商户银行卡
							state:'customer.appbusinessmanbank',
							url : '/businessman/appbusinessmanbank/:page/:id',
							path : ['js/customer/businessman/AppBusinessmanBank'],
							controller : 'customer_businessman_appBusinessmanBank'
						},{//c2c交易订单
							state:'customer.c2ctransaction',
							url : '/businessman/c2ctransaction/:page/:id',
							path : ['js/customer/businessman/C2cTransaction'],
							controller : 'customer_businessman_c2ctransaction'
						},{//c2c交易规则
							state:'customer.c2cchangerule',
							url : '/businessman/c2cchangerule',
							path : ['js/customer/businessman/C2cchangerule'],
							controller : 'customer_businessman_c2cchangerule'
						},{//c2c交易超时设置
							state:'customer.c2ctimeout',
							url : '/businessman/c2ctimeout',
							path : ['js/customer/businessman/c2ctimeout'],
							controller : 'customer_businessman_c2ctimeout'
						},{
							state:'customer.appagentscustromer',
							url : '/agents/appagentscustromer/:page/:id',
							path : ['js/customer/agents/AppAgentsCustromer'],
							controller : 'customer_user_appagentscustromer'
						},{
							
						
								
							state:'customer.commissiondeploy',
							url : '/agents/commissiondeploy/:page/:id',
							path : ['js/customer/agents/CommissionDeploy'],
							controller : 'customer_user_commissiondeploy'
						},{
							state:'customer.commissionDetail',
							url : '/agents/commissiondetail/:page/:id',
							path : ['js/customer/agents/CommissionDetail'],
							controller : 'customer_user_commissionDetail'
						},{
							state:'customer.appAgentsForMoney',
							url : '/agents/appagentformoney/:page/:id',
							path : ['js/customer/agents/AppAgentsForMoney'],
							controller : 'customer_user_appAgentsForMoney'
						},{
							state:'customer.hello_hellouser',
							url : '/hello/hellouser/:page/:id',
							path : ['js/customer/hello/HelloUser'],
							controller : 'customer_hello_hellouser'
						},{
							state:'customer.apppersoninfo',
							url : '/person/apppersoninfo/:page/:id',
							path : ['js/customer/person/AppPersonInfo'],
							controller : 'customer_person_appPersonInfo'
						},
						
						//金科新加：代理商申请审核列表
						{
							state:'customer.agentapplylist',
							url : '/person/agentApplyList',//页面
							path : ['js/customer/person/AgentApplyList'],//js
							controller : 'customer_person_appPersonInfo'//controller
						},
						{	state:'customer.platforCustomerForProfit1',
							url : '/person/platforCustomerForProfit1',//页面
							path : ['js/customer/person/PlatforCustomerForProfit1'],//js
							controller : 'customer_person_appPersonInfo'//controller
						},
						{
							state:'customer.appcommenduser',
							url : '/commend/appcommenduser/:page/:id',
							path : ['js/customer/commend/AppCommendUser'],
							controller : 'customer_commend_appcommenduser'
						},
						{
							state:'customer.appcommenddeploy',
							url : '/deploy/appcommenddeploy/:page/:id',
							path : ['js/customer/deploy/AppCommendDeploy'],
							controller : 'customer_deploy_appcommenddeploy'
						},
						{
							state:'customer.appcommendtrade',
							url : '/trade/appcommendtrade/:page/:id',
							path : ['js/customer/trade/AppCommendTrade'],
							controller : 'customer_trade_appcommendtrade'
						},
						{
							state:'customer.appcommendmoney',
							url : '/money/appcommendmoney/:page/:id',
							path : ['js/customer/money/AppCommendMoney'],
							controller : 'customer_money_appcommendmoney'
						},
						{
							state:'customer.appcommendrank',
							url : '/rank/appcommendrank/:page/:id',
							path : ['js/customer/rank/AppCommendRank'],
							controller : 'customer_rank_appcommendrank'
						},
						{
							state:'customer.appcommendrebat',
							url : '/rebat/appcommendrebat/:page/:id',
							path : ['js/customer/rebat/AppCommendRebat'],
							controller : 'customer_trade_appcommendrebat'
						}
				]
			}
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("customerRoute",customerRouteService);
	return app;
	
})