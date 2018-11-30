<#include "/base/base.ftl">

<script type="text/JavaScript">
	window.nofind=function(){
		var img=event.srcElement;
		img.src=_ctx+"/static/"+_version+"/img/drd/logo.png";
		img.onerror=null;
	}
</script>
<style type="text/css">
	.navbar-brand{
		/*position: absolute;
		z-index: 2;
		width: 100%;
		height: 60px;
		line-height: 60px;
		margin-top: 80px;*/
	}
	.navbar-brand img{
		display: block;
		margin: 5px auto;
		/*width: 206px;*/
		height: 55px;
	}
</style>
<div class="navbar-brand ng-scope">
	<a href="${ctx}/index">
		<img id="logo_img" src="${siteLogo!}"/>
		<!--<img id="logo_img" src="${ctx}/static/${version}/img/drd/logo.png"/>-->
 	</a>
</div>