/**
 * Created by andyk on 2015/11/2.
 * 闪电手主框架
 */
define(function (require) {
      var $         = require('jquery'),
        J         = require('./module_this_jack'),
        UTT       = require('./module_time'),
        mVad      = require('./module_validator'), //表单校验
        mMd5      = require('./module_md5'),
        mDialog   = require('./module_dialog'),
        mTip      = require('./module_tip'),
        LM        = require('./page_l_market'),
        LT        = require('./page_l_trade'),
        LC        = require('./page_l_chart'),
        LN        = require('./page_l_notice'),
        OAUTH     =require('./module_oauth'),
        COINACTION     =require('./module_coin'),
        APPCONFDATA     =require('./module_appseting'),
        BAL       = (window.UID && document.getElementById('balance')) ? require('./module_balance') : '',
        DOM       = $(document),
        WIN       = $(window),
        $areas    = [WIN,$('body'),$('#doc_head'),$('#doc_body'),$('#doc_foot'),$('#main'),$('#bar'),$('#chart'),$('#order'),$('#depth'),$('#trade'),$('#overview'),$('#market_info'),$('#copyright')],
        $sizes    = [],
        DTD = $.Deferred(),
        docHidden = 0,
        browserHidden,
        visibilityChange;

        HRYB.GLOBAL = {};
        $(".symbol").html(CURRENT_SYMBOL.split("_")[0]).parent().attr("href",HRY.host+"/#/user/loan").attr("target","_blank").css("color","#707070");
        $(".CURRENCY_STR").html(CURRENCY_STR).parent().attr("href",HRY.host+"/#/user/loan").attr("target","_blank").css("color","#707070");
        $(".CURRENCY_SYMBOL").html(CURRENCY_SYMBOL);
        
        
        //Document Hidden
        if (typeof document.hidden !== "undefined") {
            browserHidden = "hidden";
            visibilityChange = "visibilitychange";
        } else if (typeof document.mozHidden !== "undefined") {
            browserHidden = "mozHidden";
            visibilityChange = "mozvisibilitychange";
        } else if (typeof document.msHidden !== "undefined") {
            browserHidden = "msHidden";
            visibilityChange = "msvisibilitychange";
        } else if (typeof document.webkitHidden !== "undefined") {
            browserHidden = "webkitHidden";
            visibilityChange = "webkitvisibilitychange";
        }
        function handleVisibilityChange() {
            if (document[browserHidden]) {
                docHidden  = 1;
            } else {
                docHidden  = 0;
            }
            HRYB.GLOBAL['docHidden'] = docHidden;
            $(document).trigger('DoDocHidden')
        }
        if (typeof document.addEventListener !== "undefined" || typeof document[browserHidden] !== "undefined") {
            document.addEventListener(visibilityChange, handleVisibilityChange, false);
        }


    /*****共享*****/
    HRYB.UTT = UTT;
    AR = $areas;

    /*****Frame*****/
    function Frame(){
        var _this = this;

        /*Close*/
        _this.Close = function(obj,callback){
            var _o = [];
            if(obj){
                Array.prototype.push.apply(_o, obj);
            }else{
                Array.prototype.push.apply(_o, _this);
            }

            $.each(_o,function(){
                var _t = $(this);
                _t.css({'height':'','width':''});
                !_t.hasClass('close') && _t.addClass('close');
            });
            callback && callback('close',_this)
        };

        /*Open*/
        _this.Open = function(obj,callback){
            var _o = [];
            if(obj){
                Array.prototype.push.apply(_o, obj);
            }else{
                _o = _this;
            }
            if(_o.length >1){
                $.each(_o,function(i,c){
                    var _t = $(this);
                    _t.hasClass('close') && _t.removeClass('close');
                    c.DragSize && c.SetSize('',c.DragSize);
                })
            }else{
                _o.hasClass('close') && _o.removeClass('close');
                _o.DragSize && _o.SetSize('',_o.DragSize);
            }
            callback&&callback('open',_this)
        };

        /*Toggle*/
        _this.Toggle = function(obj,type,handle,minSize,callback){
            var _o = obj ? obj : _this,
                _min = minSize!==undefined ? minSize : '',
                _state;

            _o.on('click',handle,function(){
                if(_o.hasClass('close')){
                    !type ?
                        _o.removeClass('close') :
                    type == 'height' ?
                        _this.SetSize('',['initial',''],function(o){
                            _o.removeClass('close');
                        }) :
                        _this.SetSize('',['', 'initial'],function(o){
                            _o.removeClass('close');
                        });
                    _state = 'show';
                }else{
                    !type ?
                        _o.addClass('close') :
                    type == 'height' ?
                        _o.SetSize('',['initial', _min],function(o){
                        _o.addClass('close')
                    }) :
                        _o.SetSize('',[_min, 'initial'],function(o){
                        _o.addClass('close')
                    });
                    _state = 'close';
                }
                callback&&callback(_state)
            });
        };
        /*Dragging*/
        _this.Drag = function (type,minSize,callback){
            var _handle,
                _mouse = [],
                _min = minSize!==undefined ? minSize : '',
                _max   = [800,375],
                _drag_size = [],
                _temp_size = [],
                _call;
                
            _this.on('mousedown','.handle',function(e){
                e.stopImmediatePropagation();
                _handle = $(this);
                _mouse[0] = e.pageX;
                _mouse[1] = e.pageY;
                _temp_size = [_this.Size[0],_this.Size[1]];
                //_this.removeClass('close');
                _handle.attr('data-active',1).addClass('active');
                _this.addClass('dragging');
            });

            _this.on('mouseup',function(){
                _handle && _handle.attr('data-active',0).removeClass('active');
                _this.removeClass('dragging');
                if( _handle && !(_handle.attr('data-active') * 1)){
                    if(type == 'width' && _drag_size[0] <= _min){
                        _this.Close();
                        _this.DragSize[0] = 'initial';
                    }else{
                        _this.removeClass('close')
                    }

                    if(type == 'height' && _drag_size[1] <= _min){
                        _this.Close();
                        _this.DragSize[1] = 'initial';
                    }else{
                        _this.removeClass('close')
                    }

                    _call && callback&&callback(_drag_size,_this);
                    _drag_size = [];
                    _call = false;
                }
            });

            DOM.on('mousemove',function(e){
                e.stopImmediatePropagation();
                _mouse[2] = e.pageX;
                _mouse[3] = e.pageY;
                if(  _handle &&  _handle.attr('data-active') * 1){
                    var _s = [(type == 'width' ? _drag_size[0] : 'initial'),(type == 'height' ? _drag_size[1] : 'initial')];
                    if(_drag_size.length> 0){
                        _this.SetSize('',_s);
                        _this.DragSize = _s;
                    }
                    _drag_size.length> 0 && callback && callback(_drag_size,_this);
                    _call = _call || true;
                }else{
                    return
                }

                _drag_size    = [(_mouse[0] - _mouse[2]) + _temp_size[0], _mouse[1] - _mouse[3] + _temp_size[1]];
                _drag_size[0] = _drag_size[0] > _max[0] ? _max[0] : _drag_size[0];
                _drag_size[1] = _drag_size[1] > _max[1] ? _max[1] : _drag_size[1];

            }).on('mouseup',function(){
                _handle && _handle.attr('data-active',0).removeClass('active');
            });
        };

        /*GetMouse*/
        _this.GetMouse = function(){
            var _position = [];
            _this.on('mousedown',function(e){
                _position[0] = e.pageX;
                _position[1] = e.pageY;
            }).on('mousemove',function(e){
                _position[2] = e.pageX;
                _position[3] = e.pageY;
                _position[4] = _position[2] - _position[0] > 0 ? _position[2] - _position[0] : 0;
                _position[5] = _position[3] - _position[1] > 0 ? _position[3] - _position[1] : 0;
            });
            _this.mouseXY = _position;
        };

        /*GetSize*/
        _this.GetSize = function(obj){
            var _o = obj ? obj : _this,
                _s = [];
            if(_o.is && _o.outerWidth){
                _s = _o.is(':hidden')?[0,0] : [_o.outerWidth(true), _o.outerHeight(true)];
            }

            return _s;
        };
        /*SetSize*/
        _this.SetSize = function(obj,arr,callback){
            var _o = obj ? obj : _this;
            arr[0]!='initial' && _o.css("width" , arr[0]);
            arr[1]!='initial' && _o.css("height", arr[1]);
            arr[2]!='initial' && _o.css('left'  , arr[2]);
            arr[3]!='initial' && _o.css('top'   , arr[3]);
            callback&&callback(_o)
        };

        /*选项卡*/
        _this.Tab = function (tab,content,callback,index,condition){
            if(index!=undefined && typeof index == 'number'){
                _this.find(tab).eq(index*1).addClass('cur').siblings().removeClass('cur');
                _this.find(content).eq(index*1).show().siblings(content).hide();

                callback && callback(index);
                return;
            }
            _this.on('click',tab,function(){
                var _t = $(this),
                    _i = _t.index(),
                    _c = condition && condition(_this);
                    if(_c===false) return false;
                    _t.addClass('cur').siblings().removeClass('cur');
                    _this.find(content).eq(_i).show().siblings(content).hide();
                    callback && callback(_i);

            })
        };

        return _this;
    }

    function Resize(setChart){
        $sizes = AreaSize();
        SetLayout(setChart);
    }

    function AreaSize(){
        var _sizes = [];
        for( var _i in $areas){
            _sizes[_i] = WIN.GetSize($areas[_i]);
            $areas[_i].Size = WIN.GetSize($areas[_i]);
        }
        return _sizes;
    }

    function SetLayout(setChart){
        var _mW = $sizes[1][0] - $sizes[6][0] ,
            _cH = $sizes[0][1] - 40 - $sizes[8][1] - $sizes[13][1]-35,
            _dH = $sizes[0][1] - 40 - ($sizes[10][1] > $sizes[11][1] ? $sizes[10][1] : $sizes[11][1]  ),
            _bH = $sizes[0][1] - 40,//win-head-foot
            _mB = Math.floor(_dH - (5 + 26) - (_dH - (5 + 26 + 50)) % 42);
        WIN.SetSize($areas[12],['initial',_dH-5]);
        WIN.SetSize($areas[3],[$sizes[1][0],_bH]);
        WIN.SetSize($areas[5],['initial',_bH]);
        WIN.SetSize($areas[6],['initial',_bH]);
        WIN.SetSize($areas[7],['initial',_cH]);
        WIN.SetSize($areas[9],['initial',_dH]);
        WIN.SetSize($('.market_body'),['initial', _mB<=8 ? 20 : _mB ]);
        _mW >= 1130 ? $areas[8].addClass('column') : $areas[8].removeClass('column');
        $sizes[1][0] <= 1000 ? $areas[1].css('overflow-x','auto') : $areas[1].css('overflow-x','hidden');
        if(!WIN['LayoutInit'] || setChart){
            DOM.trigger('__SetChart',[[_mW,_cH]]);
        }
        WIN['LayoutInit'] = 1;
        
    }


    /*****Init*****/
    !function(){
    	//校验用户是否登录
    	OAUTH.GetOauth();
    	//获取币种信息
    	COINACTION.GetCoinList();
    	//获取系统信息
    	APPCONFDATA.GetAppConfigData();
        J(Frame,WIN);
        J(Frame,$areas[8]);
        J(Frame,$areas[6]);
        J(Frame,$areas[10]);
        var _dLoading = $('#loading'),
            _diaLogin =  new mDialog({'dialog':'#login_dialog'},function(dialog,type){
                if(!UID && type=='close'){
                    WIN.Close([ $areas[10]],function(){
                        WIN.trigger('__RESIZE',['','',1])
                    })
                }
            }),
            _diaTwoFactor = new mDialog({'dialog':'#two_factor_dialog'},function(){

            });
        $areas[8].Drag('height','80',function(){
            WIN.trigger('__RESIZE',['',1, 2]);
        });

        //$areas[6].Toggle('','width','.handle','',function(){
        //    WIN.trigger('__RESIZE');
        //});

        $areas[10].Toggle('','height','.panel_trigger','',function(t){
            if(!UID){
                //_diaLogin.Show();
            }
            t=='close' ? $areas[8].Close(
                '',
                function(){
                    WIN.trigger('__RESIZE',['','',3])
                }
            ) : $areas[8].Open(
                '',
                function(){
                    WIN.trigger('__RESIZE',['','',4])
                }
            );
            WIN.trigger('__RESIZE',[150,5]);
        });

        $areas[10].Tab('.tabs li','.panel',function(i){
            if(!UID){
                //_diaLogin.Show();
            }
            WIN.Open([ $areas[8], $areas[10]], function(){
                WIN.trigger('__RESIZE',['',1,6]);
            });
            DOM.trigger('__OrderType',i)
        });

       $areas[8].Tab('.tabs li','.order_col',function(i){
           i ? $areas[8].addClass('current'):$areas[8].removeClass('current')
        },'', function(t){return !t.hasClass('column')});

        $areas[10].is(':visible') && $areas[11].css('padding-bottom','30px');
        var $balance = $('#balance');
        WIN.Toggle($balance,'','.bar','',function(t){
            console.log(t)
        });
        $balance.blur(function(){
            $balance.addClass('close')
        });
        /*Event*/
        WIN.on('__RESIZE',function(e,time,setChart,type){
            var _time = time || 0;
            WIN['SetChart'] = 0;
            _time ? setTimeout(function(){
                Resize(setChart);
            },_time) :  Resize(setChart);

        }).on('load',function(){
            WIN.trigger('__RESIZE',['','',7])
        }).on('resize',function(){
            WIN.trigger('__RESIZE',['','',8])
            if(WIN['SetChart']){
                return false
            }
        });

        DOM.on('__TradeTab',function(e,i){
            $areas[10].Tab('.tabs li','.panel','',i);
        }).on('__SetChart',function(e,x){
            WIN['SetChart'] = 1;
            _Chart.rd&&_Chart.rd({
                w: x[0],
                h: x[1],
                fs: 1
            });
        }).on('__MarketData',function(){
            if(_dLoading.is(':visible')){
                _dLoading.addClass('out');
                WIN.trigger('__RESIZE',['','',9]);
                setTimeout(function(){
                    _dLoading.remove();
                },750)
            }
        });

        /*载入资产*/
        BAL && $.when(BAL(DTD,function(){
            $('#balance').data('refresh') || HRYB['AJAX']['GetBalance'].Stop();
        })).done(function(){
            HRYB['AJAX']['GetBalance'].Play();
            $(document).on('DoDocHidden',function(){
               !docHidden ? HRYB['AJAX']['GetBalance'].Play() : HRYB['AJAX']['GetBalance'].Stop();
            });
        });
         
        /*载入图表*/
        var _Chart = LC('chart', {
            border: 0,
            currentTools: 'CrossLine',
            symlist: [SYMBOL],
            symbol: 0,
            vIndicator: 'MACD',
            timeShare: 1,
            schemes: 'lightning',
            drawRange: 75 / 100,
            level: 4,
            version: '151201',
            url: $_ws_hq_url
        });
        window._Chart =_Chart;
        
        Period('#period',function(data){
        	
        	window.localStorage.clear();//清空缓存
            data && _Chart[data[0]] && _Chart[data[0]](data[1]);
        });
        Period('#indicator',function(data){
        	
            data && _Chart[data[0]] && _Chart[data[0]](data[1]);
        });
        /*图表周期*/
        function Period(obj,callback){       	
        	window.localStorage.clear();//清空缓存
            var _dPeriod = $(obj);
            _dPeriod.on('click','span',function(){
            	
                var _t      = $(this),
                    _act    = _t.data('act'),
                    _val = _t.data('val');
                _t.addClass('cur').siblings().removeClass('cur');
                callback && callback([_act,_val])
            })
        }

        $('#chart_zoom').click(function(){
            var _t =$(this);
            if(_t.hasClass('zoom')){
                WIN.Open([$areas[6],$areas[8]],function(){
                    WIN.trigger('__RESIZE',['',1,10])
                });
                _t.removeClass('zoom');
            }else{
                WIN.Close([$areas[6],$areas[8]],function(){
                    WIN.trigger('__RESIZE',['',1,11])
                });
                _t.addClass('zoom');
            }
        });

        //登录表单
        mVad({
            forms:$('.form_login'),
            tip:'.form_tip',
            changes:function(obj){
                var _obj = $(obj),
                    _group = $(obj).parents('.group');
                if(_obj.attr('data-write')){
                    PC.RapidWrite({"input":_obj})
                }
                _group.find('.v_info').hide();
                _group.find('.group_help').show()
            },
            beforeSubmit:function(_form){
                var  _btn      = _form.find(':submit');
                _form.attr('action',HRY.host+HRY.modules.oauth+"loginService.do");
                BtnSubmit = _btn.text();
                _btn.prop('disabled',true).text(Lang.L('Loading'));
            },
            postData:function(data){
                var _data = [];
                $.each(data, function(i, field){
                    field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':mMd5.hbmd5(field['value'])});
                	
                	//field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':field['value']});
                });
                return _data;
            },
            callback:function(_data,_form){
            	
            	_data=JSON.parse(_data);
                var _tip = $(_form).find('.form_tip'),
                    _btn  = _form.find(':submit');
                _btn.prop('disabled',false).text(BtnSubmit);
                if(!_data.success){
                    var _p = $('#by_phone'),
                        _v = $('#vcode_p'),
                        _c1 = $('#v_code');

                    if(_data.code == 15){
                        _v.show()
                    }
                    _c1.attr('src',HRY.host + HRY.modules.oauth + "/validcode/registcode.jpg?r=" + Math.random());
                    _tip.html(_data.msg).addClass('v_error')
                } else {
                	
                	 window.location.reload();
                    /*if(_data.url) {
                        if(/twofactor/.test(_data.url)){
                            _diaTwoFactor.Show();
                            //window.location.href = '/account/login.do?a=show&step=twofactor&backurl=/k'
                        }else if(/\/trade/.test(_data.url)){
                            window.location.reload()
                        }else{
                            window.location.href = _data.url;
                        }
                    }*/
                }
            },
            ajaxError:function(_form){
                var _tip = _form.find('.form_tip'),
                    _btn = _form.find(':submit');
                _tip.html(window.Texts && Texts['NetworkError']).addClass('v_error')
                _btn.prop('disabled',false).text(BtnSubmit);
            }
        });

        $('#v_code').click(function(){
            var _t = $(this);
            _t[0].src = HRY.host + HRY.modules.oauth + "/validcode/registcode.jpg" + Math.random();
        });
        
        //add by zongwei 20180502 begin
        function datainit(){
        	 _Chart["setTime"]("60min");
        }
        datainit();
        //add by zongwei 20180502 end
        
        /*tip*/
        mTip({"tip": '.bind_tips'}, function (o,type) {});

    }();
});