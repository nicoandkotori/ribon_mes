$.fn.serializeObject = function () {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function () {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return JSON.stringify(o);
	//return o;
}


//还有checkbox的form表单
$.fn.serializeObjectWithChk = function () {
    var o = {};
    var a = this.serializeArray();
    //取出所有Checkbox的值
    $chk = $("form input:checkbox");
    $.each($chk, function (i,n) {
        //将每一个chk的值插入到a中.  
        var obj = new Object();
        obj.name = n.id;//obj.name = n.name;   modify on2016-12-24  by Yao
        obj.value = n.checked;
        a.push(obj);
    });

    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value );
        } else {
            o[this.name] = this.value ;
        }
    });
    return JSON.stringify(o);
    //return o;
}


$.fn.serializeObjectQuery=function()
{
    var o = {};
    var a = this.serializeArray();
    //取出所有Checkbox的值
    $chk = $("form input:checkbox");
    $.each($chk, function (i, n) {
        //将每一个chk的值插入到a中.  
        var obj = new Object();
        obj.name = n.id;//obj.name = n.name;   modify on2016-12-24  by Yao
        obj.value = n.checked;
        a.push(obj);
    });

   
    //radio 的值
    $radio = $("form input:radio");
    $.each($radio, function (i, n) {
        //将 radio的值插入到a中.  
        if (n.checked)
        {
            var obj = new Object();
            obj.name = n.name; 
            obj.value = n.value;
            a.push(obj);
        }
       
    });

    

    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value);
        } else {
            o[this.name] = this.value;
        }
    });
    return JSON.stringify(o);
}

/**
 * 将josn对象赋值给form
 * @param {dom} 指定的选择器
 * @param {obj} 需要给form赋值的json对象
 * @method serializeJson
 * */
$.fn.setForm = function (jsonValue) {
    if (jsonValue == null){
        return;
    }
    var obj = this;
    $.each(jsonValue, function (name, ival) {
        var $oinput = obj.find("input[id=" + name + "]");
        if ($oinput.length > 0) {
            if ($oinput.attr("type") == "checkbox") {
                if (ival !== null) {
                    var checkboxObj = $("[id=" + name + "]");
                    if (typeof(ival) == 'boolean'){
                        checkboxObj.prop('checked', ival);
                    }else if (typeof(ival) == 'string'){
                        var checkArray = ival.split(";");
                        for (var i = 0; i < checkboxObj.length; i++) {
                            for (var j = 0; j < checkArray.length; j++) {
                                if (checkboxObj[i].value == checkArray[j]) {
                                    checkboxObj[i].click();
                                }
                            }
                        }
                    }
                }
            }
            else if ($oinput.attr("type") == "radio") {
                $oinput.each(function () {
                    var radioObj = $("[name=" + name + "]");
                    for (var i = 0; i < radioObj.length; i++) {
                        if (radioObj[i].value == ival) {
                            radioObj[i].click();
                        }
                    }
                });
            }
            else if ($oinput.attr("type") == "textarea") {
                obj.find("[name=" + name + "]").html(ival);
            }
            else {

                obj.find("[name=" + name + "]").val(ival);

            }
        }

        var $oselect = obj.find("select[name=" + name + "]");
        if ($oselect.length > 0) {
            //$("[name=" + name + "]").select2("val", ival);
            //$("[name=" + name + "]").select2.defaults.set("key", "value").
            $("[name=" + name + "]").val(ival).trigger("change");
        }

        var $otextarea = obj.find("textarea[name=" + name + "]");
        if ($otextarea.length > 0) {
            $("[name=" + name + "]").val(ival);
        }
    })
}
