/**
 * AppUser.js
 */
define([ 'app' ,'hryTable'], function(app,DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {

		$rootScope.headTitle = $rootScope.title = "用户";
		var table = $("#dataTable");

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {

			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'schedule/scheduleJob/list';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);

				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			};
			config.columns = [ {
				"data" : "jobId"
			}, {
				"data" : "description"
			}, {
				"data" : "jobName"
			}, {
				"data" : "springId"
			}, {
				"data" : "beanClass"
			}, {
				"data" : "methodName"
			}, {
				"data" : "scheduleTime"
			}, {
				"data" : "methodArgs"
			} ]
			config.columnDefs = [ {
				"targets" : 0,"orderable" :false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}/*, {
				"targets" : 2,
				"orderable" : false,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "已处理"
					} else {
						return "<font color='red'>未收到</font>"
					}
				}
			}, {
				"targets" : 3,
				"orderable" : false,
				"render" : function(data, type, row) {
					if (data == "1") {
						return "已发送"
					} else {
						return "<font color='red'>未发送</font>"
					}
				}
			}*/ ]
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
			function fnAdd() {
				$state.go("oauth_company_apporganization.appuser", {
					spath : "oauth/user/appuser",
					spage : "add",
					sid : "anon"
				});
			}

			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnSee() {

				// 	
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					// { relative: "oauth_company_apporganization", inherit:
					// false } --- 留着备用 go的第三个参数
					$state.go("oauth_company_apporganization.appuser", {
						spath : "oauth/user/appuser",
						spage : 'see',
						sid : ids
					});
				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnModify() {
			}

			// 删除按钮
			// ng-click="fnRemove(url,selectes)"
			function fnRemove(url) {
			}

		}

		// 加载插件
		hryCore.initPlugins();
	}
	// ----------------------------

	return {
		controller : controller
	};
});