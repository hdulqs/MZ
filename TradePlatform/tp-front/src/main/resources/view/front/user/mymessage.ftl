<#import "/base/spring.ftl" as spring/>
<div class="container-fluid person-con">
	<div role="tabpanel">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" >
					<li role="presentation" class="active pull-left">
						<a href="#current" aria-controls="current" role="tab" data-toggle="tab"><@spring.message code="wodexiaoxi"/></a>
					</li>
				</ul>
			</div>
					
		</div>

		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="current">
						
				<table   id="table"
		 	           data-toolbar="#toolbar"
		 	           data-show-refresh="true"
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
seajs.use(["js/front/user/mymessage"],function(o){
	o.init();
});
</script>

