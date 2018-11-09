<#include "/base/base.ftl">

<script type="text/JavaScript">
	window.nofind=function(){
				var img=event.srcElement; 
				img.src=_ctx+"/static/"+_version+"/lib/exstatic/img/logo.png"; 
				img.onerror=null;
	}
</script>

		<a href="/">
		  		<img id="logo_img" src="${siteLogo!}" style="height: 50px;margin-bottom:20px;"/>
 		 </a>
