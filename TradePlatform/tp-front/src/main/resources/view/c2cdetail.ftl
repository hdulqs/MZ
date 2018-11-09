<html  lang="en">
<head>
<#include "/base/base.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="" name="description">
<meta content="" name="author">


<style type="text/css">
body { background:#FFF;  color:#666;}
.mtitle { font-size:24px; text-align:left;  }
.mtitle h3 { margin:0px; padding-left:20px; color:#666;   }
.privilege .tb-list2 {font-size:14px;}
.privilege .tb-list2 b { color:#f00;}
.privilege .tb-list2 th {border:1px solid #f3da91; background-color:#fffbef ; text-align:right; height:28px; line-height:28px; padding-right:15px;}
.privilege .tb-list2 td {border:1px solid #f3da91; background-color:#fff; text-align:left; height:28px; line-height:28px; padding-left:15px;}
.privilege .tb-list2 tr {height:40px;}
.privilege .do a { width:140px;}
.privilege .do a h4 { width:125px;}
a.alibtn_orange35 { background-color: #de211d; border-color: #de211d; padding: 0px 12px; height:35px; border-radius: 3px; transition: all .2s linear;  font-size: 16px;   color: #fff;  }
</style>
</head>

<body>
<!-- 头部 -->
<#if obj.transactionType==2>

<div class="mtitle">
<h3>收款订单</h3>
</div>
<div class="main-bd" style="padding:10px 20px; border:none;">
  <div class="mention">
    <div class="privilege">
      <h5>1.您的收款信息如下:</h5>
      <table width="100%" class="tb-list2">
 		<tbody>
     	<tr>
          <th scope="row">收款方户名</th>
          <td>${obj.bankowner!}</td>
        </tr>
      	<tr>
          <th scope="row">收款方开户行</th>
          <td>${obj.bankname!}</td>
        </tr>
        <tr>
          <th scope="row">收款方账号</th>
          <td>${obj.bankcard!}</td>
        </tr>
        <tr>
          <th scope="row">收账金额</th>
          <td><b>${obj.transactionMoney!}</b>  </td>
        </tr>
        
       <tr>
         <th scope="row">状态</th>
         <#if obj.status==1>
         <td>待支付&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <#elseif obj.status==2>
          <td>已完成&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <#else>
          <td>已否决&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         </#if>
       </tr>
        
      </tbody></table>
      <h5>2.卖家为认证商户，可放心等待收款；</h5>
      <h5>3.收款时请确认金额信息；</h5>
          <h5>4.卖家确认收到款后，自动充值。如超过24小时未收到款项，请向客服反馈解决。</h5>
      <p>&nbsp;</p>
		<hr/>      
      <div class="">
      <p>温馨提示：如有任何疑问请联系在线客服或查看帮助中心。</p>
      </div>

    </div>
  </div>
</div>
<!--页面中部内容结束-->
<#else>
<div class="mtitle">
<h3>汇款订单</h3>
</div>
<div class="main-bd" style="padding:10px 20px; border:none;">
  <div class="mention">
    <div class="privilege">
      <h5>1.请按提示信息向该卖家汇款；</h5>
      <table width="100%" class="tb-list2">
     		<tbody><tr>
          <th scope="row">收款方户名</th>
          <td>${obj.bankowner!}</td>
        </tr>
      	<tr>
          <th scope="row">收款方开户行</th>
          <td>${obj.bankname!}</td>
        </tr>
        <tr>
          <th scope="row">收款方账号</th>
          <td>${obj.bankcard!}</td>
        </tr>
        <tr>
          <th scope="row">转账金额</th>
          <td><b>${obj.transactionMoney!}</b>  </td>
        </tr>
        
           <tr>
             <th scope="row">汇款时备注内容</th>
             <td><b>${obj.randomNum!}</b> (请务必填写)</td>
           </tr>
           <tr>
             <th scope="row">状态</th>
             <#if obj.status==1>
             <td>待支付</td>
             <#elseif obj.status==2>
              <td>已完成</td>
             <#else>
              <td>已否决</td>
             </#if>
           </tr>
        
      </tbody></table>
      <h5>2.卖家为认证商户，可放心付款；</h5>
      <h5>3.汇款时请一定要填写备注信息；</h5>
          <h5>4.卖家确认收到款后，自动充值。如超过24小时未收到币，请向客服反馈解决。</h5>

      <p>&nbsp;</p>
		<hr/>      
      
      <div class="">
      <p>温馨提示：如有任何疑问请联系在线客服或查看帮助中心。</p>
      </div>

    </div>
  </div>
</div>
<!--页面中部内容结束-->
</#if>

</body></html>