package com.common.util;


/**
 * @author tao
 * @date 2019/5/31 15:42:09
 */
public interface EnumUtil {

    /**
     * 流转卡生产流转状态
     */
    enum SHIFT_STATUS {
        /*** 货架转道 */
        shelfTrans("-1", "货架转道"),
        /*** 车间上架 */
        upShelf("0", "车间上架"),
        /*** 上料 */
        upMaterial("1", "上料"),
        /*** 完工 */
        report("2", "完工"),
        /*** 延时完工 */
        delayedReport("3", "延时完工"),
        /*** 中途下料 */
        downMaterial("4", "中途下料"),
        /*** 暂停 */
        suspend("5", "暂停"),
        /*** 继续作业 */
        continueReport("6", "继续作业"),
        /*** 无工时完工 */
        noManHourReport("7", "无工时完工"),
        /*** 交班 */
        handOverReport("8", "交班"),
        /*** 接班 */
        takeOverReport("9", "接班"),
        /*** 委外发货 */
        dispatch("10", "委外发货"),
        /*** 委外收货 */
        receipt("11", "委外收货");

        private final String value;
        private final String desc;

        SHIFT_STATUS(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDesc(String code) {
            for (SHIFT_STATUS ele : values()) {
                if(ele.getValue().equals(code)){
                    return ele.getDesc();
                }
            }
            return null;
        }
    }

    //吊号的使用状态
    enum HANGER_STATUS {
        ONLINE("1", "上线中"), OFFLINE("0", "未上线");
        private String value;
        private String desc;

        HANGER_STATUS(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

    }

    //角色类型
    enum ROLE_TYPE {
        PC("PC", "电脑端"), PAD("PAD", "PAD端");
        private String value;
        private String desc;

        ROLE_TYPE(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

    }

    //单据状态
    enum puchaseApply {
        ADD("新增", "新增"),
        COMMIT("提交", "提交"),
        CHECK("审核", "审核"),
        REVIEW("复核", "复核");
        private String value;
        private String desc;

        puchaseApply(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    //单据名称
    enum vouchName {
        PUVOUCH("采购订单", "采购订单"),
        DYVOUCH("送货单", "送货单"),
        AGVOUCH("到货单", "到货单"),
        MAVOUCH("领料申请单", "领料申请单"),
        TAVOUCH("调拨申请单", "调拨申请单");
        private String value;
        private String desc;

        vouchName(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    //条码状态
    enum barStatus {
        ADD("新增", "新增"),
        COMMIT("提交", "提交"),
        CHECK("审核", "审核"),
        REVIEW("复核", "复核"),
        INPUT("入库", "入库"),
        OUTPUT("出库", "出库"),
        ASMB("装配", "装配");
        private String value;
        private String desc;

        barStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    //账套
    enum u8Account {
        JIANJI("建机", "建机"),
        ZHONGGONG("重工", "重工");
        private String value;
        private String desc;

        u8Account(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    //业务类型
    enum BusinessType {
        LLCK("领料", "领料"),
        TRANS("调拨", "调拨"),
        QTCK("其他出库", "其他出库"),
        CGRK("普通采购", "普通采购"),
        XSCK("销售出库", "销售出库"),
        QTRK("其他入库", "其他入库"),
        CPRK("成品入库", "成品入库");
        private String value;
        private String desc;

        BusinessType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    enum InputType {
        PU("采购入库", "采购入库"),
        PD("生产入库", "生产入库"),
        OTHER("其他入库", "其他入库"),
        CPRK("成品入库", "成品入库");
        private String value;
        private String desc;

        InputType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    enum OutputType {
        QTCK("其他出库", "其他出库"),
        SCLY("生产领用", "生产领用");
        private String value;
        private String desc;

        OutputType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    //生产订单类别
    enum moType {
        Host("主机", "主机"),
        HostJJ("整机", "整机"),
        NonStandard("非标", "非标"),
        Repair("维修", "维修"),
        Tooling("工装", "工装"),
        SparePart("备件", "备件"),
        Part("部件", "部件"),
        Rop("rop", "rop");
        private String value;
        private String desc;

        moType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 生产订单明细表来源
     */
    enum moDetailSource {
        BOM("BOM", "BOM"),
        Hand("手工 ", "手工 "),
        Import("导入", "导入"),
        OA("OA", "OA");
        private String value;
        private String desc;

        moDetailSource(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 生产订单清单类型
     */
    enum listType {
        X("下料", "下料"),
        M("铆焊", "铆焊"),
        J("金工", "金工"),
        T("涂装", "涂装"),
        Z("装配", "装配"),
        F("发货", "发货"),
        D("电工", "电工");
        private String value;
        private String desc;

        listType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 质检分类
     */
    enum inspectClass {
        Material("原材料", "原材料");
        private String value;
        private String desc;

        inspectClass(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 质检状态
     */
    enum inspectStatus {
        Qualified("合格", "合格"),
        UnQualified("不合格", "不合格");
        private String value;
        private String desc;

        inspectStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 库存类型
     */
    enum stockType {
        Begin("期初", "期初"),
        Inv("套料", "套料"),
        Over("超报结转", "超报结转");
        private String value;
        private String desc;

        stockType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }


    /**
     * 打印机： 打印业务类型
     */
    enum printBiz {
        SHIFT("派工", "派工"),
        BOOK("报工", "报工标签");
        private String value;
        private String desc;

        printBiz(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 是否
     */
    enum yesOrNo {
        Yes("是", "是"),
        No("否", "否");
        private String value;
        private String desc;

        yesOrNo(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 浮动方式
     */
    enum floatMode {
        upFloat("上浮", "上浮"),
        downFloat("下浮", "下浮");
        private String value;
        private String desc;

        floatMode(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }


    /**
     * rop类型
     */
    enum ropType {
        ropType1("补足最高库存", "补足最高库存"),
        ropType2("按日均用量", "按日均用量"),
        ropType3("按经济批量", "按经济批量");
        private String value;
        private String desc;

        ropType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    /*** 申购状态*/
    enum APPLY_STATUS {
        /*** 关闭 */
        CLOSE("关闭", 0),
        /*** 保存 */
        SAVE("保存", 1),
        /*** 审核 */
        CHECK("审核", 2);
        
        private final String desc;
        private final Integer value;

        APPLY_STATUS(String desc, Integer value) {
            this.value = value;
            this.desc = desc;
        }

        public Integer getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }
    /*** 审核状态*/
    //状态
    enum STATUS {
        PENDING("待审核", 0), PASSED("审核通过", 1), CLOSE("暂停", 2);
        private String desc;
        private Integer value;

        STATUS(String desc, Integer value) {
            this.value = value;
            this.desc = desc;
        }

        public Integer getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }
}
