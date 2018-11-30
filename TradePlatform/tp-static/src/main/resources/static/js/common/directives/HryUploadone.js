/**
 * 一件上传控件one 一个上传按钮，一个input显示文件名
 * 调用方式：<hry-uploadone
 * 			name="上传"       //必填
 * 			path="imgSrc"    //必填
 *          value="{{data}}" //回显数据
 *          prefix="" 请求资源前缀  eg. http://www.100.cn
 *        >    
 *       </hry-uploadone>
 *       
 * 调用示例：<hry-uploadone name="上传标题图片"  path="imgSrc" value={{titleImg}}></hry-uploadone>
 *         获得path路径方法$("#imgSrc").val();
 * 
 * add by liushilei 2016年6月25日 下午8:46:15
 */
define(['app','layer'], function (app,layer) {
    app.directive('hryUploadone', ["$rootScope",function ($rootScope) {
    return {  
        restrict: 'E',  
   //   require: '^ngModel',  
        scope: false,
        template: function(element){
        	//回显值
        	var value = element.attr("value");
        	var name = element.attr("name");
        	var path = element.attr("path");
        	//激活图片显示的位置 
        	var imgActivates=element.attr("img-activates");
        	isImgAct=false;
        	
        	if(value===undefined){
        		value="";
        	}
        	
        	var template = "<div class=\"file-field input-field\">"
        		          +"<div class='row'>"
        		         
				          +"<div class='col s12 m9 '>"
				          +"  		<input value=\""+value+"\" type=\"hidden\" id=\""+path+"\" name=\"uploadonePath\"  />"
				          +"   		<input value=\""+value+"\" id=\"uploadonePathShow_"+path+"\"  placeholder=\""+name+"\" name=\"uploadonePathShow\" readonly class=\"file-path validate \" type=\"text\" />"
				          +"   </div>"
				          
        		          +"<div class='col s12 m3 '>"
				          +" 	<a class=\"btn-floating green\" id=\"btn_"+path+"\" name=\"uploadoneBtn\"  ><i class='mdi-file-cloud-upload '></i></a>"  
				          +" 	<a class=\"btn-floating blue\" title='查看图片' id=\"see_"+path+"\"    ><i class='mdi-action-search '></i></a>"  

				          +"</div >"
				          +"	</div>"
				          +"	</div>";
    		return template;
        },
       
        
        link : function(scope, element, attributes, controllerInstance){
        	
        	// 页面加载完成后渲染
        	  scope.$watch("", init);
        	  

        	  function seeImg(e){
        		  var key=e.currentTarget.id.split("_")[1];
        		  var path = $("#uploadonePathShow_"+key).val();
        		  if(path===undefined||path==""){
        			  layer.msg('请先上传图片');
        		  }else{
        			  layer.open({
						  type: 1,
						  title: false,
						  closeBtn : 2,
						  area: '516px',
						  skin: 'layui-layer-nobg', //没有背景色
						  shadeClose: true,
						 // content:'<img src="'+HRY.host+"/"+path+'"/>'  modifyby zongwei 20180702
                          content:'<img src="'+path+'"/>'
                      });
        		  }
        	  }
			
			// 初始化
			function init() {
				
				var path=attributes.path;
				if(path===undefined){
					path="uploadId";
	        	}
				// 初始化图片上传
				var uploadBtn =$("#btn_"+path)[0];
				var seeBtn =$("#see_"+path)[0];
				var uploadPath = $("#"+path)[0];
				var uploadPathShow =$("#uploadonePathShow_"+path)[0];
                if(attributes.prefix===undefined) attributes.prefix="";
				
				$("#see_"+path).click(seeImg);
				
				new AjaxUpload(uploadBtn, {
					action : HRY.modules.web+ "/file/upload",
                    responseType: 'json',
					data : {},
					name : 'myfile',
					onSubmit : function(file, ext) {
						if (!(ext && /^(jpg|JPG|png|PNG|gif|GIF|bpm|BPM|ico)$/.test(ext))) {
							alert("您上传的图片格式不对，请重新选择！");
							return false;
						}
					},
					onComplete : function(file, response) {
						if(response!=undefined && response.success){
							uploadPathShow.value=response.obj[0].fileWebPath;
							uploadPath.value = response.obj[0].fileWebPath;
						}else{
							layer.msg("上传失败", {
				    		    icon: 1,
				    		    time: 2000
				    		});
						}
					}
				});
			}
        	
        }
    } 
  }])

})
