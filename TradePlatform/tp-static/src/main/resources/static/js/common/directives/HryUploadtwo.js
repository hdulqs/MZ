/**
 * 一件上传控件two 一个上传按钮，一个input显示文件名
 * 调用方式：<hry-uploadone
 *      path="imgSrc"    //必填
 *          delbtn=""   //非必填  填false时  不显示删除按钮
 *          uploadbtn=""  //非必填  填false时 不显示上传按钮
 *        >
 *       </hry-uploadone>
 *
 * 调用示例：<hry-uploadtwo path="imgSrc" delbtn="false" uploadbtn="false"></hry-uploadtwo>
 *         获得path路径方法$("#imgSrc").val();
 *
 * add by liushilei 2016年6月25日 下午8:46:15
 */
define(['app'], function (app) {
  app.directive('hryUploadtwo', ["$rootScope", function ($rootScope) {
    return {
      restrict: 'E',
      //   require: '^ngModel',
      scope: false,
      template: function (element) {

        var name = element.attr("name");
        var path = element.attr("path");
        var delbtn = element.attr("delbtn");
        var uploadbtn = element.attr("uploadbtn");

        var template = "<div class=\"row photoswipe-gallery isotope mr-20\"> "
            + "    <a name=\"uploadtwoA\" class=\"col s12 item\" " +
            "href=\"/{{formData.picturePath}}\"     data-size=\"2880x1800\" " +
            "data-med=\"/{{formData.picturePath}}\" data-med-size=\"1440x900\" data-author=\"\">"
            + "  	<img name=\"uploadtwoImg\" "
            + " 	 src=\"/{{formData.picturePath}}\"  "
            + "  	 onerror=\"nofind();\" "
            + "  	 alt=\"logo\" /> "
            + "	    </a> "
            + "  </div> "
            + "  <p> "
            + "		<input value=\"{{formData.picturePath}}\" type=\"hidden\" id=\""
            + path + "\" name=\"uploadtwoPath\" />  ";
        if (uploadbtn != undefined && uploadbtn == "false") {
          template += "		<input class=\"btn hide\" type=\"button\"  name=\"uploadtwoBtn\" value=\"上传\" />   ";
        } else {
          template += "		<input class=\"btn\" type=\"button\" name=\"uploadtwoBtn\" value=\"上传\" />   ";
        }
        if (delbtn != undefined && delbtn == "false") {
        } else {
          template += "	    <input  class=\"btn\" type=\"button\" name=\"uploadTwoDelBtn\" value=\"删除\" />   ";
        }

        template += " </p> ";

        return template;
      },
      link: function (scope, element, attributes, controllerInstance) {

        init();

        // 初始化
        function init() {
          // 初始化图片上传
          var uploadBtn = $("hry-uploadtwo input[name=uploadtwoBtn]")[0];
          // 删除按钮
          var uploadDelBtn = $("hry-uploadtwo input[name=uploadTwoDelBtn]")[0];
          //图片路径
          var uploadPath = $("hry-uploadtwo input[name=uploadtwoPath]")[0];
          //图片
          var uploadtwoImg = $("hry-uploadtwo img[name=uploadtwoImg]")[0];
          //图片放大器A标签
          var uploadtwoA = $("hry-uploadtwo a[name=uploadtwoA]")[0];

          new AjaxUpload(uploadBtn, {
            action: HRY.modules.web + "/file/upload",
            data: {},
            name: 'myfile',
            onSubmit: function (file, ext) {
              if (!(ext && /^(jpg|JPG|png|PNG|gif|GIF|bpm|BPM)$/.test(ext))) {
                alert("您上传的图片格式不对，请重新选择！");
                return false;
              }
            },
            onComplete: function (file, response) {
              var resp = JSON.parse(response.substring(response.indexOf("{"),
                  response.lastIndexOf("}") + 1));
              if (resp != undefined && resp.success) {
                //图片路径
                uploadPath.value = resp.obj[0].fileWebPath;
                debugger;
                //展示图片
                uploadtwoImg.src = resp.obj[0].fileWebPath;

                //图片放大器
                $(uploadtwoA).attr("data-med", resp.obj[0].fileWebPath);
                uploadtwoA.href = resp.obj[0].fileWebPath;
              } else {
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
