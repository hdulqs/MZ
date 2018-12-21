define(function(require, exports, module) {

	module.exports = {

        //初始化方法
        init: function () {
            $(".btn-confirm").click(function () {
                //parent.window.location.reload();
                var confirmflag = $("#confirmcancelflag").is(":checked");
                if(!confirmflag){
                    layer.msg("请勾选确认！",{"icon":5,"time":2000});
                    return;
                }
                var formData = new FormData()
                formData.append('transactionorderid', $('#otctransactionorderid').val());
                formData.append('confirmcancelflag',confirmflag);

                $.ajax({
                    url: _ctx + '/otc/otcUndo',
                    type: 'POST',
                    data: formData,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if(data.success) {
                            layer.msg(data.msg, {icon: 1, time: 1000},function() {
                                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                parent.layer.close(index);
                                parent.refreshtable();
                            });

                        }else{
                            layer.msg(data.msg, {icon: 5, time: 1000});
                        }
                    }
                });
            });
            $(".btn-otccancel").click(function () {
                //parent.window.location.reload();
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);

            });
        }


    }


});