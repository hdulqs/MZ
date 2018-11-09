 /**
 * Copyright:   北京互融时代软件有限公司
 * AppCommendUser.js
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-28 15:30:38 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "commend/appcommenduser/see/" + $stateParams.id).success(function(data) {
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
					url : HRY.modules.customer + 'commend/appcommenduser/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/commend/appcommenduser/list/anon';
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

			$http.get(HRY.modules.customer + "commend/appcommenduser/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.customer + 'commend/appcommenduser/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/commend/appcommenduser/list/anon';
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
			config.ajax.url = HRY.modules.customer + 'commend/appcommenduser/list';
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
			/*{
				"data" : "pid"   //pid
			},*/
			{
				"data" : "username"   //username
			},
			/*{
				"data" : "pname"   //pname
			}*/
			{
				"data" : "receCode"   //username
			},
			{
				"data" : "isFrozen"   //username
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			} ,
			 {
				"targets" : 3,

				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "已冻结"
					}else if(data=="0"){
						return "未冻结"
					}
				}
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			   $scope.fnFeeWithdrawals=fnFeeWithdrawals;//刷新方法
	            $scope.modifySubmit = modifySubmit;//修改提交按钮方法
			
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
					window.location.href = "#/customer/commend/appcommenduser/see/" + ids[0];
				}
			}
			
		    //手续费账户提现
            function fnFeeWithdrawals(){
            	var ids = DT.getSelect(table);
            	var selectData = DT.getRowData(table)[0];
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(selectData.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            			//判断余额
            			var money=selectData.accountFee;
        					$('#modifyInfoone').removeClass("hide");
        					$scope.accountFee=selectData.accountFee;
        					$("#accountFee").val(selectData.username);
        					$("#id").val(selectData.id);
        					layer.open({
        						type : 1,
        						title : "单独奖励比例设置",
        						closeBtn : 2,
        						area : [ '450px', '400px' ],
        						shadeClose : true,
        						content : $('#modifyInfoone')
        					});
            	}
            }
			  //手续费提现提交按钮
			function modifySubmit(){
				//检验金額
				var id = $("#id").val();//appaccount的id
				var money = $("#money").val();//派发金额
				if(money==undefined ||money == ""){
					layer.msg("手续费不能为空！", {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
				     return false;		
				}
				
				
					layer.confirm("确定设置？", {
					btn : [ '确定', '取消' ], // 按钮
						id : id
					}, function() {
						$http.get(HRY.modules.customer+"/commend/appcommenduser/updateRewaMoney?id="+id+"&money="+money).
		                 success(function(data) {
		                 	debugger;
		                 if(data.success){
		                	 layer.msg(data.msg,{icon: 1, time: 2000},function(){
		                			
		                	 }); 
		                 }else {
                             layer.msg(data.msg,{icon: 2, time: 2000},function(){

                             });
                         }
		                	
		                
		                 });
						layer.closeAll();
	                	$scope.fnList();
					});
			}
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/customer/commend/appcommenduser/add/anon";
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
        			window.location.href='#/customer/commend/appcommenduser/modify/'+ids[0];
            	}
            	$scope.fnList();

			}
		/*	$scope.noforzen = function(){
            	var	ids = DT.getSelect(table);
            	if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
        			window.location.href='#/customer/commend/appcommenduser/noforzen/'+ids[0];
        			growl.addInfoMessage('冻结成功')
            	}
            	$scope.fnList();
			}*/
			$scope.noforzen = function(){
            	var	ids = DT.getSelect(table);
            	if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$.ajax({
						type : "POST",
						dataType : "JSON",
						url : HRY.modules.customer+'commend/appcommenduser/noforzen/'+ids[0],
						cache : false,
						success : function(data) {
						layer.msg("解冻成功", {icon: 1,time:3000},function(){
							layer.closeAll();
							$scope.fnList();
						})
						}
					})
            	}
			}
			
			$scope.forzen = function(){
            	var	ids = DT.getSelect(table);
            	if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					$.ajax({
						type : "POST",
						dataType : "JSON",
						url : HRY.modules.customer+'commend/appcommenduser/forzen/'+ids[0],
						cache : false,
						success : function(data) {
						layer.msg("冻结成功", {icon: 1,time:3000},function(){
							layer.closeAll();
							$scope.fnList();
						})
						}
					})
            	}
			}
			
			/**
			 * 删除按钮
			 */
			$scope.fnRemove = function() {
				var id = DT.getSelect(table);
				if (id.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else if (id.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				$.ajax({
					type : "POST",
					dataType : "JSON",
					url : HRY.modules.customer+'commend/appcommenduser/findLen/'+id,
					cache : false,
					success : function(data) {
						
						$("#modifyInfo").removeClass("hide")
						$("#one").html(data.oneNumber);
						$("#two").html(data.twoNumber);
						$("#three").html(data.threeNumber);
						$("#later").html(data.laterNumber);
						$("#selectAll").html(data.selectAll);
						$("#conut").html(data.conut);
						layer.open({
							type : 1,
							title : "推荐详情",
							closeBtn : 2,
							area : [ '450px', '400px' ],
							shadeClose : true,
							content : $('#modifyInfo')
						});
					}
				})
			}
			
			/**
			 * 查询推荐人按钮
			 */
			$scope.findReferees = function() {
				var id = DT.getSelect(table);
				if (id.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else if (id.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				$.ajax({
					type : "POST",
					dataType : "JSON",
					url : HRY.modules.customer+'commend/appcommenduser/findReferees/'+id,
					cache : false,
					success : function(data) {
						$("#modifyInfo11").removeClass("hide")
						$("#userName").html(data.username);
						layer.open({
							type : 1,
							title : "直推人账号",
							closeBtn : 2,
							area : [ '450px', '400px' ],
							shadeClose : true,
							content : $('#modifyInfo11')
						});
					}
				})
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