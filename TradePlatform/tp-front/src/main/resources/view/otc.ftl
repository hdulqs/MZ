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
		<link rel="stylesheet" href="${ctx}/static/${version}/css/otc/safe.css?v=20180518">
		<link rel="stylesheet" href="${ctx}/static/${version}/css/iconfont/iconfont.css">
		<link href="${ctx}/static/${version}/css/common/global.css" rel="stylesheet" />

</head>
<body>
	<#include "/base/user/header.ftl">
		<input type="hidden" id="username" value="${user.username!}" />
        <input type="hidden" style="display: none" id="otcuserid" value="${user.customerId!}" />
		<input type="hidden" id="otccoinCode" value="${activeCoin!}" />
         <input type="hidden" id="otctransactionType" value="buy" />
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
											<a style="margin-right:2px;" class="active" href="${ctx}/otc/${coin}">${coin}</a>
											<#else>
												<a style="margin-right:2px;" href="${ctx}/otc/${coin}">${coin}</a>
										</#if>
									</#list>
								</#if>
							</div>
							<!-- ---- -->
							<div class="c2c-right" style="width: 100%;">
								<div class="bk-tabList-bd bk-onekey-form bk-c2c-contlist" style="padding: 20px 80px 30px !important;">

									<div class="bk-tabList-list-usdt active">
										<div class="no-usdt-w ">
										</div>
										<div class="bk-c2c-bd">
											<div class="row">
												<div class="col-xs-12">
													<div class="row">
														<div class="col-xs-6 buy">
															<h3 class="b-title"><@spring.message code="ReleaseBuy"/> ${activeCoin!}</h3>
															<div id="buyDefaultForm">
																<div class="form-group has-feedback form-subline">
																	<label for="buyUnitPrice" class="control-label"><span class="buyDefaultLabel"><@spring.message code="buyprice"/></span> (￥)</label>
																	<div class="input-group">
																		<input type="hidden" value="${otcminBuyPrice!0}">
																		<input type="text" id="buyUnitPrice" style="background: #fdfdfd " name="buyUnitPrice"  value="${otcminBuyPrice!0}" class="form-control form-second"></div>
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
            											<@spring.message code="ReleaseBuy"/>
                            </button></div>
															</div>
														</div>

														<div class="col-xs-6 sell">
															<h3 class="b-title"><@spring.message code="ReleaseSell"/> ${activeCoin!}</h3>
															<div id="sellDefaultForm">
																<div class="form-group has-feedback form-subline">
																	<label for="buyUnitPrice" class="control-label"><span class="sellDefaultLabel"><@spring.message code="sellprice"/></span> (￥)
                           										    </label>
																	<div class="input-group"><input type="hidden" value="${otcminBuyPrice!0}">
																		<input type="text" id="sellUnitPrice" STYLE="background: #fdfdfd " name="sellUnitPrice"  value="${otcminBuyPrice!0}" class="form-control form-second"></div>
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
														          <@spring.message code="ReleaseSell"/>
														            </button>
														       </div>
															</div>
														</div>
													</div>
												</div>
											</div>

											


										</div>


									</div>

								</div>

								<div class="usdtnote">
									<div class="notecont">
										<@spring.message code="OTC_Prompt" />
									</div>
								</div>

                                <style>
                                    .select_active{
                                        background: #ff5b57 !important;
                                    }
                                    .select_active_1{
                                        background: #3dc18e !important;
                                    }
                                </style>

								<div class="bk-pageTit" id="exchangeRecord">

									<div class="select_btn" style="width: 100%;height: 40px;line-height: 40px;">
										<div style="float:left;color: #61727c;font-size: 18px;padding-left: 10px;"><@spring.message code="shichangguadan"/></div>
										<div id="select_buy" class="select_active" style="cursor:pointer;float:left;margin:3px 0 0 10px;width: 60px;text-align: center;color: #fff;background: #ccc;font-size: 18px;border-radius: 3px;height: 34px;line-height: 34px;"><@spring.message code="mai"/></div>
                                        <div id="select_sell" style="cursor:pointer;float:left;margin:3px 0 0 10px;width: 60px;text-align: center;color: #fff;background: #ccc;font-size: 18px;border-radius: 3px;height: 34px;line-height: 34px;"><@spring.message code="mai1"/></div>
									</div>




									<div class="table_head" style="width:100%;margin:auto;font-size:15px;font-weight: bold;color:#333;">
										<div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="jiaoyileixing"/></div>
										<div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="jiaoyizhonglei"/></div>
										<div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="price"/></div>
										<div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="shuliang(ge)"/></div>
										<div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="money"/></div>
                                        <div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="shangjia"/></div>
										<div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="24hourjiaoyiliang"/></div>
                                        <div style="width:108px;float: left;padding:10px 0;text-align: center;"><@spring.message code="zhifufanshi"/></div>
										<div style="float: left;padding:10px 0;text-align: center;"></div>
									</div>
									<div style="clear:both;"></div>
									<div class="table_scroll_panel" style="width: 100%;">
										<table id="otc_table" cellpadding="0" border="0">
											
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

