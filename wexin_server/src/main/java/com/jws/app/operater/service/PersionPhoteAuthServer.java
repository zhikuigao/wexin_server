package com.jws.app.operater.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

public interface PersionPhoteAuthServer {
			public JSONObject uploadPhoto(HttpServletRequest reques);
}
