/**
 * student.js
 */
define([ 'app', 'hryTable' ,'hryUtil'], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$location' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore, $location) {
		// 初始化js 插件

		var table = $("#table2");

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.exchange + "transaction/exdmcustomerpublickey/see/" + $stateParams.id).success(function(data) {
				alert(data)

			});
		}

		// ------------------------增加页面路径---------------------------------------------
		if ($stateParams.page == "add") {
			$scope.formData = {};
			$scope.processForm = function() {

				// 附值权限id
				$scope.formData.appResourceSet = $("#appResourceSet").val();
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
						growl.addInfoMessage('添加失败')
					}

				});

			};
		}

		// ------------------------修改页面路径---------------------------------------------
		if ($stateParams.page == "modify") {
			$http.get(HRY.modules.oauth + "user/approle/see/" + $stateParams.id).success(function(data) {
				$scope.model = data;
				$scope.formData = data;

				// 渲染ztree树
				var zTree;
				var setting = {
					view : {
						dblClickExpand : false,
						showLine : true,
						selectedMulti : false
					},
					check : {
						autoCheckTrigger : false,
						chkboxType : {
							"Y" : "ps",
							"N" : "ps"
						},
						chkStyle : "checkbox",
						enable : true,
						nocheckInherit : false,
						chkDisabledInherit : false,
						radioType : "level"

					},
					data : {
						simpleData : {
							enable : true,
							id : "id",
							idKey : "mkey",
							pIdKey : "pkey",
							rootPId : ""
						}
					},
					callback : {
						beforeClick : function(treeId, treeNode) {
							// alert(treeId);
						}
					}
				};

				var zNodes = [

				];

				loadMenuList();
				function loadMenuList() {
					$.post(HRY.modules.oauth + "user/approle/findMyResourceAndNohasResource?id=" + $stateParams.id, null, function(data) {
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
									open : true,
									checked : true
								// drop : false
								}
							} else {
								item = {
									id : data.all[i].id,
									mkey : data.all[i].mkey,
									pkey : data.all[i].pkey,
									name : data.all[i].name,
									open : true
								// drop : false
								}

							}
							zNodes.push(item);
						}
						// 初始化菜单树
						$.fn.zTree.init($("#tree"), setting, zNodes);

					}, "json");
				}

			});

			// $scope.formData = {};
			$scope.processForm = function() {
				// 删除属性--不传给controller
				$($scope.formData).removeProp("appResourceSet");
				// 附值权限id
				$scope.formData.appResourceSet = $("#appResourceSet").val();
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
						$scope.errorName = data.msg;
					} else {
						$scope.message = data.msg;
						window.location.href = '#/oauth/user/approle/list/anon';
					}

				});

			};

		}
		// 

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {

			 var id=$location.search().id;
			   //id不为空时 查询单个用户的信息
			   if(id != undefined && id!="" ){
	        		 $scope.serchData={
	        			     customerId_EQ:id
	        				// publicKeyName_EQ:id
	             	}; 
	        	 }else{
	        		 $scope.serchData={
	         				
	              	}; 
	        	 }
			// --------------------加载dataTable--------------------------------
			
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'transaction/exdmcustomerpublickey/list';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "publicKeyName"
			}, {
				"data" : "surname"
			},{
				"data" : "trueName"
			}, {
				"data" : "currencyType"
			}, {
				"data" : "publicKey"
			}, {
				"data" : "remark"
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
			
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"提币地址管理");
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
					window.location.href = '#/exchange/' + url + '/' + ids[0];
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

		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});