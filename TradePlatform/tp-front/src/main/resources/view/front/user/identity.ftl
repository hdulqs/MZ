<#include "/base/base.ftl">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/${version}/lib/google/css/global.css">

    <div class="container-fluid person-con ng-scope">
    <div class="identity">
    	<div class="identity-title"><@spring.message code="shenfenrenzhen"/></div>
    	<div class="identity-body">
    	<div class="select">
    		<div class="filed f-cb">
    			<div class="label f-fl"><@spring.message code="xuanzeleixing"/></div>
    			<div class="f-fl">
    				<label class=""><input type="radio" name="type" value="1" checked="checked"   class=""><img src="${ctx}/static/${version}/img/idetify/map-cn.png"><@spring.message code="chinacountry"/></label>
    				<label class=""><input type="radio" onclick="show()" name="type" value="2"  class=""><img src="${ctx}/static/${version}/img/idetify/map-other.png"><@spring.message code="waiguo"/></label>
    			</div>
    		</div>
    	</div>
    	<p class="nomargin Validform_checktip"></p>
    	<!-- ngIf: identity.type==1 -->
      <form id="authenticationForm1" action="/user/identity" method="post" enctype="multipart/form-data" >
    		<input type="hidden" name="country" class="country1" value="中国大陆">
    		<input type="hidden" name="type" value="1">
    		<input type="hidden" name="cardType" value="0">
    			<div class="filed f-cb">
    				<div class="label f-fl"></div>
    				<div class="f-fl">
    					<div class="tip" style="margin-left:154px;"><p class="nopadding"><@spring.message code="qingquebaoninbenren"/></p></div>
    				</div>
    			</div>
    			<p class="nomargin Validform_checktip"></p>

    			<div class="filed f-cb">
    				<div class="label f-fl"><@spring.message code="xingshi"/></div>
    				<div class="f-fl">
    					<input type="text" id="surname1" name="surname" datatype="*">
    					<p class="nomargin Validform_checktip"></p>
    				</div>
    			</div>

    			<div class="filed f-cb">
    				<div class="label f-fl"><@spring.message code="mingzi"/></div>
    				<div class="f-fl">
    					<input type="text" id="trueName1" name="trueName" datatype="*">
    					<p class="nomargin Validform_checktip"></p>
    				</div>
    			</div>

    			<div class="filed f-cb">
    				<div class="label f-fl"><@spring.message code="youxiaoshenfenzheng"/></div>
    				<div class="f-fl">
    					<input type="text" id="cardId1" name="cardId" datatype="idcard" >
    					<p class="nomargin Validform_checktip"></p>
    				</div>
    			</div>
    			
    			<div class="filed filed-file f-cb">
    				<div class="label sp f-fl zh"><@spring.message code="benrenzhengmian"/></div>
    				<div class="f-fl" id="exampleInputUpload">
    				<div class="f-fl">
	    					<div class="uploadBox">
	    						<span class="btn btn-primary btn-file">
			                        <span class="fileinput-new"><@spring.message code="dianjishangchuan"/></span>
			                        <!--<span class="fileinput-exists">换一张</span>-->
			                        <input type="file" name="file" id="picIDa1" accept="image/gif,image/jpeg,image/x-png,image/bmp,image/jpg" style="opacity:0;" />
			                    </span>
    						<span class="fileName"></span>
    						<div class="tip"><p class=""><@spring.message code="quebaoneirong"/></p></div>
    						<div class="f-cb">
    							<div class="preView f-fl">
    								<canvas id="canvas1" style="display:none;"></canvas>
    								<img src="" id="picImg1">
    							</div>
    							<div class="f-fl li">
    								<span class=""><@spring.message code="shili"/><i class="iconfont icon-list"></i></span>
    								<img src="${ctx}/static/${version}/img/idetify/idcard-f.png">
    							</div>
    						</div>
    						<p class="nomargin Validform_checktip" id="frontError"></p>
    					</div>
    					</div>
    				</div>
    			</div>
    			<div class="filed filed-file f-cb">
    				<div class="label sp f-fl zh"><@spring.message code="benrenbeimian"/></div>
    				<div class="f-fl">
    					<div class="uploadBox">
    						<span class="btn btn-primary btn-file">
		                        <span class="fileinput-new"><@spring.message code="dianjishangchuan"/></span>
		                        <!--<span class="fileinput-exists">换一张</span>-->
		                        <input type="file" name="file" id="picIDb1" accept="image/gif,image/jpeg,image/x-png,image/bmp,image/jpg" style="opacity:0;" />
		                    </span>
    						<span class="fileName"></span>
    						<div class="tip"><p class=""><@spring.message code="quebaoneirong"/></p></div>
    						<div class="f-cb">
    							<div class="preView f-fl">
    								<canvas id="canvas2" style="display:none;"></canvas>
    								<img src="" id="picImg2">
    							</div>
    							<div class="f-fl li">
    								<span class=""><@spring.message code="shili"/><i class="iconfont icon-list"></i></span>
    								<img src="${ctx}/static/${version}/img/idetify/idcard-b.png">
    							</div>
    						</div>
    						<p class="nomargin Validform_checktip" id="backError"></p>
    					</div>
    				</div>
    			</div>
    			<div class="filed filed-file f-cb">
    				<div class="label sp f-fl"><@spring.message code="shouchibenren"/></div>
    				<div class="f-fl">
    					<div class="uploadBox">
    						<span class="btn btn-primary btn-file">
		                        <span class="fileinput-new"><@spring.message code="dianjishangchuan"/></span>
		                        <!--<span class="fileinput-exists">换一张</span>-->
		                        <input type="file" name="file" id="picIDc1" accept="image/gif,image/jpeg,image/x-png,image/bmp,image/jpg" style="opacity:0;" />
		                    </span>
    						<span class="fileName"></span>
    						<div class="tip">
    							<p class=""><@spring.message code="qingshangchuanyizhangshouchi"/></p>
    							<ul class="other-info">
    								<li class=""><i class="iconfont icon-gou"></i><@spring.message code="touxiang"/></li>
    								<li class=""><i class="iconfont icon-gou"></i><@spring.message code="haomaqingxi"/></li>
    								<li class=""><i class="iconfont icon-gou"></i><@spring.message code="baohan"/></li>
    								<li class=""><i class="iconfont icon-gou"></i><@spring.message code="baohandate"/></li>
    							</ul>
    						</div>
    						<div class="f-cb">
    							<div class="preView f-fl">
    								<canvas id="canvas3" style="display:none;"></canvas>
    								<img src="" id="picImg3">
    							</div>
    							<div class="f-fl li">
    								<span class=""><i class="iconfont icon-list"></i></span>
    								<img src="${ctx}/static/${version}/img/idetify/idcard-h.jpg">
    							</div>
    						</div>
    						<p class="nomargin Validform_checktip" id="handError"></p>
    					</div>
    				</div>
    			</div>
    			<div class="submit">
    				<input type="button" value='<@spring.message code="queding"/>' class="btn btn-orange" id="btn-submit">
    			</div>
    	</form>
      <!-- ngif:identity.type==2 -->
      <form id="authenticationForm2" action="/user/identity" method="post" enctype="multipart/form-data" >
          <input type="hidden" name="type" value="2">
          <input type="hidden" name="cardType" value="1">
        <!--  <input type="hidden" name="country2" class="country2" value="香港"> -->
          <input type="hidden" name="country2" class="country2" value="台湾">
          
          <div class="filed f-cb">
              <div class="label f-fl"></div>
              <div class="f-fl">
                  <div class="tip" style="margin-left:154px;">
                      <p class="nopadding"><@spring.message code="quebaoyinwen"/></p>
                  </div>
              </div>
          </div>
          <p class="nomargin Validform_checktip"></p>
          <div class="filed f-cb">
              <div class="label f-fl"><@spring.message code="mingzi"/></div>
              <div class="f-fl">
                  <input type="text" id="trueName2" name="trueName">
                  <p class="nomargin Validform_checktip"></p>
              </div>
          </div>
          <div class="filed f-cb">
              <div class="label f-fl"><@spring.message code="xingshi"/></div>
              <div class="f-fl">
                  <input type="text" id="surname2" name="surname">
                  <p class="nomargin Validform_checktip"></p>
              </div>
          </div>


          <div class="filed f-cb">
              <div class="label f-fl"><@spring.message code="xingbie"/></div>
              <div class="f-fl">
                  <input id="male" type="radio" name="sex" value="男"><label class="sex" for="male"><@spring.message code="nan"/></label>
                  <input id="female" type="radio" name="sex" value="女"><label class="sex" for="female"><@spring.message code="nv"/></label>
                  <p class="nomargin Validform_checktip"></p>
              </div>
          </div>
          <div class="filed f-cb">
              <div class="label sp f-fl zh"><@spring.message code="guojiadiqu"/></div>
              <div class="f-fl">
                        <div class="filed" style="position: relative;">
                            <input style="height: 34px;opacity:0;" id="mobile-number"  name="country" type="text"  class="">
                            <p style="margin-left: 163px" class="Validform_checktip Validform_wrong"></p>
                        </div>
              </div>
          </div>

          <div class="filed f-cb">
              <div class="label f-fl ng-binding"><@spring.message code="huzhao"/></div>
              <div class="f-fl">
                  <input type="text" id="cardId2" name="cardId" datatype="passport" nullmsg="该字段不能为空" errormsg="护照ID错误">
                  <p class="nomargin Validform_checktip"></p>
              </div>
          </div>
          <div class="filed filed-file f-cb">
              <div class="label f-fl ng-binding"><@spring.message code="huzhaofengmian"/></div>
              <div class="f-fl">
                  <div class="uploadBox">
                      <span class="btn btn-primary btn-file">
	                      <span class="fileinput-new"><@spring.message code="dianjishangchuan"/></span>
	                        	<!--<span class="fileinput-exists">换一张</span>-->
	                        	<input type="file" name="file" id="picIDa2" accept="image/gif,image/jpeg,image/x-png,image/bmp,image/jpg" style="opacity:0;" />
		                  </span>
                      <span class="fileName"></span>
                      <div class="tip">
                          <p class=""><@spring.message code="quebaoneirong"/></p>
                      </div>
                      <div class="f-cb">
                          <div class="preView f-fl">
                              <canvas id="canvas1" style="display:none;"></canvas>
                              <img src="" id="picImg4">
                          </div>
                          <div class="f-fl li">
                              <span class="ng-binding"><@spring.message code="shili"/><i class="iconfont icon-list"></i></span>
                              <img src="${ctx}/static/${version}/img/idetify/passport-f.png">
                          </div>
                      </div>
                      <p class="nomargin Validform_checktip" id="frontError"></p>
                  </div>
              </div>
          </div>
          <div class="filed filed-file f-cb">
              <div class="label sp f-fl zh"><@spring.message code="huzhaogerenxinxi"/></div>
              <div class="f-fl">
                  <div class="uploadBox">
	                      <span class="btn btn-primary btn-file">
	                          <span class="fileinput-new"><@spring.message code="dianjishangchuan"/></span>
	                          <!--<span class="fileinput-exists">换一张</span>-->
	                          <input type="file" name="file" id="picIDb2" accept="image/gif,image/jpeg,image/x-png,image/bmp,image/jpg" style="opacity:0;"/>
	                      </span>
                      <span class="fileName"></span>
                      <div class="tip">
                          <p class=""><@spring.message code="huzhaoyouxiaoqi"/></p>
                      </div>
                      <div class="f-cb">
                          <div class="preView f-fl">
                              <canvas id="canvas2" style="display:none;"></canvas>
                              <img src="" id="picImg5">
                          </div>
                          <div class="f-fl li">
                              <span class=""><@spring.message code="shili"/><i class="iconfont icon-list"></i></span>
                              <img src="${ctx}/static/${version}/img/idetify/passport-b.png">
                          </div>
                      </div>
                      <p class="nomargin Validform_checktip" id="backError"></p>
                  </div>
              </div>
          </div>
          <div class="filed filed-file f-cb">
              <div class="label sp f-fl"><@spring.message code="shouchibenren"/></div>
              <div class="f-fl">
                  <div class="uploadBox">
	                      <span class="btn btn-primary btn-file">
	                          <span class="fileinput-new"><@spring.message code="dianjishangchuan"/></span>
	                          <!--<span class="fileinput-exists">换一张</span>-->
	                          <input type="file" name="file" id="picIDc2" accept="image/gif,image/jpeg,image/x-png" style="opacity:0;"/>
	                      </span>
                      <span class="fileName"></span>
                      <div class="tip">
                          <p class=""><@spring.message code="qingshangchuanyizhangshouchi"/></p>
                          <ul class="other-info">
                              <li class=""><i class="iconfont icon-gou"></i><@spring.message code="touxiang"/></li>
                              <li class=""><i class="iconfont icon-gou"></i><@spring.message code="huzhaoqingxi"/></li>
                              <li class=""><i class="iconfont icon-gou"></i><@spring.message code="baohan"/></li>
                              <li class=""><i class="iconfont icon-gou"></i><@spring.message code="baohandate"/></li>
                          </ul>
                      </div>
                      <div class="f-cb">
                          <div class="preView f-fl">
                              <canvas id="canvas3" style="display:none;"></canvas>
                              <img src="" id="picImg6">
                          </div>
                          <div class="f-fl li">
                              <span class=""><@spring.message code="shili"/><i class="iconfont icon-list"></i></span>
                              <img src="${ctx}/static/${version}/img/idetify/passport-h.jpg">
                          </div>
                      </div>
                      <p class="nomargin Validform_checktip" id="handError"></p>
                  </div>
              </div>
          </div>
          <div class="submit">
              <input type="button" value="<@spring.message code="queding"/>" class="btn btn-orange" id="btn-submit2">
          </div>

      </form>
    	</div>
    	</div>
    </div>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/jqueryNewForm/jquery.form.js"></script>
  <script>
  seajs.use(["js/front/user/identity2","js/i18n_base"],function(o){
    o.init()
 });
  

  </script>

