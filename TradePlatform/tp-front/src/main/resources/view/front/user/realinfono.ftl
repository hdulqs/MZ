<#include "/base/base.ftl">
<div class="container-fluid person-con min-hg">

<!-- begin page-header -->
<div class="row" style="margin-bottom:15px;">
	<div class="panel_wrap_head wrap_head">
		<div class="">
			<ul class="wrap_tabs" role="tablist" id="RMBtab">
				<li role="presentation" class="active pull-left">
				 <a href=""><@spring.message code="shimingrenzheng"/></a>
				
				</li>

			</ul>
		</div>
	</div>
</div>
<!-- end page-header -->

	<div class="media two-rz-title">
		<a href="#" class="pull-left">
			<!--验证失败-->
			<img src="${ctx}/static/${version}/lib/exstatic/static/assets/img/user/rz-yes.png" class="media-object" />
			<!--验证失败-->

		</a>
		<div class="media-body">
			<h4 class="media-heading text-success p-t-10 f-s-16">
			<@spring.message code="shimingjujue"/>
			<a  style="right: 15px;margin-top:13px;border-radius: 4px;padding: 4px 10px; color: #fff; background: #68c08a;top: 20px;position: absolute;" href="javascript:void(0);" onclick="loadUrl('${ctx}/user/identitymavno')" class="color-o editlink"><@spring.message code="chongxinrenzheng"/></a>
			
			</h4>
			<@spring.message code="shiming_tishi1"/><br/>
			<@spring.message code="shiming_tishi2"/>
		</div>
	</div>
<!----->
<#if states=3>

<#else>



</#if>
</div>
