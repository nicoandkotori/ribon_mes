const MAIN_TABLE_ID = "main_table";
const SELECTOR_MAIN_TABLE_ID = "#"+MAIN_TABLE_ID;
const PRODUCT_TABLE_ID = "jqGrid";
const SELECTOR_PRODUCT_TABLE_ID = "#"+PRODUCT_TABLE_ID;
const PART_TABLE_ID = "part_table";
const SELECTOR_PART_TABLE_ID = "#"+PART_TABLE_ID;
const MATERIAL_TABLE_ID = "jqGridDetail";
const SELECTOR_MATERIAL_TABLE_ID = "#"+MATERIAL_TABLE_ID;
//行标识隐藏flag
const INDEX_HIDDEN = false;
const DEBUG_MODEL = false;

const URL_BASE_ORDER = "/om/order/";
//分页查询mes委外订单url
const URL_GET_MES_ORDER = URL_BASE_ORDER+"getmainlistbypage";
//分页查询mes委外订单子表url
const URL_GET_MES_DETAIL = URL_BASE_ORDER+"getdetaillist";
/*
 * 订单表字段
 */
//订单编号
const VOUCH_CODE = "vouchCode";
//订单日期
const VOUCH_DATE = "vouchDate";
//委外合同
const CONTRACT_OM = "contractOm";
//销售合同
const CONTRACT_SALE = "contractSale";
//供应商编码
const VEN_CODE = "venCode";
//供应商名称
const VEN_NAME = "venName";
//部门编码
const DEP_CODE = "depCode";
//部门名称
const DEP_NAME = "depName";
//业务员编码
const PERSON_CODE = "personCode";
//业务员姓名
const PERSON_NAME = "personName";
//备注
const REMARK = "remark";
//运输方式
const TRANSPORT_WAY = "transportWay";


/*
 * 产品表字段
 */
const RECORD_ID = "recordId";
const ID = "id";
const MAIN_ID = "mainId";
//产品编码
const PRODUCT_INV_CODE = "productInvCode";
//产品名称
const PRODUCT_INV_NAME = "productInvName";
//规格型号
const PRODUCT_INV_STD = "productInvStd";
//单位
const PRODUCT_INV_UNIT = "productInvUnit";
//数量
const PRODUCT_QTY = "productQty";
//材料单价
const MATERIAL_PRICE = "materialPrice";
//单件材料费
const MATERIAL_AMOUNT = "materialAmount";
//单件加工费
const WORK_PRICE = "workPrice";
//加工费合计
const TOTAL_WORK_AMOUNT = "totalWorkAmount";
//单件价格
const PRICE = "price";
//合计
const AMOUNT = "amount";
//计划开工日期
const PLAN_START_DATE = "planStartDate";
//计划完工日期
const PLAN_END_DATE = "planEndDate";
const ROW_NO = "rowNo";

/*
 * 部件表字段
 */
const PART_ROW_ID = "partRowId";
//产品表ID
const DETAIL_ID = "detailId";
//部件编码
const PART_INV_CODE = "partInvCode";
//部件名称
const PART_INV_NAME = "partInvName";
//规格
const PART_INV_STD = "partInvStd";
//单位
const PART_INV_UNIT = "partInvUnit";
//数量
const PART_QTY = "partQty";

/*
 * 材料表字段
 */
//部件表ID
const PART_ID = "partId";
//材料编码
const INV_CODE = "invCode";
//材料名称
const INV_NAME = "invName";
//规格
const INV_STD = "invStd";
//材料单位
const INV_UNIT = "invUnit";
//材料单价
const UNIT_MATERIAL_PRICE = "unitMaterialPrice";
//单件材料费
const UNIT_MATERIAL_AMOUNT = "unitMaterialAmount";
//厚度
const INV_LAND = "invLand";
//宽度
const INV_WIDTH = "invWidth";
//长
const INV_LEN = "invLen";
//外径
const INV_EXTERNAL_DIAMETER = "invExternalDiameter";
//内径
const INV_INTERNAL_DIAMETER = "invInternalDiameter";
//密度
const INV_DENSITY = "invDensity";
//下料尺寸
const INV_SIZE = "invSize";
//单耗
const IQTY = "iqty";
//总量
const TQTY = "tqty";
const U8_MO_MATERIAL_ID = "u8MoMaterialId";
