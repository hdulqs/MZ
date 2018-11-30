<#include "/base/base.ftl">

<div id="header" class="header navbar navbar-default navbar-fixed-top">
	<div class="container" style="">
		<div class="navbar-header ">
			<style >
				.dropdown-menu {
					padding: 0;
					margin: 0;
				}

				.navbar-brand .open .dropdown-menu {
					display: inline-flex;
				}

				#header .container,#page-container{
					margin: 0 auto !important;
				}

			</style>


<!--=logo-->
			<div>
			<@HryTopOrFooter url="base/logo.ftl"/>
			</div>
           <!-- <div class="navbar-brand ng-scope">
				<span class="brand-logo">
					<a href="/">
					  	<h1 class="brand-logo" style="height:auto; margin-top:12px;">


					  		<img id="logo_img" src="/${siteLogo!}" style="vertical-align:top;height:33px;"/>
					    </h1>
			 		 </a>
					</span>
			</div>-->
<!--=logo---->

			<button type="button" class="navbar-toggle collapsed ng-scope" data-toggle="collapse" data-target="#header-navbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
		</div>

		<div class="collapse navbar-collapse" id="header-navbar">

			<!--<ul class="nav navbar-nav navbar-right navbar-login">

				<li class="dropdown navbar-user">
				   <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" style="padding: 15px 0;">
				   <span class="hidden-xs ng-binding" style="color: #1a85e3; background: #f0f3f5; padding-left: 10px; font-size: 12px; font-weight: 600; display: inline-block; height: 35px; line-height: 1px;"> 您好, ${user.username} <span style="border-left: 1px solid #e6e4e5; display: inline-block;"> <i style="margin: 10px;" class="fa fa-angle-down"></i>
						</span>
					</span>
				</a>
					<ul class="dropdown-menu animated fadeInLeft" style="min-width: 152px !important;">
						<li class="arrow"></li>

						<li><a href="javascript:void(0);" onclick="loadUrl('${ctx}/v.do?u=front/user/safe')"><@spring.message code="shezhi"/></a></li>
						<li class="divider"></li>
						<li><a href="${ctx}/logout" ><@spring.message code="logout"/></a></li>
					</ul>
				</li>

			</ul>-->
			<input id="language" value="${locale}" type="hidden">
			<#if isOpenLanguage=='0'>
			 <div class="foot_lang" style="margin-top:23px;">
                <dl id="slide_lang"  class="">
                   <dt>
                     <span><i class="icon_lang icon_lang_zh_cn"></i></span><i class="icon_gray_arrows"></i>
                   </dt>
                   <dd id="slide_lang_box">
                    	<a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i></a>
                  	    <a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i></a>
                  	  <#if locale == 'zh_CN'>
                  	  	<a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i></a>
                  	  <#elseif locale == 'en'>
                  	    <a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i></a>
                  	  <#elseif locale == 'tn'>
                  	    <a href="javascript:void(0);" class=""><i class="icon_lang icon_lang_en"></i></a>
                  	  </#if>
                   </dd>
                </dl>
            </div>
            </#if>
			<ul class="navbar-right">
                  	<#if user==null>
                  	<li class="clearfix" style="margin-top:23px; line-height:20px;">
                  		<i><img src="${ctx}/static/${version}/lib/exstatic/img/avator2.png" width="20" alt=""></i>
						<a class="text-bold text-primary no-underline"  href="${ctx}/login"><@spring.message code="Login"/></a>
						<span class="text-gray">or</span>
						<a class="text-bold text-primary  no-underline" href="${ctx}/reg"><@spring.message code="register"/></a>
					</li>

					<#else>
					<li user-Islogin class="dropdown clearfix" style="margin-top:23px; line-height:20px;">
					  <i><img src="${ctx}/static/${version}/lib/exstatic/img/avator2.png" width="20" alt=""></i>
					  <a href="${ctx}/user/center"  >${user.username}<!--<b class="caret"></b>--></a>
					  <a href="${ctx}/logout" style="margin-left:10px;"><@spring.message code="logout"/></a>
						<#--<ul class="dropdown-menu animated fadeInLeft">
							<li><a href="${ctx}/user/center"><@spring.message code="personCenter"/></a></li>
							<li><a href="${ctx}/logout"><@spring.message code="logout"/></a></li>
						</ul>-->
					</li>

					</#if>

                  </ul>

			<!-- ngInclude: 'static/views/common/layout/nav.html' -->
			<ul class="nav navbar-nav navbar-left ng-scope" ng-include="&#39;static/views/common/layout/nav.html&#39;">
				<li class="dropdown  ng-scope" ui-sref-active="active" <#if showColor=='1'>class="active"</#if>><a href="${ctx}<#if tokenId??>/index.do?tokenId=${tokenId}<#else>/index.do?showColor=1</#if>"><@spring.message code="Index"/></a></li>
				<!-- 行情中心隐藏 -->
				<li ui-sref-active="active" ><a target="_blank" href="${ctx}/market<#if tokenId??>.do?tokenId=${tokenId}</#if>" ><@spring.message code="Tradinghall"/></a></li>
				<li ui-sref-active="active" <#if showColor=='6'>class="active"</#if> ><a   href="${ctx}/c2c<#if tokenId??>?tokenId=${tokenId}</#if>" ><@spring.message code="c2cIndex"/></a></li>
				<#if hasotc=="true">
				<li ui-sref-active="active" <#if showColor=='7'>class="active"</#if> ><a   href="${ctx}/otc<#if tokenId??>?tokenId=${tokenId}</#if>" ><@spring.message code="otcIndex"/></a></li>
				</#if>
				<li ui-sref-active="active" <#if showColor=='3'>class="active"</#if>><a href="${cxt}/news/index/5<#if tokenId??>.do?tokenId=${tokenId}&showColor=3<#else>.do?showColor=3</#if>"  ><@spring.message code="NewsInformation"/></a></li>
				<li ui-sref-active="active" <#if showColor=='4'>class="active"</#if>><a href="${cxt}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>37<#else>68</#if>&tokenId=${tokenId}&showColor=4<#else>.do?categoryId=<#if locale == 'zh_CN'>37<#else>68</#if>&showColor=4</#if>" ><@spring.message code="Helpcenter"/></a></li>
				<li ui-sref-active="active" <#if showColor=='5'>class="active"</#if>><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>84<#else>72</#if>&tokenId=${tokenId}&showColor=5<#else>.do?categoryId=<#if locale == 'zh_CN'>84<#else>85</#if>&showColor=183</#if>"><@spring.message code="guanyuwomen"/></a></li>
				<!--
				<li ui-sref-active="active" ><a   href="http://www.200.com/#/guide">浏览项目（ico）</a></li>
				<li ui-sref-active="active" ><a   href="#" onclick="isReal2Url('/ico/launch')">发起项目（ico）</a></li>
				<li ui-sref-active="active" ><a   href="http://www.200.com/#/guide">ICO日历（ico）</a></li>
				<li ui-sref-active="active" ><a   href="http://www.200.com/#/guide">ICO新闻（ico）</a></li>-->
			</ul>
		</div>


	</div>
</div>
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
