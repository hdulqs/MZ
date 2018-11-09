/**
 * student.js
 */
define(['app','hryTable','pikadayJq','hryUtil'], function (app,DT) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	
    	
    	var table =  $("#table2");
    	$scope.formData = {};
    	
       if($stateParams.page=="list"){
       	
        	$scope.serchData={
        		
        	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.calculate+'/appReportSettlementcoin/list';
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
										"data" : "coinCode"
									}, {
										"data" : "trueName"
									}, {
										"data" : "customerType"
									}, {
										"data" : "beginCoinCount"
									}, {
										"data" : "endCoinCount"
									}, {
										"data" : "inCoinCount"
									}, {
										"data" : "outCoinCount"
									}, {
										"data" : "outCoinFee"
									},
									
									{
										"data" : "buyTransactionCount"
									},
									{
										"data" : "sellTransactionCount"
									},
									
										{
										"data" : "lendMoney"
									}, {
										"data" : "repaylendMoney"
									}, {
										"data" : "startSettleDate"
									}, {
										"data" : "endSettleDate"
									}, {
										"data" : "stutus"
									}
									
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 4,"orderable" :true,
											"render" : function(data, type, row) {
												if(data == 1){
													return "甲类账户"
												}if(data == 2){
													return "乙类账户"
												}if(data == 3){
													return "丙类账户"
												}
												return "其他类型"
											}
										},{
											"targets" : 16,"orderable" :true,
												
											"render" : function(data, type, row) {
												if(data ==  -1){
													return "初始状态";
												}else if(data == 1){
													return "平台确认状态";
												}else{
													return "客户确认状态";
												}
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
				DT.excel(table,this.serchData,"会员币种结算单");
			}
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/web/'+url+'/anon';
            }
            
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url){
            	var ids = DT.getSelect(table);
            	//var ids = transform(selectes);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		window.location.href='#/web/'+url+'/'+ids[0];
            	}
            }
            
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		$rootScope.id= ids[0];
            		window.location.href='#/web/'+url+'/'+ids[0];
            	}
            }
            
            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
            	hryCore
				.CURD({
						url:HRY.modules.web+url+"?ids[]="+ids
				 })
				.remove(null,
						function(data) {
		                	if(data.success){
		                		//提示信息
		                		growl.addInfoMessage('删除成功')
		                		//重新加载list
		                		fnList();
		                	}else{
		                		growl.addInfoMessage(data.msg)
		                	}
		                },
						function(data) {
							growl.addInfoMessage("error:"+ data.msg);
						});
			}
        }
    	                              	
    	hryCore.initPlugins();
       
    }
    return {controller:controller};
});



