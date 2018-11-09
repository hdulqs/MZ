define(function(require, exports, module) {
	this._table = require("js/base/table");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			
			//加载base
			require("base");
			
			var conf = {
				detail : function(e, index, row, $detail) {
					var html = [];
					$.each(row, function(key, value) {
						html.push('<p><b>' + key + ':</b> ' + value + '</p>');
					});
					$detail.html(html.join(''));
				},
				url : _ctx + "/user/entrust/list?type=current",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : "id",
					visible : false,
					searchable : false
				},
				{
					title : 'id',
					field : 'id',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				},
				{
					title : weituoshijian,
					field : 'entrustTime_long',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value,row,index){
						return TimestampFormat('Y-m-d H:i:s', value/1000);
					}
				},
				{
					title : jiaoyileixing,
					field : 'type',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){  
						//1买 2卖
						if(value==1){
							return mai;
						}
						return maii
					}
				},
				{
					title : jiaoyizhonglei,
					field : 'coinCode',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : laiyuan,
					field : 'source',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){  
						if(value==1){
							return 'PC';
						}
						if(value==2){
							return jiqiren;
						}
						if(value==3){
							return yidongduan;
						}
						if(value==4){
							return qingzhipingcang;
						}
						if(value==5){
							return jihuaweituo;
						}
						if(value==6){
							return zhiyingpingcang;
						}
						if(value==7){
							return zhisunpingcang;
						}
						return ""
					}
				},
				{
					title : weituojiage,
					field : 'entrustPrice',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){  
						if(value==0){
							return " 市价";
						}
						
						return value;
					}
				},
				{
					title : dingdanhao,
					field : 'entrustNum',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				},
				{
					title : '定价币',
					field : 'fixPriceCoinCode',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				},
				{
					title : 'coinCode',
					field : 'coinCode',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				},
				{
					title : weituogeshu,
					field : 'entrustCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){ 
						if(row.type==1&&row.entrustPrice==0){
							
							return row.entrustSum;
						}
						
						return value;
					}
				},
				{
					title : shengyushuliang,
					field : 'surplusEntrustCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){ 
						if(row.type==1&&row.entrustPrice==0){
							
							return row.entrustSum-row.transactionSum;
						}
						
						return value;
					}
				},
				{
					title : zhaungtai,
					field : 'status',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){  
						if(value==0){
							return weichengjiao;
						}
						if(value==1){
							return bufenchengjiao;
						}
						if(value==2){
							return yiwancheng;
						}
						if(value==3){
							return bufenchengjiaoyichexiao;
						}
						if(value==4){
							return yichexiao;
						}
						return ""
					}
				},
				{
					title : caozuo,
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false,
					formatter:function(value,row,index){
						return '<a id="'+row.entrustNum+','+row.fixPriceCoinCode+','+row.coinCode+','+row.entrustPrice+','+row.type+'">'+cexiao+'</a>';
					}
				}
				],
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        typeone:$($("#type").find(".selected")[0]).attr("value"),
				        querypath:"center"
				    };
				}
			}
			_table.initTable($("#table_current"), conf);
			//_table.getHiddenColumns($("#table_current"), "entrustNum");
			
			
			$("#table_current").on("click","a[id]",function(){
				var sid = $(this).attr("id");
				var split = sid.split(',');
				var coincode = split[2]+'_'+split[1];
				
				var entrustPrice =  split[3];
				var type = split[4];
				
				layer.confirm(niquedingchexiao, 
				{	title:' ',
	    			btn: [quding,quxiao]
				},
				function(){
					$.ajax({
						type : "POST",
						dataType : "JSON",
						url : _ctx + "/user/trades/cancelExEntrust",
						cache : false,
						data : {entrustNums:split[0],entrustPrice:entrustPrice,type:type,coinCode:coincode,tokenId:$("#tokenId").val()},
						success : function(data) {
							layer.msg("success",{time:2000},function(){
								loadUrl(_ctx+"/v.do?u=front/user/entrust");
							})
						}
					})
				})
			})
			
			//充值筛选条件
			$("#type").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table_current").bootstrapTable('refresh',null);
			})
			
			
			//查询历史委托记录加载
			$("#historyBtn").on("click",function(){
				var conf = {

						detail : function(e, index, row, $detail) {
							var html = [];
							$.each(row, function(key, value) {
								html.push('<p><b>' + key + ':</b> ' + value + '</p>');
							});
							$detail.html(html.join(''));
						},
						url : _ctx + "/user/entrust/list?type=history",
						columns : [ {
							field : 'state',
							checkbox : true,
							align : 'center',
							valign : 'middle',
							value : "id",
							visible : false,
							searchable : false
						},
						{
							title : 'id',
							field : 'id',
							align : 'center',
							visible : false,
							sortable : false,
							searchable : false
						},
						{
							title : weituoshijian,
							field : 'entrustTime_long',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true,
							formatter : function(value,row,index){
								return TimestampFormat('Y-m-d H:i:s', value/1000);
							}
						},
						{
							title : jiaoyileixing,
							field : 'type',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true,
							formatter:function(value,row,index){  
								//1买 2卖
								if(value==1){
									return mai;
								}
								return maii
							}
							
						},
						{
							title : jiaoyizhonglei,
							field : 'coinCode',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true
						},
						{
							title : laiyuan,
							field : 'source',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true,
							formatter:function(value,row,index){  
								if(value==1){
									return "PC";
								}
								if(value==2){
									return jiqiren;
								}
								if(value==3){
									return yidongduan;
								}
								if(value==4){
									return qingzhipingcang;
								}
								if(value==5){
									return jihuaweituo;
								}
								if(value==6){
									return zhiyingpingcang;
								}
								if(value==7){
									return zhisunpingcang;
								}
								return ""
							}
						},
						{
							title : weituojiage,
							field : 'entrustPrice',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true,
							formatter:function(value,row,index){  
								if(value==0){
									return " 市价";
								}
								
								return value;
							}
						},
						{
							title : weituogeshu,
							field : 'entrustCount',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true,
							formatter:function(value,row,index){ 
								if(row.type==1&&row.entrustPrice==0){
									
									return row.entrustSum;
								}
								
								return value;
							}
						},
						{
							title : shengyushuliang,
							field : 'surplusEntrustCount',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true
						},
						{
							title : zhaungtai,
							field : 'status',
							align : 'center',
							visible : true,
							sortable : false,
							searchable : true,
							formatter:function(value,row,index){  
								if(value==0){
									return weichengjiao;
								}
								if(value==1){
									return bufenchengjiao;
								}
								if(value==2){
									return yiwancheng;
								}
								if(value==3){
									return bufenchengjiaoyichexiao;
								}
								if(value==4){
									return yichexiao;
								}
								return ""
							}
						}
						],	
						queryParams : function queryParams(params) {
						    return {
						        limit:params.limit,
						        offset:params.offset,
						        sortOrder: params.order,
						        typeone:$($("#type2").find(".selected")[0]).attr("value"),
						        querypath:"center"
						    };
						}
					}
					_table.initTable($("#table_history"), conf);
				
				
				//充值筛选条件
				$("#type2").on("click","a",function(){
					$(this).siblings().removeClass('selected');
					$(this).addClass('selected');
					$("#table_history").bootstrapTable('refresh',null);
				})
			});
		}

	}
	

});