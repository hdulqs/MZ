<!DOCTYPE html>
<html  lang="en">
<head>
<#include "/base/base.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="" name="description">
<meta content="" name="author">
<@HryTopOrFooter url="base/title.ftl"/>
<!-- ================== BEGIN BASE CSS STYLE =============== -->
<link href="${ctx}/static/${version}/lib/exstatic/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/style.min.css" rel="stylesheet" />
</head>
<body >
<#import "/base/spring.ftl" as spring/><!-- 新增 -->

	<!-- topbar -->
	<div class="container-fluid person-con"><!-- 新增 -->
	<div role="tabpanel"><!-- 新增 -->
	
	<div id="page-container" class="container fade page-header-fixed ng-scope in">
		<!-- 头部导航 -->
	
		<input type="hidden" name="tokenId" id="tokenId" value="${tokenId}"/>
		<!-- 中间切换区域 -->
		<div id="content" class="content col-md-10 col-sm-10"  style="margin: 0px; min-height: 800px; padding: 0;">
			<#import "/base/spring.ftl" as spring/>
			<style>
				i.form-control-feedback {
					right: 10px;
				}
			</style>
            <div class="container-fluid person-con">
				<div class="row" style="margin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs">
								<li role="presentation" class="active pull-left">
									<a href="javascript:void(0);"><@spring.message code="yaoqingtuijian"/></a>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<!-- end page-header -->
				<!-- 邀请推荐中间内容 -->
				<div class="invite ng-scope">
				<div class="f-cb">
					<div class="inviteLink f-fl">
						<div class="item ng-binding" style="margin-top:25px;">
							<label><@spring.message code="wodeyaoqingma"/>： </label><span id="inviteLink2">${info.commendCode}</span>
							<span class="iconfont fa fa-file-text icon-copy copy-btn" data-clipboard-target="#inviteLink2" title="复制到剪切板"></span>
						</div>
						<div class="item">
							<label><@spring.message code="yaoqinglianjie"/>： </label>
							<input id="inviteLink1" type="text" name="inviteLink" value="${info.commendLink}" readonly="">
							<span class="iconfont fa fa-file-text icon-copy copy-btn" data-clipboard-target="#inviteLink1" title="复制到剪切板"></span>
						</div>
					</div>
					
					<div class="inviteData f-fr">
						<table>
							<colgroup style="width:50%"></colgroup>
							<colgroup style="width:50%"></colgroup>
							<tbody>
								<tr>
									<th><img src=""><@spring.message code="yituijianpengyou"/></th>
								</tr>
								<tr>
									<td><strong class="ng-binding">${info.commendCount}</strong></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="qrcodeTable" style="position: absolute;top: 70px;right: 10px;"></div>
		</div>
				<!-- 邀请推荐中间内容结束 -->
				
                  
</body>	

<div class="tab-content"><!--新增-->
			<div role="tabpanel" class="tab-pane active" id="current"><!--新增-->
					
			<!--end page-container --->
			  <div class="container-fluid person-con">
				<!-- begin page-header -->
				<div class="row" style="gin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs">
								<li role="presentation" class="active pull-left">
									<a href="javascript:void(0);"><@spring.message code="fanyongjilu"/><!-- <@spring.message code="xunibitixian"/> --></a>
								</li>
	
							</ul>
						</div>
					</div>
				</div>
				</div><!--新增-->
				
				<!--<div class="Commission">
			<table>
				  <thead>
				   <tr>
				    <th><@spring.message code="tuijianren"/></th>
				    <th><@spring.message code="huodejianglijine"/></th>
				     <th><@spring.message code="bizhongdanwei"/></th>
				   </tr>
				  </thead>
				  <tbody id="aab">
				  </tbody>
				 </table>
				 
				</div>-->  <!--禁用-->
				
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
		 	           data-page-list="[10]"
		 	           data-show-footer="false"  
		 	           data-side-pagination="server"
		 	           >
		 	    </table>
				
				
				
			</div><!--新增-->
            </div><!--新增-->	
	</div><!--新增-->
</div><!--新增-->

<#include "/base/base.ftl">

<!-- <script type="text/javascript" src="/static/src/js/base/googleauth/jquery-1.11.1.js" ></script>
<script type="text/javascript" src="/static/src/js/base/googleauth/jquery.qrcode.js" ></script>
<script type="text/javascript" src="/static/src/js/base/googleauth/qrcode.js" ></script> 
<script type="text/javascript" src="/static/src/js/base/googleauth/utf.js" ></script> -->
<script type="text/javascript">
   seajs.use(["js/front/user/setcommend","js/i18n_base"],function(o){
	o.init();
});
</script>	
</html>




