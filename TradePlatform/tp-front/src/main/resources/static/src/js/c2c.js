define(function(require, exports, module) {
    this.md5 = require("js/base/utils/hrymd5");

	
	module.exports = {
		/**
		 * 查看订单详细信息
		 */
		getdeatil  : function(){
			
			//查看订单详细
			$("#billDetail").on("click","a[transactionnum]",function(){
				var transactionnum = $(this).attr("transactionnum");
				layer.open({
					  title: false,
					  type: 2,
					  shadeClose: false,
					  move : false,
					  scrollbar  : false,
					  shade: 0.8,
					  area: ['33%', '70%'],
					  content: _ctx+"/user/getc2cTransaction/"+transactionnum
				}); 
			})
			
			//支付完成
			$("#billDetail").on("click","a[transactionnum1]",function(){
				var transactionnum = $(this).attr("transactionnum1");
				layer.confirm('确认支付!', {
					  btn: ['确认','取消'] //按钮
					}, function(){
					    
						$.ajax({
							type : "post",
							url : _ctx+"/user/payc2cTransaction/"+transactionnum,
							cache : false,
							dataType : "json",
							success : function(data) {
								if(data){
									if(data.success){
										window.location.reload()
									}else{
										layer.msg(data.msg);
									}
								}
							},
							error : function(e) {
							}
						});
						
						
					}, function(){
						layer.close();
				});
			})
			
			//确认到账
			$("#billDetail").on("click","a[transactionnum4]",function(){
				var transactionnum = $(this).attr("transactionnum4");
				layer.confirm('确认收到款了吗!', {
					  btn: ['确认','取消'] //按钮
					}, function(){
					    
						$.ajax({
							type : "post",
							url : _ctx+"/user/confirm/"+transactionnum,
							cache : false,
							dataType : "json",
							success : function(data) {
								if(data){
									if(data.success){
										window.location.reload()
									}else{
										layer.msg(data.msg);
									}
								}
							},
							error : function(e) {
							}
						});
						
						
					}, function(){
						layer.close();
				});
			})
			
			
			
			//交易失败
			$("#billDetail").on("click","a[transactionnum2]",function(){
				var transactionnum = $(this).attr("transactionnum2");
				var remark = $("#failRemark").html();
				$('#faildiv').removeClass("hide");
				layer.open({
					  title: false,
					  type: 1,
					  shadeClose: false,
					  move : false,
					  scrollbar  : false,
					  shade: 0.8,
					  area: ['400px', '200px'],
					  content:$('#faildiv'),
					  btn: ['确定'],
					  btnAlign: 'c',
				      yes: function(index, layero){
				    	 
				    	 $.ajax({
								type : "post",
								url : _ctx+"/user/failc2cTransaction/"+transactionnum,
								cache : false,
								dataType : "json",
								data :{
									remark : remark
								},
								success : function(data) {
									if(data){
										if(data.success){
											window.location.reload()
										}else{
											layer.msg(data.msg);
										}
									}
								},
								error : function(e) {
								}
						});
				    	 
				     },
				     cancel : function(index){
				    	 $('#faildiv').addClass("hide");
				    	 layer.closeAll();
				     }
				}); 
			
			})
			
			//关闭交易
			$("#billDetail").on("click","a[transactionnum3]",function(){
				var transactionnum = $(this).attr("transactionnum3");
				
				
				$('#closediv').removeClass("hide");
				layer.open({
					  title: false,
					  type: 1,
					  shadeClose: false,
					  move : false,
					  scrollbar  : false,
					  shade: 0.8,
					  area: ['400px', '200px'],
					  content:$('#closediv'),
					  btn: ['确定'],
					  btnAlign: 'c',
				      yes: function(index, layero){
				    	  var remark = $("#closeRemark").val();
				    	  debugger
				    	 $.ajax({
								type : "post",
								url : _ctx+"/user/closec2cTransaction/"+transactionnum,
								cache : false,
								dataType : "json",
								data :{
									remark : remark
								},
								success : function(data) {
									if(data){
										if(data.success){
											window.location.reload()
										}else{
											layer.msg(data.msg);
										}
									}
								},
								error : function(e) {
								}
						});
				    	 
				     
				    	 
				     },
				     cancel : function(index){
				    	 $('#closediv').addClass("hide");
				    	 layer.closeAll();
				     }
				}); 
			})
			
		},
		//添加页面提交方法
		init : function(){
			
			var formartInput = function(obj){

				obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  
				obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的  
				obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
				obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数  
				if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额 
					obj.value= parseFloat(obj.value); 
				} 
			
			}
			
			$("#buyNumber").on("input",function(){
				
				formartInput(this);
				
				var count = 0;
				if(this.value!=""){
					count = parseFloat(this.value).toFixed(2);
				}
				
				var price = 0;
				if($("#buyUnitPrice").val()!=""){
					price = parseFloat($("#buyUnitPrice").val()).toFixed(2);
				}
				
				$($("#buyfinish").find("span")[0]).html(Math.floor(count*price*100)/100);
			});
			$("#sellNumber").on("input",function(){
				
				formartInput(this);

				var count = 0;
				if(this.value!=""){
					count = parseFloat(this.value).toFixed(2);
				}
				
				var price = 0;
				if($("#sellUnitPrice").val()!=""){
					price = parseFloat($("#sellUnitPrice").val()).toFixed(2);
				}
				
				$($("#sellfinish").find("span")[0]).html(Math.floor(count*price*100)/100);
			
			});
			
			
			 //买
			 $("#buyBtn").on("click",function(){
				 	var username = $("#username").val();
				 	if(username==undefined||username==""){
				 		layer.alert('请先登录后再进行交易', {
				 			  skin: 'layui-layer-molv' //样式类名
				 			  ,closeBtn: 0
				 			});
				 		return false;
				 	}
				 	
				 	var coinCode = $("#coinCode").val();
				 	var transactionCount = $("#buyNumber").val();
				 	if(!transactionCount || transactionCount==0){
				 		layer.alert('请输入正确的数量', {
				 			  skin: 'layui-layer-molv' //样式类名
				 			  ,closeBtn: 0
				 			});
				 		return 
				 	}
				 	var transactionPrice = $("#buyUnitPrice").val();
				 	
					$.ajax({
						type : "post",
						url : _ctx+"/user/createTransaction",
						cache : false,
						data : {
							coinCode : coinCode,
							transactionType : 1,
							transactionCount : transactionCount,
							transactionPrice : transactionPrice
						},
						dataType : "json",
						success : function(data) {
							
							if(data){
								if(data.success){
									layer.open({
										  title: false,
										  type: 2,
										  shadeClose: false,
										  move : false,
										  scrollbar  : false,
										  shade: 0.8,
										  area: ['33%', '70%'],
										  content: _ctx+"/user/getc2cTransaction/"+data.obj,
										  cancel: function(index){
											  window.location.reload() ;
										  } 
									}); 
								}else{
									layer.msg(data.msg);
								}
							}
					 	
						},
						error : function(e) {
							
						}
					});
				 	
			 })
			 var  showflag = 'N';
			  //卖
			 $("#sellBtn").on("click",function(){

				 	var username = $("#username").val();
				 	if(username==undefined||username==""){
				 		layer.alert('请先登录后再进行交易', {
				 			  skin: 'layui-layer-molv' //样式类名
				 			  ,closeBtn: 0
				 			});
				 		return false;
				 	}
				 	
				 	var coinCode = $("#coinCode").val();
				 	var transactionCount = $("#sellNumber").val();
				 	var transactionPrice = $("#sellUnitPrice").val();
				 	if(!transactionCount || transactionCount==0){
				 		layer.alert('请输入正确的数量', {
				 			  skin: 'layui-layer-molv' //样式类名
				 			  ,closeBtn: 0
				 			});
				 		return 
				 	}
				 	var accountPassWordflag = $("#accountPassWordflag").val();
                    var phoneState = $("#phoneState").val();
				 	if (accountPassWordflag || phoneState== 1) {
                        var accountPassWord = $("#accountPassWord").val();
                        var verifyCode = $("#verifyCode").val();
                        if (!accountPassWord && accountPassWordflag) {
                            if (showflag == 'N') {
                                $(".verifyLayout").show();
                                showflag = 'Y';
                                return
                            }else {
                                layer.alert('请输入交易密码', {
                                    skin: 'layui-layer-molv' //样式类名
                                    ,closeBtn: 0
                                });
                                return
                            }
                        }
                        if (!verifyCode && phoneState== 1) {
                            if (showflag == 'N') {
                                $(".verifyLayout").show();
                                showflag = 'Y';
                                return
                            }else {
                                layer.alert('请输入短信验证码', {
                                    skin: 'layui-layer-molv' //样式类名
                                    ,closeBtn: 0
                                });
                                return
                            }
                        }
                    }
					$.ajax({
						type : "post",
						url : _ctx+"/user/createTransaction",
						cache : false,
						data : {
							coinCode : coinCode,
							transactionType : 2,
							transactionCount : transactionCount,
							transactionPrice : transactionPrice
						},
						dataType : "json",
						success : function(data) {
							
							if(data){
								if(data.success){
//									layer.open({
//									  title: false,
//									  type: 2,
//									  shadeClose: false,
//									  move : false,
//									  scrollbar  : false,
//									  shade: 0.8,
//									  area: ['33%', '70%'],
//									  content: _ctx+"/user/getc2cTransaction/"+data.obj,
//									  cancel: function(index){
//										  window.location.reload() ;
//									  } 
//								}); 
								
								layer.msg(data.msg,{time:1500},function(){
									window.location.reload() ;
									
								});
							}else{
									layer.msg(data.msg);
								}
							}
					 	
						},
						error : function(e) {
							
						}
					});
				 
			 });
            $("#c2c_dialog-close").on("click",function(){
                showflag = 'N';
                $(".verifyLayout").hide();
            });
            $("#c2c_Validform_check").on("click",function(){
                var username = $("#username").val();
                if(username==undefined||username==""){
                    layer.alert('请先登录后再进行交易', {
                        skin: 'layui-layer-molv' //样式类名
                        ,closeBtn: 0
                    });
                    return false;
                }

                var coinCode = $("#coinCode").val();
                var transactionCount = $("#sellNumber").val();
                var transactionPrice = $("#sellUnitPrice").val();
                if(!transactionCount || transactionCount==0){
                    layer.alert('请输入正确的数量', {
                        skin: 'layui-layer-molv' //样式类名
                        ,closeBtn: 0
                    });
                    return
                }
                var accountPassWordflag = $("#accountPassWordflag").val();
                var accountPassWord = $("#accountPassWord").val();
                var phoneState = $("#phoneState").val();
                var verifyCode = $("#verifyCode").val();
                if (accountPassWordflag || phoneState== 1) {
                    if (!accountPassWord && accountPassWordflag) {
                        if (showflag == 'N') {
                            $(".verifyLayout").show();
                            showflag = 'Y';
                            return
                        }else {
                            layer.alert('请输入交易密码', {
                                skin: 'layui-layer-molv' //样式类名
                                ,closeBtn: 0
                            });
                            return
                        }
                    }
                    if (!verifyCode && phoneState== 1) {
                        if (showflag == 'N') {
                            $(".verifyLayout").show();
                            showflag = 'Y';
                            return
                        }else {
                            layer.alert('请输入短信验证码', {
                                skin: 'layui-layer-molv' //样式类名
                                ,closeBtn: 0
                            });
                            return
                        }
                    }
                }
                $("#c2c_Validform_check").attr("disabled", "disabled");
                $.ajax({
                    type : "post",
                    url : _ctx+"/user/createTransaction",
                    cache : false,
                    data : {
                        coinCode : coinCode,
                        transactionType : 2,
                        transactionCount : transactionCount,
                        transactionPrice : transactionPrice,
                        accountPassWord : md5.md5(accountPassWord),
                        verifyCode : verifyCode
                    },
                    dataType : "json",
                    success : function(data) {

                        if(data){
                            if(data.success){

                                layer.msg(data.msg,{time:1500},function(){
                                    window.location.reload() ;

                                });
                            }else{
                                layer.msg(data.msg);
                                $("#c2c_Validform_check").removeAttr("disabled");
                            }
                        }

                    },
                    error : function(e) {
                    }
                });
            });

            $("#sendsmsBtn").on("click", function() {
                var username = $("#username").val();
                var inputNumWit = $("#inputNumWit").val();
                //var accountPassWord = $("#accountPassWord").val();
                var withdrawCode = $("#withdrawCode").val();
                $(this).attr("disabled", "disabled");
                $.ajax({
                    type : "post",
                    url : _ctx + "/sms/smsPhone",
                    data : {
                        username : username,
                        //accountPassWord : md5.md5(accountPassWord),
                        inputNumWit : inputNumWit,
                        withdrawCode : withdrawCode
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        if (data) {
                            if (data.success) {
                                layer.msg(sms_success, {
                                    icon : 1
                                })

                                var time = 120;
                                window.clearInterval(pageTimer["btcget"]);
                                // 开启点击后定时数字显示
                                pageTimer["btcget"] = window.setInterval(function() {
                                    time = time - 1;
                                    if (time == 0) {
                                        $("#sendsmsBtn").html(fasongyanzhengma);
                                        $("#sendsmsBtn").removeAttr("disabled");// 按钮可用
                                        window.clearInterval(pageTimer["btcget"]);
                                    } else {
                                        $("#sendsmsBtn").html(time + miaochongxinfasong);
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
                            layer.msg(fasongshibai, {
                                icon : 2
                            })
                        }
                    },
                    error : function(e) {

                    }
                });

            });

            $("#sendBtn").on("click",function(){
                $(this).attr("disabled","disabled");
                $(this).html(yifasong);
                $.ajax({
                    type : "post",
                    url : _ctx + "/sms/smsPhone",
                    data : {
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        if(data){
                            if(data.success){
                                layer.msg("发送成功", {icon: 1})

                                var time = 120;
                                window.clearInterval(pageTimer["c2cget"]);
                                // 开启点击后定时数字显示
                                pageTimer["c2cget"] = window.setInterval(function() {
                                    time = time - 1;
                                    if (time == 0) {
                                        $("#sendBtn").html(dianji);
                                        $("#sendBtn").removeAttr("disabled");// 按钮可用
                                        $("#sendBtn").html(chongxinfasong);// 按钮可用
                                        window.clearInterval(pageTimer["c2cget"]);
                                    } else {
                                        $("#sendBtn").html(time+"秒" );
                                    }

                                }, 1000);

                            }else{
                                $("#sendBtn").removeAttr("disabled");// 按钮可用
                                layer.msg(data.msg, {icon: 2})
                            }
                        }else{
                            $("#sendBtn").removeAttr("disabled");// 按钮可用
                            layer.msg(fasongshibai, {icon: 2})
                        }
                    },
                    error : function(e) {

                    }
                });

            });
		}
	

	}
});