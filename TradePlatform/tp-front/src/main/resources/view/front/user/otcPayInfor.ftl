<html lang="en">
<head>
<#include "/base/base.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="" name="description">
    <meta content="" name="author">
    <link rel="stylesheet" href="${ctx}/static/src/css/otc/otc.css?v=20180523">
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
    <h3>付款信息</h3>
</div>
<div class="main-bd" style="padding:10px 20px; border:none;">
    <div class="mention">
        <div class="privilege">
            <table width="100%" class="tb-list2">
                <tbody>
                <tr>
                    <th scope="row">交易方账号</th>
                    <td>${userinfo.username!}</td>
                </tr>
                <tr>
                    <th scope="row">姓名</th>
                    <td>${userinfo.name!}</td>
                </tr>
                <tr>
                    <th scope="row">手机号</th>
                    <td>${userinfo.phone!}</td>
                </tr>
                <tr>
                    <th scope="row">银行开户名</th>
                    <td><b>${Bankinfo.surName!}${Bankinfo.trueName!}</b></td>
                </tr>
                <tr>
                    <th scope="row">银行名称</th>
                    <td><b>${Bankinfo.cardBank!}</b></td>
                </tr>
                <tr>
                    <th scope="row">银行账号</th>
                    <td><b>${Bankinfo.cardNumber!}</b></td>
                </tr>
                <tr>
                    <th scope="row">支付宝账号</th>
                    <td>
                        <b>${Bankinfo.alipay!}</b>
                        <b onclick="receiptCode(this)" data="${Bankinfo.alipayPicture!}"
                           style="padding: 0 20px;float: right;cursor: pointer">查看收款码</b>
                    </td>
                </tr>
                <tr>
                    <th scope="row">微信账号</th>
                    <td>
                        <b>${Bankinfo.weChat!}</b>
                        <b onclick="receiptCode(this)" data="${Bankinfo.weChatPicture!}"
                           style="padding: 0 20px;float: right;cursor: pointer">查看收款码</b>
                    </td>
                </tr>
                <tr>
                    <th scope="row">付款金额</th>
                    <td><b>${orderinfo.transactionMoney!}</b></td>
                </tr>
                <tr>
                    <th scope="row">付款备注</th>
                    <td><b>${orderinfo.randomNum!}</b>(请务必填写)</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="margint20"></div>
    <hr/>
    <#if orderinfo.paymentTime  ??>
    <div class="margint20"><label>付款方式：</label>
        <#if orderinfo.paymentType == "Alipay">
           <span>支付宝</span>
         <#elseif orderinfo.paymentType == "WeChat" >
           <span>微信</span>
        <#else>
           <span>银行转账</span>
        </#if>
    </div>
    <div class="margint16" style="width: 100%;height: auto;">
        <label>付款凭证：</label>
        <ul class="uploadList">
           <#if orderinfo.img1  ?? && orderinfo.img1!="">
               <li><a href="${orderinfo.img1!}" target="_blank"><img src="${orderinfo.img1!}"/></a></li>
           </#if>
             <#if orderinfo.img2  ?? && orderinfo.img2!="">
                 <li><a href="${orderinfo.img2!}" target="_blank"><img src="${orderinfo.img2!}"/></a></li>
             </#if>
            <#if orderinfo.img3  ?? && orderinfo.img3!="">
                <li class="marginb16"><a href="${orderinfo.img3!}" target="_blank"><img src="${orderinfo.img3!}"/></a></li>
            </#if>
        </ul>
    </div>
    <div><label>付款时间：</label><span>${orderinfo.paymentTime?string("yyyy-MM-dd HH:mm:ss")}</span></div>
    </#if>
    <div class="center margint30" style="width: 100%">
        <button class="btn btn-gray btn-close">关闭</button>
    </div>
</div>
<!--页面中部内容结束-->


<!--收款码窗口-->
<style>
    #reDialog {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        display: none;
    }

    .codeDialog {
        width: 400px;
        height: 500px;
        position: absolute;
        top: 30%;
        left: 30%;
        margin-top: -100px;
        margin-left: -100px;
        background: #fff;
    }

    .code_close {
        position: absolute;
        width: 20px;
        height: 20px;
        top: -10px;
        right: -10px;
        background: #fff;
        border-radius: 100%;
        border: 2px solid #000;
    }

    .close_img {
        width: 10px;
        height: 10px;
        margin: 5px auto;
        background: url("../static/src/img/noticeClose.png") center no-repeat;
        background-size: 100% 100%;
    }

    .code {
        width: 100%;
        height: 100%;
    }

    .code img {
        width: 350px;
        height: 450px;
        margin: 25px;
    }
</style>
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


</body>
<script type="text/javascript" src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript"
        src="${ctx}/static/${version}/lib/exstatic/static/lib/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/${version}/lib/layer/layer.js"></script>
<script>
    $(".btn-close").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        window.parent.refreshtable;//访问父页面方法
        parent.layer.close(index);
    });

    function receiptCode(obj) {
        $("#otcimgId").attr('src', $(obj).attr('data'));
        $('#reDialog').show();
    }
</script>
</html>