package com.jws.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

public class UploadUtil{
	public static  String uploadFile(String  stream, String type, String userId, String  savePath, Date currentTime){
		OutputStream os = null;
		try {
			//设置保存名称  userId+当前时间（yyyyMMddHHmmss）+.+type(png等)
			String headName = "";
			StringBuffer headBuffer = new StringBuffer();
			headBuffer.append(userId);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			headBuffer.append(df.format(currentTime));
			headBuffer.append(".");
			headBuffer.append(type);
			headName = headBuffer.toString();
			//获取字节
			byte[] buffer =  stream.getBytes("ISO-8859-1");			
			File destFile = new File(savePath,headName);
            os = new FileOutputStream(destFile);
            os.write(buffer, 0, buffer.length);
             os.close();
             return headName;
		} catch (Exception e) {
			e.printStackTrace();
			try {
			     if(null != os){
			      os.close();
			     }
			} catch (Exception e1) {
					e1.printStackTrace();
			    }   
		}
		return "";
	}
	
	public static void main(String[] args) {
		String[]  array = {"123","456"};
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", array);
		System.out.println(jsonObject);
	}
	

	
}