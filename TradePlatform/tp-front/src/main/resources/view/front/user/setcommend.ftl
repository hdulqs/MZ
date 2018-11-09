<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>推荐返佣</title>
    <#include "/base/base.ftl">
</head>

<body>
<div class="container-fluid person-con">
   <div>邀请人数 ${info.commendCount}</div>
   <div>邀请码 ${info.commendCode}</div>
   <div>邀请注册链接 ${info.commendLink}</div>
   <div> <div class="coss"></div></div>
   <div> <div class="cossno"></div></div>
   
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
seajs.use(["js/front/user/setcommend","js/i18n_base"],function(o){
	o.init();
});


</script>	