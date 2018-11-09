/**
 * student.js
 */
define([ 'app', 'hryTable', 'hryUtil' ], function(app, DT) {
	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore) {

		// 初始化js 插件

		var table = $("#table2");

		// ------------------------查看页面路径---------------------------------------------
		$scope.creatSelect = function() {
			$http.get(HRY.modules.exchange + "/product/exproduct/selectlist").success(function(data) {
				$scope.parameter = data[0]
				$scope.productParameter = data;
				var html = "";
				for (var i = 0; i < data.length; i++) {
					html += "<option value='" + data[i].id + "' >" + data[i].name + "</option>";
				}
				$("#wselectid").append(html);
				hryCore.initPlugins();
			});
		}

		// ============================ 全局公用方法 start
		// =============================================================================

		// ================================= 全局公用方法 end
		// ========================================================================
		// ------------------------增加页面路径---------------------------------------------
		if ($stateParams.page == "add") {

			$scope.creatSelect();

			// 确定按钮 影藏div
			$scope.correctDiv = function() {
				$("#xiu").attr("class", "mydiv hide");
			}

			// 取消按钮 div
			$scope.sorryDiv = function() {
				var id = $('#wselectid').val();
				for (var i = 0; i < $scope.productParameter.length; i++) {
					if (id == $scope.productParameter[i].id) {
						$("#buyFeeRate").val($scope.productParameter[i].buyFeeRate);
						$("#sellFeeRate").val($scope.productParameter[i].sellFeeRate);
						$("#buyMinMoney").val($scope.productParameter[i].buyMinMoney);
						$("#sellMinCoin").val($scope.productParameter[i].sellMinCoin);
					}
				}
			}

			$scope.formData = {};
			$scope.processForm = function() {

				// 刚添加的产品默认都是准备发行状态
				$scope.formData.issueState = 0;
				$scope.formData.transactionType = $("#schemaModel").val();

				$scope.formData.picturePath = $("#productImage").val();

				// 封装置顶参数
				$scope.formData.isRecommend = $("#isRecommend").val();

				var coinCode = $("#coinCode").val();
				if (!checkCoinCode(coinCode)) {
					growl.addInfoMessage("币的代码必须为英文");
					return false;
				}

				// 
				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'product/exproduct/add',
					data : $.param($scope.formData),
					// params:{'appResourceStr':ids},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage("添加成功");
						window.location.href = '#/exchange/product/exproduct/list/anon';
					} else {
						growl.addInfoMessage('添加失败--- ' + data.msg);
					}
				});
			};
		}

		// ------------------------修改页面路径---------------------------------------------
		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.exchange + "/product/exproduct/see/" + $stateParams.id).success(function(data) {

				$scope.model = data;
				$scope.formData = data;

				if (data.transactionType == 2) {
					$("#schemaModel  option[value='2'] ").attr("selected", true)
					$("#schemaModel  option[value='1'] ").attr("selected", false)
				}

			});

			$scope.creatSelect();

			$('#wselectid').change(function() {
				var id = $('#wselectid').val();
				for (var i = 0; i < $scope.productParameter.length; i++) {
					if (id == $scope.productParameter[i].id) {
						$scope.formData.buyFeeRate = $scope.productParameter[i].buyFeeRate;
						$scope.formData.sellFeeRate = $scope.productParameter[i].sellFeeRate;
						$scope.formData.buyMinMoney = $scope.productParameter[i].buyMinMoney;
						$scope.formData.sellMinCoin = $scope.productParameter[i].sellMinCoin;
						$scope.formData.prepaidFeeRate = $scope.productParameter[i].prepaidFeeRate;
						$scope.formData.paceFeeRate = $scope.productParameter[i].paceFeeRate;
						$scope.formData.oneTimePaceNum = $scope.productParameter[i].oneTimePaceNum;
						$scope.formData.oneDayPaceNum = $scope.productParameter[i].oneDayPaceNum;
						$scope.formData.leastPaceNum = $scope.productParameter[i].leastPaceNum;
						$("#buyFeeRate").val($scope.productParameter[i].buyFeeRate);
						$("#sellFeeRate").val($scope.productParameter[i].sellFeeRate);
						$("#buyMinMoney").val($scope.productParameter[i].buyMinMoney);
						$("#sellMinCoin").val($scope.productParameter[i].sellMinCoin);

					}
				}
			});

			// $scope.formData = {};
			$scope.processForm = function() {

				$scope.formData.picturePath = $("#productImage").val();
				$scope.formData.transactionType = $("#schemaModel").val();

				// $scope.formData.issueState = $("#issueState").val();
				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'product/exproduct/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					console.log(data);
					if (!data.success) {
						$scope.errorName = data.msg;
					} else {
						$scope.message = data.msg;
						window.location.href = '#/exchange/product/exproduct/list/anon';
					}
				});
			};
		}

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'product/exproduct/list?page=product';
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
				"data" : "name"
			}, {
				"data" : "totalNum"
			}, {
				"data" : "issuePrice"
			}, {
				"data" : "openBell"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "issueName"
			},  {
				"data" : "issueTime"
			}, {
				"data" : "issueState"
			} // 
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {
					if (data != null && data == "0") {
						return "<font color='blue'>尚未发行</font>"
					}
					if (data != null && data == "1") {
						return "<font color='red'> 发行中...</font>"
					}
					if (data != null && data == "2") {
						return "<font color='gray'>停牌</font>"
					}
					if (data != null && data == "3") {
						return "退市"
					}
					return "";
				}
			},  {
				"targets" : 8,

				"render" : function(data, type, row) {
					if (data != null && data == 0) {
						return "<font color='blue'>关闭</font>"
					}
					return "<font color='red'>开启</font>";
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnAdd = fnAdd;// add按钮方法
			$scope.fnSee = fnSee;// see按钮方法
			$scope.fnModify = fnModify;// see按钮方法
			// $scope.removeProduct=removeProduct;//remove方法
			$scope.fnList = fnList;// 刷新方法

			$scope.fnPublish = fnPublish; // 发布产品
			$scope.fnOpen = openProduct; // 发布产品
			$scope.fnEnd = endProduct; // 发布产品
			$scope.delistProduct = delistProduct; // 产品退市
			$scope.openTransaction = openTransaction;  //开启交易
			$scope.closeTransaction = closeTransaction; 	//关闭交易
			
			//开启c2c
			$scope.openc2c = function(){


				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {

					layer.confirm("你确定要开启吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/openc2c/" + ids).success(function(data) {
							growl.addInfoMessage("开启成功!");
							// 重新加载list
							fnList();
						});
					})
				}
			
			}
			
			//关闭c2c
			$scope.closec2c = function(){


				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {

					layer.confirm("你确定要关闭吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/closec2c/" + ids).success(function(data) {
							growl.addInfoMessage("关闭成功!");
							// 重新加载list
							fnList();
						});
					})
				}
			
			}


			// 发布商品
			function fnPublish() {

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {

					layer.confirm("你确定要发布吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/publishProduct/" + ids).success(function(data) {
							// 提示信息
							$scope.message = data.msg;
							// 重新加载list
							fnList();
						});
					})
				}
			}

			function openProduct() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {

					layer.confirm("你确定开启此产品吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/openProduct/" + ids[0]).success(function(data) {
							// 提示信息
							// $scope.message=data.msg;
							growl.addInfoMessage(data.msg);
							// 重新加载list
							fnList();
						});
					})
				}

			}

			function endProduct() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {
					layer.confirm("你确定关闭此产品吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/endProduct/" + ids[0]).success(function(data) {
							// 提示信息
							// $scope.message=data.msg;
							growl.addInfoMessage(data.msg);
							// 重新加载list
							fnList();
						});
					})
				}
			}

			function openTransaction() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {

					layer.confirm("你确定开启此产品吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/openTransaction/" + ids[0]).success(function(data) {
							// 提示信息
							// $scope.message=data.msg;
							growl.addInfoMessage(data.msg);
							// 重新加载list
							fnList();
						});
					})
				}

			}

			function closeTransaction() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {
					layer.confirm("你确定关闭此产品吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						layer.closeAll();

						$http.get(HRY.modules.exchange + "product/exproduct/closeTransaction/" + ids[0]).success(function(data) {
							// 提示信息
							// $scope.message=data.msg;
							growl.addInfoMessage(data.msg);
							// 重新加载list
							fnList();
						});
					})
				}
			}
			
			function delistProduct() {

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {

					layer.confirm('你确定要将这个产品退市吗？', {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						// 关闭提示框
						layer.closeAll();
						// 执行操作开始
						var id = ids[0];

						$http.get(HRY.modules.exchange + "product/exproduct/delistProduct/" + ids).success(function(data) {
							// 提示信息
							growl.addInfoMessage(data.msg);
							// 重新加载list
							fnList();
						});
						// 执行操作结束
					})
				}
			}

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 添加按钮
			// ng-click="fnAdd(url)"
			function fnAdd(url) {
				window.location.href = '#/exchange/' + url + '/anon';
			}

			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnSee(url) {
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {
					window.location.href = '#/exchange/' + url + '/' + ids[0];
				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnModify(url) {
				var ids = DT.getSelect(table);
				// 	
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {
					$rootScope.id = ids[0];
					window.location.href = '#/exchange/' + url + '/' + ids[0];
				}
			}

			function removeProduct() {

				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage("请选择数据");
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage("只能选择一条数据");
					return false;
				} else {
					$http.get(HRY.modules.exchange + "/product/exproduct/see/" + ids[0]).success(function(data) {
						if (data.issueState == 1 || data == undefined) {
							layer.confirm("正在发行中的产品不能修改", {
								btn : [ '确定', '取消' ]
							// 按钮
							// ids: ids
							}, function() {
								layer.closeAll();
							});
						} else {

							layer.confirm("你确定删除这个产品", {
								btn : [ '确定', '取消' ]
							// 按钮
							// ids: ids
							}, function() {

								layer.closeAll();

								hryCore.CURD({
									url : HRY.modules.exchange + "product/exproduct/remove/" + ids[0]
								}).remove(null, function(data) {
									if (data.success) {
										// 提示信息
										growl.addInfoMessage("删除成功");
										// 重新加载list
										fnList();
									} else {
										growl.addInfoMessage(data.msg);
									}
								}, function(data) {
									growl.addInfoMessage("error:" + data.msg);
								});
							});
						}
					});
				}
			}
		}

		// ---------------------- 2
		// --列表页面路径---------------------------------------------
		if ($stateParams.page == "publish") {
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'product/exproduct/list';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "name"
			}, {
				"data" : "totalNum"
			}, {
				"data" : "issueTotalMoney"
			}, {
				"data" : "issueState"
			}, {
				"data" : "coinCode"
			}, {
				"data" : "issueName"
			}, {
				"data" : "issueTime"
			} // 
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,
				"orderable" : false,
				"render" : function(data, type, row) {
					if (data == "0") {
						return "<font color='blue'>尚未发行</font>"
					}
					if (data == "1") {
						return "<font color='red'> 发行中...</font>"
					}
					if (data == "2") {
						return "<font color='gray'>停牌</font>"
					}
					if (data == "3") {
						return "退市"
					} else {
						return "";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnAdd = fnAdd;// add按钮方法
			$scope.fnSee = fnSee;// see按钮方法
			$scope.fnModify = fnModify;// see按钮方法
			$scope.fnRemove = fnRemove;// remove方法
			$scope.fnList = fnList;// 刷新方法
			$scope.fnPublish = fnPublish; // 发布产品
			$scope.delistProduct = delistProduct; // 产品退市

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 添加按钮
			// ng-click="fnAdd(url)"
			function fnAdd(url) {
				window.location.href = '#/exchange/' + url + '/anon';
			}

			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnSee(url) {
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据');
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据');
					return false;
				} else {
					window.location.href = '#/exchange/' + url + '/' + ids[0];
				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnModify(url) {
				var ids = DT.getSelect(table);
				// 	
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据');
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据');
					return false;
				} else {
					$rootScope.id = ids[0];
					window.location.href = '#/exchange/' + url + '/' + ids[0];
				}
			}

			// 删除按钮
			// ng-click="fnRemove(url,selectes)"
			function fnRemove(url) {

				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据');
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据');
					return false;
				}

				var id = ids[0];

				hryCore.CURD({
					url : HRY.modules.exchange + url + "/" + id
				}).remove(null, function(data) {
					if (data.success) {
						// 提示信息
						growl.addInfoMessage('删除成功');
						// 重新加载list
						fnList();
					} else {
						growl.addInfoMessage(data.msg);
					}
				}, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});

			}
		}

	}
	return {
		controller : controller
	};
});