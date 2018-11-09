/**
 * Created by andyk on 2015/11/17.
 * 闪电手通知层
 */
;(function(){
    var _Tout = [];
    function Notice(option,callback){
        var _this = this,
            _op   = option || {},
            _msg  = _op.message?_op.message : '',
            _type = _op.type || 'succeed',
            _long = _op.long,
            _wrap = $('<div id="notice" class="notice"><a href="" class="close">×</a><div class="message">'+_msg+'</div></div>'),
            _body = $('body'),
            _nBody = _wrap.find('.message'),
            _nClose= _wrap.find('.close');

        _this.Init = function (t){
            var _s;
            if(_body.find('#notice').length>0){
                _wrap = _body.find('#notice');
                _nBody = _wrap.find('.message');
                _nClose= _wrap.find('.close');
                t!='close' && _wrap.addClass('reappear');
                _s = false;
            }else{
                _body.append(_wrap);
                _s = true;
            }
            _type == 'error' ? _wrap.addClass('error').removeClass('succeed') : _wrap.removeClass('error').addClass('succeed');

            setTimeout(function(){
                _wrap.removeClass('reappear');
            },1000);

            return _s;
        };

        _this.Open = function(){
            var _s = _this.Init();
            _this.CloseNow = 0;
            if(!_s){
                return;
            }
            _wrap.removeClass('notice_close').addClass('notice_open');
            RemoveC(_wrap,'notice_open')
        };

        _this.Close = function(){
            _this.Init('close');
            _this.CloseNow = 1;
            _wrap.addClass('notice_close').removeClass('notice_open');
            _Tout[1] = setTimeout(function(){
                _wrap.remove();
            },300)
        };

        _this.Write = function(msg){
            _nBody.html(msg || _msg);
        };

        function RemoveC(o,c,t){
            var _t = t||200;
            setTimeout(function(){
                o.removeClass(c);
            },_t)
        }
        !function(){
            _this.Open();
            _this.Write();

            clearTimeout(_Tout[0]);
            clearTimeout(_Tout[1]);

            _Tout[0] = _long &&  setTimeout(
               function(){
                   _this.Close()
               },
                _long);

            _nClose.click(function(){
                _this.Close();
                return false;
            });
            _wrap.hover(function(){
                clearTimeout(_Tout[0]);
            },function(){
                if(_body.find('#notice').length<=0){return}
                _Tout[0] = _long &&  setTimeout(
                function(){
                    _this.Close()
                },
                _long);
            })
        }();
        return _this;
    }
    "function" == typeof define ? define(function() {
        return Notice
    }) : "undefined" != typeof exports ? module.exports = Notice : window.Notice = Notice;
})();