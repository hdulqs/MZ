 /**
 * Copyright:   北京互融时代软件有限公司
 * C2cTransaction.js
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
define([ 'app', 'hryTable', 'layer','pikadayJq','hryUtil' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		



		/**
		 * ------------------------列表页面路径---------------------------------------------
		 */
		if ($stateParams.page == "list") {
			var table = $("#dataTable");
			$scope.serchData = {
					//status_EQ : 1
			};
			// --------------------加载dataTable--------------------------------
			var config = DT.config();
			config.bAutoSerch = true; // 是否开启键入搜索
			config.ajax.url = HRY.modules.customer + 'businessman/otctransaction/list';
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
				"data" : "transactionNum"   //委托订单
			},
			{
				"data" : "created"   //创建时间
			},
			{
				"data" : "transactionTypeByDes"   //交易类型
			},
			{
				"data" : "coinCode"   //交易种类
			},
			{
				"data" : "transactionPrice"   //单价
			},
			{
				"data" : "transactionCount"   //数据量
			},
				{
                    "data" : "transactionMoney"   //金额
                },
                {
                    "data" : "dealQuantity"   //成交量
                },
			{
				"data" : "fee"   //手续费
			},
			{
				"data" : "userName"   //委托账号
            },
			// {
			// 	"data" : "finishTime"   //成交时间
			// },
			{
				"data" : "statusByDes"   //状态
			}
			
			]
			config.columnDefs = [ {
				"targets" : 0,
				"orderable" : false,
				"orderable" : false,
				"render" : function(data, type, row) {
					return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
				},
			}  ]
			// --------------------加载dataTable--------------------------------
			DT.draw(table, config);
			
			/**
			 * 刷新按钮
			 */
			$scope.fnList = function(){
				table.DataTable().draw();
			}

            //导出Excel
            $scope.fnExcel = function() {
                DT.excel(table,this.serchData,"OTC委托订单");
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