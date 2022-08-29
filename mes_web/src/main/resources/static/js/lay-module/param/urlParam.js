layui.define([], function (exports) {

    var bas = "/basicinfo";
    var mo = "/mo";

    var kuanNo = bas + "/kuanno";
    var kuanNoDropDown = kuanNo + "/findlist";

    var inv = bas + "/inventory";
    var invTableSelect = inv + "/findtableselect";

    var size = bas + "/size";
    var sizeFindList = size + "/findpage";

    var department = bas + "/department";
    var departmentSave = department + "/saveorupdate";
    var departmentDelete = department + "/softdelete";
    var departmentFindList = department + "/findpage";
    var departmentGetById = department + "/getallbyid";

    var craft = mo + "/craft";
    var craftSave = craft + "/saveorupdate";
    var craftDelete = craft + "/softdelete";
    var craftFindList = craft + "/findpage";
    var craftGetById = craft + "/getallbyid";
    var craftUploadInvImage = craft + "/uploadinvimage";
    var craftUploadPhoto = craft + "/uploadphoto";

    var obj = {
        invTableSelect: invTableSelect,
        sizeFindList: sizeFindList,
        craftSave: craftSave,
        craftDelete: craftDelete,
        craftFindList: craftFindList,
        craftGetById: craftGetById,
        craftUploadInvImage: craftUploadInvImage,
        craftUploadPhoto: craftUploadPhoto,
        kuanNoDropDown: kuanNoDropDown
    };

    /*
     * @todo 弹出层，弹窗方法
     * layui.use 加载layui.define 定义的模块，当外部 js 或 onclick调用 use 内部函数时，需要在 use 中定义 window 函数供外部引用
     * http://blog.csdn.net/xcmonline/article/details/75647144
     */
    /*
     参数解释：
     title   标题
     url     请求的url
     id      需要操作的数据id
     w       弹出层宽度（缺省调默认值）
     h       弹出层高度（缺省调默认值）
     */
    window.WeAdminShow = function (title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        ;
        if (url == null || url == '') {
            url = "404.html";
        }
        ;
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        ;
        if (h == null || h == '') {
            h = ($(window).height() * 0.9);
        }
        ;
        layer.open({
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false, //不固定
            maxmin: true,
            shadeClose: true,
            shade: 0.4,
            title: title,
            content: url
        });
    }

    /*
     * 弹出层，右侧弹窗方法
     */
    /*
     参数解释：
     title   标题
     url     请求的url
     id      需要操作的数据id
     w       弹出层宽度（缺省调默认值）
     h       弹出层高度（缺省调默认值）
     */
    window.WeAdminShowRight = function (title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        if (url == null || url == '') {
            url = "404.html";
        }
        if (w == null || w == '') {
            w = ($(window).width() * 0.4);
        }
        if (h == null || h == '') {
            h = ($(window).height());
        }
        layer.open({
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: true, //不固定
            maxmin: true,
            shadeClose: true,
            shade: 0.4,
            offset: 'r',
            title: title,
            content: url
        });
    }

    //输出接口
    exports('urlParam', obj);
});

