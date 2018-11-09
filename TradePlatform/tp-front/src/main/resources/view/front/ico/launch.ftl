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
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/lib/jquery.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
<script>
/**
	$.validator.setDefaults({
	    submitHandler: function() {
	      //alert("提交事件!");
	    }
	});
	$().ready(function() {
	    $('form[name="form-two"]').validate();
	});
	**/
</script>
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
        <div class="launch-container ico-container">
            <nav id="launch_nav">
                <div id="nav_bar">
                    <ul class="nav-item clearfix">
                        <li class="nav-list-one ready">
                            <svg class="svg-step" width="250px" height="55px" viewBox="0 0 227 56">
                              <g fill="none" fill-rule="evenodd"><path class="step-content" fill="#FBFCFD" d="M1 55V1h210.113l14.57 27.496L211.117 55z"></path><path class="step-border" d="M.5.5v55h210.914l14.836-27-14.836-28H.5zm1 1h209.312l14.302 26.992L210.822 54.5H1.5v-53z" fill="#CAC9C9"></path></g></svg>
                            <div class="item-step">
                                <a href="javascript:" class="nav-item-step" id="nav_item_first" data-step="one" data-auth="1">
                                    <svg class="svg-icon" height="24" viewBox="0 0 27 26">
                                      <path class="launch-person" fill-rule="evenodd" clip-rule="evenodd" fill="currentColor" d="M16.057 17.612c-.485-.191-.862-.41-1.131-.651a2.52 2.52 0 0 1-.6-.768 2.158 2.158 0 0 1-.222-.809c-.018-.269 0-.534.053-.794.069-.436.208-.743.416-.925.208-.184.479-.404.807-.665.139-.121.265-.292.379-.507.112-.218.212-.438.298-.665.089-.261.176-.538.262-.833.173-.052.337-.146.495-.287.139-.12.265-.29.378-.508.113-.216.187-.507.221-.872a1.989 1.989 0 0 0-.013-.702 1.734 1.734 0 0 0-.17-.47.877.877 0 0 0-.286-.337c0-.713-.043-1.416-.13-2.11a12.561 12.561 0 0 0-.442-1.914 5.469 5.469 0 0 0-.991-1.837 3.79 3.79 0 0 0-.794-.729 6.17 6.17 0 0 0-1.159-.653 7.714 7.714 0 0 0-1.396-.457 6.752 6.752 0 0 0-1.497-.17A8.85 8.85 0 0 0 9.298.04a5.435 5.435 0 0 0-1.276.354 5.701 5.701 0 0 0-1.251.715 4.877 4.877 0 0 0-1.107 1.187 5.472 5.472 0 0 0-.822 1.719 12.504 12.504 0 0 0-.48 3.776c-.157.174-.27.355-.34.548a2.327 2.327 0 0 0-.142.624c-.026.242.012.511.118.808.086.295.191.521.313.678s.233.277.337.364c.122.086.243.147.365.183.088.294.174.571.261.832.088.227.183.447.286.665.104.215.226.385.365.507.294.243.563.481.807.717.243.233.383.551.416.95.018.278.027.529.027.754 0 .227-.044.443-.13.653a2.153 2.153 0 0 1-.417.624c-.19.208-.467.427-.833.651a5.67 5.67 0 0 1-1.614.678c-.609.157-1.195.335-1.759.533a5.09 5.09 0 0 0-1.484.808c-.427.34-.682.83-.77 1.473A14.37 14.37 0 0 0 .013 22.3H0v3.751h17.877V18.194a23.215 23.215 0 0 0-.399-.113c-.513-.14-.986-.296-1.421-.469zM25.918 7.914H19.33v2.163H27V7.914zM20.411 14.458H19.33v2.163H27v-2.163h-1.082zM20.411 21.001H19.33v2.163H27v-2.163h-1.082z"></path></svg>
                                    <span class="item-txt">完善个人信息</span>
                                </a>
                            </div>
                        </li>
                        <li class="nav-list-two active">
                            <svg class="svg-step" width="250px" height="55px" viewBox="0 0 227 56">
                              <g fill="none" fill-rule="evenodd"><path class="step-content" fill="#FBFCFD" d="M1.665 55l14.378-27.504L1.68 1h209.774l14.567 27.496L211.459 55z"></path><path class="step-border" d="M.84.5l14.637 27L.84 55.5h210.914l14.837-27-14.837-28H.84zm15.523 27.464l.247-.472-.254-.468L2.519 1.5h208.633l14.302 26.992L211.163 54.5H2.49l13.872-26.536z" fill="#6671AB"></path></g></svg>
                            <div class="item-step">
                                <a href="javascript:" class="nav-item-step" id="nav_item_second" data-step="two" data-auth="1">
                                    <svg class="svg-icon" height="24" viewBox="0 0 22 26">
                                      <path class="launch-info" fill-rule="evenodd" clip-rule="evenodd" fill="currentColor" d="M15.714 6.659H22v-.005L15.714.308z"></path><path class="launch-info" fill-rule="evenodd" clip-rule="evenodd" fill="currentColor" d="M15.714 8.23h-1.571V.308H0V25.691h22V8.23h-6.286zm1.553 11.381H4.732v-2.035h12.534v2.035zm0-5.103H4.732v-2.037h12.534v2.037z"></path></svg>
                                    <span class="item-txt">项目基本信息</span>
                                </a>
                            </div>

                        </li>
                        <li class="nav-list-three ">
                            <svg class="svg-step" width="250px" height="55px" viewBox="0 0 227 56">

                              <g fill="none" fill-rule="evenodd"><path class="step-content" fill="#FBFCFD" d="M1.665 55l14.378-27.504L1.68 1h209.774l14.567 27.496L211.459 55z"></path><path class="step-border" d="M.84.5l14.637 27L.84 55.5h210.914l14.837-27-14.837-28H.84zm15.523 27.464l.247-.472-.254-.468L2.519 1.5h208.633l14.302 26.992L211.163 54.5H2.49l13.872-26.536z" fill="#6671AB"></path></g></svg>
                            <div class="item-step">
                                <a href="javascript:" class="nav-item-step" id="nav_item_third" data-step="three" data-auth="0">
                                    <svg class="svg-icon" height="24" viewBox="0 0 22 25">

                                      <path class="launch-introduction" fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M1.61 22.377V1.594H13.897L12.312-.019H-.001v23.996h1.61v-1.6zM15.103 9.724H22v-.085l-6.897-7.015v7.1z"></path><path class="launch-introduction" fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M14.271 10.555V2.624H2.791V25.02H22V10.556h-7.729zM6.776 9.047h5.211v1.352H6.776V9.047zm11.239 13.004H6.776v-1.352h11.239v1.352zm0-3.883H6.776v-1.352h11.239v1.352zm0-3.885H6.776v-1.352h11.239v1.352z"></path></svg>
                                    <span class="item-txt">项目详细介绍</span>
                                </a>
                            </div>

                        </li>
                        <li class="nav-list-four ">
                            <svg class="svg-step" width="250px" height="55px" viewBox="0 0 227 56">

                              <g fill="none" fill-rule="evenodd"><path class="step-content" fill="#FBFCFD" d="M1.665 55l14.378-27.504L1.68 1h209.774l14.567 27.496L211.459 55z"></path><path class="step-border" d="M.84.5l14.637 27L.84 55.5h210.914l14.837-27-14.837-28H.84zm15.523 27.464l.247-.472-.254-.468L2.519 1.5h208.633l14.302 26.992L211.163 54.5H2.49l13.872-26.536z" fill="#6671AB"></path></g></svg>
                            <div class="item-step">
                                <a href="javascript:" class="nav-item-step" id="nav_item_fourth" data-step="four" data-auth="0">
                                    <svg class="svg-icon" height="24" viewBox="0 0 40 27">

                                      <path class="launch-plan" fill="currentColor" d="M28.496 12.504c.381-.313.566-.752.566-1.309 0-.986-.674-1.47-2.031-1.47l-1.401.005.005 3.252h1.27c.673 0 1.2-.161 1.591-.478zM27.08 14.149l-1.44.005.005 3.623h1.68c.723 0 1.289-.166 1.689-.488.4-.327.586-.781.586-1.357 0-1.187-.84-1.787-2.52-1.783z"></path><path class="launch-plan" fill="currentColor" d="M26.514 3.207c-5.688 0-10.298 4.609-10.298 10.293s4.609 10.293 10.298 10.293c5.684 0 10.293-4.609 10.293-10.293S32.197 3.207 26.514 3.207zm3.554 14.912a3.396 3.396 0 0 1-1.27.654v1.377h-1.641v-1.191h-1.23v1.191l-1.631.01-.005-1.201h-.039l-1.782.01V17.25h1.777l-.015-7.085h-1.777l-.005-1.704 1.816-.005V7.362h1.636v1.187h1.235V7.362l1.641-.005v1.387c.332.107.645.259.898.459.576.43.859 1.011.859 1.733 0 .571-.166 1.079-.508 1.514-.332.444-.801.752-1.387.938v.029c.732.073 1.328.342 1.768.796.439.459.654 1.035.664 1.729.002.898-.34 1.621-1.004 2.177z"></path><path class="launch-plan" fill="currentColor" d="M26.514.014c-5.142 0-9.609 2.871-11.89 7.104H4.473a.639.639 0 1 0 0 1.275h9.551c-.332.82-.591 1.675-.762 2.559H3.125a.64.64 0 1 0 0 1.28h9.956a13.674 13.674 0 0 0 .004 2.564H1.489c-.352 0-.64.288-.64.64s.288.64.64.64h11.777c.171.874.425 1.714.752 2.524H3.247c-.352 0-.64.283-.64.635s.288.645.64.645h11.372a13.692 13.692 0 0 0 1.797 2.568H.64a.636.636 0 0 0-.64.642c0 .352.288.635.64.635h17.075a13.441 13.441 0 0 0 8.799 3.262C33.965 26.986 40 20.951 40 13.5S33.965.014 26.514.014zm0 25.693c-6.748 0-12.212-5.459-12.212-12.207 0-6.743 5.464-12.212 12.212-12.212 6.738 0 12.207 5.469 12.207 12.212 0 6.748-5.469 12.207-12.207 12.207z"></path></svg>
                                    <span class="item-txt">设置投资回报</span>
                                </a>
                            </div>
                        </li>
                        <li class="nav-list-five ">
                            <svg class="svg-step" width="239px" height="55px" viewBox="0 0 226 56">

                              <g fill="none" fill-rule="evenodd"><path class="step-content" fill="#FBFCFD" d="M1.665 55l14.378-27.504L1.68 1h223.202v54z"></path><path class="step-border" d="M.84.5l14.637 27L.84 55.5h224.542V.5H.84zm15.523 27.464l.247-.472-.254-.468L2.519 1.5h221.863v53H2.49l13.872-26.536z" fill="#6671AB"></path></g></svg>
                            <div class="item-step">
                                <a href="javascript:" class="nav-item-step" id="nav_item_fifth" data-step="five" data-auth="0">
                                    <svg class="svg-icon" height="24" viewBox="0 0 21 24">

                                      <path class="launch-submit" fill-rule="evenodd" clip-rule="evenodd" fill="currentColor" d="M20.764 21.958l-2.519-2.519a5.442 5.442 0 0 0 .909-3.006 5.457 5.457 0 1 0-5.456 5.456 5.41 5.41 0 0 0 3.006-.91l2.519 2.52a.811.811 0 0 0 1.14 0l.401-.402a.805.805 0 0 0 0-1.139zm-4.121-4.125a3.266 3.266 0 0 1-1.545 1.544 3.244 3.244 0 0 1-1.403.326 3.273 3.273 0 1 1 3.273-3.273c0 .505-.123.978-.325 1.403zm-10.068-.381h-2.67c-.456 0-.83-.374-.83-.83s.374-.83.83-.83H6.53a7.12 7.12 0 0 1 .563-2.217H3.905a.833.833 0 0 1-.83-.831c0-.457.374-.831.83-.831h4.204a7.138 7.138 0 0 1 3.126-2.216h-7.33c-.456 0-.83-.374-.83-.831s.374-.831.83-.831h12.803c.456 0 .83.374.83.831s-.374.831-.83.831h-.629a7.176 7.176 0 0 1 4.533 5.044V.268H0V23.3h11.654a7.171 7.171 0 0 1-5.079-5.848zM3.905 4.145h12.803c.456 0 .83.374.83.831s-.374.831-.83.831H3.905c-.456 0-.83-.374-.83-.831s.374-.831.83-.831z"></path></svg>
                                    <span href="javascript:" class="item-txt">提交审核</span>
                                </a>
                            </div>
                        </li>
                    </ul>
                </div>
            </nav>
            <!-- launch content -->
            <div class="launch-content">
                <div class="launch-part-one launch-part" data-part="one">
                    <div class="launch-person-info">
                    	<!-- 项目id -->
                     	<input type="hidden" name="id" id="id" value="${id!}" maxlength="500">
                     	<!-- 项目步骤 -->
                      	<input type="hidden" name="step" id="step" value="${step!}" maxlength="500">
                        <form action="#" id="information-form" name="form-one">
                            <div class="form-group">
                                <label for="username">用户账号：</label>
                                <span id="username">${userName!}</span>
                            </div>
                            <div class="form-group">
                                <label for="email">EMAIL：</label>
                                <span id="email">${appPersonInfoDTO.email!}</span>
                            </div>
                            <div class="form-group">
                                <label for="auth">实名认证：</label>
                                <span class="verify-state"></span>
                                <span class="identify-name">${appPersonInfoDTO.trueName!}</span>
                                <span class="identify-code">${appPersonInfoDTO.cardId!}</span>
                            </div>
                            <div class="form-group is-show-phone">
                                <label for="phone">手机号码：</label>
                                <span id="phone">${appPersonInfoDTO.mobilePhone!}</span>
                            </div>

                            <div class="form-group is-show-gender">
                                <label for="gender">性别：</label>
                                <div class="gender" id="asdasd">
                                    <span class="gender-male ${(appPersonInfoDTO.sex==0)?string('active','')}" data-id="1">
                                          <svg width="17" height="17" viewBox="0 0 17 17">

                                          <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>男
                                      </span>
                                    <span class="gender-female ${(appPersonInfoDTO.sex==1)?string('active','')}" data-id="0">
                                          <svg width="17" height="17" viewBox="0 0 17 17">

                                          <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>女
                                      </span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="country">所在地区：</label>
                                <div class="country-select">
                                    <p class="select-header" id="person_id">
                                        <span class="person-country-txt">中国(China)</span>
                                        <input type="hidden" name="person_id" value="7">
                                        <svg width="10" height="10" viewBox="0 0 10 6">

                                          <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                    </p>
                                    <div class="country-list person-country-list">
                                        <svg class="searchSVG" focusable="false" viewBox="0 0 21.867 21.87">

                                          <path d="M8.5 0a8.5 8.5 0 1 0 4.855 15.478l.001-.001 6.304 6.304c.117.117.303.12.423 0l1.699-1.699a.303.303 0 0 0-.001-.423l-6.303-6.303A8.5 8.5 0 0 0 8.5 0zm0 15a6.5 6.5 0 1 1 0-13 6.5 6.5 0 0 1 0 13z" fill-rule="evenodd" clip-rule="evenodd"></path></svg>
                                        <input type="text" title="" class="filter-search" autocomplete="off" placeholder="搜索">
                                        <ul>
                                            <li data-val="0086">中国(China)</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="address">详细地址：</label>
                                <span class="person-country-txt">${appPersonInfoDTO.postalAddress!}</span>
                            </div>
                        </form>
                    </div>
                    <div class="person-info-footer">
                        <div class="launch-save-operate">
                            <button type="button" name="save" data-step="one">保存</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" name="save_next" data-step="one" data-to="two">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                </div>
                <!-- part two -->
                
                <div class="launch-part-two launch-part" data-part="two" style="display:block">
                    <div class="launch-project-info">
                    <form action="#"  name="form-two"> 
                        <div id="project_info_form">
                            <div class="form-group">
                                <label for="name"><b class="is-required">*</b>项目名称：</label>
                                <input type="text" name="projectName" id="projectName" value="${project.projectName!}" maxlength="500" placeholder="请输入项目名称" required>
                            </div>
                            <div class="form-group">
                                <label for="stage"><b class="is-required">*</b>所属阶段：</label>
                                <div class="stage-select">
                                    <p class="pro-select-header">
                                    	<#if project.projectStage==0>
                                    		<span class="pro-stage-txt" id="progess_id">尚未开启</span>
                                    	<#elseif project.projectStage==1>
                                    		<span class="pro-stage-txt" id="progess_id">产品开发中</span>
                                    	<#elseif project.projectStage==2>
                                    		<span class="pro-stage-txt" id="progess_id">产品已上市</span>
                                    	<#elseif project.projectStage==3>
                                    		<span class="pro-stage-txt" id="progess_id">已经盈利</span>
                                    	<#else>
                                    		<span class="pro-stage-txt" id="progess_id">请选择阶段</span>
                                    	</#if>
                                        <input type="hidden" name="projectStage">
                                        <span class="stage-select-btn">
                                              <svg width="10" height="10" viewBox="0 0 10 6">
                                              <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                          </span>
                                    </p>
                                    <ul class="stage-list pro-stage-list">
                                        <li data-val="">请选择阶段</li>
                                        <li data-val="0">尚未启动</li>
                                        <li data-val="1">产品开发中</li>
                                        <li data-val="2">产品已上市</li>
                                        <li data-val="3">已经盈利</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="person_name"><b class="is-required">*</b>真实姓名：</label>
                                <input type="text" name="trueName" id="trueName" value="${project.trueName!}" placeholder="请输入真实姓名">
                            </div>
                            <div class="form-group">
                                <label for="office_address"><b class="is-required">*</b>办公地址：</label>
                                <div class="country-select">
                                    <p class="select-header" id="country_id">
                                        <span class="pro-country-txt">请选择国家</span>
                                        <input type="hidden" name="country_id">
                                        <svg width="10" height="10" viewBox="0 0 10 6">
                                          <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                    </p>
                                    <div class="country-list pro-country-list">
                                        <svg class="searchSVG" focusable="false" viewBox="0 0 21.867 21.87">

                                          <path d="M8.5 0a8.5 8.5 0 1 0 4.855 15.478l.001-.001 6.304 6.304c.117.117.303.12.423 0l1.699-1.699a.303.303 0 0 0-.001-.423l-6.303-6.303A8.5 8.5 0 0 0 8.5 0zm0 15a6.5 6.5 0 1 1 0-13 6.5 6.5 0 0 1 0 13z" fill-rule="evenodd" clip-rule="evenodd"></path></svg>
                                        <input type="text" title="" class="filter-search" autocomplete="off" placeholder="搜索">
                                        <ul>
                                            <li data-val="">请选择国籍</li>
                                            <li data-val="0086">中国(China)</li>
                                        </ul>
                                    </div>
                                </div>
                                <input type="text" name="workAddress" id="office_address" value="${project.workAddress}" placeholder="请输入详细办公地址" maxlength="100">
                            </div>
                            <div class="form-group">
                                <label for="join_count"><b class="is-required">*</b>团队人数：</label>
                                <input type="text" name="teamSize" id="teamSize" value="${project.teamSize!}" placeholder="请输入人数">
                            </div>
                            <div class="form-group">
                                <label for="finance_stage"><b class="is-required">*</b>融资阶段：</label>
                                <div class="stage-select">
                                    <p class="finance-select-header" id="financing_id">
                                    	<#if project.projectStage==0>
                                    		<span class="finance-stage-txt">未融资</span>
                                    	<#elseif project.projectStage==1>
                                    		<span class="pro-stage-txt" id="progess_id">D轮</span>
                                    	<#elseif project.projectStage==2>
                                    		<span class="pro-stage-txt" id="progess_id">C轮</span>
                                    	<#elseif project.projectStage==3>
                                    		<span class="pro-stage-txt" id="progess_id">B轮</span>
                                		<#elseif project.projectStage==4>
                                			<span class="pro-stage-txt" id="progess_id">A轮</span>
                                		<#elseif project.projectStage==5>
                                			<span class="pro-stage-txt" id="progess_id">天使轮</span>
                                    	<#else>
                                    		<span class="finance-stage-txt">请选择阶段</span>
                                    	</#if>
                                        <input type="hidden" name="financingStage">
                                        <span class="stage-select-btn">
                                              <svg width="10" height="10" viewBox="0 0 10 6">
                                              <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                          </span>
                                    </p>
                                    <ul class="stage-list finance-stage-list">
                                        <li data-val="">请选择阶段</li>
                                        <li data-val="5">天使轮</li>
                                        <li data-val="4">A轮</li>
                                        <li data-val="3">B轮</li>
                                        <li data-val="2">C轮</li>
                                        <li data-val="1">D轮</li>
                                        <li data-val="0">未融资</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="introduction"><b class="is-required">*</b>项目简介：</label>
                                <textarea name="info" id="info" maxlength="1000" placeholder="请输入项目简介">
                                	${project.info!}
                                </textarea>
                                
                                <!-- 文件上传 -->
                                <input type="hidden" name="othermaterial" value="${project.othermaterial!}">
                                <input type="hidden" name="cover" value="${project.cover!}">
                                <input type="hidden" name="whitepaper" value="${project.whitepaper!}">
                                <input type="hidden" name="businessplan" value="${project.businessplan!}">
                            </div>
                            <div class="form-group">
                                <label for="video_address">宣传视频：</label>
                                <input type="text" name="videoaddress" id="video_address" value="${project.videoaddress}" placeholder="请输入Flash视频地址(支持爱奇艺、腾讯、优酷、土豆、酷6、新浪、搜狐视频)">
                            </div>
                            </form>
                            <div class="form-group">
                                <form action="${ctx}/manage/filefront/upload.do"  method='post' enctype='multipart/form-data'  class="uploadForm" name="cover">
                                    <label for="upload_img"><b class="is-required">*</b>封面图片：</label>
                                    <button type="button" id="pic_address">上传图片<input type="file" name="picture" id="upload_img_file" value="上传图片"></button>
                                    <span>支持JPG/JPEG/PNG格式；建议尺寸：790x461px；最多3张【图片大小不超过1M】</span>
                                    <div class="img-preview">
                                    </div>
                                    <button type="button" onclick="upload_logo('cover')">上传确认</button>
                                </form>
                            </div>
                            <div class="form-group">
                                <label for="upload_file"><b class="is-required">*</b>上传附件：</label>
                                <span> 请上传项目白皮书、商业计划书、其他资料文档，支持Word/Excle/PPT/PDF格式【文件大小不超过10M】</span>
                                <div class="upload-file">
                                    <ul class="upload-ul">
                                        <li class="upload-file-item">
                                            <form action="${ctx}/manage/filefront/upload.do"  method='post' enctype='multipart/form-data'  class="uploadForm" name="whitepaper">
                                                <div class="upload-file-button upload-white_paper" id="white_pager">
                                                    <input type="file" name="file" class="file-upload">
                                                    <svg class="svg-center" height="34px" width="34px" viewBox="0 0 34 34">

                                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                                </div>
                                                <p>项目白皮书</p>
                                                 <button type="button" onclick="upload_logo('whitepaper')">上传确认</button>
                                            </form>
                                        </li>
                                        <li class="upload-file-item">
                                            <form action="${ctx}/manage/filefront/upload.do"  method='post' enctype='multipart/form-data'  class="uploadForm" name="businessplan">
                                                <div class="upload-file-button upload-item_plan" id="item_plan">
                                                    <input type="file" name="file" class="file-upload">
                                                    <svg class="svg-center" height="34px" width="34px" viewBox="0 0 34 34">

                                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                                </div>
                                                <p>商业计划书</p>
                                                <button type="button" onclick="upload_logo('businessplan')">上传确认</button>
                                            </form>
                                        </li>
                                        <li class="upload-file-item">
                                            <form action="${ctx}/manage/filefront/upload.do"  method='post' enctype='multipart/form-data'  class="uploadForm" name="othermaterial">
                                                <div class="upload-file-button upload-item_others" id="item_others">
                                                    <input type="file" name="file" class="file-upload">
                                                    <svg class="svg-center" height="34px" width="34px" viewBox="0 0 34 34">

                                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                                </div>
                                                <p>其他资料</p>
                                                <button type="button" onclick="upload_logo('othermaterial')">上传确认</button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="project-info-footer">
                        <div class="launch-pre-operate">
                            <button type="button" class="operate-prev-btn" name="front" data-step="one"><svg width="25" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>上一步</button>
                        </div>
                        <div class="launch-save-operate">
                            <button type="button" name="save" data-step="two">保存</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" name="save_next" data-step="two" data-to="three">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                </div>
                <!-- part three -->
                <div class="launch-part-three launch-part" data-part="three">
                <form action="#"  name="form-three"> 
                    <div class="launch-pro-introduction">
                        <div id="detail">
                          <textarea name="projectHomePage" style="width: 800px; height: 800px;padding:20px;" 
                          placeholder="（注意：以下内容只做参考，您可以根据自己项目的实际情况详细介绍您的项目！）
