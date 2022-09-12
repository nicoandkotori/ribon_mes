/*
 * 将u8物料字段转换成材料表字段
 */
function setMaterialInfoFromU8Data(material,u8Data){
    material.invName = u8Data.cinvname;
    material.invCode = u8Data.cinvcode;
    material.invStd = u8Data.cinvstd;
    material.invUnit = u8Data.ccomunitname;
    return material;
}

/*
 * 将部件表行记录转换成材料表行记录
 */
function setMaterialTableRowFromPartTableRow(material,part){

}

/*
 * 将u8物料字段转换成部件表字段
 */
function setPartInfoFromU8Data(part,u8Data){
    part.partInvName = u8Data.cinvname;
    part.partInvCode = u8Data.cinvcode;
    part.partInvStd = u8Data.cinvstd;
    part.partInvUnit = u8Data.ccomunitname;
    return part;
}

/*
 * 若入参不为空，则在小数点后保留两位小数，否则为0
 */
function initDecimal(num){
    let result = 0;
    if (num != "" && num != null) {
        result = parseFloat(num - 0).toFixed(2);
    }
    return result;
}

