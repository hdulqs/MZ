/**
 * balance
 */
define(function(require, exports,module){
    var $, HurongbiAjax, HB, coin_type, BALANCE, waitBalance,GetBalance,DOM,mNum,OAUTH;
    $ = require('jquery');
    HurongbiAjax = require('./module_ajax');
    HB = require('./module_hb_extend');
    IO  = require('./module_socket_io');
    connect = require('./module_connect');
    mNum = require('./module_number');
    OAUTH     =require('./module_oauth');
    coin_type = $("input[name='coin_type']").val();
    BALANCE = {};
    DOM =  {};
    HRYB['BALANCE'] = BALANCE;
    DOM['dom'] = $(document);
    DOM['convert_total_cny']  = $('.convert_total_cny');
    DOM['convert_total_usd'] = $('.convert_total_usd');
    DOM['convert_total_btc'] = $('.convert_total_btc');
    DOM['convert_net_cny']   = $('.convert_net_cny');
    DOM['convert_net_usd']   = $('.convert_net_usd');
    DOM['convert_net_btc']   = $('.convert_net_btc');
    DOM['cny_available']     = $('.cny_cny_available');
    DOM['cny_btc_available'] = $('.cny_btc_available');
    DOM['cny_ltc_available'] = $('.cny_ltc_available');
    DOM['cny_dsc_available'] = $('.cny_dsc_available');
  
    //可用杠杆额度  cny
    DOM['loan_cny_cny'] = $('.loan_cny_cny');
    
    //可用杠杆额度  coin
    DOM['loan_cny_btc'] = $('.loan_cny_btc');
    
    //买卖费率
    DOM['sellRate'] = $('.sellRate');
    DOM['buyRate'] = $('.buyRate');
    
    //最小买入量,最大卖出量
    DOM['minimum'] = $('.minimum');
    
    //市价最小下单金额
    DOM['minprice'] = $('.minprice');
    
    DOM['cny_frozen']        = $('.cny_cny_frozen');
    DOM['cny_btc_frozen']    = $('.cny_btc_frozen');
    DOM['cny_ltc_frozen']    = $('.cny_ltc_frozen');
    DOM['cny_loan']          = $('.cny_cny_loan');
    DOM['cny_btc_loan']      = $('.cny_btc_loan');
    DOM['cny_ltc_loan']      = $('.cny_ltc_loan');
    DOM['cny_risk_rate']     = $('.cny_risk_rate');
    DOM['cny_total']         = $('.cny_total');
    DOM['cny_net_asset']     = $('.cny_net_asset');
    DOM['usd_available']     = $('.usd_usd_available');
    DOM['usd_btc_available'] = $('.usd_btc_available');
    DOM['usd_frozen']        = $('.usd_usd_frozen');
    DOM['usd_btc_frozen']    = $('.usd_btc_frozen');
    DOM['usd_loan']          = $('.usd_usd_loan');
    DOM['usd_btc_loan']      = $('.usd_btc_loan');
    DOM['usd_risk_rate']     = $('.usd_risk_rate');
    DOM['usd_total']         = $('.usd_total');
    DOM['usd_net_asset']     = $('.usd_net_asset');
    DOM['fee_rate']          = $('.fee_rate');
    DOM['head_balance']      = $('#head_balance');
    DOM['cny_c_a_panel']     = $('cny_cny_available_panel');
    
    
  	if(HRY.user!=undefined&&HRY.user!=""){

  		if(HRY.redispush=="yes"){
	  		HRY.socket_account = connect($_ws_hq_url);
	  		HRY.socket_account.conn.on('change',function(data){
	  	        if(HRY.user==data){
	  	        	window.setTimeout(function(){ 
	  		    		//console.log("刷新账户"+data)
	  		    		HRY.user_room_waitBalance();
	  	    		}, 500);
	  	        }
	  			
	  	    });
  		}
	    
  	}
    
    waitBalance = function (dtd,callback) {
    	//校验用户是否登录
    	if(HRY.user!=undefined&&HRY.user!=""){
    		if(HRY.redispush=="yes"){//使用redis push
    			GetBalance = new HurongbiAjax({"url":HRY.host + '/user/getAccountInfo',"data": {"m": 'user_balance',"coinCode":CURRENT_SYMBOL,"tokenId":$("#tokenId").val()},  damp:true}, function (data) {
    				 if (data.code == 'success') {
    		                Process(data.data);
    		            }
		        });
    		}else{
    			GetBalance = new HurongbiAjax({"url":HRY.host + '/user/getAccountInfo',"data": {"m": 'user_balance',"coinCode":CURRENT_SYMBOL,"tokenId":$("#tokenId").val()}, "refresh":2000, damp:true}, function (data) {
   				 if (data.code == 'success') {
   		                Process(data.data);
   		            }
		        });
    		}
    	    
    	}	
		
    	HB({'name': 'AJAX', 'value': {'GetBalance': GetBalance}});
        
        function Process(data) {
        	try {
	        	 data=JSON.parse(JSON.parse(data).obj);
	        	 DOM['buyRate'].attr("data-tips",Lang.L("dangqianmairu_feilv")+ data.buyFeeRate +"%");
	        	 DOM['sellRate'].attr("data-tips",Lang.L("dangqianmaichu_feilv")+ data.sellFeeRate +"%");
	        	 
	        	 $('.sellrate').val(data.sellFeeRate);
	        	 $('.buyrate').val(data.buyFeeRate);
	        	 
	        	 $('.keepDecimalForCoin').val(data.keepDecimalForCoin);
	        	 $('.keepDecimalForCurrency').val(data.keepDecimalForCurrency);
	        	 
	        	 DOM['minimum'].attr("data-min",data.sellMinCoin);
	        	 DOM['minimum'].attr("data-max",data.oneTimeOrderNum);
	        	 
	        	 //市价最小下单金额
	        	 DOM['minprice'].attr("data-min",data.buyMinMoney);
	        	 
	        	 
	            BALANCE['cny_available'] = data.rmb * 1;
	            BALANCE['cny_cny_available'] = data.rmb * 1;
	            BALANCE['cny_btc_available'] =data.coinHotMoney* 1;
	            BALANCE['cny_dsc_available'] = data.coinHotMoney * 1;
	           // BALANCE['cny_frozen'] = data.ext.CNY.CNY.frozen * 1;
	           // BALANCE['cny_btc_frozen'] = data.ext.CNY.BTC.frozen * 1;
	           // BALANCE['cny_ltc_frozen'] = data.ext.CNY.LTC.frozen * 1;
	            BALANCE['cny_loan'] = data.rMBAccountNetAsse * 1;
	            BALANCE['loan_cny_cny'] = data.rMBAccountNetAsse * 1;
	            BALANCE['loan_cny_btc'] = data.coinAccountNetAsse * 1;
	            BALANCE['cny_btc_loan'] = data.coinAccountNetAsse * 1;
	            BALANCE['cny_ltc_loan'] = data.coinAccountNetAsse * 1;
	            /*BALANCE['cny_risk_rate'] = data.ext.CNY.risk_rate * 1;
	            BALANCE['cny_total'] = data.ext.CNY.total * 1;
	            BALANCE['cny_net_asset'] = data.ext.CNY.net_asset * 1;
	            BALANCE['cny_btc_burst'] = data.ext.CNY['burst_price']['BTC'] * 1;
	            BALANCE['cny_ltc_burst'] = data.ext.CNY.burst_price.LTC * 1;
	
	            BALANCE['usd_available'] = data.ext.USD.USD.available * 1;
	            BALANCE['usd_usd_available'] = data.ext.USD.USD.available * 1;
	            BALANCE['usd_btc_available'] = data.ext.USD.BTC.available * 1;
	            BALANCE['usd_frozen'] = data.ext.USD.USD.frozen * 1;
	            BALANCE['usd_btc_frozen'] = data.ext.USD.BTC.frozen * 1;
	            BALANCE['usd_loan'] = data.ext.USD.loan.USD * 1;
	            BALANCE['usd_btc_loan'] = data.ext.USD.loan.BTC * 1;
	            BALANCE['usd_risk_rate'] = data.ext.USD.risk_rate * 1;
	            BALANCE['usd_total'] = data.ext.USD.total * 1;
	            BALANCE['usd_btc_burst'] = data.ext.USD.burst_price.BTC * 1;
	            BALANCE['usd_net_asset'] = data.ext.USD.net_asset * 1;
	
	            BALANCE['convert_total_cny'] = data.ext.equivalent.CNY.total * 1;
	            BALANCE['convert_net_cny'] = data.ext.equivalent.CNY.net_asset * 1;
	            BALANCE['convert_total_usd'] = data.ext.equivalent.USD.total * 1;
	            BALANCE['convert_net_usd'] = data.ext.equivalent.USD.net_asset * 1;
	            BALANCE['convert_total_btc'] = data.ext.equivalent.BTC.total * 1;
	            BALANCE['convert_net_btc'] = data.ext.equivalent.BTC.net_asset * 1;
	            BALANCE['fee_rate'] = data.ext.fee_rate;*/
	            DOM['dom'].trigger('__GetBalance', data['ext']);
	            InDom();
	            callback&&callback();
    		
			} catch (e) {
			}
        }

        function InDom(){
            $.each(DOM,function(i,v){

                var _l = v.length,
                    _b = (i.indexOf("btc")>0 || i.indexOf("ltc")>0) ? mNum(BALANCE[i],HRY.keepDecimalForCoin) : mNum(BALANCE[i],HRY.keepDecimalForCurrency);
                if(_l === 1){

                    v.attr('data-flaunted') == '0' ? v.html('---').attr('data-flaunt',_b) : v.html(_b).attr('data-flaunt', _b);

                }else if(_l > 1){

                    v.each(function(){
                        var _t =  $(this);
                        _t.attr('data-flaunted') == '0' ? _t.html('---').attr('data-flaunt', _b) : _t.html(_b).attr('data-flaunt', _b);
                    });

                }else{

                }
            });

            if (BALANCE['cny_risk_rate']) {
                $('.cny_loan_rate_yes').show();
                $('.cny_loan_rate_no').hide();
            } else {
                $('.cny_loan_rate_yes').hide();
                $('.cny_loan_rate_no').show();
            }
            if (BALANCE['usd_risk_rate']) {
                $('.usd_loan_rate_yes').show();
                $('.usd_loan_rate_no').hide();
            } else {
                $('.usd_loan_rate_yes').hide();
                $('.usd_loan_rate_no').show();
            }
            dtd.resolve();
            //人民币账户预估爆仓价
            if (BALANCE['cny_btc_burst'] > 0 || BALANCE['cny_ltc_burst'] > 0) {
                if (BALANCE['cny_btc_burst'] > 0) {
                    $('.cny_loan_burst_price_btc').find('.price').html(BALANCE['cny_btc_burst']).end().show().siblings('.cny_loan_burst').hide();
                } else if (BALANCE['cny_ltc_burst'] > 0) {
                    $('.cny_loan_burst_price_ltc').find('.price').html(BALANCE['cny_ltc_burst']).end().show().siblings('.cny_loan_burst').hide();
                }
            } else {
                $('.cny_loan_burst_price_no').show().siblings('.cny_loan_burst').hide();
            }
            //美元账户预估爆仓价
            if (BALANCE['usd_btc_burst'] > 0 ) {
				$('.usd_loan_burst_price_btc').find('.price').html(BALANCE['usd_btc_burst']).end().show().siblings('.usd_loan_burst').hide();
            } else {
                $('.usd_loan_burst_price_no').show().siblings('.usd_loan_burst').hide();
            }
        }
        return dtd;
    };
    
    HRY.user_room_waitBalance = waitBalance;

    module.exports = waitBalance;
});
