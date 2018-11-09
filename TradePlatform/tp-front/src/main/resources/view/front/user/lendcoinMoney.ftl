<#import "/base/spring.ftl" as spring/>
<div class="container-fluid person-con">
	<div role="tabpanel">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" >
					<li role="presentation" class="active pull-left">
						<a href="#current" aria-controls="current" role="tab" data-toggle="tab"><@spring.message code="rongziguanli"/></a>
					</li>
				</ul>
			</div>
					
		</div>
		<div class="tab-content">
		   <div role="tabpanel" class="tab-pane active" id="current">
		       <div class="row">
				<!-- begin col-12 -->
					<div class="col-md-12 col-sm-12">
					 <select id="coinSelect" style="width:200px;margin-top:20px;">
								<#list list as list>
								<option value="${list.coinCode}">
									${list.coinCode}
								</option>
								</#list>
							</select>
						<ul class="fund-raise">
						  <!-- <li> <img src="./" width="35" height="35" style="display:inline-block;" alt=""/></li> -->
						  <li><label>杠杆倍数：</label><span class="red" id="lendTimes">${lendTimes} 倍</span></li>
						  <li><label>总资产</label><label id="coinCodeForRmb1">(${coinCodeForRmb})</label>：</label><span class="red" id="rMBSum">${rMBSum} </span></li>
						  <li><label>净资产</label><label id="coinCodeForRmb2">(${coinCodeForRmb})</label>：</label><span class="red" id="coinNetAsset">${coinNetAsset} </span></li>
						  <li><label>已借入</label><label id="coinCodeForRmb3">(${coinCodeForRmb})</label>：</label><span class="red" id="rMBLendMoneyed">${rMBLendMoneyed} </span></li>
						  <li><label>可借入</label><label id="coinCodeForRmb4">(${coinCodeForRmb})</label>：</label><span class="theme-color" id="canLendMoney">${canLendMoney} </span></li>
												</ul>
						
					</div>
				<!-- end col-12 -->
			  <form  name="lendFrom" id="lendFrom"  >
			  	   <div class="toloanBody col-md-12 col-sm-12" style="padding-left:190px;">
						<div class="loanText">
							<ul>
								<li>
									<span class="title"><span id="selectCoinCode">${coinCodeForRmb}</span>:<@spring.message code="jieru"/>：</span>
									<span>
									<input type="hidden" id="canLendMoney"  value="${canLendMoney}"/>
										<input class="loanPriceInput" id="lendmoneycount" type="text" >
									    <span style="color:red" id="lendmoneycount_message" ></span>
									</span>
									<p class="daliyRade red">
										<span><@spring.message code="rihualilv"/>: </span>
										<span class="" style="display: ">${lendRates}%</span>
									</p>
								</li>
							</ul>
						</div>
						
						<div style="margin-left:120px;margin-top: 20px;">
							<label style="cursor: pointer;">
								<span style="display:inline-block;">
								   <span style="float:left;"><input id="checkyuedu" type="checkbox"></span><span style="float:none;margin-left:10px;color: #767676;"> <@spring.message code="woyiyuedu"/>
									<a href="javascript:;" data-toggle="modal" data-target="#reg_pro"><@spring.message code="ydbtyrzrbywyhsyxie"/></a>
								   </span>
								</span>
							</label>
						</div>
                       <div class="modal fade ng-scope" id="reg_pro" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                           <div class="modal-dialog" style="z-index:9999">
                               <div class="modal-content  p-0 bg-dark" style="height:500px;overflow-y:auto;">
                                   <div class="modal-header">
                                       <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                                       <h4 class="modal-title"><@spring.message code="xieyi111"/></h4>
                                   </div>
                                   <div class="modal-body">
                                       <p class="text-left">   ${protocol}
                                       </p>
                                   </div>
                                   <div class="modal-footer">
                                       <button type="button" class="btn btn-default" data-dismiss="modal"><@spring.message code="guanbi"/></button>
                                   </div>
                               </div>
                           </div>
                       </div>
						<div class="loanSubmit">
							<input class="buttonLoanGreen" style="cursor:pointer"  value="<@spring.message code='tijiao'/>" type="button" id="lendSubmit">
						</div>
				     </div>
					</form>
		        </div>
			</div>
			<!---->
		</div>
	</div>

