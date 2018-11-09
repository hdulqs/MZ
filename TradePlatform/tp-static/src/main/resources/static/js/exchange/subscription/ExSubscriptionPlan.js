 /**
 * Copyright:   北京互融时代软件有限公司
 * ExSubscriptionPlan.js
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-21 15:48:49 
 */
define([ 'app', 'hryTable', 'layer' ,'pikadayJq','clockpicker','hryUtil','bootstrap-datetimepicker'], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		/**时间控件**/
		$('.datetimepicker').datetimepicker({
   		 format: 'yyyy-MM-dd hh:mm:ss',
   		 collapse: false
          });

		
		
		/**
		 * 判断输入的数字是否是正数
		 */
		$scope.fnFormNumber = function(forNumber){
			var number = $("#"+forNumber).val();
			if(number!=""){
				if(!positiveNumber(number)){
					growl.addInfoMessage('输入的数据不符合规范');
					$("#"+forNumber).val("");
            		return false;
				}
			}
		}
		/**
		 * ------------------------制定认购计划---------------------------------------------
		 */
		if ($stateParams.page == "formulate") {	
			var table =  $("#table2");
        	$scope.serchData={};
            //--------------------加载dataTable--------------------------------
            	var config = DT.config();
            		config.bAutoSerch = true; //是否开启键入搜索
            		config.ajax.url = HRY.modules.exchange+'product/exproduct/list?page=product';
            		config.ajax.data = function(d){
    		        			$.each($scope.serchData, function(name,value){
    								if(""!=value){
    									eval("d."+name+"='"+value+"'");
    								}
    							});
    						}
            		config.columns = [	{
            								"data" : "id"
            							}, {
    										"data" : "name"
    									}, {
    										"data" : "totalNum"
    									}, {
    										"data" : "issuePrice"
    									}, {
    										"data" : "issueState"
    									}, {
    										"data" : "openBell"
    									}, {
    										"data" : "coinCode"
    									}, {
    										"data" : "issueName"
    									}, {
    										"data" : "issueTime"
    									} // 
            		                  ]
            		config.columnDefs  = [
    										{
    											"targets" : 0,"orderable" :false,
    												
    											"render" : function(data, type, row) {
    												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
    											}
    										},
    										{
    											"targets" : 4,
    												
    											"render" : function(data, type, row) {
    												if(data!=null&&data=="0"){
    													return "<font color='blue'>尚未发行</font>"
    												}
    												if(data!=null&&data=="1"){
    								    				return "<font color='red'> 发行中...</font>"
    												}
    												if(data!=null&&data=="2"){
    													return "<font color='gray'>停牌</font>"
    												}
    												if(data!=null&&data=="3"){
    													return "退市"
    												}
    												return "";
    											}
    										},{
    											"targets" : 5,
    												
    											"render" : function(data, type, row) {
    												if(data!=null&&data=="1"&&row.issueState == "1"){
    													return "<font color='blue'>开启</font>"
    												}
    												return "<font color='red'>关闭</font>";
    											}
    										}
    									 ]
            	DT.draw(table,config);
        		//--------------------加载dataTable--------------------------------

                $scope.fnList=fnList;//刷新方法
                //刷新按钮
                function fnList(){
                	 table.DataTable().draw();
                }
                
                /**
    			 * 制定认购计划
    			 */
    			$scope.fnAdd = function(){
    				var ids = DT.getSelect(table);
                	if(ids.length==0){
                		growl.addInfoMessage('请选择数据')
                		return false;
                	}else if(ids.length>1){
                		growl.addInfoMessage('只能选择一条数据')
                		return false;
                	}else{	
                		window.location.href = "#/exchange/subscription/exsubscriptionplan/add/"+ids[0];
                	}
    			}
            
        }

		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.exchange + "subscription/exsubscriptionplan/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
				if(data.state==0){
					$scope.formData.state = "未发布";
				}else if(data.state==1){
					$scope.formData.state = "已发布";
				}else if(data.state==2){
					$scope.formData.state = "已开始认购";
				}else if(data.state==3){
					$scope.formData.state = "认购完成";
				}else if(data.state==4){
					$scope.formData.state = "已删除";
				}
			});
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {
			$scope.formData = {};
			$scope.formData.state = '0';
			$scope.processForm = function() {
				$scope.formData.startTime=$("#startTime").val();
				$scope.formData.coinCode=$('#coidCode option:selected').val();
				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'subscription/exsubscriptionplan/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/exchange/subscription/exsubscriptionplan/list/anon';
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

			$http.get(HRY.modules.exchange + "subscription/exsubscriptionplan/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'subscription/exsubscriptionplan/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/exchange/subscription/exsubscriptionplan/list/anon';
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
			config.ajax.url = HRY.modules.exchange + 'subscription/exsubscriptionplan/list';
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
				"data" : "coinCode"   //产品代码
			},{
				"data" : "coinName"   //币种名称
			},
			{
				"data" : "period"   //认购期数
			},
			{
				"data" : "startTime"   //开始认购时间
			},
			{
				"data" : "openNumber"   //开放认购数量
			},
			{
				"data" : "minimumNumber"   //最低认购数量
			},
			{
				"data" : "maximumNumber"   //最高认购数量
			},
			{
				"data" : "initialPrice"   //认购初始价
			},
			{
				"data" : "highestPrice"   //交易初始价
			},
			{
				"data" : "startInitialPrice"   //回购有效期
			},
			{
				"data" : "repurchasePeriod"   //认购最高价
			},
			{
				"data" : "state"   //状态：0未发布，1已发布，2已开始认购，3认购完成，4已删除状态为2，3时，不能删除
			},
			{
				"data" : "repurchaseFee"   //回购手续费
			},
			{
				"data" : "priceBase"   //涨价基数
			},
			{
				"data" : "rose"   //涨幅
			},
			{
				"data" : "sratioOne"   //认购返佣比例一级
			},
			{
				"data" : "sratioTwo"   //认购返佣比例二级
			},
			{
				"data" : "sratioTheree"   //认购返佣比例三级
			},
			{
				"data" : "purchaseNumber"   //已认购数量
			},
			{
				"data" : "surplusNumber"   //剩余可认购个数
			},
			{
				"data" : "transactionPrice"   //交易最高价
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			},
			{
				"targets" : 12,
					
				"render" : function(data, type, row) {
					if(data!=null&&data=="0"){
						return "<font color='gray'>未发布</font>"
					}else if(data!=null&&data=="1"){
	    				return "<font color='blue'>已发布</font>"
					}else if(data!=null&&data=="2"){
						return "<font color='blue'>已开始认购</font>"
					}else if(data!=null&&data=="3"){
						return "<font color='blue'>认购完成</font>"
					}else if(data!=null&&data=="4"){
						return "<font color='red'>已删除</font>"
					}
					return "";
				}
			} ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			/**
			 * 发布
			 */
			$scope.fnRelease = function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var data = DT.getRowData(table);
					if(data[0].state==0){
						$http({
							method : 'POST',
							url : HRY.modules.exchange + 'subscription/exsubscriptionplan/release/'+ids[0],
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							if (data.success) {
								growl.addInfoMessage('发布成功');
								$scope.fnList();
							} else {
								growl.addInfoMessage(data.msg)
							}
						});
					}else{
						growl.addInfoMessage('只能选择未发布的记录')
						return false;
					}
				}
			}
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
					window.location.href = "#/exchange/subscription/exsubscriptionplan/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/exchange/subscription/exsubscriptionplan/add/anon";
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
					var data = DT.getRowData(table);
					if(data[0].state==0){
						window.location.href='#/exchange/subscription/exsubscriptionplan/modify/'+ids[0];
					}else{
						growl.addInfoMessage('只能选择未发布的记录修改')
						return false;
					}
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
						url : HRY.modules.web + "subscription/exsubscriptionplan/remove/"+ ids
					}).remove(null, function(data) {
						// 提示信息
						growl.addInfoMessage(data.msg)
						// 重新加载list
						$scope.fnList();
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