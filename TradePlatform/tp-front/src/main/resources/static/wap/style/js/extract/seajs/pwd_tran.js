define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/layer/css/layer.css");
	require("style/js/mobile/js/public.js");
	this.md5 = require("style/js/hrymd5");
	this.validate = require("style/js/validate");
	
	module.exports = {
		init : function(){
			
			//清除定时器
			clearPageTimer()
			
			$("#submitBtn").on("click",function(){
				var reaccountPassWord = $("#newaccountPassWord").val();//新交易密码
				var newaccountPassWordref = $("#newaccountPassWordref").val();//重复新交易密码
				var accountpwSmsCode = $("#phone_code_login").val();//短信验证码
				
				if(!validate.check_password(reaccountPassWord)){
					layer.msg('交易密码格式不正确', {icon: 2});
					return ;
				}
				if(!reaccountPassWord){
					layer.msg('确认密码不能为空', {icon: 2});
					return ;
				}
				if(reaccountPassWord!=newaccountPassWordref){
					layer.msg('两个密码不一致', {icon: 2});
					return ;		
				}
				
				if(!accountpwSmsCode){
					layer.msg('短信验证码不能为空', {icon: 2});
					return ;
				
				}
				
				$("#submitBtn").attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : ctx_ + "/mobile/user/apppersondetail/appjypwd.do",
					data : {
						accountPassWord : md5.md5(reaccountPassWord),
						reaccountPassWord : md5.md5(newaccountPassWordref),
						accountpwSmsCode : accountpwSmsCode,
						tokenId : tokenId
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(data.msg, {icon: 1,time:1500},function(){
									window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
								});
							}else{
								layer.msg(data.msg, {icon: 2});
								$("#submitBtn").removeAttr("disabled");
							}
						}else{
							layer.msg("修改失败", {icon: 2})
							$("#submitBtn").removeAttr("disabled");
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		},
		//发送短信
		sendsms :function(){
			$("#sendsmsBtn").on("click",function(){
				var reaccountPassWord = $("#newaccountPassWord").val();//新交易密码
				var newaccountPassWordref = $("#newaccountPassWordref").val();//重复新交易密码
				var accountpwSmsCode = $("#phone_code_login").val();//短信验证码
				
				if(!validate.check_password(reaccountPassWord)){
					layer.msg('交易密码格式不正确', {icon: 2});
					return ;
				}
				if(!reaccountPassWord){
					layer.msg('确认密码不能为空', {icon: 2});
					return ;
				}
				if(reaccountPassWord!=newaccountPassWordref){
					layer.msg('两个密码不一致', {icon: 2});
					return ;		
				}
				
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : ctx_ + "/mobile/user/apppersondetail/appjypwdcode.do",
					data : {
						accountPassWord : md5.md5(reaccountPassWord),
						reaccountPassWord : md5.md5(newaccountPassWordref),
						accountpwSmsCode : accountpwSmsCode,
						tokenId : tokenId
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("发送成功", {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["jypwd"]);
								// 开启点击后定时数字显示
								pageTimer["jypwd"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html("发送验证码");
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(pageTimer["jypwd"]);
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