package com.jws.app.operater.service;

import net.sf.json.JSONObject;

public interface ProblemServiceControl {
			public JSONObject queryProData();
			public JSONObject addProblem(JSONObject json);
			public JSONObject queryProblemlist(JSONObject josn);
			public JSONObject solvProblemRecord(JSONObject josn);
			public JSONObject updateProStatus(JSONObject json);
			public JSONObject updateSolvProStatus(JSONObject json);
			public JSONObject addShareRecord(JSONObject json);
			public JSONObject generateShareImage(JSONObject json);
			public JSONObject queryProblemDesc(JSONObject json);
			public JSONObject queryProblemKillRecord(JSONObject json);
			public JSONObject problemAdopt(JSONObject json);
			public JSONObject problemApplypay(JSONObject json);
			public JSONObject verificationProblem(JSONObject json);
			public JSONObject queryRespondentMoney(JSONObject json);
			
			
}
