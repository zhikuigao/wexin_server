package com.jws.app.operater.model;

import java.util.Date;

public class ProblemData {
    private Integer id;

    private String firstClassification;

    private String secondClassification;

    private String contentProblem;

    private Integer statusProblem;

    private String openid;

    private String mobilePhone;

    private Integer amountMoney;

    private Date createTime;

    private Date lastTime;

    private String spareField1;

    private String spareField2;

    private String spareField3;

    private String spareField4;

    private String spareField5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstClassification() {
        return firstClassification;
    }

    public void setFirstClassification(String firstClassification) {
        this.firstClassification = firstClassification == null ? null : firstClassification.trim();
    }

    public String getSecondClassification() {
        return secondClassification;
    }

    public void setSecondClassification(String secondClassification) {
        this.secondClassification = secondClassification == null ? null : secondClassification.trim();
    }

    public String getContentProblem() {
        return contentProblem;
    }

    public void setContentProblem(String contentProblem) {
        this.contentProblem = contentProblem == null ? null : contentProblem.trim();
    }

    public Integer getStatusProblem() {
        return statusProblem;
    }

    public void setStatusProblem(Integer statusProblem) {
        this.statusProblem = statusProblem;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public Integer getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(Integer amountMoney) {
        this.amountMoney = amountMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getSpareField1() {
        return spareField1;
    }

    public void setSpareField1(String spareField1) {
        this.spareField1 = spareField1 == null ? null : spareField1.trim();
    }

    public String getSpareField2() {
        return spareField2;
    }

    public void setSpareField2(String spareField2) {
        this.spareField2 = spareField2 == null ? null : spareField2.trim();
    }

    public String getSpareField3() {
        return spareField3;
    }

    public void setSpareField3(String spareField3) {
        this.spareField3 = spareField3 == null ? null : spareField3.trim();
    }

    public String getSpareField4() {
        return spareField4;
    }

    public void setSpareField4(String spareField4) {
        this.spareField4 = spareField4 == null ? null : spareField4.trim();
    }

    public String getSpareField5() {
        return spareField5;
    }

    public void setSpareField5(String spareField5) {
        this.spareField5 = spareField5 == null ? null : spareField5.trim();
    }
}