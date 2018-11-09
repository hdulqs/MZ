<#import "/base/spring.ftl" as spring/>
<style>
	i.form-control-feedback {
		right: 10px;
	}
</style>

<div class="container-fluid person-con">


	<!-- begin page-header -->
	<div class="row" style="margin-bottom:15px;">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" id="RMBtab">
					<li role="presentation" class="active pull-left">
						<a href=""><@spring.message code="tianjiayinhangka"/></a>
					</li>

				</ul>
			</div>
		</div>
	</div>
	<!-- end page-header -->

	<!---start:添加{{' A93 '| translate}}{{' A98 '| translate}}表{{' A84 '| translate}}---->
	<form class="form-horizontal p-t-30 bank-manage-add form-all" name="cardForm" id="cardForm" method="post" enctype="multipart/form-data"  >
		<span><input type="hidden" required class="form-control" name="bankId" id="bankId" ></span>
		<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="xuanzeyinhang"/>：</label>
			<div class="col-sm-5">
				<select required class="form-control" id="bankselect" name="cardBank" >
					<option value=""><@spring.message code="qingxuanze"/></option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="yinhangkasuozaidi"/>：</label>
			<input type="hidden" id="provinceValue"   />
			<input type="hidden" name="bankProvince" id="bankProvince" />
			<div class="col-sm-5">
				<select required class="form-control" id="province" style="width:45%; float:left;" >
					<option value=""><@spring.message code="qingxuanze"/></option>
				</select>
				<select required class="form-control" id="city" name="bankAddress" style="width:45%; float:right;">
					<option value=""><@spring.message code="qingxuanze"/></option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="kaihuzhihang"/>：</label>
			<div class="col-sm-5">
				<input type="text" ng-model="formData.subBank" name="subBank" id="subBank" required class="form-control" placeholder="">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label"><@spring.message code="chikarenxingshi"/>：</label>
			<div class="col-sm-5">
				<input type="text" required class="form-control" name="surName" id="surName" value="${user.surname}" <#if ischeckName!=0>readonly</#if>>
		</div>
</div>


<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="chikarenmingzi"/>：</label>
	<div class="col-sm-5">
		<input type="text" required class="form-control" name="trueName" id="trueName" value="${user.truename}" <#if ischeckName!=0>readonly</#if>>
</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="yinhangkazhanghao"/>：</label>
	<div class="col-sm-5">
		<input type="text" required class="form-control" name="cardNumber" id="cardNumber">
	</div>
</div>
<!--分割线 --->
<div class="row" style="margin-bottom:15px;">
	<div class="panel_wrap_head wrap_head">
	</div>
</div>
<!--支付宝图片上传--->
<style>
	.form-group .shangchuan{
		background:  #eee;
		height: 100px;
		width: 100px;
		text-align:  center;
	}

	.form-group .shangchuan img{
		height: 100px;
		width: 100px;
	}

	.form-group .shangchuan div{
		font-size: 12px;
		color: #666;
	}
</style>
<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="shangchuanzhifubaoshoukuanma"/>：</label>
	<div class="col-sm-5">
		<div class="shangchuan"  id="shangchuan">
			<img src="/static/src/img/xiaosan.png" id='myImg'/>
			<!--<div id='zhicode'>上传收款码</div>-->
		</div>
		<input style="display:none;" type="file" required  class="form-control" name="alipayPicture" id="alipayPicture" value="http://oqwos88hj.bkt.clouddn.com/smileFront57564cee-2b6d-4ce0-8b6c-2f18570be002.jps"/>
		<input type="hidden" id="alipayPicture_1">
	</div>
</div>

<!--支付宝账号上传 --->
<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="zhifubaozhanghao"/>：</label>
	<div class="col-sm-5">
		<input type="text" required class="form-control" name="alipay" id="alipay">
		<button type="button"   id="deteteAlipay"><@spring.message code="quxiaozhifubaozhifu"/></button>
	</div>
</div>
<!--分割线 --->
<div class="row" style="margin-bottom:15px;">
	<div class="panel_wrap_head wrap_head">
	</div>
</div>
<!--微信图片上传 --->
<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="shangchuanweixinshoukuanma"/>：</label>
	<div class="col-sm-5">
		<div class="shangchuan"  id="shangchuan2">
			<img src="/static/src/img/xiaosan.png" id='myImg2'/>
			<!--<div id='zhicode2'>上传收款码</div>-->
		</div>
		<input style="display:none;" type="file" required  class="form-control" name="weChatPicture" id="weChatPicture"  value="http://oqwos88hj.bkt.clouddn.com/smileFront57564cee-2b6d-4ce0-8b6c-2f18570be002.jps"  />
		<input type="hidden" id="weChatPicture_1">
	</div>
