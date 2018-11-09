<!DOCTYPE html>
<html>
<head>
<#include "/base/base.ftl">

<@HryTopOrFooter url="base/title.ftl"/>
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">

<link href="${ctx}/static/${version}/lib/exchange/css/page_lightning.css" rel="stylesheet" media="screen">
<script src="${ctx}/static/${version}/lib/exchange/dist/script/jquery.js"></script>
<script src="${ctx}/static/${version}/lib/exchange/dist/script/jqueryi18n.js"></script>
<#include "/base/base_market.ftl">


</head>

<body class="" style="overflow-x: hidden;">
<input type="hidden" name="tokenId" id="tokenId" value="${tokenId}"/>
<!--topbar66-->
<div id="doc_head">
  <@HryTopOrFooter url="base/logo_market.ftl"/>
  <div class="head_login">
			
		<#if user??>
		<@spring.message code="hello"/>，<a href="${ctx}/user/center<#if tokenId??>.do</#if>">${user.username}</a>
<a href="${ctx}/logout">
        <@spring.message code="logout"/></a>
		<#else>
			<a href="${ctx}/login?market=ket" class="marketlogin"><@spring.message code="Login"/></a>
		</#if>
		
	</div>
  <!--<div class="head_nav" id="view_coin_list"> </div>-->
  
  
  <!---coin-select    start--->
  
    <div class="coin-select bk-kType">
        <p class="pro-select-header">
            <span class="pro-coin-txt" id="progess_id"><label class="qingxuanze"></label></span>
            <input type="hidden" name="progess_id">
            <span class="coin-select-btn">
                  <svg class="" width="10" height="10" viewBox="0 0 10 6">
                  <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path>
                  </svg>
              </span>
        </p>
        <div class="all_coin_box dropdown-menu animated" >
         <div class="row"  id="all_coin">
         <!--
		    <div class="col-xs-6 text-center" style="padding-right:0">
			   <p class="mt0 mb0 text-primary"><b>主板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="active">BTC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">LTC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">ETH/CNY</a></li>
	   			<li><a href="#" target="_self" class="">ETC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BTS/CNY</a></li>
			   </ul>
		   </div>
		   <div class="col-xs-6 text-center" style="padding-left:0">
			   <p class="mt0 mb0 text-primary"><b>创新板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="">EOS/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BCC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">QTUM/CNY</a></li>
	   			<li><a href="#" target="_self" class="">HSR/CNY</a></li>
			   </ul>
		   </div>
		   <div class="col-xs-6 text-center" style="padding-left:0">
			   <p class="mt0 mb0 text-primary"><b>创新板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="">EOS/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BCC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">QTUM/CNY</a></li>
	   			<li><a href="#" target="_self" class="">HSR/CNY</a></li>
			   </ul>
		   </div>
		    <div class="col-xs-6 text-center" style="padding-right:0">
			   <p class="mt0 mb0 text-primary"><b>主板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="active">BTC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">LTC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">ETH/CNY</a></li>
	   			<li><a href="#" target="_self" class="">ETC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BTS/CNY</a></li>
	   				<li><a href="#" target="_self" class="">LTC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">ETH/CNY</a></li>
	   			<li><a href="#" target="_self" class="">ETC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BTS/CNY</a></li>
			   </ul>
		   </div>
		   <div class="col-xs-6 text-center" style="padding-left:0">
			   <p class="mt0 mb0 text-primary"><b>创新板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="">EOS/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BCC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">QTUM/CNY</a></li>
	   			<li><a href="#" target="_self" class="">HSR/CNY</a></li>
			   </ul>
		   </div>
		   <div class="col-xs-6 text-center" style="padding-left:0">
			   <p class="mt0 mb0 text-primary"><b>创新板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="">EOS/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BCC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">QTUM/CNY</a></li>
	   			<li><a href="#" target="_self" class="">HSR/CNY</a></li>
			   </ul>
		   </div>
		   <div class="col-xs-6 text-center" style="padding-left:0">
			   <p class="mt0 mb0 text-primary"><b>创新板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="">EOS/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BCC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">QTUM/CNY</a></li>
	   			<li><a href="#" target="_self" class="">HSR/CNY</a></li>
			   </ul>
		   </div>
		   <div class="col-xs-6 text-center" style="padding-left:0">
			   <p class="mt0 mb0 text-primary"><b>创新板</b></p>
			   <ul>
	   			<li><a href="#" target="_self" class="">EOS/CNY</a></li>
	   			<li><a href="#" target="_self" class="">BCC/CNY</a></li>
	   			<li><a href="#" target="_self" class="">QTUM/CNY</a></li>
	   			<li><a href="#" target="_self" class="">HSR/CNY</a></li>
			   </ul>
		   </div>
		   
		  -->
		    
	   </div>
    </div>  
  <!----coin-select   end--->
  
  
  
</div>

