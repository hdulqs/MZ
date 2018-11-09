/**
 * Copyright: 北京互融时代软件有限公司 AppQuartzJob.js
 * 
 * @author: liushilei
 * @version: V1.0
 * @Date: 2016-11-10 20:31:53
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {

		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.web + "quartz/appquartzjob/see/" + $stateParams.id).success(function(data) {
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
					url : HRY.modules.web + 'quartz/appquartzjob/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/web/quartz/appquartzjob/list/anon';
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

			$http.get(HRY.modules.web + "quartz/appquartzjob/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.web + 'quartz/appquartzjob/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/web/quartz/appquartzjob/list/anon';
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
			config.ajax.url = HRY.modules.web + 'quartz/appquartzjob/list';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id" // id
			}, {
				"data" : "name" // 定时器名称
			}, {
				"data" : "beanClass" // 类名
			}, {
				"data" : "methodName" // 方法名
			}, {
				"data" : "quarzTime" // 定时时间
			}, {
				"data" : "remark" // 备注
			}, {
				"data" : "start" // 是否启动
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			} ,{
				"targets" : 6,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data==0){
						return "已停止";
					}
					return "工作中...";
				},
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);

			/**
			 * 刷新按钮
			 */
			$scope.fnList = function() {
				table.DataTable().draw();
			}

			/**
			 * 查看按钮
			 */
			$scope.fnSee = function() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = "#/web/quartz/appquartzjob/see/" + ids[0];
				}
			}

			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function() {
				window.location.href = "#/web/quartz/appquartzjob/add/anon";
			}

			/**
			 * 修改按钮
			 */
			$scope.fnModify = function() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = '#/web/quartz/appquartzjob/modify/' + ids[0];
				}
			}

			/**
			 * 启动按钮
			 */
			$scope.fnStart = function() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					
					layer.confirm("你确定启动？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						
						layer.closeAll();
						
						$http({
							method : 'POST',
							url : HRY.modules.web + 'quartz/appquartzjob/start',
							data : $.param({
								id : ids[0]
							}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('启动成功')
								$scope.fnList();
							} else {
								growl.addInfoMessage(data.msg)
							}
						});
						
					});
				

				}
			}

			/**
			 * 停止按钮
			 */
			$scope.fnStop = function() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					layer.confirm("你确定停止？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {
						
						layer.closeAll();
						
						$http({
							method : 'POST',
							url : HRY.modules.web + 'quartz/appquartzjob/stop',
							data : $.param({
								id : ids[0]
							}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('停止成功')
								$scope.fnList();
							} else {
								growl.addInfoMessage(data.msg)
							}
						});
						
					});
					
				}
			}

			/**
			 * 删除按钮
			 */
			$scope.fnRemove = function() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {

					layer.confirm("你确定删除？", {
						btn : [ '确定', '取消' ], // 按钮
						ids : ids
					}, function() {

						layer.closeAll();

						hryCore.CURD({
							url : HRY.modules.web + "quartz/appquartzjob/remove/" + ids
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