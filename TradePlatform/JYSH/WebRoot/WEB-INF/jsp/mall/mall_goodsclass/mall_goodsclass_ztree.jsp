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
   	<%@ include file="../../system/admin/top.jsp"%> 
    <meta name="description" content="overview & stats" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <!--引入文件： 1、zTree默认css样式  2、jquery  3、zTree js-->
	<link rel="stylesheet" href="plugins/zTree/3.5/zTreeStyle.min.css" type="text/css">
    <script type="text/javascript" src="static/js/jquery.min.js"></script>
	<script type="text/javascript" src="static/js/jquery.ztree.all.min.js"></script>
</head>
<style>
    /*按钮*/
    .icon_div {
        display: inline-block;
        height: 25px;
        width: 35px;
        background: url(http://c.csdnimg.cn/public/common/toolbar/images/f_icon.png) no-repeat 12px -127px;
    }

    .icon_div a {
        display: inline-block;
        width: 27px;
        height: 20px;
        cursor: pointer;
    }

    /*end--按钮*/

    /*ztree表格*/
    .ztree {
        padding: 0;
        border: 2px solid #CDD6D5;
    }

    .ztree li a {
        vertical-align: middle;
        height: 30px;
    }

    .ztree li > a {
        width: 100%;
    }

    .ztree li > a,
    .ztree li a.curSelectedNode {
        padding-top: 0px;
        background: none;
        height: auto;
        border: none;
        cursor: default;
        opacity: 1;
    }

    .ztree li ul {
        padding-left: 0px
    }

    .ztree div.diy span {
        line-height: 30px;
        vertical-align: middle;
    }

    .ztree div.diy {
        height: 100%;
        width: 20%;
        line-height: 30px;
        border-top: 1px dotted #ccc;
        border-left: 1px solid #eeeeee;
        text-align: center;
        display: inline-block;
        box-sizing: border-box;
        color: #6c6c6c;
        font-family: "SimSun";
        font-size: 12px;
        overflow: hidden;
    }

    .ztree div.diy:first-child {
        text-align: left;
        text-indent: 10px;
        border-left: none;
    }

    .ztree .head {
        background: #5787EB;
    }

    .ztree .head div.diy {
        border-top: none;
        border-right: 1px solid #CDD2D4;
        color: #fff;
        font-family: "Microsoft YaHei";
        font-size: 14px;
    }

    /*end--ztree表格*/
</style>
<body>
<div class="layer">
    <div id="tableMain">
        <ul id="dataTree" class="ztree">

        </ul>
    </div>
</div>

<div class="page-header position-relative">
	<table style="width:100%;">
		<tr>
			<td style="vertical-align:top;">
				<c:if test="${QX.add == 1 }">
				<a class="btn btn-small btn-success" onclick="add();">新增</a>
				</c:if>
			</td>
			<td  style="vertical-align:top;display: none;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
		</tr>
	</table>
</div>
		
		
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		
<script type="text/javascript">

    var zTreeNodes;
    var setting = {
        view: {
            showLine: false,
            showIcon: false,
            addDiyDom: addDiyDom
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
    /**
     * 自定义DOM节点
     */
    function addDiyDom(treeId, treeNode) {
        var spaceWidth = 15;
        var liObj = $("#" + treeNode.tId);
        var aObj = $("#" + treeNode.tId + "_a");
        var switchObj = $("#" + treeNode.tId + "_switch");
        var icoObj = $("#" + treeNode.tId + "_ico");
        var spanObj = $("#" + treeNode.tId + "_span");
        aObj.attr('title', '');
        aObj.append('<div class="diy swich"></div>');
        var div = $(liObj).find('div').eq(0);
        switchObj.remove();
        spanObj.remove();
        icoObj.remove();
        div.append(switchObj);
        div.append(spanObj);
        var spaceStr = "<span style='height:1px;display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></span>";
        switchObj.before(spaceStr);
        var editStr = '';
        editStr += '<div class="diy">' + (treeNode.SEQUENCE == null ? '&nbsp;' : treeNode.SEQUENCE) + '</div>';
        var corpCat = '<div title="' + treeNode.DISPLAY + '">' + treeNode.DISPLAY + '</div>';
        editStr += '<div class="diy">' + (treeNode.DISPLAY == '-' ? '&nbsp;' : corpCat ) + '</div>';
        editStr += '<div class="diy">' + (treeNode.RECOMMEND == null ? '&nbsp;' : treeNode.RECOMMEND ) + '</div>';
        editStr += '<div class="diy">' + formatHandle(treeNode) + '</div>';
        aObj.append(editStr);
    }
    /**
     * 查询数据
     */
    function query() {
		var data='${varList}';
		data=eval(data);
		//初始化列表
        zTreeNodes = data;
        //初始化树
        $.fn.zTree.init($("#dataTree"), setting, zTreeNodes);
        //添加表头
        var li_head = ' <li class="head"><a><div class="diy">分类名称</div><div class="diy">顺序</div><div class="diy">是否显示</div>' +
            '<div class="diy">是否推荐</div><div class="diy">操作</div></a></li>';
        var rows = $("#dataTree").find('li');
        if (rows.length > 0) {
            rows.eq(0).before(li_head)
        } else {
            $("#dataTree").append(li_head);
            $("#dataTree").append('<li ><div style="text-align: center;line-height: 30px;" >无符合条件数据</div></li>')
        }
    }
    /**
     * 根据权限展示功能按钮
     * @param treeNode
     * @returns {string}
     */
    function formatHandle(treeNode) {
        var htmlStr = '';
        
		htmlStr +='<c:if test="${QX.edit != 1 && QX.del != 1 }">无权限</c:if>';
        htmlStr += '<c:if test="${QX.add == 1 }"><a class="icon_add" title="添加子类" href="javascript:add(\'' + treeNode.id + '\')">添加子类</a>|  </c:if>';
        htmlStr += '<c:if test="${QX.edit == 1 }"><a class="icon_edit" title="修改" href="javascript:edit(\'' + treeNode.id + '\')">修改</a>|  </c:if>';
        htmlStr += '<c:if test="${QX.del == 1 }"><a class="icon_del" title="删除" href="javascript:del(\'' + treeNode.id + '\')">删除</a></c:if>';
        return htmlStr;
    }
     
     
	//新增
	function add(Id){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="新增";
		 diag.URL = '<%=basePath%>mall_goodsclass/goAdd.do?ID='+Id;
		 diag.Width = 700;
		 diag.Height = 600;
		 diag.CancelEvent = function(){ //关闭事件
			 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 nextPage(${page.currentPage});
				 }
			}
			diag.close();
		 };
		 diag.show();
	}
     
	//修改
	function edit(Id){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="编辑";
		 diag.URL = '<%=basePath%>mall_goodsclass/goEdit.do?ID='+Id;
		 diag.Width = 700;
		 diag.Height = 600;
		 diag.CancelEvent = function(){ //关闭事件
			 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 nextPage(${page.currentPage});
			}
			diag.close();
		 };
		 diag.show();
	}
	
	
   	//删除
	function del(Id){
		if(confirm("确定要删除吗?")) {
			top.jzts();
			var url = "<%=basePath%>mall_goodsclass/delete.do?ID="+Id+"&tm="+new Date().getTime();
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		}
	}
     
     

    $(function () {
        //初始化数据
        query();
        //数据加载完成后去除灰色界面
        $(top.hangge());
    });
</script>
</body>
</html>