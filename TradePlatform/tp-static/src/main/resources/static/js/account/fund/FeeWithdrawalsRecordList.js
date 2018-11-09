/**
 * 手续费账户提现列表页面
 */
define(['app','hryTable' ,'hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "手续费账户提现记录";
    	//初始化js 插件

    	//------------------------列表页面路径---------------------------------------------
        //if($stateParams.page=="list"){
        	var table =  $("#table2");
        	$scope.serchData="";
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'/fund/feeWithdrawalsRecord/list';
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
										"data" : "ourAccountId"
									},{ 
										"data" : "ourAccountNum"
									}, {
										"data" : "withdrawalsMoney"
									},{
										"data" : "status"
									},{
										"data" : "created"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 4,
											"orderable" :false,
											"render" : function(data, type, row) {
												if(data==0){
													return "成功";
												}else{
													return "失败";
												}
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnList=fnList;//刷新方法
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"手续费提现列表");
			}
            
		
            //添加按钮
            function fnAdd(url){}
            
            //查看按钮
            function fnSee(url){}
            
            //修改按钮
            function fnModify(url){}
            
            //删除按钮
            function fnRemove(url){}
        //}
            
        
        hryCore.initPlugins();
        //上传插件
		hryCore.uploadPicture();
        
    }
    return {controller:controller};
});