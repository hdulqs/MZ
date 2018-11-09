<#import "/base/spring.ftl" as spring/>
<style>
    i.form-control-feedback {
        right: 10px;
    }
</style>
<div class="container-fluid person-con">

    <!-- 虚拟币提现 start-->
    <div class="row" style="margin-bottom:15px;">
        <div class="panel_wrap_head wrap_head">
            <div class="">
                <ul class="wrap_tabs" role="tablist" id="RMBtab">
                    <li role="presentation" class="active pull-left">
                        <a href="javascript:void(0);"><@spring.message code="xunibitixian"/></a>
                    </li>

                </ul>
            </div>
        </div>
    </div>
    <!-- 虚拟币提现 end -->

    <!---start:{{' A11 '| translate}}{{' Address '| translate}}---->
    <user-coin-select coincode="{{coinCode}}" url="#/user/account/btcget" accountshow="false"></user-coin-select>

    <input type="hidden" id="keepDecimalForCoin" value="${keepDecimalForCoin!8}"></input>
    <form class="form-horizontal withdraw-form p-20" name="withdraw" id="withdraw">

        <ul class="withdraw-count">
            <li>
                <select id="coinSelect" class="form-control" style="border:none;outline:none;height:64px;">
                    <#list list as list>
                    <option value="${list.id}HURONGSPLIT${list.hotMoney}HURONGSPLIT${list.coldMoney}HURONGSPLIT${list.publicKey}HURONGSPLIT${list.coinCode}HURONGSPLIT${list.paceFeeRate}HURONGSPLIT${list.leastPaceNum}HURONGSPLIT${list.oneDayPaceNum}HURONGSPLIT${list.keepDecimalForCoin}">
                        ${list.coinName}
                    </option>
                </#list>
                </select>
            </li>
            <li>
                <@spring.message code="keyong"/><span id="coincode1">${coincode}</span> : <input type="text"
                                                                                                 id="availableCTC"
                                                                                                 value="${firstHot}"
                                                                                                 style="border:none;background:#fff;margin-bottom:0;"
                                                                                                 readonly="readonly"/>
            </li>
            <li>
                <@spring.message code="dongjie"/><span id="coincode2">${coincode}</span> : <input type="text"
                                                                                                  id="frozenCTC"
                                                                                                  value="${firstCold}"
                                                                                                  style="border:none;background:#fff;margin-bottom:0;"
                                                                                                  readonly="readonly"/>
            <li>
                <input type="text" style="display:none" id="coinType" name="coinType" value="${coincode}"
                       readonly="readonly"/>
                <input type="text" style="display:none" id="currencyType" name="currencyType" value="${currencyType}"
                       readonly="readonly"/>
        </ul>
        <div class="form-group" style="margin-top:30px;">
            <input type="hidden" id="remark"/>
            <label class="col-sm-3 control-label"><@spring.message code="qianbaodizhi"/>：</label>
            <div class="col-sm-6">
                <select name="btcKey" ng-model="formData.btcKey" class="form-control" id="btcKey">
                    <#list list2 as list>
                    <option remark="${list.remark}" value="${list.publicKey}">${list.publicKey}</option>
                </#list>
                </select>
                <a href="javascript:void(0);" id="addWaccount" class="collection-item"><@spring.message
                    code="tianjiatixianzhanghu"/></a>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label"><@spring.message code="ketixianshuliang"/>：</label>
            <div class="col-sm-6">
                <span class="RMBmoney f-s-18 maxv" id="firstHotspan">${firstHot}</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><@spring.message code="tixianshuliang"/>：</label>
            <div class="col-sm-6">
                <label class="decimalCion hide">{{decimalCion}}</label>
                <input type="text" name="btcNum" id="inputNumWit" require class="form-control" autocomplete="off">
                <span style="color:red" id="btcNum_message"></span>
            </div>
        </div>


        <div id="shouxufeiId"></div>

        <!--------------------->

        <!--<div class="form-group">
							<label class="col-sm-3 control-label">邮箱账号：</label>
							<div class="col-sm-6 p-l-0"><input type="hidden" id="username" value="${user.mobile}" />
								<label class="col-sm-2 control-label f-s-18">${user.mobile}</label>
							</div>
						</div>-->

        <!--<div class="form-group">
							<label class="col-sm-3 control-label"><@spring.message code="duanxinyanzhengma"/>：</label>
							<div class="col-sm-9 p-l-0">
								<div class=" col-sm-7">
									<input type="text" name="withdrawCode" id="withdrawCode" require class="control-label col-md-12 -center">
									<label ng-if="send" name="sendMessage" class=" control-label text-warning"></label>
									<div ng-if="send" class="m-l-15 p-t-10"></div>
								</div>
								<div class="col-sm-3">
									<button class="btn btn-primary  " type="button" id="sendsmsBtn"><@spring.message code="dianjihuoqu"/></button>
								</div>
							</div>
						</div>
						-->
        <div class="form-group">
            <div class="col-sm-6 col-sm-offset-3">
                <button type="button" class="btn btn-primary form-control f-s-16" id="oktx"><@spring.message
                    code="querentixian"/>
                </button>
            </div>
        </div>

    </form>
    <!---end:{{' A62 '| translate}}{{' A44 '| translate}}表{{' A84 '| translate}}---->
    <!--注意事项start-->
    <dl class="withdraw-info p-t-20">
        <dt class="f-s-16 p-b-10"><@spring.message code="zhuyishixiang"/>：</dt>
        <dd style="display:none;"><span id="paceFeeRate"> ${paceFeeRate}</span></dd>
        <dd>1. <@spring.message code="zhuanbitiaojian"/> <span id="leastPaceNum">${leastPaceNum}</span> <span
                id="coinCode1">${coincode}</span>，<@spring.message code="zhuanbitiaojian2"/> <span id="oneDayPaceNum">${oneDayPaceNum}</span>
            <span id="coinCode2">${coincode}</span><@spring.message code="/day"/>
        </dd>
        <dd>2. <@spring.message code="zhuanbijinzhi1"/> <span id="coinCode3">${coincode}</span> <@spring.message
            code="zhuanbijinzhi2"/> <span id="coinCode4">${coincode}</span>
            <@spring.message code="zhuanbijinzhi3"/>
            <span id="coinCode5">${coincode}</span>
            <@spring.message code="zhuanbijinzhi4"/>
            <span id="coinCode6">${coincode}</span>
            <@spring.message code="zhuanbijinzhi5"/>
        </dd>
    </dl>
    <!--注意事项end-->
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
                        <a href=""><@spring.message code="tixianjilu"/></a>
                    </li>
                </ul>
            </div>
            <!--<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>-->
        </div>
    </div>
    <!-- end page-header -->

    <!--手机认证 start-->
    <div class="verifyLayout">
        <div class="dialog_bg"></div>
        <div class="main dialogcon" style="padding-top: 40px;width: 500px;">
            <!--<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
            <div class="dialog-close">×</div>
            <!--banner -->
            <div class="verify middle ng-scope" ng-controller="googleVerifyCtr">
                <!-- 谷歌二次验证 -->
                <!-- ngIf: verifyType==1 -->


                <!-- 手机二次验证 -->
                <!-- ngIf: verifyType==2 -->
                <form class="verify-form ng-scope ng-pristine ng-valid" action="/user/login.html" method="post"
                      id="mobile-form" ng-if="verifyType==2">
                    <div class="verify-title"><span class="" style="font-size: 18px"><@spring.message code="yanzhengxinxi"/></span></div>
                    <input type="text" readonly="" name="email" ng-model="email" style="display:none;"
                           class="ng-pristine ng-valid">
                    <input type="hidden" name="operationType" value="mobile">
                    <div class="filed">
                         <#if user.accountPassWord!=null>
                         <input placeholder="<@spring.message code='jiaoyimima'/>" style="width: 100%;margin-bottom: 20px" type="password" id="accountPassWord" name="accountPassWord" class="ipt">
                         </#if>
                        <div style="width: 100%">
                            <input placeholder="<@spring.message code='duanxinyanzhengma'/>" type="text" id="verifyCode" name="verifyCode" class="ipt" style="width: 70%;float: left">
                            <button type="button" style="float: right" id="sendBtn" class="btn btn-grey"><@spring.message
                                code="fasongyanzhengma"/>
                            </button>
                        </div>
                        <p class="Validform_checktip f-nomargin f-left Validform_wrong"></p>
                    </div>
                    <div class="filed">
                        <input style="margin-top: 30px;" type="button" value=<@spring.message code="queding"/> class="btn btn-orange btn-block
                        phone" class="mobile-btn">
                    </div>
                </form>
                <!-- end ngIf: verifyType==2 -->

                <!-- 谷歌或手机二次验证 -->
                <!-- ngIf: verifyType==0 -->
            </div>
        </div>
    </div>
    <!--手机认证 end-->

    <!--交易密码 start-->
    <!--<div class="verifyLayout">
        <div class="dialog_bg"></div>
        <div class="main dialogcon">
            &lt;!&ndash;<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>&ndash;&gt;
            <div class="dialog-close">×</div>
            &lt;!&ndash;banner &ndash;&gt;
            <div class="verify middle ng-scope" ng-controller="googleVerifyCtr">
                <form class="verify-form ng-scope ng-pristine ng-valid" action="/user/login.html" method="post"
                      id="mobile-form" ng-if="verifyType==2">
                    <div class="verify-title"><span class=""><@spring.message code="jiaoyimimarenzheng"/></span></div>
                    <input type="text" readonly="" name="email" ng-model="email" style="display:none;"
                           class="ng-pristine ng-valid">
                    <input type="hidden" name="operationType" value="mobile">
                    <div class="filed">
                        <input style="width:186px;" type="text" id="verifyCode" name="verifyCode" class="ipt">
                        <p class="Validform_checktip f-nomargin f-left Validform_wrong"></p>
                    </div>
                    <div class="filed">
                        <input type="button" value=<@spring.message code="queding"/> id="tradepwd">
                    </div>
                </form>
            </div>
        </div>
    </div>-->
    <!--交易密码 end-->

    <!--谷歌认证 start-->
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
                        <input type="button" value=<@spring.message code="queding"/> class="btn btn-orange btn-block
                        goog" class="googleVerify-btn">
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
    <!--谷歌认证 end-->



