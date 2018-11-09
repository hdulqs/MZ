<!DOCTYPE html>
<html>

<head>
  <#include "/base/base.ftl">
  <meta charset="utf-8">
  <@HryTopOrFooter url="base/title.ftl"/>
  <link href="${ctx}/static/${version}/lib/view_v1/dist/style/common/global.css"  rel="stylesheet" />
  <link href="${ctx}/static/${version}/lib/view_v1/dist/style/iconfont/iconfont.css" rel="stylesheet" />
  <link href="${ctx}/static/${version}/lib/view_v1/dist/style/bootstrap/bootstrap.min.css" rel="stylesheet" />
  <link href="${ctx}/static/${version}/lib/swiper/swiper-4.3.3.min.css" rel="stylesheet" />
  <link href="${ctx}/static/${version}/lib/view_v1/dist/style/index.css?v=20181101" rel="stylesheet" />
  <link href="${ctx}/static/${version}/lib/layer/css/layer.css" rel="stylesheet" />
  <style type="text/css">
    .swiper-container{
      margin: 0 auto;
      overflow-x: scroll;
    }
    .swiper-wrapper{
      width: auto;
      margin: 0 auto;
    }
    .swiper-slide{
      width: 270px !important;
    }

    @media (max-width: 767px) {
      /*在小于768像素的屏幕里,这里的样式才生效*/
      .swiper-slide{
        width: 200px !important;
      }
      .trade-area .trade-header .nav-drd{
        width: 200px !important;
        height: 51.85px;
        line-height: 51.85px;
      }
      .trade-area .trade-header .nav-drd.active{
        width: 200px !important;
        height: 62.96x !important;
        line-height: 55px !important;
      }
    }
    @media (min-width: 768px) and (max-width: 991px) {
      /*在768和991像素之间的屏幕里,这里的样式才生效*/
      .swiper-slide{
        width: 228px !important;
      }
      .trade-area .trade-header .nav-drd{
        width: 228px !important;
        height: 59.1px;
        line-height: 59.1px;
      }
      .trade-area .trade-header .nav-drd.active{
        width: 228px !important;
        height: 71.78px !important;
        line-height: 64.5px !important;
      }

      .download{
        background-size: 45% 45%;
      }
      .download .download-title{
        width: 50%;
        float: right;
      }
      .download-code:nth-child(1){
        margin-right: 20px;
      }
      .download-codes{
        margin-bottom: 30px;
      }
    }
    /*@media (min-width: 992px) and (max-width: 1199px) {
      !*在992和1199像素之间的屏幕里,这里的样式才生效*!
    }
    @media (min-width: 1200px) {
      !*在大于1200像素的屏幕里,这里的样式才生效*!
    }*/
	
	.download-box{
          background: #05183a !important;
          padding:40px 0 60px;
      }
      .download-box h3{
          font-size: 36px;
          color: #fff;
      }
      .download-box p{
          color:#fff;
          font-size: 20px;
      }
      .download-first-title{
          color: #fff;
          font-size: 30px;
          text-align: center;
      }
      .download-second-title{
          color: #a6a6a6;
          font-size: 18px;
          text-align: center;
          padding: 20px 0 40px;
      }
      .display_flex{
          /* Center slide text vertically */
          display: -webkit-box;
          display: -ms-flexbox;
          display: -webkit-flex;
          display: flex;
          -webkit-box-pack: center;
          -ms-flex-pack: center;
          -webkit-box-align: center;
          -ms-flex-align: center;
          -webkit-align-items: center;
          align-items: center;
          -webkit-justify-content: center;
          justify-content: center;
      }
      .flex_img1{
          width:100%;
          margin-right: 116px;
      }
      .flex_img2{
          margin-right: 80px;
          position: relative;
      }
      .flex_img2 img{
          width: 140px;
          height: 170px;
      }
      .flex_img2_absolute{
          position: absolute;
          top: 15px;
          left: 15px;
          width:110px !important;
          height:110px !important;
      }
  </style>
</head>

<body>

  <div class="slider-box">
	<div id="slider" class="banner flexslider fl" style="width:100%;">
		<ul class="slides" id="banner_box">
		</ul>
    </div>
    <ul class="notice" >
        <li><a id="news_zxgg" href=""></a></li>
        <span class="close-notice">x</span>
    </ul>
  </div>
 <link rel="stylesheet" href="">

