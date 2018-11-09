<#include "/base/base.ftl">
<!-- begin page-container -->
<div class="container-fluid person-con min-hg">
	<!-- begin page-header -->
		<h1 class="page-header"><@spring.message code="shimingrenzheng"/></h1>
	<!-- end page-header -->

	<!-- begin row -->
	<div class="media two-rz-title">
		<a href="#" class="pull-left">
			<!--验证失败-->
			<img src="${ctx}/static/${version}/lib/exstatic/static/assets/img/user/rz-no.png" class="media-object" />
			<!--验证失败-->

		</a>
		<div class="media-body">
			<h4 class="media-heading text-warning p-t-10 f-s-16">
				<@spring.message code="realname_tishi1"/>
			</h4>
			<@spring.message code="realname_tishi2"/><br/>
			<@spring.message code="realname_tishi3"/>
		</div>
	</div>
	<!----->
	<form class="form-horizontal p-t-30 bank-manage-add form-all" name="myfrom"   >
		<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="xingming"/>：</label>
			<div class="col-sm-6">
				<input type="text" name="trueName" id="trueName" class="form-control" placeholder="" /></div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="guojia/diqu"/>:</label>
			<div class="col-sm-3" style="padding-right:0px !important">
				<select  name="country" id="country" class="form-control"  >
					<option value="0086" selected="selected">China（中国）</option>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="zhengjianleixing"/>:</label>
			<div class="col-sm-6">
				<select name="cardType" id="cardType" class="form-control" >
					<option value="0" selected="selected"><@spring.message code="shenfenzheng"/></option>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-4 control-label"><@spring.message code="zhengjianhao"/>:</label>
			<div class="col-sm-6">
				<input type="text" name="cardId" id="cardId"  class="form-control" placeholder="">
			</div>
		</div>


		<div class="form-group">
			<div class="col-sm-6 col-sm-offset-4">
				<button type="button"  id="realnameBtn" class="btn btn-primary form-control"><@spring.message code="tijiao"/></button>
			</div>
		</div>
	</form>
	<!-- end row -->
</div>
<script type="text/javascript">
seajs.use(["js/front/user/realname"],function(o){
	o.init();
});
</script>


