/**
 * AppUser.js
 */
define(['app','hryTable','module_md5','pikadayJq'], function (app, DT,md5) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$state'];
    function controller($scope,$rootScope,$http,$stateParams,$state,hryCore ){
    	 
        $rootScope.headTitle = $rootScope.title = "用户";
        var table =  $("#dataTable");
        $scope.orgid = $stateParams.orgid;
        
        //返回list页面方法
        $scope.returnList = function(){
        	$state.go("oauth.org.user", {page:"list",orgid:$scope.orgid,pid:"anon"});
        }
        
        //------------------------查看页面路径---------------------------------------------
        if($stateParams.page=="see"){
			 hryCore.CURD({url : HRY.modules.oauth+"user/appuser/see/"+$stateParams.pid})
			        .get(
			        	   null,
			        	   function(data) {
			        		   $scope.formData = data;
			        		   hryCore.RenderAllSelect(data);
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
        	$scope.formData.picturePath="";
			$scope.processForm = function() {
				//所属公司
				$scope.formData.companySet= $("#companySet").val();
				//所属门店
				$scope.formData.shopSet = $("#shopSet").val();
				//所属部门
				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
				//拥有角色
				$scope.formData.appRoleSet= hryCore.ArrayToString($("#appRoleSet").val());
				
				//图片
				//$scope.formData.picturePath = $("#imgSrc").val();
				
				$scope.formData.sex = $("#sex").val();
				
				$scope.formData.password = md5.hbmd5($scope.formData.password);
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
						$state.go("oauth.org.user", {page:"list",orgid:$scope.orgid,pid:"anon"});
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
        	
        	$http.get(HRY.modules.oauth+"user/appuser/see/"+$stateParams.pid).
            success(function(data) {
            	$scope.formData = data;
            	hryCore.RenderAllSelect(data);
     		    hryCore.RenderHTML(data);//异步回显控件
            });
        	
        	$scope.processForm = function() {
        		
        		$scope.formData.sex = $("#sex").val();
        		//图片路径
        		//$scope.formData.picturePath = $("#imgSrc").val();
 				//所属公司
 				$scope.formData.companySet= $("#companySet").val();
 				//所属门店
 				$scope.formData.shopSet = $("#shopSet").val();
 				//所属部门
 				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
 				//拥有角色
 				$scope.formData.appRoleSet= hryCore.ArrayToString($("#appRoleSet").val());
 				
 				
 				 
 				$scope.formData.password = md5.hbmd5($scope.formData.password);
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
 						$state.go("oauth.org.user", {page:"list",orgid:$scope.orgid,pid:"anon"});
 					} else {
 						growl.addInfoMessage(data.msg)
 					}
 				});
 				
 			};
        	
        }
        
        
        
        //重置密码
        if($stateParams.page=="resetpw"){
        	$http.get(HRY.modules.oauth+"user/appuser/see/"+$stateParams.pid).
            success(function(data) {
            	$scope.formData = data;
            });
        	
        	//重置方法
        	$scope.processForm = function() {
        		
        		$scope.formData.newPassword = md5.hbmd5($("#newPassword").val());
 				
 				$http({
 					method : 'POST',
 					url : HRY.modules.oauth+'user/appuser/resetpw',
 					data : $.param($scope.formData), 
 					headers : {
 						'Content-Type' : 'application/x-www-form-urlencoded'
 					}
 				})
 				.success(function(data) {
 					if (data.success) {
 						growl.addInfoMessage('修改成功')
 						$state.go("oauth.org.user", {page:"list",orgid:$scope.orgid,pid:"anon"});
 					} else {
 						growl.addInfoMessage(data.msg)
 					}
 				});
 				
 			};
        	
        }
        
        
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	//ui-route路由事件
        	/*$scope.$on('$stateChangeStart', function(evt, toState, toParams, fromState, fromParams){
        	})*/
        	 
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        	    config.onlyClick = function(obj){
        	    	var userid = obj.attr("userid");
        	    	$state.go("oauth.org.user", {page:"resetpw",orgid:$scope.orgid,pid:userid});
        	    	
        	    };
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.onlyEvent = false; //取消行点击变色
        		config.ajax.url = HRY.modules.oauth+'user/appuser/list?organizationId='+$stateParams.orgid;
        		config.ajax.data = function(d){
						        			$.each($scope.serchData, function(name,value){
												if(""!=value){
													eval("d."+name+"='"+value+"'");
												}
											});
						           };
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "username"
									}, {
										"data" : "name"
									}, {
										"data" : "mobilePhone"
									}, {
										"data" : "roleName"
									},  {
										"data" : "departmentName"
									},{
										"data" : "id"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 6,
												
											"render" : function(data, type, row) {
												return "<input type=\"button\" userid="+data+" class=\"btn btn-small\" value=\"重置密码\" />"
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
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(){
            	$state.go("oauth.org.user", {page:"add",orgid:$scope.orgid,pid:"anon"});
            }
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(){
            	 
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		//{ relative: "oauth_company_apporganization", inherit: false }  --- 留着备用  go的第三个参数
            		$state.go("oauth.org.user", {page:"see",orgid:$scope.orgid,pid:ids});
            	}
            }
            
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		$state.go("oauth.org.user", {page:"modify",orgid:$scope.orgid,pid:ids});
            	}
            }
            
            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url){
            	var select = DT.getSelect(table);
            	if(select.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
            	var ids = ""	;
            	for(var i = 0 ; i < select.length ; i++){
            		ids += select[i];
            		if(i!=select.length-1){
            			ids += ",";
            		}
            	}
            	
            	layer.confirm('你确定要删除吗？', {
	    			btn: ['确定','取消'], //按钮
	    			ids: ids
				}, function(){
					hryCore
					.CURD({
							url:HRY.modules.oauth+url+"/"+ids
					 })
					.remove(null,
							function(data) {
			                	if(data.success){
			                		//提示信息
			                		layer.msg('删除成功', {
    		                		    icon: 1,
    		                		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
    		                		});
			                		//重新加载list
			                		fnList();
			                	}else{
			                		layer.msg(data.msg, {
    		                		    icon: 1,
    		                		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
    		                		}); 
    		                		layer.close();
			                	}
			                },
							function(data) {
								growl.addInfoMessage("error:"+ data.msg);
							});
				});
            	
            	
            	
			}
            
        
             
        }
        
        //------------------------个人中心——个人资料---------------------------------------------
        if($stateParams.page=="info"){
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
      //------------------------个人中心——上传图片---------------------------------------------
        if($stateParams.page=="picture"){
        	
        	$http.get(HRY.modules.oauth+"user/appuser/see/"+$stateParams.id).
            success(function(data) {
            	$scope.formData = data;
     		    hryCore.RenderHTML(data);//异步回显控件
            });
        	
        	$scope.processForm = function() {
 				
        		$.ajax({
     	 		     type: 'POST',
     	 		     url : HRY.modules.oauth+'user/appuser/uploadpicture',
     	 		     data :{
     	 		    	id :  $scope.formData.id,
     	 		    	picturePath : $("#imgSrc").val()
     	 		     },
     	 		     dataType: "json",
     	 		     success: function(data){
     	 		    	if (data.success) {
     						growl.addInfoMessage('修改成功')
     					} else {
     						growl.addInfoMessage(data.msg)
     					}
     	 		     }
     	 		});
        		
 				
 			};
        	
        }
        //------------------------个人中心——修改密码---------------------------------------------
        if($stateParams.page=="password"){
        	
        	$scope.processForm = function() {
        		var newPassword = $("#newPassword").val();
        		var affirmPassword = $("#affirmPassword").val();
        		if(newPassword!=affirmPassword){
        			layer.msg("新密码不一致", {
            		    icon: 1,
            		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
            		}); 
            		layer.close();
        			return false;
        		}
        		
        		$.ajax({
     	 		     type: 'POST',
     	 		     url : HRY.modules.oauth+'user/appuser/modifypassword',
     	 		     data :{
     	 		    	id :  $stateParams.id,
     	 		    	oldPassword : md5.hbmd5($("#oldPassword").val()),
     	 		    	newPassword : md5.hbmd5(newPassword)
     	 		     },
     	 		     dataType: "json",
     	 		     success: function(data){
     	 		    	if (data.success) {
     						growl.addInfoMessage('修改成功')
     						window.location.href='#/oauth/user/appuser/info/'+$rootScope.user.id;
     					} else {
     						growl.addInfoMessage(data.msg)
     					}
     	 		     }
     	 		});
        		
 				
 			};
        	
        }
        
        
        //加载插件
		hryCore.initPlugins();
		//上传插件
		hryCore.uploadPicture();
    }
    //----------------------------

    
    return {controller:controller};
});