<#include "/base/base.ftl">

<div class="container-fluid person-con min-hg">

			<!-- begin page-header -->
			<div class="row" style="margin-bottom:15px;">
				<div class="panel_wrap_head wrap_head">
					<div class="">
						<ul class="wrap_tabs" role="tablist" id="RMBtab">
							<li role="presentation" class="active pull-left">
								<a href=""><@spring.message code="shezhijiaoyimima"/></a>
							</li>

						</ul>
					</div>
				</div>
			</div>
			<!-- end page-header -->

			<!-- begin row -->
			<div class="bg-changeTxt p-20">
				<@spring.message code="setapw_tishi"/>
			</div>

			<form class="form-horizontal p-t-30 set-pwd form-all" ng-submit="processForm()">
				<div class="form-group">
					<label class="col-sm-4 control-label"><@spring.message code="jiaoyimima"/>：</label>
					<div class="col-sm-4 has-success has-feedback"><!--错误加class has-error has-feedback--->
						<input type="password" name="accountPassWord" id="accountPassWord" required  class="form-control" placeholder="">
					</div>

				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><@spring.message code="querenmima"/>：</label>
					<div class="col-sm-4 has-success has-feedback"><!--{{' B5 '| translate}}加class has-success has-feedback-->
						<input type="password" name="reaccountPassWord" id="reaccountPassWord"  required class="form-control" placeholder="">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label"><@spring.message code="shoujihaoma"/>：</label>
					<div class="col-sm-4 p-l-0">
						<!--<label class="col-sm-2 control-label f-s-18">${user.phone!''}</label>-->
						<span class="color-g" >${user.phone!''}</span>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label"><@spring.message code="duanxinyanzhengma"/>：</label>
					<div class="col-sm-1">
						<button class="btn btn-default " type="button" id="sendsmsBtn"   ><@spring.message code="dianjihuoqu"/></button>
					</div>
					<div class="pull-left col-sm-3"> 
					
						<input type="text" id="accountpwSmsCode" name="accountpwSmsCode" required class="control-label col-sm-offset-1 col-sm-11 border-rad " style="text-align:center;" placeholder="">
						<label  id="sms_message" class=" control-slabel text-warning"></label>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-4">
						<button type="button" id="submitBtn" class="btn btn-primary form-control"><@spring.message code="baocun"/></button>
					</div>
				</div>
			</form>
		
</div>
		


 
		
<script type="text/javascript">
seajs.use(["js/front/user/setapw"],function(o){
	o.init();
	o.sendsms();
});
</script>	

	
	
	 


 
