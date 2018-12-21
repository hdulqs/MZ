 /**
 * Copyright:   北京互融时代软件有限公司
 * ExCointoCoin.js
 * @author:      gaomimi
 * @version:     V1.0 
 * @Date:        2017-07-06 19:40:34 
 */
define([ 'app', 'hryTable', 'layer','hryUtil' ], function(app, DT, layer,hryUtil) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		/**
		 * ------------------------查看页面路径---------------------------------------------
		 */
		if ($stateParams.page == "see") {
			$http.get(HRY.modules.exchange + "product/excointocoin/see/" + $stateParams.id).success(function(data) {
				$scope.formData = data;
			});
		}

		/**
		 * ------------------------添加页面路径---------------------------------------------
		 */ 
		if ($stateParams.page == "add") {
           
			$scope.formData = {};
			$scope.processForm = function() {
				
				$scope.formData.coinCode=$('#coinCode option:selected').val();
				$scope.formData.fixPriceCoinCode=$('#fixPriceCoinCode option:selected').val();
				$scope.formData.fixPriceType=$('#fixPriceType option:selected').val();
				 if(isEmpty($scope.formData.coinCode)){
					 layer.msg("交易币种不能为空", {
	 		    		    icon: 1,
	 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	 		    		});
	 		          return false;
				
				 }
		       if(isEmpty($scope.formData.fixPriceType)){
					
					layer.msg("定价币种类型不能为空", {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
		          return false;
				}
			     if(isEmpty($scope.formData.fixPriceCoinCode)){
	 					
	 					layer.msg("定价币种不能为空", {
	 		    		    icon: 1,
	 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	 		    		});
	 		          return false;
	 				}
			     if($scope.formData.coinCode==$scope.formData.fixPriceCoinCode){
					 layer.msg("交易币种和定价币种不能相同", {
	 		    		    icon: 1,
	 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	 		    		});
	 		          return false;
				
				 }
			   /*  if(isEmpty($scope.formData.keepDecimalFixPrice)){
					 layer.msg("定价币种保留位数不能为空", {
	 		    		    icon: 1,
	 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	 		    		});
	 		          return false;
				
				 }*/
				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'product/excointocoin/add',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('添加成功')
						window.location.href = '#/exchange/product/excointocoin/list/anon';
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
			$http.get(HRY.modules.exchange + "product/excointocoin/see/" +$stateParams.ing).success(function(data) {
				$scope.formData = data;
				hryCore.RenderHTML(data);//异步回显控件
				 $scope.coinCode=$scope.formData.coinCode;
                 $scope.fixPriceCoinCode=$scope.formData.fixPriceCoinCode;
                 $scope.fixPriceType=$scope.formData.fixPriceType;
			});

			$scope.processForm = function() {
            /*    $scope.formData.coinCode=$("#coinCode").val();
				$scope.formData.fixPriceCoinCode=$("#fixPriceCoinCode").val();
				$scope.formData.fixPriceType=$("#fixPriceType").val();*/
				 
				$http({
					method : 'POST',
					url : HRY.modules.exchange + 'product/excointocoin/modify',
					data : $.param($scope.formData),
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data) {
					if (data.success) {
						growl.addInfoMessage('修改成功')
						window.location.href = '#/exchange/product/excointocoin/list/anon';
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
			config.ajax.url = HRY.modules.exchange + 'product/excointocoin/list';
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
				"data" : "coinCode"   //交易币种
			},
			{
				"data" : "fixPriceCoinCode"   //定价币种
			},
			{
				"data" : "fixPriceType"   //定价类型
			},
			{
				"data" : "state"   //开启/关闭交易
			},
			{
				"data" : "isOperate"   //是否操盘
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}}, {
					"targets" : 3,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (data == "0") {
							return "真实货币"
						}
						if (data == "1") {
							return "虚拟货币"
						}
						
					}
				}, {
					"targets" : 4,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (data == "0") {
							return "<font color='blue'>关闭交易</font>"
						}
						if (data == "1") {
							return "<font color='red'> 已开启交易</font>"
						}

					}
				},{
                "targets" : 5,
                "orderable" : false,
                "render" : function(data, type, row) {
                    if (data == "0") {
                        return "<font color='blue'>否</font>"
                    }
                    if (data == "1") {
                        return "<font color='red'>是</font>"
                    }

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

            /**
             * 从火币导入K线
             */
            $scope.fnImportKlineFromHuobi = function() {
                var arrId = DT.getSelect(table);
                if (arrId.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                }else if (arrId.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }

                var ids = hryCore.ArrayToString(arrId);

                layer.confirm("你确定要从火币导入K线吗？", {
                    btn : [ '确定', '取消' ], // 按钮
                    ids : ids
                }, function() {

                    layer.closeAll();

                    hryCore.CURD({
                        url : HRY.modules.web + '/product/excointocoin/importKline/'+ ids
                        }).remove(null, function(data) {
                        if (data.success) {
                            // 提示信息
                            growl.addInfoMessage('后台同步火币K线中')
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
			 * 开启操盘
			 */
			$scope.fnOperateOpen = function(){
                var arrId = DT.getSelect(table);
                if (arrId.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                }else if (arrId.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }

                var ids = hryCore.ArrayToString(arrId);

                layer.confirm("你确定开启操盘吗？", {
                    btn : [ '确定', '取消' ], // 按钮
                    ids : ids
                }, function() {

                    layer.closeAll();

                    hryCore.CURD({
                        url : HRY.modules.web + "product/excointocoin/openOperate/"+ ids
                    }).remove(null, function(data) {
                        if (data.success) {
                            // 提示信息
                            growl.addInfoMessage('开启操盘成功')
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
			 * 关闭操盘
			 */
			$scope.fnOperateClose = function(){
                var arrId = DT.getSelect(table);
                if (arrId.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                }else if (arrId.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }

                var ids = hryCore.ArrayToString(arrId);

                layer.confirm("你确定关闭操盘吗？", {
                    btn : [ '确定', '取消' ], // 按钮
                    ids : ids
                }, function() {

                    layer.closeAll();

                    hryCore.CURD({
                        url : HRY.modules.web + "product/excointocoin/closeOperate/"+ ids
                    }).remove(null, function(data) {
                        if (data.success) {
                            // 提示信息
                            growl.addInfoMessage('关闭操盘成功')
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
					window.location.href = "#/exchange/product/excointocoin/see/" + ids[0];
				}
			}
			
			/**
			 * 添加按钮
			 */
			$scope.fnAdd = function(){
				window.location.href = "#/exchange/product/excointocoin/add/anon";
			}
			/**
			 * 添加按钮
			 */
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
        			window.location.href='#/exchange/product/excointocoin/modify/'+ids[0];
            	}
			}
			/**
			 * 删除开启交易
			 */
			$scope.fnOpen = function() {
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else if (arrId.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确定开启交易吗？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "product/excointocoin/open/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('开启交易成功')
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
			 * 关闭交易按钮
			 */
			$scope.fnClose = function() {
				var arrId = DT.getSelect(table);
				if (arrId.length == 0) {
					growl.addInfoMessage('请选择数据')
					return false;
				}else if (arrId.length > 1) {
					growl.addInfoMessage('只能选择一条数据')
					return false;
				}

				var ids = hryCore.ArrayToString(arrId);

				layer.confirm("你确定关闭交易吗？", {
					btn : [ '确定', '取消' ], // 按钮
					ids : ids
				}, function() {

					layer.closeAll();
					
					hryCore.CURD({
						url : HRY.modules.web + "product/excointocoin/close/"+ ids
					}).remove(null, function(data) {
						if (data.success) {
							// 提示信息
							growl.addInfoMessage('关闭交易成功')
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
						url : HRY.modules.web + "product/excointocoin/remove/"+ ids
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
if ($stateParams.page == "listauto") {
			
			var table = $("#dataTableauto");
			$scope.serchData = {};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.exchange + 'product/excointocoin/listauto';
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
				"data" : "coinCode"   //产品名称
			},
			{
				"data" : "fixPriceCoinCode"   //产品名称
			},
			{
				"data" : "fixPriceType"   //产品名称
			},
			{
				"data" : "isSratAuto"   //
			},
			{
				"data" : "autoUsername"   //
			},
			{
				"data" : "atuoPriceType"   //
			},
			{
				"data" : "autoPrice"   //
			},
			{
				"data" : "autoPriceFloat"   //
			},
			{
				"data" : "autoCount"   //
			},
			{
				"data" : "autoCountFloat"   //
			},
			{
				"data" : "isHedge"   //
			}
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				}}, {
					"targets" : 3,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (data == "0") {
							return "真实货币"
						}
						if (data == "1") {
							return "虚拟货币"
						}
						
					}
				}, {
					"targets" : 4,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (data == "0") {
							return "<font color='blue'>关闭自动交易</font>"
						}
						if (data == "1") {
							return "<font color='red'> 已开启自动交易</font>"
						}
						
					}
				}, {
					"targets" : 6,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (data == 1) {
							return "定价下单"
						}
						if (data == 2) {
							return "市价下单"
						}
						if (data == 3) {
							return "参考火币限价下单"
						}
					}
				}, {
					"targets" : 7,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (row.atuoPriceType == 1) {
							return data
						} else {
							return ""
						}
						
					}
				}, {
					"targets" : 11,
					"orderable" : false,
					"render" : function(data, type, row) {
						if (data == 0) {
							return "否"
						}
						if (data == 1) {
							return "是"
						}
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
					window.location.href = "#/exchange/product/excointocoin/seeauto/" + ids[0];
				}
			}
			

			/**
			 * 添加按钮
			 */
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
        			window.location.href='#/exchange/product/excointocoin/modifyauto/'+ids[0];
            	}
			}
			/**
			 * 删除开启交易
			 */
			$scope.fnOpen = function() {}
			/**
			 * 关闭交易按钮
			 */
			$scope.fnClose = function() {}

			/**
			 * 删除按钮
			 */
			$scope.fnRemove = function() {}

		}
		
			if ($stateParams.page == "modifyauto") {
				$http.get(HRY.modules.exchange + "product/excointocoin/see/" +$stateParams.ing).success(function(data) {
					$scope.formData = data;
					$("#isSratAuto").val(data.isSratAuto);
					$("#atuoPriceType").val(data.atuoPriceType);
					 if(data.atuoPriceType!=1){
					     $("#autoPrice").val("");
		                  $("#autoPrice").attr("disabled",true);
		               }else{
		                  $("#autoPrice").attr("disabled",false);
		                  
		              }
					 hryCore.initPlugins();
				});
	            $("#atuoPriceType").change(function(){
	               if($("#atuoPriceType").val()!="1"){
	                $("#autoPrice").attr("disabled",true);
	               }else{
	                $("#autoPrice").attr("disabled",false);
	                 $("#autoPrice").val("");
	               }
				 
				});
				$scope.processForm = function() {
						$scope.formData.isSratAuto=$("#isSratAuto").val();
							$scope.formData.atuoPriceType=$("#atuoPriceType").val();
							$scope.formData.isHedge=$("#isHedge").val();
					$http({
						method : 'POST',
						url : HRY.modules.exchange + 'product/excointocoin/modifyauto',
						data : $.param($scope.formData),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					}).success(function(data) {
						if (data.success) {
							growl.addInfoMessage('修改成功')
							window.location.href = '#/exchange/product/excointocoin/listauto/anon';
						} else {
							growl.addInfoMessage(data.msg)
						}
					});

			};

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
