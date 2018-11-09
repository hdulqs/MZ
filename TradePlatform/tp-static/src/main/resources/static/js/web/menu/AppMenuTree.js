/**
 * student.js
 */
define([ 'app' ,'hryTable' ,'hrytree'], function(app,DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$compile', '$http', '$stateParams', '$state' ,'hrytree'];
	function controller($scope, $rootScope, $compile, $http, $stateParams, $state, hryCore,hrytree) {

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see" || $stateParams.page == "seezfb") {
			$http.get(HRY.modules.account + "fund/appouraccount/see/" + $stateParams.id).success(function(data) {
				$scope.model = data;
				// 静态下拉框全部回显
				hryCore.RenderAllSelect(data);
			});
		}
		//系统菜单类型
		$scope.trees=[{
						type:'hurong_oauth',
						name:'组织权限'
					},{
						type:'hurong_web',
						name:'基础应用'
					},{
						type:'hurong_account',
						name:'财务应用'
					},{
						type:'hurong_customer',
						name:'客户应用'
					},{
						type:'hurong_exchange',
						name:'交易所应用'
					},{
						type:'hurong_sms',
						name:'消息应用'
					}]
		
		window._trees=$scope.trees;
		
		$("select[id^='appName']").each(function(){
			var _this=$(this);
			_this.append("<option value='' selected>请选择系统分类</option>");
			$.each($scope.trees,function(index,tree){
				_this.append("<option value="+tree.type+">"+tree.name+"</option>");
			})
		});

		// ------------------------配置应用页面路径---------------------------------------------
		if ($stateParams.page == "conf") {
			var table1 = $("#table1");
			var table2 = $("#table2");
			$scope.flag1 = true;//标记table是否是第一次渲染
			$scope.flag2 = true;//标记table是否是第一次渲染
			// 加载应用菜单树
			loadTree();
			function loadTree() {
				var zNodes = [];
				$.post(HRY.modules.web + "menu/appmenutree/findByApp", {
					id : $stateParams.id,
					type : "tree"
				}, function(data) {
					for (var i = 0; i < data.length; i++) {
						var item = {
							id : data[i].id,
							mkey : data[i].mkey,
							pkey : data[i].pkey,
							name : data[i].name,
							type : data[i].type,
							open : true
						//    drop : true
						}
						zNodes.push(item);
					}
					var conf = hrytree.config();
					// 初始化菜单树
					conf.sort=true;
					conf.fnModify=fnModify;
					conf.fnUp=fnTreeUp;
					conf.fnDown=fnTreeDown;
			    	conf.fnAdd=fnTreeAdd;
			    	conf.fnRemove=fnTreeRemove;
			    	hrytree.init("tree",conf,zNodes);
					
					//树上的 添加按钮弹窗---窗体中的添加按钮
					$scope.fnTreeAddBtn = function(){
						var ids = DT.getSelect(table2);
						if (ids.length < 1) {
							growl.addInfoMessage("请选择功能菜单")
							return false;
						}
						
						$http({
							method : 'POST',
							url : HRY.modules.web + 'menu/appmenutree/addnode',
							data : $.param({id:$scope.nodeId,functionIds : hryCore.ArrayToString(ids)}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							loadTree();
						});
						layer.closeAll();
					}
					
					//树上的 修改按钮
					 function fnModify(treeNode){
						$scope.nodeId =  treeNode.id;
						$('#modifyNodeDiv').removeClass("hide");
						$("#modifyNodeName").val(treeNode.name);
						layer.open({
							type : 1,
							title : "修改菜单名称",
							closeBtn : 2,
							area : [ '300px', '200px' ],
							shadeClose : true,
							content : $('#modifyNodeDiv')
						});
					}
					 
					// 菜单上的上移按钮
					  function fnTreeUp(treeNode){
							var id=treeNode.id;
							$http({
								method : 'POST',
								url : HRY.modules.web + 'menu/appmenutree/move',
								data : $.param({id:id,type:"up"}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								loadTree();
							});
						}
						
						// 菜单上的下移按钮
						function fnTreeDown(treeNode){
							var id=treeNode.id;
							$http({
								method : 'POST',
								url : HRY.modules.web + 'menu/appmenutree/move',
								data : $.param({id:id,type:"down"}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								loadTree();
							});
						}
					
					//树上的 添加按钮弹窗
					 function fnTreeAdd(treeNode){
						//被添加的菜单node(父级node)
						$scope.nodeId = treeNode.id;
						
						$scope.serchData = {};
						
						// --------------------加载dataTable--------------------------------
						var loadDataTable = function(){
							var config = DT.config();
							config.scrollX = false;
							config.bPaginate = false;//不开分页
							config.bAutoSerch = true; // 是否开启键入搜索
							config.ajax.url = HRY.modules.web + "menu/appmenucust/findAllPage";
							config.ajax.data = function(d) {
								// 设置select下拉框
								//DT.selectData($scope.serchData);
								$scope.serchData.appName_EQ = $("#appName2").val();
								$.each($scope.serchData, function(name, value) {
									if ("" != value) {
										eval("d." + name + "='" + value + "'");
									}
								});
							}
							config.columns = [ {
								"data" : "id"
							}, {
								"data" : "appName"
							}, {
								"data" : "name"
							}, {
								"data" : "mkey"
							} ]
							config.columnDefs = [ {
								"targets" : 0,"orderable" :false,
								"orderable" : false,
								"render" : function(data, type, row) {
									return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
								}
							} ,{
								"targets" : 1,
								"orderable" : false,
								"render" : function(data, type, row) {
									if(data=="hurong_oauth"){
										return "组织权限"
									}else if(data=="hurong_web"){
										return "基础应用"
									}else if(data=="hurong_account"){
										return "财务应用"
									}else if(data=="hurong_customer"){
										return "客户应用"
									}else if(data=="hurong_exchange"){
										return "交易所应用"
									}else if(data=="hurong_sms"){
										return "消息应用"
									}
									return data;
								}
							}]
							DT.draw(table2, config);
							// --------------------加载dataTable--------------------------------
						}
						
						//使dataTable配置只加载一次
						if($scope.flag2){
							loadDataTable();	
							$scope.flag2 = false;
						}else{
							table2.DataTable().draw();
						}
						
						// 移除隐藏样式
						$("#treeAddDiv").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "请勾选功能",
							closeBtn : 2,
							area : [ '930px', '600px' ],
							shadeClose : true,
							content : $('#treeAddDiv')
						});

					
						
					}
					
					//修改菜单节点名称
					$scope.fnModifyNode = function(id){
						var modifyNodeName =  $("#modifyNodeName").val();
						$http({
							method : 'POST',
							url : HRY.modules.web + 'menu/appmenutree/modify',
							data : $.param({id:$scope.nodeId,name:modifyNodeName}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							loadTree();
						});
						layer.closeAll();
					}
					
					

					// 菜单上的删除按钮
					function fnTreeRemove(treeNode) {
						var id=treeNode.id;
						$http({
							method : 'POST',
							url : HRY.modules.web + 'menu/appmenutree/remove',
							data : $.param({id:id}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								layer.msg("删除成功", {
									icon : 1,
									time : 2000
								});
								loadTree();
							} else {
								layer.msg(data.msg, {
									icon : 1,
									time : 2000
								});
							}
						});
					
						
					

					}

				}, "json");
			}

			// 添加菜单的保存方法
			$scope.fnAdd = function() {
				$scope.formData = {};
				// 上级菜单
				var superiorMenuId = $("#superiorMenuId").val();
				var functionIds = $("#functionIds").val();
				if (superiorMenuId == undefined || superiorMenuId == "") {
					layer.msg("上级菜单不能为空", {
						icon : 1,
						time : 2000
					});
					return false;
				}

				// 发送请求
				$scope.formData.superiorMenuId = superiorMenuId;
				$scope.formData.name = $("#name").val();
				$scope.formData.functionIds = functionIds;
				$http({
					method : 'POST',
					url : HRY.modules.web + 'menu/appmenutree/addconf',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						layer.msg("配置成功", {
							icon : 1,
							time : 2000
						});
						// 刷新左边树
						// loadTree();
						$state.go("web.menu_appmenutree", {
							id : $stateParams.id,
							page : "conf"
						}, {
							reload : true
						});

					} else {
						layer.msg(data.msg, {
							icon : 1,
							time : 2000
						});
					}
					layer.closeAll();
				});

			}
			/**
			 * 双击选中
			 */
			function fnDblClick(treeNode){
				$("#superiorMenu").val(treeNode.name);
				if (treeNode.id == -1) {
					$("#superiorMenuId").val(0);
				} else {
					$("#superiorMenuId").val(treeNode.id);
				}
				layer.closeAll();
			
			}

			//选择上级
			$scope.selectAppMenuTree = function() {
				var zNodes = [];
				function loadMenuList() {
					$.post(HRY.modules.web + "menu/appmenutree/findByApp", {
						id : $stateParams.id,
						type : "select"
					}, function(data) {
						for (var i = 0; i < data.length; i++) {
							var item = {
								id : data[i].id,
								mkey : data[i].mkey,
								pkey : data[i].pkey,
								name : data[i].name,
								open : true
							// drop : false
							}
							zNodes.push(item);
						}
						// 移除隐藏样式
						$("#selectTreeDiv").removeClass("hide")
						
						// 初始化菜单树
					var conf = hrytree.config();	
			    	conf.fnDblClick=fnDblClick;
					conf.setting.edit={};
					conf.setting.view={};
			    	hrytree.init("selectTree",conf,zNodes);
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "双击选择上级菜单",
							closeBtn : 2,
							area : [ '530px', '400px' ],
							shadeClose : true,
							content : $('#selectTreeDiv')
						});

					}, "json");
				}
				loadMenuList();

			}

			// 配置功能中的
			$scope.fnSelectAppMenuCustTree = function() {
				// 获得选中的菜单荐
				var sdata = DT.getRowData(table1);
				if (sdata.length < 1) {
					growl.addInfoMessage("请选择菜单")
					return false;
				}
				var ids = "";
				var names = "";
				var l = sdata.length;
				for (var i = 0; i < l; i++) {
					ids += sdata[i].id;
					names += sdata[i].name;
					if (i != sdata.length - 1) {
						ids += ",";
						names += ",";
					}
				}
				$("#functionIds").val(ids);
				$("#functionName").val(names);

				layer.closeAll();

			}
			
			
			// 配置功能点击事件框
			$scope.selectAppMenuCust = function() {
				
				$scope.serchData = {};
				
				// --------------------加载dataTable--------------------------------
				var loadDataTable = function(){
					var config = DT.config();
					config.scrollX = false;
					config.bPaginate = false;//不开分页
					config.bAutoSerch = true; // 是否开启键入搜索
					config.ajax.url = HRY.modules.web + "menu/appmenucust/findAllPage";
					config.ajax.data = function(d) {
						// 设置select下拉框
						//DT.selectData($scope.serchData);
						$scope.serchData.appName_EQ = $("#appName1").val();
						$.each($scope.serchData, function(name, value) {
							if ("" != value) {
								eval("d." + name + "='" + value + "'");
							}
						});
					}
					config.columns = [ {
						"data" : "id"
					}, {
						"data" : "appName"
					}, {
						"data" : "name"
					}, {
						"data" : "mkey"
					} ]
					config.columnDefs = [ {
						"targets" : 0,"orderable" :false,
						"orderable" : false,
						"render" : function(data, type, row) {
							return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
						}
					} ,{
						"targets" : 1,
						"orderable" : false,
						"render" : function(data, type, row) {
							if(data=="hurong_oauth"){
								return "组织权限"
							}else if(data=="hurong_web"){
								return "基础应用"
							}else if(data=="hurong_account"){
								return "财务应用"
							}else if(data=="hurong_customer"){
								return "客户应用"
							}else if(data=="hurong_exchange"){
								return "交易所应用"
							}else if(data=="hurong_sms"){
								return "消息应用"
							}
							return data;
						}
					}]
					DT.draw(table1, config);
					// --------------------加载dataTable--------------------------------
				}
				
				//使dataTable配置只加载一次
				if($scope.flag1){
					loadDataTable();	
					$scope.flag1 = false;
				}else{
					table1.DataTable().draw();
				}
				
				// 移除隐藏样式
				$("#selectAppMenuCustDiv").removeClass("hide")
				// 弹出选择窗体
				layer.open({
					type : 1,
					title : "请勾选功能",
					closeBtn : 2,
					area : [ '1000px', '600px' ],
					shadeClose : true,
					content : $('#selectAppMenuCustDiv')
				});

			}

		}

		// ------------------------增加页面路径---------------------------------------------
		if ($stateParams.page == "add") {
			
			$scope.formData = {};
			$scope.processForm = function() {

				// 附值权限id
				$http({
					method : 'POST',
					url : HRY.modules.web + 'menu/appmenutree/add',
					data : $.param($scope.formData),
					// params:{'appResourceStr':ids},
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/web/menu/appmenutree/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}

				});

			};

		}

		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {
			var table = $("#table");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'menu/appmenutree/list';
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
				"data" : "name"
			}, {
				"data" : "mkey"
			} ]
			config.columnDefs = [ {
				"targets" : 0,"orderable" :false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			} ]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnAdd = fnAdd;// add按钮方法
			$scope.fnConf = fnConf;// see按钮方法
			$scope.fnList = fnList;// 刷新方法
			
			$scope.fnRemove = function(url){
				window.selectData = DT.getRowData(table);
				if (selectData.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (selectData.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					layer.msg('你确定要删除吗？', {
						  time: 0 //不自动关闭
						  ,btn: ['确定', '取消']
						  ,yes: function(index){
						    layer.close(index);
						    //post -start-
							$http({
								method : 'POST',
								url : HRY.modules.web + 'menu/appmenutree/remove',
								data : $.param({id:selectData[0].id}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if (data.success) {
									growl.addInfoMessage('删除成功')
									fnList();
								} else {
									growl.addInfoMessage(data.msg)
								}
							});
							
							//post -end-
						  }
						});
					}
			}
			
			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			// 添加按钮
			function fnAdd(url) {
				window.location.href = '#/web/' + url + '/anon';
			}

			// 配置应用按钮
			function fnConf(url) {
				var selectData = DT.getRowData(table);
				if (selectData.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (selectData.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = '#/web/' + url + '/' + selectData[0].id;
				}

			}

		}

		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();

	}
	return {
		controller : controller
	};
});