define(function(require,exports,module){
    this.md5 = require("js/base/utils/hrymd5")
    this._table = require("js/base/table");
    this.validate = require("js/base/validate");
    this.base = require("js/base/base");
    require("js/base/secondvail");
    this.firstvail = require("js/base/firstvail");
	
	module.exports = {
		renderTime : function(){
			$("td[name=createlong]").each(function(index,element){
				
				$(element).html(TimestampFormat('Y-m-d H:i:s',$(element).html()/1000));
				
			})
		},
		init : function(){
            clearPageTimer();
            $(".verifyLayout").hide();
            $(".verifyLayout1").hide();
            $(".verifyLayout2").hide();
            $(".verifyLayout").hide();

			//加载base
			require("base");
			
			$("#select").on("change",function(){
				$("#currencyType").val($(this).find("option:selected").val());
			})
			
			$("#savepublickey").on("click",function(){
                var publicKey = $("#publicKey").val();
                if(!publicKey){
                    layer.msg(qiangbaoisnull,{icon:2});
                    return false;
                }
                $.ajax({
                    type : "post",
                    url : _ctx + "/sencodvail",
                    data : {
                        type:"6"
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        if(data){
                        	debugger;
                            if(data.success){
                                var phone =data.obj.phoneState;
                                var google=data.obj.googleState;

                                if(google==1&&phone==0){
                                    $(".verifyLayout1").show();
                                    $('.dialog-close').on('click',function(){
                                        $("#oktx").removeAttr("disabled");
                                        $(this).parent().parent().hide()
                                    })

                                }else if(google==0&&phone==1){
                                    $(".verifyLayout").show();
                                    $('.dialog-close').on('click',function(){
                                        $("#oktx").removeAttr("disabled");
                                        $(this).parent().parent().hide()
                                    })

                                }else if(google==1&&phone==1){

                                    $('#mobile-form').css('display','none');
                                    $(".verifyLayout2").show();
                                    $('.verify-form1').hide();
                                    $('.dialog-close').on('click',function(){
                                        $("#oktx").removeAttr("disabled");
                                        $(this).parent().parent().hide()
                                    })
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
                                    firstvail.sendvail("setaddr");
                                    //window.location.href = _ctx+"/user/center";
                                    //$("#oktx").removeAttr("disabled");// 按钮可用
                                }



                            }else{
                                layer.msg(data.msg, {icon: 2});
                                $("#oktx").removeAttr("disabled");
                            }
                        }else{
                            layer.msg("tibishibai", {icon: 2})
                            $("#oktx").removeAttr("disabled");
                        }
                    },
                    error : function(e) {

                    }
                });

				/*var publicKey = $("#publicKey").val();
				if(!publicKey){
					layer.msg(qiangbaoisnull,{icon:2});
					return false;
				}
				var reg = /^[a-zA-Z\d~\!@#\$%\^&\*\(\)\._\+]+$/;
				if(!reg.test(publicKey)){
	        		layer.msg(qianbaobunengbaohanhanzi,{icon:2});
	        		return false;
	        	}
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
				})*/
			})
			
			$("#tablepublic").on("click","#deletePub",function(){
				var value = $(this).siblings().val();
				layer.confirm(isdelete, {
	    			btn: [quding,quxiao]
				}, function(){
					$.ajax({
						type : "POST",
						dataType : "JSON",
						url : _ctx + "/user/publickeylist/delete",
						cache : false,
						data : {id:value},
						success : function(data) {
							layer.closeAll('dialog');
							loadUrl(_ctx+"/user/publickeylist/index");
						}
					})
				})
			})

            $("#sendBtn,#sendBtn1").on("click",function(){

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
                                layer.msg(fasongchenggong, {icon: 1})

                                var time = 120;
                                window.clearInterval(pageTimer["btcget"]);
                                // 开启点击后定时数字显示
                                pageTimer["btcget"] = window.setInterval(function() {
                                    time = time - 1;
                                    if (time == 0) {
                                        $("#sendBtn1").html(dianji);
                                        $("#sendBtn1").removeAttr("disabled");// 按钮可用
                                        $("#sendBtn1").html(chongxinfasong);// 按钮可用
                                        $("#sendBtn").html(dianji);
                                        $("#sendBtn").removeAttr("disabled");// 按钮可用
                                        $("#sendBtn").html(chongxinfasong);// 按钮可用
                                        window.clearInterval(pageTimer["btcget"]);
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
                            $("#sendBtn").removeAttr("disabled");// 按钮可用
                            $("#sendBtn").removeAttr("disabled");// 按钮可用

                            layer.msg(fasongshibai, {icon: 2})
                        }
                    },
                    error : function(e) {

                    }
                });

            });
		}
		
		/*getPublickey : function() {
			$("#savepublickey").on("click",function(){
				var tableObj = document.getElementById("tablepublic");
				var trObjArr = tableObj.rows;
				var Publickey = "";
				if(trObjArr.length > 0){
					for (var i = 0; i < trObjArr.length; i++){
						Publickey = trObjArr[i].publicKey ;
						if($("#publicKey").val() == Publickey){}
							layer.msg("钱包公钥不能重复",{icon:2});
							return false;
					}
				}
			});
		}*/
		
	}
})