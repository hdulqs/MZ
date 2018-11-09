$(document).ready(function(){
	//提示框
	$(document.body).append('<div id="blackDialog" style="display:none;width:100%; height:100%; position:absolute; top:0;left:0; z-index:9999999999; "><div style="width:5.4rem; height:1rem; font-size: 0.26rem; color:#fff; text-align:center; position:absolute; left:50%; top:50%; margin-left:-2.6rem; margin-top:-0.75rem;border-radius:0.2rem; "><table cellspacing="0" cellspacing="0" style="width:100%; height:100%; border:none; position:absolute; left:0px; top:0px;border-radius:0.2rem;"><tr style="border:none;"><td class="text" align="center" valign="middle" style="line-height:0.3rem; color:#FFF; padding:0.3rem; border:none; background-color:rgba(0,0,0,0.8);border-radius:0.2rem;"></td></tr></table></div></div>');
	//询问框
	$(document.body).append('<div id="blackConfirmlog" style="display:none;width:100%; height:100%; position:absolute; top:0;left:0; z-index:9999999999; "><div style="width:5.4rem; height:1rem; font-size: 0.26rem; color:#fff; text-align:center; position:absolute; left:50%; top:50%; margin-left:-2.6rem; margin-top:-0.75rem;border-radius:0.2rem; z-index:99999999; "><table cellspacing="0" cellspacing="0" style="width:100%; height:100%; border:none; position:absolute; left:0px; top:0px;border-radius:0.2rem;line-height:0.3rem; color:#FFF; background-color:rgba(0,0,0,0.8);"><tr style="border:none;"><td class="text" align="center" valign="middle" style="padding:0.3rem;line-height: 0.3rem;"></td></tr><tr style="line-height: 0.3rem;"><td style="padding-bottom:0.3rem;padding-left: 2rem;color:#13f3f5" align="left" valign="middle" class="confirmBtn"></td><td align="right" valign="middle" class="cancelBtn" style="padding-bottom:0.3rem;padding-right: 0.5rem;"></td></tr></table></div></div>');
	
});
var blackDialog = {
		defaultDelay : 2000,
		confirmBtn : 'Confirm',
		cancelBtn : 'Cancel',
		show : function(text,delay,callback){
			$("#blackDialog").on('touchmove', function (event) {
				event.stopPropagation();
			});
			$("#blackDialog .text").html(text);
			$("#blackDialog").show();
			if(delay==undefined||delay==null){
				delay = this.defaultDelay;
			}
			setTimeout(function(){
				$("#blackDialog").fadeOut(500)
			},delay);
		},
		reload : function(text,delay,callback){
			$("#blackDialog").on('touchmove', function (event) {
				event.stopPropagation();
			});
			$("#blackDialog .text").html(text);
			$("#blackDialog").show();
			if(delay==undefined||delay==null){
				delay = this.defaultDelay;
			}
			setTimeout(function(){
				$("#blackDialog").fadeOut(500,function(){
					window.location.reload();
				});
			},delay);
		},
		redirect : function(text,redirectUrl,delay,callback){
			$("#blackDialog").on('touchmove', function (event) {
				event.stopPropagation();
			});
			$("#blackDialog .text").html(text);
			$("#blackDialog").show();
			if(delay==undefined||delay==null){
				delay = this.defaultDelay;
			}
			setTimeout(function(){
				$('#blackDialog').fadeOut(500,function(){
					window.location.href=redirectUrl;
				})
			},delay);
		},
		confirm : function(callback,text,confirmBtn,cancelBtn){
			$("#blackConfirmlog").on('touchmove', function (event) {
				event.stopPropagation();
			});
			$("#blackConfirmlog .text").html(text);
			$("#blackConfirmlog").show();
			if(confirmBtn==undefined||confirmBtn==null){
				if(getCookie(name)=="en"){
					confirmBtn = this.confirmBtn;
				}
				if(getCookie(name)=="ch"){
					confirmBtn = "确认";
				}
			}
			if(cancelBtn==undefined||cancelBtn==null){
				if(getCookie(name)=="en"){
					cancelBtn = this.cancelBtn;
				}
				if(getCookie(name)=="ch"){
					cancelBtn = "取消";
				}
			}
			$("#blackConfirmlog .confirmBtn").html(confirmBtn).click(function(){
				$("#blackConfirmlog").fadeOut(500);
				callback();
			});
			$("#blackConfirmlog .cancelBtn").html(cancelBtn).click(function(){
				$("#blackConfirmlog").fadeOut(500);
			});
		}
};

/*$(document).ready(function(){
	blackDialog.redirect("跳转到百度测试","http://www.baidu.com",5000);
});*/