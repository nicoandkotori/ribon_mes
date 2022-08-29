﻿//按钮权限设置
function initBtn(ele, baseUrl) {
    var arr = new Array();
    $(ele).each(function () {
        $(this).css("display", "none");
        var onclickAttr = $(this).attr("onclick");
        var methodName = null;
        if (onclickAttr == null) {
            methodName = $(this).attr("lay-filter");
        } else {
            methodName = onclickAttr.substr(0, onclickAttr.indexOf("("));
        }
        arr.push({methodName: methodName, permissionUrl: baseUrl + ":" + methodName});
    });
    sendPostReqWithAlert("/sys/permission/getbymenuid",
        {query: JSON.stringify(arr), menuId: GetQueryString("id")}, function (data) {
            $.each(data.result, function (i, item) {
                $(ele).each(function () {
                    var onclickAttr = $(this).attr("onclick");
                    var methodName = null;
                    if (onclickAttr == null) {
                        methodName = $(this).attr("lay-filter");
                    } else {
                        methodName = onclickAttr.substr(0, onclickAttr.indexOf("("));
                    }
                    if (methodName == item.methodName) {
                        if (item.hasAuthority) {
                            $(this).css("display", "inline");
                        } else {
                            $(this).css("display", "none");
                        }
                        return false;
                    }
                });
            });
        });
}

//设置登录信息
$.ajaxSetup({
    //发送请求前触发
    beforeSend: function (xhr) {
        //可以设置自定义标头
        var token = sessionStorage.getItem("mestoken");
        if (!token) {
            alert("请先登录！");
            setInterval(function () {
                window.top.location.href = "./login.html";

            }, 1500);
            return;
        }
        xhr.setRequestHeader('Authorization', token);
    },
    complete: function (xhr) {
        if (xhr.code == '401') {
            alert("操作失败，原因：请先登录！");
            setInterval(function () {
                window.location.href = "/login.html";
            }, 1500);


        }
    }
})


//自定义清除图标
function clearVal(id, id2) {
    if (id != null) {
        $("#" + id).val("");
    }
    if (id2 != null) {
        $("#" + id2).val("");
    }
}

//清空select元素内容后，顺带清空其他附加值
function clearSelect(e, id, id2) {
    if ($(e).val() == "" || $(e).val() == null) {
        if (id != null) {
            $("#" + id).val("");
        }
        if (id2 != null) {
            $("#" + id2).val("");
        }
    }
}

//布尔类型格式化
function formatBool(cellval, colpos, rwdat, _act) {
    if (cellval != null && cellval == true) {
        return "是";
    }
    return "否";
}
//布尔类型格式化
function formatBool1(cellval, colpos, rwdat, _act) {
    if (cellval != null && cellval == 1) {
        return "是";
    }
    return "否";
}
/*
 参数解释：
 title	标题
 url		请求的url
 id		需要操作的数据id
 w		弹出层宽度（缺省调默认值）
 h		弹出层高度（缺省调默认值）
 */
function layer_open(title, url, w, h, endFunc, hasBtn, yesFunc) {
    if (title == null || title == '') {
        title = false;
    }
    if (w == null || w == '') {
        w = 800;
    }
    if (h == null || h == '') {
        h = $(window).height() - 50;
    }
    var option = {
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false, //不固定
        maxmin: true,
        shade: 0.4,
        title: title,
        content: url,
        end: function () {
            if ($.isFunction(endFunc)) {
                endFunc();
            }
        }
    };
    if (hasBtn) {
        option.btn = ["确定", "关闭"];
        option.yes = yesFunc;
        option.close = function (index) {
            layer.close(index);
        }
    }
    layui.use("layer", function () {
        var layer = layui.layer;
        layer.open(option);
    });
}

//字符串转为数字
function parseStrToNum(str) {
    if (str == null || str == '' || isNaN(str)) {
        return 0.00;
    }
    return parseFloat(str);
}

//字符串转为整数
function parseStrToInt(str) {
    if (str == null || str == '' || isNaN(str)) {
        return 0;
    }
    return parseInt(str);
}

//获取URL地址中的参数
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    //如果地址栏中出现中文则进行编码
    var r = encodeURI(window.location.search).substr(1).match(reg);
    if (r != null) {
        //将中文编码的字符重新变成中文
        return decodeURI(unescape(r[2]));
    }
    return null;
}

