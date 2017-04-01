package com.jws.app.operater.model;

import java.util.Date;

public class ProblemShareRecord {
    private Integer id;

    private Integer probelemid;

    private String openid;

    private String openidBy;

    private Date createTime;

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

    public Integer getProbelemid() {
        return probelemid;
    }

    public void setProbelemid(Integer probelemid) {
        this.probelemid = probelemid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getOpenidBy() {
        return openidBy;
    }

    public void setOpenidBy(String openidBy) {
        this.openidBy = openidBy == null ? null : openidBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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