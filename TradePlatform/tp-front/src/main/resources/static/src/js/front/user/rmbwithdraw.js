define(function(require,exports,module){
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	require("js/base/secondvail");
	this.firstvail = require("js/base/firstvail");
	
	module.exports = {
		init : function(){
			//清除定时器
			clearPageTimer();
			
			$(".verifyLayout").hide();
			$(".verifyLayout1").hide();
			$(".verifyLayout2").hide();

            $('.dialog-close').on('click',function(){
                $(this).parent().parent().hide()
            })
			
	
			//跳转管理银行卡界面
			$("#manageBank").on("click",function(){
				loadUrl(_ctx+"/user/bankcard/index");
			})
			
			/*$("#transactionMoney").on("input",function(){
				
				var okhotMoney = $("#okhotMoney").text();
				
				var transactionMoney = $("#transactionMoney").val();
				var maxWithdrawMoneyOneTime=$("#maxWithdrawMoneyOneTime").val()
				var i, j, strTemp;
			    strTemp = "0123456789";
				if(parseInt(transactionMoney)>parseInt(okhotMoney)){
					 $("#transactionMoney_message").html(bukedayutixianjine);
					return false;
				}else if(parseInt(transactionMoney)>parseInt(maxWithdrawMoneyOneTime)){
					layer.msg(danbibuchao+maxWithdrawMoneyOneTime, {icon: 2});
					return false;
				}else if(parseInt(transactionMoney)>(parseInt(maxWithdrawMoney) - parseInt(oldMoney))){
					layer.msg(dangtiantixianbukechaoguo+maxWithdrawMoney, {icon: 2});
					return false;
				}
				else{

		        	 if(!validate.isNumber(transactionMoney)){
		        		 
		        		 $("#transactionMoney_message").html(jinebixuweishuzi);
		        		 return false;
		        	 }else{
		        		 $("#transactionMoney_message").html("");
		        		 var actualMoney = parseInt(transactionMoney) * $("#onlineWithdrawFeeRate").val()/100;
		        		 var actualMoney_b = parseInt(transactionMoney) - actualMoney;
		        		 $("#showWithdrawMoney").empty().html(shouxufeie+actualMoney+"RMB, "+shijidaozhangjine+actualMoney_b+"RMB<br>"+dangtianzuiduo+$("#maxWithdrawMoney").val()+"RMB<br>"+danbizuiduo+$("#maxWithdrawMoneyOneTime").val()+"RMB");
		        	 }
		         
				}
			})

			$("#transactionMoney").bind("input", function() {
				var oldMoney = $("#oldMoney").val();
				var firstHotspan = $("#firstHotspan").text();
				var inputNumWit = $("#transactionMoney").val();
				var maxWithdrawMoney=$("#maxWithdrawMoney").val();
				var i, j, strTemp;
				strTemp = "0123456789";
				if (parseInt(inputNumWit) < parseInt($("#leastPaceNum").html())) {
					layer.msg(bukexiaoyudanbizuixiaotibuedu, {
						icon : 2
					});
					return false;
				}
				if (parseInt(inputNumWit) > parseInt(firstHotspan)) {
					layer.msg(bukedayutixianshuliang, {icon : 2});
					return false;
				}
				if (parseInt(inputNumWit) > (parseInt(maxWithdrawMoney)- parseInt(oldMoney))) {
					layer.msg(tx_gt_today_max + maxWithdrawMoney, {icon : 2});
					return false;
				}
				
				if(!validate.isNumber(inputNumWit)){
	        		 $("#btcNum_message").html(tixianshuliangbixuweishuzi);
	        		 return false;
	        	}
				else{
	        		$("#btcNum_message").html("");
					var actualMoney = parseInt(inputNumWit) * $("#onat").text() / 100;
					var actualMoney_b = parseInt(inputNumWit) - actualMoney;
					$("#shouxufei").html(actualMoney);
					$("#shijidao").html(actualMoney_b);
					$("#coincode3").hide();
					$("#coincode4").hide();
				}
			})*/
			
			
			//提交
			$("#submitWithdraw").click(function(){
				debugger 
				var transactionMoney = $("#transactionMoney").val();
				var accountPassWord = $("#accountPassWord").val();
				var custromerAccountNumber =  $("#custromerAccountNumber").val();
				var withdrawCode = $("#withdrawCode").val();
				var okhotMoney = $("#okhotMoney").text();
				var transactionMoney = $("#transactionMoney").val();
				var maxWithdrawMoneyOneTime=$("#maxWithdrawMoneyOneTime").val()
				
				var inputNumWit = $("#transactionMoney").val();        
				
				var i, j, strTemp;
			    strTemp = "0123456789";
			    
			    if(parseInt(transactionMoney)<0 || parseInt(transactionMoney)==0){
			    	layer.msg(tixianjinebunengwei0, {icon: 2});
					return false;
			    }
				if(parseInt(transactionMoney)>parseInt(okhotMoney)){
					layer.msg(bukedayutixianjine, {icon: 2});
					return false;
				} 

				if (parseInt(inputNumWit) > parseInt(maxWithdrawMoney)) {
					layer.msg(tx_gt_today_max, {icon : 2});
					return false;
				}
				if(!custromerAccountNumber){
					layer.msg(qingxuanzeyinghangka,{icon:2});
					return false;
				}
				if(!transactionMoney){
					layer.msg(tixianjinebuweikong,{icon:2});
					return false;
				}
				if(!validate.isNumber(transactionMoney)){
	        		 layer.msg(jinebixuweishuzi,{icon:2});
	        		 return false;
		         }
				if(parseInt(transactionMoney)>parseInt(maxWithdrawMoneyOneTime)){
						layer.msg(danbibuchao+maxWithdrawMoneyOneTime, {icon: 2});
						return false;
					}
				/*if(!accountPassWord){
					layer.msg("交易密码不能为空!",{icon:2});
					return false;
				}*/
			/*	if(!withdrawCode){
					layer.msg("短信验证码不能为空!",{icon:2});
					return false;
				}*/
				
				
				
				//$("#submitWithdraw").attr("disabled",true);
				//开始
				$.ajax({
					type : "post",
					url : _ctx + "/sencodvail",
					data : {
						type:"6"
					},
					cache : false,
					dataType : "json",
					success : function(data) {debugger
						if(data){
							if(data.success){
								var phone =data.obj.phoneState;
								var google=data.obj.googleState;
								
								if(google==1&&phone==0){
									
									$(".verifyLayout1").show();
									
								}else if(google==0&&phone==1){
									
									$(".verifyLayout").show();

								}else if(google==1&&phone==1){
									
									$('#mobile-form').css('display','none');
									$(".verifyLayout2").show();
									 $('.verify-form1').hide();
									 $('.btns').find('span').on('click',function(){
								      var ind=$(this).index();
								      $(this).addClass('cur').siblings().removeClass('cur');
								      if(ind==0){
								          $('.verify-form').show();
								          $('.verify-form1').hide();
								      }else{
								    	  $('.verify-form1').show();
								    	  $('.verify-form').hide();
								      }
								    })
								
								}else{
									firstvail.sendvail("rmbwithdraw");
									//window.location.href = _ctx+"/user/center";
								}
							}else{
								
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							
							layer.msg(data.msg, {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});//结束
			})
			
			
			
	
			
			
			$("#sendBtn,#sendBtn1").on("click",function(){
			var username=	$("#username").val();
			$(this).attr("disabled","disabled");
			$(this).html(yifasong);
			$.ajax({
				type : "post",
				url : _ctx + "/sms/smsPhone",
				data:{username:username},
				cache : false,
				dataType : "json",
				success : function(data) {
					if(data){
						if(data.success){
							layer.msg(fasongchenggong, {icon: 1})
							
							var time = 120;
							window.clearInterval(pageTimer["login"]);
							// 开启点击后定时数字显示
							pageTimer["login"] = window.setInterval(function() {
								time = time - 1;
								if (time == 0) {
//								$("#sendBtn1").html("点击");
								$("#sendBtn1").removeAttr("disabled");// 按钮可用
								$("#sendBtn1").html(chongxinfasong);// 按钮可用
//								$("#sendBtn").html("点击");
								$("#sendBtn").removeAttr("disabled");// 按钮可用
								$("#sendBtn").html(chongxinfasong);// 按钮可用
//							    clearInterval(pageTimer["setapw"]);
							    clearInterval(pageTimer["login"]);
								} else {
									$("#sendBtn").html(time+miaochongxinfasong );
									$("#sendBtn1").html(time+miaochongxinfasong );
								}

							}, 1000);
							
						}else{
							$("#sendBtn").removeAttr("disabled");// 按钮可用
							$("#sendBtn1").removeAttr("disabled");// 按钮可用
							layer.msg(data.msg, {icon: 2})
						}
					}else{
						$("#sendBtn1").removeAttr("disabled");// 按钮可用
						$("#sendBtn").removeAttr("disabled");// 按钮可用
						
						layer.msg(fasongshibai, {icon: 2})
					}
				},
				error : function(e) {
					
				}
			});
			
		});
			
			
			
			//分页bootstrapTable插件
			var conf = {

				url : _ctx + "/user/rmbdeposit/list",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : "id",
					visible : false,
					searchable : false
				},
				{
					title : 'id',
					field : 'id',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				},
				/*{
					title : leixing,
					field : 'transactionType',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//1线上充值,2线上提现 3线下充值 4线下取现
						if(value==1){
							return xianshangchongzhi;
						}else if(value==2){
							return xiashangtixian;
						}else if(value==3){
							return xianxiatixian;
						}else if(value==3){
							return xianxiaquxian;
						}
					}
				},*/
				{
					title : tixianjine,
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : shouxufei,
					field : 'fee',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : dingdanhao,
					field : 'transactionNum',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : shijian,
					field : 'created_long',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value,row,index){
						return TimestampFormat('Y-m-d H:i:s', value/1000);
					}
				},
				{
					title : zhaungtai,
					field : 'status',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//1待审核 2已完成 3以否决
						if(value==1){
							return daishenhe;
						}else if(value==2){
							return yiwancheng;
						}else if(value==3){
							return yifoujue;
						}
					}
				},
				{
					title : bohuiyuanyin,
					field : 'rejetionReason',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//1待审核 2已完成 3已否决
						if(row.rejectionReason!=null){
							return row.rejectionReason;
						}else{
							return '-';
						}
					}
				}
				],
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        status:$($("#status").find(".selected")[0]).attr("value"),
				        transactionType:"tixian"
				    };
				}
			}
			 _table.initTable($("#table"), conf);
			
			//充值筛选条件
			$("#status").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table").bootstrapTable('refresh',null);
			})
			
		},
		//发送短信
		sendsms :function(){
			$("#sendsmsBtn").on("click",function(){
				var transactionMoney = $("#transactionMoney").val();
				var accountPassWord = $("#accountPassWord").val();
				var custromerAccountNumber =  $("#custromerAccountNumber").val();
				
				if(!custromerAccountNumber){
					layer.msg("请选择银行卡!",{icon:2});
					return false;
				}
				if(!transactionMoney){
					layer.msg("提现金额不能为空!",{icon:2});
					return false;
				}
				if(!accountPassWord){
					layer.msg("交易密码不能为空!",{icon:2});
					return false;
				}
			
				
				
				$(this).attr("disabled","disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getRmbWithdrawCode",
					data : {
						accountPassWord : md5.md5(accountPassWord),
						transactionMoney : transactionMoney,
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data){
							if(data.success){
								layer.msg("发送成功", {icon: 1})
								
								var time = 120;
								window.clearInterval(pageTimer["rmbwithdraw"]);
								// 开启点击后定时数字显示
								pageTimer["rmbwithdraw"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html("发送验证码");
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(pageTimer["rmbwithdraw"]);
									} else {
										$("#sendsmsBtn").html(time+"秒后重新发送" );
									}

								}, 1000);
								
							}else{
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {icon: 2})
							}
						}else{
							$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
							layer.msg("发送失败", {icon: 2})
						}
					},
					error : function(e) {
						
					}
				});
				
			});
		}
	}
});