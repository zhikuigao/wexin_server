package com.jws.common.util;

import net.sf.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jws.common.constant.ConstantZh;

/**
 * 封装公共的返回函数
 * @author lx
 *
 */
public class ResultPackaging {

	/**
	 * 封装失败的返回函数
	 * @param code 调用结果 0 失败 1成功5
	 * @param desc  失败原因
	 * @return
	 */
	public static final  JSONObject dealJsonObject(int resultCode, int code,  Object object){ 
		JSONObject returnObject= new JSONObject();
		JSONObject returnCode= new  JSONObject();
		returnCode.put("code", resultCode);
		returnCode.put("desc", ConstantZh.map.get(code));
		returnObject.put("result", returnCode);
		if (null != object) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String gsont = gson.toJson(object).toString();	
			returnObject.put("resultDesc", gsont);
		}
		return returnObject ;
	}


	public static final  JSONObject dealJsonObjectString(int resultCode, int code,  String object){ 
		JSONObject returnObject= new JSONObject();
		JSONObject returnCode= new  JSONObject();
		returnCode.put("code", resultCode);
		returnCode.put("desc", ConstantZh.map.get(code));
		returnObject.put("result", returnCode);
		if (null != object) {
//			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//			String gsont = gson.toJson()
			returnObject.put("resultDesc", object);
		}
		return returnObject ;
	}


	
	/**
	 * 封装返回函数
	 * @param resultCode 返回码
	 * @param code  错误描述映射码
	 * @param language  返回信息语种
	 * @param   object  返回数据
	 * @return
	 */
	public static final  JSONObject dealSuccessJsonObject(int resultCode, int code,  Object object){ 
		JSONObject returnObject= new JSONObject();
		JSONObject returnCode= new  JSONObject();
		returnCode.put("code", resultCode);
		returnCode.put("desc", ConstantZh.map.get(code));
		returnObject.put("resultDesc", returnCode);
		if (null != object) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String gsont = gson.toJson(object).toString();	
			returnObject.put("result", gsont);
		}
		return returnObject ;
	}
    
	
	/**
	 * 封装返回函数
	 * @param resultCode 返回码
	 * @param code  错误描述映射码
	 * @param language  返回信息语种
	 * @param   object  返回数据
	 * @return
	 */
	public static final  JSONObject dealJsonObject2(int resultCode, int code,  Object object ,String language){ 
		JSONObject returnObject= new JSONObject();
		JSONObject returnCode= new  JSONObject();
		returnCode.put("code", resultCode);
		returnCode.put("desc", ConstantZh.map.get(code));
		returnObject.put("resultDesc", returnCode);
		if (null != object) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String gsont = gson.toJson(object).toString();	
			returnObject.put("result", gsont);
		}
		return returnObject ;
	}
}
