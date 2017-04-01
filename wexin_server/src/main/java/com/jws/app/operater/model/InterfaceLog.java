package com.jws.app.operater.model;

import java.util.Date;

public class InterfaceLog {
    private String id;

    private String source;

    private String inputobject;

    private Date createdTime;

    private Date updateTime;

    private String returnobject;


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getInputobject() {
        return inputobject;
    }

    public void setInputobject(String inputobject) {
        this.inputobject = inputobject == null ? null : inputobject.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getReturnobject() {
        return returnobject;
    }

    public void setReturnobject(String returnobject) {
        this.returnobject = returnobject == null ? null : returnobject.trim();
    }
}