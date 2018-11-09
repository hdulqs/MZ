/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	//初始化js 插件
    	var table =  $("#table2");
    	var website = $stateParams.id;
    	$scope.website = $stateParams.id;
    	$scope.website2 = $stateParams.state;
    	
    	$scope.formData = {};
		//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			 hryCore.CURD({
						url : HRY.modules.web+"article/apparticle/see/"+$stateParams.id
					}).get(null,
				function(data) {
             		$scope.formData = data;
             		hryCore.RenderHTML(data);
			    }, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});
			}

    	
		//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"){
        	 
			$scope.processForm = function() {
				 
				var ss = $scope.formData.content;
				var kk = $("[name=radios2]:checked").val();
				
				$scope.formData.website=website;
				$scope.formData.categoryId = $("#companySet").val();
				$scope.formData.titleImage = $("#titleImage").val();
				if( kk ==0 && ss == ""){
					growl.addInfoMessage('文章内容不能为空');
					return;
				}
				if( kk ==1){
					if( $("#outLink").val() == ""){
						growl.addInfoMessage('请填写外链地址');
						return ;
					}else{
						$scope.formData.content = "";
					}
				}
				
				
				
				if($scope.formData.categoryId===undefined||$scope.formData.categoryId==""){
					growl.addInfoMessage('请选择文章类型');
					return false;
				}
				
				if($scope.formData.writer===undefined||$scope.formData.writer==""){
					growl.addInfoMessage('请填写文章作者');
					return false;
				}
				$http({
 					method : 'POST',
 					url : HRY.modules.web+'article/apparticle/add',
 					data : $.param($scope.formData), 
 					headers : {
 						'Content-Type' : 'application/x-www-form-urlencoded'
 					}
 				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
								window.location.href='#/web/article/apparticle/list/'+website+"/anon";
							} else {
								growl.addInfoMessage('添加失败')
							}
				});
			};
        }
        
      //------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"){
        	
        	$http.get(HRY.modules.web+"/article/apparticle/see/"+$stateParams.id).
                 success(function(data) {
                 	
                 	 $scope.model = data;
                 	 $scope.formData = data;
                 	 $("#categoryName").val(data.categoryId)
                 	 hryCore.initPlugins();
            });
            
      //  	$scope.formData = {};
			$scope.processForm = function() {
				
				$scope.formData.categoryId = $("#categoryName").val();
				$scope.formData.categoryName = $("#categoryName option:selected").text();
				$scope.formData.titleImage = $("#titleImage").val();//获得图片路径
				$http({
					method : 'POST',
					url : HRY.modules.web+'/article/apparticle/modify',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							console.log(data);
							if (!data.success) {
								$scope.errorName = data.msg;
							} else {
								$scope.message = data.msg;
								window.location.href='#/web/article/apparticle/list/'+$scope.formData.website+"/anon";
							}
				});
			};
        }
        
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.serchData={
        		"website_EQ":website
        	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.web+'/article/apparticle/list';
        		config.ajax.data = function(d){
        		
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "title"
									}, {
										"data" : "categoryName"
									}, {
										"data" : "sort"
									}, {
										"data" : "isStick"
									}, {
										"data" : "isOutLink"
									}, {
										"data" : "writer"
									}, {
										"data" : "hits"
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
										}/*,
										{
											"targets" : 8,
												
											"render" : function(data, type, row) {
												if(data == 0){
													return "有效"
												}if(data == 1){
													return "无效"
												}if(data == 2){
													return "已删除"
												}
												return data;
											}
										}*/,
										{
											"targets" : 4,
												
											"render" : function(data, type, row) {
												if(data == 0){
													return "不置顶"
												}if(data == 1){
													return "置顶";
												}
												return "错误"
											}
										},
										{
											"targets" : 5,
												
											"render" : function(data, type, row) {
												if(data == 0){
													return "不外链"
												}if(data == 1){
													return "外链";
												}
												return "错误"
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
            function fnAdd(url){
            	window.location.href='#/web/'+url+"/"+website+"/anon";
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
            		window.location.href='#/web/'+url+'/'+ids[0]+"/"+website;
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
            		window.location.href='#/web/'+url+'/'+ids[0]+"/"+website;
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
            	
            	layer.confirm("确定删除该条数据？",{
    				btn: ['确定','取消'], //按钮
    				ids: ids
    			}, function(){
    				layer.closeAll();
	            	$http({
	 					method : 'POST',
	 					url:HRY.modules.web+url+"?ids[]="+ids,
	 					data : $.param($scope.formData), 
	 					headers : {
	 						'Content-Type' : 'application/x-www-form-urlencoded'
	 					}
	 				})
					.success(function(data) {
								if (data.success) {
									if(data.success){
				                		//提示信息
				                		growl.addInfoMessage('删除成功')
				                		//重新加载list
				                		fnList();
				                	}else{
				                		growl.addInfoMessage(data.msg)
				                	}
								} else {
									growl.addInfoMessage(data.msg);
								}
					});
    			});
            	/*hryCore
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
						});*/
			}
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});