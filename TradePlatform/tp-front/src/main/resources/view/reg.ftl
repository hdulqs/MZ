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
	<link href="${ctx}/static/${version}/lib/exstatic/css/test.less" rel="stylesheet/less" type="text/css" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/index.css" rel="stylesheet"/>
	<!-- ================== END BASE CSS STYLE ================== -->
	
	<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css?v=20180816" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/index.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/global.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/intlTelInput.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/css/normalize.css?v=20180917"/>
</head>
<body>
<style>
      html,body{
          height: 100%;
          background: rgba(255,255,255,0);
      }
    .login-form{
        margin-top: 98px;
        background: #fff;
        border-radius: 8px;
    }
    table#marketlist td {
        border: 0px !important;
        border-bottom-color: #fff !important;
    }
    a.siteTypeSelect,
    a.skipToOtherWeb {
	color: #ffffff;
	background: rgba(39,74,127,0.3);
}
	.header.navbar .navbar-nav>li>a.btn-blue{
	background-color: rgba(39,155,253,0.35);
}
</style>
<script type="text/JavaScript">
    window.nofind=function(){
        var img=event.srcElement;
        img.src=_ctx+"/static/"+_version+"/img/drd/logo.png";
        img.onerror=null;
    }
</script>
<style type="text/css">
    .navbar-brand{
        width: 100%;
        height: 60px;
        line-height: 60px;
    }
    .navbar-brand img{
        display: block;
        margin: 0 auto;
        width: 60px;
        height: 60px;
    }
