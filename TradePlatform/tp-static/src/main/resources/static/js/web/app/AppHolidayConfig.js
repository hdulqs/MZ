/**
 * AppConfig.js
 */
define([ 'app', 'hryTable','layer', 'pikadayJq' ], function(app, DT,layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore) {

		$rootScope.headTitle = $rootScope.title = "节日配置";

		// 初始化js 插件
		var table = $("#table2");

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			hryCore.CURD({
				url : HRY.modules.customer + "/app/appHolidayConfig/see/" + $stateParams.id
			}).get(null, function(data) {
				$scope.model = data;

			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {

			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'app/appHolidayConfig/list.do';
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
				"data" : "beginDate"
			}, {
				"data" : "endDate"
			}, {
				"data" : "created"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnAdd = fnAdd;// add按钮方法
			$scope.fnSee = fnSee;// see按钮方法
			$scope.fnModify = fnModify;// see按钮方法
			$scope.fnRemove = fnRemove;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 添加按钮
			// ng-click="fnAdd(url)"
			function fnAdd(url) {

				window.location.href = '#/web/' + url + '/anon';
			}

			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnSee(url) {
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = '#/web/' + url + '/' + ids[0];
				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnModify(url) {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$rootScope.id = ids[0];
					window.location.href = '#/web/' + url + '/' + ids[0];
				}
			}

			// 删除按钮
			// ng-click="fnRemove(url,selectes)"
			function fnRemove(url) {

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				layer.confirm("你确定删除？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();

					hryCore.CURD({
						url : HRY.modules.web + "app/appHolidayConfig/remove" + "/" + ids[0]
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('删除成功')
							// 重新加载list
							fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

				});

			}

		}

		// ------------------------增加页面路径---------------------------------------------
		if ($stateParams.page == "add") {

			$scope.formData = {};

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.web + 'app/appHolidayConfig/add',
					data : $.param($scope.formData),
					// params:{'appResourceStr':ids},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/web/app/appholidayconfig/list/anon';
					} else {
						growl.addInfoMessage('添加失败--- ' + data.msg)
					}
				});

			};
		}

		// ------------------------修改页面路径---------------------------------------------
		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.web + "/app/appHolidayConfig/see/" + $stateParams.id).success(function(data) {
				$scope.model = data;
				$scope.formData = data;
			});

			// $scope.formData = {};
			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.web + 'app/appHolidayConfig/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (!data.success) {
						$scope.errorName = data.msg;
					} else {
						$scope.message = data.msg;
						window.location.href = '#/web/app/appholidayconfig/list/anon';
					}
				});
			};
		}

		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});