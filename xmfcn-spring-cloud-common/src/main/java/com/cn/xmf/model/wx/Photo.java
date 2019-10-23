package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Photo extends BaseEntitys {
    private static final long serialVersionUID = 1L;
    /**
     * // 图片名称
     */
    private String name;

    /**
     * // 图片类型
     */
    private String type;

    /**
     * // 图片地址
     */
    private String url;

    /**
     * // 图片描述
     */
    private String description;

    /**
     * // 存储地址
     */
    private String path;


    /**
     * 点赞数
     */
    private long likeCount;

    /**
     * 评论数
     */
    private long commentCount;


    public Photo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("type", type)
                .append("url", url)
                .append("description", description)
                .append("path", path)
                .append("likeCount", likeCount)
                .append("commentCount", commentCount)
                .toString();
    }
}
