<html  lang="en">
<head>
<#include "/base/base.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="" name="description">
<meta content="" name="author">
<@HryTopOrFooter url="base/title.ftl"/>
<!-- ================== BEGIN BASE CSS STYLE =============== -->
<link href="${ctx}/static/${version}/lib/exstatic/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/animate.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/web-responsive.min.css" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/mdefault.css" id="theme" rel="stylesheet">
<link href="${ctx}/static/${version}/lib/exstatic/css/test.less" rel="stylesheet/less" type="text/css">
<link href="${ctx}/static/${version}/lib/exstatic/css/style.min.css" rel="stylesheet" />
<script type="text/javascript">var m1="新闻资讯";</script>

</head>
<body >
		<@HryTopOrFooter url="base/header_news.ftl"/>
		
		<!-- 中间切换区域 -->
		<div id="content" class="content container-fluid trade-con"  style="margin: 0px; min-height: 800px;">
			<div class="col-sm-2 trade-slider"  style="margin:10px 10px 0 0;padding: 0;border: 1px solid #d8d8d8;">
	            
	            <div id="artic_category" class="slider-con" role="grid">
	                <ul class="list-group">
	                  <#if category??>
	                  <#list category as obj>
					  <li  style="border-bottom: 1px solid #efefef;padding-right: 30px;"><a href="${ctx}/news/index/${obj.id}" categoryid="${obj.id}">${obj.name}</a></li>
					  </#list>
					  </#if>
					</ul>
	            </div>
	            
	        </div>
	        <div class="col-sm-9 help-main white " style="margin:10px 0;padding:0 60px;">
 				
 			
	            <div class="page-header" style="margin:10px 0 20px;">
	                <h1 class="f-s-18">${thiscategory.name}</h1>
	            </div>
	            <hr/>
	            <#if frontPage??>
	            <#list  frontPage.rows as obj>
	            <div class="latestnews">
	                <!--start:循环-->
	                <div ng-repeat = "list1 in articleList" class="lastsnew-list">
	                    <h4 class="lasts-ntit">
	                        <a  href="${ctx}/news/info/${obj.id}"  data-ng-bind-html="list1.title|trustHtml">${obj.title}</a>
	                    </h4>
	                    <div class="media media-sm">
	                        <a class="media-left" href="${ctx}/news/info/${obj.id}" >
	                            <img  src="${obj.titleImage}"   width="130"  height="130" alt="" class="media-object" style="width:130px;" />
	                        </a>
	                        <div class="media-body">
	                            <p ></p>
	                            <div class="newstail">
                                    <span id="author"><@spring.message code="bianji"/>：${obj.writer}</span>
	                                <span id="taggreen"><@spring.message code="yuedu"/>：${obj.hits}</span>
	                                <span id="updated"><@spring.message code="fabushijian"/>：${obj.created?string("yyyy-MM-dd")}</span>
	                            </div>
	                        </div>
                    	</div>
	                </div>
	            </div>
	            </#list>
	            </#if>
	           
	           	<#include "/base/pagebar.ftl">
	            	
	        </div>
			 <!----------文章详情页开始---------->
	            <!--<div class="col-sm-9 help-main white " style="margin:10px 0;padding:0 60px;">
		            <div class="page-header ng-scope">
	                <h1 class="f-s-18 ng-binding">最新动态</h1>
	            </div>
	            <div class="latestnews ng-scope">
                <#--start:循环-->
                <div class="lastsnew-list">
                    <h4 class="lasts-ntit text-center">
                       <a  class="ng-binding">鑫特币注意事项</a>
                        <div class="newstail">
                            <span id="author" class="ng-binding">
                                                                               编辑：<a href="javascript:void(0)" title="金鼎世界" target="_blank" class="ng-binding">金鼎世界</a>
                            </span>
                            <span id="taggreen" class="ng-binding">阅读：805</span>
                            <span id="updated" class="ng-binding">更新：2017-07-06 22:10:08</span>
                        </div>
                    </h4>
                </div>
                <div class="lastsnew-con">
                    <div class="new-text ng-binding" >
                      <p style="white-space: normal;">尊敬的金鼎世界用户，</p>
                      <p style="white-space: normal;">科技发展日新月异，平台也对数字货币进行了升级，在此特别提醒您注意金鼎世界区块链（Central Coin)2.0版上线后的以下事项：</p>
                      <ol class=" list-paddingleft-2" style="width: 889.188px; white-space: normal;">
                        <li>
                         <p>用户使用不同浏览器或浏览器缓存的影响可能在进入交易大厅不能正常交易，此时可点击交易大厅左上角的CTC，即可解决。</p>
                        </li>
                        <li>
                         <p>新钱包的下载链接将于近日在平台上提供，请留意平台公告。</p>
                        </li>
                        <li>
                         <p>此次换币，原钱包及币将不再使用；原本从平台转出到冷钱包的用户可联系平台客服，客服将会统计并联系技术统一解决。</p>
                        </li>
                        <li>
                         <p>新币的提币及收币地址都是以C开头，所以用户如果从平台上提币，则需要重新添加提币地址。</p>
                        </li>
                        <li>
                         <p>平台将把以上注意事项做成课件，并辅以详图。用户可向平台客服索取课件。</p>
                         <p><br></p>
                         <p style="line-height: 16px;"><br></p>
                        </li>
                        </ol>
                        <p>金鼎世界</p><p>2017年7月6日</p>
                      </div>
                    <div class="new-text-page">
                        <p>
                            <a href=" " class="ng-binding"> 上一篇: 7月4日开盘时间延后2小时
                            </a>
                        </p>
                        <p>
                            <a href=" " class="ng-binding">下一篇:鑫特币注意事项
                            </a>
                        </p>
                    </div>

                </div>
                <#--end:循环-->
				
				
            </div>-->
	           <!----------文章详情页结束----------> 	 
	           
		
		</div>
		
		<!-- 底部锚点 -->
		<a href="javascript:void(0);" class="btn btn-icon btn-circle btn-success btn-scroll-to-top fade" data-click="scroll-top"><i class="fa fa-angle-up"></i></a>
	</div>
	<div class="page-footer col-md-12 col-sm-12 ng-scope"  >
			<@HryTopOrFooter url="base/footer.ftl"/>
	</div>
</body>
</html>

	
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript">
 seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
      "jqueryForm": "lib/jqueryForm/jquery.form.js",
      <!-- layer -->
      "layer" : "lib/layer/layer.js",
      <!-- 自定义JS -->
      "base": "js/base/base.js"
    },
    preload: ['jquery','jqueryForm','layer'],
    map:[
		['.js','.js?v=${t}']//映射规则
	]
  });
 
 seajs.use("js/front/main_news",function(m){
	 m.init();
 });
</script>