/**
 * Created by Jason on 2015/5/25.
 */
;(function(){
    var WIN = window,
    Tips = function(options,callback){
        var _option    = options || {},
            domTrigger = _option.tip ? $(_option.tip) : '',
            control    = _option.control,
            text       = _option.text,
            domTip,
            tipArrow,
            data,
            delay,
            state;

        function Init(){
            domTip = $('.bit_tips');
            if(domTip.length<=0){
                var _html = '<div class="bit_tips">'+
                    '<div class="content"></div>'+
                    '<button class="confirm btn btn_link size_xs" type="button"></buton>'+
                    '<button class="cancel btn btn_link size_xs" type="button"></button>'+
                    '<i class="arrow"></i>'+
                    '<i class="close">×</i></div>';
                $('body').append(_html);
            }
            domTip   = $('body .bit_tips');
            tipArrow = domTip.find('.arrow');
        }

        function TipHide(obj,time,t){
            if(!domTip || domTip.length<=0) return;
            time ? delay = setTimeout(function(){
                domTip.fadeOut(10)
            },time) : domTip.hide();

            t || callback && callback(obj,'close');
            if (typeof(document.onselectstart) != "undefined") {
                document.onselectstart = new Function("return false");
            }
            setTimeout(function(){
                if (typeof(document.onselectstart) != "undefined") {
                    document.onselectstart = new Function("return true");
                }
            },300)
        }

        function TipShow(obj,event){
            var _option = domTip.find('.option'),
                _close  = domTip.find('.close'),
                _confirm= domTip.find('.confirm'),
                _cancel = domTip.find('.cancel'),
                _text   = obj.attr('data-tips')||text,
                _btn    = obj.attr('data-tips-btn') && obj.attr('data-tips-btn').split(','),
                _clo    = obj.attr('data-tips-close'),
                _width  = obj.attr('data-tips-width') || '',
                _po     = obj.attr('data-tips-position') || 'default';

            if(!callback){
                _show()
            }else if(callback(obj, 'show') !== false){
                _show()
            }

            function _show(){
                clearTimeout(delay);
                domTip.find('.content').html(_text);

                _width ? domTip.css({
                    "min-width": _width + 'px',
                    "width"    : _width + 'px'
                }):domTip.css({
                    "min-width":'',
                    "width"    :''
                });

                if($.trim(_clo)==='true'||event=='click'){
                    domTip.find('.close').show();
                }else{
                    domTip.find('.close').hide();
                }
                if(_btn){
                    _confirm.html(_btn[0]||'').show();
                    _cancel.html(_btn[1]||'').show();
                }else{
                    _confirm.hide();
                    _cancel.hide();
                }
                Position(obj,_po);
            }
        }

        function TipEvent(obj){
            var _obj = obj;
            domTip.unbind('click');

            domTip.on('click','.confirm',function(){
                TipHide(_obj);
                callback && callback(_obj,'confirm');
                return false
            });
            domTip.on('click','.close, .cancel',function(){
                TipHide(_obj);
            });
        }

        function Position(obj,po){
            var _x  = obj.offset().left,
                _y  = obj.offset().top,
                _w  = domTip.outerWidth(true),
                _h  = domTip.outerHeight(true),
                _ow = obj.width(),
                _po = po || 'default',
                _l;
            if(isNaN(parseFloat(_po))){
                if(_po == 'left'){
                    _l = _x;
                    domTip.removeClass('center right');
                }
                if(_po == 'center'){
                    _l = _x - _w/2 + _ow/2;
                    domTip.addClass('center');
                    domTip.removeClass('right')
                }
                if(_po == 'right'){
                    _l = _x - (_w - _ow/2 - 25);
                    domTip.removeClass('center');
                    domTip.addClass('right')
                }
                if(_po == 'default'){
                    _l = _x + _ow/2 - 25;
                    domTip.removeClass('center right');
                }

                tipArrow && tipArrow.css('left', '')
            }else{
                _l = _x + _po*1;
                _po*1<0 && tipArrow.css('left', -(_po*1)+10)
            }

            domTip.css({
                'left' : _l,
                'top'  : _y - (_h + 15)
            }).show()
        }

        if(_option.tip){
            $(document).on('click',_option.tip,function(e){
                Init();
                var _this    = $(this),
                    _trigger = _this.attr('data-tips-trigger');
                if(_trigger === 'click'){
                    TipShow(_this,'click');
                    TipEvent(_this);
                }
                e.stopPropagation();
            });
            $(document).on('mouseover',_option.tip,function(){
                Init();
                var _this    = $(this),
                    _trigger = _this.attr('data-tips-trigger');
                if(_trigger != 'click'){
                    if(_this.is(':input')){
                        _this.focus(function(){
                            TipShow(_this);
                        })
                    }else{
                        TipShow(_this);
                    }
                }
            });

            $(document).on('mouseout',_option.tip,function(){
                var _this    = $(this),
                    _trigger = _this.attr('data-tips-trigger');
                if(_trigger != 'click') {
                    if(_this.is(':input')){
                        _this.blur(function(){
                            TipHide(_this,1)
                        });
                    }else{
                        TipHide(_this, 1)
                    }
                }
            })

        }else{

        }


        function Write(options){
            var _domTrigger = options.tip ? $(options.tip) : '',
                _content    = options.content || '';
            Init();
            text = _content;
            TipShow(_domTrigger);
            TipEvent(_domTrigger);
        }

        domTip && $(WIN).on('click',function(e){
            (e.target !== domTip[0] && e.target.parentNode!== domTip[0]) && TipHide(domTip,0,1);
        });

        this.Write = Write;
        this.Close = TipHide;
    };


    "function" == typeof define ? define(function() {
        return Tips
    }) : "undefined" != typeof exports ? module.exports = Tips : window.Tips = Tips;
})();
