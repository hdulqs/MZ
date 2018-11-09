/**
 * student.js
 */
define(['app','hryTable','pikadayJq','hryUtil'], function (app,DT) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	
    	
   //------------------------ 列表页面路径  ---------------------------------------------
        
    	var table =  $("#table2");
    	$scope.formData = {};
    	
    	$scope.serchData={};
    	//--------------------加载dataTable--------------------------------
    	var config = DT.config();
    		config.bAutoSerch = true; //是否开启键入搜索
    		config.ajax.url = HRY.modules.customer + "person/apppersoninfo/listPersonReport";
    		config.ajax.data = function(d){
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
									"data" : "mobilePhone"
								}, {
									"data" : "trueName"
								}, {
									"data" : "totalAvailableMoney"
								}, {
									"data" : "totalFrozenMoney"
								},{
									"data" : "totalRechargeMoney"
								},{
									"data" : "totalWithdrawalsMoney"
								},{
									"data" : "moneyChangeRate"
								},{
									"data" : "profitRate"
								}
								
    		                  ]
    		config.columnDefs  = [
									{
										"targets" : 0,"orderable" :false,
											
										"render" : function(data, type, row) {
											return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
										}
									}/*,{
										"targets" : 3,"orderable" :true,
											
										"render" : function(data, type, row) {
											if(data ==  1){
												return "甲类账户"
											}if(data == 2){
												return "乙类账户"
											}if(data == 3){
												return "丙类账户"
											}
											return "333"
										}
									},{
										"targets" : 7,"orderable" :true,
											
										"render" : function(data, type, row) {
											if(data == "en"){
												return "国际站"
											}else{
												return "中国站"
											}
										}
									}*/
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
				DT.excel(table,this.serchData,"用户资金变动收益报表");
			}
            
            $scope.fnModify=fnModify;//
            function fnModify(){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
              	hryCore
				.CURD({
						url: HRY.modules.calculate+'/appReportSettlement/settlementByCustomerId/'+ids
				 })
				.remove(null,
						function(data) {
		                	if(data.success){
		                		//提示信息
		                		growl.addInfoMessage('成功')
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
    	
    	hryCore.initPlugins();
       
    }
    return {controller:controller};
});



