<#import "/base/spring.ftl" as spring/>
<div class="container-fluid person-con">
	<div role="tabpanel">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" >
					<li role="presentation" class="active pull-left">
						<a href="#current" aria-controls="current" role="tab" data-toggle="tab">融资管理</a>
					</li>
				</ul>
			</div>
					
		</div>
		<div class="tab-content">
		   <div role="tabpanel" class="tab-pane active" id="current">
		       <div class="row">
				<!-- begin col-12 -->
					<div class="col-md-12 col-sm-12">
						<ul class="fund-raise">
						  <li> <img src="./" width="35" height="35" style="display:inline-block;" alt=""/></li>
						  <li><label>杠杆倍数：</label><span class="red" id="lendTimes">${lendTimes} 倍</span></li>
						  <li><label>总资产</label><label id="coinCodeForRmb1">(${coinCodeForRmb})</label>：</label><span class="red" id="rMBSum">${rMBSum} </span></li>
						  <li><label>净资产</label><label id="coinCodeForRmb2">(${coinCodeForRmb})</label>：</label><span class="red" id="coinNetAsset">${coinNetAsset} </span></li>
						  <li><label>已借入</label><label id="coinCodeForRmb3">(${coinCodeForRmb})</label>：</label><span class="red" id="rMBLendMoneyed">${rMBLendMoneyed} </span></li>
						  <li><label>可借入</label><label id="coinCodeForRmb4">(${coinCodeForRmb})</label>：</label><span class="theme-color" id="canLendMoney">${canLendMoney} </span></li>
						</ul>
						<ul class="fund-raise">
						  <li>
							  <select id="coinSelect" >
								<#list list as list>
								<option value="${list.coinCode}">
									${list.coinCode}
								</option>
								</#list>
							</select>
							</li>
						  <li>
						  </ul>
					</div>
				<!-- end col-12 -->
			  <form  name="lendFrom" id="lendFrom"  >
			  	   <div class="toloanBody col-md-12 col-sm-12" style="padding-left:190px;">
						<div class="loanText">
							<ul>
								<li>
									<span class="title"><label id="coinCodeForRmb5">(${coinCodeForRmb})</label>借入：</span>
									<span>
									<input type="hidden" id="canLendMoney"  value="${canLendMoney}"/>
										<input class="loanPriceInput" id="lendmoneycount" type="text" placeholder="请输入">
									    <span style="color:red" id="lendmoneycount_message" ></span>
									</span>
									<p class="daliyRade red">
										<span>当前最优日费率: </span>
										<span class="" style="display: ">${lendRates}%</span>
									</p>
								</li>
							</ul>
						</div>
						
						<div style="margin-left:120px;margin-top: 20px;">
							<label style="cursor: pointer;">
								<span style="display:inline-block;">
								   <span style="float:left;"><input type="checkbox"></span><span style="float:none;margin-left:10px;color: #767676;"> 阅读并同意
									<a target="_blank"  href="">《融资融币业务用户使用协议》</a>
								   </span>
								</span>
							</label>
						</div>
						<div class="loanSubmit">
							<input class="buttonLoanGreen" style="cursor:pointer"  value="立即借款" type="button" id="lendSubmit">
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
							<ul class="wrap_tabs">
								<li role="presentation" class="active pull-left">
									<a href="javascript:void(0);">融资记录</a>
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
	     <th>还款日期</th>
	     <th>本息</th>
	     <th>还款状态</th>
	    </tr>
	   </thead>
	   <tbody>
	    <tr>
	     <td>2018-12-02</td>
	     <td>80000</td>
	     <td>已还款</td>
	    </tr>
	   </tbody>
	  </table>
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