<!--/topbar-->
<div id="doc_body">
  <div id="bar" class="">
    <div class="handle"></div>
    <div id="depth" style="height: 0px;">
      <div id="market_info" class="market_info market_info_btc">
        <div class="market_col market_col_1">
          <div class="t_head"> <span class="col_1_2" > <label id="Price" >价格</label>(<label class="CURRENCY_SYMBOL"></label>)</span> <span class="col_3"><label class="NUMBER">数量</label></span> <span class="col_4"><label id="Cumulative">累计</label></span> </div>
          <div class="t_body market_body" style="height: 20px;">
            <div class="fixed" id="market_depth">
             
              <div class="asks" id="view_depth_ask">
                <div class="tr" data-type="sell">  </div>
            
              </div>
             
              <div class="new_price" id="view_new_price">  </div>
              
              
              <div class="bids" id="view_depth_bid">
               
              </div>
            </div>
          </div>
          <div class="t_foot depth_select" id="depth_select">
         <!--    <label class="DEEP">深度</label>
           <span class="cur" data-option="0">0</span>
          <span data-option="1">1</span> 
            <span data-option="2">2</span>
             <span data-option="3">3</span> 
             <span data-option="4">4</span>
              <span data-option="5">5</span> 
             -->
           </div>
        </div>
        <div class="market_col market_col_2">
          <div class="t_head"> <span class="col_1 align_left" id="Time" >时间</span> <span class="col_2"><label class="PRICE">价格</label>(<lable class="CURRENCY_SYMBOL"></lable>)</span> <span class="col_3"><label class="NUMBER">数量</label></span> </div>
          <div class="t_body" id="view_trades">
           
          </div>
        </div>
      </div>
    </div>
   
   <!--右侧交易-->
      <div id="trade" <#if user!=null>  <#else> class="hide"  </#if>>
          <div class="trade_head">
              <ul class="tabs">

                  <li class="cur" id="TR04">限价交易</li>
                 <#if marketPrice="true">  <li id="TR05">市价交易</li> </#if>
              </ul>
              <div class="panel_trigger"></div>
          </div>
          <div class="trade_body">
              <div id="balance" class="balance close" data-refresh="1" tabindex="0">
                  <div class="bar">
                            <span>
                               <label class="TR06"> 可用</label> <b class="font_16 cny_cny_available">&#45;&#45;.&#45;&#45;</b> <lable class="CURRENCY_STR"></lable>
                            </span>
                            <span>
                               <label class="TR06"> 可用</label> <b class="font_16 cny_btc_available">&#45;&#45;.&#45;&#45;--</b> <lable class="symbol"></lable>
                            </span>
                      <!-- <i ><label class="TR07">资产详情</label>>></i> -->
                  </div>
                  <!-- <div class="info">
                      <p>
                          <em class="TR06">可用</em>
                          <i class="icon_cny"></i><span class="cny_cny_available">&#45;&#45;.&#45;&#45;</span>
                          <i class="icon_btc"></i><span class="cny_btc_available">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          <i class="icon_ltc"></i><span class="cny_ltc_available">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          <i class="icon_dsc"></i><span class="cny_dsc_available">&#45;&#45;.&#45;&#45;&#45;&#45;</span>

                      </p>
                      <p>
                          <em class="Freeze">冻结</em>
                          <i class="icon_cny"></i><span class="cny_cny_frozen">&#45;&#45;.&#45;&#45;</span>
                          <i class="icon_btc"></i><span class="cny_btc_frozen">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          <i class="icon_ltc"></i><span class="cny_ltc_frozen">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          <i class="icon_dsc"></i><span class="cny_dsc_frozen">&#45;&#45;.&#45;&#45;&#45;&#45;</span>

                      </p>
                      <p>
                          <em class="Lever">杠杆</em>
                          <i class="icon_cny"></i><span class="cny_cny_loan">&#45;&#45;.&#45;&#45;</span>
                          <i class="icon_btc"></i><span class="cny_btc_loan">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          <i class="icon_ltc"></i><span class="cny_ltc_loan">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          <i class="icon_dsc"></i><span class="cny_dsc_loan">&#45;&#45;.&#45;&#45;&#45;&#45;</span>
                          
                      </p>
                  </div> -->
                  <input type="hidden" name="_csrf" value="" />
              </div>


              <div class="panel limit_panel" data-order-type="PlaceOrder" data-panel-type="Limited" style="">

                  <div class="panel_col">

                      <form action="" class="form_trade" data-trade-type="buy">
                          <input type="password" class="invisible"/>
                          <input type="hidden" name="coin_type" value="cny_btc"/>
                          <input type="hidden" name="order_type" value="PlaceOrder">
                          <input type="hidden" name="status" value="0">
                          <input type="hidden" name="source" value="1">
                         <input type="hidden" name="type" value="1">
                         <input type="hidden" name="entrustWay" value="1">
                         <input type="hidden" name="entrustSum" value="">
                          
                          
                          <div class="credit hide">
                              <div class="float_right switch_buy bind_tips hide" data-tips="开启“一键杠杆”功能，可委托超过可用额度的杠杆下单，使用杠杆额度部分需收取手续费，费率保持不变。请注意杠杆风险《杠杆交易风险声明》">
                                  <input type="checkbox" class="switch loan_switch_cny_cny " name="loan_switch" data-loan-state="0" disabled>
                              </div>
                             <label class="TR08">可用杠杆额度</label> <a> <b class="font_buy loan_cny_cny">&#45;.&#45;&#45;</b> <lable class="CURRENCY_STR"></lable></a>
                          </div>
                          <label class="group">
                              <input type="text" id="buy_price" class="input_text " name="price" autocomplete="off" maxlength="20"
                                     data-type="num_range"
                                     data-min="0"
                                     data-max="100000000"
                                     data-msg-error="买入价格式不正确,买入价不能低于@@,买入价过高"
                                     data-msg-null="请输入买入价"/>

                              <span class="unit"><label class="TR21">买入价</label> <lable class="CURRENCY_STR"></lable><!--/<span class="symbol"></span>--></span>
                          </label>
                          <div class="range range_buy" id="range_buy"></div>
                          <label class="group">
                              <input type="text"  id="buy_amount" class="input_text minimum" name="amount" autocomplete="off" maxlength="20"
                                     data-type="num_range"
                                     data-min="0.01"
                                     data-max="100000000"
                                     data-msg-error="买入量格式不正确,买入量不能小于@@,买入量不能大于@@"
                                     data-msg-null="请输入买入量"/>
                              <span class="unit"><label class="BuyVol">买入量 </label> <span class="symbol"></span></span>
                                <#--<span class="max_amount">
                                   <label class="TR25"> 最大买入量 </label> <b class="max_amount_num">0.0000</b>
                                </span>-->
                          </label>
                          <div class="trade_amount">
                              <p><b class="float_right transaction_amount" id="buyb">0.00</b><label class="TR09">交易额</label><lable class="CURRENCY_STR"></lable></p>
                              <input type="text" class="hide buyrate">
                              <input type="text" class="hide keepDecimalForCoin">
                              <input type="text" class="hide keepDecimalForCurrency">
                              <p style="color: rgb(112, 112, 112);"><label class="TR12">买入手续费</label><span class="hidden"></span><span class=" bind_tips buyRate" style="margin-top: 0;" data-tips="当前手续费为%，请仔细确认后再操作。">[?]</span></p>
                              <p class="loan_amount_wrap hide"><b class="float_right loan_amount" data-amount="0">0.00</b>使用杠杆额度 <lable class="CURRENCY_STR"></lable></p>
                          </div>
                          <div class="trade_msg"></div>
                          <div class="trade_button">
                              <button type="submit" id="buysubmit" class="btn btn_buy" data-msg-submit="买入,正在买入,正在申请杠杆,..."><label class="TR10"></label><lable class="symbol"></lable></button>
                          </div>
                      </form>
                  </div>
                  <div class="panel_col">

                      <form action="" class="form_trade" data-coin="cny_btc" data-trade-type="sell">
                          
                          <input type="password" class="invisible"/>
                          <input type="hidden" name="coin_type" value="cny_btc"/>
                          <input type="hidden" name="order_type" value="PlaceOrder">
                          <input type="hidden" name="status" value="0">
                          <input type="hidden" name="source" value="1">
                         <input type="hidden" name="type" value="2">
                         <input type="hidden" name="entrustWay" value="1">
                         <input type="hidden" name="entrustSum" value="">
                          
                          <div class="credit hide">
                              <div class="float_right switch_sell bind_tips hide" data-tips="开启“一键杠杆”功能，可委托超过可用额度的杠杆下单，使用杠杆额度部分需收取手续费，费率保持不变。请注意杠杆风险《杠杆交易风险声明》" data-tips-position="right">
                                  <input type="checkbox" class="switch loan_switch_cny_btc" name="loan_switch" data-loan-state="0" disabled>
                              </div>
                              <label class="TR08">可用杠杆额度</label><a><b class="font_sell loan_cny_btc">&#45;.&#45;&#45;</b><span class="symbol"></span></a>
                          </div>
                          <label class="group">
                              <input type="text" id="sell_price" class="input_text " name="price" maxlength="10" autocomplete="off"
                                     data-type="num_range"
                                     data-min="0"
                                     data-max="100000000"
                                     data-msg-error="请输入正确的卖出价,卖出价不能低于@@,卖出价过高"
                                     data-msg-null="请输入卖出价"/>

                              <span class="unit"><label class="TR22">卖出价</label> <lable class="CURRENCY_STR"></lable><!--/<span class="symbol"></span>--></span>
                          </label>
                          <div class="range range_sell" id="range_sell"></div>
                          <label class="group">
                              <input type="text" id="sell_amount" class="input_text minimum" name="amount" maxlength="10" autocomplete="off"
                                     data-type="num_range"
                                     data-min="0.01"
                                     data-max="100000000"
                                     data-msg-error="请输入正确的卖出量,卖出量不能小于@@,卖出量不能大于@@"
                                     data-msg-null="请输入卖出量"/>

                              <span class="unit"><label class="TR17">卖出量</label> <span class="symbol"></span></span>
                                <#--<span class="max_amount">
                                    <label class="TR26">最大卖出量</label> <b class="max_amount_num">0.0000</b>
                                </span>-->
                          </label>
                          <div class="trade_amount">
                              <p><b class="float_right transaction_amount" id="sellb">0.00</b><label class="TR09">交易额</label> <lable class="CURRENCY_STR"></lable></p>
                              <input type="text" class="hide sellrate">
                              <p style="color: rgb(112, 112, 112);"><label class="TR13">卖出手续费</label><span class="hidden"></span><span class=" bind_tips sellRate" data-tips-position="right" style="margin-top: 0;" data-tips="当前手续费率为%，请仔细确认后再操作。">[?]</span></p>
                              <p class="loan_amount_wrap hide"><b class="float_right loan_amount" data-amount="0">0.0000</b>使用杠杆额度 <span class="symbol"></span></p>
                          </div>
                          <div class="trade_msg"></div>

                          <div class="trade_button">
                              <button type="submit" id="sellsubmit" class="btn btn_sell" data-msg-submit="卖出,正在卖出,正在申请杠杆,..."><label class="TR11"></label><lable class="symbol"></lable></button>
                          </div>
                      </form>
                  </div>

              </div>

              <div class="panel market_panel" data-order-type="PlaceMarketOrder" data-panel-type="PlaceMarket" style="display: none">
                  <input type="hidden" name="order_type" value="PlaceMarketOrder">
                  <div class="panel_col">

                      <form action="" class="form_trade" data-trade-type="buy">
                          <input type="password" class="invisible"/>
                          <input type="hidden" name="coin_type" value="cny_btc"/>
                          <input type="hidden" name="order_type" value="PlaceMarketOrder">
                          <input type="hidden" name="status" value="0">
                          <input type="hidden" name="source" value="1">
                          <input type="hidden" name="type" value="1">
                          <input type="hidden" name="entrustWay" value="2">
                          <input type="hidden" name="entrustPrice" value="">
                          <input type="hidden" name="entrustCount" value="">
                          
                          <div class="credit hide">
                              <div class="float_right switch_buy bind_tips hide" data-tips="开启“一键杠杆”功能，可委托超过可用额度的杠杆下单，使用杠杆额度部分需收取手续费，费率保持不变。请注意杠杆风险《杠杆交易风险声明》">
                                  <input type="checkbox" class="switch loan_switch_cny_cny" name="loan_switch" data-loan-state="0" disabled>
                              </div>
                             <label class="TR08">可用杠杆额度</label>   <a> <b class="font_buy loan_cny_cny">&#45;.&#45;&#45;</b><lable class="CURRENCY_STR"></lable></a>
                          </div>

                          <div class="mp_price">
                              <span class="float_right bind_tips" data-tips="当使用市价买入时，系统会根据您预留的金额在市场上从低到高进行扫单，直至金额用完为止。">[?]</span>
                             <label class="TR19"> 以市场上最优价格买入</label>
                          </div>
                          <div class="range range_buy" id="range_buy_mp"></div>
                          <label class="group">
                              <input type="text" class="input_text minprice" name="market_transaction_price" placeholder="" maxlength="10" autocomplete="off"
                                     data-type="num_range" data-min="1" data-max="99999999"
                                     data-msg-error="交易额格式不正确,交易额不能小于@@,交易金额不能大于最大值@@" data-msg-null="请输入交易额" />
                              <span class="unit"><label class="TR09">交易额</label> <lable class="CURRENCY_STR"></lable></span>
                                <span class="max_amount">
                                   <label class="TR27"></label>  <b class="max_amount_num">0.00</b>
                                </span>
                          </label>

                          <div class="trade_amount">
                              <p class="loan_amount_wrap hide"><b class="float_right loan_amount" data-amount="0">0.00</b>使用杠杆额度 <lable class="CURRENCY_STR"></lable></p>
                              <p style="color: rgb(112, 112, 112);"><label class="TR12">买入手续费3333</label><span class="float_right bind_tips buyRate" style="margin-top: 0;" data-tips="当前手续费率为%，请仔细确认后再操作。">[?]</span></p>
                          </div>
                          <div class="trade_msg"></div>

                          <div class="trade_button">
                              <button type="submit" class="btn btn_buy" data-msg-submit="买入,正在买入,正在申请杠杆,..."><label class="TR10"></label><lable class="symbol"></lable></button>
                          </div>
                      </form>
                  </div>
                  <div class="panel_col">

                      <form action="" class="form_trade" data-coin="cny_btc" data-trade-type="sell">
                          <input type="password" class="invisible"/>
                          <input type="hidden" name="coin_type" value="cny_btc"/>
                          <input type="hidden" name="order_type" value="PlaceMarketOrder">
                         
                          <input type="hidden" name="status" value="0">
                          <input type="hidden" name="source" value="1">
                         <input type="hidden" name="type" value="2">
                         <input type="hidden" name="entrustWay" value="2">
                         <input type="hidden" name="entrustPrice" value="">
                         <input type="hidden" name="entrustSum" value="">
                         
                          <div class="credit hide">
                              <div class="float_right switch_sell bind_tips hide" data-tips="开启“一键杠杆”功能，可委托超过可用额度的杠杆下单，使用杠杆额度部分需收取手续费，费率保持不变。请注意杠杆风险《杠杆交易风险声明》" data-tips-position="right">
                                  <input type="checkbox" class="switch loan_switch_cny_btc" name="loan_switch" data-loan-state="0" disabled>
                              </div>
                             <label class="TR08">可用杠杆额度</label> <a><b class="font_sell loan_cny_btc">&#45;.&#45;&#45;</b><span class="symbol"></span></a>
                          </div>

                          <div class="mp_price">
                              <span class="float_right bind_tips" data-tips="当使用市价卖出时，系统会根据您预留的数量在市场上从高到低进行扫单，直至货币卖完为止。" data-tips-position="right">[?]</span>
                              <label class="TR20">以市场上最优价格卖出</label>
                          </div>
                          <div class="range range_sell" id="range_sell_mp"></div>
                          <label class="group">
                              <input type="text" class="input_text minimum" name="market_amount" placeholder=""
                                     data-type="num_range" data-min="1" data-max="99999999"
                                     data-msg-error="请输入正确的卖出量,卖出量不能小于@@,卖出量不能大于@@" data-msg-null="请输入卖出量"
                                     maxlength="10" autocomplete="off"/>
                              <span class="unit"><label class="TR17">卖出量</label> <span class="symbol"></span></span>
                                <span class="max_amount">
                                    最大<label class="TR17">卖出量</label> <b class="max_amount_num">0.0000</b>
                                </span>
                          </label>

                          <div class="trade_amount">
                              <p style="color: rgb(112, 112, 112);"><label class="TR13">卖出手续费</label><span class="float_right bind_tips sellRate" data-tips-position="right" style="margin-top: 0;" data-tips="当前手续费为%，请仔细确认后再操作。">[?]</span></p>
                              <p class="loan_amount_wrap hide"><b class="float_right loan_amount" data-amount="0">0.0000</b>使用杠杆额度 <span class="symbol"></span></p>
                          </div>
                          <div class="trade_msg"></div>

                          <div class="trade_button">
                              <button type="submit" class="btn btn_sell" data-msg-submit="卖出,正在卖出,正在申请杠杆,..."><label class="TR11"></label><lable class="symbol"></lable></button>
                          </div>
                      </form>
                  </div>
              </div>
          </div>
      </div>
    
    <!--/右侧交易-->
    
    <!--右侧涨跌信息-->

    <div id="overview"> <span class="float_right">  <lable class="symbol"></lable><span id="VOLUME">最新行情 </span></span>
      <!-- <h3 class="font_up"> ￥0.00 <i class="icon_arrows_big"></i> </h3>
      <ul>
        <li><span>最高价</span> <b class="font_up">￥0.00</b></li>
        <li><span class="TR02">涨跌比</span><b class=" font_up ">0.0%</b></li>
        <li><span>最低价</span> <b class="font_down">￥0.00</b></li>
        <li><span id="VOLUME">成交量</span> <b class=""> ? 0</b></li>
      </ul> -->

    </div>
     <!--/右侧涨跌信息-->
  </div>
  <!--K线区域-->
  <div id="main" style="height: 113px;">
    
    <div id="chart_option">
            <div class="menu" id="period">
                <span data-act="noKMode" class="TR18"></span>
                <span data-act="setTime" data-val="1min" ><label class="one"></label><label class="TR03"></label></span>
                <span data-act="setTime" data-val="5min"><label class="five"></label><label class="TR03"></label></span>
                <span data-act="setTime" data-val="15min"><label class="fifteen"></label><label class="TR03"></label></span>
                <span data-act="setTime" data-val="30min" ><label class="Thirty"></label><label class="TR03"></label></span>
                <span data-act="setTime" data-val="60min" class="cur"><label class="Sixty"></label><label class="TR03"></label></span>
                <span data-act="setTime" data-val="1day"><label class="DAY"></label></span>
                <span data-act="setTime" data-val="1week"><label class="WEEK"></label></span>
                <span data-act="setTime" data-val="1mon"><label class="MONTH"></label></span>
            </div>
            <div class="menu" id="indicator">
                <dl>
                    <dt><label class="INDEX"></label></dt>
                    <dd>
                        <div class="label"><span id="zhutuzhibiao">主图指标</span></div>
                        <span data-act="changeMI" data-val="MA" class="cur"><i class="icon_check"></i> MA</span>
                        <span data-act="changeMI" data-val="BOLL"><i class="icon_check"></i> BOLL</span>
                    </dd>
                    <dd>
                        <div class="label"><span id="futuzhibiao">副图指标</span></div>
                        <span data-act="changeVI" data-val="MACD" class="cur" ><i class="icon_check"></i> MACD</span>
                        <span data-act="changeVI" data-val="KDJ"><i class="icon_check"></i> KDJ</span>
                        <span data-act="changeVI" data-val="RSI"><i class="icon_check"></i> RSI</span>
                        <span data-act="changeVI" data-val="WR"><i class="icon_check"></i> WR</span>
                    </dd>
                </dl>
            </div>
            <div id="chart_zoom" class="icon_zoom"></div>
        </div>
    
    <div id="chart" style="height: 0px; background: rgb(19, 19, 19);">
    
    </div>
    
    <!--订单信息-->
    <div id="order" <#if user!=null>  <#else> class="hide"  </#if>" style="height: 169px;">
      <div class="handle" data-active="0"></div>
      <div class="order_wrap">
        <div class="order_hd">
          <ul class="tabs">
            <li class="TR15">历史委托</li>
            <li class="cur TR16">当前委托</li>
          </ul>
          <button class="option" style="display:inline" id="cancel_order_all"><label class="TR14">全部撤销</label></button>
        </div>
        <div class="order_col" style="display: none;">
          <div class="hd tr"> 
          <span class="col_5 Time align_center">时间</span>
           <span class="col_1 align_center Type">类型</span>
           <span class="col_4 align_center OrderPrice">委托价(￥)</span> 
           <span class="col_4 align_center OrderVol">委托数</span>
            <span class="col_4 align_center Completed">已成交(?)</span> 
          <span class="col_4 align_center AvgPrice">均价(￥)&nbsp;&nbsp;</span> 
          <span class="col_3 align_center Status">状态</span></div>
          <div class="bd" id="view_us_order_history">
            <div class="align_center p_all_30 NoOrder">暂无委托</div>
          </div>
        </div>
        <div class="order_col" style="display: block;">
          <div class="hd tr"> <span class="col_6 Time">时间</span> <span class="col_1 align_center Type">类型</span> <span class="col_4 align_center OrderPrice">委托价(￥)</span> <span class="col_4 align_center OrderVol">委托数</span> <span class="col_4 align_center Remain">剩余数量</span> <span class="col_3 align_center Status">状态</span> <span class="col_3 align_center Operation">操作</span> </div>
          <div class="bd" id="view_us_order">
            <div class="align_center p_all_30 NoOrder">暂无委托</div>
          </div>
        </div>
      </div>
    </div>
   <!--/订单信息-->

      <div id="copyright">
          <ul class="foot_record">
              <li id="copyRight"></li>
          </ul>
      </div>

  </div>
  
  <!--K线区域-->
