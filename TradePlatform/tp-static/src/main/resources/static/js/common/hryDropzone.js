
(function ($) {
 $.fn.hryDropzone = function(options) { //定义插件的名称，这里为userCp
	 var rootPath=options.ctx;
	 var dft = {  
    		    url: rootPath+"/file/appfile/upload.do?mark="+options.mark,
			    maxFiles: 10,
		        maxFilesize: 2048,
		    //  acceptedFiles: ".txt",
		        addRemoveLinks : true,//添加移除文件
		        autoProcessQueue: false,//不自动上传
		        dictCancelUploadConfirmation:'你确定要取消上传吗？',
		        dictMaxFilesExceeded: "您一次最多只能上传{{maxFiles}}个文件",
		        dictFileTooBig:"文件过大({{filesize}}MB). 上传文件最大支持: {{maxFilesize}}MB.",
		        dictDefaultMessage :
		        '<span ><i ></i> 拖动文件至该处</span> \
		        <span >(或点击此处)</span> <br /> \
		        <i ></i>',
		        dictResponseError: '文件上传失败!',
		    //  dictInvalidFileType: "你不能上传该类型文件,文件类型只能是*.xls。",
		        dictCancelUpload: "取消上传",
		        dictCancelUploadConfirmation: "你确定要取消上传吗?",
		        dictRemoveFile: "移除文件",
		        addRemoveLinks:true,//默认false。如果设为true，则会给文件添加一个删除链接。
		   //   uploadMultiple: true,
		        parallelUploads: 100,
		        init : function() {
		                var submitButton = document.querySelector("#submit-all")               
						myDropzone = this; // closure 
		                
		                //为上传按钮添加点击事件     
						submitButton.addEventListener("click", function () {
						     myDropzone.processQueue();          
						 });         
						
					    //当添加图片后的事件，上传按钮恢复可用           
					    this.on("addedfile", function () {              
						    $("#submit-all").removeAttr("disabled");               
						});               
					    
						this.on("complete", function(data) {
							 
							var res = eval('(' + data.xhr.responseText + ')');
							data.fileid=res[1];
							
							
						//	alert(" success : "+ res.success +"\n" + " msg : "+ res.msg +"\n" );
						});
						this.on("removedfile", function( file) {
							 var data={id:file.fileid};
							  
							 if(typeof(file.fileid)!='undefined'){
								 $.ajax({
								     type: 'POST',
								     url: rootPath+"file/appfile/delete.do",
								     data: data,
								     dataType: 'json',
								     success:   function(data, textStatus){
								    	 
								    	  
								    	 
								     }

								});

								 
							 }
							
							 
							 return true;
						});
					}};
	 var ops = $.extend(dft,options);
	 $(this).dropzone(ops);
 }
})(jQuery);