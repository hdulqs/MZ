define(function(require, exports, module) {
	
	this.md5 = require("js/base/utils/hrymd5")
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	this.base = require("js/base/base");
	require("js/base/secondvail");
	this.firstvail = require("js/base/firstvail");
	require("js/plugins/regIntlTelInput");
	
	module.exports = {
		
		//添加页面提交方法
		init : function(){
			$("#mobile").intlTelInput();
			
			clearPageTimer();
			$(".verifyLayout").hide();
			$(".verifyLayout1").hide();
			$(".verifyLayout2").hide();
			$(".verifyLayout").hide();
			
			
			
			$("#loginBtn").on("click",function(){
				
				var username = $("#username").val();
				var password = $("#password").val();
				if(!username){
					layer.msg(youxiangbunengweikong, {icon: 2});
					return ;
				}
				if(!password){
					layer.msg(mimabunengweikong, {icon: 2});
					return ;
				}
				
				
				$.ajax({
					type : "post",
					url : _ctx + "/sencodvail",
					data : {
						username : username,
						password : md5.md5(password),
						type:"1"
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								
								var phone =data.obj.checkStates;
								var google=data.obj.googleState;
								
								if(google==1&&phone==0){
									$(".verifyLayout1").show();
									$('.dialog-close').on('click',function(){
										$(this).parent().parent().hide()
									})
									
								}else if(google==0&&phone==1){
									$(".verifyLayout").show();
									$('.dialog-close').on('click',function(){
										$(this).parent().parent().hide()
									})

								}else if(google==1&&phone==1){
									
									$('#mobile-form').css('display','none');
									$(".verifyLayout2").show();
									 $('.verify-form1').hide();
									 $('.dialog-close').on('click',function(){
										 $(this).parent().parent().hide()
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
									firstvail.sendvail("login");
									//window.location.href = _ctx+"/user/center";
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
				});
				
			});
			
			
//			$(document).keyup(function(event){  
//                if(event.keyCode ==13){  
//                	$('#loginBtn').click();  
//                }  
//              }); 
			
			
			$("#loginBtn2").on("click",function(){
				var country = $(".dail-txt").html();
				var mobile = $("#mobile").val();
				var password2= $("#password2").val();
				if(!mobile){
					layer.msg(shoujibuweikong, {icon: 2});
					return ;
				}
				if(!password2){
					layer.msg(mimabunengweikong, {icon: 2});
					return ;
				}
				
				$.ajax({
					type : "post",
					url : _ctx + "/loginService",
					data : {
						mobile : mobile,
						country : country,
						password : md5.md5(password2)
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
									window.location.href = _ctx+"/user/center?tokenId="+data.obj;
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							layer.msg(data.msg, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
			
				
			});
			
			
//			$(document).keyup(function(event){  
//                if(event.keyCode ==13){  
//                	$('#loginBtn2').click();  
//                }  
//              }); 
			
			
			
			$("#sendBtn,#sendBtn1").on("click",function(){
			var username=	$("#username").val();
			$(this).attr("disabled","disabled");
			$(this).html(yifasong);
			$.ajax({
				type : "post",
				url : _ctx + "/sms/smsPhone",
				data:{username:username},
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
//								$("#sendBtn1").html("点击");
								$("#sendBtn1").removeAttr("disabled");// 按钮可用
								$("#sendBtn1").html(chongxinfasong);// 按钮可用
//								$("#sendBtn").html("点击");
								$("#sendBtn").removeAttr("disabled");// 按钮可用
								$("#sendBtn").html(chongxinfasong);// 按钮可用
//							    clearInterval(pageTimer["setapw"]);
							    clearInterval(pageTimer["login"]);
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
						$("#sendBtn1").removeAttr("disabled");// 按钮可用
						$("#sendBtn").removeAttr("disabled");// 按钮可用
						
						layer.msg(fasongshibai, {icon: 2})
					}
				},
				error : function(e) {
					
				}
			});
			
		});
			
			
			
		},
		loginTab:function(){
			$('#myTab li').on('click',function(){
				var that=$(this),
				    cur=that.index();
				that.addClass('active').siblings().removeClass('active');
				$('.login-conbox .login-con').hide().eq(cur).show();
			})
		}
	

	}
});