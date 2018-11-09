/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    	var table =  $("#table2");
    	$scope.classNum = $stateParams.id
    	
 	
//=================================== see 页面  ================================================================
    	
    	
    	if($stateParams.page=="see"){
    		  
    		$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        	
        	// 这个方法是 给每行数据加一个查看事件  查看用户的图片
        	config.onlyClick = function(obj){
        		
        		$http.get(HRY.modules.customer+"agents/appAgentscustromer/getById?id="+obj.attr("name")).
	            	success(function(data2) {
	            		if(data2 != ''){
	            			$scope.appAgentsCustromer=data2
	            			openWindow();
	            		}
	            	});
        	    };
        	    
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.onlyEvent = false; //取消单行事件  
        		config.ajax.url = HRY.modules.customer+'agents/appAgentscustromer/see.do';
        		config.ajax.data = function(d){
        					//设置select下拉框
        					//	DT.selectData($scope.serchData);
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "address"
									}, {
										"data" : "customerName"
									}, {
										"data" : "agentName"
									},  /*{
										"data" : "eagerRelationName"
									},*/{
										"data" : "recommendCode"
									},{           
										"data" : "states"
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
										},{
											"targets" : 6,
												
											"render" : function(data, type, row) {
												if(data == 1){
													return "<font color='red'>待处理...</font>"
												}else{
													return data
												}
											}
										}
										
//										,{
//											"targets" : 8,
//												
//											"render" : function(data, type, row) {
//												return "<input type='button' name='"+data+"' id='seeImage' value='查看'></input>"
//											}
//										}
								
										]
        	DT.draw(table,config);
 
    
 // -------------------- 其他方法   ---------------------------------------------------------------------------
        	$scope.fnList=fnList; //刷新方法
        	$scope.fnCheck=fnCheck; //审核方法     
        	$scope.fnStorpSee=fnStorpSee; // 审核失败 
 
	        function fnCheck(){
	        	
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		// Materialize.toast('', 4000)
            		layer.msg('请选择数据', {
			    		    icon: 1,
			    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
			    		});	
            		return false;
            	}else{
            			layer.confirm('你确定要通过这条代理商申请吗？', {
    	    			btn: ['确定','取消'], //按钮
    	    			ids: ids
    				}, function(){
            	    	//关闭提示框
            	    	layer.closeAll();
            		
	            		$http.get(HRY.modules.customer+"agents/appAgentscustromer/paseUser?ids="+ids).
		            	success(function(data2) {
		            		if(data2.success){
		            			layer.msg("通过成功" , {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
		            			fnList();
		            		}
		            	});
	            	});
            	}
            }
	        
	    	  function fnStorpSee (){
		        	 
		        	var ids = DT.getSelect(table);
		        	if(ids.length==0){
		        		// Materialize.toast('', 4000)
		        		layer.msg('请选择数据', {
			    		    icon: 1,
			    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
			    		});
		        		return false;
		        	}else{
		        		
		        		layer.confirm('你确定要驳回这条代理商申请吗？', {
    	    			btn: ['确定','取消'], //按钮
    	    			ids: ids
    				}, function(){
            	    	//关闭提示框
            	    	layer.closeAll();
		        		$http.get(HRY.modules.customer+"agents/appAgentscustromer/storpUser?ids="+ids).
		        		success(function(data2) {
		        				if(data2.success){
		        				
		        					layer.msg("驳回成功", {
						    		    icon: 1,
						    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
						    		});
		        					
		        					fnList();
		        				}
		        			});
		        		});
		        	}
		        }
	 		
	  	   //刷新按钮
	       function fnList (){
	         	 table.DataTable().draw();
	         }
	       
    
	        // 弹出 查看图片窗口      
            function openWindow(){
        		$("#lockCustomer").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "锁定用户",
							closeBtn : 2,
							area : [ '900px', '500px' ],
							shadeClose : true,
							content : $('#lockCustomer')
						});
        	}
      // 关闭产看图片窗口      
            $scope.submitTmie = function (){
                		layer.closeAll();
            	}		
    		  
    	  }
    	
