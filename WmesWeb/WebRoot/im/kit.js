!
function(a) {
    Array.prototype.forEach || (Array.prototype.forEach = function(a) {
        if (!a) return ! 1;
        for (var b = 0,
        c = this.length; c > b; b++) a(this[b], b, this)
    }),
    a.util = {
        unparam: function(a) {
            var b = {};
            if (a) {
                var c, d = a.split("&");
                d.forEach(function(a) {
                    c = a.split("="),
                    b[c[0]] = decodeURIComponent(c[1])
                })
            }
            return b
        },
        getMyScriptSrc: function(a) {
            for (var b = document.getElementsByTagName("script"), c = b.length, d = 0, e = ""; c > d && (e = b[d].src, !(e && e.indexOf(a) > -1)); d++);
            return e
        },
        isMobile: function() {
            for (var a = navigator.userAgent,
            b = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"], c = b.length, d = !1, e = 0; c > e; e++) if (a.toLocaleUpperCase().indexOf(b[e].toUpperCase()) > 0) {
                d = !0;
                break
            }
            return d
        },
        loadScript: function(a, b, c) {
            var d = document.createElement("script"),
            e = document.getElementsByTagName("head")[0];
            d.setAttribute("charset", "utf-8"),
            d.type = "text/javascript",
            d.src = a,
            window.addEventListener ? d.addEventListener("load",
            function() {
                b && b.call(c)
            },
            !1) : d.onreadystatechange = function() { ("loaded" == this.readyState || "complete" == this.readyState) && (d.onreadystatechange = null, b && b.call(c))
            },
            e.insertBefore(d, e.firstChild)
        },
        merge: function() {
            for (var a, b = {},
            c = 0,
            d = arguments.length; d > c; c++) for (a in arguments[c]) if (arguments[c].hasOwnProperty(a)) {
                var e = arguments[c][a];
                "" !== e && null !== e && "undefined" != typeof e && (b[a] = e)
            }
            return b
        }
    }
} (window.WKIT || (window.WKIT = {})),
function(a) {
    var b = {
        debug: window.__DEBUG || !1,
        version: "0.0.5",
        cdnHost: "https://" + (window.__DEBUG ? "g-assets.daily.taobao.net": "g.alicdn.com"),
        cdnPath: "/aliww/h5.openim.kit/"
    },
    c = {
        uid: "",
        touid: "",
        appkey: "",
        credential: "",
        container: null,
        width: 0,
        height: 0,
        autoMsg: "",
        autoMsgType: 0,
        titleBar: !0,
        title: "在线客服",
        onBack: null,
        placeholder: "说点什么吧...",
        logo: "",
        avatar: "https://img.alicdn.com/tps/TB1BsZ6JpXXXXaZXpXXXXXXXXXX-81-81.png",
        toAvatar: "https://img.alicdn.com/tps/TB1FSo0JpXXXXXBXFXXXXXXXXXX-111-111.png",
        theme: "",
        themeColor: "",
        themeBgColor: "",
        msgColor: "",
        msgBgColor: "",
        pluginUrl: "",
        customUrl: "",
        sendMsgToCustomService: !1,
        groupId: "",
        onLoginSuccess: null,
        onLoginError: null,
        imgUploader: !0,
        beforeImageUploaderTrigger: null,
        onUploaderSuccess: null,
        onUploaderError: null,
        onMsgReceived: function() {},
        onMsgSent: function() {}
    },
    d = a.util,
    e = [],
    f = !1,
    g = !1,
    h = function() {
        g = !0,
        f && (e.forEach(function(a) {
            a()
        }), e = [])
    };
    a.version = b.version,
    a.init = function(b) {
        if (f = !0, !(b && b.uid && b.touid && b.appkey && b.credential)) throw new Error('传入的参数必须包含以下字段{uid: "", appkey: "", credential: "", touid: ""}');
        var e = d.merge(c, b);
        this.waitUntilBoot(function() {
            a.start(e)
        })
    },
    a.resize = function(b) {
        if (a.UI) {
            var c = a.UI.Window.resize(b);
            a.UI.Plugin && a.UI.Plugin.resize({
                iWidth: c.wh.left[0],
                iHeight: c.wh.left[1]
            }),
            a.UI.Chat && a.UI.Chat.resize({
                iWidth: c.wh.middle[0],
                iHeight: c.wh.middle[1]
            }),
            a.UI.Custom && a.UI.Custom.resize({
                iWidth: c.wh.right[0],
                iHeight: c.wh.right[1]
            })
        }
    },
    a.destroy = function() {
        a.Plugin && a.Plugin.destroy(),
        a.Chat && a.Chat.destroy(),
        a.UI.Custom && a.UI.Custom.destroy(),
        a.UI.Window && a.UI.Window.destroy()
    },
    a.waitUntilBoot = function(a) {
        a && (g ? a.call(this) : e.push(a))
    },
    function() {
        var a = d.getMyScriptSrc(b.cdnPath),
        c = d.unparam(a.split("?")[1]),
        e = b.cdnHost + b.cdnPath + b.version + "/scripts/",
        f = b.debug ? ".debug": "";
        c.mobile || d.isMobile() ? d.loadScript(e + "mkit" + f + ".js", h) : d.loadScript(e + "pckit" + f + ".js", h)
    } ()
} (window.WKIT || (window.WKIT = {}));