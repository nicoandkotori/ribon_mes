layui.define(['jquery', 'laydate'], function (exports) {
    var $ = layui.jquery,
        laydate = layui.laydate;

    var obj = {
        initSelect: function () {
            $("td select").each(function(){
                $(this).val($(this).data('value')).parents('div.layui-table-cell').css('overflow', 'visible');
            });
            $("td .layui-input.layui-unselect").each(function(){
                $(this).height('30px');
            });
        },

        initDate: function (obj, ele) {
            laydate.render({
                elem: ele.firstChild
                , show: true //直接显示
                , closeStop: ele
                , done: function (value, date) {
                    var data = {[obj.event]: value};
                    obj.update(data);
                }
            });
        }
    };
    //输出接口
    exports('tableEdit', obj);
});