//====================== see页面完 ================================================================================    	
	

   		
 //--------------------------look页面---------------------------------
    	    
	if ($stateParams.page == "look") {
		hryCore.CURD({
			url : HRY.modules.customer + "agents/appAgentscustromer/look/" + $stateParams.id
		}).get(null, function(data) {
			$scope.model = data;

		}, function(data) {
			growl.addInfoMessage("error:" + data.msg, '1000');
		});		

    }  
    	  
//====================== lookall 页面开始  ================================================================================   	  
       
    if(($stateParams.page).indexOf("look") != -1){
 // -------------- dataTable 方式 --------------------------------   
  
      function findAgent2(username,num){
      	
      		$http.get(HRY.modules.customer+"agents/appAgentscustromer/findPersonInfo?userName="+"15917060107"+"&num="+1).
		        		success(function(data2) {
		        			if(data2.success){
	        					layer.msg(data2.msg, {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
		        				fnList();
		        			}
		        		});
      }
    	
 
      		    $scope.serchData={"num":$stateParams.id};
      		    var user = "";
      		 	var config = DT.config();
      		 	config.onlyEvent = false;
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.customer+"agents/appAgentscustromer/findPersonInfo.do";
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
        								"data" : "id"
        							}, {
										"data" : "appPersonInfo.mobilePhone"
									}, {
										"data" : "appPersonInfo.customerType"
									}, {
										"data" : "appPersonInfo.cardType"
									},  {
										"data" : "appPersonInfo.cardId"
									},{
										"data" : "appPersonInfo.sex"
									}, {
										"data" : "appPersonInfo.created"
									}
        		                  ]
    	        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 1,"orderable" :false,
											"render" : function(data, type, row) {
												return user;
											}
										},
										{
											"targets" : 3,
												
											"render" : function(data, type, row) {
												if(data==1){
													return "普通用户"
												}
												if(data==2){
													return "乙类账户"
												}
												if(data==3){
													return "丙类账户"
												}else{
													return "未知类型"
												}
											}
										},
										{
											"targets" : 4,
												
											"render" : function(data, type, row) {
													return "身份证"
											}
										},
										{
											"targets" : 6,
											"render" : function(data, type, row) {
													if(data == 0){
														return "男"
													}if(data == 1){
														return "女"
													}else{
														return "未知性別"
													}
											}
										}
									 ]
									 DT.draw(table,config);
      
									 		// 按钮执行 datable
    	$scope.findAgent = function (username){
      		
      		user = $("#"+username).val();
      		$scope.serchData={"userName":user,"num":$stateParams.id};
			table.DataTable().draw();
      		
     	}  
    	
    	
    }   	
//====================== lookall 页面结束  ================================================================================   	  
    	  

//------------------------列表页面路径---------------------------------------------
     
    	if($stateParams.page=="list"){
        	
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        	    
        	config.onlyClick = function(b){
        	
        		$http.get(HRY.modules.customer+"agents/appAgentscustromer/getById?id="+b.attr("name")).
	            	success(function(data2) {
	            		if(data2 != ''){
	            			$scope.appAgentsCustromer=data2
	            			openWindow();
	            		}
	            	});
        	    };
        	    
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.onlyEvent = false; //取消单行事件  
        		config.ajax.url = HRY.modules.customer+'agents/appAgentscustromer/list.do';
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
        							}, /*{
										"data" : "address"
									},*/ {
										"data" : "customerName"
									}, {
										"data" : "surname"
									},{
										"data" : "agentName"
									}, /* {
										"data" : "eagerRelationName"
									},*/{
										"data" : "recommendCode"
									},/*{
										"data" : "states"
									}, */{
										"data" : "created"
									}/*,{
										"data" : "id"
									}*/
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},/*{
											"targets" : 6,
												
											"render" : function(data, type, row) {
												if(data == 2){ // #FF6600  
													return "<font color='green'>已处理</font>"
												}if(data == 3){
													return "<font color='#006699'>已删除</font>"
												}else{
													return data
												}
											}
										}
										, {
											"targets" : 5,
											"render" : function(data, type, row) {
												var s ="";
												if(row.isRealUsd=="1"){
													s = "【国际站已实名】"
												}
												if (data != null && data == "1") {
													return "申请中";
												}
												if (data != null && data == "2") {
													return "已认证"+s;
												}
												return "未认证"+s
												}
											}*/
										
