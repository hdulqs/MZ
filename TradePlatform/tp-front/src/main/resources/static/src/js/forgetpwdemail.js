define(function(require, exports, module) {
	this.md5 = require("js/base/utils/hrymd5");
	this.validate = require("js/base/validate");
	module.exports = {
		init : function(){
			//清除定时器
			try{
				window.clearInterval(forgetTimer);
			}catch(e){
			}
			//第一步
			
			$("#btn-ok").on("click",function(){

				var email = $(".fillemail").val();
				if(!email){
					alert(1)
					layer.msg(emailisnull, {icon: 2});
					return ;
				}
				if(!validate.check_email(email)){
					//layer.msg(emailvail, {icon: 2});

					if(!validate.check_mobile(email)){
						layer.msg("请输入邮箱或手机号码", {icon: 2});
						
						return ;
					}
					window.location.href = _ctx+"/forgetpwd/forgetpwdphone/1" +'?phone=' + email;
					//手机密码找回
				     
				}else{
				//邮箱找回
				$.ajax({
					type : "post",
					url : _ctx + "/forgetService",
					data : {
						email : email,
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(chongzhiemail, {icon: 1,time:1500},function(){
									window.location.href = _ctx+"/findPwdTip";
								})
								
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							layer.msg(chongzhishibai, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				 });
				}
				/*$("#form_forget1").ajaxSubmit({
					type: "post",
			         url: _ctx + "/forgetpwd/firststep",
			         dataType: "JSON",
			         data : {code:code,phoneNum:phoneNum},
			         success: function(data) {debugger
			        	 if(data.success){
			        		 window.location.href = _ctx+"/forgetpwd/forgetpwd/2";
			        	 }else{
			        		 layer.msg(data.msg, {icon: 2});
			        	 }
			         }
				})*/
			})
			//第二步
			$("#forgetPwdBtn2").on("click",function(){debugger;
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
			         success: function(data) {debugger
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