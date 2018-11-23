<meta name="renderer" content="webkit"> 
<#import "/base/spring.ftl" as spring/>
<!-- 时间戳 -->
<#assign t="${.now?long}">
<!-- js版本 -->
<#assign version="src">
<#assign domain="http://127.0.0.1:9003">
<#assign ctx="">
<#assign i18n="true">
<#assign  lend="false">
<#assign  lendMoney="false">
<#assign  lendTimes="true">
<#assign  lendCoin="false">
<#assign  hasc2c="true">
<#assign  hasotc="true">
<#assign  marketPrice="false">
<#assign socketioUrl="wss://hq.babyex.net">
<script type="text/JavaScript">
	var _ctx = "${ctx}";
	var _version="${version}";
	var _domain ="${domain}";
	var _hasico ="${hasico}";
    var hry_socketioUrl ="${socketioUrl}";
</script>

