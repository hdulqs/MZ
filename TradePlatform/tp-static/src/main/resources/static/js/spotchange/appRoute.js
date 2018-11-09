/**
 * 注册spotchange路由服务
 */
define(['app'], function (app) {
	 
	var spotchangeRouteService = function(){
		this.$get = function(){
			return {
			state : 'spotchange',
			url:'/spotchange',
			path : ['js/spotchange/spotchange'],
			controller : 'spotchangeCtrl',
			
			routes:[
			
			       ]
			};
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("spotchangeRoute",spotchangeRouteService);
	return app;
	
})