define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/mobile/js/public.js");
	require("style/js/layer/css/layer.css");
	require("style/js/form.js");
	require("style/js/base.js")

	this.md5 = require("style/js/hrymd5")
	this.validate = require("style/js/validate");
	
	require("style/js/extract/common/reg.js");

	
	module.exports = {
		
		//初始化方法
		init : function(){
			try{
				window.clearInterval(regTimer);
			}catch(e){
			}
			//刷新验证码
			$("#img_captcha").attr('src',_ctx+'/sms/registcode?'+new Date().getTime()); 
			
			$("#img_captcha").on("click",function(){
				$("#img_captcha").attr('src',_ctx+'/sms/registcode?'+new Date().getTime()); 
			})
			
			//验证注册信息
			$("#step").on("click",function(){
				
				var email = $("#email").val();
				var password = $("#pwd").val();
				if(!email){
					layer.msg("邮箱格式不能为空", {icon: 2});
					return ;
				};
				if(!validate.check_email(email)){
					layer.msg("邮箱格式不正确", {icon: 2});
					return ;
				};
				if(!password){
					layer.msg("密码不能为空", {icon: 2});
					return ;
				};
				if(!validate.check_password(password)){
					layer.msg("密码格式不正确", {icon: 2});
					return ;
				};
				localStorage.setItem('email',email);
				localStorage.setItem('password',password);
				$("#form").submit(); 
			});
			
			//注册提交
			$("#regBtn").on("click",function(){
			
				var email = $("#email").val();
				var password = $("#pwd").val();
				var name = $("#name").val();
				var cardtype=$("#cardtype").val();
				var idcard=$("#idcard").val();
				var mo=$("#mo").val();
				var registCode=$("#registCode").val();
				if(!email){
					layer.msg("邮箱格式不能为空", {icon: 2});
					return ;
				};
				if(!validate.check_email(email)){
					layer.msg("邮箱格式不正确", {icon: 2});
					return ;
				};
				if(!password){
					layer.msg("密码不能为空", {icon: 2});
					return ;
				};
				if(!validate.check_password(password)){
					layer.msg("密码格式不正确", {icon: 2});
					return ;
				};
				
				if(!registCode){
					layer.msg("验证码不能为空", {icon: 2});
					return ;
				}
				
				
				$("#regBtn").attr("disabled","disabled");

				$.ajax({
					type : "post",
					url : _ctx+"/registService",
					data : {
						email : email,
						password : md5.md5(password),
						registCode : registCode,
						referralCode : $("#referralCode").val()
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("注册成功请激活", {icon: 1,time:5500},function(){
									window.location.href = _ctx+"/static/wap/html/user/login.htm";
								})
								
							}else{
								$("#regBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
								//刷新验证码
								$("#img_captcha").attr("src", _ctx + "/sms/registcode.jpg?t=" + new Date().getTime());
							}
						}else{
							layer.msg(zheceshibai, {icon: 2})
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
				var username = $("#username").val();
				var password = $("#password").val();
				var registCode = $("#registCode").val();
				if(!username){
					layer.msg(shoujibuweikong, {icon: 2});
					return ;
				}
				if(!validate.check_mobile(username)){
					layer.msg(shoujigeshibuzhengque, {icon: 2});
					return ;
				}
				
				if(!password){
					layer.msg(mimabunengweikong, {icon: 2});
					return ;
				}
				if(!validate.check_password(password)){
					layer.msg(mimageshibuzhengque, {icon: 2});
					return ;
				}
				if(!registCode){
					layer.msg(tuxingyanzhengmaweikong, {icon: 2});
					return ;
				}

				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/registSmsCode",
					data : {
						username : username,
						registCode : registCode
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(fasongchenggong, {icon: 1})
								
								var time = 120;
								window.clearInterval(regTimer);
								// 开启点击后定时数字显示
								var regTimer = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html(fasongyanzhengma);
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(regTimer);
									} else {
										$("#sendsmsBtn").html(time+miaochongxinfasong);
									}

								}, 1000);
								
							}else{
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
							layer.msg(fasongshibai, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		}
		

	}
	
})