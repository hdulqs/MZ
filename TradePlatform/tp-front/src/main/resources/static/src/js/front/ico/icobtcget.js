define(function(require, exports, module) {
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5")

	module.exports = {
		init : function() {
			// 清除定时器
			clearPageTimer();

			$("#coinSelect").on("change", function() {
				var selectValue = $(this).find("option:selected").val();
				var split = selectValue.split(",");
				$("#availableCTC").val(split[1]);
				$("#frozenCTC").val(split[2]);
				$("#publicKey").empty().text(split[3]);
				$("#coinType").val(split[4]);
				$("#firstHotspan").empty().text(split[1]);

				$("#coincode1").empty().text(split[4]);
				$("#coincode2").empty().text(split[4]);
				$("#coincode3").empty().text(split[4]);
				$("#coincode4").empty().text(split[4]);
				$("#paceFeeRate").empty().text(split[5]);
				$("#leastPaceNum").empty().text(split[6]);
				$("#oneDayPaceNum").empty().text(split[7]);
				
				$.ajax({
					type : "post",
					url : _ctx + "/user/btc/findPublicKey",
					dataType : "JSON",
					data : {
						coinCode : split[4],
					},
					success : function(data) {
						$("#btcKey").empty();
						for(var i = 0 ; i < data.length; i++){
							$("#btcKey").append("<option value=\""+data[i].publicKey+"\">"+data[i].publicKey+"</option>");
						}
					}
				})
				
			})

			// 添加提现账户
			$("#addWaccount").on("click", function() {
				loadUrl(_ctx + "/user/btc/get");
			})

			$("#inputNumWit").bind("input", function() {
				var firstHotspan = $("#firstHotspan").text();
				var inputNumWit = $("#inputNumWit").val();
				var i, j, strTemp;
				strTemp = "0123456789";
				if (parseInt(inputNumWit) < parseInt($("#leastPaceNum").html())) {
					layer.msg('不可小于单笔最小提币额度', {
						icon : 2
					});
					return false;
				}
				if (parseInt(inputNumWit) > parseInt(firstHotspan)) {
					layer.msg('不可大于可提现数量', {
						icon : 2
					});
					return false;
				}
				if(!validate.isNumber(inputNumWit)){
	        		 $("#btcNum_message").html("提现数量必须为数字");
	        		 return false;
	        	}else{
	        		 $("#btcNum_message").html("");
					var actualMoney = parseInt(inputNumWit) * $("#paceFeeRate").html() / 100;
					var actualMoney_b = parseInt(inputNumWit) - actualMoney;
					$("#shouxufei").html(actualMoney);
					$("#shijidao").html(actualMoney_b);
				}
			})

			$("#oktx").on("click", function() {
				var coinType = $("#coinType").val();
				var withdrawCode = $("#withdrawCode").val();
				var accountPassWord = $("#accountPassWord").val();
				var currencyType = $("#currencyType").val();
				var btcNum = $("#inputNumWit").val();
				var btcKey = $("#btcKey").val();
				$("#oktx").attr("disabled", true);
				$.ajax({
					type : "post",
					url : _ctx + "/ico/recandwit/getIco",
					dataType : "JSON",
					data : {
						coinType : coinType,
						withdrawCode : withdrawCode,
						accountPassWord : md5.md5(accountPassWord),
						currencyType : currencyType,
						btcNum : btcNum,
						btcKey : btcKey
					},
					success : function(data) {
						if(data!=undefined&&data.success){
							layer.msg("申请成功",{icon:1},function(){
								loadUrl(_ctx + "/ico/recandwit/icojump")
							});
							
						}else{
							layer.msg(data.msg, {icon : 2});
							$("#oktx").attr("disabled", false);
							$('#table').bootstrapTable('refresh');
						}
					}
				})
			})

			// 分页bootstrapTable插件
			var conf = {
				url : _ctx + "/ico/recandwit/list?transactionType=2",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : "id",
					visible : false,
					searchable : false
				}, {
					title : 'id',
					field : 'id',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				}, {
					title : '订单号',
					field : 'transactionNum',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				/*
				 * formatter:function(value,row,index){ //1线上充值,2线上提现 3线下充值
				 * 4线下取现 if(value==1){ return "线上充值"; }else if(value==2){ return
				 * "线上提现"; }else if(value==3){ return "线下充值"; }else
				 * if(value==3){ return "线下取现"; } }
				 */
				}, {
					title : '数量',
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : '手续费',
					field : 'fee',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : '数量',
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : '时间',
					field : 'created',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				}, {
					title : '状态',
					field : 'status',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value, row, index) {
						// 1待审核 2已完成 3已否决
						if (value == 1) {
							return "待审核";
						} else if (value == 2) {
							return "已完成";
						} else if (value == 3) {
							return "已否决";
						}
					}
				} ],
				queryParams : function queryParams(params) {
					return {
						limit : params.limit,
						offset : params.offset,
						sortOrder : params.order,
						type : $($("#type").find(".selected")[0]).attr("value"),
						status : $($("#type").find(".selected")[0]).attr("value")
					};
				}
			}
			_table.initTable($("#table"), conf);
			// 筛选条件
			$("#type").on("click", "a", function() {
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table").bootstrapTable('refresh', null);
			})
		},
		// 发送短信
		sendsms : function() {
			$("#sendsmsBtn").on("click", function() {
				var username = $("#username").val();
				var inputNumWit = $("#inputNumWit").val();
				var accountPassWord = $("#accountPassWord").val();
				var withdrawCode = $("#withdrawCode").val();
				$(this).attr("disabled", "disabled");
				$.ajax({
					type : "post",
					url : _ctx + "/sms/getWithdrawCoinCode",
					data : {
						username : username,
						accountPassWord : md5.md5(accountPassWord),
						inputNumWit : inputNumWit,
						withdrawCode : withdrawCode
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if (data) {
							if (data.success) {
								layer.msg("发送成功", {
									icon : 1
								})

								var time = 120;
								window.clearInterval(pageTimer["btcget"]);
								// 开启点击后定时数字显示
								pageTimer["btcget"] = window.setInterval(function() {
									time = time - 1;
									if (time == 0) {
										$("#sendsmsBtn").html("发送验证码");
										$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
										window.clearInterval(pageTimer["btcget"]);
									} else {
										$("#sendsmsBtn").html(time + "秒后重新发送");
									}

								}, 1000);

							} else {
								$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
								layer.msg(data.msg, {
									icon : 2
								})
							}
						} else {
							$("#sendsmsBtn").removeAttr("disabled");// 按钮可用
							layer.msg("发送失败", {
								icon : 2
							})
						}
					},
					error : function(e) {

					}
				});

			});
		}
	}
})