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
       <div id="pitch_content" class="ico-content">
        <div class="pitch-container ico-container">
            <nav id="pitch_nav" class="pitches-nav">
                <div id="nav_bar">
                    <ul class="nav-item clearfix">
                        <li class="active">
                            <a href="javascript:;">项目主页</a>
                        </li>
                        <li>
                            <a href="javascript:; ">项目进展<span class="count-tip">1</span></a>
                        </li>
                        <li>
                            <a href="javascript:; ">项目讨论<span class="count-tip">0</span></a>
                        </li>
                        <li>
                            <a href="javascript:; ">支持者<span class="count-tip">1400</span></a>
                        </li>
                    </ul>
                    <div class="social-sharing">
                        <ul class="bdsharebuttonbox clearfix">
                            <li>
                                <a class="icon-share icon-weibo" id="sina-share-btn"></a>
                            </li>
                            <li>
                                <a class="icon-share icon-weixin" id="weixin-share-btn">
                                    <div class="wechat-qrcode">
                                        <h4>微信扫一扫分享</h4>
                                        <div class="qrcode"></div>
                                        <div class="help">
                                            <p>微信扫一扫</p>
                                            <p>分享到朋友圈或朋友圈</p>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a class="icon-share icon-qq" id="qq-share-btn">

                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <div class="detail-bo" style="display:block;">
            <div class="project-info ">
                <div class="section clearfix ">
                    <div class="block-left ">
                        <div class="raising ongoing ">
                            <svg height="83" width="80" viewBox="0 0 83 83">
                              <path class="label-sm-color" fill="#fff" d="M0 0h78v83L42.25 63.729 0 83z"></path>
                              <path fill="#666" d="M83 9h-5V0z"></path>
                            </svg>
                            <h4>进行中</h4>
                        </div>
                        <div class="pictures-slick ">
                            <img src="" alt=" ">
                        </div>
                    </div>
                    <!----未开始de 已完成的--->
                      <div class="block-right" style="display:hidden;">
                        <div class="block-right-top">
                            <div class="project-detail-title">波场TRON</div>
                            <div class="project-detail-sub-title"></div>
                            <div class="project-detail-content">
                                波场[TRON]是基于区块链的全球自由内容娱乐体系，其目标在于通过区块链的方式，构建一个全球范围内的自由内容娱乐体系，这个体系可以实现自由、高效的自增长，并且不会被内部或外部的中心化寡头控制。
                            </div>
                        </div>
                          <!-- 已完成 -->
                        <div class="project-detail-price project-detail-price2">
                            <div>目标金额 ：ETH 6,000</div>
                            <div class="project-detail-price-content">
                                <div class="per-line-wrapper">
                                    <div class="per-line-left">ETH &nbsp;6001.56
                                    </div>
                                    <div class="per-line-right">100.03%</div>
                                </div>
                                <div class="per-line">
                                    <div class="per-line-num" style="width: 100.03%;"></div>
                                </div>
                            </div>
                        </div>
                        <div class="project-detail-time">
                            <div>已完成</div>
                        </div>
                        <div class="project-bottom-wrapper">
                            <a class="heart-wrapper2 inactive"></a>
                            <a class="cat-detail2 cat-detail4">该项目已完成</a>
                        </div>
                        <!--已完成-->
                        <!-- 未开始 -->
                        <input type="hidden" id="starttime" value="">
                        <div class="project-detail-time project-detail-time2">
                            <div>距离开始还有<span>：</span></div>
                            <div><span class="big-number remain_time" id="remain_day">48</span>
                            </div>
                        </div>
                        <div class="project-detail-price project-detail-price2 project-detail-price3">
                            <div>目标金额 ：ETH 20,000</div>
                        </div>
                        <div class="project-detail-start-time">
                            <div class="start-time-title">
                                开始时间：
                            </div>
                            <div class="start-time">
                                2017-09-09 20:00:00
                            </div>
                        </div>
                        <div class="project-bottom-wrapper">
                            <a class="heart-wrapper2 inactive">
                            </a><a class="cat-detail2 cat-detail4">该项目未开始</a>
                        </div>
                    </div>
                    <!---未开始的---->
                    <div class="block-right ">
                        <div class="support-num ">
                            <h4 class="big-number " id="supporter_num ">${appIcoProjectDTO.support}</h4>
                            <p class="normal-text ">支持人数</p>
                        </div>
                        <div class="raise-money ">
                            <span class="btc-font btc-font-family ">${appIcoProjectDTO.coinType}</span><span class="big-number " id="current_money ">${appIcoProjectDTO.getMoney}</span>
                            <div class="money-detail ">
                                    <div class="detail-box ">
                                    <div class="detail-content ">
                                        <p class="btc-font-family "><span class="detail-font-color detail-font-size ">${appIcoProjectDTO.coinType}: </span><span class="detail-num-color detail-font-size ">${appIcoProjectDTO.sumMoney}</span></p>
                                    </div>
                                </div>
                            </div>
                            <p class="normal-text ">目标金额：
                                <span class="btc-font-family ">${appIcoProjectDTO.coinType} ${appIcoProjectDTO.getMoney}</span>
                            </p>
                        </div>
                        <div class="raise-day ">
                        <#if (appIcoProjectDTO.daysRemaining)??>
                            <span class="big-number " id="remain_day ">${appIcoProjectDTO.daysRemaining}</span>
                            <input type="hidden" id="remain_time" value=" " />
                            <p class="normal-text mt5 ">剩余天数</p>
                        <#else>
                        	<span class="big-number " id="remain_day">${appIcoProjectDTO.distanceStartDate}</span>
                            <input type="hidden" id="remain_time " value=" " />
                            <p class="normal-text mt5 ">距离开始时间</p>
                        </#if>
                        </div>
                        <div class="raise-progress " style="visibility: visible ">
                            <span class="normal-text ">现在进度 </span>
                            <span class="progress-percent "><a id="progress-percent " class=" ">${appIcoProjectDTO.getMoney/appIcoProjectDTO.sumMoney}</a>%</span>
                            <div class="meter ">
                                <span class="current-percent" data-number="${appIcoProjectDTO.getMoney/appIcoProjectDTO.sumMoney}"></span>
                            </div>
                        </div>
                        <p class="describe ">此项目将在<span style="color: red;font-weight: bold ">${appIcoProjectDTO.endTime?string("yyyy-MM-dd HH:mm:ss")}</span>前结束，有任何问题需要咨询请联系客服！</p>
                        <input type="hidden" id="projectId" value="${appIcoProjectDTO.id}"/>
                        <a href="${ctx}/ico/payJump.do?projectId=${appIcoProjectDTO.id}"><button class="investment-btn goto-invest ">我要支持</button></a>
                    </div>
                </div>
            </div>
            <div class="about-project clearfix ">
                <div class="about-us ">
                    <div class="pro-introduction ">
                        <h1 style="white-space: normal; ">
                          <span style="font-family: Microsoft YaHei;" >
                            <strong>
                             <span style="font-size: 18px;">一、什么是路印协议</span>
                            </strong>
                          </span>
                            <strong style="font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 16px; ">
                            <span style="font-size: 18px;">？</span>
                          </strong>
                        </h1>
                        <p style="line-height: 2em; ">
                            <span style="color: rgb(69, 69, 69); font-size: 12px; font-family: &quot;Helvetica Neue&quot;; ">&nbsp; &nbsp;</span>
                            <span style="font-size: 16px; font-family: 微软雅黑, Microsoft YaHei&quot;; color: rgb(69, 69, 69);">路印协议</span>
                            <span style="font-family: 微软雅黑, &quot;Microsoft YaHei; font-size: 16px; ">
                            <span style="font-family: 微软雅黑, &quot;Microsoft YaHei color: rgb(69, 69, 69); "></span>
                            <span style="font-family: 微软雅黑, &quot;Microsoft YaHei; color: rgb(63, 63,63); ">Lo</span>
                            </span>
                            <span style="color: rgb(63, 63, 63); ">
                                <span style="font-size: 16px; font-family: 微软雅黑, &quot;Microsoft YaHei">opring是新一代区块链资产交易协议和交易所。它采用去中心化技术，提供零风险的代币交易所模式，并允许多家交易所通过竞争，对同样的订单进行链外撮合及链上清结算。Loopring将彻底解决现有中心化交易所模式的一些固有风险。</span></span><span style="font-size: 16px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; color: rgb(69, 69, 69); "></span>
                            <span
                                style="font-size: 14px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; color: rgb(69, 69, 69); "><br/></span>
                        </p>
                        <p style="line-height:2em; "><span style="font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; color: rgb(69, 69, 69); font-size: 16px; "></span></p>
                        <p style="line-height: 2em; "><span style="font-family: 微软雅黑; font-size: 16px;">&nbsp; &nbsp; &nbsp;</span><span style="color: rgb(63, 63, 63); ">
                          <span style="color: rgb(63, 63, 63); font-family: 微软雅黑; font-size: 16px; ">&nbsp;</span>
                          <span style="color: rgb(63, 63, 63); font-family: 微软雅黑; font-size: 16px; ">更多详情，请查看官网</span>
                            <span style="color: rgb(63, 63,63); font-family: 微软雅黑; font-size: 16px; ">：</span>
                                <strong>
                                  <span style="font-family: 微软雅黑; font-size: 16px; ">
                                    <a href=""></a>
                                  </span>
                                </strong>
                         </p>
                    </div>
                </div>
                <div class="about-right ">
                    <div class="pro-originator ">
                        <svg class="svg-cube " width="25 " height="25 ">
                        </svg>
                        <a class="originator-text ">项目发起人</a>
                        <div class="originator-info ">
                            <div class="avatar ">
                                <img src="" alt=" ">
                            </div>
                            <div class="originator-detail ">
                                <a href="# " class="originator-name originator-text ">${appIcoProjectDTO.linkman}</a>
                                <p>项目负责人</p>
                                <p>上次登录时间：2017/07/29</p>
                                <button type="button " class="message-btn ">
                                  <svg width="22" height="22" viewBox="0 0 22 22.008">
                                    <path fill="currentColor" d="M10.998 0C4.924 0-.001 4.312-.001 9.628c0 3.04 1.611 5.747 4.125 7.51v4.871l4.818-2.927c.667.111 1.353.175 2.056.175 6.075 0 11.001-4.312 11.001-9.629C21.999 4.312 17.073 0 10.998 0zm-5.5 11.006a1.376 1.376 0 1 1 .003-2.751 1.376 1.376 0 0 1-.003 2.751zm5.5 0a1.376 1.376 0 1 1 .003-2.753 1.376 1.376 0 0 1-.003 2.753zm5.501 0a1.375 1.375 0 1 1 0-2.75 1.375 1.375 0 0 1 0 2.75z"></path></svg>发送私信</button>
                            </div>
                        </div>
                    </div>
                    <div id="project_file ">
                        <div class="white-paper ">
                            <a href="javascript:; ">
                                <svg width="52px " height="50px "></svg>
                            </a>
                        </div>
                        <div class="project-paper ">
                            <h3 class="paper-title ">项目白皮书</h3>
                            <a href=" " target="_blank " class="paper-btn " id="preview_paper ">点击查看详情</a>
                        </div>
                    </div>
                    <div class="pro-invest ">
                        <div class="invest-money ">
                            <span class="large-number ">${appIcoProjectDTO.coinType}</span>
                            <span class="small-number ">/ 金额不限</span>
                            <span class="bought ">已参与${appIcoProjectDTO.support}人次</span>
                        </div>
                        <div class="invest-introduce ">
                            <h4>不限投资人数</h4>
                            <p>1个ETH=5000个LRC，从认购开启之日起，每三天为一个阶段，共10个阶段，每阶段都有相应的LRC奖励，奖励比例依次是20%， 16%， 14%， 12%， 10%， 8%， 6%，4%，2%，0%。<br/><br/></p>
                        </div>
                        <div class="invest-post ">
                            <div class="project-post ">
                            </div>
                            <div class="project-reward ">
                                <span class="circle-point "></span>
                                <span class="title ">回报时间：</span>
                                <span class="content ">预计项目ICO成功结束后90天内</span>
                            </div>
                        </div>
                        <button class="invest-btn goto-invest ">立即支持</button>
                    </div>
                </div>
            </div>
           </div>
           <div class="detail-bo" style="display:none;">
           		项目进度：
           		<div class="meter ">
	                <span class="current-percents" data-number="${appIcoProjectDTO.getMoney/appIcoProjectDTO.sumMoney}"></span>
	            </div>
	            ${appIcoProjectDTO.support}
           </div>
           <div class="detail-bo" style="display:none;">
           		<#list listeval as list>
           			${list.userName}:${list.content}
           		</#list>
           </div>
           <div class="detail-bo" style="display:none;">
           		<#list listProjectSuport as list>
           			${list.userName}      支持币个数：${list.money}
           		</#list>
           </div>
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
      "superslide": "lib/exstatic/js/jquery.SuperSlide2.js",
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
 
 seajs.use(["js/main","js/front/ico/detail","js/ueditor.all.min"],function(m,d,login){
	 m.init();
	 d.init();
 });
</script>



