define(function () {
    'use strict';
    /**
     * 操作cookie
     * @type {Object}
     */
    var Cookies = {
        /**
         * 获取cookie
         * @param  {string}   name          想要获取的cookie的名称
         * @param  {boolean}  code = false  是否需要对cookie值解码
         * @return {string}                 cookie值
         */
        get: function (name, code) {
            var result = '',
                reg = new RegExp('(^|; )' + name + '=([^;]+)(;|$)'),
                text = document.cookie,
                match = [];

            match = text.match(reg);

            if (match) {
                result = code ? decodeURIComponent(match[2]) : match[2];
            }

            return result;
        },
        /**
         * 添加cookie
         * @param {string}  name     添加cookie的名称
         * @param {string}  value    cookie值
         * @param {object}  options  配置cookie的过期时间、域、路径、安全特性
         *  - expires = 1     {number}   
         *  - domain          {string}
         *  - path            {string}
         *  - secure = false  {boolean}
         */
        set: function (name, value, options) {
            var text = name + '=' + value,
                date = new Date();

            options = options || {};

            if (typeof(options.expires) === 'undefined') {
                options.expires = 1;
            }
            date.setDate(date.getDate + options.expires);
            text += '; expires=' + date.toUTCString();

            if (options.domain) {
                text += '; domain=' + options.domain;
            }

            if (options.path) {
                text += '; path=' + options.path;
            }

            if (options.secure) {
                text += '; secure';
            }


            document.cookie = text;
            return text;
        },

        /**
         * 移除cookie
         * @param  {string}  name  将要移除cookie的名称
         * @return {string}
         */
        remove: function (name) {
            return this.set(name, '', {expires: -1});
        }
    };


    return Cookies;
});