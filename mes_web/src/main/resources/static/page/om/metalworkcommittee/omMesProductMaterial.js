/*
 * 产品表+材料表entity
 * @author mijiahao
 */
class omMesProductMaterial {


    constructor() {
        //材料表的ID
        this.id = "";
        this.recordId = "";
        this.partRowId = "";
        this.detailId = "";
        this.mainId = "";
        this.productInvCode = "";
        this.productInvName = "";
        this.productInvStd = "";
        this.productInvUnit = "";
        this.productQty = "";
        this.invCode = "";
        this.invName = "";
        this.invStd = "";
        this.invUnit = "";
        this.qty = "";
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

    }




    getPartRowId(){
        return this.partRowId;
    }


    setPartRowId(value) {
        this.partRowId = value;
    }

    getId() {
        return this.id;
    }

    getRecordId(){
        return this.recordId;
    }

    setRecordId(value){
        this.recordId = value;
    }

    getMainId() {
        return this.mainId;
    }

    //部件编码
    getPartInvCode() {
        return this.partInvCode;
    }

    //部件名称
    getPartInvName() {
        return this.partInvName;
    }

    //规格
    getPartInvStd() {
        return this.partInvStd;
    }

    //单位
    getPartInvUnit() {
        return this.partInvUnit;
    }

    //数量
    getPartQty() {
        return this.partQty;
    }

    //产品表ID
    getDetailId() {
        return this.detailId;
    }

    //部件表ID
    getPartId() {
        return this.partId;
    }

    //材料编码
    getInvCode() {
        return this.invCode;
    }

    //材料名称
    getInvName() {
        return this.invName;
    }

    //规格
    getInvStd() {
        return this.invStd;
    }

    //单位
    getInvUnit() {
        return this.invUnit;
    }

    //数量
    getQty() {
        return this.qty;
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

    setId( id) {
        this.id = id;
    }
    //委外订单ID
    setMainId( mainId) {
        this.mainId = mainId;
    }
    //产品编码
    setProductInvCode(productInvCode) {
        this.productInvCode = productInvCode;
    }

    //产品名称
    setProductInvName(productInvName) {
        this.productInvName = productInvName;
    }

    //规格型号
    setProductInvStd(productInvStd) {
        this.productInvStd = productInvStd;
    }

    //单位
    setProductInvUnit(productInvUnit) {
        this.productInvUnit = productInvUnit;
    }

    //数量
    setProductQty(productQty) {
        this.productQty = productQty;
    }

    //产品表ID
    setDetailId(detailId) {
        this.detailId = detailId;
    }

    //部件表ID
    setPartId(partId) {
        this.partId = partId;
    }

    //材料编码
    setInvCode(invCode) {
        this.invCode = invCode;
    }

    //材料名称
    setInvName(invName) {
        this.invName = invName;
    }

    //规格
    setInvStd(invStd) {
        this.invStd = invStd;
    }

    //单位
    setInvUnit(invUnit) {
        this.invUnit = invUnit;
    }

    //数量
    setQty(qty) {
        this.qty = qty;
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

    setEntity(data) {
        this.setId(data.id);
        this.setPartRowId(data.partRowId);
        this.setRecordId(data.recordId);
        this.setMainId(data.mainId);
        //产品编码
        this.setProductInvCode(data.productInvCode);
        //产品名称
        this.setProductInvName(data.productInvName);
        //规格型号
        this.setProductInvStd(data.productInvStd);
        //单位
        this.setProductInvUnit(data.productInvUnit);
        //数量
        this.setProductQty(data.productQty);
        //材料编码
        this.setInvCode(data.invCode);
        //材料名称
        this.setInvName(data.invName);
        //规格
        this.setInvStd(data.invStd);
        //单位
        this.setInvUnit(data.invUnit);
        //数量
        this.setQty(data.qty);
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
    setRowDataFromProductRow(data){
        this.setRecordId(data.recordId);
        this.setMainId(data.mainId);
        this.setDetailId(data.id);
        this.setProductInvName(data.productInvName);
        this.setProductInvCode(data.productInvCode);
        this.setProductInvStd(data.productInvStd);
        this.setProductQty(data.productQty)
        this.setProductInvUnit(data.productInvUnit);
    }

    setRowDataFromU8Data(u8Data){

    }


}