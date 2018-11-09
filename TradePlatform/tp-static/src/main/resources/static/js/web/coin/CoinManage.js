/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	
    	var html = "";
    	var divControl = "";
    	
    	$scope.isShow=false;
    	
    	// 生成下拉框  方法
    function createSelect(selectId){
    	$http.get(HRY.modules.web+"/coin/bitCoinController/findProductSelect?userName="+userName+"&type="+type).
                 success(function(data) {
                 	if(""!=data){
                 		html = "";
                 		for(var i=0;i<data.length;i++){
                 			 //	var html = "";
                 			 html+= "<option value='"+data[i].coinCode+"'>"+data[i].name+"</option>";
                 			}                     
                 		}          
                 	
                 	if(""!= html){
                 		$("#"+selectId).empty();
                 		$("#"+selectId).append(html);
                 		    hryCore.initPlugins();
                 	}
           });
    }
    	
    	
    	// 创建账户
   	$scope.create = function(){
   					debugger
   				var userName = $("#userName").val();
   				var type = $("#type").val();
   				$http.get(HRY.modules.web+"/coin/bitCoinController/createPublick?userName="+userName+"&type="+type).
                 success(function(data) {
                 		//	var publick = "<font size='20px'>"+"成功创建的账户为:"+"</font><font color='red'>"+data.obj+"</font>"
                 	if(data.success){
                 		debugger  
                 		var s = "创建的账号为 :  "
                 		$("#create").val(s+data.obj);
                 	}else{
                 		alert(data.msg);
                 	}	
                 })
   			}
   		
   			
   			
   		//  查询某个币的余额	
    	$scope.selectBalance = function(){
    		var userName = $("#balanceUserName").val();
   			var type = $("#balanceType").val();
   			
   			$http.get(HRY.modules.web+"/coin/bitCoinController/balanceUserName?userName="+userName+"&type="+type).
                 success(function(data) {
                 	if(data.success){
                 		var s = "币的数量为 :  "
                 		$("#selectBalance").val(s+data.obj);
                 	}else{
                 		alert(data.msg);
                 	}	
                 })
    	}	
    	
    	
   			
   		//  查询某个币的流水	
    	$scope.selectRecord = function(){
    		debugger                              
   			var type = $("#recordType").val();
   			var userName = $("#userNameText").val();
   			$http.get(HRY.modules.web+"coin/bitCoinController/selectList?type="+type+"&userName="+userName).
                 success(function(data) {
                 	var Da={};
                 	if(data.success){
                 		$("#selectRecord").val(data.obj);
                 	}else{
                 		alert(data.msg);
                 	}	
                 })
    	}	
    	
    	
    	// 转币  
    	$scope.sendFrom = function(){
    		var account = $("#account").val();
   			var address = $("#address").val();
   			var amount = $("#amount").val();
   			var type = $("#sendFromSelect").val();
   			$http.get(HRY.modules.web+"coin/bitCoinController/sendFrom?account="+account+"&address="+address+"&amount="+amount+"&type="+type).
                 success(function(data) {
                
                 	if(data.success){
                 		if(data.obj != ""){
                 			var aa = $scope.StrongToObj(data.obj);
                 			if(aa.code == 8){
                 				$("#sendFromArea").val("转账成功");
                 				return ;
                 			}if(aa.code == 18){
                 				$("#sendFromArea").val("接口未开放");
                 				return ;
                 			}
                 			$("#sendFromArea").val("未知错误");
                 			return ;
                 		}
                 		
                 		$("#sendFromArea").val(data.msg);
                 		return ;
                 	}else{
                 		alert(data.msg);
                 	}                   
              })
    	}
    	
                                                                      
//    	// 转币  
//    	$scope.sendFrom = function(){
//    		var account = $("#account").val();
//   			var address = $("#address").val();
//   			var amount = $("#amount").val();
//   			var type = $("#sendFromSelect").val();
//   			$http.get(HRY.modules.web+"coin/bitCoinController/sendFrom?account="+account+"&address="+address+"&amount="+amount+"&type"+type).
//                 success(function(data) {
//                 if(data.success){
//                 		$("#balancePublicKey").val(data.obj);
//                 	}else{
//                 		alert(data.msg);
//                 	}	
//              })
//    	}
//    	
    	
    	// 查询某个币的所有用户 
    	$scope.getAllUsers = function(){
   			var type = $("#allUsersType").val();
   			$http.get(HRY.modules.web+"coin/bitCoinController/getAllUsers?type="+type).
                 success(function(data) {
                 	debugger
                 	var j = ""
                 	for(var key in data){
                 	//	var s = $("#getAllUsers").val();
                 		j =j +" \n "+ key +" :  "+data[key];
                 	}
                 	$("#getAllUsersText").val("用户   :  数量  \n "+j);
              })
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	// 显示跟隐藏方法        
    	$scope.showView = function(select,div){
    		debugger
    		// divControl
    		if(""!=divControl){
    			$("#"+divControl).attr("ng-show",false);
    			$("#"+divControl).attr("class","ng-hide");
    		}
    		$("#"+div).attr("ng-show",true);
    		$("#"+div).attr("class","");
    		divControl = div;
    		createSelect(select);
    		
    	}
    	
    	
    	$scope.StrongToObj = function(str){
    		 	var send = new Array();
                var send2 = new Array();
                var result = {};
    		if(str != ''){
    				var sendFrom = str.replace('{','').replace('}','').replace("\'","").replace("\'","");
                 			send = sendFrom.split(",");
                 			if(send.length>0){
                 				for(var j=0; j<send.length ; j++){
                 					send2 = send[j].split("=");
                 					for(var i=0; i<send2.length ; i++){
                 						if(send2[i] == "code"){
                 							result.code=send2[1];
                 						}
                 						if(send2[i] == "msg"){
                 							result.message=send2[1];
                 						}
                 					}
                 				}
                 			}
    		}
    		return result;
    	}
    	
    						
    						
    									
        							
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});