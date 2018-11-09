/*(function () {
	var d = document,
	w = window,
	p = parseInt,
	dd = d.documentElement,
	db = d.body,
	dc = d.compatMode == 'CSS1Compat',
	dx = dc ? dd: db,
	ec = encodeURIComponent;
	
	
	w.CHAT = {
		msgObj:d.getElementById("message"),
		screenheight:w.innerHeight ? w.innerHeight : dx.clientHeight,
		username:null,
		userid:null,
		socket:null,
		//让浏览器滚动条保持在最低部
		scrollToBottom:function(){
			w.scrollTo(0, this.msgObj.clientHeight);
		},
		//退出，本例只是一个简单的刷新
		logout:function(){
			//this.socket.disconnect();
			location.reload();
		},
		//提交聊天消息内容
		submit:function(){
			var content = d.getElementById("content").value;
			if(content != ''){
				var obj = {
					userid: this.userid,
					username: this.username,
					content: content
				};
				this.socket.emit('message', obj);
				d.getElementById("content").value = '';
			}
			return false;
		},
		genUid:function(){
			return new Date().getTime()+""+Math.floor(Math.random()*899+100);
		},
		//更新系统消息，本例中在用户加入、退出的时候调用
		updateSysMsg:function(o, action){
			//当前在线用户列表
			var onlineUsers = o.onlineUsers;
			//当前在线人数
			var onlineCount = o.onlineCount;
			//新加入用户的信息
			var user = o.user;
				
			//更新在线人数
			var userhtml = '';
			var separator = '';
			for(key in onlineUsers) {
		        if(onlineUsers.hasOwnProperty(key)){
					userhtml += separator+onlineUsers[key];
					separator = '、';
				}
		    }
			d.getElementById("onlinecount").innerHTML = '当前共有 '+onlineCount+' 人在线，在线列表：'+userhtml;
			
			//添加系统消息
			var html = '';
			html += '<div class="msg-system">';
			html += user.username;
			html += (action == 'login') ? ' 加入了聊天室' : ' 退出了聊天室';
			html += '</div>';
			var section = d.createElement('section');
			section.className = 'system J-mjrlinkWrap J-cutMsg';
			section.innerHTML = html;
			this.msgObj.appendChild(section);	
			this.scrollToBottom();
		},
		//第一个界面用户提交用户名
		usernameSubmit:function(){
			var username = d.getElementById("username").value;
			if(username != ""){
				d.getElementById("username").value = '';
				d.getElementById("loginbox").style.display = 'none';
				d.getElementById("chatbox").style.display = 'block';
				this.init(username);
			}
			return false;
		},
		init:function(username){
			
			客户端根据时间和随机数生成uid,这样使得聊天室用户名称可以重复。
			实际项目中，如果是需要用户登录，那么直接采用用户的uid来做标识就可以
			
			this.userid = this.genUid();
			this.username = username;
			
			d.getElementById("showusername").innerHTML = this.username;
			this.msgObj.style.minHeight = (this.screenheight - db.clientHeight + this.msgObj.clientHeight) + "px";
			this.scrollToBottom();
			
			//连接websocket后端服务器
			this.socket = io.connect('ws://localhost:3000');
			
			//告诉服务器端有用户登录
			this.socket.emit('login', {userid:this.userid, username:this.username});
			
			//监听新用户登录
			this.socket.on('login', function(o){
				CHAT.updateSysMsg(o, 'login');	
			});
			
			//监听用户退出
			this.socket.on('logout', function(o){
				CHAT.updateSysMsg(o, 'logout');
			});
			
			//监听消息发送
			this.socket.on('message', function(obj){
				var isme = (obj.userid == CHAT.userid) ? true : false;
				var contentDiv = '<div>'+obj.content+'</div>';
				var usernameDiv = '<span>'+obj.username+'</span>';
				
				var section = d.createElement('section');
				if(isme){
					section.className = 'user';
					section.innerHTML = contentDiv + usernameDiv;
				} else {
					section.className = 'service';
					section.innerHTML = usernameDiv + contentDiv;
				}
				CHAT.msgObj.appendChild(section);
				CHAT.scrollToBottom();	
			});

		}
	};
	//通过“回车”提交用户名
	d.getElementById("username").onkeydown = function(e) {
		e = e || event;
		if (e.keyCode === 13) {
			CHAT.usernameSubmit();
		}
	};
	//通过“回车”提交信息
	d.getElementById("content").onkeydown = function(e) {
		e = e || event;
		if (e.keyCode === 13) {
			CHAT.submit();
		}
	};
})();
*/