</style>
<!-- begin #page-container -->
    <div id="page-container" class="in login-h">

        <!-- begin #home -->
        <style>
        	.borderActive{
        		border-bottom: 2px solid #ff4646;
        	}
        </style>
            <!-- begin #header -->
            <@HryTopOrFooter url="base/logo.ftl"/>
            <!-- end #header -->

            <div class="page-banner page-banner-home login-h">
                <div class="banner-slogan">
                    <div class="container">

                        <div role="tabpanel" class="reg-container col-md-8 col-sm-8 col-md-offset-2 col-sm-offset-2">

                            <!--注册表{{' A84 '| translate}}-->
                            <div role="tabpanel" class="tab-pane " id="reg1" >
                                <form method="post"  class="login-form" >
                                    <div class="login-container">
                                        <div style="height: 25px;">
                                            <#--<div onclick="tabActive(this)" class="tabItem borderActive" style="cursor:pointer;width:50%;font-size: 20px;color: #ff4646;float: left;" class="text-center"><@spring.message code="youxiangzhuce"/></div>-->
                                            <div onclick="tabActive(this)" class="tabItem borderActive" style="cursor:pointer;width:100%;font-size: 20px;color: #ff4646;padding: 5px 0;" class="text-center"><@spring.message code="shoujizhuce"/></div>
                                        </div>

                                        <#--<script>
                                            function
                                            tabActive(obj){
                                                $(".tabItem").removeClass("borderActive");
                                                $(obj).css("color","#ff4646").siblings().css("color","#999");
                                                $(obj).addClass("borderActive");
                                                var index = $(obj).index();
                                                $(".form").hide();
                                                $(".form"+index).show();
                                                //触发验证码刷新
                                                if (index == 0){
                                                    $("#img_captcha").click();
                                                }else if (index == 1){
                                                    $("#img_captcha2").click();
                                                }else{
                                                    $("#img_captcha2").click();
                                                    $("#img_captcha").click();
                                                }
                                            }
                                        </script>-->
                                        <!-- 邮箱注册 -->
                                        <#--<div class="row form form0" style="padding-top: 30px;">
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-envelope" style="top: 11px; font-size:22px;"></i>&ndash;&gt;
                                                <i class="posa" style="left: 23px;top:15px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/email.png"/></i>
                                                <input name="email" id="email" class="form-control input-lg" style="padding-left:50px" type="text" placeholder="<@spring.message code='please_write_email'/>"/>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-keyboard-o"></i>&ndash;&gt;
                                                <i class="posa" style="left: 23px;top:11px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/lock.png"/></i>
                                                <input name="password" id="password" class="form-control input-lg" style="padding-left:50px" type="password" placeholder="<@spring.message code='denglumimma'/>"/>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-keyboard-o"></i>&ndash;&gt;
                                                <i class="posa" style="left: 23px;top:11px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/lock.png"/></i>
                                                <input name="rePassword" id="rePassword" class="form-control input-lg" style="padding-left:50px" type="password" placeholder="<@spring.message code='denglumimma'/>"/>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa megico"></i>&ndash;&gt;
                                                <div class="code-form-input clearfix">
                                                    <i class="posa" style="left: 23px;top:12px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/qrcode.png"/></i>
                                                    <input style="padding-left:50px;outline:none;border:1px solid #ccc" name="registCode" id="registCode" class="form-picode input-lg" type="text" placeholder="<@spring.message code='tuxingyanzhengma'/>">
                                                    <span class="pull-right code-span"> <img id="img_captcha" title="换一张" src="${ctx}/sms/registcode?${t}" alt="换一张" width="80" > </span>
                                                </div>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-user"></i>&ndash;&gt;
                                                <i class="posa" style="left: 23px;top:12px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/recommend.png"/></i>
                                                <input name="referralCode" id="referralCode" value="${commendCode}" class="form-control input-lg" style="padding-left:50px" type="text" placeholder="<@spring.message code='tuijianrenshoujihao'/>"/>
                                            </div>
                                            <div class="col-xs-12 col-sm-12">
                                                <button class="btn btn-red-light btn-lg col-xs-12"  type="button"   id="regBtn" style="margin-top: 40px;"><@spring.message code="zhuce"/></button>
                                                <a class="pull-right color-red-drd"  href="${ctx}/login" style="color: #666;margin-top: 10px"><@spring.message code="Login"/></a>
                                            </div>
                                            <div class="checkbox form-group  col-xs-12 col-sm-5  col-md-12 " style="height:20px;line-height: 20px;">
                                                <label>
                                                    <input type="checkbox" id="check_deal"> <@spring.message code="woyiyuedu"/>  <a href="javascript:;" data-toggle="modal" data-target="#reg_pro">《<@spring.message code="zhucexieyi"/>》</a>
                                                </label>
                                            </div>
                                        </div>-->
                                        <!-- 手机注册 -->
                                        <div class="row form form1" style="padding-top: 30px">
                                            <div class="col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-mobile" style="top: 11px; font-size:22px;"></i>-->
                                                <div class="form-group  col-xs-8 col-sm-8" style="padding:0 !important;float: left !important;">
                                                    <i class="posa" style="left: 17px;top:12px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/mobile.png"/></i>
                                                    <input id="mobile2"  name="mobile2" class="form-control input-lg" style="padding-left:50px" type="text" placeholder="<@spring.message code='qingshurushoujihao'/>" />
                                                </div>
                                                <div class="filed col-xs-3 col-sm-3" style="position: relative;padding:0 !important;float: right !important;">
                                                    <input class="form-control input-lg" id="mobile" readonly  name="mobile" type="text"  class="">
                                                </div>
                                            </div>

                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa megico"></i>-->
                                                <i class="posa" style="left: 26px;top:11px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/qrcode.png"/></i>
                                                <div class="code-form-input clearfix">
                                                    <input style="padding-left:50px;outline:none; border:1px solid #ccc" name="registCode2" id="registCode2" class="form-picode input-lg" type="text" placeholder="<@spring.message code='tuxingyanzhengma'/>" />
                                                    <span class="pull-right code-span"> <img id="img_captcha2" title="换一张" src="${ctx}/sms/registcode?${t}" alt="换一张" width="80" > </span>
                                                </div>
                                            </div>

                                            <div class="form-group col-xs-12 col-sm-12" style="padding:0 !important;">
                                                    <!--  <i class="posa fa fa-keyboard-o" style="left:12px !important;"></i>-->
                                                    <i class="posa" style="left: 26px;top:11px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/code.png"/></i>
                                                    <input name="registSmsCode" id="registSmsCode" class="form-control input-lg" style="width: 57%;margin-left: 10px;padding-left:50px;float: left" type="text" placeholder="<@spring.message code='qingshurushoujiyanzhengma'/>">
                                                    <button class="form-control input-lg" type="button" id="sendsmsBtn" style="width: 130px;margin-right: 10px;padding:0 !important;float:right;font-size: 12px !important;text-align: center;background: #ff4646;color:#fff;"><@spring.message code="fasongyanzhengma"/></button>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-keyboard-o"></i>-->
                                                <i class="posa" style="left: 26px;top:11px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/lock.png"/></i>
                                                <input name="password2" id="password2" class="form-control input-lg" style="padding-left:50px" type="password" placeholder="<@spring.message code='denglumimma'/>"/>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-keyboard-o"></i>-->
                                                <i class="posa" style="left: 26px;top:11px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/lock.png"/></i>
                                                <input name="rePassword2" id="rePassword2" class="form-control input-lg" style="padding-left:50px" type="password" placeholder="<@spring.message code='denglumimma'/>"/>
                                            </div>

                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <!--<i class="posa fa fa-user"></i>-->
                                                <i class="posa" style="left: 26px;top:12px;"><img src="${ctx}/static/${version}/lib/view_v1/dist/images/login/recommend.png"/></i>
                                                <input id="pusername" type="hidden" value="${commendCode}">
                                                <input name="referralCode2" id="referralCode2" value="${commendCode}" class="form-control input-lg" style="padding-left:50px" type="text" placeholder="<@spring.message code='tuijianrenshoujihao'/>"/>
                                            </div>
                                            <div class="form-group  col-xs-12 col-sm-12">
                                                <button class="btn btn-red-light btn-lg col-xs-12"  type="button"   id="regBtn2" style="margin-top: 40px;"><@spring.message code="zhuce"/></button>
                                                <a class="pull-right color-red-drd"  href="${ctx}/login" style="color: #666;padding-top: 20px"><@spring.message code="Login"/></a>
                                            </div>
                                            <div class="checkbox form-group  col-xs-12 col-sm-5  col-md-12 " style="height:20px;line-height: 20px;margin-top: 0;">
                                                <label>
                                                    <input type="checkbox" id="check_deal2"> <@spring.message code="woyiyuedu"/>  <a href="javascript:;" data-toggle="modal" data-target="#reg_pro">《<@spring.message code="zhucexieyi"/>》</a>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>


                        </div>

                    </div>
                </div>
                <!-- /.banner-slogan -->
            </div>
            <!-- end #home -->

            <!-- begin #footer -->
            <@HryTopOrFooter url="base/footer-drd.ftl"/>
            <!-- end #footer -->
    </div>
    <!-- end #page-container -->

