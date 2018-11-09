"use strict";

function Ebank() {
    this.name = 'Ebank';
    this.bank_flag = 1;

    // 充值小数写入
    this.rand_num = $('#rand_num').val();
    if (undefined != this.rand_num) {
        // $('#write_num').html(this.rand_num.substring(1,4));
        $('#write_num').html(this.rand_num);
    }

    this.choice_money = 1;

}

Ebank.prototype.decimal = function() {
    var over_num = $('#over_num').val();
    var number = Fnc.isDigit(over_num) ? over_num: 0;
    //充值数量
    var num = parseFloat(number) + parseFloat(this.rand_num);
    var nus = Fnc.changeTwoDecimal(num);

    $('#ure').html(nus);
}

Ebank.prototype.choiceMoney = function(flag) {
    this.choice_money = flag;
    if (flag == 0) {
        $('.choice_ebank').hide();
        $('.truns_upper').hide();
        $('.turns_lower').show();
        $('#choice_submit').html('去互融理财充值');
    } else {
        $('.choice_ebank').show();
        $('.turns_lower').hide();
        $('.truns_upper').show();
        $('#choice_submit').html('生成汇款订单');
    }
}

Ebank.prototype.licai = function() {
	$('.ebank_box_common').show();
}

Ebank.prototype.hideAlert = function() {
	$('.ebank_box_common').hide();
}

Ebank.prototype.platform = function(name) {

    if ('bank' == name) {
        this.bank_flag = 1;
        $('#recharge_name').html('汇款人姓名');
        $('#recharge_account').html('汇款银行').removeClass('recharge_left_input');
    }

    if ('alipay' == name) {
        this.bank_flag = 2;
        $('#recharge_name').html('支付宝姓名');
        $('#recharge_account').html('支付宝账号').addClass('recharge_left_input');
    }

    //$('#recharge_name_yes').val('');
    //$('#recharge_account_yes').val('');

}

// 特定时间提示特定银行汇款情况
Ebank.prototype.checkBank = function(){
	// 获取当前星期\小时
	var dateNow = new Date();
	var weekDay = dateNow.getDay();
	var hours = dateNow.getHours();
	// 获取选择的值
	var bankName = $('#bankOut').val();
	// 周末不提供汇款的银行名称
	var banks = ['工商银行','农业银行','建设银行','交通银行','中国银行','民生银行'];
	// 时间验证
	//if(true){
	if(weekDay == 6 || weekDay == 0 || (weekDay == 5 && hours >= 17) || (weekDay == 1 && hours < 9)){
		// 银行验证
		if(Fnc.in_array(bankName,banks)){
			$('body').append('<div class="box_bg" id="dimmer"></div><div class="special_note" id="bankModal"><h2>特别提示</h2><p>由于周末部分银行不提供汇款到账业务，导致用户充值不能及时到账。</p><p>建议周末（周五17:00至周一09:00）请尽量避免使用：<span style="color:#f00;">工商银行、农业银行、建设银行、交通银行、中国银行、民生银行</span>等银行卡进行汇款，否则可能会延迟至周一到账。</p><div class="yes_no"><button class="yes bank-y">确认继续汇款</button><button class="no bank-n">取消</button></div></div>');
		}
	}

	// 点击确定
	$('.bank-y').click(function() {
		$('.box_bg').remove();
		$('#bankModal').remove();
	});

	// 点击取消
	$('.bank-n').click(function(){
		$('#bankOut').val('请选择银行');
		$('.box_bg').remove();
		$('#bankModal').remove();
	})
}


