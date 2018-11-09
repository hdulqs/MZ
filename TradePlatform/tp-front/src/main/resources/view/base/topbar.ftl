<#include "/base/base.ftl">
<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css" rel="stylesheet" />
<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />
<!--begin nav-->
<nav class="navbar navbar-inverse" style="z-index: 1031;height: 30px;margin-bottom: 0;top:0;border-radius: 0;min-height:30px;position:fixed; width:100%;" >
    <div class="container" style="margin-top: -10px;">
        <div class="navbar-header">
            <ul class="nav navbar-nav collapse navbar-collapse">
                <li><a class="" href="#">客户电话：${servicePhone!}</a></li>
                <li><a class="" href="#">服务时间:${workHours!}</a></li>
            </ul>
        </div>
          
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
             <!--<#if i18n=='true'>
                <div class="language">
					<a href="${ctx}/language.do?language=zh_CN"  class="btn btn-blue"><@spring.message code="Chinese"/></a>
				    <a href="${ctx}/language.do?language=en" class="btn btn-blue"><@spring.message code="English"/></a>
				 </div>
			</#if>-->
			<input id="language" value="${locale}" type="hidden"> 
			<#if isOpenLanguage=='0'>
			 <div class="foot_lang" style="margin-top:11px;">
                <dl id="slide_lang"  class="">
                   <dt>
                     <span><i class="icon_lang icon_lang_zh_cn"></i>简体中文</span><i class="icon_gray_arrows"></i>
                   </dt>
                   <dd id="slide_lang_box">
                  	  <#if locale == 'zh_CN'>
                  	  	<a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i>English</a>
                  	  <#elseif locale == 'en'>
                  	    <a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i>简体中文</a>
                  	    <#else>
                  	    <a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i>ไทย</a>
                  	  </#if>
                   </dd>
                </dl>
            </div>
            </#if>
			  <ul class="navbar-right">
                  	<#if user==null>
                  	<li class="hidden-md hidden-sm clearfix" style="margin-top:13px; line-height:20px;">
                  		<i><img src="${ctx}/static/${version}/lib/exstatic/img/avator.png" width="20" alt=""></i>
						<a class="text-bold text-white no-underline" href="${ctx}/login"><@spring.message code="Login"/></a>
						<span class="text-gray" style="color:#fff;">or</span>
						<a class="text-bold text-white no-underline" href="${ctx}/reg"><@spring.message code="register"/></a>
					</li>
					
					<#else>                   
					<li user-Islogin class="dropdown hidden-md hidden-sm clearfix" style="margin-top:13px; line-height:20px;">
					  <i><img src="${ctx}/static/${version}/lib/exstatic/img/avator.png" width="20" alt=""></i>
					  <a href="${ctx}/user/center<#if tokenId??>.do?tokenId=${tokenId}</#if>" style="color:#fff;" >${user.username}<!--<b class="caret"></b>--></a>
					  <a href="${ctx}/logout<#if tokenId??>.do?tokenId=${tokenId}</#if>" style="margin-left:10px;"><@spring.message code="logout"/></a>
						<#--<ul class="dropdown-menu animated fadeInLeft">
							<li><a href="${ctx}/user/center"><@spring.message code="personCenter"/></a></li>
							<li><a href="${ctx}/logout"><@spring.message code="logout"/></a></li>
						</ul>-->
					</li>
					
					</#if>
                  </ul>
	        <#--<ul class="nav navbar-nav navbar-right">
	        	<li style="margin-top: 15px;color: #9d9d9d;"><span>客服QQ : </span></li>
	        	<li><a style="padding:12px 0 0 0;" target="_blank" href="https://wpa.qq.com/msgrd?v=3&uin=${serviceQQ!}&site=qq&menu=yes"><img border="0" style="margin: 0 5px 0 10px;" src="https://pub.idqqimg.com/qconn/wpa/button/button_121.gif" alt="点击这里给我发消息" title="点击这里给我发消息"/>${serviceQQ!}</a></li>
	        	<#if hasico>
	        		<li style="margin-top: 15px;color: #9d9d9d;"><span><a href="/ico2exchange">&nbsp;&nbsp;ICO-交易所</a></span></li>
	        	</#if>
	        </ul>-->
	        
        </div>
    </div>
</nav>
<!--end nav-->
<#include "/base/base.ftl">
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
      "superslide": "lib/exstatic/js/jquery.SuperSlide2.js",
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
 
seajs.use(["js/base/topbar"],function(t){
	 t.language();
});
</script>