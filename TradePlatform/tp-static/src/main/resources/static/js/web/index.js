/**
 * Created by kenkozheng on 2015/7/10.
 */
define(['angular','highcharts'], function (angular) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){

    	
    	// ---------------------- 初始化首页信息  ---------------------------------------------------------
    	// http://localhost:7074/calculate/calculateapptransaction/apptransaction/findCustromerRegister?timeago=1
    	
    	// 返回当天用户与总用户对象的比例(对象)
    	var s = HRY.modules.calculate
    	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/findCustromerRegister?timeago="+1).
	       success(function(data) {
	        	if(data!=null&&data!=""){
		          	$scope.custromerRatio = data;
	        	}
	        });
    	
    	// 返回未处理的订单充值订单总钱数 以及币的提现数量
    	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/getTransactionGet").
	       success(function(data) {
	        	if(data!=null&&data!=""){
		          	$scope.pendingOrdersGet = data;
	        	}
	        });
    	
    	// 查询币种冠军
    	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/getFirstCoin").
	       success(function(data) {
	        	if(data!=null&&data!=""){
		          	$scope.FirstCoin = data;
	        		
	        	}
	        });
    	
    	// 查询服务器钱包总币数
    	$http.get(HRY.modules.calculate+"exDmTransfColdDetail/exdmtransfcolddetail/operation").
	       success(function(data) {
	        	if(data!=null&&data!=""){
		          	$scope.PageResult = data.rows;
	        		
	        	}
	        });
    	
    	// 返回未处理的充值订单总钱数以及币充值的订单数量
    	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/getTransactionPost").
    	success(function(data) {
    		if(data!=null&&data!=""){
    			$scope.pendingOrdersPost = data;
    		}
    	});
                                                                                          
    	
    	// 返回未处理的提现订单总钱数(数)
    	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/findOrderMoney?timeago="+1).
    	success(function(data) {
    			$scope.CalculatePo = data;
    	});
    	
    	
    	// 查询用户分布图  
    	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/findUserDistribution").
    	success(function(data) {
    		 
    			$scope.appStatisticalToIndexVo = data;
    	});
    	
    	// -------------------------------------------------------------------------------
    	
    	
    	// 查询我方币账户信息
    /*	$http.get(HRY.modules.account+'fund/appouraccount/coinServiceInfo').
    	success(function(data) {
    		$scope.ourCoinInfo = data.obj;
    	});*/
    	
    	
    	
    	
    	
    	    var highchartsProportion={};

    	    highchartsProportion.hcDivId="#container1",
//    	    highchartsProportion.url =  HRY.modules.exchange+ "statistics/account/test.do",
    	    highchartsProportion.chart = {
			 	chart: {
		            type: 'pie'
		        },
		        title: {
		            text: ''
		        },
		         tooltip: {
		    	    pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		        },
		         plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    color: '#000000',
		                    connectorColor: '#000000',
		                    format: '<b>{point.name}</b>: {point.percentage:.2f} %'
		                },
		                 showInLegend: true
		            }
		        },
		        series: [{
	                type: 'pie',
	                name: '投资人',
	                data: []
	            }],
		        credits:{
			        	text:''//去掉右下角的Highcharts.com超链接
				        }
			};
//		    $http({
//					method : 'GET',
//				    url:highchartsProportion.url,
//					headers : {
//						'Content-Type' : 'application/x-www-form-urlencoded'
//					}
//				})
//				.success(function(data) {
//					alert(1)
//					 
//					data='["注册":"100","仅注册":"50"]'
//					var list = JSON.parse(data);
//					console.log(list);
//					//赋值
//					highchartsProportion.chart.series[0].data=list;
//		            $(highchartsProportion.hcDivId).highcharts(highchartsProportion.chart);
//		            
//				});
			
			
		
			$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/findUserDistribution").
    	success(function(data) {
    		
    		$scope.appStatisticalToIndexVo = data;
			
    	    data='[["仅注册",'+$scope.appStatisticalToIndexVo.countToRegist+'],["仅实名",'+$scope.appStatisticalToIndexVo.countToReal+'],["仅充值",'+$scope.appStatisticalToIndexVo.countToPost+'],["已交易",'+$scope.appStatisticalToIndexVo.countToClinch+']]';
    	    var list = JSON.parse(data);
    	    highchartsProportion.chart.series[0].data=list;
            $(highchartsProportion.hcDivId).highcharts(highchartsProportion.chart);
      
            });
           
            // findOrderDistribution
        	$http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/findOrderDistribution").
    	success(function(data) {
    		 
    			$scope.appOrderVo = data;
    			var data3 = [];
    			for(var i = 0; i<data.length;i++){
    				var data1 = data[i];

    		//		s ="{\'name\':'"+data1.coinName+'/'+data1.coinCode+"',\'y\':'"+data1.coinCount/data1.totalCount+"'}";
    		//		data2.push(s)

    			    var item={};
    			    item.name=data1.coinName+"/"+data1.coinCode;
    			    item.y=data1.coinCount;
    			    data3.push(item);

    			}
    			
    	
     $(function () {
    	 
    	 $http.get(HRY.modules.calculate+"calculateapptransaction/apptransaction/findOrderDistribution").
	       success(function(data) {
	        	if(data!=null&&data!=""){
		    		   var chart = {
		    			      type: 'column'
		    		   };
		    		   var title = {
		    		      text: '总成交量'   
		    		   };
		    		   var subtitle = {
		    		      text: 'total volume'  
		    		   };
		    		   var xAxis = {
		    		      categories: [],
		    		      crosshair: true
		    		   };
		    		   var yAxis = {
		    		      min: 0,
		    		      title: {
		    		         text: '成交总量'         
		    		      }      
		    		   };
		    		   var tooltip = {
		    		      headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		    		      pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		    		         '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
		    		      footerFormat: '</table>',
		    		      shared: true,
		    		      useHTML: true
		    		   };
		    		   var plotOptions = {
		    		      column: {
		    		         pointPadding: 0.2,
		    		         borderWidth: 0
		    		      }
		    		   };  
		    		   var credits = {
		    		      enabled: false
		    		   };
		    		   
		    		   var series= [{
		    		        name: '成交总量',
		    		            data: []
		    		        }];     
		    		   for(var i=0; i<data.length; i++){
		    			   xAxis.categories[i] = data[i].coinName+"/"+data[i].coinCode;
		    			   series[0].data[i] = data[i].coinCount;
		    		   }   
		    		   var json = {};   
		    		   json.chart = chart; 
		    		   json.title = title;   
		    		   json.subtitle = subtitle; 
		    		   json.tooltip = tooltip;
		    		   json.xAxis = xAxis;
		    		   json.yAxis = yAxis;  
		    		   json.series = series;
		    		   json.plotOptions = plotOptions;  
		    		   json.credits = credits;
		    		   $('#container2').highcharts(json);
	        	}
	        });
    	 
    	 
    	 
    	 });    
    	
    	
    	
    	
    	
    	});    
            
            
     
    }
    

    
    return {controller:controller};
    
});