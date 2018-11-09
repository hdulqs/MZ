<!DOCTYPE html>
<html  lang="en">
<head>
<#include "/base/base.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="" name="description">
<meta content="" name="author">
<@HryTopOrFooter url="base/title.ftl"/>
<!-- ================== BEGIN BASE CSS STYLE =============== -->
<link href="${ctx}/static/${version}/lib/exstatic/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/static/lib/bootstrap-select/bootstrap-select.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/animate.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/web-responsive.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/mdefault.css" id="theme" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/test.less" rel="stylesheet/less" type="text/css">
<link href="${ctx}/static/${version}/lib/exstatic/css/style.min.css" rel="stylesheet" />


</head>
<body >
	<!-- topbar -->
	<div id="page-container" class="container fade page-header-fixed ng-scope in">
		<!-- 头部导航 -->
		<#if hasico!>
			<!--头部导航 -->
			<#include "/base/user/ico_header.ftl">
			<!-- 左侧菜单 -->
			<#include "/base/user/ico_left.ftl">		
		<#else>
			<!--头部导航 -->
			<#include "/base/user/header.ftl">
			<!-- 左侧菜单 -->
			<#include "/base/user/left.ftl">
		</#if>
		<input type="hidden" name="tokenId" id="tokenId" value="${tokenId}"/>
		<!-- 中间切换区域 -->
		<div id="content" class="content col-md-10 col-sm-10"  style="margin: 0px; min-height: 800px; padding: 0;">
		</div>
		<!-- 底部锚点 -->
		<a href="javascript:void(0);" class="btn btn-icon btn-circle btn-success btn-scroll-to-top fade" data-click="scroll-top"><i class="fa fa-angle-up"></i></a>
	</div>
	<div class="page-footer col-md-12 col-sm-12 ng-scope"  >
			<@HryTopOrFooter url="base/footer.ftl"/>
	</div>
</body>
</html>
	
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
 seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
      <!-- 基础框架JS -->
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
 
 seajs.use(["js/front/main_center","js/i18n_base"],function(m){
	 m.init();
 });
</script>
