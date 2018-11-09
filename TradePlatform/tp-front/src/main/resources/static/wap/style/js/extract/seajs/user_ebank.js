define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/layer/css/layer.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/index.js");
	require("style/js/mobile/js/public.js");
	require("style/js/Fnc.js");
	require("style/js/recharge.js");
	require("style/js/extract/common/user_ebank.js");
	
	module.exports = {
		init : function(){
			$("#Popup").hide();
			
			//定义一个局部变量
			var bankName0 = '';
			
			if(tokenId!=""){
				//跳转到首页 我的账户 设置
				$("#bbjy").on("click",function(){
					window.open(basepath + "/html/coins.htm?tokenId="+tokenId,"_self");
				})
				$("#wdzh").on("click",function(){
					window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
				})
				$("#sz").on("click",function(){
					window.open(basepath + "/html/user/account.html?tokenId="+tokenId,"_self");
				})
				
				$.ajax({
					url: ctx_ + "/mobile/user/apppersondetail/isrealandpwd.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data!="" && data!=null){
							if(data.success){
								if(data.obj.user.isReal=='0'){
									layer.msg("请先实名", {icon: 2,time:1500},function(){
										window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
									});
								}else{
									$("#huikuanrenming").val(data.obj.user.truename);
									$("#huikuanrenxing").val(data.obj.user.surname);
								}
							}else{
								layer.msg(data.msg, {icon: 2});
								window.open(basepath + "/html/user/login.htm","_self");
							}
						}
					}
				});
			}
			//加载银行
			$.ajax({
				url: ctx_ + "/mobile/user/apprmbdeposit/selectRedisBank.do",
				type:"post",
				data : {tokenId:tokenId},
				dataType:'json',
				success:function(data){
					if(data!="" && data!=null){
						debugger;
						if(data.success){
							var obj = eval(data.obj);
							var html = "";
							var key =eval(obj.key)
							$("#bankName").val(key[0].itemName);
							for(var i=0;i<key.length;i++){
								html += "<option value="+key[i].itemName+">"+key[i].itemName+"</option>"
							}
							$("#selbank").append(html);
						}else{
							layer.msg(data.msg, {icon: 2});
						}
					}
				}
			});
			//选择银行
			$("#selbank").on("change",function(){
				debugger;
				var value=$("#selbank").val();
				$("#bankName").val(value);
			})
			
			var bankCodeFlag = 0;
			var bankAmountFlag = 0;
			//银行卡号
			$("#bankcode").on("blur",function(){
				var code = this.value;
				if(code==''){
					layer.msg('银行卡号必填', {icon: 2});
					bankCodeFlag = 0;
				}else{
					 var i, j, strTemp;
				     strTemp = "0123456789";
				     if (code.length == 0) return 0
				     for (i = 0; i < code.length; i++) {
				         j = strTemp.indexOf(code.charAt(i));
				         if (j == -1) {
				        	 layer.msg('银行卡号必须为数字', {icon: 2});
				        	 bankCodeFlag = 1;
				         }else{
				        	 bankCodeFlag = 2;
				         }
				     }
				}
			})
			//金额
			$("#over_num").on("blur",function(){debugger
				var money = this.value;
				if(money==''){
					layer.msg('金额不能为空', {icon: 2});
					bankAmountFlag = 00;
				}else{
					var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
					if(!reg.test(money)){
						layer.msg('金额格式不正确', {icon: 2});
						bankAmountFlag = 1;
					}else{
						bankAmountFlag = 2;
						$("#over_num_p").empty().text("实际到账金额:"+money+"RMB,请严格按照当前金额汇款。");
					}
				}
			})
			//生成充值汇款单
			$("#rmbsubmit").on("click",function(){
				if(bankCodeFlag==0){
					layer.msg('银行卡号必填', {icon: 2});
					return false;
				}else if(bankCodeFlag==1){
					layer.msg('银行卡号必须为数字', {icon: 2});
					return false;
				}
				if(bankAmountFlag==0){
					layer.msg('金额不能为空', {icon: 2});
					return false;
				}else if(bankAmountFlag==1){
					layer.msg('金额格式不正确', {icon: 2});
					return false;
				}
				
				$("#rmbsubmit").attr("disabled",true);
				$("#rmbsubmit").empty().text("Loading...");
				$.ajax({
					url: ctx_ + "/mobile/user/apprmbdeposit/rmbdeposit.do",
					type:"post",
					data : {tokenId:tokenId,surname:$("#huikuanrenxing").val(),remitter:$("#huikuanrenming").val(),bankCode:$("#bankcode").val(),bankAmount:$("#over_num").val(),bankName:$("#bankName").val()},
					dataType:'json',
					success:function(data){
						if(data!="" && data!=null){debugger;
							if(data.success){
								$("#rmbsubmit").attr("disabled",false);
								 $("#rmbsubmit").empty().text("生成银行汇款单");
								 
								 $("#bankAccount").empty().html("<b>"+data.obj.accountNumber+"</b>");
					         	 $("#bankName_").empty().html(data.obj.bankName);
					         	 $("#bankAddress").empty().html(data.obj.bankAddress);
					         	 $("#accountName").empty().html(data.obj.accountName);
					         	 $("#remittanceMoney").empty().html("<b>"+data.obj.transactionMoney+" (汇款时时填写金额)</b>");
					         	 $("#transactionNum").empty().text(data.obj.transactionNum);
					         	 $("#remark").empty().html("<b>"+data.obj.remark+"  (汇款时备注内容)</b>");
								 
					        	 layer.open({
								   type: 1,
								   title: '',
								   area: ['400px', '450px'],
								   scrollbar: false,   // 父页面 滚动条 禁止
								   shadeClose: false, //点击遮罩关闭
								   content: $('#Popup')
							     });
							}
						}
					}
				});
			})
		}
	}
})