//post请求
function sendPostReqWithAlert(url, param, successFunc, asyncFlag, failFunc) {
    var loadIndex = '';
    //ajax同步提交时遮罩层不显示。
    //原因：浏览器引擎分为js引擎和渲染（UI）引擎。
    // js引擎负责解析js脚本代码，渲染引擎负责渲染页面。当ajax设置为同步时，由于js线程和UI线程是互斥的，
    // 当执行ajax的时候，js线程耗时操作，此时会阻塞UI线程，当ajax执行完成的时候才会显示遮罩层。
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "post",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        success: function (data) {
            if (data.success == null || data.success == true) {
                if ($.isFunction(successFunc)) {
                    successFunc(data);
                }
            } else {
                var msg = "";
                if(data.msg == null && data.errorMsg == null){
                        msg = "操作失败！"
                }
                if(data.msg !=null){
                    msg = data.msg
                }
                if(data.msg == null && data.errorMsg !=null){
                    msg = data.errorMsg
                }
                alert(msg, {
                    time: 0,
                    btn: ['关闭']
                });

                if ($.isFunction(failFunc)) {
                    failFunc(data);
                }
            }
        },
        error: function (XMLHttpRequest) {
            alert('数据处理失败! 错误码:' + XMLHttpRequest.status);
        }
    });
}

//post请求
function sendPostReq(url, param, successFunc, asyncFlag, failFunc) {
    var loadIndex = '';
    //ajax同步提交时遮罩层不显示。
    //原因：浏览器引擎分为js引擎和渲染（UI）引擎。
    // js引擎负责解析js脚本代码，渲染引擎负责渲染页面。当ajax设置为同步时，由于js线程和UI线程是互斥的，
    // 当执行ajax的时候，js线程耗时操作，此时会阻塞UI线程，当ajax执行完成的时候才会显示遮罩层。
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "post",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        beforeSend: function (xhr) {
            loadIndex = layer.load(1, {
                shade: [0.3, '#fff']
            });
            var token = sessionStorage.getItem("mestoken");
            if (!token) {
                alert("请先登录！");
                setInterval(function () {
                    window.top.location.href = "./login.html";

                }, 1500);
                return;
            }
            xhr.setRequestHeader('Authorization', token);
        },
        complete: function () {
            layer.close(loadIndex);
        },
        success: function (data) {
            //layer.close(loadIndex);
            if (data.success == null || data.success == true || data.code == 200) {
                if ($.isFunction(successFunc)) {
                    successFunc(data);
                }
            } else {
                var msg = "";
                if(data.msg == null && data.errorMsg == null){
                    msg = "操作失败！"
                }
                if(data.msg !=null){
                    msg = data.msg
                }
                if(data.msg == null && data.errorMsg !=null){
                    msg = data.errorMsg
                }
                layer.msg(msg, {
                    time: 0,
                    btn: ['关闭']
                });

                if ($.isFunction(failFunc)) {
                    failFunc(data);
                }
            }
        },
        error: function (XMLHttpRequest) {
            layer.msg('数据处理失败! 错误码:' + XMLHttpRequest.status);
        }
    });
}

//post请求
function sendGetReq(url, param, successFunc, asyncFlag, failFunc) {
    var loadIndex = '';
    //ajax同步提交时遮罩层不显示。
    //原因：浏览器引擎分为js引擎和渲染（UI）引擎。
    // js引擎负责解析js脚本代码，渲染引擎负责渲染页面。当ajax设置为同步时，由于js线程和UI线程是互斥的，
    // 当执行ajax的时候，js线程耗时操作，此时会阻塞UI线程，当ajax执行完成的时候才会显示遮罩层。
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "get",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        beforeSend: function (xhr) {
            loadIndex = layer.load(1, {
                shade: [0.3, '#fff']
            });
            var token = sessionStorage.getItem("mestoken");
            if (!token) {
                alert("请先登录！");
                setInterval(function () {
                    window.top.location.href = "./login.html";

                }, 1500);
                return;
            }
            xhr.setRequestHeader('Authorization', token);
        },
        complete: function () {
            layer.close(loadIndex);
        },
        success: function (data) {
            //layer.close(loadIndex);
            if (data.success == null || data.success == true || data.code == 200) {
                if ($.isFunction(successFunc)) {
                    successFunc(data);
                }
            } else {
                var msg = "";
                if(data.msg == null && data.errorMsg == null){
                    msg = "操作失败！"
                }
                if(data.msg !=null){
                    msg = data.msg
                }
                if(data.msg == null && data.errorMsg !=null){
                    msg = data.errorMsg
                }
                layer.msg(msg, {
                    time: 0,
                    btn: ['关闭']
                });

                if ($.isFunction(failFunc)) {
                    failFunc(data);
                }
            }
        },
        error: function (XMLHttpRequest) {
            layer.msg('数据处理失败! 错误码:' + XMLHttpRequest.status);
        }
    });
}

