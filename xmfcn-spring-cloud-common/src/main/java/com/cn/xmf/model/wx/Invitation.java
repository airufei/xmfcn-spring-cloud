package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 邀请函Entity
 *
 * @author rufei.cn
 * @version 2019-11-08
 */
public class Invitation extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 男方农历
     */
    private String manOldTime;

    /**
     * 男方新历
     */
    private String manNewTime;

    /**
     * 地址
     */
    private String manAdress;

    /**
     * 酒店
     */
    private String manHostel;

    /**
     * 新郎
     */
    private String bridegRoom;

    /**
     * 新娘
     */
    private String bride;

    /**
     * 女方新历时间
     */
    private String womanNewTime;

    /**
     * 女方婚礼地点
     */
    private String womanAdress;

    /**
     * woman_hostel
     */
    private String womanHostel;

    /**
     * 女方农历
     */
    private String womanOldTime;


    public Invitation() {

    }


    public String getManOldTime() {
        return manOldTime;
    }

    public void setManOldTime(String manOldTime) {
        this.manOldTime = manOldTime;
    }


    public String getManNewTime() {
        return manNewTime;
    }

    public void setManNewTime(String manNewTime) {
        this.manNewTime = manNewTime;
    }


    public String getManAdress() {
        return manAdress;
    }

    public void setManAdress(String manAdress) {
        this.manAdress = manAdress;
    }


    public String getManHostel() {
        return manHostel;
    }

    public void setManHostel(String manHostel) {
        this.manHostel = manHostel;
    }


    public String getBridegRoom() {
        return bridegRoom;
    }

    public void setBridegRoom(String bridegRoom) {
        this.bridegRoom = bridegRoom;
    }


    public String getBride() {
        return bride;
    }

    public void setBride(String bride) {
        this.bride = bride;
    }


    public String getWomanNewTime() {
        return womanNewTime;
    }

    public void setWomanNewTime(String womanNewTime) {
        this.womanNewTime = womanNewTime;
    }


    public String getWomanAdress() {
        return womanAdress;
    }

    public void setWomanAdress(String womanAdress) {
        this.womanAdress = womanAdress;
    }


    public String getWomanHostel() {
        return womanHostel;
    }

    public void setWomanHostel(String womanHostel) {
        this.womanHostel = womanHostel;
    }


    public String getWomanOldTime() {
        return womanOldTime;
    }

    public void setWomanOldTime(String womanOldTime) {
        this.womanOldTime = womanOldTime;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("manOldTime", manOldTime)
                .append("manNewTime", manNewTime)
                .append("manAdress", manAdress)
                .append("manHostel", manHostel)
                .append("bridegRoom", bridegRoom)
                .append("bride", bride)
                .append("womanNewTime", womanNewTime)
                .append("womanAdress", womanAdress)
                .append("womanHostel", womanHostel)
                .append("womanOldTime", womanOldTime)
                .toString();
    }
}