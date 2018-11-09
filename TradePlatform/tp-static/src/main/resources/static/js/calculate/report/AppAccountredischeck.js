/**
 * student.js
 */
define(['app','hryTable' ,'hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore){
    	$rootScope.headTitle = $rootScope.title = "会员资金核算";
    	//初始化js 插件
    	var table =  $("#table2");
    	    if($stateParams.page=="allerrorrightinfo"){
        	$scope.allfnaccounting = allfnaccounting;
    		var ids=$stateParams.id;
    		$scope.stateParams="ids";
    		$http.get(HRY.modules.calculate+"/redisandsqlcheck/culAccountByCustomerErrorAndRightInfosureold/"+ids).
        	success(function(data) {
        		$scope.data=data;
        	});
        	$scope.funddetial1 = funddetial1;
              // 余额核算到数据库
            function funddetial1(customerId){
            	  debugger
	          		$http.get(HRY.modules.calculate+"/redisandsqlcheck/culAccountByCustomerErrorAndRightInfosureold/"+customerId).
	              	success(function(data) {
	              		$scope.hisorylist=data[0].hisorylist;
	              		$scope.sureoldlist=data[0].sureoldlist;
	              		$scope.oldAccountFundInfolist=data[0].oldAccountFundInfolist;
	              		$scope.newAccountFundInfolist=data[0].newAccountFundInfolist;
	              		
	              	});
		        	$("#funddetial").removeClass("hide");
					layer.open({
						type : 1,
						title : false, // 不显示标题
						closeBtn : 2,
						area : [ '1300px', '600px' ],
						shadeClose : true,
						content : $('#funddetial')
					});
              }
        }
		//------------------------查看页面路径---------------------------------------------
        if($stateParams.page=="allerrorinfo"){
        	 $scope.allfnaccounting = allfnaccounting;
             // 全部余额核算到数据库
             function allfnaccounting(){
             	// 提示框
     			layer.confirm('你确定要全部余额核算到数据库吗?请慎重!', {
     				btn : [ '确定', '取消' ], // 按钮
     				ids : ids
     			}, function() {	
     				// 提示框
     				layer.closeAll();
         			layer.confirm('你确定要全部余额核算到数据库吗?请慎重!请慎重!', {
         				btn : [ '确定', '取消' ], // 按钮
         				ids : ids
         			}, function() {	
         				layer.closeAll();
         				layer.confirm('你确定要全部余额核算到数据库吗?请慎重!请慎重!请慎重!', {
             				btn : [ '确定', '取消' ], // 按钮
             				ids : ids
             			}, function() {	
             				layer.closeAll();
         				//加载层
                 		var index = layer.load(0, {shade: false}); //0代表加载的风格，支持0-2
 		            		$http.get(HRY.modules.calculate+"/redisandsqlcheck/culAccountAllCustomersureold").
 			            	success(function(data) {
 			            		layer.close(index);
 			            		// 提示信息
 								layer.msg('全部余额核算到数据库成功', {
 									icon : 1,
 									time : 2000
 								// 2秒关闭（如果不配置，默认是3秒）
 								});
 			            	});
 		            		
             			});
         			});
     			});
             }
        	
        	
        	if($stateParams.id=="anon"){
        		$scope.stateParams="all";
        		//加载层
        		var index = layer.load(0, {shade: false}); //0代表加载的风格，支持0-2
        		$http({
                    method:"get",
                    url:HRY.modules.calculate+"/redisandsqlcheck/culAccountAllCustomerErrorInfo",
                    timeout:500000}).
        		success(function(data) {
            		
            		layer.close(index);
            	//	var obj = JSON.parse(data);
            		$scope.data=data;
            		if($scope.data.length==0){

    					// 提示信息
    					layer.msg('没有错误信息', {
    						icon : 1,
    						time : 2000
    					// 2秒关闭（如果不配置，默认是3秒）
    					});

            		}
            		
            	});
        	}else{
        		var ids=$stateParams.id;
        		$scope.stateParams="ids";
        		$http.get(HRY.modules.calculate+"/redisandsqlcheck/culAccountByCustomerErrorInfosureold/"+ids).
            	success(function(data) {
            		$scope.data=data;
            		if($scope.data.length==0){

    					// 提示信息
    					layer.msg('没有错误信息', {
    						icon : 1,
    						time : 2000
    					// 2秒关闭（如果不配置，默认是3秒）
    					});

            		}
            	});
        	
        	}
        	
        	  $scope.funddetial = funddetial;
              // 余额核算到数据库
              function funddetial(customerId){
            	  
	          		$http.get(HRY.modules.calculate+"/redisandsqlcheck/culAccountByCustomerErrorInfosureold/"+customerId).
	              	success(function(data) {
	              		debugger
	              		$scope.hisorylist=data[0].hisorylist;
	              		$scope.sureoldlist=data[0].sureoldlist;
	              		$scope.oldAccountFundInfolist=data[0].oldAccountFundInfolist;
	              		$scope.newAccountFundInfolist=data[0].newAccountFundInfolist;
	              		
	              	});
          	
          	
        	
		        	$("#funddetial").removeClass("hide");
					layer.open({
						type : 1,
						title : false, // 不显示标题
						closeBtn : 2,
						area : [ '1300px', '600px' ],
						shadeClose : true,
						content : $('#funddetial')
					});
              }
        }
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appaccount/list.do';
        		config.ajax.data = function(d){
        								// 设置select下拉框
										DT.selectData($scope.serchData);
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "userName"
									}, {
										"data" : "trueName"
									},  {
										"data" : "currencyType"
									}, {
										"data" : "accountNum"
									}, {
										"data" : ""//总资产
									},{
										"data" : "hotMoney"
									}, { 
										"data" : "coldMoney"
									}, {
										"data" : "lendMoney"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},
										{
											"targets" :5,

											"render" : function(data, type, row) {
												return row.hotMoney+row.coldMoney;
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
      
            $scope.fnList=fnList;//刷新方法
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
          //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"会员资产核算");
			}
        
            $scope.fnaccounting = fnaccounting;
            // 余额核算到数据库
            function fnaccounting(){
            	var customerIds = DT.getSelect(table,"customerId");
            	if(customerIds.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	if(customerIds.length>1){
            		growl.addInfoMessage('请选择一条数据')
            		return false;
            	}
            	// 提示框
    			layer.confirm('你确定要余额核算到数据库吗?请慎重!', {
    				btn : [ '确定', '取消' ], // 按钮
    				ids : ids
    			}, function() {		
    				layer.closeAll();	
        		$http.get(HRY.modules.calculate+"/redisandsqlcheck/culAccountByCustomersureold/"+customerIds).
            	success(function(data) {
            		
					// 提示信息
					layer.msg('余额核算到数据库成功', {
						icon : 1,
						time : 2000
					// 2秒关闭（如果不配置，默认是3秒）
					});
                 
				
            	});
    			});
        	
            }
           
            $scope.allfnaccountingerrorTomongoSure = allfnaccountingerrorTomongoSure;
            // 全部余额核算错误信息Tomongo
            function allfnaccountingerrorTomongoSure(url){
           
						$("#matchDetailDiv").removeClass("hide")
						layer.open({
							type : 1,
							title : false, // 不显示标题
							closeBtn : 2,
							area : [ '400px', '200px' ],
							shadeClose : true,
							content : $('#matchDetailDiv')
						});
        }
            $scope.allfnaccountingerrorTomongo = allfnaccountingerrorTomongo;
            // 全部余额核算错误信息Tomongo
            $scope.days=1;
            function allfnaccountingerrorTomongo(url){
		            	// 提示信息
		    			layer.confirm('可能时间有点长，请耐心等待', {
		    				btn : [ ] // 按钮
		    			}, function() {	
		    				
		    			});
		        		$http({
		                    method:"get",
		                    url:HRY.modules.calculate+"/redisandsqlcheck/culSureOldAccountAllCustomerErrorInfoToRedis?days="+$scope.days,
		                    timeout:500000}).
		        		success(function(data) {
		            		layer.close(index);
		            		$scope.data=data;
		            		if($scope.data.length==0){
	
		    					// 提示信息
		            			layer.confirm('没有错误信息', {
		            				btn : [ ] // 按钮
		            			}, function() {	
		            				
		            			});
	
		            		}else{
		            			// 提示信息
		            			layer.confirm(data.msg, {
		            				btn : [] // 按钮
		            			}, function() {	
		            				
		            			});
		            		}
		            		
		            	});
				
        		
        		
        }
            $scope.allfnaccountingerror = allfnaccountingerror;
	            // 全部余额核算错误信息
	            function allfnaccountingerror(url){
						layer.closeAll();
		                window.location.href='#/calculate/'+url+'/anon';
            		
            }
	     
          
            
            // 单个客户余额核算错误信息
            $scope.fnaccountingerrorAndRight = fnaccountingerrorAndRight;
            function fnaccountingerrorAndRight(){
            	var customerIds = DT.getSelect(table,"customerId");
            	if(customerIds.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
            	window.location.href='#/calculate/report/appaccountredischeck/allerrorrightinfo/'+customerIds;
            		
            }
        // 单个客户余额核算错误And正确信息
            $scope.fnaccountingerror = fnaccountingerror;
            function fnaccountingerror(url){
            	var customerIds = DT.getSelect(table,"customerId");
            	if(customerIds.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
            	window.location.href='#/calculate/'+url+'/'+customerIds;
            		
            }
           
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});