</div>
<div id="doc_foot"></div>




<div class="">
<script type="text/x-tmpl" id="tmp_depth_ask">
    <% 
       var keepDecimalForCoin = 2;  
       var keepDecimalForCurrency =2 ;
       if(HRY.keepDecimalForCoin!=undefined){
         keepDecimalForCoin = HRY.keepDecimalForCoin;
       }
       if(HRY.keepDecimalForCurrency!=undefined){
         keepDecimalForCurrency = HRY.keepDecimalForCurrency;
       }
       
       var _l   = Math.min( 20, __data.length);
       for(var _i = 0,_r; _i < __data.length; _i++ ){
         var num = HRYB.NUM(__data[_i][0],keepDecimalForCurrency), arr = num.split('.'),tmp = '';
            if(arr[0]==_r){
                tmp = "<b>" + arr[0] + ".</b>" + arr[1];
            }else{
                _r = arr[0];
                tmp = num;
            }
        }
        for(var _i = _l; _i--;){
    %>
     <div class="tr" data-type="sell">
         <span class="col_1 font_sell"><%= Lang.L("Sell") %><%= _i+1 %></span>
         <span class="col_2" data-info="<%= __data[_i][0] %>"><%= HRYB.NUM(__data[_i][0],keepDecimalForCurrency) %></span>
         <span class="col_3" data-info="<%= __data[_i][0] %>|<%= HRYB.NUM(__data[_i][1],keepDecimalForCoin) %>" ><%= HRYB.NUM(__data[_i][1],keepDecimalForCoin) %></span>
         <span class="col_4" data-info="<%= __data[_i][0] %>|<%= HRYB.NUM(__data[_i][2],keepDecimalForCoin) %>"><%= HRYB.NUM(__data[_i][2],keepDecimalForCoin) %></span>
     </div>
    <%}%>
