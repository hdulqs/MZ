/**
 * Created by Jason on 2014/10/10.
 */
;(function(window){

    var BitValidator = function(options){
    	
        var _language ={
                'error'        : L('错误'),
                'success'      : L('校验通过'),
                'undefined'    : L('未定义'),
                'error_min'    : L('数值小于'),
                'error_max'    : L('数值大于'),
                'error_submit' : L('表单提交出错'),
                'error_input'  : L('输入错误'),
                'error_null'   : L('请输入信息')
            },
            _dataType = {
                /*是否为空*/
                "*":/[\w\W]+/,
                /*数字*/
                "n":/^\d+\.?\d*$/,
                /*特殊字符*/
                "s":/^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]+$/,
                /*手机号*/
                "m":/^1[0-9]{10}$/,
                /*通用电话*/
                "p":/^[0-9]{5,11}$/,
                /*邮箱*/
                "e":/^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
                /*链接*/
                "url":/^(\w+:\/\/)?\w+(\.\w+)+.*$/,
                /*汉字或字母*/
                "ZEN":/^(?!_)(?!.*?_$)([a-zA-Z0-9_.\u4e00-\u9fa5\s])*$/,
                /*汉字或字母不能数字，用于姓名*/
                "NAME":/^(?!_)(?!.*?_$)([·a-zA-Z_.\u4e00-\u9fa5\s])*$/,  
                //仅字母
                "EN":/^(?!_)(?!.*?_$)([a-zA-Z_\s.])*$/,
                //字母或数字
                "NEN":/^(?!_\-)(?!.*?_$)([a-zA-Z0-9\s\,\'.])*$/,
                //除一些特殊字符可中文
                "SPE":/^([^/\\/:<>!\*\|"\?\$\=])*$/,
                "pwd":/(?!\d+$)[\dA-Za-z\W]/,
                /*数字对比*/
                "num_range":function(obj){
                    var _min = Number($(obj).attr('data-min')),
                        _max = Number($(obj).attr('data-max')),
                        _msg = $(obj).attr('data-msg-error') ? $(obj).attr('data-msg-error').split(',') : [],
                        _val = $(obj).val(),
                        _mes = '';


                    if(/^\d+\.?\d*$/.test(_val)){
                        if(_val<_min){
                            if(_msg[1]){
                                if(_msg[1].indexOf('@@') > 0){
                                    _mes = _msg[1].replace('@@',_min)
                                }else{
                                    _mes = _msg[1];
                                }
                            }else{
                                _mes = _language.error_min +_min
                            }
                            return _mes
                        }
                        if(_val>_max){
                            if(_msg[2]){

                                if(_msg[2].indexOf('@@') > 0){
                                    _mes = _msg[2].replace('@@',_max)
                                }else{
                                    _mes = _msg[2];
                                }
                            }else{
                                _mes = _language.error_max +_max
                            }
                           // console.log(_val,_msg)
                            return _mes
                        }
                    }else{
                        return false
                    }
                    return true;
                },
                /*两个值对比*/
                "compare":function(obj){
                    var _obj     = $(obj),
                        _form    = _obj.parents('form'),
                        _val     = _obj.val(),
                        _com     = _obj.data("compare"),
                        _compare = /^\./.test(_com) ? _form.find(_com).val() :_form.find('[name="'+_com+'"]').val();
                    return _val == _compare;
                }
            },
            _default = {
                //表单对象
                forms:null,
                /*默认设置*/
                /*表单验证前*/
                beforeSend:null,
                /*表单验证中未通过*/
                process:null,
                /*表单验证前*/
                beforeValidation:null,
                /*表单验证通过提交前*/
                beforeSubmit:null,
                /*表单每次提交*/
                formSubmit:null,
                /*提交后回调*/
                callback:null,
                /*单项校验返回结果:[元素,1:为空 2:未通过]*/
                single:null,
                /*change 返回当前DOM元素*/
                changes:null,
                /*是否忽略隐藏元素*/
                ignoreHidden:true,
                /*ajax方式提交*/
                ajaxPost:true,
                /*ajax提交出错*/
                ajaxError:null,
                /*是否即时验证*/
                instant:false,
                /*自定义提交数据*/
                postData:null,
                /*tip提示*/
                tip:'',
                /*验证表单错误后是否获取input焦点*/
                is_focus:true,
                dataType:{}
            },
            _class = {
                /*默认ClassName*/
                class_name:{
                    input:{
                        'default':'input_text',
                        'success':'',
                        'error'  :'input_text_red'
                    },
                    msg:{
                        'default':'v_info',
                        'success':'v_success',
                        'error'  :'v_error'
                    }
                }
            },
            _option    = $.extend(_default, _class, options),
            _data_type = $.extend(_dataType, _option.dataType, $.ValidatorDataType),
            _form      = $(options.forms),
            _record    = false,
            _init      = {

                /*初始化*/
                fn_init:function(obj){
                    obj.attr('validator','true');

                    /*按钮初始化*/
                    var _submit = obj.find(':submit');
                    _submit.prop('disabled') && !_submit.data('disabled') && _submit.prop('disabled',false);
                    /*验证前执行*/
                    _option.beforeSend && _option.beforeSend(obj);

                    /*即时校验*/
                    if(_option.instant){
                        /*使用即时验证*/
                        obj.on('change',function(o){
                            _init.fn_find(obj);
                            //_option.changes && _option.changes(o.target);
                        })
                    }else{
                        obj.on('change',function(o){
                            _init.fn_msg(o.target,3);
                           _option.changes && _option.changes(o.target,obj);
                        })
                    }

                    /*提交时校验*/
                    obj.submit(function(){

                        /*表单验证前*/
                        _option.beforeValidation && _option.beforeValidation($(this));

                        if(_init.fn_find($(this))){
                            /*表单验证通过*/
                            var isBeforeSubmit = _option.beforeSubmit && _option.beforeSubmit($(this));

                            if(isBeforeSubmit===false){
                                return false
                            }

                            /*是否使用ajax方式提交*/
                            if(_option.ajaxPost){
                                /*ajax提交*/
                                _init.fn_ajax($(this));
                                return false
                            }else{

                            }

                        }else{
                            /*表单验证未通过*/
                            _option.process && _option.process();
                            return false
                        }
                    });

                },
                /*遍历表单*/
                fn_find:function(obj){
                    var _result  = [],
                        _single  = null,
                        _ignore  = null,
                        _handle  = null;
                        if(obj.find('[data-type]').length>0){
                            obj.find('[data-type]').each(function(i,o) {
                            	
                            	
                                /*单项判断*/
                                var _obj = $(o),
                                    _must    = _obj.data('must');

                                _result  = _init.fn_check(_obj, _init.fn_value(_obj,obj), _obj.data('type'));
                                _single  = _obj;
                                _ignore  = _obj.data('ignore');
                                _handle  = _obj.data('handle');//加上这个 即使隐藏也要验证

                                /*被忽略的元素*/
                                if($(_single).is(':hidden') && _option.ignoreHidden && typeof(_handle) == 'undefined' || !!$(_single).attr('disabled') && !_must || !!$(_single).attr('readonly') && !_must){
                                    //隐藏,禁用,只读元素忽略
                                    return true
                                }else if(_ignore === 'empty' && _result[0] == 1){
                                    //为空忽略
                                    return true;
                                }else if(_ignore === 'ignore'){
                                    //完全忽略
                                    return true;
                                }else{
                                    /*返回单项校验结果*/
                                    _option.single && _option.single([_single,_result[0]]);

                                    if(_result[0]==1){
                                        _init.fn_msg(o,1);
                                    }
                                    if(_result[0]==2){
                                        _init.fn_msg(o,2,_result[1]);
                                    }
                                    if(_result[0]==3){
                                        _init.fn_msg(o,3);
                                    }
                                    if(_result[0]!=0){
                                        _record = false;
                                        return false
                                    }else{
                                        if(!!_result[1]){
                                            _init.fn_msg(o,0,_result[1]);
                                        }else{
                                            _init.fn_msg(o,0);
                                        }
                                        _record = true;
                                    }
                                }
                            })
                        }else{
                            _record = true;
                        }
                    /*返回校验结果*/
                    return _record;
                },
                /*获取value*/
                fn_value:function(obj,form){
                    var _val;
                    if(obj.is(':radio')){
                        _val = form.find(":radio[name='"+obj.attr("name")+"']:checked").val();
                        _val = _val === undefined ? "" : _val;
                    }else if(obj.is(":checkbox")){
                        form.find(":checkbox[name='"+obj.attr("name")+"']:checked").each(function(){
                            _val +=$(this).val()+',';
                        });
                        _val = _val === undefined ? "" : _val;
                    }else{
                        _val = obj.val();
                    }
                    return $.trim(_val)
                },

                /*获取元素设置的datatype返回其类型和规则*/
                //类型有三种
                //0.直接写正则
                //1.函数方法
                //2.绑定已有的正则方法
                //3.扩展已有的方法（如*1-3表示任意1~3个字符）
                //4.绑定多个规则（“,”代表与运算“&&”，“|”代表或运算“||” 如 m|e 手机和邮箱均可）
                //5.未定义返回null
                fn_data_type:function(type){
                    var _type      = type,
                        _reg       = /\/.+\//g,
                        _match     = /^(.+?)(\d+)-(\d+)$/,
                        _multi_reg = /^(.+?)(,|$|\||\s)(.+?)$/,
                        _multi     = /\/.+?\/[mgi]*(?=(,|$|\||\s))|[\w\*-]+/g,
                        _rule      = null;
                    if(_reg.test(_type)){
                        //直接写正则
                        _rule = [0, eval(_type)]
                    }
                    else if(!_data_type[_type] && !!_type.match(_match) && !_multi_reg.test(_type)){
                        //扩展
                        var _temp = _type.match(_match);
                        _rule = [3,_data_type[_temp[1]],_temp[2],_temp[3]];

                    }else if(!!_data_type[_type]){
                        if(typeof (_data_type[_type]) == 'function'){
                            //函数方法
                            _rule = [1,_data_type[_type]]
                        }else{
                            //已有正则
                            _rule = [2,_data_type[_type]];
                        }
                    }else if(_multi_reg.test(_type)){
                        //多个规则
                        var _arr  = [[]],
                            _mark = _type.replace(_multi,"").replace(/\s*/g,"").split(""),
                            _item = _type.match(_multi),
                            _m    = 0;

                        _arr[0].push(_item[0]);
                        for(var _n=0;_n<_mark.length;_n++){
                            if(_mark[_n]=="|"){
                                _m++;
                                _arr[_m]=[];
                            }
                            _arr[_m].push(_item[_n+1]);
                        }
                        _rule = [4,_arr];

                    }

                    //审查规则是否定义
                    if(!_rule){
                        alert(_type + _language.undefined);
                        return false
                    }else if(_rule[0]=='4'){
                        var _it,_its,_rules = _rule[1];
                        if(_rules.length>1){
                            for(_it in _rules) for (_its in _rules[_it]) {
                                var _items = _rules[_it][_its];
                                if (!_data_type[_items] && !_data_type[_items.match(_match)[1]]) {
                                    alert('l276 ' + _items + _language.undefined);
                                    _rule = false
                                }
                            }
                        }
                    }
                    return _rule;
                },

                /*校验方法*/
                //这里负责单项校验并返回校验结果_result[0-3,'msg']
                //0 校验通过
                //1 为空
                //2 不通过
                //3 校验中
                fn_check:function(obj,val,type){
                    var _obj    = obj,
                        _result = [],
                        _rule   = _init.fn_data_type(_obj.data('type')),
                        _item   = null;
                    if(!_rule){
                        return false
                    }else{
                        if(_rule[0]=='4'){
                            //多规则
                            for(var _i=0; _i<_rule[1].length; _i++){
                                for(var _s=0; _s<_rule[1][_i].length; _s++){
                                    _item = _init.fn_data_type( _rule[1][_i][_s]);
                                    _result = _check(_item);
                                    if(_result[0] != '0'){
                                        break
                                    }
                                }
                                if(_result[0] == '0'){
                                    break
                                }
                            }
                        }else{
                            _result = _check(_rule)
                        }

                        return _result
                    }

                    //单项校验
                    function _check(rule){

                        var _aResult = [];
                        if( $.trim(val)==''){
                            //是否为空
                            _aResult[0]=1;
                        }else{
                            if(rule[0]=='0'||rule[0]=='2'){
                                //正则校验
                                if(rule[1].test(val)){
                                    _aResult[0]=0;
                                }else{
                                    //复查
                                    $(obj).change(function(){
                                        if(rule[1].test($(this).val())){
                                            _init.fn_msg(obj,0);
                                        }
                                    });
                                    _aResult[0]=2;
                                }
                            }

                            if(rule[0]=='1'){
                                //函数校验
                                var _temp = rule[1](obj);
                                if(typeof _temp == "string"){
                                    //复查
                                    $(obj).change(function(){
                                        if(rule[1](obj)){
                                            _init.fn_msg(obj,0);
                                        }
                                    });
                                    _aResult=[2,rule[1](obj)];
                                    //alert('msg:'+_temp)
                                }else{
                                    if(rule[1](obj)){
                                        _aResult[0]=0;
                                    }else{
                                        _aResult[0]=2;
                                    }
                                }
                            }

                            if(rule[0]=='3'){
                                //扩展
                                if(rule[1].test(val)){
                                    if(val.length < Number(rule[2]) || val.length > Number(rule[3])){
                                        _aResult[0]=2;
                                    }else{
                                        _aResult[0]=0;
                                    }
                                }else{
                                    _aResult[0]=2;
                                }

                            }
                        }
                        return _aResult;
                    }
                },

                /**ajax表单提交*/
                fn_ajax:function(obj){
                    var _url,_data,_method;
                    _url = obj.attr('action');
                    _method = obj.attr('method') || 'post';
                    _data = _option.postData ?  _option.postData(obj.serializeArray()) : obj.serializeArray();
                    var _async = true;
                    if(obj.attr('data-async') == 1){ //如果form中有data-async 这个ajax请求为同步请求
                        _async = false;
                    }
                    //console.log(_async);
                    $.ajax({
                        url  : _url,
                        type : _method,
                        async: _async,
                        data : _data,
                        beforeSend: function(){

                        },
                        success:function(data){
                            /*表单提交完成后执行回调*/
                            _option.callback && _option.callback(data,obj);
                        },
                        error:function(){
                            /*提交出错*/
                            _option.ajaxError && _option.ajaxError(obj);
                            console.log(_language.error_submit)

                        }
                    })
                },
                /*信息显示*/
                fn_msg:function(obj,code,msg){

                    var _form                = $(obj).parents('form'),
                        _box                 = _option.tip ? _form.find(_option.tip) : $(obj).closest('.group').find('.'+_option.class_name.msg['default']),
                        _class_msg_success   = _option.class_name.msg.success,
                        _class_msg_error     = _option.class_name.msg.error,
                        _class_input_success = _option.class_name.input.success,
                        _class_input_error   = _option.class_name.input.error,
                        _nullMsg,_errorMsg,_correctMsg;

                    _nullMsg    = $(obj).data('msg-null')    || _language.error_null;
                    _errorMsg   = $(obj).data('msg-error')   || _language.error_input;
                    _correctMsg = $(obj).data('msg-success') || '';

                    _errorMsg   = /(.),(.)/.test(_errorMsg) ? _errorMsg.split(',')[0] : _errorMsg;

                    // && !$(obj).data('msg-error')
                    if(!!msg){
                        _errorMsg = msg;
                    }
                    /*code：0通过 1为空 2未通过 3校验中*/
                    switch(code){
                        case 0:
                            $(obj).removeClass(_class_input_error).addClass(_class_input_success);
                            _box.removeClass(_class_msg_error).addClass(_class_msg_success).html(_correctMsg);
                            break;
                        case 1:
                            if(_option.is_focus){
                                $(obj).removeClass(_class_input_success).addClass(_class_input_error).focus();
                            }
                            _box.removeClass(_class_msg_success).addClass(_class_msg_error).html(_nullMsg);
                            break;
                        case 2:
                            if(_option.is_focus) {
                                $(obj).removeClass(_class_input_success).addClass(_class_input_error).focus();
                            }
                            _box.removeClass(_class_msg_success).addClass(_class_msg_error).html(_errorMsg);
                            break;
                        case 3:
                            $(obj).removeClass(_class_input_error);
                            _box.removeClass(_class_msg_error).html('');
                            //console.log('校验中');
                            break;
                    }

                }
            };
        /*表单走起*/
        if(!!_form &&_form.length>0){
        	
            _form.each(function(){
            	
                var _this = $(this);
                !_this.attr('validator') && _init.fn_init(_this);
            });

        }
    };
    "function" == typeof define ? define(function() {
        return BitValidator
    }) : "undefined" != typeof exports ? module.exports = BitValidator : window.BitValidator = BitValidator
})(window);