/**
 * Created by andyk on 2015/9/17.
 *
 */
;(function(window) {
    BitSwitch = function (option) {
        var _op     = option||{},
            _width  = _op.width || 13,
            _gap    = _op.gap || 1,
            _switch = $(_op.Switch),
            _wrap   = $('<label class="input_switch"></label>');

        /*init*/
        Init();
        function Init(){
            _switch.length > 0 && _switch.wrap(_wrap).after('<b></b><i></i><span></span>');
        }
        /*dom*/
        /**
         * 0 初始 1 focus 2 off 3 on 4 disabled
         *
         */
        function Dom(state,obj){
            var _p = obj.parent(),
                _b = _p.find('b'),
                _i = _p.find('i'),
                _s = _p.find('span'),
                _t = obj.data('text') && obj.data('text').split(','),
                _d = obj.prop('disabled')?1: 0,
                _w = _p.width()*1,
                _x = _w-_gap*2 - _width;

            switch (state){
                case 0:
                    _p.removeClass('focus');
                    _t&&_s.text(_t[1]);
                    break;
                case 1:
                    _p.addClass('focus');
                    break;
                case 2:
                    _i.animate({
                        'left':0,
                        'right':_x
                    },100,'swing');
                    _p.addClass('switch_off').removeClass('switch_on ');
                    _t&&_s.text(_t[1]);
                    break;
                case 3:
                    _i.animate({
                        'left':_x,
                        'right':0
                    },100,'swing');
                    _p.addClass('switch_on').removeClass('switch_off ');
                    _t&&_s.text(_t[0]);
                    break;
                case 4:
                    _p.addClass('disabled');
                    _t&&_s.text(_t[1]);
                    break;
                case 5:
                    _p.removeClass('focus');
                    break;
            }
            _d ? _p.addClass('disabled') : _p.removeClass('disabled');

        }
        /*event*/
        _switch.each(function(){
            var _this = $(this),
                _s    = _this.prop('checked') ? 3 : 0 ;
            Dom(_s,_this);
            _this.on('change',function(){
                _s    = _this.prop('checked') ? 3 : 2;
                Dom(_s,_this);
            }).on('focus',function(){
                Dom(1, _this);
            }).on('blur',function(){
                Dom(5, _this);
            }).on('__Update',function(){
                _s    = _this.prop('checked') ? 3 : 2;
                Dom(_s,_this);
            })
        })
    };

    "function" == typeof define ? define(function() {
        return BitSwitch
    }) : "undefined" != typeof exports ? module.exports = BitSwitch : window.BitSwitch = BitSwitch


})(window);