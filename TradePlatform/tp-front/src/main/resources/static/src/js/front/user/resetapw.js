define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			//清除定时器
			clearPageTimer()
			
			$("#submitBtn").on("click",function(){
				
				var passWord = $("#passWord").val();
				var accountPassWord = $("#accountPassWord").val();
				var reaccountPassWord = $("#reaccountPassWord").val();
				
				var accountpwSmsCode = $("#accountpwSmsCode").val();
				
				if(!passWord){
					layer.msg('登录密码不能为空', {icon: 2});
					return ;
				}
				if(!accountPassWord){
					layer.msg('新密码不能为空', {icon: 2});
					return ;
				}
				if(!validate.check_password(accountPassWord)){
					layer.msg('新密码格式不正确', {icon: 2});
					return ;
				}
				if(!reaccountPassWord){
					layer.msg('确认密码不能为空', {icon: 2});
					return ;
				}
				if(accountPassWord!=reaccountPassWord){
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
					url : _ctx + "/user/resetapwsubmit",
					data : {
						passWord : md5.md5(passWord),
						accountPassWord : md5.md5(accountPassWord),
						reaccountPassWord : md5.md5(reaccountPassWord),
						accountpwSmsCode : accountpwSmsCode
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("重置成功!", {icon: 1,time:1000},function(){
									window.location.href = _ctx+"/user/center";
								})
								
							}else{
								layer.msg(data.msg, {icon: 2});
								$("#submitBtn").removeAttr("disabled");
							}
						}else{
							layer.msg("重置失败", {icon: 2});
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
				var passWord = $("#passWord").val();
				var accountPassWord = $("#accountPassWord").val();
				var reaccountPassWord = $("#reaccountPassWord").val();
				
				
				if(!passWord){
					layer.msg('登录密码不能为空', {icon: 2});
					return ;
				}
				if(!accountPassWord){
					layer.msg('新密码不能为空', {icon: 2});
					return ;
				}
				if(!validate.check_password(accountPassWord)){
					layer.msg('新密码格式不正确', {icon: 2});
					return ;
				}
				if(!reaccountPassWord){
					layer.msg('确认密码不能为空', {icon: 2});
					return ;
				}
				if(accountPassWord!=reaccountPassWord){
					layer.msg('两个密码不一致', {icon: 2});
					return ;		
				}
				
	
				$(this).attr("disabled","disabled");
				$(this).html("已发送");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getReApwSmsCode",
					data : {
						passWord : md5.md5(passWord),
						accountPassWord : md5.md5(accountPassWord),
						reaccountPassWord : md5.md5(reaccountPassWord)
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("发送成功", {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["resetapw"]);
								// 开启点击后定时数字显示
								pageTimer["resetapw"]  = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html("点击");
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										$("#sms_message").html("");// 按钮可用
										window.clearInterval(pageTimer["resetapw"]);
									} else {
										$("#sms_message").html(time+"秒后重新获取" );
									}
	
								}, 1000);
								
							}else{
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
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
	

});