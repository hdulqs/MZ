
define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	require("js/base/googleauth/utf");
	require("js/base/googleauth/qrcode");
	require("js/base/googleauth/jquery.qrcode");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			//清除定时器
			clearPageTimer(); 
			google();
		
			
			function  google(){
				var mobile=$(".mobile").val();
				$.ajax({
					type : "post",
					url : _ctx + "/google/sendgoogle",
					cache : false,
					dataType : "json",
					/*success : function(data) {debugger;
					alert(data)
						$(".text-con").val(data);
					
						jQuery('#qrcodeTable').qrcode({
					         render    : "canvas",                
					         text :"otpauth://totp/Xstar:"+mobile+"?secret="+data,  
					         width : "180",               //二维码的宽度
			                 height : "180",
					     }); 
					},
					error : function(e) {
						alert(renzhengshibai)
					}*/
				success : function(data) {
					if(data.success){
						
						$(".text-con").val(data.obj.googleKey);
						//data.obj.company为app.properties里配置公司英文名称
						jQuery('#qrcodeTable').qrcode({
					         render    : "canvas",                
					         text :"otpauth://totp/"+data.obj.company+":"+data.obj.userName+"?secret="+data.obj.googleKey,  
					         width : "180",               //二维码的宽度
			                 height : "180",
					     });
						$(".mobile").val(data.obj.userName);
					}else{
						layer.msg(data.msg);

					}
				},
				eerror:function(e){
					alert(e)
				}
				});
				
				
			}
			
			
              $("#submitBtn1").on("click",function(){debugger
				var accountGoogleWord = $("#accountGoogleWord").val();
				var PassWord = $("#PassWord").val();
				if(!accountGoogleWord){
					layer.msg(gugerenzhengbuweikong, {icon: 2});
					return ;
				
				}if(!PassWord){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
				
				$("#submitBtn").attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/google/jcgoogle",
					data : {
						codes : accountGoogleWord,
						PassWord : PassWord
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
			
			
			
			$("#submitBtn").on("click",function(){debugger;
				
				
				var googlekey = $(".text-con").val();
				var accountGoogleWord = $("#accountGoogleWord").val();
				var accountpwSmsCode =$("#accountpwSmsCode").val();
				if(!accountGoogleWord){
					layer.msg(gugerenzhengbuweikong, {icon: 2});
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
					$(".text-con").val(data.obj.googleKey);
						$('#qrcodeTable').html("");
						jQuery('#qrcodeTable').qrcode({
					         render    : "canvas",                
					         text :"otpauth://totp/"+data.obj.company+":"+mobile+"?secret="+data.obj.googleKey,  
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
					layer.msg(gugerenzhengbuweikong, {icon: 2});
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
								layer.msg(fasongchenggong, {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["setgoogle"]);
								// 开启点击后定时数字显示
								pageTimer["setgoogle"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html(dianji);
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										$("#sms_message").html("");// 按钮可用
										window.clearInterval(pageTimer["setgoogle"]);
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