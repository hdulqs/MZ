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
			if($("#USERNAME").val()==""){
			$("#USERNAME").tips({
				side:3,
	            msg:'请输入用户账号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#USERNAME").focus();
			return false;
		}
		if($("#SURNAME").val()==""){
			$("#SURNAME").tips({
				side:3,
	            msg:'请输入姓氏',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#SURNAME").focus();
			return false;
		}
		if($("#TRUENAME").val()==""){
			$("#TRUENAME").tips({
				side:3,
	            msg:'请输入名字',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#TRUENAME").focus();
			return false;
		}
		if($("#ACCOUNTNUM").val()==""){
			$("#ACCOUNTNUM").tips({
				side:3,
	            msg:'请输入资金账户号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ACCOUNTNUM").focus();
			return false;
		}
		if($("#HOTMONEY").val()==""){
			$("#HOTMONEY").tips({
				side:3,
	            msg:'请输入可用金额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#HOTMONEY").focus();
			return false;
		}
		if($("#COLDMONEY").val()==""){
			$("#COLDMONEY").tips({
				side:3,
	            msg:'请输入冻结金额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#COLDMONEY").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="app_account/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="APP_ACCOUNT_ID" id="APP_ACCOUNT_ID" value="${pd.APP_ACCOUNT_ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">用户账号:</td>
				<td><input type="text" name="USERNAME" id="USERNAME" value="${pd.USERNAME}" maxlength="32" placeholder="这里输入用户账号" title="用户账号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">姓氏:</td>
				<td><input type="text" name="SURNAME" id="SURNAME" value="${pd.SURNAME}" maxlength="32" placeholder="这里输入姓氏" title="姓氏"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">名字:</td>
				<td><input type="text" name="TRUENAME" id="TRUENAME" value="${pd.TRUENAME}" maxlength="32" placeholder="这里输入名字" title="名字"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">资金账户号:</td>
				<td><input type="text" name="ACCOUNTNUM" id="ACCOUNTNUM" value="${pd.ACCOUNTNUM}" maxlength="32" placeholder="这里输入资金账户号" title="资金账户号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">可用金额:</td>
				<td><input type="text" name="HOTMONEY" id="HOTMONEY" value="${pd.HOTMONEY}" maxlength="32" placeholder="这里输入可用金额" title="可用金额"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">冻结金额:</td>
				<td><input type="text" name="COLDMONEY" id="COLDMONEY" value="${pd.COLDMONEY}" maxlength="32" placeholder="这里输入冻结金额" title="冻结金额"/></td>
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