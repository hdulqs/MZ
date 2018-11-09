 /**
 * Copyright:   北京互融时代软件有限公司
 * AppCommendMoney.js
 * @author:      menwei
 * @version:     V1.0 
 * @Date:        2017-11-29 10:05:55 
 */
define([ 'app', 'hryTable', 'layer',"hryUtil",'pikadayJq' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "money/appcommendmoney/see/" + $stateParams.id).success(function(data) {
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
					url : HRY.modules.customer + 'money/appcommendmoney/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/money/appcommendmoney/list/anon';
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

			$http.get(HRY.modules.customer + "money/appcommendmoney/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.customer + 'money/appcommendmoney/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/money/appcommendmoney/list/anon';
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
			config.ajax.url = HRY.modules.customer + 'money/appcommendmoney/list';
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
				"data" : "custromerId"   //custromerId
			},*/
			{
				"data" : "custromerName"   //custromerName
			},
			/*{
				"data" : "refecode"   //邀请码
			},*/
			{
				"data" : "moneyNum"   //总佣金
			},
			{
				"data" : ""   //带派发佣金
			},
			{
				"data" : "paidMoney"   //已派发佣金
			},
			{
				"data" : "dealType"   //用户方
			},
			/*{
				"data" : "sid"   //所有下级
			},*/
			/*{
				"data" : "fixPriceType"   //fixPriceType
			},*/
			{
				"data" : "fixPriceCoinCode"   //fixPriceCoinCode
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
                    "renderRule" : "VALUE_OPERATION",//值的计算
                    "renderValueRule" : "moneyNum-paidMoney",//值替换规则
                    "render" : function(data, type, row) {

                        return row.moneyNum - row.paidMoney
                    }
                } ,{
                    "targets" : 5,
                    "orderable" : false,
                    "orderable" : false,
                    "render" : function(data, type, row) {
                        return data==1?'买方':'卖方'
                    }
                }]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
			
			// 导出excel
            $scope.fnExcel1 = function() {
                DT.excel(table, this.serchData, "推荐派发管理");
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
					window.location.href = "#/customer/money/appcommendmoney/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/customer/money/appcommendmoney/add/anon";
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
        			window.location.href='#/customer/money/appcommendmoney/modify/'+ids[0];
            	}
			}
			
			
			
			
			$scope.fncommend=function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				$.ajax({
					type : "POST",
					dataType : "JSON",
					url : HRY.modules.customer+'money/appcommendmoney/findMoneyDetail/'+ids,
					cache : false,
					success : function(data) {debugger;
						$("#modifyInfo").removeClass("hide")
						$("#one").html(data.commendOne);
						$("#two").html(data.commendTwo);
						$("#three").html(data.commendThree);
						$("#later").html(data.commendLater);
						layer.open({
							type : 1,
							title : "手续费账户提现",
							closeBtn : 2,
							area : [ '450px', '400px' ],
							shadeClose : true,
							content : $('#modifyInfo')
						});
					}
				})
			}
			
			
			
			// 派发佣金	
			$scope.fnPost=function(){
            	var ids = DT.getSelect(table);
            	/*var data = DT.getRowData(table);*/
            	/*var  fixPriceCoinCode=data[0].fixPriceCoinCode;*/
            	
            	if(ids.length==0){
        			layer.msg('请选择数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}else{  
            		layer.confirm("确定派发佣金？", {
    					btn : [ '确定', '取消' ], // 按钮
    					ids : ids,
    					/*fixPriceCoinCode:fixPriceCoinCode*/
    				}, function() {
    					$http.get(HRY.modules.customer+"money/appcommendmoney/postMoney?id="+ids).
		                 success(function(data) {
		                	layer.msg(data.msg, {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
		                	$scope.fnList();
		                 });
    					
    				});
            		
            	}
            
            }
			
			/*$scope.fnPost=function(){
            	var ids = DT.getSelect(table);
            	var data = DT.getRowData(table);
            	var  fixPriceCoinCode=data[0].fixPriceCoinCode;
            	if(ids.length==0){
        			layer.msg('请选择数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}else if(ids.length>1){
            		layer.msg('只能选择一条数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else if(data[0].surplusMoney<=0){
            		layer.msg('待派发佣金为0', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else{  
            		layer.confirm("确定派发佣金？", {
    					btn : [ '确定', '取消' ], // 按钮
    					ids : ids,
    					fixPriceCoinCode:fixPriceCoinCode
    				}, function() {
    					$http.get(HRY.modules.customer+"money/appcommendmoney/postMoney?id="+ids.
		                 success(function(data) {
		                	layer.msg(data.msg, {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
		                	$scope.fnList();
		                 });
    					
    				});
            		
            	}
            
            }*/
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
						url : HRY.modules.web + "money/appcommendmoney/remove/"+ ids
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