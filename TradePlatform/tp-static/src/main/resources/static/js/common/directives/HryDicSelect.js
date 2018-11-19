/**
 * 下拉框插件
 * 调用方式：<hry-select 
 *           select2  //可选  没有这个属性就是单选下拉框  有这个属性就是复选下拉框
 *           disabled //可选  没有这个属性可编辑      有这个属性为只读
 *           url="oauth/company/subcompany/findCompanyList"   //url  必填   url除去http://localhost/   
 *           name="name"       //必填    下拉框显示的字段
 *           value="id"        //必填    下拉框的值字段
 *           selectId="departmentList"   //必填   当前下拉框的值字段   用$("#departmentList").val()获取    注复选值为数组
 *           parentId="companyList"       //可选  二级连动时  上级的selectID  同一个上级下可以挂多个同级下级，且可以无限级连动
 *        >    
 *       </hry-select>
 * 
 * add by liushilei  2015/12/18 17:37
 */
define(['app'], function (app) {
    app.directive('hryDicselect', ["$rootScope",function ($rootScope) {
    return {  
        restrict: 'E',  
   //   require: '^ngModel',  
        scope: true, 
        template:'',
        link : function(scope, element, attributes, controllerInstance){
        	
        	var path = '';
        	var select = element;		  
        	var pDicKey=attributes.pdickey;
        	var projName=attributes.projname;
        	var url =HRY.modules.web+'dictionary/appdiconelevel/findbykey?Q_pDickey_eq_String='+pDicKey;
        	var name = "itemName";   //显示字段
        	var value = "itemValue"; //值字段
        	if(attributes.value!=undefined){
        		value = attributes.value;
        	}
        	var selectId = attributes.selectid; //selectId 用于post取值与回显取值
        	var	select2	= attributes.select2; //复选框  有这个属性表示是复选框
        	var	parentId = attributes.parentid; //二级连动Id
        	var dataTableSerch=attributes.datatableserch;
        	var dataTableSerchtrue="";
        	var labelName=attributes.labelname;
        	if(dataTableSerch==""){
        		dataTableSerchtrue="dataTableSerch";
        	}
        	
        	function createSelect(select){
       		 var html;
       		 var selectId = select.attr("selectId");
       		 if(select.attr("select2")!=undefined){
       			  if(select.attr("disabled")!=undefined){
       				  html = "<select disabled class='select2' multiple='' id='"+selectId+"'  "+dataTableSerchtrue+">";
       			  }else{
       				  html = "<select class='select2' multiple='' id='"+selectId+"'  "+dataTableSerchtrue+">";
       			  }
			   	 }else{
			   		  if(select.attr("disabled")!=undefined){
			   			  if(labelName!=undefined){
			   				 html = "<select class='select2' disabled id='"+selectId+"'   "+dataTableSerchtrue+"> <option value='' >"+labelName+"</option>"; 
			   			  }else{
			   				 html = "<select class='select2' disabled id='"+selectId+"'   "+dataTableSerchtrue+"> "; 
			   			  }
			   			 
			   		  }else{
			   			if(labelName!=undefined){
			   			   html = "<select class='select2'  id='"+selectId+"'  "+dataTableSerchtrue+"> <option value='' >"+labelName+"</option>"; 
			   			}else{
			   			 html = "<select class='select2'  id='"+selectId+"'  "+dataTableSerchtrue+"> "; 
			   			}
			   		}
			   	 }
       		 return html;
       	   }
   
        	
        	
        	//如果为只读  这里不加载
        	if(attributes.disabled!=undefined){
        		return false;
        	}
        	//如果为修改 这里不加载
        	if(attributes.modify!=undefined){
        		return false;
        	}
        	if(parentId!=undefined){//有父级  有父级直接渲染空
        		var html = createSelect(select);html +="<option value=''>请先择选上级</option></select>";select.append(html);
        	}else{
        	
        		//没有父级
	        	$.ajax({
	        		   type: "POST",
	        		   url: path+"/"+url,
	        		   selectId : selectId,//异步回调时鉴别下拉框
	        		   success: function(data){
	        			   var selectId = this.selectId;
	        			   var str = "hry-dicselect[selectId="+this.selectId+"]"
	        			   var select = $(str);
                           if (typeof data === "string" && data.constructor === String) {
                               data = $.parseJSON(data);
                           }
	        			   if(data!=null&&data.length>0){
	        				   var html =createSelect(select);
	            			   for(var i = 0 ; i < data.length; i++){
            					   html += "<option value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
	            			   }
	            			   html += "</select>";
	            			   select.append(html);
	        			   }else{
	        				   var html = createSelect();html +="<option value=''>无数据</option></select>";select.append(html);
	        			   }
	        			    
	        			   //渲染下拉框
	        	    	   conApp.initPlugins();
	        			   conApp.initMaterialPlugins();
	        			   //开启监听
	        			   startLintener();
	        			   
	        		   }, 
	        		   error: function(e) { 
	        			   var selectId = this.selectId;
	        			   var str = "hry-dicselect[selectId="+this.selectId+"]"
	        			   var select = $(str);
	        			   var html = createSelect(select);html +="<option value=''>加载异常，请检查后台方法</option></select>";select.append(html);
	        			   //渲染下拉框
	        	    	   conApp.initPlugins();
	        			   conApp.initMaterialPlugins();
	    			   } 
	    		});
    		
        	}

        	//渲染完成后开启监听事件
			//------------------------------监听事件----------------------------------------
			function startLintener(){
	        	$("div.select-wrapper ul").find('li:not(.optgroup)').each(function (i) {
	                $(this).click(function (e) {
	                	
	                	var selectId = $(this).parent().parent().parent().attr("selectId");
	                	var str = "hry-dicselect[parentId="+selectId+"]"
	                	var sonSelect  =$(str);
	                	//如果没有直接返回
	                	if(sonSelect==null||sonSelect==undefined||sonSelect==""||sonSelect.length==0){
	                		return ;
	                	}
	                	sonSelect.children().remove();//删除子元素
	                	
	                	var selectValue = $("#"+selectId).val();
	                	var data = {}
	                	data[selectId] = selectValue;//参数默认为parentId
	                	
	                	for(var i = 0 ; i < sonSelect.length ; i++){
	                		
		                	var path = '';
		                	var select = $(sonSelect[i]);		  
		                	var url = select.attr("url");     //请求url地址
		                	var name = select.attr("name");   //显示字段
		                	var value = select.attr("value"); //值字段
		                	var selectId = select.attr("selectid"); //selectId 用于post取值与回显取值
		                	var	select2	= select.attr("select2"); //复选框  有这个属性表示是复选框
		                	var	parentId = select.attr("parentid"); //二级连动Id
		                	
		                	
		                	$.ajax({
		 	        		   type: "POST",
		 	        		   url: path+"/"+url,
		 	        		   data : data,
		 	        		   selectId : selectId,//异步回调时鉴别下拉框
		 	        		   success: function(data){
			 	        			   var selectId = this.selectId;
				        			   var str = "hry-dicselect[selectId="+this.selectId+"]"
				        			   var select = $(str);
                                   if (typeof data === "string" && data.constructor === String) {
                                       data = $.parseJSON(data);
                                   }
			 	        			   if(data!=null&&data.length>0){
			 	        				   
			 	        				   var html = createSelect(select);
			 	        				   
			 	            			   for(var i = 0 ; i < data.length; i++){
			 	            				   html += "<option value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
			 	            			   }
			 	            			   html += "</select>";
			 	            			   select.append(html);
			 	        			   }else{
			 	        				   var html = createSelect(select);html +="<option value=''>无数据</option></select>";select.append(html);
			 	        			   }
			 	        			   
			 	        			    
			 	        			   //渲染下拉框
				        	    	   conApp.initPlugins();
				        			   conApp.initMaterialPlugins();
				        			   //再次开启监听
					        		   startLintener();
			 	        			   
			 	        		   }, 
			 	        		   error: function(e) { 
			 	        			   var selectId = this.selectId;
				        			   var str = "hry-dicselect[selectId="+this.selectId+"]"
				        			   var select = $(str);
			 	        			   var html = createSelect(select);html +="<option value=''>加载异常，请检查后台方法</option></select>";select.append(html);
			 	        			   //渲染下拉框
				        	    	   conApp.initPlugins();
				        			   conApp.initMaterialPlugins();
			 	        		   } 
			 	    		});
	                	
	                	}

	                	
	                });
	                
	             });
	        	//------------------------------监听事件----------------------------------------
			}
        	
        	
        }
    } 
    
//----------------------------------------------------------------------------------------------------------------------------   
//--------------------------------------------下面的注释请勿删除---add by liushilei-----------------------------------------------------------------------------   
//----------------------------------------------------------------------------------------------------------------------------   
/*    return {
    		contextPath : contextPath,
            restrict: 'E',
            replace : true,
            scope: true,
            transclude:true,
            template : "",
            compile: function(element, attributes) {  
            	var path = this.contextPath;
            	var select = element;
            	var url = attributes.url;
            	var name = attributes.name;   //显示字段
            	var value = attributes.value; //值字段
            	var sid = attributes.sid; 
            	$.ajax({
            		   type: "POST",
            		   async :false,
            		   url: path+"/"+url,
            		   success: function(data){
            			   data = $.parseJSON(data);
            			   if(data!=null&&data.length>0){
            				   var html = "<select id='"+sid+"' >";
	            			   for(var i = 0 ; i < data.length; i++){
	            				    
	            				   if(data[i].id==18){
	            					   html += "<option selected value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
	            				   }else{
	            					   html += "<option value='"+eval("data[i]."+value)+"' >"+eval("data[i]."+name)+"</option>"; 
	            				   }
	            			   }
	            			   html += "</select>";
	            			   select.after(html);
            			   }else{
            				   select.after("<select><select>");
            			   }
            		   }, 
            		   error: function(e) { 
            			   select.after("<select><select>");
        			   } 
        		})
        		
                return {  
                    pre: function preLink(scope, element, attributes) {  
                    },  
                    post: function postLink(scope, element, attributes) {  
                    }  
                };  
            }
            
    }*/
  }])

})
