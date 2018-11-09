init();
function init() {
   //这个方法用来启动该页面的ReverseAjax功能
   dwr.engine.setActiveReverseAjax(true);
   //设置在页面关闭时，通知服务端销毁会话
   dwr.engine.setNotifyServerOnPageUnload(true);
   var sendUserId = $('#sendUserId').val()
   JavascriptChat.onPageLoad(sendUserId);
   
}

/**
 * 发送消息
 */
function sendMessage() {
  
  //获得发送消息
  var chatmessage = dwr.util.getValue("chatmessage");
  //获得当前人ID
  var sendUserId = $('#sendUserId').val();
   //获得接收人ID
  var receiveUserId = $('#receiveUserId').val();
  //清空消息框
  dwr.util.setValue("chatmessage", "");
  //调发送方法
  JavascriptChat.addMessage(chatmessage,sendUserId,receiveUserId);
}

/**
 * 接收新消息
 * @param {} messages
 */
function receiveMessages(messages) {
	
	var chatdisplay = $(".chat").css("display");
	if(chatdisplay=="none" || !$(".chat").hasClass("open-messages")){
		growl.addInfoMessage('你有一条信息');
	}
	
    var strHTML = "<div class=\"date\">2015-10-02 23:11</div>"; //初始化保存内容变量  
    for (var index in messages) { //遍历获取的数据  
        
        if(messages[index].sendUserId==$('#sendUserId').val()){
            strHTML += "<div class=\"from-me\">" ;
	        strHTML += dwr.util.escapeHtml(messages[index].text) ;
	        strHTML += "</div>";
	        strHTML += " <div class=\"clear\"></div>";
        }else{
			strHTML +="<div class=\"from-them\">";
			strHTML +="<img src=\"../component/baseUI/_con/images/user2.jpg\" alt=\"John Doe\" class=\"circle photo\">";
			strHTML += dwr.util.escapeHtml(messages[index].text) ;
			strHTML +="</div>";
			strHTML +=" <div class=\"clear\"></div>";
		//	strHTML +="<div class=\"date\"></div>";
        }
        
    }
    dwr.util.setValue("chatlog", strHTML, { escapeHtml:false });
}

/**
 * 被踢出后接收提示信息
 * @param {} messages
 */
function kitOutMessage(messages){
	//关闭dwr
	dwr.engine.setActiveReverseAjax(false);
	dwr.engine._activeReverseAjax = false;
	dwr.engine._pollBatch = null;
	
	alert(messages);
}

$(document).ready(function(){
		var loadOnlineSession = function(){
	 		$.getJSON("/oauth/sessionController/list.do", function(data) {
	                $("#chat").empty(); //先清空标记中的内容  
	                var strHTML = "<span class=\"label\">Online</span>"; //初始化保存内容变量  
	                $.each(data, function(InfoIndex, Info) { //遍历获取的数据  
	                    strHTML += "<div class=\"user\"  >" ;
	                    strHTML += "<input type=\"hidden\"  value=\""+Info["id"]+"\" />" ;
	                    strHTML += "<input type=\"hidden\"  value=\""+Info["username"]+"\" />" ;
	                    strHTML += "<img src=\"static/lib/_con/images/user2.jpg\" alt=\"Felecia Castro\" class=\"circle photo\">";
	                    strHTML += "<div class=\"name\" >"+Info["username"]+"</div>";
	                    strHTML += "<div class=\"status\">在线</div>";
	                    strHTML += "<div class=\"online\"><i class=\"green-text fa fa-circle\"></i></div>";
	                    strHTML += "</div>";
	                   // strHTML += "<span class=\"label\">Offline</span>";
	                })  
	                $("#chat").html(strHTML); //显示处理后的数据  
	           });  
        }
       //单击进入聊天室
       $("#chatOnline").on("click",function(){
       	//加载在线人
       	loadOnlineSession();
       	//初始化反推配置
       });
       
       //单击加载聊天记录
       $("#chat").on("click",".user",function(){
       	//取接收人ID
       	var receiveUserId = $($(this).children("input")[0]).val();
       	var receiveUserName = $($(this).children("input")[1]).val();
       	var sendUserId = $("#sendUserId").val();
       	if(sendUserId==receiveUserId){
       		 growl.addInfoMessage('不能和自己聊天');
       		 return false;
       	}
       	//设置页面临时接收人ID
       	$("#receiveUserId").val(receiveUserId);
       	//设置跳转后的显示
       	$("#receiveUserName").html(receiveUserName);
       	
       	
   		$.getJSON("/oauth/chat/loadChatLog?receiveUserId="+receiveUserId, function(data) {
               $("#chatlog").empty(); //先清空标记中的内容  
               var strHTML = "<div class=\"date\"><a href=\"##\">查看更多消息</a></div>"
               	strHTML += "<div class=\"date\">2015-10-02 23:11</div>"; //初始化保存内容变量  
               $.each(data, function(InfoIndex, Info) { //遍历获取的数据  
               	if(Info.sendUserId==$("#sendUserId").val()){
	                    strHTML += "<div class=\"from-me\">" ;
	                    strHTML += Info.text ;
	                    strHTML += "</div>";
	                    strHTML += " <div class=\"clear\"></div>";
                   }else{
         				strHTML +="<div class=\"from-them\">";
         				strHTML +="<img src=\"static/lib/_con/images/user2.jpg\" alt=\"John Doe\" class=\"circle photo\">";
         				strHTML +=Info.text;
         				strHTML +="</div>";
         				strHTML +="<div class=\"date\"></div>";
                   }
                   
               });
           	$("#chatlog").html(strHTML); //显示处理后的数据  
               
          });  
       	
       });
});



