define(function(require, exports, module) {
	
	this.md5 = require("js/base/utils/hrymd5")
	this.validate = require("js/base/validate");
    require("js/plugins/regIntlTelInput");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			if ( $("#pusername").val() == '' || $("#pusername").val() == undefined || $("#pusername").val() == null){
				
			}else{
				$("#referralCode2").attr("readonly",true);
			}
			
            $("#mobile").intlTelInput();

			$('#myTab li').on('click',function(){
				var cur=$(this).index();
				$(this).addClass('active').siblings().removeClass('active');
				$('.tab-content .tab-pane').hide().eq(cur).show()
			})
			try{
				window.clearInterval(regTimer);
			}catch(e){
			}
			
			var referralCode = $("#referralCode").val();
			if(referralCode!=null&&referralCode!=""){
				$('#referralCode').attr("disabled","disabled");
			}
			
			//注册提交
			$("#regBtn").on("click",function(){
				var email = $("#email").val();
				var password = $("#password").val();
				var registCode = $("#registCode").val();
				var check_deal = $("#check_deal").get(0).checked
				if(!email){
					layer.msg(youxiangbunengweikong, {icon: 2});
					return ;
				}
				if(!validate.check_email(email)){
					layer.msg(youxianggeshibuzhengque, {icon: 2});
					return ;
				}
				
				if(!password){
					layer.msg(mimabunengweikong, {icon: 2});
					return ;
				}
				if(!validate.check_password(password)){
					layer.msg(mimageshibuzhengque, {icon: 2});
					return ;
				}
                if($("#rePassword").val()!=password){
                    layer.msg(mimabuyiyang, {icon: 2});
                    return ;
                }
				if(!registCode){
					layer.msg(tuxingyanzhengmaweikong, {icon: 2});
					return ;
				}
				if(!check_deal){
					layer.msg(qingtongyixieyi, {icon: 2});
					return ;
				}
				
				$("#regBtn").attr("disabled","disabled");

				$.ajax({
					type : "post",
					url : _ctx + "/registService",
					data : {
						email : email,
						password : md5.md5(password),
						registCode : registCode,
						referralCode : $("#referralCode").val()
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(zhucechenggongqingjihuo, {icon: 1,time:1500},function(){
									window.location.href = _ctx+"/login";
								})
							}else{
								$("#regBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
								//刷新验证码
								$("#img_captcha").attr("src", _ctx + "/sms/registcode?t=" + new Date().getTime());
							}
						}else{
							layer.msg(zheceshibai, {icon: 2})
						}
					},
					error : function(e) {
					}
				});
				
			});
			
			//手机注册提交
			$("#regBtn2").on("click",function(){
				var mobile = $("#mobile2").val();
				var country = $(".dail-txt").html();
				var password2 = $("#password2").val();
				var registCode2 = $("#registCode2").val();
				var registSmsCode = $("#registSmsCode").val();
				var check_deal2 = $("#check_deal2").get(0).checked
				if(!mobile){
					layer.msg(shoujibuweikong, {icon: 2});
					return ;
				}		       
				if(!registSmsCode){
					layer.msg(duanxinyanzhengmabuweikong, {icon: 2});
					return ;
				}
				if(!password2){
					layer.msg(mimabunengweikong, {icon: 2});
					return ;
				}
				if(!validate.check_password(password2)){
					layer.msg(mimageshibuzhengque, {icon: 2});
					return ;
				}
				if($("#rePassword2").val()!=password2){
	                    layer.msg(mimabuyiyang, {icon: 2});
	                    return ;
	                }
				if(!registCode2){
					layer.msg(tuxingyanzhengmaweikong, {icon: 2});
					return ;
				}
				
				if(!check_deal2){
					layer.msg(qingtongyixieyi, {icon: 2});
					return ;
				}
				
				$("#regBtn2").attr("disabled","disabled");

				$.ajax({
					type : "post",
					url : _ctx + "/registService2",
					data : {
						mobile : mobile,
						password : md5.md5(password2),
						registCode : registCode2,
						registSmsCode : registSmsCode,
						country : country,
						referralCode : $("#referralCode2").val()
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(zhucechenggongqingdenglu, {icon: 1,time:1500},function(){
									window.location.href = _ctx+"/login";
								})
								
							}else{
								$("#regBtn2").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
								//刷新验证码
								$("#img_captcha2").attr("src", _ctx + "/sms/registcode?t=" + new Date().getTime());
							}
						}else{
							layer.msg(zheceshibai, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
				
			});
			
			
		},
		//刷新验证码
		refreshCode : function(){
			
			$("#img_captcha").on("click",function(){
				$(this).attr("src", _ctx + "/sms/registcode?t=" + new Date().getTime());
			})
			$("#img_captcha2").on("click",function(){
				$(this).attr("src", _ctx + "/sms/registcode?t=" + new Date().getTime());
			})
		},
		//发送短信
		sendsms :function(){
			$("#sendsmsBtn").on("click",function(){
				var username = $("#mobile2").val();
				var country = $(".dail-txt").html();
				var password = $("#password2").val();
				var registCode = $("#registCode2").val();
				if(!username){
					layer.msg(shoujibuweikong, {icon: 2});
					return ;
				}
				if(!validate.check_mobile(username)){
					layer.msg(shoujigeshibuzhengque, {icon: 2});
					return ;
				}
				
//				if(!password){
//					layer.msg(mimabunengweikong, {icon: 2});
//					return ;
//				}
//				if(!validate.check_password(password)){
//					layer.msg(mimageshibuzhengque, {icon: 2});
//					return ;
//				}
//				if(!registCode){
//					layer.msg(tuxingyanzhengmaweikong, {icon: 2});
//					return ;
//				}

				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/registSmsCode",
					data : {
						username : username,
						registCode : registCode,
						country : country
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg(fasongchenggong, {icon: 1})
								
								var time = 120;
								window.clearInterval(regTimer);
								// 开启点击后定时数字显示
								var regTimer = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html(fasongyanzhengma);
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(regTimer);
									} else {
										$("#sendsmsBtn").html(time+"s");
									}

								}, 1000);
								
							}else{
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2});
								$("#img_captcha2").attr("src", _ctx + "/sms/registcode?t=" + new Date().getTime());
							}
						}else{
							$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
							layer.msg(fasongshibai, {icon: 2})
							$("#img_captcha2").attr("src", _ctx + "/sms/registcode?t=" + new Date().getTime());
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		}
		

	}
});