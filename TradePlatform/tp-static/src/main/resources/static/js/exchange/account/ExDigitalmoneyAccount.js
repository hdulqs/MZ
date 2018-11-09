/**
 * student.js
 */
define([ 'app', 'hryTable'  ,'hryUtil' ], function(app, DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$location' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore, $location) {
		// 初始化js 插件

		var table = $("#table2");

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {

			$http.get(HRY.modules.exchange + "account/exdigitalmoneyaccount/see/" + $stateParams.id).success(function(data) {
				$scope.allTransaction = data;
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

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {
			// --------------------加载dataTable--------------------------------
			 var n=$location.search().name;
	        	//name不为空时 查询单个用户的信息
			 if(n != undefined && n!="" ){
        		 $scope.serchData={
        			 userName_like:n
              	};
        	 }else{
        		 $scope.serchData={
          				
               	}; 
         	 }
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'account/exdigitalmoneyaccount/list';
			config.ajax.data = function(d) {
				// 设置select下拉框
				DT.selectData($scope.serchData);

				$.each($scope.serchData, function(name, value) {

					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			//禁用
			var ermissions=$rootScope.requiresPermissions('exchange','/account/exdigitalmoneyaccount/disable');
			config.columns = [ {
				"data" : "id"
			}, {
				"data" : "userName"
			},{
				"data" : "surname"
			}, {
				"data" : "trueName"
			}, {
				"data" : "coinCode"
			},  {
				"data" : "publicKey"
			}, {
				"data" : "accountNum"
			}, {
				"data" : "hotMoney"
			}, {
				"data" : "coldMoney"
			}, {
				"data" : ""
			}, {
				"data" : "disableMoney"
			} ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 9,

				"render" : function(data, type, row) {
						return row.hotMoney + row.coldMoney;
					
				}
			}, {
				"targets" : 10,

				"render" : function(data, type, row) {
					if(ermissions){
						return data;
					}else{
						return "";
					}
					
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnAdd = fnAdd;// add按钮方法
			$scope.fnSee = fnSee;// see按钮方法
			$scope.fnModify = fnModify;// see按钮方法
			$scope.fnRemove = fnRemove;// remove方法
			$scope.fnList = fnList;// 刷新方法
			
			$scope.refreshUserCoin=refreshUserCoin;//刷新用户充币记录
			
			// 刷新用户充币记录
			
			function refreshUserCoin(url) {
				var ids = DT.getSelect(table);
				var obj = DT.getRowData(table)[0];
				// var ids = transform(selectes);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var parameter={id:ids[0],coinCode:obj.coinCode,account:obj.userName,count:50};
					hryCore.CURD({
						url : HRY.modules.exchange + "/account/exdigitalmoneyaccount/refreshUserCoin" ,
						parameter : parameter
					}).query(null, function(data) {
						growl.addInfoMessage(data.msg)
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});
					
				}
			}

            /**
             * 手动提币方法
             */
            $scope.fntibi = function () {
                var ids = DT.getSelect(table);
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                } else {

                    $('#tibiDiv').removeClass("hide");
                    layer.open({
                        type: 1,
                        title: "手动提币",
                        closeBtn: 2,
                        area: ['500px', '300px'],
                        shadeClose: true,
                        content: $('#tibiDiv')
                    });
                    $scope.fntibiSubmit = function () {
                        $http({
                            method: 'POST',
                            url: HRY.modules.exchange + '/account/exdigitalmoneyaccount/tibi',
                            data: $.param({id: ids[0], number: $scope.tibi}),
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            }
                        }).success(function (data) {
                            if (data.success) {
                                growl.addInfoMessage('提币成功')
                                layer.closeAll();
                                fnList();
                            } else {
                                growl.addInfoMessage('提币成功')
                            }
                        });
                    }

                }

            }

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}
			//导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"货币账户管理");
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
					window.location.href = '#/exchange/' + url + '/' + ids[0];
				}
			}
			/**
			 * 禁用
			 */
			$scope.fnExdisable = function(){
				$scope.exaccount = {};
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var data = DT.getRowData(table);
					if(data[0].hotMoney>0){
						$('#exDisableDiv').removeClass("hide");
						layer.open({
							type : 1,
							title : "禁用",
							closeBtn : 2,
							area : [ '500px', '300px' ],
							shadeClose : true,
							content : $('#exDisableDiv')
						});
						$scope.fnExDisableSubmit = function(){
							$http({
								method : 'POST',
								url : HRY.modules.exchange + '/account/exdigitalmoneyaccount/disable',
								data : $.param({id:ids[0],disableMoney:$scope.exaccount.disableMoney}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if (data.success) {
									growl.addInfoMessage('禁用成功')
									layer.closeAll();
									fnList();
								} else {
									growl.addInfoMessage(data.msg)
								}
							});
						}
					}else{
						growl.addInfoMessage('可用币不足,不能禁用')
					}
				}
				
			
			}
			
			/**
			 * 币禁用
			 */
			$scope.fnCoinExdisable = function(){
				$scope.exaccount = {};
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var data = DT.getRowData(table);
					if(data[0].hotMoney>0){
						$('#coinexDisableDiv').removeClass("hide");
						layer.open({
							type : 1,
							title : "禁用",
							closeBtn : 2,
							area : [ '500px', '300px' ],
							shadeClose : true,
							content : $('#coinexDisableDiv')
						});
						$scope.fncoinExDisableSubmit = function(){
							if($scope.exaccount.disableMoney<0){
								growl.addInfoMessage('禁用数量不能小于0');
								return ;
							}
							$http({
								method : 'POST',
								url : HRY.modules.exchange + 'account/appaccountdisable/coindisable',
								data : $.param({id:ids[0],disableMoney:$scope.exaccount.disableMoney,remark:$scope.exaccount.remark}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if (data.success) {
									growl.addInfoMessage('禁用成功')
									layer.closeAll();
									fnList();
								} else {
									growl.addInfoMessage(data.msg)
								}
							});
						}
					}else{
						growl.addInfoMessage('可用币不足,不能禁用')
					}
				}
			}
			/**
			 * 系统拨币
			 */
			$scope.fnCoinPoking = function(){
				$scope.account = {};
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$('#coinPokingDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "系统拨币",
						closeBtn : 2,
						area : [ '500px', '300px' ],
						shadeClose : true,
						content : $('#coinPokingDiv')
					});
					$scope.fnCoinPokingSubmit = function(){
						$http({
							method : 'POST',
							url : HRY.modules.exchange + '/account/exdigitalmoneyaccount/coinPoking',
							data : $.param({id:ids[0],coinPokingNumber:$scope.account.coinPoking}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('拨币成功')
								layer.closeAll();
								fnList();
							} else {
								growl.addInfoMessage('拨币失败')
							}
						});
					}
				}
			}
			/**
			 * 手动充值方法
			 */
			$scope.fnRecharge = function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					
					$('#rechargeDiv').removeClass("hide");
					layer.open({
						type : 1,
						title : "手动充值",
						closeBtn : 2,
						area : [ '500px', '300px' ],
						shadeClose : true,
						content : $('#rechargeDiv')
					});
					$scope.fnRechargeSubmit = function(){
						$http({
							method : 'POST',
							url : HRY.modules.exchange + '/account/exdigitalmoneyaccount/recharge',
							data : $.param({id:ids[0],number:$scope.recharge.number}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('充值成功')
								layer.closeAll();
								fnList();
							} else {
								growl.addInfoMessage('充值失败')
							}
						});
					}
					
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