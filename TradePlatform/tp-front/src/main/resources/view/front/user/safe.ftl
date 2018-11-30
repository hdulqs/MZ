<#include "/base/base.ftl">
 <div class="container-fluid person-con min-hg">

	<!-- begin page-header -->
	<div class="row" style="margin-bottom:15px;">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" id="RMBtab">
					<li role="presentation" class="active pull-left">
						<a href=""><@spring.message code="anquanshezhi"/></a>
					</li>

				</ul>
			</div>
		</div>
	</div>
	<!-- end page-header -->

	<!-- begin row -->
	<ul class="list-group safety-alist">

        <!--实名认证start-->
		<li class="list-group-item">
			<div class="media">
				<a href="#" class="pull-left">
							<#if user.isReal==0>
							<i  class="safety-ico safety-ico2"></i>
							<#else>
							<i  class="safety-ico safety-ico2"></i>
							</#if>
						</a>
						<div class="media-body" >
							<h4 class="media-heading"><@spring.message code="shimingrenzheng"/>：
								<#if states=0>
								<span  class="color-o">(<@spring.message code="weirenzheng"/>)</span>
								<#elseif states=1>
								<span  class="color-g">(<@spring.message code="daishenhe"/>)</span>
								<#elseif states=2>
								<span  class="color-g">(<@spring.message code="chakanrenzheng"/>)</span>
								<#else>
								<span  class="color-g">(<@spring.message code="yijuejue"/>)</span>
								</#if>
							</h4>
								<@spring.message code="realname_tishi"/>
						</div>

								<#if states=0>
									<a  href="javascript:void(0);" onclick="loadUrl('${ctx}/user/identitymav')" class="color-o editlink"><@spring.message code="lijirenzheng"/></a>
								<#else>
									<a  href="javascript:void(0);" onclick="loadUrl('${ctx}/user/realinfo')" class="color-g editlink"><@spring.message code="chakanrenzheng"/></a>
								</#if>
				</div>
        </li>
        <!--实名认证end-->

        <!--绑定手机start-->
		<#if customer!="kk">
				<li class="list-group-item">
					<div class="media">
						<a href="#" class="pull-left">
							<i  class="safety-ico safety-ico6"></i>
						</a>
						<div class="media-body" >
							<h4 class="media-heading"><@spring.message code="bangdingshouji"/>：
								<#if phoneState=0>
									<span class="color-o" >（<@spring.message code="weishezhi"/>）</span>
								<#else>
									<span class="color-g" >（<@spring.message code="yibangding"/>）${phone}</span>
								</#if>
						</div>

								<#if phoneState==0>
								<a href="javascript:volid(0)" onclick="loadUrl('${ctx}/v.do?u=front/user/setphone')" class="color-o editlink"><@spring.message code="lijishezhi"/></a>
								<#else>
								<a href="javascript:volid(0)" onclick="loadUrl('${ctx}/v.do?u=front/user/setphoneone')" class="color-g editlink"><@spring.message code="genghuanshouji"/></a>
		          			    <a href="javascript:volid(0)" style="margin-top: 42px;" onclick="loadUrl('${ctx}/v.do?u=front/user/setaphone')" class="color-g editlink"><@spring.message code="jiechubangding"/></a>

								</#if>
					</div>
				</li>
        </#if>
        <!--绑定手机end-->

        <!--登录密码start-->
            <li class="list-group-item">
                <div class="media">
							<a href="#" class="pull-left">
							<i class="safety-ico safety-ico3"></i>
						</a>
						<div class="media-body" >
							<h4 class="media-heading"><@spring.message code="denglumima"/>：<span class="color-g">（<@spring.message code="yishezhi"/>）*********</span></h4>
							<@spring.message code="safe_tishi"/>
						</div>
							<a href="#" onclick="loadUrl('${ctx}/v.do?u=front/user/setpw')" class="color-g editlink"><@spring.message code="chongxinshezhi"/></a>
		 	    </div>
			</li>
        <!--登录密码end-->

		<!--交易密码start-->
            <li class="list-group-item">
                <div class="media">
                    <a href="#" class="pull-left">
                        <i class="safety-ico safety-ico3"></i>
                    </a>
                    <div class="media-body" >

                        <h4 class="media-heading"><@spring.message code="jiaoyimima"/>：
							<#if user.accountPassWord!=null>
								<span class="color-g">（<@spring.message code="yishezhi"/>）*********</span>
							<#elseif user.accountPassWord==null>
								<span class="color-o">（<@spring.message code="weishezhi"/>）</span>
							</#if>
						</h4>

                        <@spring.message code="safe_tishi2"/>
                    </div>
					<#if user.accountPassWord!=null>
							<a href="#" onclick="loadUrl('${ctx}/v.do?u=front/user/resetapw')" class="color-g editlink"><@spring.message code="chongxinshezhi"/></a>
						<#elseif user.accountPassWord==null>
							<a href="#" onclick="loadUrl('${ctx}/v.do?u=front/user/setapw')" class="color-o editlink"><@spring.message code="lijishezhi"/></a>
					</#if>

                </div>
            </li>
		<!--交易密码end-->

		<!--邮箱绑定start-->
		<#--<li class="list-group-item">
			<div class="media">
				<a href="#" class="pull-left">
					<#if user.mail!=null>
					<i  class="safety-ico safety-ico4" style="background-position: -5px -416px;"></i>
					<#else>
					<i  class="safety-ico safety-ico4" style="background-position: -5px -416px;"></i>
				</#if>
				</a>
				<div class="media-body" >
					<h4 class="media-heading"><@spring.message code="bandmail"/>：
						<#if user.mailStates=0>
						<span class="color-o" >（<@spring.message code="weishezhi"/>）</span>
						<#else>
						<span class="color-g" >（<@spring.message code="yibangding"/>）${user.mail}</span>
					</#if>
					</h4>
					<@spring.message code="safe_tishi2"/>
				</div>


				<#if user.mail!=null&&user.mailStates==1>
				<a href="#" onclick="loadUrl('${ctx}/user/setmail')" class="color-g editlink " data-click="scroll-top"><@spring.message code="jiechubangding"/></a>
				<#elseif user.mail!=null&&user.mailStates==1>
				<a href="#" onclick="loadUrl('${ctx}/user/setmail')" class="color-g editlink"><@spring.message code="chongxinshezhi"/></a>
				<#else>
				<a href="#" onclick="loadUrl('${ctx}/user/setmail')" class="color-o editlink"><@spring.message code="lijishezhi"/></a>
			</#if>
			</div>
		</li>-->
		<!--邮箱绑定end-->

			<!--谷歌认证start-->
			<!--<li class="list-group-item">
				<div class="media">
					<a href="#" class="pull-left">
								<#if user.googleKey!=null>
								<i  class="safety-ico safety-ico4"></i>
								<#else>
								<i  class="safety-ico safety-ico4"></i>
								</#if>
							</a>
							<div class="media-body" >
								<h4 class="media-heading"><@spring.message code="gugerenzheng"/>：
								<#if googleKey!=null&&googleState=1>
								<span class=""></span>
								<span class="color-g">（<@spring.message code="yishezhi"/>）</span>
								<#else>
								<span class="color-o">（<@spring.message code="weishezhi"/>）</span>
								</#if>
								</h4>
								<@spring.message code="safe_tishi2"/>
							</div>


								<#if googleKey!=null&&googleState==1>
							    <a href="#" onclick="loadUrl('${ctx}/user/setgoogle')" class="color-g editlink"><@spring.message code="chongxinshezhi"/></a>
								<#else>
								<a href="#" onclick="loadUrl('${ctx}/user/setgoogle')" class="color-o editlink"><@spring.message code="lijishezhi"/></a>
								</#if>
					   </div>


					</li>-->
			<!--谷歌认证end-->

			</ul>
	<!-- end row -->
	</div>

 
