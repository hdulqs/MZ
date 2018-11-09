/**
 * 互融app 路由器
 * oauth:Yzc
 */
define(['app','js/exchange/appRoute','js/account/appRoute','js/customer/appRoute',
		'js/factoring/appRoute','js/finance/appRoute','js/oauth/appRoute',
		'js/sms/appRoute','js/web/appRoute','js/thirdpay/appRoute','js/calculate/appRoute','js/spotchange/appRoute','js/ico/appRoute'
		], function (app) {
//_G 全局变量。设置一些全局参数
//初始化服务
	return app.config(['$httpProvider','$ocLazyLoadProvider','$stateProvider','$urlRouterProvider','$controllerProvider','exchangeRouteProvider',
	                   'accountRouteProvider','customerRouteProvider','factoringRouteProvider',
	                   'financeRouteProvider','oauthRouteProvider','smsRouteProvider',
	                   'webRouteProvider','growlProvider','thirdRouteProvider','calculateRouteProvider','spotchangeRouteProvider','icoRouteProvider',
	                   function($httpProvider,$ocLazyLoadProvider,$stateProvider,$urlRouterProvider,$controllerProvider,exchangeRouteProvider,
	                		   accountRouteProvider,customerRouteProvider,factoringRouteProvider,
	                		   financeRouteProvider,oauthRouteProvider,smsRouteProvider,webRouteProvider,growlProvider,thirdRouteProvider,calculateRouteProvider,
	                		   spotchangeRouteProvider,icoRouteProvider
	                   ) {
		
		//http 拦截器
		$httpProvider.interceptors.push('UserInterceptor'); 
		//grow
		growlProvider.globalTimeToLive(2000);
		growlProvider.messagesKey("my-messages");
		growlProvider.messageTextKey("messagetext");
		growlProvider.messageSeverityKey("severity-level");
		growlProvider.onlyUniqueMessages(true);
  	
	  	//ocLazyLoad config
		   $ocLazyLoadProvider.config({
	  		 loadedModules: ['HryApp'],
	           asyncLoader: require
	           // debug: _G['debug'],
	           // events: _G['debug'],
	           // cssFilesInsertBefore: 'ng_load_plugins_before' // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
	           //, modules: []
	        });
		
  	 var lazyDeferred;
     /**
      * 路由切换时调用
      * @param param.file 懒加载文件数组
      * @param tpl 子模块view视图
      * @param module 子模块名
      * @param param.files 第一个文件必须传controller js
      */
     function resovleDep(param,tpl,module,ctrlName){
    	 
         var resolves = {
             loadCtrl: ['$ocLazyLoad', '$templateCache', '$q', function($ocLazyLoad,$templateCache,$q) {
                 lazyDeferred = $q.defer();
                 return $ocLazyLoad.load({
                     name : module,
                     cache: false,
                     files: param.files
                 }).then(function() {
                     lazyDeferred.resolve($templateCache.get(tpl));
                 });
             }],
             
             keyName:requireModule(param.files[0],ctrlName) //注册controller
            
         };
         return resolves;
     };
     
     /**
      * 自动注册controller JS
      */
     function requireModule(path, controllerName) {
    	
         return function ($state, $q) {
             var deferred = $q.defer();
             require([path], function (ret) {
                 $controllerProvider.register(controllerName, ret.controller);
                 deferred.resolve();
             });
             return deferred.promise;
         }
     };
     /**
      * 加版本号 清楚html缓存
      */
	 function gettemplateUrl(templateUrl){
		 var scripts = document.scripts;
	 	 for (i=0;i<scripts.length;i++){ 
			 var value = scripts[i]; 
			 //console.log(value); 
			 if (value.type=='text/javascript'){
				 var hash = HRY.ver;
				 var hashTemplateUrl = templateUrl+'?v='+hash; 
				 console.log(hashTemplateUrl); 
				 return hashTemplateUrl; 
			 } 
		 } 
	 };
  	
     //设置默认路由
     $urlRouterProvider.otherwise('/');
      
        /**
         * 路由表配置 core
         * 无限级路由配置
         * .state('index', {
  		        url: '/index',
  		      templateUrl : 'static/views/web/index.html',
  		      // 动态模版 
  		      //templateProvider: function() {return lazyDeferred.promise;},
              controller: 'indexCtrl',
              resolve : resovleDep({files:[
                  'js/web/index',
                  'js/web/indexService',
                  'js/web/indexDirective'
              ]}, 'views/web/index.html', 'index','indexCtrl')
		}) .state('index.test', {
		        url: '/test',
	  		      templateUrl : 'static/views/web/indextest.html',
	  		     // templateProvider: function() { return lazyDeferred.promise; },
	              controller: 'indextestCtrl',
	              resolve : resovleDep({files:[
	                  'js/web/index''
	              ]}, 'test.html', 'index.test','indextestCtrl')
			}).state('index.test.test1', {
		        url: '/test1',
	  		      templateUrl : 'static/views/web/indextest1.html',
	  		     // templateProvider: function() { return lazyDeferred.promise; },
	              controller: 'indextest1Ctrl',
	              resolve : resovleDep({files:[
	                  'js/web/index'
	              ]}, 'test1.html', 'index.test1','indextest1Ctrl')
			})
         *
         * @templateUrl 模版地址
         * @controller 控制器名字
         * resovleDep 方法中的参数说明：
         *      @1：要引用的js /css 
         *      @2模版id 为了做缓存用 目前传空
         *      @3moduleName 模型名称 目前传空 也可传eg:indexModule 
         *      @4controller 名字 目前要和 @controller 一致
         *      @oauth:    是否被权限拦截  true 为拦截， 不填 或false 为不被权限拦截
         */
     
     //home 路由设置
     $stateProvider
	  .state('index', {
			        url: '/',
			        templateUrl : gettemplateUrl('static/views/web/index.html'),
		            controller: 'indexCtrl',
		            resolve : resovleDep({files:[
		           'js/web/index'
		   ]}, '', '','indexCtrl')
		}).state('manage', {
	        url: '/manage',
	        templateUrl : gettemplateUrl('static/views/web/manage.html'),
            controller: 'manageCtrl',
            resolve : resovleDep({files:[
           'js/web/manage'
   ]}, '', '','manageCtrl')
})
     
     
     /**
      * 动态设置路由
      * 
      */
    function routeSeting(route){
    	 
        $stateProvider
   		.state(route.state, {
   			 url: route.url,
   			 templateUrl :function(o,elem,attrs){
   				var url=route.url;
	   	    	if(route.url.indexOf(":")>0){
	   	    		 url=route.url.substring(0,route.url.indexOf(":"));
	   	    	 }
 				var htmlPath = "static/views/";
 				// 页面参数  路由表中需要配置   url:'/**/**/:page'
             	var page=(o.page===undefined?"":o.page);
 				
             	//获取state
             	var state=route.state;
             	if(route.state.indexOf(".")>0){
             		state=route.state.substring(0,route.state.indexOf("."));
             	}
             	
             	/**
             	 * 判断是否为二级路由
             	 * 生成路由对应的html页面
             	 */
             	
 				/*if(route.routes!=undefined&&route.routes.length>0){
 				htmlPath += state+url;
             	}else{}*/
             		
             		if(route.url.indexOf(":")<0){
             			url=route.url+"/";
	   	    	     }
             		
             		url=url.substring(0,url.lastIndexOf("/"));
             		var routUrl=url.substring(0,url.lastIndexOf("/"));
             	    var htmlPer=url.substring(url.lastIndexOf("/")).replace(/(\w)/,function(v){return v.toUpperCase()});
             	    htmlPath += state+ routUrl +htmlPer+page.replace(/(\w)/,function(v){return v.toUpperCase()});
             	
             	
             	return htmlPath+".html?"+(new Date()).getTime();
             	
              },
   			 controller: route.controller,
   			 resolve : resovleDep({files:route.path}, '', '',route.controller)
   		}) 
   		if(route.routes!=undefined&&route.routes.length>0){
   		
   		  return routeEngine(route.routes);
   		}
       /* .state('exchange', {
   		    url: '/exchange',
   		    templateUrl : 'static/views/exchange/exchangeHome.html',
   		    controller: 'index1Ctrl',
   		    resolve : resovleDep({files:[
   		   'js/exchange/exchangeHome'
   		]}, '', '','index1Ctrl')
   		})
   		.state('exchange.product', {
   		    url: '/product/exproduct/:page',
   		    templateUrl : 'static/views/exchange/product/ExproductList.html',
   		    controller: 'indexCtrl',
   		    resolve : resovleDep({files:[
   		   'js/exchange/product/ExProduct'
   		]}, '', '','indexCtrl')
   		})*/
    }
     
     /**
      * 路由设置引擎
      */
    function routeEngine(map){
    	for (var i=0;i<map.length;i++){
    		routeSeting(map[i]);
    	}
     }
     /**
      * 路由表数组
      */
     var routeMap=[];
     //exchange
     var exchangeRoute =exchangeRouteProvider.$get();
     routeMap.push(exchangeRoute);
     //account
     var accountRoute =accountRouteProvider.$get();
     routeMap.push(accountRoute);
     //customer
     var customerRoute =customerRouteProvider.$get();
     routeMap.push(customerRoute);
     //factoring
     var factoringRoute =factoringRouteProvider.$get();
     routeMap.push(factoringRoute);
     //finance
     var financeRoute =financeRouteProvider.$get();
     routeMap.push(financeRoute);
     //oauth
     var oauthRoute =oauthRouteProvider.$get();
     routeMap.push(oauthRoute);
     //sms
     var smsRoute =smsRouteProvider.$get();
     routeMap.push(smsRoute);
     //web
     var webRoute =webRouteProvider.$get();
     routeMap.push(webRoute);
     //third
     var thirdRoute =thirdRouteProvider.$get();
     routeMap.push(thirdRoute);
     //calculate
     var calculate =calculateRouteProvider.$get();
     routeMap.push(calculate);
     //spotchange
//     var spotchange =spotchangeRouteProvider.$get();
//     routeMap.push(spotchange);
     //ico
     var ico =icoRouteProvider.$get();
     routeMap.push(ico);
     
     //启动路由配置引擎
     routeEngine(routeMap);

   }]) ;

});