// 生成汇款订单的验证
Ebank.prototype.recharge = function() {

    if (this.choice_money == 0) {
        // window.open("http://hurong.com/user_ebank");
        $('.ebank_box_common').show();
        return false;
    }

    // 姓名
    //var recharge_name_yes = $('#recharge_name_yes').val();
    var recharge_name_yes = $('#realname').attr('data');

    if (!(/([\u4E00-\u9FA5]{2,5}|[a-zA-Z]{5,32})(?:·[\u4E00-\u9FA5]{2,5})*/).test(recharge_name_yes)) {
    //if (!(/^[\u4e00-\u9fa5]{2,10}$/).test(recharge_name_yes)) {

        if (this.bank_flag == 1 || this.bank_flag == 3) {
            Layer.alert('请输入正确的汇款人姓名');
        } else {
            Layer.alert('请输入正确的支付宝姓名');
        }

        return false;

    }

    // 银行卡号
    var recharge_account_yes = $('#recharge_account_yes').val();

    if (recharge_account_yes == '' || recharge_account_yes.length == 0) {

        if (this.bank_flag == 1) {
            Layer.alert('请输入正确的汇款银行账号');
        } else {
            Layer.alert('请输入正确的支付宝账号');
        }

        return false;

    }

	// 汇出银行
	var bankName = $('#bankOut').val();
	if(bankName == '请选择银行'){
		Layer.alert('请选择汇出银行');
		return false;
	}

    // 充值金额
    var over_num = $('#over_num').val();
    var number = Fnc.isDigit(over_num) ? over_num: 0;
    var num = parseInt(number) + parseFloat(this.rand_num);

    if(num < 3000) {
	   Layer.alert('充值金额不能小于3000元');
        return false;
    }

    if (num >= 1000000) {
        Layer.alert('转账金额不能大于100万');
        return false;
    }

    // base64 加密
    var base64 = new Base64();
    var str = base64.encode(num.toString());

    var request_uri = "/user_ebank/confirm/money/" + str + "/pay/" + this.bank_flag;

    var re_flag = this.bank_flag;

    $.post("/user_ebank/confirm", {'money': str, 'pay': this.bank_flag, 'account': recharge_account_yes, 'name': recharge_name_yes},
        function(data) {
            if (data.code != 0) {
                Layer.alert(data.msg);
            } else {
                $('#recharge_alert_bank').show();
                $("#showw").hide();
                $('#recharge_alert_bank_close').click(function(){$('#recharge_alert_bank').hide(); location.reload();});
                $('.recharge_html_num').html(num);
                if (re_flag == 1) {
                    $('#recharge_alert_title').html('银行卡汇款信息');
                    $('.phone_bank_and_net').html('“手机银行”或“网上银行”');
                } else {
                    $('#recharge_alert_title').html('支付宝汇款信息');
                    $('.phone_bank_and_net').html('“支付宝”');
                }
            }
        }
    );

    return false;
};

Ebank.prototype.showTips = function(pay, num) {

    $('#recharge_alert_bank').show();
    $('#recharge_alert_bank_close').click(function(){$('#recharge_alert_bank').hide();});
    $('.recharge_html_num').html(num);
    if (pay != '支付宝') {
        $('#recharge_alert_title').html('银行卡汇款信息');
        $('.phone_bank_and_net').html('“手机银行”或“网上银行”');
    } else {
        $('#recharge_alert_title').html('支付宝汇款信息');
        $('.phone_bank_and_net').html('“支付宝”');
    }

}

Ebank.prototype.fromDai = function() {

    var number = $("#tagContent1 input[name='number']").val();
    var pwdtrade = $("#tagContent1 input[name='pwdtrade']").val();

    $.post('/user_ebank/daitoybex', {'number': number, 'pwdtrade': pwdtrade}, function(data) {
        if (data.code == 2) {
            Layer.alert(data.msg, function() {
                self.location="/user/login?url=%2Fuser_ebank%2Findex";
            });
        } else if (data.code == 1) {
            Layer.alert(data.msg);
        } else {
            Layer.alert(data.msg, function() {
				self.location="/user_ebank";
			});
        }
    });

    return false;
};

var Ebank = new Ebank();


function Confirm() {
    this.name = 'Confirm';
    this.mark = -1;
}

Confirm.prototype.confirm = function() {
    var username = $('#username').val();
    var account = $('#account').val();
    var money = $('#money').val();
    var order_id = $('#exchange_id').val();

    if(username == null || username.length == 0){
        Layer.alert('请输入汇款人姓名');
        return false;
    }
    if(account == null || account.length == 0){
        Layer.alert('请输入汇款帐号');
        return false;
    }
    if(money == null || money.length == 0){
        Layer.alert('请输入实际汇款金额');
        return false;
    }

    $.post("/user_ebank/transferConfirm", { order_id: order_id, username: username, account: account, money: money },
        function(data) {
            var obj = eval('(' + data + ')');
            if (11 == obj.code) {
                $('#tag_show3').addClass('current');
                $('#in_msg').hide();
                $('#result_msg').show();
            } else if(12 == obj.code) {
                // Layer.alert(obj.msg);
                $('#tag_show3').addClass('current');
                $('#in_msg').hide();
                $('#result_msg').show();
                $('#cny_outing').html(obj.msg);
            } else {
                Layer.alert(obj.msg);
            }
        }
    );
}

