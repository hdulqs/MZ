
define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	module.exports = {
		
		//初始化方法
		init : function(){
			//清除定时器
			clearPageTimer(); 
		
			
			
			
             
			
			$("#submitBtn").on("click",function(){debugger;
				var verifyCode = $("#verifyCode").val();
				var mail=$("#mail_code").val();
				if(!verifyCode){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
				
				//$("#submitBtn").attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/mail/cancelMail",
					data : {
                        mail:mail,
						verifyCode:verifyCode
						
					},
					cache : false,
					dataType : "json",
					success : function(data) {debugger;
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								///window.location.href = _ctx+"/user/center";
                                base.loadUrl( _ctx +'/v.do?u=front/user/safe');
							})

						}else{
							layer.msg(data.msg);
						}
					},
					
				});
				
			});
			
		},
		
		
		//发送短信
		sendsms :function(){//debugger;
			$("#yzm-btn").on("click",function(){//debugger;
				var mail=$("#mail_code").val();
				
				$(this).attr("disabled","disabled");
				$(this).html(yifasong);
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
							if(data.success){debugger
								layer.msg(fasongchenggong, {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["setaphone"]);
								// 开启点击后定时数字显示
								pageTimer["setaphone"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#yzm-btn").html(dianji);
										$("#yzm-btn").removeAttr("disabled");// 按钮可用
										$("#yzm-btn").html(chongxinfasong);// 按钮可用
										window.clearInterval(pageTimer["setaphone"]);
									} else {
										$("#yzm-btn").html(time+miaochongxinfasong );
									}
	
								}, 1000);
								
							}else{
								$("#yzm-btn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							$("#yzm-btn").removeAttr("disabled");// 按钮可用
							layer.msg(fasongshibai, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		}
	}
	

});