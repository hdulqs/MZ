<#import "/base/spring.ftl" as spring/>
	<#include "/base/base.ftl">
	<head>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
    </head>
<style>
	i.form-control-feedback {
		right: 10px;
	}
</style>

<div class="container-fluid person-con">
	<!-- begin page-header -->
	<div class="panel_wrap_head wrap_head">
		<div class="">
			<ul class="wrap_tabs" role="tablist" id="RMBtab">
				<li role="presentation" class="active pull-left">
					<a href=""><@spring.message code="renminbitixian"/></a>
				</li>

			</ul>
		</div>
	</div>
	<!-- end page-header -->

	<!---start:{{' A62 '| translate}}{{' A44 '| translate}}表{{' A84 '| translate}}---->
	<form class="form-horizontal withdraw-form RMB-widthdraw p-20" name="withdraw" id="withdraw" method="post">
		<div class="form-group m-t-30">
			<label class="col-sm-3 control-label"><@spring.message code="xuanzetikuanka"/>：</label>
			<div class="col-sm-6">
				<div class="radio  m-l-10" style="display:inline-block;padding-top:0;" >
					<select id="custromerAccountNumber" name="custromerAccountNumber" class="form-control">
						<#list list as list>
						<option value="${list.id}" >${list.cardName}&nbsp;&nbsp;&nbsp;${list.subBank}&nbsp;&nbsp;&nbsp;${list.cardNumber}</option>
						</#list>
					</select>
				</div>

				<div class="radio" style="display:inline-block;">
					<label class="set-bankcard"> <i class="fa fa-cog"></i> <a href="javascript:void(0);" id="manageBank"><@spring.message code="guanliyinhanka"/></a>
					</label>
				</div>
			</div>
		</div>
         <div class="form-group">
			<label class="col-sm-3 control-label "><@spring.message code="keyongjine"/>：</label>
			<div class="col-sm-2">
				<span class="RMBmoney f-s-18 " id="firstHotspan">${appAccount.hotMoney}</span>
			</div>
			<label class="col-sm-2 control-label"><@spring.message code="ketixianjine"/>：</label>
			<div class="col-sm-3">
				<span class="RMBmoney f-s-18 maxv" id="okhotMoney">${appAccount.hotMoney}</span>
				<span class="RMBmoney f-s-18 maxb hide">{{maxWithdrawMoneyOneTime | number:3}}</span>
			</div>
		</div> 
		<div class="form-group">
			<label class="col-sm-3 control-label "><@spring.message code="tixianjine"/>：</label>
			<div class="col-sm-6">
				<input type="text" name="transactionMoney" id="transactionMoney" required  class="form-control" placeholder="<@spring.message code="qingshurujine"/>" />
				<span style="color:red" id="transactionMoney_message" ></span>
			</div>
		</div>
			<div class="col-md-6 col-sm-6 col-md-offset-3 col-sm-offset-3">
				    <div class="alert alert-warning fade in m-t-15 col-md-12 col-sm-12" id="divPrompt">
									<@spring.message code="shouxufeie"/><span id="shouxufei"></span><span id="coincode3">0.0000</span> <@spring.message code="shijidaozhange"/><span id="shijidao"></span><span id="coincode4">0.0000</span>
					</div>
			</div>
		
		<!--<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="jiaoyimima"/>：</label>
			<div class="col-sm-6">
				<input type="password" id="accountPassWord" required class="form-control" placeholder="<@spring.message code="please_write_pwd"/>">
			</div>
			<div class="col-md-6 col-md-offset-3">
				
				<input type="hidden" id="onlineWithdrawFeeRate"  value="${onlineWithdrawFeeRate}"/>
				<input type="hidden" id="maxWithdrawMoney" value="${maxWithdrawMoney}"/>
				<input type="hidden" id="maxWithdrawMoneyOneTime" value="${maxWithdrawMoneyOneTime}"/>
				<div class="alert alert-warning fade in m-t-15 col-sm-12" id="showWithdrawMoney">
					<@spring.message code="shouxufeie"/>0.000RMB,<@spring.message code="shijidaozhange"/> 0.000RMB
					<br><@spring.message code="dangtiantixianmax"/>${maxWithdrawMoney}RMB
				     <br><@spring.message code="danbitixianmax"/>${maxWithdrawMoneyOneTime}RMB
				</div>
			</div>
		</div>-->
				<!--当天最多提现金额(元)-->
				<input type="hidden" id="oldMoney"  value="${oldMoney}"/>
				<input type="hidden" id="maxWithdrawMoney" value="${maxWithdrawMoney}"/>
				<input type="hidden" id="maxWithdrawMoneyOneTime" value="${maxWithdrawMoneyOneTime}"/>
		<!--<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="shoujihaoma"/>：</label>
			<div class="col-sm-6 p-l-0"><input type="hidden" id="username" value="${username}" />
				<label class="col-sm-2 control-label f-s-18">${user.username}</label>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="duanxinyanzhengma"/>：</label>
			<div class="col-sm-9 p-l-0">
				<div class="col-sm-7">
					<input type="text" name="withdrawCode" id="withdrawCode" required class="control-label col-md-12"> 
					<label ng-if="send"  name="sendMessage" class=" control-label text-warning"></label>
					<div class="m-l-15 p-t-10"></div>
				</div>
				<div class="col-sm-3">
					<button id="sendsmsBtn" class="btn btn-primary" type="button" name="sendSms"  ><@spring.message code="dianjihuoqu"/></button>
				</div>
			</div>
		</div>-->
		
		<div class="form-group">
			<div class="col-sm-6 col-sm-offset-3">
				<button type="button" class="btn btn-primary form-control f-s-16" id="submitWithdraw"><@spring.message code="querentixian"/></button>
				<!--<button type="submit" ng-if="isAllowRecharge==0"  ng-if="appSettingPermissions('/exmain/account/rmbwithdraw.do')" class="btn btn-primary form-control f-s-16">{{' B1 '| translate}}{{' A62 '| translate}}{{' A44 '| translate}}</button>-->
			</div>
		</div>
	</form>
	<!---end:{{' A62 '| translate}}{{' A44 '| translate}}表{{' A84 '| translate}}---->
	<dl class="withdraw-info p-t-20">
		<dt class="f-s-16 p-b-10"><@spring.message code="tixianqingxuanzeyinhang"/>：</dt>
		<dd id="ontixian"><@spring.message code="ontixian"/></dd>
		<dd><@spring.message code="tixianshenheshijian"/></dd>
	<@spring.message code="danbitixianmax"/>${maxWithdrawMoneyOneTime} ${languageCode},<@spring.message code="dangtiantixianmax"/>${maxWithdrawMoney} ${languageCode}.
	</dl>
