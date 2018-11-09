define(function(require,exports,module){
	this._table = require("js/base/table");
	
	
	
	
	module.exports = {
		//生成地址
		getPublicKey : function(){
			
			$("#coins").on("click","button[name=coinobjcet]",function(){
				$(this).attr("disabled","disabled");
				debugger;
				$(this).siblings().removeAttr('disabled');
                var c1 = $("#coincode3").html().trim();
                $("#coincode3").html($(this).html()+" ");
                var c2 = $("#coincode3").html().trim();
                var zdbefore = $("#zdbefore").html();
               var aa="/"+c1+"/g"
//                $("#zdbefore").html(zdbefore.replace(c1,c2).replace(c1,c2).replace(c1,c2).replace(c1,c2));
                $("#zdbefore").html(zdbefore.replace(eval(aa),c2));
                
                $("#coincode4").html($(this).html());
				$("#coincode5").html("("+$(this).html()+")");
				$("#postCoinCode").val($(this).attr("accountid"));
				
				$.ajax({
					type : "post",
					url : _ctx + "/user/btc/getPublicKey",
					cache : false,
					data:{
						accountId : $("#postCoinCode").val()
					},
					dataType : "json",
					success : function(data) {
						if(data!=undefined&&data.success&&data.obj!=undefined&&data.obj!=""){
							$("#publicKeyValue").html(data.obj);
							$("#createPublicKey").addClass("hide");
						}else{
							$("#publicKeyValue").html("");
							$("#createPublicKey").removeClass("hide");
						}
					},
					error : function(e) {
					}
				});
				
			});
			
			$($("#coins button")[0]).trigger("click");
			
			$("#createPublicKey").on("click",function(){
				
				$(this).attr("disabled","disabled");
				
				//$("#coincode3").html($(this).html());
				//$("#coincode4").html($(this).html());
				$.ajax({
					type : "post",
					url : _ctx + "/user/btc/createPublicKey",
					cache : false,
					data:{
						accountId : $("#postCoinCode").val()
					},
					dataType : "json",
					success : function(data) {
						if(data!=undefined){
							if(data.success&&data.obj!=undefined&&data.obj!=""){
								$("#publicKeyValue").html(data.obj);
								$("#createPublicKey").addClass("hide");
								$("#createPublicKey").removeAttr("disabled");
							}else{
								layer.msg(data.msg, {icon: 1,time:2000},function(){
									$("#createPublicKey").removeAttr("disabled");
								})
							}
						}else{
							$("#createPublicKey").removeAttr("disabled");
						}
						
					},
					error : function(e) {
						$("#createPublicKey").removeAttr("disabled");
					}
				});
				
			});
		},
		init : function(){
			
			
			$("#coinSelect").on("change",function(){
				var selectValue = $(this).find("option:selected").val();
				var split = selectValue.split(",");
				$("#availableCTC").val(split[1]);
				$("#frozenCTC").val(split[2]);
				
				if(split[3])
				$("#publicKey").empty().html(split[3]);
				
				$("#coincode1").empty().text(split[4]);
				$("#coincode2").empty().text(split[4]);
				$("#coincode3").empty().text(split[4]);
				$("#coincode4").empty().text(split[4]);
			})
			
			
			//分页bootstrapTable插件
			var conf = {
				url : _ctx +"/user/btc/list",
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
					title : dingdanhao,
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
					title : shuliang,
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : shouxufei,
					field : 'fee',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : bizhong,
					field : 'coinCode',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : chongbishijian,
					field : 'created_long',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value,row,index){
						return TimestampFormat('Y-m-d H:i:s', value/1000);
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
						//1待审核 2已完成 3已否决
						if(value==1){
							return daishenhe;
						}else if(value==2){
							return yiwancheng;
						}else if(value==3){
							return yifoujue;
						}
					}
				},
				{
					title : beizhu,
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