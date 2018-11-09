<#include "/base/base.ftl">
<style type="text/css">
    body{
        font-family: "MicrosoftYaHei";
    }
</style>
<!--<div class="funcbox">
    <div class="func-bar">
        <div class="notice fl">
            <img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/speaker.png" /img>
            蓝宝链（SPC）26号正式上线
        </div>

        <div class="lang-box fr">
            <#if locale == 'en'>
            <p class="curbox">
                <span class="lang-cur"><i class="langzn"></i>EN</span>
                <i class="iconfont icon-xiaosanjiao fl" style=""></i>
            </p>
            <ul class="lang-list" style="display:none;">
                <li><i class="langcn"></i>CN</li>
                <li><i class="langtn"></i>TN</li>
            </ul>

            <#elseif locale == 'tn'>
            <p class="curbox">
                <span class="lang-cur"><i class="langtn"></i>TN</span>
                <i class="iconfont icon-xiaosanjiao fl" style=""></i>
            </p>
            <ul class="lang-list" style="display:none;">
                <li><i class="langzn"></i>EN</li>
                <li><i class="langcn"></i>CN</li>
            </ul>

            <#else>
            <p class="curbox">
                <span class="lang-cur"><i class="langcn"></i>CN</span>
                <i class="iconfont icon-xiaosanjiao fl" style=""></i>
            </p>
            <ul class="lang-list" style="display:none;">
                <li><i class="langzn"></i>EN</li>
                <li><i class="langtn"></i>TN</li>
            </ul>
        </#if>
    </div>
    <div class="logreg fr">
        <#if user==null>
        <a href="${ctx}/login"><@spring.message code="Login"/></a>
        <i style="color: #fff;font-style: normal;">&frasl;</i>
        <a href="${ctx}/reg"><@spring.message code="register"/></a>
        <#else>
        <a href="${ctx}/user/center">${user.username}</a>
        <i style="color: #fff;font-style: normal;">&frasl;</i>
        <a href="${ctx}/logout"><@spring.message code="logout"/></a>
    </#if>
    </div>
        <div class="downloadApp fl">
            <img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/mobile.png" />
            手机APP下载
        </div>
    </div>
</div>-->

<div class="navbox clear">
        <div class="nav">
        <div class="logo" style="width:20%;text-align: right">
            <img src="${siteLogo!}" alt="" style="width: 187px;height: 55px;">
        </div>
        <UL class="nav-list">
            <li class="active"><a href="${ctx}"><@spring.message code="Index"/></a></li>
            <li><a href="${ctx}/market"><@spring.message code="Tradinghallv1"/></a></li>
            <#--<li><a href="${ctx}/market"><@spring.message code="gangganTrading"/></a></li>-->
            <#if hasc2c=="true">
	      	<li><a href="${ctx}/c2c"><@spring.message code="c2cIndex"/></a></li>
	     	</#if>
            <#if hasotc=="true">
	      	<li><a href="${ctx}/otc"><@spring.message code="otcIndex"/></a></li>
            </#if>
	        <li activeId="news"><a href="${cxt}/news/index/5.do?showColor=3&newsId=5&activeId=news"><@spring.message code="NewsInformation"/></a></li>
	      	<#if user!=null>
	     	<li><a href="${ctx}/user/center"><@spring.message code="personCenter"/></a></li>
	        <#else>
	      	<li><a href="${ctx}/login"><@spring.message code="personCenter"/></a></li>
	     	</#if>
	    </UL>
        <div class="nav-right-drd">
            <div class="lang-box fr">
                <#if locale == 'en'>
                <p class="curbox">
                    <span class="lang-cur"><i class="langzn"></i>EN</span>
                    <i class="iconfont icon-xiaosanjiao fl" style=""></i>
                </p>
                <ul class="lang-list" style="display:none;">
                    <li><i class="langcn"></i>CN</li>
                    <li><i class="langtn"></i>TN</li>
                </ul>

                <#elseif locale == 'tn'>
                <p class="curbox">
                    <span class="lang-cur"><i class="langtn"></i>TN</span>
                    <i class="iconfont icon-xiaosanjiao fl" style=""></i>
                </p>
                <ul class="lang-list" style="display:none;">
                    <li><i class="langzn"></i>EN</li>
                    <li><i class="langcn"></i>CN</li>
                </ul>

                <#else>
                <p class="curbox">
                    <span class="lang-cur"><i class="langcn"></i>CN</span>
                    <i class="iconfont icon-xiaosanjiao fl" style=""></i>
                </p>
                <ul class="lang-list" style="display:none;">
                    <li><i class="langzn"></i>EN</li>
                    <li><i class="langtn"></i>TN</li>
                </ul>
            </#if>
            </div>
            <div class="logreg fr">
                <#if user==null>
                <a href="${ctx}/login"><@spring.message code="Login"/></a>
                <i style="color: #fff;font-style: normal;">&frasl;</i>
                <a href="${ctx}/reg"><@spring.message code="register"/></a>
                <#else>
                <a href="${ctx}/user/center">${user.username}</a>
                <i style="color: #fff;font-style: normal;">&frasl;</i>
                <a href="${ctx}/logout"><@spring.message code="logout"/></a>
            </#if>
            </div>
        </div>
    </div>
</div>
