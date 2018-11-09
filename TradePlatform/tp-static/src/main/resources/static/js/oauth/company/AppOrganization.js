/**
 * AppOrganziation.js
 */
define([ 'app', 'hryTable', 'ztree' ], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', "$state" ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {

		$rootScope.headTitle = $rootScope.title = "用户";

		/**
		 * 二级添加页面
		 */
		if ($stateParams.page == "add") {
			var orgId = $stateParams.id;
			// ------------------------添加页面初始化start-----------------
			$http.get(HRY.modules.oauth + 'company/apporganization/see/' + $stateParams.id).success(function(data) {
				$scope.model = data;// 传值到页面

				$("#pid").val(data.id);
				$("#pidValue").val(data.name);

				// ------------------加载部门级别树\
				loadTree();
				function loadTree() {
					var zTree;
					var setting = {
						view : {
							dblClickExpand : false,
							showLine : true,
							selectedMulti : false
						},
						data : {
							simpleData : {
								enable : true,
								idKey : "id",
								pIdKey : "pId",
								rootPId : ""
							}
						},
						callback : {
							beforeDblClick : function(treeId, treeNode) {
								$("#pidValue").val(treeNode.name);
								if (treeNode.id == -1) {
									$("#pid").val(0);
								} else {
									$("#pid").val(treeNode.id);
								}
								$("#treediv").slideToggle("fast");
							}
						}
					};

					var zNodes = [];

					$.post(HRY.modules.oauth + "company/apporganization/loadTree.do", null, function(data) {
						for (var i = 0; i < data.length; i++) {
							var item = {
								id : data[i].id,
								pId : data[i].pid,
								name : data[i].name,
								open : true
							// drop : false
							}
							zNodes.push(item);
						}
						// 初始化菜单树
						$.fn.zTree.init($("#tree"), setting, zNodes);
						$("#treediv").hide();
						// 细节处理组织树
						$("#treediv").removeClass("hide");
					}, "json");
				}

				// 弹出树的事件
				$("#pidValue").on("click", function() {
					$("#treediv").slideToggle("fast");
				});

			});
			// ------------------------添加页面初始化end-----------------

			$scope.formData = {};
			$scope.formData.orderNo = 1

			$scope.processForm = function() {
				$scope.formData.pid = $("#pid").val();
				$scope.formData.type = $("#type").val();

				if ($scope.formData.pid == null) {
					growl.addInfoMessage('上级部门不能为空')
				}
				if ($scope.formData.type == null) {
					growl.addInfoMessage('组织类型不能为空')
				}

				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'company/apporganization/add',
					data : $.param($scope.formData),
					// params : {pid:pid},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {

						$state.go("oauth.org.user", {
							page : "list",
							orgid : orgId,
							pid : "anon"
						}, {
							reload : true
						});

						growl.addInfoMessage('添加成功')
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};
		}

		/**
		 * 查看页面
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.oauth + "/user/appuser/see.do?id=" + $stateParams.id).success(function(data) {
				$scope.model = data;
			});

		}

		/**
		 * 二级添加页面
		 */
		if ($stateParams.page == "modify") {
			var orgId = $stateParams.id;
			$http.get(HRY.modules.oauth + 'company/apporganization/see/' + $stateParams.id).success(function(data) {
				$scope.formData = data;
				hryCore.RenderSelect($("#type"), $scope.formData.type);
				// ------------------加载部门级别树\
				loadTree();
				function loadTree() {
					var zTree;
					var setting = {
						view : {
							dblClickExpand : false,
							showLine : true,
							selectedMulti : false
						},
						data : {
							simpleData : {
								enable : true,
								idKey : "id",
								pIdKey : "pId",
								rootPId : ""
							}
						},
						callback : {
							beforeDblClick : function(treeId, treeNode) {

								// 递归查叶子节点
								function findParent(startnode, echoId) {
									if (startnode.id == echoId) {
										return true;
									}
									for (var i = 0; i < zNodes.length; i++) {
										var aa = startnode.name;
										var bb = zNodes[i].name;
										if (startnode.pId == zNodes[i].id) {
											if (zNodes[i].id == echoId) {
												return true;
											}
											return findParent(zNodes[i], echoId);
										}
									}

									return false;
								}

								// treeNode.id==$scope.formData.id
								if (findParent(treeNode, $scope.formData.id)) {
									// 提示信息
									layer.msg('不能选择当前节点及字节点', {
										icon : 1,
										time : 2000
									// 2秒关闭（如果不配置，默认是3秒）
									});
									return false;
								}
								$("#pidValue").val(treeNode.name);
								if (treeNode.id == -1) {
									$("#pid").val(0);
								} else {
									$("#pid").val(treeNode.id);
								}
								$("#treediv").slideToggle("fast");
							}
						}
					};

					// 节点集合
					var zNodes = [];

					$.post(HRY.modules.oauth + "company/apporganization/loadTree.do", null, function(data) {
						for (var i = 0; i < data.length; i++) {
							var item = {
								id : data[i].id,
								pId : data[i].pid,
								name : data[i].name,
								open : true
							// drop : false
							}
							zNodes.push(item);

							// 数据回显
							if (data[i].id == $scope.formData.pid) {
								$("#pidValue").val(data[i].name);
								$("#pid").val(data[i].id);
							}
						}
						// 初始化菜单树
						$.fn.zTree.init($("#tree"), setting, zNodes);
						$("#treediv").hide();
						// 细节处理组织树
						$("#treediv").removeClass("hide");
					}, "json");
				}
				// 弹出树的事件
				$("#pidValue").on("click", function() {
					$("#treediv").slideToggle("fast");
				});

			});

			$scope.processForm = function() {

				$scope.formData.pid = $("#pid").val();
				$scope.formData.type = $("#type").val();

				if ($scope.formData.pid == null) {
					growl.addInfoMessage('上级部门不能为空')
				}
				if ($scope.formData.type == null) {
					growl.addInfoMessage('组织类型不能为空')
				}
				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'company/apporganization/modify',
					data : $.param($scope.formData),
					// params : {pid:pid},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {

						$state.go("oauth.org.user", {
							page : "list",
							orgid : orgId,
							pid : "anon"
						}, {
							reload : true
						});
						growl.addInfoMessage('修改成功')
					} else {
						$scope.message = data.msg;
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