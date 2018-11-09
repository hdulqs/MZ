define(function(require,exports,module){
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	
	module.exports = {
		init : function(){
			tool.switchTab();
			tool.switchTab({ele:".form_box",tabList:".payment_li",boxList:".payment_form_box"});
			/***************充值表单******************************/
			//金额
			$("#lendmoneycount").on("input",function(){
				
				var money = this.value;
				var canLendMoney = $("#canLendMoney").html();//单笔不能少于
				if(!validate.isNumber(money)){
					$("#lendmoneycount_message").html(shuruwuxiao);
				}else{
					$("#lendmoneycount_message").html("");
					
				
				}
			})
			//提交
			$("#lendSubmit").on("click",function(){
				var oldTimestr = $('#oldLendTimes').val();
				var lendTimestr = $("#lendmoneycount").val();
				if(!validate.isNumber(lendTimestr)){
						layer.msg(shuruwuxiao, {icon: 2});
						return false;
				}else if(oldTimestr==lendTimestr){
                    layer.msg(beishuwugenggai, {icon: 2});
                    return false;
				}
				
				$("#lendSubmit").attr("disabled",true);
				$("#lendSubmit").empty().text("Loading...");
				$("#lendFrom").ajaxSubmit({
					 type: "post",
			         url: _ctx + "/lendcoin/lendTimesApplyAdd",
			         dataType: "JSON",
			         data : {"lendTimestr":lendTimestr},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
			        	 $("#lendSubmit").attr("disabled",false);
						 $("#lendSubmit").empty().text(shenqing);
	
						  layer.alert(shenqingdaishenhe);
			        	 /*layer.open({
						   type: 1,
						   title: '',
						   area: ['500px', '550px'],
						   scrollbar: false,   // 父页面 滚动条 禁止
						   shadeClose: false, //点击遮罩关闭
						   content: $('#Popup')
					     });*/
			        	 
			        	 $('#table').bootstrapTable('refresh');
			         }else{
			         	 $("#lendSubmit").attr("disabled",false);
						 $("#lendSubmit").empty().text(shenqing);
			        	 layer.alert(data.msg);
			        	 
			         }
			         }
				})
			})
			
			//分页bootstrapTable插件
			var conf = {

				url : _ctx + "/lendcoin/lendTimesApplyList",
				columns : [
				{
					title : shenqingshijian,
					field : 'applyTime',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : gangganbeishu,
					field : 'lendTimes',
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
						if(value=="1"){
						  return daishenhe;
						}else{
						   if(row.lendTimesStatus=="1"){
						    return tongguo;
						   }else{
						     return bohui;
						   }
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
