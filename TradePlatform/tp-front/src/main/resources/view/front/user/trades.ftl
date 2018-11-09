<#import "/base/spring.ftl" as spring/>
<div class="container-fluid person-con">
	<div role="tabpanel">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" >
					<li role="presentation" class="active pull-left">
						<a href="#current" aria-controls="current" role="tab" data-toggle="tab"><@spring.message code="jiaoyijilu"/></a>
					</li>
				</ul>
			</div>
					
		</div>







		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="current">
						
						
						
						
	<div class="row">
			<!-- begin col-12 -->
			<div class="col-md-12 col-sm-12">
				<form class="form-inline input-daterange clearfix ng-pristine ng-valid">

					<div class="form-group form-labels  col-md-12 col-sm-12" id="type">
						 <label for="" ><@spring.message code="jiaoyileixing"/>：</label>
						  <a class="label ng-binding selected" value="0"><@spring.message code="quanbu"/></a>
						  <a class="label ng-binding" value="1"><@spring.message code="mai3"/></a>
						  <a class="label ng-binding"  value="2" ><@spring.message code="mai4"/></a>
						 
					</div>
				</form>
			</div>
			<!-- end col-12 -->
	</div>
						
						
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
	</div>

</div>
<script type="text/javascript">
seajs.use(["js/front/user/trades"],function(o){
	o.init();
});
</script>

