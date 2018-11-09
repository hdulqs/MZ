 /**
 * Copyright:   北京互融时代软件有限公司
 * C2cTransaction.js
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "businessman/otccoin/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {
			
			$scope.formData = {};
			$scope.processForm = function() {
				var coinCode = $("#coinCode").val();
				var isOpen = $("#isOpen").val();
                var minbuyPrice = $("#minbuyPrice").val();
                var maxbuyPrice = $("#maxbuyPrice").val();
                var minsellPrice = $("#minsellPrice").val();
                var maxsellPrice = $("#maxsellPrice").val();
				var minCount = $("#minCount").val();
				var maxCount = $("#maxCount").val();
				var poundage_type = $("#poundage_type").val();
				if(coinCode==undefined||coinCode==""){
					growl.addInfoMessage('交易币种不能为空');
					return false;
				}
				if(isOpen==undefined||isOpen==""){
					growl.addInfoMessage('交易开启关闭不能为空');
					return false;
				}
				// if(buyPrice==undefined||buyPrice==""){
				// 	growl.addInfoMessage('买入定价不能为空');
				// 	return false;
				// }
				// if(sellPrice==undefined||sellPrice==""){
				// 	growl.addInfoMessage('卖出定价不能为空');
				// 	return false;
				// }
				
				if(minCount==undefined||minCount==""){
					growl.addInfoMessage('最小交易数量不能为空');
					return false;
				}
				if(maxCount==undefined||maxCount==""){
					growl.addInfoMessage('最大交易数量不能为空');
					return false;
				}
				$scope.formData.isOpen=isOpen;
				$scope.formData.poundage_type=poundage_type;
				$http({
					method : 'POST',
					url : HRY.modules.customer + 'businessman/otccoin/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/businessman/otccoin/list/anon';
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

			$http.get(HRY.modules.customer + "businessman/otccoin/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				hryCore.RenderSelect($("#isOpen"),data.isOpen);
				hryCore.RenderSelect($("#poundage_type"),data.poundage_type);
			});

			$scope.processForm = function() {
				var coinCode = $("#coinCode").val();
				var isOpen = $("#isOpen").val();
				var minbuyPrice = $("#minbuyPrice").val();
                var maxbuyPrice = $("#maxbuyPrice").val();
                var minsellPrice = $("#minsellPrice").val();
				var maxsellPrice = $("#maxsellPrice").val();
				var minCount = $("#minCount").val();
				var maxCount = $("#maxCount").val();
				var poundage_type = $("#poundage_type").val();
				if(coinCode==undefined||coinCode==""){
					growl.addInfoMessage('交易币种不能为空');
					return false;
				}
				if(isOpen==undefined||isOpen==""){
					growl.addInfoMessage('交易开启关闭不能为空');
					return false;
				}
				// if(buyPrice==undefined||buyPrice==""){
				// 	growl.addInfoMessage('买入定价不能为空');
				// 	return false;
				// }
				// if(sellPrice==undefined||sellPrice==""){
				// 	growl.addInfoMessage('卖出定价不能为空');
				// 	return false;
				// }
				
				if(minCount==undefined||minCount==""){
					growl.addInfoMessage('最小交易数量不能为空');
					return false;
				}
				if(maxCount==undefined||maxCount==""){
					growl.addInfoMessage('最大交易数量不能为空');
					return false;
				}
				
				$scope.formData.isOpen=isOpen;
				$scope.formData.poundage_type=poundage_type;
				$http({
					method : 'POST',
					url : HRY.modules.customer + 'businessman/otccoin/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/businessman/otccoin/list/anon';
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
					//status_EQ : 1
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.customer + 'businessman/otccoin/list';
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
				"data" : "coinCode"   //币种代码
			},
			{
				"data" : "isOpen"   //开启交易
			},
			{
				"data" : "poundage_type"   //手续费类型
			},
			{
				"data" : "poundage"   //手续费
			},
			{
				"data" : "maxTradeTime"   //允许最大未付款时间
			},
			{
				"data" : "minbuyPrice"   //最小买入价
			},
				{
                    "data" : "maxbuyPrice"   //最大买入价
                },
                {
                    "data" : "minsellPrice"   //最大卖出价
                },
			{
				"data" : "maxsellPrice"   //最大卖出价
			},
			{
				"data" : "minCount"   //最小交易数量
			},
			{
				"data" : "maxCount"   //最大交易数量
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
				"targets" : 2,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data==1){
						return "<font color=\"red\">开启</font>"
					}
					return "<font color=\"green\">关闭</font>";
				},
				
			},
			{
				"targets" : 3,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data=="Definite"){
						return "<font color=\"red\">固定的</font>"
					}
					return "<font color=\"red\">比例的</font>";
				},
				
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
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
				window.location.href = "#/customer/businessman/otccoin/add/anon";
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
        			window.location.href='#/customer/businessman/otccoin/modify/'+ids[0];
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