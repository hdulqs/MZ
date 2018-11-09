/**
 * student.js
 */
define([ 'app', 'hryTable', 'layer','module_md5' ,'hryUtil','pikadayJq'], function(app, DT,layer,md5) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams' ];
	function controller($scope, $rootScope, $http, $stateParams, hryCore) {
		$rootScope.headTitle = $rootScope.title = "客户账号管理";
		// 初始化js 插件

		

		// ------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			hryCore.CURD({
				url : HRY.modules.customer + "user/appcustomer/see/" + $stateParams.id
			}).get(null, function(data) {
				$scope.model = data;

			}, function(data) {
				growl.addInfoMessage("error:" + data.msg, '1000');
			});
			
			// ()
			$scope.fnPublicKeySee = fnPublicKeySee;// 查看钱包地址
			$scope.fnBalanceSee = fnBalanceSee;// 查看账户余额
			$scope.fnRechargeSee = fnRechargeSee;// 查看充值流水
			$scope.fnWithdrawSee = fnWithdrawSee;// 查看提现流水
			$scope.fnFinanceApplySee = fnFinanceApplySee;// 查看融资申请流水
			$scope.fnFinanceReturn = fnFinanceReturn;// 查看融资归还流水
			$scope.fnRechargeCoinSee = fnRechargeCoinSee;// 查看充币流水
			$scope.fnWithdrawCoinSee = fnWithdrawCoinSee;// 查看取币流水
			$scope.fnCommissionDetailSee = fnCommissionDetailSee;// 查看佣金明细

			// 查看钱包地址
			function fnPublicKeySee() {
				window.location.href = '#/exchange/transaction/exdmcustomerpublickey/list/anon?id=' + $scope.model.id;
			}

			// 查看账户余额
			function fnBalanceSee() {
				window.location.href = '#/exchange/account/exdigitalmoneyaccount/list/anon?name=' + $scope.model.userName;
			}

			// 查看充值流水
			function fnRechargeSee() {
				window.location.href = '#/account/fund/apptransaction/dsuccesslist/2?name=' + $scope.model.userName;
			}

			// 查看提现流水
			function fnWithdrawSee() {
				window.location.href = '#/account/fund/apptransaction/wsuccesslist/2?name=' + $scope.model.userName;
			}

			// 查看融资申请流水
			function fnFinanceApplySee() {
				window.location.href = '#/exchange/lend/exdmLend/find?id=' + $scope.model.id;
			}

			// 查看融资归还流水
			function fnFinanceReturn() {
				window.location.href = '#/exchange/lend/exdmLendIntent/find?id=' + $scope.model.id;
			}

			// 查看充币流水
			function fnRechargeCoinSee() {
				
				window.location.href = '#/exchange/transaction/exdmtransaction/DsuccessList?name=' + $scope.model.userName;

			}

			// 查看取币流水
			function fnWithdrawCoinSee() {

				window.location.href = '#/exchange/transaction/exdmtransaction/WsuccessList?name=' + $scope.model.userName;
			}
			//查看佣金明细
			function fnCommissionDetailSee(){
				
				window.location.href = '#/customer/agents/commissiondetail/list/anon?name=' + $scope.model.userName;
				
			}
			
		}
		// ------------------------增加页面路径---------------------------------------------
		if ($stateParams.page == "add") {
        	
        	$scope.formData = {};
			$scope.processForm = function() {
				//证件类型
				$scope.formData.cardType= $("#cardType").val();
				var reg = /^1\d{10}$/;
				if (!reg.test($scope.formData.userName)) {
					layer.msg("手机号格式不正确", {
						icon : 1,
						time : 2000
					});
					return false;
				}
				
				if(!checkPassWord($scope.formData.passWord)){
					layer.msg("新密码格式不正确!", {
						icon : 1,
						time : 2000
					});
					return false;
				}
				
				if(!isIdCardNo($scope.formData.cardId)){
					layer.msg('身份证号格式不正确', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
					return false;
				}
				$scope.formData.passWord=md5.hbmd5($scope.formData.passWord);
				
				$http({
					method : 'POST',
					url : HRY.modules.customer+'user/appcustomer/add',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功');
						window.location.href = '#/customer/user/appcustomer/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});
				
			};
        
		}
		// ------------------------列表页面路径---------------------------------------------
		if ($stateParams.page == "list") {
			var table = $("#table2");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			config.onlyClick = function(b){
				if(b.attr("src")=='1'){
					$http.get(HRY.modules.customer + "person/apppersoninfo/seeByCustomerId/"+b.attr("name")).
					success(function(data2) {
						if(data2 != ''){
							$scope.appPersonInfo=data2;
							openPicWindow();
						}
					});
				}else if(b.attr("src")=='2'){
					apiKeyWindow();
					$("#apiKeyId").val(b.attr("name"));
				}
			}
				config.bAutoSerch = true; // 是否开启键入搜索
				config.ajax.url = HRY.modules.customer + 'user/appcustomer/list.do';
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
			},{
				"data" : "userName"
			}, {
				"data" : "appPersonInfo.surname"
			}, {
				"data" : "appPersonInfo.trueName"
			}, {
				"data" : "appPersonInfo.cardType"
			}, {
				"data" : "appPersonInfo.cardId"
			}, {
				"data" : "phone"
			},{
                "data" : "mail"
            },{
				"data" : "created"
					
			}, {
				"data" : "googleState"
			},{
				"data" : "phoneState"
			},{
				"data" : "states"
			},
			 {
				"data" : "appPersonInfo.customerType"
			}, /*{
				"data" : "isLock"
			},*/ {
				"data" : "isDelete"
			} , {
				"data" : "isChange"
			},
			{
				"data" : "hasEmail"
			},
			/* {
				"data" : "appPersonInfo.jkApplyAuthorizationCode"
			},*/{
				"data" : "id"
			},{
				"data" : "id"
			}
			 ]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,

				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}, {
				"targets" : 4,

				"render" : function(data, type, row) {
					if (data != null && data == "0") {
						return "身份证"
					}
					return "其他"
				}
			}, {
				"targets" : 9,

				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "已认证"
					}else if(data=="0"){
						return "未认证"
					}
				}
			},{
				"targets" : 10,

				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "已认证"
					}else if(data=="0"){
						return "未认证"
					}
				}
			},{
				"targets" : 11,

				"render" : function(data, type, row) {
					if (data != null && data == "2") {
						return "已实名"
					}else if(data=="1"){
						return "待审核"
					}else if(data=="3"){
						return "已拒绝"
					}
					return "未实名"
				}
			}, {
				"targets" : 12,
				"render" : function(data, type, row) {
					 
					if (data != null && data == 1) {
						return "普通账户";
					} else if (data != null && data == 2) {
						return "操盘账户";
					} /*else if (data != null && data == 3) {
						return "丙类";
					}*/
					return "";
				}
			}, /*{
				"targets" : 8,

				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "已锁定"
					}
					return "未锁定"
				}
			},*/ {
				"targets" : 13,

				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "已禁用"
					}
					return "未禁用"
				}
			} , {
				"targets" :14,
				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "未开启"
					}
					return "已开启"
				}
			} ,{
				"targets" : 15,
				"render" : function(data, type, row) {
					if (data != null && data == "1") {
						return "已激活"
					}else{
						return "未激活"
					}
				}
			} ,{
				"targets" : 16,
					
				"render" : function(data, type, row) {
//					var seePic=$rootScope.requiresPermissions('customer','/user/appcustomer/seePic');
//					if(seePic){
						return "<input type='button' src='1' name='"+data+"' id='seeImage' value='查看照片'></input>"
//					}else{
//						return "";
//					}
				}
			},{
				"targets" : 17,
				
				"render" : function(data, type, row) {
					return "<input type='button' src='2' name='"+data+"' value='申请ApiKey'></input>"
				}
			}]
			DT.draw(table, config);
			// --------------------加载dataTable--------------------------------

			$scope.fnSee = fnSee;// see按钮方法
			$scope.fnModify = fnModify;// 修改按钮方法
			$scope.modifySubmit = modifySubmit;//修改提交按钮方法
			$scope.fnRemove = fnRemove;// remove方法
			$scope.fnList = fnList;// 刷新方法
			$scope.fnForbidden = fnForbidden;
			$scope.fnLock = fnLock;
			$scope.fnUnlock = fnUnlock;
			$scope.fnPermissible = fnPermissible;
			
			// 弹出 查看图片窗口      
            function openPicWindow(){
        		$("#showPic").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "查询用户照片",
							closeBtn : 2,
							area : [ '85%', '85%' ],
							shadeClose : true,
							content : $('#showPic')
						});
        	}
            function apiKeyWindow(){
        		$("#apiKeyDiv").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "申请ApiKey",
							closeBtn : 2,
							area : [ '75%', '75%' ],
							shadeClose : true,
							content : $('#apiKeyDiv'),
							success: function(){
								$("#apikeySumb").on('click',function(){
									$("#apikeySumb").attr("disabled",true);
									var apiKeyId = $("#apiKeyId").val();
									var apiKeyIp = $("#apiKeyIp").val();
									$.ajax({
										type : "POST",
										dataType : "JSON",
										url : HRY.modules.customer+'user/appcustomer/applyApi',
										data : {id:apiKeyId,ip:apiKeyIp},
										cache : false,
										success : function(data) {
											$("#apikeySumb").attr("disabled",false);
											layer.msg("申请成功："+data.msg+"，请复制下您的Key，8S之后关闭.", {icon: 1,time:8000},function(){
												layer.closeAll();
											})
										}
									})
								})
							}
						});
        	}
     		// 关闭产看图片窗口      
            $scope.submitTmie = function (){
                		layer.closeAll();
            	}
			//导出Excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"会员信息");
			}
			
			//增加会员
			$scope.fnAdd = function() {
				window.location.href = '#/customer/user/appcustomer/add/anon';
			}
			/**
			 * 提现审核额度
			 */
			$scope.fnWithdrawCheckMoney = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				
				var selectData = DT.getRowData(table);
				var ddd=selectData[0].appPersonInfo.withdrawCheckMoney;
				$scope.setWithdrawCheckMoneyData = {
						id : selectData[0].id,
						money : selectData[0].appPersonInfo.withdrawCheckMoney
				};
				
				$('#setWithdrawCheckMoneyDiv').removeClass("hide");
				layer.open({
					type : 1,
					title : "修改提现审核额度",
					closeBtn : 2,
					area : [ '400px', '300px' ],
					shadeClose : true,
					content : $('#setWithdrawCheckMoneyDiv')
				});
				
				
				$scope.fnSetWithdrawCheckMoneySubmit = function(){
					var money = $scope.setWithdrawCheckMoneyData.money;
					if("-1"==money){
						
					}else if (!(/(^[1-9]\d*$)/.test(money))){
						$("#withdrawCheckMoney").val("");
						layer.msg("格式不正确", {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
					     return false;
					}
	 				$http({
						method : 'POST',
						url : HRY.modules.customer + "user/appcustomer/setWithdrawCheckMoney",
						data : $.param($scope.setWithdrawCheckMoneyData), 
	 					headers : {
	 						'Content-Type' : 'application/x-www-form-urlencoded'
	 					}
	 				})
	 				.success(function(data) {
	 					if (data.success) {
	 						growl.addInfoMessage('修改成功')
	 					} else {
	 						growl.addInfoMessage(data.msg)
	 					}
	 					layer.closeAll();
	 					fnList();
	 				});
					

				}
				
			}
			/**
			 * 设置会员角色 甲乙丙
			 */
			$scope.menuDialog = function(){
				$("#panel").slideToggle("slow");
			}
			$scope.fnSetCutomerType = function(customerType) {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}

				layer.confirm('你确定要更改吗？', {
					btn : [ '确定', '取消' ],
					ids : ids,
					customerType : customerType
				}, function() {

					layer.closeAll();
					$.ajax({
						url : HRY.modules.customer + "user/appcustomer/setCustomerType/" + ids[0],
						method : 'POST',
						data : {
							customerType : customerType
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
                                layer.alert("更改成功");
								fnList();
							} else {
                                layer.alert("更改失败");
							}
						}
					});

				});
			};
			
			
			/**
			 * 重置密码
			 */
			$scope.fnSetPw = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				
				
				
				var selectData = DT.getRowData(table);
				$scope.setPwData = {
						id : selectData[0].id,
						userName : selectData[0].userName
				};
				
				$('#setPwDiv').removeClass("hide");
				layer.open({
					type : 1,
					title : "重置密码",
					closeBtn : 2,
					area : [ '400px', '300px' ],
					shadeClose : true,
					content : $('#setPwDiv')
				});
				
				
				$scope.fnSetPwSubmit = function(){
					var pw = $scope.setPwData.password;
					if(!checkPassWord(pw)){
						growl.addInfoMessage("密码格式不正确");
						return false;
					}
					
					$scope.setPwData.password=md5.hbmd5($scope.setPwData.password);
	 				$http({
						method : 'POST',
						url : HRY.modules.customer + "user/appcustomer/setpw",
						data : $.param($scope.setPwData), 
	 					headers : {
	 						'Content-Type' : 'application/x-www-form-urlencoded'
	 					}
	 				})
	 				.success(function(data) {
	 					if (data.success) {
	 						growl.addInfoMessage('修改成功')
	 					} else {
	 						growl.addInfoMessage(data.msg)
	 					}
	 					layer.closeAll();
	 				});
					

				}
				
			}
			/**
			 * 设置申请代理商授权码
			 */
			$scope.fnSetAuthorizationCode = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				
				//已经由授权码的就不需要生成了
				var selectData = DT.getRowData(table);
				
				var authorizationCode=selectData[0].appPersonInfo.jkApplyAuthorizationCode;
				if(authorizationCode!=""){
					growl.addInfoMessage('已经生成授权码了！')
					return false;
				}
				
				
				
				//传递到页面
				$scope.setAuthorizationCodeData = {
						id : selectData[0].id,
						userName:selectData[0].userName//手机号
						
				};
				
				$('#setAuthorizationCodeDataDiv').removeClass("hide");
				layer.open({
					type : 1,
					title : "设置申代授权码",
					closeBtn : 2,
					area : [ '400px', '300px' ],
					shadeClose : true,
					content : $('#setAuthorizationCodeDataDiv')
				});
				
				
				//提交
				$scope.setAuthorizationCodeDataDivSubmit = function(){
					var code = $scope.setAuthorizationCodeData.code;
					if(code==null || code==""){
						layer.closeAll();
						growl.addInfoMessage("授权码不能为空！");
						return false;
					}
					
					//提交
					$http({
						method : 'POST',
						url : HRY.modules.customer + "user/appcustomer/setAuthorizationCode",
						data : $.param($scope.setAuthorizationCodeData), 
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
						if (data.success) {
							growl.addInfoMessage('设置成功！')
						} else {
							growl.addInfoMessage(data.msg)
						}
						layer.closeAll();
						fnList();
					});
					
					
				}
				
			}

			// 禁用一个用户
			function fnForbidden() {

				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要禁用吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {

					layer.closeAll();
					$http.get(HRY.modules.customer + "user/appcustomer/forbidden/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("禁用成功");
							fnList();
						} else {
							growl.addInfoMessage("禁用失败");
						}
					});
				});

			}
			// 解除锁定一个用户
			function fnUnlock() {

				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要解锁吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/unlock/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("解除锁定成功");
							fnList();
						} else {
							growl.addInfoMessage(data.msg);
						}
						layer.closeAll();
					});
				});

			}

			// 禁用一个用户
			function fnPermissible() {

				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要解除禁用吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/permissible/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("解除禁用成功");
							fnList();
						} else {
							growl.addInfoMessage(data.msg);
						}
						layer.closeAll();
					});
				});

			}
			// 锁定一个用户
			function fnLock() {

				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据', 4000)
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据', 4000)
					return false;
				}
				$scope.custromerIdByTime = ids[0];
				openWindow();

			}
			

			function openWindow() {

				$("#lockCustomer").removeClass("hide")
				// 弹出选择窗体
				layer.open({
					type : 1,
					title : "锁定用户",
					closeBtn : 2,
					area : [ '600px', '300px' ],
					shadeClose : true,
					content : $('#lockCustomer')
				});
			}

			$scope.fnLockHandle = function() {
				var time = $("#lockTimeId").val();
				$http.get(HRY.modules.customer + "user/appcustomer/lock?time=" + time + "&id=" + $scope.custromerIdByTime).success(function(data) {
					layer.closeAll();
					if (data.success) {
						growl.addInfoMessage(data.msg, 4000);
						fnList();
					} else {
						growl.addInfoMessage(data.msg, 4000);
					}
				});
			}

			// 刷新按钮
			function fnList() {
				table.DataTable().draw();
			}

			/**
			 * 审核认证
			 */
			$scope.audit = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要提交审核吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/audit/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("审核成功");
							fnList();
						} else {
							growl.addInfoMessage("审核条件不满足");
						}
						layer.closeAll();
					});
				});
			}
			/**
			 * 关闭谷歌认证
			 */
			$scope.offgoogle = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要关闭谷歌认证吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/offgoogle/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("关闭谷歌认证成功");
							fnList();
						} else {
							growl.addInfoMessage("关闭谷歌认证失败");
						}
						layer.closeAll();
					});
				});
			}
			
			/**
			 * 关闭谷歌认证
			 */
			$scope.offphone = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要关闭手机认证吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/offphone/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("关闭手机认证成功");
							fnList();
						} else {
							growl.addInfoMessage("关闭手机认证失败");
						}
						layer.closeAll();
					});
				});
			}
			
			$scope.isHasemail =function(){
				
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要激活账户吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/isHasemail/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("激活账户成功");
							fnList();
						} else {
							growl.addInfoMessage("不满足激活条件");
						}
						layer.closeAll();
					});
				});
			}
			
			/**
			 * 审核认证
			 */
			$scope.auditall = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要清除实名信息吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/auditall/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("清除实名信息成功");
							fnList();
						} else {
							growl.addInfoMessage("清除条件不满足");
						}
						layer.closeAll();
					});
				});
			}

			/**
			 * 审核认证
			 */
			$scope.auditback = function() {
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				layer.confirm('你确定要提交审核吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					$http.get(HRY.modules.customer + "user/appcustomer/auditback/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("审核成功");
							fnList();
						} else {
							growl.addInfoMessage("审核条件不满足");
						}
						layer.closeAll();
					});
				});
			}
			
			
			/**
			 * 开启交易
			 */
			$scope.fnOpenChange = function() {
				$scope.formData = {};
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} 
				var postIds="";
				for(var i= 0 ; i < ids.length ; i++){
					postIds+=ids[i];
					if(i!=ids.length-1){
						postIds += ",";
					}
				}
				/*else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}*/
				$scope.formData.ids=postIds;
				//alert(postIds);
				layer.confirm('你确定要开启交易吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					
					$http({
						method : 'POST',
						url : HRY.modules.customer+'user/appcustomer/openBatch',
						data : $.param($scope.formData), 
						headers : {	
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("开启成功");
							fnList();
						} else {
							growl.addInfoMessage(data.msg);
						}
						layer.closeAll();
				    });
					/*$http.get(HRY.modules.customer + "user/appcustomer/close/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("禁止成功");
							fnList();
						} else {
							growl.addInfoMessage(data.msg);
						}
						layer.closeAll();
					});*/
				});
			}
			
			
			/**
			 * 关闭交易
			 */
			$scope.fnCloseChange = function() {

				$scope.formData = {};
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} 
				var postIds="";
				for(var i= 0 ; i < ids.length ; i++){
					postIds+=ids[i];
					if(i!=ids.length-1){
						postIds += ",";
					}
				}
				/*else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}*/
				$scope.formData.ids=postIds;
				//alert(postIds);
				layer.confirm('你确定要禁止交易吗？', {
					btn : [ '确定', '取消' ],
					ids : ids
				}, function() {
					
					$http({
						method : 'POST',
						url : HRY.modules.customer+'user/appcustomer/closeBatch',
						data : $.param($scope.formData), 
						headers : {	
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("禁止成功");
							fnList();
						} else {
							growl.addInfoMessage(data.msg);
						}
						layer.closeAll();
				    });
					/*$http.get(HRY.modules.customer + "user/appcustomer/close/" + ids[0]).success(function(data) {
						if (data.success) {
							growl.addInfoMessage("禁止成功");
							fnList();
						} else {
							growl.addInfoMessage(data.msg);
						}
						layer.closeAll();
					});*/
				});
			
			}
			
			// 查看按钮
			// ng-click="fnSee(url,selectes)"
			function fnSee(url) {
				var ids = DT.getSelect(table);
				// var ids = transform(selectes);
				if (ids.length == 0) {

					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = '#/customer/' + url + '/' + ids[0];
				}
			}

			// 修改按钮
			// ng-click="fnModify(url,selectes)"
			function fnModify() {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					var selectData = DT.getRowData(table)[0];
					$('#modifyInfo').removeClass("hide");
					$scope.person=selectData.appPersonInfo;
					layer.open({
						type : 1,
						title : "修改",
						closeBtn : 2,
						area : [ '450px', '400px' ],
						shadeClose : true,
						content : $('#modifyInfo')
					});
				}
			}
			//修改提交按钮
			function modifySubmit(){
				//检验身份证
				var success_flag = true;
				var params = this.person;
				if(!isIdCardNo(this.person.cardId)){
					growl.addInfoMessage('身份格式不正确');
					return false;
				}else{
					$http({
						method : 'POST',
						url : HRY.modules.customer+'user/appcustomer/checkCard',
						data : $.param(this.person), 
						headers : {	
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {							
			        	if(!data.success){
			        		layer.msg(data.msg, {
			        		    icon: 2,
			        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
			        		});
			        		success_flag = false;
			        		return false;
			        	}else{
			        		//处理				
							//this.person.appBankCard={};
			        		
			        		debugger;
							$http({
								method : 'POST',
								url : HRY.modules.customer+'user/appcustomer/modifycardIdInfo',
								data : $.param({
									"id"       : params.id,
									"cardId"   : params.cardId,
									"trueName" : params.trueName,
									"surname"  : params.surname
								}), 
								headers : {
									'Content-Type' : 'application/x-www-form-urlencoded'
								}
							}).success(function(data) {
								if(data.success){
									layer.closeAll();
									fnList();
								}
								layer.msg(data.msg, {
				        		    icon: 1,
				        		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				        		});
				        		
						    });
			        	}
				    });
				}
				
				
				
			}

			// 删除按钮
			// ng-click="fnRemove(url,selectes)"
			function fnRemove(url) {
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}

				var id = "";
				for (var i = 0; i < ids.length; i++) {
					id += ids[i];
					if (i != ids.length - 1) {
						id += ",";
					}
				}

				hryCore.CURD({
					url : HRY.modules.oauth + url + "/" + id
				}).remove(null, function(data) {
					if (data.success) {
						// 提示信息
						growl.addInfoMessage('删除成功')
						// 重新加载list
						fnList();
					} else {
						growl.addInfoMessage(data.msg)
					}
				}, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});

			}

            /**
             * 关闭OTC冻结
             */
            $scope.offotcFrozen = function() {
                var ids = DT.getSelect(table)
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }
                layer.confirm('你确定要OTC解冻吗？', {
                    btn : [ '确定', '取消' ],
                    ids : ids
                }, function() {
                    $http.get(HRY.modules.customer + "user/appcustomer/offotcFrozen/" + ids[0]).success(function(data) {
                        if (data.success) {
                            growl.addInfoMessage("OTC解冻成功");
                            fnList();
                        } else {
                            growl.addInfoMessage("OTC解冻失败");
                        }
                        layer.closeAll();
                    });
                });
            }
            /**
             * 开通OTC委托
             */
            $scope.openotc = function() {
                var ids = DT.getSelect(table)
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }
                layer.confirm('你确定要开通吗？', {
                    btn : [ '确定', '取消' ],
                    ids : ids
                }, function() {
                    $http.get(HRY.modules.customer + "user/appcustomer/openotc/" + ids[0]).success(function(data) {
                        if (data.success) {
                            growl.addInfoMessage("开通成功");
                            fnList();
                        } else {
                            growl.addInfoMessage("开通失败");
                        }
                        layer.closeAll();
                    });
                });
            }
            /**
             * 关闭OTC委托
             */
            $scope.closeotc = function() {
                var ids = DT.getSelect(table)
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }
                layer.confirm('你确定要关闭OTC委托吗？', {
                    btn : [ '确定', '取消' ],
                    ids : ids
                }, function() {
                    $http.get(HRY.modules.customer + "user/appcustomer/closeotc/" + ids[0]).success(function(data) {
                        if (data.success) {
                            growl.addInfoMessage("关闭OTC委托成功");
                            fnList();
                        } else {
                            growl.addInfoMessage("关闭OTC委托失败");
                        }
                        layer.closeAll();
                    });
                });
            }

            /**
             * 清除邮箱绑定
             */
            $scope.clearmail = function() {
                var ids = DT.getSelect(table)
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }
                layer.confirm('你确定要清除邮箱绑定吗？', {
                    btn : [ '确定', '取消' ],
                    ids : ids
                }, function() {
                    $http.get(HRY.modules.customer + "user/appcustomer/clearmail/" + ids[0]).success(function(data) {
                        if (data.success) {
                            growl.addInfoMessage("清除邮箱绑定成功");
                            fnList();
                        } else {
                            growl.addInfoMessage("清除邮箱绑定失败");
                        }
                        layer.closeAll();
                    });
                });
            }

		}

		hryCore.initPlugins();

	}
	return {
		controller : controller
	};
});