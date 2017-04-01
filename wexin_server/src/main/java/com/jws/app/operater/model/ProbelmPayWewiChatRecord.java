package com.jws.app.operater.model;

import java.util.Date;

public class ProbelmPayWewiChatRecord {
    private Integer id;

    private String paymentId;

    private Integer amountMoney;

    private String paymentInfo;

    private Integer paymentStatus;

    private String spareField1;

    private String spareField2;

    private String spareField3;

    private String spareField4;

    private String spareField5;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId == null ? null : paymentId.trim();
    }

    public Integer getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(Integer amountMoney) {
        this.amountMoney = amountMoney;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo == null ? null : paymentInfo.trim();
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}