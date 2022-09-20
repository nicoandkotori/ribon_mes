//产品表表头配置
const PRODUCT_COL_MODEL = [
    {name: "momaterialsid", hidden: true},
    {name: U8_MO_DETAIL_ID, key: true, hidden: true},
    {name: ID, hidden: true},
    {label:"产品表行标识",name: RECORD_ID,width: 80,sortable: false, editable: true, align: "center"},
    {label: "产品编码", name: PRODUCT_INV_CODE, width: 110, sortable: false, editable: true, align: "center"},
    {label: "产品名称", name: PRODUCT_INV_NAME, width: 90, sortable: false, editable: false, align: "center"},
    {label: "规格型号", name: PRODUCT_INV_STD, width: 80, sortable: false, editable: false, align: "center"},
    {label: "单位", name: PRODUCT_INV_UNIT, width: 40, sortable: false, editable: false, align: "center"},
    {label: "数量", name: PRODUCT_QTY, width: 40, sortable: false, editable: true, align: "center"},
    {label: "材料单价", name: MATERIAL_PRICE, width: 60, sortable: false, editable: false, align: "center"},
    {label: "单件材料费", name: MATERIAL_AMOUNT, width: 70, sortable: false, editable: false, align: "center"},
    {label: "单件加工费（含税）", name: WORK_PRICE, width: 120, sortable: false, editable: true, align: "center"},
    {label: "加工费合计", name: TOTAL_WORK_AMOUNT, width: 70, sortable: false, editable: false, align: "center"},
    {label: "单件价格", name: PRICE, width: 60, sortable: false, editable: true, align: "center"},
    {label: "合计", name: AMOUNT, width: 70, sortable: false, editable: false, align: "center"},
    {label: "税率", name: TAX_RATE, width: 70, sortable: false, editable: true, align: "center"},
    {label: "不含税单价", name: WORK_PRICE_WITHOUT_TAX,hidden: true ,width: 70, sortable: false, editable: true, align: "center"},
    {name: ROW_ID, hidden: true},
    {name: MAIN_ID, hidden: true},
    {
        label: "计划开工日期",
        name: PLAN_START_DATE,
        width: 80,
        editable: true,
        sortable: false,
        align: "center",
        datefmt: 'yyyy-mm-dd',
        editoptions: {
            dataInit: function (e) {
                $(e).datepicker({
                    todayHighlight: true,
                    language: "zh-CN",//语言
                    autoclose: true,//自动关闭
                    todayBtn: "linked",//
                    format: "yyyy-mm-dd",//时间显示格式
                }).on('changeDate',function(e){
                    let dateObj = e.date;
                    //转换为年月日
                    let time = dateObj.getFullYear()+"-"+(dateObj.getMonth()+1)+"-"+dateObj.getDate();
                    planStartDate = time;
                });

                $(this).click(function (e) {//选中时间后隐藏
                    $(e).parent().datepicker('hide');
                });
            }
        }
    },
    {
        label: "计划完工日期",
        name: PLAN_END_DATE,
        width: 80,
        editable: true,
        sortable: false,
        align: "center",
        datefmt: 'yyyy-mm-dd',
        editoptions: {
            dataInit: function (e) {
                $(e).datepicker({
                    todayHighlight: true,
                    language: "zh-CN",//语言
                    autoclose: true,//自动关闭
                    todayBtn: "linked",//
                    format: "yyyy-mm-dd"//时间显示格式
                }).on('changeDate',function(e){
                    let dateObj = e.date;
                    //转换为年月日
                    let time = dateObj.getFullYear()+"-"+(dateObj.getMonth()+1)+"-"+dateObj.getDate();
                    planEndDate = time;
                });
                $(this).click(function (e) {//选中时间后隐藏
                    $(e).parent().datepicker('hide');
                });
            }
        }
    },
]

/*
 * 材料表表头
 */
