<#include "/base/base.ftl">
<style type="text/css">
    .container-drd{
        width: 100%;
        text-align: center;
        padding-bottom: 60px;
        position: relative;
        z-index: 1;
        margin-top: 80px;
    }
    .container-drd ul{
        display: inline-block;
    }
    .container-drd li{
        display: inline-block;
        float: left;
        text-align: center;
        border-right: 1px solid #fff;
        padding: 1px 14px;
        font-size: 16px;
        color: #fff;
    }
    .container-drd li a.active {
        color: #ff3c57 !important;
    }
    .container-drd li a:link {color: #fff;text-decoration:none;}
    .container-drd li a:visited {color:#fff;text-decoration:none;}
    .container-drd li a:active {color:#ff3c57;text-decoration:none;}
    .container-drd li a:hover {color:#ff3c57;text-decoration:none;}
</style>
<!-- begin container -->
<div class="container-drd">
    <input id="language" value="${locale}" type="hidden">
    <ul class="slide_lang">
        <li><a href="${ctx}/index">首页</a></li>
        <#if isOpenLanguage=='0'>
        <li class="slide_lang_box"><a class="zh_CN">简体中文</a></li>
        <li class="slide_lang_box"><a class="tn">Thailand</a></li>
        <li class="slide_lang_box"><a class="en">English</a></li>
        </#if>
    </ul>
</div>
<!-- end container -->