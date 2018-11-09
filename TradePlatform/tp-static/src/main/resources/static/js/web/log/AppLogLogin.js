/**
 * AppUser.js
 */
define([ 'app','hryTable' ], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore) {

		$rootScope.headTitle = $rootScope.title = "登录日志";

		var table = $("#dataTable");

		$scope.hryCore = hryCore;

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			hryCore.CURD({
				url : HRY.modules.oauth + "user/appuser/see/" + $stateParams.id
			}).get(null, function(data) {
				$scope.formData = data;
				hryCore.RenderHTML(data);// 异步回显控件
			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}

		/**
		 * 添加页面
		 */
		if ($stateParams.page == "add") {

			$scope.formData = {};
			$scope.processForm = function() {
				// 所属公司
				$scope.formData.companySet = $("#companySet").val();
				// 所属门店
				$scope.formData.shopSet = $("#shopSet").val();
				// 所属部门
				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
				// 拥有角色
				$scope.formData.appRoleSet = hryCore.ArrayToString($("#appRoleSet").val());
				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'user/appuser/add.do',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/oauth/user/appuser/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};
		}

		/**
		 * 修改页面
		 */
		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.oauth + "user/appuser/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				hryCore.RenderHTML(data);// 异步回显控件
			});

			$scope.processForm = function() {

				// 图片路径
				$scope.formData.picturePath = $("#imgSrc").val();
				// 所属公司
				$scope.formData.companySet = $("#companySet").val();
				// 所属门店
				$scope.formData.shopSet = $("#shopSet").val();
				// 所属部门
				$scope.formData.departmentSet = hryCore.ArrayToString($("#departmentSet").val());
				// 拥有角色
				$scope.formData.appRoleSet = hryCore.ArrayToString($("#appRoleSet").val());

				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'user/appuser/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/oauth/user/appuser/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}
		 
		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {

			$scope.serchData = {};
			//前台显示的时候注销了
			//$scope.serchData.type_EQ="AppUser";
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'log/apploglogin/list';
			config.ajax.data = function(d) {
				//设置select下拉框
				DT.selectData($scope.serchData);
				
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			};
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName"
			}, {
				"data" : "loginTime"
			}, {
				"data" : "ip"
			}, {
				"data" : "status"
			} ]
			config.columnDefs = [ {
				"targets" : 0,"orderable" :false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data=="1"){
						return "登录成功";
					}else{
						return "<font color='red'>登录失败</font>";
					}
				}
			} 
			
			]
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
				window.location.href = '#/oauth/' + url + '/anon';
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
					window.location.href = '#/oauth/' + url + '/' + ids[0];
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
					window.location.href = '#/oauth/' + url + '/' + ids[0];
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

				var id = "";
				for (var i = 0; i < ids.length; i++) {
					id += ids[i];
					if (i != ids.length - 1) {
						id += ",";
					}
				}

				hryCore.CURD({
					url : HRY.modules.oauth + url + "/" + id
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

			}

		}
		
		// ------------------------前台账户登录日志列表页面路径---------------------------------------------
		if ($stateParams.page == "customerlist") {

			$scope.serchData = {};
			$scope.serchData.type_EQ="AppCustomer";
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'log/apploglogin/list';
			config.ajax.data = function(d) {
				//设置select下拉框
				DT.selectData($scope.serchData);
				
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			};
			config.columns = [ {
				"data" : "id"
			},  {
				"data" : "userName"
			}, {
				"data" : "loginTime"
			}, {
				"data" : "ip"
			}, {
				"data" : "status"
			} ]
			config.columnDefs = [ {
				"targets" : 0,"orderable" :false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data=="1"){
						return "登录成功";
					}else{
						return "<font color='red'>登录失败</font>";
					}
				}
			} 
			
			]
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
				window.location.href = '#/oauth/' + url + '/anon';
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
					window.location.href = '#/oauth/' + url + '/' + ids[0];
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
					window.location.href = '#/oauth/' + url + '/' + ids[0];
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

				var id = "";
				for (var i = 0; i < ids.length; i++) {
					id += ids[i];
					if (i != ids.length - 1) {
						id += ",";
					}
				}

				hryCore.CURD({
					url : HRY.modules.oauth + url + "/" + id
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

			}

		}
		
		// 加载插件
		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();
	}
	// ----------------------------

	return {
		controller : controller
	};
});