</body>
</html>


<div class="modal fade ng-scope" id="reg_pro" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="z-index:9999">
    <div class="modal-content  p-0 bg-dark" style="height:500px;overflow-y:auto;">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title"><@spring.message code="xieyi1"/></h4>
      </div>
      <div class="modal-body">
        <p class="text-left">   ${regreg}   
<#--<@spring.message code="xieyi2"/></br>
<@spring.message code="xieyi3"/></br>
<@spring.message code="xieyi4"/></br>
<@spring.message code="xieyi5"/></br>
<@spring.message code="xieyi6"/></br>
<@spring.message code="xieyi7"/></br>
<@spring.message code="xieyi8"/></br>
<@spring.message code="xieyi9"/></br>
<@spring.message code="xieyi10"/></br>
<@spring.message code="xieyi11"/></br>
<@spring.message code="xieyi12"/></br>
<@spring.message code="xieyi13"/></br>
<@spring.message code="xieyi14"/></br>
<@spring.message code="xieyi15"/></br>
<@spring.message code="xieyi16"/></br>
<@spring.message code="xieyi17"/></br>
<@spring.message code="xieyi18"/></br>
<@spring.message code="xieyi19"/></br>
<@spring.message code="xieyi20"/></br>
<@spring.message code="xieyi21"/></br>
<@spring.message code="xieyi22"/></br>
<@spring.message code="xieyi23"/></br>
<@spring.message code="xieyi24"/></br>
<@spring.message code="xieyi25"/></br>
<@spring.message code="xieyi26"/></br>
<@spring.message code="xieyi27"/></br>
<@spring.message code="xieyi28"/></br>
<@spring.message code="xieyi29"/></br>
<@spring.message code="xieyi30"/></br>
<@spring.message code="xieyi31"/></br>
<@spring.message code="xieyi32"/></br>
<@spring.message code="xieyi33"/></br>
<@spring.message code="xieyi34"/></br>
<@spring.message code="xieyi35"/></br>
<@spring.message code="xieyi36"/></br>
<@spring.message code="xieyi37"/></br>
<@spring.message code="xieyi38"/></br>
<@spring.message code="xieyi39"/></br>
<@spring.message code="xieyi40"/></br>
<@spring.message code="xieyi41"/></br>
<@spring.message code="xieyi42"/></br>
<@spring.message code="xieyi43"/></br>
<@spring.message code="xieyi44"/></br>
<@spring.message code="xieyi45"/></br>
<@spring.message code="xieyi46"/></br>
<@spring.message code="xieyi47"/></br>
<@spring.message code="xieyi48"/></br>
<@spring.message code="xieyi49"/></br>
<@spring.message code="xieyi50"/></br>
<@spring.message code="xieyi51"/></br>
<@spring.message code="xieyi52"/></br>
<@spring.message code="xieyi53"/></br>
<@spring.message code="xieyi54"/></br>
<@spring.message code="xieyi55"/></br>
<@spring.message code="xieyi56"/></br>
<@spring.message code="xieyi57"/></br>
<@spring.message code="xieyi58"/></br>
<@spring.message code="xieyi59"/></br>
<@spring.message code="xieyi60"/></br>
<@spring.message code="xieyi61"/></br>
<@spring.message code="xieyi62"/></br>
<@spring.message code="xieyi63"/></br>
<@spring.message code="xieyi64"/></br>
<@spring.message code="xieyi65"/></br>
<@spring.message code="xieyi66"/></br>
<@spring.message code="xieyi67"/></br>
<@spring.message code="xieyi68"/></br>
<@spring.message code="xieyi69"/></br>
<@spring.message code="xieyi70"/></br>
<@spring.message code="xieyi71"/></br>
<@spring.message code="xieyi72"/></br>
<@spring.message code="xieyi73"/></br>
<@spring.message code="xieyi74"/></br>
<@spring.message code="xieyi75"/></br>
<@spring.message code="xieyi76"/></br>
<@spring.message code="xieyi77"/></br>
<@spring.message code="xieyi78"/></br>
<@spring.message code="xieyi79"/></br>
<@spring.message code="xieyi80"/></br>
<@spring.message code="xieyi81"/></br>
<@spring.message code="xieyi82"/></br>
<@spring.message code="xieyi83"/></br>
<@spring.message code="xieyi84"/></br>
<@spring.message code="xieyi85"/></br>
<@spring.message code="xieyi86"/></br>
<@spring.message code="xieyi87"/></br>
<@spring.message code="xieyi88"/></br>
<@spring.message code="xieyi89"/></br>
<@spring.message code="xieyi90"/></br>
<@spring.message code="xieyi91"/></br>
<@spring.message code="xieyi92"/></br>
<@spring.message code="xieyi93"/></br>
<@spring.message code="xieyi94"/></br>
<@spring.message code="xieyi95"/></br>
<@spring.message code="xieyi96"/></br>
<@spring.message code="xieyi97"/></br>
<@spring.message code="xieyi98"/></br>
<@spring.message code="xieyi99"/></br>
<@spring.message code="xieyi100"/></br>
<@spring.message code="xieyi101"/></br>
<@spring.message code="xieyi102"/></br>
<@spring.message code="xieyi103"/></br>
<@spring.message code="xieyi104"/></br>
<@spring.message code="xieyi105"/></br>
<@spring.message code="xieyi106"/></br>
<@spring.message code="xieyi107"/></br>
<@spring.message code="xieyi108"/></br>
<@spring.message code="xieyi109"/></br>
<@spring.message code="xieyi110"/></br>-->
        </p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><@spring.message code="guanbi"/></button>
      </div>
    </div>
  </div>
</div>


<script src="${ctx}/static/${version}/lib/google/js/jquery.min.js"></script>
<script src="${ctx}/static/${version}/lib/polygonizr.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript" >
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
 
 seajs.use(["js/main","js/reg","js/base/switchlan","js/i18n_base"],function(m,reg,switchlan,o){
	 m.init();
	 //注册页js
	 reg.init();
	 reg.refreshCode();
	 reg.sendsms();
     switchlan.language();
 });
</script>
<script type="text/javascript">
    $(window).resize(function(){
        location.reload();
    });
    $('#page-container').polygonizr();
</script>


