<!DOCTYPE html>
<html  lang="en">
<head>
<#include "/base/base.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="" name="description">
<meta content="" name="author">
<@HryTopOrFooter url="base/title.ftl"/>
<!-- ================== BEGIN BASE CSS STYLE =============== -->
<link href="${ctx}/static/${version}/lib/exstatic/css/style.min.css" rel="stylesheet" />


</head>
<body >
	<div id="content" class="content container-fluid verifyLayout"  style="margin: 0px;">
        <div class="main white">
            <div class="page-header">
                <h1 class="f-s-18 messageBox-con"><img src="${ctx}/static/${version}/lib/exstatic/img/done.png">${message}</h1>
            </div>
            <a href="${ctx}/login"><@spring.message code="dianjidenglu"/></a>
        </div>
	</div>
</body>
</html>
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
 
 seajs.use("js/front/main_news",function(m){
	 m.init();
 });
</script>
