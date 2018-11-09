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
	            msg:'请输入时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ADDTIME").focus();
			return false;
		}
		if($("#NAME").val()==""){
			$("#NAME").tips({
				side:3,
	            msg:'请输入品牌名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#NAME").focus();
			return false;
		}
		if($("#SEQUENCE").val()==""){
			$("#SEQUENCE").tips({
				side:3,
	            msg:'请输入序号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#SEQUENCE").focus();
			return false;
		}
		if($("#ICON_PATH").val()==""){
			$("#ICON_PATH").tips({
				side:3,
	            msg:'请输入icon地址',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ICON_PATH").focus();
			return false;
		}
		if($("#APPLICATIONNOTE").val()==""){
			$("#APPLICATIONNOTE").tips({
				side:3,
	            msg:'请输入申请描述',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#APPLICATIONNOTE").focus();
			return false;
		}
		if($("#STATE").val()==""){
			$("#STATE").tips({
				side:3,
	            msg:'请输入审核状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STATE").focus();
			return false;
		}
		if($("#INITIALS").val()==""){
			$("#INITIALS").tips({
				side:3,
	            msg:'请输入首字母',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#INITIALS").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="mall_brandcategory/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">时间:</td>
				<td><input class="span10 date-picker" name="ADDTIME" id="ADDTIME" value="${pd.ADDTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="时间" title="时间"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">品牌名称:</td>
				<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="32" placeholder="这里输入品牌名称" title="品牌名称"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">序号:</td>
				<td><input type="number" name="SEQUENCE" id="SEQUENCE" value="${pd.SEQUENCE}" maxlength="32" placeholder="这里输入序号" title="序号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">icon地址:</td>
				<td><input type="text" name="ICON_PATH" id="ICON_PATH" value="${pd.ICON_PATH}" maxlength="32" placeholder="这里输入icon地址" title="icon地址"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">申请描述:</td>
				<td><input type="text" name="APPLICATIONNOTE" id="APPLICATIONNOTE" value="${pd.APPLICATIONNOTE}" maxlength="32" placeholder="这里输入申请描述" title="申请描述"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">审核状态:</td>
				<td><input type="number" name="STATE" id="STATE" value="${pd.STATE}" maxlength="32" placeholder="这里输入审核状态" title="审核状态"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">首字母:</td>
				<td><input type="text" name="INITIALS" id="INITIALS" value="${pd.INITIALS}" maxlength="32" placeholder="这里输入首字母" title="首字母"/></td>
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