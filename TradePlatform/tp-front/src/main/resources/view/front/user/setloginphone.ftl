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
					<!-- <div class="">
						<ul class="wrap_tabs" role="tablist" id="RMBtab">
							<li role="presentation" class="active pull-left">
							<#if phoneState=0>
									<a href="11" ><@spring.message code="shezhisjrz"/></a>
							<#else>
								   <a href="22" ><@spring.message code="genghuansjh"/></a>
					        </#if>
							</li>

						</ul>
					</div> -->
                </div>

                <div class="safecontent">
                    <#--<p class="jy_infobox" style="color:red;"><span class="jy_tag">!</span><@spring.message code="weiwanchengsjbd"/></p>-->
                    <form id="bind-form" action="" method="post" class="">
                        <!-- <div class="filed" style="position: relative;">
                            <label class=""><@spring.message code="shoujihao"/></label>
                            <input style="height: 34px;padding-left:82px;" id="mobile-number"  name="mobile" type="text"  class="">
                            <p style="margin-left: 163px" class="Validform_checktip Validform_wrong"></p>
                        </div> -->
                        <!-- <div class="filed">
                            <label class=""><@spring.message code="duanxinma"/></label>
                            <input type="hidden" value="${user.mobile}" class="mobile" />
                            <input id="verifyCode1" type="text" name="verifyCode" datatype="*" style="width:156px" nullmsg="该字段不能为空">
                            <span style="margin-top: -10px;" id="yzm-btn" class="btn btn-yellow"><@spring.message code="fasongyanzhengma"/></span>
                            <p style="margin-left: 163px" class="Validform_checktip"></p>填写提示语句的地儿，校验我没加，如果这个用不到，可以删掉
                        </div>-->

                        <div class="filed"><label></label><span class="btn btn-orange " id="submitBtn" style="margin-left:4px;"><@spring.message code="kaiqidengluyanzheng"/></span></div>
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
seajs.use(["js/front/user/setloginphone","js/i18n_base"],function(o,b,mg){
	o.init();
	/* o.sendsms(); */
	/* mg.mgvail("setphone"); */
	
});

            

</script>	
