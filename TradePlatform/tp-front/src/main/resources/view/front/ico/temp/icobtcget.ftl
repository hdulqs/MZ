<#import "/base/spring.ftl" as spring/>
<style>
	i.form-control-feedback {
		right: 10px;
	}
</style>
<div class="container-fluid person-con">

				<!-- begin page-header -->
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
				<!-- end page-header -->

				<!---start:{{' A11 '| translate}}{{' Address '| translate}}---->
				<user-coin-select coincode="{{coinCode}}" url="#/user/account/btcget" accountshow="false"></user-coin-select>

				
				<form class="form-horizontal withdraw-form p-20" name="withdraw" id="withdraw">
						
						<ul class="withdraw-count">
						  <li>
							  <select id="coinSelect" class="form-control" style="border:none;outline:none;height:56px;" >
								<#list list as list>
								<option value="${list.id},${list.hotMoney},${list.coldMoney},${list.publicKey},${list.coinCode},${list.paceFeeRate},${list.leastPaceNum},${list.oneDayPaceNum}">
									${list.coinName}
								</option>
								</#list>
							</select>
							</li>
						  <li>
						    <@spring.message code="keyong"/><span id="coincode1">${coincode}</span> : <input type="text" id="availableCTC"   value="${firstHot}" style="border:none"  readonly="readonly"/>
						  </li>
						  <li>
						    <@spring.message code="dongjie"/><span id="coincode2">${coincode}</span> : <input type="text" id="frozenCTC" value="${firstCold}" style="border:none"   readonly="readonly"/>
						  <li>
						<input type="text" style="display:none" id="coinType" name="coinType" value="${coinType}" readonly="readonly" />
						<input type="text" style="display:none" id="currencyType" name="currencyType" value="${currencyType}" readonly="readonly"  />
						</ul>
						<div class="form-group" style="margin-top:30px;">
							<label class="col-sm-3 control-label"><@spring.message code="qianbaodizhi"/>：</label>
							<div class="col-sm-6">
								 <select name="btcKey" ng-model="formData.btcKey" class="form-control" id="btcKey">
								 	 <#list list2 as list>
                            		 <option value="${list.publicKey}">${list.publicKey}</option>
                            		 </#list>
                        		</select>
                        		<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/publickeylist/index')" id="addWaccount" class="collection-item"><@spring.message code="tianjiatixianzhanghu"/></a>
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
								<label class="decimalCion hide" >{{decimalCion}}</label>
								<input type="text" name="btcNum" id="inputNumWit" require class="form-control" placeholder="请填写数量" autocomplete="off">
								<span style="color:red" id="btcNum_message" ></span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><@spring.message code="jiaoyimima"/>：</label>
							<div class="col-sm-6">
								<input type="password" name="accountPassWord" id="accountPassWord" require class="form-control" placeholder="请填写交易密码" autocomplete="off">
							</div>
							<div class="col-md-6 col-md-offset-3">
								<div class="alert alert-warning fade in m-t-15 col-md-12" id="divPrompt">
									<@spring.message code="shouxufeie"/><span id="shouxufei">0.0000</span><span id="coincode3">${coincode}</span> <@spring.message code="shijidaobi"/><span id="shijidao">0.0000</span><span id="coincode4">${coincode}</span>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label"><@spring.message code="shoujihaoma"/>：</label>
							<div class="col-sm-6 p-l-0"><input type="hidden" id="username" value="${user.username}" />
								<label class="col-sm-2 control-label f-s-18">${user.username}</label>
							</div>
						</div>
						
						<div class="form-group">
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
						
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-3">
								<button type="button" class="btn btn-primary form-control f-s-16" id="oktx"><@spring.message code="querentixian"/></button>
							</div>
						</div>

				</form>
				<!---end:{{' A62 '| translate}}{{' A44 '| translate}}表{{' A84 '| translate}}---->
				<dl class="withdraw-info p-t-20">
					<dt class="f-s-16 p-b-10"><@spring.message code="tixianqingxuanzeyinhang"/>：</dt>
					<dd>1. <@spring.message code="tibishouxufeiwei"/><span id="paceFeeRate">${paceFeeRate}</span>%，<@spring.message code="qingzixiqueren"/>。</dd>
					<dd>2. <@spring.message code="zhuanbitiaojian"/><span id="leastPaceNum">${leastPaceNum}</span>，<@spring.message code="zhuanbitiaojian2"/><span id="oneDayPaceNum">${oneDayPaceNum}</span></dd>
				</dl>
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
						<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>
					</div>
				</div>
				<!-- end page-header -->

				<!-- begin row -->
				<div class="row">
					<!-- begin col-12 -->
					<div class="col-md-12">
						<form class="form-inline input-daterange clearfix">
							<div class="form-group form-labels  col-md-12" id="type">
								<label for=""><@spring.message code="zhuangtai"/>：</label> 
								<a class="label ng-binding selected" lang="" name="status" value=""><@spring.message code="quanbu"/></a>
								 <a class="label"  lang="1" value="1"><@spring.message code="dengdai"/></a>
								 <a class="label"  lang="2" value="2"><@spring.message code="success"/></a> 
								 <a class="label"  lang="3" value="3"><@spring.message code="error"/></a>
							</div>
						</form>
					</div>
					<!-- end col-12 -->
				</div>
				<div class="row">
					<!-- begin col-3 -->
					<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="current">
						
				<table   id="table"
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

<#include "/base/base.ftl">
<script type="text/javascript">
seajs.use(["js/front/ico/icobtcget"],function(btc){
	btc.init();
	btc.sendsms();
});
</script>