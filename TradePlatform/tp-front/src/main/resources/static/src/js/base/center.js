define(function(require,exports,module){
	
	var highcharts = require("lib/highcharts/highcharts");
	var exporting = require("lib/highcharts/exporting");
	var highchartszhcn= require("lib/highcharts/highcharts-zh_CN");
//	require("lib/highcharts/grid-light");
	require("lib/highcharts/color/hei");
//	require("lib/highcharts/color/hui");
	
	
	module.exports = {
		init : function(){
			
			//7日数据图
			var change7day_chart;
			//15日数据图
			var change15day_chart;
			//月度数据图
			var change30day_chart;
			//年度数据图
			var change365day_chart;
			//决策分布图
			var decisionChart;
			
			//曲线图切换
			$("#change7day").on("click","a",function(){
				
				var name = $(this).attr("name");
				if(name=="7day"){
					$("#title").html("7数据统计")
					curveChart("7数据统计",7);
					
				}
				if(name=="15day"){
					$("#title").html("15日数据统计")
					curveChart("15日数据统计",15);
				}
				if(name=="month"){
					$("#title").html("月度数据统计")
					curveChart("月度数据统计",30);
				}
				if(name=="year"){
					$("#title").html("年度数据统计")
					curveChart("年度数据统计",365);
				}
			})
			
			
			//饼图切换
			$("#changeCircle").on("click","a",function(){
				var name = $(this).attr("name");
				$.ajax({
					type : "get",
					url : _ctx+"/admin/process/proprocess/circleChartData.do",
					dataType : "json",
					flag : name,
					success : function(data) {
						var chatData  ;
						if(this.flag=="today"){
							chatData = [
						                 ['通过',   data.t1],
							                ['建议通过', data.t2],
							                ['人工审核',  data.t3],
							                ['审慎审核',  data.t4],
							                ['拒绝',   data.t5]
							            ];
							text = "今日";
						}else if(this.flag=="all") {
							chatData = [
						                 ['通过',   data.a1],
							                ['建议通过', data.a2],
							                ['人工审核',  data.a3],
							                ['审慎审核',  data.a4],
							                ['拒绝',   data.a5]
							            ];
							text = "全部";
						}else if(this.flag=="month") {
							chatData = [
						                 ['通过',   data.m1],
							                ['建议通过', data.m2],
							                ['人工审核',  data.m3],
							                ['审慎审核',  data.m4],
							                ['拒绝',   data.m5]
							            ];
							text = "本月";
						}else if(this.flag=="year") {
							chatData = [
						                 ['通过',   data.y1],
							                ['建议通过', data.y2],
							                ['人工审核',  data.y3],
							                ['审慎审核',  data.y4],
							                ['拒绝',   data.y5]
							            ];
							text = "本年";
						}
						
						decisionChart.update({
					    	credits: {  
						        enabled: false,  
					    	} ,
					        chart: {
					            plotBackgroundColor: null,
					            plotBorderWidth: null,
					            plotShadow: false
					        },
					        title: {
					            text: text+'决策分布图'
					        },
					        tooltip: {
					            headerFormat: '{series.name}<br>',
					            pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
					        },
					        plotOptions: {
					            pie: {
					                allowPointSelect: true,
					                cursor: 'pointer',
					                dataLabels: {
					                    enabled: true,
					                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					                    style: {
					                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					                    }
					                }
					            }
					        },
					        series: [{
					            type: 'pie',
					            name: '浏览器访问量占比',
					            data: chatData
					        }]
					    });
					},
					error : function(e) {
					}
				});
			
				
			})
			
	
			/**
			 * 曲线图
			 */
			var load = function(){
				$.ajax({
					type : "get",
					url : _ctx+"/admin/process/proprocess/count7day.do",
					cache : false,
					dataType : "json",
					success : function(data) {
						change7day_chart = new Highcharts.Chart('container', {
							credits: {  
							        enabled: false,  
							} ,
							chart : {
								type : "area"
							},
						    title: {
						        text: '七日数据统计',
						        x: -20
						    },
						    subtitle: {
						        text: '数据来源: 互融云',
						        x: -20
						    },
						    xAxis: {
						        categories: data.date
						    },
						    yAxis: {
						        title: {
						            text: '数量(件)',
						            rotation: 0,
						            align: 'high'
						        },
						        plotLines: [{
						            value: 0,
						            width: 1,
						            color: '#808080'
						        }]
						    },
						    tooltip: {
						        valueSuffix: ''
						    },
						    legend: {
						        layout: 'vertical',
						        align: 'right',
						        verticalAlign: 'middle',
						        borderWidth: 0
						    },
						    series: [{
					            name: '全部',
					            data: data.data.allarr
					        }, {
					            name: '通过',
					            data: data.data.tongarr
					        }, {
					            name: '建议通过',
					            data: data.data.jiantongarr
					        }, {
					            name: '人工审核',
					            data: data.data.renarr
					        }, {
					            name: '审慎审核',
					            data: data.data.jianjuarr
					        }, {
					            name: '拒绝',
					            data: data.data.juarr
					        }]
						});
						
				
					},
					error : function(e) {
					}
				});
				
			}
			load();
			
			/**
			 * 曲线图
			 */
			var curveChart = function(name,type){
				$.ajax({
					type : "get",
					url : _ctx+"/admin/process/proprocess/count"+type+"day.do",
					cache : false,
					dataType : "json",
					success : function(data) {
						
						
						change7day_chart = new Highcharts.Chart('container', {
							credits: {  
							        enabled: false,  
							} ,
							chart : {
								type : "area"
							},
						    title: {
						        text: name,
						        x: -20
						    },
						    subtitle: {
						        text: '数据来源: 互融云',
						        x: -20
						    },
						    xAxis: {
						        categories: data.date
						    },
						    yAxis: {
						        title: {
						            text: '数量(件)',
						            rotation: 0,
						            align: 'high'
						        },
						        plotLines: [{
						            value: 0,
						            width: 1,
						            color: '#808080'
						        }]
						    },
						    tooltip: {
						        valueSuffix: ''
						    },
						    legend: {
						        layout: 'vertical',
						        align: 'right',
						        verticalAlign: 'middle',
						        borderWidth: 0
						    },
						    series: [{
					            name: '全部',
					            data: data.data.allarr
					        }, {
					            name: '通过',
					            data: data.data.tongarr
					        }, {
					            name: '建议通过',
					            data: data.data.jiantongarr
					        }, {
					            name: '人工审核',
					            data: data.data.renarr
					        }, {
					            name: '审慎审核',
					            data: data.data.jianjuarr
					        }, {
					            name: '拒绝',
					            data: data.data.juarr
					        }]
						});
						
					},
					error : function(e) {
					}
				});
			}
			

			
			/**
			 *  圆形图
			 */
			
			var circleChart = function(){
				
				$.ajax({
					type : "get",
					url : _ctx+"/admin/process/proprocess/circleChartData.do",
					dataType : "json",
					flag : "today",
					success : function(data) {
						var chatData  ;
						if(this.flag=="today"){
							chatData = [
						                 ['通过',   data.t1],
							                ['建议通过', data.t2],
							                ['人工审核',  data.t3],
							                ['审慎审核',  data.t4],
							                ['拒绝',   data.t5]
							            ];
						}
						
						decisionChart = new Highcharts.Chart('circleChart', {
					    	credits: {  
						        enabled: false,  
					    	} ,
					        chart: {
					            plotBackgroundColor: null,
					            plotBorderWidth: null,
					            plotShadow: false
					        },
					        title: {
					            text: '今日决策分布图'
					        },
					        tooltip: {
					            headerFormat: '{series.name}<br>',
					            pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
					        },
					        plotOptions: {
					            pie: {
					                allowPointSelect: true,
					                cursor: 'pointer',
					                dataLabels: {
					                    enabled: true,
					                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					                    style: {
					                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					                    }
					                }
					            }
					        },
					        series: [{
					            type: 'pie',
					            name: '浏览器访问量占比',
					            data: chatData
					        }]
					    });
						
						
					},
					error : function(e) {
					}
				});
			}
			circleChart();
			
			
		}
	}
	
	
})