package com.web.common.util;

import java.io.File;

/**
 * Created by CaiHuan on 2019/7/16.
 */
public class CodeModel {
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * @param contents
     *            the contents to set
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the character_set
     */
    public String getCharacter_set() {
        return character_set;
    }

    /**
     * @param character_set
     *            the character_set to set
     */
    public void setCharacter_set(String character_set) {
        this.character_set = character_set;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize
     *            the fontSize to set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the logoFile
     */
    public File getLogoFile() {
        return logoFile;
    }

    /**
     * @param logoFile
     *            the logoFile to set
     */
    public void setLogoFile(File logoFile) {
        this.logoFile = logoFile;
    }

    /**
     * @return the logoRatio
     */
    public float getLogoRatio() {
        return logoRatio;
    }

    /**
     * @param logoRatio
     *            the logoRatio to set
     */
    public void setLogoRatio(float logoRatio) {
        this.logoRatio = logoRatio;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the whiteWidth
     */
    public int getWhiteWidth() {
        return whiteWidth;
    }

    /**
     * @param whiteWidth
     *            the whiteWidth to set
     */
    public void setWhiteWidth(int whiteWidth) {
        this.whiteWidth = whiteWidth;
    }

    /**
     * @return the bottomStart
     */
    public int[] getBottomStart() {
        return bottomStart;
    }

    /**
     * @param bottomStart
     *            the bottomStart to set
     */
    public void setBottomStart(int[] bottomStart) {
        this.bottomStart = bottomStart;
    }

    /**
     * @return the bottomEnd
     */
    public int[] getBottomEnd() {
        return bottomEnd;
    }

    /**
     * @param bottomEnd
     *            the bottomEnd to set
     */
    public void setBottomEnd(int[] bottomEnd) {
        this.bottomEnd = bottomEnd;
    }

    /**
     * ??????
     */
    private String contents;
    /**
     * ???????????????
     */
    private int width = 400;
    /**
     * ???????????????
     */
    private int height = 400;
    /**
     * ????????????
     */
    private String format = "png";
    /**
     * ????????????
     */
    private String character_set = "utf-8";
    /**
     * ????????????
     */
    private int fontSize = 12;
    /**
     * logo
     */
    private File logoFile;
    /**
     * logo?????????????????????
     */
    private float logoRatio = 0.20f;
    /**
     * ??????????????????
     */
    private String desc;
    /**
     * ????????????
     */
    private String date;
    private int whiteWidth;//???????????????
    private int[] bottomStart;//?????????????????????????????????
    private int[] bottomEnd;//?????????????????????????????????
}
