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

        
<!--pitch content start--> 
        <div id="pay_content" class="ico-content" style="margin-top:45px;">
        <section class="banner-body">
            <div class="banner-bg">
                <div class="banner-center">
                    <div class="banner-process clearfix">
                        <div class="node node-one ready focus">
                            <svg class="icon-proce" height="60px" width="60px" viewBox="0 0 60 60">
                            <path class="proce-bg-color" fill="#bfc8ff" d="M57.521 23.991c3.305 3.305 3.305 8.714 0 12.019L36.01 57.521c-3.305 3.305-8.713 3.308-12.018.002L2.479 36.008c-3.305-3.306-3.305-8.712 0-12.017L23.991 2.479c3.305-3.305 8.712-3.305 12.018 0l21.512 21.512z"></path><path class="proce-num-color" fill="#3F4B94" d="M33.372 18.933v22.135h-5.526V29.2c0-1.713-.041-2.743-.123-3.09-.082-.346-.307-.608-.676-.786s-1.192-.267-2.469-.267h-.547v-2.581c2.671-.575 4.699-1.756 6.083-3.544h3.258z"></path></svg>
                            <p>填写支持金额</p>
                        </div>
                        <div class="proce"></div>
                        <div class="node node-two">
                            <svg class="icon-proce" height="60px" width="60px" viewBox="0 0 60 60">
                            <path class="proce-bg-color" fill="#bfc8ff" d="M57.521 23.991c3.305 3.305 3.305 8.714 0 12.019L36.01 57.521c-3.305 3.305-8.713 3.308-12.018.002L2.479 36.008c-3.305-3.306-3.305-8.712 0-12.017L23.99 2.479c3.306-3.305 8.713-3.305 12.019 0l21.512 21.512z"></path><path class="proce-num-color" fill="#3F4B94" d="M35.831 37.506v3.773H23.677l.004-3.158c3.6-5.888 5.739-9.531 6.418-10.931.68-1.399 1.02-2.49 1.02-3.274 0-.602-.104-1.05-.309-1.347-.205-.296-.518-.444-.938-.444s-.732.164-.938.492c-.206.328-.309.98-.309 1.955v2.105h-4.949v-.807c0-1.239.063-2.217.191-2.933.127-.715.441-1.419.943-2.112a4.727 4.727 0 0 1 1.955-1.572c.802-.355 1.764-.533 2.885-.533 2.196 0 3.857.545 4.983 1.634 1.126 1.089 1.688 2.468 1.688 4.136 0 1.267-.316 2.607-.95 4.02-.634 1.413-2.5 4.411-5.599 8.996h6.059z"></path></svg>
                            <p>确认投资信息</p>
                        </div>
                        <div class="proce"></div>
                        <div class="node node-three">
                            <svg class="icon-proce" height="60px" width="60px" viewBox="0 0 60 60">
                            <path class="proce-bg-color" fill="#bfc8ff" d="M57.521 23.991c3.305 3.305 3.305 8.714 0 12.019L36.01 57.521c-3.305 3.305-8.713 3.308-12.018.002L2.479 36.008c-3.305-3.306-3.305-8.712 0-12.017L23.99 2.479c3.306-3.305 8.713-3.305 12.019 0l21.512 21.512z"></path><path class="proce-num-color" fill="#3F4B94" d="M34.026 28.523c.838.283 1.463.746 1.873 1.389s.615 2.149.615 4.521c0 1.76-.201 3.125-.602 4.096-.401.972-1.094 1.711-2.078 2.217s-2.247.759-3.787.759c-1.75 0-3.124-.294-4.122-.882s-1.654-1.308-1.969-2.16c-.314-.852-.472-2.331-.472-4.437v-1.75h5.523v3.596c0 .957.057 1.565.171 1.825s.366.39.759.39c.428 0 .711-.164.848-.492s.205-1.185.205-2.57v-1.531c0-.848-.096-1.468-.287-1.859s-.475-.649-.848-.772c-.374-.123-1.099-.193-2.174-.212v-3.213c1.313 0 2.123-.05 2.434-.15.31-.1.533-.319.67-.656s.205-.866.205-1.586v-1.23c0-.774-.08-1.285-.239-1.531s-.408-.369-.745-.369c-.383 0-.645.13-.786.39s-.212.813-.212 1.661v1.818h-5.523v-1.887c0-2.114.482-3.543 1.449-4.286.966-.743 2.502-1.114 4.607-1.114 2.634 0 4.42.515 5.359 1.544.938 1.029 1.408 2.459 1.408 4.29 0 1.239-.169 2.133-.506 2.685-.336.548-.928 1.051-1.776 1.506z"></path></svg>
                            <p>在线支付</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <div class="pay-container ico-container">
            <div class="warm-tips">
                <p>
                    <span class="tips-title">温馨提示：</span>
                    <span class="tips-txt tips-txt-first">ICO成功之后，所有项目投资的资金可获得相应回报。投资过程中，如果ICO金额未达到最低目标金额，即该ICO失败，所有筹集的资金将会退回给投资者。</span>
                    <span class="tips-txt tips-txt-two hide">请认真核对以下所有投资信息！</span>
                    <span class="tips-txt tips-txt-three hide">一旦完成支付，ico365确认收到支付后，将会发邮件或手机短信通知您！</span>
                </p>
            </div>
            <div class="process-content clearfix">
                <!-- the part one of pay process -->
                <div class="pay-part pay-part-one">
                    <div class="part-one-top">
                        <div>
                            <div class="form-group">
                                <label for="curtype" class="form-title">选择可支持的币种：</label>
                                <span class="curtype_limit"><span class=" active" data-itemid="303" data-id="${appIcoProjectDTO.coinType}"><svg width="17" height="17" viewBox="0 0 17 17"><path class="radio-circle" fill="#424E98" d="M8.499 1c4.136 0 7.5 3.364 7.5 7.5s-3.364 7.5-7.5 7.5C4.364 16 1 12.636 1 8.5S4.364 1 8.499 1m0-1a8.5 8.5 0 1 0 .003 17 8.5 8.5 0 0 0-.003-17z"></path><circle class="radio-center" fill="#424E98" cx="8.5" cy="8.5" r="5.417"></circle></svg>${appIcoProjectDTO.coinType}</span></span>
                            </div>
                            <div class="pay-money-panel clearfix">
                                <div class="input-box">
                                	<input type="hidden" id="msg"/>
                                	<input type="hidden" id="sumMoney" value="${appIcoProjectDTO.sumMoney}"/>
                                    <input class="money-panel-input hide" type="text" onkeyup="value=value.replace(/[^\d.]/g,&#39;&#39;)" maxlength="9" value="<#if getMoney??>${getMoney}<#else>1</#if>" placeholder="" id="getMoney">
                                    <span class="money-panel-num"><#if getMoney??>${getMoney}<#else>1</#if></span>
                                    <span class="money-panel-type">${appIcoProjectDTO.coinType}</span>
                                    <span class="money-panel-cny-num" style="display: none;"></span>
                                    <input type="hidden" id="coinType" value="${appIcoProjectDTO.coinType}"/>
                                    <input type="hidden" id="projectId" value="${appIcoProjectDTO.id}"/>
                                </div>
                                <div class="scale-box">
                                    <p class="scale-num"><span id="bili">0.01</span>%</p>
                                    <p class="scale-tex">所占比例</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="part-one-body">
                        <div class="part-one-title"></div>
                        <div class="funds-plan-list">
                            <div class="funds-plan-item">
                                <div class="funds-item-header">
                                    <div class="funds-item-left"><input type="hidden" class="price" value="" ><span class="funds-checkbox checked"></span><span class="money-type">${appIcoProjectDTO.coinType}</span><span class="pan-exchange-val">/金额不限</span></div>
                                    <div class="funds-item-right"><a href="javascript:" class="funds-detail-toggle open"><span>收起详情</span><svg height="10" width="20" viewBox="0 0 19 9.477"><path class="arrow-down" fill-rule="evenodd" clip-rule="evenodd" fill="#272536" d="M1.92 0H0l9.522 9.477L19 0h-2.024L9.508 7.364 1.92 0z"></path></svg></a></div>
                                </div>
                            <div class="funds-item-body" style="display:block;">
                                <div class="funds-item-title"><strong>不限投资人次</strong><span>已参与1995人次</span></div>
                                <p>1个ETH=5000个LRC，从认购开启之日起，每三天为一个阶段，共10个阶段，每阶段都有相应的LRC奖励，奖励比例依次是20%， 16%， 14%， 12%， 10%， 8%， 6%，4%，2%，0%。
                                </p>
                                <div class="mt15">
                                    <p><span class="circle-point"></span><span class="title">回报时间：</span><span class="content">预计项目ICO成功结束后90天内</span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="part-one-footer">
                    <div class="funds-agreement">
                        <span class="ico-agree">
                                <svg height="18px" width="18px" viewBox="0 0 18 18">

                                <path class="check-color" fill-rule="evenodd" clip-rule="evenodd" fill="#24D96D" d="M3.389 8.418l3.273 2.609 8.151-7.51s.547-.5 1.024-.109c.143.118.307.45-.063.97l-8.511 9.97s-.653.893-1.427-.01L2.165 9.379s-.436-.671.109-1.075c.184-.135.602-.345 1.115.114m0 0"></path><path class="border-color" fill="#9B9B9B" d="M17 1v16H1V1h16m1-1H0v18h18V0z"></path></svg>
                            </span>
                        <span>我已阅读并同意<a href="" target="_blank">《支持者风险协议》</a></span>
                    </div>
                    <div class="funds-next-operate">
                        <button type="button" id="one_next_btn" data-step="two">下一步
                                <svg height="14px" width="18px" viewBox="0 0 9 14">

                                <path fill="currentColor" d="M0 14V0l9 7z"></path></svg>
                            </button>
                    </div>
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
 
 seajs.use(["js/main","js/front/ico/pay","js/ueditor.all.min"],function(m,p,login){
	 m.init();
	 p.one();
 });
</script>