Confirm.prototype.switch = function() {
    var alert_money = $('#alert_money').val();
    // Layer confirm 回调函数
    Layer.confirm('您确定已经通过“网上银行”或“手机银行”向“李怀顺”汇款<font color="red" style="font-size:25px;">'+ alert_money +'</font>元?', function(flag) {
        console.log(flag);
        if (1 != flag) {
            this.mark = flag
        } else {
            $('#tag_show2').addClass('current');
            $('#bank_msg').hide();
            $('#in_msg').show();
        }
    });
}

var Confirm = new Confirm();



function Chinagpay() {
    this.name = 'Chinagpay';
}

Chinagpay.prototype.confirm = function() {
	var under = $('#under').attr('data');
	if ('yes' == under) {
		return Layer.alert('快速充值临时维护，详情请见公告', function(flag) {
			if (1 == flag) {
				self.location = "/news/detail/?id=1064";
			}
		});
	}
	var txnAmt	= $('#txnAmt').val();
	var preg	= /^[0-9]*(\.[0-9]{1,2})?$/;
	if(!txnAmt || !preg.test(txnAmt)) {
		return Layer.alert('输入金额不合法');
	}

	if(txnAmt<100) {
		return Layer.alert('最低充值金额不能少于100元');
	}

	self.location="/user_chinagpay/confirm/txnAmt/"+txnAmt;
}

var Chinagpay = new Chinagpay();


function Gatewaypay() {
    this.name = 'Gatewaypay';
}

Gatewaypay.prototype.confirm = function() {
	var under = $('#under').attr('data');
	// if ('yes' !== under) {
		return Layer.alert('快速充值服务维护中，请稍后再试！', function(flag) {
			// if (1 == flag) {
			// 	self.location = "/news/detail/?id=1064";
			// }
		});
	// }
	var txnAmt	= $('#txnAmt').val();
	var preg	= /^[0-9]*(\.[0-9]{1,2})?$/;
	if(!txnAmt || !preg.test(txnAmt)) {
		return Layer.alert('输入金额不合法');
	}

	if(txnAmt<100 || txnAmt>5000000) {
		return Layer.alert('充值金额不能少于100元，或大于500万元');
	}

	self.location="/user_sumapay/confirm/txnAmt/"+txnAmt;
}

var Gatewaypay = new Gatewaypay();



var wechattime;
function Wechatpay(){
    this.name = 'Wechatpay';
    this.t2;
}

