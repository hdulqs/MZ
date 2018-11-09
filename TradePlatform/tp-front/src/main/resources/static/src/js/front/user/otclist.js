define(function(require, exports, module) {
	this._table = require("js/base/table");

	module.exports = {
		
		//初始化方法
		init : function(){

            function formatNum(num,n)
            {//参数说明：num 要格式化的数字 n 保留小数位
                num = String(num.toFixed(n));
                var re = /(-?\d+)(\d{3})/;
                while(re.test(num)) num = num.replace(re,"$1,$2")
                return num;
            }

			var conf = {

				detail : function(e, index, row, $detail) {
					var html = [];
					$.each(row, function(key, value) {
						html.push('<p><b>' + key + ':</b> ' + value + '</p>');
					});
					$detail.html(html.join(''));
				},
				url : _ctx + "/otc/getOtclists",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : "id",
					visible : false,
					searchable : false
				}, {
                        title : transactionorderNum,
                        field : 'transactionNum',
                        align : 'center',
                        visible : true,
                        sortable : false,
                        searchable : false,
                        formatter : function(value,row,index){
                            return value;
                        }
                    },
				{
					title : created,
					field : 'created',
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
						}else if (value==2){
                            return maii ;
                        }else if (value==3){
                            return maimai;
                        }
						return maii;
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
					searchable : false,
                    formatter:function(value,row,index){
                        try{
                         return "￥" + formatNum(value,3);
                        }catch (e) {
                           return value;
                        }
                    }
				}, {
                        title : dealQuantity,
                        field : 'dealQuantity',
                        align : 'center',
                        visible : true,
                        sortable : false,
                        searchable : false
                    },{
                        title : businessQuantity,
                        field : 'businessQuantity',
                        align : 'center',
                        visible : true,
                        sortable : false,
                        searchable : false
                    },{
                        title : shouxufei,
                        field : 'fee',
                        align : 'center',
                        visible : true,
                        sortable : false,
                        searchable : false,
                        formatter:function(value,row,index){
                            if(row.transactionType == 1){
                                return 0;
                            }else{
                                return value;
                            }
                        }
                    }, {
                    title : zhaungtai,
                    field : 'status',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : false,
                    formatter:function(value,row,index){
                        if(value==1){
                            return jinxinzhong;
                        }else if(value==2){
                        		return wancheng;
						}else if(value==3){
                            return yiquxiao;
						}else if(value==4){
                            return bufenjiaoyi;
                        }else if(value==5){
                            return quanbujiaoyi;
                        }else if(value==6){
                            return bufenwancheng;
                        }
                        return "";
                    }
                },
                    {
                        title : caozuo,
                        field : 'status',
                        align : 'left',
                        visible : true,
                        sortable : false,
                        searchable : false,
                        formatter:function(value,row,index){
                            if(value==1 || value==4|| value==5|| value==6){//未付款
                                if(row.businessFlag == "Y" && row.cancelFlag == "N") {
                                    return '<button id="' + index + '" data-rowid="' + row.id + '"  class="btn btn-spacing btn-info">'+cexiao+'</button>';
                                }
                            }
                            return "";
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
			_table.initTable($("#otcliststable"), conf);
		
			
			//充值筛选条件
			$("#type").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#otcliststable").bootstrapTable('refresh',null);
			})


            //撤消
            $("#otcliststable").on('click','button[id]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.confirm(niquedingchexiao, {
                    btn: [quding,quxiao] //按钮
                }, function(){
                    //事件1
                    $.ajax({
                        type : "post",
                        url : _ctx+"/otc/OtcListclose",
                        cache : false,
                        dataType : "json",
                        data :{
                            transactionid: rowId
                        },
                        success : function(data) {
                            if(data){
                                if(data.success){
                                    layer.closeAll('dialog');
                                    $("#otcliststable").bootstrapTable('refresh',null);
                                }else{
                                    layer.msg(data.msg);
                                }
                            }
                        },
                        error : function(e) {
                        }
                    });
                }, function(){
                    //事件2
                    layer.closeAll('dialog');
                });
            });



		}

	}




});