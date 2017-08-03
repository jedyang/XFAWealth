window.Messenger = function() {
    function a(a, b) {
        var c = "";
        if (arguments.length < 2 ? c = "target error - target and name are both required": "object" != typeof a ? c = "target error - target itself must be window object": "string" != typeof b && (c = "target error - target name must be string type"), c) throw new Error(c);
        this.target = a,
        this.name = b
    }
    function b(a, b) {
        this.targets = {},
        this.name = a,
        this.listenFunc = [],
        c = b || c,
        "string" != typeof c && (c = c.toString()),
        this.initListen()
    }
    var c = "[PROJECT_NAME]",
    d = "postMessage" in window;
    return d ? a.prototype.send = function(a) {
        this.target.postMessage(c + a, "*")
    }: a.prototype.send = function(a) {
        var b = window.navigator[c + this.name];
        if ("function" != typeof b) throw new Error("target callback function is not defined");
        b(c + a, window)
    },
    b.prototype.addTarget = function(b, c) {
        var d = new a(b, c);
        this.targets[c] = d
    },
    b.prototype.initListen = function() {
        var a = this,
        b = function(b) {
            "object" == typeof b && b.data && (b = b.data),
            b = b.slice(c.length);
            for (var d = 0; d < a.listenFunc.length; d++) a.listenFunc[d](b)
        };
        d ? "addEventListener" in document ? window.addEventListener("message", b, !1) : "attachEvent" in document && window.attachEvent("onmessage", b) : window.navigator[c + this.name] = b
    },
    b.prototype.listen = function(a) {
        this.listenFunc.push(a)
    },
    b.prototype.clear = function() {
        this.listenFunc = []
    },
    b.prototype.send = function(a) {
        var b, c = this.targets;
        for (b in c) c.hasOwnProperty(b) && c[b].send(a)
    },
    b
} (),
function(a) {
    Array.prototype.forEach || (Array.prototype.forEach = function(a) {
        if (!a) return ! 1;
        for (var b = 0,
        c = this.length; c > b; b++) a(this[b], b, this)
    });
    var b = 1,
    c = {};
    a.Constants = {
        CODE: {
            1e3: "SUCCESS",
            1001 : "NOT_LOGIN",
            1002 : "TIMEOUT",
            1003 : "OTHER_ERROR",
            1004 : "PARSE_ERROR",
            1005 : "NET_ERROR",
            1006 : "KICK_OFF",
            1007 : "LOGIN_INFO_ERROR",
            1008 : "START_ALREADY",
            1009 : "NO_MESSAGE",
            1010 : "PARAM_ERROR"
        }
    },
    a.on = function(b, d, e, f) {
        if ("function" == typeof d) {
            if (f) {
                var g = b,
                h = d;
                d = function() {
                    a.off(g, d, e),
                    h.apply(this, arguments)
                }
            }
            return b = b.split(/\s+/),
            b.forEach(function(a) {
                var b = c[a] || (c[a] = []);
                b.push({
                    callback: d,
                    context: e || this
                })
            }),
            this
        }
    },
    a.one = function(b, c, d) {
        return a.on(b, c, d, 1)
    },
    a.off = function(a, b) {
        return a || b ? (a = a.split(/\s+/), a.forEach(function(a) {
            var d = c[a];
            if (!d) return this;
            if (!b) return delete c[a],
            this;
            for (var e = 0,
            f = d.length; f > e; e++) if (b === d[e].callback) {
                d.splice(e, 1);
                break
            }
        }), this) : (c = {},
        this)
    },
    a.fire = function(a, b) {
        return arguments.length ? (a = a.split(/\s+/), a.forEach(function(a) {
            var d = c[a];
            return d ? void d.forEach(function(a) {
                a.callback.call(a.context, b)
            }) : this
        }), this) : this
    },
    a.getUUID = function() {
        return b++
    },
    a.checkParam = function(a, b) {
        var c = "param lost: ",
        d = !1;
        if (a ? b.forEach(function(b) {
            "undefined" == typeof a[b] && (c += b + ";", d = !0)
        }) : (c += "config", d = !0), d) throw new Error(c);
        return ! 0
    },
    a.callbackHandler = function(b, c) { (c.success || c.failure || c.error) && a.one(b,
        function(a) {
            a && 1e3 === a.code ? c.success && c.success.call(null, a) : (c.failure && c.failure.call(null, a), c.error && c.error.call(null, a))
        })
    }
} (window.WSDKUtil || (window.WSDKUtil = {})),
function(a) {
    a.Base = function(a) {
        function b(a, b) {
            function k() {
                g && (g.clear(), g = null),
                g = new Messenger(p, o),
                g.addTarget(e.contentWindow, q),
                g.listen(function(a) {
                    d(a)
                }),
                c({
                    action: "SET_LOGIN_INFO",
                    result: {
                        uid: h.LoginInfo.uid,
                        appkey: h.LoginInfo.appkey,
                        toAppkey: h.LoginInfo.toAppkey
                    }
                })
            }
            e && e.parentNode.removeChild(e);
            var l = document.createElement("frame");
            l.style.display = "none",
            e = l,
            l.attachEvent ? l.attachEvent("onload", k) : l.onload = k,
            f && clearTimeout(f),
            f = setTimeout(function() {
                f = null,
                i.fire("LOGIN", {
                    code: 1002,
                    resultText: j[1002]
                })
            },
            b || n),
            e.src = a,
            document.body.appendChild(l)
        }
        function c(a, b) {
            b && !r ? i.fire(a.action, {
                code: 1001,
                resultText: j[1001]
            }) : g && g.targets[q].send(JSON.stringify(a))
        }
        function d(a) {
            a = JSON.parse(a),
            i.fire(a.action, a.result),
            a.result && 1e3 == a.result.code && ("CHAT_START_RECEIVE_MSG" === a.action ? (i.fire("MSG_RECEIVED", a.result), 16908304 == a.result.data.cmdid && i.fire("CHAT.MSG_RECEIVED", a.result)) : "START_RECEIVE_ALL_MSG" === a.action && (i.fire("MSG_RECEIVED", a.result), 16908304 == a.result.data.cmdid ? i.fire("CHAT.MSG_RECEIVED", a.result) : 16908545 == a.result.data.cmdid && i.fire("TRIBE.MSG_RECEIVED", a.result)))
        }
        var e, f, g, h = this,
        i = window.WSDKUtil,
        j = i.Constants.CODE,
        k = "https:",
        l = "taobao.com",
        m = "chat.im",
        n = 5e3,
        o = "WSDK",
        p = "CUSTOM_PAGE",
        q = "PROXY_PAGE",
        r = !1,
        s = !1;
        return a && a.debug && ("daily" == a.env ? (l = "daily.taobao.net", k = "http:") : "pre" == a.env && (m = "chatpre.im")),
        i.on("SET_LOGIN_INFO",
        function(a) {
            f && (clearTimeout(f), r = !0, h.LoginInfo.prefix = a.data.prefix, h.LoginInfo.toPrefix = a.data.toPrefix, i.fire("LOGIN", {
                code: 1e3,
                resultText: j[1e3]
            }))
        }),
        {
            messengerSender: c,
            login: function(a) {
                i.checkParam(a, ["uid", "appkey", "credential"]);
                var c = h.LoginInfo.uid = a.uid.toLowerCase(),
                d = h.LoginInfo.appkey = a.appkey,
                e = h.LoginInfo.toAppkey = a.toAppkey || a.appkey,
                f = h.LoginInfo.credential = a.credential,
                g = "undefined" != typeof a.tokenFlag ? a.tokenFlag: 64;
                h.LoginInfo.timeout = a.timeout;
                var j = k + "//" + m + "." + l + "/login/oauth?cross=true&tokenFlag=" + g + "&uid=" + c + "&credential=" + f + "&appkey=" + d + "&toAppkey=" + e + "&ver=" + WSDK.version;
                return i.callbackHandler("LOGIN", a),
                b(j, a.timeout),
                h
            },
            getUnreadMsgCount: function(a) {
                return i.callbackHandler("GET_UNREAD_MSG_COUNT", a),
                c({
                    action: "GET_UNREAD_MSG_COUNT",
                    result: {
                        count: a && a.count || 30
                    }
                },
                !0),
                h
            },
            getRecentContact: function(a) {
                return i.callbackHandler("GET_RECENT_CONTACT", a),
                c({
                    action: "GET_RECENT_CONTACT",
                    result: {
                        count: a && a.count || 30
                    }
                },
                !0),
                h
            },
            startListenAllMsg: function() {
                if (!r) throw new Error("未登录");
                return s || (s = !0, c({
                    action: "START_RECEIVE_ALL_MSG"
                },
                !0), i.one("STOP_RECEIVE_ALL_MSG",
                function() {
                    s = !1
                })),
                h
            },
            stopListenAllMsg: function() {
                return s && (s = !1, c({
                    action: "STOP_RECEIVE_ALL_MSG"
                })),
                h
            },
            getNick: function(a) {
                return a && a.length > 8 && (0 == a.indexOf(h.LoginInfo.prefix) || 0 == a.indexOf(h.LoginInfo.toPrefix)) ? a.substring(8) : a
            },
            unlogin: function() {
                e && (e.parentNode && e.parentNode.removeChild(e), e = null),
                g && (g.clear(), g = null),
                this.stopListenAllMsg(),
                h.Chat.stopListenMsg()
            }
        }
    }
} (window.WSDKMods || (window.WSDKMods = {})),
function(a) {
    a.Event = function() {
        var a = window.WSDKUtil;
        return {
            on: function(b, c, d) {
                return a.on(b, c, d),
                this
            },
            off: function(b, c) {
                return a.off(b, c),
                this
            },
            fire: function(b, c) {
                return a.fire(b, c),
                this
            },
            one: function(b, c, d) {
                return a.one(b, c, d),
                this
            }
        }
    }
} (window.WSDKMods || (window.WSDKMods = {})),
function(a) {
    a.Chat = function() {
        var a = this,
        b = !1,
        c = window.WSDKUtil,
        d = this.Base;
        return {
            sendMsg: function(b) {
                return c.checkParam(b, ["touid", "msg"]),
                c.callbackHandler("CHAT_SEND_MSG", b),
                d.messengerSender({
                    action: "CHAT_SEND_MSG",
                    result: {
                        touid: b.touid,
                        msg: b.msg,
                        msgType: b.msgType || 0,
                        hasPrefix: !!b.hasPrefix
                    }
                },
                !0),
                a
            },
            sendCustomMsg: function(b) {
                return c.checkParam(b, ["touid", "msg"]),
                c.callbackHandler("CHAT_SEND_CUSTOM_MSG", b),
                d.messengerSender({
                    action: "CHAT_SEND_CUSTOM_MSG",
                    result: {
                        touid: b.touid,
                        msg: b.msg,
                        summary: b.summary,
                        hasPrefix: !!b.hasPrefix
                    }
                },
                !0),
                a
            },
            sendMsgToCustomService: function(b) {
                return c.checkParam(b, ["touid", "msg"]),
                c.callbackHandler("CHAT_SEND_MSG_TO_CUSTOM_SERVICE", b),
                d.messengerSender({
                    action: "CHAT_SEND_MSG_TO_CUSTOM_SERVICE",
                    result: {
                        touid: b.touid,
                        msg: b.msg,
                        msgType: b.msgType,
                        nocache: b.nocache,
                        groupid: b.groupid,
                        hasPrefix: !!b.hasPrefix
                    }
                },
                !0),
                a
            },
            getHistory: function(b) {
                return c.checkParam(b, ["touid"]),
                c.callbackHandler("GET_HISTORY_MSG", b),
                d.messengerSender({
                    action: "GET_HISTORY_MSG",
                    result: {
                        touid: b.touid,
                        count: b.count || 20,
                        nextkey: b.nextkey || "",
                        hasPrefix: !!b.hasPrefix,
                        type: 1
                    }
                },
                !0),
                a
            },
            setReadState: function(b) {
                return c.checkParam(b, ["touid"]),
                c.callbackHandler("CHAT_SET_READ_STATE", b),
                d.messengerSender({
                    action: "CHAT_SET_READ_STATE",
                    result: {
                        touid: b.touid,
                        timestamp: b.timestamp || parseInt( + new Date / 1e3),
                        hasPrefix: !!b.hasPrefix
                    }
                },
                !0),
                a
            },
            startListenMsg: function(e) {
                return c.checkParam(e, ["touid"]),
                b || (b = !0, d.messengerSender({
                    action: "CHAT_START_RECEIVE_MSG",
                    result: {
                        touid: e.touid,
                        hasPrefix: !!e.hasPrefix
                    }
                },
                !0), c.one("CHAT_STOP_RECEIVE_MSG",
                function() {
                    b = !1
                })),
                a
            },
            stopListenMsg: function() {
                return b && (b = !1, d.messengerSender({
                    action: "CHAT_STOP_RECEIVE_MSG"
                })),
                this
            }
        }
    }
} (window.WSDKMods || (window.WSDKMods = {})),
function(a) {
    a.Tribe = function() {
        var a = this,
        b = window.WSDKUtil,
        c = this.Base;
        return {
            getHistory: function(d) {
                return b.checkParam(d, ["tid"]),
                b.callbackHandler("TRIBE_GET_HISTORY_MSG", d),
                c.messengerSender({
                    action: "TRIBE_GET_HISTORY_MSG",
                    result: {
                        tid: d.tid,
                        count: d.count || 20,
                        nextkey: d.nextkey || "",
                        type: 2
                    }
                },
                !0),
                a
            },
            getTribeInfo: function(d) {
                return b.checkParam(d, ["tid"]),
                b.callbackHandler("TRIBE_GET_INTO", d),
                c.messengerSender({
                    action: "TRIBE_GET_INTO",
                    result: {
                        tid: parseInt(d.tid),
                        excludeFlag: d.excludeFlag || 0
                    }
                },
                !0),
                a
            },
            getTribeList: function(d) {
                return b.callbackHandler("TRIBE_GET_LIST", d),
                c.messengerSender({
                    action: "TRIBE_GET_LIST",
                    result: {
                        tribeTypes: d.tribeTypes
                    }
                },
                !0),
                a
            },
            getTribeMembers: function(d) {
                return b.checkParam(d, ["tid"]),
                b.callbackHandler("TRIBE_GET_MEMBERS", d),
                c.messengerSender({
                    action: "TRIBE_GET_MEMBERS",
                    result: {
                        tid: parseInt(d.tid)
                    }
                },
                !0),
                a
            },
            sendMsg: function(d) {
                b.checkParam(d, ["tid", "msg"]),
                b.callbackHandler("TRIBE_SEND_MSG", d);
                var e = +new Date;
                return c.messengerSender({
                    action: "TRIBE_SEND_MSG",
                    result: {
                        tid: parseInt(d.tid),
                        msgTime: d.msgTime || parseInt( + new Date / 1e3),
                        uuid: e,
                        msgType: d.msgType || 0,
                        msg: d.msg
                    }
                },
                !0),
                a
            },
            responseInviteIntoTribe: function(d) {
                return b.checkParam(d, ["tid", "validatecode", "manager", "recommender"]),
                b.callbackHandler("TRIBE_RESPONSE_INVITE", d),
                c.messengerSender({
                    action: "TRIBE_RESPONSE_INVITE",
                    result: {
                        tid: parseInt(d.tid),
                        recommender: d.recommender,
                        validatecode: d.validatecode,
                        manager: d.manager
                    }
                },
                !0),
                a
            }
        }
    }
} (window.WSDKMods || (window.WSDKMods = {})),
function(a) {
    a.Plugin || (a.Plugin = {});
    var b = function() {},
    c = 3145728,
    d = 3e4;
    a.Plugin.Image = function() {
        var a = window.WSDKUtil,
        e = this.Base;
        return {
            upload: function(f) {
                if (f.success || (f.success = b), f.error || (f.error = b), !(f.target || f.base64Img && f.ext)) return f.error({
                    code: -1,
                    errorText: "PARAM ERROR"
                }),
                !1;
                if ((!f.maxSize || f.maxSize > c) && (f.maxSize = c), this.opts = f, f.base64Img) return f.base64Img.length > f.maxSize ? (f.error({
                    code: -4,
                    errorText: "SIZE LIMIT"
                }), !1) : (a.callbackHandler("UPLOAD_IMAGE", f), e.messengerSender({
                    action: "UPLOAD_IMAGE",
                    result: {
                        imageBase64: f.base64Img,
                        ext: f.ext,
                        timeout: this.opts.timeout || d
                    }
                },
                !0), !0);
                var g = this,
                h = f.target.value,
                i = f.target.files;
                if (!h) return f.error({
                    code: -2,
                    errorText: "EMPTY FILE"
                }),
                !1;
                if (!i || !i[0]) return f.error({
                    code: -3,
                    errorText: "NOT SUPPORT"
                }),
                !1;
                var j = i[0],
                k = j.size,
                l = h.split("."),
                m = l[l.length - 1];
                return k > f.maxSize ? (f.error({
                    code: -4,
                    errorText: "SIZE LIMIT"
                }), !1) : void this.getImageWH(j,
                function(b) {
                    var c = g.getImageBase64(b, m);
                    a.callbackHandler("UPLOAD_IMAGE", f),
                    e.messengerSender({
                        action: "UPLOAD_IMAGE",
                        result: {
                            imageBase64: c,
                            ext: m,
                            timeout: g.opts.timeout || d
                        }
                    },
                    !0)
                },
                function() {
                    f.error({
                        code: -5,
                        errorText: "CAN NOT LOAD IMAGE"
                    })
                })
            },
            isSupport: function() {
                return !! document.createElement("canvas").getContext
            } (),
            getImageWH: function(a, b, c) {
                var d = this;
                try {
                    var e = window.URL || window.webkitURL,
                    f = e.createObjectURL(a),
                    g = new Image;
                    g.onload = function() {
                        b && b(this),
                        g.onload = g.onerror = null
                    },
                    g.onerror = function() {
                        c && c(),
                        g.onload = g.onerror = null
                    },
                    g.src = f
                } catch(h) {
                    return d.opts.error({
                        code: -3,
                        errorText: "NOT SUPPORT"
                    }),
                    !1
                }
            },
            getType: function(a) {
                return a = a.toLowerCase(),
                "png" == a ? "image/png": "image/jpeg"
            },
            getImageBase64: function(a, b) {
                var c = this,
                d = "";
                try {
                    var e = document.createElement("canvas"),
                    f = e.getContext("2d");
                    e.width = a.width,
                    e.height = a.height,
                    f.drawImage(a, 0, 0, a.width, a.height),
                    d = e.toDataURL(this.getType(b), 1)
                } catch(g) {
                    c.opts.error({
                        code: -3,
                        errorText: "NOT SUPPORT"
                    })
                }
                return d
            }
        }
    }
} (window.WSDKMods || (window.WSDKMods = {})),
function(a, b) {
    "use strict";
    "undefined" != typeof module && module.exports ? module.exports = b() : "function" == typeof define && (define.amd || define.cmd) ? define(b) : a.WSDK = b()
} (this,
function() {
    function a(b) {
        return this instanceof a ? this.init(b) : new a
    }
    var b = window.WSDKMods;
    return a.version = "0.1.4",
    a.prototype = {
        constructor: a,
        init: function(a) {
            return this._inited ? this: (this._inited = !0, this.Base = b.Base.call(this, a), this.Event = b.Event.call(this), this.Chat = b.Chat.call(this), this.Tribe = b.Tribe.call(this), this.Plugin = {
                Image: b.Plugin.Image.call(this)
            },
            this)
        },
        LoginInfo: {}
    },
    a
});