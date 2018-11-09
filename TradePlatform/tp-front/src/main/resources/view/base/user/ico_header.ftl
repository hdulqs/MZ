<#include "/base/base.ftl">
<div id="header" class="header navbar navbar-default navbar-fixed-top">
	<div class="container include_fit" style="padding-top: 42px;">
		<div class="navbar-header ">
			<style >
				.dropdown-menu {
					padding: 0;
					margin: 0;
				}
				
				.navbar-brand .open .dropdown-menu {
					display: inline-flex;
				}
			</style>

			<@HryTopOrFooter url="base/logo.ftl"/>

			<button type="button" class="navbar-toggle collapsed ng-scope" data-toggle="collapse" data-target="#header-navbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
		</div>

		<div class="collapse navbar-collapse" id="header-navbar">

			<ul class="nav navbar-nav navbar-right navbar-login">

				<li class="dropdown navbar-user"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" style="padding: 15px 0;"> <span class="hidden-xs ng-binding" style="color: #1a85e3; background: #f0f3f5; padding-left: 10px; font-size: 12px; font-weight: 600; display: inline-block; height: 35px; line-height: 1px;"> 您好, ${user.username} <span style="border-left: 1px solid #e6e4e5; display: inline-block;"> <i style="margin: 10px;" class="fa fa-angle-down"></i>
						</span>
					</span>
				</a>
					<ul class="dropdown-menu animated fadeInLeft" style="min-width: 152px !important;">
						<li class="arrow"></li>

						<li><a href="javascript:void(0);" onclick="loadUrl('${ctx}/v.do?u=front/user/safe')">设置</a></li>
						<li class="divider"></li>
						<li><a href="${ctx}/logout" >退出</a></li>
					</ul></li>

			</ul>

			<!-- ngInclude: 'static/views/common/layout/nav.html' -->
			<ul class="nav navbar-nav navbar-right ng-scope" ng-include="&#39;static/views/common/layout/nav.html&#39;">
				<#include '/base/index_ico_top.ftl'>
			</ul>
		</div>
	</div>
</div>
