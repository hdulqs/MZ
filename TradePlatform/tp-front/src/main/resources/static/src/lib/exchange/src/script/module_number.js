/**
 * Created by Jason on 2015/3/18.
 */
;(function(){

    function BitNumber(num,n){
        if(num == undefined){
            return
        }
        var _str  = scientific2float(num),
            _ind  = _str.indexOf('.'),
            _len  = 0,
            _num  = 0,
            _temp = 1;

        function scientific2float(n){
            var num = (typeof n == 'string') ? n.toLowerCase().replace(/\s/g,'') : n.toString().toLowerCase().replace(/\s/g,''),
                standby = '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000',
                zf,pn,base,readyNum,offset,ext,len;
            if(/e[+-]/.test(num)){
                pn = /\+/.test(num);
                base = num.split('e');
                zf = /\-/.test(base[0].toString()) ? '-0.' : '0.';
                base[0] = base[0].replace('-','');
                offset = base[0].split('.')[1] ? base[0].split('.')[1].length : 0;
                ext = base[1];
                len = ext.split(pn ? '+' : '-')[1];
                base = base[0].replace('.','');
                return pn ? base + standby.substring(0,len - offset) :
                zf + standby.substring(0,len-1) + base;
            }else{
                return num;
            }
        }
        
        if(isNaN(parseFloat(_str))){
            //非数字
            _num = 0;
        }else if(isNaN(_str)){
            //转为数字
            _num = parseFloat(_str);

        }else{
            if(n!==undefined){
//            	if(_ind < 0 ) {
//            		_str = _str + '.';
//            		_str = _str + '00'
//            	}else {
//            		_num = _str;
//            	}
//            	 _num = _str;
//            	 return _num;
                //设置了保留位数
                if(_ind >= 0 ) {
                    //有小数
                    _len = (_str.substring(_ind + 1, _str.length)).length;
                    if(_len < n){
                        //位数不够增加0
                        for(var a = 0; a < (n-_len); a++){
                            _str = _str + '0'
                        }
                        _num = _ind==0 ? '0'+_str : _str;

                    }else{
                        //保留相应位数
                        var _n = _str.substring(0,n+1+_ind);
                        _num = _ind==0 ? '0' + _n : _n;
                    }

                    if(n <= 0){
                        _num = parseInt(num)
                    }

                }else if( n > 0 ){
                    //无小数
                    _str = _str + '.';
                    for(var c = 0; c < n; c++){
                        _str = _str + '0'
                    }
                    _num = _str
                }else if(n <= 0){
                    _num = _str
                }

                function Process(){

                }
            }else{
                //没有设置保留位数
                _num = Number(_str)
            }
        }
        return _num
    }
    "function" == typeof define ? define(function() {
        return BitNumber
    }) : "undefined" != typeof exports ? module.exports = BitNumber : window.BitNumber = BitNumber
})();