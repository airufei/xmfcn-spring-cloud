package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 奖品信息Entity
 *
 * @author rufei.cn
 * @version 2020-01-02
 */
public class UserPrize extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 类型
     */
    private String type;

    /**
     * 奖品名称
     */
    private String name;

    /**
     * 图片
     */
    private String imgUrl;


    public UserPrize() {

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("name", name)
                .append("imgUrl", imgUrl)
                .toString();
    }
}