/**
 * get 请求， 无授权认证
 * @param url
 * @param param
 * @param successFunc
 * @param asyncFlag
 * @param failFunc
 */
function sendGetReqWithoutAuth(url, param, successFunc, asyncFlag, failFunc) {
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "get",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        beforeSend: function (xhr) {
            //layer.load(2);

        },
        success: function (data) {
            if (data.success == null || data.success == tru || data.code == 200) {
                if ($.isFunction(successFunc)) {
                    successFunc(data);
                }
            } else {
                var msg = "";
                if(data.msg == null && data.errorMsg == null){
                    msg = "操作失败！"
                }
                if(data.msg !=null){
                    msg = data.msg
                }
                if(data.msg == null && data.errorMsg !=null){
                    msg = data.errorMsg
                }
                layer.msg(msg, {
                    time: 0,
                    btn: ['关闭']
                });

                if ($.isFunction(failFunc)) {
                    failFunc(data);
                }
            }
            layer.closeAll('loading');
        },
        error: function (XMLHttpRequest) {
            layer.msg('数据处理失败! 错误码:' + XMLHttpRequest.status);
            layer.closeAll('loading');
        }
    });
}

//post请求  不判断是否登录 并且。头文件不携带，
function sendPostReqWithoutAuth(url, param, successFunc, asyncFlag, failFunc) {
    var loadIndex = '';
    //ajax同步提交时遮罩层不显示。
    //原因：浏览器引擎分为js引擎和渲染（UI）引擎。
    // js引擎负责解析js脚本代码，渲染引擎负责渲染页面。当ajax设置为同步时，由于js线程和UI线程是互斥的，
    // 当执行ajax的时候，js线程耗时操作，此时会阻塞UI线程，当ajax执行完成的时候才会显示遮罩层。
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "post",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        beforeSend: function (xhr) {
            loadIndex = layer.load(1, {
                shade: [0.3, '#fff']
            });

        },
        complete: function () {
            layer.close(loadIndex);
        },
        success: function (data) {
            if (data.success == null || data.success == true || data.code == 200) {
                if ($.isFunction(successFunc)) {
                    successFunc(data);
                }
            } else {
                var msg = "";
                if(data.msg == null && data.errorMsg == null){
                    msg = "操作失败！"
                }
                if(data.msg !=null){
                    msg = data.msg
                }
                if(data.msg == null && data.errorMsg !=null){
                    msg = data.errorMsg
                }
                layer.msg(msg, {
                    time: 0,
                    btn: ['关闭']
                });

                if ($.isFunction(failFunc)) {
                    failFunc(data);
                }
            }
            layer.closeAll('loading');
        },
        error: function (XMLHttpRequest) {
            layer.msg('数据处理失败! 错误码:' + XMLHttpRequest.status);
            layer.closeAll('loading');
        }
    });
}

function loading() {
    $('body').loading({
        loadingWidth: 240,
        title: '请稍等!',
        name: 'test',
        // discription: '描述描述描述描述',
        direction: 'column',
        type: 'origin',
        // originBg:'#71EA71',
        originDivWidth: 40,
        originDivHeight: 40,
        originWidth: 6,
        originHeight: 6,
        smallLoading: false,
        loadingMaskBg: 'rgba(0,0,0,0.2)'
    });
}

