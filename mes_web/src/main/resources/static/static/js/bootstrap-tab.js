function addTabsByTabs(options) {
    var $navTabs = $(window.parent.parent.document.getElementsByClassName("nav-tabs"));
    if ($navTabs.children().length >= 10) {
        alert("请先尝试关闭一些页面，再打开新页面");
        return;
    }
    //var rand = Math.random().toString();
    //var id = rand.substring(rand.indexOf('.') + 1);
    var url = window.location.protocol + '//' + window.location.host;
    options.url = url + options.url;
    id = "tab_" + options.id;
    var $active = window.parent.parent.document.getElementsByClassName("active");
    $($active).removeClass("active");
    //如果TAB不存在，创建一个新的TAB
    var $ids = window.parent.parent.document.getElementById(id);
    if (!$($ids)[0]) {
        //固定TAB中IFRAME高度
        mainHeight = $(window.parent.parent.document).height() - 63 - $navTabs.height();
        //创建新TAB的title
        title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
        //是否允许关闭
        if (options.close) {
            title += ' <i class="glyphicon glyphicon-remove-sign" tabclose="' + id + '"></i>';
        }
        title += '</a></li>';
        //是否指定TAB内容
        if (options.content) {
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"  >' + options.content + '</div>';
        } else {//没有内容，使用IFRAME打开链接
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"  ><iframe src="' + options.url + '" width="100%" height="' + mainHeight + '" frameborder="no" border="0"   marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe></div>';
        }
        //加入TABS
        var $tabcontent = window.parent.parent.document.getElementsByClassName("tab-content");
        $navTabs.append(title);
        $($tabcontent).append(content);
    }
    //激活TAB
    var $id = window.parent.parent.document.getElementById(id);
    var $tabid = window.parent.parent.document.getElementById("tab_" + id);
    $($tabid).addClass('active');
    $($id).addClass("active");
};

function addTabsByTab(options) {
    var $navTabs = $(window.parent.document.getElementsByClassName("nav-tabs"));
    if ($navTabs.children().length >= 10) {
        alert("请先尝试关闭一些页面，再打开新页面");
        return;
    }
    //var rand = Math.random().toString();
    //var id = rand.substring(rand.indexOf('.') + 1);
    var url = window.location.protocol + '//' + window.location.host;
    options.url = url + options.url;
    id = "tab_" + options.id;
    var $active = window.parent.document.getElementsByClassName("active");
    $($active).removeClass("active");
    //如果TAB不存在，创建一个新的TAB
    var $ids = window.parent.document.getElementById(id);
    if (!$($ids)[0]) {
        //固定TAB中IFRAME高度
        mainHeight = $(window.parent.document).height() - 63 - $navTabs.height();
        //创建新TAB的title
        title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
        //是否允许关闭
        if (options.close) {
            title += ' <i class="glyphicon glyphicon-remove-sign" tabclose="' + id + '"></i>';
        }
        title += '</a></li>';
        //是否指定TAB内容
        if (options.content) {
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"  >' + options.content + '</div>';
        } else {//没有内容，使用IFRAME打开链接
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"  ><iframe src="' + options.url + '" width="100%" height="' + mainHeight + '" frameborder="no" border="0"   marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe></div>';
        }
        //加入TABS
        var $tabcontent = window.parent.document.getElementsByClassName("tab-content");
        $navTabs.append(title);
        $($tabcontent).append(content);
    }
    //激活TAB
    var $id = window.parent.document.getElementById(id);
    var $tabid = window.parent.document.getElementById("tab_" + id);
    $($tabid).addClass('active');
    $($id).addClass("active");
};

var addTabs = function (options) {
    if ($(".nav-tabs").children().length >= 10){
        alert("请先尝试关闭一些页面，再打开新页面");
        return;
    }
    //var rand = Math.random().toString();
    //var id = rand.substring(rand.indexOf('.') + 1);
    var url = window.location.protocol + '//' + window.location.host;
    options.url = url + options.url+"?mid="+options.id;
    id = "tab_" + options.id;
    $(".active").removeClass("active");
    //如果TAB不存在，创建一个新的TAB
    if (!$("#" + id)[0]) {
        //固定TAB中IFRAME高度
        //mainHeight = $(document.body).height() + 60;
        mainHeight = $(document).height()-63- $(".nav-tabs").height();
        //创建新TAB的title
        title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
        //是否允许关闭
        if (options.close) {
            title += ' <i class="glyphicon glyphicon-remove-sign" tabclose="' + id + '"></i>';
        }
        title += '</a></li>';
        //是否指定TAB内容
        if (options.content) {
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"  >' + options.content + '</div>';
        } else {//没有内容，使用IFRAME打开链接
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"  ><iframe src="' + options.url + '" width="100%" height="' + mainHeight + '" frameborder="no" border="0"   marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe></div>';
        }
        //加入TABS
        $(".nav-tabs").append(title);
        $(".tab-content").append(content);
    }
    //激活TAB
    $("#tab_" + id).addClass('active');
    $("#" + id).addClass("active");
};

var closeTab = function (id) {
    //如果关闭的是当前激活的TAB，激活他的前一个TAB
    if ($("li.active").attr('id') == "tab_" + id) {
        $("#tab_" + id).prev().addClass('active');
        $("#" + id).prev().addClass('active');
    }
    //关闭TAB
    $("#tab_" + id).remove();
    $("#" + id).remove();
};

$(function () {
    mainHeight = $(document.body).height() - 45;
    $('.main-left,.main-right').height(mainHeight);
    $("[addtabs]").click(function () {
        addTabs({ id: $(this).attr("id"), title: $(this).attr('title'), close: true });
    });

    $(".nav-tabs").on("click", "[tabclose]", function (e) {
        id = $(this).attr("tabclose");
        closeTab(id);
    });
});
