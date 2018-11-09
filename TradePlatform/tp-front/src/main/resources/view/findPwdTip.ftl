<!DOCTYPE html>
<html>
<head>
	<#include "/base/base.ftl">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!--<meta name="viewport" content="width=device-width, initial-scale=1">-->
    <meta name="description" content="">
    <meta name="author" content="">
	<@HryTopOrFooter url="base/title.ftl"/>
	<link rel="icon" type="image/x-icon"  />
	<link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/secret.css">
</head>
<body>
<!-- begin #page-container -->
  <div class="verifyLayout">
        <div class="main">
            <div>
                <div class="messageBox">
                    <div class="messageBox-title"><span><@spring.message code="chongzhimimaqueren"/></span></div>
                    <div class="messageBox-con">
                        <img src="${ctx}/static/${version}/lib/exstatic/img/pic_info.png">
                        <p>
                        <@spring.message code="chongzhiemail"/>
                           
                        </p>
                    </div>
                </div>
            </div>

        </div>
    </div>
<!-- end #page-container -->
</body>
</html>




