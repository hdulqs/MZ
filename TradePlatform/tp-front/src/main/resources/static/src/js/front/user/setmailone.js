
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



                $("#submitBtn").on("click", function () {
                    var mail = $("#mail-number").val();

                    if (!mail) {
                        layer.msg("邮箱不能为空！", {icon: 2});
                        return;

                    }
                    var verifyCode = $("#verifyCode").val();
                    if (!verifyCode) {
                        layer.msg("邮箱验证码不能为空！", {icon: 2});
                        return;
                    }
                    $.ajax({
                        type : "get",
                        url : _ctx  + "/mail/setmailtwo",
                        cache : false,
                        data : {
                            mail:mail,
                            verifyCode :verifyCode
                        },
                        dataType : "html",
                        success : function(data) {
                            //hrynologinkeye993f9d7322e4fcab554fe3741d46aca 为退出时OauthFilter中返回一个不重复的值
                            if(data!=undefined&&data.indexOf("hrynologinkeye993f9d7322e4fcab554fe3741d46aca")!=-1){

                            }else{
                                //清空主体
                                $("#content").empty();
                                //进行渲染
                                $("#content").html(data);
                            }
                        },
                        error : function(e) {
                            console.log("加载"+url+"出错")
                        }
                    });

                });
			
		},
		
		
		//发送短信
		sendsms :function(){//debugger;
			$("#yzm-btn").on("click",function(){
				var mail=$("#mail-number").val();
				if(!mail){
					layer.msg("邮箱地址不能为空！", {icon: 2});
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
								layer.msg(fasongchenggong, {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["setphone"]);
								// 开启点击后定时数字显示
								pageTimer["setphone"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#yzm-btn").html(dianji);
										$("#yzm-btn").removeAttr("disabled");// 按钮可用
										$("#yzm-btn").html(chongxinfasong);// 按钮可用
										window.clearInterval(pageTimer["setphone"]);
									} else {
										$("#yzm-btn").html(time+miaochongxinfasong );
									}
	
								}, 1000);
								
							}else{
								$("#yzm-btn").removeAttr("disabled");// 按钮可用
								$("#yzm-btn").html(chongxinfasong);// 按钮可用
								if(data.msg=200){
									layer.msg(phone_format_erro, {icon: 2})
								}
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