————————————————————————————————————————————————————————
关于我们
向支持者介绍一下你自己或团队，以及你自己或团队与所发起的项目之间的背景。这样有助于拉近你与支持者之间的距离。
我们想要做什么
以视频、图文并茂的方式简洁生动地说明你的项目，让大家一目了然，这会决定支持者是否将你的项目描述继续看下去。
项目的发展规划和风险
让大家更好的了解项目的进度，增加支持者对项目的信任度，以及在支持者选择支持前就了解和认同项目在执行过程中所存在的风险。
为什么需要你的支持
这是加分项。说说你的项目不同寻常的特色、资金用途、以及大家支持你的理由，这会让更多人能够支持你。
我们的承诺与回报
让大家感到你对待项目的认真程度，鞭策你将项目执行最终成功。同时向大家展示一下你为支持者准备的回报，吸引更多人支持你。">${homePageDTO.content!}</textarea>
                        </div>
                    </div>
                    <div class="project-intro-footer">
                        <div class="launch-pre-operate">
                            <button type="button" class="operate-prev-btn" name="front" data-step="two"><svg width="25" height="14" viewBox="0 0 9 14">
                        <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>上一步</button>
                        </div>
                        <div class="launch-save-operate">
                            <button type="button" name="save" data-step="three">保存</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" name="save_next" data-step="three" data-to="four">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">
                        <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                    </form>
                </div>
                <!-- part four -->
                <div class="launch-part-four launch-part" data-part="four">
                    <div class="launch-plan-content clearfix">
                        <div class="launch-plan-left">
                            <ul class="launch-plan-left-list">
                                <li class="pro-plan-item active">
                                    <a href="javascript:" class="plan-list-a" data-id="">回报一</a>
                                </li>
                            </ul>
                            <div class="add-div">
                                <a href="javascript:" class="add-plan">
                                    <svg width="14" height="14" viewBox="0 0 34 34">
                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                    <span class="add-txt">添加</span>
                                </a>
                                <a href="javascript:" class="delete-plan">
                                    <svg width="14" height="14" viewBox="0 0 16 16">
                                      <path class="del-color" fill-rule="evenodd" clip-rule="evenodd" fill="#B5B7C5" d="M8 0C3.6 0 0 3.6 0 8s3.6 8 8 8 8-3.6 8-8-3.6-8-8-8zm4 11l-1 1-3-3-3 3-1-1 3-3-3-3 1-1 3 3 3-3 1 1-3 3 3 3z"></path></svg>
                                    <span class="add-txt">删除</span>
                                </a>
                            </div>
                        </div>
                        <div class="launch-plan-right">
                            <form action="#" id="pro_plan_form" name="form-four">
                                <input type="hidden" value="" id="plan_id">
                                <div class="form-group">
                                    <label for="curtype"><b class="is-required">*</b>投资币种：</label>
                                    <span class="curtype_limit">
                                    	<#if projectRepay.investCoinCode??>
											<span class="edit-focus curtype-btc ${(projectRepay.investCoinCode=='BTC')?string('active','')}" data-id="BTC">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>BTC
                                          </span>
                                    	  <span class="edit-focus curtype-eth ${(projectRepay.investCoinCode=='ETH')?string('active','')}"  data-id="ETH">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>ETH
                                          </span>
                                          <span class="edit-focus curtype-etc ${(projectRepay.investCoinCode=='ETC')?string('active','')}"  data-id="ETC">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>ETC
                                          </span>
										<#else>
											<span class="edit-focus curtype-btc active" data-id="BTC">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>BTC
                                          </span>
                                          <span class="edit-focus curtype-eth"  data-id="ETH">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>ETH
                                          </span>
                                          <span class="edit-focus curtype-etc"  data-id="ETC">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>ETC
                                          </span>
										</#if>
                                          <input type="hidden" name="investCoinCode"  value="${projectRepay.investCoinCode!}">
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="money"><b class="is-required">*</b>投资金额：</label>
                                    <span class="money_limit">
                                          <span class="edit-amount money-custom ${(projectRepay.isLimitedMoney==0)?string('active','')}" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>不限
                                          </span>
                                    	  <span class="edit-amount money-custom ${(projectRepay.isLimitedMoney==1)?string('active','')}" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>限制
                                          </span>
                                    </span>
                                    	<span class="is-show-money" style="display:${(projectRepay.isLimitedMoney==1)?string('line','none')}"">
                                    	  <input type="hidden"  name="isLimitedMoney"  value="${projectRepay.isLimitedMoney!}">
                                          <input type="text" id="money" name="money" value="${projectRepay.money!}" class="edit-focus limit">
                                          <span class="money-type">BTC</span>
                                    	</span>
                                </div>
                                <div class="form-group">
                                    <label for="plan_intro"><b class="is-required">*</b>回报说明：</label>
                                    <textarea name="repayExplain" id="repayExplain" class="edit-focus" cols="30" rows="10" maxlength="150" placeholder="请输入您的回报说明，不超过150字">
                                    	${projectRepay.repayExplain!}
                                    </textarea>
                                </div>
                                <div class="form-group">
                                    <label for="quota_limit"><b class="is-required">*</b>名额限制：</label>
                                    <span class="quota_limit">
                                          <span class="edit-islimitpeople quota-no ${(projectRepay.isLimitedSize==0)?string('active','')}" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>否
                                          </span>
                                    	  <span class="edit-islimitpeople quota-yes ${(projectRepay.isLimitedSize==1)?string('active','')}" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>是
                                          </span>
                                    </span>
                                    <span class="is-show-quota" style="display:${(projectRepay.isLimitedSize==1)?string('line','none')}">
                                    	  <input type="hidden" name="isLimitedSize"  value="${projectRepay.isLimitedSize!}">
                                          <label for="limit_persons" class="label-quota hiden">名额数量：</label>
                                          <input type="text" id="size" name="size" value="${projectRepay.size!}" class="edit-focus limit">
                                          <span></span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="buy_limit"><b class="is-required">*</b>支持限制：</label>
                                    <span class="buy_limit">
                                          <span class="edit-islimittime buy-no  ${(projectRepay.isLimitedSupport==0)?string('active','')}" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>否
                                          </span>
                                    <span class="edit-islimittime buy-yes ${(projectRepay.isLimitedSupport==1)?string('active','')}" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>是
                                          </span>
                                    </span>
                                    <span class="is-show-buy" style="display:${(projectRepay.isLimitedSupport==1)?string('line','none')}">
                                          <input type="hidden" name="isLimitedSupport"  value="${projectRepay.isLimitedSupport!}">
                                          <label for="limit_support" class="label-quota">此回报每个用户只能购买</label>
                                          <input type="text" id="support" name="support" value="${projectRepay.support!}" class="edit-focus limit">
                                          <span>&nbsp;次</span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="free_shipping"><b class="is-required">*</b>是否包邮：</label>
                                    <input type="hidden" name="isFreeShipping"  value="${projectRepay.isFreeShipping!}">
                                    <span class="free_shipping" id="post_state">
                                          <span class="edit-isfreeshipping post-no ${(projectRepay.isFreeShipping==0)?string('active','')}" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>否
                                          </span>
                                    <span class="edit-isfreeshipping post-yes ${(projectRepay.isFreeShipping==1)?string('active','')}" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>大陆包邮
                                          </span>
                                    </span>
                                    
                                </div>
                                <div class="form-group">
                                    <label for="reward_time"><b class="is-required">*</b>回报时间：</label>
                                    <span class="reward-txt">预计项目ICO成功结束后&nbsp;</span>
                                    <input type="text" name="paybackTime" id="paybackTime" value="${projectRepay.paybackTime!}" class="edit-focus limit">
                                    <span class="reward-txt">&nbsp;天内</span>
                                </div>
                                <div class="form-group">
                                    <button type="button" name="save" data-step="four" class="launch-plan-save">保存</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="project-plan-footer">
                        <div class="launch-pre-operate">
                            <button type="button" name="front" class="operate-prev-btn" data-step="three"><svg width="25" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>上一步</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" name="save_next" data-step="four" data-to="five">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                </div>
                <!-- part five -->
                <div class="launch-part-five launch-part" data-part="five">
                    <div class="launch-submit-content">
                        <div class="launch-submit-body">
                            <form action="#" class="launch-submit-form" name="form-five">
                                <div class="form-group">
                                    <label for="funding_day"><b class="is-required">*</b>ICO天数：</label>
                                    <input type="text" name="icoDays" id="icoDays" value="${project.icoDays!}" class="launcha">
                                    <span>&nbsp;天</span>
                                </div>
                                <div class="form-group">
                                    <label for="target_money"><b class="is-required">*</b>目标金额：</label>
                                    <span class="">
                                          <div class="limittype-select">
                                              <p class="limittype-select-header" id="limittype">
                                              	<#if project.isLimitMoney==1>
                                              		<span class="limittype-txt">限制</span>
                                              	<#else>
                                              		<span class="limittype-txt">不限</span>
                                              	</#if>
                                                  <input type="hidden" name="isLimitMoney" id="isLimitMoney" value="${project.isLimitMoney!}" value="0">
                                                  <svg width="10" height="10" viewBox="0 0 10 6"><path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                              </p>
                                              <ul class="limittype-list">
                                                  <li data-val="0" data-id="0">不限</li>
                                                  <li data-val="1" data-id="1">限制</li>
                                              </ul>
                                            </div>
                                    </span>
                                    <input type="text"  name="sumMoney" id="sumMoney" value="${project.sumMoney!}" class="${(project.isLimitMoney==0)?string('hide','')} limit launcha">
                                    <span class="">
                                          <div class="moneytype-select">
                                              <p class="moneytype-select-header" id="moneytype">
                                                  
                                                    <#if project.coinType=='BTC'>
                                              		 	<span class="moneytype-txt">BTC</span>
                                              		<#elseif project.coinType=='ETH'>
                                              			<span class="moneytype-txt">ETH</span>
                                              		<#elseif project.coinType=='ETC'>
                                              			<span class="moneytype-txt">ETC</span>
                                              		<#else>
                                              			<span class="moneytype-txt">BTC</span>
                                              		</#if>
                                                  <input type="hidden" name="coinType" value="${project.coinType!}">
                                                  <svg width="10" height="10" viewBox="0 0 10 6"><path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                              </p>
                                              <ul class="moneytype-list">
                                                  <li data-val="BTC">BTC</li>
                                                  <li data-val="ETH">ETH</li>
                                                  <!--<li data-val="ETC">ETC</li>-->
                                              </ul>
                                          </div>
                                  </span>
                                </div>
                                <div class="form-group">
                                    <label for="contacts"><b class="is-required">*</b>联 系 人：</label>
                                    <input type="text" class="" name="linkman" id="linkman" value="${project.linkman!}" placeholder="请输入联系人姓名">
                                </div>
                                <div class="form-group">
                                    <label for="response_job"><b class="is-required">*</b>负责职位：</label>
                                    <input type="text" class="" name="position" id="position" value="${project.position!}" placeholder="请输入负责人职位">
                                </div>
                                <div class="form-group">
                                    <label for="contacts_phone"><b class="is-required">*</b>联系电话：</label>
                                    <input type="text" class="" name="phone" id="phone" value="${project.phone!}" placeholder="请输入负责人联系电话">
                                </div>
                                <div class="form-group">
                                    <label for="apply_starttime"><b class="is-required">*</b>开始时间：</label>
                                    <span>申请&nbsp;</span>
                                    <input type="text" class="launcha" name="startDays" id="startDays" value="${project.startDays!}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
                                    <span>&nbsp;天内开始</span>
                                </div>
                            </form>
                            <div class="launch-submit-tips">
                              <span class="ico-agree">
                                  <svg height="18" width="18" viewBox="0 0 18 18">
                                  <path class="check-color"  fill-rule="evenodd" clip-rule="evenodd" fill="#24D96D" d="M3.389 8.418l3.273 2.609 8.151-7.51s.547-.5 1.024-.109c.143.118.307.45-.063.97l-8.511 9.97s-.653.893-1.427-.01L2.165 9.379s-.436-.671.109-1.075c.184-.135.602-.345 1.115.114m0 0"></path><path class="border-color" fill="#9B9B9B" d="M17 1v16H1V1h16m1-1H0v18h18V0z"></path></svg>
                              </span>
                              <span>请认真阅读<a href="javascript:;" data-toggle="modal" data-target="#reg_pro">《发起者协议》</a>，确认项目内容符合规范。</span>
                            </div>
                        </div>
                    </div>
                    <div class="project-submit-footer">
                <div class="launch-pre-operate">
                    <button type="button" class="operate-prev-btn" name="front" data-step="four">
                      <svg width="25" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path>
                      </svg>上一步
                    </button>
                </div>
                <div class="launch-save-operate">
                    <button type="button" name="save" data-step="five">保存</button>
                </div>
                <div class="launch-next-operate">
                    <button type="button" name="save_next" data-step="five" data-to="six">提交审核
                      <svg width="18" height="14" viewBox="0 0 9 14">
                          <path fill="currentColor" d="M0 14V0l9 7z"></path>
                      </svg>
                    </button>
                </div>
            </div>
                </div>
            </div>
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
<div class="modal fade ng-scope" id="reg_pro" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="z-index:9999">
    <div class="modal-content  p-0 bg-dark">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title">互融世界交易平台注册协议互融世界交易平台注册协议</h4>
      </div>
      <div class="modal-body">
        <textarea class="form-control text-left" rows="20">        互融世界专业数字货币交易平台，又称互融世界数字货币交易平台或互融世界交易平台，归互融时代区块链信用管理（北京）有限公司所有，互融世界交易平台与用户共同确认：
