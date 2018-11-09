//获取地址栏参数
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
    var cate=GetQueryString('activeId');
    var newsId=GetQueryString('newsId')
	//顶部的导航栏
    if(cate){
	$('.nav-list li').map(function(){
	    if($(this).attr('activeId')==cate){
	        $(this).addClass('active');
	    }else{
	        return $(this).attr('activeId');
	    }
	}).get();
    }
	//新闻资讯的左侧导航栏

	$('#artic_category li').map(function(){
		
		if($(this).find('a').attr('categoryid')==newsId){
			$(this).addClass('active');
			 if($(this).find('i').length){
		    	 return;
		    	    
		      }else{
		    	  $(this).append('<i class="fr iconfont icon-jiantouyou"></i>') 
		      }
		}else{
			return $(this).find('a').attr('categoryid');
		}
	}).get();
	
