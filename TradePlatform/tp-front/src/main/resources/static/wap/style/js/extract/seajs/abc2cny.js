define(function(require, exports, module){
	require("style/css/mobile/css/css.css");
	require("style/css/bootstrap/css/boot.css");
	require("style/js/layer/css/layer.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/mobile/js/public.js");
	require("style/js/index.js");
	require("style/js/bootstrap/js/jquery.cookie.js");
	require("style/js/bootstrap/js/bootstrap.js");
	require("style/js/mobile/js/zepto.js");
	require("style/js/extract/common/abc2cny.js");
	
	module.exports = {
		init : function(){
			
			var str=location.href; 
			var meter=str.split("?")[1];
			var tokenId=meter.split("&")[0];
			tokenId=tokenId.split("=")[1];
			var symbol=meter.split("&")[1];
			symbol=symbol.split("=")[1];
			
			//判断是否已经登录
			if(tokenId!=""){
				$("#isToken").html("<a href='"+basepath+"/html/coins.htm?tokenId="+tokenId+"' class='a-on'>交易中心</a><a href='"+basepath+"/html/user/user-index.html?tokenId="+tokenId+"' class='a-on'>我的账户</a>");
				$("#logo").attr("href",ctx_+"/static/wap/html/coins.htm?tokenId="+tokenId);
				
			}else{
				$("#isToken").html("<a href='"+basepath+"/html/coins.htm' class='a-on'>交易中心</a><a href='"+basepath+"/html/user/login.htm' class='a-on'>登录</a><a href='"+basepath+"/html/user/reg.htm'>注册</a>");
			}
			
	
			
			//跳转到首页
			$("#bbjy").on("click",function(){
				window.open(basepath + "/html/coins.htm?tokenId="+tokenId,"_self");
			})
			$("#logo").on("click",function(){
				window.open(basepath + "/html/coins.htm?tokenId="+tokenId,"_self");
			})
			debugger;
			//成交记录
			$("#coinorderlist").html("<tr><td>11-23 17:20:20</td><td>1</td><td>10</td><td>100W</td></tr>")
			//买入委托
			$("#view_new_price").html("<tr class='j-bg-c' ><td>买入</td><td>1</td><td>10</td></tr>")
			//卖出委托
			$("#view_depth_ask").html("<tr class='j-bg-c' ><td>卖出</td><td>1</td><td>10</td></tr>")
			if(tokenId!=""){
				debugger;
				//我的委托
				$.ajax({
					url: ctx_ + "/user/entrust/list?type=current",
					type:"post",
					dataType:'json',
					success:function(data){
						debugger;
						alert(123);
					}
				});
				//$("#table_current").html("<tr><td>卖</td><td>5.00</td><td>5.0000</td><td>5.0000</td><td>撤单</td></tr>")
				//历史委托
				$("#more_history").html("<tr><td>11-23 17:20:20</td><td>买</td><td>BTC-USD</td><td>2</td><td>3</td><td>3</td><td>未成交</td><td>撤消</td></tr>")
				//交易历史
				$("#Tran_history").html("<tr><td>2017-11-23 18:46:41</td><td> 买 </td><td>BTC-USD</td><td> 5</td><td> 3</td><td> 15</td><td> 1.5</td></tr>")
			}else{
				//我的委托
				$("#table_current").html("<tr><td>登陆后显示</td></tr>")
				//委托历史
				$("#more_history").html("<tr><td>登陆后显示</td></tr>")
				//交易历史
				$("#Tran_history").html("<tr><td>登陆后显示</td></tr>")
			}
			
			if(tokenId!=""){
				//跳转到首页 我的账户 设置
				$("#bbjy").on("click",function(){
					window.open(basepath + "/html/coins.htm?tokenId="+tokenId,"_self");
				})
				$("#wdzh").on("click",function(){
					window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
				})
				$("#sz").on("click",function(){
					window.open(basepath + "/html/user/account.html?tokenId="+tokenId,"_self");
				})
				
				
				
			
				
				
				$.ajax({
					url: ctx_ + "/mobile/user/apppersondetail/isrealandpwd.do",
					type:"post",
					data : {tokenId:tokenId},
					dataType:'json',
					success:function(data){
						if(data!="" && data!=null){
							if(data.success){
								if(data.obj.user.isReal=='0'){
									layer.msg("请先实名", {icon: 2,time:1500},function(){
										window.open(basepath + "/html/user/user-index.html?tokenId="+tokenId,"_self");
									});
								}else{
									$("#huikuanren").val(data.obj.user.truename);
								}
							}else{
								$("#wdzh").empty().text("登录");
								$("#wdzh").attr("href",basepath + "/html/user/login.htm");
							}
						}
					}
				});
			}
			
			// 页面数据赋值
			$.ajax({
				url: ctx_ + "/mobile/nouser/appmarketlist.do",
				type:"post",
				dataType:'json',
				success:function(data){
					
					var coins = [];
					var urlHuoBi=symbol.split("_")[0];
					var urlFaBi=symbol.split("_")[1];
					
					for(var i=0;i<data.length;i++){
						var huobi =  data[i].coinCode.split("_")[0];
						var fabi =  data[i].coinCode.split("_")[1];
					if(huobi==urlHuoBi&&urlFaBi==fabi){
						
							// 最新成交价
							var currentExchangPrice=data[i].currentExchangPrice;
							// 24小时交易量
							var transactionSum=data[i].transactionSum;
							// 最高价
							var maxPrice=data[i].maxPrice;
							// 最低价
							var minPrice=data[i].minPrice;
							
							$("#title").html(huobi+"对"+fabi);
							$("#new_price").html(currentExchangPrice);
							$("#24h_max").html(maxPrice);
							$("#24h_min").html(minPrice);
							$("#24h_count").html(transactionSum);
						}
						
					}
					
				}
		    });
			
			
			
			//买
			$("#coinpricein").on("blur",function(){debugger;
				if($(this).val()!=''&&$(this).val()!=null){
					var regex = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
					if(!regex.test($(this).val())){
						layer.msg("请输入数字", {icon: 2});
						return false;
					}
				}
			})
			//买入数量
			$("#numberin").on("blur",function(){
				var coinpricein = $("#coinpricein").val();
				if(coinpricein==""){
					layer.msg("请先输入买入价格", {icon: 2});
					$(this).val('');
					return false;
				}
				var regex = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
				if(!regex.test($(this).val())){
					layer.msg("请输入数字", {icon: 2});
					return false;
				}
				$("#coinin_sumprice").val(coinpricein*$(this).val());
			})
			//买入
			$("#trustbtnin").on("click",function(){
				var coinpricein = $("#coinpricein").val();
				var regex = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
				if(coinpricein == '' || coinpricein == null){
					layer.msg("请填写买入价格", {icon: 2});
					return false;
				}
				if(!regex.test(coinpricein)){
					layer.msg("请输入数字", {icon: 2});
					return false;
				}
				
				var numberin = $("#numberin").val();
				if(!regex.test(numberin)){
					layer.msg("请输入数字", {icon: 2});
					return false;
				}
			
				$.ajax({
					url: ctx_ + "/mobile/nouser/trades/appadd.do",
					type:"post",
					dataType:'json',
					data : {entrustPrice:coinpricein,type:1,coinCode:'HRC_cny',entrustCount:numberin,entrustWay:1,tokenId:tokenId},
					success:function(data){
						if(data.success){
							$("#coinpricein").val("");
							$("#numberin").val("");
							$("#coinin_sumprice").val("");
							
						}else{
							layer.msg(data.msg, {icon: 2});
						}
					}
				})
			})
			
			//卖
			$("#coinpriceout").on("blur",function(){debugger;
				if($(this).val()!=''&&$(this).val()!=null){
					var regex = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
					if(!regex.test($(this).val())){
						layer.msg("请输入数字", {icon: 2});
						return false;
					}
				}
			})
			//卖出数量
			$("#numberout").on("blur",function(){
				var coinpriceout = $("#coinpriceout").val();
				if(coinpriceout==""){
					layer.msg("请先输入买入价格", {icon: 2});
					$(this).val('');
					return false;
				}
				var regex = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
				if(!regex.test($(this).val())){
					layer.msg("请输入数字", {icon: 2});
					return false;
				}
				$("#coinout_sumprice").val(coinpricein*$(this).val());
			})
			//卖出
			$("#trustbtnout").on("click",function(){
				var coinpriceout = $("#coinpriceout").val();
				var regex = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
				if(coinpriceout == '' || coinpriceout == null){
					layer.msg("请填写买入价格", {icon: 2});
					return false;
				}
				if(!regex.test(coinpriceout)){
					layer.msg("请输入数字", {icon: 2});
					return false;
				}
				
				var numberout = $("#numberout").val();
				if(!regex.test(numberout)){
					layer.msg("请输入数字", {icon: 2});
					return false;
				}
				
				$.ajax({
					url: ctx_ + "/mobile/nouser/trades/appadd.do",
					type:"post",
					dataType:'json',
					data : {entrustPrice:coinpriceout,type:2,coinCode:'HRC_cny',entrustCount:numberout,entrustWay:1,tokenId:tokenId},
					success:function(data){
						if(data.success){
							$("#coinpriceout").val("");
							$("#numberout").val("");
							$("#coinout_sumprice").val("");
						}else{
							layer.msg(data.msg, {icon: 2});
						}
					}
				})
			})
		}
	}
})