<!DOCTYPE html>
<html>
<head>
	<#include "/base/base.ftl">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!--<meta name="viewport" content="width=device-width, initial-scale=1">-->
    <meta name="description" content="">
    <meta name="author" content="">
	<@HryTopOrFooter url="base/title.ftl"/>
	<link rel="icon" type="image/x-icon"  />
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/reg.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/jquery.slider.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/secret.css">
</head>
<body>
<!-- begin #page-container -->
  <div class="verifyLayout">
        <div class="main">
            <!--<a href="/">
                <img class="icon-logo" src="/${siteLogo!}"></a>-->
            <div>
                <div class="formWrapLR">

                    <form class="loginReg-form ng-pristine" id="resetPwd-form" action="" method="get">
                         <h3 class="LRtitle"><span class="ng-binding"><@spring.message code="chongzhimima"/></span></h3>
                        <!-- <div class="alert" style="white-space: normal;line-height: 14px;width: 346px;"><@spring.message code="weileninde"/></div> -->
                        <p class="Validform_checktip f-nomargin"></p>
                        <input type="hidden" name="email" id="email" value="">
                        <input type="hidden" name="vc" id="verifyCode" value="">
                        <div class="filed">
                            <input type="password" placeholder="<@spring.message code="xinmima"/>" name="userPassword" class="ipt pwd" id="regiterPassword" datatype="pwd">
                            <input type="hidden" name="newPassword" id="password">
                        </div>
                        <div class="filed">
                            <input id="rePwd" type="password" placeholder="<@spring.message code="querenmima"/>" class="ipt pwd" onkeyup="isFormReady()" datatype="*">
                        </div>
                        <!--<div class="filed" style="position:relative;">
                            <#--<div id="slider1"></div>-->
                            <div class="ddd imgCaptcha">
                                <div class="imgCaptcha_text"><input id="nc_1_captcha_input" maxlength="6" type="text" style="ime-mode:disabled"></div>
                                <div class="imgCaptcha_img" id="nc_1__imgCaptcha_img">
                                  <div class="form-group  col-xs-12 col-sm-12">
                                                <i class="posa megico"></i>
                                                <div class="code-form-input clearfix">
                                                  <span class="pull-right code-span"> <img style=" max-width: 100%; height: 40px;" id="img_captcha" title="换一张" src="${ctx}/sms/registcode?${t}" alt="换一张" width="80" > </span>
                                                </div>
                                   </div>
                                    <img src="#" style="width:139px;" />
                                </div>
                                <div class="imgCaptcha_btn">
                                    <div id="nc_1__captcha_img_text" class="nc_captcha_img_text"></div>
                                    <div id="nc_1_scale_submit" class="nc_scale_submit"><span class="nc-lang-cnt" data-nc-lang="_submit"><@spring.message code="tijiao"/></span></div>
                                </div>
                            </div>
                        </div>-->
                        <div class="filed f-right">
                            <input type="button" id="btn-ok" value="<@spring.message code="queding"/>" class="btn btn-orange btn-block">
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
<!-- end #page-container -->

</body>
</html>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/jquery/jquery.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/jquery/jquery.cookie.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/js/i18n_base.js"></script>

<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js" ></script>
<script type="text/javascript" >
 seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/google/js/jquery.min.js",
      <!-- layer -->
      "layer" : "lib/layer/layer.js"
    },
    preload: ['jquery','layer'],
    map:[
		['.js','.js?v=${t}']//映射规则
	]
  });
 
 
 seajs.use(["js/secret"],function(s){
	 s.init();
		
 });
</script>




