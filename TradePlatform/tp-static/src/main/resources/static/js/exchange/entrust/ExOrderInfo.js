/**
 * student.js
 */
define(['app','hryTable','pikadayJq','hryUtil' ], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$scope.unitTips=tips.unitTips;
    	//初始化js 插件
    	
    	$scope.reset=reset;
    	//重置按钮
    	function reset(){
    		$scope.serchData=hryCore.reset($scope.serchData);
    		fnList();
    	}
    	$scope.fnList=fnList;
    	 //刷新按钮
        function fnList(){
        	 table.DataTable().draw();
        }
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	var table =  $("#table2");
        	debugger
        	$scope.projName=HRY.modules.exchange;
        	var type=$stateParams.id;
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索                               
        		config.ajax.url = HRY.modules.exchange+'entrust/exorderinfo/list';
        		config.ajax.data = function(d){
					//设置select下拉框
					DT.selectData($scope.serchData);
        			$.each($scope.serchData, function(name,value){
						if(""!=value){
							eval("d."+name+"='"+value+"'");
						}
					});
	           }
        		config.columns = [	/*{
        								"data" : "id"
        							},*/
        		                  	{
										"data" : "orderNum"
									},{
										"data" : "coinCode"
									},{
										"data" : "fixPriceCoinCode"
									}/*,{
										"data" : "type"
									}*/, {
										"data" : "transactionPrice"
									}, {
										"data" : "transactionCount"
									},{
										"data" : "transactionSum"
									},{
										"data" : "transactionBuyFee"
									},{
										"data" : "transactionSellFee"
									},  {
										"data" : "buyEntrustNum"
									},  {
										"data" : "buyUserName"
									},  {
										"data" : "sellEntrustNum"
									},  {
										"data" : "sellUserName"
									}, {
										"data" : "transactionTime"
									}
        		                  ]
        		/*config.columnDefs  = [
										{
											"targets" : 3,"orderable" :false,
												
											"render" : function(data, type, row) {
												if(data=="2"){
													return "卖";
												}else{
													return "买"
												}
											},
										}
									 ]*/
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
       
            $scope.fnList=fnList;//刷新方法
            
           
            //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"成交记录信息");
			}
     
            
        
        }
        if ($stateParams.page != "listfees") {
        } else {
            var table = $("#table2");
            $scope.projName = HRY.modules.exchange;
            var type = $stateParams.id;
            $scope.serchData = {};
            //--------------------加载dataTable--------------------------------
            var config = DT.config();
            config.bAutoSerch = true; //是否开启键入搜索
            config.ajax.url = HRY.modules.exchange + 'entrust/exorderinfo/listfees';
            config.ajax.data = function (d) {
                //设置select下拉框
                DT.selectData($scope.serchData);
                $.each($scope.serchData, function (name, value) {
                    if ("" != value) {
                        eval("d." + name + "='" + value + "'");
                    }
                });
            }
            config.columns = [{
                "data": "id"
            },
                {
                    "data": "buyUserName"
                }, {
                    "data": "sellUserName"
                }, {
                    "data": "transactionTime"
                }, {
                    "data": "coinCode"
                }, {
                    "data": "fixPriceCoinCode"
                }, {
                    "data": "transactionBuyFee"
                }, {
                    "data": "transactionSellFee"
                }/*,  {
										"data" : "type"
									}*/, {
                    "data": "transactionPrice"
                }, {
                    "data": "transactionCount"
                }, {
                    "data": "transactionSum"
                }/*, {
										"data" : "transactionFeeRate"
									}, {
										"data" : "transactionFee"
									}*/
            ]
            config.columnDefs = [{
                "targets": 0, "orderable": false,

                "render": function (data, type, row) {
                    return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
                },
            },
                {
                    "targets": 6,
                    "render": function (data, type, row) {
                        return data.toFixed(8);
                    }
                },{
    				"targets" :7,

    				"render" : function(data, type, row) {
    					if(data.toString().indexOf("e")!=-1){
    						//alert(data.toString().length);
    						return data.toFixed(7);
    					}else{
    						return data;
    					}

    				},
    			} 
            /*{
												"targets" : 5,

												"render" : function(data, type, row) {
													if(data==1){
														return "买入"
													}else if(data==2){
														return "卖出"
													}else{
														return "";
													}
												}
											},*/
                /*	{
                        "targets" :11,

                        "render" : function(data, type, row) {
                            return data+"%";
                    },
                }*/
            ]
            DT.draw(table, config);


            //导出excel
            $scope.fnExcel = function () {
                DT.excel(table, this.serchData, "交易手续费查询");
            }


        }
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});