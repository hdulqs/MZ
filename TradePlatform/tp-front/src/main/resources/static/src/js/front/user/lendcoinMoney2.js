define(function(require,exports,module){
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	
	module.exports = {
		init : function(){
			tool.switchTab();
			tool.switchTab({ele:".form_box",tabList:".payment_li",boxList:".payment_form_box"});
			/***************充值表单******************************/
			
			$('#coinSelect').change(function(){ 
				var p1=$(this).children('option:selected').val();//这就是selected的值 
				$.ajax({
					 type: "get",
			         url: _ctx + "/lendcoin/coinlist",
			         dataType: "JSON",
			         data : {"lendCoin":p1},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
						  $("#coinCodeForRmb1").html(data.obj.coinCodeForRmb);
						   $("#coinCodeForRmb2").html(data.obj.coinCodeForRmb);
						    $("#coinCodeForRmb3").html(data.obj.coinCodeForRmb);
						     $("#coinCodeForRmb4").html(data.obj.coinCodeForRmb);
						       $("#coinCodeForRmb5").html(data.obj.coinCodeForRmb);
						 $("#rMBSum").html(data.obj.rMBSum);
					     $("#coinNetAsset").html(data.obj.coinNetAsset);
					     $("#rMBLendMoneyed").html(data.obj.rMBLendMoneyed);
					     $("#canLendMoney").html(data.obj.canLendMoney);
						 
			        	 
			         }else{
			        	 
			         }
			         }
				})
				
				}) 
			
			//金额
			$("#lendmoneycount").on("input",function(){
				
				var money = this.value;
				var canLendMoney = $("#canLendMoney").html();//单笔不能少于
				if(!validate.isNumber(money)){
					$("#lendmoneycount_message").html("输入无效");
				}else{
					$("#lendmoneycount_message").html("");
					
					if(parseFloat(money)>parseFloat(canLendMoney)){
						$("#lendmoneycount_message").html("不能大于可借");
					}else{
					$("#lendmoneycount_message").html("");
					}
				}
			})
			//提交
			$("#lendSubmit").on("click",function(){
				var canLendMoney = $("#canLendMoney").html();
				var lendCoin=$("#coinSelect").children('option:selected').val();//这就是selected的值 
				var money = $("#lendmoneycount").val();
				if(!validate.isNumber(money)){
						layer.msg("输入无效", {icon: 2});
						return false;
				}else{
					if(parseFloat(money)>parseFloat(canLendMoney)){
						layer.msg("不能对于可借", {icon: 2});
						return false;
					}
				}
				$("#lendSubmit").attr("disabled",true);
				$("#lendSubmit").empty().text("Loading...");
				$("#lendFrom").ajaxSubmit({
					 type: "post",
			         url: _ctx + "/lendcoin/addCoin",
			         dataType: "JSON",
			         data : {"lendCount":money,"lendCoin":lendCoin},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
			        	 $("#lendSubmit").attr("disabled",false);
						 $("#lendSubmit").empty().text("申请");
						 
						 $("#rMBSum").html(data.obj.rMBSum);
					     $("#coinNetAsset").html(data.obj.coinNetAsset);
					     $("#rMBLendMoneyed").html(data.obj.rMBLendMoneyed);
					     $("#canLendMoney").html(data.obj.canLendMoney);
						 
			        	 /*layer.open({
						   type: 1,
						   title: '',
						   area: ['500px', '550px'],
						   scrollbar: false,   // 父页面 滚动条 禁止
						   shadeClose: false, //点击遮罩关闭
						   content: $('#Popup')
					     });*/
			        	 
			        	 $('#table').bootstrapTable('refresh');
			        	  layer.alert("申请成功");
			         }else{
			         	 $("#lendSubmit").attr("disabled",false);
						 $("#lendSubmit").empty().text("申请");
			        	 layer.alert(data.msg);
			        	 
			         }
			         }
				})
			})
			
			//分页bootstrapTable插件
			var conf = {

				url : _ctx + "/lendcoin/listpage",
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
					title : "借款时间",
					field : 'lendTime',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : "类型",
					field : 'lendCoin',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : "已借/已还",
					field : 'lendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return value+"/"+value;
					}
				},
				{
					title : "日化利率",
					field : 'lendRate',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return value+"%";
					}
				},
				{
					title : "总利息",
					field : 'interestCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : "已还金额",
					field : 'repayLendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return row.repayLendCount+row.repayInterestCount;
					}
				},{
					title : "应还金额",
					field : 'lendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return row.lendCount- row.repayLendCount+ row.interestCount- row.repayInterestCount;
			     }
			  },{
					title : "操作",
					field : 'lendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						if(row.status==1){
							return '<a id="'+row.id+','+row.lendCount+'">去还款</a>';
						}else if(row.status==2){
								return '<a id="'+row.id+','+row.lendCount+'">查看</a><a id="'+row.id+'">去还款</a>';
					
						}else if(row.status==3){
								return '<a id="'+row.id+','+row.lendCount+'">查看</a>';
					
						}
				   }
				}
				],
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        status:$($("#status").find(".selected")[0]).attr("value"),
				        transactionType:"chongzhi"
				    };
				}
			}
			 _table.initTable($("#table"), conf);
			
			 
			 $("#table").on("click","a[id]",function(){
				var sid = $(this).attr("id");
				var split = sid.split(',');
				var coincode = split[2]+'_'+split[1];
				var entrustPrice =  split[3];
				var type = split[4];
				
			
				
				
			})
			
		}
	}
})
