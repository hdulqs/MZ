define(function(require, exports, module) {

	module.exports = {
			sendvail : function(type){
				if(type=="login"){
					var username = $("#username").val();
					var password = $("#password").val();
					var marketlogin=$(".market").val();
					if(!username){
						layer.msg(youxiangbunengweikong, {icon: 2});
						return ;
					}
					if(!password){
						layer.msg(mimabunengweikong, {icon: 2});
						return ;
					}
					$.ajax({
						type : "post",
						url : _ctx + "/loginService",
						data : {
							username : username,
							password : md5.md5(password)
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							if(data){
								if(data.success){
									if(marketlogin=="ket"){
										window.location.href = _ctx+"/market.do?tokenId="+data.obj;

									}else{
										window.location.href = _ctx+"/user/center?tokenId="+data.obj;

									}
									//layer.msg("登录成功", {icon: 1,time:1000},function(){
									//})
								}else{
									layer.msg(data.msg, {icon: 2})
								}
							}else{
								layer.msg(data.msg, {icon: 2})
							}
						},
						error : function(e) {
							
						}
					});
				}else if(type=="setpw"){
					 oldPassWord = $("#oldPassWord").val();
					 newPassWord = $("#newPassWord").val();
					 reNewPassWord = $("#reNewPassWord").val();
					var pwSmsCode = $("#pwSmsCode").val();
					
					if(!oldPassWord){
						layer.msg(yuanshidengluisnull, {icon: 2});
						return ;
					}
					if(!newPassWord){
						layer.msg(xindenglumimaisnull, {icon: 2});
						return ;
					}
					if(newPassWord==oldPassWord){
						layer.msg(newoldisnot, {icon: 2});
						return ;		
					}
					if(!validate.check_password(newPassWord)){
						layer.msg(xinmimageshi, {icon: 2});
						return ;
					}
					if(!reNewPassWord){
						layer.msg(ercimimaisnull, {icon: 2});
						return ;
					}
					if(newPassWord!=reNewPassWord){
						layer.msg(liangcimima, {icon: 2});
						return ;
					
					}
					
					$.ajax({
						type : "post",
						url : _ctx + "/user/setpw",
						data : {
							oldPassWord : md5.md5(oldPassWord),
							newPassWord : md5.md5(newPassWord),
							reNewPassWord : md5.md5(reNewPassWord),
						},
						cache : false,
						dataType : "json",
						success : function(data) {
							if(data.success){
									layer.msg(xiugaichenggong, {icon: 1,time:1000},function(){
										//跳转到个人中心
										window.location.href = _ctx+"/user/center";
									})
								}else{
									layer.msg(data.msg, {icon: 2});
									$("#submitBtn").removeAttr("disabled");
								}
						},
						error : function(e) {
							
						}
					});
				}else if(type=="btcget"){
					
					var coinType = $("#coinType").val();
					var withdrawCode = $("#withdrawCode").val();
					var accountPassWord = $("#accountPassWord").val();
					var currencyType = $("#currencyType").val();
					var btcNum = $("#inputNumWit").val();
					var btcKey = $("#btcKey").val();
					var pacecurrecy=$("#issued_sub_key_c").val();
					var shouxufei=$("#shouxufei").text();
					var shouxu="";
					if(pacecurrecy!=null){
						shouxu=$("#issued_sub_key_c").val();
					}else{
					    shouxu=$("#shouxufei").text();
					}
					if(shouxu==null||shouxu==""){
						layer.msg(qingtianjiaqianbaodizhi, {
							icon : 2
						});
					}
					if($("#issued_sub_key_c").val()!=null&&Number($("#issued_sub_key_c").val())>=Number(btcNum)){
						layer.msg(tibishouxugeibunengdayutibishul, {
							icon : 2
						});
						return false;
					}
					if(btcKey==null){
						layer.msg(qingtianjiaqianbaodizhi, {
							icon : 2
						});
						return false;
					}
					if(btcNum ==null || btcNum==""){
						layer.msg(qingshurutibishuliang, {
							icon : 2
						});
						return false;
					}
					$.ajax({
						type : "post",
						url : _ctx + "/user/publickeylist/getOrder",
						dataType : "JSON",
						data : {
							coinType : coinType,
							withdrawCode : withdrawCode,
							accountPassWord : md5.md5(accountPassWord),
							currencyType : currencyType,
							btcNum : btcNum,
							btcKey : btcKey,
							shouxufei:shouxu
						},
						success : function(data) {
							if(data!=undefined&&data.success){
								layer.msg(shengqingchenggong,{icon:1,time:1500},function(){
									loadUrl(_ctx + "/user/btc/get")
								});
								
							}else{
								layer.msg(data.msg, {icon : 2});
								$("#oktx").attr("disabled", false);
								$('#table').bootstrapTable('refresh');
								$("#oktx").removeAttr("disabled");// 按钮可用
							}
						}
					})
				}else if(type=="setagoogle"){
					
					var accountGoogleWord = $("#accountGoogleWord").val();
					var PassWord = $("#PassWord").val();
				$.ajax({
					type : "post",
					url : _ctx + "/google/jcgoogle",
					data : {
						codes : accountGoogleWord,
						PassWord : md5.md5(PassWord)
					},
					cache : false,
					dataType : "json",
					success : function(data) {
						if(data.success){
							
							layer.msg(data.msg, {icon: 1,time:1000},function(){
								//跳转到个人中心
								window.location.href = _ctx+"/user/center";
							})

						}else{
							layer.msg(data.msg);

						}
					},
					
				});
				}else if(type=="setphone"){
					
					var mobile=$("#mobile-number").val();
					var verifyCode = $("#verifyCode1").val();
					
					$.ajax({
						type : "post",
						url : _ctx + "/phone/setPhone",
						data :{mobile:mobile,verifyCode:verifyCode},
						cache : false,
						dataType : "json",
						success : function(data) {
							if(data.success){
								
								layer.msg(data.msg, {icon: 1,time:1000},function(){
									//跳转到个人中心
									window.location.href = _ctx+"/user/center";
								})

							}else{
								layer.msg(data.msg);
								$("#verifyCode").val("");
							}
						},
						
					});
				}else if(type=="rmbwithdraw"){
					var transactionMoney = $("#transactionMoney").val();
					var accountPassWord = $("#accountPassWord").val();
					var custromerAccountNumber =  $("#custromerAccountNumber").val();
					var withdrawCode = $("#withdrawCode").val();
					if(!custromerAccountNumber){
						layer.msg("请选择银行卡!",{icon:2});
						return false;
					}
					if(!transactionMoney){
						layer.msg("提现金额不能为空!",{icon:2});
						return false;
					}
					if(!validate.isNumber(transactionMoney)){
		        		 layer.msg("金额必须为数字",{icon:2});
		        		 return false;
			         }
					$("#withdraw").ajaxSubmit({
						 type: "post",
				         url: _ctx + "/user/rmbWithdraw/rmbwithdraw",
				         dataType: "JSON",
				         data : {
				        	 accountPassWord : md5.md5(accountPassWord)
				         },
				         resetForm : true,
				         beforeSubmit : function(formData, jqForm, options) {
						 },
				         success: function(data) {
				        	 if(data!=undefined){
				        		 if(data.success){
				        			 layer.msg(shengqingchenggong, {icon: 1,time:1000},function(){
				        				 loadUrl(_ctx+"/user/rmbWithdraw/index");
				        			 });
				        		 }else{
				        			 layer.msg(data.msg, {icon: 2});
				        		 }
				        	 }
				        	 
				         }
					})
				}else if(type=='setaddr'){
                    var publicKey = $("#publicKey").val().trim();
                    if(!publicKey){
                        layer.msg(qiangbaoisnull,{icon:2});
                        return false;
                    }
                    var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
                    if(reg.test(publicKey)){
                        layer.msg(qianbaobunengbaohanhanzi,{icon:2});
                        return false;
                    }
                   /* var reg2 = /[\s]/;
                    debugger
                    if(reg2.test(publicKey)){
                        layer.msg(qianbaobunengbaohanhanzi,{icon:2});
                        return false;
                    }*/


                    $("#withdraw_address_form").ajaxSubmit({
                        type : 'post',
                        url : _ctx + "/user/publickeylist/save",
                        dataType : 'JSON',
                        success : function(data){debugger;
                            if(data.success){
                                layer.msg(data.msg, {icon: 1,time:1000},function(){
                                    loadUrl(_ctx+"/user/publickeylist/index");
                                })
                            }else{
                                if(data.code=='googleCheck'){
                                    $(".verifyLayout").show();
                                    $('.dialog-close').on('click',function(){
                                        $(this).parent().parent().hide()
                                    })
                                }else {
                                    layer.msg(data.msg, {icon: 2, time: 1000}, function () {
                                        loadUrl(_ctx + "/user/publickeylist/index");
                                    })
                                }
                            }
                        }
                    })
				}
			}
		}
		
});