<@HryTopOrFooter url="base/navbox.ftl"/>

  <div class="container-drd">
    <div class="trade-area">
      <div class="trade-header">
        <!--<div class="swiper-container">
          <div class="swiper-wrapper" id="areaname">
            &lt;!&ndash;<div class="swiper-slide"><div class="nav-drd fl active">BTC交易区</div></div>
            <div class="swiper-slide"><div class="nav-drd fl">BTC交易区</div></div>
            <div class="swiper-slide"><div class="nav-drd fl">BTC交易区</div></div>
            <div class="swiper-slide"><div class="nav-drd fl">BTC交易区</div></div>
            <div class="swiper-slide"><div class="nav-drd fl">BTC交易区</div></div>
            <div class="swiper-slide"><div class="nav-drd fl">BTC交易区</div></div>&ndash;&gt;
          </div>
          &lt;!&ndash;<div class="swiper-scrollbar"></div>&ndash;&gt;
          &lt;!&ndash;<div class="swiper-pagination"></div>&ndash;&gt;
        </div>-->
          <div class="trade-nav-drd" id="areaname">
              <!--<div class="nav-item active">USDT交易区</div>
              <div class="nav-item">USDT交易区</div>-->
          </div>

        <!--<ul class="fl"  id="areaname">
          &lt;!&ndash;
            <li class="active"><i class="iconfont icon-star"></i>自选</li>
            <li><i class="iconfont icon-star"></i>BTC交易区</li>
           &ndash;&gt;
        </ul>-->
        <!-- <div class="trade-search">
          <i class="iconfont icon-search"></i>
          <input type="search" name="" value="">
        </div>-->
      </div>
      <div class="trade-con">
        <table>
          <thead>
          <tr>
            <th><@spring.message code="bizhong"/></th>
            <th><@spring.message code="zuixinchengjiaojia"/></th>
            <th><@spring.message code="rizhangdie"/></th>
            <th><@spring.message code="zuidijia"/></th>
            <th><@spring.message code="zuigaojia"/></th>
            <th><@spring.message code="zuorishoupanjia"/></th>
            <th><@spring.message code="24hourjiaoyiliang"/></th>
          </tr>
          </thead>
          <tbody  id="changearea">
          <!-- <tr>
            <td><i class="iconfont icon-star red-txt"></i><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index_03.jpg" alt="">IPT/BTC</td>
            <td><span class="red-txt">$51.380 /</span> ¥335.727</td>
            <td class="red-txt"><i class="iconfont red-txt icon-arrows-4-7"></i>+2.77%</td>
            <td><span class="red-txt">$51.380 /</span> ¥335.727</td>
            <td><span class="red-txt">$51.380 /</span> ¥335.727</td>
            <td>11135951 IPT</td>
          </tr>
          <tr>
            <td><i class="iconfont icon-star"></i><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index_03.jpg" alt="">IPT/BTC</td>
            <td><span class="green-txt">$51.380 /</span> ¥335.727</td>
            <td class="green-txt"><i class="iconfont green-txt icon-jiantouxia"></i>-2.77%</td>
            <td><span class="green-txt">$51.380 /</span> ¥335.727</td>
            <td><span class="green-txt">$51.380 /</span> ¥335.727</td>
            <td>11135951 IPT</td>
          </tr>
          -->
          </tbody>
        </table>
      </div>
    </div>
    <!--<div class="ex-introduce-box">
      <img class="flex" style="margin: 95px auto 64px auto;padding: 30px 0 0" src="${ctx}/static/${version}/lib/view_v1/dist/images/index/advantageNav.png" alt="">
        <div class="ex-introduce">
          <dl class="">
            <dd><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/1.png" /></dd>
            <dt style="font-weight: 500;"><@spring.message code="zcdzjyxs"/></dt>
            <dd><@spring.message code="zcdzjyxs2"/>
            </dd>
          </dl>
          <dl class="">
            <dd><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/2.png" /></dd>
            <dt style="font-weight: 500;"><@spring.message code="kzkj"/></dt>
            <dd><@spring.message code="kzkj2"/>
            </dd>
          </dl>
          <dl class="">
            <dd><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/3.png" /></dd>
            <dt style="font-weight: 500;"><@spring.message code="24ksfw"/></dt>
            <dd><@spring.message code="24ksfw2"/></dd>
          </dl>
        </div>
    </div>-->
      <div class="ex-homeNews">
        <p style=" text-align: center;font-size: 36px;" bacg><@spring.message code="xinwendongtai"/></p>
        <!--<img class="flex" style="margin: 0 auto 30px auto" src="${ctx}/static/${version}/lib/view_v1/dist/images/index/newsNav.png" alt="">-->
        <ul class="col-xs-6" style="padding-right:3%;" id="xwzx">
          <h3><span><@spring.message code="xinwenzixun"/></span><a class="ex-homeNews" href="${ctx}/news/index/5"><@spring.message code="more"/></a></h3>
        </ul>
        <ul class="col-xs-6" style="padding-left:3%;"  id="hydt">
          <h3><span><@spring.message code="hangyedongtai"/></span><a class="ex-homeNews" href="${ctx}/news/index/4"><@spring.message code="more"/></a></h3>
        </ul>
      </div>

   
    
    <div class="download-box fl">
	    <div class="container">
	      <div class="display_flex">
	          <img class="flex_img1" src="${ctx}/static/${version}/img/index_doqnload.png"></img>
	          <div>
	                <h3><@spring.message code="taiweixiaoyidongAPPshanliangdengchang"/></h3>
	                <p><@spring.message code="jiduozhonglinghuojiaoyimoshiyuyiti"/>，<@spring.message code="tiyanyizhanshihuobifuwu"/>，<@spring.message code="bicaifu"/>，<@spring.message code="yishouzhangwo"/></p>
	                <div class="display_flex" style="margin-top: 20px !important;">
	                <a href="http://sdludax.com.bdy.ctrlqh.cn/Smile/Smile.html">
	                    <div class="flex_img2">
	                        <img src="${ctx}/static/${version}/img/index_doqnload1.png">
	                        <img class="flex_img2_absolute" src="${ctx}/static/${version}/lib/view_v1/dist/images/ios.png" alt="">
	                    </div>
	                    </a>
	                    
	                    <div class="flex_img2">
	                        <img src="${ctx}/static/${version}/img/index_doqnload2.png">
	                        <img class="flex_img2_absolute" src="${ctx}/static/${version}/lib/view_v1/dist/images/android.png" alt="">
	                    </div>
	                </div>
	          </div>
	      </div>
	    </div>
    </div>

    <div class="friend-box clear">
        <div class="friend">
          <h3><@spring.message code="friendLink"/></h3>
          <ul id="friendlink">
              <!--<li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>
              <li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>
              <li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>
              <li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>
              <li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>
              <li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>
              <li class="fl"><a><img src="${ctx}/static/${version}/lib/view_v1/dist/images/index/fireCoin.png" /></a></li>-->
          </ul>
        </div>
    </div>
  </div>

