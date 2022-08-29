package com.util;

public interface EnumUtils {
    //状态
    enum StatusId{
        ADD("未审核","未审核"), CHECK("已审核", "已审核"), CLOSE("作废", "作废");

        private String value;
        private String desc;

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        StatusId(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

    }

}