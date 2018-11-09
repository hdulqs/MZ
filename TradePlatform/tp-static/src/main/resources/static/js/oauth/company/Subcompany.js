/**
 * AppUser.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "角色管理";
    	//初始化js 插件
    	var table =  $("#dataTable");
    	
        //--------------------------添加基本信息页面------------------------------------
        if($stateParams.page=="addbase"){
        	$scope.model = {};
        	$scope.model.logoPath = "static/lib/_con/images/logo.png";
        	
        	$scope.formData = {};
			$scope.processForm = function() {
				
				//获取图片路径值
				$scope.formData.logoPath = $("#hidImgName").val();
				
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'/company/subcompany/addbase',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
						 
						if (data.success) {
							window.location.href='#/oauth/company/subcompany/addadmin/anon';
						} else {
							growl.addInfoMessage('添加失败')
						}
				});
	
			};
        }
        
        //-----------------------------------添加管理员页面-------------------------------
        if($stateParams.page=="addadmin"){
        	$scope.formData = {};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'/company/subcompany/addadmin',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
						if (data.success) {
							window.location.href='#/oauth/company/subcompany/addpermissions/anon';
						} else {
							growl.addInfoMessage(data.msg)
						}
				});
	
			};
        }
        
        
        //------------------------------------------添加权限页面------------------------------
        if($stateParams.page=="addpermissions"){
        	
        	$scope.formData = {};
			$scope.fnSave = function() {
				var menuTreeIds = "";
				$rootScope.applicactions.forEach(function(data) {
					var ztree = $.fn.zTree.getZTreeObj(data.mkey + "Tree");
					var checkNodes = ztree.getCheckedNodes();
					if (checkNodes != undefined && checkNodes.length > 0) {
						if (menuTreeIds.length > 0) {
							menuTreeIds += ","
						}
						for (var i = 0; i < checkNodes.length; i++) {
							menuTreeIds += checkNodes[i].id
							if (i != checkNodes.length - 1) {
								menuTreeIds += ","
							}
						}
					}
				})
				$scope.formData.permissions = menuTreeIds;
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'/company/subcompany/addpermissions',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
						if (data.success) {
							growl.addInfoMessage('添加成功')
							window.location.href='#/oauth/company/subcompany/list/anon';
						} else {
							growl.addInfoMessage(data.msg)
						}
				});
	
			};
			
			// -----------加载树------------
			$http.get(HRY.modules.web + 'menu/appmenutree/loadapp').success(function(data) {
				$rootScope.applicactions = data;
				// 循化创建树
				$rootScope.applicactions.forEach(function(data) {
					loadTree(data);
				})
			});

			// 创建树的方法
			function loadTree(rootMenu) {
				var zTree;
				var setting = {
					view : {
						dblClickExpand : false,
						showLine : true,
						selectedMulti : false
					},
					check : {
						autoCheckTrigger : false,
						chkboxType : {
							"Y" : "ps",
							"N" : "ps"
						},
						chkStyle : "checkbox",
						enable : true,
						nocheckInherit : false,
						chkDisabledInherit : false,
						radioType : "level"

					},
					data : {
						simpleData : {
							enable : true,
							id : "id",
							idKey : "mkey",
							pIdKey : "pkey",
							rootPId : ""
						}
					},
					callback : {
					}
				};
				var zNodes = [];

				$.post(HRY.modules.web + "menu/appmenutree/findByApp", {
					id : rootMenu.id
				}, function(data) {
					for (var i = 0; i < data.length; i++) {
						var item = {
							id : data[i].id,
							mkey : data[i].mkey,
							pkey : data[i].pkey,
							name : data[i].name,
							type : data[i].type,
							appName :data[i].appName,
							open : true
						}
						zNodes.push(item);
					}
					// 初始化菜单树
					zTree = $.fn.zTree.init($("#" + rootMenu.mkey + "Tree"), setting, zNodes);
					$('ul.tabs').tabs();
				}, "json");
			}
			
			
			
        }
    	
    	
		
		//查看权限信息
		if ($stateParams.page == "see") {
			 
			$http.get(HRY.modules.oauth+"company/subcompany/see/"+$stateParams.id).
            success(function(data) {
            	$scope.formData = data;
            	//权限数据
            	var myData = data.appResourceSet;
            	
            	var zTree;

            	var setting = {
            		view: {
            			dblClickExpand: false,
            			showLine: true,
            			selectedMulti: false,
            		},
            		check : {
            			autoCheckTrigger:false,
            			chkboxType :{"Y":"ps","N":"ps"},
            			chkStyle : "checkbox",
            			enable : true,
            			nocheckInherit : false,
            			chkDisabledInherit : false,
            			radioType : "level"
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
            	
        		$.ajax({
	        		   type: "POST",
	        		   url: HRY.modules.oauth+"user/appresource/findToJsonOnRolesAdd.do",
	        		   myData : myData,//异步回调时鉴别下拉框
	        		   success: function(data){
	        			    
	        			    var data = $.parseJSON(data);
	        			    
	        	 			for(var i = 0 ; i < data.length ; i++) {
	        	 				var item 
	        	 				var flag = false;
	        	 				for(var j = 0 ; j < this.myData.length ; j++ ){
	        	 					if(data[i].id=data[j].id){
		        	 					flag = true;
		        	 					break;
	        	 					 }
	        	 				 }
	        	 				
	        	 				if(flag){
	        	 					item = {
	        	   							id:data[i].id,
	        	   							mkey:data[i].mkey,
	        	   							pkey:data[i].pkey,
	        	   							name:data[i].name,
	        	   							checked : true,
	        	   							chkDisabled : true,
	        	   							open : true
	        	   						   }
	        	 				}else{
	        	 					item = {
	        	   							id:data[i].id,
	        	   							mkey:data[i].mkey,
	        	   							pkey:data[i].pkey,
	        	   							name:data[i].name,
	        	   							chkDisabled : true,
	        	   							open : true
	        	   							//drop : false
	        	   						   }
	        	 				}
	        	   				zNodes.push(item);
	        	   			}
	        	   			//初始化菜单树
	        				$.fn.zTree.init($("#tree"), setting, zNodes);
	        		   }
	        		});
            	
            	
            	
            	
            	
            });
			 
		}

    	
		//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"){
        	$scope.formData = {};
			$scope.processForm = function() {
				 
				//附值权限id
				$scope.formData.appResourceSet = $("#appResourceSet").val();
				
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'user/approle/add',
					data : $.param($scope.formData), 
				//	params:{'appResourceStr':ids},
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
								window.location.href='#/oauth/user/approle/list/anon';
							} else {
								growl.addInfoMessage('添加失败')
							}
	
				});
	
			};
        }
        
      //------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"){
        	$http.get(HRY.modules.oauth+"user/approle/see/"+$stateParams.id).
                 success(function(data) {
                 	 $scope.model = data;
                 	 $scope.formData = data;
            });
        	
			$scope.processForm = function() {
				//附值权限id
				$scope.formData.appResourceSet = $("#appResourceSet").val();
				//删除属性--不传给controller
				$($scope.formData).removeProp("appResourceList");
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'user/approle/modify',
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
								window.location.href='#/oauth/user/approle/list/anon';
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
        		config.ajax.url = HRY.modules.oauth+'company/subcompany/list';
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
										"data" : "companyNo"
									}, {
										"data" : "shortName"
									}, {
										"data" : "setDate"
									}, {
										"data" : "person"
									}, {
										"data" : "mobile"
									}, {
										"data" : "address"
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
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/oauth/'+url+'/anon';
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
            		window.location.href='#/oauth/'+url+'/'+ids[0];
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
            		window.location.href='#/oauth/'+url+'/'+ids[0];
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
            	
            	var id = ""	;
            	for(var i = 0 ; i < ids.length ; i++){
            		id += ids[i];
            		if(i!=ids.length-1){
            			id += ",";
            		}
            	}
            	 
            	hryCore
				.CURD({
						url:HRY.modules.oauth+url+"/"+id
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
        
        //加载插件
		hryCore.initPlugins();
		//上传插件
		hryCore.uploadPicture();
    	
    	
    }
    return {controller:controller};
});