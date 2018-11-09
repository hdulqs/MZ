<html  lang="en">
<head>
<#include "/base/base.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="" name="description">
<meta content="" name="author">
    <link rel="stylesheet" href="${ctx}/static/src/css/otc/otc.css?v=20180523">
    <style type="text/css">
        body {
            background:#fff;
            color:#666;
        }
        .canceltext{
            left: 735px;
            top: 1025px;
            width: 445px;
            height: 45px;
            line-height: 20px;
            color: rgba(229, 28, 35, 1);
            font-size: 14px;
            text-align: left;
            font-family: Roboto;

        }
    </style>
</head>

<body>
    <!-- 头部 -->
    <div class="mtitle">
        <h3>撤销订单</h3>
    </div>
    <div class="main-bd" style="padding:10px 20px; border:none;">
      <div class="mention">
        <div class="privilege">
            <div style="display: none">
                <input   id="otctransactionorderid" value="${transactionorderid!}" >
            </div>
          <table width="100%" class="tb-list2">
              <tbody>
                <tr>
                  <th scope="row">订单号</th>
                  <td>${orderinfo.transactionNum!}</td>
                </tr>
                <tr>
                  <th scope="row">交易对</th>
                  <td>${orderinfo.coinCode!}/CNY</td>
                </tr>
                <tr>
                  <th scope="row">总额</th>
                  <td>${orderinfo.transactionMoney!}CNY</td>
                </tr>
                <tr>
                  <th scope="row">操作</th>
                  <td><b>取消订单</b></td>
                </tr>
            </tbody>
          </table>
        </div>
      </div>
        <#if orderinfo.sellCustomId != user.customerId >
         <div class="canceltext">
              重要提示(必看)：撤单后交易取消，切勿再付款（如已付款切勿撤销），恶意撤销，将禁封交易
        </div>
        </#if>
        <#if orderinfo.sellCustomId != user.customerId >
        <div>
            <input id="confirmcancelflag" name="payment"     type="checkbox">
            <label for="confirmcancelflag">请确认：我确定我没付款，撤销后也不会再付款</label>
        </div>
        <#else >
           <div style="display: none">
               <input id="confirmcancelflag" name="payment"  checked type="checkbox">
               <label for="confirmcancelflag">请确认：我确定我没付款，撤销后也不会再付款</label>
           </div>
       </#if>
        <div class="center margint30" style="width: 100%">
            <button type="button" class="margint30 btn btn-primary btn-confirm">确定</button>
            <button type="button"  style="margin-left: 20px" class="margint30 btn btn-primary btn-otccancel">返回</button>
        </div>
    </div>
    <!--页面中部内容结束-->
    </body>
<script type="text/javascript" src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
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

    seajs.use(["js/front/main_news", "js/front/user/otcUndo"], function (m, otcUndo) {
        otcUndo.init();
    });


</script>
</html>