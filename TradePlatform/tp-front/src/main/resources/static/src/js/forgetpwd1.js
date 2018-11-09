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
			$("#sendsmsBtn").on("click",function(){
				var phoneNum = $("#phoneNum").val();				
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/sendForgetSmsCode",
					data : {
						phoneNum : phoneNum
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg('发送成功', {icon: 1})
								
								var time = 120;
								window.clearInterval(forgetTimer);
								// 开启点击后定时数字显示
								var forgetTimer = window.setInterval(function() {
									time = time - 1;									
									if (time == 0) {
										$("#sendsmsBtn").html('发送短信验证码');
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(forgetTimer);
									} else {
										$("#sendsmsBtn").html(time+"秒后重新发送" );
									}

								}, 1000);
								
							}else{
								debugger;
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
							layer.msg("发送失败", {icon: 2})
						}
					},
					error : function(e) {
						debugger;
					}
				});
				
			});
			$("#forgetPwdBtn").on("click",function(){
				var code = $("#forgetSmsCode").val();
				var phoneNum = $("#phoneNum").val();
				$("#form_forget1").ajaxSubmit({
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
				})
			});
			//第二步
			$("#forgetPwdBtn2").on("click",function(){debugger;
				var passwd = $("#passwd").val();
				var confirmpwd = $("#confirmpwd").val();
				if(passwd == "" || passwd == undefined){
					layer.msg("密码不允许为空", {icon: 2});
					return false;
				}
		/*		if(!validate.check_password(passwd)){
					layer.msg("密码格式不正确", {icon: 2});
					return false;
				}*/
				if(passwd!=confirmpwd){
					layer.msg("两次输入密码不一致", {icon: 2});
					return false;
				}
				var tel = $("#tel").val();
				$("#form_forget2").ajaxSubmit({
					type: "post",
			         url: _ctx + "/forgetpwd/secondstep2",
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
		initdata:function(){
			//获取取传参然后填充
			var result = location.search.match(new RegExp("[\?\&]" + 'phone'+ "=([^\&]+)","i"));
			$("#phoneNum").val(result[1]);
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