
define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	require("js/base/googleauth/utf");
	require("js/base/googleauth/qrcode");
	require("js/base/googleauth/jquery.qrcode");
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
			
			
              $("#submitBtn1").on("click",function(){debugger
				var accountGoogleWord = $("#accountGoogleWord").val();
				var PassWord = $("#PassWord").val();
				if(!accountGoogleWord){
					layer.msg(gugerenzhengmabunengweikong, {icon: 2});
					return ;
				
				}if(!PassWord){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
				
			$.ajax({
				type : "post",
				url : _ctx + "/google/jcgoogle",
				data : {
					codes : accountGoogleWord,
					PassWord : md5.md5(PassWord)
				},
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

					}
				},
				
			});
			});
			
			$("#sendBtn,#sendBtn1").on("click",function(){
			
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
						if(data.success){debugger;
							layer.msg(fasongchenggong, {icon: 1})
							
							var time = 120;
							window.clearInterval(pageTimer["setagoogle"]);
							// 开启点击后定时数字显示
							pageTimer["setagoogle"] = window.setInterval(function() {
								time = time - 1;
								if (time == 0) {
								$("#sendBtn1").html(dianji);
								$("#sendBtn1").removeAttr("disabled");// 按钮可用
								$("#sendBtn1").html(chongxinfasong);// 按钮可用
									$("#sendBtn").html(dianji);
									$("#sendBtn").removeAttr("disabled");// 按钮可用
									$("#sendBtn").html(chongxinfasong);// 按钮可用
									window.clearInterval(pageTimer["setagoogle"]);
								} else {
									$("#sendBtn").html(time+miaochongxinfasong );
									$("#sendBtn1").html(time+miaochongxinfasong );
								}

							}, 1000);
							
						}else{
							$("#sendBtn").removeAttr("disabled");// 按钮可用
							$("#sendBtn1").removeAttr("disabled");// 按钮可用
							layer.msg(data.msg, {icon: 2})
						}
					}else{
						$("#sendBtn").removeAttr("disabled");// 按钮可用
						$("#sendBtn").removeAttr("disabled");// 按钮可用
						
						layer.msg(fasongshibai, {icon: 2})
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
				var oldPassWord = $("#oldPassWord").val();
				var newPassWord = $("#newPassWord").val();
				var reNewPassWord = $("#reNewPassWord").val();
				
				if(!oldPassWord){
					layer.msg(yuanshidengluisnull, {icon: 2});
					return ;
				}
				if(!newPassWord){
					layer.msg(xindenglumimaisnull, {icon: 2});
					return ;
				}
				if(newPassWord==oldPassWord){
					layer.msg(newoldisnot, {icon: 2});
					return ;		
				}
				if(!validate.check_password(newPassWord)){
					layer.msg(xinmimageshi, {icon: 2});
					return ;
				}
				if(!reNewPassWord){
					layer.msg(ercimimaisnull, {icon: 2});
					return ;
				}
				if(newPassWord!=reNewPassWord){
					layer.msg(liangcimima, {icon: 2});
					return ;
				
				}
				
	
				$(this).attr("disabled","disabled");
				$(this).html(yifasong);
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getPwSmsCode",
					data : {
						oldPassWord : md5.md5(oldPassWord),
						newPassWord : md5.md5(newPassWord),
						reNewPassWord : md5.md5(reNewPassWord)
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(fasongchenggong, {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["setagoogle"]);
								// 开启点击后定时数字显示
								pageTimer["setagoogle"]  = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html(dianji);
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										$("#sms_message").html(chongxinfasong);// 按钮可用
										window.clearInterval(pageTimer["setagoogle"] );
									} else {
										$("#sms_message").html(time+miaochongxinfasong );
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
	
			$("#submitBtn").on("click",function(){debugger;
				
				
				var googlekey = $(".text-con").val();
				var accountGoogleWord = $("#accountGoogleWord").val();
				var accountpwSmsCode =$("#accountpwSmsCode").val();
				if(!accountGoogleWord){
					layer.msg(gugerenzhengmabunengweikong, {icon: 2});
					return ;
				
				}
				
				//$("#submitBtn").attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/google/yzgoogle",
					data : {
						codes : accountGoogleWord,
						savedSecret:googlekey,
						accountpwSmsCode:accountpwSmsCode
					},
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

						}
					},
					
				});
				
			});
			
		},
		
		sendb :function(){
			var mobile=$(".mobile").val();
			$("#sendb").on("click",function(){
				$.ajax({
					type : "post",
					url : _ctx + "/google/sendgoogle",
					cache : false,
					dataType : "json",
					success : function(data) {debugger;
					$(".text-con").val(data);
						$('#qrcodeTable').html("");
						jQuery('#qrcodeTable').qrcode({
					         render    : "canvas",                
					         text :"otpauth://totp/hry:"+mobile+"?secret="+data,  
					         width : "180",               //二维码的宽度
			                 height : "180",
					     }); 
					},
					error : function(e) {
						alert(renzhengshibai)
					}
				});
			})
		},
		//发送短信
		sendsms :function(){
			$("#sendsmsBtn").on("click",function(){
				var accountPassWord = $("#accountPassWord").val();
				
				if(!accountGoogleWord){
					layer.msg(gugerenzhengmabunengweikong, {icon: 2});
					return ;
				}
				
			
				
	
				$(this).attr("disabled","disabled");
				$(this).html(yifasong);
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getGoogleKey",
					data : {
						//accountPassWord : md5.md5(accountPassWord),
						//reaccountPassWord : md5.md5(reaccountPassWord)
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(fasongchengong, {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["setagoogle"]);
								// 开启点击后定时数字显示
								pageTimer["setagoogle"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html(dianji);
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										$("#sms_message").html("");// 按钮可用
										window.clearInterval(pageTimer["setagoogle"]);
									} else {
										$("#sms_message").html(time+miaochongxinfasong );
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
	

});