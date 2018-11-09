<html lang="en">

<head>
	<#include "/base/base.ftl">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="" name="description">
		<meta content="" name="author">
		<@HryTopOrFooter url="base/title.ftl" />
		<!-- ================== BEGIN BASE CSS STYLE =============== -->
		<link href="${ctx}/static/${version}/lib/exstatic/css/bootstrap.min.css" rel="stylesheet">
		<link href="${ctx}/static/${version}/lib/exstatic/css/font-awesome.min.css" rel="stylesheet">
		<link href="${ctx}/static/${version}/lib/exstatic/css/animate.min.css" rel="stylesheet">
		<link href="${ctx}/static/${version}/lib/exstatic/css/web-responsive.min.css" rel="stylesheet">
		<link href="${ctx}/static/${version}/lib/exstatic/css/mdefault.css" id="theme" rel="stylesheet">
		<link href="${ctx}/static/${version}/lib/exstatic/css/style.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="${ctx}/static/${version}/css/c2c/module.base.css">
		<link rel="stylesheet" href="${ctx}/static/${version}/css/c2c/module.common.css">
		<link rel="stylesheet" href="${ctx}/static/${version}/css/c2c/module.user.css">
		<link rel="stylesheet" href="${ctx}/static/${version}/css/c2c/safe.css">
		<link rel="stylesheet" href="${ctx}/static/${version}/css/iconfont/iconfont.css">
		<link href="${ctx}/static/${version}/css/common/global.css" rel="stylesheet" />

</head>

