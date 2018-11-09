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
    <h3>完成付款</h3>
</div>
<div class="main-bd" style="padding:10px 20px; border:none;">
    <div class="margint20"></div>
    <form id="uploadForm" action="${ctx}/otc/otcPayment"  method="post" enctype="multipart/form-data">
        <div style="display: none">
            <input  id="transactionorderid" name="transactionorderid" value="${transactionorderid!}" >
        </div>
        <div class="margint20">

            <span>付款方式：</span>
            <div class="radio-box">
                <input id="zfb" name="payment" value="Alipay" type="radio" checked>
                <label for="zfb">支付宝</label>
            </div>
            <div class="radio-box">
                <input id="wx" name="payment" value="WeChat" type="radio">
                <label for="wx">微信</label>
            </div>
            <div class="radio-box">
                <input id="bank" name="payment" value="Bank" type="radio">
                <label for="bank">银行转账</label>
            </div>
        </div>
        <div class="margint16" style="width: 100%;height: auto;">
            <label>付款凭证：</label>

            <ul class="uploadList">
                <li>
                    <img src="${ctx}/static/${version}/img/drd/add.png"/>

                    <input type="file" multiple class="addphoto" name="img1"/>

                </li>
                <li class="hide">
                    <img src="${ctx}/static/${version}/img/drd/add.png"/>
                    <input type="file" multiple class="addphoto" name="img2"/>
                </li>
                <li class="hide">
                    <img src="${ctx}/static/${version}/img/drd/add.png"/>
                    <input type="file" multiple class="addphoto" name="img3"/>
                </li>
            </ul>
            <p class="addInform">点击添加图片(不超过500KB)，最多添加3张</p>
        </div>
    </form>
    <div class="center" style="width: 100%">
        <button class="margint30 btn btn-primary btn-confirm">确定</button>
        <button style="margin-left: 20px" class="margint30 btn btn-primary btn-otccancel">返回</button>
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

    seajs.use(["js/front/main_news", "js/front/user/otcPay"], function (m, otcPay) {

        m.init();

        otcPay.init();
    });
</script>
</html>