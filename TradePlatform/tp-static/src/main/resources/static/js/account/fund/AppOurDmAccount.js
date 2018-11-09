/**
 * student.js
 */
define(['app','hryTable' ,'hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "我方银行管理";
    	//初始化js 插件
    	
		//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			 $http.get(HRY.modules.account+"fund/appouraccount/see/"+$stateParams.id).
	            success(function(data) {
	            	$scope.model = data;
	             	//静态下拉框全部回显
	 	            hryCore.RenderAllSelect(data);
	 	            hryCore.RenderHTML(data);//异步回显控件
             });
		}
		
		//------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify" || $stateParams.page=="icomodify"){
        
        	$http.get(HRY.modules.account+"fund/appouraccount/see/"+$stateParams.id).
            success(function(data) {
            	 
            	$scope.formData = data;
             	//静态下拉框全部回显
            	hryCore.RenderSelect($("#openAccountType"),data.openAccountType);
            	hryCore.RenderSelect($("#isShow"),data.isShow);
            	if("cn"==data.website){
            		hryCore.RenderSelect($("#website"),"cn")
    			}if("en"==data.website){
    				hryCore.RenderSelect($("#website"),"en")
    			}
 	            hryCore.RenderAllSelect(data);
 	            hryCore.RenderHTML(data);//异步回显控件
 	            hryCore.initPlugins();
            });
        	
        	$scope.processForm = function() {
       		
        			layer.confirm("你确定修改吗？",{
           			btn: ['确定','取消'] //按钮
    	    		    // 	ids: ids
            		}, function(){
            			
            			layer.closeAll();
       		
				        $scope.formData.openAccountType = $("#openAccountType").val();
			        	$scope.formData.isShow = $("#isShow").val();
			        	var vb = $("#website").val();
			        	$scope.formData.website = vb;
			        	$scope.formData.accountType = "1";
				        		
		 				$http({
		 					method : 'POST',
		 					url : HRY.modules.account+'fund/appouraccount/modify',
		 					data : $.param($scope.formData), 
		 					headers : {
		 						'Content-Type' : 'application/x-www-form-urlencoded'
		 					}
		 				})
		 				.success(function(data) {
		 					if (data.success) {
		 						growl.addInfoMessage('修改成功')
		 						window.location.href='#/account/fund/appourdmaccount/list/anon';
		 					} else {
	 						growl.addInfoMessage(data.msg);
		 					}
		 				});
		 		   });
 			  };
       	 }
		

