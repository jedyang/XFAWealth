package com.fsll.wmes.chat.pojo.result;

/**
 * Created by mjhuang
 */
public class UploadFileResult extends  UploadImgResult {
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private String name;
}
