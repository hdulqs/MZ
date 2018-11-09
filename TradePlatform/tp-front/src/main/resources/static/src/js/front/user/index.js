define(function(require, exports, module) {
	this._table = require("js/base/table");
     require('js/highchart/highcharts.js')
	module.exports = {
    	 //香港未来//
    	   futureTab:function(){
    		$('.assetTab ul').on('click',"li",function(){
    			var cur=$(this).index();
    			$(this).addClass('active').siblings().removeClass('active');
    			$('.assetTabCon table').hide().eq(cur).show();
    		})   
    	   },
    	 
    	 //香港未来的

		// 添加页面提交方法
		init : function() {
			
			var conf = {
				url : _ctx + "/user/rmbdeposit/list",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : "id",
					visible : false,
					searchable : false
				}, {
					title : 'id',
					field : 'id',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				}, {
					title : chongzhileixing,
					field : 'transactionType',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value, row, index) {
						// 1线上充值,2线上提现 3线下充值 4线下取现
						if (value == 1) {
							return xianshangchongzhi;
						} else if (value == 2) {
							return xianshangtixian;
						} else if (value == 3) {
							return xianxiaxhongzhi;
						} else if (value == 4) {
							return xainxiaquxian;
						}
					}
				}, {
					title : chongzhijine,
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : shouxufei,
					field : 'fee',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : dingdanhao,
					field : 'transactionNum',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : shijian,
					field : 'created',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : zhaungtai,
					field : 'status',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value, row, index) {
						// 1待审核 2已完成 3已否决
						if (value == 1) {
							return daishenhe;
						} else if (value == 2) {
							return yiwancheng;
						} else if (value == 3) {
							return yifoujue;
						}
					}
				} ],
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        transactionType:$($("#transactionType").find(".selected")[0]).attr("value")
				       
				    };
				}
			}
			//_table.initTable($("#table"), conf);
			
			
			//充值类型事件
			$("#transactionType").on("click","a",function(){
				$(this).siblings().removeClass("selected");
				$(this).addClass("selected");
				$("#table").bootstrapTable('refresh',null);
			})
			
			$('.noticeCloseNews').on('click',function(){
				$('.noticeTips').parent().hide()
			})
			
		
		
		},
		highpie:function(){
			  $('#rate-chart').highcharts({
			        credits: {
			          enabled: false
			        },
			        chart: {
			          plotBackgroundColor: null,
			          plotBorderWidth: null,
			          plotShadow: false,
			          spacing: [0, 0, 0, 0]
			        },
			        title: {
			          floating: true,
			          text: '',

			        },
			        tooltip: {
			          pointFormat: '{point.name}：<b>{point.percentage:.1f}%</b>',
			          backgroundColor: '#808385',
			          style: { // 文字内容相关样式
			            color: "#fff",
			            fontSize: "14px",
			          }
			        },
			        plotOptions: {
			          pie: {
			            allowPointSelect: true,
			            cursor: 'pointer',
			            showInLegend: true,
			            dataLabels: {
		                    enabled: true,
		                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                    style: {
		                        color:  '#a6a6a6'|| 'black'
		                    }
		                },
			            point: {
			              events: {
//			                mouseOver: function(e) { // 鼠标滑过时动态更新标题
//			                  // 标题更新函数，API 地址：https://api.hcharts.cn/highcharts#Chart.setTitle
//			                  chart.setTitle({
//			                    text: e.target.name,
//
//			                  });
//			                  
//
//			                },
			                legendItemClick: function(e) {
			                  return false; //禁用图例点击事件
			                }
			              }
			            },
			          }
			        },
			        legend: {
			        	enabled:'true',
			            align: 'right',
			            verticalAlign: 'top',
			            layout:'vertical',
			            x: 0,
			            y: 100
			        },
			        series: [{
			          type: 'pie',
			          innerSize: '60%',
			          name: '',
			          data: [{
			              name: '现货资产',
			              y: 50.0,
			              url: ''
			            },
			            {
			              name: '杠杆资产',
			              y: 50,
			              selected: true,
			              url: ''
			            }
			          ]
			        }]
			      }, function(c) {
			        // 环形图圆心
			        var centerY = c.series[0].center[1],
			          titleHeight = parseInt(c.title.styles.fontSize);
			        c.setTitle({
			          y: centerY + titleHeight / 2
			        });
			        chart = c;
			      });
		},
		messageList : function(){
			
			$.ajax({
				type : "get",
				url : _ctx +"/user/oamessage/list",
				cache : false,
				dataType : "json",
				success : function(data) {
					if(data!=undefined){
						if(data.rows!=undefined&&data.rows.length>0){
							$(".noticeNewsHref").attr("sid",data.rows[0].id).html(data.rows[0].title);
						}else{
							$("#messageDiv").addClass("hide");
						}
					}else{
						$("#messageDiv").addClass("hide");
					}
				},
				error : function(e) {
					$("#messageDiv").addClass("hide");
				}
			});
			
			$(".noticeNewsHref").on("click",function(){
				loadUrl(_ctx +"/user/oamessage/read/"+$(this).attr("sid"));
			})
			
		}

	}

});