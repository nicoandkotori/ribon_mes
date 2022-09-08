/*
 * mes产品表类
 * @author mijiahao
 */
class omMesProduct {

    constructor() {
        //
        this.id = "";
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
        this.planStartDate = "";
        //
        this.planEndDate = "";
        //
        this.u8MoDetailId = "";
        //
        this.rowNo = "";

    }

    setId(id) {
        this.id = id;
    }

    setMainId(mainId) {
        this.mainId = mainId;
    }

    setProductInvCode(productInvCode) {
        this.productInvCode = productInvCode;
    }

    setProductInvName(productInvName) {
        this.productInvName = productInvName;
    }

    setProductInvStd(productInvStd) {
        this.productInvStd = productInvStd;
    }

    setProductInvUnit(productInvUnit) {
        this.productInvUnit = productInvUnit;
    }

    setProductQty(productQty) {
        this.productQty = productQty;
    }

    setMaterialPrice(materialPrice) {
        this.materialPrice = materialPrice;
    }

    setMaterialAmount(materialAmount) {
        this.materialAmount = materialAmount;
    }

    setWorkPrice(workPrice) {
        this.workPrice = workPrice;
    }

    setTotalWorkAmount(totalWorkAmount) {
        this.totalWorkAmount = totalWorkAmount;
    }

    setPrice(price) {
        this.price = price;
    }

    setAmount(amount) {
        this.amount = amount;
    }

    setPlanStartDate(planStartDate) {
        this.planStartDate = planStartDate;
    }

    setPlanEndDate(planEndDate) {
        this.planEndDate = planEndDate;
    }

    setU8MoDetailId(u8MoDetailId) {
        this.u8MoDetailId = u8MoDetailId;
    }

    setRowNo(rowNo) {
        this.rowNo = rowNo;
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

/*
 * mes部件表类
 */
class omMesPart {

    constructor() {
        //
        this.id = "";
        //
        this.mainId = "";
        //
        this.detailId = "";
        //
        this.partInvCode = "";
        //
        this.partInvName = "";
        //
        this.partInvStd = "";
        //
        this.partInvUnit = "";
        //
        this.partQty = "";
        //
        this.rowNo = "";
    }

    setId(id) {
        this.id = id;
    }

    setMainId(mainId) {
        this.mainId = mainId;
    }

    setDetailId(detailId) {
        this.detailId = detailId;
    }

    setPartInvCode(partInvCode) {
        this.partInvCode = partInvCode;
    }

    setPartInvName(partInvName) {
        this.partInvName = partInvName;
    }

    setPartInvStd(partInvStd) {
        this.partInvStd = partInvStd;
    }

    setPartInvUnit(partInvUnit) {
        this.partInvUnit = partInvUnit;
    }

    setPartQty(partQty) {
        this.partQty = partQty;
    }

    setRowNo(rowNo) {
        this.rowNo = rowNo;
    }

    /*
     * 将u8数据的字段进行转换
     */
    setDataFromU8Data(data) {
        this.setPartInvCode(data.cinvcode);
        this.setPartInvName(data.cinvname);
        this.setPartInvStd(data.cinvstd);
        this.setPartInvUnit(data.ccomunitname);
    }



}


/*
 * 产品表和材料表entity
 */
class omMesProductMaterial{


    constructor() {
        //材料表的ID
        this.id = "";
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
        <!--DATE: 2022/9/4-->
        <!--mijiahao TODO:这踏马是什么东西？ -->
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
        this.planStartDate = "";
        //
        this.planEndDate = "";
        //
        this.detailId = "";
        //
        this.partId = "";
        //
        this.mainId = "";
        //
        this.invCode = "";
        //
        this.invName = "";
        //
        this.invStd = "";
        //
        this.invUnit = "";
        //
        this.qty = "";
        //
        this.unitMaterialPrice = "";
        //
        this.unitMaterialAmount = "";
        //
        this.invLand = "";
        //
        this.invWidth = "";
        //
        this.invLen = "";
        //
        this.invExternalDiameter = "";
        //
        this.invInternalDiameter = "";
        //
        this.invDensity = "";
        //
        this.invSize = "";
        //
        this.iqty = "";
        //
        this.tqty = "";
        //
        this.itype = "";
        //
        this.u8MoMaterialId = "";
        //
        this.rowNo = "";

    }


    
    /*
     * 将部件表行记录转换成材料表行记录
     */
    setDataFromPartData(data){
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




}