</div>
<!--end page-container --->




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
    </div>



<!-- begin page-container -->
<div class="container-fluid person-con">
	<!-- begin page-header -->
	<div class="row" style="margin-bottom:15px;">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" id="RMBtab">
					<li role="presentation" class="active pull-left">
						<a href="javascript:void(0);"><@spring.message code="tixianjilu"/></a>
					</li>
				</ul>
			</div>
			<!--<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>-->
		</div>
	</div>
	<!-- end page-header -->

	<!-- begin row -->
	<div class="row">
			<!-- begin col-12 -->
			<div class="col-md-12 col-sm-12">
				<form class="form-inline input-daterange clearfix ng-pristine ng-valid">

					<div class="form-group form-labels  col-md-12 col-sm-12" id="status">
						 <label for="" ><@spring.message code="chognzhileixing"/>：</label>
						 <a class="label ng-binding selected" value="0"><@spring.message code="quanbu"/></a>
						 <a class="label ng-binding"  value="1" ><@spring.message code="daishenhe"/></a>
						 <a class="label ng-binding" value="2"><@spring.message code="yitongguo"/></a>
						 <a class="label ng-binding" value="3"><@spring.message code="yifoujue"/></a> &nbsp;&nbsp; 
					</div>
				</form>
			</div>
			<!-- end col-12 -->
	</div>
	<div class="row">
			<div class="col-sm-12">
						
				<table   id="table"
		 	           data-show-refresh="false"
		 	           data-show-columns="false"
		 	           data-show-export="false"
		 	           data-search="false"
		 	           data-detail-view="false"
		 	           data-minimum-count-columns="2"
		 	           data-pagination="true"
		 	           data-id-field="id"
		 	           data-page-list="[10, 25, 50, 100, ALL]"
		 	           data-show-footer="false"  
		 	           data-side-pagination="server"
		 	           >
		 	    </table>
			</div>
			<!---->
	</div>
	<!-- end row -->

	<div class="row text-center">
		<hry-page conf="pageConf"></hry-page>
	</div>

</div>

<#include "/base/base.ftl">
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
seajs.use(["js/front/user/rmbwithdraw","js/base/secondvail","js/base/firstvail"],function(rmb,mg){
	rmb.init();
	rmb.sendsms();
	mg.mgvail("rmbwithdraw");
	
});
</script>