<div class="mask">
    <div class="dialogOtc">
			<div class="buy">
				<h3 style="text-align: left;padding-left: 22px;color: #333"><@spring.message code="buy"/> ${activeCoin!}</h3>
				<div id="dialogForm">
					<div class="dialog_input">
                        <span style="position: absolute;top: 56px;left: 32px"><@spring.message code="buyprice"/>(￥)</span>
						<div class="">
                            <input id="otcbuytransactionid" type="hidden"">
							<input style="text-align: right;color: #de211d" placeholder="" id="otcbuyUnitPrice" type="text" readonly="readonly" value="" class="form-control form-second"></div>
					</div>
					<div class="dialog_input">
                        <span style="position: absolute;top: 110px;left: 32px"><@spring.message code="buycount"/>(${activeCoin!})</span>
						<div class="">
							<input style="text-align: right;color: #de211d" maxlength="20" placeholder="" type="text" id="otcbuyNumber" name="otcbuyNumber" class="form-control form-second color-red">
						</div>
					</div>
					<div id="otcbuyfinish" style="text-align: left;padding-left: 20px">
						<@spring.message code="xuyao" /> <span class="color-red fonts14">0.00</span> CNY
					</div>

				</div>
			</div>
        <button id="diaConfirm" class="diaConfirm"><@spring.message code="quedingtijiao"/></button><button class="back bgcolor-white"><@spring.message code="guanbi"/></button>
    </div>
</div>

<div class="mask2">
    <div class="dialogOtc">
        <div class="buy">
            <h3 style="text-align: left;padding-left: 22px;color: #333"><@spring.message code="sell"/> ${activeCoin!}</h3>
            <div id="dialogForm">
                <div class="dialog_input">
                    <span style="position: absolute;top: 56px;left: 32px"><@spring.message code="sellprice"/>(￥)</span>
                    <div class="">
                        <input id="otcselltransactionid" type="hidden"">
                        <input style="text-align: right;color: #009688" placeholder="" id="otcsellUnitPrice" type="text" readonly="readonly" value="" class="form-control form-second"></div>
                </div>
                <div class="dialog_input">
                    <span style="position: absolute;top: 110px;left: 32px"><@spring.message code="sellcount"/>(${activeCoin!})</span>
                    <div class="">
                        <input style="text-align: right;color: #009688" maxlength="20" placeholder="" type="text" id="otcsellNumber" name="otcsellNumber" class="form-control form-second">
                    </div>
                </div>
                <div id="otcsellfinish" style="text-align: left;padding-left: 20px">
				     <@spring.message code="kede" /> <span class="color-green fonts14">0.00</span> CNY
                </div>
            </div>
        </div>
        <button id="diaConfirm2" class="diaConfirm2"><@spring.message code="quedingtijiao"/></button><button class="back bgcolor-white"><@spring.message code="guanbi"/></button>
    </div>
</div>

<div id='closediv' class="hide">
	<h3>是否确认关闭交易？</h3>
	<textarea id="closeRemark" style="width:80%;" rows="3" cols="20">请填写关闭原因</textarea>
</div>

<div id='faildiv' class="hide">
	<h3>是否确认交易失败？</h3>
	<textarea id="failRemark" style="width:80%;" rows="3" cols="20">请填写失败原因</textarea>
</div>

</html>



<script type="text/javascript" src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
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

    function otc_btn_buy_fun(e){
        var id = $(e).attr("id");
        var transactionPrice = $(e).attr("transactionPrice");
        $("#otcbuytransactionid").val(id);
        $("#otcbuyUnitPrice").val(transactionPrice);
        $("#otcbuyNumber").val(0);
        $(".mask").show();
    }

    function otc_btn_sold_fun(e){
        var id = $(e).attr("id");
        var transactionPrice = $(e).attr("transactionPrice");
        $("#otcselltransactionid").val(id);
        $("#otcsellUnitPrice").val(transactionPrice);
        $("#otcsellNumber").val(0);
        $(".mask2").show();
    }

	seajs.use(["js/front/main_news", "js/otc","lib/exchange/src/script/module_socket_io","js/i18n_base"], function(m, otc,ao,ba) {
        m.init();
		otc.init();

		otc.getdeatil();

	});
</script>