//限制为整数和两位小数
function onInput(event) {
    var value = event.target.value;
    var len = value.length;
    var reg = /^\d+(\.\d{1,2})?$/;
    if (!reg.test(value)) {
        var d = value.charAt(len - 1);
        var dotIdx = value.indexOf('\.');
        //第一位是小数点时，清空
        if (dotIdx == 0) {
            event.target.value = "";
        }
        //保留第一个小数点
        if (d == '\.') {
            if (dotIdx == len - 1) {
                return;
            }
            event.target.value = value.substring(0, len - 1);
        }
        //截取非数字前的部分
        for (var i = len - 1; i >= 0; i--) {
            if ((value[i] < '0' || value[i] > '9')) {
                event.target.value = value.substring(0, i);
            }
        }
        //截取两位小数
        if (dotIdx != -1) {
            if (len - dotIdx - 1 > 2) {
                var decimal = value.substring(dotIdx + 1, dotIdx + 3);
                event.target.value = value.substring(0, dotIdx + 1) + decimal;
            }
        }
    }
}

//限制为整数
function onIntegerInput(event) {
    var value = event.target.value;
    var len = value.length;
    var reg = /^\d+$/;
    if (!reg.test(value)) {
        event.target.value = value.substring(0, len - 1);
    }
}

function OpenTab(title, url) {
    var id = (new Date()).getTime();
    addTabsByTab({
        id: id,
        title: title,
        close: true,
        url: url
    });
}

function isJsonString(str) {
    try {
        if (typeof str == 'string') {
            if (typeof JSON.parse(str) == "object") {
                return true;
            }
        }
    } catch (e) {
        console.log("convert error in: " + e);
    }
    return false;
}