//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"||$stateParams.page=="icoadd"){
        	
        	$scope.formData = {};
			$scope.processForm = function() {
				var s = $("#coinCode").val();
		        $scope.formData.currencyType = s;
	        	$scope.formData.openAccountType = $("#openAccountType").val();
	        	$scope.formData.isShow = $("#isShow").val();
	        	var vb = $("#website").val();
	        	$scope.formData.website = vb;
	        	$scope.formData.accountType = "1";
				// 附值权限id
				$http({
					method : 'POST',
					url : HRY.modules.account+'fund/appouraccount/add',
					data : $.param($scope.formData), 
					//	params:{'appResourceStr':ids},
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
													        
								window.location.href='#/account/fund/appourdmaccount/list/anon';
							} else {
								growl.addInfoMessage(data.msg)
							}
				});
			};
        }

        
  //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	var table =  $("#table2");
	        	$scope.serchData={
	        		accountType_EQ:1
	        	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appouraccount/list';
        		config.ajax.data = function(d){
					        			//设置select下拉框
										DT.selectData($scope.serchData);
					        			$.each($scope.serchData, function(name,value){											if(""!=value){
												eval("d."+name+"='"+value+"'");
										}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, { 
										"data" : "currencyType"
									}, {
										"data" : "accountNumber"
									},{
										"data" : "openAccountType"
									},{
										"data" : "accountName"
									}/*,{
										"data" : "accountMoney"
									},{
										"data" : "retainsMoney"
									}*/,{
										"data" : "website"
									},{
										"data" : "isShow"
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
											"targets" : 3,
											"render" : function(data, type, row) {
												if(data=="0"){
													return "充币";
												}
												return "提币";
											}
										}/*,
										{
											"targets" : 5,
											"render" : function(data, type, row) {
												return "<font color='blue'>"+data+"</font>"
											}
										},{
											"targets" : 6,
											"render" : function(data, type, row) {
											
												if(row.openAccountType==0){
													return "—— ——"
												}if(row.openAccountType==1){
													return data
												}
												
											}
										}*/,{
											"targets" : 5,
											"render" : function(data, type, row) {
												if("cn" == data){
													return "中国站"
												}if("en" == data){
													return "国际站"
												}
											}
										},
										{
											"targets" : 6,
											"render" : function(data, type, row) {
												if(data=="1"){
													return "<font color='red'>是</front>";
											}
												return "否";
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
             $scope.fnTransfer=fnTransfer;//钱包转币到我方账户
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
        	//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"我方币种账户管理");
			}
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/account/'+url+'/anon';
            }
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url){
            	var	selectData = DT.getRowData(table);
            	if(selectData.length==0){
            		growl.addInfoMessage('请选择数据')
           		return false;
            	}else if(selectData.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		
            		if(selectData[0].accountType=="0"){
            			window.location.href='#/account/fund/appouraccount/see/'+selectData[0].id;
            		}else{
           				window.location.href='#/account/fund/appouraccount/seezfb/'+selectData[0].id;
            		}
           	}
         }
            
              	
           	   function fnTransfer(url,e){

               	$(e.currentTarget).attr("disabled","disabled");
               	$(e.currentTarget).find("span").html("请耐心等待");
            
            	$http({
					method : 'GET',
					url : HRY.modules.exchange+'transaction/exdmtransaction/transfer',
				
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
													        
								window.location.href='#/account/fund/appourdmaccount/list/anon';
							} else {
								growl.addInfoMessage('添加失败')
							}
							
							 $(e.currentTarget).removeAttr("disabled");
                        	 $(e.currentTarget).find("span").html("转入记录刷新");
				});
            
       }
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            
            	var	selectData = DT.getRowData(table);
            	if(selectData.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(selectData.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		window.location.href='#/account/fund/appourdmaccount/modify/'+selectData[0].id;
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
           
            	layer.confirm("你确定删除？",{
            			btn: ['确定','取消'], //按钮
    	    			ids: ids
            		}, function(){
            			
            			layer.closeAll();
            	
            	hryCore.CURD({
						url:HRY.modules.account+url+"/"+ids
				 }).remove(null,function(data) {
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
           			
            	});
			}
			
		}
        
        
        if($stateParams.page=="icolist"){
        	var table =  $("#table2");
	        	$scope.serchData={
	        		accountType_EQ:1
	        	};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appouraccount/list';
        		config.ajax.data = function(d){
					        			//设置select下拉框
										DT.selectData($scope.serchData);
					        			$.each($scope.serchData, function(name,value){											if(""!=value){
												eval("d."+name+"='"+value+"'");
										}
										});
						           }
        		//ICO数据
        		$scope.serchData.ico="1";
        		config.columns = [	{
        								"data" : "id"
        							}, { 
										"data" : "currencyType"
									}, {
										"data" : "accountNumber"
									},{
										"data" : "openAccountType"
									},{
										"data" : "accountName"
									},{
										"data" : "website"
									},{
										"data" : "isShow"
									}
        		                  ]
					        		config.columnDefs = [{
																"targets" : 0,
																"orderable" : false,
								
																"render" : function(data, type, row) {
																	return "<input type=\"checkbox\" id=\"checkbox"
																			+ data
																			+ "\" /><label for=\"checkbox"
																			+ data + "\"></label>"
																}
														}, {
															"targets" : 3,
															"render" : function(data, type, row) {
																if (data == "0") {
																	return "充币";
																}
																return "提币";
															}
														}, {
															"targets" : 5,
															"render" : function(data, type, row) {
																if ("cn" == data) {
																	return "中国站"
																}
																if ("en" == data) {
																	return "国际站"
																}
															}
														}, {
															"targets" : 6,
															"render" : function(data, type, row) {
																if (data == "1") {
																	return "<font color='red'>是</front>";
																}
																return "否";
															}
														}]
        	DT.draw(table,config);
    		// --------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnList=fnList;//刷新方法
             $scope.fnTransfer=fnTransfer;//钱包转币到我方账户
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
        	//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"我方币种账户管理");
			}
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/account/fund/appourdmaccount/'+url+'/anon';
            }
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url){
            	var	selectData = DT.getRowData(table);
            	if(selectData.length==0){
            		growl.addInfoMessage('请选择数据')
           		return false;
            	}else if(selectData.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		
            		if(selectData[0].accountType=="0"){
            			window.location.href='#/account/fund/appouraccount/see/'+selectData[0].id;
            		}else{
           				window.location.href='#/account/fund/appouraccount/seezfb/'+selectData[0].id;
            		}
           	}
         }
            
              	
           	   function fnTransfer(url,e){

               	$(e.currentTarget).attr("disabled","disabled");
               	$(e.currentTarget).find("span").html("请耐心等待");
            
            	$http({
					method : 'GET',
					url : HRY.modules.exchange+'transaction/exdmtransaction/transfer',
				
					headers : {	
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							if (data.success) {
								growl.addInfoMessage('添加成功')
													        
								window.location.href='#/account/fund/appourdmaccount/list/anon';
							} else {
								growl.addInfoMessage('添加失败')
							}
							
							 $(e.currentTarget).removeAttr("disabled");
                        	 $(e.currentTarget).find("span").html("转入记录刷新");
				});
            
       }
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            
            	var	selectData = DT.getRowData(table);
            	if(selectData.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(selectData.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		window.location.href='#/account/fund/appourdmaccount/'+url+'/'+selectData[0].id;
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
           
            	layer.confirm("你确定删除？",{
            			btn: ['确定','取消'], //按钮
    	    			ids: ids
            		}, function(){
            			
            			layer.closeAll();
            	
            	hryCore.CURD({
						url:HRY.modules.account+url+"/"+ids
				 }).remove(null,function(data) {
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
           			
            	});
			}
			
		}
        
        hryCore.initPlugins();
        //上传插件
		hryCore.uploadPicture();
        
    }
    return {controller:controller};
});