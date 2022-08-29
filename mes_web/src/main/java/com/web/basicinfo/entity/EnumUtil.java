package com.web.basicinfo.entity;

/**
 * @author tao
 * @date 2019/7/16 17:18:06
 */
public interface EnumUtil {

    //业务类型
    enum BUSINESS_TYPE{
        INPUT_ADD("新增入库单","新增入库单"),INPUT_EDIT("修改入库单","修改入库单"),
        INPUT_DEL("删除入库单","删除入库单"),
        OUTPUT_ADD("新增出库单","新增出库单"),OUTPUT_EDIT("修改出库单","修改出库单"),
        OUTPUT_DE("删除出库单","删除出库单"),
        MATCH_ADD("新增配货单","新增配货单"),MATCH_EDIT("修改配货单","修改配货单"),
        MATCH_DEL("删除配货单","删除配货单");
        private final String desc;
        private final String value;

        BUSINESS_TYPE(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }
}
