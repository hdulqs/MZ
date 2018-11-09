<#include "/base/base.ftl">
<div id="sidebar" class="sidebar col-md-2 col-sm-2" style="margin-top: 15px; padding: 0;">
    <div data-height="100%" style="background: #fff; padding-bottom: 30px;">

        <ul class="nav">
            <li class="has-sub "><i class="slideico slideico1"></i><a href="javascript:;" style=""><span
                    class="ng-binding"><@spring.message code="zhanghuzhongxin"/></span><i
                    class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
                <ul class="sub-menu " ng-if="item.name==&#39;Account&#39;">
                    <li class=" active"><i class="slideico slideico1"></i> <a src="${ctx}/user/index"
                                                                              href="javascript:void(0);"><@spring.message code="zhanghuzhongxin"/></a>
                    </li>
                </ul>
            </li>
            <li class="has-sub "><i class="slideico slideico2"></i><a href="javascript:;" style=""><span
                    class="ng-binding"><@spring.message code="jiaoyijilu"/></span><i
                    class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
                <ul class="sub-menu " style="display: block">
                    <li><i class="slideico slideico3"></i> <a src="${ctx}/v.do?u=front/user/entrust"
                                                              href="javascript:void(0);"><@spring.message code="wodeweituo"/></a>
                    </li>
                    <li><i class="slideico slideico5"></i> <a src="${ctx}/v.do?u=front/user/trades"
                                                              href="javascript:void(0);"><@spring.message code="jiaoyijilu"/></a>
                    </li>
                    <#if hasc2c="true">
                    <li><i class="slideico slideico5"></i> <a src="${ctx}/v.do?u=front/user/c2corder"  href="javascript:void(0);"><@spring.message code="c2corders"/></a></li>
                   </#if>
                  <#if hasotc="true">
                     <li><i class="slideico slideico5"></i> <a src="${ctx}/v.do?u=front/user/otcorder"  href="javascript:void(0);">OTC订单</a></li>
                 </#if>
					 <#if hasotc="true">
						    <li><i class="slideico slideico5"></i> <a src="${ctx}/v.do?u=front/user/otclist"  href="javascript:void(0);"><@spring.message code="OTCweituodingdan"/></a></li>
							<li><i class="slideico slideico5"></i> <a src="${ctx}/v.do?u=front/user/otcorder"  href="javascript:void(0);"><@spring.message code="OTCjiaoyidingdan"/></a></li>
					 </#if>
                </ul>
            </li>
            <li class="has-sub "><i class="slideico slideico3"></i><a href="javascript:;"><span
                    class="ng-binding"><@spring.message code="caichanzhongxin"/></span><i
                    class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
                <ul class="sub-menu " style="display: block">
                <#--<#if customer!="kk"><li><i class="slideico slideico5"></i> <a src="${ctx}/user/rmbdeposit/index"   href="javascript:void(0);" ><@spring.message code="woyaochongzhi"/></a></li>
                <li><i class="slideico slideico6"></i> <a src="${ctx}/user/rmbWithdraw/index" href="javascript:void(0);"><@spring.message code="woyaotixian"/></a></li></#if>-->
                    <li><i class="slideico slideico7"></i> <a src="${ctx}/user/btc/post"
                                                              href="javascript:void(0);"><@spring.message code="woyaochongbi"/></a>
                    </li>
                    <li><i class="slideico slideico8"></i> <a src="${ctx}/user/btc/get" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="woyaotibi"/></a>
                    </li>
						<#if customer!="kk">
                            <li><i class="slideico slideico9"></i> <a src="${ctx}/user/bankcard/index"
                                                                      href="javascript:void(0);"
                                                                      data-click="scroll-top"><@spring.message code="yinhangkaguanli"/></a>
                            </li></#if>
                    <li><i class="slideico slideico2"></i> <a src="${ctx}/user/publickeylist/index"
                                                              href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="bizhanghu"/></a>
                    </li>
                </ul>
            </li>
            <#if lend=="true">
                <li class="has-sub "><i class="slideico slideico2"></i><a href="javascript:;"
                                                                          style=""><@spring.message code="rongzirongbi"/></span>
                    <i class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
                    <ul class="sub-menu " style="display: block">
                    <#if lendMoney=="true">
                         <li><i class="slideico slideico3"></i> <a src="${ctx}/lendcoin/lendMoney"
                                                                   href="javascript:void(0);"><@spring.message code="rongzi"/></a>
                         </li>
                    </#if>
                     <#if lendTimes=="true">
                        <li><i class="slideico slideico5"></i> <a src="${ctx}/lendcoin/lendTimesApply"
                                                                  href="javascript:void(0);"><@spring.message code="gangganbeishushenqing"/></a>
                        </li>
                     </#if>
                     <#if lendCoin=="true">
                        <li><i class="slideico slideico5"></i> <a src="${ctx}/lendcoin/lendcoinMoney"
                                                                  href="javascript:void(0);"><@spring.message code="rongbi"/></a>
                        </li>
                     </#if>
                    </ul>
                </li>
            </#if>
            <li class="has-sub "><i class="slideico slideico4"></i><a href="javascript:;" style=""><span
                    class="ng-binding"><@spring.message code="anquanzhongxin"/></span><i
                    class="fa fa-caret-right pull-right" style="margin: 18px 5px 0 0;"></i> </a>
                <ul class="sub-menu " style="display: block">
                    <li><i class="slideico slideico10"></i> <a src="${ctx}/v.do?u=front/user/safe"
                                                               href="javascript:void(0);"
                                                               data-click="scroll-top"><@spring.message code="anquanshezhi"/></a>
                    </li>
				<#if states!=null&&states==0>
					<li><i class="slideico slideico2"></i> <a src="${ctx}/user/identitymav" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="shimingrenzheng"/></a>
                    </li>
                <#elseif states==2>
					<li><i class="slideico slideico2"></i> <a src="${ctx}/user/realinfo" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="shimingrenzheng"/></a>
                    </li>
                <#else>
					<li><i class="slideico slideico2"></i> <a src="${ctx}/user/realinfo" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="shimingrenzheng"/></a>
                    </li>
                </#if>

                    <li><i class="ab slideico slideico2"></i> <a src="${ctx}/v.do?u=front/user/mymessage"
                                                                 href="javascript:;"
                                                                 data-click="scroll-top"><@spring.message code="wodexiaoxi"/></a>
                    </li>

                    <!--	<#if phone!=null&&phoneState==1>
						<li><i class="slideico slideico2"></i> <a src="${ctx}/v.do?u=front/user/setaphone" href="javascript:void(0);"  data-click="scroll-top"><@spring.message code="shoujirenzheng"/></a></li>
				<#else>
						<li><i class="slideico slideico2"></i> <a src="${ctx}/v.do?u=front/user/setphone" href="javascript:void(0);"  data-click="scroll-top"><@spring.message code="shoujirenzheng"/></a></li>
				</#if>-->
				<#if customer!="kk">
                    <li><i class="slideico slideico2"></i> <a src="${ctx}/user/setphone" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="shoujirenzheng"/></a>
                    </li></#if>

                   <!-- <li><i class="slideico slideico2"></i> <a src="${ctx}/user/setgoogle" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="gugerenzheng"/></a>
                    </li>-->

                    <!--<#if googleKey!=null&&googleState==1>
						<li><i class="slideico slideico2"></i> <a src="${ctx}/v.do?u=front/user/setagoogle" href="javascript:void(0);"  data-click="scroll-top"><@spring.message code="gugerenzheng"/></a></li>
				<#else>
						<li><i class="slideico slideico2"></i> <a src="${ctx}/v.do?u=front/user/setgoogle" href="javascript:void(0);"  data-click="scroll-top"><@spring.message code="gugerenzheng"/></a></li>
				</#if>-->
                    <li><i class="slideico slideico2"></i> <a src="${ctx}/user/setcommend" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="tuijianfanyong"/></a>
                    <li>

                    <li><i class="slideico slideico2"></i> <a src="${ctx}/user/loginPass" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="denglumima"/></a>
                                                              <!-- 新增关闭登录验证 -->
                                                              <!-- <li><i class="slideico slideico2"></i> <a src="${ctx}/phone/offLoginPhone" href="javascript:void(0);"
                                                              data-click="scroll-top"><@spring.message code="guanbidengluyanzheng"/></a> --!>
                                          <!-- 新增登录验证 -->  
                    <#if phoneState=1>
                    	  <input type="hidden" id="title" value="<@spring.message code='xinxi'/>"/>
                    	  <input type="hidden" id="btn1" value="<@spring.message code='queding'/>"/>
                    	  <input type="hidden" id="btn2" value="<@spring.message code='quxiao'/>"/>
                          <li>
                          	<i class="slideico slideico2"></i>
                          	<#if "${user.checkStates}"== 0>
                          		<input type="hidden" id="openv" value="<@spring.message code='dengluyanzheng'/>"/>
                          		<a onclick="openValidy()" href="javascript:void(0);" data-click="scroll-top"><@spring.message code="kaiqidengluyanzheng"/></a>
                          	<#else> 
                          		<input type="hidden" id="closev" value="<@spring.message code='quxiaorenzheng'/>"/>
                          		<a onclick="closeValidy()" href="javascript:void(0);" data-click="scroll-top"><@spring.message code="guanbidengluyanzheng"/></a>
                            </#if>
                          </li>
					</#if>
                </ul>
            </li>
				
			<#if hasico=="true">
				<!-- ico菜单 -->
            </#if>
        </ul>
    </div>
</div>
<div class="sidebar-bg"></div>
<script>
	function openValidy(){
		var openv = $("#openv").val();
		var title = $("#title").val();
		var btn1 = $("#btn1").val();
		var btn2 = $("#btn2").val();
		layer.confirm(openv+'？', {
			  title: title,
			  btn: [btn1,btn2] //按钮
			}, function(){
				$.ajax({
					type : "post",
					url : _ctx + "/phone/setLoginPhone",
					data :null,
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								window.location.href = _ctx+"/user/center";
							})

						}else{
							layer.msg(data.msg);
							$("#verifyCode").val("");
						}
					},
					
				});
			});
	}
	
	function closeValidy(){
		var closev = $("#closev").val();
		var title = $("#title").val();
		var btn1 = $("#btn1").val();
		var btn2 = $("#btn2").val();
		layer.confirm(closev+"？", {
			  title: title, 
			  btn: [btn1,btn2] //按钮
			}, function(){
				$.ajax({
					type : "post",
					url : _ctx + "/phone/offLoginPhone",
					data :null,
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								window.location.href = _ctx+"/user/center";
							})

						}else{
							layer.msg(data.msg);
						}
					},
					
				});
			});
	}
</script>