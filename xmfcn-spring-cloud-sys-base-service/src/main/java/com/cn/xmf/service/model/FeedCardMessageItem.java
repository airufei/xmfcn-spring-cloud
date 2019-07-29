package com.cn.xmf.service.model;

/**
 * Created by dustin on 2017/3/19.
 */
public class FeedCardMessageItem {
    private String title;
    private String picUrl;
    private String messageUrl;

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}