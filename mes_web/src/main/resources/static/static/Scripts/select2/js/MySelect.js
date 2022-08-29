//绑定字典内容到指定的Select控件


//select2 的绑定  同步  
//url  数据的地址
//param  查询的参数对象   {code:code}
//o     object对象 , 是要绑定的对象(对应到后台实体类中的哪个属性)  比如  {id:Id,text:name,code:Code}   id 和text 必填
//IsNullItem   是否要显示空项目 默认false
//allowSearch 是否要显示搜索框 默认false
//placeholder 占位符 默认''
//isDefaultSelect 是否默认选中第一项，默认false
$.fn.Select2BindSync = function (url, param, o, IsNullItem, allowSearch,
                     placeholder,isDefaultSelect, asyncFlag) {
    //var o = {};
    var result = new Array();
    var selector = this;
    var arr = new Array();
    if (IsNullItem == null){
        IsNullItem = false;
    }
    if (allowSearch == null){
        allowSearch = false;
    }
    if(placeholder == undefined){
        placeholder = "";
    }
    if (isDefaultSelect == null){
        isDefaultSelect = false;
    }
    if (IsNullItem) {
        arr.push(new Object({ id: "", text: "" }));
    }
    var firstVal = '';
    //var o = { id: "Id", text: "LicenseNum", TrailerLicenseNum: "TrailerLicenseNum" };
    sendPostReq(url, param, function (data) {
        result = data.result;
        $.each(data.result, function (i, n) {
            var obj = new Object();
            //obj.id = this.Id;
            //obj.text = this.LicenseNum;
            //obj.TrailerLicenseNum = this.TrailerLicenseNum;
            $.each(o, function (j, m) {
                obj[j] = n[m];
            });
            if (i == 0) {
                firstVal = obj.id;
            }
            arr.push(obj);
        });

        if (allowSearch == false) {
            selector.select2({
                minimumResultsForSearch: -1,
                placeholder: placeholder,

                allowClear: true,
                data: arr
            });
        } else {
            selector.select2({
                placeholder: placeholder,

                allowClear: true,
                data: arr
            });
        }
        if (isDefaultSelect) {
            selector.val(firstVal).trigger('change');
        }
    }, asyncFlag);
    return result;
}

/**
 * 下拉的时候  实时搜索
 * @param url
 * @param name 要显示的字段属性
 * @param inputMessage 输入提示
 * @constructor
 */
$.fn.Select2BindCache = function (url, name, inputMessage) {
    var selector = this;
    selector.select2({
        language: "zh-CN",// 指定语言为中文，国际化才起效
        inputMessage: inputMessage,// 添加默认参数
        ajax: {
            url: url,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                params.page = params.page || 1;
                return {
                   query: params.term, // search term
                   page: params.page,   //第几页
                   rows:10
               };
            },
            processResults: function (data, params) {
                // parse the results into the format expected by Select2
                // since we are using custom formatting functions we do not need to
                // alter the remote JSON data, except to indicate that infinite
                // scrolling can be used
                params.page = params.page || 1;

                return {
                    results: data.data,
                    pagination: {
                        more: params.page < data.total
                    }
                };
            },
            cache: false
        },
        escapeMarkup: function (markup) {
            return markup;
        }, // let our custom formatter work
        minimumInputLength: 1,// 最少输入一个字符才开始检索
        templateResult: function (repo) {// 显示的结果集格式，这里需要自己写css样式，可参照demo
            return repo[name];
        },
        templateSelection: function (repo) {
            return repo[name];
        }// 列表中选择某一项后显示到文本框的内容
    });
}

//select2 的异步绑定 下拉列表  ***如果isSelect=TRUE   默认选中.
//url  数据的地址
//param  查询的参数对象   {code:code}
//DefaultValue  默认选中的值。  为空 就根据查询出来的数据来。
//o     object对象 , 是要绑定的对象(对应到后台实体类中的哪个属性)  比如  {id:Id,text:name,code:Code}   id 和text 必填
//isSearchArea   是否要显示搜索框,默认false
//allowClear 是否允许清空，默认true
$.fn.Select2BindAndSetDefault = function (url, param, o,defaultVal, isSearchArea, allowClear) {
    //var o = {};
    var selector = this;
    var arr = new Array();
    arr.push(new Object({ id: "", text: "" }));
    if (allowClear == null){
        allowClear = true;
    }
    //var o = { id: "Id", text: "LicenseNum", TrailerLicenseNum: "TrailerLicenseNum" };
    $.ajax(
        {
            type: "get",
            url: url,
            data: param,
            async: false,
            success: function (data) {
                var selectValue = defaultVal;
                $.each(data, function (i, n) {
                    var obj = new Object();
                    $.each(o, function (j, m) {
                        obj[j] = n[m];
                        if (selectValue == "" || selectValue == undefined) {
                            if (j == "id") {
                                if (n.defaultSelect == true || n.defaultSelect == "true") {
                                    selectValue = n[m];
                                }
                            }
                        }
                        if (selectValue == obj[j]) {
                            selectValue = obj["id"];
                        }
                    });

                    arr.push(obj);
                });
                if (isSearchArea == true) {
                    selector.select2({
                        data: arr,
                        placeholder: "",
                        allowClear: allowClear
                    });
                } else {
                    selector.select2({
                        minimumResultsForSearch: -1,
                        allowClear: allowClear,
                        placeholder: "",
                        data: arr
                    });
                }
                selector.val(selectValue).trigger('change');
            }
        });
    //return o;
}

 