<body>
	<#include "/base/user/header.ftl">
		<input type="hidden" id="username" value="${user.username!}" />
		<input type="hidden" id="coinCode" value="${activeCoin!}" />
        <input type="hidden" id="accountPassWordflag" value="${user.accountPassWord!}" />
        <input type="hidden" id="phoneState" value="${user.phoneState!}" />
		<!-- 中间切换区域 -->
		<div id="content" class="content" style="margin: 0px; min-height: 800px;">

			<!--页面中部内容开始-->
			<div class="bk-onekey financen " style="padding-top:85px;">
				<div class="container">
					<div class="finance-rd" style="width:100%; margin-left:0;">
						<div class="bk-tabList">
							<div class="bk-c2c-nav bk-band clearfix">
								<#if coinList ??>
									<#list coinList as coin>
										<#if activeCoin==coin>
											<a style="margin-right:2px;" class="active" href="${ctx}/c2c/${coin}">${coin}</a>
											<#else>
												<a style="margin-right:2px;" href="${ctx}/c2c/${coin}">${coin}</a>
										</#if>
									</#list>
								</#if>
							</div>
							<!-- ---- -->
							<div class="c2c-right">
								<div class="bk-tabList-bd bk-onekey-form bk-c2c-contlist">
									<div class="bk-tabList-list-usdt active">
										<div class="no-usdt-w ">
										</div>
										<div class="bk-c2c-bd">
											<div class="row">
												<div class="col-xs-12">
													<div class="row">
														<div class="col-xs-6 buy">
															<h3 class="b-title"><@spring.message code="buy"/> ${activeCoin!}</h3>
															<div id="buyDefaultForm">
																<div class="form-group has-feedback form-subline">
																	<label for="buyUnitPrice" class="control-label"><span class="buyDefaultLabel"><@spring.message code="buyprice"/></span> (￥)</label>
																	<div class="input-group">
																		<input type="hidden" value="${c2cBuyPrice!0}">
																		<input type="text" id="buyUnitPrice" name="buyUnitPrice" readonly="readonly" value="${c2cBuyPrice!0}" class="form-control form-second"></div>
																</div>
																<div class="form-group has-feedback form-subline">
																	<label for="buyNumber" class="control-label"><@spring.message code="buycount"/>(${activeCoin!})</label>
																	<div class="input-group">
																		<input type="text" id="buyNumber" name="buyNumber" class="form-control form-second">
																	</div>
																</div>
																<div id="buyfinish">
																	<@spring.message code="xuyao" /> <span>0.00</span> CNY
																</div>
																<div class="form-group"><button id="buyBtn" type="button" class="btn btn-danger btn-block ft16">
            											<@spring.message code="lijimairu"/>
                            </button></div>
															</div>
														</div>

														<div class="col-xs-6 sell">
															<h3 class="b-title"><@spring.message code="sell"/> ${activeCoin!}</h3>
															<div id="sellDefaultForm">
																<div class="form-group has-feedback form-subline">
																	<label for="buyUnitPrice" class="control-label"><span class="sellDefaultLabel"><@spring.message code="sellprice"/></span> (￥)
                           										    </label>
																	<div class="input-group"><input type="hidden" value="${c2cSellPrice!0}">
																		<input type="text" id="sellUnitPrice" name="sellUnitPrice" readonly="readonly" value="${c2cSellPrice!0}" class="form-control form-second"></div>
																</div>
																<div class="form-group has-feedback form-subline">
																	<label for="sellNumber" class="control-label"><@spring.message code="sellcount"/> (${activeCoin!})</label>
																	<div class="input-group">
																		<input type="text" id="sellNumber" name="sellNumber" class="form-control form-second"></div>
																</div>
																<div id="sellfinish">
																	<@spring.message code="kede" /> <span>0.00</span> CNY
																</div>
																<div class="form-group">
																	<button id="sellBtn" type="button" class="btn btn-second btn-block ft16">
														          <@spring.message code="lijimaichu"/>
														            </button>
														       </div>
															</div>
														</div>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-xs-6">
													<div class="exchangetlist" id="usdtcnybuylist">
														<div class="shd">
															<span><@spring.message code="shanghu"/></span>
															<b><@spring.message code="chengjiaoshuliang"/></b>
															<span class="typeshow"><@spring.message code="type"/></span>
															<a>
																<@spring.message code="state" />
															</a>
														</div>
														<div class="bd">
															<div class="tempWrap" style="overflow:hidden; position:relative; height:64px">
																<ul style="height: 1248px; position: relative; padding: 0px; margin: 0px; top: -32px;">
																	<li style="height: 32px;"></li>
																	<#if buyList??>
																		<#list buyList as o>
																			<li style="height: 32px;"><span><i class="fa fa-user fa-fw"></i>${o.userName}</span><b>${o.transactionCount}&nbsp;&nbsp;${o.coinCode}</b><span class="typeshow"><@spring.message code="buy"/></span>
																				<a>
																					<@spring.message code="jiaoyiwancheng" />
																				</a>
																			</li>
																		</#list>
																	</#if>
																</ul>
															</div>
														</div>

													</div>



												</div>

												<div class="col-xs-6">
													<div class="exchangetlist" id="usdtcnyselllist">
														<div class="shd">
															<span><@spring.message code="shanghu"/></span>
															<b><@spring.message code="chengjiaoshuliang"/></b>
															<span class="typeshow"><@spring.message code="type"/></span>
															<a>
																<@spring.message code="state" />
															</a>
														</div>
														<div class="bd">
															<ul style="height: 1248px; position: relative; padding: 0px; margin: 0px; top: -32px;">
																<li style="height: 32px;"></li>
																<#if sellList??>
																	<#list sellList as o>
																		<li style="height: 32px;"><span><i class="fa fa-user fa-fw"></i>${o.userName}</span><b>${o.transactionCount}&nbsp;&nbsp;${o.coinCode}</b><span class="typeshow"><@spring.message code="sell"/></span>
																			<a>
																				<@spring.message code="jiaoyiwancheng" />
																			</a>
																		</li>
																	</#list>
																</#if>
															</ul>
														</div>

													</div>
												</div>

											</div>


										</div>


									</div>

								</div>

								<div class="usdtnote">
									<div class="notecont">
										<@spring.message code="c2cshuoming" />
									</div>
								</div>

								<div class="bk-pageTit" id="exchangeRecord">
									<h4 class="pull-left"><@spring.message code="zuijinduihuanjilu"/>
										<a class="btn card-add" role="button" href="${ctx}/user/center"><i class="iconfont icon-tianjialeimu"></i><@spring.message code="more"/></a>
									</h4>
									<div class="clearfix"></div>
									<div class="table-responsive ">
										<table id="billDetail" class="table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th width="10%">
														<@spring.message code="time" />
													</th>
													<th width="10%" style="text-align:left;">
														<@spring.message code="type" />
													</th>
													<th width="10%" style="text-align:left;">
														<@spring.message code="shuliang" />
													</th>
													<th width="10%" style="text-align:left;">
														<@spring.message code="price" />(￥)</th>
													<th width="11%" style="text-align:left;">
														<@spring.message code="totalprice" />(￥)</th>
													<th width="11%" style="text-align:center;">
														<@spring.message code="state" />
													</th>
													<th width="11%" style="text-align:center;"><@spring.message code="dingdanxinxi"/></th>
											</thead>
											<tbody>
												<#if orderList?? && (orderList?size> 0)>
													<#list orderList as o>
														<tr class="wait" style="font-size:12px;">
															<td>${o.createTime}</td>
															<td style="text-align:left;">${o.coinCode}
																<#if o.transactionType==1>(买入)
																	<#else>(卖出)</#if>
															</td>
															<td style="text-align:left;">${o.transactionCount}</td>
															<td style="text-align:left;">${o.transactionPrice}</td>
															<td style="text-align:left;">${o.transactionMoney}</td>
															<td style="text-align:center;">
																<#if o.status==1>
																	待处理
																	<#elseif o.status==2>
																		交易成功
																		<#elseif o.status==3>
																			交易取消
																</#if>
															</td>
															<td style="text-align:center;">
																<a href="javascript:void(0)" transactionnum="${o.transactionNum}">
																	<font color="red">
																		<#if o.status==1 || o.status==2>
																		<#if o.transactionType==1>付款信息</#if>
																	</#if>
																	</font>
																</a>
															</td>

														</tr>
													</#list>
													<#else>
														<tr>
															<td colspan="6">
																<div class="bk-norecord">
																	<p><i class="bk-ico info"></i></p>
																</div>
															</td>
														</tr>
												</#if>
												<tr></tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<!-- c2c right content end -->
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--页面中部内容结束-->


		</div>

		<!-- 底部锚点 -->
		<a href="javascript:void(0);" class="btn btn-icon btn-circle btn-success btn-scroll-to-top fade" data-click="scroll-top"><i class="fa fa-angle-up"></i></a>
		</div>
		<div class="page-footer col-md-12 col-sm-12 ng-scope">
			<@HryTopOrFooter url="base/footer.ftl" />
		</div>
