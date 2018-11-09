<style>
	i.form-control-feedback {
		right: 10px;
	}
</style>
<!-- begin page-container -->
<div class="container-fluid person-con">
	<!-- begin page-header -->
	<div class="row" style="margin-bottom:15px;">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" id="RMBtab">
					<li role="presentation" class="active pull-left">
						<a href="javascript:void(0);">我发起的项目</a>
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
				<form class="form-inline input-daterange clearfix ng-pristine ng-valid">

					<div class="form-group form-labels  col-md-12" id="status">
						 <label for="" >项目状态：</label>
						 <a class="label ng-binding selected" value="">全部</a>
						 <a class="label ng-binding" value="0">未提交</a>
						 <a class="label ng-binding" value="1">待审核</a>
						 <a class="label ng-binding" value="4">进行中</a>
						 <a class="label ng-binding" value="5">已成功</a>
						 <a class="label ng-binding" value="3">即将开始</a>
						 <a class="label ng-binding" value="6">失败</a>
						 <a class="label ng-binding" value="2">未通过</a>
					</div>
				</form>
			</div>
			<!-- end col-12 -->
	</div>
	<div class="row">
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
	<!-- end row -->
</div>

<#include "/base/base.ftl">
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
seajs.config({
	base: "${ctx}/static/${version}",
	preload: ['rmbWithdraw'],
	map:[['.js','.js?v='+new Date().getTime()]]
});
seajs.use(["js/front/ico/iLaunchProject"],function(launch){
	launch.init();
});
</script>
