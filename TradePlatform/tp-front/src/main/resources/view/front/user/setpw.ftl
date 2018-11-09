	<head>
	    <#include "/base/base.ftl">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
    </head>
<!-- begin page-container -->
<div class="container-fluid person-con min-hg">

	<!-- begin page-header -->
	<div class="row" style="margin-bottom:15px;">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" id="RMBtab">
					<li role="presentation" class="active pull-left">
						<a href=""><@spring.message code="xiugaidenglumima"/></a>
					</li>

				</ul>
			</div>
		</div>
	</div>
	<!-- end page-header -->

	<!-- begin row -->
	<div class="bg-changeTxt p-20">
		<@spring.message code="setpw_tishi1"/>
	</div>

	<form class="form-horizontal cpwd-form p-20"  >
	 <div class="form-group"> 
            <label class="col-sm-4 control-label"><@spring.message code="yuanshidenglumima"/>:</label>
            <div class="col-sm-4">
	       <input type="password" name="oldPassWord" id="oldPassWord"   required class="form-control"  placeholder=""  validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})"/>
	       </div>
      </div>
		<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="xindenglumima"/>：</label>
			<div class="col-sm-4">
				<input  type="password" name="newPassWord" id="newPassWord"   required  class="form-control" placeholder="" validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})">
				<ul class="parsley-errors-list filled">
					<li class="parsley-required"><@spring.message code="mimageshi"/></li>
				</ul>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="zaicishuruxinmima"/>：</label>
			<div class="col-sm-4">
				<input type="password" name="reNewPassWord" id="reNewPassWord"  required class="form-control" placeholder="" validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})">
			</div>
		</div>
	<!--	<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="duanxinyanzhengma"/>：</label>
			<div class="pull-left col-sm-3">
				<input type="text" name="pwSmsCode" id="pwSmsCode" required class="control-label form-control col-sm-11 border-rad" style="text-align:center;" placeholder="">
				<label id="sms_message" class=" control-label text-warning"></label>
			</div>
			<div class="col-sm-1">
				<button class="btn btn-default" type="button" id="sendsmsBtn" ><@spring.message code="dianjihuoqu"/></button>
			</div>
		</div>
		-->
		
		<div class="form-group">
		
			<div class="col-sm-4 col-sm-offset-4">
				<button type="button"  id="submitBtn"  class="btn btn-primary form-control f-s-16"><@spring.message code="baocun"/></button>
			</div>
		</div>
	</form>
	

	<!-- end row -->
</div>



