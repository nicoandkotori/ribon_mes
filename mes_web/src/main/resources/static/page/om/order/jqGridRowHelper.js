
/*
 * jqGrid表格行操作工具
 * @author mijiahao
 */
class jqGridRowHelper {

    /*
     * 传入table的DOM对象
     */
    constructor(table) {
        this.table = table;
    }

    /*
     * 设置对应的实体类
     */
    setEntity(entity){
        this.entity = entity;
    }

    /*
     * 获取所有行数据
     */
    getAllRowData(){
        return this.table.jqGrid("getRowData");
    }
    /*
     * 通过id获取某行数据
     */
    getRowDataById(rowId){
        return this.table.getRowData(rowId)
    }

    /*
     * 获取选中行ID
     */
    getSelectedRowId(){
        let selectedRowId = this.table.getGridParam("selrow");
        if (selectedRowId == null){
            layer.alert("请先选择行！");
            console.log("请先选择行！");
            return ;
        }
        return selectedRowId;
    }

    /*
     * 获取选中行数据
     */
    getSelectedRowData(){
        let selectedRowId = this.getSelectedRowId();
        return this.getRowDataById(selectedRowId)
    }

    /*
     * 获取所有行的ID
     */
    getAllRowsId(){
        return this.table.getDataIDs();
    }

    /*
     * 保存单元格
     */
    saveCell(row,col){
        this.table.jqGrid('saveCell', row, col);
    }

    /*
     * 添加行数据
     */
    addRowData(rowId,rowData){
        this.table.addRowData(rowId, rowData);
    }


    /*
     * 根据ID设置某行数据
     */
    setRowDataById(rowId,rowData){
        this.table.setRowData(rowId,rowData);
    }

    /*
     * 设置选中行数据
     */
    setSelectedRowData(rowData){
        this.setRowDataById(this.getSelectedRowId(),rowData)
    }

    /*
     * 删除选中行
     */
    deleteSelectedRow(){
       this.deleteRowById(this.getSelectedRowId()) ;
    }

    /*
     * 通过id删除行
     */
    deleteRowById(id){
        this.table.jqGrid("delRowData", id);
    }



}