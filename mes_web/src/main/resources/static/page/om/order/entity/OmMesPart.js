/*
 * mes部件表类,新增字段需要修改constructor,getter,setter,setEntity
 * @author mijiahao
 */
class OmMesPart {

    constructor(){
        this.id = "";
        this.mainId = "";
        this.recordId = "";
        this.partRowId = "";
        this.detailId = "";
        this.partInvCode = "";
        this.partInvName = "";
        this.partInvStd = "";
        this.partInvUnit = "";
        this.partQty = "";
        this.rowNo = "";
    }

    getId() {
        return this.id;
    }

    getPartRowId(){
        return this.partRowId;
    }


    setPartRowId(value) {
        this.partRowId = value;
    }

    getRecordId(){
        return this.recordId;
    }

    setRecordId(value){
        this.recordId = value;
    }

    //委外订单ID
    getMainId() {
        return this.mainId;
    }

    //产品表ID
    getDetailId() {
        return this.detailId;
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
    //产品表ID
    setDetailId( detailId) {
        this.detailId = detailId;
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
    setRowNo( rowNo) {
        this.rowNo = rowNo;
    }

    setEntity(data){
        this.setId(data.id);

        this.setRecordId(data.recordId);
        this.setPartRowId(data.partRowId);
        //委外订单ID
        this.setMainId(data.mainId);
        //产品表ID
        this.setDetailId(data.detailId);
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
        this.setRowNo(data.rowNo);
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