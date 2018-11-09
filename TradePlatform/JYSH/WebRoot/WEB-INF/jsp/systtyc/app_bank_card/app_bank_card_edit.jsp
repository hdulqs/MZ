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
	            msg:'请输入用户手机号',
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
	            msg:'请输入姓名',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#TRUENAME").focus();
			return false;
		}
		if($("#BANKPROVINCE").val()==""){
			$("#BANKPROVINCE").tips({
				side:3,
	            msg:'请输入开户省份',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#BANKPROVINCE").focus();
			return false;
		}
		if($("#BANKADDRESS").val()==""){
			$("#BANKADDRESS").tips({
				side:3,
	            msg:'请输入开户城市',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#BANKADDRESS").focus();
			return false;
		}
		if($("#CARDBANK").val()==""){
			$("#CARDBANK").tips({
				side:3,
	            msg:'请输入开户银行	',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CARDBANK").focus();
			return false;
		}
		if($("#SUBBANK").val()==""){
			$("#SUBBANK").tips({
				side:3,
	            msg:'请输入开户支行',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#SUBBANK").focus();
			return false;
		}
		if($("#CARDNUMBER").val()==""){
			$("#CARDNUMBER").tips({
				side:3,
	            msg:'请输入银行卡号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CARDNUMBER").focus();
			return false;
		}
		if($("#MOBILE").val()==""){
			$("#MOBILE").tips({
				side:3,
	            msg:'请输入手机号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#MOBILE").focus();
			return false;
		}
		if($("#ALIPAY").val()==""){
			$("#ALIPAY").tips({
				side:3,
	            msg:'请输入支付宝账号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ALIPAY").focus();
			return false;
		}
		if($("#ALIPAYPICTURE").val()==""){
			$("#ALIPAYPICTURE").tips({
				side:3,
	            msg:'请输入支付宝收款码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ALIPAYPICTURE").focus();
			return false;
		}
		if($("#WECHAT").val()==""){
			$("#WECHAT").tips({
				side:3,
	            msg:'请输入微信账号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#WECHAT").focus();
			return false;
		}
		if($("#WECHATPICTURE").val()==""){
			$("#WECHATPICTURE").tips({
				side:3,
	            msg:'请输入微信收款码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#WECHATPICTURE").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="app_bank_card/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="APP_BANK_CARD_ID" id="APP_BANK_CARD_ID" value="${pd.APP_BANK_CARD_ID}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">用户手机号:</td>
				<td><input type="text" name="USERNAME" id="USERNAME" value="${pd.USERNAME}" maxlength="32" placeholder="这里输入用户手机号" title="用户手机号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">姓氏:</td>
				<td><input type="text" name="SURNAME" id="SURNAME" value="${pd.SURNAME}" maxlength="32" placeholder="这里输入姓氏" title="姓氏"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">姓名:</td>
				<td><input type="text" name="TRUENAME" id="TRUENAME" value="${pd.TRUENAME}" maxlength="32" placeholder="这里输入姓名" title="姓名"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">开户省份:</td>
				<td><input type="text" name="BANKPROVINCE" id="BANKPROVINCE" value="${pd.BANKPROVINCE}" maxlength="32" placeholder="这里输入开户省份" title="开户省份"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">开户城市:</td>
				<td><input type="text" name="BANKADDRESS" id="BANKADDRESS" value="${pd.BANKADDRESS}" maxlength="32" placeholder="这里输入开户城市" title="开户城市"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">开户银行	:</td>
				<td><input type="text" name="CARDBANK" id="CARDBANK" value="${pd.CARDBANK}" maxlength="32" placeholder="这里输入开户银行	" title="开户银行	"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">开户支行:</td>
				<td><input type="text" name="SUBBANK" id="SUBBANK" value="${pd.SUBBANK}" maxlength="32" placeholder="这里输入开户支行" title="开户支行"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">银行卡号:</td>
				<td><input type="text" name="CARDNUMBER" id="CARDNUMBER" value="${pd.CARDNUMBER}" maxlength="32" placeholder="这里输入银行卡号" title="银行卡号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">手机号:</td>
				<td><input type="text" name="MOBILE" id="MOBILE" value="${pd.MOBILE}" maxlength="32" placeholder="这里输入手机号" title="手机号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">支付宝账号:</td>
				<td><input type="text" name="ALIPAY" id="ALIPAY" value="${pd.ALIPAY}" maxlength="32" placeholder="这里输入支付宝账号" title="支付宝账号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">支付宝收款码:</td>
				<td><input type="text" name="ALIPAYPICTURE" id="ALIPAYPICTURE" value="${pd.ALIPAYPICTURE}" maxlength="32" placeholder="这里输入支付宝收款码" title="支付宝收款码"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">微信账号:</td>
				<td><input type="text" name="WECHAT" id="WECHAT" value="${pd.WECHAT}" maxlength="32" placeholder="这里输入微信账号" title="微信账号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">微信收款码:</td>
				<td><input type="text" name="WECHATPICTURE" id="WECHATPICTURE" value="${pd.WECHATPICTURE}" maxlength="32" placeholder="这里输入微信收款码" title="微信收款码"/></td>
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