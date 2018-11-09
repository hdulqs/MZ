define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/index.js");
	require("style/js/mobile/js/public.js");
	
	module.exports = {
			
		init : function(){
				//判断是否已经登录
				if(tokenId!=""){
					$("#isToken").html("<a href='"+basepath+"/html/coins.htm?tokenId="+tokenId+"' class='a-on'>交易中心</a><a href='"+basepath+"/html/user/user-index.html?tokenId="+tokenId+"' class='a-on'>我的账户</a>");
					$("#logo").attr("href",ctx_+"/static/wap/html/coins.htm?tokenId="+tokenId);
					
				}else{
					$("#isToken").html("<a href='"+basepath+"/html/coins.htm' class='a-on'>交易中心</a><a href='"+basepath+"/html/user/login.htm' class='a-on'>登录</a><a href='"+basepath+"/html/user/reg.htm'>注册</a>");
				}
			
			
			//在一个对象后面添加一个方法
			Array.prototype.unique = function (){
				 var res = [];
				 var json = {};
				 for(var i = 0; i < this.length; i++){
					  if(!json[this[i]]){
						  res.push(this[i]);
						  json[this[i]] = 1;
					  }
				 }
				 return res;
			}
			
			//交易区
			$.ajax({
				url: ctx_ + "/mobile/nouser/appmarketlist.do",
				type:"post",
				dataType:'json',
				data : {'tokenId':tokenId},
				success:function(data){
					if(data!=undefined){
						$("#click_sort1_tbody").empty();
						var coins = [];
						for(var i=0;i<data.length;i++){
							var change =  data[i].coinCode.split("_")[1];
							coins.push(change);
						}
						
						var quchong = coins.unique();
						
						for(var i=0; i<quchong.length;i++){debugger
							var change = quchong[i];
							if(i==0){
								$("#tags_coin").append("<li><a class=\"s-aClick tags_coin_click\" change=\""+change+"\" href=\"#\">"+change+"</a></li>")
								
								//默认交易区
								var activeData = [];
								activeData.unique();
								activeData.push(data[0]);
								$("#click_sort1_tbody").empty();
								$("#productInfo_tmp").tmpl(activeData).appendTo("#click_sort1_tbody");
								
							
							}else{
								$("#tags_coin").append("<li><a  href=\"#\" class=\"tags_coin_click\" change=\""+change+"\">"+change+"</a></li>")
							}
						}
					}
				}
		    });
			$("#tags_coin").on("click","li",function(){
				$(this).parent().find('li').children().removeClass('s-aClick');
				//$(this).closest("#tags_coin").find("li").children().removeClass('s-aClick');
				$(this).children().toggleClass("s-aClick");
				$("#click_sort1_tbody").empty();
				var activeChange = $(this).children().attr("change");
				$.ajax({
					url: ctx_ + "/mobile/nouser/appmarketlist.do",
					type:"post",
					dataType:'json',
					success:function(data){
						if(data!=undefined){
							for(var i=0;i<data.length;i++){
								var change =  data[i].coinCode.split("_")[1];
								if(activeChange==change){
									//激活交易区
									var activeData = [];
									activeData.push(data[i]);
									$("#productInfo_tmp").tmpl(data[i]).appendTo("#click_sort1_tbody");
								}
							}
						}
					}
				});
			})
			
			//公告
			$.ajax({
				url: ctx_ + "/mobile/nouser/apparticle.do",
				type:"post",
				dataType:'json',
				data : {type:27},
				success:function(data){
					if(data!=null&&data.obj!=null&&data.obj[0]!=null){
						var html = "<a href='javascript:void(0)' onclick='window.open(\"news/detail/-id=1511.htm?id="+data.obj[0].id+"\",\"_self\");'>"+data.obj[0].title+"</a>";
						$("#gonggao").html(html);
					}
				}
			});
		},
		banner : function(){
			$.ajax({
				url: ctx_ + "/mobile/nouser/appbanner.do",
				type:"post",
				dataType:'json',
				success:function(data){debugger
					if(data.obj!=null){
						var html = "";
						var li = "";
						var width = 0;
						for(var i=0;i<data.obj.length;i++){
							html += "<div><a><img class=\"img-responsive\" src=\""+ctx_+"/"+data.obj[i].picturePath+"\"/></a></div>";
							li += "<li></li>";
							width += document.body.clientWidth;
						}
						$("#bannerDiv").attr("style",width);
						$("#bannerDiv").html(html);
						$("#position").html(li);
					}else{
						$("#bannerDiv").html("<div><a><img class=\"img-responsive\" src=\"../style/images/banner/banner1.jpg\" alt=\"互融云\" /></a></div>");
						$("#bannerDiv").attr("style",document.body.clientWidth);
					}
				}
			});
		},
		content : function(){
			//详情页自带的js
			$(".bullet_p .bull_click").click(function() {
	            $(this).addClass('actives').siblings().removeClass('actives');
	            var  index =  $(".bullet_p .bull_click").index(this);
	            $("#box_bull > ul").eq(index).show().siblings().hide();
	        });
			
			var id = window.location.href.split("?")[1].split("=")[1];
			
			//首页公告详情页
			$.ajax({
				url: ctx_ + "/mobile/nouser/getContent.do",
				type:"post",
				dataType:'json',
				data : {id:id},
				success:function(data){
					if(data!=null){
						$("#title").text(data.obj.title);
						$("#createtime").text(data.obj.modified);
						$("#content").html(data.obj.content);
					}
				}
			});
		}
	}
})


