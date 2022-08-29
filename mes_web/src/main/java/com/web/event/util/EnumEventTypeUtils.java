package com.web.event.util;

public interface EnumEventTypeUtils {
    enum EVENT_TYPE {
        SALEORDER("SALEORDER", "SALEORDER");

        private String value;
        private String desc;

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        EVENT_TYPE(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    enum  OPER_TYPE{

        operType("ADD","新增");
        private String value;
        private String desc;


        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        OPER_TYPE(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
}
