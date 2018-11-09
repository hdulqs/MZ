
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
			
			function  secondvail(){
				var mobile=$("#mobile-number").val();
				var verifyCode = $("#verifyCode1").val();
				var country = $(".dail-txt").html();
				
				
				if(!mobile){
					layer.msg(shoujirenzhengbuweikong, {icon: 2});
					return ;
				
				}if(!verifyCode){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
				mobile = country +" "+ mobile;
				$.ajax({
					type : "post",
					url : _ctx + "/phone/setPhone",
					data : {
						mobile:mobile,
						verifyCode:verifyCode
						
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								window.location.href = _ctx+"/user/center?tokenId="+data.obj;
							})

						}else{
							layer.msg(data.msg);
							$("#verifyCode").val("");
						}
					},
					
				});
			}
             
			
             
			
			$("#submitBtn").on("click",function(){
				var mobile=$("#mobile-number").val();
				var country = $(".dail-txt").html();
				if(!mobile){
					layer.msg(shoujibuweikong, {icon: 2});
					return ;
				
				}
				mobile = country +" "+ mobile;
				var verifyCode = $("#verifyCode1").val();
				if(!mobile){
					layer.msg(shoujirenzhengbuweikong, {icon: 2});
					return ;
				
				}if(!verifyCode){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
				$.ajax({
					type : "post",
					url : _ctx + "/phone/setPhone",
					data :{mobile:mobile,verifyCode:verifyCode},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								window.location.href = _ctx+"/user/center";
							})

						}else{
							layer.msg(data.msg);
							$("#verifyCode").val("");
						}
					},
					
				});
				
			/*	$.ajax({
					type : "post",
					url : _ctx + "/sencodvail",
					data : {
						mobile:mobile,
						verifyCode:verifyCode,
						type:"4"
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								var phone =data.obj.phoneState;
								var google=data.obj.googleState;
								
								if(google==1&&phone==0){
									$(".verifyLayout1").show();
									
								}else if(google==0&&phone==1){
									$(".verifyLayout").show();

								}else if(google==1&&phone==1){
									
									$('#mobile-form').css('display','none');
									$(".verifyLayout2").show();
									 $('.verify-form1').hide();
									 $('.btns').find('span').on('click',function(){debugger;
								      var ind=$(this).index();
								      $(this).addClass('cur').siblings().removeClass('cur');
								      if(ind==0){
								          $('.verify-form').show();
								          $('.verify-form1').hide();
								      }else{
								    	  $('.verify-form1').show();
								    	  $('.verify-form').hide();
								      }
								    })
								
								}else{
									firstvail.sendvail("setphone");
								}
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							layer.msg(data.msg, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});*/
			});
			
			
			
			
			

			$("#sendBtn").on("click",function(){
			
			$(this).attr("disabled","disabled");
			
			$.ajax({
				type : "post",
				url : _ctx + "/sms/smsPhone",
				data : {
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					if(data){
						if(data.success){
							layer.msg(fasongchenggong, {icon: 1})
							$(this).html(yifasong);
							var time = 120;
							window.clearInterval(pageTimer["setphone"]);
							// 开启点击后定时数字显示
							pageTimer["setphone"] = window.setInterval(function() {
								time = time - 1;
								if (time == 0) {
//								$("#sendBtn1").html("点击");
								
//								$("#sendBtn").html("点击");
								$("#sendBtn").removeAttr("disabled");// 按钮可用
								$("#sendBtn").html(chongxinfasong);// 按钮可用
//							    clearInterval(pageTimer["setapw"]);
							    clearInterval(pageTimer["setphone"]);
								} else {
									$("#sendBtn").html(time+miaochongxinfasong );
								}

							}, 1000);
							
						}else{
							$("#sendBtn").removeAttr("disabled");// 按钮可用
							$("#sendBtn").html(chongxinfasong);// 按钮可用
							layer.msg(data.msg, {icon: 2})
						}
					}else{
						$("#sendBtn").removeAttr("disabled");// 按钮可用
						
						layer.msg(fasongshibai, {icon: 2})
					}
				},
				error : function(e) {
					
				}
			});
			
		});
			$("#sendBtn1").on("click",function(){
			
			$(this).attr("disabled","disabled");
			$(this).html(yifasong);
			$.ajax({
				type : "post",
				url : _ctx + "/sms/smsPhone",
				data : {
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
//								$("#sendBtn1").html("点击");
								$("#sendBtn1").removeAttr("disabled");// 按钮可用
								$("#sendBtn1").html(chongxinfasong);// 按钮可用
//								$("#sendBtn").html("点击");
							    clearInterval(pageTimer["setphone"]);
								} else {
									$("#sendBtn1").html(time+miaochongxinfasong );
								}

							}, 1000);
							
						}else{
							$("#sendBtn1").removeAttr("disabled");// 按钮可用
							layer.msg(phone_format_erro, {icon: 2})
						}
					}else{
						$("#sendBtn1").removeAttr("disabled");// 按钮可用
						
						layer.msg(fasongshibai, {icon: 2})
					}
				},
				error : function(e) {
					
				}
			});
			
		});
			
		},
		
		
		//发送短信
		sendsms :function(){//debugger;
			$("#yzm-btn").on("click",function(){
				var mobile=$("#mobile-number").val();
				var country = $(".dail-txt").html();
				if(!mobile){
					layer.msg(shoujibuweikong, {icon: 2});
					return ;
				
				}
				mobile = country +" "+ mobile;
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getPhone",
					data : {
						mobile:mobile
						//accountPassWord : md5.md5(accountPassWord),
						//reaccountPassWord : md5.md5(reaccountPassWord)
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