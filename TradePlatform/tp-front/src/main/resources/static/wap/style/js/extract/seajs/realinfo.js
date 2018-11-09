define(function(require, exports, module){
	require("style/css/bootstrap/css/bootstrap.min.css");
	require("style/css/mobile/css/css.css");
	require("style/css/mobile/css/roveath.css");
	require("style/js/layer/css/layer.css");
	require("style/js/layer/layer.min.js");
	require("style/js/mobile/js/zepto.js");
	require("style/js/index.js");
	require("style/js/mobile/js/public.js");
	
	module.exports = {
		init : function(){
			if(tokenId!=""){
				//跳转到首页
				$("#bbjy").on("click",function(){
					window.open(basepath + "/html/coins.htm?tokenId="+tokenId,"_self");
				})
				//我的账户
				$("#wdzh").on("click",function(){
					window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
				})
				//设置
				$("#sz").on("click",function(){
					window.open(basepath + "/html/user/account.html?tokenId="+tokenId,"_self");
				})
			
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
									window.open(basepath + "/html/user/roveath.html?tokenId="+tokenId,"_self");
								}else if(data.obj.user.isReal=='1'){
									$("#name").text(data.obj.user.truename);
									$("#card").text(data.obj.user.cardcode);
								}
							}else{
								layer.msg(data.msg, {icon: 2});
								window.open(basepath + "/html/user/login.htm","_self");
							}
						}
					}
				});
			}
		}
	}
})