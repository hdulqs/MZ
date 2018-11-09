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
    	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        	//		config.ajax.url = HRY.modules.account+'fund/appaccount/list.do';
        		config.ajax.url = HRY.modules.calculate+"appReportSettlement/operationAccountFundInfoLogList";
        		config.ajax.data = function(d){
        								// 设置select下拉框
										DT.selectData($scope.serchData);
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	 {
										"data" : "operatorName"
									}, {
										"data" : "userName"
									}, {
										"data" : "creatDate"
									}, {
										"data" : "operatorName"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
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
            $scope.getDate = getDate;
             function getDate(time){
             	var newTime = new Date(time); //就得到普通的时间了  
                    return newTime.getFullYear() + "-" + (newTime.getMonth() + 1) + "-" + newTime.getDate() + "  " + newTime.getHours() + ":" + newTime.getMinutes() + ":" + newTime.getSeconds() ;
               };
            $scope.seeDeatil = seeDeatil;
	            // 全部余额核算错误信息
	            function seeDeatil(){
            	var context = DT.getSelect(table,"context");
            	var data=eval("("+context+")");
            	$scope.hisorylist=data[0].hisorylist;
          		$scope.sureoldlist=data[0].sureoldlist;
          		$scope.oldAccountFundInfolist=data[0].oldAccountFundInfolist;
          		$scope.newAccountFundInfolist=data[0].newAccountFundInfolist;
            	if(context.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	$scope.context=context;
            	$("#contextdetial").removeClass("hide");
					layer.open({
						type : 1,
						title : false, // 不显示标题
						closeBtn : 2,
						area : [ '1300px', '600px' ],
						shadeClose : true,
						content : $('#contextdetial')
					});
            		
            }
           
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});