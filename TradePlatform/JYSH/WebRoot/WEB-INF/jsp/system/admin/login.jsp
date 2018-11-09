<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>微笑国际合作方管理后台</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

 
<link rel="stylesheet" href="static/login/css/login.css?fqdddddq" /> 
<link href="static/login/font-awesome.css" rel="stylesheet" />
<script type="text/javascript" src="static/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="static/js/layer/layer.js"></script>
 
<style type="text/css">
	.layui-layer {
		left: 50% !important;
		top: 314px !important;
		margin-left: -130px;
	}
</style>
</head>

 
	 
	<body>
		<div class="login">
			<form action="" class="formLogin" method="post" id="login_form" >
				<div class="login_head">
					<h3>欢迎登录</h3>
					<!--<a href="enroll.htm">免费注册</a>-->
				</div>
				<div class="loginList loginListUser">
					<label></label>
					<input type="text" class="loginText" name="loginname" id="loginname" value="" placeholder="账号" maxlength="30">
					<span class="errorTips"><i></i><em></em></span>
				</div>
				<div class="loginList loginListPwd">
					<label></label>
					<input type="password" class="loginText" name="password"  id="password" value="" placeholder="请输入您的密码" maxlength="20">
					<span class="errorTips"><i></i><em></em></span>
				</div>
				<div class="loginList2">
					<label></label>
					<input type="text" name="code" id="code" value="" placeholder="输入右侧验证码" maxlength="4" />
					<i>
						<img style="height:22px;" id="codeImg" alt="点击更换"
								title="点击更换" src="" />
					</i>
				</div>
	  

				<div class="login_btn">
					<!--<a class="forget_password" href="password.htm">忘记密码?</a>-->
					<input class="loginBtn" type="button" id="loginBtn" value="登录" onclick="severCheck();">
				</div>
				<!--<input class="loginBtn" type="button" id="loginBtn" value="测试" onclick="test()">-->
			</form>
		</div>
	</body>
</html>

 

	<script type="text/javascript">
	
	//回车提交表单
	var EnterSubmit = function(evt){
		evt = window.event || evt;
		 if (evt.keyCode == 13)
		 { 
			 severCheck();
		 }
		}

	window.document.onkeydown=EnterSubmit;
	
	
	//服务器校验
	function dolongin(pass){
			 
		
			$.ajax({
				type: "POST",
				url: 'login_login2',
		    	data: {smsCode:pass},
				dataType:'json',
				cache: false,
				success: function(data){
					if("success" == data.result){
						 
						window.location.href="main/index";
					 
					}else{
						layer.msg('验证码错误');
						changeCode();
					}
				}
			});
		 
	}
 

	
		//服务器校验
		function severCheck(){
			if(check()){
				
				var loginname = $("#loginname").val();
				var password = $("#password").val();
				var code = "qq313596790fh"+loginname+",fh,"+password+"QQ978336446fh"+",fh,"+$("#code").val();
				$.ajax({
					type: "POST",
					url: 'login_login',
			    	data: {KEYDATA:code,tm:new Date().getTime()},
					dataType:'json',
					cache: false,
					success: function(data){
						if("success" == data.result){
							saveCookie();
							//window.location.href="main/index";
							//prompt层
							layer.prompt({title: '请输入手机短信', formType: 1}, function(pass, index){
							  layer.close(index); 
							 
							  if(pass=='') return ;
							  
							  dolongin(pass);
							});
							
							
							
							
						}else if("usererror" == data.result){
							$("#loginname").tips({
								side : 1,
								msg : "用户名或密码有误",
								bg : '#FF5080',
								time : 15
							});
							$("#loginname").focus();
						}else if("codeerror" == data.result){
							$("#code").tips({
								side : 1,
								msg : "验证码输入有误",
								bg : '#FF5080',
								time : 15
							});
							$("#code").focus();
							changeCode();
						}else{
							$("#loginname").tips({
								side : 1,
								msg : "缺少参数",
								bg : '#FF5080',
								time : 15
							});
							$("#loginname").focus();
						}
					}
				});
			}
		}
	
		$(document).ready(function() {
			changeCode();
			$("#codeImg").bind("click", changeCode);
		});

		$(document).keyup(function(event) {
			if (event.keyCode == 13) {
				$("#to-recover").trigger("click");
			}
		});

		function genTimestamp() {
			var time = new Date();
			return time.getTime();
		}

		function changeCode() {
			$("#codeImg").attr("src", "code.do?t=" + genTimestamp());
		}

		

		//客户端校验
		function check() {

			if ($("#loginname").val() == "") {

				$("#loginname").tips({
					side : 2,
					msg : '用户名不得为空',
					bg : '#AE81FF',
					time : 3
				});

				$("#loginname").focus();
				return false;
			} else {
				$("#loginname").val(jQuery.trim($('#loginname').val()));
			}

			if ($("#password").val() == "") {

				$("#password").tips({
					side : 2,
					msg : '密码不得为空',
					bg : '#AE81FF',
					time : 3
				});

				$("#password").focus();
				return false;
			}
			if ($("#code").val() == "") {

				$("#code").tips({
					side : 1,
					msg : '验证码不得为空',
					bg : '#AE81FF',
					time : 3
				});

				$("#code").focus();
				return false;
			}

			$("#loginbox").tips({
				side : 1,
				msg : '正在登录 , 请稍后 ...',
				bg : '#68B500',
				time : 10
			});

			return true;
		}

		function savePaw() {
			if (!$("#saveid").attr("checked")) {
				$.cookie('loginname', '', {
					expires : -1
				});
				$.cookie('password', '', {
					expires : -1
				});
				$("#loginname").val('');
				$("#password").val('');
			}
		}
		

		function saveCookie() {
			if ($("#saveid").attr("checked")) {
				$.cookie('loginname', $("#loginname").val(), {
					expires : 7
				});
				$.cookie('password', $("#password").val(), {
					expires : 7
				});
			}
		}
		function quxiao() {
			$("#loginname").val('');
			$("#password").val('');
		}
		
		jQuery(function() {
			var loginname = $.cookie('loginname');
			var password = $.cookie('password');
			if (typeof(loginname) != "undefined"
					&& typeof(password) != "undefined") {
				$("#loginname").val(loginname);
				$("#password").val(password);
				$("#saveid").attr("checked", true);
				$("#code").focus();
			}
		});
	</script>
	<script>
		//TOCMAT重启之后 点击左侧列表跳转登录首页 
		if (window != top) {
			top.location.href = location.href;
		}
	</script>

 	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/jquery-1.7.2.js"></script>
	<script src="static/login/js/jquery.easing.1.3.js"></script>
	<script src="static/login/js/jquery.mobile.customized.min.js"></script>
	<script src="static/login/js/camera.min.js"></script>
	<script src="static/login/js/templatemo_script.js"></script>
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
</body>

</html>