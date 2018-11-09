<!DOCTYPE html>
<html>
<head>
	<#include "/base/base.ftl">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!--<meta name="viewport" content="width=device-width, initial-scale=1">-->
    <meta name="description" content="">
    <meta name="author" content="">
    <@HryTopOrFooter url="base/title.ftl"/>
	<link rel="icon" type="image/x-icon"  />
	<!-- ================== BEGIN BASE CSS STYLE =============== -->
	<link href="${ctx}/static/${version}/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/font-awesome.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/animate.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/web-responsive.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/mdefault.css" id="theme" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/test.less" rel="stylesheet/less" type="text/css" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/index.css" rel="stylesheet"/>
	<!-- ================== END BASE CSS STYLE ================== -->
	
	<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/index.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/global.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />
	
</head>
<body>
<link href="static/style/web.min.css" rel="stylesheet" />
<!-- begin #page-container -->
<div id="page-container" >
	<!-- begin #header -->

	<!-- end #header -->
	<!-- begin #container -->
	<div class="container">
		<div class="fpcommon_box">
			<h1>找回密码</h1>
			<form  action="" method="post" id="form_forget2" class="form-horizontal">
				<input type="hidden" id="tel" value="${tel}"/>
				<div class="stepimg p-t-40 p-b-40">
					<!--------第一步{{ ' C37 ' | translate }}step4.png 第二部{{ ' C37 ' | translate }}step5.png  第三部{{ ' C37 ' | translate }}step6.png----------->
					<!--<img class="img-responsive center-block" src="${ctx}/static/src/img/step5.png" />-->
				</div>
				<div class="find-form width-500 center-block">
					<div class="form-group m-b-30">
						<label class="col-md-3 col-sm-3 control-label">新登录密码：</label>
						<div class="input-group col-md-9 col-sm-9">
							<div class="input-group-addon username">
								<span class="fa fa-lock f-s-18"></span>
							</div>
							<input class="form-control" id="passwd" name="password"  placeholder="请输入新的登录密码" type="password">

						</div>

						 <div class="text-warning col-md-offset-3 col-sm-offset-3">
							密码格式:(含英文字母+数字,不小于6位)
						</div>
					</div>
					<div class="form-group m-b-30">
						<label class="col-md-3 col-sm-3 control-label">确认密码：</label>
						<div class="input-group col-md-9 col-sm-9">
							<div class="input-group-addon password">
								<span class="fa fa-lock f-s-18"></span>
							</div>
							<input class="form-control" id="confirmpwd" placeholder="请再次输入" type="password">
						</div>

						<div class="text-warning col-md-offset-3 col-sm-offset-3">
							<!-- *错误信息 -->
						</div>

					</div>
					<div class="form-group m-b-30  p-t-30 p-b-30">
						<div class="input-group col-md-6 col-sm-6 col-md-offset-3 col-sm-offset-3">
							<button type="button" id="forgetPwdBtn2" class="btn btn-primary form-control" >提交</button>
						</div>
					</div>

				</div>
			</form>
		</div>
	</div>
	<!-- end #container -->

	<!-- begin #footer -->
	<!-- end #footer -->

</div>
<!-- end #page-container -->

</body>
</html>
<!-- end #page-container -->
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
 seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
      "jqueryForm": "lib/jqueryForm/jquery.form.js",
      <!-- layer -->
      "layer" : "lib/layer/layer.js",
      <!-- 自定义JS -->
      "base": "js/base/base.js"
    },
    preload: ['jquery','jqueryForm','layer'],
    map:[
		['.js','.js?v=${t}']//映射规则
	]
  });
 
 seajs.use(["js/main","js/forgetpwd1"],function(m,f){
	 m.init();
	 //登录页js
	 f.init();
 });
</script>
