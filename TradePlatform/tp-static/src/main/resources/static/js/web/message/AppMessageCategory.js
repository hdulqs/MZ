/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	//初始化js 插件
    	
    	
    	var table =  $("#table2");
    	
		//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			 hryCore.CURD({
						url : HRY.modules.web+"message/appmessagecatrgory/see/"+$stateParams.id
					}).get(null,
						function(data) {
		             	$scope.model = data;
		             	//渲染权限树
		             	var zTree;
		            	var setting = {
		            		view: {
		            			dblClickExpand: false,
		            			showLine: true,
		            			selectedMulti: false
		            		},
		            		data: {
		            			simpleData: {
		            				enable:true,
		            				id:"id",
		            				idKey: "mkey",
		            				pIdKey: "pkey",
		            				rootPId: ""
		            			}
		            		},
		            		callback: {
		            				beforeClick:function(treeId, treeNode){
		            					//alert(treeId);
		            				}
		            		}
		            	};
		            	var zNodes =[
		            		
		            	];
		            	
			   			for(var i = 0 ; i < data.appResourceSet.length ; i++) {
			   				var item = {
			   							id:data.appResourceSet[i].id,
			   							mkey:data.appResourceSet[i].mkey,
			   							pkey:data.appResourceSet[i].pkey,
			   							name:data.appResourceSet[i].name,
			   							open : true
			   							//drop : false
			   						   }
			   				zNodes.push(item);
			   			}
			   			//初始化菜单树
						$.fn.zTree.init($("#tree"), setting, zNodes);
             	 
		    }, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}

    	
		//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"){
        	$scope.formData = {};
        	 
			$scope.processForm = function() {
				var s = $scope.formData.titleImage
				//附值权限id
					$scope.formData.categoryId = $("#companySet").val();
			//	$scope.formData.appResourceSet = $("#appResourceSet").val();
				$http({
					method : 'POST',
					url : HRY.modules.web+'message/appmessagecategory/add',
					data : $.param($scope.formData), 
				//	params:{'appResourceStr':ids},
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
								window.location.href='#/web/message/appmessagecategory/list/anon';
							} else {
								growl.addInfoMessage('添加失败')
							}
	
				});
	
			};
        }
        
      //------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"){
        
        	$http.get(HRY.modules.web+"/message/appmessagecategory/see/"+$stateParams.id).
                 success(function(data) {
                 	 $scope.model = data;
                 	 $scope.formData = data;
                 });
        	
        	//表单提交
			$scope.processForm = function() {
		    		$scope.formData.nextAppArticle = ""
					$scope.formData.upAppArticle = ""
					$scope.formData.categoryId = $("#companySet").val();
					$http({
						method : 'POST',                 
						url : HRY.modules.web+'/message/appmessagecategory/modify',
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
									window.location.href='#/web/message/appmessagecategory/list/anon';
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
        		config.ajax.url = HRY.modules.web+'/message/appmessagecategory/list';
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
										"data" : "name"
									}, {
										"data" : "describes"
									}, {
										"data" : "sort"
									}, {
										"data" : "state"
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
										},
										{
											"targets" : 4,
												
											"render" : function(data, type, row) {
														if(data == 0){
															return "无效"
														}if(data == 1){
															return "有效"
														}else{
															return "错误";
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
            	}else{
            	
            	layer.confirm("确定删除这些数据？",{
	    				btn: ['确定','取消'], //按钮
	    				ids: ids
	    			}, function(){
	    			
	    			layer.closeAll();
	    			
	    			hryCore.CURD({
						url:HRY.modules.web+url+"/"+ids
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
	    			})
            	}
			}
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});