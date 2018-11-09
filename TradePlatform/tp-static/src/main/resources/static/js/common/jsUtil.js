function isEmpty(value){
	if(value!="undefinded"&&value!=null&&value!=""){
		return false;
		
	}else {
		
		return true;
	}
	
}

//get cookie add by liusl
$.getCookie =  function(name) {
    var prefix = name + "="
    var cookieStartIndex = document.cookie.indexOf(prefix)
    if (cookieStartIndex == -1)
        return null
    var cookieEndIndex = document.cookie.indexOf(";", cookieStartIndex + prefix.length)
    if (cookieEndIndex == -1)
        cookieEndIndex = document.cookie.length
    return unescape(document.cookie.substring(cookieStartIndex + prefix.length,
    cookieEndIndex))
}

//set cookie add by liusl
$.setCookie = function(name,value){
 document.cookie = name + "="+ escape (value) + ";";
}

/**
 * 校验身份证号码格式
 * @param num 身份证号码
 * @returns
 */

function isIdCardNo(num) {
    return /^(\d{18,18}|\d{15,15}|\d{17,17}(x|X))$/.test(num);
}

/**
 * 验证密码
 * @param str
 * @returns
 */
function checkPassWord(str) {
	if(str!=undefined){
		if(str.length>=6){
			return /^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[a-zA-Z0-9]+$/.test(str);
		}
	}
	return false;
}

/**
 * 校验必须为英文
 * @param code
 * @returns
 */
function checkCoinCode(code){
	return /^[A-Za-z]+$/.test(code);
}

/**
 * 加法运算
 * @param a
 * @param b
 * @returns
 */
function methAdd(a,b){
	
return parseInt(a) + parseInt(b);
		
}
/**
 * 判断是否是正数
 * @param number
 */
function positiveNumber(number){
	if((/^(\+|-)?\d+$/.test( number ))&&number>0){  
        return true;  
    }else{  
        return false;  
    }  
}