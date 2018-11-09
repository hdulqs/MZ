define(function(require,exports,module){
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	
	module.exports = {
		init : function(){
			tool.switchTab();
			tool.switchTab({ele:".form_box",tabList:".payment_li",boxList:".payment_form_box"});
			/***************充值表单******************************/
			$(".form .recharge_num").each(function(index, elemesssnt) {
	              $(this).on('blur',function(){
					    $(this).parents('form').find('.rechargecount').html('0.00');
						if(isNumber(this.value)){
							var num = parseFloat(this.value);
							var maxNum = parseFloat($(this).parents('form').find('input[name="maxnum"]').val());
							var minNum = parseFloat($(this).parents('form').find('input[name="minnum"]').val());
							
							if(maxNum<num)
							{
								showMsg(chongzhijinebunengdayu+"￥"+maxNum.toFixed(2)+yuan);
								this.value="";
								return;
							}else if(minNum>num)
							{
							    showMsg(chongzhijinebunengdiyu+"￥"+minNum.toFixed(2)+yuan);
								this.value="";
								return;
							}
							else
							{
								var fee = parseFloat($(this).parents('form').find('input[name="fee"]').val());
								var total = num * (1-fee);
								
								var rechargeType = $(this).attr('recharge-type');
								//if(rechargeType == 'offline')
								//{
									total += parseFloat($(this).parents('form').find('input[name="randNum"]').val());
								//}
								var total = total.toFixed(2);
								$(this).parents('form').find('.rechargecount').html(total);
								
								return;
							}
						}
						this.value="";
				   });
	        });
	
			$("#Popup").hide();
			
			$.ajax({
				type : "POST",
				dataType : "JSON",
				url : _ctx + "/user/rmbdeposit/selectRedisBank",
				cache : false,
				success : function(data) {
					var json = eval(data.obj);
					var html = "";
					
					for(var i=0;i<json.length;i++){
						html += "<li class='morebank' id='li"+json[i].id+"'><input type='hidden' value='"+json[i].id+"'/><input type='hidden' name='bankName' id='bankName' value='"+json[i].itemName+"'/>" +
								"<img width='128px' height='38px' src="+_ctx+"/"+json[i].remark2+"></li>";
					}
					html += "<li id='showOther' class='morebank' style='display: none;' ><a >"+chakangengduo+"+</a></li>"+
							"<li id='closeOther'  class='morebank' style='display: block;' ><a >"+shouqi+" -</a></li>";
							//<i class='icon_checked'></i>
					$("#bankList").append(html);
					var bankpage = $('#bankList').children('li');
					for(var i=8;i<bankpage.length;i++){
						if($(bankpage[i]).attr("id")=="showOther"){
							$(bankpage[i]).css("display","");
							continue;
						}
						$(bankpage[i]).css("display","none");
					}
				}
			})
			
			var bankFlag = false;
			var remitterFlag = 0;
			var bankCodeFlag = 0;
			var bankAmountFlag = 0;
			var selectedBankName = 0;
			
			//查看更多
			$("#bankList").on("click","li[id=showOther]",function(){
				var bankpage = $('#bankList').children('li');
				for(var i=0;i<bankpage.length;i++){
					if($(bankpage[i]).attr("id")=="showOther"){
						$(bankpage[i]).css("display","none");
						continue;
					}
					$(bankpage[i]).css("display","");
				}
			})
			
			//收起
			$("#bankList").on("click","li[id=closeOther]",function(){
				var bankpage = $('#bankList').children('li');
				for(var i=8;i<bankpage.length;i++){
					if($(bankpage[i]).attr("id")=="showOther"){
						$(bankpage[i]).css("display","");
						continue;
					}
					$(bankpage[i]).css("display","none");
				}
			})
			//高亮
			$("#bankList").on("click","li",function(){
				$("#bankList li i").each(function(){
					$("#bankList li i").removeClass("icon_checked");
				});
				var html = "<i class='icon_checked'></i>";
				$(this).append("<i class='icon_checked'></i>");
				bankFlag = true;
				selectedBankName = $(this).find('input').eq(1).val();
			})
			//汇款人姓名
			$("#remitter").on("blur",function(){
				var name = this.value;
				if(name==''){
					layer.msg(huikuanrenxiangmingbitian, {icon: 2});
					remitterFlag = 1;
				}else{
					//var reg = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
					if(validate.isNumber(name)){
						layer.msg(huikuanrenxingmingnumber, {icon: 2});
						remitterFlag = 2;
					}else{
						remitterFlag = 0;
					}
				}
			})
			//银行卡号
			$("#bankCode").on("input",function(){
				 var code = this.value;
				 if(!validate.isNumber(code)){
					 $("#bankCode_message").html(yinhangkahaobixuweishuzi);
				 }else{
					 $("#bankCode_message").html("");
				 }
			
			})
			//金额
			$("#bankAmount").on("input",function(){
				
				var money = this.value;
				var minRechargeMoney = $("#minRechargeMoney").val();//单笔不能少于
				if(!validate.isNumber(money)){
					$("#bankAmount_message").html(chongzhijinebixuweishuzi);
				}else{
					$("#bankAmount_message").html("");
					
					if(parseInt(money)<parseInt(minRechargeMoney)){
						$("#bankAmount_message").html(danbibunengshaoyu+minRechargeMoney);
						bankAmountFlag = 3;
					}else{
						bankAmountFlag = 0;
						var fee = $("#rechargeFeeRate").val()/100*money;
						$("#promptShow").empty().text(shouxufeie+fee+"RMB, "+shijidaozhangjine+(money-fee)+"RMB ,  "+qingyangeanzhaohuikuan);
					}
				}
			})
			//生成银行汇款单
			$("#generate_single").on("click",function(){
				var minRechargeMoney = $("#minRechargeMoney").val();
				if(bankFlag==false){
					layer.msg(qingxuanzeyinhang, {icon: 2});
					return false;
				}
				if(remitterFlag==1){
					layer.msg(huikuanrenxiangmingbitian,{icon: 2});
					return false;
				}/*else if(remitterFlag==2){
					layer.msg('汇款人姓名必须为中文', {icon: 2});
					return false;
				}*/
				
				 var bankCode = $("#bankCode").val();
				 if(bankCode==undefined||bankCode==""){
					 $("#bankCode_message").html(yinhangkahaobunengweikong);
					 return false;
				 }
				 if(!validate.isNumber(bankCode)){
					 $("#bankCode_message").html(yinhangkaisunmber);
					 return false;
				 }
				
				 var bankAmount = $("#bankAmount").val();
				 if(bankAmount==undefined||bankAmount==""){
					 $("#bankAmount_message").html(chongzhijinebunengweikong);
					 return false;
				 }
				 if(!validate.isNumber(bankAmount)){
					 $("#bankAmount_message").html(chongzhijinebixuweishuzi);
					 return false;
				 }
				 if(parseInt(bankAmount)>99999999999999){
					 layer.msg(jingeguoda, {icon: 2});
					 return false;
				 }
				 var minRechargeMoney = $("#minRechargeMoney").val();//单笔不能少于
				 if(parseInt(bankAmount)<parseInt(minRechargeMoney)){
					$("#bankAmount_message").html(danbibunengshaoyu+minRechargeMoney);
					return false;
				 }
				
				
				$("#generate_single").attr("disabled",true);
				$("#generate_single").empty().text("Loading...");
				$("#cg_bankForm_offline").ajaxSubmit({
					 type: "post",
			         url: _ctx + "/user/rmbdeposit/rmbdeposit",
			         dataType: "JSON",
			         data : {selectedBankName:selectedBankName},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
			        	 $("#generate_single").attr("disabled",false);
						 $("#generate_single").empty().text(""+shengchenghuikuandan+"");
						 
						 $("#bankAccount").empty().html("<b>"+data.obj.accountNumber+"</b>");
			         	 $("#bankName_").empty().html(data.obj.bankName);
			         	 $("#bankAddress").empty().html(data.obj.bankAddress);
			         	 $("#accountName").empty().html(data.obj.accountName);
			         	 $("#remittanceMoney").empty().html("<b>"+data.obj.transactionMoney+" ("+huikuanshitianxiejine+")</b>");
			         	 $("#transactionNum").empty().text(data.obj.transactionNum);
			         	 $("#remark").empty().html("<b>"+data.obj.remark+"  ("+huikuanshibeizhuneirong+")</b>");
			         	 $("#wenxintishi").empty().html(data.obj.remark);
						 
			        	 layer.open({
						   type: 1,
						   title: '',
						   area: ['500px', '550px'],
						   scrollbar: false,   // 父页面 滚动条 禁止
						   shadeClose: false, //点击遮罩关闭
						   content: $('#Popup')
					     });
			        	 
			        	 $('#table').bootstrapTable('refresh');
			         }else{
			        	 layer.alert(data.msg);
			        	 
			         }
			         }
				})
			})
			
			//分页bootstrapTable插件
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
					title : chongzhileixingone,
					field : 'transactionType',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//1线上充值,2线上提现 3线下充值 4线下取现
						if(value==1){
							return xianshangchongzhi;
						}else if(value==2){
							return xiashangtixian;
						}else if(value==3){
							return xianxiatixian;
						}else if(value==3){
							return xianxiaquxian;
						}
					}
				},
				{
					title : chongzhijine,
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
					title : dingdanhao,
					field : 'transactionNum',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : shijian,
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
				},{
					title : bohuiyuanyin,
					field : 'rejetionReason',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//1待审核 2已完成 3已否决
						if(row.rejectionReason!=null){
							return row.rejectionReason;
						}else{
							return '-';
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
			
			//充值筛选条件
			$("#status").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table").bootstrapTable('refresh',null);
			})
		}
	}
})
