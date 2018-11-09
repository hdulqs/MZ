define(['app','hryTable' ,'hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "奖励币的记录";
    	//初始化js 插件

    	//------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	var table =  $("#table2");
        	$scope.serchData="";
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appCoinRewardRecord/list';
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
										"data" : "customerName"
									}, {
										"data" : "customerMobil"
									},{
										"data" : "recordType"
									},{
										"data" : "recordNum"
									},{
										"data" : "coverCustomerId"
									},{
										"data" : "coverCustomerName"
									},{
										"data" : "coverCustomerMobile"
									},{
										"data" : "status"
									},{
										"data" : "failMsg"
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
											"targets" : 3,
											"render" : function(data, type, row) {
												//类型     1实名得币  2推荐奖励得币  
												if(data=="1"){
													return "实名奖励币";
												}else if(data=="2"){
													return "推荐奖励得币";
												}else{
													return "类型错误";
												}
											}
										},{
											"targets" : 8,
											"render" : function(data, type, row) {
												//0成功  1失败
												if(data=="1"){
													return "失败";
												}else if(data=="0"){
													return "成功";
												}else{
													return "状态错误";
												}
											}
										},{
											"targets" : 9,
											"render" : function(data, type, row) {
												//0成功  1失败
												if(data=="" || data==null){
													return "派发成功！";
												}else{
													return data;
												}
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnList=fnList;//刷新方法
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"我方账户管理");
			}
            
            
            //添加按钮
            function fnAdd(url){}
            
            //查看按钮
            function fnSee(url){}
            
            //修改按钮
            function fnModify(url){}
            
            //删除按钮
            function fnRemove(url){}
        }
        
        hryCore.initPlugins();
        //上传插件
		hryCore.uploadPicture();
        
    }
    return {controller:controller};
});