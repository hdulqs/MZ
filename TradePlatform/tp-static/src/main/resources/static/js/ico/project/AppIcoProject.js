 /**
 * Copyright:   北京互融时代软件有限公司
 * AppIcoProject.js
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-07-19 13:40:56 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.ico + "project/appicoproject/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {

			$scope.formData = {};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.ico + 'project/appicoproject/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/ico/project/appicoproject/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};
		}

		/**
		 * ------------------------修改页面路径---------------------------------------------
		 */
		if ($stateParams.page == "modify") {

			$http.get(HRY.modules.ico + "project/appicoproject/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.ico + 'project/appicoproject/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/ico/project/appicoproject/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}

		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.ico + 'project/appicoproject/list';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			{
				"data" : "id"   //id
			},{
				"data":"projectName"
			},{
				"data":"created"
			},{
				"data":"projectStage"//0.尚未开启 1.产品开发中 2.产品已上市 3.已经盈利
			},{
				"data":"financingStage"//0.未融资 1.D轮 2.C轮 3.B轮 4.A轮 5.天使轮
			},{
				"data":"icoDays"
			},{
				"data":"coinType"
			},{
				"data":"linkman"
			},{
				"data":"phone"
			},{
				"data":"status"//'项目状态 0.未提交 1.待审核 2.未通过 3.即将开始 4.进行中 5.已完成 6.失败'
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			},{
				"targets" : 3,//0.尚未开启 1.产品开发中 2.产品已上市 3.已经盈利
				"orderable" : false,
				"render" : function(data, type, row) {
					switch (data) {
					case 0:
						data="尚未开启"
						break;
					case 1:
						data="产品开发中"				
						break;
					case 2:
						data="产品已上市"
						break;
					case 3:
						data="产品已盈利"
						break;
					default:
						break;
					}
					return data;
				},
			},{
				"targets" : 4,//0.未融资 1.D轮 2.C轮 3.B轮 4.A轮 5.天使轮
				"orderable" : false,
				"render" : function(data, type, row) {
					switch (data) {
					case 0:
						data="未融资"
						break;
					case 1:
						data="D轮"				
						break;
					case 2:
						data="C轮"
						break;
					case 3:
						data="B轮"
						break;
					case 4:
						data="A轮"
						break;
					case 5:
						data="天使轮"
						break;
					default:
						break;
					}
					return data;
				},
			},{
				"targets" : 9,//'项目状态 0.未提交 1.待审核 2.未通过 3.即将开始 4.进行中 5.已完成 6.失败'
				"orderable" : false,
				"render" : function(data, type, row) {
					switch (data) {
					case 0:
						data="未提交"
						break;
					case 1:
						data="待审核"				
						break;
					case 2:
						data="未通过"
						break;
					case 3:
						data="即将开始"
						break;
					case 4:
						data="进行中"
						break;
					case 5:
						data="已完成"
						break;
					case 6:
						data="失败"
						break;
					case 7:
						data="删除"
						break;
					default:
						break;
					}
					return data;
				},
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
			
			/**
			 * 查看按钮
			 */
			$scope.fnSee = function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = "#/ico/project/appicoproject/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/ico/project/appicoproject/add/anon";
			}
			
			/**
			 * 修改按钮
			 */
			$scope.fnModify = function(){
            	var	ids = DT.getSelect(table);
            	if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
        			window.location.href='#/ico/project/appicoproject/modify/'+ids[0];
            	}
			}
			
			/**
			 * 删除按钮
			 */
			$scope.fnRemove = function() {
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确定删除？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "project/appicoproject/remove/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('删除成功')
							// 重新加载list
							$scope.fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
					}, function(data) {
						growl.addInfoMessage("error:" + data.msg);
					});

				});
			}
			
			/**
			 * 审批按钮
			 */
			$scope.fnAudit = function() {
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}
				var audit=function(data){
					 layer.closeAll();
					 var list=DT.getRowData(table);
					 var flag=data.isAgree?3:2;
					 for(var i=0;i<list.length;i++){
						 if((list[i].status==2||list[i].status==3)&&list[i].status==flag){
							layer.alert("存在已处理目标，请检查！");
							return ;
						 }
					 }
					  $.ajax({
						  type:'post',
						  url : HRY.modules.web + "project/appicoproject/audit/"+ ids,
						  data:data,
						  dataType:'json',
						  success:function(data){
							  if(data.success){
								  layer.alert("操作成功");
							  }else{
								  layer.alert("操作失败");
							  }
							  $scope.fnList();
						  }
					  })
				}
				var ids = hryCore.ArrayToString(arrId);
				//通过、拒绝、取消三个按钮
				layer.open({
					  content: '项目审核？',
					  btn: ['通过', '拒绝'],
					  btn1: function(index, layero){
						 audit({
							 	isAgree:true 
						  	})
					  }
					  ,btn2: function(index, layero){
						  audit({
							 	isAgree:false 
						  	})
					  },cancel: function(){
						  //右上角关闭回调
					  }
					});
			}

		}

		// 加载插件
		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();
	}
	
	// -----------controller.js加载完毕--------------
	return {
		controller : controller
	};
});