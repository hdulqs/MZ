/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore){
    	var table =  $("#table2");
     
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.projName=HRY.modules.exchange;
        	var type=$stateParams.id;
        	$scope.states = $stateParams.id;
        	
        	$scope.serchData={};
        	
        	if(type == 1){
        		$scope.serchData={
        				transactionType_in:"1,3"
        		};
        		
        	}if(type == 2){
        		$scope.serchData={
        				transactionType_in:"2,4"
        		};
        	}
        	
        	
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索                               
        		config.ajax.url = HRY.modules.account+'fund/apptransaction/list';
        		config.ajax.data = function(d){
					//设置select下拉框
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
										"data" : "transactionNum"
									}, {
										"data" : "userName"
									}, {
										"data" : "transactionMoney"
									}, {
										"data" : "fee"
									}, {
										"data" : "currencyType"
									}, {
										"data" : "transactionType"
									}, {
										"data" : "modified"
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
											"targets" : 2,
												
											"render" : function(data, type, row) {
												return "<font color='blue'>"+data+"</font>";
											}
										},
										{
											"targets" : 4,
												
											"render" : function(data, type, row) {
												return "<font color='red'>"+data+"</font>";
											}
										}
										,
										{
											"targets" : 6,
												
											"render" : function(data, type, row) {
												if(data == 1 || data == 3){
													return "充值";
												}
												if(data == 2 || data == 4){
													return "提现";
												}
												
											}
										}
									
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnList=fnList;//刷新方法
            
            function fnList(){
              	 table.DataTable().draw();
            }
            

        
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});