const MATERIAL_COL_MODEL = [
    {name: U8_MO_MATERIAL_ID, key: true, hidden: true},
    {name: ID, hidden: true},
    {label:'产品表行标识',name: RECORD_ID, hidden: INDEX_HIDDEN,width: 80},
    {label:"部件表行表示",name: PART_ROW_ID, hidden: INDEX_HIDDEN,width: 80},
    {label: "产品编码", name: PRODUCT_INV_CODE, width: 110, sortable: false, editable: false, align: "center"},
    {label: "产品名称", name: PRODUCT_INV_NAME, width: 90, sortable: false, editable: false, align: "center"},
    {label: "规格型号", name: PRODUCT_INV_STD, width: 80, sortable: false, editable: false, align: "center"},
    {label: "单位", name: PRODUCT_INV_UNIT, width: 40, sortable: false, editable: false, align: "center"},
    {label: "产品数量", name: PRODUCT_QTY, width: 70, sortable: false, editable: false, align: "center"},
    {label: "规格型号", name: PART_INV_STD, width: 80, sortable: false, editable: false, align: "center"},
    {label: "单位", name: PART_INV_UNIT, width: 40, sortable: false, editable: false, align: "center"},
    {label: "材料单价", name: UNIT_MATERIAL_PRICE, width: 60, sortable: false, editable: true, align: "center"},
    {label: "单件材料费", name: UNIT_MATERIAL_AMOUNT, width: 70, sortable: false, editable: true, align: "center"},
    {label: "材料编码", name: INV_CODE, width: 90, sortable: false, editable: true, align: "center"},
    {label: "材料名称", name: INV_NAME, width: 120, sortable: false, editable: false, align: "center"},
    {label: "材料规格", name: INV_STD, width: 80, sortable: false, editable: false, align: "center"},
    {label: "厚度", name: INV_LAND, width: 40, sortable: false, editable: true, align: "center"},
    {label: "长", name: INV_LEN, width: 45, sortable: false, editable: true, align: "center"},
    {label: "宽", name: INV_WIDTH, width: 40, sortable: false, editable: true, align: "center"},
    {label: "外径", name: INV_EXTERNAL_DIAMETER, width: 40, sortable: false, editable: true, align: "center"},
    {label: "内径", name: INV_INTERNAL_DIAMETER, width: 40, sortable: false, editable: true, align: "center"},
    {label: "密度", name: INV_DENSITY, width: 40, sortable: false, editable: true, align: "center"},
    {label: "下料尺寸", name: INV_SIZE, width: 110, sortable: false, editable: true, align: "center"},
    {label: "单耗", name: IQTY, width: 45, sortable: false, editable: true, align: "center"},
    {label: "总量", name: TQTY, width: 45, sortable: false, editable: false, align: "center"},
    {label: "单位", name: INV_UNIT,hidden: true },
    {label: "部件编码", name: PART_INV_CODE,hidden: true, width: 110, sortable: false, editable: true, align: "center"},
    {label: "部件名称", name: PART_INV_NAME,hidden: true, width: 90, sortable: false, editable: false, align: "center"},
    {label: "规格型号", name: PART_INV_STD, hidden: true,width: 80, sortable: false, editable: false, align: "center"},
    {label: "单位", name: PART_INV_UNIT, hidden: true,width: 40, sortable: false, editable: false, align: "center"},
    {label: "部件数量", name: PART_QTY,hidden: true, width: 70, sortable: false, editable: true, align: "center"},
    {name: ROW_ID, hidden: true},
    {name: MAIN_ID, hidden: true},
]

/*
 * 部件表表头
 */
const PART_COL_MODEL = [
    {label:"产品表行标识",name: RECORD_ID,width: 80,sortable: false, editable: false, align: "center",hidden:INDEX_HIDDEN},
    {label: "部件表行标识",name: PART_ROW_ID, width: 80,hidden:INDEX_HIDDEN},
    {label: "部件编码", name: PART_INV_CODE, width: 110, sortable: false, editable: false, align: "center"},
    {label: "部件名称", name: PART_INV_NAME, width: 90, sortable: false, editable: false, align: "center"},
    {label: "规格型号", name: PART_INV_STD, width: 80, sortable: false, editable: false, align: "center"},
    {label: "单位", name: PART_INV_UNIT, width: 40, sortable: false, editable: false, align: "center"},
    {label: "数量", name: PART_QTY, width: 40, sortable: false, editable: false, align: "center"},
    {name: DETAIL_ID, hidden: true},
    {name: ROW_ID, hidden: true},
    {name: MAIN_ID, hidden: true},
    {name: ID, hidden: true},
]