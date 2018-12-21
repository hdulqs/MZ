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

	<!-- ================== BEGIN BASE CSS STYLE =============== -->
	<link href="${ctx}/static/${version}/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/font-awesome.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/animate.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/web-responsive.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/mdefault.css" id="theme" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/index.css" rel="stylesheet"/>
	<!-- ================== END BASE CSS STYLE ================== -->

	<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/index.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/global.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />



	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/reg.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/jquery.slider.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/secret.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/css/normalize.css?v=20180917"/>
    <style>
        html,body{
            height: 100%;
            background: rgba(255,255,255,0);
        }
        .verifyLayout{
            background: transparent;
        }
        .navbar-brand{
            width: 100%;
            float: none !important;
            height: auto !important;
            padding: 0px !important;
            line-height: normal !important;
            max-height: 75px !important;
            position: absolute;
            top: 50px !important;
            z-index: 999999;
            /*height: 60px;
            line-height: 60px;*/
        }
        .navbar-brand img{
            display: block;
            margin: 0 auto;
            max-height: 75px !important;
            /*width: 60px;*/
            /* height: 60px;*/
        }
        .verifyLayout{
            height: auto !important;
            min-height: auto !important;
            min-width: auto !important;

        }
        .container-drd{
            position: absolute !important;
            bottom: 100px !important;
        }
        .verifyLayout .main {
            width: 518px;
            padding: 30px 0;
            background: #fff;
            position: absolute;
            z-index: 1;
            left: 50%;
            top: 400px;
            text-align: center;
            -webkit-transform: translate(-50%,-50%);
            -moz-transform: translate(-50%,-50%);
            -ms-transform: translate(-50%,-50%);
            -o-transform: translate(-50%,-50%);
            transform: translate(-50%,-50%);
            border-radius: 8px;
        }
    </style>
</head>
<body>
<!-- begin #page-container -->
<div id="page-container" class="in login-h">
    <@HryTopOrFooter url="base/logo.ftl"/>
  <div class="verifyLayout">
        <div class="main">
              <#--<@HryTopOrFooter url="base/logoemail.ftl"/>-->
            <div>
                <div class="formWrapLR">

                    <form class="loginReg-form" id="resetPwd-form" action="" method="get">

                        <h3 class="LRtitle"><span class=""><@spring.message code="zhaohuimima"/></span></h3>
                        <p class="Validform_checktip f-nomargin"></p>
                        <input type="hidden" name="email" id="email" value="">
                        <input type="hidden" name="vc" id="verifyCode" value="">
                        <div class="filed">
                           <input type="email" placeholder="请输入注册邮箱/手机号码" class="ipt-drd fillemail fillemail-white" name="email"  datatype="e">
                        </div>
                        <!--<div class="filed" style="position:relative;">
                            <div id="slider1"></div>
                            <div class="ddd imgCaptcha">
                                <div class="imgCaptcha_text"><input id="nc_1_captcha_input" maxlength="6" type="text" style="ime-mode:disabled"></div>
                                <div class="imgCaptcha_img" id="nc_1__imgCaptcha_img">
                                    <img src="#" style="width:139px;" />
                                </div>
                                <i id="nc_1__btn_1" class="nc_iconfont nc_btn_1 icon-shuaxin iconfont icon-shuaxin"></i>
                                <div class="imgCaptcha_btn">
                                    <div id="nc_1__captcha_img_text" class="nc_captcha_img_text"></div>
                                    <div id="nc_1_scale_submit" class="nc_scale_submit"><span class="nc-lang-cnt" data-nc-lang="_submit"><@spring.message code="querentijiao"/></span></div>
                                </div>
                            </div>
                        </div>-->
                        <div class="filed f-right">
                            <input type="button" id="btn-ok" value=<@spring.message code="queding"/> class="margint30 btn btn-red-light btn-lg btn-block"  >
                            <a class="pull-right"  href="${ctx}/login" style="color: #ff4646;padding-top:20px"><@spring.message code="Login"/></a>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
    <!-- begin #footer -->
    <@HryTopOrFooter url="base/footer-drd.ftl"/>
    <!-- end #footer -->
</div>
<!-- end #page-container -->
</body>
</html>
<!-- end #page-container -->
<script src="${ctx}/static/${version}/lib/google/js/jquery.min.js"></script>
<script src="${ctx}/static/${version}/lib/polygonizr.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
 seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
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

 seajs.use(["js/main","js/forgetpwdemail","js/i18n_base","js/base/switchlan"],function(m,f,o,switchlan){
	 m.init();
	 //登录页js
	 f.init();
     switchlan.language();
 });
</script>
<script type="text/javascript">
    $(window).resize(function(){
        location.reload();
    });
    $('#page-container').polygonizr();
</script>