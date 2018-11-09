define(function(require, exports, module) {

    var room =  "otc";
    var otcroom ="otc_buy_" + $("#otccoinCode").val();

    window.localStorage.setItem("weixin_img","/static/src/img/otc/weixin.png");
    window.localStorage.setItem("zhifubao_img","/static/src/img/otc/zhifubao.png");
    window.localStorage.setItem("yinhangka_img","/static/src/img/otc/yinhangka.png");

    var weixin_img = window.localStorage.getItem("weixin_img");
    var zhifubao_img = window.localStorage.getItem("zhifubao_img");
    var yinhangka_img = window.localStorage.getItem("yinhangka_img");

    function formatNum(num,n)
    {//参数说明：num 要格式化的数字 n 保留小数位
        num = String(num.toFixed(n));
        var re = /(-?\d+)(\d{3})/;
        while(re.test(num)) num = num.replace(re,"$1,$2")
        return num;
    }

	function Refreshtable (datas){
        $("#otc_table tr").remove();
        data  = datas;
        if(data) {
            for (var i = 0; i < data.length; i++) {
                var transaction_type;
                if (data[i].transactionType == 1) {
                    transaction_type = mai;
                } else {
                    transaction_type = maii;
                }
                var money = formatNum(data[i].transactionMoney, 3);
                html = ' <tr> <td style="width:110px">' + transaction_type + '</td>' +
                    '<td style="width:110px">' + data[i].coinCode + '</td>' +
                    '<td style="width:110px;text-align: right;">' + data[i].transactionPrice + '</td>' +
                    '<td style="width:110px;text-align: right;">' + data[i].transactionCount + '</td>' +
                    '<td style="width:110px;text-align: right;">￥' + money + '</td>' ;
                //在线判断
                if(data[i].online == "true") {
                    html = html + '<td style="width:110px"><div style="display: flex;justify-content: center;align-items: center;"><div style="width: 10px;height: 10px;background: green;border-radius: 100%;"></div><div>' + data[i].customerName + '</div></div></td>';
                }else{
                    html = html + '<td style="width:110px"><div style="display: flex;justify-content: center;align-items: center;"><div style="width: 10px;height: 10px;background: #CCC;border-radius: 100%;"></div><div>' + data[i].customerName + '</div></div></td>';
                }
                html = html +    '<td style="width:110px">' + data[i].businessQuantity + '</td>' +
                    '<td style="width:110px">';
                        if(data[i].weChatFlag == "true") {
                            html = html + '<div style="float:left;margin-left:10px;width: 20px;height: 20px;background: url(' + weixin_img + ') center no-repeat;background-size: cover"></div>';
                        }
                    if(data[i].alipayFlag == "true") {
                    html = html + '<div style="float:left;margin-left:10px;width: 20px;height: 20px;background: url(' + zhifubao_img + ') center no-repeat;background-size: cover"></div>';
                    }
                   if(data[i].cardNumberFlag == "true") {
                    html = html + '<div style="float:left;margin-left:10px;width: 20px;height: 20px;background: url(' + yinhangka_img + ') center no-repeat;background-size: cover"></div>';
                    }
                        html =html + '</td>' +
                        '<td style="width:120px">';
                if (data[i].transactionType == 1) {
                    html = html + '<div id="' + data[i].id + '" onclick="otc_btn_sold_fun(this)" class="otc_btn otc_btn_sold" transactionPrice="' + data[i].transactionPrice + '">'+maiichu+'</div>';
                } else {
                    html = html + '<div id="' + data[i].id + '" onclick="otc_btn_buy_fun(this)" class="otc_btn otc_btn_buy" transactionPrice="' + data[i].transactionPrice + '">'+mairu+'</div>';
                }
                html = html + '</td></tr>';

                $("#otc_table").append(html);

            }
        }

	}

	
	module.exports = {
		/**
		 * 查看订单详细信息
		 */
		getdeatil  : function(){




			
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
            $("#buyUnitPrice").on("input",function(){

                formartInput(this);

                var count = 0;
                if(this.value!=""){
                    count = parseFloat(this.value).toFixed(2);
                }

                var price = 0;
                if($("#buyNumber").val()!=""){
                    price = parseFloat($("#buyNumber").val()).toFixed(2);
                }

                $($("#buyfinish").find("span")[0]).html(Math.floor(count*price*100)/100);
            });

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

            $("#sellUnitPrice").on("input",function(){

                formartInput(this);

                var count = 0;
                if(this.value!=""){
                    count = parseFloat(this.value).toFixed(2);
                }

                var price = 0;
                if($("#sellNumber").val()!=""){
                    price = parseFloat($("#sellNumber").val()).toFixed(2);
                }

                $($("#sellfinish").find("span")[0]).html(Math.floor(count*price*100)/100);

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

            $("#otcbuyNumber").on("input",function(){

                formartInput(this);

                var count = 0;
                if(this.value!=""){
                    count = parseFloat(this.value).toFixed(2);
                }

                var price = 0;
                if($("#otcbuyUnitPrice").val()!=""){
                    price = parseFloat($("#otcbuyUnitPrice").val()).toFixed(2);
                }

                $($("#otcbuyfinish").find("span")[0]).html(Math.floor(count*price*100)/100);
            });
            $("#otcsellNumber").on("input",function(){

                formartInput(this);

                var count = 0;
                if(this.value!=""){
                    count = parseFloat(this.value).toFixed(2);
                }

                var price = 0;
                if($("#otcsellUnitPrice").val()!=""){
                    price = parseFloat($("#otcsellUnitPrice").val()).toFixed(2);
                }

                $($("#otcsellfinish").find("span")[0]).html(Math.floor(count*price*100)/100);

            });

            // 点击任何地方，让弹窗消失
            $(".mask").click(function(){
                $(".mask").fadeOut(500);
            });
            $(".mask2").click(function(){
                $(".mask2").fadeOut(500);
            });
            // 点击弹窗 阻拦(外层事件)(stopPropagation阻拦click事件冒泡) 及实现了 点击弹窗之外的任何地方  让弹窗隐藏
            $('.dialogOtc').click(function(event){
                event.stopPropagation();
            });

            $(".back").click(function(){
                $(".mask").fadeOut(500);
                $(".mask2").fadeOut(500);
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
				 	
				 	var coinCode = $("#otccoinCode").val();
				 	var transactionCount = $("#buyNumber").val();
				 	if(!transactionCount || transactionCount==0){
				 		layer.alert('请输入正确的数量', {
				 			  skin: 'layui-layer-molv' //样式类名
				 			  ,closeBtn: 0
				 			});
				 		return 
				 	}
				 	var transactionPrice = $("#buyUnitPrice").val();

                   if(!transactionPrice || transactionPrice==0){
                     layer.alert('请输入正确的价格', {
                         skin: 'layui-layer-molv' //样式类名
                         ,closeBtn: 0
                      });
                     return
                    }
				 	
					$.ajax({
						type : "post",
						url : _ctx+"/user/otcCreateTransaction",
						cache : false,
						data : {
							coinCode : coinCode,
							transactionType : 1,
							transactionCount : transactionCount,
							transactionPrice : transactionPrice
						},
						dataType : "json",
						success : function(data) {
							debugger;
							if(data){
								if(data.success){
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
				 	
				 	var coinCode = $("#otccoinCode").val();
				 	var transactionCount = $("#sellNumber").val();
				 	var transactionPrice = $("#sellUnitPrice").val();
				 	if(!transactionCount || transactionCount==0){
				 		layer.alert('请输入正确的数量', {
				 			  skin: 'layui-layer-molv' //样式类名
				 			  ,closeBtn: 0
				 			});
				 		return 
				 	}
					$.ajax({
						type : "post",
						url : _ctx+"/user/otcCreateTransaction",
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


            $("#diaConfirm").click(function(){
                // $(".mask").fadeOut(500);
                $.ajax({
                    type: "post",
                    url: _ctx + "/otc/createOrder",
                    cache: false,
                    data: {
                        id: $("#otcbuytransactionid").val(),
                        transactioncount:$("#otcbuyNumber").val()
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.msg(data.msg);
                        $(".mask").fadeOut(500);
                    },
                    error : function(e) {
                        layer.msg("提交错误！");
                    }
                });
            });
            $("#diaConfirm2").click(function(){
                // $(".mask").fadeOut(500);
                debugger;
                $.ajax({
                    type: "post",
                    url: _ctx + "/otc/createOrder",
                    cache: false,
                    data: {
                        id: $("#otcselltransactionid").val(),
                        transactioncount:$("#otcsellNumber").val()
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.msg(data.msg);
                        $(".mask2").fadeOut(500);
                    },
                    error : function(e) {
                        layer.msg("提交错误！");
                    }
                });
            });


            //连接websocket后端服务器
            this.socket = io.connect(hry_socketioUrl);
            var websocket = this.socket;

            //告诉服务器端有用户登录
            var username = $("#username").val();
            if(username==undefined||username=="") {
                var coinCode = $("#otccoinCode").val();
                this.socket.emit('otclogin', {
                    userid: "1",
                    username: "游客登录",
                    room: otcroom,
                    transactionType: "buy",
                    coinCode : coinCode
                });
            }else {
                var coinCode = $("#otccoinCode").val();
                this.socket.emit('otclogin', {
                    userid: $("#otcuserid").val(),
                    username: $("#username").val(),
                    room: otcroom,
                    transactionType: "buy",
                    coinCode : coinCode
                });
			}

            //监听新用户登录
            this.socket.on('login', function(o){
                //CHAT.updateSysMsg(o, 'login');
               // alert('login');
            });

            //监听用户退出
            this.socket.on('logout', function(o){
                //CHAT.updateSysMsg(o, 'logout');
                console.log(o);
            });

            //监听用户index
            this.socket.on('otc_room', function(o){
                //CHAT.updateSysMsg(o, 'logout');
                Refreshtable(o);
               // console.log(o);

            });

            $("#select_buy").click(function(){

                $("#otc_table tr").remove();
                var coinCode = $("#otccoinCode").val();
                websocket.emit('otcloginroom', {
                    userid: $("#otcuserid").val(),
                    username: $("#username").val(),
                    room: otcroom,
                    transactionType: "buy",
                    coinCode : coinCode
                });
                otcroom = 'otc_buy_' + coinCode;
                $(".select_btn").children().removeClass("select_active_1");
                $(this).addClass("select_active");
            });
            $("#select_sell").click(function(){

                $("#otc_table tr").remove();
                var coinCode = $("#otccoinCode").val();
                websocket.emit('otcloginroom', {
                    userid: $("#otcuserid").val(),
                    username: $("#username").val(),
                    room: otcroom,
                    transactionType: "sell",
                    coinCode : coinCode
                });
                otcroom = 'otc_sell_' + coinCode;
                $(".select_btn").children().removeClass("select_active");
                $(this).addClass("select_active_1");
            });
		}
	

	}
});