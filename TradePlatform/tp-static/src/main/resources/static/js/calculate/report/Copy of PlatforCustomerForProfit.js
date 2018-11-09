/**
 * student.js
 */
define(['app','hryTable','pikadayJq'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	

    	
// ========= 请别删 备份用  ================================================================================    	
    
    	
    	//初始化js 插件
    	var beginTime='';
    	
    function querySpecific (){
    	var beginTime2 = $("#fristData").val();
    	var customerType = $("#customerType").val();
    	
    	if('' != beginTime2){
    		beginTime = beginTime2
    	}
    
	    // 查询  
	    $http.get(HRY.modules.calculate+"appReportController/findTotalCustomerProfitForReport?date="+beginTime+"&customerType="+customerType).
	       	success(function(data) {
	       		$scope.totalCustomerProfitForReport = data;
	       	}); 
    }
    
     function fnList () {
     
    	$("#fristData").val('');
      $http.get(HRY.modules.calculate+"appReportController/findTotalCustomerProfitForReport?beginTime=''&customerType = ''").
	       	success(function(data) {
	       		$scope.totalCustomerProfitForReport = data;
	       		 hryCore.initPlugins();
	       	});     
	    hryCore.initPlugins();
    }
    
    	$scope.querySpecific = querySpecific;
     	$scope.fnList = fnList;
    	querySpecific();   
// =========================================================================================     	
  

    		
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});