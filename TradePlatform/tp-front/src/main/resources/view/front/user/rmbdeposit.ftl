<#import "/base/spring.ftl" as spring/>
<style>
	i.form-control-feedback {
		left:515px;
	}
	.validation-invalid {
    color: red;
    position: absolute;
    right: 10px;
    z-index: 999;
    top: 5px;
}
</style>
<input type="hidden" id="msg" value="${msg}">

<div class="person_con person-con tab_switch">
						<ul class="tab_switch_ul clear">
							<li class="fl switch_li active">
								<@spring.message code="xianxiachongzhi"/>
							</li>
							<#if isOpenThird == '0'>
							<li class="fl switch_li ">
								<@spring.message code="zaixianchongzhi"/>
							</li>
							</#if>
						</ul>
						<div class="tab_switch_con">
							<div class="tab_switch_item active tab-content col-md-offset-1 col-sm-offset-1">
							 <div role="tabpanel" class="tab-pane active" id="bankpay">
			          <form  name="cg_bankForm_offline" id="cg_bankForm_offline" class="form-horizontal " >
							<div class="form-group has-feedback line-2" id="selectBank">
								<div class="help-block offline " style="margin-left: 6%;"><@spring.message code="huikuanchongzhiFee"/>${rechargeFeeRate}%，<@spring.message code="gongzuorishenhe"/>：<b class="text-danger">9：00-17：00</b></div>

								<label class="control-label col-sm-2" for="newAddr"><@spring.message code="xuanzechongzhiyinhang"/>：</label>
								<div class="input-group col-sm-10">
									<div class="MoneyChoose">  
										<div class="bank_list">
											<ul id="bankList">
											<input type="hidden" id="bankNum" value="">
											<input type="hidden" id="bTitle" value="">
											 <input type="hidden" id="bURL" value="">
											  <input type="hidden" id="bLOGO" value="">
											</ul>
										</div>
									</div>
								</div>
							</div>
						<div id="bankForm_offline">
							<input type="hidden" name="rechargeBankId">
							<div class="payinchoose offline on">
								<div class="form-group has-feedback line-2 ">
									<label class="control-label col-sm-2" for="buyAccount"><@spring.message code="huikuanrenxingshi"/>:</label>
 									<div class="col-sm-5">
											<input  class="form-control  payininput width-550" type="text" name="remitter" placeholder="<@spring.message code="qingshuruhuikuanrenxingming"/>" id="remitter" value="${user.surname}">
											<!-- <span id="" class="help-block">注：汇{{' B33  '| translate}}{{' A93 '| translate}}账{{' B41  '| translate}}的开户名必需{{' B26  '| translate}}实名认证的{{' B43  '| translate}}，否则{{ ' C34 ' | translate }}{{ ' C15 ' | translate }}进行{{' A61 '| translate}}，{{' A63 '| translate}}{{ ' C34 ' | translate }}退回到原{{' A98 '| translate}}</span> -->
									</div>
								</div>
								
								<div class="form-group has-feedback line-2 ">
									<label class="control-label col-sm-2" for="buyAccount"><@spring.message code="huikuanrenmingzi"/>:</label>
 									<div class="col-sm-5">
											<input  class="form-control  payininput width-550" type="text" name="surname" placeholder="<@spring.message code="qingshuruhuikuanrenxingming"/>" id="remitter" value="${user.truename}">
											<!-- <span id="" class="help-block">注：汇{{' B33  '| translate}}{{' A93 '| translate}}账{{' B41  '| translate}}的开户名必需{{' B26  '| translate}}实名认证的{{' B43  '| translate}}，否则{{ ' C34 ' | translate }}{{ ' C15 ' | translate }}进行{{' A61 '| translate}}，{{' A63 '| translate}}{{ ' C34 ' | translate }}退回到原{{' A98 '| translate}}</span> -->
									</div>
								</div>
								
								
								<input type="hidden" name="bankReId">
								<div class="form-group has-feedback line-2 ">
									<label class="control-label col-sm-2" for="buyAccount"><@spring.message code="yinhangkahao"/>：</label>
									<div class="col-sm-5">
										<div class="kanumberw">
											<div class="input-group inputgmod">
												<div class="drop-group" style="position:relative;">
													<input maxlength="20" name="bankCode" placeholder="<@spring.message code="qingshuruyinhangkahao"/>" id="bankCode"  class="form-control width-550">
													<!--<i class="fa fa-caret-down" style="top:10px" onclick="dropSelect(this);"></i>-->
													<span style="color:red" id="bankCode_message" ></span>

												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group has-feedback line-2 ">
									<label class="control-label col-sm-2" for="buyAccount"><@spring.message code="chongzhijine"/>：</label>
									<label class="minRecharge hide" >{{minRecharge}}</label>
									<div class="col-sm-5 input-group p-l-15">
										<input id="bankAmount"  name="bankAmount" class="form-control width-550 " type="text" >
										<span style="color:red" id="bankAmount_message" ></span>
										<span class="input-group-addon">.00</span>
									</div>
								</div>

								<div class="alert alert-warning fade in m-b-15 width-550 col-md-offset-2 col-sm-offset-2">
									<input type="hidden" id="rechargeFeeRate"  value="${rechargeFeeRate}"/>
									<input type="hidden" id="minRechargeMoney"  value="${minRechargeMoney}"/>
									
									<span id="promptShow"><@spring.message code="shouxufeie"/> 0.000${languageCode}, <@spring.message code="shijidaozhange"/>0.000${languageCode},  <@spring.message code="qingyangeanzhao"/></span>										
								</div>

								<div class="form-group line-4" id="button_type_1">
									<label class="control-label col-sm-2"></label>
									<div class="col-sm-10 paysendbtn newpopover">
										<button type="button" class="btn btn-primary form-control f-s-16 width-550" id="generate_single" ><@spring.message code="shengchengyinhanghuikuandan"/></button>
										<#--<button type="submit" ng-if="isAllowRecharge==0" disabled data-loading-text="Loading..." class="btn btn-primary form-control f-s-16 width-550"  >{{' A96 '| translate}}{{' A93 '| translate}}{{' A88 '| translate}}{{' A84 '| translate}}</button>-->
									</div>
								</div>
							</div>
						</div>
					  </form>
					</div>
				</div>
				<#if isOpenThird == '0'>
					<div class="tab_switch_item ">
                            	
						<div class="form_box">
							<ul class="payment_ul clear">
								<li class="payment_li fl active">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/zhi.png" class="default" alt="">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/zhi_on.png" class="on" alt="">
									<span style="display:none;">2</span>
									<div class="txt fr"><@spring.message code="zhifubao"/></div>
								</li>
                                <li class="payment_li fl">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/wx.png" class="default" alt="">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/wx_on.png" class="on" alt="">
									<span style="display:none;">1</span>
									<div class="txt fr"><@spring.message code="zhifubao"/></div>
								</li>
                                
								<li class="payment_li fl">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/yin.png" class="default" alt="">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/yin_on.png" class="on" alt="">
									<span style="display:none;">yinlian</span>
									<div class="txt fr"><@spring.message code="wangyin"/></div>
								</li>
								
								<li class="payment_li fl">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/yin.png" class="default" alt="">
									<img src="${ctx}/static/${version}/lib/exstatic/img/recharge/yin_on.png" class="on" alt="">
									<span style="display:none;">yinlian</span>
									<div class="txt fr"><@spring.message code="kuaijie"/></div>
								</li>
							</ul>
							<div class="payment_form_box active">
								<form class="form" target="_blank" action="/account/dorecharge" method="post">
                                    <input type="hidden" name="fee" value="0.00">
                      				<input type="hidden" name="maxnum" value="30000.00">
                                    <input type="hidden" name="minnum" value="1.00">
                                	<input type="hidden" value="jinkongpay_zhifubao" name="payment">
									<ul class="form_list_ul">
										<li class="clear">
											<div class="left fl">
												<@spring.message code="chongzhijine"/>:
											</div>
											<div class="right fr">
												<input type="text" datatype="*1-7" errormsg="请输入有效数字！" class="block_input recharge_num" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" id="zfbNum" name="zfbNum">
												<#--<span class="txt">${randomStr}CNY</span>
												<input type="hidden" id="randNumzfb" value="${random}">-->
                                                <input type="hidden" id="onlineRechargeFeeRatezfb" value="${onlineRechargeFeeRate}" />
											</div>
										</li>
									</ul>
									<div class="explain mid_box">
										<input type="hidden" id="rechargefeehiddenzfb" name="rechargefeehiddenzfb"/>
										<@spring.message code="shouxufeie"/> <span class="rechargefee">${onlineRechargeFeeRate}</span>%, <@spring.message code="shijidaozhange"/> <span class="rechargecount" id="rechargefeezfb">0.00</span>${languageCode}
									</div>
									<input type="button" class="mid_box sub_btn" value="去充值" id="zfbRecharge">
								</form>
							</div>
                           <div class="payment_form_box">
								<form class="form" target="_blank" action="/account/dorecharge" method="post">
                                    <input type="hidden" name="fee" value="0.00">
                      				<input type="hidden" name="maxnum" value="30000.00">
                                    <input type="hidden" name="minnum" value="1.00">
                                 	<input type="hidden" value="jinkongpay_weixin" name="payment">
									<ul class="form_list_ul">
										<li class="clear">
											<div class="left fl">
												<@spring.message code="chongzhijine"/>:
											</div>
											<div class="right fr">
												<input type="text" datatype="*1-7" errormsg="请输入有效数字！" class="block_input recharge_num" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" id="wxNum" name="num">
												<#--<span class="txt">${randomStr}CNY</span>
												<input type="hidden" id="randNumwx" value="${random}">-->
                                                <input type="hidden" id="onlineRechargeFeeRatewx" value="${onlineRechargeFeeRate}" />
											</div>
										</li>
									</ul>
									<div class="explain mid_box">
									<input type="hidden" id="rechargefeehiddenwx" name="rechargefeehiddenwx"/>
										<@spring.message code="shouxufeie"/> <span class="rechargefee">${onlineRechargeFeeRate}</span>%, <@spring.message code="shijidaozhange"/> <span class="rechargecount" id="rechargefeewx">0.00</span>${languageCode}
									</div>
									<!--  -->
									<input type="button" class="mid_box sub_btn" value="<@spring.message code="quchongzhi"/>" id="wxRecharge"/>
								</form>
							</div>
							<div class="payment_form_box">
								<form class="form" target="_self" action="${ctx}/user/rmbdeposit/inpayWY" method="post">
                                <!--<input type="hidden" value="jinkongpay_wangyin" name="payment">-->
                                <input type="hidden" value="guofubao_wanyin" name="payment">
                                <input type="hidden" name="fee" value="0.00">
                                <input type="hidden" name="maxnum" value="30000.00">
                               <input type="hidden" name="minnum" value="1.00">
									<ul class="form_list_ul">
										<li class="clear">
											<div class="left fl">
												<@spring.message code="xuanzeyinhang"/>:
											</div>
											<div class="right fr">
												<select name="bankCode" id="bank_name">
                                                    <option value=""><@spring.message code="qingxuanze"/>..</option>
											</select>
											</div>
										</li>
										<li class="clear" style="display:none">
											<div class="left fl">
												<@spring.message code="xuanzekaleixing"/>:
											</div>
											<div class="right fr">
												<select name="bankCardType" id="banktype">
													<option value="01"><@spring.message code="jiejika"/></option>
												</select>
											</div>
										</li>
										<li class="clear">
											<div class="left fl">
												<@spring.message code="chongzhijine"/>:
											</div>
											<div class="right fr">
												<input type="text" datatype="*1-7" errormsg="请输入有效数字！" class="block_input recharge_num" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" name="tranAmt" id="wyNum">
												<#--<span class="txt">${randomStr}CNY</span>
                                                <input type="hidden" id="randNumwy" name="randNum" value="${random}">-->
                                                <input type="hidden" id="onlineRechargeFeeRatewy" value="${onlineRechargeFeeRate}" />
											</div>
										</li>
									</ul>
									<div class="explain mid_box">
										<input type="hidden" id="rechargefeehiddenwy" name="rechargefeehidden"/>
										<@spring.message code="shouxufeie"/> <span class="rechargefee">${onlineRechargeFeeRate}</span>%, <@spring.message code="shijidaozhange"/><span class="rechargecount" id="rechargefeeWY">0.00</span>${languageCode}
									</div>
									<!--  -->
									<input type="submit" class="mid_box sub_btn" value="<@spring.message code="quchongzhi"/>" id="wyRecharge">
								</form>
							</div>
							<div class="payment_form_box">
						<form class="form" target="_blank" action="" method="post">
							<ul class="form_list_ul">
								<li class="clear">
									<div class="left fl">
										<@spring.message code="chongzhijine"/>:
									</div>
									<div class="right fr">
										<input type="text" datatype="*1-7" errormsg="请输入有效数字！" class="block_input recharge_num" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" placeholder="<@spring.message code="qingshurujine"/>" id="kjNum" name="kjNum" >
									</div>
									<div class="explain mid_box">
									<input type="hidden" id="rechargefeehiddenkj" name="rechargefeehiddenkj"/>
										<@spring.message code="shouxufeie"/> <span class="rechargefee">${onlineRechargeFeeRate}</span>%, <@spring.message code="shijidaozhange"/><span class="rechargecount" id="rechargefeekj">0.00</span>${languageCode}
									</div>
								</li>
								<li class="clear">
									<div class="left fl">
										<@spring.message code="yinhang"/>：
									</div>
									<div class="right fr">
										<select name="bankCode" id="bank_namekj">
                                                <option value=""><@spring.message code="qingxuanze"/>..</option>
										</select>
									</div>
								</li>
								<li class="clear">
									<div class="left fl">
										<@spring.message code="yinhangyuliushoujihao"/>：
									</div>
									<div class="right fr">
										<input type="text" maxlength="11" class="block_input" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" id="mobileNo"/>
									</div>
								</li>
								<li class="clear">
									<div class="left fl">
										<@spring.message code="yinhangkahao"/>：
									</div>
									<div class="right fr">
										<input type="text" class="block_input" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" id="bankCardNokj"/>
									</div>
								</li>
								<li class="clear">
									<div class="left fl">
										<@spring.message code="yinhangzhanghumingcheng"/>：
									</div>
									<div class="right fr">
										<input type="text" class="block_input" id="bankAcctName" readonly="true" value="${user.truename}"/>
									</div>
									<#--<span class="txt">${randomStr}CNY</span>
									<input type="hidden" id="randNumkj" value="${random}">-->
                                    <input type="hidden" id="onlineRechargeFeeRatekj" value="${onlineRechargeFeeRate}" />
								</li>
							</ul>
							<!--  -->
							<input type="button" class="mid_box sub_btn" value="<@spring.message code="quchongzhi"/>" id="kjRecharge"/>
						</form>
					</div>
						</div>
					</div>
					
					<!-- offline recharge -->
				</#if>
			</div>
		</div>
