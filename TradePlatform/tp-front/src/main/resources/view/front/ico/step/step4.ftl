<form role="form">
	<div class="form-group">
	  <label for="icoDays" class="col-sm-2 control-label">ICO天数</label>
	  <div class="col-sm-10">
	   <input type="text" name="icoDays" value="${project.icoDays}" class="form-control" id="icoDays" placeholder="请输入天数">
	  </div>
	</div>
	<div class="form-group">
	  <label for="coinType" class="col-sm-2 control-label">币种</label>
	  <div class="col-sm-10">
	   <input type="text" name="coinType" value="${project.coinType}" class="form-control" id="coinType" placeholder="币种">
	  </div>
	</div>
  	<div class="form-group">
	  <label for="sumMoney" class="col-sm-2 control-label">目标金额</label>
	  <div class="col-sm-10">
	   <input type="text" name="sumMoney" value="${project.sumMoney}" class="form-control" id="sumMoney" placeholder="请输入目标金额">
	  </div>
	</div>
	<div class="form-group">
	  <label for="linkman" class="col-sm-2 control-label">联系人</label>
	  <div class="col-sm-10">
	   <input type="text" name="linkman" value="${project.linkman}" class="form-control" id="linkman" placeholder="请输入联系人">
	  </div>
	</div>
	<div class="form-group">
	  <label for="position" class="col-sm-2 control-label">负责职位</label>
	  <div class="col-sm-10">
	   <input type="text" name="position" value="${project.position}" class="form-control" id="position" placeholder="请输入职位">
	  </div>
	</div>
	<div class="form-group">
	  <label for="phone" class="col-sm-2 control-label">联系电话</label>
	  <div class="col-sm-10">
	   <input type="text" name="phone" value="${project.phone}" class="form-control" id="phone" placeholder="请输入联系电话">
	  </div>
	</div>
	<div class="form-group">
	  <label for="startDays" class="col-sm-2 control-label">开始时间</label>
	  <div class="col-sm-10">
	 	 申请<input type="startDays" name="startDays" value='${project.startDays}' class="form-control" id="startDays" placeholder="申请多少天内开始">天内开始
	  </div>
	</div>
</form>