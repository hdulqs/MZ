<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title></title>
        <#include "/base/base.ftl">

    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/intlTelInput.css">
  </head>
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
								<a href=""><@spring.message code="guanbishoujirenzheng"/></a>
							</li>

						</ul>
					</div>
                </div>

                <div class="safecontent">
                    <#--<p class="jy_infobox" style="color:green;"><img src="${ctx}/static/${version}/lib/exstatic/img/done.png"><@spring.message code="ywcsjbd"/></p>-->
                    <form id="bind-form" action="" method="post" class="">
                   	<input type="hidden" value="${phone}" class="phone" />
                        <div style="line-height:50px;">
                            <label><@spring.message code="yibangdingphone"/></label>
                            <span style="color: red;margin-left:5px;">${phone}</span>
                        </div>
                        <div class="filed">
                            <label class=""><@spring.message code="duanxinma"/> </label>
                            <input id="verifyCode" type="text" name="verifyCode" datatype="*" style="width:165px" nullmsg="该字段不能为空">
                            <span style="margin-top: -10px;" id="yzm-btn" class="btn btn-yellow"><@spring.message code="fasongyanzhengma"/></span>
                            <p style="margin-left: 163px" class="Validform_checktip"></p><!--填写提示语句的地儿，校验我没加，如果这个用不到，可以删掉-->
                        </div>

                        <div class="filed"><label></label><span class="btn btn-orange " id="submitBtn" style="margin-left:4px;"><@spring.message code="querentijiao"/></span></div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

  </body>
</html>
<#include "/base/base.ftl">
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script>


seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
    },
    preload: ['jquery'],
    map:[
		['.js','.js?v=${t}']//映射规则
	]
  });
</script>
<script type="text/javascript">
seajs.use(["js/front/user/setaphone","js/i18n_base"],function(o){
	o.init();
	o.sendsms();

});


</script>
