/**
 * 注册finance路由服务
 */
define(['app'], function (app) {
	var financeRouteService = function(){
		this.$get = function() {
			return {
				state:'finance',
				url:'/finance',
				path:['js/finance/finance'],
				controller:'financeCtrl',

				routes:[{
					state:'finance.fundParam_dic',
					url: '/fundParam/fincfundparamdic/:page/anon',
					path: ['js/finance/fundParam/FincFundParamdic'],
					controller: 'finance_fundParam_fincfundparamdic'
				},
				{
					state:'finance.fundParam_setting',
					url: '/fundParam/fincfundparamsetting/:page/anon',
					path: ['/js/finance/fundParam/FincFundParamsetting'],
					controller: 'finance_fundParam_fincfundparamsetting'
				}]
			}
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("financeRoute",financeRouteService);
	return app;
	
})