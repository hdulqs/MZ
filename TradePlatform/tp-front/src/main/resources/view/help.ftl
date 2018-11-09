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
<link href="${ctx}/static/${version}/lib/exstatic/css/shoufengqing.css" rel="stylesheet" />


</head>
<body >
		<@HryTopOrFooter url="base/header_news.ftl"/>
		
		<!-- 中间切换区域 -->
		<div id="content" class="content container-fluid trade-con"  style="margin: 0px; min-height: 800px;">
			<div class="col-sm-2 trade-slider"  style="margin:10px 10px 0 0;padding: 0;border: 1px solid #d8d8d8;">
	            
	            <div id="artic_category" class="slider-con" role="grid">
	                <!-- Contenedor -->
					<ul id="accordion" class="accordion">
						<#if category??>
						<#list category as c>
						<li>
							<input type="hidden" value="${c.id}"/>
							<div class="link"><i class="fa  fa-bookmark-o"></i>${c.name}<i class="fa fa-chevron-down"></i></div>
							<#if c.articles?? && (c.articles?size > 0)>
							<ul class="submenu">
								<#list c.articles as a>
								<input type='hidden' value="${a.id}">
								<li><a href="${ctx}/news/help?categoryId=${c.id}&id=${a.id}">${a.title}</a></li>
								</#list>
							</ul>
							</#if>
						</li>
						</#list>
						</#if>
						
					</ul>
	                
	            </div>
	            
	        </div>
	        <div class="col-sm-9 help-main white " style="margin:10px 0;padding:0 60px;">
 				
	            <div class="page-header" style="margin:10px 0 20px;">
	                <h1 class="f-s-18"></h1>
	            </div>
	            
	         	<div class="lastsnew-list">
                    <h4 class="lasts-ntit text-center">
                        <a  <#if article.outLink?contains('http')>href="${article.outLink}" </#if> class="ng-binding">${article.title}</a>
                    </h4>
                     <hr/>
                    <br/>
                    <h5 class="lasts-ntit text-center">
                        <div class="newstail">
                            <span id="author" class="ng-binding">
                                        <@spring.message code="bianji"/>：<a href="javascript:void(0)" title="${article.writer}" target="_blank" class="ng-binding">${article.writer}</a>
                            </span>
                            <span id="taggreen" class="ng-binding"><@spring.message code="yuedu"/>：${article.hits}</span>
                            <span id="updated" class="ng-binding"><@spring.message code="fabushijian"/>：${(article.modified?string("yyyy-MM-dd HH:dd:ss"))!}</span>
                        </div>
                    </h5>
                </div>
                <div class="lastsnew-con">
                    <div class="new-text ng-binding" >
                      ${article.content}
                    </div>
                    <div class="new-text-page">
                    </div>

                </div>
	           
	            	
	        </div>
				
				
	           
		
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
	 
	 $(function() {
		var Accordion = function(el, multiple) {
			this.el = el || {};
			this.multiple = multiple || false;
	
			// Variables privadas
			var links = this.el.find('.link');
			// Evento
			links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
		}
	
		Accordion.prototype.dropdown = function(e) {
			var $el = e.data.el;
				$this = $(this),
				$next = $this.next();
	
			$next.slideToggle();
			$this.parent().toggleClass('open');
	
			if (!e.data.multiple) {
				$el.find('.submenu').not($next).slideUp().parent().removeClass('open');
			};
		}	
	
		var accordion = new Accordion($('#accordion'), false);
	});
	 
 });
</script>
