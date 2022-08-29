package com.web.system.dto;

import com.common.BaseMulDataModel;

/**
 * Created by Yao on 2019/4/29.
 */
public class MenuPdaVM extends BaseMulDataModel {
    private String pdaMenuName;
    private String pdaMenuUrl;
    private String pdaMenuIcon;

    public String getPdaMenuName() {
        return pdaMenuName;
    }

    public void setPdaMenuName(String pdaMenuName) {
        this.pdaMenuName = pdaMenuName;
    }

    public String getPdaMenuUrl() {
        return pdaMenuUrl;
    }

    public void setPdaMenuUrl(String pdaMenuUrl) {
        this.pdaMenuUrl = pdaMenuUrl;
    }

    public String getPdaMenuIcon() {
        return pdaMenuIcon;
    }

    public void setPdaMenuIcon(String pdaMenuIcon) {
        this.pdaMenuIcon = pdaMenuIcon;
    }
}
