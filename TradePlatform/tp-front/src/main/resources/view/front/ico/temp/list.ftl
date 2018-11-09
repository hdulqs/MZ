<!DOCTYPE html>
<html>
<head>
	<#include "/base/base.ftl">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	<!--title及ico-->
	<@HryTopOrFooter url="base/title.ftl"/>
	<link rel="icon" type="image/x-icon"  />
	<link href="${ctx}/static/${version}/lib/exstatic/css/web.min.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/global.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/common.css" rel="stylesheet" />
	<link href="${ctx}/static/${version}/lib/exstatic/css/item/app-07d4b7dc84.css" rel="stylesheet" />
</head>
<body>
	<@HryTopOrFooter url="base/topbar.ftl"/>
	<!-- begin #page-container -->
    <div id="page-container" class="in">
	    <!-- begin #header -->
        <div id="header"  class="header navbar navbar-transparent " style="background:#fff; position:fixed;" >
        	
            <!-- begin container -->
            <div class="container" style="margin-top: 42px;">
                <!-- begin navbar-header -->
                <div class="navbar-header" >
	                <style>
						.dropdown-menu {
							padding: 0;
							margin: 0;
						}
						.navbar-brand .open .dropdown-menu {
							display: inline-flex;
						}
					</style>
					
					<@HryTopOrFooter url="base/logo.ftl"/>
						
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#header-navbar">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
				</div>
                <!-- end navbar-header -->
                
             
                <!-- begin navbar-collapse -->
                <div class="navbar-collapse collapse" id="header-navbar" aria-expanded="false" style="">
                   
                  <ul class="nav navbar-nav navbar-right navbar-login" >
                  	<#if user==null>
                  	<li ng-if="user==null" class="hidden-md hidden-sm clearfix" style="margin-top:25px;">
						<a  href="${ctx}/login"  class="btn btn-blue"><@spring.message code="Login"/></a>
						<a  href="${ctx}/reg" class="btn btn-blue" style="margin-left: 5px;"><@spring.message code="register"/></a>
					</li>
					
					<#else>                   
					<li user-Islogin class="dropdown hidden-md hidden-sm clearfix">
					  <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">${user.username}<b class="caret"></b></a>
						<ul class="dropdown-menu animated fadeInLeft">
							<li><a href="${ctx}/user/center"><@spring.message code="personCenter"/></a></li>
							<li><a href="${ctx}/logout"><@spring.message code="logout"/></a></li>
						</ul>
					</li>
					</#if>
                          
                  </ul>
				<#if hasico!>
					<#include '/base/index_ico_top.ftl'>
				<#else>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown " ui-sref-active="active">
						    <a  href="${ctx}"><@spring.message code="Index"/></a>
						</li>
						<li  ui-sref-active="active"><a target="_blank" href="${ctx}/market" ><@spring.message code="Tradinghall"/></a></li>
						<li ui-sref-active="active"><a  href="${ctx}/news/index/5"  ><@spring.message code="NewsInformation"/></a>
						</li>
						<li ui-sref-active="active"><a href="${ctx}/news/help"><@spring.message code="Helpcenter"/></a></li>
                  </ul>
				</#if>
                </div>
                
                <!-- end navbar-collapse -->
            </div>
            <!-- end container -->
        </div>
        <!-- end #header -->

	            
	<!--home start-->
        <!-- begin #home -->
        <div id="home" class="content has-bg home" style="margin-top: 107px;">
            <!-- begin content-bg -->
            <div class="index_banner_box" id="index_banner_box">
                <div class="banner_big_box" id="banner_big_box">
                  <img src="${ctx}/static/${version}/lib/exstatic/img/index_banner2.jpg" style="height:100%; width:100%;"/>
                </div>
                <div class="index_t">
                    <div class="banner_box" id="banner_box"></div>
                    <div class="section floor_top">
                        <!-- 轮播的页码  开始 -->
                        <ul id="page_list"></ul>
                    </div>
                </div>
            </div>
            <!-- end content-bg -->
            <!-- begin login -->
            <#if user==null>
            <div class="index_login_box" >
                <div class="login_box">
                    <div class="opacity_bg"></div>
                    <!--登录页面-->
                    <div class="tab tab01">
                        <form method="post"  class="form_login">
                            <h4 class="title"><@spring.message code="Login"/></h4>
                            <p>
                                <input type="text"  id="username" name="username" class="input_login mail_complete" data-type="*" data-msg-null="请输入邮箱或手机" value="" placeholder="<@spring.message code="please_write_tel"/>" autocomplete="off" />
                            </p>
                            <div class="relative">
                                <div class="mail_complete_list absolute" style="display: none;"></div>
                            </div>
                            <p>
                                <input type="password" id="password" name="password" class="input_login" data-type="*" data-msg-null="请输入密码" placeholder="<@spring.message code="please_write_pwd"/>" />
                            </p>

                            <div class="form_tip"></div>
                            <p><button class="btn btn_orange sign_btn loading" id="loginBtn" type="button"><@spring.message code="Login"/></button></p>
                            <p class="help"><a href="${ctx}/forgetpwd/forgetpwd/1" target="_blank"><@spring.message code="forgotpassword"/>？</a> <a href="${ctx}/reg" target="_blank" class="pull-right"><@spring.message code="register"/></a></p>

                            <input type="hidden" name="step" value="" />
                        </form>
                        <hr class="login_hr">
                        <p class="hr_tis"><i class="icon_tis"></i> <a href=""> <@spring.message code="zhuanyejiagou_lengqiaobao"/></a></p>
                    </div>
                </div>
            </div>
            </#if>
            <!-- end login -->
        </div>
        <!-- end #home -->
        