//显示Loading
(function ($) {
    $.fn.loading = function (options) {
        var $this = $(this);
        var _this = this;
        return this.each(function () {
            var loadingPosition = '';
            var defaultProp = {
                direction: 'column',												//方向，column纵向   row 横向
                animateIn: 'fadeInNoTransform',    								//进入类型
                title: '请稍等...',      										//显示什么内容
                name: 'loadingName', 											//loading的data-name的属性值  用于删除loading需要的参数
                type: 'origin', 			  									//pic   origin
                //discription: '这是一个描述', 										//loading的描述
                titleColor: 'rgba(255,255,255,0.7)',								//title文本颜色
                discColor: 'rgba(255,255,255,0.7)',								//disc文本颜色
                loadingWidth: 260,                									//中间的背景宽度width
                loadingBg: 'rgba(0, 0, 0, 0.6)',  									//中间的背景色
                borderRadius: 12,                 									//中间的背景色的borderRadius
                loadingMaskBg: 'transparent',          								//背景遮罩层颜色
                zIndex: 1000000001,              									//层级

                // 这是圆形旋转的loading样式
                originDivWidth: 60,           											//loadingDiv的width
                originDivHeight: 60,           											//loadingDiv的Height

                originWidth: 8,                  									//小圆点width
                originHeight: 8,                  									//小圆点Height
                originBg: '#fefefe',              								//小圆点背景色
                smallLoading: false,                  								//显示小的loading

                // 这是图片的样式   (pic)
                imgSrc: 'http://www.daiwei.org/index/images/logo/dw.png',    	//默认的图片地址
                imgDivWidth: 80,           											//imgDiv的width
                imgDivHeight: 80,           											//imgDiv的Height

                flexCenter: false, 													//是否用flex布局让loading-div垂直水平居中
                flexDirection: 'row',													//row column  flex的方向   横向 和 纵向
                mustRelative: false, 													//$this是否规定relative
            };


            var opt = $.extend(defaultProp, options || {});

            //根据用户是针对body还是元素  设置对应的定位方式
            if ($this.selector == 'body') {
                loadingPosition = 'fixed';
            } else if (opt.mustRelative) {
                $this.css({
                    position: 'relative',
                });
                loadingPosition = 'absolute';
            } else {
                loadingPosition = 'absolute';
            }

            defaultProp._showOriginLoading = function () {
                var smallLoadingMargin = opt.smallLoading ? 0 : '-10px';
                if (opt.direction == 'row') {
                    smallLoadingMargin = '-6px'
                }

                //悬浮层
                _this.cpt_loading_mask = $('<div class="cpt-loading-mask animated ' + opt.animateIn + ' ' + opt.direction + '" data-name="' + opt.name + '"></div>').css({
                    'background': opt.loadingMaskBg,
                    'z-index': opt.zIndex,
                    'position': loadingPosition,
                }).appendTo($this);

                //中间的显示层
                _this.div_loading = $('<div class="div-loading"></div>').css({
                    'background': opt.loadingBg,
                    'width': opt.loadingWidth,
                    'height': opt.loadingHeight,
                    '-webkit-border-radius': opt.borderRadius,
                    '-moz-border-radius': opt.borderRadius,
                    'border-radius': opt.borderRadius,
                }).appendTo(_this.cpt_loading_mask);

                if (opt.flexCenter) {
                    _this.div_loading.css({
                        "display": "-webkit-flex",
                        "display": "flex",
                        "-webkit-flex-direction": opt.flexDirection,
                        "flex-direction": opt.flexDirection,
                        "-webkit-align-items": "center",
                        "align-items": "center",
                        "-webkit-justify-content": "center",
                        "justify-content": "center",
                    });
                }

                //loading标题
                _this.loading_title = $('<p class="loading-title txt-textOneRow"></p>').css({
                    color: opt.titleColor,
                }).html(opt.title).appendTo(_this.div_loading);

                //loading中间的内容  可以是图片或者转动的小圆球
                _this.loading = $('<div class="loading ' + opt.type + '"></div>').css({
                    'width': opt.originDivWidth,
                    'height': opt.originDivHeight,
                }).appendTo(_this.div_loading);

                //描述
                _this.loading_discription = $('<p class="loading-discription txt-textOneRow"></p>').css({
                    color: opt.discColor,
                }).html(opt.discription).appendTo(_this.div_loading);

                if (opt.type == 'origin') {
                    _this.loadingOrigin = $('<div class="div-loadingOrigin"><span></span></div><div class="div-loadingOrigin"><span></span></div><div class="div_loadingOrigin"><span></span></div><div class="div_loadingOrigin"><span></span></div><div class="div_loadingOrigin"><span></span></div>').appendTo(_this.loading);
                    _this.loadingOrigin.children().css({
                        "margin-top": smallLoadingMargin,
                        "margin-left": smallLoadingMargin,
                        "width": opt.originWidth,
                        "height": opt.originHeight,
                        "background": opt.originBg,
                    });
                }

                if (opt.type == 'pic') {
                    _this.loadingPic = $('<img src="' + opt.imgSrc + '" alt="loading" />').appendTo(_this.loading);
                }


                //关闭事件冒泡  和默认的事件
                _this.cpt_loading_mask.on('touchstart touchend touchmove click', function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                });
            };
            defaultProp._createLoading = function () {
                //不能生成两个loading data-name 一样的loading
                if ($(".cpt-loading-mask[data-name=" + opt.name + "]").length > 0) {
                    // console.error('loading mask cant has same date-name('+opt.name+'), you cant set "date-name" prop when you create it');
                    return
                }

                defaultProp._showOriginLoading();
            };
            defaultProp._createLoading();
        });
    }

})(jQuery);

//关闭Loading
function removeLoading(loadingName) {
    alert("log");
    var loadingName = loadingName || '';

    if (loadingName == '') {
        $(".cpt-loading-mask").remove();
    } else {
        var name = loadingName || 'loadingName';
        $(".cpt-loading-mask[data-name=" + name + "]").remove();
    }
}

/**
 * 验证数据 是数字：返回true；不是数字：返回false
 **/
function izNumber(val) {
    if (parseFloat(val).toString() == "NaN") {
        return false;
    } else {
        return true;
    }
}

/**
 * 附件预览
 * @param url
 * @param fileNameExt
 */
function showFile(url, fileNameExt) {
    if (fileNameExt == "pdf") {
        var cht = $(window).height();
        var cwd = $(window).width();
        layer.open({
            type: 2,
            area: [cwd - 200 + 'px', cht - 100 + 'px'],
            fixed: false, //不固定
            maxmin: true,
            content: url
        });
    } else if ("jpg|png|gif|bmp|jpeg".indexOf(fileNameExt) > -1) {
        layer.photos({
            photos: {
                "title": "", //标题
                "id": "", //id
                "start": 0, //初始显示的文件序号，默认0
                "data": [   //文件夹包含的文件，数组格式
                    {
                        "alt": "",
                        "pid": "", //文件id
                        "src": url, //文件地址
                        "thumb": "" //缩略图地址
                    }
                ]
            }
        });
    } else {
        layer.msg("只允许预览图片和pdf!");
    }
}

