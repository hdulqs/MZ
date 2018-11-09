<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/taglibs.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
<layout:head title="权限添加" />
</head>
<body>

	<layout:top />
	<layout:left />
	<layout:chat/>
	<layout:search/>
	
	<!-- Main Content -->
	<section class="content-wrap"> 
	
	<!-- Breadcrumb -->
	<layout:breadcrumb/>
	<!-- /Breadcrumb --

	<!-- With Search -->
	<div class="card-panel">
		<h4>权限用户</h4>

		<div class="row">
			<div class="col s1 "></div>
			<div class="col s4 ">
				<form id="form" action="${ctx }/user/appresource/add.do" method="post" >
					权限名称<input type="text" name="name"  />
					url<input type="text" name="url"  />
					pid<input type="text" name="pid"  />
					<a class="btn" id="submit" >保存</a>
				</form>
			</div>
		</div>
	</div>
	<!-- /With Search --> 

	</section>
	<layout:footer/>
</body>
<%@include file="/WEB-INF/view/common/import-js.jspf"%>
<link rel="stylesheet" href="${request_static}/static/component/ztree/css/demo.css" type="text/css">
<link rel="stylesheet" href="${request_static}/static/component/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${request_static}/static/component/ztree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${request_static}/static/component/ztree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${request_static}/static/component/ztree/js/jquery.ztree.exedit-3.5.js"></script>
<SCRIPT type="text/javascript">
		//表单提交
		$("#submit").on("click", function() {
			$("#form").submit();
		});
</SCRIPT>
</html>