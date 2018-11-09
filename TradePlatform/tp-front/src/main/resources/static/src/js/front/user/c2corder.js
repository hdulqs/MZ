define(function(require, exports, module) {
	this._table = require("js/base/table");
	
	module.exports = {
		
		//初始化方法
		init : function(){

			var conf = {

				detail : function(e, index, row, $detail) {
					var html = [];
					$.each(row, function(key, value) {
						html.push('<p><b>' + key + ':</b> ' + value + '</p>');
					});
					$detail.html(html.join(''));
				},
				url : _ctx + "/user/c2clist",
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
					title : jiaoyishijian,
					field : 'transactionTime',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false,
					formatter : function(value,row,index){
						return value;
					}
				},
				{
					title : jiaoyileixing,
					field : 'transactionType',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false,
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
					title : danjja,
					field : 'transactionPrice',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false
				},
				{
					title : shuliang,
					field : 'transactionCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false
				},
				{
					title : jine,
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false
				}, {
                    title : zhaungtai,
                    field : 'status',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : false,
                    formatter:function(value,row,index){
                        //1买 2卖
                        if(value==1){
                            return dengdaishenhe;
                        }else if(value==2){
                        		return jiaoyichenggong;
						}else if(value==3){
                            return jiaoyiquxiao
						}
                        return jiaoyiquxiao
                    }
                }
				],
				
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        transactionType:$($("#type").find(".selected")[0]).attr("value")
				    };
				}
			}
			_table.initTable($("#table"), conf);
		
			
			//充值筛选条件
			$("#type").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table").bootstrapTable('refresh',null);
			})
			
		}

	}
	
	
	

});