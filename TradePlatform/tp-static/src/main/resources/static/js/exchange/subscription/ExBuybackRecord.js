 /**
 * Copyright:   北京互融时代软件有限公司
 * ExBuybackRecord.js
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-24 16:36:09 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.exchange + "subscription/exbuybackrecord/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				if(data!=null&&data.state=="0"){
					$scope.formData.state = "未审核";
				}else if(data!=null&&data.state=="1"){
					$scope.formData.state = "已通过";
				}else if(data!=null&&data.state=="2"){
					$scope.formData.state = "已驳回";
				}else if(data!=null&&data.state=="3"){
					$scope.formData.state = "已撤销";
				}else{
					return "";
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
					url : HRY.modules.exchange + 'subscription/exbuybackrecord/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/exchange/subscription/exbuybackrecord/list/anon';
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

			$http.get(HRY.modules.exchange + "subscription/exbuybackrecord/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'subscription/exbuybackrecord/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/exchange/subscription/exbuybackrecord/list/anon';
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
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'subscription/exbuybackrecord/list';
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
				"data" : "userName"   //账户名
			},
			{
				"data" : "trueName"   //真实姓名
			},
			{
				"data" : "coinCode"   //币种代码
			},
			{
				"data" : "coinName"   //币种名称
			},
			{
				"data" : "period"   //认购期数
			},
			{
				"data" : "backPrice"   //认购价格
			},
			{
				"data" : "amount"   //认购数量
			},
			{
				"data" : "backAmount"   //回购数量
			},
			{
				"data" : "buyTotalAmount"   //回购总金额
			},
			{
				"data" : "repurchaseFee"   //回购手续费
			},{
				"data" : "feeMoney"   //回购手续费金额
			},
			{
				"data" : "transactionNum"   //回购单号
			},
			{
				"data" : "subTransactionNum"   //认购单号
			},
			{
				"data" : "state"   //状态（0未审核，1已通过，2已驳回，3用户撤销）
			},
			{
				"data" : "remarks"   //驳回备注
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			},{
				"targets" : 14,
					
				"render" : function(data, type, row) {
					if(data!=null&&data=="0"){
						return "<font color='lightgrey'>未审核</font>"
					}else if(data!=null&&data=="1"){
	    				return "<font color='blue'>已通过</font>"
					}else if(data!=null&&data=="2"){
						return "<font color='red'>已驳回</font>"
					}else if(data!=null&&data=="3"){
						return "<font color='gainsboro'>已撤销</font>"
					}else{
						return "";
					}
				}
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
					window.location.href = "#/exchange/subscription/exbuybackrecord/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/exchange/subscription/exbuybackrecord/add/anon";
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
        			window.location.href='#/exchange/subscription/exbuybackrecord/modify/'+ids[0];
            	}
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
						url : HRY.modules.web + "subscription/exbuybackrecord/remove/"+ ids
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
			/**
			 * 通过
			 */
			$scope.fnPassed = function(){
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择一条数据')
					return false;
				}
				if (arrId.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				var data = DT.getRowData(table);
				if(data[0].state==0){
					layer.confirm("确定通过回购？", {
						btn : [ '确定', '取消' ] // 按钮
					}, function() {
						layer.closeAll();
						hryCore.CURD({
							url : HRY.modules.web + "subscription/exbuybackrecord/passed/"+ arrId[0]
						}).remove(null, function(data) {
							if (data.success) {
								// 提示信息
								growl.addInfoMessage('回购已通过')
								// 重新加载list
								$scope.fnList();
							} else {
								growl.addInfoMessage(data.msg)
							}
						}, function(data) {
							growl.addInfoMessage("error:" + data.msg);
						});

					});
				}else{
					growl.addInfoMessage('已经操作过了，不能重复操作')
					return false;
				}
			}
			/**
			 * 驳回
			 */
			$scope.fnReject = function(){
				$scope.buyBack = {};
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择一条数据')
					return false;
				}
				if (arrId.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				var data = DT.getRowData(table);
				if(data[0].state==0){
					$('#rejectDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "驳回回购",
						closeBtn : 2,
						area : [ '500px', '350px' ],
						shadeClose : true,
						content : $('#rejectDiv')
					});
					$scope.fnRejectSubmit = function(){
						$http({
							method : 'POST',
							url : HRY.modules.exchange + 'subscription/exbuybackrecord/reject',
							data : $.param({id:data[0].id,remarks:$scope.buyBack.remarks}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('驳回成功')
								layer.closeAll();
								$scope.fnList();
							} else {
								growl.addInfoMessage(data.msg)
							}
						});
					}
				}else{
					growl.addInfoMessage('已经操作过了，不能重复操作')
					return false;
				}
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