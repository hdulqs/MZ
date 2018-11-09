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
		if($("#CLASSNAME").val()==""){
			$("#CLASSNAME").tips({
				side:3,
	            msg:'请输入分类名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CLASSNAME").focus();
			return false;
		}
		if($("#DISPLAY").val()==""){
			$("#DISPLAY").tips({
				side:3,
	            msg:'请输入是否显示',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DISPLAY").focus();
			return false;
		}
		if($("#RECOMMEND").val()==""){
			$("#RECOMMEND").tips({
				side:3,
	            msg:'请输入是否推荐',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#RECOMMEND").focus();
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
	            msg:'请添加图标',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ICON_PATH").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		document.getElementById('zhongxin').style.display = 'none';
	}
	
</script>
<style>
	#table_report tr td input {
		color: #438eb9 !important; 
	}
	#table_report tr td #PARENT_ID {
		color: #438eb9 !important; 
	}
</style>

	</head>
<body>
	<form action="mall_goodsclass/${msg }.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">分类名称:</td>
				<td><input type="text" name="CLASSNAME" id="CLASSNAME" maxlength="32" placeholder="这里输入分类名称" title="分类名称"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">上级分类:</td>
				<td>
					<select name="PARENT_ID" id="PARENT_ID" onchange="Sselect()">
						${option}
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">是否显示:</td>
				<td><input name="DISPLAY" id="DISPLAY" class="ace-switch ace-switch-5" type="checkbox" /><span class="lbl"></span></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">是否推荐:</td>
				<td><input name="RECOMMEND" id="RECOMMEND" class="ace-switch ace-switch-5" type="checkbox" /><span class="lbl"></span></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">序号:</td>
				<td><input type="number" name="SEQUENCE" id="SEQUENCE" maxlength="32" placeholder="这里输入序号" title="序号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">图标:</td>
				<td>
					<input multiple type="file" name="ICON_PATH" id="ICON_PATH" />
				</td>
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
			
			
			$('#ICON_PATH').ace_file_input({
				
				style:'well',
				btn_choose:'Did you see my little bear?',
				btn_change:null,
				no_icon:'icon-cloud-upload',
				droppable:true,
				onchange:null,
				thumbnail:'small',
				before_change:function(files, dropped) {
					if(files instanceof Array || (!!window.FileList && files instanceof FileList)) {
						//check each file and see if it is valid, if not return false or make a new array, add the valid files to it and return the array
						//note: if files have not been dropped, this does not change the internal value of the file input element, as it is set by the browser, and further file uploading and handling should be done via ajax, etc, otherwise all selected files will be sent to server
						//example:
							
						var result = [];
						if(files.length<2){
						for(var i = 0; i < files.length; i++) {
							var file = files[i];
							if((/^image\//i).test(file.type) && file.size < 5242880)
								result.push(file);
						}
						}
						return result;
					}
					return true;
				}
				/*,
				before_remove : function() {
					return true;
				}*/
			});
			
		});
		
		</script>
		
</body>
</html>