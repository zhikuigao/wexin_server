!function(e, t) {
    function i() {
        o = 1,
        e.devicePixelRatioValue = o,
        s = 1 / o;
        var t = a.createElement("meta");
        if (t.setAttribute("name", "viewport"), t.setAttribute("content", "initial-scale=" + s + ", maximum-scale=" + s + ", minimum-scale=" + s + ", user-scalable=no"), d.firstElementChild) d.firstElementChild.appendChild(t);
        else {
            var i = a.createElement("div");
            i.appendChild(t),
            a.write(i.innerHTML)
        }
    }
    function n() {
        var e = Math.min(d.getBoundingClientRect().width, 540);
        r = 100 * e / t.desinWidth,
        d.style.fontSize = r + "px"
    }
    var a = e.document,
    d = a.documentElement,
    o = (e.devicePixelRatio, 1),
    s = 1;
    i();
    var l, r = 100;
    t.desinWidth = 640,
    t.baseFont = 18,
    t.init = function() {
        e.addEventListener("resize",
        function() {
            clearTimeout(l),
            l = setTimeout(n, 300)
        },
        !1),
        e.addEventListener("pageshow",
        function(e) {
            e.persisted && (clearTimeout(l), l = setTimeout(n, 300))
        },
        !1),
        "complete" === a.readyState ? a.body.style.fontSize = t.baseFont * o + "px": a.addEventListener("DOMContentLoaded",
        function() {
            //a.body.style.fontSize = t.baseFont * o + "px"
        },
        !1),
        n(),
        d.setAttribute("data-dpr", o)
    }
} (window, window.adaptive || (window.adaptive = {}));
/*立即调用*/
window['adaptive'].desinWidth = 750;// 设计图宽度
//window['adaptive'].baseFont = ".28rem";// 默认body字体大小,在这里我们不要设置固定默认字体大小，故将对应代码注释，通过body设置 .28rem设置字体大小
window['adaptive'].init();// 调用初始化方法
