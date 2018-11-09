<#import "/base/spring.ftl" as spring/>
			<!-- begin page-container -->
			<div class="container-fluid person-con">
			
				<!-- begin page-header -->
				<div class="row" style="margin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs" role="tablist" id="RMBtab">
								<li role="presentation" class="active pull-left">
									<a href="javascript:void(0);"><@spring.message code="xunibichongzhi"/></a>
								</li>

							</ul>
						</div>
					</div>
				</div>
				<!-- end page-header -->

				
				<div id="coins">
				<#list list as list>
				<button type="button" class="btn btn-primary   btn-lg f-s-16" name="coinobjcet" accountid="${list.id}">${list.coinCode}</button>
				</#list>
				</div>
				<hr>
				<input type="hidden" id="postCoinCode" ></input>
				<div class="wallet_link ">
					<h4 class="f-s-12 p-b-20 wallet-tit"><@spring.message code="nideqianbaoBefore"/><span id="coincode3">${coincode} </span><@spring.message code="nideqianbaoAfter"/>：</h4>
					<div class="clearfix">
						<p class="col-sm-8 f-s-20 p-30 wallet-txt">
							<span id="publicKey">
								<span id="publicKeyValue"></span>
								<button type="button" class="hide btn btn-danger  btn-lg btn-block f-s-16" id="createPublicKey"><@spring.message code="GetPublicKey"/><span id="coincode5"></span></button>
							</span>
						</p>
						<div class="thumbnail text-center walcode col-sm-2">
						<div id="code" style="margin-left:45px"></div>
							<div class="caption" >
								<p></p>
							</div>
						</div>
					</div>
				</div>
				<!---end:{{' A11 '| translate}}{{' Address '| translate}}---->
				<dl class="withdraw-info p-t-20">

					<dt class="f-s-16 p-b-10"><@spring.message code="zhuyishixiang"/>： </dt>
					<dd id="zdbefore"><@spring.message code="zhuanrushizidongBefore"/></dd>
					<dd><@spring.message code="addressOnly"/></dd>
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
									<a href=""><@spring.message code="chongzhijilu"/></a>
								</li>
							</ul>
						</div>
					<!--	<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>-->
					</div>
				</div>
				<!-- end page-header -->

				<!-- begin row -->
				<div class="row">
					<!-- begin col-12 -->
					<div class="col-md-12 col-sm-12">
						<form class="form-inline input-daterange clearfix">
							<div class="form-group form-labels  col-md-12 col-sm-12" id="type">
								<label for=""><@spring.message code="caozuoleixing"/>：</label>
								<a class="label ng-binding selected" lang="" name="status" value="" ><@spring.message code="quanbu"/></a> 
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

					<!-- end col-3 -->
				</div>
				<!-- end row -->
			</div>

<#include "/base/base.ftl">
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
seajs.use(["js/front/user/btcpost"],function(btc){
	btc.init();
	btc.getPublicKey();
});
</script>
