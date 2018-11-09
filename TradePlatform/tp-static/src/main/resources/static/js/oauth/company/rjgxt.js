/**
 * AppUser.js
 */
define(['app'], function (app) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore){
        $rootScope.headTitle = $rootScope.title = "上下级关系图";
        	
		//加载插件
		hryCore.initPlugins();
		//上传插件
		hryCore.uploadPicture();
			
    }
        
    return {controller:controller};
});