<@HryTopOrFooter url="base/footerv1.ftl"/>

</body>

<script type="text/javascript"  src="${ctx}/static/${version}/lib/view_v1/dist/js/plugins/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/jquery/jquery.cookie.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/layer/layer.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/js/i18n_base.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/seajs/sea.js"></script>
<script type="text/javascript"  src="${ctx}/static/${version}/lib/swiper/swiper-4.3.3.min.js"></script>
<script type="text/javascript">
 seajs.config({
    base: "${ctx}/static/${version}",
    alias: {
      <!-- 基础框架JS -->
      "jquery": "lib/view_v1/dist/js/plugins/jquery/jquery-3.2.1.min.js",
      <!-- layer -->
      "layer" : "lib/layer/layer.js",
      <!-- 自定义JS -->
      "base": "js/base/base.js"
    },
    preload: ['jquery','layer'],
    map:[
		['.js','.js?v=${t}']//映射规则
	]
  });
 
 seajs.use(["js/index_view_v1"],function(m){
	  m.init();
      
      
      //切换语言
	  $('.curbox').on('click', function() {
	    var that = $(this);
	    that.next('.lang-list').slideToggle();
	  })
	
	  $('.lang-list li').on('click', function() {
	    var target = $('.lang-cur').html();
	    $(this).parent().hide(), $('.lang-cur').html($(this).html()), $(this).html(target);
	    if($('.lang-cur').html().indexOf("CN")!=-1){
	   		window.location.href = _ctx+"/language.do?language=zh_CN&split=/index";
	    }else if($('.lang-cur').html().indexOf("EN")!=-1){
	    	window.location.href = _ctx+"/language.do?language=en&split=/index";
	    }else if($('.lang-cur').html().indexOf("TN")!=-1){
	    	window.location.href = _ctx+"/language.do?language=tn&split=/index";
	    }
	    
	  })

 });
</script>

</html>