</div>

<!--谷歌认证+手机认证 start -->
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
                        <input type="button" value="<@spring.message code="queding"/>" class="btn btn-orange btn-block
                        googleVerifyb " class="">
                    </div>
                </form>
                <form class="verify-form1" action="" method="post" id="mobile-form">
                    <div class="label f-left"><@spring.message code="duanxinyanzhengma"/></div>
                    <input type="text" readonly="" name="email" style="display:none;" class="">
                    <input type="hidden" name="operationType" value="mobile">
                    <div class="filed">
                        <input style="width:186px;" type="text" id="password" name="verifyCode" class="ipt secondp">
                        <button type="button" id="sendBtn1" class="btn btn-grey"><@spring.message
                            code="fasongyanzhengma"/>
                        </button>
                        <p class="Validform_checktip f-nomargin f-left"></p>
                    </div>
                    <div class="filed">
                        <input type="button" value="<@spring.message code="queding"/>" class="btn btn-orange btn-block
                        mobileb" class="">
                    </div>
                </form>
            </div>
            <!-- end ngIf: verifyType==0 -->
        </div>
    </div>
</div>
<!--谷歌认证+手机认证 end -->

</div>

<!-- begin row -->
<div class="row">
    <!-- begin col-12 -->
    <div class="col-md-12 col-sm-12">
        <form class="form-inline input-daterange clearfix">
            <div class="form-group form-labels  col-md-12 col-sm-12" id="type">
                <label for=""><@spring.message code="zhuangtai"/>：</label>
                <a class="label ng-binding selected" lang="" name="status" value=""><@spring.message code="quanbu"/></a>
                <a class="label" lang="1" value="1"><@spring.message code="dengdai"/></a>
                <a class="label" lang="2" value="2"><@spring.message code="success"/></a>
                <a class="label" lang="3" value="3"><@spring.message code="error"/></a>
            </div>
        </form>
    </div>
    <!-- end col-12 -->
</div>
<div class="row">
    <!-- begin col-3 -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="current">

            <table id="table"
                   data-toolbar="#toolbar"
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
</div>
<input type="hidden" value="${jpCoinCode}" id="jpCoinCode"/>

<#include "/base/base.ftl">
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">

<script type="text/javascript">
    seajs.use(["js/front/user/btcget", "js/i18n_base", "js/base/secondvail", "js/base/firstvail"], function (btc, b, mg) {
        btc.init();
        btc.sendsms();
        mg.mgvail("btcget");
    });
</script>