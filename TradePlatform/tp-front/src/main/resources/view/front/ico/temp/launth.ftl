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
	<link rel="icon" type="image/x-icon"/>
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
                        <form action="#" id="information-form">
                            <div class="form-group">
                                <label for="username">用户账号：</label>
                                <span id="username">shangxol</span>
                            </div>
                            <div class="form-group">
                                <label for="email">EMAIL：</label>
                                <span id="email">sh***@163.com</span>
                            </div>
                            <div class="form-group">
                                <label for="auth">实名认证：</label>
                                <span class="verify-state"></span>
                                <span class="identify-name">尚小亮</span>
                                <span class="identify-code">622*********110</span>
                            </div>
                            <div class="form-group is-show-phone">
                                <label for="phone">手机号码：</label>
                                <span id="phone">153*********752</span>
                            </div>

                            <div class="form-group is-show-gender">
                                <label for="gender">性别：</label>
                                <div class="gender" id="asdasd">
                                    <span class="gender-male active" data-id="1">
                                          <svg width="17" height="17" viewBox="0 0 17 17">

                                          <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>男
                                      </span>
                                    <span class="gender-female" data-id="0">
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
                                            <li data-val="">请选择国籍</li>
                                            <li data-val="10">阿富汗(Afghanistan)</li>
                                            <li data-val="27">奥兰群岛(Aland lslands)</li>
                                            <li data-val="8">阿尔巴尼亚(Albania)</li>
                                            <li data-val="9">阿尔及利亚(Algeria)</li>
                                            <li data-val="141">美属萨摩亚(American Samoa)</li>
                                            <li data-val="21">安道尔(Andorra)</li>
                                            <li data-val="22">安哥拉(Angola)</li>
                                            <li data-val="23">安圭拉(Anguilla)</li>
                                            <li data-val="156">南极洲(Antarctica)</li>
                                            <li data-val="24">安提瓜岛和巴布达(Antigua and Barbuda)</li>
                                            <li data-val="11">阿根廷(Argentina)</li>
                                            <li data-val="229">亚美尼亚(Armenia)</li>
                                            <li data-val="13">阿鲁巴(Aruba)</li>
                                            <li data-val="16">阿森松岛(Ascension&nbsp;Island)</li>
                                            <li data-val="25">澳大利亚(Australia)</li>
                                            <li data-val="26">奥地利(Austria)</li>
                                            <li data-val="15">阿塞拜疆(Azerbaijan)</li>
                                            <li data-val="30">巴哈马(Bahamas)</li>
                                            <li data-val="34">巴林(Bahrain)</li>
                                            <li data-val="145">孟加拉(Bangladesh)</li>
                                            <li data-val="28">巴巴多斯岛(Barbados)</li>
                                            <li data-val="42">比利时(Belgium)</li>
                                            <li data-val="49">伯利兹(Belize)</li>
                                            <li data-val="41">贝宁(Benin)</li>
                                            <li data-val="38">百慕大(Bermuda)</li>
                                            <li data-val="50">不丹(Bhutan)</li>
                                            <li data-val="46">玻利维亚(Bolivia)</li>
                                            <li data-val="47">波斯尼亚和黑塞哥维那(Bosnia&nbsp;and&nbsp;Herzegovina)</li>
                                            <li data-val="48">博茨瓦纳(Botswana)</li>
                                            <li data-val="53">布韦岛(Bouvet&nbsp;Island)</li>
                                            <li data-val="36">巴西(Brazil)</li>
                                            <li data-val="238">英属印度洋领地(British&nbsp;Indian&nbsp;Ocean&nbsp;Territory)</li>
                                            <li data-val="216">文莱(Brunei)</li>
                                            <li data-val="39">保加利亚(Bulgaria)</li>
                                            <li data-val="51">布基纳法索(Burkina&nbsp;Faso)</li>
                                            <li data-val="52">布隆迪(Burundi)</li>
                                            <li data-val="102">柬埔寨(Cambodia)</li>
                                            <li data-val="105">喀麦隆(Cameroon)</li>
                                            <li data-val="99">加拿大(Canada)</li>
                                            <li data-val="73">佛得角(Cape&nbsp;Verde)</li>
                                            <li data-val="107">开曼群岛(Cayman&nbsp;Islands)</li>
                                            <li data-val="246">中非共和国(Central&nbsp;African&nbsp;Republic)</li>
                                            <li data-val="243">乍得(Chad)</li>
                                            <li data-val="245">智利(Chile)</li>
                                            <li data-val="7">中国(China)</li>
                                            <li data-val="4171">中国香港(China HongKong)</li>
                                            <li data-val="4170">中国台湾(China Taiwan)</li>
                                            <li data-val="181">圣诞岛(Christmas&nbsp;Island)</li>
                                            <li data-val="108">科科斯群岛(Cocos(Keeling)Islands)</li>
                                            <li data-val="78">哥伦比亚(Colombia)</li>
                                            <li data-val="109">科摩罗(Comoros)</li>
                                            <li data-val="76">刚果(Congo)</li>
                                            <li data-val="77">刚果民主共和国(Congo(DRC))</li>
                                            <li data-val="114">库克群岛(Cook&nbsp;Islands)</li>
                                            <li data-val="79">哥斯达黎加(Costa&nbsp;Rica)</li>
                                            <li data-val="110">科特迪瓦(Cote&nbsp;d'Ivoire)</li>
                                            <li data-val="112">克罗地亚(Croatia)</li>
                                            <li data-val="83">古巴(Cuba)</li>
                                            <li data-val="178">塞浦路斯(Cyprus)</li>
                                            <li data-val="103">捷克共和国(Czech&nbsp;Republic)</li>
                                            <li data-val="55">丹麦(Denmark)</li>
                                            <li data-val="95">吉布提(Djibouti)</li>
                                            <li data-val="59">多米尼加(Dominica)</li>
                                            <li data-val="60">多米尼加共和国(Dominican&nbsp;Republic)</li>
                                            <li data-val="62">厄瓜多尔(Ecuador)</li>
                                            <li data-val="17">埃及(Egypt)</li>
                                            <li data-val="173">萨尔瓦多(El&nbsp;Salvador)</li>
                                            <li data-val="63">厄立特里亚(Eritrea)</li>
                                            <li data-val="20">爱沙尼亚(Estonia)</li>
                                            <li data-val="18">埃塞俄比亚(Ethiopia)</li>
                                            <li data-val="74">弗兰克群岛(Falkland&nbsp;Islands)</li>
                                            <li data-val="65">法罗群岛(Faroe&nbsp;Islands)</li>
                                            <li data-val="71">斐济(Fiji&nbsp;Islands)</li>
                                            <li data-val="72">芬兰(Finland)</li>
                                            <li data-val="64">法国(France)</li>
                                            <li data-val="66">法属波利尼西亚(Frech&nbsp;Polynesia)</li>
                                            <li data-val="67">法属圭亚那(French&nbsp;Guiana)</li>
                                            <li data-val="68">法属南部领地(French&nbsp;Southern&nbsp;and&nbsp;Antarctic&nbsp;Lands)</li>
                                            <li data-val="101">加蓬(Gabon)</li>
                                            <li data-val="75">冈比亚(Gambia)</li>
                                            <li data-val="169">乔治亚(Georgia)</li>
                                            <li data-val="56">德国(Germany)</li>
                                            <li data-val="100">加纳(Ghana)</li>
                                            <li data-val="244">直布罗陀(Gibraltar)</li>
                                            <li data-val="222">希腊(Greece)</li>
                                            <li data-val="82">格陵兰(Greenland)</li>
                                            <li data-val="81">格林纳达(Grenada)</li>
                                            <li data-val="84">瓜德罗普(Guadeloupe)</li>
                                            <li data-val="85">关岛(Guam)</li>
                                            <li data-val="212">危地马拉(Guatemala)</li>
                                            <li data-val="80">格恩西岛(Guernsey)</li>
                                            <li data-val="97">几内亚(Guinea)</li>
                                            <li data-val="98">几内亚比绍(Guinea-Bissau)</li>
                                            <li data-val="86">圭亚那(Guyana)</li>
                                            <li data-val="88">海地(Haiti)</li>
                                            <li data-val="92">赫德和麦克唐纳群岛(Heard&nbsp;Island&nbsp;and&nbsp;McDonald&nbsp;Islands)</li>
                                            <li data-val="93">洪都拉斯(Honduras)</li>
                                            <li data-val="226">匈牙利(Hungary)</li>
                                            <li data-val="43">冰岛(Iceland)</li>
                                            <li data-val="235">印度(India)</li>
                                            <li data-val="236">印度尼西亚(Indonesia)</li>
                                            <li data-val="232">伊朗(Iran)</li>
                                            <li data-val="231">伊拉克(Iraq)</li>
                                            <li data-val="19">爱尔兰(Ireland)</li>
                                            <li data-val="137">曼岛(Isle&nbsp;of&nbsp;Man)</li>
                                            <li data-val="233">以色列(Israel)</li>
                                            <li data-val="234">意大利(Italy)</li>
                                            <li data-val="228">牙买加(Jamaica)</li>
                                            <li data-val="170">日本(Japan)</li>
                                            <li data-val="242">泽西岛(Jersey)</li>
                                            <li data-val="239">约旦(Jordan)</li>
                                            <li data-val="87">哈萨克斯坦(Kazakhstan)</li>
                                            <li data-val="113">肯尼亚(Kenya)</li>
                                            <li data-val="94">基里巴斯(Kiribati)</li>
                                            <li data-val="89">韩国(Korea)</li>
                                            <li data-val="111">科威特(Kuwait)</li>
                                            <li data-val="96">吉尔吉斯斯坦(Kyrgyzstan)</li>
                                            <li data-val="117">老挝(Laos)</li>
                                            <li data-val="115">拉脱维亚(Latvia)</li>
                                            <li data-val="118">黎巴嫩(Lebanon)</li>
                                            <li data-val="116">莱索托(Lesotho)</li>
                                            <li data-val="119">利比里亚(Liberia)</li>
                                            <li data-val="120">利比亚(Libya)</li>
                                            <li data-val="122">列支敦士登(Liechtenstein)</li>
                                            <li data-val="121">立陶宛(Lithuania)</li>
                                            <li data-val="124">卢森堡(Luxembourg)</li>
                                            <li data-val="133">马其顿(Macedonia)</li>
                                            <li data-val="127">马达加斯加(Madagascar)</li>
                                            <li data-val="130">马拉维(Malawi)</li>
                                            <li data-val="131">马来西亚(Malaysia)</li>
                                            <li data-val="128">马尔代夫(Maldives)</li>
                                            <li data-val="132">马里(Mali)</li>
                                            <li data-val="129">马耳他(Malta)</li>
                                            <li data-val="134">马绍尔群岛(Marshall&nbsp;Islands)</li>
                                            <li data-val="135">马提尼克(Martinique)</li>
                                            <li data-val="139">毛里塔尼亚(Mauritania)</li>
                                            <li data-val="138">毛里求斯(Mauritius)</li>
                                            <li data-val="136">马约特岛(Mayotte)</li>
                                            <li data-val="153">墨西哥(Mexico)</li>
                                            <li data-val="146">密克罗尼西亚(Micronesia)</li>
                                            <li data-val="149">摩尔多瓦(Moldova)</li>
                                            <li data-val="151">摩纳哥(Monaco)</li>
                                            <li data-val="143">蒙古(Mongolia)</li>
                                            <li data-val="144">蒙特塞拉特(Montserrat)</li>
                                            <li data-val="150">摩洛哥(Morocco)</li>
                                            <li data-val="152">莫桑比克(Mozambique)</li>
                                            <li data-val="148">缅甸(Myanmar)</li>
                                            <li data-val="154">纳米比亚(Namibia)</li>
                                            <li data-val="158">瑙鲁(Nauru)</li>
                                            <li data-val="159">尼泊尔(Nepal)</li>
                                            <li data-val="90">荷兰(Netherlands)</li>
                                            <li data-val="91">荷属安地列斯(Netherlands&nbsp;Antilles)</li>
                                            <li data-val="224">新喀里多尼亚(New&nbsp;Caledonia)</li>
                                            <li data-val="225">新西兰(New&nbsp;Zealand)</li>
                                            <li data-val="160">尼加拉瓜(Nicaragua)</li>
                                            <li data-val="161">尼日尔(Niger)</li>
                                            <li data-val="162">尼日利亚(Nigeria)</li>
                                            <li data-val="163">纽埃(Niue)</li>
                                            <li data-val="165">诺福克(Norfolk&nbsp;Island)</li>
                                            <li data-val="40">北马里亚纳群岛(Northern&nbsp;Mariana&nbsp;Islands)</li>
                                            <li data-val="54">朝鲜(North&nbsp;Korea)</li>
                                            <li data-val="164">挪威(Norway)</li>
                                            <li data-val="14">阿曼(Oman)</li>
                                            <li data-val="31">巴基斯坦(Pakistan)</li>
                                            <li data-val="166">帕劳群岛(Palau)</li>
                                            <li data-val="33">巴勒斯坦(Palestinian&nbsp;Authority)</li>
                                            <li data-val="35">巴拿马(Panama)</li>
                                            <li data-val="29">巴布亚新几内亚(Papua&nbsp;New&nbsp;Guinea)</li>
                                            <li data-val="32">巴拉圭(Paraguay)</li>
                                            <li data-val="147">秘鲁(Peru)</li>
                                            <li data-val="70">菲律宾(Philippines)</li>
                                            <li data-val="167">皮特凯恩(Pitcairn&nbsp;Islands)</li>
                                            <li data-val="45">波兰(Poland)</li>
                                            <li data-val="168">葡萄牙(Portugal)</li>
                                            <li data-val="44">波多黎各(Puerto&nbsp;Rico)</li>
                                            <li data-val="106">卡塔尔(Qatar)</li>
                                            <li data-val="123">留尼旺岛(Reunion)</li>
                                            <li data-val="126">罗马尼亚(Romania)</li>
                                            <li data-val="61">俄罗斯(Russia)</li>
                                            <li data-val="125">卢旺达(Rwanda)</li>
                                            <li data-val="174">萨摩亚(Samoa)</li>
                                            <li data-val="186">圣马力诺(San&nbsp;Marino)</li>
                                            <li data-val="182">圣多美和普林西比(Sao&nbsp;Tome&nbsp;and&nbsp;Principe)</li>
                                            <li data-val="180">沙特阿拉伯(Saudi&nbsp;Arabia)</li>
                                            <li data-val="177">塞内加尔(Senegal)</li>
                                            <li data-val="175">塞尔维亚,黑山(Serbia,Montenegro)</li>
                                            <li data-val="179">塞舌尔(Seychelles)</li>
                                            <li data-val="176">塞拉利昂(Sierra&nbsp;Leone)</li>
                                            <li data-val="223">新加坡(Singapore)</li>
                                            <li data-val="190">斯洛伐克(Slovakia)</li>
                                            <li data-val="191">斯洛文尼亚(Slovenia)</li>
                                            <li data-val="196">所罗门群岛(Solomon&nbsp;Islands)</li>
                                            <li data-val="197">索马里(Somalia)</li>
                                            <li data-val="155">南非(South&nbsp;Africa)</li>
                                            <li data-val="157">南乔治亚和南桑德威奇群岛(South&nbsp;Georgia&nbsp;and&nbsp;South&nbsp;Sandwich&nbsp;Islands)</li>
                                            <li data-val="221">西班牙(Spain)</li>
                                            <li data-val="189">斯里兰卡(Sri&nbsp;Lanka)</li>
                                            <li data-val="183">圣赫勒拿(St.Helena)</li>
                                            <li data-val="184">圣基茨和尼维斯(St.Kitts&nbsp;and&nbsp;Nevis)</li>
                                            <li data-val="185">圣卢西亚(St.Lucia)</li>
                                            <li data-val="187">圣皮埃尔和米克隆群岛(St.Pierre&nbsp;and&nbsp;Miquelon)</li>
                                            <li data-val="188">圣文森特和格林纳丁斯(St.Vincent&nbsp;and&nbsp;the&nbsp;Grenadines)</li>
                                            <li data-val="194">苏丹(Sudan)</li>
                                            <li data-val="195">苏里南(Suriname)</li>
                                            <li data-val="192">斯瓦尔巴和扬马廷(Svalbard&nbsp;and&nbsp;Jan&nbsp;Mayen)</li>
                                            <li data-val="193">斯威士兰(Swaziland)</li>
                                            <li data-val="171">瑞典(Sweden)</li>
                                            <li data-val="172">瑞士(Switzerland)</li>
                                            <li data-val="227">叙利亚(Syria)</li>
                                            <li data-val="198">塔吉克斯坦(Tajikistan)</li>
                                            <li data-val="200">坦桑尼亚(Tanzania)</li>
                                            <li data-val="199">泰国(Thailand)</li>
                                            <li data-val="57">东帝汶(Timor-Leste)</li>
                                            <li data-val="58">多哥(Togo)</li>
                                            <li data-val="209">托克劳(Tokelau)</li>
                                            <li data-val="201">汤加(Tonga)</li>
                                            <li data-val="204">特立尼达和多巴哥(Trinidad&nbsp;and&nbsp;Tobago)</li>
                                            <li data-val="203">特里斯坦达昆哈(Tristan&nbsp;da&nbsp;Cunha)</li>
                                            <li data-val="205">突尼斯(Tunisia)</li>
                                            <li data-val="207">土耳其(Turkey)</li>
                                            <li data-val="208">土库曼斯坦(Turkmenistan)</li>
                                            <li data-val="202">特克斯和凯克特斯群岛(Turks&nbsp;and&nbsp;Caicos&nbsp;Islands)</li>
                                            <li data-val="206">图瓦卢(Tuvalu)</li>
                                            <li data-val="217">乌干达(Uganda)</li>
                                            <li data-val="218">乌克兰(Ukraine)</li>
                                            <li data-val="12">阿拉伯联合酋长国(United&nbsp;Arab&nbsp;Emirates)</li>
                                            <li data-val="237">英国(United&nbsp;Kingdom)</li>
                                            <li data-val="140">美国(United&nbsp;States)</li>
                                            <li data-val="142">美属外岛(United&nbsp;States&nbsp;Minor&nbsp;Outlying&nbsp;Islands)</li>
                                            <li data-val="219">乌拉圭(Uruguay)</li>
                                            <li data-val="220">乌兹别克斯坦(Uzbekistan)</li>
                                            <li data-val="211">瓦努阿图(Vanuatu)</li>
                                            <li data-val="69">梵蒂冈(Vatican&nbsp;City)</li>
                                            <li data-val="215">委内瑞拉(Venezuela)</li>
                                            <li data-val="240">越南(Vietnam)</li>
                                            <li data-val="213">维尔京群岛，美属(Virgin&nbsp;Islands)</li>
                                            <li data-val="214">维尔京群岛，英属(Virgin&nbsp;Islands,British)</li>
                                            <li data-val="210">瓦利斯和福图纳(Wallis&nbsp;and&nbsp;Futuna)</li>
                                            <li data-val="37">白俄罗斯(White&nbsp;Russia)</li>
                                            <li data-val="230">也门(Yemen)</li>
                                            <li data-val="241">赞比亚(Zambia)</li>
                                            <li data-val="104">津巴布韦(Zimbabwe)</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="address">详细地址：</label>
                                <input type="text" name="address" id="address">
                            </div>
                        </form>
                    </div>
                    <div class="person-info-footer">
                        <div class="launch-save-operate">
                            <button type="button" id="person_info_save">保存</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" id="person_info_save_next" data-step="two">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">

                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                </div>
                <!-- part two -->
                <div class="launch-part-two launch-part" data-part="two" style="display:block">
                    <div class="launch-project-info">
                        <div id="project_info_form">
                            <div class="form-group">
                                <label for="name"><b class="is-required">*</b>项目名称：</label>
                                <input type="text" name="name" id="name" maxlength="500" placeholder="请输入项目名称">
                            </div>
                            <div class="form-group">
                                <label for="stage"><b class="is-required">*</b>所属阶段：</label>

                                <div class="stage-select">
                                    <p class="pro-select-header">
                                        <span class="pro-stage-txt" id="progess_id">请选择阶段</span>
                                        <input type="hidden" name="progess_id">
                                        <span class="stage-select-btn">
                                              <svg width="10" height="10" viewBox="0 0 10 6">

                                              <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                          </span>
                                    </p>
                                    <ul class="stage-list pro-stage-list">
                                        <li data-val="">请选择阶段</li>
                                        <li data-val="1">尚未启动</li>
                                        <li data-val="2">产品开发中</li>
                                        <li data-val="3">产品已上市</li>
                                        <li data-val="4">已经盈利</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="person_name"><b class="is-required">*</b>真实姓名：</label>
                                <input type="text" name="person_name" id="person_name" placeholder="请输入真实姓名">
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
                                            <li data-val="10">阿富汗(Afghanistan)</li>
                                            <li data-val="27">奥兰群岛(Aland lslands)</li>
                                            <li data-val="8">阿尔巴尼亚(Albania)</li>
                                            <li data-val="9">阿尔及利亚(Algeria)</li>
                                            <li data-val="141">美属萨摩亚(American Samoa)</li>
                                            <li data-val="21">安道尔(Andorra)</li>
                                            <li data-val="22">安哥拉(Angola)</li>
                                            <li data-val="23">安圭拉(Anguilla)</li>
                                            <li data-val="156">南极洲(Antarctica)</li>
                                            <li data-val="24">安提瓜岛和巴布达(Antigua and Barbuda)</li>
                                            <li data-val="11">阿根廷(Argentina)</li>
                                            <li data-val="229">亚美尼亚(Armenia)</li>
                                            <li data-val="13">阿鲁巴(Aruba)</li>
                                            <li data-val="16">阿森松岛(Ascension&nbsp;Island)</li>
                                            <li data-val="25">澳大利亚(Australia)</li>
                                            <li data-val="26">奥地利(Austria)</li>
                                            <li data-val="15">阿塞拜疆(Azerbaijan)</li>
                                            <li data-val="30">巴哈马(Bahamas)</li>
                                            <li data-val="34">巴林(Bahrain)</li>
                                            <li data-val="145">孟加拉(Bangladesh)</li>
                                            <li data-val="28">巴巴多斯岛(Barbados)</li>
                                            <li data-val="42">比利时(Belgium)</li>
                                            <li data-val="49">伯利兹(Belize)</li>
                                            <li data-val="41">贝宁(Benin)</li>
                                            <li data-val="38">百慕大(Bermuda)</li>
                                            <li data-val="50">不丹(Bhutan)</li>
                                            <li data-val="46">玻利维亚(Bolivia)</li>
                                            <li data-val="47">波斯尼亚和黑塞哥维那(Bosnia&nbsp;and&nbsp;Herzegovina)</li>
                                            <li data-val="48">博茨瓦纳(Botswana)</li>
                                            <li data-val="53">布韦岛(Bouvet&nbsp;Island)</li>
                                            <li data-val="36">巴西(Brazil)</li>
                                            <li data-val="238">英属印度洋领地(British&nbsp;Indian&nbsp;Ocean&nbsp;Territory)</li>
                                            <li data-val="216">文莱(Brunei)</li>
                                            <li data-val="39">保加利亚(Bulgaria)</li>
                                            <li data-val="51">布基纳法索(Burkina&nbsp;Faso)</li>
                                            <li data-val="52">布隆迪(Burundi)</li>
                                            <li data-val="102">柬埔寨(Cambodia)</li>
                                            <li data-val="105">喀麦隆(Cameroon)</li>
                                            <li data-val="99">加拿大(Canada)</li>
                                            <li data-val="73">佛得角(Cape&nbsp;Verde)</li>
                                            <li data-val="107">开曼群岛(Cayman&nbsp;Islands)</li>
                                            <li data-val="246">中非共和国(Central&nbsp;African&nbsp;Republic)</li>
                                            <li data-val="243">乍得(Chad)</li>
                                            <li data-val="245">智利(Chile)</li>
                                            <li data-val="7">中国(China)</li>
                                            <li data-val="4171">中国香港(China HongKong)</li>
                                            <li data-val="4170">中国台湾(China Taiwan)</li>
                                            <li data-val="181">圣诞岛(Christmas&nbsp;Island)</li>
                                            <li data-val="108">科科斯群岛(Cocos(Keeling)Islands)</li>
                                            <li data-val="78">哥伦比亚(Colombia)</li>
                                            <li data-val="109">科摩罗(Comoros)</li>
                                            <li data-val="76">刚果(Congo)</li>
                                            <li data-val="77">刚果民主共和国(Congo(DRC))</li>
                                            <li data-val="114">库克群岛(Cook&nbsp;Islands)</li>
                                            <li data-val="79">哥斯达黎加(Costa&nbsp;Rica)</li>
                                            <li data-val="110">科特迪瓦(Cote&nbsp;d'Ivoire)</li>
                                            <li data-val="112">克罗地亚(Croatia)</li>
                                            <li data-val="83">古巴(Cuba)</li>
                                            <li data-val="178">塞浦路斯(Cyprus)</li>
                                            <li data-val="103">捷克共和国(Czech&nbsp;Republic)</li>
                                            <li data-val="55">丹麦(Denmark)</li>
                                            <li data-val="95">吉布提(Djibouti)</li>
                                            <li data-val="59">多米尼加(Dominica)</li>
                                            <li data-val="60">多米尼加共和国(Dominican&nbsp;Republic)</li>
                                            <li data-val="62">厄瓜多尔(Ecuador)</li>
                                            <li data-val="17">埃及(Egypt)</li>
                                            <li data-val="173">萨尔瓦多(El&nbsp;Salvador)</li>
                                            <li data-val="63">厄立特里亚(Eritrea)</li>
                                            <li data-val="20">爱沙尼亚(Estonia)</li>
                                            <li data-val="18">埃塞俄比亚(Ethiopia)</li>
                                            <li data-val="74">弗兰克群岛(Falkland&nbsp;Islands)</li>
                                            <li data-val="65">法罗群岛(Faroe&nbsp;Islands)</li>
                                            <li data-val="71">斐济(Fiji&nbsp;Islands)</li>
                                            <li data-val="72">芬兰(Finland)</li>
                                            <li data-val="64">法国(France)</li>
                                            <li data-val="66">法属波利尼西亚(Frech&nbsp;Polynesia)</li>
                                            <li data-val="67">法属圭亚那(French&nbsp;Guiana)</li>
                                            <li data-val="68">法属南部领地(French&nbsp;Southern&nbsp;and&nbsp;Antarctic&nbsp;Lands)</li>
                                            <li data-val="101">加蓬(Gabon)</li>
                                            <li data-val="75">冈比亚(Gambia)</li>
                                            <li data-val="169">乔治亚(Georgia)</li>
                                            <li data-val="56">德国(Germany)</li>
                                            <li data-val="100">加纳(Ghana)</li>
                                            <li data-val="244">直布罗陀(Gibraltar)</li>
                                            <li data-val="222">希腊(Greece)</li>
                                            <li data-val="82">格陵兰(Greenland)</li>
                                            <li data-val="81">格林纳达(Grenada)</li>
                                            <li data-val="84">瓜德罗普(Guadeloupe)</li>
                                            <li data-val="85">关岛(Guam)</li>
                                            <li data-val="212">危地马拉(Guatemala)</li>
                                            <li data-val="80">格恩西岛(Guernsey)</li>
                                            <li data-val="97">几内亚(Guinea)</li>
                                            <li data-val="98">几内亚比绍(Guinea-Bissau)</li>
                                            <li data-val="86">圭亚那(Guyana)</li>
                                            <li data-val="88">海地(Haiti)</li>
                                            <li data-val="92">赫德和麦克唐纳群岛(Heard&nbsp;Island&nbsp;and&nbsp;McDonald&nbsp;Islands)</li>
                                            <li data-val="93">洪都拉斯(Honduras)</li>
                                            <li data-val="226">匈牙利(Hungary)</li>
                                            <li data-val="43">冰岛(Iceland)</li>
                                            <li data-val="235">印度(India)</li>
                                            <li data-val="236">印度尼西亚(Indonesia)</li>
                                            <li data-val="232">伊朗(Iran)</li>
                                            <li data-val="231">伊拉克(Iraq)</li>
                                            <li data-val="19">爱尔兰(Ireland)</li>
                                            <li data-val="137">曼岛(Isle&nbsp;of&nbsp;Man)</li>
                                            <li data-val="233">以色列(Israel)</li>
                                            <li data-val="234">意大利(Italy)</li>
                                            <li data-val="228">牙买加(Jamaica)</li>
                                            <li data-val="170">日本(Japan)</li>
                                            <li data-val="242">泽西岛(Jersey)</li>
                                            <li data-val="239">约旦(Jordan)</li>
                                            <li data-val="87">哈萨克斯坦(Kazakhstan)</li>
                                            <li data-val="113">肯尼亚(Kenya)</li>
                                            <li data-val="94">基里巴斯(Kiribati)</li>
                                            <li data-val="89">韩国(Korea)</li>
                                            <li data-val="111">科威特(Kuwait)</li>
                                            <li data-val="96">吉尔吉斯斯坦(Kyrgyzstan)</li>
                                            <li data-val="117">老挝(Laos)</li>
                                            <li data-val="115">拉脱维亚(Latvia)</li>
                                            <li data-val="118">黎巴嫩(Lebanon)</li>
                                            <li data-val="116">莱索托(Lesotho)</li>
                                            <li data-val="119">利比里亚(Liberia)</li>
                                            <li data-val="120">利比亚(Libya)</li>
                                            <li data-val="122">列支敦士登(Liechtenstein)</li>
                                            <li data-val="121">立陶宛(Lithuania)</li>
                                            <li data-val="124">卢森堡(Luxembourg)</li>
                                            <li data-val="133">马其顿(Macedonia)</li>
                                            <li data-val="127">马达加斯加(Madagascar)</li>
                                            <li data-val="130">马拉维(Malawi)</li>
                                            <li data-val="131">马来西亚(Malaysia)</li>
                                            <li data-val="128">马尔代夫(Maldives)</li>
                                            <li data-val="132">马里(Mali)</li>
                                            <li data-val="129">马耳他(Malta)</li>
                                            <li data-val="134">马绍尔群岛(Marshall&nbsp;Islands)</li>
                                            <li data-val="135">马提尼克(Martinique)</li>
                                            <li data-val="139">毛里塔尼亚(Mauritania)</li>
                                            <li data-val="138">毛里求斯(Mauritius)</li>
                                            <li data-val="136">马约特岛(Mayotte)</li>
                                            <li data-val="153">墨西哥(Mexico)</li>
                                            <li data-val="146">密克罗尼西亚(Micronesia)</li>
                                            <li data-val="149">摩尔多瓦(Moldova)</li>
                                            <li data-val="151">摩纳哥(Monaco)</li>
                                            <li data-val="143">蒙古(Mongolia)</li>
                                            <li data-val="144">蒙特塞拉特(Montserrat)</li>
                                            <li data-val="150">摩洛哥(Morocco)</li>
                                            <li data-val="152">莫桑比克(Mozambique)</li>
                                            <li data-val="148">缅甸(Myanmar)</li>
                                            <li data-val="154">纳米比亚(Namibia)</li>
                                            <li data-val="158">瑙鲁(Nauru)</li>
                                            <li data-val="159">尼泊尔(Nepal)</li>
                                            <li data-val="90">荷兰(Netherlands)</li>
                                            <li data-val="91">荷属安地列斯(Netherlands&nbsp;Antilles)</li>
                                            <li data-val="224">新喀里多尼亚(New&nbsp;Caledonia)</li>
                                            <li data-val="225">新西兰(New&nbsp;Zealand)</li>
                                            <li data-val="160">尼加拉瓜(Nicaragua)</li>
                                            <li data-val="161">尼日尔(Niger)</li>
                                            <li data-val="162">尼日利亚(Nigeria)</li>
                                            <li data-val="163">纽埃(Niue)</li>
                                            <li data-val="165">诺福克(Norfolk&nbsp;Island)</li>
                                            <li data-val="40">北马里亚纳群岛(Northern&nbsp;Mariana&nbsp;Islands)</li>
                                            <li data-val="54">朝鲜(North&nbsp;Korea)</li>
                                            <li data-val="164">挪威(Norway)</li>
                                            <li data-val="14">阿曼(Oman)</li>
                                            <li data-val="31">巴基斯坦(Pakistan)</li>
                                            <li data-val="166">帕劳群岛(Palau)</li>
                                            <li data-val="33">巴勒斯坦(Palestinian&nbsp;Authority)</li>
                                            <li data-val="35">巴拿马(Panama)</li>
                                            <li data-val="29">巴布亚新几内亚(Papua&nbsp;New&nbsp;Guinea)</li>
                                            <li data-val="32">巴拉圭(Paraguay)</li>
                                            <li data-val="147">秘鲁(Peru)</li>
                                            <li data-val="70">菲律宾(Philippines)</li>
                                            <li data-val="167">皮特凯恩(Pitcairn&nbsp;Islands)</li>
                                            <li data-val="45">波兰(Poland)</li>
                                            <li data-val="168">葡萄牙(Portugal)</li>
                                            <li data-val="44">波多黎各(Puerto&nbsp;Rico)</li>
                                            <li data-val="106">卡塔尔(Qatar)</li>
                                            <li data-val="123">留尼旺岛(Reunion)</li>
                                            <li data-val="126">罗马尼亚(Romania)</li>
                                            <li data-val="61">俄罗斯(Russia)</li>
                                            <li data-val="125">卢旺达(Rwanda)</li>
                                            <li data-val="174">萨摩亚(Samoa)</li>
                                            <li data-val="186">圣马力诺(San&nbsp;Marino)</li>
                                            <li data-val="182">圣多美和普林西比(Sao&nbsp;Tome&nbsp;and&nbsp;Principe)</li>
                                            <li data-val="180">沙特阿拉伯(Saudi&nbsp;Arabia)</li>
                                            <li data-val="177">塞内加尔(Senegal)</li>
                                            <li data-val="175">塞尔维亚,黑山(Serbia,Montenegro)</li>
                                            <li data-val="179">塞舌尔(Seychelles)</li>
                                            <li data-val="176">塞拉利昂(Sierra&nbsp;Leone)</li>
                                            <li data-val="223">新加坡(Singapore)</li>
                                            <li data-val="190">斯洛伐克(Slovakia)</li>
                                            <li data-val="191">斯洛文尼亚(Slovenia)</li>
                                            <li data-val="196">所罗门群岛(Solomon&nbsp;Islands)</li>
                                            <li data-val="197">索马里(Somalia)</li>
                                            <li data-val="155">南非(South&nbsp;Africa)</li>
                                            <li data-val="157">南乔治亚和南桑德威奇群岛(South&nbsp;Georgia&nbsp;and&nbsp;South&nbsp;Sandwich&nbsp;Islands)</li>
                                            <li data-val="221">西班牙(Spain)</li>
                                            <li data-val="189">斯里兰卡(Sri&nbsp;Lanka)</li>
                                            <li data-val="183">圣赫勒拿(St.Helena)</li>
                                            <li data-val="184">圣基茨和尼维斯(St.Kitts&nbsp;and&nbsp;Nevis)</li>
                                            <li data-val="185">圣卢西亚(St.Lucia)</li>
                                            <li data-val="187">圣皮埃尔和米克隆群岛(St.Pierre&nbsp;and&nbsp;Miquelon)</li>
                                            <li data-val="188">圣文森特和格林纳丁斯(St.Vincent&nbsp;and&nbsp;the&nbsp;Grenadines)</li>
                                            <li data-val="194">苏丹(Sudan)</li>
                                            <li data-val="195">苏里南(Suriname)</li>
                                            <li data-val="192">斯瓦尔巴和扬马廷(Svalbard&nbsp;and&nbsp;Jan&nbsp;Mayen)</li>
                                            <li data-val="193">斯威士兰(Swaziland)</li>
                                            <li data-val="171">瑞典(Sweden)</li>
                                            <li data-val="172">瑞士(Switzerland)</li>
                                            <li data-val="227">叙利亚(Syria)</li>
                                            <li data-val="198">塔吉克斯坦(Tajikistan)</li>
                                            <li data-val="200">坦桑尼亚(Tanzania)</li>
                                            <li data-val="199">泰国(Thailand)</li>
                                            <li data-val="57">东帝汶(Timor-Leste)</li>
                                            <li data-val="58">多哥(Togo)</li>
                                            <li data-val="209">托克劳(Tokelau)</li>
                                            <li data-val="201">汤加(Tonga)</li>
                                            <li data-val="204">特立尼达和多巴哥(Trinidad&nbsp;and&nbsp;Tobago)</li>
                                            <li data-val="203">特里斯坦达昆哈(Tristan&nbsp;da&nbsp;Cunha)</li>
                                            <li data-val="205">突尼斯(Tunisia)</li>
                                            <li data-val="207">土耳其(Turkey)</li>
                                            <li data-val="208">土库曼斯坦(Turkmenistan)</li>
                                            <li data-val="202">特克斯和凯克特斯群岛(Turks&nbsp;and&nbsp;Caicos&nbsp;Islands)</li>
                                            <li data-val="206">图瓦卢(Tuvalu)</li>
                                            <li data-val="217">乌干达(Uganda)</li>
                                            <li data-val="218">乌克兰(Ukraine)</li>
                                            <li data-val="12">阿拉伯联合酋长国(United&nbsp;Arab&nbsp;Emirates)</li>
                                            <li data-val="237">英国(United&nbsp;Kingdom)</li>
                                            <li data-val="140">美国(United&nbsp;States)</li>
                                            <li data-val="142">美属外岛(United&nbsp;States&nbsp;Minor&nbsp;Outlying&nbsp;Islands)</li>
                                            <li data-val="219">乌拉圭(Uruguay)</li>
                                            <li data-val="220">乌兹别克斯坦(Uzbekistan)</li>
                                            <li data-val="211">瓦努阿图(Vanuatu)</li>
                                            <li data-val="69">梵蒂冈(Vatican&nbsp;City)</li>
                                            <li data-val="215">委内瑞拉(Venezuela)</li>
                                            <li data-val="240">越南(Vietnam)</li>
                                            <li data-val="213">维尔京群岛，美属(Virgin&nbsp;Islands)</li>
                                            <li data-val="214">维尔京群岛，英属(Virgin&nbsp;Islands,British)</li>
                                            <li data-val="210">瓦利斯和福图纳(Wallis&nbsp;and&nbsp;Futuna)</li>
                                            <li data-val="37">白俄罗斯(White&nbsp;Russia)</li>
                                            <li data-val="230">也门(Yemen)</li>
                                            <li data-val="241">赞比亚(Zambia)</li>
                                            <li data-val="104">津巴布韦(Zimbabwe)</li>
                                        </ul>
                                    </div>
                                </div>
                                <input type="text" name="office_address" id="office_address" placeholder="请输入详细办公地址" maxlength="100">
                            </div>
                            <div class="form-group">
                                <label for="join_count"><b class="is-required">*</b>团队人数：</label>
                                <input type="text" name="join_count" id="join_count" placeholder="请输入人数">
                            </div>
                            <div class="form-group">
                                <label for="finance_stage"><b class="is-required">*</b>融资阶段：</label>
                                <div class="stage-select">
                                    <p class="finance-select-header" id="financing_id">
                                        <span class="finance-stage-txt">请选择阶段</span>
                                        <input type="hidden" name="financing_id">
                                        <span class="stage-select-btn">
                                              <svg width="10" height="10" viewBox="0 0 10 6">

                                              <path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                          </span>
                                    </p>
                                    <ul class="stage-list finance-stage-list">
                                        <li data-val="">请选择阶段</li>
                                        <li data-val="1">天使轮</li>
                                        <li data-val="2">A轮</li>
                                        <li data-val="3">B轮</li>
                                        <li data-val="4">C轮</li>
                                        <li data-val="5">D轮</li>
                                        <li data-val="6">未融资</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="introduction"><b class="is-required">*</b>项目简介：</label>
                                <textarea name="introduction" id="introduction" maxlength="1000" placeholder="请输入项目简介"></textarea>
                            </div>
                            <div class="form-group">
                                <label for="video_address">宣传视频：</label>
                                <input type="text" name="video_address" id="video_address" placeholder="请输入Flash视频地址(支持爱奇艺、腾讯、优酷、土豆、酷6、新浪、搜狐视频)">
                            </div>
                            <div class="form-group">
                                <form action="" class="uploadForm">
                                    <label for="upload_img"><b class="is-required">*</b>封面图片：</label>
                                    <input type="hidden" name="type" value="1">
                                    <button type="button" id="pic_address">上传图片<input type="file" name="picture" id="upload_img_file" value="上传图片"></button>
                                    <span>支持JPG/JPEG/PNG格式；建议尺寸：790x461px；最多3张【图片大小不超过1M】</span>
                                    <div class="img-preview">
                                    </div>
                                </form>
                            </div>
                            <div class="form-group">
                                <label for="upload_file"><b class="is-required">*</b>上传附件：</label>
                                <span> 请上传项目白皮书、商业计划书、其他资料文档，支持Word/Excle/PPT/PDF格式【文件大小不超过10M】</span>
                                <div class="upload-file">
                                    <ul class="upload-ul">
                                        <li class="upload-file-item">
                                            <form action="" class="uploadForm">
                                                <div class="upload-file-button upload-white_paper" id="white_pager">
                                                    <input type="file" name="file" class="file-upload">
                                                    <svg class="svg-center" height="34px" width="34px" viewBox="0 0 34 34">

                                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                                </div>
                                                <p>项目白皮书</p>
                                            </form>
                                        </li>
                                        <li class="upload-file-item">
                                            <form action="" class="uploadForm">
                                                <div class="upload-file-button upload-item_plan" id="item_plan">
                                                    <input type="file" name="file" class="file-upload">
                                                    <svg class="svg-center" height="34px" width="34px" viewBox="0 0 34 34">

                                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                                </div>
                                                <p>商业计划书</p>
                                            </form>
                                        </li>
                                        <li class="upload-file-item">
                                            <form action="" class="uploadForm">
                                                <div class="upload-file-button upload-item_others" id="item_others">
                                                    <input type="file" name="file" class="file-upload">
                                                    <svg class="svg-center" height="34px" width="34px" viewBox="0 0 34 34">

                                                      <circle class="add-circle" fill="#BEBEBE" cx="17" cy="17" r="17"></circle><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M7 15.345h20v3.31H7z"></path><path class="add-rect" fill-rule="evenodd" clip-rule="evenodd" fill="#FFF" d="M15.345 7h3.31v20h-3.31z"></path></svg>
                                                </div>
                                                <p>其他资料</p>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="project-info-footer">
                        <div class="launch-pre-operate">
                            <button type="button" class="operate-prev-btn" data-step="one"><svg width="25" height="14" viewBox="0 0 9 14">

                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>上一步</button>
                        </div>
                        <div class="launch-save-operate">
                            <button type="button" id="pro_info_save">保存</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" id="pro_info_save_next" data-step="three">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">

                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                </div>
                <!-- part three -->
                <div class="launch-part-three launch-part" data-part="three">
                    <div class="launch-pro-introduction">
                        <div id="detail">
                            <textarea name="content" style="width: 800px; height: 800px;">
                          </textarea>
                        </div>
                    </div>
                    <div class="project-intro-footer">
                        <div class="launch-pre-operate">
                            <button type="button" class="operate-prev-btn" data-step="two"><svg width="25" height="14" viewBox="0 0 9 14">
                        <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>上一步</button>
                        </div>
                        <div class="launch-save-operate">
                            <button type="button" id="pro_intro_save">保存</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" id="pro_intro_save_next" data-step="four">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">
                        <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
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
                            <form action="#" id="pro_plan_form">
                                <input type="hidden" value="" id="plan_id">
                                <div class="form-group">
                                    <label for="curtype"><b class="is-required">*</b>投资币种：</label>
                                    <span class="curtype_limit">
                                          <span class="edit-focus curtype-btc active" data-id="BTC">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>BTC
                                          </span>
                                    <span class="edit-focus curtype-eth" data-id="ETH">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>ETH
                                          </span>
                                    <span class="edit-focus curtype-etc" data-id="ETC">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>ETC
                                          </span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="money"><b class="is-required">*</b>投资金额：</label>
                                    <span class="money_limit">
                                          <span class="edit-focus money-custom" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>不限
                                          </span>
                                    <span class="edit-focus money-astrict active" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">

                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>限制
                                          </span>
                                    </span>
                                    <span class="is-show-money">
                                          <input type="text" id="limit_money" class="edit-focus">
                                          <span class="money-type">BTC</span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="plan_intro"><b class="is-required">*</b>回报说明：</label>
                                    <textarea name="describe" id="describe" class="edit-focus" cols="30" rows="10" maxlength="150" placeholder="请输入您的回报说明，不超过150字"></textarea>
                                </div>
                                <div class="form-group">
                                    <label for="quota_limit"><b class="is-required">*</b>名额限制：</label>
                                    <span class="quota_limit">
                                          <span class="edit-focus quota-no active" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>否
                                          </span>
                                    <span class="edit-focus quota-yes" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>是
                                          </span>
                                    </span>
                                    <span class="is-show-quota hide">
                                          <label for="limit_persons" class="label-quota">名额数量：</label>
                                          <input type="text" id="limit_persons" class="edit-focus">
                                          <span></span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="buy_limit"><b class="is-required">*</b>支持限制：</label>
                                    <span class="buy_limit">
                                          <span class="edit-focus buy-no active" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>否
                                          </span>
                                    <span class="edit-focus buy-yes" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>是
                                          </span>
                                    </span>
                                    <span class="is-show-buy hide">
                                          <label for="limit_support" class="label-quota">此回报每个用户只能购买</label>
                                          <input type="text" id="limit_support" class="edit-focus">
                                          <span>&nbsp;次</span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="free_shipping"><b class="is-required">*</b>是否包邮：</label>
                                    <span class="free_shipping" id="post_state">
                                          <span class="edit-focus post-no active" data-id="0">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>否
                                          </span>
                                    <span class="edit-focus post-yes" data-id="1">
                                              <svg width="17" height="17" viewBox="0 0 17 17">
                                              <path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>大陆包邮
                                          </span>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="reward_time"><b class="is-required">*</b>回报时间：</label>
                                    <span class="reward-txt">预计项目ICO成功结束后&nbsp;</span>
                                    <input type="text" name="report_day" id="report_day" class="edit-focus">
                                    <span class="reward-txt">&nbsp;天内</span>
                                </div>
                                <div class="form-group">
                                    <button type="button" class="launch-plan-save">保存</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="project-plan-footer">
                        <div class="launch-pre-operate">
                            <button type="button" class="operate-prev-btn" data-step="three"><svg width="25" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>上一步</button>
                        </div>
                        <div class="launch-next-operate">
                            <button type="button" id="pro_plan_save_next" data-step="five">保存并下一步<svg width="18" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path></svg></button>
                        </div>
                    </div>
                </div>
                <!-- part five -->
                <div class="launch-part-five launch-part" data-part="five">
                    <div class="launch-submit-content">
                        <div class="launch-submit-body">
                            <form action="#" class="launch-submit-form">
                                <div class="form-group">
                                    <label for="funding_day"><b class="is-required">*</b>ICO天数：</label>
                                    <input type="text" name="funding_day" id="funding_day">
                                    <span>&nbsp;天</span>
                                </div>
                                <div class="form-group">
                                    <label for="target_money"><b class="is-required">*</b>目标金额：</label>
                                    <span class="">
                                          <div class="limittype-select">
                                              <p class="limittype-select-header" id="limittype">
                                                  <span class="limittype-txt">不限</span>
                                                  <input type="hidden" name="target_money_state" id="target_money_state" value="0">
                                                  <svg width="10" height="10" viewBox="0 0 10 6"><path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                              </p>
                                              <ul class="limittype-list">
                                                  <li data-val="0" data-id="0">不限</li>
                                                  <li data-val="1" data-id="1">限制</li>
                                              </ul>
                                            </div>
                                    </span>
                                    <input type="text" name="target_money" id="target_money" class="hide">
                                    <span class="">
                                          <div class="moneytype-select">
                                              <p class="moneytype-select-header" id="moneytype">
                                                  <span class="moneytype-txt">BTC</span>
                                                  <input type="hidden" name="limit_curtype_state" value="BTC">
                                                  <svg width="10" height="10" viewBox="0 0 10 6"><path class="triangle-down" fill="#636363" d="M5 6L0 0h10z"></path></svg>
                                              </p>
                                              <ul class="moneytype-list">
                                                  <li data-val="BTC">BTC</li>
                                                  <li data-val="ETH">ETH</li>
                                                  <li data-val="ETC">ETC</li>
                                              </ul>
                                          </div>
                                  </span>
                                </div>
                                <div class="form-group">
                                    <label for="contacts"><b class="is-required">*</b>联 系 人：</label>
                                    <input type="text" name="contacts" id="contacts" placeholder="请输入联系人姓名">
                                </div>
                                <div class="form-group">
                                    <label for="response_job"><b class="is-required">*</b>负责职位：</label>
                                    <input type="text" name="contacts_job" id="contacts_job" placeholder="请输入负责人职位">
                                </div>
                                <div class="form-group">
                                    <label for="contacts_phone"><b class="is-required">*</b>联系电话：</label>
                                    <input type="text" name="contacts_phone" id="contacts_phone" placeholder="请输入负责人联系电话">
                                </div>
                                <div class="form-group">
                                    <label for="apply_starttime"><b class="is-required">*</b>开始时间：</label>
                                    <span>申请&nbsp;</span>
                                    <input type="text" name="apply_starttime" id="apply_starttime" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
                                    <span>&nbsp;天内开始</span>
                                </div>
                            </form>
                            <div class="launch-submit-tips">
                              <span class="ico-agree">
                                  <svg height="18" width="18" viewBox="0 0 18 18">
                                  <path class="check-color"  fill-rule="evenodd" clip-rule="evenodd" fill="#24D96D" d="M3.389 8.418l3.273 2.609 8.151-7.51s.547-.5 1.024-.109c.143.118.307.45-.063.97l-8.511 9.97s-.653.893-1.427-.01L2.165 9.379s-.436-.671.109-1.075c.184-.135.602-.345 1.115.114m0 0"></path><path class="border-color" fill="#9B9B9B" d="M17 1v16H1V1h16m1-1H0v18h18V0z"></path></svg>
                              </span>
                              <span>请认真阅读<a href="/about/sponsor-agreement" target="_blank">《发起者协议》</a>，确认项目内容符合规范。</span>
                            </div>
                        </div>
                    </div>
                    <div class="project-submit-footer">
                <div class="launch-pre-operate">
                    <button type="button" class="operate-prev-btn" data-step="four">
                      <svg width="25" height="14" viewBox="0 0 9 14">
                                  <path fill="currentColor" d="M0 14V0l9 7z"></path>
                      </svg>上一步
                    </button>
                </div>
                <div class="launch-save-operate">
                    <button type="button" id="preview_save_btn">保存</button>
                </div>
                <div class="launch-next-operate">
                    <button type="button" id="pro_submit_btn">提交审核
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



