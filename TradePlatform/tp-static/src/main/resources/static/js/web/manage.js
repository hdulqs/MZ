/**
 * Created by Yuanzc on 2016/9/5.
 */
define(['app'], function (app) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){
    	
    }
    
    return {controller:controller};
    
});