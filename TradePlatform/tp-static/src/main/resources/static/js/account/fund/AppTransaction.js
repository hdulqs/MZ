/**
 * student.js
 */
define([ 'app', 'hryTable', 'pikadayJq','bootstrap-datetimepicker' ,'hryUtil'], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$location' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore, $location) {
		$rootScope.headTitle = $rootScope.title = "客户账号管理";
		// 初始化js 插件
		
		$('.datetimepicker').datetimepicker({
   		 format: 'yyyy-MM-dd hh:mm:ss',
   		 collapse: false
          });
		
		var table = $("#table2");

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			hryCore.CURD({
				url : HRY.modules.customer + "user/appcustomer/see/" + $stateParams.id
			}).get(null, function(data) {
				$scope.model = data;

			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}

		// ------------------------提现申请记录--------------------------------------------
		if ($stateParams.page == "wapplylist") {
			$scope.serchData = {
				status_EQ : $stateParams.id,
				transactionType_in : "4,2"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			}, {
				"data" : "surname" //姓氏
			},  {
				"data" : "trueName" //名字
			},/*{
				"data" : "currencyType"
			},*/{
				"data" : "bankName"
			},  {
				"data" : "custromerAccountNumber"
			},{
				"data" : "created"
			}, {
				"data" : "bankProvince"
			},{
				"data" : "bankAddress"
			},{
				"data" : "subBank"
			},/*{
				"data" : "subBankNum"
			},*/{
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : ""
			},{
				"data" : "transactionNum"
			}, {
				"data" : "modified"
			}]
			
			
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				} 
			} ,{
				"targets" : 12,
				"renderRule" : "VALUE_OPERATION",//值的计算
				"renderValueRule" : "transactionMoney-fee",//值替换规则
				"render" : function(data, type, row) {
					//提现汇率
					var dollarRateWithdraw=1;
					var money="";
					if(row.currencyType=="usd"){
						dollarRateWithdraw=$rootScope.financeConfig.dollarRateWithdraw;
						money=(row.transactionMoney-row.fee) + "("+ ((row.transactionMoney-row.fee)*dollarRateWithdraw).toFixed(2) +"RMB)";
					}else{
						money=(row.transactionMoney-row.fee);
					}
					return money;
				}
			}
			]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnList = fnList;// 刷新方法


			// 通过取现方法
			$scope.fnConfirmWithdraw = function(){

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$scope.confirm = DT.getRowData(table)[0];
					$scope.confirm.actualMoney = $scope.confirm.transactionMoney - $scope.confirm.fee

					$('#fnConfirmWithdrawDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "提现确认",
						closeBtn : 2,
						area : [ '400px', '340px' ],
						shadeClose : true,
						content : $('#fnConfirmWithdrawDiv')
					});

					//取现确认提交
					$scope.fnConfirmWithdrawSubmit = function() {
						var id = $scope.confirm.id;
						$http({
							method : 'POST',
							url : HRY.modules.account + 'fund/apptransaction/checkWithdraw/'+id,
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (!data.success) {
								layer.confirm('该用户在余额中有错误数据，是否确认取现？', {
									btn : [ '确定', '取消' ],
								}, function() {
									drowExecut(id);
								});
							} else {
								drowExecut(id);
							}
						});
					}
				}
			}
			
			
			// 无效处理
			$scope.fnInvalidWithdraw = function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					$scope.invalid = DT.getRowData(table)[0];

					$('#fnInvalidWithdrawDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "驳回确认",
						closeBtn : 2,
						area : [ '400px', '400px' ],
						shadeClose : true,
						content : $('#fnInvalidWithdrawDiv')
					});

					// 驳回确认提交
					$scope.fnInvalidWithdrawSubmit = function() {
						var id = $scope.invalid.id;
						var reason=$("#reason").val();
						if(reason!=""&&reason!=undefined){
							var param = {
									id : $scope.invalid.id,
									reason : reason
								}
								$http({
									method : 'POST',
									url : HRY.modules.account + 'fund/apptransaction/invalid',
									data : $.param(param),
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									}
								}).success(function(data) {
									if (!data.success) {
										$scope.errorName = data.msg;
									} else {
										growl.addInfoMessage('处理成功')
										fnList();
									}
									layer.closeAll();
								});
							
							
						}else{
							$("#message").html("请填写驳回理由");
						}
					
					};

				}
			}

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"取现申请审核");
			}
			function drowExecut(id) {
				//取现确认提交
				$http({
					method : 'POST',
					url : HRY.modules.account + 'fund/apptransaction/confirmwithdraw',
					params : {
						'id' : id
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					console.log(data);
					if (!data.success) {
						$scope.errorName = data.msg;
						growl.addInfoMessage(data.msg);
					} else {
						growl.addInfoMessage('确认成功')
						fnList();
					}
					layer.closeAll();
				});
			}
			
			
		}
		
		// ------------------------提现处理中记录--------------------------------------------
		if ($stateParams.page == "handling") {
			$scope.serchData = {
					status_EQ : $stateParams.id,
					transactionType_in : "2"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			},   {
				"data" : "trueName" //真实姓名
			},{
				"data" : "bankName"
			},  {
				"data" : "custromerAccountNumber"
			},{
				"data" : "created"
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "modified"
			}]
			
			
			config.columnDefs = [ 
			]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			
			$scope.fnList = fnList;// 刷新方法
			
			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"取现第三方处理中");
			}
		}
		
		// ------------------------提现成功---------------------------------------------
		if ( $stateParams.page == "wsuccesslist" ) {
			
			   var n=$location.search().name;
	        	//name不为空时 查询单个用户的信息
	   	         if(n != undefined && n!="" ){
		   		        $scope.serchData={
			           			userName_EQ:n,
			           			status_EQ : $stateParams.id,
			    				transactionType_in : "4,2"
		           	   };
		     	 }else{
		     		    $scope.serchData = {
		    				status_EQ : $stateParams.id,
		    				transactionType_in : "4,2"
		    			};
		     		 
		     	 }
			
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			},  {
				"data" : "surname" //真实姓名
			},  {
				"data" : "trueName" //真实姓名
			}, {
				"data" : "currencyType"
			},{
				"data" : "bankName"
			}, {
				"data" : "custromerAccountNumber"
			}, {
				"data" : "bankProvince"
			},{
				"data" : "bankAddress"
			},{
				"data" : "subBank"
			},/*{
				"data" : "subBankNum"
			},*/{
				"data" : "created"
			},{
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			},{
				"data" : ""
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "modified"
			}]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			} ,{
				"targets" : 13,
				"renderRule" : "VALUE_OPERATION",//值的计算
				"renderValueRule" : "transactionMoney-fee",//值替换规则
				"render" : function(data, type, row) {
					//提现汇率
					var dollarRateWithdraw=1;
					var money="";
					if(row.currencyType=="usd"){
						dollarRateWithdraw=$rootScope.financeConfig.dollarRateWithdraw;
						money=(row.transactionMoney-row.fee) + "("+ ((row.transactionMoney-row.fee)*dollarRateWithdraw).toFixed(2) +"RMB)";
					}else{
						money=(row.transactionMoney-row.fee);
					}
					return money;
				}
			} 
			]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			// 刷新按钮
			$scope.fnList =function() {
				table.DataTable().draw();
			}
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"取现成功查询");
			}
		}
		
		// ------------------------提现失败---------------------------------------------
		if ( $stateParams.page == "wfaillist" ) {
			$scope.serchData = {
				status_EQ : $stateParams.id,
				transactionType_in : "4,2"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			}, {
				"data" : "surname" //真实姓名
			},  {
				"data" : "trueName" //真实姓名
			},{
				"data" : "currencyType"
			}, {
				"data" : "custromerAccountNumber"
			},{
				"data" : "subBankNum"
			}, {
				"data" : "transactionMoney"
			},{
				"data" : "transactionNum"
			}, {
				"data" : "rejectionReason"
			},{
				"data" : "modified"
			}]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}}
			
			]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			// 刷新按钮
			$scope.fnList =function() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"取现失败查询");
			}
		}

		// ------------------------充值申请---------------------------------------
		if ($stateParams.page == "dapplylist") {
			$scope.serchData = {
				status_EQ : $stateParams.id,
				transactionType_in : "5,3"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			}, {
				"data" : "surname" // trueName真实姓名(金科改成前台填写的名字)
			}, {
				"data" : "trueName" // trueName真实姓名(金科改成前台填写的名字)
			}/*,{
				"data" : "currencyType"
			}*/, {
				"data" : "transactionType"
			},  {
				"data" : "bankNum"
			},{
				"data" : "custromerAccountNumber"
			}, {
				"data" : "ourAccountNumber"
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : "remark"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "thirdPayName"
			} , {
				"data" : "created"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {debugger;
					if (data != null && data == "1") {
						return "线上充值"
					}
					if (data != null && data == "2") {
						return "线上提现"
					}
					if (data != null && data == "3") {
						return "线下充值"
					}
					if (data != null && data == "4") {
						return "线下提现"
					}if (data != null && data == "5") {
						return "支付宝充值"
					}
					return "";
				}
			},{
				"targets" : 8,
				"render" : function(data, type, row) {
					//提现汇率
					var dollarRate=1;
					var money="";
					if(row.currencyType=="usd"){
						dollarRate=$rootScope.financeConfig.dollarRate;
						money=(row.transactionMoney) + "("+ ((row.transactionMoney)*dollarRate).toFixed(2) +"RMB)";
					}else{
						money=(row.transactionMoney);
					}
					return money;
				}
			}  ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnList = fnList;// 刷新方法
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"充值申请审核");
			}

			$scope.fnConfirm = fnConfirm;// 确认充值
			$scope.fnInvalid = fnInvalid;
			$scope.fnInvalid1 = fnInvalid1;
			$scope.fnQuery = fnQuery;// 查看订单状态

			// 通过充值方法
			function fnConfirm() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					$scope.confirm = DT.getRowData(table)[0];
					$scope.confirm.actualMoney = $scope.confirm.transactionMoney - $scope.confirm.fee

					$('#fnConfirmDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "充值确认",
						closeBtn : 2,
						area : [ '400px', '340px' ],
						shadeClose : true,
						content : $('#fnConfirmDiv')
					});

					// 充值通过提交
					$scope.fnConfirmSubmit = function() {
						var id = $scope.confirm.id;
						$http({
							method : 'POST',
							url : HRY.modules.account + 'fund/apptransaction/confirm',
							params : {
								'id' : id
							},
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (!data.success) {
								$scope.errorName = data.msg;
							} else {
								growl.addInfoMessage('确认成功')
								fnList();
							}
							layer.closeAll();
						});

					}

				}
			}
			// 驳回处理
			function fnInvalid1() {
				var post_ids="";
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$scope.invalid = DT.getRowData(table)[0];

					$('#fnInvalidDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "驳回确认",
						closeBtn : 2,
						area : [ '400px', '400px' ],
						shadeClose : true,
						content : $('#fnInvalidDiv')
					});
					// 驳回确认提交
					$scope.fnInvalidSubmit = function() {
						var id = $scope.invalid.id;
						var reason=$("#reason").val();
						if(reason!=""&&reason!=undefined){
							var param = {
									id : $scope.invalid.id,
									reason : reason
								}
								$http({
									method : 'POST',
									url : HRY.modules.account + 'fund/apptransaction/invalid',
									data : $.param(param),
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									}
								}).success(function(data) {
									if (!data.success) {
										$scope.errorName = data.msg;
									} else {
										growl.addInfoMessage('处理成功')
										fnList();
									}
									layer.closeAll();
								});
						}else{
							$("#message").html("驳回理由不能为空")
						}
					
					};

				}
			}
			// 无效处理(批量)
			function fnInvalid() {
				var post_ids="";
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} 
				
				else  {
					for(var i=0;i<ids.length;i++){
						post_ids=ids[i]+"_"+post_ids
					}
					$scope.invalid = DT.getRowData(table)[0];
					$('#fnInvalidDiv1').removeClass("hide");
					layer.open({
						type : 1,
						title : "驳回确认",
						closeBtn : 2,
						area : [ '400px', '400px' ],
						shadeClose : true,
						content : $('#fnInvalidDiv1')
					});
					// 驳回确认提交
					$scope.fnInvalidSubmit1 = function() {
						var id = $scope.invalid.id;
						var reason=$("#reason1").val();
						if(reason!=""&&reason!=undefined){
							var param = {
									//id : $scope.invalid.id,
									id:post_ids,
									reason : reason
							}
							$http({
								method : 'POST',
								url : HRY.modules.account + 'fund/apptransaction/invalid1',
								data : $.param(param),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if (!data.success) {
									$scope.errorName = data.msg;
								} else {
									growl.addInfoMessage('处理成功')
									fnList();
								}
								layer.closeAll();
							});
						}else{
							$("#message").html("驳回理由不能为空")
						}
						
					};
					
				}
			}

			// 查询订单状态
			function fnQuery() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$http({
						method : 'POST',
						url : HRY.modules.account + 'fund/apptransaction/queryOrder',
						params : {
							'id' : ids
						},
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						console.log(data);
						if (!data.success) {
							$scope.errorName = data.msg;
						} else {
							growl.addInfoMessage('订单成功')
							fnList();
						}
					});

				}
			}

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			// 刷新按钮
			$scope.fnList =function() {
				table.DataTable().draw();
			}
		}
		// ------------------------充值成功---------------------------------------
		if ($stateParams.page == "dsuccesslist") {
				 var n=$location.search().name;
		        	//name不为空时 查询单个用户的信息
	   	         if(n != undefined && n!="" ){
		   		        $scope.serchData={
			           			userName_EQ:n,
			           			status_EQ : $stateParams.id,
			    				transactionType_in : "5,3,1"
		           	   };
		     	 }else{
						$scope.serchData = {
							status_EQ : $stateParams.id,
							transactionType_in : "5,3,1"
						};
		     	 }
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			},  {
				"data" : "surname" // 真实姓名
			},  {
				"data" : "trueName" // 真实姓名
			}/*,{
				"data" : "currencyType"
			}*/, {
				"data" : "transactionType"
			},{
				"data" : "bankNum"
			}, {
				"data" : "custromerAccountNumber"
			}, {
				"data" : "ourAccountNumber"
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : ""
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "remark"
			}, {
				"data" : "modified"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,
				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "线上充值"
					}
					if (data != null && data == "2") {
						return "线上提现"
					}
					if (data != null && data == "3") {
						return "线下充值"
					}
					if (data != null && data == "4") {
						return "线下提现"
					}if (data != null && data == "5") {
						return "支付宝充值"
					}
					return "";
				}
			}, {
				"targets" : 10,
				"renderRule" : "VALUE_OPERATION",//值的计算
				"renderValueRule" : "transactionMoney-fee",//值替换规则
				"render" : function(data, type, row) {
					//提现汇率
					var dollarRate=1;
					var money="";
					if(row.currencyType=="usd"){
						dollarRate=$rootScope.financeConfig.dollarRate;
						money=(row.transactionMoney-row.fee) + "("+ ((row.transactionMoney-row.fee)*dollarRate).toFixed(2) +"RMB)";
					}else{
						money=(row.transactionMoney-row.fee);
					}
					return money;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			$scope.fnList = fnList;// 刷新方法
			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			/**
			 * 撤销方法
			 */
			$scope.fnUndo = function(){

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$http({
						method : 'POST',
						url : HRY.modules.account + 'fund/apptransaction/undo',
						params : {
							'id' : ids
						},
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						console.log(data);
						if (!data.success) {
							$scope.errorName = data.msg;
						} else {
							growl.addInfoMessage('撤销成功')
							fnList();
						}
					});

				}
			
			}
			
			// 刷新按钮
			$scope.fnList =function() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"充值成功查询");
			}
			
		}
		// ------------------------充值失败---------------------------------------
		if ($stateParams.page == "dfaillist") {
			$scope.serchData = {
				status_EQ : $stateParams.id,
				transactionType_in : "5,3,1"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/list.do';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName" // 账户ID
			},  {
				"data" : "surname" // 真实姓名
			}, {
				"data" : "trueName" // 真实姓名
			}/*, {
				"data" : "currencyType"
			}*/, {
				"data" : "transactionType"
			},{
				"data" : "bankNum"
			},  {
				"data" : "custromerAccountNumber"
			}, {
				"data" : "ourAccountNumber"
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "remark"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "rejectionReason"
			}, {
				"data" : "remark"
			}, {
				"data" : "modified"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,
				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "线上充值"
					}
					if (data != null && data == "2") {
						return "线上提现"
					}
					if (data != null && data == "3") {
						return "线下充值"
					}
					if (data != null && data == "4") {
						return "线下提现"
					}
					if (data != null && data == "5") {
						return "支付宝充值"
					}
					return "";
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			$scope.fnList = fnList;// 刷新方法
			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"充值失败查询");
			}
		}
		
		
		if($stateParams.page == "dreport"){

			$scope.serchData = {
				status_EQ : 2,
				transactionType_in : "5,3,1"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/report';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [  {
				"data" : "id"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "modified"
			} ,{
				"data" : "userName" // 账户ID
			}, {
				"data" : "trueName" //真实姓名
			},{
				"data" : "currencyType"
			}, {
				"data" : "transactionType"
			},  {
				"data" : "bankNum"
			},{
				"data" : "custromerAccountNumber"
			}, {
				"data" : "ourAccountNumber"
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			},  {
				"data" : ""
			}]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			},{
				"targets" : 6,
				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "线上充值"
					}
					if (data != null && data == "2") {
						return "线上提现"
					}
					if (data != null && data == "3") {
						return "线下充值"
					}
					if (data != null && data == "4") {
						return "线下提现"
					}if (data != null && data == "5") {
						return "支付宝充值"
					}
					return "";
				}
			},{
				"targets" : 12,
				"renderRule" : "VALUE_OPERATION",//值的计算
				"renderValueRule" : "transactionMoney-fee",//值替换规则
				"render" : function(data, type, row) {
					return row.transactionMoney - row.fee;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnList = fnList;// 刷新方法
			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"资金充值及收费台账");
			}
		}
		
		
		if($stateParams.page == "wreport"){

			$scope.serchData = {
				status_EQ : 2,
				transactionType_in : "2,4"
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.account + 'fund/apptransaction/report';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				DT.inputData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [  {
				"data" : "id"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "modified"
			} ,{
				"data" : "userName" // 账户ID
			}, {
				"data" : "trueName" //真实姓名
			},{
				"data" : "currencyType"
			}, {
				"data" : "transactionType"
			},  {
				"data" : "bankNum"
			}, {
				"data" : "bankProvince"
			}, {
				"data" : "bankAddress"
			}, {
				"data" : "subBank"
			},{
				"data" : "custromerAccountNumber"
			}, {
				"data" : "ourAccountNumber"
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			},  {
				"data" : ""
			}]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			},{
				"targets" : 6,
				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "线上充值"
					}
					if (data != null && data == "2") {
						return "线上提现"
					}
					if (data != null && data == "3") {
						return "线下充值"
					}
					if (data != null && data == "4") {
						return "线下提现"
					}
					return "";
				}
			},{
				"targets" : 15,
				"renderRule" : "VALUE_OPERATION",//值的计算
				"renderValueRule" : "transactionMoney-fee",//值替换规则
				"render" : function(data, type, row) {
					return row.transactionMoney - row.fee;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnList = fnList;// 刷新方法
			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"资金取现及收费台账");
			}
		}
		
		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});