</div>
 <div class="container-fluid person-con">
				<!-- begin page-header -->
				<div class="row" style="margin-bottom:15px;">
					<div class="panel_wrap_head wrap_head">
						<div class="">
							<ul class="wrap_tabs" style="width:100%;">
								<li role="presentation" class="active pull-left" style="width:100%;">
									<a href="javascript:void(0);"><@spring.message code="ganganjilu"/>333 </a>
									<span id="netAsseToLend" class="pull-right">风险比例：<label class="red">(${netAsseToLend}%)</label></span>
								</li>
	
							</ul>
						</div>
					</div>
				</div>
				<div class="row" >
		<!-- begin col-3 -->
			<div class="col-sm-12">
				<table   id="table"
		 	           data-show-refresh="false"
		 	           data-show-columns="false"
		 	           data-show-export="false"
		 	           data-search="false"
		 	           data-detail-view="false"
		 	           data-minimum-count-columns="2"
		 	           data-pagination="true"
		 	           data-id-field="id"
		 	           data-page-list="[10, 25, 50, 100, ALL]"
		 	           data-show-footer="false"  
		 	           data-side-pagination="server"
		 	           >
		 	    </table>
			</div>
			<!---->
		</div>
		<!-- end col-3 -->
	</div>
<!-- 	融资记录弹窗	 -->		
	<div id="layer-record" class="layer-record Commission" style="display:none;">
	  <table>
	   <thead>
	    <tr>
	     <th><@spring.message code="leixing"/> </th>
	     <th><@spring.message code="benxi"/> </th>
	     <th><@spring.message code="huankuanriqi"/></th>
	    </tr>
	   </thead>
	   <tbody id="tbodyrecord">
	
	   </tbody>
	  </table>
	</div>
<!-- 融资记录弹窗end -->

<!-- 	融资记录弹窗	 -->		
	<div id="repayment" class="layer-record Commission" style="display:none;">
       
        <ul>
	   <li class="col-xs-6"><label><@spring.message code="zongjiekuan"/>：</label><span id="LendCount">0.2222</span></li>
	   <li class="col-xs-6"><label><@spring.message code="weichanghuanbenjin"/>：</label><span id="notRepayLendCount">0.221</span></li>
	   <li class="col-xs-6"><label><@spring.message code="keyongbenjin"/>：</label><span id="balance">1.21</span></li>
	    <li class="col-xs-6"><label><@spring.message code="weihuanlixi"/>：</label><span  id="notInterest">0.000006</span></li>
	  </ul>
	  <div>
	   <div class="repayType">
	    <span class="col-xs-6" style="text-align:center;"><input type="radio" checked name="repaid" htmlType="part"/><@spring.message code="bufenchanghuang"/></span>
	    <span class="col-xs-6"><input type="radio" name="repaid" htmlType="all"/><@spring.message code="quanbuchanghuan"/></span>
	   </div>
	    <div class="part-con col-md-offset-2">
	      <input type="hidden" id="repaymentid" >  </input>
	      <input type="hidden" id="repaymentType" value="part">  </input>
	      <label class="fl" style="line-height:30px;"><@spring.message code="changhuanbenjin"/>：</label><input type="text" id="repaymentMoney"/>
	    </div>
	    <div class="pop_submit"><input class="btn btn-info btn-lg col-xs-8 col-md-offset-2" style="float:none;font-size:16px;" type="button" value="<@spring.message code='tijiao'/>" id="repaymentSubmit"></div>
	  </div>
       
	</div>
<!-- 融资记录弹窗end -->
   </div>

<script>
  $(function(){
	  $('.check').on('click',function(){
		  layer.open({
			  type: 1,
			  title :'融资记录',
			  area: '500px',
			  content: $('#layer-record'),
			  cancel: function(){ 
				  $('#layer-record').hide();
				  
				}    
		  })
	  })
  })
</script>

<script type="text/javascript">
seajs.use(["js/front/user/lendcoinMoney","js/front/user/common"],function(rmb){
	rmb.init();
});
</script>