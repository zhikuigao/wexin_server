package com.jws.app.operater.control;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.gzk.weixin.service.impl.addChatMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.IInterfaceLogService;
import com.jws.common.constant.Constants;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;

@Controller("viewControlUser")
public class WechatRuquestUserInfoControl {
	@Resource
	private IndusReportMapper  indusReportMapper;
	// 日志
	@Resource(name = "interfaceLogService")
	private IInterfaceLogService interfaceLogService;
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private UserInfoMapper userInfoMapper;

	
	/**
	 * 统一入口
	 * 
	 * @param paramObject
	 *            json格式
	 * @return
	 */
	@RequestMapping(value = "/wecahtUserControl/interfaceEntry.do")
	public String  interfaceEntry(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String code = request.getParameter("code");
			String response = addChatMenu.getInstance().pullToken(code);
			String state = request.getParameter("state");
			//logger.info("###打印回调参数"+state);
		//	String response ="{\"access_token\":\"ceshi\",\"openid\":\"ceshi1232432432\"}";
			JSONObject json = JSONObject.fromObject(response);
		//	logger.info("###回调信息打印"+json.toString());
			if(!JiveGlobe.isEmpty(json) && json.has("access_token") && json.has("openid")){
				String access_token = json.optString("access_token");
				String access_tokenFlush = json.optString("refresh_token ");
				EHCacheConfig.put("access_tokenFlush", access_tokenFlush);
				String openid = json.optString("openid");
				String session = openid+JiveGlobe.getFromRom();
			//	redirectAttributes.addFlashAttribute("openid",openid);
				UserInfo userExits = userInfoMapper.queryPersonInfo(openid);
				//查询用户是否在系统里面,若用户存在直接跳转首页
				if(!JiveGlobe.isEmpty(userExits) ){
					//跳到首页，加入缓存
					EHCacheConfig.put(openid, session);
					 return "redirect:/index.html?openid="+openid+"&ticket="+session;
				}else{
					EHCacheConfig.put(openid, session);
					//&& !JiveGlobe.isEmpty(userExits.getMobPhone())
					//可以去拉取用户信息了
					UserInfo record = addChatMenu.getInstance().pullTokenUserInfo(access_token, openid);
					record.setCreateTime(indusReportMapper.selectCurrTime());
//					record.setCompany("创伙伴");
//					record.setCountry("中国");
//					record.setCreateTime(indusReportMapper.selectCurrTime());
//					record.setNickname("创伙伴测试哥");
//					record.setOpenId("ceshi1232432432");
//					record.setPhoto("");
//					record.setPost("助理");
//					record.setSex(1);
//					record.setPhoto(Constants.imageUrlBack+"cshi.jpg");
					if(!JiveGlobe.isEmpty(record)){
						//写进数据库
						userInfoMapper.insert(record);
						//跳进手机验证页面
						 return "redirect:/index.html?openid="+openid+"&ticket="+session;
					}
				}
			}
		} catch (Exception e) {
			logger.error("###网页单点失败："+e);
		}
		return null;
		//跳到error页面
		//return "login.html";
	}
}
