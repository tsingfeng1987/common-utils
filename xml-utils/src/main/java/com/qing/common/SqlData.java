package com.qing.common;

/**
 * @author guoqf
 * @date 2019/12/24 19:32
 */
public class SqlData {
    private byte[] data;

    private boolean hasSingleLineComment;

    private boolean hasMultiLineComment;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isHasSingleLineComment() {
        return hasSingleLineComment;
    }

    public void setHasSingleLineComment(boolean hasSingleLineComment) {
        this.hasSingleLineComment = hasSingleLineComment;
    }

    public boolean isHasMultiLineComment() {
        return hasMultiLineComment;
    }

    public void setHasMultiLineComment(boolean hasMultiLineComment) {
        this.hasMultiLineComment = hasMultiLineComment;
    }
}