1. 用户点击互融世界交易平台注册页面的”同意注册”按钮并完成注册程序、获得互融世界交易平台账号和密码时，视为用户与互融时代区块链信用管理（北京）有限公司已达成《互融世界交易平台注册协议》(下称“本协议”)，就用户进入互融世界交易平台使用相关的服务达成本协议的全部约定。
2. 互融世界交易平台及用户均已认真阅读本《互融世界交易平台注册协议》中全部条款及互融币平台发布的法律声明和操作规则的内容，对本协议及前述服务条款、法律声明和操作规则均已知晓、理解并接受，同意将其作为确定双方权利义务的依据。互融世界交易平台《法律声明》为本协议的必要组成部分，用户接受本协议的同时即视为接受互融世界交易平台《法律声明》的全部内容。本协议内容包括本协议正文以及在互融世界交易平台已经发布的或将来可能发布的各类规则、声明、说明。所有规则、声明、说明均为本协议不可分割的一部分，与本协议正文具有同等法律效力。
3. 本协议不涉及互融世界交易平台用户与其他用户之间因互融币交易而产生的法律关系及法律纠纷。
一、定义条款
1.1  “互融币”：一种基于区块链底层技术上升级的游戏币，可以在游戏或商城中使用。
1.2  “用户”：接受并同意本协议全部条款及互融世界交易平台不时发布和更新的公告、法律条款和操作规则、通过互融世界交易平台进行互融币交易的互融世界交易平台注册会员。
1.3  “用户注册”：用户注册是指用户登录互融世界交易平台，并按要求填写相关信息并确认同意履行相关用户协议的过程。
1.4 “互融币交易”：用户通过互融世界交易平台进行的互融币交易活动。
1.5 “充值款”：用户为购买互融币/出售互融币而向互融币交易平台预充入的人民币/互融币的款项。
1.6 “手续费”：用户在互融世界交易平台达成互融币交易而向互融世界交易平台支付的交易服务费用。
二、用户注册
2.1 注册资格
用户承诺：用户具有完全民事权利能力和行为能力，或虽本人不具有完全民事权利能力和行为能力,由其法定代理人同意并由其法定代理人代理注册、点击同意注册按钮及应用互融世界交易平台服务。
2.2 注册目的
用户承诺：用户在互融世界交易平台进行用户注册不得违反法律法规或破坏互融世界交易平台交易秩序。
2.3 注册流程
2.3.1 用户同意根据互融世界交易平台用户注册页面的要求提供有效电子邮箱等信息，设置互融币平台账号及密码，用户应确保所提供全部信息的真实性和有效性。
2.3.2 用户合法、完整并有效提供互融世界交易平台注册所需信息的，有权获得互融世界交易平台账号和密码，互融世界交易平台账号和密码用于用户在互融世界交易平台进行会员登录。
2.3.3 用户获得互融世界交易平台账号及密码时即视为用户注册成功，用户同意接收互融币发送的与互融币网站管理、运营相关的电子邮件和/或短消息。
2.3.4 用户注册成功后进行互融币交易，必须提供本人的真实身份证号码，进行实名认证。
三、用户服务
互融世界交易平台为用户提供网络交易服务。
3.1 服务内容
3.1.1 用户有权在互融世界交易平台浏览互融币实时行情及交易信息、有权通过互融世界交易平台提交互融币交易指令并完成互融币交易。
3.1.2 用户有权在互融世界交易平台查看其互融币会员账号下的信息，有权应用互融世界交易平台提供的功能进行操作。
3.1.3 用户有权按照互融世界交易平台发布的活动规则参与互融世界交易平台组织的网站活动。
3.1.4 互融世界交易平台承诺为用户提供的其他服务。
3.2 服务规则
用户承诺遵守下列互融世界交易平台的服务规则：：
3.2.1 用户应当遵守法律法规、规章、规范性文件及政策要求的规定，保证账户中所有资金和互融币来源的合法性，不得在互融世界交易平台或利用互融世界交易平台服务从事非法或其他损害互融币或第三方权益的活动，如发送或接收任何违法、违规、违反公序良俗、侵犯他人权益的信息，发送或接收传销材料或存在其他危害的信息或言论；未经互融世界交易平台授权使用或伪造互融币电子邮件题头信息等。
3.2.2 用户应当遵守法律法规，应当妥善使用和保管其互融世界交易平台账号及密码、资金密码、和其注册时绑定的电子邮箱、手机号码、以及手机接收的手机验证码的安全。用户对使用其互融世界交易平台账号和密码、资金密码、手机验证码进行的任何操作和后果承担全部责任。当用户发现互融世界交易平台账号、密码、或资金密码、验证码被未经其授权的第三方使用，或存在其他账号安全问题时，应立即有效通知互融世界交易平台，要求互融世界交易平台暂停该用户在互融世界交易平台账号的服务。互融世界交易平台有权在合理时间内对用户的该等请求采取行动，但对互融世界交易平台采取行动前用户已经遭受的损失不承担任何责任。用户在未经互融世界交易平台同意的情况下不得将互融世界交易平台的账号以赠与、借用、租用、转让或其他方式处分给他人。
3.2.3 用户应当遵守互融币不时发布和更新的用户协议以及其他服务条款和操作规则。
四、互融币交易规则
用户承诺在其进入互融世界交易平台交易，通过互融世界交易平台与其他用户进行互融币交易的过程中遵守如下互融世界交易平台的交易规则。
4.1 浏览交易信息
用户在互融世界交易平台浏览互融币交易信息时，应当仔细阅读交易信息中包含的全部内容，包括但不限于互融币价格、委托量、手续费、买入或卖出方向。用户完全接受交易信息中包含的全部内容后方可点击按钮进行确认交易。
4.2 提交委托
在浏览完交易信息并确认无误之后用户可以提交交易委托。用户提交交易委托后，即用户授权互融世界交易平台代理用户进行相应的交易撮合。互融世界交易平台在有满足用户委托价格的交易时将会自动完成撮合交易而无需提前通知用户。
4.3 查看交易明细
用户可以通过管理中心的交易明细中查看相应的交易记录，确认及查看其其详情交易记录。
4.4 撤销/修改委托
在委托未达成交易之前，用户有权随时撤销或修改委托。
五、用户的权利和义务
5.1 用户有权按照本协议约定接受互融世界交易平台提供的服务。
5.2 用户有权随时终止使用互融世界交易平台的服务。
5.3 除在本注册协议第七项特别声明标注的情况外，用户有权按照平台要求随时提取在互融世界交易平台的资金余额，包括人民币以及互融币，但需向互融世界交易平台支付相应的提现手续费用。
5.4 用户对注册时提供的个人资料的真实性、有效性及安全性负责。
5.5 用户在互融世界交易平台进行互融币交易时不得恶意干扰互融币交易的正常进行、破坏交易秩序。
5.6 用户不得以任何技术手段或其他方式干扰互融币的正常运行或干扰其他用户对互融世界交易平台服务的使用。
5.7 如用户在互融世界交易平台因网上交易与其他用户产生诉讼的，不得通过司法或行政以外的途径要求互融时代区块链信用管理（北京）有限公司提供相关资料。
5.8 用户不得以虚构事实等方式恶意诋毁互融世界交易平台及所属互融时代区块链信用管理（北京）有限公司的商誉。
六、互融币的权利和义务
6.1 如用户不具备本协议约定的注册资格，则互融世界交易平台有权拒绝用户进行注册，对已注册的用户有权注销其互融世界交易平台会员账号。互融世界交易平台因此而遭受损失的有权向前述用户或其法定代理人主张赔偿。同时，互融世界交易平台保留其他任何情况下决定是否接受用户注册的权利。
6.2 互融世界交易平台发现账户使用者并非账户初始注册人时，有权中止该账户的使用。
6.3 互融世界交易平台通过技术检测、人工抽检等检测方式合理怀疑用户提供的信息错误、不实、失效或不完整时，有权通知用户更正、更新信息或中止、终止为其提供互融币平台的服务。
6.4 互融世界交易平台有权在发现互融世界交易平台上显示的任何信息存在明显错误时，对信息予以更正。
6.5 互融世界交易平台保留随时修改、中止或终止互融世界交易平台服务的权利，互融世界交易平台行使修改或中止服务的权利不需事先告知用户，互融世界交易平台终止互融世界交易平台一项或多项服务的，终止自互融世界交易平台在网站上发布终止公告之日生效。
6.6 互融世界交易平台应当采取必要的技术手段和管理措施保障互融世界交易平台的正常运行，并提供必要、可靠的交易环境和交易服务，维护互融世界交易平台的交易秩序。
6.7 如用户连续一年未使用互融世界交易平台会员账号和密码登录互融币，则互融世界交易平台有权注销用户的互融世界交易平台账号。账号注销后，互融世界交易平台有权将相应的会员名开放给其他用户注册使用。
6.8 互融世界交易平台通过加强技术投入、提升安全防范等措施保障用户的人民币资金及互融币托管安全，在用户资金出现可以预见的安全风险时应提前通知用户。
6.9 互融世界交易平台有权在本协议履行期间及本协议终止后保留用户的注册信息及用户应用互融世界交易平台服务期间的全部交易信息，但不得非法使用该等信息。
6.10 互融世界交易平台有权随时删除互融币网站内各类不符合国家法律法规、规范性文件或互融世界交易平台网站规定的用户评价等内容信息，互融世界交易平台行使该等权利不需提前通知用户。
七、特别声明
7.1 在法律允许的范围内，不论在何种情况下，互融世界交易平台对由于信息网络设备维护、信息网络连接故障、电脑、通讯或其他系统的故障、电力故障、罢工、劳动争议、暴乱、起义、骚乱、生产力或生产资料不足、火灾、洪水、风暴、爆炸、战争、政府行为、司法行政机关的命令、其他不可抗力或第三方的不作为而造成的不能服务或延迟服务，以及用户因此而遭受的损失不承担责任。
八、知识产权
8.1 互融世界交易平台所包含的全部智力成果包括但不限于网站标志、数据库、网站设计、文字和图表、软件、照片、录像、音乐、声音及其前述组合，软件编译、相关源代码和软件 (包括小应用程序和脚本) 的知识产权权利均归互融世界交易平台所有。用户不得为商业目的复制、更改、拷贝、发送或使用前述任何材料或内容。
8.2 互融世界交易平台名称中包含的所有权利 (包括但不限于商誉和商标、标志) 均归互融时代区块链信用管理（北京）有限公司所有。
8.3 用户接受本协议即视为用户主动将其在互融世界交易平台发表的任何形式的信息的著作权，包括但不限于：复制权、发行权、出租权、展览权、表演权、放映权、广播权、信息网络传播权、摄制权、改编权、翻译权、汇编权以及应当由著作权人享有的其他可转让权利无偿独家转让给互融世界交易平台所有，互融世界交易平台有权利就任何主体侵权单独提起诉讼并获得全部赔偿。本协议属于《中华人民共和国著作权法》第二十五条规定的书面协议，其效力及于用户在互融世界交易平台发布的任何受著作权法保护的作品内容，无论该内容形成于本协议签订前还是本协议签订后。
8.4 用户在使用互融世界交易平台服务过程中不得非法使用或处分互融世界交易平台或他人的知识产权权利。用户不得将已发表于互融世界交易平台的信息以任何形式发布或授权其它网站（及媒体）使用。
九、客户服务
互融世界交易平台建立专业的客服团队，并建立完善的客户服务制度，从技术、人员和制度上保障用户提问及投诉渠道的畅通，为用户提供及时的疑难解答与投诉反馈。
十、协议的变更和终止
10.1 协议的变更：互融世界交易平台有权随时对本协议内容或互融世界交易平台发布的其他服务条款及操作规则的内容进行变更，变更时互融世界交易平台将在网站显著位置发布公告，变更自公告发布之时生效，如用户继续使用互融世界交易平台提供的服务即视为用户同意该内容变更，如用户不同意变更后的内容则用户有权注销互融世界交易平台账户、停止使用互融世界交易平台提供的服务。
10.2 协议的终止
10.2.1 互融世界交易平台有权依据本协议约定注销用户的互融世界交易平台账号，本协议于账号注销之日终止。
10.2.2 互融世界交易平台有权依据本协议约定终止全部互融币服务，本协议于互融世界交易平台全部服务终止之日终止。
10.2.3 本协议终止后，用户无权要求互融世界交易平台继续向其提供任何服务或履行任何其他义务，包括但不限于要求互融世界交易平台为用户保留或向用户披露其原互融世界交易平台账号中的任何信息，向用户或第三方转发任何其未曾阅读或发送过的信息等。
10.2.4 本协议的终止不影响守约方向违约方追究违约责任。
十一、隐私权政策
11.1 适用范围
11.1.1 在用户注册互融世界交易平台账号或者支付账户时，用户根据互融世界交易平台要求提供的个人注册信息，包括但不限于身份证信息；
11.1.2 在用户使用互融世界交易平台的服务时，或访问互融世界交易平台网页时，互融世界交易平台自动接收并记录的用户浏览器上的服务器数值，包括但不限于IP地址等数据及用户要求取用的网页记录；
11.1.3 互融世界交易平台收集到的用户在互融世界交易平台进行交易的有关数据，包括但不限于出价、购买等记录；
11.1.4 互融世界交易平台通过合法途径取得的其他用户个人信息。
11.2 信息使用
11.2.1 互融世界交易平台不会向任何人出售或出借用户的个人信息，除非事先得到用户的许可。互融世界交易平台也不允许任何第三方以任何手段收集、编辑、出售或者无偿传播用户的个人信息。
11.3 互融世界交易平台对所获得的客户身份资料和交易信息进行保密，不得向任何单位和个人提供客户身份资料和交易信息，法律法规另有规定的除外。
十二、反洗钱
12.1 互融世界交易平台遵守和执行《中华人民共和国反洗钱法》的规定，对用户进行身份识别、客户身份资料和交易记录保存制度，以及大额的和可疑交易报告的制度。
12.2 用户注册、挂失交易密码或者资金密码时，应当提供并上传身份证复印件，互融世界交易平台对用户提供的身份证信息进行识别和比对。互融世界交易平台有合理的理由怀疑用户使用虚假身份注册时，有权拒绝注册或者注销已经注册的账户。
12.3 互融世界交易平台参照《金融机构大额交易和可疑交易报告管理办法》的规定，保存大额交易和有洗钱嫌疑的交易记录，在监管机构要求提供大额交易和可疑交易的记录时，向监管机构提供。
12.4 互融世界交易平台对用户身份信息以及大额交易、可疑交易记录进行保存，依法协助、配合司法机关和行政执法机关打击洗钱活动，依照法律法规的规定协助司法机关、海关、税务等部门查询、冻结和扣划客户存款。
十三、互融币交易有较高的风险。
13.1 互融币交易有较高的风险。
13.1.1 互融币市场是全新的、未经确认的，而且可能不会增长。目前，互融币主要由投机者大量使用，零售和商业市场目前使用相对较少，因此互融币价格易产生波动，并进而对互融币投资产生不利影响。
13.1.2 互融世界交易平台像中国股市一样，有涨跌停限制，目前涨跌幅为昨日收盘价的10%，同时目前交易时间是周一至周五上午9:30 至 11:30，下午13:00 至 15:00 （节假日，周日及特殊通知除外）。互融币由于筹码较少，价格易受到庄家控制，可能会出现一天内波动较大的情况，请投资者注意风险。
13.2 参与互融币交易，用户应当自行控制风险，评估互融币投资价值和投资风险，承担损失全部投资的经济风险。
13.3 因国家法律、法规和规范性文件的制定或者修改，导致互融世界交易平台的交易被暂停、或者禁止的，因此造成的经济损失全部由用户自行承担。
十四、违约责任
14.1 互融世界交易平台或用户违反本协议的约定即构成违约，违约方应当向守约方承担违约责任。
14.2 如因用户提供的信息不真实、不完整或不准确给互融世界交易平台造成损失的，互融世界交易平台有权要求用户对互融世界交易平台进行损失的赔偿。
14.3 如因用户违反法律法规规定或本协议约定，在互融世界交易平台或利用互融币服务从事非法活动的，互融世界交易平台有权立即终止继续对其提供互融币服务，注销其账号，并要求其赔偿由此给互融世界交易平台造成的损失。
14.4 如用户以技术手段干扰互融世界交易平台的运行或干扰其他用户对互融世界交易平台的使用，互融世界交易平台有权立即注销其互融世界交易平台账号，并有权要求其赔偿由此给互融世界交易平台造成的损失。
14.5 如用户以虚构事实等方式恶意诋毁互融世界交易平台的商誉，互融世界交易平台有权要求用户向互融世界交易平台公开道歉，赔偿其给互融世界交易平台造成的损失，并有权终止对其提供互融世界交易平台的服务。
十五、争议解决
15.1 用户与互融世界交易平台因本协议的履行发生争议的应通过友好协商解决，协商解决不成的，任一方有权将争议提交青岛仲裁委员会依据该会仲裁规则进行仲裁。
十六、生效和解释
16.1 本协议于用户点击互融世界交易平台注册页面的“同意注册”并完成注册程序、获得互融世界交易平台账号和密码时生效，对互融世界交易平台和用户均具有约束力。
        </textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>


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
 
 seajs.use(["js/main","js/index","js/front/ico/launch","js/ueditor.all.min"],function(m,obj,launch,uedit){
	 m.init();
	 //obj.lunbo();
	 //obj.gundong();
	 //obj.init();
	 //项目
	 launch.init();
 });
 
 
 window.upload_logo=function(name){
 	 $('form[name="'+name+'"]').ajaxSubmit({
            dataType :'json',//返回数据类型
            beforeSend:function(){
                //alert("'上传中..'");
            },
            //更新进度条事件处理代码
            uploadProgress:function(event,position,total,percentComplete){
                //alert("更新进度条事件处理代码");
            },
            success:function(data){//图片上传成功时
            	debugger;
            	if(data!=null&&data.obj!=null&&data.obj[0]!=null){
            		layer.alert("上传成功！");
            		$('input[name="'+name+'"]').val(data.obj[0].fileWebPath);
            	}else{
            		layer.alert("上传失败！");
            	}
            },
            error:function(xhr){
	            layer.alert("上传失败");
            }
          });
 }
</script>



