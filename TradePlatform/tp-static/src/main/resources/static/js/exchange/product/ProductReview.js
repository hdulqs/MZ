/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
	
 	 var table =  $("#table2");
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'product/productReview/list';
        		config.ajax.data = function(d){
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							},{
        								"data" : "productName"
        							}, {
										"data" : "status"
									}, /*{
										"data" : "reviewRank"
									}, {
										"data" : "reviewSkill"
									}, {           
										"data" : "reviewApply"
									}, {
										"data" : "reviewVista"
									},*/ {
										"data" : "reviewContent"
									}, {
										"data" : "customerName"
									},/* {
										"data" : "praise"
									}, {
										"data" : "criticize"
									} ,*/ {
										"data" : "created"
									} // 
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
												if(data!=null&&data=="0"){
													return "<font color='red'>待审核</font>"
												}
												if(data!=null&&data=="1"){
													return "<font color='blue'>已审核</font>"
												}
												if(data!=null&&data=="2"){
													return "<font color='gray'>审核失败</font>"
												}
												return "";
											}
										}
									 ]
        	DT.draw(table,config);
   //--------------------加载dataTable--------------------------------
        		
            $scope.fnPass=fnPass;//see按钮方法
            $scope.fnDelete=fnDelete;//remove方法
            $scope.fnList=fnList;//刷新方法
           
       
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
 
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnPass(){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else{
            		
            $http.get(HRY.modules.exchange+"product/productReview/pass/"+ids).
      	        success(function(data) {
      	        	if(data.success){
      	        		growl.addInfoMessage(data.msg);
      	        		fnList();
      	        	}else{
      	        		growl.addInfoMessage("error:"+ data.msg);
      	        	}
      	        });
            		
            	}
            }
            
            //删除按钮
            function fnDelete(){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	hryCore.CURD({
						url:HRY.modules.exchange+"product/productReview/delete/"+ids
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