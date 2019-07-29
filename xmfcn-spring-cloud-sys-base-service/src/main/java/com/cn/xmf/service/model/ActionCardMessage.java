package com.cn.xmf.service.model;

import com.alibaba.fastjson.JSON;
import com.cn.xmf.model.ding.MarkdownMessage;
import com.cn.xmf.model.ding.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dustin on 2017/3/17.
 */
public class ActionCardMessage implements Message {
    public static final int MAX_ACTION_BUTTON_CNT = 5;
    public static final int MIN_ACTION_BUTTON_CNT = 1;

    private String title;
    private String bannerUrl;
    private String briefTitle;
    private String briefText;
    private boolean hideAvatar;
    private ActionButtonStyle actionButtonStyle = ActionButtonStyle.VERTICAL;
    private List<ActionCardAction> actions = new ArrayList<ActionCardAction>();

    public boolean isHideAvatar() {
        return hideAvatar;
    }

    public void setHideAvatar(boolean hideAvatar) {
        this.hideAvatar = hideAvatar;
    }

    public String getBriefTitle() {
        return briefTitle;
    }

    public void setBriefTitle(String briefTitle) {
        this.briefTitle = briefTitle;
    }

    public ActionButtonStyle getActionButtonStyle() {
        return actionButtonStyle;
    }

    public void setActionButtonStyle(ActionButtonStyle actionButtonStyle) {
        this.actionButtonStyle = actionButtonStyle;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefText() {
        return briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }


    public void addAction(ActionCardAction action) {
        if (actions.size() >= MAX_ACTION_BUTTON_CNT) {
            throw new IllegalArgumentException("number of actions can't more than " + MAX_ACTION_BUTTON_CNT);
        }
        actions.add(action);
    }

    @Override
    public String toJsonString() {

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "actionCard");

        Map<String, Object> actionCardContent = new HashMap<String, Object>();
        actionCardContent.put("title", title);

        StringBuffer text = new StringBuffer();
        if (StringUtils.isNotBlank(bannerUrl)) {
            text.append(MarkdownMessage.getImageText(bannerUrl) + "\n");
        }
        if (StringUtils.isNotBlank(briefTitle)) {
            text.append(MarkdownMessage.getHeaderText(3, briefTitle) + "\n");
        }
        if (StringUtils.isNotBlank(briefText)) {
            text.append(briefText + "\n");
        }
        actionCardContent.put("text", text.toString());

        if (hideAvatar) {
            actionCardContent.put("hideAvatar", "1");
        }

        if (actions.size() < MIN_ACTION_BUTTON_CNT) {
            throw new IllegalArgumentException("number of actions can't less than " + MIN_ACTION_BUTTON_CNT);
        }
        actionCardContent.put("btns", actions);

        if (actions.size() == 2 && actionButtonStyle == ActionButtonStyle.HORIZONTAL) {
            actionCardContent.put("btnOrientation", "1");
        }

        items.put("actionCard", actionCardContent);

        return JSON.toJSONString(items);
    }
}
