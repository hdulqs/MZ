<form role="form">
  	<div class="form-group">
	  <label for="investCoinCode" class="col-sm-2 control-label">投资币种</label>
	  <div class="col-sm-10">
	   	  <input type="text" name="investCoinCode" value="${projectRepay.investCoinCode!}" class="form-control" id="investCoinCode" placeholder="投资币种">
	  </div>
	</div>
	<div class="form-group">
	  <label for="money" class="col-sm-2 control-label">投资金额</label>
	  <div class="col-sm-10">
	  	<input <#if (((projectRepay.isLimitedMoney)!'') == '0')>checked</#if> 	type="radio" name="isLimitedMoney" id="isLimitedMoney0" value="0">不限
	  	<input <#if (((projectRepay.isLimitedMoney)!'') == '1')>checked</#if>  	type="radio" name="isLimitedMoney" id="isLimitedMoney1" value="1">限制
	  	<input type="text" name="money" value="${projectRepay.money!}" class="form-control" id="money" placeholder="投资币量">
	  </div>
	</div>
	<div class="form-group">
	  <label for="repayExplain" class="col-sm-2 control-label">回报说明</label>
	  <div class="col-sm-10">
	  	<textarea name="repayExplain" class="form-control" rows="3">${projectRepay.repayExplain}</textarea>
	  </div>
	</div>
	<div class="form-group">
	  <label for="size" class="col-sm-2 control-label">投资名额</label>
	  <div class="col-sm-10">
	  	<input <#if (((projectRepay.isLimitedSize)!'') == '0')>checked</#if> 	type="radio" name="isLimitedSize" id="isLimitedSize0" value="0">不限
	  	<input <#if (((projectRepay.isLimitedSize)!'') == '1')>checked</#if>  	type="radio" name="isLimitedSize" id="isLimitedSize1" value="1">限制
	  	<input type="text" name="size" value="${projectRepay.size!}" class="form-control" id="size" placeholder="投资名额">
	  </div>
	</div>
	<div class="form-group">
	  <label for="support" class="col-sm-2 control-label">支持限制</label>
	  <div class="col-sm-10">
	  	<input <#if (((projectRepay.isLimitedSupport)!'') == '0')>checked</#if> 	type="radio" name="isLimitedSupport" id="isLimitedSupport0" value="0">不限
	  	<input <#if (((projectRepay.isLimitedSupport)!'') == '1')>checked</#if>  	type="radio" name="isLimitedSupport" id="isLimitedSupport1" value="1">限制
	  	<input type="text" name="support" value="${projectRepay.support!}" class="form-control" id="size" placeholder="投资币量">
	  </div>
	</div>
	<div class="form-group">
	  <label for="isFreeShipping" class="col-sm-2 control-label">是否包邮</label>
	  <div class="col-sm-10">
	  	<input <#if (((projectRepay.isFreeShipping)!'') == '0')>checked</#if> 	type="radio" name="isFreeShipping" id="isFreeShipping0" value="0">不限
	  	<input <#if (((projectRepay.isFreeShipping)!'') == '1')>checked</#if>  	type="radio" name="isFreeShipping" id="isFreeShipping1" value="1">限制
	  	<hr>
	  </div>
	</div>
	<div class="form-group">
	  <label for="paybackTime" class="col-sm-2 control-label">回报时间</label>
	  <div class="col-sm-10">
	  预计项目ICO成功结束后<input type="text" name='paybackTime' value='${projectRepay.paybackTime}' class="form-control" id="paybackTime" placeholder="请输入回报时间">天内
	  </div>
	</div>
</form>