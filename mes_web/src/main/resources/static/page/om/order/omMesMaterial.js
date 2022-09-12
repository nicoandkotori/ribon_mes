/*
 * 材料表entity
 * @author mijiahao
 */
class omMesMaterial {


    constructor() {
        this.id = "";
        this.detailId = "";
        this.partId = "";
        this.recordId = "";
        this.partRowId = "";
        this.mainId = "";
        this.invCode = "";
        this.invName = "";
        this.invStd = "";
        this.unitMaterialPrice = "";
        this.unitMaterialAmount = "";
        this.invLand = "";
        this.invWidth = "";
        this.invLen = "";
        this.invExternalDiameter = "";
        this.invInternalDiameter = "";
        this.invDensity = "";
        this.invSize = "";
        this.iqty = "";
        this.tqty = "";
        this.itype = "";
        this.u8MoMaterialId = "";
        this.rowNo = "";
        this.productInvStd = "";
        this.productInvUnit = "";
        this.productQty = "";
        this.productInvCode = "";
        this.productInvName = "";
        this.partInvName = "";
        this.partInvCode = "";
        this.partQty = "";
        this.partInvStd = "";
        this.partInvUnit = "";
    }



    getId() {
        return this.id;
    }

    //产品表ID
    getDetailId() {
        return this.detailId;
    }

    //部件表ID
    getPartId() {
        return this.partId;
    }

    //委外订单表ID
    getMainId() {
        return this.mainId;
    }

    //材料编码
    getInvCode() {
        return this.invCode;
    }

    getInvStd() {
        return this.invStd;
    }

    //材料名称
    getInvName() {
        return this.invName;
    }

    //材料单价
    getUnitMaterialPrice() {
        return this.unitMaterialPrice;
    }

    //单件材料费
    getUnitMaterialAmount() {
        return this.unitMaterialAmount;
    }

    //厚度
    getInvLand() {
        return this.invLand;
    }

    //宽度
    getInvWidth() {
        return this.invWidth;
    }

    //长
    getInvLen() {
        return this.invLen;
    }

    //外径
    getInvExternalDiameter() {
        return this.invExternalDiameter;
    }

    //内径
    getInvInternalDiameter() {
        return this.invInternalDiameter;
    }

    //密度
    getInvDensity() {
        return this.invDensity;
    }



    //下料尺寸
    getInvSize() {
        return this.invSize;
    }

    //单耗
    getIqty() {
        return this.iqty;
    }

    //总量
    getTqty() {
        return this.tqty;
    }


    getItype() {
        return this.itype;
    }

    getU8MoMaterialId() {
        return this.u8MoMaterialId;
    }

    getRowNo() {
        return this.rowNo;
    }

    //规格
    getProductInvStd() {
        return this.productInvStd;
    }

    //单位
    getProductInvUnit() {
        return this.productInvUnit;
    }

    //产品数量
    getProductQty() {
        return this.productQty;
    }

    //产品编码
    getProductInvCode() {
        return this.productInvCode;
    }

    //产品名称
    getProductInvName() {
        return this.productInvName;
    }

    //部件名称
    getPartInvName() {
        return this.partInvName;
    }

    //部件编码
    getPartInvCode() {
        return this.partInvCode;
    }

    //部件数量
    getPartQty() {
        return this.partQty;
    }

    //部件规格
    getPartInvStd() {
        return this.partInvStd;
    }

    //部件单位
    getPartInvUnit() {
        return this.partInvUnit;
    }


    setId(id) {
        this.id = id;
    }

    //产品表ID
    setDetailId(detailId) {
        this.detailId = detailId;
    }

    //部件表ID
    setPartId(partId) {
        this.partId = partId;
    }

    //委外订单表ID
    setMainId(mainId) {
        this.mainId = mainId;
    }

    //材料编码
    setInvCode(invCode) {
        this.invCode = invCode;
    }

    //材料规格
    setInvStd(invStd) {
        this.invStd = invStd
    }

    //材料名称
    setInvName(invName) {
        this.invName = invName;
    }


    //材料单价
    setUnitMaterialPrice(unitMaterialPrice) {
        this.unitMaterialPrice = unitMaterialPrice;
    }

    //单件材料费
    setUnitMaterialAmount(unitMaterialAmount) {
        this.unitMaterialAmount = unitMaterialAmount;
    }

    //厚度
    setInvLand(invLand) {
        this.invLand = invLand;
    }

    //宽度
    setInvWidth(invWidth) {
        this.invWidth = invWidth;
    }

    //长
    setInvLen(invLen) {
        this.invLen = invLen;
    }


    //外径
    setInvExternalDiameter(invExternalDiameter) {
        this.invExternalDiameter = invExternalDiameter;
    }

    //内径
    setInvInternalDiameter(invInternalDiameter) {
        this.invInternalDiameter = invInternalDiameter;
    }

    //密度
    setInvDensity(invDensity) {
        this.invDensity = invDensity;
    }

    //下料尺寸
    setInvSize(invSize) {
        this.invSize = invSize;
    }