<div class="verifyLayout">
    <div class="dialog_bg"></div>
    <div class="main dialogcon">
        <!--<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
        <!--banner -->
        <div class="dialog-close">×</div>
        <div class="verify middle ng-scope" ng-controller="googleVerifyCtr">
            <!-- 谷歌二次验证 -->
            <!-- ngIf: verifyType==1 -->


            <!-- 手机二次验证 -->
            <!-- ngIf: verifyType==2 -->
            <form class="verify-form ng-scope ng-pristine ng-valid" action="/user/login.html" method="post" id="mobile-form" ng-if="verifyType==2">
                <div class="verify-title"><span class=""><@spring.message code="shoujirenzheng"/></span></div>
                <input type="text" readonly="" name="email" ng-model="email" style="display:none;" class="ng-pristine ng-valid">
                <input type="hidden" name="operationType" value="mobile">
                <div class="filed">
                    <input style="width:186px;" type="text" id="verifyCode" name="verifyCode" class="ipt">
                    <span id="sendBtn" class="btn btn-grey" ><@spring.message code="fasongyanzhengma"/></span>
                    <p class="Validform_checktip f-nomargin f-left Validform_wrong"></p>
                </div>
                <div class="filed">
                    <input type="button"  value="<@spring.message code="queding"/>" class="btn btn-orange btn-block phone" class="mobile-btn">
                </div>
            </form>
            <!-- end ngIf: verifyType==2 -->

            <!-- 谷歌或手机二次验证 -->
            <!-- ngIf: verifyType==0 -->
        </div>
    </div>
  </div>



 <div class="verifyLayout1">
      <div class="dialog_bg"></div>
    <div class="main dialogcon">
            <!--<a href="/"><img  class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
            <!--banner -->
            <div class="dialog-close">×</div>
            <div class="verify middle">
                <!-- 谷歌二次验证 -->
                <!-- ngIf: verifyType==1 -->
                <form class="verify-form" action="" method="post" id="googleVerify-form">
                    <div class="verify-title"><span class=""><@spring.message code="gugerenzheng"/></span></div>
                    <input type="text" readonly="" name="email" style="display:none;" class="n">
                    <input type="hidden" name="operationType" value="google">
                    <div class="filed">
                        <input type="text" id="password" name="verifyCode" class="ipt googlee" id="verifyCode1">
                        <p class="Validform_checktip f-nomargin f-left"></p>
                    </div>
                    <div class="filed">
                        <input type="button"  value="<@spring.message code="queding"/>" class="btn btn-orange btn-block goog" class="googleVerify-btn">
                    </div>
                </form>
                <!-- end ngIf: verifyType==1 -->


                <!-- 手机二次验证 -->
                <!-- ngIf: verifyType==2 -->

                <!-- 谷歌或手机二次验证 -->
                <!-- ngIf: verifyType==0 -->
            </div>
             </div>
        </div>
    </div>










 <div class="verifyLayout2">
         <div class="dialog_bg"></div>
    <div class="main dialogcon">
            <!--<a href="/"><img class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
            <!--banner -->
            <div class="dialog-close">×</div>
            <div class="verify middle">
                <!-- 谷歌二次验证 -->
                <!-- ngIf: verifyType==1 -->


                <!-- 手机二次验证 -->
                <!-- ngIf: verifyType==2 -->

                <!-- 谷歌或手机二次验证 -->
                <!-- ngIf: verifyType==0 -->
                <div>
                    <div class="verify-title"><span class=""><@spring.message code="erciyanzheng"/></span></div>
                    <div class="btns">
                        <span class="btn cur"><@spring.message code="gugerenzheng"/></span><span class="btn"><@spring.message code="shoujirenzheng"/></span>
                    </div>
                    <form class="verify-form" action="" method="post" id="googleVerify-form">

                        <input type="text" id="email" name="email" style="display:none;">
                        <input type="hidden" name="operationType" value="google">
                        <div class="label f-left"><@spring.message code="gugeyanzhengma"/></div>
                        <div class="filed">
                            <input type="text" name="verifyCode" class="ipt  secondg" datatype="*" nullmsg="该字段不能为空">
                            <p class="Validform_checktip f-nomargin f-left"></p>
                        </div>
                        <div class="filed">
                            <input type="button"  value="<@spring.message code="queding"/>" class="btn btn-orange btn-block googleVerifyb " class="">
                        </div>
                    </form>
                    <form class="verify-form1" action="" method="post" id="mobile-form">
                        <div class="label f-left"><@spring.message code="duanxinyanzhengma"/></div>
                        <input type="text" readonly="" name="email" style="display:none;" class="">
                        <input type="hidden" name="operationType" value="mobile">
                        <div class="filed">
                            <input style="width:186px;" type="text" id="password" name="verifyCode" class="ipt secondp">
                            <span id="sendBtn1" class="btn btn-grey"><@spring.message code="fasongyanzhengma"/></span>
                            <p class="Validform_checktip f-nomargin f-left"></p>
                        </div>
                        <div class="filed">
                            <input type="button"  value="<@spring.message code="queding"/>" class="btn btn-orange btn-block mobileb" class="">
                        </div>
                    </form>
                </div>
                <!-- end ngIf: verifyType==0 -->
            </div>
        </div>
    </div>
    </div>

<script type="text/javascript">
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
seajs.use(["js/front/user/setpw","js/base/secondvail","js/base/firstvail"],function(o,mg,fi){
	o.init();
	o.sendsms();
	mg.mgvail("setpw");
});
</script>	

	
	
	 


 
