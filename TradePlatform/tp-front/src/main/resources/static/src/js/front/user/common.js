/****************************************
 * 点击切换函数：
 * 点击列表，显示对应的区块，列表和区块放在一个盒子里
 * 
 * def = {
        ele: '.tab_switch',       //列表和区块所在的盒子
        tabList:".switch_li",      //列表的类
        boxList: '.tab_switch_item'       //区块的类
    }
 * 
 * ***************************************/
function  isNumber(num) 
{
	 var re = /^\d+(?=\.{0,1}\d+$|$)/
	 if (num != "") {
		 if (re.test(num)) {
			 return  true;
		 }
	 }
	 return false;
}
var tool = {
	switchTab:function(params,call){
		var def = {
            ele: '.tab_switch',
            tabList:".switch_li",
            boxList: '.tab_switch_item'
        }
        var option = $.extend(def,params);
        var e = $(option.ele);
        var tabList = option.tabList;
        var boxList = option.boxList;
        
        
		e.each(function(i,item){
			var tab = $(item).find(tabList);
			var box = $(item).find(boxList);
			
			tab.click(function(){
				var num = $(this).index();
				
				tab.removeClass("active");
				$(this).addClass("active");
				console.log(boxList);
				if(boxList != null){
					box.removeClass("active");
					box.eq(num).addClass("active");
				}
				if(typeof call === "function"){
					call(num);
				}
			})
		})
	},
	/**
	 * 跳转分页
	 * number 页码
	 * params 额外参数
	 */
	goPage:function(number,params){
		var url = location.href;
		var status = false;	
			if(/page\=[0-9]+/.test(url)){
				url = url.replace(/page\=[0-9]+/,'page='+number);
			}
			
			if(/\?{1}/.test(url)){
				status = true;
			}
			
			if(typeof params == 'object'){
				for(var i in params){
					if(status){
						url += '&'+i+'='+params[i];
					}else{
						url += '?'+i+'='+params[i];
						status = true;
					}
				}
			}
			;
			window.location = url;
	}
}


