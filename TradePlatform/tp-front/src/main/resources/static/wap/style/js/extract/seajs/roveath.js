define(function(require, exports, module){
	require("style/css/bootstrap/css/bootstrap.min.css");
	require("style/css/mobile/css/css.css");
	require("style/css/mobile/css/roveath.css");
	require("style/js/layer/css/layer.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/index.js");
	require("style/js/mobile/js/public.js");
	this.validate = require("style/js/validate");
	
	module.exports = {
		init : function(){
			
			if(tokenId!=""){
				//是否已实名
				$.ajax({
					url: ctx_ + "/mobile/user/apppersondetail/isrealandpwd.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data!="" && data!=null){debugger
							if(data.success){
								if(data.obj.user.isReal=='0'){
									
								}else if(data.obj.user.isReal=='1'){
									$("#name").text(data.obj.user.truename);
									$("#card").text(data.obj.user.cardcode);
									window.open(basepath + "/html/user/realinfo.html?tokenId="+tokenId,"_self");
								}
							}else{
								layer.msg(data.msg, {icon: 2});
								window.open(basepath + "/html/user/login.htm","_self");
							}
						}
					}
				});
				
				$("#sub").on("click",function(){
					debugger;
					var trueName = $("#trueName").val();
					var surName = $("#surName").val();
					var country = $("#country").val();
					var cardType = $("#cardType").val();
					var cardId = $("#cardId").val();
					
					if(!trueName){
						layer.msg('真实姓名不能为空', {icon: 2});
						return ;
					}
					if(!validate.check_card(cardId)){
						layer.msg('身份证号不正确', {icon: 2});
						return ;
					}
					
					$.ajax({
						url: ctx_ + "/mobile/user/apppersondetail/apprealname.do",
						type:"post",
						data : {tokenId:tokenId,surName:surName,trueName:trueName,country:country,cardType:cardType,cardId:cardId},
						dataType:'json',
						success:function(data){
							if(data!="" && data!=null){
								if(data.success){
									layer.msg(data.msg, {icon: 2,time:1500},function(){
										window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
									});
								}else{
									layer.msg(data.msg, {icon: 2});
								}
							}
						}
					})
				})
			}
		}
	}
})