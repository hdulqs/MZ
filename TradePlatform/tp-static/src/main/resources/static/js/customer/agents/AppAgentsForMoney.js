/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    	
    	var table =  $("#table2");
 
    	

    	
		//----------------------  查看页面路径   ---------------------------------------------
		if ($stateParams.page == "see") {
			 hryCore.CURD({
						url : HRY.modules.customer+"agents/commissionDeploy/see/"+$stateParams.id
					}).get(null,
				function(data) {
                //	$scope.model = data;
             	 
		    }, function(data) {
				Materialize.toast("error:" + data.msg, '1000');
			});
		}
       	
 
//------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	
        	// var agentName = $("#agentNameId").val();
       // 	 var agentName = '18201202026';
        	
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.customer+'agents/appAgentscustromer/findAgentsForMoney';
        		config.ajax.data = function(d){
        								//设置select下拉框
        								// DT.selectData($scope.serchData);
        								
					        			$.each($scope.serchData, function(name,value){
					        				
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "customerName"
									},/* {
										"data" : "address"
									}, 
*/
									{
										"data" : "fixPriceCoinCode"
									},

									/*{
										"data" : "fixPriceType"
									},
									*/
									
									{
										"data" : "oneMoney"
									},  {
										"data" : "twoMoney"
									},{
										"data" : "threeMoney"
									}, {
										"data" : "sumMoney"
									}, {         
										"data" : "deawalMoney"
									}, {
										"data" : "modified"
									}, {
										"data" : "surplusMoney"
									}, {
										"data" : "created"
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
											"targets" : 2,"orderable" :false,
												
											"render" : function(data, type, row) {
												if(data!=null){
													return data;
												}else{
													return "暂无"
												}
											}
										},
										
										{
											"targets" : 9,
												
											"render" : function(data, type, row) {
												return "<font color='red'>"+data+"</font>"
											}
										},{
											"targets" : 10,"orderable" :false,
											"render" : function(data, type, row) {
												if(null == data){
													return "暂无派发记录";
												}
												return data;
											}
										}
										
										
										
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
     
            $scope.fnList=fnList;//刷新方法
            
            $scope.fnPost=fnPost; // 派发佣金

            $scope.fnBatchPost=fnBatchPost; // 批量派发佣金
            
            //批量派发佣金
            function fnBatchPost(){
            	var ids = DT.getSelect(table);
            	var data = DT.getRowData(table);
            	if(ids.length==0){
        			layer.msg('请选择数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}else{  
            		layer.confirm("确定派发佣金？", {
    					btn : [ '确定', '取消' ], // 按钮
    				}, function() {
    					for(var i=0; i<ids.length; i++){
    						var fixPriceCoinCode=data[i].fixPriceCoinCode;
        					$http.get(HRY.modules.customer+"agents/angestasmoney/postMoney?id="+ids[i]+"&fixPriceCoinCode="+fixPriceCoinCode)
    					}
    					layer.msg('派发成功', {
    		    		    icon: 1,
    		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
    		    		});
    				});
            		
            	}
            
            }
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
    	// 派发佣金	
            function fnPost(){
            	var ids = DT.getSelect(table);
            	var data = DT.getRowData(table);
            	var  fixPriceCoinCode=data[0].fixPriceCoinCode;
            	if(ids.length==0){
        			layer.msg('请选择数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}else if(ids.length>1){
            		layer.msg('只能选择一条数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else if(data[0].surplusMoney<=0){
            		layer.msg('待派发佣金为0', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else{  
            		layer.confirm("确定派发佣金？", {
    					btn : [ '确定', '取消' ], // 按钮
    					ids : ids,
    					fixPriceCoinCode:fixPriceCoinCode
    				}, function() {
    					$http.get(HRY.modules.customer+"agents/angestasmoney/postMoney?id="+ids[0]+"&fixPriceCoinCode="+fixPriceCoinCode).
		                 success(function(data) {
		                	layer.msg(data.msg, {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
		                	fnList();
		                 });
    				});
            		
            	}
            }

        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});