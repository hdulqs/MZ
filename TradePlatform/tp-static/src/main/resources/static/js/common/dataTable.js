/**
 * add by liushilei
 * 自动化智能搜索排序DataTable
 */
define([], function() {

		function config(){
			//参数
			var c =
				 {	
			//		"pagingType":   "full_numbers",  //分页带首页尾页
					"scrollX": "1119px",
					"bAutoSerch" : false,//是否开启自动搜索
					"bAutoWidth" : true,//自动计算列宽
					"bPaginate" : true,//是否开启分页
					"bServerSide" : true,//使用服务器数据
					"bProcessing" : true,//加载缓冲效果
					"bLengthChange" : false, //是否显示查询条数的下拉框 
					"filter" : false, //是否开搜索
					"onlyEvent" : true, //是否取消单行事件
				    "onlyClick" : null, // 给定某一列添加事件
					"deferRender" : true,
					"ordering" : false,
					"iDisplayLength" : 10, //默认查询多少条
					 "language": {
						 "emptyTable":     "未查询到任何数据",  
						 "infoEmpty":      "从 0 到 0 条",  
					     "processing": "数据加载中..."
					  },
	    	        "ajax": {
	    	        	"type" : "post",
	    	        	"dataSrc" : "rows",
	    	        	"url":"#######################",
	    	        	"data" :null,
	    	        	"timeout": 60000,
	    	        /*	"success" : function(obj){
	    	        		if(obj!=null&&(obj.success!=undefined)&&(!obj.success)){
	    	        			layer.msg(obj.msg, {icon: 2,time: 2000});
	    	        		}
	    	        	},*/
	    	        	"error" : function(response){
	    	        		 if( typeof response.responseText== "string"){
	    				        	if(response.responseText.indexOf("login_html")>0){
	    				        		layer.msg('登录超时,2秒后退出系统！', {icon: 2,time: 2000},function(){
	    				        			window.location.href=HRY.staticUrl+"/login.html";
	    				        		});
	    				        	}else{
	    				        		layer.msg('服务器超时', {icon: 2,time: 2000});
	    				        	}
	    		        	 }
	    	        		
	    	        	}
	    	        },
	    	        //"dom": '<lf<t>ip>',
	    	        "aLengthMenu" : [ [ 5, 10, 25, 50, -1 ],
	  								[ 5, 10, 25, 50, "all" ] ],
	    	        "columns" : [   
	    	                        {
										"data" : "id"
									}
								],
					"columnDefs" :null
						   
	    	    } 
			
			return c;
		
		}
			
		
		//生成dataTable
		function draw(table,conf){
			table.hryconf = conf;
			setTimeout(function() {
				table.DataTable(
						conf
				);
				
				
				
				//请求
				function filterGlobal () {
					table.DataTable().draw();
		    	}
				//文本框自动搜索框
			    $('input[dataTableSerch]').on('keyup',function () {
			    	//是否开启键入搜索
			    	if(conf.bAutoSerch){
			    		filterGlobal();
			    	}
			    });
			    setTimeout(function() {
			    	  //下拉框自动搜索
				    $('select[datatableserch]').on('change',function () {
				    	//是否开启键入搜索
				    	if(conf.bAutoSerch){
				    		filterGlobal();
				    	}
				    });
			    }, 1000)
			  
			    //日期自动搜索框
			    $('input[dataTableSerch].pikaday').on('change',function () {
			    	//是否开启键入搜索
			    	if(conf.bAutoSerch){
			    		filterGlobal();
			    	}
			    });
			    
			  //日期自动搜索框
			    $('input[dataTableSerch].clockpicker').on('change',function () {
			    	//是否开启键入搜索
			    	if(conf.bAutoSerch){
			    		filterGlobal();
			    	}
			    });
			    
			  //日期自动搜索框
			    $('input[dataTableSerch].form_datetime').on('blur',function () {
			    	//是否开启键入搜索
			    	if(conf.bAutoSerch){
			    		filterGlobal();
			    	}
			    });
			    //日期自动搜索框
			    $('input[dataTableSerch]').on('change',function () {
			    	//是否开启键入搜索
			    	if(conf.bAutoSerch){
			    		filterGlobal();
			    	}
			    });
			    
		 // ---------------------------------------------------------------------------
			//  
			    table.children("tbody").on('click', 'input[type=button]', function() {
			    	conf.onlyClick($(this));
			    });
			    
		 // ---------------------------------------------------------------------------
	 
			   
			    //行单击事件
			    table.children("tbody").on('click', 'tr', function() {
			     
			    	//判断是否取消单行点击事件  
			    	if(conf.onlyEvent){
				    	//变色
				    	$(this).toggleClass('selected');
				    	
				    	//单行打勾
				    	var checkbox = $(this).find("input[type=checkbox]")[0];
				    	if(!checkbox.checked){
				    		checkbox.checked = true;
				    	}else{
				    		checkbox.checked = false;
				    	}
			    	}
				});
			    
			    //勾单击事件
			    table.children("tbody").on('click', 'input[type^=checkbox][id^=checkbox][id!=checkboxAll]', function() {
			    	 
			    	if(this.checked){
			    		$(this.parentElement.parentElement).addClass('selected');
			    	}else{
			    		$(this.parentElement.parentElement).removeClass('selected');
			    	}
			    });
			    
				
			    //全选
			    $("#checkboxAll").on('click',function(){
			    	 
			    	
			    	var chekcboxlist = $("input[type^=checkbox][id^=checkbox][id!=checkboxAll]");
			    	var trList = table.find("tbody tr");
			    	for(var i = 0 ; i < chekcboxlist.length ; i++){
			    		if(this.checked){
			    			$(trList[i]).addClass('selected');
			    			chekcboxlist[i].checked  = true
			    		}else{
			    			$(trList[i]).removeClass('selected');
			    			chekcboxlist[i].checked  = false
			    		}
			    	}
			    	
			    });
			    //重置全选按钮
				table.on( 'page.dt',   function () { 
					if($("#checkboxAll")[0]!=undefined){
						$("#checkboxAll")[0].checked = false;
					}					
				}).DataTable();
				
				//防止dataTable后渲染  直接渲染完dataTable后执行插件
				conApp.initMaterialPlugins();
			}, 300);
		}
		
		
		/**
		 * 获得选中的数据id,
		 * 默认主键字段名称为id
		 * 如果主键字段为userId ,则传一个userId
		 */
		function  getSelect(table,id){
			 
			var idName = "id" ;
			if(id!=null&&id!=""&&id!="undefined"){
				idName = id;
			}
			
			var data = table.DataTable().rows('.selected').data();
			var ids = []
			for(var i = 0 ; i < data.length ; i++){
				ids.push(eval("data[i]."+idName));
			}
			return ids;
		}
		
		/**
		 * 获得选中行的行记录
		 */
		function  getRowData(table){
			var data = table.DataTable().rows('.selected').data();
			return data;
		}
		
		
		/**
		 * 封装select下拉框数据
		 * add by liushilei
		 */
		function selectData(data){
			 var selects = $('select[dataTableSerch]');
			 if(selects!=null&&selects.length>0){
				 for(var i = 0 ; i < selects.length; i++){
					 var select = $(selects[i]);
					 var name = select.attr("name");
					 if(name==undefined||name==""){
						 name = select.attr("id");
					 }
					 var value = select.val();
					 if(value!=null&&value!=""){
						 eval("data."+name +"='"+value+"'");
				 	 }else{
				 		 //画龙点睛之笔,含意：当属性值为空时删除这个属性,起到清空JS缓存作用，可少写一个JAVA  if 判断
				 		 eval("delete data."+name);
				 	 }
				 }
			 }
		}
		
		function inputData(data){
			 var input = $('input[dataTableSerch].form_datetime');
			 if(input!=null&&input.length>0){
				 for(var i = 0 ; i < input.length; i++){
					 var input = $(input[i]);
					 var name = input.attr("name");
					 if(name==undefined||name==""){
						 name = input.attr("id");
					 }
					 var value = input.val();
					 if(value!=null&&value!=""){
						 eval("data."+name +"='"+value+"'");
				 	 }else{
				 		 //画龙点睛之笔,含意：当属性值为空时删除这个属性,起到清空JS缓存作用，可少写一个JAVA  if 判断
				 		 eval("delete data."+name);
				 	 }
				 }
			 }
		}
		
		/**
		 * 万能...............导出excel
		 * 前三个必填
		 * 必填 table  当前的dataTable对象
		 * 必填 serchData  查询参数
		 * 必填 file  excel下载的文件名
		 * 可选 headerArr 表头名称  一一对应字段
		 * 可选 columnArr 字段名称  一一对应表头
		 * add by liushilei  就是这么神奇
		 */
		function excel(table,serchData,filename,headerArr,columnArr){
			//获得columns
			var cvs = "";
			var columns  = table.hryconf.columns;
			for(var i = 0 ; i < columns.length; i++){
				cvs += columns[i].data;
				if(i!=columns.length-1){
					cvs += ",";
				}
			}
			
		
			//获得渲染规则
			//分别为  "参数位置 ":"计算规则:规则明细"
			//组成 1:VALUE_CONVERT:1=甲类,2=乙类,3=丙类,else=
			//或  1:VALUE_OPERATION: transactionMoney-fee
			//规则用  & 连接
			var ruleStr="";
			var rederRules  = table.hryconf.columnDefs;
			if(rederRules!=undefined){
				
				for(var i = 0 ; i < rederRules.length; i++){
					var target=rederRules[i].targets;
					var renderRule= rederRules[i].renderRule;
					var renderValueRule= rederRules[i].renderValueRule;
					if(renderRule && renderValueRule 
							&& ("VALUE_CONVERT"==renderRule || "VALUE_OPERATION"==renderRule)){//替换值,或值的计算
						ruleStr+=target+":"+renderRule+":"+renderValueRule;
						if(i!=rederRules.length-1){
							ruleStr += "&";
						}
					}else if(renderRule && "NOT_SHOW"==renderRule){//该列不显示
						//为保持一致先这样拼接
						ruleStr+=target+":"+renderRule+":"+renderRule;
						if(i!=rederRules.length-1){
							ruleStr += "&";
						}
					}
				}
			}
			//获得header
			var ths = table.find("th");
			var titles = "ID,";
			//从1开始循环跳过ID列
			for(var i= 1 ; i < ths.length ; i++){
				titles += $(ths[i]).find("div").html();
				if(i!=ths.length-1){
					titles += ",";
				}
			}
			
			//获得请求地址
			var url = table.hryconf.ajax.url
			
			//ajax请求数据
			var d = {};
			
			//获得sessionId,进行免登录
			var sessionId = $.getCookie("sdfer");
			
			//清空form
			var tempExcleForm = $("#excelForm")
			if(tempExcleForm!=undefined){
				tempExcleForm.remove();
			}
			
		    var turnForm = document.createElement("form");   
		    turnForm.setAttribute("id","excelForm")  
		      
		    //一定要加入到body中！！   
		    document.body.appendChild(turnForm);
		    turnForm.method = 'post';
		    turnForm.action = "/manage/export/exportutil/down";   //领略神奇的入口  add by liushilei
		    turnForm.target = '_blank';
		    //创建隐藏表单  五个必要参数
		    //表头、字段名、数据请求路径、登录认证信息、下载的文件名称
		    var cvsElement = document.createElement("input");
		    cvsElement.setAttribute("name","excel_cvs");
		    cvsElement.setAttribute("type","hidden");
		    cvsElement.setAttribute("value",cvs);
		    turnForm.appendChild(cvsElement);
		    
		    var titlesElement = document.createElement("input");
		    titlesElement.setAttribute("name","excel_titles");
		    titlesElement.setAttribute("type","hidden");
		    titlesElement.setAttribute("value",titles);
		    turnForm.appendChild(titlesElement);
		    
		    var urlElement = document.createElement("input");
		    urlElement.setAttribute("name","excel_url");
		    urlElement.setAttribute("type","hidden");
		    urlElement.setAttribute("value",url);
		    turnForm.appendChild(urlElement);
		    
		    var sessionIdElement = document.createElement("input");
		    sessionIdElement.setAttribute("name","excel_sessionId");//excel_sessionId 是神奇导出功能的钥匙 add by liushilei  
		    sessionIdElement.setAttribute("type","hidden");
		    sessionIdElement.setAttribute("value",sessionId);
		    turnForm.appendChild(sessionIdElement);
		    
		    var filenameElement = document.createElement("input");
		    filenameElement.setAttribute("name","excel_filename");
		    filenameElement.setAttribute("type","hidden");
		    filenameElement.setAttribute("value",filename);
		    turnForm.appendChild(filenameElement);
		    
		    var filePathElement = document.createElement("input");
		    filePathElement.setAttribute("name","excel_filePath");
		    filePathElement.setAttribute("id","excel_filePath");
		    filePathElement.setAttribute("type","hidden");
		    filePathElement.setAttribute("value","");
		    turnForm.appendChild(filePathElement);
		    
		    
		    d.excel_cvs = cvs;
		    d.excel_titles = titles;
		    d.excel_url = url;
		    d.excel_sessionId = sessionId;
		    d.excel_filename = filename;
		    d.excel_renderRules=ruleStr;
		    
		    
		    $.each(serchData, function(name, value) {
				if ("" != value) {
					var element = document.createElement("input");
					element.setAttribute("name",name);
					element.setAttribute("type","hidden");
					element.setAttribute("value",value);
				    turnForm.appendChild(element);
				    
				    eval("d."+name+"='"+value+"'")
				    
				}
			});
		    
		    layer.msg("下载中,数据量过大，请耐心等待...", {
		    	  icon: 16,
		    	  shade: 0.4,
		    	  time: 200000000
	    	});
		    
			$.ajax({
				type : "POST",
				data :d,
				url : "/manage/export/exportutil/excel",
				dataType : "json",
				timeout : 6000000,
				success : function(data) {
					$("#excel_filePath").val(data.msg);
					turnForm.submit();
					layer.closeAll();
				}
			}); 
		    
		}
		
		return {
			draw : draw,
			config : config,
			getSelect :  getSelect,
			selectData : selectData,
			getRowData : getRowData,
			inputData : inputData,
			excel : excel
		}
		
		

})
