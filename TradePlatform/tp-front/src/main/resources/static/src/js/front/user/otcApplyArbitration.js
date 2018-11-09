define(function(require, exports, module) {

	module.exports = {
		
		//初始化方法
		init : function(){
            var num = 0;
            function changeCard(obj){
                if(!isCanvasSupported){
                    console.log("失败");
                }else{
                    compress(event, function(base64Img){
                        var arr= base64Img.split(',');
                        var base64 = arr[1];
                        if(num<3){
                            if(num == 0){
                                $(".uploadList li").eq(1).removeClass("hide");
                                $(".uploadList li").eq(0).find("img").attr("src",base64Img);
                            }else if(num == 1){
                                $(".uploadList li").eq(2).removeClass("hide");
                                $(".uploadList li").eq(1).find("img").attr("src",base64Img);
                            }else{
                                $(".uploadList li").eq(2).find("img").attr("src",base64Img);
                            }
                        }else
                            layer.msg("最多可上传3张照片",{"icon":5,"time":2000});
                        num++;
                    });
                }
            }
            //绑定添加图片事件
            $(".addphoto").change(function(){
                changeCard(this);
            });
            $(".btn-confirm").click(function(){
                var formData   =  new FormData($('#uploadForm')[0]);
                console.log(formData);
                $.ajax({
                    url: _ctx + '/otc/otcApplyArbitration',
                    type: 'POST',
                    data: formData,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (responseText) {
                        var data =  JSON.parse(responseText)
                        console.log(data);
                        debugger;
                        if(data.success) {
                            layer.msg(data.msg, {icon: 1, time: 1000},function() {
                                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                parent.refreshtable();
                                parent.layer.close(index);

                            });

                        }else{
                            layer.msg(data.msg, {icon: 5, time: 1000});
                        }
                    },error:function (e) {
                        layer.msg("服务器错误！", {icon: 5, time: 1000});
                    }
                });
            });
            $(".btn-otccancel").click(function(){
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                //window.parent.refreshtable;//访问父页面方法
                parent.layer.close(index);
               // parent.location.reload();
            });
		}

	}
	
	
	

});