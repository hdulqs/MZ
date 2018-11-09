
/**
 * 注册customer路由服务
 */
define(['app'], function (app) {
	 
	var calculateRouteService = function(){
		this.$get = function(){
			return{	
					state:'calculate',
					url:'/calculate',
					path : ['js/calculate/calculate'],
					controller:'calculateCtrl',				
					routes:
						[{
							state:'calculate.report_financialstatements',
							url : '/report/financialstatements/:page/:id',
							path : ['js/calculate/report/FinancialStatements'],
							controller : 'calculate_report_financialstatements'
						},{
							state:'calculate.report_platformearnings',
							url : '/report/platformearnings/:page/:id',
							path : ['js/calculate/report/Platformearnings'],
							controller : 'calculate_report_platformearnings'
						},{
							state:'calculate.report_platformcurrency',
							url : '/report/platformcurrency/:page/:id',
							path : ['js/calculate/report/Platformcurrency'],
							controller : 'calculate_report_platformcurrency'
						},{
							state:'calculate.report_platformuser',
							url : '/report/platformuser/:page/:id',
							path : ['js/calculate/report/Platformuser'],
							controller : 'calculate_report_platformuser'
						},{
							state:'calculate.report_platforcustomerforprofit',
							url : '/report/platforcustomerforprofit/:page/:id',
							path : ['js/calculate/report/PlatforCustomerForProfit'],
							controller : 'calculate_report_platforcustomerforprofit'
						},{
							state:'calculate.report_platformoneytoday',
							url : '/report/platformoneytoday/:page/:id',
							path : ['js/calculate/report/PlatForMoneytoday'],
							controller : 'calculate_report_platformoneytoday'
						}
						,{
							state:'calculate.report_platforsettlement',
							url : '/report/platforsettlement/:page/:id',
							path : ['js/calculate/report/Platforsettlement'],
							controller : 'calculate_report_platforsettlement'
						},{
							state:'calculate.report_platforsettlementcode',
							url : '/report/platforsettlementcode/:page/:id',
							path : ['js/calculate/report/Platforsettlementcode'],
							controller : 'calculate_report_platforsettlementcode'
						},{
							state:'calculate.report_platforfinancialaudit',
							url : '/report/platforfinancialaudit/:page/:id',
							path : ['js/calculate/report/Platforfinancialaudit'],
							controller : 'calculate_report_platforfinancialaudit'
						},{
							state:'calculate.report_platforrestated',
							url : '/report/platforrestated/:page/:id',
							path : ['js/calculate/report/Platforrestated'],
							controller : 'calculate_report_platforrestated'
						},{
							state:'calculate.report_AppAccountcheck',
							url : '/report/appaccountcheck/:page/:id',
							path : ['js/calculate/report/AppAccountcheck'],
							controller : 'calculate_report_AppAccountcheck'
						},{
							state:'calculate.report_AppAccountredischeck',
							url : '/report/appaccountredischeck/:page/:id',
							path : ['js/calculate/report/AppAccountredischeck'],
							controller : 'calculate_report_AppAccountredischeck'
						},{
							state:'calculate.report_AppAccountcheckLog',
							url : '/report/appaccountchecklog/:page/:id',
							path : ['js/calculate/report/AppAccountcheckLog'],
							controller : 'calculate_report_AppAccountcheckLog'
						}
				]
			}
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("calculateRoute",calculateRouteService);
	return app;
	
})