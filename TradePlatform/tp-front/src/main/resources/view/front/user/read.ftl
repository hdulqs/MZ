<#include "/base/base.ftl">
<div class="container-fluid person-con">
	<div role="tabpanel">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" >
					<li role="presentation" class="active pull-left">
						<a href="#current" aria-controls="current" role="tab" data-toggle="tab"><@spring.message code="xiaoxixiangqing"/></a>
						<a href="javascript:void(0);" gobank><@spring.message code="fanhui"/></a>
					</li>
				</ul>
			</div>
					
		</div>

		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="current">
				${content}
		</div>
	</div>

</div>
<#include "/base/base.ftl">
<script type="text/javascript">
seajs.use(["js/front/user/mymessage"],function(o){
	o.read();
});
</script>