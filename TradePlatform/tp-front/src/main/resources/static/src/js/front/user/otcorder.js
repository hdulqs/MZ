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
				url : _ctx + "/otc/otcorderlistall",
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
                        //1买 2卖
                        if(value==1){
                            return weifukuan;
                        }else if(value==2){
                        		return yifukuan;
						}else if(value==3){
                            return yiquxiao
						}else if(value==4){
                            return yiwancheng
                        }else if(value==5){
                            return yiguanbi
                        }else if(value==6){
                            return shensuzhong
                        }
                        return jiaoyiquxiao
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
                        	var btn = '<button id="'+index+'" data-rowid="'+row.id+'"  class="btn btn-spacing btn-info">付款信息</button>';
                            //1买 2卖
                            if(value==1){//未付款
                                if( row.buyCustomId == $("#otcuserid").val()) {
                                    btn += '<button id2="' + index + '" data-rowid="' + row.id + '" class="btn btn-primary">撤销</button>';
                                }
                                if(row.transactionType ==1) {
                                    btn += '<button id3="' + index + '" data-rowid="' + row.id + '" class="btn btn-pric">完成付款</button>';
                                }
                                btn +=    '<button id5="'+index+'" data-rowid="'+row.id+'" class="btn btn-pric">我要申诉</button>';
                            }else if(value==2){//已付款
                                if(row.transactionType ==2) {
                                    btn += '<button id4="' + index + '" data-rowid="' + row.id + '" class="btn btn-spacing btn-primary">确认收款</button>';
                                }
                                btn +=    '<button id5="'+index+'" data-rowid="'+row.id+'" class="btn btn-pric">我要申诉</button>';
                            }else if(value==3){//已取消
                               // btn += '<button id5="'+index+'" data-rowid="'+row.id+'" class="btn btn-pric">申请仲裁</button>';
                            }else if(value==4){//已完成

                            }else if(value==5){//已关闭

                            }else if(value==6){//申请仲裁
                                if(row.transactionType == 2) {
                                    btn += '<button id4="' + index + '" data-rowid="' + row.id + '" class="btn btn-spacing btn-primary">确认收款</button>';
                                }
                                if( row.appealCustomId == $("#otcuserid").val()) {
                                    btn += '<button id6="' + index + '" data-rowid="' + row.id + '" class="btn btn-pric">申诉详情</button>';
                                }
                            }
                            return btn;
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

			$("#table").on('click','button[id]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.open({
                    title: false,
                    type: 2,
                    shadeClose: false,
                    move : false,
                    scrollbar  : false,
                    shade: 0.8,
                    area: ['40%', '70%'],
                    content: _ctx + '/user/otcPayInfor?id='+ rowId,
                    cancel: function(index){
						layer.close();
                    }
                });
			});
            //撤消
            $("#table").on('click','button[id2]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.open({
                    title: false,
                    type: 2,
                    shadeClose: false,
                    move : false,
                    scrollbar  : false,
                    shade: 0.8,
                    area: ['33%', '50%'],
                    content: _ctx + '/user/otcUndo?id='+ rowId,
                    cancel: function(index){
                        layer.close();
                    }
                });
            });
            //完成付款
            $("#table").on('click','button[id3]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.open({
                    title: false,
                    type: 2,
                    shadeClose: false,
                    move : false,
                    scrollbar  : false,
                    shade: 0.8,
                    area: ['33%', '70%'],
                    content: _ctx + '/user/otcPay?id='+ rowId,
                    cancel: function(index){
                        layer.close();
                    }
                });
            });
             //确认
            $("#table").on('click','button[id4]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.confirm('确定到账了？', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    //事件1
                    $.ajax({
                        type : "post",
                        url : _ctx+"/otc/finishOtcOrder",
                        cache : false,
                        dataType : "json",
                        data :{
                            transactionorderid : rowId
                        },
                        success : function(data) {
                            if(data){
                                if(data.success){
                                    layer.closeAll('dialog');
                                    $("#table").bootstrapTable('refresh',null);
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

            $("#table").on('click','button[id5]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.open({
                    title: false,
                    type: 2,
                    shadeClose: false,
                    move : false,
                    scrollbar  : false,
                    shade: 0.8,
                    area: ['33%', '70%'],
                    content: _ctx + '/user/otcApplyArbitration?id='+ rowId,
                    cancel: function(index){
                        layer.close();
                    }
                });
            });

            $("#table").on('click','button[id6]',function(e){
                var rowId = e.target.dataset.rowid;
                layer.open({
                    title: false,
                    type: 2,
                    shadeClose: false,
                    move : false,
                    scrollbar  : false,
                    shade: 0.8,
                    area: ['33%', '70%'],
                    content: _ctx + '/user/otcApplyArbitrationInfo?id='+ rowId,
                    cancel: function(index){
                        layer.close();
                    }
                });
            });

		}

	}




});