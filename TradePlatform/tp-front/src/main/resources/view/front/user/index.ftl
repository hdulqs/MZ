<#include "/base/base.ftl">

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/exstatic/css/mycc/myCC.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/exstatic/css/mycc/iconfont.css">
  </head>

	<div class="container-fluid person-con ng-scope">
		<!-- ngRepeat: item in accountList | orderBy:col:!desc -->
		<#if customer!="kk">
			<div class="row ng-scope" ng-repeat="item in accountList | orderBy:col:!desc" style="margin: 0 0 20px 0; border-bottom: 1px solid #f0f3f5; padding: 0 0 25px 0;">
		<#else>
			<div class="row ng-scope" ng-repeat="item in accountList | orderBy:col:!desc" style="border-bottom: 1px solid #f0f3f5;">
		</#if>
			<#--<#if customer!="kk">
			<div class="col-md-8 col-sm-8">
				<div class="col-md-6 col-sm-6">
					<p><@spring.message code="keyong"/> ${languageCode} :</p>
					<h5 class="text-success ng-binding"><#if myaccount??><span style="font-size:34px;color:#ec1a1a;">${(myaccount.hotMoney!0?string(",###"))!}</span><#else><span style="font-size:34px;color:#ec1a1a;">0</span><span style="color:#666;"></span></#if></h5>
				</div>
				<div class="col-md-6 col-sm-6">
					<p style="margin:0;"><@spring.message code="dongjie"/> ${languageCode} :<span class="ng-binding"><#if myaccount??>${(myaccount.coldMoney!0?string(",###"))!}<#else>0</#if></span></p>
					
				</div>
				<div class="col-md-6 col-sm-6 m-t-20">
					<p style="margin:0;"><@spring.message code="heji"/> ${languageCode} :<span class="ng-binding"><#if myaccount??> ${((myaccount.hotMoney!0)+(myaccount.coldMoney!0))}<#else>0</#if></span></p>
				</div>
			</div>
			</#if>-->
			<#if customer!="kk">
				<div class="col-md-4  col-sm-4 p-d-0" style="padding:0 6px;">
					<div class="col-md-6 col-sm-12">
						<#--<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/rmbdeposit/index')" class="btn btn-success"><@spring.message code="renminbichongzhi"/></a>-->
						<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/btc/post')" class="btn btn-success"><@spring.message code="xunibichongzhi"/></a> 
					</div>
					<div class="col-md-6 col-sm-12">
					    <#--<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/rmbWithdraw/index')"class="btn btn-danger"><@spring.message code="renmibiquxian"/></a>-->
						<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/btc/get')"class="btn btn-danger"><@spring.message code="xunibiquxian"/></a>
				   </div>
				</div>
			<#else>
				<div style="padding:0 6px;">
					<div style="padding: 0;display: inline;">
						<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/btc/post')" class="btn btn-success"><@spring.message code="xunibichongzhi"/></a> 
					</div>
					<div style="padding: 0; margin-top: 10px;display: inline;">
						<a href="javascript:void(0);" onclick="loadUrl('${ctx}/user/btc/get')"class="btn btn-danger"><@spring.message code="xunibiquxian"/></a>
				   </div>
				</div>
			</#if>

		<!--	<div class="col-md-6">
		     <div class="" style="margin-top: 35px;">
              <ul class="levels">
               <li class="curlevel" style="">
                <h4 class=""><@spring.message code="ritixianedu"/>：<strong class="">${user.uncardCurrency}</strong>BTC</h4>
                <p><i class="iconfont icon-diamond"></i>Lv.1</p>
               </li>
             
                <#if states==3||states==0>
                 <li>
                	<h4 class=""><@spring.message code="ritixianedu"/>：<strong class="ng-binding">${user.cardCurrency}</strong>BTC</h4>
                	<p><i class="iconfont icon-diamond"></i>Lv.2</p>
					<a  href="javascript:void(0);" onclick="loadUrl('${ctx}/user/identitymav')" class="color-o editlink"><@spring.message code="wanchengshengfenrenzheng"/></a>
				</li>
                 <#elseif states==2||states==1>
                  <li class="curlevel">
                	<h4 class=""><@spring.message code="ritixianedu"/>：<strong class="ng-binding">${user.cardCurrency}</strong>BTC</h4>
                	<p><i class="iconfont icon-diamond"></i>Lv.2</p>
					<a  href="javascript:void(0);" onclick="loadUrl('${ctx}/user/realinfo')" class="orange" class="color-g editlink"><@spring.message code="chakanrenzheng"/></a>
				 </li>
                
                </#if>
              

            </ul>
    </div>-->
		
		
		
		
			</div>
		</div>
		<!-- end ngRepeat: item in accountList | orderBy:col:!desc -->
		<!-- <div class="row col-md-12 col-sm-12" id="messageDiv" style="padding-right:0;">
			<div class="noticeTips systemNoticeTips"  style="height:40px;">
				<div class="title">
					<span class="img img_t"></span>
					<span class="words"><@spring.message code="wodexiaoxi"/></span>
				</div>
				<div class="content_tips">
					<p><a class="noticeNewsHref" href="javasrcipt:void(0);" sid="" style="text-decoration:none;"></a></p>
				</div>
				<a class="close noticeCloseNews" style="margin-top: 14px;" href="javascript:void(0);" ><img src="${ctx}/static/${version}/img/noticeClose.png"></a>
				<div class="more" style="margin-top: 4px;">
						<a href="javascript:void(0);" onclick="loadUrl('${ctx}/v.do?u=front/user/mymessage')"><@spring.message code="chakanquanbu"/></a>
				</div>
			</div>
		
		</div> -->
		<div class="row">
			<div class="col-md-12 col-sm-12">
				<table class="table table-valign-middle table-hover" cellspacing="0" cellpadding="0">
					<thead>
						<tr style="background-color: rgb(240, 243, 245);">
							<th class="ng-binding"><@spring.message code="xuhao"/></th>
							<th class="ng-binding"><@spring.message code="bizhong"/></th>
							<th class="ng-binding"><@spring.message code="zongshu(ge)"/></th>
							<th class="ng-binding"><@spring.message code="keyong(ge)"/></th>
							<th class="ng-binding"><@spring.message code="dongjie(ge)"/></th>
							<!-- <th class="ng-binding"><@spring.message code="caozuo"/></th> -->
						</tr>
					</thead>
					<tbody>
						<#if coinAccountList ??>
						<#list coinAccountList as account >
						<tr ng-repeat="list in coinlist" class="ng-scope">
							<td class="ng-binding">${account_index+1}</td>
							<td><span class="cionpic" style="display: inline-block; width: 20px;margin-right:6px;"> <img style="width: 26px; height: 26px"  src="${account.picturePath}">
							</span> <a href="javascript:void(0)" > <span class="bizhong_en ng-binding">${account.coinCode}</span>
							</a>
							</td>
							<td>
								 <span ng-if="list.hotMoney!=null" class="rate_up ng-binding ng-scope">${((account.hotMoney+account.coldMoney)!0?string(",###.00"))!}</span> 
							</td>
							<td ng-if="list.hotMoney!=null" class="ng-binding ng-scope">${(account.hotMoney!0?string(",###.00"))!}</td>
							<td ng-if="list.coldMoney!=null" class="ng-binding ng-scope">${(account.coldMoney!0?string(",###.00"))!}</td>
							<!-- <td><a href="${ctx}/market?tokenId=${tokenId}" class="ng-binding"><@spring.message code="qujiaoyi"/></a></td> -->
						</tr>
						</#list>
						</#if>

					</tbody>
				</table>


			</div>

		</div>
	</div>

	<!--end page-container -->
	<!-- <div class="container-fluid person-con ng-scope">

		<!-- begin page-header -->
		<!-- <div class="row" style="margin-bottom: 15px;">
			<div class="panel_wrap_head wrap_head">
				<div class="">
					<ul class="wrap_tabs" role="tablist" id="RMBtab">
						<li role="presentation" class="active pull-left"><a href="http://www.200.com/" class="ng-binding"><@spring.message code="huobimingxi"/></a></li>
					</ul>
				</div>
				<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>
			</div>
		</div> -->
		<!-- end page-header -->

		<!-- begin row -->
		<!-- <div class="row">
			<!-- begin col-12 -->
			 <!-- <div class="col-md-12">
				<form class="form-inline input-daterange clearfix ng-pristine ng-valid">

					<div class="form-group form-labels  col-md-12" id="transactionType">
						 <label for="" ><@spring.message code="chognzhileixing"/>：</label>
						 <a class="label ng-binding selected" value="all"><@spring.message code="quanbu"/></a>
						 <a class="label ng-binding"  value="chongzhi" ><@spring.message code="chongbi"/></a>
						 <a class="label ng-binding" value="tixian"><@spring.message code="tibi"/></a> &nbsp;&nbsp; 
					</div>
				</form>
			</div> -->
			<!-- end col-12 -->
		</div> 
		<div class="row"> 
			<!-- begin col-3 -->
			<div class="col-sm-12">
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
	<!-- begin page-container -->

	
<script type="text/javascript">
seajs.use(["js/front/user/index"],function(o){
	o.init();
	o.messageList();
});
</script>