<div class="container-fluid person-con">

<!-- begin page-container -->
<div class="container-fluid person-con">
	<!-- begin page-header -->
	<div class="row" style="margin-bottom:15px;">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" id="RMBtab">
					<li role="presentation" class="active pull-left">
						<a href="javascript:void(0)"><@spring.message code="chongzhijilu"/></a>
					</li>
				</ul>
			</div>
			<!--<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>-->
		</div>
	</div>
	<!-- end page-header -->

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
	<div class="row" >
		<!-- begin col-3 -->
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
		<!-- end col-3 -->
	</div>
	<!-- end row -->
		
		<div class="row text-center">
			 <hry-page conf="pageConf"></hry-page>
		</div>
	</div>
<!--</div>-->



<div class="row" id="Popup">
	<div class="">
  <div class="mention">
    <div class="privilege">
      <h3 style="margin:0 0 10px 0; padding:11px;"><@spring.message code="shenqinghuikuandan"/></h3>
      <div class="orderbox">
      <table width="100%" class="tb-list2 table">
		  <colgroup>
		   <col style="width: 120px"></col>
		   <col></col>
		  </colgroup>
		<tbody>
		<tr>
          <th scope="row"><@spring.message code="huiruyinhangzhanghu"/>：</th>
          <td><span id="bankAccount"></span></td>
        </tr>
        <tr>
          <th scope="row"><@spring.message code="huiruyinhang"/>：</th>
          <td><span id="bankName_"></span></td>
        </tr>
        <tr>
          <th scope="row"><@spring.message code="huiruyinhangzhihang"/>：</th>
          <td><span id="bankAddress"></span></td>
        </tr>
        <tr>
          <th scope="row"><@spring.message code="shoukuanren"/>：</th>
          <td><span id="accountName"></span></td>
        </tr>
        <tr>
          <th scope="row"><@spring.message code="money"/>：</th>
          <td><span id="remittanceMoney"></span></td>
        </tr>
        <tr>
          <th scope="row"><@spring.message code="dingdanhao"/>：</th>
          <td><span id="transactionNum"></span></td>
        </tr>
           <tr>
             <th scope="row"><@spring.message code="beizhu"/>：</th>
             <td><span id="remark"></span></td>
           </tr>
           <tr>
             <th scope="row"><@spring.message code="zhuangtai"/>：</th>
             <td><b><@spring.message code="daichuli"/></b>
                 	<@spring.message code="a00"/><a href="javascript:void(0);" target="_blank" ><img style="vertical-align: middle;height:36px;" ng-src="{{HRY.host}}/{{bankLOGO}}" ></a>
             </td>
           </tr>
           
           
        </tbody>
	  </table>
	   <div class="ctips" style="background:rgb(254,231,225);font-size:10px;margin-bottom:5px;padding:10px;">
			<!--<i class="fa fa-flag-o"></i>-->
	  		<span style="line-height:24px;"><b style="font-size:1em;"><@spring.message code="wenxintishi"/>:</b>
				<@spring.message code="dangqianzhuanzhang"/><span id="nowMoney"><span>，<@spring.message code="wenxintishi"/>
  			<b style="color:red;"><wbr><span id="wenxintishi"></span>，</wbr><@spring.message code="2hourzidongchongzhi"/></b>
  				<@spring.message code="2hourzidongchongzhi_tishi"/>
  			</span>
	  </div>
	 </div>
    </div>
  </div>
  </div>
</div>

<#include "/base/base.ftl">
<script type="text/javascript">
seajs.use(["js/front/user/rmbdeposit","js/front/user/common"],function(rmb){
	rmb.init();
});
</script>
