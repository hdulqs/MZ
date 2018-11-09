 /**
 * Copyright:   北京互融时代软件有限公司
 * AppBusinessmanBank.js
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-18 16:55:04 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "businessman/appbusinessmanbank/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {
			
			
		
			
			$scope.formData = {};
			
			
			$scope.processForm = function() {
				
				var bankname = $("#bankname").val();
				if(bankname==undefined||bankname==""){
					growl.addInfoMessage('开户行不能为空!');
					return false;
				}
				
				var bankcard = $("#bankcard").val();
				if(bankcard==undefined||bankcard==""){
					growl.addInfoMessage('银行卡号户不能为空!');
					return false;
				}
				var bankowner = $("#bankowner").val();
				if(bankowner==undefined||bankowner==""){
					growl.addInfoMessage('持卡人不能为空!');
					return false;
				}
				
				
				var businessmanId = $("#businessmanId").val()
				if(businessmanId==undefined||businessmanId==""){
					growl.addInfoMessage('交易商户不能为空!');
					return false;
				}
				$scope.formData.businessmanId = businessmanId;
				
				
				$http({
					method : 'POST',
					url : HRY.modules.customer + 'businessman/appbusinessmanbank/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/businessman/appbusinessmanbank/list/anon';
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

			$http.get(HRY.modules.customer + "businessman/appbusinessmanbank/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				
				//$("#businessmanId").removeClass("select2");
				 //hryCore.RenderSelect($("#businessmanId"),data.businessmanId);
            	 //hryCore.RenderAllSelect(data);
 	             hryCore.RenderHTML(data);//异步回显控件
 	            
 	            // conApp.initMaterialPlugins();
			});

			$scope.processForm = function() {
				
				var bankname = $("#bankname").val();
				if(bankname==undefined||bankname==""){
					growl.addInfoMessage('开户行不能为空!');
					return false;
				}
				
				var bankcard = $("#bankcard").val();
				if(bankcard==undefined||bankcard==""){
					growl.addInfoMessage('银行卡号户不能为空!');
					return false;
				}
				var bankowner = $("#bankowner").val();
				if(bankowner==undefined||bankowner==""){
					growl.addInfoMessage('持卡人不能为空!');
					return false;
				}
				
				
				var businessmanId = $("#businessmanId").val()
				if(businessmanId==undefined||businessmanId==""){
					growl.addInfoMessage('交易商户不能为空!');
					return false;
				}
				$scope.formData.businessmanId = businessmanId;

				$http({
					method : 'POST',
					url : HRY.modules.customer + 'businessman/appbusinessmanbank/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/businessman/appbusinessmanbank/list/anon';
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
			config.ajax.url = HRY.modules.customer + 'businessman/appbusinessmanbank/list';
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
			},
			{
				"data" : "businessmanTrueName"   
			},
			{
				"data" : "bankname"   
			},
			{
				"data" : "bankcard"   
			},
			{
				"data" : "bankowner"   
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			} ]
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
					window.location.href = "#/customer/businessman/appbusinessmanbank/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/customer/businessman/appbusinessmanbank/add/anon";
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
        			window.location.href='#/customer/businessman/appbusinessmanbank/modify/'+ids[0];
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
						url : HRY.modules.web + "businessman/appbusinessmanbank/remove/"+ ids
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