/**
 * 注册factoring路由服务
 */
define(['app'], function (app) {
	var factoringRouteService = function(){
		this.$get = function(){
			return {
					state:'factoring',
					url:'/factoring',
					path:['js/factoring/factoring'],
					controller:'factoringCtrl',
					
					routes:[
				        {
			        	state:'factoring.custeloan',
			        	url : '/custeloan/custeloan/:page/:anon',
						path : ['js/factoring/custeloan/CusteloanList'],
						controller : 'factoring_custeloan_custeloanadd'
					},{
						state:'factoring.test',
						url : '/test/testpage/:page/:anon',
						path : ['js/factoring/test/TestPage'],
						controller : 'factoring_test_testadd'
					}]
			}
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("factoringRoute",factoringRouteService);
	return app;
	
})