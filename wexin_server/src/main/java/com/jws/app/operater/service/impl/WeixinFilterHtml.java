package com.jws.app.operater.service.impl;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.gzk.weixin.service.impl.addChatMenu;

import com.jws.app.operater.model.User;
import com.jws.common.util.CommonConstant;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;


public class WeixinFilterHtml implements Filter {
	// ① 需要验证的
	private static final String[] INHERENT_ESCAPE_URIS = { "/main.html","/operater.html","/about.html","/list.do","/operater"};
	private static final String FILTERED_REQUEST = "@@session_context_filtered_request";
	private final Logger logger = Logger.getLogger(this.getClass());
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		// ②-1 保证该过滤器在一次请求中只被调用一次
		if (request != null && request.getAttribute(FILTERED_REQUEST) != null) {
			chain.doFilter(request, response);
		} else{
			// ②-2 设置过滤标识，防止一次请求多次过滤
			request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			User userContext = getSessionUser(httpRequest);
			String openidContext = getSessionUserOpenid(httpRequest);
			// ②-3 用户未登录, 且当前URI资源需要登录才能访问
			if("/weixin/help-detail.html".equals(httpRequest.getRequestURI())){
				String code = httpRequest.getParameter("code");
				String from = httpRequest.getParameter("from");
				String state = "";
				String openidBy = "";
				String problemId  = "";
				String openid = "";
				String session = "";
				JSONObject json = new JSONObject();
				if(JiveGlobe.isEmpty(from) && JiveGlobe.isEmpty(code)){
				//	logger.info("###不是分享");
				}else{
					if(JiveGlobe.isEmpty(code) && JiveGlobe.isEmpty(openidContext)){
						 openidBy  = httpRequest.getParameter("shareId");
						 problemId =   httpRequest.getParameter("questId");
						 String respMessage = "questId:" +problemId +",shareId:"+ openidBy; 
						logger.info("###⑴重定向信息"+respMessage);
						//重定向到微信授权页
						httpResponse.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx64719d9b69c5714d&redirect_uri=http://wx.fsdigital.cn/weixin/help-detail.html&response_type=code&scope=snsapi_userinfo&state="+respMessage+"#wechat_redirect");
					}else{
					if(JiveGlobe.isEmpty(openidContext)){
						state = httpRequest.getParameter("state");
						openidBy = state.split(",")[1].split(":")[1];
						problemId = state.split(",")[0].split(":")[1];
						String responseMessage = addChatMenu.getInstance().pullToken(code);	
						json = JSONObject.fromObject(responseMessage);
						//当前用户的openid
						openid =  json.optString("openid");
					}else{
						//当前用户ID
						openidBy = httpRequest.getParameter("shareId");
						problemId =  httpRequest.getParameter("questId");
						openid = getSessionUserOpenid(httpRequest);
						logger.info("###⑴有session信息"+openid);
					}
					session = openid+JiveGlobe.getFromRom();
					if(JiveGlobe.isEmpty(openidContext)){
						setSessionUserOpenid(httpRequest, openid);
					}
					if(!JiveGlobe.isEmpty(openidBy) && !openidBy.equals("null")  && !openid.equals(openidBy)){
						//添加session信息
						WexinFilterProblemDetailImpl wp = new WexinFilterProblemDetailImpl();
						wp.saveShareUserinfo(json, state,openid,openidBy,problemId);
						EHCacheConfig.put(openid, session);
						httpResponse.sendRedirect("http://wx.fsdigital.cn/weixin/help-detail.html?shareId="+openid+"&ticket="+session+"&questId="+problemId+"");
						}else{
							logger.info("###分享ID为空");
							httpResponse.sendRedirect("http://wx.fsdigital.cn/weixin/help-detail.html?shareId="+openid+"&ticket="+session+"&questId="+problemId+"&openidBy="+openidBy+"");
						}
					}
				}
			}
			if (userContext == null&& !isURILogin(httpRequest.getRequestURI(), httpRequest)) {
				String toUrl = httpRequest.getRequestURL().toString();
				if (!StringUtils.isEmpty(httpRequest.getQueryString())) {
					toUrl += "?" + httpRequest.getQueryString();
				}
				request.getRequestDispatcher("/doLogin.html").forward(request,response);
				return;
			}

		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
	}
	
	
	/**
	 * 保存访问对象到Session中
	 * @param request
	 * @param user
	 */
	protected void setSessionUserOpenid(HttpServletRequest request,String openid) {
		request.getSession().setAttribute(CommonConstant.USER_CONTEXT_OPENID,openid );
	}
	
	protected String  getSessionUserOpenid(HttpServletRequest request) {
		return  (String) request.getSession().getAttribute(CommonConstant.USER_CONTEXT_OPENID);
	}
	
	   /**
	    * 当前URI资源是否需要登录才能访问
	    * @param requestURI
	    * @param request
	    * @return
	    */
		private boolean isURILogin(String requestURI, HttpServletRequest request) {
			//System.out.println(requestURI);
			if (request.getContextPath().equalsIgnoreCase(requestURI)
					|| (request.getContextPath() + "/").equalsIgnoreCase(requestURI))
				return true;
			for (String uri : INHERENT_ESCAPE_URIS) {
				if (requestURI != null && requestURI.indexOf(uri) >= 0) {
					return false;
				}
			}
			return true;
		}

}
