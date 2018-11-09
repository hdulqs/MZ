<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title></title>
    <#include "/base/base.ftl">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/intlTelInput.css">
  </head>
  <style>
  .intl-tel-input{
   display:inline-block;
  }
  </style>
  <body>
  <div class="container-fluid person-con">
    <div class="wrap">
        <div class="">
            <div class="midContainer">
                <!-- <div class="alert" style="margin-top:15px;">*只支持中国大陆地区的手机号。</div> -->
              	<div class="row" style="margin-bottofm:15px;">
				<div class="panel_wrap_head wrap_head">
					<div class="">
						<ul class="wrap_tabs" role="tablist" id="RMBtab">
							<li role="presentation" class="active pull-left">
							<#if phoneState=0>
									<a href="" ><@spring.message code="shezhisjrz"/></a>
							<#else>
								   <a href="" ><@spring.message code="genghuansjh"/></a>
					</#if>
							</li>

						</ul>
					</div>
                </div>

                <div class="safecontent">
                    <#--<p class="jy_infobox" style="color:red;"><span class="jy_tag">!</span><@spring.message code="weiwanchengsjbd"/></p>-->
                        <form id="bind-form" action="" method="post" class="">
                            <div style="line-height:50px;">
                                <label><@spring.message code="yibangdingphone"/></label>
                                <span style="color: red;margin-left:5px;">${phoneHide}</span>
                            </div>
                            <div class="filed">
                                <label class=""><@spring.message code="duanxinma"/> </label>
                                <input id="verifyCode-one" type="text" name="verifyCode-one" datatype="*" style="width:165px" nullmsg="该字段不能为空">
                                <span style="margin-top: -10px;" id="yzm-btn-one" class="btn btn-yellow"><@spring.message code="fasongyanzhengma"/></span>
                            </div>
                            <div class="filed"><label></label><span class="btn btn-orange " id="submitBtn_one" style="margin-left:4px;">下一步</span></div>
                        <#if meg!=null>
                            <p style="margin-left: 163px" class="Validform_checktip">${meg}</p>
                        </#if>
                        </form>
                </div>
            </div>
        </div>
    </div>

</div>





  </body>
</html>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
seajs.use(["js/front/user/setphoneone","js/i18n_base"],function(o,b){
	o.init();
	o.sendsms();
	
});

            

</script>	