<!--pitch content start--> 
        <div class="main-wrap" id="pitches_content">
        <section class="pitches-nav">
            <nav id="pitches_nav">
                <div id="nav_bar">
                    <ul class="nav-item clearfix">
                        <li data-type="all" <#if class=='class'>class="active"</#if>>
                            <a href="${ctx}/ico/listProject.do?isDisabled=all">全部<span class="count-tip">${sizeAll}</span>
                            </a>
                        </li>
                        <li data-type="runable" <#if class3=='class'>class="active"</#if>>
                            <a href="${ctx}/ico/listProject.do?isDisabled=jjks">即将开始<span class="count-tip">${sizeJjks}</span>
                            </a>
                        </li>
                        <li data-type="running" <#if class4=='class'>class="active"</#if>>
                            <a href="${ctx}/ico/listProject.do?isDisabled=jxz">进行中<span class="count-tip">${sizeJxz}</span>
                            </a>
                        </li>
                        <li data-type="end" <#if class5=='class'>class="active"</#if>>
                            <a href="${ctx}/ico/listProject.do?isDisabled=ywc">已完成<span class="count-tip">${sizeYwc}</span>
                            </a>
                        </li>
                        <li data-type="library" <#if classn=='class'>class="active"</#if>>
                            <a href="${ctx}/ico/listProject.do?isDisabled=xmk">项目库<span class="count-tip">${sizeXmk}</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
        </section>
        <section id="container" class="clearfix">
            <div class="content-body">
                <div class="project-show">
                    <div class="wrapper">
                        <div class="text-center pitches-loading">
                            <div id="loading" style="display: none;">
                                <p class="b-line"></p>
                                <p class="b-text">正在加载项目，请稍候</p>
                            </div>
                        </div>
                        <div class="flex-row">
                        	<#list list as list>
                            <div class="li-item">
                                <div class="block-top">
                                    <span class="status-tag">
                                      <svg width="104px" height="85px" class="ready" viewBox="0 0 104 85">
                                        <title>Page 1</title>
                                        <path class="status-color" fill="#fff" d="M34.56 0L0 28v56.32L104 0z" fill-rule="evenodd">
                                        </path>
                                      </svg>
                                     <span class="ready-txt">
                                     <#if list.status==3>即将开始
                                     <#elseif list.status==4>进行中
                                     <#elseif list.status==5>已完成
                                     </#if>
                                     </span></span>
                                    <a href="" class="pro-pic"><img src="" alt=""></a>
                                </div>
                                <div class="block-bottom">
                                    <h4><a href="" class="pro-title" title="">${list.projectName}</a></h4>
                                    <h5>${list.info}</h5>
                                    <div class="raise-funds">
                                        <div class="raise-money"><a class="raise-already btc-font-family">${list.coinType}&nbsp;${list.sumMoney}</a></div>
                                        <div class="raise-progress">
                                            <div class="meter  ">
                                            </div>
                                            <a class="progress-percent">${list.getMoney/list.sumMoney}%</a>
                                        </div>
                                    </div>
                                    <ul class="raise-info clearfix">
                                        <li class="right-bar"><a class="raise-info-val">${list.sumMoney} ${list.coinType}</a><span>目标金额</span>
                                        </li>
                                        <li class="right-bar pl15"><a class="raise-info-val"><#if (list.daysRemaining)??>${list.daysRemaining}<span>剩余天数</span><#else>距离开始时间：${list.distanceStartDate}</#if></a>
                                        </li>
                                        <li class="pl15">
                                            <a class="raise-info-val">${list.support}</a>
                                            <span>支持人数</span>
                                        </li>
                                    </ul>
                                    <div class="bottom-wrapper">
	                                    <#if list.status==3><a class="cat-detail" href="${ctx}/ico/getProject.do?id=${list.id}">查看详情</a>
	                                     <#elseif list.status==4><a class="cat-detail" href="${ctx}/ico/getProject.do?id=${list.id}">我要参与</a>
	                                     <#elseif list.status==5><a class="cat-detail" href="${ctx}/ico/getProject.do?id=${list.id}">查看详情</a>
	                                    </#if>
                                    </div>
                                    
                                </div>
                            </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
            <div class="text-center pitches-end">
                <button class="load-more" style="display: none;">查看更多</button>
                <div id="no-anymore" style="display: none;">
                    <p class="b-line"></p>
                    <p class="b-text">已经到底部了哦~</p>
                </div>
            </div>
        </section>
    </div>
<!--pitch content end-->       
        <div class="flinks" style="padding-bottom: 182px;">
        </div>
      	 <div class="page-footer"   style="margin-top: 0px;">
        	<@HryTopOrFooter url="base/footer.ftl"/>
        </div>
        
        
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
		['.js','.js?v='+new Date().getTime()]//映射规则
	]
  });
 
 seajs.use(["js/main","js/index","js/ueditor.all.min"],function(m,obj,login){
	 m.init();
	 obj.lunbo();
	 obj.gundong();
	 obj.init();
	 //登录方法初始化;
	 login.init();
 });
</script>



