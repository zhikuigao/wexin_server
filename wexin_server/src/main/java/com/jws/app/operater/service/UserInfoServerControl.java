package com.jws.app.operater.service;

import net.sf.json.JSONObject;

public interface UserInfoServerControl {
			public JSONObject updatePersonInfo(JSONObject json);
			public JSONObject queryPersonInfo(JSONObject json);
			public JSONObject queryProblemInfo(JSONObject json);
			public JSONObject suggestCollect(JSONObject json);
}
