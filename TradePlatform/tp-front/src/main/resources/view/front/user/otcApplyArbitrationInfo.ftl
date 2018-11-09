<html lang="en">
<head>
    <#include "/base/base.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="" name="description">
    <meta content="" name="author">
    <link rel="stylesheet" href="${ctx}/static/src/css/otc/otc.css?v=201805231537">
    <style type="text/css">
        body {
            background: #fff;
            color: #666;
        }
    </style>
</head>

<body>
<!-- 头部 -->
<div class="mtitle">
    <h3>申请仲裁</h3>
</div>
<form id="uploadForm" action="${ctx}/otc/otcApplyArbitration" method="post" enctype="multipart/form-data">
    <div class="main-bd" style="padding:10px 20px; border:none;">
        <div class="margint20"></div>
        <div style="display: none">
            <input   name="transactionorderid" value="${transactionorderid!}" >
        </div>
        <div class="margint20 remark">
            <span>申诉原因：</span>
            <textarea name="appealReason" readonly placeholder="请填写申诉原因~">${orderinfo.appealReason!}</textarea>
        </div>
        <div class="margint16" style="width: 100%;height: auto;">
            <label>付款凭证：</label>

            <ul class="uploadList">
                 <#if orderinfo.img4  ?? && orderinfo.img4!="">
            <li > <a href="${orderinfo.img4!}" target="_blank"><img  src="${orderinfo.img4!}"/> </a></li>
                 </#if>
             <#if orderinfo.img5  ?? && orderinfo.img5!="">
            <li ><a href="${orderinfo.img5!}" target="_blank"><img  src="${orderinfo.img5!}"/></a></li>
             </#if>
            <#if orderinfo.img6  ?? && orderinfo.img6!="">
               <li  class="marginb16">
               <a href="${orderinfo.img6!}" target="_blank">
                   <img src="${orderinfo.img6!}"/>
               </a>
               </li>
            </#if>
            </ul>
            <p class="addInform">点击添加图片(不超过500KB)，最多添加3张</p>
        </div>
    </div>
</form>
<div class="center" style="width: 100%">
    <button style="margin-left: 20px" class="margint30 btn btn-primary btn-otccancel">返回</button>
</div>

<div id="reDialog">
    <div class="codeDialog">
        <div class="code_close" onclick="$('#reDialog').hide()">
            <div class="close_img"></div>
        </div>

        <div class="code">
            <img id="otcimgId" src="">
        </div>
    </div>
</div>

<!--页面中部内容结束-->
</body>
<script type="text/javascript" src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript"
        src="${ctx}/static/${version}/lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/${version}/lib/ys.js"></script>
<script>
    seajs.config({
        base: "${ctx}/static/${version}",
        alias: {
            <!-- 基础框架JS -->
            "jquery": "lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js",
            "jqueryForm": "lib/jqueryForm/jquery.form.js",
            <!-- layer -->
            "layer": "lib/layer/layer.js",
            <!-- 自定义JS -->
            "base": "js/base/base.js"
        },
        preload: ['jquery', 'jqueryForm', 'layer'],
        map: [
            ['.js', '.js?v=${t}'] //映射规则
        ]
    });

    seajs.use(["js/front/main_news", "js/front/user/otcApplyArbitration"], function (m, otcApplyArbitration) {

        m.init();

        otcApplyArbitration.init();
    });
    function receiptCode(obj) {
        $("#otcimgId").attr('src',  $(obj).attr('src'));
        $('#reDialog').show();
    }
</script>
</html>