function checkpayisOK(){
    var id=$("input[name=trade_no]").val();
    if(id!=""||id!='undefined'){
        $.ajax({
            url:"/user_wechatpay/isPayok/",
            type:"post",
            dataType:"json",
            data:{type:$("#weichatPay").attr('data'),trade_no:id},
            success:function(data){
            	var res = eval('('+data.msg+')');
                if(res.status==1){
	                	$("div.prev-mian").removeClass("none");
	            		$("div.wechatpay-child").addClass("none");
	            		var html='<div class="success-return" style="padding:50px 0px 50px 120px;text-align:center;font-size:22px;">';
	            			html+='<span class="success-span" style="display: block;min-width: 400px;">';
						html+='<span style="font-size: 30px;padding-left:50px;"><i style="width:46px;height:46px;vertical-align: middle;display: inline-block;padding-right:20px;background: url(/images/base_pay.png) 0 -1908px no-repeat;"></i>充值成功！</span>'
	            			html+='<div style="font-size: 18px;margin-top:50px;"><span class="span-child">充值金额：</span><span class="success-num">'+res.money+'  <font style="font-size: 18px;">CNY</font></span></div>';
	            			html+='<div><span class="span-child">实际到账金额：</span><span class="success-num" style="color: #f60;">'+res.money_u+' <font style="font-size: 18px;">CNY</font></span></div><br /><br />';
	            			html+='<span style="font-size: 18px;padding-left:15px">页面将在<span class="count-time"></span>秒后跳转回充值页面</span>';
	            			html+='</span></div>';
	            		$("div.prev-mian").html(html);
	            		window.clearTimeout(wechattime);
	            		CountTime(7);//7秒后跳转
                }else if(res.status==-1){
                		console.log(res.msg);
                }else{
                    console.log(res.msg);
                }
            },
            error:function() {
                /* Act on the event */
            }

        });
    }
}
function checkAlipayisOK(){
    var id=$("input[name=orderId]").val();
    if(id!=""||id!='undefined'){
        $.ajax({
            url:"/user_alipay/isPayok/",
            type:"post",
            dataType:"json",
            data:{type:$("#weichatPay").attr('data'),orderId:id},
            success:function(data){
            	var res = eval('('+data.msg+')');
                if(res.status==1){
	                	$("div.prev-mian").removeClass("none");
	            		$("div.wechatpay-child").addClass("none");
	            		var html='<div class="success-return" style="padding:50px 0px 50px 120px;text-align:center;font-size:22px;">';
	            			html+='<span class="success-span" style="display: block;min-width: 400px;">';
						html+='<span style="font-size: 30px;padding-left:50px;"><i style="width:46px;height:46px;vertical-align: middle;display: inline-block;padding-right:20px;background: url(/images/base_pay.png) 0 -1908px no-repeat;"></i>充值成功！</span>'
	            			html+='<div style="font-size: 18px;margin-top:50px;"><span class="span-child">充值金额：</span><span class="success-num">'+res.money+'  <font style="font-size: 18px;">CNY</font></span></div>';
	            			html+='<div><span class="span-child">实际到账金额：</span><span class="success-num" style="color: #f60;">'+res.money_u+' <font style="font-size: 18px;">CNY</font></span></div><br /><br />';
	            			html+='<span style="font-size: 18px;padding-left:15px">页面将在<span class="count-time"></span>秒后跳转回充值页面</span>';
	            			html+='</span></div>';
	            		$("div.prev-mian").html(html);
	            		window.clearTimeout(wechattime);
	            		CountTime(7);//7秒后跳转
                }else if(res.status==-1){
                		console.log(res.msg);
                }else{
                    console.log(res.msg);
                }
            },
            error:function() {
                /* Act on the event */
            }

        });
    }
}
function clearset(t){
    window.clearTimeout(t);
}
var ii=0;
var jj=0;
var ts;
var tc;
function CountTime(t){
	ii = t;
	ii--;
	if(ii==-1){
		window.clearTimeout(tc);
		window.location.reload();
	}else{
		$("span.count-time").html(ii);
		tc=setTimeout("CountTime("+ii+");",1000);
	}
}
function showClick(t){
	jj = t;
	jj--;
	if(jj==-1){
		window.clearTimeout(ts);
		$("#weichatPay").show();
	    $("#weichatPay2").hide();
	}else{
		$("span.showClick").html(jj);
		ts=setTimeout("showClick("+jj+");",1000);
	}
}
Wechatpay.prototype.confirm = function(){
    var under = $('#under').attr('data');
	var paytype=$('#paytypeqr').val();
	if(paytype!='wxqr' && paytype!='aliqr'){
		return Layer.alert('请选择正确的充值类型');
	}
    /*var time = new Date().Format("hh:mm");
    if (time>"00:00" && time<"08:00") {
        return Layer.alert('微信充值服务维护，维护时间晚00:00-早08:00');
    }*/
    var wctxnAmt  = $('#wctxnAmt').val();

    var preg    = /^[0-9]*(\.[0-9]{1,2})?$/;
    if(!wctxnAmt || !preg.test(wctxnAmt)) {
        return Layer.alert('输入金额不合法');
    }
    if(wctxnAmt<100 || wctxnAmt>50000) {
        return Layer.alert('充值金额不能少于100元，或大于50000元');
    }
    $("#weichatPay").hide();
    $("#weichatPay2").show();
    showClick(5);
    var html ='<script>function setonlyid(id){$("input[name=qrcode]").val(id);}</script>';
    //var html ='';
    return Layer.msg("正在生成订单，请稍后...",0,[wctxnAmt],function(flag){
    		var url='/user_wechatpay/confirm';
			var paytype=$('#paytypeqr').val();
    		$.ajax({
	    		url:url,
	    		type:'post',
				data:{paytype:paytype,txnAmt:$('#wctxnAmt').val()},
	    		success:function(html){
	    			if(html=='service error'){
						return Layer.alert('微信服务维护中，请稍后再试！');
					}else{
						$("div.wechatpay-child").html(html);
						this.t2 = window.setInterval("checkpayisOK()",4000);
						wechattime=this.t2;
						window.setTimeout("clearset("+this.t2+")",900000);

						$("div.prev-mian").addClass("none");
						$("div.wechatpay-child").removeClass("none");
						$('.box_bg').remove();
					    $('.box_common').remove();

					}
	    		}
	    });
    });
}

