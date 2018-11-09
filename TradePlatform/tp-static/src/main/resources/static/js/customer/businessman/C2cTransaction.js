 /**
 * Copyright:   北京互融时代软件有限公司
 * C2cTransaction.js
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
define([ 'app', 'hryTable', 'layer',"hryUtil",'pikadayJq' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "businessman/c2ctransaction/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				if(data.transactionType==1){
					$scope.formData.transactionTypeValue = "买";
				}else{
					$scope.formData.transactionTypeValue = "卖";
				}
			});
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {

			$scope.formData = {};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.customer + 'businessman/c2ctransaction/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/businessman/c2ctransaction/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};
		}

		/**
		 * ------------------------修改页面路径---------------------------------------------
		 */
		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.customer + "businessman/c2ctransaction/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.customer + 'businessman/c2ctransaction/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/businessman/c2ctransaction/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}

		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {
					status_EQ : 1
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.customer + 'businessman/c2ctransaction/list';
			config.ajax.data = function(d) {

				// 设置select下拉框
				DT.selectData($scope.serchData);
				
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			{
				"data" : "id"   //id
			},
			{
				"data" : "created"   //创建时间
			},
			{
				"data" : "userName"   //用户登录名
			},
			{
				"data" : "businessmanTrueName"   //交易商
			},
                {
                    "data" : "businessmanBankcard"   //商家银行卡
                },
                {
                    "data" : "businessmanBankName"   //银行
                },

			{
				"data" : "transactionTypeByDes"   //交易类型
			},
			{
				"data" : "coinCode"   //交易金额
			},
			{
				"data" : "transactionCount"   //交易数量
			},
			{
				"data" : "transactionMoney"   //交易金额
			},
			{
				"data" : "fee"   //交易手续费
			},
			{
				"data" : "statusByDes"   //1待审核 2已完成 3以否决
			},
			{
				"data" : "randomNum"   //汇款备注码
			},
			{
				"data" : "transactionNum"   //交易订单号
			},
			{
				"data" : "allName" //会员姓名
			},{
				"data" : "cardBank"   //卖家开户银行
		    },{
				"data" : "cardNumber"   //卖家银行卡号
			},{
				"data" : "subBank"   //卖家开户支行
			}
			
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}  , {
				"targets" : 6,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					/*//1买 2卖  ---2018.4.25 by gt
					if(data==1){
						return "<font color=\"red\">买</font>"
					}
					if(data==2){
						return "<font color=\"green\">卖</font>"
					}
					return "";*/
                    //1买 2卖
                    if(data=="买"){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data=="卖"){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";
				},
			}, {
				"targets" : 11,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					/*//1待审核 2已完成 3以否决 ---2018.4.25 by gt
					if(data==1){
						return "<font color=\"red\">待审核</font>"
					}
					if(data==2){
						return "<font color=\"green\">交易成功</font>"
					}
					if(data==3){
						return "<font color=\"blue\">交易取消</font>"
					}
					return "";*/
					if(data=="待审核"){
						return "<font color=\"red\">待审核</font>"
					}
					if(data=="交易成功"){
						return "<font color=\"green\">交易成功</font>"
					}
					if(data=="交易取消"){
						return "<font color=\"blue\">交易取消</font>"
					}
					return "";
				},
			} ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
			// 导出excel
			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "交易商交易订单");
			}
			
			/**
			 * 查看按钮
			 */
			$scope.fnSee = function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = "#/customer/businessman/c2ctransaction/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/customer/businessman/c2ctransaction/add/anon";
			}
			
			/**
			 * 修改按钮
			 */
			$scope.fnModify = function(){
            	var	ids = DT.getSelect(table);
            	if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
        			window.location.href='#/customer/businessman/c2ctransaction/modify/'+ids[0];
            	}
			}
			
			
			/**
			 * 支付完成
			 */
			$scope.fnPaySuccess = function(){
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确认支付完成吗？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "businessman/c2ctransaction/paySuccess/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('确认成功')
							// 重新加载list
							$scope.fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

				});
			}
			
			
			//交易确认
			$scope.fnConfirm = function(){
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确认到账吗？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "businessman/c2ctransaction/confirm/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('确认成功')
							// 重新加载list
							$scope.fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

				});
			}
			
			
			/**
			 * 交易关闭
			 */
			$scope.fnClose = function() {
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确定关闭吗？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "businessman/c2ctransaction/close/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('关闭成功')
							// 重新加载list
							$scope.fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

				});
			}
			
			
			
			/**
			 * 删除按钮
			 */
			$scope.fnRemove = function() {
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确定删除？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "businessman/c2ctransaction/remove/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('删除成功')
							// 重新加载list
							$scope.fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

				});
			}

		}
			
		
		if ($stateParams.page == "all") {
			var table = $("#dataTable");
			$scope.serchData = {
					
			};
            // --------------------加载dataTable--------------------------------
            var config = DT.config();
            config.bAutoSerch = true; // 是否开启键入搜索
            config.ajax.url = HRY.modules.customer + 'businessman/c2ctransaction/list';
            config.ajax.data = function(d) {
                // 设置select下拉框
                DT.selectData($scope.serchData);

                $.each($scope.serchData, function(name, value) {
                    if ("" != value) {
                        eval("d." + name + "='" + value + "'");
                    }
                });
            }
			config.columns = [ 
			{
				"data" : "id"   //id
			},
			{
				"data" : "transactionNum"   //交易订单号
			},
			{
				"data" : "created"   //创建时间
			},
			{
				"data" : "userName"   //用户登录名
			},
			{
				"data" : "businessmanTrueName"   //交易商
			},
                {
                    "data" : "businessmanBankcard"   //商家银行卡
                },
                {
                    "data" : "businessmanBankName"   //银行
                },
			{
				"data" : "transactionTypeByDes"   //交易类型
			},
			{
				"data" : "coinCode"   //交易金额
			},
			{
				"data" : "transactionCount"   //交易数量
			},
			{
				"data" : "transactionMoney"   //交易金额
			},
			{
				"data" : "fee"   //交易手续费
			},
			{
				"data" : "statusByDes"   //1待审核 2已完成 3以否决
			},
			{
				"data" : "remark"   //取消原因
			},
			{
				"data" : "randomNum"   //备注码
			},
			{
				"data" : "allName" //会员姓名
			}
			,{
				"data" : "cardBank"   //卖家开户银行
			},{
				"data" : "cardNumber"   //卖家银行卡号
			}, {
				"data" : "subBank"   //卖家开户支行
			},
			{
				"data" : "handleId"   //管理人ID
			},{
				"data" : "handleName"   //管理人姓名
			}, {
				"data" : "handleIp"   //管理人IP
			},
			
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}  , {
				"targets" : 7,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
                    /*//1买 2卖  ---2018.4.25 by gt
                    if(data==1){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data==2){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";*/
                    //1买 2卖
                    if(data=="买"){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data=="卖"){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";
				},
			}, {
				"targets" : 12,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
                    /*//1待审核 2已完成 3以否决 ---2018.4.25 by gt
                    if(data==1){
                        return "<font color=\"red\">待审核</font>"
                    }
                    if(data==2){
                        return "<font color=\"green\">交易成功</font>"
                    }
                    if(data==3){
                        return "<font color=\"blue\">交易取消</font>"
                    }
                    return "";*/
                    if(data=="待审核"){
                        return "<font color=\"red\">待审核</font>"
                    }
                    if(data=="交易成功"){
                        return "<font color=\"green\">交易成功</font>"
                    }
                    if(data=="交易取消"){
                        return "<font color=\"blue\">交易取消</font>"
                    }
                    return "";
				},
			}
			]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}

            // 导出excel
            $scope.fnExcel = function() {
                DT.excel(table, this.serchData, "全部交易订单");
            }
			

		}
		
		if ($stateParams.page == "close") {
			var table = $("#dataTable");
			$scope.serchData = {
					status_EQ : 3
			};
            // --------------------加载dataTable--------------------------------
            var config = DT.config();
            config.bAutoSerch = true; // 是否开启键入搜索
            config.ajax.url = HRY.modules.customer + 'businessman/c2ctransaction/list';
            config.ajax.data = function(d) {
                // 设置select下拉框
                DT.selectData($scope.serchData);

                $.each($scope.serchData, function(name, value) {
                    if ("" != value) {
                        eval("d." + name + "='" + value + "'");
                    }
                });
            }
			config.columns = [ 
			{
				"data" : "id"   //id
			},
			{
				"data" : "transactionNum"   //交易订单号
			},
			{
				"data" : "created"   //创建时间
			},
			{
				"data" : "userName"   //用户登录名
			},
			{
				"data" : "businessmanTrueName"   //交易商
			},
			{
				"data" : "transactionTypeByDes"   //交易类型
			},
			{
				"data" : "coinCode"   //交易金额
			},
			{
				"data" : "transactionCount"   //交易数量
			},
			{
				"data" : "transactionMoney"   //交易金额
			},
			{
				"data" : "fee"   //交易手续费
			},
			{
				"data" : "statusByDes"   //1待审核 2已完成 3以否决
			},
			{
				"data" : "remark"   //取消原因
			},
			{
				"data" : "randomNum"   //备注码
			},
			{
				"data" : "allName" //会员姓名
			},
			{
				"data" : "cardBank"   //卖家开户银行
			},
			{
				"data" : "cardNumber"   //卖家银行卡号
			},
			{
				"data" : "subBank"   //卖家开户支行
			},
			{
				"data" : "handleId"   //管理人ID
			},{
				"data" : "handleName"   //管理人姓名
			}, {
				"data" : "handleIp"   //管理人IP
			},
			
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}  , {
				"targets" : 5,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
                    /*//1买 2卖  ---2018.4.25 by gt
                    if(data==1){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data==2){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";*/
                    //1买 2卖
                    if(data=="买"){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data=="卖"){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";
				},
			}, {
				"targets" : 10,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
                    /*//1待审核 2已完成 3以否决 ---2018.4.25 by gt
                    if(data==1){
                        return "<font color=\"red\">待审核</font>"
                    }
                    if(data==2){
                        return "<font color=\"green\">交易成功</font>"
                    }
                    if(data==3){
                        return "<font color=\"blue\">交易取消</font>"
                    }
                    return "";*/
                    if(data=="待审核"){
                        return "<font color=\"red\">待审核</font>"
                    }
                    if(data=="交易成功"){
                        return "<font color=\"green\">交易成功</font>"
                    }
                    if(data=="交易取消"){
                        return "<font color=\"blue\">交易取消</font>"
                    }
                    return "";
				},
			} ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
            // 导出excel
            $scope.fnExcel = function() {
                DT.excel(table, this.serchData, "已取消交易订单");
            }

		}
		
		
		if ($stateParams.page == "confirm") {
			var table = $("#dataTable");
			$scope.serchData = {
					status_EQ : 2
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.customer + 'businessman/c2ctransaction/list';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			{
				"data" : "id"   //id
			},
			{
				"data" : "transactionNum"   //交易订单号
			},
			{
				"data" : "created"   //创建时间
			},
			{
				"data" : "userName"   //用户登录名
			},
			{
				"data" : "businessmanTrueName"   //交易商
			},
			{
				"data" : "transactionTypeByDes"   //交易类型
			},
			{
				"data" : "coinCode"   //交易金额
			},
			{
				"data" : "transactionCount"   //交易数量
			},
			{
				"data" : "transactionMoney"   //交易金额
			},
			{
				"data" : "fee"   //交易手续费
			},
			{
				"data" : "statusByDes"   //1待审核 2已完成 3以否决
			},
			{
				"data" : "randomNum"   //备注码
			},{
				"data" : "allName" //会员姓名
			},{
				"data" : "cardBank"   //卖家开户银行
			},{
				"data" : "cardNumber"   //卖家银行卡号
			},{
				"data" : "subBank"   //卖家开户支行
			},{
				"data" : "handleId"   //管理人ID
			},{
				"data" : "handleName"   //管理人姓名
			}, {
				"data" : "handleIp"   //管理人IP
			}
			
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}  , {
				"targets" : 5,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
                    /*//1买 2卖  ---2018.4.25 by gt
                    if(data==1){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data==2){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";*/
                    //1买 2卖
                    if(data=="买"){
                        return "<font color=\"red\">买</font>"
                    }
                    if(data=="卖"){
                        return "<font color=\"green\">卖</font>"
                    }
                    return "";
				},
			}, {
				"targets" : 10,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
                    /*//1待审核 2已完成 3以否决 ---2018.4.25 by gt
                    if(data==1){
                        return "<font color=\"red\">待审核</font>"
                    }
                    if(data==2){
                        return "<font color=\"green\">交易成功</font>"
                    }
                    if(data==3){
                        return "<font color=\"blue\">交易取消</font>"
                    }
                    return "";*/
                    if(data=="待审核"){
                        return "<font color=\"red\">待审核</font>"
                    }
                    if(data=="交易成功"){
                        return "<font color=\"green\">交易成功</font>"
                    }
                    if(data=="交易取消"){
                        return "<font color=\"blue\">交易取消</font>"
                    }
                    return "";
				},
			} ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
            // 导出excel
            $scope.fnExcel = function() {
                DT.excel(table, this.serchData, "已完成交易订单");
            }
			

		}
		
		
		// 加载插件
		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();
	}
	
	// -----------controller.js加载完毕--------------
	return {
		controller : controller
	};
});