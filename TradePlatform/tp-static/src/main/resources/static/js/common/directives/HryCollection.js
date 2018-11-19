/**
 * 折叠树插件
 * 调用方式：<hry-collection
 *           select2  //可选  没有这个属性就是单选下拉框  有这个属性就是复选下拉框
 *           disabled //可选  没有这个属性可编辑      有这个属性为只读
 *           url="oauth/company/subcompany/findCompanyList"   //url  必填   url除去http://localhost/   
 *           name="name"       //必填    下拉框显示的字段
 *           value="id"        //必填    下拉框的值字段
 *           mid   //自身id
 *           pid   //父级id
 *           clickshow   //弹出页面
 *        >    
 *       </hry-select>
 * 
 * add by liushilei  2015/12/18 17:37
 */
define(['app'], function (app) {
    app.directive('hryCollection', ["$rootScope","$compile",function ($rootScope,$compile) {
    return {  
        restrict: 'E',  
        scope: true, 
        template:'',
        link : function(scope, element, attributes, controllerInstance){
        	//配置
        	var conf ={
    		  	 url : attributes.url,
            	 mid : attributes.mid,  //Id
            	 pid : attributes.pid,  //父ID
            	 name : attributes.name, //显示字段
            	 clickshow : attributes.clickshow,
            	 toolshow : attributes.toolshow,
            	 clickColor : "grey lighten-3",
            	 space : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
            	 expand : "<i expand class=\"fs-24 mdi-navigation-arrow-drop-down\"></i>",
            	 collapse : "<i expand class=\"fs-24 mdi-navigation-arrow-drop-up\"></i>",
            	 remove :"<a title=\"删除\" remove class=\"secondary-content right\"><i class=\"secondary-content mdi-action-delete fs-20\"></i></a>",
            	 modify : "<a title=\"编辑\"  modify class=\"secondary-content right\"><i class=\"secondary-content mdi-editor-border-color fs-20\"></i></a>",
            	 add : "<a title=\"添加\" add class=\"secondary-content right\"><i class=\"secondary-content mdi-av-my-library-add fs-20\"></i></a>"
            	 
        	}
        	
        	//li临时集合
        	var items = [];
        	//li最终集合
        	var allItems = [];
        	
        	//增加空格  和设置展开属性
        	function setSpace(){
        		for(var i = 0 ; i < allItems.length ; i++){
        			if(allItems[i].attr("level")<=1){
        				allItems[i].attr("show","");//默认2级
        			}
        			for(var level = 0 ; level < allItems[i].attr("level") ; level++){
        				$(allItems[i].find("span[space]")[0]).append(conf.space);
        			}
        		}
        	}
        	
        	//排序
        	function itemSort(pid){
        		for(var i = 0 ; i < items.length ; i++){
        			if(items[i].attr("pid")==pid||items[i].attr("pid")==""){
        				if($.inArray(items[i],allItems)==-1){
        					allItems.push(items[i]);
        					items[i].attr("level",getParentLevel(items[i].attr("pid"),0));
        					itemSort(items[i].attr("mid"))
        				}
        			}
        		}
        	}
        	//设置级别
        	function getParentLevel(pid,level){
        		if(pid==null){
        			return level;
        		}
        		for(var i = 0 ; i < items.length ; i++ ){
        			if(items[i].attr("mid")==pid){
        				level++;
        				return getParentLevel(items[i].attr("pid"),level);
        			}
        		}
        	}
        	
        	
        	//创建下拉框
        	function makeNode(nodes,conf){
        		for(var i = 0 ; i < nodes.length ; i++){
        			var li = $("<li class=\"collection-item\" style=\"padding-left:2px;padding-right:0px\" ></li>");
        			
        			li.attr("mid",eval("nodes[i]."+conf.mid));
        			li.attr("pid",eval("nodes[i]."+conf.pid));
        			var div = $("<div  ></div>");
        			var a = $("<a style=\"color:black;\"></a>"); 
        			if(conf.clickshow!=null&&conf.clickshow!=""){
        				
        				var clickshow = eval("("+conf.clickshow+")");
        				var routeUrl=clickshow.spath.replace("{nodeid}",nodes[i].id);
        				a.attr("href",routeUrl);
        				
        				/*a.attr("ui-sref-active","activecollection");
        				var clickshow = eval("("+conf.clickshow+")");
        				a.attr("ui-sref","."+clickshow.name+"({"
        														+"spath:\"" + clickshow.spath+ "\","
        														+"spage:\"" + clickshow.spage+ "\","
        														+"sid:\"" + clickshow.sid+ "\","
        														+"ppage:\"" + eval("nodes[i]."+conf.mid)+ "\""
        												     +"})");*/
        			}
        			//添加名称
        			a.append(eval("nodes[i]."+conf.name));
        			//添加空格
        			div.append("<span space></span>");
        			//添加图标
    				div.append(conf.collapse);
        			//添加a标签
        			div.append(a);
        			//添加工具栏
        			if(conf.toolshow!=undefined&&conf.toolshow!=null&&conf.toolshow!=""){
        				if(eval("nodes[i].type")!="root"&&eval("nodes[i].type")!="company"){
        					div.append(conf.remove);
        					div.append(conf.modify);
        				}
        				
	        			div.append(conf.add);
        			}
        			
        			li.append(div);
        			
        			items.push(li);
        		}
        		
        		//排序  并设置级别
        		itemSort(null);
        		//增加空格
        		setSpace();
        		//去图标
        		for(var i = 0 ; i < allItems.length ; i++ ){
        			if(!removeLogo(allItems[i])){
        				allItems[i].find("i[expand]").replaceWith('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
        			}
        		}
        		return allItems;
        	}
        	
        	//查找是否有下级
        	function removeLogo(node){
        		for(var i = 0 ; i < allItems.length ; i++){
        			if(node.attr("mid")==allItems[i].attr("pid")){
        				return true;
        			}
        		}
        		return false;
        	}
        	
        	//发送ajax请求数据
        	$.ajax({
     		   type: "POST",
     		   url: conf.url,
     		   success: function(data){
                   if (typeof data === "string" && data.constructor === String) {
                       var jsonData = $.parseJSON(data);
                   } else {
                       var jsonData = data;
                   }
     			  var html = $("<ul class='collection pt-0 mt-0' ><li  class='pink lighten-5 pt-14 pb-14 pl-5'><h4><i class='fa fa-sitemap'></i>组织架构</h4></li></ul>");
     			  //添加node
     			  element.append(html.append(makeNode(jsonData,conf)));
     			  //隐藏node工具栏
     			  hideTools();
     			  //显示要默认0-1级的节点
     		//	  hideNohasShow();
     			  //angularJs 编译html
     			  $compile(element.contents())(scope);
     			  //开启监听
     			  startLintener();
     		   }, 
     		   error: function(e) {
     			  $("ul.collection").find('a[add][remove][modify]')
     		   } 
        	});
        	
        	//初始化时的显示
        	function hideNohasShow(){
        		$("ul.collection li:not([show])").each(function(i){
        			$(this).hide(); 
        		});
        	}
        	//初始化时隐藏工具栏
        	function hideTools(){
        		 $("ul.collection").find("a[title]").each(function(i){
        			$(this).hide(); 
        		 });
        	}
        	
        	//返回当前节点的所有子节点    传li 和空数组
        	function findChild(node,childNode){
        		var mid = node.attr("mid");
        		for(var i = 0 ; i < items.length; i++){
        			if(mid==$(items[i]).attr("pid")){
        				childNode.push($(items[i]));
        				findChild($(items[i]),childNode);
        			}
        		}
    			return childNode;
        	}
        	
        	//------------------------------监听事件----------------------------------------
			function startLintener(){
				
				//点击选中变色事件
				$("ul.collection").find('a').each(function(i){
					 $(this).click(function(e){
						 $("ul.collection").find('li').each(function(){
							 $(this).removeClass(conf.clickColor);
						 })
						 $(this).parent().parent().removeClass("grey lighten-2").addClass(conf.clickColor);
					 });
					
				})
				 
				//折叠事件
	        	$("ul.collection").find('i[expand]').each(function (i) {
	                $(this).click(function (e) {
	                	 
	                	if($(this).parent().parent().attr("show")!=null){
	                		$.each(findChild($(this).parent().parent(),[]),function(i,a){
	                			a.removeAttr("show");
	                			a.hide();
	                		})
	                		$(this).removeClass("mdi-navigation-arrow-drop-up").addClass("mdi-navigation-arrow-drop-down");
	                		$(this).parent().parent().removeAttr("show");
	                	}else{
	                		$.each(findChild($(this).parent().parent(),[]),function(i,a){
	                			a.attr("show","");
	                			a.show();
	                		})
	                		$(this).removeClass("mdi-navigation-arrow-drop-down").addClass("mdi-navigation-arrow-drop-up");
	                		$(this).parent().parent().attr("show","");
	                	}
	                });
	                
	             });
	        	 
	        	//增删查改显示隐藏事件
	        	$("ul.collection").find('li').each(function (i) {
	                $(this).mouseleave(function(e){
	                	 if(!$(this).hasClass(conf.clickColor)){
	                		 $(this).removeClass("grey lighten-2");
	                	 }
	                	 if(conf.toolshow!=null&&conf.toolshow!=""){
		                	 $(this).find("a[title]").each(function(i){
				        			$(this).hide(); 
			        		 });
	                	 }
	                });
	                
	                
					$(this).mousemove(function(e){
						 if(!$(this).hasClass(conf.clickColor)){
							 $(this).addClass("grey lighten-2");
						 }
						 if(conf.toolshow!=null&&conf.toolshow!=""){
							 
							 function selectPage(a){
								 if(a.attr("add")!=undefined) return "add";
								 if(a.attr("modify")!=undefined) return "modify";
								 return "";
							 }
							 $(this).find("a[title]").each(function(i){
								 	//调整tools大小
								 	if($(this).parent().parent().parent().width()<299){
								 		$($(this).find("i")[0]).removeClass("fs-20").addClass("fs-18");
								 	}
								 	
								 	//增加和修改
								 	if($(this).attr("add")!=undefined||$(this).attr("modify")!=undefined){
								 		$(this).attr("ui-sref-active","activecollection");
				        				var toolshow = eval("("+conf.toolshow+")");
				        				var routeUrl = toolshow.spath+"/"+selectPage($(this))+"/"+$(this).parent().parent().attr("mid");
				        				
				        				$(this).attr("href",routeUrl);
				        				/*$(this).attr("ui-sref","."+toolshow.name+"({"
				        														+"spath:\"" + toolshow.spath+ "\","
				        														+"spage:\"" + selectPage($(this))+ "\","
				        														+"sid:\"" + toolshow.sid+ "\","
				        														+"ppage:\"" + $(this).parent().parent().attr("mid")+ "\""
				        												     +"})");*/
								 	}
								 	
								 	//删除
								 	if($(this).attr("remove")!=undefined){
								 		$(this).attr("ng-click","fnRemove(\""+$(this).parent().parent().attr("mid")+"\")");
								 	}
								 	
								 	
				        			$(this).show(); 
			        		 });
							 
							 $compile($(this).contents())(scope);
						 }
	                });
	                
	             });
	        	
	        	
			}
			//------------------------------监听事件----------------------------------------
        	
        	
        	
        	
        }
    } 
    

  }])

})
