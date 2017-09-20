package com.rz.httpapi.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by rzw2 on 2017/8/14.
 */

@DatabaseTable(tableName = "tb_friend_information")
public class FriendInformationBean implements Serializable {
    /**
     * custId : yehao
     * custImg : http://img
     * custLevel : 0
     * custNname : yehao
     * custRole : 0
     * custSignature : hello
     * nameNotes : 123
     */
    @DatabaseField(id = true)
    private String custId;
    @DatabaseField
    private String custImg;
    @DatabaseField
    private int custLevel;
    @DatabaseField
    private String custNname;
    @DatabaseField
    private int custRole;
    @DatabaseField
    private String custSignature;
    @DatabaseField
    private String nameNotes;
    @DatabaseField
    private String cityCode;
    @DatabaseField
    private String custPhone;
    @DatabaseField
    private String custQr;
    @DatabaseField
    private String custSex;
    @DatabaseField
    private String firstLetter;
    @DatabaseField
    private int relation;

    private boolean select;
    private boolean disable;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustImg() {
        return custImg;
    }

    public void setCustImg(String custImg) {
        this.custImg = custImg;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public int getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(int custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustNname() {
        return custNname;
    }

    public void setCustNname(String custNname) {
        this.custNname = custNname;
    }

    public int getCustRole() {
        return custRole;
    }

    public void setCustRole(int custRole) {
        this.custRole = custRole;
    }

    public String getCustSignature() {
        return custSignature;
    }

    public void setCustSignature(String custSignature) {
        this.custSignature = custSignature;
    }

    public String getNameNotes() {
        return nameNotes;
    }

    public void setNameNotes(String nameNotes) {
        this.nameNotes = nameNotes;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustQr() {
        return custQr;
    }

    public void setCustQr(String custQr) {
        this.custQr = custQr;
    }

    public String getCustSex() {
        return custSex;
    }

    public void setCustSex(String custSex) {
        this.custSex = custSex;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
