/**
 * student.js
 */
define([ 'app', 'hryTable', 'bootstrap-datetimepicker', 'hryUtil' ], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams','$state' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore,$state) {
		$scope.unitTips = tips.unitTips;

		$('.datetimepicker').datetimepicker({
			format : 'yyyy-MM-dd hh:mm:ss',
			collapse : false
		});

		$scope.reset = reset;
		// 重置按钮
		function reset() {
			$scope.serchData = hryCore.reset($scope.serchData);
			fnList();
		}
		$scope.fnList = fnList;
		// 刷新按钮
		function fnList() {
			table.DataTable().draw();
		}

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "listnow") {
			var table = $("#tablelistnow");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
			//当前委托表格
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName"
			},{
				"data" : "surname"
			}, {
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "surplusEntrustCount"
			}, {
				"data" : "processedPrice"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "status"
			},{
				"data" : "customerIp"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {//市价不显示
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价显示
						return data;
					}
				}
			}, {
				"targets" : 15,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 16,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			}, {
				"targets" : 17,
				"render" : function(data, type, row) {
					if (data == "0" || data == "4") {
						return "--";
					} else {

						return "<input type=\"button\" entrustid=" + row.entrustNum + " coinCode=" + row.coinCode + " currencyType=" + row.currencyType + " class=\"btn btn-small\" value=\"明细\" />"

						// return "<a ng-click='detail("+row.id+")'>明细</a>";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnExcel = function() {
				DT.excel(table, this.serchData, "当前委托");
			}
			$scope.fnList = fnList;// 刷新方法
			// $scope.cancelExEntrust=cancelExEntrust;//刷新方法
			$scope.cancelExEntrust = function() {debugger;
				var entrustNums = DT.getSelect(table, "entrustNum");
				if (entrustNums.length == 0) {
					growl.addInfoMessage('请选择数据');
					return false;
				} /*else if (entrustNums.length > 1) {
					growl.addInfoMessage('只能选择一条数据');
					return false;
				}*/
				layer.confirm("确定取消委托吗？请慎重!", {
					btn : [ "确定", "取消" ],// 按钮
					entrustNums : entrustNums[0],
				}, function() {
					layer.closeAll();

					layer.confirm("确定取消委托吗？请慎重！请慎重!", {
						btn : [ "确定", "取消" ],// 按钮
						entrustNums : entrustNums,
					}, function() {
						layer.closeAll();
						$http({
							method : 'POST',
							url : HRY.modules.exchange + 'entrust/exentrust/cancelExEntrust.do?entrustNums=' + entrustNums,
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								layer.msg(data.msg, {
									icon : 1,
									time : 2000
								// 2秒关闭（如果不配置，默认是3秒）
									
								});
								//$route.reload();
								 //$window.location.reload();
								//fnList();
								 $state.go("exchange.entrust_trust", {
							            id: "listnow"
							        },
							        {
							            reload: true
							        });
							} else {
								layer.msg(data.msg, {
									icon : 1,
									time : 2000
								// 2秒关闭（如果不配置，默认是3秒）
								});
							}

						});
					});
				});

			}
		}
		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "listhistory") {
			var table = $("#tablelistlisthistory");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
				"data" : "userName"
			},{
				"data" : "surname"
			}, {
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "surplusEntrustCount"
			}, {
				"data" : "processedPrice"
			}, {
				"data" : "transactionSum"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "status"
			}, {
				"data" : "customerIp"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价
						return data;
					}
				}
			},
			/*
			 * { "targets" :6, // "render" : function(data, type, row) {
			 * if(parseFloat(data)==parseFloat(0)){ return "¥"+row.entrustSum;
			 * }else{ return "฿"+data; } } }, { "targets" : 7, // "render" :
			 * function(data, type, row) { if(row.entrustCount==0 &&
			 * row.entrustPrice==0){ return
			 * "¥"+(parseFloat(row.entrustSum)-parseFloat(row.transactionSum));
			 * }else{ return "฿"+row.surplusEntrustCount; } } },
			 */
			/*
			 * { "targets" : 11, // "render" : function(data, type, row) {
			 * if(data=="1"){ return "人工"; }else{ return "机器人"; } } },
			 */
			{
				"targets" : 16,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 17,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			}, {
				"targets" : 18,
				"render" : function(data, type, row) {
					if (data == "0" || data == "4") {
						return "--";
					} else {

						return "<input type=\"button\" entrustid=" + row.entrustNum + " coinCode=" + row.coinCode + " currencyType=" + row.currencyType + " class=\"btn btn-small\" value=\"明细\" />"

						// return "<a ng-click='detail("+row.id+")'>明细</a>";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnList = fnList;// 刷新方法
			
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"历史委托");
			}

		}
		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "listing") {
			var table = $("#tablelisting");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
				"data" : "userName"
			},{
				"data" : "surname"
			}, {
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "surplusEntrustCount"
			}, {
				"data" : "processedPrice"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "status"
			}, {
				"data" : "customerIp"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价
						return data;
					}
				}
			},

			{
				"targets" : 15,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 16,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			}, {
				"targets" : 17,
				"render" : function(data, type, row) {
					if (data == "0" || data == "4") {
						return "--";
					} else {

						return "<input type=\"button\" entrustid=" + row.entrustNum + " coinCode=" + row.coinCode + " currencyType=" + row.currencyType + " class=\"btn btn-small\" value=\"明细\" />"

						// return "<a ng-click='detail("+row.id+")'>明细</a>";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"全部待成效委托");
			}

			$scope.fnList = fnList;// 刷新方法

		}
		if ($stateParams.page == "listpart") {
			var table = $("#tablelistpart");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
				"data" : "userName"
			}, {
				"data" : "surname"
			},{
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "surplusEntrustCount"
			}, {
				"data" : "processedPrice"
			}, {
				"data" : "transactionSum"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "status"
			} , {
				"data" : "customerIp"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价
						return data;
					}
				}
			},

			{
				"targets" : 16,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 17,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			}, {
				"targets" : 18,
				"render" : function(data, type, row) {
					if (data == "0" || data == "4") {
						return "--";
					} else {

						return "<input type=\"button\" entrustid=" + row.entrustNum + " coinCode=" + row.coinCode + " currencyType=" + row.currencyType + " class=\"btn btn-small\" value=\"明细\" />"

						// return "<a ng-click='detail("+row.id+")'>明细</a>";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"部分成交委托");
			}
			$scope.fnList = fnList;// 刷新方法

		}
		if ($stateParams.page == "listed") {
			var table = $("#tablelisted");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
				"data" : "userName"
			},{
				"data" : "surname"
			}, {
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "surplusEntrustCount"
			}, {
				"data" : "processedPrice"
			}, {
				"data" : "transactionFee"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "status"
			}, {
				"data" : "customerIp"
			}  ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价
						return data;
					}
				}
			},

			{
				"targets" : 16,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 17,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			}, {
				"targets" : 18,
				"render" : function(data, type, row) {
					if (data == "0" || data == "4") {
						return "--";
					} else {

						return "<input type=\"button\" entrustid=" + row.entrustNum + " coinCode=" + row.coinCode + " currencyType=" + row.currencyType + " class=\"btn btn-small\" value=\"明细\" />"

						// return "<a ng-click='detail("+row.id+")'>明细</a>";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"全部成交委托");
			}
			
			$scope.fnList = fnList;// 刷新方法

		}
		if ($stateParams.page == "listcancel") {
			var table = $("#tablelistcancel");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
				"data" : "userName"
			},{
				"data" : "surname"
			}, {
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "customerIp"
			}  ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价
						return data;
					}
				}
			}, {
				"targets" : 13,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 14,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"全部撤销委托");
			}
			
			$scope.fnList = fnList;// 刷新方法

		}

		if ($stateParams.page == "listcancelpart") {
			var table = $("#tablelistcancelpart");
			$scope.projName = HRY.modules.exchange;
			var type = $stateParams.page;
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.onlyClick = function(obj) {
				var entrustNum = obj.attr("entrustid");
				var coinCode = obj.attr("coinCode");
				$scope.fixPriceCoinCode = obj.attr("fixPriceCoinCode");
				$scope.list1 = [];
				$http({
					method : 'GET',
					url : HRY.modules.exchange + "entrust/exentrust/orderFindByentrustNum.do",
					params : {
						entrustNum : entrustNum,
						coinCode : coinCode
					},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					$scope.list1 = data;
				});
				$("#matchDetailDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : false, // 不显示标题
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#matchDetailDiv'),
				});

				// /$state.go("oauth.org.user",
				// {page:"resetpw",orgid:$scope.orgid,pid:userid});
			};
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; // 取消行点击变色
			config.ajax.url = HRY.modules.exchange + 'entrust/exentrust/list?type=' + type;
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
				"data" : "userName"
			}, {
				"data" : "surname"
			},{
				"data" : "trueName"
			}, {
				"data" : "entrustTime"
			}/*
				 * , { "data" : "trueName" }
				 */, {
				"data" : "coinCode"
			}, {
				"data" : "fixPriceCoinCode"
			}, {
				"data" : "type"
			}, {
				"data" : "entrustWay"
			}, {
				"data" : "entrustPrice"
			}, {
				"data" : "entrustCount"
			}, {
				"data" : "entrustSum"
			}, {
				"data" : "surplusEntrustCount"
			}, {
				"data" : "processedPrice"
			}, {
				"data" : "transactionSum"
			}, {
				"data" : "entrustNum"
			}, {
				"data" : "status"
			}, {
				"data" : "source"
			}, {
				"data" : "status"
			}, {
				"data" : "customerIp"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}, {
				"targets" : 7,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "买入";
					} else {
						return "卖出";
					}
				}
			}, {
				"targets" : 8,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "限价";
					} else {
						return "市价";
					}
				}
			}, {
				"targets" : 9,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						return "--";
					}
				}
			}, {
				"targets" : 10,
				"render" : function(data, type, row) {
					if (row.entrustWay == "1") {
						return data;
					} else { // 限价
						if (row.type == "1") { // 买
							return "--";
						} else { // 卖
							return data;
						}
						return "卖出";
					}
				}
			}, {
				"targets" : 11,
				"render" : function(data, type, row) {
					if (row.entrustWay == "2") {
						if (row.type == "1") { // 买
							return data;
						} else { // 卖
							return "--";
						}
					} else { // 限价
						return data;
					}
				}
			},

			{
				"targets" : 16,
				//		
				"render" : function(data, type, row) {
					if (data == "0") {
						return "全部待成交";
					} else if (data == "1") {
						return "部分待成交";
					} else if (data == "2") {
						return "已全部成交";
					} else if (data == "3") {
						return "部分成交已撤销";
					} else if (data == "4") {
						return "已全部撤销";
					}
				}
			}, {

				"targets" : 17,
				"render" : function(data, type, row) {
					if (data == 1) {
						return "人工"
					} else if (data == 2) {
						return "机器人"
					} else {
						return "--"
					}
				}

			}, {
				"targets" : 18,
				"render" : function(data, type, row) {
					if (data == "0" || data == "4") {
						return "--";
					} else {

						return "<input type=\"button\" entrustid=" + row.entrustNum + " coinCode=" + row.coinCode + " currencyType=" + row.currencyType + " class=\"btn btn-small\" value=\"明细\" />"

						// return "<a ng-click='detail("+row.id+")'>明细</a>";
					}
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"部分成交已撤销委托");
			}
			
			$scope.fnList = fnList;// 刷新方法

		}
		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});