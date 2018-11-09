<#include "/base/base.ftl">
    <style type="text/css">
        .contain-drd{
            width: 100%;
            background: #05183a;
            color: #fff;
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
            color: #fff;
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
          color: #fff;
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
                <!--<#if serviceQQ!=0 ><div class="contact-item col-md-3"><img src="${ctx}/static/${version}/img/drd/wx.png"/><p>${serviceWX!}</p></div></#if>-->
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

 <!-- <div class="footer-box fl">
  <div class="container">
    <div class="footer">
      <div class="footer-item fl">
        <h3><@spring.message code="guanyuwomen"/></h3>
        <ul>
          	<li ><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>61<#else>72</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>61<#else>72</#if></#if>" ><@spring.message code="shiyongtiaokuan"/></a></li> 
        	<li ><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>62<#else>73</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>62<#else>73</#if></#if>" ><@spring.message code="yinsizhengce"/></a></li> 
            <li ><a href="${ctx}/news/aboutus<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>63<#else>74</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>63<#else>74</#if></#if>" ><@spring.message code="lianxiwomen"/></a></li> 
        </ul>
      </div>
      <div class="footer-item fl">
        <h3><@spring.message code="xinwenzixun"/></h3>
        <ul>
                                          <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>4<#else>65</#if>
<#if tokenId??>.do?</#if>" target="_blank"><@spring.message code="zuixindongtai"/></a></li> 
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>5<#else>64</#if>
<#if tokenId??>.do?</#if>" target="_blank"><@spring.message code="xinwenzixun"/></a></li> 
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>6<#else>66</#if>
<#if tokenId??>.do?</#if>" target="_blank"><@spring.message code="hangyedongtai"/></a></li>
                                <li ><a href="${ctx}/news/index/<#if locale == 'zh_CN'>7<#else>80</#if>
<#if tokenId??>.do?</#if>" target="_blank"><@spring.message code="bizhongxinwen"/></a></li> 
        </ul>
      </div>
      <div class="footer-item fl">
        <h3><@spring.message code="Helpcenter"/></h3>
        <ul>
        <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>37<#else>68</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>37<#else>68</#if></#if>" target="_blank"><@spring.message code="xinshouzhiyin"/></a></li> 
        <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>38<#else>69</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>38<#else>69</#if></#if>" target="_blank"><@spring.message code="bizhongziliao"/></a></li> 
        <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>39<#else>70</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>39<#else>70</#if></#if>" target="_blank"><@spring.message code="changjianwenti"/></a></li> 
        <li ><a href="${ctx}/news/help<#if tokenId??>.do?categoryId=<#if locale == 'zh_CN'>40<#else>71</#if><#else>.do?categoryId=<#if locale == 'zh_CN'>40<#else>71</#if></#if>" target="_blank"><@spring.message code="falvwenjian"/></a></li> 
        </ul>
      </div>
      <div class="footer-item fl">
        <h3><@spring.message code="lianxiwomen"/></h3>
        <ul>
           <#if serviceEmail!=0 ><li><@spring.message code="kefurexian"/>: ${servicePhone!}</li></#if>
            <#if serviceEmail!=0 ><li><@spring.message code="kefuyouxiang"/>: ${serviceEmail!}</li></#if>
            <#if serviceQQ!=0 ><li>QQ: ${serviceQQ!}</li></#if>
            <#if companyAdress!=0 ><li><@spring.message code="dizhi"/>: ${companyAdress!}</li></#if>
            <#if siteCopyright!=0 ><li>${siteCopyright!}</li></#if>
        </ul>
      </div>
      <div class="footer-item fr" style="width:386px;">
        <h3><@spring.message code="fengxiantishi"/></h3>
        <p><@spring.message code="fengxiantishi_content"/></p>
      </div>
    </div>
  </div>
</div>
<#if isProposal!=1>${write_proposal}</#if> -->