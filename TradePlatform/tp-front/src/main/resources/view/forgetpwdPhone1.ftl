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
	
	<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/index.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/global.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />
	
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
</head>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1264472947'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s19.cnzz.com/z_stat.php%3Fid%3D1264472947%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script><style>
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
 #trans-tooltip,
    #tip-arrow-bottom,
    #tip-arrow-top{
	display:none;
}
.main {
    width: 349px;
    margin: 0 auto;
    padding-bottom: 20px;
}
#cnzz_stat_icon_1264472947{
display: none;
}
body{
	width:100%;
	height: 100%;
}
.dialogcon{
	position: absolute;
	top:30%;
	left:50%;
	margin-left:-174.5px;
}
</style>
<body>
<!-- begin #page-container -->
<div >
    <div class="dialog_bg"></div>
    <div class="main dialogcon">
        <!---<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
        
        <!--banner -->
        <div class="verify middle ng-scope" ng-controller="googleVerifyCtr">         
            <form id="form_forget1" class="verify-form ng-scope ng-pristine ng-valid"  >
                <div class="verify-title"><span class="">通过手机找回密码</span></div>
                <input type="text" readonly id="phoneNum" value="${phone}" name="email" ng-model="email" style="display:none;" class="ng-pristine ng-valid">
                <input type="hidden" name="operationType" value="mobile">
                <div class="filed">
                    <input style="width:186px;" type="text" id="forgetSmsCode" name="forgetSmsCode" class="ipt">
                    <span id="sendsmsBtn" class="btn btn-red" ><@spring.message code="fasongyanzhengma"/></span>
                    <p class="Validform_checktip f-nomargin f-left Validform_wrong"></p>
                </div>
                <div class="filed">
                    <span id="forgetPwdBtn" type="button"  class="btn btn-red-light btn-block margint30 phone" class="mobile-btn"><@spring.message code="queding"/></span>
                </div>
            </form>
                  
        </div>
    </div>
  </div>



</body>
</html>
<!-- end #page-container -->
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
 
 seajs.use(["js/main","js/forgetpwd1"],function(m,f){	
	 //登录页js
	 f.init();
	 f.initdata();
 });
</script>
