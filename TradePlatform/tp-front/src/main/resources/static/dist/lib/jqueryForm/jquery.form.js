define(function(require,exports,module){require("jquery"),function(e){"use strict";"function"==typeof define&&define.amd?define(["jquery"],e):e("undefined"!=typeof jQuery?jQuery:window.Zepto)}(function($){"use strict";function e(e){var t=e.data;e.isDefaultPrevented()||(e.preventDefault(),$(e.target).ajaxSubmit(t))}function t(e){var t=e.target,r=$(t);if(!r.is("[type=submit],[type=image]")){var a=r.closest("[type=submit]");if(0===a.length)return;t=a[0]}var n=this;if(n.clk=t,"image"==t.type)if(void 0!==e.offsetX)n.clk_x=e.offsetX,n.clk_y=e.offsetY;else if("function"==typeof $.fn.offset){var i=r.offset();n.clk_x=e.pageX-i.left,n.clk_y=e.pageY-i.top}else n.clk_x=e.pageX-t.offsetLeft,n.clk_y=e.pageY-t.offsetTop;setTimeout(function(){n.clk=n.clk_x=n.clk_y=null},100)}function r(){if($.fn.ajaxSubmit.debug){var e="[jquery.form] "+Array.prototype.join.call(arguments,"");window.console&&window.console.log?window.console.log(e):window.opera&&window.opera.postError&&window.opera.postError(e)}}var a={};a.fileapi=void 0!==$("<input type='file'/>").get(0).files,a.formdata=void 0!==window.FormData;var n=!!$.fn.prop;$.fn.attr2=function(){if(!n)return this.attr.apply(this,arguments);var e=this.prop.apply(this,arguments);return e&&e.jquery||"string"==typeof e?e:this.attr.apply(this,arguments)},$.fn.ajaxSubmit=function(e){function t(t){var r,a,n=$.param(t,e.traditional).split("&"),i=n.length,o=[];for(r=0;r<i;r++)n[r]=n[r].replace(/\+/g," "),a=n[r].split("="),o.push([decodeURIComponent(a[0]),decodeURIComponent(a[1])]);return o}function i(t){function a(e){var t=null;try{e.contentWindow&&(t=e.contentWindow.document)}catch(e){r("cannot get iframe.contentWindow document: "+e)}if(t)return t;try{t=e.contentDocument?e.contentDocument:e.document}catch(a){r("cannot get iframe.contentDocument: "+a),t=e.document}return t}function i(){function e(){try{var t=a(v).readyState;r("state = "+t),t&&"uninitialized"==t.toLowerCase()&&setTimeout(e,50)}catch(e){r("Server abort: ",e," (",e.name,")"),s(D),T&&clearTimeout(T),T=void 0}}var t=c.attr2("target"),n=c.attr2("action"),i=c.attr("enctype")||c.attr("encoding")||"multipart/form-data";j.setAttribute("target",p),o&&!/post/i.test(o)||j.setAttribute("method","POST"),n!=f.url&&j.setAttribute("action",f.url),f.skipEncodingOverride||o&&!/post/i.test(o)||c.attr({encoding:"multipart/form-data",enctype:"multipart/form-data"}),f.timeout&&(T=setTimeout(function(){y=!0,s(S)},f.timeout));var u=[];try{if(f.extraData)for(var l in f.extraData)f.extraData.hasOwnProperty(l)&&($.isPlainObject(f.extraData[l])&&f.extraData[l].hasOwnProperty("name")&&f.extraData[l].hasOwnProperty("value")?u.push($('<input type="hidden" name="'+f.extraData[l].name+'">').val(f.extraData[l].value).appendTo(j)[0]):u.push($('<input type="hidden" name="'+l+'">').val(f.extraData[l]).appendTo(j)[0]));f.iframeTarget||h.appendTo("body"),v.attachEvent?v.attachEvent("onload",s):v.addEventListener("load",s,!1),setTimeout(e,15);try{j.submit()}catch(e){var m=document.createElement("form").submit;m.apply(j)}}finally{j.setAttribute("action",n),j.setAttribute("enctype",i),t?j.setAttribute("target",t):c.removeAttr("target"),$(u).remove()}}function s(e){if(!g.aborted&&!M){if(E=a(v),E||(r("cannot access response document"),e=D),e===S&&g)return g.abort("timeout"),void w.reject(g,"timeout");if(e==D&&g)return g.abort("server abort"),void w.reject(g,"error","server abort");if(E&&E.location.href!=f.iframeSrc||y){v.detachEvent?v.detachEvent("onload",s):v.removeEventListener("load",s,!1);var t,n="success";try{if(y)throw"timeout";var i="xml"==f.dataType||E.XMLDocument||$.isXMLDoc(E);if(r("isXml="+i),!i&&window.opera&&(null===E.body||!E.body.innerHTML)&&--F)return r("requeing onLoad callback, DOM not available"),void setTimeout(s,250);var o=E.body?E.body:E.documentElement;g.responseText=o?o.innerHTML:null,g.responseXML=E.XMLDocument?E.XMLDocument:E,i&&(f.dataType="xml"),g.getResponseHeader=function(e){return{"content-type":f.dataType}[e.toLowerCase()]},o&&(g.status=Number(o.getAttribute("status"))||g.status,g.statusText=o.getAttribute("statusText")||g.statusText);var u=(f.dataType||"").toLowerCase(),c=/(json|script|text)/.test(u);if(c||f.textarea){var l=E.getElementsByTagName("textarea")[0];if(l)g.responseText=l.value,g.status=Number(l.getAttribute("status"))||g.status,g.statusText=l.getAttribute("statusText")||g.statusText;else if(c){var d=E.getElementsByTagName("pre")[0],p=E.getElementsByTagName("body")[0];d?g.responseText=d.textContent?d.textContent:d.innerText:p&&(g.responseText=p.textContent?p.textContent:p.innerText)}}else"xml"==u&&!g.responseXML&&g.responseText&&(g.responseXML=O(g.responseText));try{L=C(g,u,f)}catch(e){n="parsererror",g.error=t=e||n}}catch(e){r("error caught: ",e),n="error",g.error=t=e||n}g.aborted&&(r("upload aborted"),n=null),g.status&&(n=g.status>=200&&g.status<300||304===g.status?"success":"error"),"success"===n?(f.success&&f.success.call(f.context,L,"success",g),w.resolve(g.responseText,"success",g),m&&$.event.trigger("ajaxSuccess",[g,f])):n&&(void 0===t&&(t=g.statusText),f.error&&f.error.call(f.context,g,n,t),w.reject(g,"error",t),m&&$.event.trigger("ajaxError",[g,f,t])),m&&$.event.trigger("ajaxComplete",[g,f]),m&&!--$.active&&$.event.trigger("ajaxStop"),f.complete&&f.complete.call(f.context,g,n),M=!0,f.timeout&&clearTimeout(T),setTimeout(function(){f.iframeTarget?h.attr("src",f.iframeSrc):h.remove(),g.responseXML=null},100)}}}var u,l,f,m,p,h,v,g,x,b,y,T,j=c[0],w=$.Deferred();if(w.abort=function(e){g.abort(e)},t)for(l=0;l<d.length;l++)u=$(d[l]),n?u.prop("disabled",!1):u.removeAttr("disabled");if(f=$.extend(!0,{},$.ajaxSettings,e),f.context=f.context||f,p="jqFormIO"+(new Date).getTime(),f.iframeTarget?(h=$(f.iframeTarget),b=h.attr2("name"),b?p=b:h.attr2("name",p)):(h=$('<iframe name="'+p+'" src="'+f.iframeSrc+'" />'),h.css({position:"absolute",top:"-1000px",left:"-1000px"})),v=h[0],g={aborted:0,responseText:null,responseXML:null,status:0,statusText:"n/a",getAllResponseHeaders:function(){},getResponseHeader:function(){},setRequestHeader:function(){},abort:function(e){var t="timeout"===e?"timeout":"aborted";r("aborting upload... "+t),this.aborted=1;try{v.contentWindow.document.execCommand&&v.contentWindow.document.execCommand("Stop")}catch(e){}h.attr("src",f.iframeSrc),g.error=t,f.error&&f.error.call(f.context,g,t,e),m&&$.event.trigger("ajaxError",[g,f,t]),f.complete&&f.complete.call(f.context,g,t)}},m=f.global,m&&0==$.active++&&$.event.trigger("ajaxStart"),m&&$.event.trigger("ajaxSend",[g,f]),f.beforeSend&&!1===f.beforeSend.call(f.context,g,f))return f.global&&$.active--,w.reject(),w;if(g.aborted)return w.reject(),w;(x=j.clk)&&(b=x.name)&&!x.disabled&&(f.extraData=f.extraData||{},f.extraData[b]=x.value,"image"==x.type&&(f.extraData[b+".x"]=j.clk_x,f.extraData[b+".y"]=j.clk_y));var S=1,D=2,k=$("meta[name=csrf-token]").attr("content"),A=$("meta[name=csrf-param]").attr("content");A&&k&&(f.extraData=f.extraData||{},f.extraData[A]=k),f.forceSync?i():setTimeout(i,10);var L,E,M,F=50,O=$.parseXML||function(e,t){return window.ActiveXObject?(t=new ActiveXObject("Microsoft.XMLDOM"),t.async="false",t.loadXML(e)):t=(new DOMParser).parseFromString(e,"text/xml"),t&&t.documentElement&&"parsererror"!=t.documentElement.nodeName?t:null},X=$.parseJSON||function(e){return window.eval("("+e+")")},C=function(e,t,r){var a=e.getResponseHeader("content-type")||"",n="xml"===t||!t&&a.indexOf("xml")>=0,i=n?e.responseXML:e.responseText;return n&&"parsererror"===i.documentElement.nodeName&&$.error&&$.error("parsererror"),r&&r.dataFilter&&(i=r.dataFilter(i,t)),"string"==typeof i&&("json"===t||!t&&a.indexOf("json")>=0?i=X(i):("script"===t||!t&&a.indexOf("javascript")>=0)&&$.globalEval(i)),i};return w}if(!this.length)return r("ajaxSubmit: skipping submit process - no element selected"),this;var o,s,u,c=this;"function"==typeof e?e={success:e}:void 0===e&&(e={}),o=e.type||this.attr2("method"),s=e.url||this.attr2("action"),u="string"==typeof s?$.trim(s):"",u=u||window.location.href||"",u&&(u=(u.match(/^([^#]+)/)||[])[1]),e=$.extend(!0,{url:u,success:$.ajaxSettings.success,type:o||$.ajaxSettings.type,iframeSrc:/^https/i.test(window.location.href||"")?"javascript:false":"about:blank"},e);var l={};if(this.trigger("form-pre-serialize",[this,e,l]),l.veto)return r("ajaxSubmit: submit vetoed via form-pre-serialize trigger"),this;if(e.beforeSerialize&&!1===e.beforeSerialize(this,e))return r("ajaxSubmit: submit aborted via beforeSerialize callback"),this;var f=e.traditional;void 0===f&&(f=$.ajaxSettings.traditional);var m,d=[],p=this.formToArray(e.semantic,d);if(e.data&&(e.extraData=e.data,m=$.param(e.data,f)),e.beforeSubmit&&!1===e.beforeSubmit(p,this,e))return r("ajaxSubmit: submit aborted via beforeSubmit callback"),this;if(this.trigger("form-submit-validate",[p,this,e,l]),l.veto)return r("ajaxSubmit: submit vetoed via form-submit-validate trigger"),this;var h=$.param(p,f);m&&(h=h?h+"&"+m:m),"GET"==e.type.toUpperCase()?(e.url+=(e.url.indexOf("?")>=0?"&":"?")+h,e.data=null):e.data=h;var v=[];if(e.resetForm&&v.push(function(){c.resetForm()}),e.clearForm&&v.push(function(){c.clearForm(e.includeHidden)}),!e.dataType&&e.target){var g=e.success||function(){};v.push(function(t){var r=e.replaceTarget?"replaceWith":"html";$(e.target)[r](t).each(g,arguments)})}else e.success&&v.push(e.success);if(e.success=function(t,r,a){for(var n=e.context||this,i=0,o=v.length;i<o;i++)v[i].apply(n,[t,r,a||c,c])},e.error){var x=e.error;e.error=function(t,r,a){var n=e.context||this;x.apply(n,[t,r,a,c])}}if(e.complete){var b=e.complete;e.complete=function(t,r){var a=e.context||this;b.apply(a,[t,r,c])}}var y=$("input[type=file]:enabled",this).filter(function(){return""!==$(this).val()}),T=y.length>0,j="multipart/form-data",w=c.attr("enctype")==j||c.attr("encoding")==j,S=a.fileapi&&a.formdata;r("fileAPI :"+S);var D,k=(T||w)&&!S;!1!==e.iframe&&(e.iframe||k)?e.closeKeepAlive?$.get(e.closeKeepAlive,function(){D=i(p)}):D=i(p):D=(T||w)&&S?function(r){for(var a=new FormData,n=0;n<r.length;n++)a.append(r[n].name,r[n].value);if(e.extraData){var i=t(e.extraData);for(n=0;n<i.length;n++)i[n]&&a.append(i[n][0],i[n][1])}e.data=null;var s=$.extend(!0,{},$.ajaxSettings,e,{contentType:!1,processData:!1,cache:!1,type:o||"POST"});e.uploadProgress&&(s.xhr=function(){var t=$.ajaxSettings.xhr();return t.upload&&t.upload.addEventListener("progress",function(t){var r=0,a=t.loaded||t.position,n=t.total;t.lengthComputable&&(r=Math.ceil(a/n*100)),e.uploadProgress(t,a,n,r)},!1),t}),s.data=null;var u=s.beforeSend;return s.beforeSend=function(t,r){e.formData?r.data=e.formData:r.data=a,u&&u.call(this,t,r)},$.ajax(s)}(p):$.ajax(e),c.removeData("jqxhr").data("jqxhr",D);for(var A=0;A<d.length;A++)d[A]=null;return this.trigger("form-submit-notify",[this,e]),this},$.fn.ajaxForm=function(a){if(a=a||{},a.delegation=a.delegation&&$.isFunction($.fn.on),!a.delegation&&0===this.length){var n={s:this.selector,c:this.context};return!$.isReady&&n.s?(r("DOM not ready, queuing ajaxForm"),$(function(){$(n.s,n.c).ajaxForm(a)}),this):(r("terminating; zero elements found by selector"+($.isReady?"":" (DOM not ready)")),this)}return a.delegation?($(document).off("submit.form-plugin",this.selector,e).off("click.form-plugin",this.selector,t).on("submit.form-plugin",this.selector,a,e).on("click.form-plugin",this.selector,a,t),this):this.ajaxFormUnbind().bind("submit.form-plugin",a,e).bind("click.form-plugin",a,t)},$.fn.ajaxFormUnbind=function(){return this.unbind("submit.form-plugin click.form-plugin")},$.fn.formToArray=function(e,t){var r=[];if(0===this.length)return r;var n,i=this[0],o=this.attr("id"),s=e?i.getElementsByTagName("*"):i.elements;if(s&&!/MSIE [678]/.test(navigator.userAgent)&&(s=$(s).get()),o&&(n=$(':input[form="'+o+'"]').get(),n.length&&(s=(s||[]).concat(n))),!s||!s.length)return r;var u,c,l,f,m,d,p;for(u=0,d=s.length;u<d;u++)if(m=s[u],(l=m.name)&&!m.disabled)if(e&&i.clk&&"image"==m.type)i.clk==m&&(r.push({name:l,value:$(m).val(),type:m.type}),r.push({name:l+".x",value:i.clk_x},{name:l+".y",value:i.clk_y}));else if((f=$.fieldValue(m,!0))&&f.constructor==Array)for(t&&t.push(m),c=0,p=f.length;c<p;c++)r.push({name:l,value:f[c]});else if(a.fileapi&&"file"==m.type){t&&t.push(m);var h=m.files;if(h.length)for(c=0;c<h.length;c++)r.push({name:l,value:h[c],type:m.type});else r.push({name:l,value:"",type:m.type})}else null!==f&&void 0!==f&&(t&&t.push(m),r.push({name:l,value:f,type:m.type,required:m.required}));if(!e&&i.clk){var v=$(i.clk),g=v[0];l=g.name,l&&!g.disabled&&"image"==g.type&&(r.push({name:l,value:v.val()}),r.push({name:l+".x",value:i.clk_x},{name:l+".y",value:i.clk_y}))}return r},$.fn.formSerialize=function(e){return $.param(this.formToArray(e))},$.fn.fieldSerialize=function(e){var t=[];return this.each(function(){var r=this.name;if(r){var a=$.fieldValue(this,e);if(a&&a.constructor==Array)for(var n=0,i=a.length;n<i;n++)t.push({name:r,value:a[n]});else null!==a&&void 0!==a&&t.push({name:this.name,value:a})}}),$.param(t)},$.fn.fieldValue=function(e){for(var t=[],r=0,a=this.length;r<a;r++){var n=this[r],i=$.fieldValue(n,e);null===i||void 0===i||i.constructor==Array&&!i.length||(i.constructor==Array?$.merge(t,i):t.push(i))}return t},$.fieldValue=function(e,t){var r=e.name,a=e.type,n=e.tagName.toLowerCase();if(void 0===t&&(t=!0),t&&(!r||e.disabled||"reset"==a||"button"==a||("checkbox"==a||"radio"==a)&&!e.checked||("submit"==a||"image"==a)&&e.form&&e.form.clk!=e||"select"==n&&-1==e.selectedIndex))return null;if("select"==n){var i=e.selectedIndex;if(i<0)return null;for(var o=[],s=e.options,u="select-one"==a,c=u?i+1:s.length,l=u?i:0;l<c;l++){var f=s[l];if(f.selected){var m=f.value;if(m||(m=f.attributes&&f.attributes.value&&!f.attributes.value.specified?f.text:f.value),u)return m;o.push(m)}}return o}return $(e).val()},$.fn.clearForm=function(e){return this.each(function(){$("input,select,textarea",this).clearFields(e)})},$.fn.clearFields=$.fn.clearInputs=function(e){var t=/^(?:color|date|datetime|email|month|number|password|range|search|tel|text|time|url|week)$/i;return this.each(function(){var r=this.type,a=this.tagName.toLowerCase();t.test(r)||"textarea"==a?this.value="":"checkbox"==r||"radio"==r?this.checked=!1:"select"==a?this.selectedIndex=-1:"file"==r?/MSIE/.test(navigator.userAgent)?$(this).replaceWith($(this).clone(!0)):$(this).val(""):e&&(!0===e&&/hidden/.test(r)||"string"==typeof e&&$(this).is(e))&&(this.value="")})},$.fn.resetForm=function(){return this.each(function(){("function"==typeof this.reset||"object"==typeof this.reset&&!this.reset.nodeType)&&this.reset()})},$.fn.enable=function(e){return void 0===e&&(e=!0),this.each(function(){this.disabled=!e})},$.fn.selected=function(e){return void 0===e&&(e=!0),this.each(function(){var t=this.type;if("checkbox"==t||"radio"==t)this.checked=e;else if("option"==this.tagName.toLowerCase()){var r=$(this).parent("select");e&&r[0]&&"select-one"==r[0].type&&r.find("option").selected(!1),this.selected=e}})},$.fn.ajaxSubmit.debug=!1})});