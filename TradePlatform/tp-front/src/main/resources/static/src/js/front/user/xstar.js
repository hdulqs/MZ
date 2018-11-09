define(function(require, exports, module) {
	this.validate = require("js/base/validate");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			
			$("#uploadSubmit").click(function () {debugger;
	            var data = new FormData($("#uploadForm")[0]);
	            $.ajax({
	                url: _ctx + "/user/upload",
	                type: "post",
	                data: data,
	                async: false,
	                cache: false,
	                contentType: false,
	                processData: false,
	                success: function (data) {
	                    console.log(data);
	                    if(data.status){
	                        console.log("upload success");
	                    }else{
	                        console.log(data.message);
	                    }
	                },
	                error: function (data) {
	                    console.log(data.status);
	                }
	            });
	        });
			
			$("#realnameBtn").on("click",function(){debugger;
				var data = new FormData($("#uploadForm")[0]);
				var trueName = $("#trueName").val();
				var sex = $("#sex").val();
				var surname = $("#surname").val();
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
				if(!sex){
					layer.msg('姓別不能为空', {icon: 2});
					return ;
				}
				if(!surname){
					layer.msg('姓氏不能为空', {icon: 2});
					return ;
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
					url : _ctx + "/user/xstar",
					data : {
						trueName : trueName,
						sex : sex,
						surname : surname,
						country : country,
						cardType : cardType,
						cardId : cardId
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("认证成功", {icon: 1})
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