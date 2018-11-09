<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8"/>
    <title></title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/css/chosen.css"/>

    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>

    <link rel="stylesheet" href="static/css/datepicker.css"/><!-- 日期框 -->
    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>

    <script type="text/javascript">


        //保存
        function save() {
            if ($("#ADDTIME").val() == "") {
                $("#ADDTIME").tips({
                    side: 3,
                    msg: '请输入创建时间',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#ADDTIME").focus();
                return false;
            }
            if ($("#STORE_ADDRESS").val() == "") {
                $("#STORE_ADDRESS").tips({
                    side: 3,
                    msg: '请输入店铺地址',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_ADDRESS").focus();
                return false;
            }
            if ($("#STORE_INFO").val() == "") {
                $("#STORE_INFO").tips({
                    side: 3,
                    msg: '请输入店铺描述',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_INFO").focus();
                return false;
            }
            if ($("#STORE_NAME").val() == "") {
                $("#STORE_NAME").tips({
                    side: 3,
                    msg: '请输入店铺名称',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_NAME").focus();
                return false;
            }
            if ($("#STORE_OWER").val() == "") {
                $("#STORE_OWER").tips({
                    side: 3,
                    msg: '请输入店主姓名',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_OWER").focus();
                return false;
            }
            if ($("#STORE_OWER_CARD").val() == "") {
                $("#STORE_OWER_CARD").tips({
                    side: 3,
                    msg: '请输入店主银行卡',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_OWER_CARD").focus();
                return false;
            }
            if ($("#STORE_QQ").val() == "") {
                $("#STORE_QQ").tips({
                    side: 3,
                    msg: '请输入店铺QQ',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_QQ").focus();
                return false;
            }
            if ($("#STORE_RECOMMEND").val() == "") {
                $("#STORE_RECOMMEND").tips({
                    side: 3,
                    msg: '请输入是否推荐',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_RECOMMEND").focus();
                return false;
            }
            if ($("#STORE_SEO_DESCRIPTION").val() == "") {
                $("#STORE_SEO_DESCRIPTION").tips({
                    side: 3,
                    msg: '请输入SEO',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_SEO_DESCRIPTION").focus();
                return false;
            }
            if ($("#STORE_SEO_KEYWORDS").val() == "") {
                $("#STORE_SEO_KEYWORDS").tips({
                    side: 3,
                    msg: '请输入SEO',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_SEO_KEYWORDS").focus();
                return false;
            }
            if ($("#STORE_STATUS").val() == "") {
                $("#STORE_STATUS").tips({
                    side: 3,
                    msg: '请输入店铺状态',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_STATUS").focus();
                return false;
            }
            if ($("#STORE_TELEPHONE").val() == "") {
                $("#STORE_TELEPHONE").tips({
                    side: 3,
                    msg: '请输入店铺电话',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_TELEPHONE").focus();
                return false;
            }
            if ($("#STORE_ZIP").val() == "") {
                $("#STORE_ZIP").tips({
                    side: 3,
                    msg: '请输入店铺邮编',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_ZIP").focus();
                return false;
            }
            if ($("#TEMPLATE").val() == "") {
                $("#TEMPLATE").tips({
                    side: 3,
                    msg: '请输入店铺模板',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#TEMPLATE").focus();
                return false;
            }
            if ($("#AREA0ID").val() == "") {
                $("#AREA0ID").tips({
                    side: 3,
                    msg: '请输入省份ID',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#AREA0ID").focus();
                return false;
            }
            if ($("#AREA1ID").val() == "") {
                $("#AREA1ID").tips({
                    side: 3,
                    msg: '请输入市ID',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#AREA1ID").focus();
                return false;
            }
            if ($("#AREA2ID").val() == "") {
                $("#AREA2ID").tips({
                    side: 3,
                    msg: '请输入区ID',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#AREA2ID").focus();
                return false;
            }
            if ($("#STORE_TYPE").val() == "") {
                $("#STORE_TYPE").tips({
                    side: 3,
                    msg: '请输入店铺类型',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_TYPE").focus();
                return false;
            }
            if ($("#STORE_LOGO").val() == "") {
                $("#STORE_LOGO").tips({
                    side: 3,
                    msg: '请输入店铺LOGO',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_LOGO").focus();
                return false;
            }
            if ($("#FAVORITE_COUNT").val() == "") {
                $("#FAVORITE_COUNT").tips({
                    side: 3,
                    msg: '请输入收藏人数',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#FAVORITE_COUNT").focus();
                return false;
            }
            if ($("#STORE_LAT").val() == "") {
                $("#STORE_LAT").tips({
                    side: 3,
                    msg: '请输入店铺纬度',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_LAT").focus();
                return false;
            }
            if ($("#STORE_LNG").val() == "") {
                $("#STORE_LNG").tips({
                    side: 3,
                    msg: '请输入店铺经度',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_LNG").focus();
                return false;
            }
            if ($("#STORE_WW").val() == "") {
                $("#STORE_WW").tips({
                    side: 3,
                    msg: '请输入店铺旺旺',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#STORE_WW").focus();
                return false;
            }
            if ($("#MAP_TYPE").val() == "") {
                $("#MAP_TYPE").tips({
                    side: 3,
                    msg: '请输入地图类型',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#MAP_TYPE").focus();
                return false;
            }
            if ($("#DELIVERY_BEGIN_TIME").val() == "") {
                $("#DELIVERY_BEGIN_TIME").tips({
                    side: 3,
                    msg: '请输入合同开始日期',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#DELIVERY_BEGIN_TIME").focus();
                return false;
            }
            if ($("#DELIVERY_END_TIME").val() == "") {
                $("#DELIVERY_END_TIME").tips({
                    side: 3,
                    msg: '请输入合同结束日期',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#DELIVERY_END_TIME").focus();
                return false;
            }
            if ($("#UID").val() == "") {
                $("#UID").tips({
                    side: 3,
                    msg: '请输入用户ID',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#UID").focus();
                return false;
            }
            $("#Form").submit();
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        }
    </script>
    <style>
        #table_report tr td input {
            color: #438eb9 !important;
        }

        #table_report tr td .chzn-select {
            color: #438eb9 !important;
        }
    </style>
</head>
<body>
<form action="mall_store/${msg }.do" name="Form" id="Form" method="post">
    <input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
    <div id="zhongxin">
        <table id="table_report" class="table table-striped table-bordered table-hover">
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">商家账号户名:</td>
                <td><input type="text" name="STORE_ACCOUNT" id="STORE_ACCOUNT" value="${pd.STORE_ACCOUNT}"
                           maxlength="32" placeholder="这里输入商家账号户名" title="商家账号户名"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">密码:</td>
                <td><input type="text" name="STORE_PASSWORD" id="STORE_PASSWORD" value="" maxlength="32"
                           placeholder="这里输入商家密码" title="商家密码"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">店主姓名:</td>
                <td><input type="text" name="STORE_OWER" id="STORE_OWER" value="${pd.STORE_OWER}" maxlength="32"
                           placeholder="这里输入店主姓名" title="店主姓名"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">店主身份证号:</td>
                <td><input type="text" name="STORE_SFZ" id="STORE_SFZ" value="${pd.STORE_SFZ}" maxlength="32"
                           placeholder="这里输入店主身份证号" title="店主身份证号"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">开户名:</td>
                <td><input type="text" name="STORE_KHM" id="STORE_KHM" value="${pd.STORE_KHM}" maxlength="32"
                           placeholder="这里输入公司对公账户名称" title="公司对公账户名称"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">银行账号:</td>
                <td><input type="text" name="STORE_YHZH" id="STORE_YHZH" value="${pd.STORE_YHZH}" maxlength="32"
                           placeholder="这里输入银行账号" title="银行账号"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">开户银行:</td>
                <td><input type="text" name="STORE_KHYH" id="STORE_KHYH" value="${pd.STORE_KHYH}" maxlength="32"
                           placeholder="这里输入开户银行" title="开户银行"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">支行名称:</td>
                <td><input type="text" name="STORE_ZHMC" id="STORE_ZHMC" value="${pd.STORE_ZHMC}" maxlength="32"
                           placeholder="这里输入支行名称" title="支行名称"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">店铺名称:</td>
                <td><input type="text" name="STORE_NAME" id="STORE_NAME" value="${pd.STORE_NAME}" maxlength="32"
                           placeholder="这里输入店铺名称" title="店铺名称"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">店铺分类:</td>
                <td><input type="text" name="STORE_TYPE" id="STORE_TYPE" value="${pd.STORE_TYPE}" maxlength="32"
                           placeholder="这里输入店铺类型" title="店铺类型"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">所在地区:</td>
                <td>
                    <!-- 					省/<input type="text" name="AREA0ID" id="AREA0ID" value="${pd.AREA0ID}" maxlength="32" placeholder="这里输入省份" title="省份"/> -->
                    <!-- 					市/<input type="text" name="AREA1ID" id="AREA1ID" value="${pd.AREA1ID}" maxlength="32" placeholder="这里输入市" title="市"/>				 -->
                    <!-- 					区/<input type="text" name="AREA2ID" id="AREA2ID" value="${pd.AREA2ID}" maxlength="32" placeholder="这里输入区" title="区"/> -->

                    <select class="chzn-select" name="field1" id="field1" data-placeholder="省"
                            style="vertical-align:top;width: 120px;">
                        <option value=""></option>
                        <option value="">全部</option>
                        <option value="">1</option>
                        <option value="">2</option>
                    </select>
                    <select class="chzn-select" name="field2" id="field2" data-placeholder="市"
                            style="vertical-align:top;width: 120px;">
                        <option value=""></option>
                        <option value="">全部</option>
                        <option value="">1</option>
                        <option value="">2</option>
                    </select>
                    <select class="chzn-select" name="field3" id="field3" data-placeholder="区"
                            style="vertical-align:top;width: 120px;">
                        <option value=""></option>
                        <option value="">全部</option>
                        <option value="">1</option>
                        <option value="">2</option>
                    </select>


                </td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">详细地址:</td>
                <td><input type="text" name="STORE_ADDRESS" id="STORE_ADDRESS" value="${pd.STORE_ADDRESS}"
                           maxlength="32" placeholder="这里输入商户详细地址" title="商户详细地址"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">邮政编码:</td>
                <td><input type="text" name="STORE_ZIP" id="STORE_ZIP" value="${pd.STORE_ZIP}" maxlength="32"
                           placeholder="这里输入邮政编码" title="邮政编码"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">联系电话:</td>
                <td><input type="text" name="STORE_TELEPHONE" id="STORE_TELEPHONE" value="${pd.STORE_TELEPHONE}"
                           maxlength="32" placeholder="这里输入联系电话" title="联系电话"/></td>
            </tr>
            <%--<tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">合同开始日期:</td>
                <td><input class="span10 date-picker" name="DELIVERY_BEGIN_TIME" id="DELIVERY_BEGIN_TIME"
                           value="${pd.DELIVERY_BEGIN_TIME}" type="text" data-date-format="yyyy-mm-dd"
                           readonly="readonly" placeholder="合同开始日期" title="合同开始日期"/></td>
            </tr>--%>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">有效期至:</td>
                <td><input class="span10 date-picker" name="DELIVERY_END_TIME" id="DELIVERY_END_TIME"
                           value="${pd.DELIVERY_END_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly"
                           placeholder="有效期至" title="有效期至"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">店铺状态:</td>
                <td>
                    <select class="chzn-select" name="STORE_STATUS" id="STORE_STATUS" data-placeholder="请选择店铺状态"
                            style="vertical-align:top;width: 120px;">
                        <option value="0"
                                <c:if test="${pd.STORE_STATUS == '0' }">selected</c:if> >关闭
                        </option>
                        <option value="1"
                                <c:if test="${pd.STORE_STATUS == '1' }">selected</c:if> >正常
                        </option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">
                    是否推荐
                </td>
                <td>
                    <c:if test="${pd.STORE_RECOMMEND == '0'}"><input
                            id="switch-field-1" name="STORE_RECOMMEND"
                            class="ace-switch ace-switch-2" type="checkbox"/><span
                            class="lbl"></span></c:if>
                    <c:if test="${pd.STORE_RECOMMEND == '1'}">
                        <input id="switch-field-1" name="switch-STORE_RECOMMEND-1" class="ace-switch ace-switch-2" type="checkbox"
                               checked="checked"/><span class="lbl"></span></c:if>
                </td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">认证情况:</td>
                <td>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input name="form-field-checkbox1" class="ace-checkbox-2" type="checkbox"/><span
                        class="lbl">实名认证</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input name="form-field-checkbox2" class="ace-checkbox-2" type="checkbox"/><span
                        class="lbl">实体店铺认证</span>
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

    <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img
            src="static/images/jiazai.gif"/><br/><h4 class="lighter block green">提交中...</h4></div>

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
    $(function () {

        //单选框
        $(".chzn-select").chosen();
        $(".chzn-select-deselect").chosen({allow_single_deselect: true});

        //日期框
        $('.date-picker').datepicker();

    });

</script>
</body>
</html>