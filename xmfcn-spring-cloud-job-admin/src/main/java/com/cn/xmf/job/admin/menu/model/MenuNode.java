package com.cn.xmf.job.admin.menu.model;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.List;

/**
* 菜单树
* @author rufei.cn
* @version 2018-10-10
*/
public class MenuNode implements Serializable {

    private String  text;//菜单名称
    private String  icon;//菜单图标
    private String  selectedIcon;//当某个节点被选择后显示的图标
    private String  href;//菜单Url
    private boolean  selectable;//节点是否可选
    private JSONObject state;//一个节点的初始状态。 state: {checked:true,disabled:true,expanded:true,selected:true}
    private String  color;//节点的前景色
    private String  backColor;//节点的背景色
    private String  tags;//节点的背景色
    private Long  nodeid;//节点ID

    private List<MenuNode> nodes;//子节点数据

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public JSONObject getState() {
        return state;
    }

    public void setState(JSONObject state) {
        this.state = state;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    public Long getNodeid() {
        return nodeid;
    }

    public void setNodeid(Long nodeid) {
        this.nodeid = nodeid;
    }

    public List<MenuNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<MenuNode> nodes) {
        this.nodes = nodes;
    }
}