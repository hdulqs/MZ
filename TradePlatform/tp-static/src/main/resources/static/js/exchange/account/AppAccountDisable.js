 /**
 * Copyright:   北京互融时代软件有限公司
 * AppAccountDisable.js
 * @author:      Gao mimi
 * @version:     V1.0 
 * @Date:        2016-12-12 19:39:38 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {}

		/**
		 * ------------------------修改页面路径---------------------------------------------
		 */
		if ($stateParams.page == "modify") {}

		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'account/appaccountdisable/list';
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
			
			config.columns = [ 
			{
				"data" : "id"   //userName
			},{
				"data" : "userName"   //userName
			},{
				"data" : "trueName"   //trueName
			},
			{
				"data" : "coinCode"   //币种（CNY，BTC）
			},
			{
				"data" : "transactionCount"   //禁用量
			},
			{
				"data" : "status"   //1禁用，2，解禁
			},
			{
				"data" : "remark"   //remark
			},
			{
				"data" : "created"   //remark
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			},
			{
				"targets" :5,"orderable" :false,
					
				"render" : function(data, type, row) {
					
					if(data=="1"){
						return "禁用";
					}else if(data=="2"){
						
						return "解禁"
					}
					
				},
			}
		  ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			/*$scope.fnList = function(){
				table.DataTable().draw();
			}
			fnList=$scope.fnList;*/
			 $scope.fnList=fnList;//刷新方法
	            
	            //刷新按钮
	            function fnList(){
	            	 table.DataTable().draw();
	            }
			/**
			 * 修改按钮
			 */
			$scope.fnModify = function(){}
			
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
						url : HRY.modules.web + "account/appaccountdisable/remove/"+ ids
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
			 * 币解禁
			 */
			$scope.fnEncoindisable = function(){
				$scope.exaccount = {};
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var ids=ids[0];
					
					layer.confirm("你确定解禁吗？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {

						layer.closeAll();
							$http({
								method : 'POST',
								url : HRY.modules.exchange + 'account/appaccountdisable/encoindisable/'+ids,
								data : $.param({}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if (data.success) {
									growl.addInfoMessage('解禁成功')
									fnList();
								} else {
									growl.addInfoMessage(data.msg)
								}
							});
							

					});
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