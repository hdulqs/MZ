<#include "/base/base.ftl">
<div id="sidebar" class="sidebar col-md-2" style="margin-top: 15px; padding: 0;">
	<div data-height="100%" style="background: #fff; padding-bottom: 30px;">
		<ul class="nav">
				<li class="has-sub "><i class="slideico slideico3"></i><a href="javascript:;"><span class="ng-binding">个人账号</span><i class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
					<ul class="sub-menu " style="display: block">
						<li><i class="slideico slideico5"></i> <a src="${ctx}/user/index"   href="javascript:void(0);" >账号总览</a></li>
						<li><i class="slideico slideico7"></i> <a src="${ctx}/user/realinfo" href="javascript:void(0);">实名认证</a></li>
						<li><i class="slideico slideico7"></i> <a src="${ctx}/v.do?u=front/user/safe" href="javascript:void(0);">安全设置</a></li>
						<li><i class="slideico slideico7"></i> <a src="${ctx}/user/btc/post" href="javascript:void(0);">收货地址</a></li>
					</ul>
				</li>
				<li class="has-sub "><i class="slideico slideico3"></i><a href="javascript:;"><span class="ng-binding">项目管理</span><i class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
					<ul class="sub-menu " style="display: block">
						<li><i class="slideico slideico5"></i> <a src="${ctx}/ico/iLaunchProject"   href="javascript:void(0);" >我支持的项目</a></li>
						<li><i class="slideico slideico6"></i> <a src="${ctx}/ico/iLaunchProject" href="javascript:void(0);">我发起的项目</a></li>
						<li><i class="slideico slideico7"></i> <a src="${ctx}/ico/iLaunchProject" href="javascript:void(0);">我分享的项目</a></li>
					</ul>
				</li>
				<li class="has-sub "><i class="slideico slideico3"></i><a href="javascript:;"><span class="ng-binding">资金管理</span><i class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
					<ul class="sub-menu " style="display: block">
						<li><i class="slideico slideico5"></i> <a src="${ctx}/user/rmbdeposit/index"   href="javascript:void(0);" >资金明细</a></li>
						<li><i class="slideico slideico6"></i> <a src="${ctx}/ico/recandwit/listAccount" href="javascript:void(0);">资金充值</a></li>
						<li><i class="slideico slideico7"></i> <a src="${ctx}/ico/recandwit/icojump" href="javascript:void(0);">资金提现</a></li>
					</ul>
				</li>
		</ul>
	</div>
</div>
<div class="sidebar-bg"></div>
