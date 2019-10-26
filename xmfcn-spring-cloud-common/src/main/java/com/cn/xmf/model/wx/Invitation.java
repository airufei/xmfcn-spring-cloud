package com.cn.xmf.model.wx;

import com.cn.xmf.base.model.BaseEntitys;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 邀请函Entity
 *
 * @author rufei.cn
 * @version 2019-10-26
 */
public class Invitation extends BaseEntitys {

    private static final long serialVersionUID = 1L;
    /**
     * 农历
     */
    private String oldTime;

    /**
     * 新历
     */
    private String newTime;

    /**
     * 地址
     */
    private String adress;

    /**
     * 酒店
     */
    private String hostel;

    /**
     * 新郎
     */
    private String bridegRoom;

    /**
     * 新娘
     */
    private String bride;


    public Invitation() {

    }


    public String getOldTime() {
        return oldTime;
    }

    public void setOldTime(String oldTime) {
        this.oldTime = oldTime;
    }


    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }


    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }


    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("oldTime", oldTime)
                .append("newTime", newTime)
                .append("adress", adress)
                .append("hostel", hostel)
                .append("bridegRoom", bridegRoom)
                .append("bride", bride)
                .toString();
    }
}