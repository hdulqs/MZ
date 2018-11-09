<#import "/base/spring.ftl" as spring/>
 	<style>
 		i.form-control-feedback{
 		 right:15px;
 		}
 	</style>	
 	
 	<div class="container-fluid person-con">
 	
				<!-- begin page-header -->
				<div class="row" style="margin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs" role="tablist" id="RMBtab">
								<li role="presentation" class="active pull-left">
									<a href=""><@spring.message code="huobitixiandizhiguanli"/></a>
								</li>
	
							</ul>
						</div>
					</div>
				</div>
				<!-- end page-header -->

				<!---start:{{' A62 '| translate}}{{' A44 '| translate}}表{{' A84 '| translate}}---->
				<form class="form-horizontal withdraw-form RMB-widthdraw p-20" id="withdraw_address_form" name="withdraw_address_form"  >
					<div class="form-group">
							<label class="col-sm-3 control-label"><@spring.message code="xunihuobileixing"/>:</label>
							<div class="col-sm-6">
								<select id = "select"  ng-model="formData.type" class="form-control"> 
									<#list listProduct as list>
                           			<option  value="${list.coinCode}" >${list.coinCode}</option>
                           			</#list>
                         		</select>
                         		<input type="hidden" name="currencyType" id="currencyType"  value="${listProductFirst}" />
							</div>
							
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label"><@spring.message code="qianbaogongyao"/>:</label>
							<div class="col-sm-6">

								<input type="text" id="publicKey" name="publicKey" require class="form-control" placeholder="">

							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><@spring.message code="beizhu"/>：</label>
							<div class="col-sm-6">
								<input type="textarea" name="remark"  required class="form-control" placeholder="">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-3">
								<button type="button" id="savepublickey" class="btn btn-primary form-control f-s-16"><@spring.message code="querentijiao"/></button>
								</div>
						</div>
				</form>
				<!---end:{{' A62 '| translate}}{{' A44 '| translate}}表{{' A84 '| translate}}---->

			</div>
			<!--end page-container --->
 	
 	
 	<!-- begin page-container -->
			<div class="container-fluid person-con">
			
				<!-- begin page-header -->
				<div class="row" style="margin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs" role="tablist" id="RMBtab">
								<li role="presentation" class="active pull-left">
									<a href=""><@spring.message code="huobitixiandizhi"/></a>
								</li>
							</ul>
						</div>
					<!--	<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>-->
					</div>
				</div>
				<!-- end page-header -->

				<!-- begin row -->
				
				<div class="row">
					<!-- begin col-3 -->
					<div class="col-sm-12">
						<div class="table-responsive fund-dtable">
							<table class="table" id="tablepublic">
								<thead>
								<tr class="active">
									<th><@spring.message code="gongyaohao"/></th>
									<th><@spring.message code="tianjiashijian"/></th>
									<th><@spring.message code="bideleixing"/></th>
									<th><@spring.message code="beizhu"/></th>
									<th><@spring.message code="caozuo"/></th>
								</tr>
								</thead>
								<tbody>
								<!--begin 循环{{' A25 '| translate}}-->
								<#list listPublic as list>
								<tr ng-repeat= "item in accountList">
									<td>${list.publicKey}</td>
								 	<td name="createlong">${list.create_long}</td>
								 	<td>${list.currencyType}</td>
								 	<td>${list.remark}</td>
								 	<td><input type="hidden" value="${list.id}" id="deletePubValue" /><input type="button" value='<@spring.message code="shanchu"/>' id="deletePub" /></td>
								</tr>
								</#list>
								<!--end 循环{{' A25 '| translate}}-->
								<!--begin 没有{{' A25 '| translate}}的时候-->
								<!--<tr ng-if="accountList==null">
									<td  colspan="5" class="no-recode text-center" >
										<i class="fa fa-info-circle"></i>
									</td>
								</tr>-->
								<!--end 没有{{' A25 '| translate}}的时候--->
								</tbody>
							</table>
						</div>
					</div>
					<!-- end col-3 -->
				</div>
				<!-- end row -->



                <div class="verifyLayout">
                    <div class="dialog_bg"></div>
                    <div class="main dialogcon">
                        <!--<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
                        <div class="dialog-close">×</div>
                        <!--banner -->
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
                        <div class="dialog-close">×</div>
                        <!--banner -->
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
         <div class="dialog-close">×</div>
         <!--banner -->
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


<#include "/base/base.ftl">
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
<script type="text/javascript">
	seajs.use(["js/front/user/publickeylist","js/i18n_base","js/base/secondvail","js/base/firstvail"],function(p,b,mg){
        p.init();
        p.renderTime();
        mg.mgvail("setaddr");
    });
</script>
</div>