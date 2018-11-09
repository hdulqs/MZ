/**
 * student.js
 */
define(['app','hryTable','pikadayJq'], function (app,DT) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	
    	//初始化js 插件 
    	var table =  $("#table2");
    	
    	if($stateParams.page=="list"){
    	
    		$scope.formData={};
    		$scope.appCalculateAllCustomer="";
    		$scope.appCalculate = "";
 
    		function querySpecific() {

	    		var customerType = $("#customerType").val();
	    		var coinCode = $("#coinCode").val();
	    		var fristData = $("#fristData").val();
	    		var secondData = $("#secondData").val();
	    		
	    	 	if(fristData == "" && secondData == "" && customerType == ""){
		   			growl.addInfoMessage('请填写完整的数据');
	   			return false;
	    	}	
	    		
	    		
	    	$scope.formData.startDate =fristData 
	    	$scope.formData.endDate =secondData 
	    	$scope.formData.customerType = customerType
	    	$scope.formData.code = coinCode
	    			
	    			
	    	$http({
					method : 'POST',
					url : HRY.modules.calculate+'calculateapptransaction/apptransaction/queryCalculate.do',
					data : $.param($scope.formData), 
				//	params:{'appResourceStr':ids},
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
					$scope.appCalculate=data
				
				});
                 
    		}
    		
    		
    		
    		function queryWhole() {
    			
    			
    	
    		var fristDataAll = $("#fristDataForAll").val();
    			
    		var secondDataAll = $("#secondDataForAll").val();
	    		
	    	if(fristDataAll == "" && secondDataAll == ""){
		   			growl.addInfoMessage('请填写完整的数据');
	   			return false;
	    	}	
	    		
	    	$scope.formData.startDate =fristDataAll 
	    	$scope.formData.endDate =secondDataAll 

		     $http({
				method : 'POST',
				url : HRY.modules.calculate+'calculateapptransaction/apptransaction/queryCalculateAll.do',
				data : $.param($scope.formData), 
				//	params:{'appResourceStr':ids},
				headers : {	
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			})
			.success(function(data) {
				$scope.appCalculateAllCustomer=data;
			});
             	
		}
    		
    		
    		$scope.querySpecific = querySpecific;
    		
    		$scope.queryWhole = queryWhole;
    		                    
    		// alert("333")
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
//============================================================================================================================
//============================================================================================================================
    
	//------------------------查看页面路径---------------------------------------------
    	$scope.creatSelect = function (){
    		$http.get(HRY.modules.exchange+"product/exProductparameter/selectPeoductParameter").
	            	success(function(data) {
	            		 $scope.parameter=data[0]
	                	 $scope.productParameter = data;
	                	 var html = "";
	                	 for(var i = 0;i<data.length;i++){
	            		      html += "<option value='"+data[i].id+"' >"+data[i].name+"</option>"; 
	                	 }
	                	$("#wselectid").append(html); 
	                    hryCore.initPlugins(); 
	          });
    	}
    	
 //   	============================  全局公用方法   start  =============================================================================
 
    	
//   	=================================   全局公用方法   end  ========================================================================
		//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"){}
        
      //------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"){}
  
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list2"){}
        
        
        
        
//----------------------  2  --列表页面路径---------------------------------------------
        if($stateParams.page=="publish"){}
  
  
  
  
  
  hryCore.initPlugins();
       
    }
    return {controller:controller};
});