/**
 * student.js
 */
define([ 'app', 'hryTable' ], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore) {

		var table = $("#table2");

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {

			// ----------------------------------------------------------------
			$scope.fnAdd = fnAdd;// add按钮方法
			$scope.fnSee = fnSee;// see按钮方法
			$scope.fnModify = fnModify;// see按钮方法
			$scope.fnRemove = fnRemove;// remove方法
			$scope.fnList = fnList;// 刷新方法

			// ----------------------------------------------------------------

			$scope.serchData = {};

			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + '/message/appmessage/messageList/2';
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
				"data" : "categoryName"
			}, {
				"data" : "title"
			}, {
				"data" : "operator"
			}, {
				"data" : "isSend"
			}, {
				"data" : "created"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,
				"orderable" : false,
				"render" : function(data, type, row) {
					// return data;
					if (data == 0) {
						return "未发送";
					}
					if (data == 1) {
						return "已发送";
					} else {
						return data
					}
				}
			}

			]
			DT.draw(table, config);

			$scope.fnSend = function() {

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else {
					$http.get(HRY.modules.web + "/message/appmessage/send?ids[]=" + ids).success(function(data) {
						if (data.success) {
							growl.addInfoMessage(data.msg)
							window.location.href = '#/web/message/appmessage/list/anon';
						} else {
							growl.addInfoMessage('发送失败')
						}
					});
				}
			}
		}

		// ------------------------增加页面路径------------------------------------------------------------------------------------------------------
		// ------------------------增加页面路径------------------------------------------------------------------------------------------------------

		if ($stateParams.page == "add") {

			$scope.formData = {};
			
			$("#reset").click(function(){
				$("#title").val("");
				$("#sortTitle").val("");
				$("#sortTitle").val("");
				var content = UE.getEditor('contentText');
				content.setContent('') ;
			})

			$scope.submit = function() {
				// 是否全部发送 true 是 false 否
				var flagAll = true;
				// 表单校验
				// 1、判断用户名是否输入正确
				window.id = this.formData.userNames;
				var mes = "确定保存该消息?"
				if (id == "" || id == undefined) {
					mes = "注意：接收人用户名为空，默认发送消息给所有人"
				} else {
					flagAll = false;
					id = id.trim()
					// 校验是否通过
					window.flag = true;
					$.ajax({
						type : 'post',
						url : HRY.modules.web + "/user/appcustomer/list",
						async : false,
						dataType : 'json',
						data : {
							start : 0,
							length : 9999,
							userName_in : id
						},
						success : function(data) {
							var arr = id.split(",");
							window.errUserName = '';
							if (data.recordsTotal != 0 && data.recordsTotal != arr.length) {
								// 获取list中的所有userName
								var list = [];
								for (var i = 0; i < data.rows.length; i++) {
									list.push(data.rows[i].userName);
								}
								for (var i = 0; i < arr.length; i++) {
									if ($.inArray(arr[i], list) == -1) {
										window.errUserName = window.errUserName + arr[i] + ",";
									}
									if (arr.length - 1 == i) {
										window.errUserName = window.errUserName.substr(0, window.errUserName.length - 1);
									}
								}
								layer.open({
									title : '提示',
									content : "[" + window.errUserName + "]:以上用户名错误请检查",
									icon : 2
								});

								flag = false;
							} else if (data.recordsTotal == 0) {
								layer.open({
									title : '提示',
									content : "[" + id + "]:以上用户名错误，请检查",
									icon : 2
								});
								flag = false;
							}
						}
					})

					if (!flag) {// 如果校验失败，停止代码执行；
						return false;
					}
				}

				layer.confirm(mes, {
					btn : [ '确定', '取消' ]
				// 按钮
				}, function() {

					layer.closeAll();
					var image = $("#contentImage").val();
					$scope.formData.titleImage = image;

					if (flagAll) {
						$scope.formData.isAll = 1
						$scope.formData.receiveUserIdList = [ 0 ];
					} else {
						$scope.formData.isAll = 0;
						$scope.formData.receiveUserIdList = window.id.split(",");
					}
					$scope.formData.categoryName = $("#companySet").find("option:selected").text();
					$scope.formData.categoryId = $("#companySet").val();
					$http({
						method : 'POST',
						url : HRY.modules.web + 'message/appmessage/add/2',
						data : $.param($scope.formData),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							growl.addInfoMessage(data.msg)
							window.location.href = '#/web/message/appmessage/see/anon';
						} else {
							growl.addInfoMessage(data.msg)
						}

					});
				});

			};

			// ------------------------------------------------------------------------------

			$scope.sendSubmit = function() {
				var names = $scope.formData.userNames;
				if (names != "" && names != undefined) {
					names = names.trim()
					// 校验是否通过
					window.flag = true;
					$.ajax({
						type : 'post',
						url : HRY.modules.web + "/user/appcustomer/list",
						async : false,
						dataType : 'json',
						data : {
							start : 0,
							length : 9999,
							userName_in : names
						},
						success : function(data) {
							var arr = names.split(",");
							window.errUserName = '';
							if (data.recordsTotal != 0 && data.recordsTotal != arr.length) {
								// 获取list中的所有userName
								var list = [];
								for (var i = 0; i < data.rows.length; i++) {
									list.push(data.rows[i].userName);
								}
								for (var i = 0; i < arr.length; i++) {
									if ($.inArray(arr[i], list) == -1) {
										window.errUserName = window.errUserName + arr[i] + ",";
									}
									if (arr.length - 1 == i) {
										window.errUserName = window.errUserName.substr(0, window.errUserName.length - 1);
									}
								}
								layer.open({
									title : '提示',
									content : "[" + window.errUserName + "]:以上用户名错误请检查",
									icon : 2
								});

								flag = false;
							} else if (data.recordsTotal == 0) {
								layer.open({
									title : '提示',
									content : "[" + names + "]:以上用户名错误，请检查",
									icon : 2
								});
								flag = false;
							}
						}
					})

					if (!flag) {// 如果校验失败，停止代码执行；
						return false;
					}
				}

				layer.confirm("确定发送该消息吗？", {
					btn : [ '确定', '取消' ]
				// 按钮
				// ids: ids
				}, function() {
					layer.closeAll();

					var s = $scope.formData.titleImage
					if (null == names || "" == names || undefined == names) {
						$scope.formData.isAll = 1;
						$scope.formData.receiveUserIdList = [ 0 ];
					} else {
						$scope.formData.isAll = 0;
						names = names.trim();
						names.split(",");
						$scope.formData.receiveUserIdList = names.split(",");
					}

					$scope.formData.categoryId = $("#companySet").val();
					$scope.formData.categoryName = $("#companySet").find("option:selected").text();
					$http({
						method : 'POST',
						url : HRY.modules.web + 'message/appmessage/add/1',
						data : $.param($scope.formData),
						// params:{'appResourceStr':ids},
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							growl.addInfoMessage(data.msg)
							window.location.href = '#/web/message/appmessage/see/anon';
						} else {
							growl.addInfoMessage(data.msg)
						}
					});
				})
			};
		}

		// ------------------------修改页面路径-----------------------------------------------------------------------------------------------
		// ------------------------修改页面路径-----------------------------------------------------------------------------------------------

		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.web + "/message/appmessage/see/" + $stateParams.id).success(function(data) {

				delete data["messageAsCustomer"];
				$scope.formData = data;
			});

			$scope.submit = function() {

				var mes = "确定修改这些参数"

				layer.confirm(mes, {
					btn : [ '确定', '取消' ]
				// 按钮
				// ids: ids
				}, function() {

					layer.closeAll();

					$scope.formData.receiveUserIdList = [ 0 ];

					$scope.formData.categoryId = $("#companySet").val();

					var image = $("#contentImage").val();

					$scope.formData.titleImage = image;

					if ($scope.formData.isAll != 0 && null != $("#receiveUserNames").val()) {
						$scope.formData.receiveUserIdList = $("#receiveUserNames").val();
						$scope.formData.isAll = 0;
					}

					$scope.formData.categoryName = $("#companySet").find("option:selected").text();

					$http({
						method : 'POST',
						url : HRY.modules.web + '/message/appmessage/modify/2',
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
							window.location.href = '#web/message/appmessage/see/anon';
						}
					});
				})
			};

			// ==========================================================================================================================================================

			$scope.sendSubmit = function() {
				var s = $scope.formData.titleImage

				$scope.formData.isAll = 0
				$scope.formData.categoryId = $("#companySet").val();
				$scope.formData.receiveUserIdList = $("#receiveUserNames").val();
				$scope.formData.categoryName = $("#companySet").find("option:selected").text();
				$http({
					method : 'POST',
					url : HRY.modules.web + 'message/appmessage/modify/1',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage(data.msg)
						window.location.href = '#/web/message/appmessage/list/1';
					} else {
						growl.addInfoMessage('添加失败')
					}

				});

			};
		}

		// ------------------------列表页面路径--------------------------------------------------------------------------------------------------------
		// ------------------------列表页面路径-------------------------------------------------------------------------------------------------------

		if ($stateParams.page == "list") {
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.iDisplayLength = 10;
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + '/message/appmessage/listMessageVo';
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
				"data" : "categoryName"
			}, {
				"data" : "messageTitle"
			}, {
				"data" : "sendDate"
			}, {
				"data" : "sendUserName"
			}, {
				"data" : "list"
			}, {
				"data" : "operator"
			} ]

			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 5,
				"orderable" : false,
				"render" : function(data, type, row) {
					var u = "";
					if (data.length > 0) {
						for (var i = 0; i < data.length; i++) {
							u = u + "," + data[i].customerName
							if (i > 3) {
								u = u + "..."
								break;
							}
						}
						u = u.substr(1);
					}
					return "<font color='blue'>" + u + "</font>";
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
				window.location.href = '#/web/' + url + '/anon';
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
					window.location.href = '#/web/' + url + "/" + $rootScope.id;
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

				mes = "确定删除这些数据吗？"

				layer.confirm(mes, {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();

					hryCore.CURD({
						url : HRY.modules.web + url + "/" + ids
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
				})
			}
		}

		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});