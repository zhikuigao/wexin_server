package com.jws.app.operater.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.PersionPhoteAuthServer;
import com.jws.common.constant.Constants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.ResultPackaging;

@Service("persionPhoteAuthServerImpl")
public class PersionPhoteAuthServerImpl implements PersionPhoteAuthServer {
	@Resource
	private UserInfoMapper userInfoMapper;
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public JSONObject uploadPhoto(HttpServletRequest reques) {
		JSONObject returnObject = new JSONObject();
		String path =  Constants.imageUrl;
		File repositoryFile = new File(path);
		if (!repositoryFile.exists()) {repositoryFile.mkdirs();}
		FileItemFactory factory = new DiskFileItemFactory(1024 * 32,repositoryFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		upload.setSizeMax(1024 * 1024 * 500);
		List<?> fileItr = null;
		try {
			 fileItr = upload.parseRequest(reques);
				// 讲非文件值放在map中
				HashMap<String, String> map = new HashMap<>();
				Iterator<?> iter = fileItr.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						map.put(item.getFieldName(), item.getString());
					}
				}
				// 验证参数
				if (map == null || !map.containsKey("openid")) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}
				String openid  = map.get("openid");
				//处理文件
				boolean isTrue = true;
				String filename = "";
				Iterator<?> iter1 = fileItr.iterator();
				while (iter1.hasNext()) {
					FileItem f = (FileItem) iter1.next();
					if (!f.isFormField()) {
				//详情 name=abc.png,  size=376507 bytes, 	isFormField=false, FieldName=picture1
						filename = f.getName() == null ? "" : f.getName();
						filename = JiveGlobe.getFromRom()+".jpg";
						File saveFile = new File(path, filename);
						if (saveFile.exists()) {
							isTrue = false;break;}
							f.write(saveFile);
						} 
			}
			if(JiveGlobe.isEmpty(filename)){
				returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
				return returnObject;			
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
			//保存文件信息
			UserInfo rd = new UserInfo();
			rd.setSpareField1("1");
			rd.setSpareField3(filename);
			rd.setOpenId(openid);
			userInfoMapper.updateByPrimaryKey(rd);
		} catch (Exception e) {
			System.out.println(e);
			logger.error("###认证失败"+e);
		}
		return returnObject;
	}
	
	
}
