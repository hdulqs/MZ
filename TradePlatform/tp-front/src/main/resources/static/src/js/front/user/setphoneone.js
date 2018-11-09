
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
			clearPageTimer(); 
			
			$(".verifyLayout").hide();
			$(".verifyLayout1").hide();
			$(".verifyLayout2").hide();
			
			$("#mobile-number").intlTelInput();


			
             
			
			$("#submitBtn_one").on("click",function(){

				var verifyCode = $("#verifyCode-one").val();
				if(!verifyCode){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
                $.ajax({
                    type : "get",
                    url : _ctx  + "/phone/setphonetwo",
                    cache : false,
                    data : {
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
			$("#yzm-btn-one").on("click",function(){
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getPwdPhone",
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
										$("#yzm-btn-one").html(dianji);
										$("#yzm-btn-one").removeAttr("disabled");// 按钮可用
										$("#yzm-btn-one").html(chongxinfasong);// 按钮可用
										window.clearInterval(pageTimer["setphone"]);
									} else {
										$("#yzm-btn-one").html(time+miaochongxinfasong );
									}
	
								}, 1000);
								
							}else{
								$("#yzm-btn-one").removeAttr("disabled");// 按钮可用
								$("#yzm-btn-one").html(chongxinfasong);// 按钮可用
								if(data.msg=200){
									layer.msg(phone_format_erro, {icon: 2})
								}
							}
						}else{
							
							$("#yzm-btn-one").removeAttr("disabled");// 按钮可用
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