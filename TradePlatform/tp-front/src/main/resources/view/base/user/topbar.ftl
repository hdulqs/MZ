<#include "/base/base.ftl">
<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css" rel="stylesheet" />
<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />
	<nav class="navbar navbar-inverse navbar-fixed-top " style="z-index: 1031; height: 30px; margin-bottom: 0; border-radius: 0; min-height: 30px;position:fixed; width:100%;">
	    <div class="container" style="margin-top: -10px;">
	        <div class="navbar-header">
	            <ul class="nav navbar-nav collapse navbar-collapse">
	                <li><a class="" href="#">客服电话:400-000-1234</a></li>
	                <li><a class="" href="#">服务时间:9:00-17:00</a></li>
	            </ul>
	        </div>
	        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		        <ul class="nav navbar-nav navbar-right">
		        	<li style="margin-top: 15px;color: #9d9d9d;"><span>客服QQ : </span></li>
		        	<li ng-repeat="list in appConfigData.serviceQQ"><a style="padding:12px 0 0 0;" target="_blank" href="https://wpa.qq.com/msgrd?v=3&uin={{list}}&site=qq&menu=yes"><img border="0" style="margin: 0 5px 0 10px;" src="https://pub.idqqimg.com/qconn/wpa/button/button_121.gif" alt="点击这里给我发消息" title="点击这里给我发消息"/>123456</a></li>
		        </ul>
	        </div>
	    </div>
	</nav>