//										,{
//											"targets" : 8,
//											"render" : function(data, type, row) {
//												return "<input type='button' name='"+data+"' id='seeImage' value='查看'></input>"
//											}
//										}
										/*,{
										"targets" : 7,
										
										"render" : function(data, type, row) {
//											var seePic=$rootScope.requiresPermissions('customer','/user/appcustomer/seePic');
//											if(seePic){
												return "<input type='button' name='"+data+"' id='seeImage' value='查看照片'></input>"
//											}else{
//												return "";
//											}
										}
									}*/
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
          //   $scope.fnStorp=fnStorp; // 审核失败 
            $scope.fnList=fnList;//刷新方法
           

     // 弹出 查看图片窗口      
            function openWindow(){
        		$("#lockCustomer").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "查询用户照片",
							closeBtn : 2,
							area : [ '900px', '500px' ],
							shadeClose : true,
							content : $('#lockCustomer')
						});
        	}
      // 关闭产看图片窗口      
            $scope.submitTmie = function (){
                		layer.closeAll();
            	}
            
            
//        	  function fnStorp (){
//  	        	 
//  	        	var ids = DT.getSelect(table);
//  	        	if(ids.length==0){
//  	        	
//    					layer.msg('请选择数据', {
//			    		    icon: 1,
//			    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
//			    		});
//  	        		
//  	        		
//  	        		return false;
//  	        	}else{
//  	        		$http.get(HRY.modules.customer+"agents/appAgentscustromer/storpUser?ids="+ids).
//  	        		success(function(data2) {
//  	        			if(data2.success){
//  	        				
//        					layer.msg(data2.msg, {
//				    		    icon: 1,
//				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
//				    		});
//  	        				fnList();
//  	        			}
//  	        		});
//  	        	}
//  	        }
   		
//    	---------------------------------------------
    	   //刷新按钮
         function fnList (){
           	 table.DataTable().draw();
           }
         
       
            
        }
    	
 // ======================================================================================================
    	
    	/**
		 * 审核认证
		 */
		$scope.audit = function() {
			var ids = DT.getSelect(table)
			if (ids.length == 0) {
				growl.addInfoMessage('请选择数据')
				return false;
			} else if (ids.length > 1) {
				growl.addInfoMessage('只能选择一条数据')
				return false;
			}
			layer.confirm('你确定要提交审核吗？', {
				btn : [ '确定', '取消' ],
				ids : ids
			}, function() {
				$http.get(HRY.modules.customer + "agents/appAgentscustromer/audit/" + ids[0]).success(function(data) {
					if (data.success) {
						growl.addInfoMessage("审核成功");
						fnList();
					} else {
						growl.addInfoMessage(data.msg);
					}
					layer.closeAll();
				});
			});
		}
  	
  //------------------------------------------------  	
		
		// 查看按钮
		$scope.fnSee = function(url) {
			var ids = DT.getSelect(table);
			if (ids.length == 0) {
				growl.addInfoMessage('请选择数据')
				return false;
			} else if (ids.length > 1) {
				growl.addInfoMessage('只能选择一条数据')
				return false;
			} else {
				window.location.href = '#/customer/' + url+ '/' +ids[0];
			}
		}
    	
   //------------------------     
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});