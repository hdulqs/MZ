var HRY = {
  hosts: {
    admin: 'http://127.0.0.1:8080',
    oauth: 'http://127.0.0.1:8081',
    manage: 'http://127.0.0.1:8082',
  },
  frontHost: "http://127.0.0.1:8081",
  filehost: 'http://127.0.0.1:8081',
  staticUrl: '',
  ver: '20151203',
  head: '互融云软件',
  modules: {
    mstatic: "/admin/",
    web: '/manage/',
    oauth: '/oauth/',
    ico: '/manage/',
    factoring: '/manage/',
    finance: '/manage/',
    exchange: '/manage/',
    customer: '/manage/',
    account: '/manage/',
    sms: '/manage/',
    calculate: '/manage/',
    thirdpay: '/manage/',

  }
}

//如果logo、banner不存在，使用默认值
window.nofind = function (key) {
  if (key != null && key != "" && key != undefined) {
    var img = event.srcElement;
    if (key == "logo") {
      img.src = HRY.modules.mstatic + "static/style/images/nofind/"
          + key + ".png";
    } else if (key == "banner") {
      img.src = HRY.modules.mstatic + "/static/style/images/nofind/"
          + key + ".png";
    }
    img.onerror = null; //控制不要一直跳动
    img.alt = null;
  }
}

//允许跨域发送cookie,便于本地调试使用
/*
$.ajaxSetup({
  crossDomain: true,
  xhrFields: {
    withCredentials: true
  },
});*/
