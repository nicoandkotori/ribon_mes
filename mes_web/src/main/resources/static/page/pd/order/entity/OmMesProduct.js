/*
 * mes产品表类,新增字段需要修改constructor,getter,setter,setEntity
 * @author mijiahao
 */
class OmMesProduct {

    constructor() {
        //
        this.id = "";
        this.rowId = "";
        //
        this.recordId = "";
        //
        this.mainId = "";
        //
        this.productInvCode = "";
        //
        this.productInvName = "";
        //
        this.productInvStd = "";
        //
        this.productInvUnit = "";
        //
        this.productQty = "";
        //
        this.materialPrice = "";
        //
        this.materialAmount = "";
        //
        this.workPrice = "";
        //
        this.totalWorkAmount = "";
        //
        this.price = "";
        //
        this.amount = "";
        //
        this.workPriceWithoutTax = "";
        //
        this.planStartDate = "";
        //
        this.planEndDate = "";
        //
        this.u8MoDetailId = "";
        //
        this.rowNo = "";

        this.taxRate = "";

    }


    setWorkPriceWithoutTax(value){
        this.workPriceWithoutTax = value;
    }

    getWorkPriceWithoutTax(){
        return this.workPriceWithoutTax;
    }

    setRowId(value){
        this.rowId = value;
    }

    getRowId(){
        return this.rowId;
    }
    getId() {
        return this.id;
    }

    getRecordId(){
        return this.recordId;
    }

    getMainId() {
        return this.mainId;
    }

    //产品编码
    getProductInvCode() {
        return this.productInvCode;
    }

    //产品名称
    getProductInvName() {
        return this.productInvName;
    }

    //规格型号
    getProductInvStd() {
        return this.productInvStd;
    }

    //单位
    getProductInvUnit() {
        return this.productInvUnit;
    }

    //数量
    getProductQty() {
        return this.productQty;
    }

    //材料单价
    getMaterialPrice() {
        return this.materialPrice;
    }

    //单件材料费
    getMaterialAmount() {
        return this.materialAmount;
    }

    //单件加工费
    getWorkPrice() {
        return this.workPrice;
    }

    //加工费合计
    getTotalWorkAmount() {
        return this.totalWorkAmount;
    }

    //单件价格
    getPrice() {
        return this.price;
    }

    setTaxRate(value){
        this.taxRate = value;
    }

    getTaxRate(){
        return this.taxRate;
    }

    //合计
    getAmount() {
        return this.amount;
    }

    //计划开工日期
    getPlanStartDate() {
        return this.planStartDate;
    }

    //计划完工日期
    getPlanEndDate() {
        return this.planEndDate;
    }


    getU8MoDetailId() {
        return this.u8MoDetailId;
    }

    getRowNo() {
        return this.rowNo;
    }


    setId(id) {
        this.id = id;
    }

    setRecordId(recordId){
        this.recordId = recordId;
    }

    setMainId(mainId) {
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

    //材料单价
    setMaterialPrice(materialPrice) {
        this.materialPrice = materialPrice;
    }

    //单件材料费
    setMaterialAmount(materialAmount) {
        this.materialAmount = materialAmount;
    }

    //单件加工费
    setWorkPrice(workPrice) {
        this.workPrice = workPrice;
    }

    //加工费合计
    setTotalWorkAmount(totalWorkAmount) {
        this.totalWorkAmount = totalWorkAmount;
    }

    //单件价格
    setPrice(price) {
        this.price = price;
    }

    //合计
    setAmount(amount) {
        this.amount = amount;
    }

    //计划开工日期
    setPlanStartDate(planStartDate) {
        this.planStartDate = planStartDate;
    }

    //计划完工日期
    setPlanEndDate(planEndDate) {
        this.planEndDate = planEndDate;
    }

    setU8MoDetailId(u8MoDetailId) {
        this.u8MoDetailId = u8MoDetailId;
    }

    setRowNo(rowNo) {
        this.rowNo = rowNo;
    }

    setEntity(data) {
        this.setId(data.id);
        this.setRowId(data.rowId)
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
        //税率
        this.setTaxRate(data.taxRate);
        //材料单价
        this.setMaterialPrice(data.materialPrice);
        //单件材料费
        this.setMaterialAmount(data.materialAmount);
        //单件加工费
        this.setWorkPrice(data.workPrice);
        //加工费合计
        this.setTotalWorkAmount(data.totalWorkAmount);
        //单件价格
        this.setPrice(data.price);
        //合计
        this.setAmount(data.amount);
        //计划开工日期
        this.setPlanStartDate(data.planStartDate);
        //计划完工日期
        this.setPlanEndDate(data.planEndDate);
        //不含税单价
        this.setWorkPriceWithoutTax(data.workPriceWithoutTax);
        this.setU8MoDetailId(data.u8MoDetailId);
        this.setRowNo(data.rowNo);
    }


    /*
     * 将u8数据的字段进行转换
     */
    setDataFromU8Data(data) {
        this.setProductInvName(data.cinvname);
        this.setProductInvCode(data.cinvcode);
        this.setProductInvUnit(data.ccomunitname);
        this.setProductInvStd(data.cinvstd);
    }
}








