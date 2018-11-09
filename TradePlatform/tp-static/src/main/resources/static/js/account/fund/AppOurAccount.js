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
		if ($stateParams.page == "see"||$stateParams.page == "seezfb") {
			 $http.get(HRY.modules.account+"fund/appouraccount/see/"+$stateParams.id).
	            success(function(data) {
	            	$scope.model = data;
	             	//静态下拉框全部回显
	            	if("cn"==data.website){
        				data.currencyType = "0"
        			}if("en"==data.website){
        				data.currencyType = "1"
        			}
	            	hryCore.RenderSelect($("#openAccountType"),data.openAccountType);
	            	hryCore.RenderSelect($("#isShow"),data.isShow);
	            	hryCore.RenderAllSelect(data);
	 	            hryCore.RenderHTML(data);//异步回显控件
	 	            hryCore.initPlugins();
             });
		}
		
		//------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"||$stateParams.page=="modifyzfb"){
        
        	$http.get(HRY.modules.account+"fund/appouraccount/see/"+$stateParams.id).
            success(function(data) {
            	 
            	$scope.formData = data;
             	//静态下拉框全部回显
            	if("cn"==data.website){
            		hryCore.RenderSelect($("#currencyType"),"0")
    			}if("en"==data.website){
    				hryCore.RenderSelect($("#currencyType"),"1")
    			}
    			hryCore.RenderSelect($("#openAccountType"),data.openAccountType);
            	hryCore.RenderSelect($("#isShow"),data.isShow);
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
            			var type = $("#currencyType").val();
            			if("0"==type){
            				$scope.formData.website="cn";
            				$scope.formData.currencyType = "cny"
            			}if("1"==type){
            				$scope.formData.website="en";
            				$scope.formData.currencyType = "usd";
            			}
		        	//	$scope.formData.currencyType = $("#currencyType").val();
			        	$scope.formData.bankName = $("#bankName").val();
			        	$scope.formData.openAccountType = $("#openAccountType").val();
			        	$scope.formData.isShow = $("#isShow").val();
			        	$scope.formData.bankLogo = $("#imgSrc").val();
			        	if($stateParams.page=="modifyzfb"){
			        		$scope.formData.accountType = "2";
			        	}
		        		
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
		 						window.location.href='#/account/fund/appouraccount/list/anon';
		 					} else {
		 						growl.addInfoMessage(data.msg);
		 					}
		 				});
		 		   });
 			  };
       	 }
		
	//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"||$stateParams.page=="addzfb"){
        
        	$scope.formData = {};
			$scope.processForm = function() {
			
			var type = $("#currencyType").val();
        			if("0"==type){
        				$scope.formData.website="cn";
        				$scope.formData.currencyType = "cny"
        			}if("1"==type){
        				$scope.formData.website="en";
        				$scope.formData.currencyType = "usd";
        			}
				
			//	$scope.formData.currencyType = $("#currencyType").val();
	        	$scope.formData.bankName = $("#bankName").val();
	        	$scope.formData.openAccountType = $("#openAccountType").val();
	        	$scope.formData.isShow = $("#isShow").val();
	        	//  $scope.formData.bankLogo = $("#imgSrc").val();
	        	if($("#accountType").val()!=undefined||null!=$("#accountType").val()){
	        	    $scope.formData.accountType=$("#accountType").val();
	        	}else{
	        	   $scope.formData.accountType=0;
	        	}
	        	
				
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
								window.location.href='#/account/fund/appouraccount/list/anon';
							} else {
								growl.addInfoMessage(data.msg)
							}
				});
			};
        }

        
  //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	var table =  $("#table2");
        	$scope.serchData="";
		    $scope.serchData={
			        	accountType:2
			     };
        	
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        	
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appouraccount/list';
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
										"data" : "accountType"
									}, {
										"data" : "accountNumber"
									},{
										"data" : "openAccountType"
									},{
										"data" : "accountName"
									},{
										"data" : "accountMoney"
									}/*,{
										"data" : "accountFee"
									},{
										"data" : "hasOutFee"
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
											"targets" : 1,
												
											"render" : function(data, type, row) {
												if(data=="0"){
													return "银行账户";
												}
												return "支付宝账户";
											}
										},
										{
											"targets" : 3,
												
											"render" : function(data, type, row) {
												if(data=="0"){
													return "企业";
												}
												return "个人";
											}
										},{
											"targets" : 6,
												
											"render" : function(data, type, row) {
												if("cn" == data){
													return "中国站";
												}
												if("en" == data){
													return "国际站";
												}
											}
										},
										{
											"targets" : 7,
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
            $scope.fnFeeWithdrawals=fnFeeWithdrawals;//刷新方法
            $scope.modifySubmit = modifySubmit;//修改提交按钮方法
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
          //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"我方账户管理");
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
            		
	            		if(selectData[0].accountType=="0"){
	            			window.location.href='#/account/fund/appouraccount/modify/'+selectData[0].id;
	            		}else{
	            			window.location.href='#/account/fund/appouraccount/modifyzfb/'+selectData[0].id;
	            		}
            
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
            	
            	layer.confirm("你确定删除？",{
            			btn: ['确定','取消'], //按钮
    	    			ids: ids
            		}, function(){
            			
            			layer.closeAll();
            	
            	hryCore.CURD({
						url:HRY.modules.account+url+"/"+id
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
            
            
            //手续费账户提现
            function fnFeeWithdrawals(){
            	var ids = DT.getSelect(table);
            	var selectData = DT.getRowData(table)[0];
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(selectData.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		//判断是否是银行账户
            		if(selectData.isShow){
            			//判断余额
            			var money=selectData.accountFee;
            			if(money>0){
        					$('#modifyInfo').removeClass("hide");
        					$scope.accountFee=selectData.accountFee;
        					$("#accountFee").val(selectData.accountFee);
        					$("#id").val(selectData.id);
        					layer.open({
        						type : 1,
        						title : "手续费账户提现",
        						closeBtn : 2,
        						area : [ '450px', '400px' ],
        						shadeClose : true,
        						content : $('#modifyInfo')
        					});
            			}else{
            				growl.addInfoMessage('余额不足不能提现！')
                    		return false;
            			}
            		}else{
            			growl.addInfoMessage('只能选择主账户进行手续费提现！')
                		return false;
            		}
            	}
            }
            
            //手续费提现提交按钮
			function modifySubmit(){
				debugger;
				//检验金額
				var id = $("#id").val();//appaccount的id
				var money = $("#money").val();//派发金额
				if(money==undefined ||money == ""){
					layer.msg("提现金额不能为空！", {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
				     return false;		
				}
				
				if (!(/^(-?\d*)\.?\d{1,4}$/.test(money))){
					$("#money").val("")
					layer.msg("只能输入数字(最多四位小数)!", {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
					     return false;
				}
				
				//校验金额大小
				if (money>$scope.accountFee){
					$("#money").val("")
					layer.msg("提现金额大于可提现金额！", {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
				     return false;
				}else{
					layer.confirm("确定提现？", {
					btn : [ '确定', '取消' ], // 按钮
						id : id
					}, function() {
						$http.get(HRY.modules.customer+"/fund/appouraccount/feeWithdrawals?id="+id+"&money="+money).
		                 success(function(data) {
		                	layer.msg(data.msg, {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
		                	layer.closeAll();
		                	fnList();
		                 });
					});
				}
			}
        }
        
        hryCore.initPlugins();
        //上传插件
		hryCore.uploadPicture();
        
    }
    return {controller:controller};
});