define([ 'app', 'hryTable', 'layer','module_md5','hryUtil' ], function(app, DT, layer,md5) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope','layer','$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		/**
		 * ------------------------代理商申请审核列表---------------------------------------------
		 */
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			
			config.bAutoSerch = true; // 是否开启键入搜索
			//config.onlyEvent = false; //取消单行事件  
			config.ajax.url = HRY.modules.customer + 'person/apppersoninfo/agentApplyList';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			{
				"data" : "id"   //customerId
			},
			{
				"data" : "mobilePhone"   //手机、用户账号
			},
			{
				"data" : "trueName"   //真实姓名
			},
			{
				"data" : "jkApplyType"   //申请类型
			},
			{
				"data" : "jkApplyAgentProvince"   //申请所在省
			},
			{
				"data" : "jkApplyAgentCity"   //申请所在市
			},
			{
				"data" : "jkApplyAgentCounty"   //申请所在区（县）
			},
			{
				"data" : "created"   //申请时间
			}
			]
			config.columnDefs = [{
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			},{
				"targets" : 3,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data==3){
						return "省代";
					}else if(data==2){
						return "市代";
					}else if(data==1){
						return "区代";
					}
				}
			},{
				"targets" : 5,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data.indexOf("请选择")>0){
						return "--";
					}else{
						return data;
					}
				}
			},{
				"targets" : 6,
				"orderable" : false,
				"render" : function(data, type, row) {
					if(data.indexOf("请选择")>0){
						return "--";
					}else{
						return data;
					}
				}
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			
			//$scope.fnList = fnList;// 刷新方法
			//$scope.fnPass = fnPass;// 审核通过方法
			//$scope.fnRefuse = fnRefuse;// 审核拒绝方法
     		// 审核通过    
            $scope.fnPass = function (){
            	var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
            	var id=ids[0];
            	layer.confirm('你确定要通过吗？', {
					btn : [ '确定', '取消' ],
					id : id
					//customerType : customerType
				}, function() {
					layer.closeAll();
					$.ajax({
						url : HRY.modules.customer + "/person/apppersoninfo/agentApplyPass",
						method : 'POST',
						data : {
							id : id
						},
						dataType : 'json',
						success : function(data) {
							debugger;
							if (data.success) {
								layer.msg("审核通过成功！", {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
								table.DataTable().draw();
							} else {
								layer.msg("审核通过失败！", {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
							}
						}
						
					});

				});
            }
            // 审核拒绝     
            $scope.fnRefuse = function (){
            	var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
            	var id=ids[0];
            	layer.confirm('你确定要通过吗？', {
					btn : [ '确定', '取消' ],
					id : id
					//customerType : customerType
				}, function() {
					layer.closeAll();
					$.ajax({
						url : HRY.modules.customer + "/person/apppersoninfo/agentApplyRefuse",
						method : 'POST',
						data : {
							id : id
						},
						dataType : 'json',
						success : function(data) {
							debugger;
							if (data.success) {
								layer.msg("审核拒绝成功！", {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
								table.DataTable().draw();
							} else {
								layer.msg("审核拒绝失败！"+data.msg, {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
							}
						}
					});
					
					
					
					
					

				});
            }
            	
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
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