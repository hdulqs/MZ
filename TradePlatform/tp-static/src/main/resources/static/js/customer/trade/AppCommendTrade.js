 /**
 * Copyright:   北京互融时代软件有限公司
 * AppCommendTrade.js
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 17:40:59 
 */
define([ 'app', 'hryTable', 'layer',"hryUtil",'pikadayJq' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "trade/appcommendtrade/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
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
					url : HRY.modules.customer + 'trade/appcommendtrade/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/trade/appcommendtrade/list/anon';
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

			$http.get(HRY.modules.customer + "trade/appcommendtrade/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.customer + 'trade/appcommendtrade/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/trade/appcommendtrade/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}

		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		debugger;
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.customer + 'trade/appcommendtrade/list';
			config.ajax.data = function(d) {
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
			/*{
				"data" : "custromerId"   //代理商id
			},*/
			{
				"data" : "custromerName"   //代理商名称
			},
			{
				"data" : "ordertNum"   //订单号
			},
			{
				"data" : "personType"   //返佣人
			},
			{
				"data" : "coinCode"   //定价币种
			},
			/*{
				"data" : "pid"   //pid
			},*/
			{
				"data" : "feeamout"   //手续费金额
			},
			{
				"data" : "basemoney"   //返佣基数
			},
			{
				"data" : "rewardmoney"   //奖励金额
			},
			{
				"data" : "hierarchy"   //sid
			},
			{
				"data" : "deliveryName"   //交款人
			},
			{
				"data" : "userMoney"   //
			},
			/*{
				"data" : "deliveryId"   //交款人id
			}*/
			{
				"data" : "transactionTime"   //交易时间
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			} ,{
                "targets" : 3,
                "orderable" : false,
                "orderable" : false,
                "render" : function(data, type, row) {
                    return data==1?'买方':'卖方'
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
            // 导出excel
            $scope.fnExcel = function() {
                DT.excel(table, this.serchData, "推荐返佣明细");
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
					window.location.href = "#/customer/trade/appcommendtrade/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/customer/trade/appcommendtrade/add/anon";
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
        			window.location.href='#/customer/trade/appcommendtrade/modify/'+ids[0];
            	}
			}
			
			/*
			 * 返佣统计按钮(新增)
			 */
		
			$scope.fnTotal = function() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				$.ajax({
					type : "POST",
					dataType : "JSON",
					url : HRY.modules.customer + "trade/appcommendtrade/total/"+ ids,
					cache : false,
					success : function(data) {
						
						$("#modifyInfo22").removeClass("hide")
						$("#custromerName").html(data.custromerName);
						$("#coinCode").html(data.coinCode);
						$("#rewardmoney").html(data.rewardmoney);
						layer.open({
							type : 1,
							title : "返佣统计",
							closeBtn : 2,
							area : [ '450px', '400px' ],
							shadeClose : true,
							content : $('#modifyInfo22')
						});
					}
				})
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
						url : HRY.modules.web + "trade/appcommendtrade/remove/"+ ids
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