    //单耗
    setIqty(iqty) {
        this.iqty = iqty;
    }

    //总量
    setTqty(tqty) {
        this.tqty = tqty;
    }


    setItype(itype) {
        this.itype = itype;
    }

    setU8MoMaterialId(u8MoMaterialId) {
        this.u8MoMaterialId = u8MoMaterialId;
    }

    setRowNo(rowNo) {
        this.rowNo = rowNo;
    }

    //规格
    setProductInvStd(productInvStd) {
        this.productInvStd = productInvStd;
    }

    //单位
    setProductInvUnit(productInvUnit) {
        this.productInvUnit = productInvUnit;
    }

    //产品数量
    setProductQty(productQty) {
        this.productQty = productQty;
    }

    //产品编码
    setProductInvCode(productInvCode) {
        this.productInvCode = productInvCode;
    }

    //产品名称
    setProductInvName(productInvName) {
        this.productInvName = productInvName;
    }

    //部件名称
    setPartInvName(partInvName) {
        this.partInvName = partInvName;
    }

    //部件编码
    setPartInvCode(partInvCode) {
        this.partInvCode = partInvCode;
    }

    //部件数量
    setPartQty(partQty) {
        this.partQty = partQty;
    }

    //部件规格
    setPartInvStd(partInvStd) {
        this.partInvStd = partInvStd;
    }

    //部件单位
    setPartInvUnit(partInvUnit) {
        this.partInvUnit = partInvUnit;
    }

    getPartRowId() {
        return this.partRowId;
    }


    setPartRowId(value) {
        this.partRowId = value;
    }


    getRecordId() {
        return this.recordId;
    }

    setRecordId(value) {
        this.recordId = value;
    }

    setEntity(data) {

        this.setId(data.id);
        this.setRecordId(data.recordId);
        this.setPartRowId(data.partRowId);
        //产品表ID
        this.setDetailId(data.detailId);
        //部件表ID
        this.setPartId(data.partId);
        //委外订单表ID
        this.setMainId(data.mainId);
        //材料编码
        this.setInvCode(data.invCode);
        //材料规格
        this.setInvStd(data.invStd);
        //材料名称
        this.setInvName(data.invName);
        //材料单价
        this.setUnitMaterialPrice(data.unitMaterialPrice);
        //单件材料费
        this.setUnitMaterialAmount(data.unitMaterialAmount);
        //厚度
        this.setInvLand(data.invLand);
        //宽度
        this.setInvWidth(data.invWidth);
        //长
        this.setInvLen(data.invLen);
        //外径
        this.setInvExternalDiameter(data.invExternalDiameter);
        //内径
        this.setInvInternalDiameter(data.invInternalDiameter);
        //密度
        this.setInvDensity(data.invDensity);
        //下料尺寸
        this.setInvSize(data.invSize);
        //单耗
        this.setIqty(data.iqty);
        //总量
        this.setTqty(data.tqty);
        this.setItype(data.itype);
        this.setU8MoMaterialId(data.u8MoMaterialId);
        this.setRowNo(data.rowNo);
        //规格
        this.setProductInvStd(data.productInvStd);
        //单位
        this.setProductInvUnit(data.productInvUnit);
        //产品数量
        this.setProductQty(data.productQty);
        //产品编码
        this.setProductInvCode(data.productInvCode);
        //产品名称
        this.setProductInvName(data.productInvName);
        //部件名称
        this.setPartInvName(data.partInvName);
        //部件编码
        this.setPartInvCode(data.partInvCode);
        //部件数量
        this.setPartQty(data.partQty);
        //部件规格
        this.setPartInvStd(data.partInvStd);
        //部件单位
        this.setPartInvUnit(data.partInvUnit);
    }


    /*
     * 转换部件表行记录
     */
    setDataFromPartData(data) {
        this.partId = data.id;
        this.detailId = data.detailId;
        this.mainId = data.mainId;
        this.recordId = data.recordId;
        this.partRowId = data.partRowId;
        this.partInvCode = data.partInvCode;
        this.partInvName = data.partInvName;
        this.partInvStd = data.partInvStd;
        this.partInvUnit = data.partInvUnit;
        this.partQty = data.partQty;
    }

    /*
     * 转换产品表行记录
     */
    setRowDataFromProductRow(data) {
        this.setRecordId(data.recordId);
        this.setMainId(data.mainId);
        this.setDetailId(data.id);
        this.setProductInvName(data.productInvName);
        this.setProductInvCode(data.productInvCode);
        this.setProductInvStd(data.productInvStd);
        this.setProductQty(data.productQty)
        this.setProductInvUnit(data.productInvUnit);
    }

    <!--DATE: 2022/9/12-->
    <!--mijiahao TODO:123 -->
    /*
     * 转换u8数据
     */
    setRowDataFromU8Data(u8Data) {
        this.setInvName(u8Data.cinvname);
        this.setInvCode(u8Data.cinvcode);
        this.setInvStd(u8Data.cinvstd);
        this.setPro(u8Data.ccomunitname);
        this.setInvDensity(u8Data.cinvdefine2);
        return this;
    }


}