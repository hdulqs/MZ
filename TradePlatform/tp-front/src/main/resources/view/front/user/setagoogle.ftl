<#include "/base/base.ftl">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
<div class="container-fluid person-con min-hg">

			<!-- begin page-header -->
			<div class="row" style="margin-bottom:15px;">
				<div class="panel_wrap_head wrap_head">
					<div class="">
						<ul class="wrap_tabs" role="tablist" id="RMBtab">
							<li role="presentation" class="active pull-left">
								<a href=""><@spring.message code="offgoogle"/></a>
							</li>

						</ul>
					</div>
				</div>
			</div>
			<!-- end page-header -->

			<!-- begin row -->
			<div class="bg-changeTxt p-20">
				<@spring.message code="24_offgoogle"/>
				
			</div>

			<form class="form-horizontal p-t-30 set-pwd form-all" ng-submit="processForm()">
			
			
				<div class="form-group">
				
				 <div class="form-group"> 
            <label class="col-sm-4 control-label"><@spring.message code="denglumima"/>:</label>
            <div class="col-sm-4">
	       <input type="password" name="PassWord" id="PassWord"   required class="form-control"  placeholder=""  validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})"/>
	       </div>
      </div>
				
				
				<label class="col-sm-4 control-label"><@spring.message code="gugeyanzhengma"/>  :  </label>
					<div class="col-sm-4 has-success has-feedback">
						<input type="password" name="accountGoogleWord" id="accountGoogleWord" required  class="form-control" placeholder="">
						<input type="hidden" value="${user.mobile}" class="mobile" />
					</div>
  
				</div>
				

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-4">
						<button type="button" id="submitBtn1" class="btn btn-primary form-control"><@spring.message code="tingyong"/></button>
					</div>
				</div>
			</form>
		
</div>
		




<div class="verifyLayout">
    <div class="dialog_bg"></div>
    <div class="main dialogcon">
        <!--<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
        <!--banner -->
        <div class="verify middle ng-scope" ng-controller="googleVerifyCtr">
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
        </div>
    </div>
  </div>



 <div class="verifyLayout1">
      <div class="dialog_bg"></div>
      <div class="main dialogcon">
            <!--<a href="/"><img  class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
            <!--banner -->
            <div class="verify middle">
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
            </div>
             </div>
        </div>
    </div>










 <div class="verifyLayout2">
         <div class="dialog_bg"></div>
    <div class="main dialogcon">
            <!--<a href="/"><img class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
            <!--banner -->
            <div class="verify middle">
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
            </div>
        </div>
    </div>
    </div>


<script type="text/javascript" src="/static/src/js/base/googleauth/jquery-1.11.1.js" ></script>
<script type="text/javascript" src="/static/src/js/base/googleauth/jquery.qrcode.js" ></script>
<script type="text/javascript" src="/static/src/js/base/googleauth/qrcode.js" ></script> 
<script type="text/javascript" src="/static/src/js/base/googleauth/utf.js" ></script>
<script type="text/javascript">
seajs.use(["js/front/user/setagoogle","js/i18n_base","js/base/secondvail","js/base/firstvail"],function(o,b,mg){
	o.init();
	o.sendsms();
	o.sendb();
	mg.mgvail("setagoogle");
	
});


</script>	

	
	
	 


 