</body>



<div id='closediv' class="hide">
	<h3>是否确认关闭交易？</h3>
	<textarea id="closeRemark" style="width:80%;" rows="3" cols="20">请填写关闭原因</textarea>
</div>

<div id='faildiv' class="hide">
	<h3>是否确认交易失败？</h3>
	<textarea id="failRemark" style="width:80%;" rows="3" cols="20">请填写失败原因</textarea>
</div>

</html>

 <div id="c2c_yanzheng_Layout" class="verifyLayout">
     <div class="dialog_bg"></div>
     <div class="main dialogcon" style="padding-top: 40px;width: 500px;">
         <!--<a href="/"><img ng-src="/resources/img/logo-cn.svg" class="icon-logo" src="/resources/img/logo-cn.svg"></a>-->
         <div class="dialog-close" id="c2c_dialog-close">×</div>
         <!--banner -->
         <div class="verify middle ng-scope" ng-controller="googleVerifyCtr">
             <!-- 谷歌二次验证 -->
             <!-- ngIf: verifyType==1 -->
             <!-- 手机二次验证 -->
             <!-- ngIf: verifyType==2 -->
             <form class="verify-form ng-scope ng-pristine ng-valid" action="/user/login.html" method="post"
                   id="mobile-form">
                 <div class="verify-title"><span class="" style="font-size: 18px"><@spring.message code="yanzhengxinxi"/></span></div>
                 <input type="text" readonly="" name="email" ng-model="email" style="display:none;"
                        class="ng-pristine ng-valid">
                 <input type="hidden" name="operationType" value="mobile">
                 <div class="filed">
                         <#if user.accountPassWord!=null>
                         <input placeholder="<@spring.message code='jiaoyimima'/>" style="width: 100%;margin-bottom: 20px" type="password" id="accountPassWord" name="accountPassWord" class="ipt">
						 </#if>
                         <#if user.phoneState == 1>
							 <div style="width: 100%">
								 <input placeholder="<@spring.message code='duanxinyanzhengma'/>" type="text" id="verifyCode" name="verifyCode" class="ipt" style="width: 70%;float: left">
								 <button type="button" style="float: right" id="sendBtn" class="btn btn-grey"><@spring.message
								 code="fasongyanzhengma"/>
								 </button>
							 </div>
						 </#if>
                     <p class="Validform_checktip f-nomargin f-left Validform_wrong"></p>
                 </div>
                 <div class="filed">
                     <input style="margin-top: 30px;background: #2ad593" id="c2c_Validform_check" type="button"  value=<@spring.message code="queding"/> class="btn btn-orange btn-block
                            phone" class="mobile-btn">
                 </div>
             </form>
             <!-- end ngIf: verifyType==2 -->

             <!-- 谷歌或手机二次验证 -->
             <!-- ngIf: verifyType==0 -->
         </div>
     </div>
 </div>



<script type="text/javascript" src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/second.css">
<script type="text/javascript">
	seajs.config({
		base: "${ctx}/static/${version}",
		alias: {
			<!-- 基础框架JS -->
			"jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
			"jqueryForm": "lib/jqueryForm/jquery.form.js",
			<!-- layer -->
			"layer": "lib/layer/layer.js",
			<!-- 自定义JS -->
			"base": "js/base/base.js"
		},
		preload: ['jquery', 'jqueryForm', 'layer'],
		map: [
			['.js', '.js?v=${t}'] //映射规则
		]
	});

	seajs.use(["js/front/main_news", "js/c2c", "js/i18n_base"], function(m, c2c,ba) {

		m.init();

		c2c.init();

		c2c.getdeatil();

	});
</script>
