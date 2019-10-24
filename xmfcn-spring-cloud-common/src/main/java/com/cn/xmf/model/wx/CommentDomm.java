package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import com.cn.xmf.util.ImageUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 弹幕
 */
public class CommentDomm extends BaseEntitys {
    /**
     * 距离顶部的距离
     */
    private int top=RandomUtils.nextInt(30, 60);

    /**
     * 弹幕距离时间
     */
    private int time=RandomUtils.nextInt(5, 20);

    /**
     * 弹幕字体颜色
     */
    private String color= ImageUtil.getRandColorCode();


    public double getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("top", top)
                .append("time", time)
                .append("color", color)
                .toString();
    }
}
