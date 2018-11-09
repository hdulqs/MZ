define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/layer/css/layer.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/mobile/js/public.js");
	require("style/js/form.js");
	require("style/js/base.js")
	require("style/js/index.js")
	this.md5 = require("style/js/hrymd5");
	this.validate = require("style/js/validate");
	require("style/js/extract/common/findpwd.js");
	module.exports = {
			init : function(){
				//清除定时器
				try{
					window.clearInterval(forgetTimer);
				}catch(e){
				}
				
				//加载logo 	
				$.ajax({
					url: ctx_ + "/mobile/nouser/logo",
					type:"post",
					dataType:'json',
					success:function(data){
						//加载logo 	
						var url=ctx_+"/"+data.obj;
						log.style.backgroundImage="url("+url+")";
						
					}
				})
	
				//第一步
				
				$("#btn-ok").on("click",function(){
					var email = $("#email").val();
					if(!email){
						layer.msg("邮箱不能为空", {icon: 2});
						return ;
					}
					if(!validate.check_email(email)){
						layer.msg("邮箱格式不正确", {icon: 2});
						return ;
					}
					
					$.ajax({
						type : "post",
						url : _ctx + "/mobile/nouser/forgetSmsCode",
						data : {
							email : email,
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							if(data){
								if(data.success){
									layer.msg("重置成功,请登录邮件修改后登录！", {icon: 1,time:3000},function(){
										window.location.href = _ctx+"/static/wap/html/user/login.htm";
									})
									
								}else{
									layer.msg("该用户不存在", {icon: 2})
								}
							}else{
								layer.msg("重置失败", {icon: 2})
							}
						},
						error : function(e) {
							
						}
					});
					/*$("#form_forget1").ajaxSubmit({
						type: "post",
				         url: _ctx + "/forgetpwd/firststep",
				         dataType: "JSON",
				         data : {code:code,phoneNum:phoneNum},
				         success: function(data) {
				        	 if(data.success){
				        		 window.location.href = _ctx+"/forgetpwd/forgetpwd/2";
				        	 }else{
				        		 layer.msg(data.msg, {icon: 2});
				        	 }
				         }
					})*/
				})
				//第二步
				$("#forgetPwdBtn2").on("click",function(){;
					var passwd = $("#passwd").val();
					var confirmpwd = $("#confirmpwd").val();
					if(passwd == "" || passwd == undefined){
						layer.msg(mimabunengweikong, {icon: 2});
						return false;
					}
					var reg = /^[a-zA-Z][a-zA-Z0-9]{5,19}/;
					if(!reg.test(passwd)){
						layer.msg(mimageshibuzhengque, {icon: 2});
						return false;
					}
					if(passwd!=confirmpwd){
						layer.msg(passowordssecond, {icon: 2});
						return false;
					}
					var tel = $("#tel").val();
					$("#form_forget2").ajaxSubmit({
						type: "post",
				         url: _ctx + "/forgetpwd/secondstep",
				         dataType: "JSON",
				         data : {passwd:md5.md5(passwd),tel:tel},
				         success: function(data) {
				        	 if(data.success){
				        		 window.location.href = _ctx+"/forgetpwd/forgetpwd/3";
				        	 }else{
				        		 layer.msg(data.msg, {icon: 2});
				        	 }
				         }
					})
				})
			},
			setInterval : function(){
				var t=5;//设定跳转的时间 
				window.setInterval(function(){
					if(t==0){
						 window.location.href=_ctx+"/login"; //#设定跳转的链接地址 
				    } 
				    $("#time").text(t); // 显示倒计时 
				    t--; // 计数器递减 
				},1000);
			}
		}
})