/**
 * approle.js
 */
define([ 'app','hryTable' ,'hrytree'], function(app,DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams','$state','hrytree' ];
	function controller($scope, $rootScope, $http, $stateParams,$state, hryCore,hrytree ) {
		$rootScope.headTitle = $rootScope.title = "角色管理";
		// 初始化js 插件

		var table = $("#table2");
		var roleId = $stateParams.id;
		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			hryCore.CURD({
				url : HRY.modules.oauth + "user/approle/see/" + $stateParams.id
			}).get(null, function(data) {
				$scope.model = data;
				// 渲染权限树
				
				// -----------加载树------------
				$http.get(HRY.modules.web + 'menu/appmenutree/loadapp').success(function(data) {
					$rootScope.applicactions = data;
					// 循化创建树
					$rootScope.applicactions.forEach(function(data) {
						loadTree(data);
					})
				});

				// 创建树的方法
				function loadTree(rootMenu) {
					var zNodes = [];
					$.post(HRY.modules.web + "menu/appmenutree/findRoleByApp", {
						roleId : roleId,
						appName : rootMenu.appName,
					}, function(data) {
						for (var i = 0; i < data.length; i++) {
							var item = {
								id : data[i].id,
								mkey : data[i].mkey,
								pkey : data[i].pkey,
								name : data[i].name,
								type : data[i].type,
								appName :data[i].appName,
								open : true
							}
							zNodes.push(item);
						}
						// 初始化菜单树
						var conf = hrytree.config();	
						conf.setting.edit={};
						conf.setting.callback={};
						conf.setting.view={};
				    	hrytree.init(rootMenu.mkey + "Tree",conf,zNodes);
						// 渲染页签
						$('ul.tabs').tabs();
					}, "json");

				}
				
				
			}, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}

		// ------------------------增加页面路径---------------------------------------------
		if ($stateParams.page == "add") {
			
			$scope.formData = {};
			$scope.formData.menuTreeIds = ""
			$scope.processForm = function() {
				// 附值权限id
				var menuTreeIds = "";
				$rootScope.applicactions.forEach(function(data) {
					var checkNodes = hrytree.getTreeNodes(data.mkey + "Tree");
					if (checkNodes != undefined && checkNodes.length > 0) {
						if (menuTreeIds.length > 0) {
							menuTreeIds += ","
						}
						for (var i = 0; i < checkNodes.length; i++) {
							menuTreeIds += checkNodes[i].id
							if (i != checkNodes.length - 1) {
								menuTreeIds += ","
							}
						}
					}
				})
				$scope.formData.menuTreeIds = menuTreeIds;
				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'user/approle/add',
					data : $.param($scope.formData),
					// params:{'appResourceStr':ids},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/oauth/user/approle/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}

				});
			};

			// -----------加载树------------
			$http.get(HRY.modules.web + 'menu/appmenutree/loadapp').success(function(data) {
				$rootScope.applicactions = data;
				// 循化创建树
				$rootScope.applicactions.forEach(function(data) {
					loadTree1(data);
				})
			});

			// 创建树的方法
			function loadTree1(rootMenu) {
				var zNodes = [];
				$.post(HRY.modules.web + "menu/appmenutree/findByApp", {
					id : rootMenu.id
				}, function(data) {
					for (var i = 0; i < data.length; i++) {
						var item = {
							id : data[i].id,
							mkey : data[i].mkey,
							pkey : data[i].pkey,
							name : data[i].name,
							type : data[i].type,
							appName :data[i].appName,
							open : true
						}
						zNodes.push(item);
					}
					// 初始化菜单树
					var conf = hrytree.config();
					conf.setting.edit={};
					conf.setting.view={};
					conf.setting.callback={};
					conf.setting.check={
								autoCheckTrigger : false,
								chkboxType : {
									"Y" : "ps",
									"N" : "s"
								},
								chkStyle : "checkbox",
								enable : true,
								nocheckInherit : false,
								chkDisabledInherit : false,
								radioType : "level"
					}
					//初始化树
			    	hrytree.init(rootMenu.mkey + "Tree",conf,zNodes);
				}, "json");

			}

			// 选择权限框
			$scope.selectResource = function() {
				// 弹窗
				$("#treeDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : "请勾选权限",
					closeBtn : 2,
					area : [ '80%', '80%' ],
					shadeClose : true,
					content : $('#treeDiv')
				});
				// 渲染页签
				$('ul.tabs').tabs();
			}
		}

		// ------------------------修改页面路径---------------------------------------------
		if ($stateParams.page == "modify") {
			$http.get(HRY.modules.oauth + "user/approle/see/" + $stateParams.id).success(function(data) {
				$scope.model = data;
				$scope.formData = data;

			});

			// $scope.formData = {};
			$scope.processForm = function() {
				// 附值权限id
				var menuTreeIds = "";
				$rootScope.applicactions.forEach(function(data) {
					var checkNodes = hrytree.getTreeNodes(data.mkey + "Tree");
					if (checkNodes != undefined && checkNodes.length > 0) {
						if (menuTreeIds.length > 0) {
							menuTreeIds += ","
						}
						for (var i = 0; i < checkNodes.length; i++) {
							menuTreeIds += checkNodes[i].id
							if (i != checkNodes.length - 1) {
								menuTreeIds += ","
							}
						}
					}
				})
				$scope.formData.menuTreeIds = menuTreeIds;
				
				$http({
					method : 'POST',
					url : HRY.modules.oauth + 'user/approle/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					console.log(data);
					if (!data.success) {
						growl.addInfoMessage(data.msg)
					} else {
						growl.addInfoMessage('保存成功')
						window.location.href = '#/oauth/user/approle/list/anon';
					}

				});

			};

			
			// -----------加载树------------
			$http.get(HRY.modules.web + 'menu/appmenutree/loadapp').success(function(data) {
				$rootScope.applicactions = data;
				// 循化创建树
				$rootScope.applicactions.forEach(function(data) {
					loadTree(data);
				})
			});

			// 创建树的方法
			function loadTree(rootMenu) {
				var zNodes=[];
				$.post(HRY.modules.web + "menu/appmenutree/findRoleByAppHas", {
					roleId : roleId,
					appName : rootMenu.appName,
				}, function(data) {
					for (var i = 0; i < data.all.length; i++) {
						var item
						var flag = false;

						for (var j = 0; j < data.has.length; j++) {
							if (data.all[i].mkey == data.has[j].mkey) {
								flag = true;
								break;
							}
						}
						if (flag) {
							item = {
								id : data.all[i].id,
								mkey : data.all[i].mkey,
								pkey : data.all[i].pkey,
								name : data.all[i].name,
								type : data.all[i].type,
								appName :data.all[i].appName,
								open : true,
								checked : true,
							// drop : false
							}
						} else {
							item = {
								id : data.all[i].id,
								mkey : data.all[i].mkey,
								pkey : data.all[i].pkey,
								name : data.all[i].name,
								type : data.all[i].type,
								appName :data.all[i].appName,
								open : true
							// drop : false
							}

						}
						zNodes.push(item);
					}
					
					// 初始化菜单树
					var conf = hrytree.config();
					conf.setting.edit={};
					conf.setting.view={};
					conf.setting.callback={};
					conf.setting.check={
						autoCheckTrigger : false,
						chkboxType : {
							"Y" : "ps",
							"N" : "s"
						},
						chkStyle : "checkbox",
						enable : true,
						nocheckInherit : false,
						chkDisabledInherit : false,
						radioType : "level"
					}
					//初始化树
			    	hrytree.init(rootMenu.mkey + "Tree",conf,zNodes);
				}, "json");

			}

			// 选择权限框
			$scope.selectResource = function() {
				// 弹窗
				$("#treeDiv").removeClass("hide")
				layer.open({
					type : 1,
					title : "请勾选权限",
					closeBtn : 2,
					area : [ '80%', '80%' ],
					shadeClose : true,
					content : $('#treeDiv')
				});
				// 渲染页签
				$('ul.tabs').tabs();
			}

		}

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.oauth + 'user/approle/list';
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
				"data" : "remark"
			}, {
				"data" : "created"
			}, {
				"data" : "modified"
			} ]
			config.columnDefs = [ {
				"targets" : 0,"orderable" :false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
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
				layer.confirm('你确定要删除吗？', {
	    			btn: ['确定','取消'], //按钮
	    			id: id
				},function(){
				hryCore.CURD({
					url : HRY.modules.oauth + url + "/" + id
				}).remove(null, function(data) {
					if (data.success) {
						// 提示信息
						//growl.addInfoMessage('删除成功')
						layer.msg('删除成功', {
                		    icon: 1,
                		    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                		});
						// 重新加载list
						fnList();
					} else {
						layer.msg(data.msg, {
                		    icon: 1,
                		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                		}); 
                		layer.close();
						//growl.addInfoMessage(data.msg)
					}
				}, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});

			});
		}
		}

		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});