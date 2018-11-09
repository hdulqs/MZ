<form role="form">
  	<div class="form-group">
	  <label for="projectName" class="col-sm-2 control-label">项目名称</label>
	  <div class="col-sm-10">
	   <input type="text" name="projectName" value="${project.projectName!}" class="form-control" id="projectName" placeholder="请输入您的详细地址">
	  </div>
	</div>
	<div class="form-group">
	  <label for="projectStage" class="col-sm-2 control-label">所属阶段</label>
	  <div class="col-sm-10">
	  	 <select name="projectStage" value="" class="form-control">
	  	  <option <#if (((project.projectStage)!'') == '')>selected="selected"</#if> value=''>请选择数据</option>
		  <option <#if (((project.projectStage)!'') == '0')>selected="selected"</#if> value='0'>尚未启动</option>
		  <option <#if (((project.projectStage)!'') == '1')>selected="selected"</#if> value='1'>产品开发中</option>
		  <option <#if (((project.projectStage)!'') == '2')>selected="selected"</#if> value='2'>产品已上市</option>
		  <option <#if (((project.projectStage)!'') == '3')>selected="selected"</#if> value='3'>已盈利</option>
		</select>
	  </div>
	</div>
	<div class="form-group">
	  <label for="trueName" class="col-sm-2 control-label">真实姓名</label>
	  <div class="col-sm-10">
	  	<input type="text" name="trueName" value="${project.trueName!}" class="form-control" id="trueName" placeholder="请输入真实姓名">
	  </div>
	</div>
	<div class="form-group">
	  <label for="workAddress" class="col-sm-2 control-label">办公地址</label>
	  <div class="col-sm-10">
	  	<input type="text" name="workAddress" value="${project.workAddress!}" class="form-control" id="workAddress" placeholder="请输入您的详细地址">
	  </div>
	</div>
	<div class="form-group ">
	  <label for="teamSize" class="col-sm-2 control-label">团队人数</label>
	  <div class="col-sm-10">
	  	<input type="text" name="teamSize" value="${project.teamSize}" class="form-control" id="teamSize" placeholder="请输入您的详细地址">
	  </div>
	</div>
	<div class="form-group">
	  <label for="financingStage" class="col-sm-2 control-label">融资阶段</label>
	  <div class="col-sm-10">
	  	<select name="financingStage" class="form-control">
	  	  <option <#if (((project.financingStage)!'') == '')>selected="selected"</#if> value=''>请选择数据</option>
		  <option <#if (((project.financingStage)!'') == '5')>selected="selected"</#if> value='5'>天使轮</option>
		  <option <#if (((project.financingStage)!'') == '4')>selected="selected"</#if> value='4'>A轮</option>
		  <option <#if (((project.financingStage)!'') == '3')>selected="selected"</#if> value='3'>B轮</option>
		  <option <#if (((project.financingStage)!'') == '2')>selected="selected"</#if> value='2'>C轮</option>
		  <option <#if (((project.financingStage)!'') == '1')>selected="selected"</#if> value='1'>D轮</option>
		  <option <#if (((project.financingStage)!'') == '0')>selected="selected"</#if> value='0'>未融资</option>
		</select>
	  </div>
	</div>
	<div class="form-group ">
	  <label for="info" class="col-sm-2 control-label">项目简介</label>
	  <div class="col-sm-10">
	  	<textarea name="info" class="form-control" rows="3">${project.info}</textarea>
	  </div>
	</div>
</form>