/**
 * 下拉框初始化
 * selectId: 绑定的元素
 * url：请求的地址
 * param：请求的参数
 * o: 返回结果的对象
 * asyncFlag：同步为flase, 异步为空或者true
 * form: layui的form
 */
function laySelect(selectId, url, param, o, asyncFlag, form) {
    if (selectId.indexOf('#') != 0) {
        selectId = '#' + selectId;
    }
    $(selectId).empty();
    $(selectId).append('<option value=""></option>');
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "post",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        success: function (data) {
            if (data.success == null || data.success == true) {
                for (var i in data.result) {
                    $(selectId).append('<option value="' + data.result[i][o.id] + '">' + data.result[i][o.text] + '</option>');
                }
                form.render();
            } else {
                alert(data.msg);
            }
        },
        error: function (XMLHttpRequest) {
            alert('数据处理失败! 错误码:' + XMLHttpRequest.status);
        }
    });
}

function laySelect2(selectId, url, param, o, asyncFlag, form) {
    if (selectId.indexOf('#') != 0) {
        selectId = '#' + selectId;
    }
    $(selectId).empty();
    $(selectId).append('<option value=""></option>');
    if (asyncFlag == null) {
        asyncFlag = true;
    }
    $.ajax({
        type: "post",
        async: asyncFlag,
        dataType: "json",
        url: url,
        data: param,
        success: function (data) {
            if (data.success == null || data.success == true) {
                result = data.result;
                for (var i in data.result) {
                  /*  var currObject = {};
                    $.each(o.param, function (index, item) {
                        currObject[item] = data.result[i][item];
                    });*/
                    $(selectId).append("<option  data-object = '" + JSON.stringify(data.result[i]) + "'" +
                        " value='" + data.result[i][o.id] + "'>" + data.result[i][o.text] + "</option>");
                }
                form.render();
            } else {
                alert(data.msg);
            }
        },
        error: function (XMLHttpRequest) {
            alert('数据处理失败! 错误码:' + XMLHttpRequest.status);
        }
    });
}


function getCurDateStr() {
    var curDate = new Date();
    var month = curDate.getMonth() + 1;
    if (parseInt(month) < 10) {
        month = "-0" + month;
    } else {
        month = "-" + month;
    }

    var day = curDate.getDate();
    if (parseInt(day) < 10) {
        day = "-0" + day;
    } else {
        day = "-" + day;
    }

    return curDate.getFullYear() + month + day;
}

function getDateStr(date) {
    var month = date.getMonth() + 1;
    if (parseInt(month) < 10) {
        month = "-0" + month;
    } else {
        month = "-" + month;
    }

    var day = date.getDate();
    if (parseInt(day) < 10) {
        day = "-0" + day;
    } else {
        day = "-" + day;
    }

    return date.getFullYear() + month + day;
}

// 自定义合并方法
function merger(gridName, CellName) {
    // 得到显示到界面的id集合
    var mergerList = $("#" + gridName).getDataIDs();
    // 当前显示多少条
    var length = mergerList.length;
    for (var i = 0; i < length; i++) {
        // 从上到下获取一条信息
        var before = $("#" + gridName).jqGrid('getRowData', mergerList[i]);
        // 定义合并行数
        var rowSpanTaxCount = 1;
        for (j = i + 1; j <= length; j++) {
            // 和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏
            var end = $("#" + gridName).jqGrid('getRowData', mergerList[j]);
            if (before[CellName] == end[CellName]) {
                rowSpanTaxCount++;
                $("#" + gridName).setCell(mergerList[j], CellName, '', {display: 'none'});
            } else {
                rowSpanTaxCount = 1;
                break;
            }
            $("#" + CellName + "_" + mergerList[i]).attr("rowspan", rowSpanTaxCount);
        }
    }
}
function isEmpty(val) {
    if (val == null || val == ""){
        return true;
    }
    return false;
}
// 获取当前登录用户的信息
function loginUserInfo() {
    var result;
    $.ajax({
        type: "get",
        async: false,
        dataType: "json",
        url: "/sysinfo/user/getcuruser",
        data: {},
        success: function (data) {
            if (data.success == null || data.success == true) {
                result = data.result
            } else {
                alert(data.msg);
            }
        },
        error: function (XMLHttpRequest) {
            alert("数据处理失败！");
        }
    });
    return result;
}


