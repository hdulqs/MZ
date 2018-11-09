/**
 * Created by andyk on 2015/10/8.
 */
;(function(window){

    var BitDialog = function (options,callback){
        var _op     = options || {},
            _object = $('<div class="doc_dialog" style="display: none">'+
                '<div class="mod_dialog">'+
                '<div class="mod_hd">' +
                '<span class="icon_wrap"><i class="icon"></i></span>' +
                '<span class="mod_title"></span><div class="mod_option"><a href="#" class="close">×</a></div></div>'+
                '<div class="mod_bd"></div>'+
                '<div class="mod_ft"></div></div></div>'),
            _obj_backdrop = $('<div class="dialog_backdrop" style="display: none"></div>'),
            _wrap     = _op.wrap   ? $(_op.wrap) : $('body'),
            _dialog   = _op.dialog ? $(_op.dialog) : $('.doc_dialog'),
            _trigger  = $(_op.trigger)  || '',
            _backdrop = _op.backdrop ? $(_op.backdrop) : $('.dialog_backdrop'),
            _callback = callback ? callback : null;
        _Init();
        function _Init(){
            if(_dialog.length <= 0){
                var _selector = _dialog.selector;
                //console.log(_selector, /^\./.test(_selector) );
                /^\./.test(_selector) ?  _object.addClass(_selector.replace('.','')) : /^\#/.test(_selector) && _object.attr('id',_selector.replace('#',''));
                _wrap.append(_object);
                _dialog = _object;
            }

            if($(_backdrop).length <= 0){
                _obj_backdrop.addClass(_backdrop.selector.replace('.',''));
                $('body').append(_obj_backdrop);
                _backdrop = _obj_backdrop;
            }

            _dialog._title = _dialog.find('.mod_title');
            _dialog._body  = _dialog.find('.mod_bd');
            _dialog._foot  = _dialog.find('.mod_ft');
            _dialog._close = _dialog.find('.close');
        }

        function _Write(options){
            options.title &&_dialog._title.html(options.title);
            options.content && _dialog._body.html(options.content);
            options.foot && _dialog._foot.html(options.foot);
        }

        function _Show(callback){
            _dialog.show();
            _backdrop.show().addClass('in');
            callback ? callback(_dialog,'show') : _callback&&_callback(_dialog,'show');
        }

        function _Hide(callback,obj){
            _dialog.hide();
            _backdrop.hide().removeClass('in');
            callback ? callback(_dialog,'close') : _callback&&_callback(_dialog,'close',obj);
        }

        _trigger&&_trigger.on('click',function(){
            _Show();
        });

        _dialog._close.on('click',function(){
            _Hide('',$(this));
        });

        this.Close = _Hide;
        this.Show  = _Show;
        this.Write = _Write;

    };

    "function" == typeof define ? define(function() {
        return BitDialog
    }) : "undefined" != typeof exports ? module.exports = BitDialog : window.BitDialog = BitDialog

})(window);