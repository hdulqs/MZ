define([ 'app', 'hryTable', 'pikadayJq', 'hryUtil' ], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', , '$location', '$rootScope', '$http', '$stateParams' ];
	function controller($scope, $location, $rootScope, $http, $stateParams, hryCore) {
		// 初始化js 插件
		$scope.unitTips = tips.unitTips;
		var table = $("#table2");

		// ------------------------ 币的充值成功驳回功能
		// ---------------------------------------------
		if ($stateParams.page == "RejectedList") {
			$scope.serchData = {
				status_EQ : "1",
				transactionType_EQ : "1"
			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 转入钱包地址
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "confirmations"
			}, {
				"data" : "blocktime"
			}, {
				"data" : "time"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 6,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 7,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 8,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法
			$scope.fnRecord = fnRecord;// 刷新钱包记录

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnPost() {
				//	
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					if (fee == null || fee == "") {
						fee = "0"

					}
					var money = transactionMoney - fee;
					layer.open({
						title : '确认通过',
						content : '用户账号：' + userName + '</br>转入个数：' + transactionMoney + "个" + '<br>手续费：' + fee + "个" + '<br>实际到账：' + money + "个" + '<br>TIPS:到账确认操作不可逆，请确认货币已到账。',
						btn : [ '到账确认', '返回' ],
						yes : function() {
							// 关闭提示框
							layer.closeAll();
							// 执行操作开始

							$http.get(HRY.modules.exchange + "coinTransaction/appicocointransaction/post/" + ids[0]).success(function(data) {

								if (data.success) {
									growl.addInfoMessage("处理成功");
									table.DataTable().draw();
								} else {
									growl.addInfoMessage("确认节点数至少为2才可以通过审核");
								}
							});

							// 执行操作结束
						}
					});

				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnStop() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					var money = transactionMoney - fee;
					layer.open({
						title : '驳回',
						content : '用户账号：' + userName + '</br>转入个数：' + transactionMoney + "个" + '<br>驳回理由：<textarea name="MSG" id="reason" cols=20 rows=4></textarea>' + '<br>TIPS:驳回操作不可逆，请确认转入操作无效' + '<br><span id="message"  style="color:red"></span>'

						,
						btn : [ '确认驳回', '返回' ],
						yes : function() {

							// 执行操作开始
							var reason = $("#reason").val();
							if (reason != "" && reason != null) {
								// 关闭提示框
								layer.closeAll();
								var param = {
									reason : reason
								}

								$http({
									method : 'POST',
									url : HRY.modules.exchange + "coinTransaction/appicocointransaction/refuseWith/" + ids[0],
									data : $.param(param),


								}).success(function(data) {

									if (data.success) {
										growl.addInfoMessage('处理成功')
										fnList();
									} else {
										$scope.errorName = data.msg;
									}

								});

							} else {
								$("#message").html("请填写驳回理由")
							}

							// 执行操作结束
						}
					});

				}
			}

			// 刷新钱包记录
			function fnRecord(e) {
				$(e.currentTarget).attr("disabled", "disabled");
				$(e.currentTarget).find("span").html("请耐心等待");
				$http.get(HRY.modules.exchange + "coinTransaction/appicocointransaction/record").success(function(data) {
					growl.addInfoMessage('刷新成功')
					table.DataTable().draw();
					$(e.currentTarget).removeAttr("disabled");
					$(e.currentTarget).find("span").html("转入记录刷新");
				});

			}
		}

		// ------------------------虚拟币充值申请审核---------------------------------------------
		if ($stateParams.page == "DapplyList") {
			$scope.serchData = {
				status_EQ : "1",
				transactionType_EQ : "1"
			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 转入钱包地址
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : "transactionNum"
			}, {
				"data" : "confirmations"
			}, {
				"data" : "blocktime"
			}, {
				"data" : "time"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 6,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 7,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 8,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法
			$scope.fnRecord = fnRecord;// 刷新钱包记录

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnPost() {
				//	
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					if (fee == null || fee == "") {
						fee = "0"

					}
					var money = transactionMoney - fee;
					layer.open({
						title : '确认通过',
						content : '用户账号：' + userName + '</br>转入个数：' + transactionMoney + "个" + '<br>手续费：' + fee + "个" + '<br>实际到账：' + money + "个" + '<br>TIPS:到账确认操作不可逆，请确认货币已到账。',
						btn : [ '到账确认', '返回' ],
						yes : function() {
							// 关闭提示框
							layer.closeAll();
							// 执行操作开始

							$http.get(HRY.modules.exchange + "coinTransaction/appicocointransaction/post/" + ids[0]).success(function(data) {

								if (data.success) {
									growl.addInfoMessage("处理成功");
									table.DataTable().draw();
								} else {
									growl.addInfoMessage("确认节点数至少为2才可以通过审核");
								}
							});

							// 执行操作结束
						}
					});

				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnStop() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					var money = transactionMoney - fee;
					layer.open({
						title : '驳回',
						content : '用户账号：' + userName + '</br>转入个数：' + transactionMoney + "个" + '<br>驳回理由：<textarea name="MSG" id="reason" cols=20 rows=4></textarea>' + '<br>TIPS:驳回操作不可逆，请确认转入操作无效' + '<br><span id="message"  style="color:red"></span>'

						,
						btn : [ '确认驳回', '返回' ],
						yes : function() {

							// 执行操作开始
							var reason = $("#reason").val();
							if (reason != "" && reason != null) {
								// 关闭提示框
								layer.closeAll();
								var param = {
									reason : reason
								}

								$http({
									method : 'POST',
									url : HRY.modules.exchange + "coinTransaction/appicocointransaction/refuseWith/" + ids[0],
									data : $.param(param),
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									}
								}).success(function(data) {

									if (data.success) {
										growl.addInfoMessage('处理成功')
										fnList();
									} else {
										$scope.errorName = data.msg;
									}

								});

							} else {
								$("#message").html("请填写驳回理由")
							}

							// 执行操作结束
						}
					});

				}
			}

			// 刷新钱包记录
			function fnRecord(e) {
				$(e.currentTarget).attr("disabled", "disabled");
				$(e.currentTarget).find("span").html("请耐心等待");
				$http.get(HRY.modules.exchange + "coinTransaction/appicocointransaction/record").success(function(data) {
					growl.addInfoMessage('刷新成功')
					table.DataTable().draw();
					$(e.currentTarget).removeAttr("disabled");
					$(e.currentTarget).find("span").html("转入记录刷新");
				});

			}

			// 导出excel
			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "充币申请审核");
			}
		}

		// ------------------------虚拟币充值成功---------------------------------------------
		if ($stateParams.page == "resuccesslist") {

			var n = $location.search().name;
			// name不为空时 查询单个用户的信息
			if (n != undefined && n != "") {
				$scope.serchData = {
					customerName_EQ : n,
					status_EQ : "2",
					transactionType_EQ : "1"
				};
			} else {
				$scope.serchData = {
					status_EQ : "2",
					transactionType_EQ : "1"
				};
			}

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 转入钱包地址
			}, {
				"data" : "modified"// 操作时间
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "transactionNum"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop2;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			function fnStop2() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					var money = transactionMoney - fee;

					layer.open({
						title : '确认通过',
						content : '用户账号：' + userName + '<br>提币个数：' + transactionMoney + '个' + '<br>驳回理由：<textarea name="MSG" id="reason" cols=20 rows=4></textarea>' + '<br>TIPS:驳回操作不可逆，请确认提币操作无效。' + '<br><span id="message"  style="color:red"></span>'

						,
						btn : [ '确认驳回', '返回' ],
						yes : function() {

							// 执行操作开始
							var reason = $("#reason").val();
							var param = {
								reason : reason
							}
							if (reason != "" && reason != null) {
								// 关闭提示框
								layer.closeAll();

								$http.get(HRY.modules.exchange + "coinTransaction/appicocointransaction/cancelTransaction?id=" + ids[0]).success(function(data) {
									debugger
									if (data.success) {
										growl.addInfoMessage('驳回成功  请手动对这个用户转币操作')
										fnList();
									} else {
										growl.addInfoMessage(data.msg);
									}
								});
							} else {
								$("#message").html("请填写驳回理由")
							}

							// 执行操作结束
						}
					});

				}
			}

			// 导出excel
			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "充币成功查询");
			}
			
			/**
			 * 手动导入excel方法(批量导币)
			 */
			$scope.fnRechargeList = function() {
				debugger;
				$('#rechargeListDiv').removeClass("hide");
				layer.open({
					type : 1,
					title : "批量导币",
					closeBtn : 2,
					area : [ '500px', '300px' ],
					shadeClose : true,
					content : $('#rechargeListDiv')
				});
				$scope.fnRechargeListSubmit = function() {
					debugger;
					//先判断上传的是否是excel
					var f_content = document.getElementById("upfile").value;
				    var fileext=f_content.substring(f_content.lastIndexOf("."),f_content.length)  
				    fileext=fileext.toLowerCase()  
				    if (fileext!='.xls' && fileext!='.xlsx'){  
				    	growl.addInfoMessage("导入数据格式必须是excel格式文件");  
				        return false;  
				    } else{
				    	var formData = new FormData($( "#fileUpload" )[0]);  
				        $.ajax({  
				             url: HRY.modules.exchange + '/account/exdigitalmoneyaccount/rechargeList',
				             type: 'POST',  
				             data: formData,  
				             async: false,  
				             cache: false,  
				             contentType: false, 
				             processData: false,
				             success: function (returndata) { 
				            	 debugger;
				            	 var resp = JSON.parse(returndata)
				            	 if(resp.msg==""){
				            		growl.addInfoMessage('充值成功')
									layer.closeAll();
									fnList();
				            	 }else{
				            		 layer.open({
				 						title : '失败信息',
				 						content : resp.msg,
				 						btn : [ '确定' ],
				 						yes : function() {
				 							layer.closeAll();
											fnList();	
				 						}
				 					});
									
				            	 }
				             },  
				             error: function (returndata) {  
				                 alert(returndata);  
				             }  
				        });  
				    	
				    	
				    	/*$http({
							method : 'POST',
							url : HRY.modules.exchange + '/account/exdigitalmoneyaccount/rechargeList',
							data : $.param($scope.formData),
							headers : {
								'Content-Type' : 'multipart/form-data'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('充值成功')
								layer.closeAll();
								fnList();
							} else {
								growl.addInfoMessage('充值失败')
							}
						});*/
				    	 /*$("#fileUpload").action(HRY.host+ HRY.modules.exchange+ '/account/exdigitalmoneyaccount/rechargeList');
				    	 $("#fileUpload").submit();*/
				    	 /* var uploadBtn = $("#upfile");
					    new AjaxUpload(uploadBtn, {
							action : HRY.host+ HRY.modules.exchange+ '/account/exdigitalmoneyaccount/rechargeList',
							name : 'myfile',
							onSubmit : function(file, ext) {
								alert("123");
							},
							onComplete : function(file, response) {
								debugger;
								var resp = JSON.parse(response)
								if(resp!=undefined&&resp.success){
									alert(resp)
									growl.addInfoMessage('批量充值成功')
									layer.closeAll();
								}else{
									layer.msg("上传失败", {
						    		    icon: 1,
						    		    time: 2000
						    		});
								}
							}
						});*/
				    }
				    
				}

			}
		}

		// ------------------------虚拟币充值失败---------------------------------------------
		if ($stateParams.page == "refaillist") {

			$scope.serchData = {
				status_EQ : "3",
				transactionType_EQ : "1"
			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 转入钱包地址
			}, {
				"data" : "modified"// 操作时间
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "transactionNum"
			}/*, {
				"data" : "rejectionReason"
			} */]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 7,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			// 导出excel
			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "充币失败查询");
			}
		}

		// --------------------------提币申请审核---------------------------------------------
		if ($stateParams.page == "withapplylist") {

			$scope.serchData = {
				status_EQ : "1",
				transactionType_EQ : "2"
			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 提币钱包地址
			}, {
				"data" : "created"// 操作时间
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : ""
			},{
				"data" : "transactionNum"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}
					return data;
				}
			}, {
				"targets" : 4,
				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}
					return data;
				}
			}, {
				"targets" : 8,
				//"renderRule" : "VALUE_OPERATION",//值的计算
				//"renderValueRule" : "transactionMoney-fee",//值替换规则
				"render" : function(data, type, row) {
					if(row.transactionMoney!=undefined&&row.transactionMoney!=""
					  &&row.fee!=undefined && row.fee!=""
					){
						return row.transactionMoney - row.fee;
					}
					return "";
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"提币申请审核");
			}

			// 查看按钮
			//批量通过
			$scope.fnPPost=function(){
				var arrId = DT.getSelect(table);
				var ids = hryCore.ArrayToString(arrId);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else {
					layer.open({
						title : '确认批量通过？',
						content : 'TIPS:提币确认操作不可逆，请确认无误！。',
						btn : [ '确认', '返回' ],
						yes : function() {
							// 关闭提示框
							layer.closeAll();
							excutePost(ids);
						}
					});

				}
			}
			
			//批量驳回
			$scope.fnPStop=function(){
				var arrId = DT.getSelect(table);
				var ids = hryCore.ArrayToString(arrId);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else {
					layer.open({
						title : '确认批量驳回？',
						content : 'TIPS:驳回确认操作不可逆，请确认无误！。',
						btn : [ '确认', '返回' ],
						yes : function() {
							// 关闭提示框
							layer.closeAll();
							$http({
								method : 'POST',
								url : HRY.modules.exchange + "coinTransaction/appicocointransaction/refuseWith/" + ids,
								data : {},
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {

								if (data.success) {
									growl.addInfoMessage('处理成功')
									fnList();
								} else {
									growl.addInfoMessage("处理失败");
									$
								}

							});

						}
					});

				}
			}
			
			
			function fnPost() {
				//	
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					var money = transactionMoney - fee;
					if (fee == null || fee == "") {
						fee = "0"

					}

					layer.open({
						title : '确认通过',
						content : '用户账号：' + userName + '<br>提币个数：' + transactionMoney + '个' + '<br>提币手续费：' + fee + '个' + '<br>实际到账个数：' + money + '个' + '<br>TIPS:提币确认操作不可逆，请确认提币个数。',
						btn : [ '提币确认', '返回' ],
						yes : function() {
							// 关闭提示框
							layer.closeAll();
							//对提币操作进行校验
							var id = ids[0];
							excutePost(ids[0]);
						}
					});

				}
			}

			//执行提币操作
			function excutePost(id) {
				// 执行操作开始
				
				$http.get(HRY.modules.exchange + "coinTransaction/appicocointransaction/withdrawals/" + id).success(function(data) {
					if (data.success) {
						growl.addInfoMessage("处理成功");
						table.DataTable().draw();
					} else {
						growl.addInfoMessage(data.msg);
					}
					layer.closeAll();
				});

				// 执行操作结束
			}
			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnStop() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					var data = DT.getRowData(table);
					var userName = data[0].customerName;
					var transactionMoney = data[0].transactionMoney;
					var fee = data[0].fee;
					var money = transactionMoney - fee;

					layer.open({
						title : '确认通过',
						content : '用户账号：' + userName + '<br>提币个数：' + transactionMoney + '个' + '<br>驳回理由：<textarea name="MSG" id="reason" cols=20 rows=4></textarea>' + '<br>TIPS:驳回操作不可逆，请确认提币操作无效。' + '<br><span id="message"  style="color:red"></span>'

						,
						btn : [ '确认驳回', '返回' ],
						yes : function() {

							// 执行操作开始
							var reason = $("#reason").val();
							var param = {
								reason : reason
							}
							if (reason != "" && reason != null) {
								// 关闭提示框
								layer.closeAll();
								$http({
									method : 'POST',
									url : HRY.modules.exchange + "coinTransaction/appicocointransaction/refuseWith/" + ids[0],
									data : $.param(param),
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									}
								}).success(function(data) {

									if (data.success) {
										growl.addInfoMessage('处理成功')
										fnList();
									} else {
										growl.addInfoMessage("处理失败");
										$
									}

								});

							} else {
								$("#message").html("请填写驳回理由")
							}

							// 执行操作结束
						}
					});

				}
			}
		}

		// --------------------------提币成功查询---------------------------------------------
		if ($stateParams.page == "withsuccesslist") {

			var n = $location.search().name;
			// name不为空时 查询单个用户的信息
			if (n != undefined && n != "") {
				$scope.serchData = {
					customerName_EQ : n,
					status_EQ : "2",
					transactionType_EQ : "1"
				};
			} else {
				$scope.serchData = {
					status_EQ : "2",
					transactionType_EQ : "2"
				};
			}

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 提币钱包地址
			}, {
				"data" : "modified"// 操作时间
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : ""// 实际到账数量
			}, {
				"data" : "fee"
			}, {
				"data" : "transactionNum"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 7,

				"render" : function(data, type, row) {

					return row.transactionMoney - row.fee;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"提币成功查询");
			}

		}

		// --------------------------提币失败查询---------------------------------------------
		if ($stateParams.page == "withfaillist") {

			$scope.serchData = {
				status_EQ : "3",
				transactionType_EQ : "2"
			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/list';
			config.ajax.data = function(d) {
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
				"data" : "customerName"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "inAddress"// 提币钱包地址
			}, {
				"data" : "modified"// 操作时间
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "transactionNum"
			}/*, {
				"data" : "rejectionReason"// 驳回原因
			} */]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"提币失败查询");
			}

		}

		// -----------------------------货币提现及收费台账---------------------------------------------
		if ($stateParams.page == "WithdrawList") {

			$scope.serchData = {

			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/allList?type=2';
			config.ajax.data = function(d) {

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
				"data" : "transactionNum"
			}, {
				"data" : "customerName"
			}, {
				"data" : "trueName"// 用户真实姓名
			}, {
				"data" : "coinCode"
			}, {
				"data" : "outAddress"// 提币钱包地址
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : "fee"// 实际到账
			}, {
				"data" : "modified"// 操作时间
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 5,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 8,

				"render" : function(data, type, row) {

					return row.transactionMoney - row.fee;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 导出excel
			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "货币提币及收费台账");
			}
		}

		// -----------------------------货币充币及收费台账---------------------------------------------
		if ($stateParams.page == "RechargeList") {

			$scope.serchData = {
				status_EQ : "1,2,3",
				transactionType_EQ : "1"
			};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'coinTransaction/appicocointransaction/allList?type=1';
			config.ajax.data = function(d) {

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
				"data" : "transactionNum"
			}, {
				"data" : "customerName"
			}, {
				"data" : "trueName"// 用户真实姓名
			}, {
				"data" : "coinCode"
			}, {
				"data" : "outAddress"// 提币钱包地址
			}, {
				"data" : "transactionMoney"
			}, {
				"data" : "fee"
			}, {
				"data" : "fee"// 实际到账
			}, {
				"data" : "modified"// 操作时间
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 5,

				"render" : function(data, type, row) {
					if (data == null || data == "") {
						return "—— ——"
					}

					return data;
				}
			}, {
				"targets" : 8,

				"render" : function(data, type, row) {

					return row.transactionMoney - row.fee;
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnPost = fnPost;// see按钮方法
			$scope.fnStop = fnStop;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 导出excel
			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "货币充币及收费台账");
			}

		}
		hryCore.initPlugins();

	}
	return {
		controller : controller
	};

});