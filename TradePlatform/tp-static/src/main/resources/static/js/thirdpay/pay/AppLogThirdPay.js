/**
 * AppLogThirdPay.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	 
        
        var table =  $("#dataTable");
        
        $scope.hryCore = hryCore;
        
        //------------------------查看页面路径---------------------------------------------
        if($stateParams.page=="see"){
			 hryCore.CURD({url : HRY.modules.oauth+"user/appuser/see/"+$stateParams.id})
			        .get(
			        	   null,
			        	   function(data) {
			        		   $scope.formData = data;
			        		   hryCore.RenderHTML(data);//异步回显控件
			               },
			               function(data) {
			            	   growl.addInfoMessage("error:" + data.msg);
			               });
        }
        
       
        /**
         * 添加页面
         */
        if($stateParams.page=="add"){
        	
        	$scope.formData = {};
			$scope.processForm = function() {
				//所属公司
				$scope.formData.companySet= $("#companySet").val();
				//所属门店
				$scope.formData.shopSet = $("#shopSet").val();
				//所属部门
				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
				//拥有角色
				$scope.formData.appRoleSet= hryCore.ArrayToString($("#appRoleSet").val());
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'user/appuser/add.do',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href='#/oauth/user/appuser/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});
				
			};
        }
        
     
        
        /**
         * 修改页面
         */
        if($stateParams.page=="modify"){
        	
        	$http.get(HRY.modules.oauth+"user/appuser/see/"+$stateParams.id).
            success(function(data) {
            	$scope.formData = data;
     		    hryCore.RenderHTML(data);//异步回显控件
            });
        	
        	$scope.processForm = function() {
        		
        		 
        		//图片路径
        		$scope.formData.picturePath = $("#imgSrc").val();
 				//所属公司
 				$scope.formData.companySet= $("#companySet").val();
 				//所属门店
 				$scope.formData.shopSet = $("#shopSet").val();
 				//所属部门
 				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
 				//拥有角色
 				$scope.formData.appRoleSet= hryCore.ArrayToString($("#appRoleSet").val());
 				
 				 
 				
 				$http({
 					method : 'POST',
 					url : HRY.modules.oauth+'user/appuser/modify',
 					data : $.param($scope.formData), 
 					headers : {
 						'Content-Type' : 'application/x-www-form-urlencoded'
 					}
 				})
 				.success(function(data) {
 					if (data.success) {
 						growl.addInfoMessage('修改成功')
 						window.location.href='#/oauth/user/appuser/list/anon';
 					} else {
 						growl.addInfoMessage(data.msg)
 					}
 				});
 				
 			};
        	
        	
        	
        }
        
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){

        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.web+'pay/applogthirdpay/list';
        		config.ajax.data = function(d){
						        			$.each($scope.serchData, function(name,value){
												if(""!=value){
													eval("d."+name+"='"+value+"'");
												}
											});
						           };
        		config.columns = [	{
        								"data" : "id"
        							},{
        								"data" : "userId"
        							}, {
										"data" : "thirdPayConfig"
									}, {
										"data" : "requestNum"
									}, {
										"data" : "money"
									}, {
										"data" : "responseMsg"
									}, {
										"data" : "remark1"
									}, {
										"data" : "created"
									}
        		                  ]
        		
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											},
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
            
          
             
        }
        
        //加载插件
		hryCore.initPlugins();
		//上传插件
		hryCore.uploadPicture();
    }
    //----------------------------

    
    return {controller:controller};
});