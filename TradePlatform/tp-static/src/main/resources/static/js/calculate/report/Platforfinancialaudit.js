/**
 * student.js
 */
define(['app','hryTable','pikadayJq'], function (app,DT) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    
    
	var b = 0; // 校验是否有失败的文本框  0表示true 
	var c = true; // 校验是否有未校验的文本框未填写 
    var d = new Date();
    var t=d.getTime()-1000*60*60*24;
	var f=new Date(t);
    var customerType = '';
	var strDate = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
    var begDate = f.getFullYear()+"-"+(f.getMonth()+1)+"-"+f.getDate()	
	
	var beginTime = begDate
    var endTime = strDate	
    function querySpecific2 (){
	   // 查询币种校验的数据
	    $http.get(HRY.modules.calculate+"appReportController/findTotalAccountForReport?beginTime="+beginTime+"&endTime="+endTime+"&customerType="+customerType).
	       	success(function(data) {
	       		$scope.totalAccountForReport = data;
	       	}); 
    }
    
    
    
     function fnList () {
    	
      $http.get(HRY.modules.calculate+"appReportController/findTotalAccountForReport?beginTime="+beginTime+"&endTime="+endTime+"&customerType="+customerType).
	       	success(function(data) {
	       		$scope.totalAccountForReport = data;
	       	}); 
	    
     }	
 
     
  // --------平台钱的校验--------------------------------------------------------------------------------   
     
     
     $scope.accountAudit = accountAudit ; 
     function accountAudit(){
     }

 // ---------币种校验方法--------------------------------------------------------
     
     $scope.currencyAudit=currencyAudit;
     $scope.removeClass2 = removeClass2;
     $scope.submitValue = submitValue;
     // 判断输入的值与平台计算的值是否相等
     function currencyAudit(coinCode,str){
       var sss = $($("#"+coinCode+str).siblings()[0]).val();
       var aaa = $("#"+coinCode+str).val();
       if(sss ==aaa){
    		$("#"+coinCode+str).css({"color":"blue","border": "2px solid blue"});
       		$("#"+coinCode+str).removeClass("test");
       	}else{
       		$("#"+coinCode+str).css({"color":"red","border": "2px solid red"});
       		$("#"+coinCode+str).attr("class","test");
       }
     }
     // 清空input的值 
     function removeClass2(coinCode,str){
     	$("#"+coinCode+str).val("")
     	$("#"+coinCode+str).css('color','')
        $("#"+coinCode+str).css('border','')
     }
     
  //   $scope.submitValue2 = checkValue;
    
     // 初始化校验所有的数据 
     function checkValue(){
	      $("input[name='settlement']").each(function (){
		     	var ss = $(this).val();
		     	var aa = $(this).next().val();
		     	if(ss == aa){
		     		$(this).css({"color":"blue","border": "2px solid blue"});
		     		$(this).removeClass("test");
		     	}else{
		     		$(this).css({"color":"red","border": "2px solid red"});
       				$(this).attr("class","test");
		     	}
		 })
		 clearInterval(sss);
     }
     
     $scope.formData ={};
     // 通过审核按钮
     function submitValue(){
     	var test = $("input[class='test']")
     	
     	if(test.length>0){
     		growl.addInfoMessage("您有未校验成功的订单");
     		return ;
     	}
     	
	     $("input[name='settlement']").each(function (){
	     	var ss = $(this).val();
	     	if("" == ss){
	     		c=false;
	     	}
	     })
     
     	if(c){
	     	$http({
						method : 'POST',
						url : HRY.modules.calculate+'appReportSettlement/postAudit',
						data : $.param($scope.formData), 
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
						growl.addInfoMessage(data.msg);
					});
     		}else{
     			growl.addInfoMessage("请填写完整的校验单");
     		}
     	
     }
     
   
     
     
     
 // ------------------------------------------------------------------------------------------
     
     
  
    function querySpecific (){
    	
    	// 查询钱的校验数据
		$http.get(HRY.modules.calculate+"appReportController/findTotalCurrencyForReport?beginTime="+beginTime+"&endTime="+endTime).
		success(function(data) {
			$scope.totalCurrencyForReport = data;
	    });
    }  
    
    function fnList () {
    	$http.get(HRY.modules.calculate+"appReportController/findTotalCurrencyForReport?beginTime="+beginTime+"&endTime="+endTime).
		success(function(data) {
			$scope.totalCurrencyForReport = data;
	    });
	    
	    hryCore.initPlugins();
	   
    }
    
    	$scope.querySpecific = querySpecific;
    	$scope.querySpecific2 = querySpecific2;
     	$scope.fnList = fnList;
    	querySpecific2(); 
    	querySpecific(); 
   // 	hryCore.initPlugins();
    	
    	 
    	var sss = setInterval(checkValue,1000);
    	
    	
    }
   
    return {controller:controller};
});



