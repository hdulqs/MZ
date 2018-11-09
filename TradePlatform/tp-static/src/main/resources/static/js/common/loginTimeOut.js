/**
 * session超时弹出框
 * add by liushilei
 * 2015/12/25
 */
var open = window.XMLHttpRequest.prototype.open,
    send = window.XMLHttpRequest.prototype.send,
    onReadyStateChange;

function openReplacement(method, url, async, user, password) {
    var syncMode = async !== false ? 'async' : 'sync';
    return open.apply(this, arguments);//可以理解成是继承，继承open对象中的属性和方法
}

function sendReplacement(data) {
	
	//layer.load(1,{time: 10*1000});//开加载  最多等待10秒
    if(this.onreadystatechange) {
        this._onreadystatechange = this.onreadystatechange;
    }
    this.onreadystatechange = onReadyStateChangeReplacement;

    return send.apply(this, arguments);
}

function onReadyStateChangeReplacement() {
	 
	//layer.closeAll();//关加载
	if(
			(this.responseText.indexOf("window.location.href")!=-1&&this.responseText.indexOf("static/login.html")!=-1)
			||
			(this.responseText.indexOf("org.apache.shiro.authz.Unauthenticated")!=-1)
	){
		
		layer.confirm('登录超时，请重新登录', {
									closeBtn: 0,
	    			    			btn: ['确定'] //按钮
	    			}, function(){
	                	window.location.href= HRY.modules.mstatic+'login.html'
  	    			});
	
	}
	//如果不判断readyState等于４,state只要变化，就进了if内，会导致一个ajax call，多次打印
    if(this._onreadystatechange&&this.readyState==4) {
		
			//我这里是需要对ajax中的json类型进行过滤
            if(this.getResponseHeader("Content-Type")=='application/json'){
                if(this.responseText){
                	var obj = eval('('+this.responseText+')');
                	console.log('NAME'+obj.emptyFlag+"\n");
                }
            }
        	return this._onreadystatechange.apply(this, arguments);
    }
   
}

//可以打印js对象
function dump_obj(myObject) { 
  var s = ""; 
  for (var property in myObject) { 
   s = s + "\n "+property +": " + myObject[property] ; 
  } 
  console.log(s); 
}  

window.XMLHttpRequest.prototype.open = openReplacement;
window.XMLHttpRequest.prototype.send = sendReplacement;
$(document).ready(function(){
		 $(document).ajaxSuccess(function(evt, request, settings){
			  
			 //layer.closeAll();//关加载
		 });
		 $(document).ajaxError(function(event,request, settings){
			  
			 	//layer.closeAll();//关加载
		 		if(
		 				(request.responseText.indexOf("window.location.href")!=-1&&request.responseText.indexOf("static/login.html")!=-1)
		 				||
		 				(request.responseText.indexOf("org.apache.shiro.authz.Unauthenticated")!=-1)
		 		){
		 			
					layer.confirm('登录超时，请重新登录', {
												closeBtn: 0,
				    			    			btn: ['确定'] //按钮
				    			}, function(){
				    				window.location.href= HRY.modules.mstatic+'login.html'
			  	    			});
				
				}
		 });
});