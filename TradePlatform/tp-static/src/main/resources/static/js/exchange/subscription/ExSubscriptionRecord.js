 /**
 * Copyright:   北京互融时代软件有限公司
 * ExSubscriptionRecord.js
 * @author:      zenghao
 * @version:     V1.0 
 * @Date:        2016-11-22 18:36:28 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.exchange + "subscription/exsubscriptionrecord/see/" + $stateParams.id).success(function(data) {
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
					url : HRY.modules.exchange + 'subscription/exsubscriptionrecord/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/exchange/subscription/exsubscriptionrecord/list/anon';
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

			$http.get(HRY.modules.exchange + "subscription/exsubscriptionrecord/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'subscription/exsubscriptionrecord/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/exchange/subscription/exsubscriptionrecord/list/anon';
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
			config.ajax.url = HRY.modules.exchange + 'subscription/exsubscriptionrecord/list';
			config.ajax.data = function(d) {
				// 设置select下拉框
					DT.selectData($scope.serchData);
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
				"data" : "userName"   //账户名
			},
			{
				"data" : "trueName"   //真实姓名
			},
			{
				"data" : "coinCode"   //币种代码
			},
			{
				"data" : "coinName"   //币种名称
			},
			{
				"data" : "period"   //认购期数
			},
			{
				"data" : "time"   //认购时间
			},
			{
				"data" : "price"   //认购价格
			},
			{
				"data" : "amount"   //认购数量
			},
			{
				"data" : "backAmount"   //回购数量
			},{
				"data" : "applyBackNum"   //回购申请中数量
			},{
				"data" : "backEndTime"   //回购截止时间
			},
			{
				"data" : "transactionNum"   //认购单号
			},
			{
				"data" : "state"   //状态（0申购成功，1部分回购，2全部回购）
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
				"targets" : 13,
					
				"render" : function(data, type, row) {
					if(data!=null&&data=="0"){
						return "<font color='blue'>申购成功</font>"
					}
					if(data!=null&&data=="1"){
	    				return "<font color='blue'>部分回购</font>"
					}
					if(data!=null&&data=="2"){
						return "<font color='blue'>全部回购</font>"
					}
					if(data!=null&&data=="3"){
						return "<font color='red'>回购失效</font>"
					}
					return "";
				}
			} ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
			$scope.backNum = function(){
				var backNum = $("#backNum").val();
				var hiddenNum = $("#hiddenNum").val();
				if(backNum!=""&&hiddenNum!=""){
					if(parseInt(hiddenNum)<parseInt(backNum)){
						growl.addInfoMessage('最多只能回购：'+hiddenNum+"个")
						return false;
					}
				}
			}
			/**
			 * 回购弹窗
			 */
			$scope.fnBuyBack = function(){
				$scope.buyBack = {};
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					//判断是否是已经全部回购
					var data = DT.getRowData(table);
					var sumNum = data[0].backAmount+data[0].applyBackNum;
					if(data[0].amount <= sumNum){
						growl.addInfoMessage('回购数量已全部申请完了')
						return false;
					}
					if(data[0].state==2||data[0].state==3){
						growl.addInfoMessage('已经全部回购了，或者已经超过回购有效期了')
						return false;
					}else{
						$("#hiddenNum").val(data[0].amount-sumNum);
						$("#backNum").attr('placeholder','最多可回购数量'+(data[0].amount-sumNum)+'个');
						$http.get(HRY.modules.exchange + "subscription/exsubscriptionplan/see/" + data[0].planId).success(function(data) {
							$scope.formData = data;
							$("#repurchaseFee").text(data.repurchaseFee);
						});
						$scope.buyBack.coinName = data[0].coinName;
						$('#buyBackDiv').removeClass("hide");
						layer.open({
							type : 1,
							title : "申请回购",
							closeBtn : 2,
							area : [ '500px', '350px' ],
							shadeClose : true,
							content : $('#buyBackDiv')
						});
						$scope.fnBuyBackSubmit = function(){
							$http({
								method : 'POST',
								url : HRY.modules.exchange + 'subscription/exsubscriptionrecord/buyBack',
								data : $.param({id:data[0].id,buyBackNum:$scope.buyBack.buyBackNum}),
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if (data.success) {
									growl.addInfoMessage('回购成功，请到回购记录管理中查看')
									$scope.fnList();
									layer.closeAll();
								} else {
									growl.addInfoMessage(data.msg)
								}
							});
						}
					}
				}
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
					window.location.href = "#/exchange/subscription/exsubscriptionrecord/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/exchange/subscription/exsubscriptionrecord/add/anon";
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
        			window.location.href='#/exchange/subscription/exsubscriptionrecord/modify/'+ids[0];
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
						url : HRY.modules.web + "subscription/exsubscriptionrecord/remove/"+ ids
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