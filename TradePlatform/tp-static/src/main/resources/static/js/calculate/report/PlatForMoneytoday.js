/**
 * student.js
 */
define(['app','hryTable','pikadayJq'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    var beginTime = "2015-08-31"
    var endTime = "2022-08-31"	
    function querySpecific (){
    	
    	var beginTime2 = $("#fristData").val();
    	var endTime2 = $("#endTime").val();
    	var customerType = $("#customerType").val();
    	if('' != beginTime2){
    		beginTime = beginTime2
    	}
    	if('' != endTime2){
    		endTime = endTime2;
    	}	
	    // 查询  
	    $http.get(HRY.modules.calculate+"appReportController/findTotalEarningsForReport?beginTime="+beginTime+"&endTime="+endTime).
	       	success(function(data) {
	       		
	       		$scope.totalEarningsForReport = data;
	       	}); 
    }
    
    
     function fnList () {
    	$("#fristData").val('');
    	$("#endTime").val('');
    	beginTime = "2015-08-31"
    	endTime = "2022-08-31"
      $http.get(HRY.modules.calculate+"appReportController/findTotalEarningsForReport?beginTime="+beginTime+"&endTime="+endTime).
	       	success(function(data) {
	       		
	       		$scope.totalEarningsForReport = data;
	       		 hryCore.initPlugins();
	       	});     
	    hryCore.initPlugins();
    }
    
    	$scope.querySpecific = querySpecific;
     	$scope.fnList = fnList;
    	querySpecific();   
    	
    	
    	
    	

        hryCore.initPlugins();
        
    }
    return {controller:controller};
});