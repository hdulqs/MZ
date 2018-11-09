define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/layer/css/layer.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/mobile/js/public.js");
	require("style/js/mobile/js/public.js");
	require("style/js/extract/common/rmbot.js");
	this.md5 = require("style/js/hrymd5");
	this.validate = require("style/js/validate");
	
	module.exports = {
		init : function(){
			//清除定时器
			clearPageTimer()
			
			if(tokenId!=""){
				//获取手续费费率
				$.ajax({
					url: ctx_ + "/mobile/user/apppersondetail/isrealandpwd.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data!="" && data!=null){
							if(data.success){debugger
								if(data.obj.user.isReal=='0'){
									layer.msg("请先实名", {icon: 2,time:1500},function(){
										window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
									});
								}else if(data.obj.user.accountPassWord==null || data.obj.user.accountPassWord==""){
									layer.msg("请先设置交易密码", {icon: 2,time:1500},function(){
										window.open(basepath + "/html/user/pwd_tran.html?tokenId="+tokenId,"_self");
									});
								}else{
									$("#hotmoney").text(data.obj.myAccount.hotMoney);
									$("#witfee").text(data.obj.witfee);
									$("#maxWithdrawMoney").val(data.obj.maxWithdrawMoney);
									$("#maxWithdrawMoneyOneTime").val(data.obj.maxWithdrawMoneyOneTime);
								}
							}else{
								layer.msg(data.msg, {icon: 2});
								window.open(basepath + "/html/user/login.htm","_self");
							}
						}
					}
				});
			}
			//回显用户姓和名
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
									window.open(basepath + "/html/user/roveath.html?tokenId="+tokenId,"_self");
								});
							}else{
								$("#ming").val(data.obj.user.truename);
								$("#xing").val(data.obj.user.surname);
							}
						}else{
							layer.msg(data.msg, {icon: 2});
							window.open(basepath + "/html/user/login.htm","_self");
						}
					}
				}
			});
			
			//加载当前账户银行
			$.ajax({
				url: ctx_ + "/mobile/user/appbankcode/findBankCard.do",
				type:"post",
				data : {tokenId:tokenId},
				dataType:'json',
				success:function(data){debugger
					if(data!="" && data!=null){
						if(data.success){
							var obj = eval(data.obj);
							var html = "";
							for(var i=0;i<obj.length;i++){
								html += "<option value="+obj[i].id+">"+obj[i].cardNumber+" "+obj[i].cardBank+"</option>"
							}
							$("#bankselect").append(html);
						}else{
							layer.msg(data.msg, {icon: 2});
							window.open(basepath + "/html/user/login.htm","_self");
						}
					}
				}
			});
			$("#money_rmb").on("blur",function(){
				var okhotMoney = $("#hotmoney").text();//可用金额
				var transactionMoney = $("#money_rmb").val();//当前输入金额
				var maxWithdrawMoneyOneTime = $("#maxWithdrawMoneyOneTime").val();//单笔最多
				var i, j, strTemp;
			    strTemp = "0123456789";
				if(parseInt(transactionMoney)>parseInt(okhotMoney)){
					layer.msg('不可大于可提现金额', {icon: 2});
					return false;
				}else if(parseInt(transactionMoney)>parseInt(maxWithdrawMoneyOneTime)){
					layer.msg('单笔不得超过'+maxWithdrawMoneyOneTime+'RMB', {icon: 2});
					return false;
				}else{
					if(transactionMoney.length>0){
						for (i = 0; i < transactionMoney.length; i++) {
					         j = strTemp.indexOf(transactionMoney.charAt(i));
					         if (j == -1) {
					        	 layer.msg('金额必须为数字', {icon: 2});
					        	 return false;
					         }else{
					        	 
					        	 var actualMoney = parseInt(transactionMoney) * $("#witfee").text()/100;
								 var actualMoney_b = parseInt(transactionMoney) - actualMoney;
								 $("#true_daozhang").empty().val("手续费额"+actualMoney+"RMB,实际到账金额 "+actualMoney_b+"RMB");
					         }
					     }
					}
				}
			})
			
			//确认提现
			$("#submit").on("click",function(){
				//银行选择
				var bankselect = $("#bankselect").val();
				if(bankselect==""){
					layer.msg("请选择银行", {icon: 2});
				}
				var okhotMoney = $("#hotmoney").text();//可用金额
				var transactionMoney = $("#money_rmb").val();//当前输入金额
				var maxWithdrawMoneyOneTime = $("#maxWithdrawMoneyOneTime").val();//单笔最多
				var i, j, strTemp;
			    strTemp = "0123456789";
				if(parseInt(transactionMoney)>parseInt(okhotMoney)){
					layer.msg('不可大于可提现金额', {icon: 2});
					return false;
				}else if(parseInt(transactionMoney)>parseInt(maxWithdrawMoneyOneTime)){
					layer.msg('单笔不得超过'+maxWithdrawMoneyOneTime+'RMB', {icon: 2});
					return false;
				}else{
					if(transactionMoney.length>0){
						for (i = 0; i < transactionMoney.length; i++) {
					         j = strTemp.indexOf(transactionMoney.charAt(i));
					         if (j == -1) {
					        	 layer.msg('金额必须为数字', {icon: 2});
					        	 return false;
					         }else{
					        	 
					        	 var actualMoney = parseInt(transactionMoney) * $("#witfee").text()/100;
								 var actualMoney_b = parseInt(transactionMoney) - actualMoney;
								 $("#true_daozhang").empty().val("手续费额"+actualMoney+"RMB,实际到账金额 "+actualMoney_b+"RMB");
					         }
					     }
					}
				}
				var rmbout_pwdtrade = $("#rmbout_pwdtrade").val();
				if(!rmbout_pwdtrade){
					layer.msg('交易密码不能为空', {icon: 2});
					return false;
				}
				/*var phone_code_login = $("#phone_code_login").val();
				if(!phone_code_login){
					layer.msg('验证码不能为空', {icon: 2});
					return false;
				}*/
				//提现
				$.ajax({
					url: ctx_ + "/mobile/user/apprmbwithdraw/rmbwithdraw.do",
					type:"post",
					data : {accountPassWord:md5.md5(rmbout_pwdtrade),bankCardId:bankselect,/*withdrawCode:phone_code_login,*/transactionMoney:transactionMoney,tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data.success){
		        			 layer.msg("申请成功!", {icon: 1,time:1500},function(){
		        				 window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
		        			 });
		        		 }else{
		        			 layer.msg(data.msg, {icon: 2});
		        		 }
					}
				});
			})
		},
		bank : function(){
			if(tokenId!=""){
				//加载银行
				$.ajax({
					url: ctx_ + "/mobile/user/apprmbdeposit/selectRedisBank.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						debugger;
						if(data!="" && data!=null){
							if(data.success){
								var obj = eval(data.obj.key);
								var html = "";
								var bank=eval(obj)
								$("#bankname").val(bank[0].itemName)
								for(var i=0;i<bank.length;i++){
									html += "<option value="+bank[i].itemName+">"+bank[i].itemName+"</option>"
								}
								$("#bank").append(html);
							}
						}
					}
				});
				//选择银行
				$("#selbank").on("change",function(){
					$("#bankname").val($(this).val());
				})
				//查询省
				$.ajax({
					type : "POST",
					dataType : "JSON",
					url : ctx_ + "/mobile/user/appbankcode/findArea.do",
					data : {tokenId:tokenId}, 
					cache : false,
					success : function(data) {debugger
						var obj = eval(data.obj);
						var html = "";
						for(var i=0;i<obj.length;i++){
							html += "<option value="+obj[i].key+">"+obj[i].province+"</option>"
						}
						$("#p1").append(html);
					}
				})
				//获取选中的省
				$("#p1").on("change",function(){
					//得到下拉列表的相应的值
					var id = this.value;
					var selectName = $(this).find("option:selected").val();
					//给隐藏文本框赋值
					$("#provinceValue").val(selectName);
					$("#c1 option").remove();
					//查询市
					$.ajax({
						type : "POST",
						dataType : "JSON",
						url : ctx_ + "/mobile/user/appbankcode/appcity/"+$("#provinceValue").val(),
						data : {tokenId:tokenId},
						cache : false,
						success : function(data) {
							var obj = eval("["+data.obj+"]");
							var html = "";
							for(var i=0;i<obj.length;i++){
								html += "<option value="+obj[i].city+">"+obj[i].city+"</option>"
							}
							$("#c1").append(html);
						}
					})
				});
				//添加银行卡
				$("#addBankcard").on("click",function(){debugger
					var bankname = $("#bankname").val();//银行
					var p1 = $("#p1").val();//省
					var c1 = $("#c1").val();//市
					var subBank = $("#subBank").val();//开户支行
					var subBankNum = $("#subBankNum").val();//银行机构代码
					/*var cardName = $("#name").val();*///持卡人
					var ming =$("#ming").val();//持卡名
					var xing =$("#xing").val();//持卡姓
					var account = $("#account").val();//银行卡号
					
					if(!bank){
						layer.msg("请选择银行",{icon:2});return false;
					}
					if(p1=="-1"){
						layer.msg("请选择银行卡所在地",{icon:2});return false;
					}
					if(!subBank){
						layer.msg("开很支行不能为空",{icon:2});return false;
					}
					if(!account){
						layer.msg("银行卡号不能为空",{icon:2});return false;
					}
					//保存银行卡
					$.ajax({
						 type: "post",
				         url: ctx_ + "/mobile/user/appbankcode/saveBankCard.do",
				         data : {bankname:bankname,p1:p1,c1:c1,subBank:subBank,subBankNum:subBankNum,trueName:ming,surName:xing,account:account,tokenId:tokenId},
				         dataType: "JSON",
				         success: function(data) {debugger
				        	 if(data!=undefined){
				        		 if(data.success){
				        			 layer.msg("添加成功",{icon:1,time:1500},function(){
				        				 window.open(basepath + "/html/user_exchange/rmbot.html?tokenId="+tokenId,"_self");
				        			 })
				        		 }else{
				        			 layer.msg(data.msg, {icon: 2});
				        		 }
				        	 }
				         }
					})
				})
			}
		},
		//发送短信
		sendsms :function(){
			$("#sendsmsBtn").on("click",function(){
				var rmbout_pwdtrade = $("#rmbout_pwdtrade").val();//交易密码
				var money_rmb = $("#money_rmb").val();//提现金额
				
				if(!rmbout_pwdtrade){
					layer.msg("交易密码必填!",{icon:2});
					return false;
				}
				if(!money_rmb){
					layer.msg("提现金额必填!",{icon:2});
					return false;
				}
				
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : ctx_ + "/mobile/user/apprmbwithdraw/getRmbWithdrawCode.do",
					data : {
						transactionMoney : money_rmb,
						accountPassWord : md5.md5(rmbout_pwdtrade),
						tokenId : tokenId
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("发送成功", {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["tixian"]);
								// 开启点击后定时数字显示
								pageTimer["tixian"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html("发送验证码");
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(pageTimer["tixian"]);
									} else {
										$("#sendsmsBtn").html(time+"秒后重新发送" );
									}

								}, 1000);
								
							}else{
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2});
								window.open(basepath + "/html/user/login.htm","_self");
							}
						}else{
							$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
							layer.msg("发送失败", {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		}
	}
})