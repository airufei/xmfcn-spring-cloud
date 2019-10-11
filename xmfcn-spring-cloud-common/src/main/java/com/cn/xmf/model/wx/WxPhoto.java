package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WxPhoto extends BaseEntitys {
    private static final long serialVersionUID = 1L;
    /**
     * // 图片名称
     */
    private String name;

    /**
     * // 图片类型
     */
    private Integer type;

    /**
     * // 图片地址
     */
    private String url;

    /**
     * // 图片描述
     */
    private String description;


    public WxPhoto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("type", type)
                .append("url", url)
                .append("description", description)
                .toString();
    }
}
