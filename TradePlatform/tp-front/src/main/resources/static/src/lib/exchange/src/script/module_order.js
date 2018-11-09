/**
 * Created by Jason on 2015/6/1.
 * 待增加本地存储缓存。。。
 * 如果委托已成交即存储当前委托详细信息
 */
define(function (require, exports, module) {

    var $ = require('jquery'),
        A = require('./module_ajax'),
        M = require('./module_tmpl'),
        D = require('./module_md5');
        
    var IO  = require('./module_socket_io');
    var connect = require('./module_connect');

    
    
  	if(HRY.user!=undefined&&HRY.user!=""){
  		if(HRY.redispush=="yes"){
	  		HRY.socket_account = connect($_ws_hq_url);
	  		HRY.socket_account.conn.on('change',function(data){
	  	        if(HRY.user==data){
	  	        	window.setTimeout(function(){ 
	  		    		//console.log("刷新委托"+data)
	  		    		
	  		    		HRY.user_roome_GetOrder({
					         temp: 'tmp_us_order',
					         wrap: 'view_us_order',
					         coin_type: SYMBOL,
					         is_history: 10,
					         damp: true}, 
					         function () {
					         
					     })
					     HRY.user_roome_GetOrder({
					         temp: 'tmp_us_order_history',
					         wrap: 'view_us_order_history',
					         coin_type: SYMBOL,
					         is_history: 20,
					         delay: 0
					     })
	  		    		
	  	    		}, 500);
	  	        }
	  			
	  	    });
  		}
  			    
  	}
  	

    
    //获取当前委托
    //options.temp 模板id
    //options.wrap 包装id
	//options.is_history 是否是历史委托

    function GetOrder(options,change){
    	
        var op   = options || {},
            temp = op.temp,
            wrap = $('#' + op.wrap),
	        coin_type = op.coin_type,
	        is_history = op.is_history,
            damp       = op.damp,
            delay = op.delay == 0 ? 3000 : 3000,
            init  = 0,
            hash  = [0,0],
            oldId = [0,0],
            Fn,
            time  = 4;

        
    	//如果history==10.查当前，否则查历史
    	var  type = "history" 
    	if(is_history=="10"){
    		type = "current"
    	}
    	if(HRY.user==undefined||HRY.user==""){
    		return null;
    	}
    	//"refresh":delay,
    	if(HRY.redispush=="yes"){//使用redis push
    		Fn =  new A({"url":HRY.host + '/user/entrust/rlist',"damp":damp,"data":{"coinCode":coin_type, "type":type,"offset":0,"limit":5,"tokenId":$("#tokenId").val()}},function(data){

        		var _code = data.code;
               
                switch (_code){
                    case 'loading':
                        if(wrap.html() == ''){
                            wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder"><i class="icon_loading"></i></td></tr>');
                        }
                        break;
                    case 'error':
                        wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder">'+data.msg+'</td></tr>');
                        hash  = [0,0];
                        break;
                    case 'success':
                        if(data.code === "success"&&data.data.indexOf("script")==-1&&data.data!=""){
                         	var delegation=eval("("+data.data+")");
                            var _h = D.md5(JSON.stringify(delegation));
                            
                            if(is_history==20){
                                if(_h !== hash[1]){
                                    for(var _i = 0; _i < delegation.length; _i++){
                                        var _t = delegation[_i];

                                        if(_t['id'] > oldId[1] && init){
                                            _t['new_order'] = 1
                                        }else{
                                            _t['new_order'] = 0
                                        }
                                    }
                                    wrap.html(M(temp, delegation));
                                    hash[1] = _h;
                                    if(!init){oldId[1] = delegation.length>0 ? delegation[0]['id']*1 : 0;}
                                    init = 1;
                                }
                            }else{
                                time --;
                                if(data['data'].total == 0){
                                    if(time<=0){
                                        Fn.Stop();
                                        console.log('Order Stop');
                                        time = 4;
                                    }
                                }else{
                                    time = 4;
                                }

                                if(_h !== hash[0]){
                                    wrap.html(M(temp, delegation));
                                    hash[0] = _h;
                                    change && change();
                                }
                            }
                        }else{
                            data.code && wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder"><i class="icon_info"></i> code:' + data.code + ', ' + (data.msg ? data.msg : "") +'</td></tr>');
                            hash  = [0,0];
                        }
                }
            });
        
    	}else{
    		
    		Fn =  new A({"url":HRY.host + '/user/entrust/rlist',"damp":damp,"refresh":delay,"data":{"coinCode":coin_type, "type":type,"offset":0,"limit":5,"tokenId":$("#tokenId").val()}},function(data){

        		var _code = data.code;
               
                switch (_code){
                    case 'loading':
                        if(wrap.html() == ''){
                            wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder"><i class="icon_loading"></i></td></tr>');
                        }
                        break;
                    case 'error':
                        wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder">'+data.msg+'</td></tr>');
                        hash  = [0,0];
                        break;
                    case 'success':
                        if(data.code === "success"&&data.data.indexOf("script")==-1&&data.data!=""){
                         	var delegation=eval("("+data.data+")");
                            var _h = D.md5(JSON.stringify(delegation));
                            
                            if(is_history==20){
                                if(_h !== hash[1]){
                                    for(var _i = 0; _i < delegation.length; _i++){
                                        var _t = delegation[_i];

                                        if(_t['id'] > oldId[1] && init){
                                            _t['new_order'] = 1
                                        }else{
                                            _t['new_order'] = 0
                                        }
                                    }
                                    wrap.html(M(temp, delegation));
                                    hash[1] = _h;
                                    if(!init){oldId[1] = delegation.length>0 ? delegation[0]['id']*1 : 0;}
                                    init = 1;
                                }
                            }else{
                                time --;
                                if(data['data'].total == 0){
                                    if(time<=0){
                                        Fn.Stop();
                                        console.log('Order Stop');
                                        time = 4;
                                    }
                                }else{
                                    time = 4;
                                }

                                if(_h !== hash[0]){
                                    wrap.html(M(temp, delegation));
                                    hash[0] = _h;
                                    change && change();
                                }
                            }
                        }else{
                            //data.code && wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder"><i class="icon_info"></i> code44444:' + data.code + ', ' + (data.msg ? data.msg : "") +'</td></tr>');
                        	data.code && wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder"><i class="icon_info"></i>'+Lang.L("zanwuweituo")+'</td></tr>');
                            hash  = [0,0];
                        }
                }
            });
        
    	
    	}

        return Fn;
    }
    HRY.user_roome_GetOrder = GetOrder;

    //获取委托详情
    function GetDetails(options){
        var op   = options || {},
            temp = op.temp,
            wrap = $(op.wrap),
	        coin_type = op.coin_type,
            id   = op.id,
	        type = op.type;

        if(!temp || !wrap){
            return
        }
        A({"url":'/trade/entrust_detail',"data":{"coin_type":coin_type,"order_id":id,"delegation_type":type}},function(data){
            var _code = data.code;
            switch (_code){
                case 'loading':
                    wrap.html('<div class="align_center p_t_30 no_barder"><i class="icon_loading"></i>'+data.msg+'</div>');
                    break;
                case 'error':
                    wrap.html('<div class="align_center p_t_30 no_barder">'+data.msg+'</div>');
                    break;
                case 'success':
                    if(data['data'].code===0){
                        wrap.html(M(temp, data['data']))
                    }else{
                        data.code && wrap.html('<tr><td colspan="100" class="align_center p_t_30 no_barder">code:'+data['data'].code +','+ data['data'].msg ? data['data'].msg : '' +'</td></tr>');
                    }
                    break;
            }
        });
    }

    
    //取消委托单
    function CancelOrder(options,callback){debugger
        var op = options || {},
	        coin_type = op.coin_type,
            orderArr = typeof op.id == 'object' ? op.id.join(',') : op.id;
           // ids=orderArr.split(",");
            ids = orderArr;
            var entrustPrice = op.entrustPrice;
            var type = op.type;
        A({"url":HRY.host + '/user/trades/cancelExEntrust.do',"data":{"coinCode":CURRENT_SYMBOL,"entrustNums":ids,"entrustPrice":entrustPrice,"type":type,"tokenId":$("#tokenId").val()},"type":"POST"},function(data){
            var _code = data.code;
            switch (_code){
                case 'loading':
                    callback && callback(data);
                    break;
                case 'error':
                    callback && callback(data);
                    break;
                case 'success':
                    callback && callback(JSON.parse(data['data']));
                    break;

            }
        });
    }
    
    //取消全部委托单
    function CancelAllOrder(options,callback){debugger
        var op = options || {},
	        coin_type = op.coin_type,
            orderArr = typeof op.id == 'object' ? op.id.join(',') : op.id;
           // ids=orderArr.split(",");
            ids = orderArr;
            //HRY.host + HRY.modules.exmain + 'entrust/cancelallcoinCodeExEntrust.do'
        A({"url":HRY.host + '/user/trades/cancelCustAllExEntrust.do',"data":{"coinCode":CURRENT_SYMBOL,"entrustNums":ids},"type":"POST"},function(data){
            var _code = data.code;
            switch (_code){
                case 'loading':
                    callback && callback(data);
                    break;
                case 'error':
                    callback && callback(data);
                    break;
                case 'success':
                    callback && callback(JSON.parse(data['data']));
                    break;

            }
        });
    }


    exports.GetOrder    = GetOrder;
    exports.GetDetails  = GetDetails;
    exports.CancelOrder = CancelOrder;
    exports.CancelAllOrder=CancelAllOrder;

});