</script>
<script type="text/x-tmpl" id="tmp_depth_bid">
    <%
    
	var keepDecimalForCoin = 2;  
	var keepDecimalForCurrency =2 ;
	if(HRY.keepDecimalForCoin!=undefined){
	     keepDecimalForCoin = HRY.keepDecimalForCoin;
	}
	if(HRY.keepDecimalForCurrency!=undefined){
	     keepDecimalForCurrency = HRY.keepDecimalForCurrency;
	}
       
    var _l   = Math.min( 20, __data.length);
    for(var _i = 0,_r; _i < _l; _i++ ){
        var num = HRYB.NUM(__data[_i][0],keepDecimalForCurrency), arr = num.split('.'),tmp = '';
        if(arr[0]==_r){
            tmp = "<b>" + arr[0] + ".</b>" + arr[1];
        }else{
            _r = arr[0];
            tmp = num;
        }
    %>
     <div class="tr" data-type="buy">
         <span class="col_1 font_buy"><%= Lang.L("Buy") %><%= _i+1 %></span>
         <span class="col_2" data-info="<%= __data[_i][0] %>"><%= HRYB.NUM(__data[_i][0],keepDecimalForCurrency) %></span>
         <span class="col_3" data-info="<%= __data[_i][0] %>|<%= HRYB.NUM(__data[_i][1],keepDecimalForCoin) %>"><%= HRYB.NUM(__data[_i][1],keepDecimalForCoin) %></span>
         <span class="col_4" data-info="<%= __data[_i][0] %>|<%= HRYB.NUM(__data[_i][2],keepDecimalForCoin) %>"><%= HRYB.NUM(__data[_i][2],keepDecimalForCoin) %></span>
     </div>
    <%}%>
</script>
<script type="text/x-tmpl" id="tmp_trades">
    <%
    
   	var keepDecimalForCoin = 2;  
	var keepDecimalForCurrency =2 ;
	if(HRY.keepDecimalForCoin!=undefined){
	     keepDecimalForCoin = HRY.keepDecimalForCoin;
	}
	if(HRY.keepDecimalForCurrency!=undefined){
	     keepDecimalForCurrency = HRY.keepDecimalForCurrency;
	}
    var _l   = Math.min( 60, __data.length);
        for(var _i = 0,_r; _i < _l; _i++){
         var num = HRYB.NUM(__data[_i][1],keepDecimalForCurrency), arr = num.split('.'),tmp = '';
            if(arr[0]==_r){
                tmp = "<b>" + arr[0] + ".</b>" + arr[1];
            }else{
                _r = arr[0];
                tmp = num;
            }
    %>
     <div class="tr">
         <span class="col_1"><b><%= HRYB.UTT(__data[_i][0],'hh:mm:ss') %></b></span>
         <span class="col_2">
            <%= tmp %>
         </span>
         <span class="col_3
         <% if(__data[_i][3] == 1 || __data[_i][3] == 3){ %>
         font_buy
         <% }else{ %>
         font_sell
         <% } %>">
         <%= HRYB.NUM(__data[_i][2],keepDecimalForCoin) %><i class="icon_arrows_small"></i>
         </span>
     </div>
    <%}%>
</script>
<script type="text/x-tmpl" id="tmp_overview">
    <%
    var _old;
    %>
    <h3 class="<%=[7]%>">
       <%=CURRENCY_SYMBOL%><%= HRYB.NUM(__data[4],HRY.keepDecimalForCurrency) %>
       <i class="icon_arrows_big"></i>
    </h3>
    <ul>
        <li><span class="H"> <%= Lang.L("H")%></span> <b class="font_up"><%=CURRENCY_SYMBOL%><%= HRYB.NUM(__data[1],HRY.keepDecimalForCurrency) %></b></li>
        <li><span><label class="TR02"><%= Lang.L("Change")%></label></span><b class="<% if(__data[6] > 0){ %> font_up <% }else if(__data[6] < 0){ %> font_down <%}%>"><%= HRYB.NUM(__data[6],3) %>%</b></li>
        <li><span class="L"><%= Lang.L("L")%></span> <b class="font_down"><%=CURRENCY_SYMBOL%><%= HRYB.NUM(__data[2],HRY.keepDecimalForCurrency) %></b></li>
        <li><span class="Vol"><%= Lang.L("Vol")%></span> <b class=""><% if(CURRENT_SYMBOL=='btc'){ %>  <%}else{ %>  <%}%><%= HRYB.NUM(__data[5],4) %></b></li>
    </ul>
</script>
<script type="text/x-tmpl" id="tmp_new_price">
 <span class="float_right"><label class="TR02"><%= Lang.L("Change")%></label> <b class=" <% if(__data[6] > 0){ %> font_up <% }else if(__data[6] < 0){ %> font_down <%}%>"><%= HRYB.NUM(__data[6],3) %>%</b></span>
 <span><label class="NUMBER"><%= Lang.L("LastPrice")%></label><b class="<%=__data[7]%>"><%=CURRENCY_SYMBOL%>  <%= HRYB.NUM(__data[4],HRY.keepDecimalForCurrency) %> <i class="icon_arrows"></i></b></span>
</script>

<script type="text/x-tmpl" id="tmp_coin_list">


   <%
    var _l   = __data.length;
    for(var _i = 0; _i < _l; _i++ ){
        var data = __data[_i];
    %>
      <!-- <a href="<%= HRY.host %><%= HRY.modules.exstatic %>market.html?symbol=<%= data.coinCode %>" class="<% if(CURRENT_SYMBOL==data.coinCode){ %> cur <% }else { %> <%}%>"><% if(window.CURRENCY_STR=="CNY"){ %> <%=Lang.L("Symbol")%> <% }else { %>  <%=Lang.L("usd")%> <% } %>-<%= data.coinCode %></span></a> -->
	  <a  href="${ctx}/market?symbol=<%= data.coinCode %>_<%= data.fixPriceCoinCode %>" class="<% if(CURRENT_SYMBOL==data.coinCode+'_'+data.fixPriceCoinCode){ %> cur <% }else { %> <%}%>"><%= data.coinCode %>/<%= data.fixPriceCoinCode %></span></a>
		
    <%}%>



