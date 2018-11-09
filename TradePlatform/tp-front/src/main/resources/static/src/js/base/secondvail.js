define(function(require, exports, module) {
	this.firstvail = require("js/base/firstvail");

	module.exports = {
			mgvail : function(typ){
			$(".mobileb").on("click",function(){
				var username = $("#username").val();
				var verifyCode=$(".secondp").val();
				if(!verifyCode){
					layer.msg(shoujirenzhengbuweikong, {icon: 2});
					return ;
				
				}
				$.ajax({
					type : "post",
					url : _ctx + "/phone/PhoneAuth",
					data : {
						verifyCode:verifyCode,
						username:username
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								firstvail.sendvail(typ);
								/*layer.msg(data.msg, {icon: 1},function(){
									//window.location.href = _ctx+"/user/center";
								})
							*/
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
			})
			
			
			
			$(".googleVerifyb").on("click",function(){
				var username = $("#username").val();

				var verifyCode=$(".secondg").val();
				if(!verifyCode){
					layer.msg(gugerenzhengbuweikong, {icon: 2});
					return ;
				
				}
				$.ajax({
					type : "post",
					url : _ctx + "/google/googleAuth",
					data : {
						username : username,
						codes:verifyCode
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								firstvail.sendvail(typ);
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							layer.msg("登录失败", {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
			})
			
			
			
			
			$(".goog").on("click",function(){
				var username = $("#username").val();

				var verifyCode=$(".googlee").val();
				if(!verifyCode){
					layer.msg(gugerenzhengbuweikong, {icon: 2});
					return ;
				
				}
				$.ajax({
					type : "post",
					url : _ctx + "/google/googleAuth",
					data : {
						username : username,
						codes:verifyCode
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								firstvail.sendvail(typ);
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							layer.msg("登录失败", {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
			})
			
			
				$(".phone").on("click",function(){
				var username = $("#username").val();
				var verifyCode=$("#verifyCode").val();
				if(!verifyCode){
					layer.msg(shoujirenzhengbuweikong, {icon: 2});
					return ;
				
				}
				$.ajax({
					type : "post",
					url : _ctx + "/phone/PhoneAuth",
					data : {
						verifyCode:verifyCode,
						username:username
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								firstvail.sendvail(typ);
							
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
			})
	}
	}
});
