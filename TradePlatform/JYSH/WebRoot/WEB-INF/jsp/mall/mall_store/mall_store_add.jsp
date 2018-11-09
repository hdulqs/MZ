<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<title></title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/font-awesome.min.css" />
		<!-- 下拉框 -->
		<link rel="stylesheet" href="static/css/chosen.css" />
		
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	
	
	//保存
	function save(){
			if($("#ADDTIME").val()==""){
			$("#ADDTIME").tips({
				side:3,
	            msg:'请输入创建时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ADDTIME").focus();
			return false;
		}
		if($("#STORE_ADDRESS").val()==""){
			$("#STORE_ADDRESS").tips({
				side:3,
	            msg:'请输入店铺地址',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_ADDRESS").focus();
			return false;
		}
		if($("#STORE_INFO").val()==""){
			$("#STORE_INFO").tips({
				side:3,
	            msg:'请输入店铺描述',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_INFO").focus();
			return false;
		}
		if($("#STORE_NAME").val()==""){
			$("#STORE_NAME").tips({
				side:3,
	            msg:'请输入店铺名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_NAME").focus();
			return false;
		}
		if($("#STORE_OWER").val()==""){
			$("#STORE_OWER").tips({
				side:3,
	            msg:'请输入店主姓名',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_OWER").focus();
			return false;
		}
		if($("#STORE_OWER_CARD").val()==""){
			$("#STORE_OWER_CARD").tips({
				side:3,
	            msg:'请输入店主银行卡',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_OWER_CARD").focus();
			return false;
		}
		if($("#STORE_QQ").val()==""){
			$("#STORE_QQ").tips({
				side:3,
	            msg:'请输入店铺QQ',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_QQ").focus();
			return false;
		}
		if($("#STORE_RECOMMEND").val()==""){
			$("#STORE_RECOMMEND").tips({
				side:3,
	            msg:'请输入是否推荐',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_RECOMMEND").focus();
			return false;
		}
		if($("#STORE_SEO_DESCRIPTION").val()==""){
			$("#STORE_SEO_DESCRIPTION").tips({
				side:3,
	            msg:'请输入SEO',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_SEO_DESCRIPTION").focus();
			return false;
		}
		if($("#STORE_SEO_KEYWORDS").val()==""){
			$("#STORE_SEO_KEYWORDS").tips({
				side:3,
	            msg:'请输入SEO',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_SEO_KEYWORDS").focus();
			return false;
		}
		if($("#STORE_STATUS").val()==""){
			$("#STORE_STATUS").tips({
				side:3,
	            msg:'请输入店铺状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_STATUS").focus();
			return false;
		}
		if($("#STORE_TELEPHONE").val()==""){
			$("#STORE_TELEPHONE").tips({
				side:3,
	            msg:'请输入店铺电话',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_TELEPHONE").focus();
			return false;
		}
		if($("#STORE_ZIP").val()==""){
			$("#STORE_ZIP").tips({
				side:3,
	            msg:'请输入店铺邮编',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_ZIP").focus();
			return false;
		}
		if($("#TEMPLATE").val()==""){
			$("#TEMPLATE").tips({
				side:3,
	            msg:'请输入店铺模板',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#TEMPLATE").focus();
			return false;
		}
		if($("#AREA0ID").val()==""){
			$("#AREA0ID").tips({
				side:3,
	            msg:'请输入省份ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#AREA0ID").focus();
			return false;
		}
		if($("#AREA1ID").val()==""){
			$("#AREA1ID").tips({
				side:3,
	            msg:'请输入市ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#AREA1ID").focus();
			return false;
		}
		if($("#AREA2ID").val()==""){
			$("#AREA2ID").tips({
				side:3,
	            msg:'请输入区ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#AREA2ID").focus();
			return false;
		}
		if($("#STORE_TYPE").val()==""){
			$("#STORE_TYPE").tips({
				side:3,
	            msg:'请输入店铺类型',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_TYPE").focus();
			return false;
		}
		if($("#STORE_LOGO").val()==""){
			$("#STORE_LOGO").tips({
				side:3,
	            msg:'请输入店铺LOGO',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_LOGO").focus();
			return false;
		}
		if($("#FAVORITE_COUNT").val()==""){
			$("#FAVORITE_COUNT").tips({
				side:3,
	            msg:'请输入收藏人数',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#FAVORITE_COUNT").focus();
			return false;
		}
		if($("#STORE_LAT").val()==""){
			$("#STORE_LAT").tips({
				side:3,
	            msg:'请输入店铺纬度',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_LAT").focus();
			return false;
		}
		if($("#STORE_LNG").val()==""){
			$("#STORE_LNG").tips({
				side:3,
	            msg:'请输入店铺经度',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_LNG").focus();
			return false;
		}
		if($("#STORE_WW").val()==""){
			$("#STORE_WW").tips({
				side:3,
	            msg:'请输入店铺旺旺',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STORE_WW").focus();
			return false;
		}
		if($("#MAP_TYPE").val()==""){
			$("#MAP_TYPE").tips({
				side:3,
	            msg:'请输入地图类型',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#MAP_TYPE").focus();
			return false;
		}
		if($("#DELIVERY_BEGIN_TIME").val()==""){
			$("#DELIVERY_BEGIN_TIME").tips({
				side:3,
	            msg:'请输入合同开始日期',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DELIVERY_BEGIN_TIME").focus();
			return false;
		}
		if($("#DELIVERY_END_TIME").val()==""){
			$("#DELIVERY_END_TIME").tips({
				side:3,
	            msg:'请输入合同结束日期',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DELIVERY_END_TIME").focus();
			return false;
		}
		if($("#UID").val()==""){
			$("#UID").tips({
				side:3,
	            msg:'请输入用户ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#UID").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="mall_store/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">创建时间:</td>
				<td><input class="span10 date-picker" name="ADDTIME" id="ADDTIME" value="${pd.ADDTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺地址:</td>
				<td><input type="text" name="STORE_ADDRESS" id="STORE_ADDRESS" value="${pd.STORE_ADDRESS}" maxlength="32" placeholder="这里输入店铺地址" title="店铺地址"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺描述:</td>
				<td><input type="text" name="STORE_INFO" id="STORE_INFO" value="${pd.STORE_INFO}" maxlength="32" placeholder="这里输入店铺描述" title="店铺描述"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺名称:</td>
				<td><input type="text" name="STORE_NAME" id="STORE_NAME" value="${pd.STORE_NAME}" maxlength="32" placeholder="这里输入店铺名称" title="店铺名称"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店主姓名:</td>
				<td><input type="text" name="STORE_OWER" id="STORE_OWER" value="${pd.STORE_OWER}" maxlength="32" placeholder="这里输入店主姓名" title="店主姓名"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店主银行卡:</td>
				<td><input type="text" name="STORE_OWER_CARD" id="STORE_OWER_CARD" value="${pd.STORE_OWER_CARD}" maxlength="32" placeholder="这里输入店主银行卡" title="店主银行卡"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺QQ:</td>
				<td><input type="text" name="STORE_QQ" id="STORE_QQ" value="${pd.STORE_QQ}" maxlength="32" placeholder="这里输入店铺QQ" title="店铺QQ"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">是否推荐:</td>
				<td><input type="number" name="STORE_RECOMMEND" id="STORE_RECOMMEND" value="${pd.STORE_RECOMMEND}" maxlength="32" placeholder="这里输入是否推荐" title="是否推荐"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">SEO:</td>
				<td><input type="text" name="STORE_SEO_DESCRIPTION" id="STORE_SEO_DESCRIPTION" value="${pd.STORE_SEO_DESCRIPTION}" maxlength="32" placeholder="这里输入SEO" title="SEO"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">SEO:</td>
				<td><input type="text" name="STORE_SEO_KEYWORDS" id="STORE_SEO_KEYWORDS" value="${pd.STORE_SEO_KEYWORDS}" maxlength="32" placeholder="这里输入SEO" title="SEO"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺状态:</td>
				<td><input type="number" name="STORE_STATUS" id="STORE_STATUS" value="${pd.STORE_STATUS}" maxlength="32" placeholder="这里输入店铺状态" title="店铺状态"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺电话:</td>
				<td><input type="text" name="STORE_TELEPHONE" id="STORE_TELEPHONE" value="${pd.STORE_TELEPHONE}" maxlength="32" placeholder="这里输入店铺电话" title="店铺电话"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺邮编:</td>
				<td><input type="text" name="STORE_ZIP" id="STORE_ZIP" value="${pd.STORE_ZIP}" maxlength="32" placeholder="这里输入店铺邮编" title="店铺邮编"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺模板:</td>
				<td><input type="text" name="TEMPLATE" id="TEMPLATE" value="${pd.TEMPLATE}" maxlength="32" placeholder="这里输入店铺模板" title="店铺模板"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">省份ID:</td>
				<td><input type="number" name="AREA0ID" id="AREA0ID" value="${pd.AREA0ID}" maxlength="32" placeholder="这里输入省份ID" title="省份ID"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">市ID:</td>
				<td><input type="number" name="AREA1ID" id="AREA1ID" value="${pd.AREA1ID}" maxlength="32" placeholder="这里输入市ID" title="市ID"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">区ID:</td>
				<td><input type="number" name="AREA2ID" id="AREA2ID" value="${pd.AREA2ID}" maxlength="32" placeholder="这里输入区ID" title="区ID"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺类型:</td>
				<td><input type="text" name="STORE_TYPE" id="STORE_TYPE" value="${pd.STORE_TYPE}" maxlength="32" placeholder="这里输入店铺类型" title="店铺类型"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺LOGO:</td>
				<td><input type="text" name="STORE_LOGO" id="STORE_LOGO" value="${pd.STORE_LOGO}" maxlength="32" placeholder="这里输入店铺LOGO" title="店铺LOGO"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">收藏人数:</td>
				<td><input type="number" name="FAVORITE_COUNT" id="FAVORITE_COUNT" value="${pd.FAVORITE_COUNT}" maxlength="32" placeholder="这里输入收藏人数" title="收藏人数"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺纬度:</td>
				<td><input type="text" name="STORE_LAT" id="STORE_LAT" value="${pd.STORE_LAT}" maxlength="32" placeholder="这里输入店铺纬度" title="店铺纬度"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺经度:</td>
				<td><input type="text" name="STORE_LNG" id="STORE_LNG" value="${pd.STORE_LNG}" maxlength="32" placeholder="这里输入店铺经度" title="店铺经度"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">店铺旺旺:</td>
				<td><input type="text" name="STORE_WW" id="STORE_WW" value="${pd.STORE_WW}" maxlength="32" placeholder="这里输入店铺旺旺" title="店铺旺旺"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">地图类型:</td>
				<td><input type="text" name="MAP_TYPE" id="MAP_TYPE" value="${pd.MAP_TYPE}" maxlength="32" placeholder="这里输入地图类型" title="地图类型"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">合同开始日期:</td>
				<td><input type="number" name="DELIVERY_BEGIN_TIME" id="DELIVERY_BEGIN_TIME" value="${pd.DELIVERY_BEGIN_TIME}" maxlength="32" placeholder="这里输入合同开始日期" title="合同开始日期"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">合同结束日期:</td>
				<td><input class="span10 date-picker" name="DELIVERY_END_TIME" id="DELIVERY_END_TIME" value="${pd.DELIVERY_END_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="合同结束日期" title="合同结束日期"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">用户ID:</td>
				<td><input type="number" name="UID" id="UID" value="${pd.UID}" maxlength="32" placeholder="这里输入用户ID" title="用户ID"/></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
	</form>
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		$(top.hangge());
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>