</div>
<!--微信账号上传 --->
<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="weixinzhanghao"/>：</label>
	<div class="col-sm-5">
		<input type="text" required class="form-control" name="wechat" id="wechat">
		<button type="button"   id="deteteWechat"><@spring.message code="quxiaoweixinzhifu"/></button>
	</div>
</div>
<!--分割线 --->
<div class="row" style="margin-bottom:15px;">
	<div class="panel_wrap_head wrap_head">
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label"><@spring.message code="duanxinma"/></label>
	<#-- <input type="hidden" value="${user.mobile}" class="mobile" />-->
	<div class="col-sm-5">
		<input id="verifyCode" type="text" required class="form-control" name="verifyCode"  style="width:156px" >
		<span style="margin-top: -10px;border: 1px solid #ccc;margin-left: 10px;background-color: #eee;" id="sendBtn" class="btn btn-yellow"><@spring.message code="fasongyanzhengma"/></span>
		<#--  <span id="sendBtn" class="btn btn-grey">发送验证码</span>-->
		<p style="margin-left: 163px" class="Validform_checktip"></p><!--填写提示语句的地儿，校验我没加，如果这个用不到，可以删掉-->
	</div>
</div>


<div class="form-group">
	<div class="col-sm-5 col-sm-offset-3">
		<button type="button"  class="btn btn-primary form-control" id="addBankcard"><@spring.message code="baocun"/></button>
	</div>
</div>
</form>
<!---end:添加{{' A93 '| translate}}{{' A98 '| translate}}表{{' A84 '| translate}}---->
</div>
<!--end page-container --->

<!-- begin page-container -->
<#--<div class="container-fluid person-con min-hg">
	<!-- begin page-header &ndash;&gt;
    <div class="row" style="margin-bottom:15px;">
        <div class="panel_wrap_head wrap_head">
            <div class="">
                <ul class="wrap_tabs" role="tablist" id="RMBtab">
                    <li role="presentation" class="active pull-left">
                        <a href="javascript:void(0);"><@spring.message code="wodeyinhangka"/></a>
                    </li>
                </ul>
            </div>
        <!--	<small class="fa fa-download pull-right text-info m-t-10 f-s-16"></small>&ndash;&gt;
        </div>
    </div>-->
	<!-- end page-header -->

	<!-- begin {{' A70 '| translate}}{{' A93 '| translate}}{{' A98 '| translate}} -->

	<#--<div class="row p-t-20" id="div_list">
	<#list list as list>
	<div class="col-sm-6" ng-repeat="item in list">
		<dl class="mybankcard">
			<dt class="bank-name p-l-20 p-r-20 clearfix">
				<span class="pull-left">${list.cardBank}</span>
				<!-- <a href=":;" ng-click="remove($event)"  bcid={{item.id}}><i  class="fa fa-trash pull-right"></i></a> &ndash;&gt;
            <span id="remove"><input type="hidden" id="bandId" value="${list.id}" /><i class="fa fa-trash pull-right"></i><span>
            </dt>
            <dd>${list.cardNumber}</dd>
            <dd><@spring.message code="xingshi"/>：${list.surName}</dd>
            <dd><@spring.message code="huming"/>：${list.cardName}</dd>
            <dd><@spring.message code="chengshi"/>：${list.bankProvince} ${list.bankAddress}</dd>
            <dd><@spring.message code="zhihang"/>：${list.subBank}</dd>
            <!--<dd><@spring.message code="yinhangjigoudaima"/>：${list.subBankNum}</dd>&ndash;&gt;
            <dt class="bank-name p-l-20 p-r-20 clearfix">
            <dd>支付宝账号：${list.alipay}</dd>
            <dd>支付宝收款码：<img src="/${list.alipayPicture}" width="100%" height="100%" /></dd>

            </dt>
            <dt class="bank-name p-l-20 p-r-20 clearfix">
            <dd>微信号：${list.weChat}</dd>
            <dd>微信收款码：<img src="/${list.weChatPicture}" width="100%" height="100%"/></dd>

            </dt>
        </dl>
    </div>
    </#list>
</div>-->

				<!-- end {{' A70 '| translate}}{{' A93 '| translate}}{{' A98 '| translate}} -->
	</div>
	<#include "/base/base.ftl">
	<script type="text/javascript">
        seajs.use(["js/front/user/bankcard"],function(bc){
            bc.init();
        });
	</script>

