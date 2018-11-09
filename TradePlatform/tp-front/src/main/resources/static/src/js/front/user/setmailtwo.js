
define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	require("js/plugins/regIntlTelInput");
	this.base = require("js/base/base");
	require("js/base/secondvail");
	this.firstvail = require("js/base/firstvail");

	module.exports = {
		
		//初始化方法
			init : function(){
			//清除定时器
			
			$(".verifyLayout").hide();
			$(".verifyLayout1").hide();
			$(".verifyLayout2").hide();


			
			$("#submitBtn2").on("click",function(){
				var mail=$("#mail-number").val();

				if(!mail){
					layer.msg("邮箱不能为空！", {icon: 2});
					return ;
				
				}
				var verifyCode = $("#verifyCode2").val();
				if(!verifyCode){
					layer.msg("邮箱验证码不能为空！", {icon: 2});
					return ;
				}
				$.ajax({
					type : "post",
					url : _ctx + "/mail/setMailtwo",
					data :{mail:mail,verifyCode:verifyCode},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								//window.location.href = _ctx+"/user/center";
                                base.loadUrl( _ctx +'/v.do?u=front/user/safe');
							});

						}else{
							layer.msg(data.msg);
							$("#verifyCode2").val("");
						}
					},
					
				});
            });
			
		},
		
		
		//发送短信
		sendsms :function(){//debugger;
			$("#yzm-btn2").on("click",function(){
				var mail=$("#mail-number").val();
				if(!mail){
					layer.msg("电子邮箱不能为空！", {icon: 2});
					return ;
				
				}
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sendmail/mailcode",
					data : {
                        mail:mail
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(fasongchenggong, {icon: 1});
								
								var time = 120;
								window.clearInterval(pageTimer["setmail"]);
								// 开启点击后定时数字显示
								pageTimer["setmail"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#yzm-btn2").html(dianji);
										$("#yzm-btn2").removeAttr("disabled");// 按钮可用
										$("#yzm-btn2").html(chongxinfasong);// 按钮可用
										window.clearInterval(pageTimer["setmail"]);
									} else {
										$("#yzm-btn2").html(time+miaochongxinfasong );
									}
	
								}, 1000);
								
							}else{
								$("#yzm-btn2").removeAttr("disabled");// 按钮可用
								$("#yzm-btn2").html(chongxinfasong);// 按钮可用
								if(data.msg=200){
									layer.msg("电子邮箱格式不正确", {icon: 2});
								}
							}
						}else{
							
							$("#yzm-btn2").removeAttr("disabled");// 按钮可用
							layer.msg(fasongshibai, {icon: 2});
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		}
	}

});