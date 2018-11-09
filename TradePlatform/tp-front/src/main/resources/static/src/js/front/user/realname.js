define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			
			$("#realnameBtn").on("click",function(){
				var trueName = $("#trueName").val();
				var country = $("#country").val();
				var cardType = $("#cardType").val();
				var cardId = $("#cardId").val();
				if(!trueName){
					layer.msg('姓名不能为空', {icon: 2});
					return ;
				}else{
					var reg = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
					if(!reg.test(trueName)){
						layer.msg('姓名必须为中文', {icon: 2});
						return ;
					}
				}
				if(!cardId){
					layer.msg('证件号不能为空', {icon: 2});
					return ;
				}
				if(!validate.check_card(cardId)){
					layer.msg('证件号格式不正确', {icon: 2});
					return ;
				}
				
				$.ajax({
					type : "post",
					url : _ctx + "/user/realname",
					data : {
						trueName : trueName,
						country : country,
						cardType : cardType,
						cardId : cardId
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("提交成功，请等待审核！", {icon: 1})
								//跳转到认证信息页面
								loadUrl(_ctx+"/user/realinfo")
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							layer.msg("认证失败", {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
				
			});
			
		}

	}
	

});