/**
 * 执行初始化方法,登录后10秒上线
 */
setTimeout(function(){
	init();
},50000)
/**
 * 初始化........
 */
function init() {
    var sendUserId = $('#sendUserId').val();
	/*
	客户端根据时间和随机数生成uid,这样使得聊天室用户名称可以重复。
	实际项目中，如果是需要用户登录，那么直接采用用户的uid来做标识就可以
	*/
	this.userid = sendUserId;
    this.username = $("#username").html();
	
	//连接websocket后端服务器
	this.socket = io.connect('ws://172.16.1.100:3000');
	
	//告诉服务器端有用户登录
	this.socket.emit('login', {userid:this.userid, username:this.username});
	
	//监听新用户登录
	this.socket.on('login', function(o){
		//CHAT.updateSysMsg(o, 'login');	
	});
	
	//监听用户退出
	this.socket.on('logout', function(o){
		//CHAT.updateSysMsg(o, 'logout');
	});
	
	//监听消息发送
	this.socket.on('message', function(obj){
		 
		var chatdisplay = $(".chat").css("display");
		if(chatdisplay=="none" || !$(".chat").hasClass("open-messages")){
			growl.addInfoMessage('你有一条信息');
		}
		
		var messages=obj;
		
	    var strHTML = "<div class=\"date\">"+new Date()+"</div>"; //初始化保存内容变量  
	    for (var index in messages) { //遍历获取的数据  
	        
	        if(messages[index].sendUserId==$('#sendUserId').val()){
	            strHTML += "<div class=\"from-me\">" ;
		        strHTML += messages[index].text;
		        strHTML += "</div>";
		        strHTML += " <div class=\"clear\"></div>";
	        }else{
				strHTML +="<div class=\"from-them\">";
				strHTML +="<img src=\"http://localhost/static/static/lib/_con/images/user2.jpg\" alt=\"John Doe\" class=\"circle photo\">";
				strHTML += messages[index].text;
				strHTML +="</div>";
				strHTML +=" <div class=\"clear\"></div>";
			//	strHTML +="<div class=\"date\"></div>";
	        }
	        
	    }
	    
	    $("#chatlog").html(strHTML);
	    
	});
}

/**
 * 发送消息
 */
function sendMessage() {
  //获得发送消息
  var chatmessage = $("#chatmessage").val();
  //获得当前人ID
  var sendUserId = $('#sendUserId').val();
  //发送人账号
  var sendUserName = $("#username").html();
  //获得接收人ID
  var receiveUserId = $('#receiveUserId').val();
  //接收人账号
  var receiveUserName = $("#receiveUserName").val();
  
  //清空消息框
  $("#chatmessage").val("");
  //调发送方法
  
  
   $.ajax({
	     type: 'POST',
	     url:HRY.modules.oauth+'/chat/appmessage/add.do',
	     data : {
	    	 sendUserId : sendUserId,
			 sendUserName : sendUserName,
			 receiveUserId : receiveUserId,
			 receiveUserName : receiveUserName,
			 text : chatmessage
	     },
	     dataType: "json",
	     socket : this.socket,
	     success: function(data){
	    	    
	    	   if(data!=null&&data.success){
		    	   
		    	   this.socket.emit('message', data.obj);
	    	   }
	     }
   });
  

  
  
}

/**
 * 接收新消息
 * @param {} messages
 */
function receiveMessages(messages) {

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

