/**
 * 图片预览
 * @param e  当前元素
 * @param isFull  窗口是否最大化
 * @param imgWidth 预览图片宽度
 * @param imgHeight 预览图片高度
 * @param windowsWidth 弹出窗口宽度
 * @param windowsHeight 弹出窗口高度
 */
function previewImg(e,isFull, imgWidth, imgHeight, windowsWidth, windowsHeight) {
    if(isFull == undefined || isFull == '' || isFull == null){
        isFull = false;
    }
    if (imgWidth == null || imgHeight == undefined) {
        imgWidth = 700;
    }
    if (imgHeight == null || imgHeight == undefined) {
        imgHeight = 600;
    }
    if (windowsWidth == null || windowsWidth == undefined) {
        windowsWidth = 700;
    }
    if (windowsHeight == null || windowsHeight == undefined) {
        imgHeight = 600;
    }
    var src = encodeURIComponent(e.src);
    var imgHtml = "<img src= '" + src + "'  " +
        "width = " + imgWidth + "'px;' " +
        "height = " + imgHeight + "'px'/>";
    //弹出层
    var index = layer.open({
        type: 1,
        shade: 0.8,
        offset: 'auto',
        area: [windowsWidth + 'px', windowsHeight + 'px'],  // area: [width + 'px',height+'px']  //原图显示
        shadeClose: true,
        scrollbar: false,
        title: "图片预览", //不显示标题
        content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        cancel: function () {
            //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });
        }
    });
    if(isFull){
        layer.full(index);
    }
}


/**
 *@param jqGridName  jqGrid表格对应的id
 * @param obj 后台传过来的数据
 * colNames  colModel  中的name属性集合（对应后台entity字段）
 * colLabels  colModel  中的label集合（jqGrid 列名）
 * i  列在jqGrid   中的索引
 * colLabelWith  jqGrid  列名的长度
 * valueLength  列对应数据的长度
 */
function jqGridColumnAuto(jqGridName,obj){
    var columnArray = $("#"+jqGridName).jqGrid('getGridParam','colModel');
    //获取colModel  的name
    var colNames = [];
    var colLabels = [];
    $.each(columnArray,function(index,item){
        colNames.push(item.name);
        colLabels.push(item.label);
    });
    //根据字段长度动态设置列宽度
    var i = 0;
    var width = 4;

    $("#jqGrid_rn").css('width',5);
    $("#jqGrid_cb").css('width',10);
    if(obj.rows !=null && obj.rows.length > 0){
        $.each(obj.rows,function(index,item){
            for(var key in item){
                i = 0;
                if(colNames.indexOf(key) !=-1){
                    var colLabelWith = colLabels[colNames.indexOf(key)] == undefined?0:colLabels[colNames.indexOf(key)].length;
                    i = colNames.indexOf(key)+1;
                    if(key != "id"){
                        //$(".ui-jqgrid-htable,ui-common-table").css('width','auto');
                        if(item[key] !=null && item[key].length > 0){
                            var valueLength = item[key].length==0?10:item[key].length;
                            if(valueLength > colLabelWith) {
                                width = valueLength;
                            }else {
                                width = colLabelWith
                            }
                            $("#"+jqGridName+"_"+key).css('width',width*3);
                            $("#"+jqGridName+" tr:first-child td:nth-child("+i+")").css("width",width*3);
                        }else{
                            $("#"+jqGridName+"_"+key).css('width',colLabelWith*5);
                            $("#"+jqGridName+" tr:first-child td:nth-child("+i+")").css("width",colLabelWith*5);
                        }
                    }
                   /* if(colNames.indexOf("rn") != -1){
                        console.log("adasdasdasd大撒大撒");
                        $(".ui-jqgrid-htable,ui-common-table").css('width','auto');
                    }
                    if(colNames.indexOf("cb") != -1){
                        console.log("adasdasdasd打算大苏打");
                        $(".ui-jqgrid-htable,ui-common-table").css('width','auto');
                    }*/
                }
            }
        });
    }
}


