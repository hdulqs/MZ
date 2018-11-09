define(function(require,exports,module){
	this._table = require("js/base/table");
	
	
	module.exports = {
		init : function(){
			
			
			$("#coinSelect").on("change",function(){
				var selectValue = $(this).find("option:selected").val();
				var split = selectValue.split(",");
				$("#availableCTC").val(split[1]);
				$("#frozenCTC").val(split[2]);
				$("#publicKey").empty().text(split[3]);
				
				$("#coincode1").empty().text(split[4]);
				$("#coincode2").empty().text(split[4]);
				$("#coincode3").empty().text(split[4]);
				$("#coincode4").empty().text(split[4]);
			})
			
			
			//分页bootstrapTable插件
			var conf = {
				url : _ctx +"/ico/recandwit/list?transactionType=1",
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
					title : '订单号',
					field : 'transactionNum',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
					/*formatter:function(value,row,index){
						//1线上充值,2线上提现 3线下充值 4线下取现
						if(value==1){
							return "线上充值";
						}else if(value==2){
							return "线上提现";
						}else if(value==3){
							return "线下充值";
						}else if(value==3){
							return "线下取现";
						}
					}*/
				},
				{
					title : '数量',
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : '手续费',
					field : 'fee',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : '币种',
					field : 'coinCode',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : '时间',
					field : 'created',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : '状态',
					field : 'status',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//1待审核 2已完成 3已否决
						if(value==1){
							return "待审核";
						}else if(value==2){
							return "已完成";
						}else if(value==3){
							return "已否决";
						}
					}
				},
				{
					title : '备注',
					field : 'remark',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}
				],
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        type:$($("#type").find(".selected")[0]).attr("value"),
				        status:$($("#type").find(".selected")[0]).attr("value")
				    };
				}
			}
			 _table.initTable($("#table"), conf);
			//筛选条件
			$("#type").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table").bootstrapTable('refresh',null);
			})
		}
	}
})