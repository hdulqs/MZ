define(function(require, exports, module) {
	
	
	
	
	
	module.exports = {
			//渲染URL加载的select
			init : function(){
					
					var allSelect = $("select[hry=true]");
					for(var i = 0 ; i < allSelect.length; i++){
						var select = $(allSelect[i]);
						var id = select.attr("id");
						var url = select.attr("url");
						var itemname = select.attr("itemname");
						var itemvalue = select.attr("itemvalue");
						var value = select.attr("value");
						
						$.ajax({
							type : "get",
							url : url,
							cache : false,
							id : id,//把下拉框ID传进来，解决异步问题
							dataType : "json",
							success : function(data) {
								for(var obj in data){
									if(value!=undefined&&value!=""){
										if(eval("data[obj]."+itemvalue)==value){
											$("#"+this.id).append("<option  selected=\"selected\"  value='"+eval("data[obj]."+itemvalue)+"' >"+ eval("data[obj]."+itemname) +"</option>");
										}else{
											$("#"+this.id).append("<option value='"+eval("data[obj]."+itemvalue)+"' >"+ eval("data[obj]."+itemname) +"</option>");
										}
										
									}else{
										//进行渲染
										$("#"+this.id).append("<option value='"+eval("data[obj]."+itemvalue)+"' >"+ eval("data[obj]."+itemname) +"</option>");
									}
								}
								
							},
							error : function(e) {
								
							}
						});
					}

			},
			getKey : function(key){
				var list = [];
				$.ajax({
					type : "get",
					url : _domain+"/admin/dic/appdic/getKey.do?key="+key,
					cache : false,
					async : false,
					dataType : "json",
					success : function(data) {
						list =  data;
					},
					error : function(e) {
					}
				});
				return list;
			}
	}

});