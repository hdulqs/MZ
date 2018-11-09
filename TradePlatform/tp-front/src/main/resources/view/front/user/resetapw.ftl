<#include "/base/base.ftl">

 
 <div class="container-fluid person-con min-hg">
 
				<!-- begin page-header -->
				<div class="row" style="margin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs" role="tablist" id="RMBtab">
								<li role="presentation" class="active pull-left">
									<a href="javascript:void(0);"><@spring.message code="chongzhijiaoyimima"/></a>
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
					<div class="text-warning col-md-offset-4 col-sm-offset-4" style="margin-right:-2px;">
							<@spring.message code="mimageshi"/>
					</div>

					
					<div class="form-group">

						<label class="col-sm-4 control-label"><@spring.message code="yanzhengdenglumima"/></label>

						<div class="col-sm-4  has-feedback"><!--错误加class has-error has-feedback--->
							<input type="password" name="passWord" id="passWord"  validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})"  class="form-control" placeholder="">
						
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><@spring.message code="xinmima"/>：</label>
						<div class="col-sm-4  has-feedback">
							<input type="password" name="accountPassWord" id="accountPassWord" validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})"  class="form-control" placeholder="">
						</div>
						
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><@spring.message code="querenmima"/>：</label>
						<div class="col-sm-4  has-feedback">
							<input type="password" name="reaccountPassWord" id="reaccountPassWord" validator="required ,password" required-error-message="必填" password-error-message="{{' A99 '| translate}}格式:({{ ' C11 ' | translate }}+{{' B19  '| translate}}{{ ' C12 ' | translate }},{{ ' C15 ' | translate }}{{' B50  '| translate}}{{ ' C14 ' | translate }}6{{' B51  '| translate}})" class="form-control" placeholder="">
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-4 control-label"><@spring.message code="shoujihaoma"/>：</label>
						<div class="col-sm-4 p-l-0">
							<!--<label class="col-sm-2 control-label f-s-18">${user.phone}</label>-->
							<span class="color-g" >${user.phone!''}</span>
						</div>

					</div>

					<div class="form-group">
						<label class="col-sm-4 control-label"><@spring.message code="duanxinyanzhengma"/>：</label>
						<div class="pull-left col-sm-3">
							<input type="text" name="accountpwSmsCode" id="accountpwSmsCode"  class="control-label form-control col-sm-11 border-rad text-center"  placeholder="">
							<label id="sms_message" class=" control-label text-warning"></label>
						</div>
						<div class="col-sm-1">
							<button class="btn btn-default" type="button" id="sendsmsBtn"  ><@spring.message code="dianjihuoqu"/></button>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-4 col-sm-offset-4">
							<button type="button" id="submitBtn" class="btn btn-primary form-control"><@spring.message code="baocun"/></button>
						</div>
					</div>
				</form>
          
		
  </div>
		


 
 
 
 
 
 
 
		
<script type="text/javascript">
seajs.use(["js/front/user/resetapw"],function(o){
	o.init();
	o.sendsms();
});
</script>	

	
	
	 


 
