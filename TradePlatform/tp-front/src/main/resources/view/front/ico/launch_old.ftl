<!DOCTYPE html>
<html lang="zh-CN">
<head>
    
    <title>ICO项目发布</title>
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="renderer" content="webkit">
    <meta name="sogou_site_verification" content="C8OJXgS1E7"/>
    <#include "/base/base.ftl">
</head>

<body>
   
  <nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
      	<li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">版本<span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#">中文版</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">English</a></li>
          </ul>
        </li>
        <#if icoProjectDTO??>
        	<li class="step ${(icoProjectDTO.step==0)?string('active','')}"><a href="#">完善个人信息 </a></li>
        	<li class="step ${(icoProjectDTO.step==1)?string('active','')}"><a href="#">项目基本信息</a></li>
        	<li class="step ${(icoProjectDTO.step==2)?string('active','')}"><a href="#">项目详细介绍</a></li>
        	<li class="step ${(icoProjectDTO.step==3)?string('active','')}"><a href="#">设置投资回报</a></li>
        	<li class="step ${(icoProjectDTO.step==4)?string('active','')}"><a href="#">提交审核</a></li>
		<#else>
			<li class="step active"><a href="#">完善个人信息</a></li>
        	<li class="step"><a href="#">项目基本信息</a></li>
        	<li class="step"><a href="#">项目详细介绍</a></li>
        	<li class="step"><a href="#">设置投资回报</a></li>
        	<li class="step"><a href="#">提交审核</a></li>
		</#if>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="col-sm-12" id="projdct_step">
</div>

<div class="btn-group col-sm-8 media" role="group" aria-label="...">
	<button type="button" id="project_save" class="btn btn-default">保存</button>
	<button type="button" id="project_save_next" class="btn btn-default">保存并下一步</button>
</div>

</body>
</html>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js" ></script>
<script type="text/javascript" >
//window作用域
//项目状态
var project_step=${icoProjectDTO.step!'0'};
//项目id
var project_id=${icoProjectDTO.id!'-1'};

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
		['.js','.js?v='+new Date().getTime()]//映射规则
	]
  });
 
 seajs.use(["js/main","js/front/ico/launch"],function(m,launch){
	 m.init();
	 //项目
	 launch.init();
 });
</script>