 /**
 * Copyright:   北京互融时代软件有限公司
 * AppPersonInfo.js
 * @author:      zhangcb
 * @version:     V1.0 
 * @Date:        2016-11-22 18:25:52
 */
define([ 'app', 'hryTable', 'layer','module_md5','hryUtil' ], function(app, DT, layer,md5) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope','layer','$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		/**
		 * -----------------------注册用户审核-审核---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.customer + "person/apppersoninfo/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});
			
			//关闭按钮
			$scope.closeExamine=function(){
				window.location.href = '#/customer/person/apppersoninfo/list/anon';
			}
			//审核通过按钮
			$scope.examineAgree=function(){
					$('#examinePass').removeClass("hide");
					layer.open({
						type : 1,
						title : "审核通过",
						closeBtn : 2,
						area : [ '400px', '300px' ],
						shadeClose : true,
						content : $('#examinePass')
					});
			}
			//通过按钮
			$scope.examinesubmit=function(){
				layer.confirm('你确定要通过审核吗？', {
					btn : [ '确定', '取消' ]
				}, function() {
					$http({
						method : 'POST',
						url : HRY.modules.customer + "person/apppersoninfo/updateExamine",
						data : $.param({
							id:$stateParams.id,
							customerId:$scope.formData.customerId,
							isExamine:1//通过
						}),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
						if (data.success) {
							growl.addInfoMessage(data.msg);
						} else {
							growl.addInfoMessage(data.msg)
						}
						layer.closeAll();
						window.location.href="#/customer/person/apppersoninfo/list/anon";
					});
				});
			}
			
			//拒绝按钮
			$scope.examineRefuse=function(){
				$('#examineRefuse').removeClass("hide");
				layer.open({
					type : 1,
					title : "审核拒绝",
					closeBtn : 2,
					area : [ '400px', '400px' ],
					shadeClose : true,
					content : $('#examineRefuse')
				});
			}
			//确认拒绝
			$scope.Refuseconfirm=function(){
				var eamineRefusalReason=this.formData.eamineRefusalReason;
				layer.confirm('你确定要拒绝审核吗？', {
					btn : ['确定', '取消']
				}, function() {
					$http({
						method : 'POST',
						url : HRY.modules.customer + "person/apppersoninfo/updateExamine",
						data : $.param({
							id:$stateParams.id,
							customerId:$scope.formData.customerId,
							isExamine:2,//拒绝
							eamineRefusalReason:eamineRefusalReason
						}),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
						if (data.success) {
							growl.addInfoMessage(data.msg);
						} else {
							growl.addInfoMessage(data.msg)
						}
						layer.closeAll();
						window.location.href="#/customer/person/apppersoninfo/list/anon";
					});
				});
			}
		}
		
		/**
		 * ------------------------查看用户信息--------------------------------------------
		 */
		if ($stateParams.page == "manageSee") {
			$http.get(HRY.modules.customer + "person/apppersoninfo/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});
			
			//关闭按钮
			$scope.closeExamine=function(){
				window.location.href = '#/customer/person/apppersoninfo/managelist/anon';
			}
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {

			$scope.formData = {};
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.customer + 'person/apppersoninfo/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/customer/person/apppersoninfo/list/anon';
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

			$http.get(HRY.modules.customer + "person/apppersoninfo/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});

			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : HRY.modules.customer + 'person/apppersoninfo/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/customer/person/apppersoninfo/list/anon';
					} else {
						growl.addInfoMessage(data.msg)
					}
				});

			};

		}
		/**
		 * ------------------------用户审核拒绝列表------------------------------------------
		 */
		if ($stateParams.page == "refuselist") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			config.onlyClick = function(b){
        		$http.get(HRY.modules.customer + "person/apppersoninfo/see/"+b.attr("name")).
	            	success(function(data2) {
	            		if(data2 != ''){
	            			$scope.appPersonInfo=data2
	            			openWindow();
	            		}
	            	});
        	    };
			
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; //取消单行事件  
			config.ajax.url = HRY.modules.customer + 'person/apppersoninfo/list?_isExamine=2';//查询拒绝的
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			{
				"data" : "customerId"   //customerId
			},
			{
				"data" : "mobilePhone"   //手机、用户账号
			},
			{
				"data" : "trueName"   //真实姓名
			},
			{
				"data" : "cardType"   //证件类型
			},
			{
				"data" : "cardId"   //证件号码
			},
			{
				"data" : "created"   //申请时间
			},
			{
				"data" : "sex"   //归属会员
			},{
				"data" : "id"   //操作
			},{
				"data" : "eamineRefusalReason"//拒绝原因
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
					return data==0?'身份证':'护照';
				}
			},{
				"targets" : 7,
					
				"render" : function(data, type, row) {
					return "<input type='button' name='"+data+"' id='seeImage' value='查看照片'></input>"
				}
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			
			 // 弹出 查看图片窗口      
            function openWindow(){
        		$("#lockCustomer").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "查询用户照片",
							closeBtn : 2,
							area : [ '900px', '500px' ],
							shadeClose : true,
							content : $('#lockCustomer')
						});
        	}
     		// 关闭产看图片窗口      
            $scope.submitTmie = function (){
                		layer.closeAll();
            	}
            	
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
		}
		/**
		 * ------------------------注销用户查看------------------------------------------
		 */
		if ($stateParams.page == "canclelist") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			config.onlyClick = function(b){
				$http.get(HRY.modules.customer + "person/apppersoninfo/see/"+b.attr("name")).
				success(function(data2) {
					if(data2 != ''){
						$scope.appPersonInfo=data2
						openWindow();
					}
				});
			};
			
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; //取消单行事件  
			config.ajax.url = HRY.modules.customer + 'person/apppersoninfo/list?_isCancle=1';//查询注销的数据
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			                  {
			                	  "data" : "customerId"   //customerId
			                  },
			                  {
			                	  "data" : "mobilePhone"   //手机、用户账号
			                  },
			                  {
			                	  "data" : "trueName"   //真实姓名
			                  },
			                  {
			                	  "data" : "cardType"   //证件类型
			                  },
			                  {
			                	  "data" : "cardId"   //证件号码
			                  },
			                  {
			                	  "data" : "created"   //注册时间
			                  },
			                  {
			                	  "data" : "vipName"   //归属会员
			                  },{
			                	  "data" : "id"   //操作
			                  },{
			                	  "data" : "cancleReason"//注销原因
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
					return data==0?'身份证':'护照';
				}
			},{
				"targets" : 7,
				
				"render" : function(data, type, row) {
					return "<input type='button' name='"+data+"' id='seeImage' value='查看照片'></input>"
				}
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			
			// 弹出 查看图片窗口      
			function openWindow(){
				$("#lockCustomer").removeClass("hide")
				// 弹出选择窗体
				layer.open({
					type : 1,
					title : "查询用户照片",
					closeBtn : 2,
					area : [ '900px', '500px' ],
					shadeClose : true,
					content : $('#lockCustomer')
				});
			}
			// 关闭产看图片窗口      
			$scope.submitTmie = function (){
				layer.closeAll();
			}
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
		}
		
		/**
		 * ------------------------注册用户审核列表---------------------------------------------
		 */
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			config.onlyClick = function(b){
        		$http.get(HRY.modules.customer + "person/apppersoninfo/see/"+b.attr("name")).
	            	success(function(data2) {
	            		if(data2 != ''){
	            			$scope.appPersonInfo=data2
	            			openWindow();
	            		}
	            	});
        	    };
			
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; //取消单行事件  
			config.ajax.url = HRY.modules.customer + 'person/apppersoninfo/list';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			{
				"data" : "customerId"   //customerId
			},
			{
				"data" : "mobilePhone"   //手机、用户账号
			},
			{
				"data" : "trueName"   //真实姓名
			},
			{
				"data" : "cardType"   //证件类型
			},
			{
				"data" : "cardId"   //证件号码
			},
			{
				"data" : "created"   //申请时间
			},
			/*{
				"data" : "sex"   //代理商名称
			},
			{
				"data" : "sex"   //会员单位
			},*/
			/*{
				"data" : "appBankCard.cardBank"   //银行名称
			},
			{
				"data" : "appBankCard.cardNumber"   //银行卡号
			},*/
			{
				"data" : "id"   //操作
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
					return data==0?'身份证':'护照';
				}
			},{
				"targets" : 6,
					
				"render" : function(data, type, row) {
					return "<input type='button' name='"+data+"' id='seeImage' value='查看照片'></input>"
				}
			}]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			
			 // 弹出 查看图片窗口      
            function openWindow(){
        		$("#lockCustomer").removeClass("hide")
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "查询用户照片",
							closeBtn : 2,
							area : [ '900px', '500px' ],
							shadeClose : true,
							content : $('#lockCustomer')
						});
        	}
     		// 关闭产看图片窗口      
            $scope.submitTmie = function (){
                		layer.closeAll();
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
				var obj = DT.getRowData(table)[0];
				debugger;
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}else if(obj.isExamine!=null&&obj.isExamine!=0){
					growl.addInfoMessage('已审核');
				}else{
					window.location.href = "#/customer/person/apppersoninfo/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/customer/person/apppersoninfo/add/anon";
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
        			window.location.href='#/customer/person/apppersoninfo/modify/'+ids[0];
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
						url : HRY.modules.web + "person/apppersoninfo/remove/"+ ids
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
		 * ------------------------用户信息管理-修改---------------------------------------------
		 * 
		 */
		 if($stateParams.page == "manageModify") {/*
			 	
			 	var table = $("#dataTable");
			 	var selectData = DT.getRowData(table);
				$scope.setPwData = selectData[0];
				var appersonInfo=$scope.setPwData;
				if(appersonInfo.appBankCard.cardBank!=null){
					$("#temp").text(appersonInfo.appBankCard.cardBank);
				}else{
					$("#temp").remove();
				}
				//银行列表
				$http({
					method : 'POST',
					url : HRY.modules.web + "dictionary/appdiconelevel/findbykey?Q_pDickey_eq_String=bank",
 					headers : {
 						'Content-Type' : 'application/x-www-form-urlencoded'
 					}
 				})
 				.success(function(data) {
 					for(var x of data){
 						$("#banklist").append("<option value=\""+x.dicKey+"\">"+x.itemName+"</option>");
 					}
 				});
				
				$scope.fnManageModify = function(){
					var bankName=$("#banklist").find("option:selected").text();
	 				$http({
						method : 'POST',
						url : HRY.modules.customer + 'person/apppersoninfo/manageModify',
						data : $.param({
							id:this.setPwData.id,
							customerId:this.setPwData.customerId,
							mobilePhone:this.setPwData.mobilePhone,
							email:this.setPwData.email,
							bankName:bankName,
							bankCardNumber:this.setPwData.appBankCard.cardNumber
						}), 
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
	 				});
				}
		 */}
		/**
		 * ------------------------用户信息管理列表---------------------------------------------
		 */
		if ($stateParams.page == "managelist") {
			var table = $("#dataTable");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			config.onlyClick = function(b){
				$http.get(HRY.modules.customer + "person/apppersoninfo/see/"+b.attr("name")).
				success(function(data2) {
					if(data2 != ''){
						$scope.appPersonInfo=data2
						openWindow();
					}
				});
			};
			
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; //取消单行事件  
			config.ajax.url = HRY.modules.customer + 'person/apppersoninfo/list';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			                  {
			                	  "data" : "customerId"   //customerId
			                  },
			                  {
			                	  "data" : "mobilePhone"   //手机、用户账号
			                  },
			                  {
			                	  "data" : "trueName"   //真实姓名
			                  },
			                  {
			                	  "data" : "cardType"   //证件类型
			                  },
			                  {
			                	  "data" : "cardId"   //证件号码
			                  },
			                  {
			                	  "data" : "created"   //注册时间
			                  },
			                  {
			                	  "data" : "agentName"   //代理商名称
			                  },
			                  {
			                	  "data" : "vipName"   //会员单位
			                  },
			                  {
			                	  "data" : "appBankCard.cardBank"   //银行名称
			                  },
			                  {
			                	  "data" : "appBankCard.cardNumber"   //银行卡号
			                  },
			                  {
			                	  "data" : "isCancle"   //是否注销
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
					return data==0?'身份证':'护照';
				}
			},{
				"targets" : 10,
				"orderable" : false,
				"render" : function(data, type, row) {
					return data==1?'是':'否';
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
			
			/**
			 * 查看按钮
			 */
			$scope.ManagefnSee = function(){
				var ids = DT.getSelect(table);
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				} else {
					window.location.href = "#/customer/person/apppersoninfo/manageSee/" + ids[0];
				}
			}
			
			/**
			 * 修改按钮
			 */
			$scope.ManagefnModify = function(){
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}else{
					window.location.href = "#/customer/person/apppersoninfo/manageModify/" + ids[0];
				}
				
			}
			
			/**
			 * 重置密码
			 */
			$scope.ManagefnsetPwd=function(){
				var ids = DT.getSelect(table);
				var apppersonInfo = DT.getRowData(table)[0];
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}else if(apppersonInfo.isCancle==1){
					growl.addInfoMessage('此用户已注销')
					return false;
				}
				
				var selectData = DT.getRowData(table);
				$scope.setPwData = {
						id : selectData[0].customerId,
						userName : selectData[0].trueName
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
				
				/**
				 * 关闭弹窗
				 */
				$scope.close=function(){
					layer.closeAll();
				}
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
			 * 注销用户
			 */
			$scope.ManagefnCancel=function(){
				var ids = DT.getSelect(table);
				var apppersonInfo = DT.getRowData(table)[0];
				debugger;
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}else if(apppersonInfo.isCancle==1){
					growl.addInfoMessage('此用户已注销')
					return false;
				}
				$scope.setPwData = apppersonInfo;
				$('#setCancle').removeClass("hide");
				layer.open({
					type : 1,
					title : "注销",
					closeBtn : 2,
					area : [ '400px', '300px' ],
					shadeClose : true,
					content : $('#setCancle')
				});
				
				/**
				 * 关闭弹窗
				 */
				$scope.close=function(){
					layer.closeAll();
				}
				$scope.fnsetCancle = function(){
					var _this=this;
	 				$http({
						method : 'POST',
						url : HRY.modules.customer + "person/apppersoninfo/modify",
						data : $.param({
							id:this.setPwData.id,
							cancleReason:this.setPwData.cancleReason,
							isCancle:1
						}), 
	 					headers : {
	 						'Content-Type' : 'application/x-www-form-urlencoded'
	 					}
	 				})
	 				.success(function(data) {
	 					if (data.success) {
	 						growl.addInfoMessage('完成注销')
	 						_this.fnList();
	 					} else {
	 						growl.addInfoMessage(data.msg)
	 					}
	 					layer.closeAll();
	 				});
					

				}
			}
		}
		/**
		 * ------------------------资料修改申请列表---------------------------------------------
		 */
		if ($stateParams.page == "modificationlist") {
			debugger;
			var table = $("#dataTable");
			$scope.serchData = {//可以设置属性
					
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			
			config.onlyClick = function(b){
				$http.get(HRY.modules.customer + "person/apppersoninfo/see/"+b.attr("name")).
				success(function(data2) {
					if(data2 != ''){
						$scope.appPersonInfo=data2
						openWindow();
					}
				});
			};
			
			config.bAutoSerch = true; // 是否开启键入搜索
			config.onlyEvent = false; //取消单行事件  
			config.ajax.url = HRY.modules.customer + 'person/apppersoninfo/list';
			config.ajax.data = function(d) {
				$.each($scope.serchData, function(name, value) {
					if ("" != value) {
						eval("d." + name + "='" + value + "'");
					}
				});
			}
			config.columns = [ 
			                  {
			                	  "data" : "customerId"   //customerId
			                  },
			                  {
			                	  "data" : "mobilePhone"   //手机、用户账号
			                  },
			                  {
			                	  "data" : "trueName"   //真实姓名
			                  },
			                  {
			                	  "data" : "cardType"   //证件类型
			                  },
			                  {
			                	  "data" : "cardId"   //证件号码
			                  },
			                  {
			                	  "data" : "created"   //注册时间
			                  },
			                  {
			                	  "data" : "agentName"   //代理商名称
			                  },
			                  {
			                	  "data" : "vipName"   //会员单位
			                  },
			                  {
			                	  "data" : "appBankCard.cardBank"   //银行名称
			                  }
			                  ]
			config.columnDefs = [{
				"targets" : 0,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}
			}/*,{
				"targets" : 3,
				"orderable" : false,
				"render" : function(data, type, row) {
					return data==0?'身份证':'护照';
				}
			},{
				"targets" : 10,
				"orderable" : false,
				"render" : function(data, type, row) {
					return data==1?'是':'否';
				}
			}*/]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}
			
			/**
			 * 通过
			 */
			$scope.modificationPass=function(){
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				var selectData = DT.getRowData(table);
				
				$scope.param="手机号码";
				
				$('#PassWindow').removeClass("hide");
				layer.open({
					type : 1,
					title : "确认",
					closeBtn : 2,
					area : [ '400px', '300px' ],
					shadeClose : true,
					content : $('#PassWindow')
				});
				
				$scope.fnPass = function(){//通过
					debugger;
					return ;
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
			 * 拒绝
			 */
			$scope.modificationRefuse=function(){
				var ids = DT.getSelect(table)
				if (ids.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				} else if (ids.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}
				
				var selectData = DT.getRowData(table);
				$scope.setPwData = selectData[0];
				
				$('#RefuseWindow').removeClass("hide");
				layer.open({
					type : 1,
					title : "驳回",
					closeBtn : 2,
					area : [ '400px', '300px' ],
					shadeClose : true,
					content : $('#RefuseWindow')
				});
				
				$scope.fnRefuse = function(){
					return;
					var _this=this;
					$http({
						method : 'POST',
						url : HRY.modules.customer + "person/apppersoninfo/modify",
						data : $.param({
							id:this.setPwData.id,
							cancleReason:this.setPwData.cancleReason,
							isCancle:1
						}), 
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
						if (data.success) {
							growl.addInfoMessage('完成注销')
							_this.fnList();
						} else {
							growl.addInfoMessage(data.msg)
						}
						layer.closeAll();
					});
					
					
				}
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