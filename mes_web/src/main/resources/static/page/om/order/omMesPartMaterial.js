/*
 * 部件表+材料表entity
 */
class omMesPartMaterial{

    constructor() {
        //材料表的ID
        this.id = "";
        this.recordId = "";
        this.partRowId = "";
        this.detailId = "";
        this.mainId = "";
        this.partInvCode = "";
        this.partInvName = "";
        this.partInvStd = "";
        this.partInvUnit = "";
        this.partQty = "";
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

    getRecordId(){
        return this.recordId;
    }

    setRecordId(value){
        this.recordId = value;
    }

    getPartRowId(){
        return this.partRowId;
    }


    setPartRowId(value) {
        this.partRowId = value;
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

    //部件编码
    setPartInvCode( partInvCode) {
        this.partInvCode = partInvCode;
    }
    //部件名称
    setPartInvName( partInvName) {
        this.partInvName = partInvName;
    }
    //规格
    setPartInvStd( partInvStd) {
        this.partInvStd = partInvStd;
    }
    //单位
    setPartInvUnit( partInvUnit) {
        this.partInvUnit = partInvUnit;
    }
    //数量
    setPartQty( partQty) {
        this.partQty = partQty;
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
        this.setRecordId(data.recordId);
        this.setPartRowId(data.partRowId);
        this.setMainId(data.mainId);
        //部件编码
        this.setPartInvCode(data.partInvCode);
        //部件名称
        this.setPartInvName(data.partInvName);
        //规格
        this.setPartInvStd(data.partInvStd);
        //单位
        this.setPartInvUnit(data.partInvUnit);
        //数量
        this.setPartQty(data.partQty);
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
     * 将部件表行记录转换成部件-材料表行记录
     */
    setDataFromPartData(data) {
        this.setPartId(data.id);
        this.setDetailId(data.detailId)
        this.setMainId(data.mainId)
        this.setRecordId(data.recordId)
        this.setPartRowId(data.partRowId)
        this.setPartInvCode(data.partInvCode)
        this.setPartInvName(data.partInvName)
        this.setPartInvStd(data.partInvStd)
        this.setPartInvStd(data.partInvUnit)
        this.setPartQty(data.partQty);
    }
}