var Wechatpay = new Wechatpay();

function Alipay(){
    this.name = 'Alipay';
    this.t2;
}
Alipay.prototype.confirm = function(){
    var under = $('#under').attr('data');
	var paytype=$('#paytypeqr').val();
	if(paytype!='wxqr' && paytype!='aliqr'){
		return Layer.alert('请选择正确的充值类型');
	}
    /*var time = new Date().Format("hh:mm");
    if (time>"00:00" && time<"08:00") {
        return Layer.alert('微信充值服务维护，维护时间晚00:00-早08:00');
    }*/
    var wctxnAmt  = $('#wctxnAmt').val();

    var preg    = /^[0-9]*(\.[0-9]{1,2})?$/;
    if(!wctxnAmt || !preg.test(wctxnAmt)) {
        return Layer.alert('输入金额不合法');
    }
    if(wctxnAmt<100 || wctxnAmt>10000) {
        return Layer.alert('充值金额不能少于100元，或大于10000元');
    }
    $("#weichatPay").hide();
    $("#weichatPay2").show();
    showClick(5);
    var html ='<script>function setonlyid(id){$("input[name=qrcode]").val(id);}</script>';
    //var html ='';
    return Layer.msg("正在生成订单，请稍后...",0,[wctxnAmt],function(flag){
    		var url='/user_alipay/confirm';
			var paytype=$('#paytypeqr').val();
			var random_fee=$('#random_fee').html();
    		$.ajax({
	    		url:url,
	    		type:'post',
				data:{paytype:paytype,txnAmt:$('#wctxnAmt').val(),total_fee:formatfloat((parseFloat($('#wctxnAmt').val())+parseFloat(random_fee)),4,0),random_fee:random_fee},
	    		success:function(html){
	    			if(html=='service error'){
						return Layer.alert('支付宝服务维护中，请稍后再试！');
					}else{
						$("div.wechatpay-child").html(html);
						this.t2 = window.setInterval("checkAlipayisOK()",4000);
						wechattime=this.t2;
						window.setTimeout("clearset("+this.t2+")",900000);

						$("div.prev-mian").addClass("none");
						$("div.wechatpay-child").removeClass("none");
						$('.box_bg').remove();
					    $('.box_common').remove();

					}
	    		}
	    });
    });
}

var Alipay = new Alipay();


Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$("#wctxnAmt").on("input",function(){
	var money=parseFloat($(this).val())+parseFloat($(this).next().html());
	var per=0.005;
	var countfee=parseFloat(money*per).toFixed(2)==0?0.01:parseFloat(money*per).toFixed(2);
	var realMoney=(money-countfee).toFixed(2);
	if(isNaN(money)|| realMoney<0){
		$("span.real-in").html("");
	}else{
		$("span.real-in").html(realMoney);
	}
});

$("#paytypeqr").on('change',function(){
	if($(this).val()=='wxqr'){
		$("#weichatPay").attr('onclick','return Wechatpay.confirm();');
	}else if($(this).val()=='aliqr'){
		$("#weichatPay").attr('onclick','return Alipay.confirm();');
	}else{
		Layer.alert('请选择正确的充值类型');
	}
});
