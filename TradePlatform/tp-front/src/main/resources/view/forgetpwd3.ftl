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
			<form id="" action="" method="post" class="form-horizontal">
				<div class="stepimg p-t-40 p-b-40">
					<!--------第一步{{ ' C37 ' | translate }}step4.png 第二部{{ ' C37 ' | translate }}step5.png  第三部{{ ' C37 ' | translate }}step6.png----------->
					<!--<img class="img-responsive center-block" src="${ctx}/static/src/img/step6.png" />-->
				</div>
				<div class="find-form width-550 center-block find-formsuc">
					<i class="fa fa-check-circle fa-2x text-primary"></i> 恭喜您，您已成功找回登录密码！<span id="time"></span>s后跳转登录页
					<a href="${ctx}/login">立即跳转</a>
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
	 f.setInterval();
 });
</script>
