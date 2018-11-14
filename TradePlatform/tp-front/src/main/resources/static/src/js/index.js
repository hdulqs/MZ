define(function (require, exports, module) {

  require("superslide");
  this.md5 = require("js/base/utils/hrymd5");
  require("lib/jquery/jquery.tmpl.min.js");

  module.exports = {
    //初始化方法
    init: function () {
      var language = $("#language").val();
      var yuyan = $("#language").val();
      var yy = $("#language").val();
      var yy1 = $("#language").val();
      if (language == "" || language == null) {
        language = "简体中文";
      } else if (language == "en") {
        language = "English";
      } else if (language == "zh_CN") {
        language = "简体中文";
      }
      if (yy == "zh_CN") {
        yy = "English";
        yy1 = "en";
      } else {
        yy = "简体中文";
        yy1 = "zh_CN";
      }
      $("#slide_lang dt span").html(
          '<i class=\'icon_lang icon_lang_' + yuyan.toLowerCase() + '\'></i>');
      $("#slide_lang_box a").html(
          '<i class=\'icon_lang icon_lang_' + yy1.toLowerCase() + '\'></i>'
          + yy);
      $("#slide_lang dt").on('click', function () {
        //获取下当前url地址
        var url = '';
        if (location.href.split('?').length > 1) {
          url = "?" + location.href.split('?')[1];
        }
        var split = window.document.location.pathname + url;

        $("#slide_lang_box").slideToggle()
        $(this).toggleClass('cur');
        if ($("#tokenId").val() != "") {
          if ($("#language").val() == "zh_CN") {
            //$("#slide_lang_box a").attr("href", _ctx + "/language.do?language=en&split=" + split + "&tokenId=" + $("#tokenId").val());
            $("#slide_lang_box a").attr("href",
                _ctx + "?lang=en&split=" + split + "&tokenId=" + $(
                "#tokenId").val());
          } else {
            //$("#slide_lang_box a").attr("href", _ctx + "/language.do?language=zh_CN&split=" + split + "&tokenId=" + $("#tokenId").val());
            $("#slide_lang_box a").attr("href",
                _ctx + "?lang=zh_CN&split=" + split + "&tokenId="
                + $("#tokenId").val());
          }
        } else {
          if ($("#language").val() == "zh_CN") {
            //$("#slide_lang_box a").attr("href", _ctx + "/language.do?language=en&split=" + split);
            $("#slide_lang_box a").attr("href",
                _ctx + "?lang=en&split=" + split);
          } else {
            //$("#slide_lang_box a").attr("href", _ctx + "/language.do?language=zh_CN&split=" + split);
            $("#slide_lang_box a").attr("href",
                _ctx + "?lang=zh_CN&split=" + split);
          }
        }
      })
      $("#slide_lang_box a").on('click', function () {
        var slidecon = $("#slide_lang dt span").html();
        //$("#slide_lang dt span").html('<i class=\'icon_lang icon_lang_'+yy1.toLowerCase()+'\'></i>'+yy);
        $('#slide_lang_box a').html(slidecon)
        $("#slide_lang dd").hide()
      })

      var lunbo = this.lunbo;
      //banner图
      $.ajax({
        url: _ctx + "/banner",
        type: "post",
        dataType: 'json',
        success: function (data) {
          if (data.obj != null) {
            var html = "";
            for (var i = 0; i < data.obj.length; i++) {
              if (data.obj[i].remark2 != null && data.obj[i].remark2
                  != "null") {
                html += "<b class='slideUp'  style='left: 0px;  background-image: url(/"
                    + data.obj[i].picturePath + ");'><a href='"
                    + data.obj[i].remark2 + "' target='_blank'></a></b>";

              } else {
                html += "<b class='slideUp'  style='left: 0px;  background-image: url(/"
                    + data.obj[i].picturePath
                    + ");'><a href='javascript:;' target='_blank'></a></b>";
              }
            }
            $("#banner_box").html(html);
          } else {
            $("#banner_box").html(
                "<b class='slideUp'  style='left: 0px;  background-image: url("
                + _ctx + "/static/" + _version
                + "/lib/exstatic/img/banner.jpg);'><a href='"
                + data.obj[i].remark2 + "' target='_blank'></a></b>");
          }
          lunbo();
        }
      });

      //邀请排行榜
      $.ajax({
        url: _ctx + "/selectCommendRanking",
        type: "post",
        dataType: 'json',
        success: function (data) {
          debugger;
          var html = "";
          for (var i = 0; i < data.obj.length; i++) {
            html += "<li class='commend-item'>"
                + "<img alt='' class='fl' src='" + _ctx + "/static/" + _version
                + "/lib/exstatic/img/" + (i + 1) + "pm.jpg'>"
                + "<dl class='fl'>"
                + "<dd>第" + (i + 1) + "名：</dd>"
                + "<dt>" + data.obj[i].name.substr(0, 1) + '***' + "</dt>"
                + "</dl>"
                + "<dl class='fr'>"
                + "<dd>获得佣金：</dd>"
                + "<dt>" + data.obj[i].fixPriceCoinCode + "</dt>"
                + "</dl>"

                + "</li>";
          }
          $(".commend").html(html);

        }
      });

      //最新动态
      $.ajax({
        url: _ctx + "/article",
        type: "post",
        dataType: 'json',
        data: {type: 4},
        success: function (data) {
          $("#zxdt4_tmp").tmpl(data.obj).appendTo("#zxdt4");
          //最新公告
          if (data != null && data.obj != null && data.obj[0] != null) {
            $("#news_zxgg").html(data.obj[0].title);
            $("#news_zxgg").attr("href", _ctx + "/news/info/" + data.obj[0].id);
          }
        }
      });
      //新闻资讯
      $.ajax({
        url: _ctx + "/article",
        type: "post",
        dataType: 'json',
        data: {type: 5},
        success: function (data) {
          $("#xwzx5_tmp").tmpl(data.obj).appendTo("#xwzx5");
        }
      });
      //行业动态
      $.ajax({
        url: _ctx + "/article",
        type: "post",
        dataType: 'json',
        data: {type: 6},
        success: function (data) {
          $("#hydt6_tmp").tmpl(data.obj).appendTo("#hydt6");
        }
      });
      //友情链接
      $.ajax({
        url: _ctx + "/friend",
        type: "post",
        dataType: 'json',
        success: function (data) {
          $("#divFriend_tmp").tmpl(data.obj).appendTo("#divFriend");
        }
      });
    },
    //轮播
    lunbo: function () {

      /*轮播*/
      var ali_box = $('#page_list');
      var aPage = $('#banner_big_box p');
      var aslide_img = $('.banner_box b');
      var iNow = 0;
      var list_num = aslide_img.length;

      var banner_list = "";
      for (var l = 0; l < list_num; l++) {
        if (l == 0) {
          ali_box.append('<li class="cur" style="width: 28px;"></li>');
        } else {
          ali_box.append('<li></li>');
        }
      }
      var ali = $('#index_banner_box li');
      ali.each(function (index) {
        $(this).click(function () {
          var o_index = ali_box.find('.cur').index();
          slide(index, o_index);
        })
      });

      function slide(index, o_index) {
        if (!arguments[1]) {
          o_index = "";
        }
        var _index;
        iNow = index;
        ali.eq(index).addClass('cur').siblings().removeClass();
        ali.eq(index).stop().animate({width: 28},
            400).siblings().stop().animate({width: 10},
            400).removeClass().stop().animate({width: 10}, 400);
        aPage.eq(index).siblings().stop().animate({opacity: 0}, 600);
        aPage.eq(index).stop().animate({opacity: 1}, 600);
        aslide_img.eq(index).stop().animate({opacity: 1}, 600).css(
            {"z-index": 5}).siblings().stop().animate({opacity: 0}, 600).css(
            {"z-index": 3});

      }

      function autoRun() {
        iNow++;
        if (iNow == ali.length) {
          iNow = 0;
        }
        slide(iNow);
      }

      autoRun();

      var timer = setInterval(autoRun, 4000);

      ali.hover(function () {
        clearInterval(timer);
      }, function () {
        timer = setInterval(autoRun, 4000);
      });

      aslide_img.hover(function () {
        clearInterval(timer);
      }, function () {
        timer = setInterval(autoRun, 4000);
      });
    },
    gundong: function () {

      Array.prototype.unique3 = function () {
        var res = [];
        var json = {};
        for (var i = 0; i < this.length; i++) {
          if (!json[this[i]]) {
            res.push(this[i]);
            json[this[i]] = 1;
          }
        }
        return res;
      }

      //行情列表
      $.ajax({
        url: _ctx + "/klinevtwo/index.do",
        type: "GET",
        dataType: 'json',
        success: function (data) {
          if (data != undefined) {
            $("#changeArea").empty();
            var coins = [];
            for (var i = 0; i < data.length; i++) {
              var change = data[i].coinCode.split("_")[1];
              coins.push(change);
            }
            var quchong = coins.unique3();

            //初始化交易区
            var activeArea = "";
//						if(quchong.length==1){
//							$("#changeArea").css('display','none')
//						}
            console.log(quchong.length)
            var html = "";
            for (var i = 0; i < quchong.length; i++) {
              var change = quchong[i];

              if (i == 0) {
                activeArea = change;
                html += "<li><a class=\"active\" href=\"#deal" + i
                    + "\" change=\"" + change + "\" data-toggle=\"tab\">"
                    + change + " " + jiaoyiqu + "</a></li>";
              } else {
                html += "<li><a  href=\"#deal" + i + "\" change=\"" + change
                    + "\" data-toggle=\"tab\">" + change + " " + jiaoyiqu
                    + "</a></li>";
              }
            }
            $("#changeArea").html(html);
            //默认交易区的货币
            var activeData = [];
            for (var i = 0; i < data.length; i++) {
              var change = data[i].coinCode.split("_")[1];
              if (activeArea == change) {
                activeData.push(data[i]);
              }
            }
            //默认交易区
            $("#productInfo").empty();
            $("#productInfo_tmp").tmpl(activeData).appendTo("#productInfo");

            if (quchong.length >= 3) {
              $(".mr_frbox").slide({
                titCell: "",
                mainCell: ".mr_frUl ul",
                autoPage: true,
                effect: "leftLoop",
                autoPlay: false,
                vis: 3
              });

            } else {
              $('.mr_frBtnL').css('display', 'none'), $('.mr_frBtnR').css(
                  'display', 'none')
            }
            //初始化交易区切换

          }

        }
      });

      /*首页交易区的内容切换*/
      $('.mr_frUl').on('click', ' ul li a', function () {
        $(this).addClass('active').parent().siblings().children().removeClass(
            'active');
        $("#productInfo").empty();
        var activeChange = $(this).attr("change");
        //行情列表
        $.ajax({
          url: _ctx + "/klinevtwo/index.do",
          type: "get",
          dataType: 'json',
          success: function (data) {
            debugger
            if (data != undefined) {
              $("#productInfo").html('');
              for (var i = 0; i < data.length; i++) {
                var change = data[i].coinCode.split("_")[1];
                if (activeChange == change) {
                  //激活交易区
                  var activeData = [];
                  activeData.push(data[i]);
                  $("#productInfo_tmp").tmpl(activeData).appendTo(
                      "#productInfo");
                  var olHeight = $("#marketlist").outerHeight();
                  $(".tab-pane").css("height", olHeight);
                  console.log(olHeight)
                }
              }

            }
          }
        });

      })
    }

  }
});