<#import "/base/spring.ftl" as spring/>
<div class="container-fluid person-con">
	<div role="tabpanel">
		<div class="panel_wrap_head wrap_head">
			<div class="">
				<ul class="wrap_tabs" role="tablist" >
					<li role="presentation" class="active pull-left">
						<a href="#current" aria-controls="current" role="tab" data-toggle="tab"><@spring.message code="gangganbeishuguanli"/></a>
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
						  <!-- <li> <img src="./" width="35" height="35" style="display:inline-block;" alt=""/></li> -->
						    <li><label><@spring.message code="ganganbeishu"/>：</label><span class="red" id="lendTimes">${lendTimes} <@spring.message code="beishu"/></span></li>
						  <li><label><@spring.message code="zongzichang"/>(${coinCodeForRmb})：</label><span class="red" id="rMBSum">${rMBSum} </span></li>
						  <li><label><@spring.message code="jinzichang"/>(${coinCodeForRmb})：</label><span class="red" id="coinNetAsset">${coinNetAsset} </span></li>
						  <li><label><@spring.message code="yijieru"/>(${coinCodeForRmb})：</label><span class="red" id="rMBLendMoneyed">${rMBLendMoneyed} </span></li>
						  <li><label><@spring.message code="kejieru"/>(${coinCodeForRmb})：</label><span class="theme-color" id="canLendMoney">${canLendMoney} </span></li>
						</ul>
					</div>
				<!-- end col-12 -->
			  <form  name="lendFrom" id="lendFrom"  >
			  	   <div class="toloanBody col-md-12 col-sm-12" style="padding-left:190px;">
						<div class="loanText">
							<ul>
								<li>
									<span class="title" style="width:140px;"><@spring.message code="sqbgggbs"/>：</span>
									<span>
										<input type="hidden" id="canLendMoney"  value="${canLendMoney}"/>
										<input type="hidden" id="oldLendTimes"  value="${lendTimes}"/>
										<select class="loanPriceInput" id="lendmoneycount">
											<#list mutiple as m>
											    <option <#if m==lendTimes>selected</#if> value="${m}">${m}</option>
											</#list>
										</select>
										<#--<input class="loanPriceInput" id="lendmoneycount" type="text">-->
										<span style="color:red" id="lendmoneycount_message" ></span>
									</span>
								</li>
							</ul>
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
							<ul class="wrap_tabs">
								<li role="presentation" class="active pull-left">
									<a href="javascript:void(0);"><@spring.message code="shenqingjilu"/></a>
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

   </div>



<script type="text/javascript">
seajs.use(["js/front/user/lendTimesApply","js/front/user/common"],function(rmb){
	rmb.init();
});
</script>