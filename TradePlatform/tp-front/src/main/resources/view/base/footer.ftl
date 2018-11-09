<#include "/base/base.ftl">
<style type="text/css">
    .contain-drd{
        width: 100%;
        background: #313131;
        color: #a6a6a6;
    }
    .contain-drd .box{
        width: 70%;
        height: auto;
        margin: 0 auto;
        padding-top: 16px;
    }
    .contain-drd .box>div{
        width: 100%;
        font-size: 12px;
        color: #a6a6a6;
    }
    .contain-drd .box>div.box-title{
        font-size: 14px;
        color: #fff;
        padding-bottom: 6px;
    }
    .contact .contact-item{
        padding-top: 10px;
    }
    .contact .contact-item img{
        display: block;
        float: left;
        width: 16px;
        height: 16px;
        margin-right: 10px;
    }
    .contact .contact-item p{
        padding-left: 25px;
    }
    .fxtip .fxtip-item{

    }
    .fxtip h3{
        color: #fff;
        font-size: 14px !important;
    }
    .fxtip p{
        font-size: 12px !important;
    }
    .copyright{
        width: 100%;
        color: #a6a6a6;
        font-size: 12px;
        padding-bottom: 16px;
        margin: 16px auto 0;
    }
</style>

<div class="contain-drd">
    <div class="box">
        <div class="box-title">联系我们</div>

        <div class="contact row">
            <#if serviceEmail!=0 ><div class="contact-item col-md-3"><img src="${ctx}/static/${version}/img/drd/phone.png"/><p><@spring.message code="kefurexian"/>: ${servicePhone!}</p></div></#if>
            <#if serviceQQ!=0 ><div class="contact-item col-md-3"><img src="${ctx}/static/${version}/img/drd/qq.png"/><p>${serviceQQ!}</p></div></#if>
            <#if serviceQQ!=0 ><div class="contact-item col-md-3"><img src="${ctx}/static/${version}/img/drd/wx.png"/><p>${serviceWX!}</p></div></#if>
            <#if serviceEmail!=0 ><div class="contact-item col-md-3"><img src="${ctx}/static/${version}/img/drd/email.png"/><p><@spring.message code="kefuyouxiang"/>: ${serviceEmail!}</p></div></#if>
        </div>
<div class="fxtip">
    <h3 class="fxtip-item"><@spring.message code="fengxiantishi"/></h3>
    <p class="fxtip-item"><@spring.message code="fengxiantishi_content"/></p>
</div>
<div class="copyright clear">
    <#if siteCopyright!=0 >${siteCopyright!}</#if>
</div>
</div>

</div>

<!--
   <#import "/base/spring.ftl" as spring/>
            <footer class="footer-nav">
                <div class="container fadeInUp contentAnimated" data-animation="true" data-animation-type="fadeInUp">
                    <div class="row">
                         <div class="col-sm-8 col-xs-6">
                            <ul  class="col-xs-6 col-sm-2 hidden-xs abouts">
                                <h4><@spring.message code="guanyuwomen"/></h4>
                                <li ><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>61<#else>72</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>61<#else>72</#if></#if>" ><@spring.message code="shiyongtiaokuan"/></a></li>
                                <li ><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>62<#else>73</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>62<#else>73</#if></#if>" ><@spring.message code="yinsizhengce"/></a></li>
                                <li ><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>63<#else>74</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>63<#else>74</#if></#if>" ><@spring.message code="lianxiwomen"/></a></li>
                            </ul>
                            <ul  class="col-xs-6 col-sm-2 hidden-xs">
                                <h4><@spring.message code="xinwenzixun"/></h4>
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>4<#else>65</#if>
<#if tokenId??>.do?tokenId=${tokenId}</#if>" target="_blank"><@spring.message code="zuixindongtai"/></a></li>
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>5<#else>64</#if>
<#if tokenId??>.do?tokenId=${tokenId}</#if>" target="_blank"><@spring.message code="xinwenzixun"/></a></li>
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>6<#else>66</#if>
<#if tokenId??>.do?tokenId=${tokenId}</#if>" target="_blank"><@spring.message code="hangyedongtai"/></a></li>
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>7<#else>80</#if>
<#if tokenId??>.do?tokenId=${tokenId}</#if>" target="_blank"><@spring.message code="bizhongxinwen"/></a></li>
                            </ul>
                              <ul  class="col-xs-6 col-sm-2 hidden-xs">
                                <h4><@spring.message code="Helpcenter"/></h4>
                                <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>37<#else>68</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>37<#else>68</#if></#if>" target="_blank"><@spring.message code="xinshouzhiyin"/></a></li>
                                <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>38<#else>69</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>38<#else>69</#if></#if>" target="_blank"><@spring.message code="bizhongziliao"/></a></li>
                                <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>39<#else>70</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>39<#else>70</#if></#if>" target="_blank"><@spring.message code="changjianwenti"/></a></li>
                                <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>40<#else>71</#if>&tokenId=${tokenId}<#else>.do?categoryId=<#if locale == 'zh_CN'>40<#else>71</#if></#if>" target="_blank"><@spring.message code="falvwenjian"/></a></li>
                            </ul>

                 	 <ul class="col-xs-12 col-sm-6">
                                <h4><@spring.message code="lianxiwomen"/></h4>
                                <li><a href="#"><@spring.message code="kefurexian"/>: ${servicePhone!}</a></li>
                                <li><a href="#"><@spring.message code="kefuyouxiang"/>: ${serviceEmail!}</a></li>
                                <li><a href="#" target="_blank"><@spring.message code="dizhi"/>: ${companyAdress!}</a></li>
                                <li>${siteCopyright!}</li>
                     </ul>


                         </div>
                        <div class="col-sm-4 col-xs-6">

                            <ul class="col-xs-12 text-left">
                                <h4 class="ui-logo-text"><@spring.message code="fengxiantishi"/></h4>
                                <li class="">
                                <@spring.message code="fengxiantishi_content"/>
                                </li>
                                <li class="secure-warn">
                                    <p><@spring.message code="jiaoyiyoufengxian"/>！</p>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </footer>
   <#if isProposal!=1>${write_proposal}</#if>
-->