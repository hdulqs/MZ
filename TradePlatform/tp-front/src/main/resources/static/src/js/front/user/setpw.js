define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	require("js/base/secondvail");
	this.firstvail = require("js/base/firstvail");


	module.exports = {
		//初始化方法
		init : function(){
			var oldPassWord = "";
			var newPassWord ="";
			var reNewPassWord ="";
			$(".verifyLayout").hide();
			$(".verifyLayout1").hide();
			$(".verifyLayout2").hide();
			$(".verifyLayout").hide();
			//清除定时器
			clearPageTimer()
			
			
			
			
			
			
			$("#submitBtn").on("click",function(){
				 oldPassWord = $("#oldPassWord").val();
				 newPassWord = $("#newPassWord").val();
				 reNewPassWord = $("#reNewPassWord").val();
				var pwSmsCode = $("#pwSmsCode").val();
				
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
				
				
				$("#submitBtn").attr("disabled","disabled");
				
				$.ajax({
					type : "post",
					url : _ctx + "/sencodvail",
					data : {
						oldPassWord : md5.md5(oldPassWord),
						newPassWord : md5.md5(newPassWord),
						reNewPassWord : md5.md5(reNewPassWord),
						type:"2"
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
									$('.dialog-close').on('click',function(){
										$(this).parent().parent().hide();
										$("#submitBtn").removeAttr("disabled");
										
									})
									
								}else if(google==0&&phone==1){
									$(".verifyLayout").show();
									$('.dialog-close').on('click',function(){
										$(this).parent().parent().hide()
										$("#submitBtn").removeAttr("disabled");

									})

								}else if(google==1&&phone==1){
									
									  $('#mobile-form').css('display','none');
									  $(".verifyLayout2").show();
									  $('.verify-form1').hide();
									  $('.dialog-close').on('click',function(){
											$(this).parent().parent().hide()
											$("#submitBtn").removeAttr("disabled");

										})
									  $('.btns').find('span').on('click',function(){
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
									firstvail.sendvail("setpw");

									//window.location.href = _ctx+"/user/center";
								}
								
								
							
							}else{
								layer.msg(data.msg, {icon: 2});
								$("#submitBtn").removeAttr("disabled");
							}
						}else{
							layer.msg("修改失败", {icon: 2})
							$("#submitBtn").removeAttr("disabled");
						}
					},
					error : function(e) {
						
					}
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
						if(data.success){
							layer.msg(fasongchenggong, {icon: 1})
							
							var time = 120;
							window.clearInterval(pageTimer["login"]);
							// 开启点击后定时数字显示
							pageTimer["login"] = window.setInterval(function() {
								time = time - 1;
								if (time == 0) {
								$("#sendBtn1").html(dianji);
								$("#sendBtn1").removeAttr("disabled");// 按钮可用
								$("#sendBtn1").html(chongxinfasong);// 按钮可用
									$("#sendBtn").html(dianji);
									$("#sendBtn").removeAttr("disabled");// 按钮可用
									$("#sendBtn").html(chongxinfasong);// 按钮可用
									window.clearInterval(pageTimer["setapw"]);
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
								window.clearInterval(pageTimer["setpw"]);
								// 开启点击后定时数字显示
								pageTimer["setpw"]  = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html(dianji);
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										$("#sms_message").html(chongxinfasong);// 按钮可用
										window.clearInterval(pageTimer["setpw"] );
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