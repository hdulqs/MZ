/**
 * Bootstrap Table Chinese translation
 * Author: Zhixin Wen<wenzhixin2010@gmail.com>
 */

define(function(require,exports,module){

require("lib/bootstrap-table/bootstrap-table.js");

(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['zh-CN'] = {
        formatLoadingMessage: function () {
            return jiazaishujuzhong;
        },
        formatRecordsPerPage: function (pageNumber) {
            return meiyexianshi + pageNumber + tiaojilu;
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return xianshidi + pageFrom + daodi + pageTo + tiaojiluzonggong + totalRows + tiaojilu;
        },
        formatSearch: function () {
            return soushuo;
        },
        formatNoMatches: function () {
            return weizhaodaojilu;
        },
        formatPaginationSwitch: function () {
            return yincangxianshi;
        },
        formatRefresh: function () {
            return shauxin;
        },
        formatToggle: function () {
            return qiehuan;
        },
        formatColumns: function () {
            return lie;
        },
        formatExport: function () {
            return daochushuju;
        },
        formatClearFilters: function () {
            return qingkongguolv;
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN']);

})(jQuery);

});
