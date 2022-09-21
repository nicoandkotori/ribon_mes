/*
 * 主表entity
 */
class OmMesMain {
    constructor(){
        this.id = "";
        this.vouchCode = "";
        this.vouchDate = "";
        this.contractOm = "";
        this.contractSale = "";
        this.venCode = "";
        this.venName = "";
        this.depCode = "";
        this.depName = "";
        this.personCode = "";
        this.personName = "";
        this.remark = "";
        this.transportWay = "";
        this.createUser = "";
        this.createDate = "";
        this.updateUser = "";
        this.updateDate = "";
        this.izDelete = "";
        this.deleteUser = "";
        this.deleteDate = "";
        this.checkUser = "";
        this.checkDate = "";
        this.u8Id = "";
        this.statusId = "";
    }

    getId() {
        return this.id;
    }

    //订单编号
    getVouchCode() {
        return this.vouchCode;
    }

    //订单日期
    getVouchDate() {
        return this.vouchDate;
    }

    //委外合同
    getContractOm() {
        return this.contractOm;
    }

    //销售合同
    getContractSale() {
        return this.contractSale;
    }

    //供应商编码
    getVenCode() {
        return this.venCode;
    }

    //供应商名称
    getVenName() {
        return this.venName;
    }

    //部门编码
    getDepCode() {
        return this.depCode;
    }

    //部门名称
    getDepName() {
        return this.depName;
    }

    //业务员编码
    getPersonCode() {
        return this.personCode;
    }

    //业务员姓名
    getPersonName() {
        return this.personName;
    }

    //备注
    getRemark() {
        return this.remark;
    }

    //运输方式
    getTransportWay() {
        return this.transportWay;
    }

    getCreateUser() {
        return this.createUser;
    }

    getCreateDate() {
        return this.createDate;
    }

    getUpdateUser() {
        return this.updateUser;
    }

    getUpdateDate() {
        return this.updateDate;
    }

    getIzDelete() {
        return this.izDelete;
    }

    getDeleteUser() {
        return this.deleteUser;
    }

    getDeleteDate() {
        return this.deleteDate;
    }

    getCheckUser() {
        return this.checkUser;
    }

    getCheckDate() {
        return this.checkDate;
    }

    getU8Id() {
        return this.u8Id;
    }

    getStatusId() {
        return this.statusId;
    }


    setId( id) {
        this.id = id;
        return this;
    }
    //订单编号
    setVouchCode( vouchCode) {
        this.vouchCode = vouchCode;
        return this;
    }
    //订单日期
    setVouchDate( vouchDate) {
        this.vouchDate = vouchDate;
        return this;
    }
    //委外合同
    setContractOm( contractOm) {
        this.contractOm = contractOm;
        return this;
    }
    //销售合同
    setContractSale( contractSale) {
        this.contractSale = contractSale;
        return this;
    }
    //供应商编码
    setVenCode( venCode) {
        this.venCode = venCode;
        return this;
    }
    //供应商名称
    setVenName( venName) {
        this.venName = venName;
        return this;
    }
    //部门编码
    setDepCode( depCode) {
        this.depCode = depCode;
        return this;
    }
    //部门名称
    setDepName( depName) {
        this.depName = depName;
        return this;
    }
    //业务员编码
    setPersonCode( personCode) {
        this.personCode = personCode;
        return this;
    }
    //业务员姓名
    setPersonName( personName) {
        this.personName = personName;
        return this;
    }
    //备注
    setRemark( remark) {
        this.remark = remark;
        return this;
    }
    //运输方式
    setTransportWay( transportWay) {
        this.transportWay = transportWay;
        return this;
    }
    setCreateUser( createUser) {
        this.createUser = createUser;
        return this;
    }
    setCreateDate( createDate) {
        this.createDate = createDate;
        return this;
    }
    setUpdateUser( updateUser) {
        this.updateUser = updateUser;
        return this;
    }
    setUpdateDate( updateDate) {
        this.updateDate = updateDate;
        return this;
    }
    setIzDelete( izDelete) {
        this.izDelete = izDelete;
        return this;
    }
    setDeleteUser( deleteUser) {
        this.deleteUser = deleteUser;
        return this;
    }
    setDeleteDate( deleteDate) {
        this.deleteDate = deleteDate;
        return this;
    }
    setCheckUser( checkUser) {
        this.checkUser = checkUser;
        return this;
    }
    setCheckDate( checkDate) {
        this.checkDate = checkDate;
        return this;
    }
    setU8Id( u8Id) {
        this.u8Id = u8Id;
        return this;
    }
    setStatusId( statusId) {
        this.statusId = statusId;
        return this;
    }

    setEntity(data){
        this.setId(data.id);
        //订单编号
        this.setVouchCode(data.vouchCode);
        //订单日期
        this.setVouchDate(data.vouchDate);
        //委外合同
        this.setContractOm(data.contractOm);
        //销售合同
        this.setContractSale(data.contractSale);
        //供应商编码
        this.setVenCode(data.venCode);
        //供应商名称
        this.setVenName(data.venName);
        //部门编码
        this.setDepCode(data.depCode);
        //部门名称
        this.setDepName(data.depName);
        //业务员编码
        this.setPersonCode(data.personCode);
        //业务员姓名
        this.setPersonName(data.personName);
        //备注
        this.setRemark(data.remark);
        //运输方式
        this.setTransportWay(data.transportWay);
        this.setCreateUser(data.createUser);
        this.setCreateDate(data.createDate);
        this.setUpdateUser(data.updateUser);
        this.setUpdateDate(data.updateDate);
        this.setIzDelete(data.izDelete);
        this.setDeleteUser(data.deleteUser);
        this.setDeleteDate(data.deleteDate);
        this.setCheckUser(data.checkUser);
        this.setCheckDate(data.checkDate);
        this.setU8Id(data.u8Id);
        this.setStatusId(data.statusId);
    }
}