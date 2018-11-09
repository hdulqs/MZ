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
				var oldPassWord = $("#oldpwd").val();
				var newPassWord = $("#pwd").val();
				var reNewPassWord = $("#repwd").val();
				var pwSmsCode = $("#phone_code_login").val();
				
				if(!oldPassWord){
					layer.msg('原始登录密码不能为空', {icon: 2});
					return ;
				}
				if(!newPassWord){
					layer.msg('新登录密码不能为空', {icon: 2});
					return ;
				}
				if(newPassWord==oldPassWord){
					layer.msg('新登录密码不能和原始登录密码一致', {icon: 2});
					return ;		
				}
				if(!validate.check_password(newPassWord)){
					layer.msg('新密码格式不正确', {icon: 2});
					return ;
				}
				if(!reNewPassWord){
					layer.msg('第二次密码不能为空', {icon: 2});
					return ;
				}
				if(newPassWord!=reNewPassWord){
					layer.msg('两次密码不一致', {icon: 2});
					return ;
				
				}
				if(!pwSmsCode){
					layer.msg('短信验证码不能为空', {icon: 2});
					return ;
				
				}
				
				$("#submitBtn").attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : ctx_ + "/mobile/user/apppersondetail/appdlpwd.do",
					data : {
						oldPassWord : md5.md5(oldPassWord),
						newPassWord : md5.md5(newPassWord),
						reNewPassWord : md5.md5(reNewPassWord),
						pwSmsCode : pwSmsCode,
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
				var oldpwd = $("#oldpwd").val();//旧密码
				var pwd = $("#pwd").val();//
				var repwd =  $("#repwd").val();
				
				if(!oldpwd){
					layer.msg("登录密码不能为空!",{icon:2});
					return false;
				}
				if(!pwd){
					layer.msg("新密码不能为空!",{icon:2});
					return false;
				}
				if(!repwd){
					layer.msg("重复密码不能为空!",{icon:2});
					return false;
				}
				if(pwd!=repwd){
					layer.msg("两次密码不一致!",{icon:2});
					return false;
				}
				
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : ctx_ + "/mobile/user/apppersondetail/appdlpwdcode.do",
					data : {
						oldPassWord : md5.md5(oldpwd),
						newPassWord : md5.md5(pwd),
						repwd : md5.md5(repwd),
						tokenId : tokenId
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("发送成功", {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["loginpwd"]);
								// 开启点击后定时数字显示
								pageTimer["loginpwd"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html("发送验证码");
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(pageTimer["loginpwd"]);
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