</script>

<script type="text/x-tmpl" id="temp_all_coin">


   <%
    function getQueryString(name){
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	var querysymbol =  getQueryString("symbol");
   
    var _l   = __data.length;
    var types = [];
     
    var tokenId =  $("#tokenId").val();
   
    
    for(var _i = 0; _i < _l; _i++ ){
        var data = __data[_i];
        if(types.indexOf(data.fixPriceCoinCode)==-1){
        	types.push(data.fixPriceCoinCode)
        }
    }
  
    %>
    
   	<%
    for(var i = 0; i < types.length; i++ ){
        var type = types[i];
    %>    
        <div class="col-xs-6 text-center" style="padding-right:0">
        <p class="mt0 mb0 text-primary"><b><%= type%>&nbsp;<%= Lang.L('jiaoyiqu') %></b></p>
        <ul>
      	<%
	    for(var x = 0; x < __data.length; x++ ){
	    	var data = __data[x];
	    	var allCode = data.coinCode + "_"+data.fixPriceCoinCode;
	    	if(data.fixPriceCoinCode==type){
	    		if(querysymbol==allCode){
	     %> 
	    				    		<li><a class="active" href="${ctx}/market?symbol=<%= data.coinCode %>_<%= data.fixPriceCoinCode %>" target="_self" ><%= data.coinCode %>/<%= data.fixPriceCoinCode %></a></li>
	    
	    
	    <%			
	    		}else{
	   	 %> 
	    	
	    		<li><a  href="${ctx}/market?symbol=<%= data.coinCode %>_<%= data.fixPriceCoinCode %>" target="_self" ><%= data.coinCode %>/<%= data.fixPriceCoinCode %></a></li>
	    <%	
	    	}
	    }
	    %> 
        
        <%}%>
         </ul>
        </div>
    <%}%>



</script>

<script>
function TimestampFormat(format, timestamp) {
	var a, jsdate = ((timestamp) ? new Date(timestamp * 1000) : new Date());
	var pad = function(n, c) {
		if ((n = n + "").length < c) {
			return new Array(++c - n.length).join("0") + n;
		} else {
			return n;
		}
	};
	var txt_weekdays = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ];
	var txt_ordin = {
		1 : "st",
		2 : "nd",
		3 : "rd",
		21 : "st",
		22 : "nd",
		23 : "rd",
		31 : "st"
	};
	var txt_months = [ "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];
	var f = {
		// Day
		d : function() {
			return pad(f.j(), 2)
		},
		D : function() {
			return f.l().substr(0, 3)
		},
		j : function() {
			return jsdate.getDate()
		},
		l : function() {
			return txt_weekdays[f.w()]
		},
		N : function() {
			return f.w() + 1
		},
		S : function() {
			return txt_ordin[f.j()] ? txt_ordin[f.j()] : 'th'
		},
		w : function() {
			return jsdate.getDay()
		},
		z : function() {
			return (jsdate - new Date(jsdate.getFullYear() + "/1/1")) / 864e5 >> 0
		},

		// Week
		W : function() {
			var a = f.z(), b = 364 + f.L() - a;
			var nd2, nd = (new Date(jsdate.getFullYear() + "/1/1").getDay() || 7) - 1;
			if (b <= 2 && ((jsdate.getDay() || 7) - 1) <= 2 - b) {
				return 1;
			} else {
				if (a <= 2 && nd >= 4 && a >= (6 - nd)) {
					nd2 = new Date(jsdate.getFullYear() - 1 + "/12/31");
					return date("W", Math.round(nd2.getTime() / 1000));
				} else {
					return (1 + (nd <= 3 ? ((a + nd) / 7) : (a - (7 - nd)) / 7) >> 0);
				}
			}
		},

		// Month
		F : function() {
			return txt_months[f.n()]
		},
		m : function() {
			return pad(f.n(), 2)
		},
		M : function() {
			return f.F().substr(0, 3)
		},
		n : function() {
			return jsdate.getMonth() + 1
		},
		t : function() {
			var n;
			if ((n = jsdate.getMonth() + 1) == 2) {
				return 28 + f.L();
			} else {
				if (n & 1 && n < 8 || !(n & 1) && n > 7) {
					return 31;
				} else {
					return 30;
				}
			}
		},

		// Year
		L : function() {
			var y = f.Y();
			return (!(y & 3) && (y % 1e2 || !(y % 4e2))) ? 1 : 0
		},
		// o not supported yet
		Y : function() {
			return jsdate.getFullYear()
		},
		y : function() {
			return (jsdate.getFullYear() + "").slice(2)
		},

		// Time
		a : function() {
			return jsdate.getHours() > 11 ? "pm" : "am"
		},
		A : function() {
			return f.a().toUpperCase()
		},
		B : function() {
			// peter paul koch:
			var off = (jsdate.getTimezoneOffset() + 60) * 60;
			var theSeconds = (jsdate.getHours() * 3600) + (jsdate.getMinutes() * 60) + jsdate.getSeconds() + off;
			var beat = Math.floor(theSeconds / 86.4);
			if (beat > 1000)
				beat -= 1000;
			if (beat < 0)
				beat += 1000;
			if ((String(beat)).length == 1)
				beat = "00" + beat;
			if ((String(beat)).length == 2)
				beat = "0" + beat;
			return beat;
		},
		g : function() {
			return jsdate.getHours() % 12 || 12
		},
		G : function() {
			return jsdate.getHours()
		},
		h : function() {
			return pad(f.g(), 2)
		},
		H : function() {
			return pad(jsdate.getHours(), 2)
		},
		i : function() {
			return pad(jsdate.getMinutes(), 2)
		},
		s : function() {
			return pad(jsdate.getSeconds(), 2)
		},
		// u not supported yet

		// Timezone
		// e not supported yet
		// I not supported yet
		O : function() {
			var t = pad(Math.abs(jsdate.getTimezoneOffset() / 60 * 100), 4);
			if (jsdate.getTimezoneOffset() > 0)
				t = "-" + t;
			else
				t = "+" + t;
			return t;
		},
		P : function() {
			var O = f.O();
			return (O.substr(0, 3) + ":" + O.substr(3, 2))
		},
		// T not supported yet
		// Z not supported yet

		// Full Date/Time
		c : function() {
			return f.Y() + "-" + f.m() + "-" + f.d() + "T" + f.h() + ":" + f.i() + ":" + f.s() + f.P()
		},
		// r not supported yet
		U : function() {
			return Math.round(jsdate.getTime() / 1000)
		}
	};

	return format.replace(/[\\]?([a-zA-Z])/g, function(t, s) {
		if (t != s) {
			// escaped
			ret = s;
		} else if (f[s]) {
			// a date function exists
			ret = f[s]();
		} else {
			// nothing special
			ret = s;
		}
		return ret;
	});
}
</script>

<script type="text/x-tmpl" id="tmp_us_order">
<%
var _l = __data.length,
    _c = document.getElementById('cancel_order_all'),
    _ids=[];
    if(_l>0){
    for ( var _i = 0; _i < _l; _i++ ) {
        var data = __data[_i],
        type = data.type,
        status=data.status;
        order_type = data.order_type;
  		entrustPrice =data.entrustPrice,
        entrustCount=data.entrustCount,
        _ids.push(data.entrustNum);
        _c.setAttribute('data-ids',_ids.join('|'));
%>
    <div class="tr">
            <span class="col_6"><%= TimestampFormat('H:i:s', data.entrustTime_long/1000) %></span>
             <% if(type === 1 || type === 3) { %>
                        <span class="col_1 align_center font_buy" type="1">
                            <%=  Lang.L("Buy") %>
                        </span>
                <% } else if(type === 2 || type === 4) { %>
                        <span class="col_1 align_center font_sell"  type="2">
                            <%=  Lang.L("Sell") %>
                        </span>
                <% } %>



    <span class="col_4 align_center">
        <% if(entrustPrice===0) { %>
        <%= Lang.L('MarketPrice') %>
        <% } else { %>
        <%= HRYB.NUM(data.entrustPrice,HRY.keepDecimalForCurrency) %>
        <% } %>
    </span>

<% if(entrustCount===0) { %>
    <span class="col_4 align_center"><%= HRYB.NUM(data.entrustSum,HRY.keepDecimalForCoin) %></span>
     <span class="col_4 align_center"><%= HRYB.NUM(data.entrustSum -data.transactionSum) %></span>
    <% } else { %>
    <span class="col_4 align_center"><%= HRYB.NUM(data.entrustCount,HRY.keepDecimalForCoin) %></span>
    <span class="col_4 align_center"><%= HRYB.NUM(data.surplusEntrustCount,HRY.keepDecimalForCoin) %></span>
    <% } %>
						 	          
        <% if(status ===0 ) {%>
                        <span class="col_3 align_center"> <%= Lang.L('NoSuccess') %></span>
                <% } else if(status === 1 ) { %>
                      <span class="col_3 align_center"> <%= Lang.L('PartSuccess') %></span>
                <% } else if(status === 2 ) { %>
                      <span class="col_3 align_center"><%= Lang.L('Success') %></span>
                <% }  else if(status=== 3) { %>
                      <span class="col_3 align_center"><%= Lang.L('PartCancel') %></span>
                <% }  else if(status === 4) { %>
                      <span class="col_3 align_center"><%= Lang.L('AllCancel') %></span>
                <%}  

%>
            <span class="col_3 align_center"><a href="#" class="cancel" data-ids="<%= data.entrustNum%>"><%= Lang.L('chedan') %></a></span>
    </div>

    <%} }else{  %>
    <div class="align_center p_all_30"><%= Lang.L('NoOrder') %></div>
    <%}%>


</script>
    <script type="text/x-tmpl" id="tmp_us_order_history">
<%
var _l = __data.length;
if(_l>0){
    for ( var _i = 0; _i < _l; _i++ ) {

    var data = __data[_i],
        type = data.type,
       status=data.status;
       entrustPrice =data.entrustPrice,
       entrustCount=data.entrustCount,
        order_type = data.order_type,
        new_order  = data.new_order;

%>

<div class="tr">
    <span class="col_6"><%= TimestampFormat('H:i:s', data.entrustTime_long/1000) %></span>

     <% if(type === 1 || type === 3) { %>
            <span class="col_1 align_center font_buy">
                <%= Lang.L("Buy") %>
            </span>
    <% } else if(type === 2 || type === 4) { %>
            <span class="col_1 align_center font_sell">
                <%= Lang.L("Sell") %>
            </span>
    <% } %>


    <span class="col_4 align_center">
        <% if(entrustPrice===0) { %>
        <%= Lang.L('MarketPrice') %>
        <% } else { %>
        <%= HRYB.NUM(data.entrustPrice,HRY.keepDecimalForCurrency) %>
        <% } %>
    </span>

    <% if(entrustCount===0) { %>
    <span class="col_4 align_center"><%= HRYB.NUM(data.entrustSum,HRY.keepDecimalForCoin) %></span>
    <% } else { %>
    <span class="col_4 align_center"><%= HRYB.NUM(data.entrustCount,HRY.keepDecimalForCoin) %></span>
    <% } %>


    <span class="col_4 align_center"><%= HRYB.NUM(data.entrustCount-data.surplusEntrustCount,HRY.keepDecimalForCoin) %></span>
    <span class="col_4 align_center"><%= HRYB.NUM(data.processedPrice,HRY.keepDecimalForCurrency) %> &nbsp;&nbsp;&nbsp;</span>
      <% if(status ===0 ) { %>
                        <span class="col_3 align_center"> <%= Lang.L('NoSuccess') %></span>
                <% } else if(status === 1 ) { %>
                      <span class="col_3 align_center"> <%= Lang.L('PartSuccess') %></span>
                <% } else if(status === 2 ) { %>
                      <span class="col_3 align_center"><%= Lang.L('Success') %></span>
                <% }  else if(status=== 3) { %>
                      <span class="col_3 align_center"><%= Lang.L('PartCancel') %></span>
                <% }  else if(status === 4) { %>
                      <span class="col_3 align_center"><%= Lang.L('AllCancel') %></span>
                <% }  

%>
</div>

<%
}}else{
%>
 <div class="align_center p_all_30"><%= Lang.L('NoOrder') %></div>
<%}%>
</script>
</div>




<!--[if lt IE 9]>
<div class="ie_bar">您使用的浏览器版本过低，无法支持闪电手图表功能，为了更好地用户体验，建议立即升级<a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" seed="kie-setup-IE9" target="_blank" >新版浏览器</a></div>
<![endif]--> 


<script type="text/javascript">
		/**window.clearInterval(getUser_Interval_market);
		var getUser_Interval_market = window.setInterval(function() {
			$.get(HRY.host + HRY.modules.oauth + 'getUser', function(data) {
				if(data!=undefined&&data!=""){
					console.log("加载成功！")
				   //userInfo = JSON.parse(data);
				}
			  });
		}, 1000*60*2);*/
</script>

<script type="text/javascript" >
		HRY.keepDecimalForCoin  = ${keepDecimalForCoin!4};
		HRY.keepDecimalForCurrency = ${keepDecimalForCurrency!4};
</script>
<script type="text/javascript">
	function getCookie(c_name)
	{
	if (document.cookie.length>0)
	  {
	  c_start=document.cookie.indexOf(c_name + "=")
	  if (c_start!=-1)
	    { 
	    c_start=c_start + c_name.length+1 
	    c_end=document.cookie.indexOf(";",c_start)
	    if (c_end==-1) c_end=document.cookie.length
	    return unescape(document.cookie.substring(c_start,c_end))
	    } 
	  }
	return ""
	}



var lang ="cn";

var Language = getCookie("Language");
if(Language!=""){
	lang = Language;
}

if (lang == "en") {
		document.write('<scr'+'ipt src="${ctx}/static/${version}/lib/exchange/dist/script/fl_config_en.js"></scr'+'ipt>');
		lang = "en";
}else if(lang == "tn"){
		document.write('<scr'+'ipt src="${ctx}/static/${version}/lib/exchange/dist/script/fl_config_tn.js"></scr'+'ipt>');
		lang = "tn";
}else{
	document.write('<scr'+'ipt src="${ctx}/static/${version}/lib/exchange/dist/script/fl_config_zh_cn.js"></scr'+'ipt>');
	lang = "cn";

}



if(HRY.marketProtocol==undefined){
	HRY.marketProtocol = "http";
}
var         CONST              = {},
            GLOBAL             = {},
            HBVERSION          = CONST['HBVERSION']          = '2016071501',
            MAIN_DOMAIN        = CONST['MAIN_DOMAIN']        = HRY.marketUrl,
            HTTP_STATIC_DOMAIN = CONST['HTTP_STATIC_DOMAIN'] = HRY.marketProtocol+'://'+HRY.marketUrl+'/',
            COOKIE_PRE         = CONST['COOKIE_PRE']         = '',
            COOKIE_DOMAIN      = CONST['COOKIE_DOMAIN']      = HRY.marketUrl,
            AJAX_API_DOMAIN    = CONST['AJAX_API_DOMAIN']    = HRY.marketUrl+'/qt',
            UID                = CONST['UID']                = '1' * 1,
            PROTOCOL           = CONST['PROTOCOL']           = window.location.protocol,
            $_ws_hq_url,$_ws_hq_url_usd,$_ws_hq_url_cny;
/**/

        /**/
                $_ws_hq_url     = CONST['$_ws_hq_url']     = HRY.socketioUrl;
                $_ws_hq_url_usd = CONST['$_ws_hq_url_usd'] = HRY.socketioUrl;
                $_ws_hq_url_cny = CONST['$_ws_hq_url_cny'] = HRY.socketioUrl;
        /**/

CONST['AJAX_DAMP'] = true;
CONST['AJAX_DAMP_TIME'] = 6;
CONST['AJAX_DAMP_REFRESH'] = 5000;
var Texts={
    "NetworkError":"网络出错,请重试"
};

</script> 


<script type= text/javascript src="${ctx}/static/${version}/lib/exchange/dist/script/sea.js"></script>
<script type= text/javascript src="${ctx}/static/${version}/lib/exchange/dist/script/lang.js"></script>
<script  src="${ctx}/static/${version}/js/base/utils/md5.js"  ></script>
<script src="${ctx}/static/${version}/lib/exchange/lang/lang.js"></script> 
<script src="${ctx}/static/${version}/lib/exchange/dist/script/config.js"></script> 
<script >
var data = seajs.data
  , doc = document;
seajs.Module.preload = function(a) {
    var b = data.preload
      , c = b.length;
    c ? seajs.Module.use(b, function() {
        b.splice(0, c),
        seajs.Module.preload(a)
    }, data.cwd + "_preload_" + data.cid()) : a()
}
,
seajs.use = function(a, b) {
    return seajs.Module.preload(function() {
        seajs.Module.use(a, b, data.cwd + "_use_" + data.cid())
    }),
    seajs
}
,
data.preload = function() {
    var a = []
      , b = location.search.replace(/(seajs-\w+)(&|$)/g, "$1=1$2");
    return b += " " + doc.cookie,
    b.replace(/(seajs-\w+)=1/g, function(b, c) {
        a.push(c)
    }),
    a
}(),
seajs.config({
    alias: {
        jquery: "static/${version}/lib/exchange/dist/script/jquery",
        "es5-safe": "static/${version}/lib/exchange/dist/es5-safe",
        json: "static/${version}/lib/exchange/dist/json2",
        lang: "static/${version}/lib/exchange/dist/lang",
        zepto: "static/${version}/lib/exchange/dist/zepto",
        Z: "static/${version}/lib/exchange/dist/zepto"
    },
    paths: {
        dev: "static/${version}/lib/exchange/src/script",
        dist: "static/dist/lib/exchange/src/script"
    },
    preload: [Function.prototype.bind ? "" : "es5-safe", this.JSON ? "" : "json", window.L ? "" : "lang"],
    debug: !1,
    base: HTTP_STATIC_DOMAIN,
    map: [[/^(.*\.(?:css|js))(?:.*)$/i, "$1?${t}" ]],
    charset: "utf-8"
});

</script> 

<script>
	var CURRENCY_STR = 'CNY';
    var CURRENCY_SYMBOL = '￥';
    if(HRY.ver.indexOf("en")!=-1){
    	CURRENCY_STR = 'USD';
    	CURRENCY_SYMBOL = '$';
    }
    
     var SYMBOL = 'btccny';
     var CURRENT_SYMBOL = 'BTC';
     var NEWPRICE = 100,
        IS_DEV = " 0 " * 1,
        RUN_MODE = "prod",
        PROTOCOL = window.location.protocol,
        COOKIE_PRE = CONST['COOKIE_PRE']  = '',
        UID        = CONST['UID']         = '1' * 1,
        marketOption = {
            "option" : [
                {
                    "uri" :  ''
                },
                {
                    "uri": '' ,
                    "io":{
                        resource: 'ajaxmarket/socket.io'
                    }
                }
            ],
            "scheme" : 0,
            "deBug"  : 0
        };
    Texts["ordering"]="正在委托";
    Texts["please_open_loan"]="请开启一键下单";
    Texts["tradeError"] ="委托失败";

      	function getQueryString(name){
		     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		     var r = window.location.search.substr(1).match(reg);
		     if(r!=null)return  unescape(r[2]); return null;
		}
     	if (getQueryString("symbol") != undefined) {
			CURRENT_SYMBOL = getQueryString("symbol");
			SYMBOL = CURRENT_SYMBOL;
			$("#progess_id").text(CURRENT_SYMBOL);
		} else {
			CURRENT_SYMBOL = "${defaultCoin!"BTC"}";
			SYMBOL = "${defaultCoin!"BTC"}";
		}
		
		//交易的付款币种
		CURRENCY_STR = CURRENT_SYMBOL.split("_")[1];
		if(CURRENCY_STR!="CNY"){
			CURRENCY_SYMBOL = CURRENCY_STR;
		}
        
        
		
        

    /**/
    $_ws_hq_url     = CONST['$_ws_hq_url']     = HRY.socketioUrl;
    $_ws_hq_url_usd = CONST['$_ws_hq_url_usd'] = HRY.socketioUrl;
    $_ws_hq_url_cny = CONST['$_ws_hq_url_cny'] = HRY.socketioUrl;
    /**/

   
  $(function(){
            $.i18n.properties({
                name : 'i18n', //资源文件名称
                path : '${ctx}/static/${version}/lib/exchange/lang/', //资源文件路径
                mode : 'map', //用Map的方式使用资源文件中的值
                language : lang,
                callback : function() {//加载成功后设置显示内容
                    $('#Login').html($.i18n.prop('Login'));
                    $('#Price').html($.i18n.prop('Price'));
                    $('.NUMBER').html($.i18n.prop('NUMBER'));
                    $('#Cumulative').html($.i18n.prop('Cumulative'));
                    $('#Time').html($.i18n.prop('Time'));
                    $('.LINE').html($.i18n.prop('LINE'));
                    $('.DAY').html($.i18n.prop('DAY'));
                    $('.WEEK').html($.i18n.prop('WEEK'));
                    $('.MONTH').html($.i18n.prop('MONTH'));
                    $('.PRICE').html($.i18n.prop('PRICE'));
                    $('.INDEX').html($.i18n.prop('INDEX'));
                    $('#VOLUME').html($.i18n.prop('VOLg-UME'));
                    
                    $('.DEEP').html($.i18n.prop('DEEP'));
                    $('.TR01').html($.i18n.prop('TR01'));
                    $('.TR02').html($.i18n.prop('TR02'));
                    $('.TR03').html($.i18n.prop('TR03'));
                    $('.one').html($.i18n.prop('one'));
                    $('.five').html($.i18n.prop('five'));
                    $('.qingxuanze').html($.i18n.prop('qingxuanze'));
                    $('.fifteen').html($.i18n.prop('fifteen'));
                    $('.Thirty').html($.i18n.prop('Thirty'));
                    $('.Sixty').html($.i18n.prop('Sixty'));
                    $('#TR04').html($.i18n.prop('TR04'));
                    $('#TR05').html($.i18n.prop('TR05'));
                    $('.TR06').html($.i18n.prop('TR06'));
                    $('.TR07').html($.i18n.prop('TR07'));
                    $('.TR08').html($.i18n.prop('TR08'));
                    $('.TR09').html($.i18n.prop('TR09'));
                    $('.TR10').html($.i18n.prop('TR10'));
                    $('.TR11').html($.i18n.prop('TR11'));
                    $('.TR12').html($.i18n.prop('TR12'));
                    $('.TR13').html($.i18n.prop('TR13'));
                    $('.TR14').html($.i18n.prop('TR14'));
                    $('.TR15').html($.i18n.prop('TR15'));
                    $('.TR16').html($.i18n.prop('TR16'));
                    $('.TR17').html($.i18n.prop('TR17'));
                    $('.TR18').html($.i18n.prop('TR18'));
                    $('.TR19').html($.i18n.prop('TR19'));
                    $('.TR20').html($.i18n.prop('TR20'));
                    $('.TR21').html($.i18n.prop('TR21'));
                    $('.TR22').html($.i18n.prop('TR22'));
                   
                    $('.Time').html($.i18n.prop('Time'));
                    $('.Type').html($.i18n.prop('Type'));
                    $('.OrderPrice').html($.i18n.prop('OrderPrice'));
                    $('.OrderVol').html($.i18n.prop('OrderVol'));
                    $('.Remain').html($.i18n.prop('Remain'));
                    $('.Status').html($.i18n.prop('Status'));
                    $('.Operation').html($.i18n.prop('Operation'));
                    $('.Completed').html($.i18n.prop('Completed'));
                    $('.AvgPrice').html($.i18n.prop('AvgPrice'));
                    $('.NoOrder').html($.i18n.prop('NoOrder'));
                    $('.Freeze').html($.i18n.prop('Freeze'));
                    $('.Lever').html($.i18n.prop('Lever'));
                    $('.BuyVol').html($.i18n.prop('BuyVol'));
                    $('.TR25').html($.i18n.prop('TR25'));
                    $('.TR26').html($.i18n.prop('TR26'));
                    $('.TR27').html($.i18n.prop('TR27'));
                    $('#hello').html($.i18n.prop('hello'));
                    $('#Logout').html($.i18n.prop('Logout'));
                    $('#zhutuzhibiao').html($.i18n.prop('zhutuzhibiao'));
                    $('#futuzhibiao').html($.i18n.prop('futuzhibiao'));
                    $('#headlogin').html($.i18n.prop('headlogin'));
                    $('#login_password').attr("placeholder",$.i18n.prop('login_password')).attr("data-msg-null",$.i18n.prop('login_password'))
                    $('#username').attr("placeholder",$.i18n.prop('TR23')).attr("data-msg-null",$.i18n.prop('TR23'))
                    $('#password').attr("placeholder",$.i18n.prop('TR24')).attr("data-msg-null",$.i18n.prop('TR24'))
                    
                    $('#buy_amount').attr("data-msg-null",$.i18n.prop('TR100')).attr("data-msg-error",$.i18n.prop('TR101'));
                    $('#sell_amount').attr("data-msg-null",$.i18n.prop('TR102')).attr("data-msg-error",$.i18n.prop('TR103'));
                    
                    $('#buy_price').attr("data-msg-null",$.i18n.prop('TR104')).attr("data-msg-error",$.i18n.prop('TR105'));
                    $('#sell_price').attr("data-msg-null",$.i18n.prop('TR106')).attr("data-msg-error",$.i18n.prop('TR107'));
                    $('#sellsubmit').attr("data-msg-submit",$.i18n.prop('TR108'));
                    $('#buysubmit').attr("data-msg-submit",$.i18n.prop('TR109'));
                    //$("#progess_id").html($.i18n.prop('qingxuanze'));
                }
            });
        });
 
        $(".pro-select-header").mouseover(function() {
        var item=$('.all_coin_box').find('.col-xs-6').length;
        var itemwid=$('.all_coin_box').find('.col-xs-6').outerWidth();
        $('.coin-select-btn').find('svg').css({'transform':'rotate(180deg)'});
        if(item>=4){
         $(".all_coin_box").css('width',4*itemwid);
        }else{
        $(".all_coin_box").css('width',item*itemwid);
        }
        $(".all_coin_box").slideDown();
        
         })
        $(".all_coin_box").mouseleave(function() {
         $(".all_coin_box").slideUp(),$('.coin-select-btn').find('svg').css({'transform':'rotate(0)'});
         })
       $(".all_coin_box li").on('click',function() {
         var e = $(this).data("val");
         $(".all_coin_box li a").removeClass('active'),$(this).find('a').addClass('active');
         $(".pro-coin-txt").html($(this).text()), $('input[name="progess_id"]').val(e), $(".all_coin_box").hide(),$('.coin-select-btn').find('svg').css({'transform':'rotate(0)'});
        
     })
     
     
     $('#view_depth_ask').on('click','.tr',function(){
     	var obj = $(this).find('.col_2');
   		var price = obj.text();
    	price = parseFloat(price);
	    if(price > 0.00)
	    {
	      $('#buy_price').val(price);
	      if($('#buy_amount').val()!='' && $('#buy_amount').val()!=null){
	     	 $("#buyb").text(parseFloat(price*parseFloat($('#buy_amount').val())).toFixed(2));
	      }
	    }
	});
	 $('#view_depth_bid').on('click','.tr',function(){
     	var obj = $(this).find('.col_2');
   		var price = obj.text();
    	price = parseFloat(price);
	    if(price > 0.00)
	    {
	      $('#sell_price').val(price);
	      if($('#sell_amount').val()!='' && $('#sell_amount').val()!=null){
	     	 $("#sellb").text(parseFloat(price*parseFloat($('#sell_amount').val())).toFixed(2));
	      }
	    }
	});
	$("#buy_amount").bind("input",function(){
		var buy_amount = $(this).val();
		var buy_price = $('#buy_price').val();
		if(buy_price!='' && buy_price!=null){
			$("#buyb").text(parseFloat(parseFloat(buy_amount)*parseFloat(buy_price)).toFixed(2));
		}
	})
	$("#sell_amount").bind("input",function(){
		var sell_amount = $(this).val();
		var sell_price = $('#sell_price').val();
		if(sell_price!='' && sell_price!=null){
			$("#sellb").text(parseFloat(parseFloat(sell_amount)*parseFloat(sell_price)).toFixed(2));
		}
	})



$("#loginBtn").on("click",function(){
				
				if($(".form_tip")!=undefined){
					$(".form_tip").remove();
				}	
				
				var username = $("#username").val();
				var password = $("#login_password").val();
				
				if(username==undefined||username==""){
					$(".form_login").append("<div class=\"form_tip v_success v_error \" id=\"loginError\" >请填写账号或密码</div>")
					return ;
				}
				if(password==undefined||password==""){
					$(".form_login").append("<div class=\"form_tip v_success v_error \" id=\"loginError\" >请填写账号或密码</div>")
					return ;
				}
				password = md5(password);
				
				$.ajax({
					type : "post",
					url : _ctx + "/loginService",
					data : {
						username : username,
						password : password
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								window.location.href = _ctx+"/market";
							}else{
								$(".form_login").append("<div class=\"form_tip v_success v_error \" id=\"loginError\" >用户名/密码错误</div>")
							}
						}else{
							$(".form_login").append("<div class=\"form_tip v_success v_error \" id=\"loginError\" >登录失败</div>")
						}
					},
					error : function(e) {
						
					}
				});
		});


  var Lang={};
  Lang.en={
		  H:'H',
		  L:'L',
		  Vol:'Vol',
		  Change:'Change',
		  Buy:'Buy',
		  Sell:'Sell',
		  LastPrice:'Price ',
		  Symbol:'USD Spot',
		  Loading:'Loading...',
		  Logout:'Logout',
		  Hello:'Hello',
		  NoOrder:'No Orders',
		  MarketPrice:'Market Price',
		  NoSuccess:'Unsettled',
		  PartSuccess:'Part Success',
		  Success:'Success',
		  PartCancel:'Part Cancel',
		  AllCancel:'All Cancel',
		  Cancel:'Cancel',
		  usd:'USD Spot',
		  chedan:'cancel',
		  jiaoyiqu:'Trading area',
		  dangqianmairu_feilv : 'Buying rate ',
		  dangqianmaichu_feilv:'Selling rate ',
		  zanwuweituo: 'No Orders',
		  wangluoyichang: 'Network anomaly',
		  hello:'hello'
		  
  }
  Lang.cn={
		  H:'最高价',
		  L:'最低价',
		  Vol:'当日成交总额',
		  Change:'涨跌比',
		  Buy:'买',
		  Sell:'卖',
		  LastPrice:'最新价',
		  Symbol:'人民币现货',
		  Loading:'请稍后',
		  Logout:'退出',
		  Hello:'你好',
		  NoOrder:'暂无委托',
		  MarketPrice:'市价',
		  NoSuccess:'未成交',
		  PartSuccess:'部分成交',
		  Success:'已完成',
		  PartCancel:'部分撤销',
		  AllCancel:'全部撤销',
		  Cancel:'撤销',
		  usd:'美元现货',
		  chedan:'撤单',
		  jiaoyiqu:'交易区',
		  dangqianmairu_feilv : '当前买入费率为',
		  dangqianmaichu_feilv: '当前卖出费率为',
		  zanwuweituo: '暂无委托',
		  wangluoyichang: '网络异常',
		  hello:'你好'
		 
  }
  
  Lang.tn={
		  H:'ราคาสูงสุด',
		  L:'ราคาต่ำสุด',
		  Vol:'ตกลงยอดในวันนั้น',
		  Change:'สัดส่วนผสม',
		  Buy:'ซื้อ',
		  Sell:'ขาย',
		  LastPrice:'ราคาล่าสุด',
		  Symbol:'หยวนสปอต',
		  Loading:'โปรดรอสักครู่',
		  Logout:'ถอนตัว',
		  Hello:'สวัสดี',
		  NoOrder:'ยังไม่มีแต่งตั้ง',
		  MarketPrice:'มูลค่าตลาด',
		  NoSuccess:'ธุรกรรมที่ยังไม่ได้',
		  PartSuccess:'ส่วนธุรกรรม',
		  Success:'ที่เสร็จสมบูรณ์',
		  PartCancel:'ส่วนการยกเลิก',
		  AllCancel:'เต็มการยกเลิก',
		  Cancel:'การยกเลิก',
		  usd:'เหรียญเงินสด',
		  chedan:'ยกเลิก',
		  jiaoyiqu:'โซนธุรกรรม',
		  dangqianmairu_feilv : 'ปัจจุบันอัตราการการซื้อสำหรับ',
		  dangqianmaichu_feilv: 'ปัจจุบันอัตราสำหรับการขาย',
		  zanwuweituo: 'ยังไม่มีการแต่งตั้ง',
		  wangluoyichang: 'เครือข่ายผิดปกติ',
		  hello:'สวัสดี'
		 
  }
 
  Lang.L=function(key){//alert(lang)
	  if(lang=="en"){//alert(key)
	    return  Lang.en[key]; 
	  }if(lang=="tn"){//alert(key)
		return  Lang.tn[key];
	  }else{
		return  Lang.cn[key];  
	  }
  }
  $.ajax({
	type : "post",
	url : _ctx + "/klinevtwo/message",
	cache : false,
	dataType : "json",
	success : function(data) {
		var coincode = getQueryString("symbol");
		if(data!=''&& data!=undefined){
			if(data.marketDetail!='' && data.marketDetail!=undefined){
				var meg = eval("data.marketDetail."+coincode)[0];
				if(meg.payload!=null && meg.payload!=''){
		        	if(meg.payload.asks!='' && meg.payload.asks!=undefined){
		        		if(meg.payload.asks.price!='' && meg.payload.asks.price!=undefined){
		        			if(meg.payload.asks.price[0]!=''){
		        				$('#buy_price').val(meg.payload.asks.price[0]);
		        			}
		        		}
		        	}
		        	if(meg.payload.bids!='' && meg.payload.bids!=undefined){
		        		if(meg.payload.bids.price!='' && meg.payload.bids.price!=undefined){
		        			if(meg.payload.bids.price[0]!=''){
		        				$('#sell_price').val(meg.payload.bids.price[0]);
		        			}
		        		}
		        	}
		        }
			}
		}
	}
});
 
    seajs.use(['dev/page_lightning']);

</script>
</body>
</html>