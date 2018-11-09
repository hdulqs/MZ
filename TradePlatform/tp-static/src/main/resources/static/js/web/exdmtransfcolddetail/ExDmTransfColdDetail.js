 /**
 * Copyright:   北京互融时代软件有限公司
 * exdmtransfcolddetail.js
 * @author:      shangxl
 * @version:     V1.0 
 * @Date:        2017-11-13 19:17:02 
 */
define([ 'app', 'hryTable', 'layer' ,"hryUtil",'pikadayJq'], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.web + "exdmtransfcolddetail/exdmtransfcolddetail/see/" + $stateParams.id).success(function(data) {
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
					url : HRY.modules.web + 'exDmTransfColdDetail/exdmtransfcolddetail/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/web/exdmtransfcolddetail/exdmtransfcolddetail/list/anon';
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

			$http.get(HRY.modules.web + "exdmtransfcolddetail/exdmtransfcolddetail/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {

				$http({
					method : 'POST',
					url : HRY.modules.web + 'exdmtransfcolddetail/exdmtransfcolddetail/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/web/exdmtransfcolddetail/exdmtransfcolddetail/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}
		
		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		if ($stateParams.page == "operation") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'exDmTransfColdDetail/exdmtransfcolddetail/listWalletBalance';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			

			config.columns = [{
				"data" : "id"   //id
			},{
				"data" : "coinCode"
			},{
				"data" : "totalMoney"
			},{
				"data" : "withdrawalsAddressMoney"
			},{
				"data":"withdrawalsAddress"
			},{
				"data":"coldwalletAddress"
			}]
			config.columnDefs = [{
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}
			]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
            // 导出excel
            $scope.fnExcel = function() {
                DT.excel(table, this.serchData, "币种冷账户明细");
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
					window.location.href = "#/web/exdmtransfcolddetail/exdmtransfcolddetail/see/" + ids[0];
				}
			}
			
			$scope.submitamount=function(){
				var toMoney=$('#modifyInfo').data('withdrawalsAddressMoney');
				var type=$('#modifyInfo').data('coinCode');
				var amount=$('#amount').val();
				toMoney=parseFloat(toMoney);
				amount=parseFloat(amount);
				if(toMoney>=amount){
					$.ajax({
						  type: 'post',
						  url: HRY.modules.web+"/exDmTransfColdDetail/exdmtransfcolddetail/toColdAccount",
						  data: {
							  type:type,
							  amount:amount
						  },
						  dataType: "json",
						  success: function(data){
							  if(data!=null&&data.success){
								  alert("操作成功");
							  }else if(data.msg!=null){
								  alert(data.msg);
								  return false;
							  }else{
								  alert("后台处理异常！");
								  return false;
							  }
							  layer.closeAll();
						  }
						});
				}else{
					layer.closeAll();
					alert("余额不足");
					return false;
				}
			}
			
			$scope.toColdAccount=function(){

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var coinCode=DT.getRowData(table)[0].coinCode;
					var withdrawalsAddressMoney=DT.getRowData(table)[0].withdrawalsAddressMoney;
					$('#modifyInfo').removeClass("hide");
					$('#modifyInfo').data('coinCode',coinCode);
					$('#modifyInfo').data('withdrawalsAddressMoney',withdrawalsAddressMoney);
					layer.open({
						type : 1,
						title : "请输入转出金额：",
						closeBtn : 2,
						area : [ '450px', '205px' ],
						shadeClose : true,
						zIndex :778,
						content : $('#modifyInfo')
					});
				}
			}
			
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/web/exdmtransfcolddetail/exdmtransfcolddetail/add/anon";
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
        			window.location.href='#/web/exdmtransfcolddetail/exdmtransfcolddetail/modify/'+ids[0];
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
						url : HRY.modules.web + "exdmtransfcolddetail/exdmtransfcolddetail/remove/"+ ids
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
		

		/**
		 * ------------------------代币资产信息  start---------------------------------------------
		 */
		if ($stateParams.page == "listTokenAssets") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = false; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'exDmTransfColdDetail/exdmtransfcolddetail/listTokenAssets';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			
			config.columns = [{
				"data" : "id"   //id
			},{
				"data" : "address"
			},{
				"data" : "tokenAssets"
			},{
				"data" : "etherAssets"
			},{
				"data":"abledCollect"
			}]
			config.columnDefs = [{
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			},{
				"targets" : 4,
				"orderable" : false,
				"render" : function(data, type, row) {
					return data?"是":"否";
				}
			}
			]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
			
			
			$scope.submitamount=function(){
				var amount=$('#amount').val();
				var address=DT.getRowData(table)[0].address;
				//币种
				var coinType=$("#input_lname").val();
				if(amount>0){
					if(address!=''&&address!=undefined){
						$.ajax({
							  type: 'post',
							  url: HRY.modules.web+"/exDmTransfColdDetail/exdmtransfcolddetail/rechargeTxFee2TokenAddress",
							  data: {
								  amount:amount,
								  address:address,


								  coinType:coinType
							  },
							  dataType: "json",
							  success: function(data){
								  if(data!=null&&data.success){
									  alert("操作成功");
								  }else if(data.msg!=null){
									  alert(data.msg);
								  }else{
									  alert("后台处理异常！");
								  }
								  layer.closeAll();
							  }
							});	
					}else{
						layer.closeAll();


						alert("地址无效");
						return false;
					}
				}else{
					layer.closeAll();
					alert("余额不足");
					return false;
				}
			}
			
			
			/**
			 * 充值旷工费
			 */
			$scope.rechargeEther=function(){

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var coinCode=DT.getRowData(table)[0].coinCode;
					var withdrawalsAddressMoney=DT.getRowData(table)[0].withdrawalsAddressMoney;
					$('#modifyInfo').removeClass("hide");
					$('#modifyInfo').data('coinCode',coinCode);
					$('#modifyInfo').data('withdrawalsAddressMoney',withdrawalsAddressMoney);
					layer.open({
						type : 1,
						title : "请输入转入以太坊金额：",
						closeBtn : 2,
						area : [ '450px', '205px' ],
						shadeClose : true,
						zIndex :778,
						content : $('#modifyInfo')
					});
				}
			}
			
			/**
			 * 归集代币
			 */
			$scope.collect=function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					//代币余额
					var tokenAssets=DT.getRowData(table)[0].tokenAssets;
					//地址
					var address=DT.getRowData(table)[0].address;
					//币种
					var coinType=$("#input_lname").val();
					$.ajax({
						  type: 'post',
						  url: HRY.modules.web+"/exDmTransfColdDetail/exdmtransfcolddetail/tokenCollect",
						  data: {
							  coinType:coinType,
							  amount:tokenAssets,
							  address:address
						  },
						  dataType: "json",
						  success: function(data){
							  if(data!=null&&data.success){
								  alert("操作成功");
							  }else if(data.msg!=null){
								  alert(data.msg);
								  return false;
							  }else{
								  alert("后台处理异常！");
								  return false;
							  }
						  }
						});
				
				}
			}
		}
		
		/**
		 * ---------------------------------代币资产信息 end--------------------------------
		 */
		
		

		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.web + 'exDmTransfColdDetail/exdmtransfcolddetail/list';
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
				"data" : "toAddress"   //toAddress
			},{
				"data" : "amount"   //amount
			},{
				"data" : "operator"   //操作人员
			},{
				"data" : "tx"   //流水号
			},{
				"data" : "coinCode"
			},{
				"data" : "created"
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
					window.location.href = "#/web/exdmtransfcolddetail/exdmtransfcolddetail/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/web/exdmtransfcolddetail/exdmtransfcolddetail/add/anon";
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
        			window.location.href='#/web/exdmtransfcolddetail/exdmtransfcolddetail/modify/'+ids[0];
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
						url : HRY.modules.web + "exdmtransfcolddetail/exdmtransfcolddetail/remove/"+ ids
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