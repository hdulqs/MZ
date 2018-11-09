define([ 'app','hryTable' ,'ztree'], function(app,DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', "$state" ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {

		/**
		 * 列表页面
		 */
		if ($stateParams.page == "list") {
			fnList();
			function fnList() {
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
							"Y" : "s",
							"N" : "s"
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
						beforeClick : function(treeId, treeNode, clickFlag) {
							$.fn.zTree.getZTreeObj("tree").checkNode(treeNode);
						}
					}
				};
				var zNodes = [];
				$.post(HRY.modules.web + "menu/appmenucust/findAll", null, function(data) {
					for (var i = 0; i < data.length; i++) {
						var item = {
							id : data[i].id,
							mkey : data[i].mkey,
							pkey : data[i].pkey,
							name : data[i].name,
							isVisible : data[i].isVisible,
							type : data[i].type,
							open : true
						// drop : false
						}
						zNodes.push(item);
					}
					// 初始化菜单树
					zTree =$.fn.zTree.init($("#tree"), setting, zNodes);
					
					// 修改ztree样式
					var treeSpan = $("#tree a");
					for (var i = 0; i < treeSpan.length; i++) {
						var ztid = $(treeSpan[i]).attr("ztid");
						var zNode = zTree.getNodeByParam("id", ztid, null);
						if(zNode.isVisible=="1"){
							$(treeSpan[i]).css("color","red");
						}
						if(zNode.type=="function"){
							var span = $($(treeSpan[i]).children()[1]);
							span.html(span.html()+" | 按钮");
						}
					}
					

				}, "json");
			}
			
			//全选
			$scope.checkFalg = 0;
			$scope.fnCheckedAll = function(treeId){
				$scope.checkFalg++;
				var ztree = $.fn.zTree.getZTreeObj(treeId);
				if($scope.checkFalg%2==1){
					ztree.checkAllNodes(true);
				}else{
					ztree.checkAllNodes(false);
				}
//				var nodes = ztree.getNodes();
//				for(var i = 0 ; i < nodes.length ; i++){
//					ztree.checkNode(nodes[i]);
//				}
			}
			
			
			$scope.fnIsVisible = function(){

				$scope.formData = {};
				layer.confirm('你确定要隐藏吗？', {
	    			btn: ['确定','取消']
				}, function(){
					// 获得选中的菜单荐
					var ztree = $.fn.zTree.getZTreeObj("tree");
					var chekcNodes = ztree.getCheckedNodes();
					if (chekcNodes.length < 1) {
						growl.addInfoMessage("请选择菜单")
						return false;
					}
					var str = "";
					for (var i = 0; i < chekcNodes.length; i++) {
						str += chekcNodes[i].id;
						if (i != chekcNodes.length - 1) {
							str += ","
						}
					}
					// 发送请求
					$scope.formData.ids = str;
					$http({
						method : 'POST',
						url : HRY.modules.web + 'menu/appmenucust/isVisible',
						data : $.param($scope.formData),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							layer.msg("隐藏成功", {
								icon : 1,
								time : 2000
							});
							fnList();
						} else {
							layer.msg(data.msg, {
								icon : 1,
								time : 2000
							});
						}
					});
					
				})
			}
			
			// 删除客户菜单
			$scope.fnRemove = function() {
				$scope.formData = {};
				
				layer.confirm('你确定要删除吗？', {
	    			btn: ['确定','取消']
				}, function(){
					// 获得选中的菜单荐
					var ztree = $.fn.zTree.getZTreeObj("tree");
					var chekcNodes = ztree.getCheckedNodes();
					if (chekcNodes.length < 1) {
						growl.addInfoMessage("请选择菜单")
						return false;
					}
					var str = "";
					for (var i = 0; i < chekcNodes.length; i++) {
						str += chekcNodes[i].id;
						if (i != chekcNodes.length - 1) {
							str += ","
						}
					}
					// 发送请求
					$scope.formData.ids = str;
					$http({
						method : 'POST',
						url : HRY.modules.web + 'menu/appmenucust/remove',
						data : $.param($scope.formData),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							layer.msg("删除成功", {
								icon : 1,
								time : 2000
							});
							fnList();
						} else {
							layer.msg(data.msg, {
								icon : 1,
								time : 2000
							});
						}
					});
					
				})
				
			

			}

			// 配置菜单保存按钮
			$scope.fnAdd = function() {
				$scope.formData = {};
				 
				var ids = DT.getSelect(table);
				// 获得选中的菜单荐
				if (ids.length < 1) {
					growl.addInfoMessage("请选择菜单")
					return false;
				}
				// 发送请求
				$scope.formData.ids = hryCore.ArrayToString(ids);
				$http({
					method : 'POST',
					url : HRY.modules.web + 'menu/appmenucust/add',
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
						fnList();
					} else {
						layer.msg(data.msg, {
							icon : 1,
							time : 2000
						});
					}
					layer.closeAll();
				});

			}
			
			var table = $("#table2");
			$scope.flag = true;//标记table是否是第一次渲染
			// 选择权限框
			$scope.selectAppMenu = function() {
				
				$scope.serchData = {};
				
				// 获得custTree所有的id
				var custTree = $.fn.zTree.getZTreeObj("tree");
				var custTreeNodes = custTree.getNodes();
				var custTreeKeys = "";
				for (var i = 0; i < custTreeNodes.length; i++) {
					custTreeKeys += custTreeNodes[i].mkey;
					if (i != custTreeNodes.length - 1) {
						custTreeKeys += ","
					}
				}
				$scope.serchData.custTreeKeys = custTreeKeys;
				// --------------------加载dataTable--------------------------------
				var loadDataTable = function(){
					var config = DT.config();
					config.scrollX = false;
					config.bPaginate = false;//不开分页
					config.bAutoSerch = true; // 是否开启键入搜索
					config.ajax.url = HRY.modules.web + "menu/appmenu/findMenu";
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
							}else if(data=="hurong_spotchange"){
								return "委托应用";
							}else{
								return "其他应用";
							}
							return data;
						}
					}]
					DT.draw(table, config);
					// --------------------加载dataTable--------------------------------
				}
				
				//使dataTable配置只加载一次
				if($scope.flag){
					loadDataTable();	
					$scope.flag = false;
				}else{
					table.DataTable().draw();
				}
				
				
				// 移除隐藏样式
				$("#selectTreeDiv").removeClass("hide");
				// 弹出选择窗体
				layer.open({
					type : 1,
					title : "请勾选菜单",
					closeBtn : 2,
					area : [ '920px', '600px' ],
					shadeClose : true,
					content : $('#selectTreeDiv')
				});
			}

		}
		
		 //加载插件
		hryCore.initPlugins();
	}
	return {
		controller : controller
	};
});