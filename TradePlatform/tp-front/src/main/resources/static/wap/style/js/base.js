var basepath = "http://www.200.com/static/wap";
var _ctx = "http://www.200.com";
var ctx_ = "http://www.200.com";
var split = window.location.href.split("?");
var tokenId = "";
if(split.length>1){
	tokenId = split[1].split("=")[1];
}

//定义定时器全局变量
var pageTimer = {} ; 

var clearPageTimer = function(){
	try{
		//全部清除方法
		for(var count in pageTimer){
			window.clearInterval(pageTimer[count]);
		}
	}catch(e){
		
	}
}