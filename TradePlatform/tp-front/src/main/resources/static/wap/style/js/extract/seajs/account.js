define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/layer/css/layer.css");
	require("style/js/layer/layer.min.js");
	require("style/js/mobile/js/zepto.js");
	require("style/js/mobile/js/public.js");
	
	module.exports = {
		init : function(){
			var isReal = 0;
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
				//实名认证
				$("#smrz").on("click",function(){
					if(isReal=='0'){
						window.open(basepath + "/html/user/roveath.html?tokenId="+tokenId,"_self");
					}else if(isReal=='1'){
						window.open(basepath + "/html/user/realinfo.html?tokenId="+tokenId,"_self");
					}
				})
				//修改登录密码
				$("#updlogin").on("click",function(){
					window.open(basepath + "/html/user/pwd.html?tokenId="+tokenId,"_self");
				})
				//修改交易密码
				$("#updtran").on("click",function(){
					window.open(basepath + "/html/user/pwd_tran.html?tokenId="+tokenId,"_self");
				})
				
				$.ajax({
					url: ctx_ + "/mobile/user/apppersondetail/isrealandpwd.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data!="" && data!=null){
							if(data.success){
								$("#username").text(data.obj.user.mobile);
								
								isReal = data.obj.user.isReal;
							}else{
								layer.msg(data.msg, {icon: 2});
								window.open(basepath + "/html/user/login.htm","_self");
							}
						}
					}
				});
				
				//退出
				$("#logout").on("click",function(){
					$.ajax({
					url: ctx_ + "/mobile/user/apppersondetail/logout.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data.success){
							window.open(basepath + "/html/coins.htm","_self");
						}else{
							layer.msg(data.msg, {icon: 2,time:1500},function(){
								window.open(basepath + "/html/user/login.htm","_self");
							});
						}
					}
				});
				})
			}
		}
	}
})