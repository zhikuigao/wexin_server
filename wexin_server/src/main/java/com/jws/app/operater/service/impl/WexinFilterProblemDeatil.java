package com.jws.app.operater.service.impl;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.gzk.weixin.service.impl.addChatMenu;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.jws.app.operater.service.impl.WexinFilterProblemDetailImpl;



public class WexinFilterProblemDeatil implements Filter {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String code = req.getParameter("code");
			String state = req.getParameter("state");
			logger.info("###分享记录code="+code);
			logger.info("###分享记录state"+state);
			String responseJson = addChatMenu.getInstance().pullToken(code);
			JSONObject json = JSONObject.fromObject(responseJson);
			logger.info("###分享获取用户信息"+json);
			WexinFilterProblemDetailImpl wp = new WexinFilterProblemDetailImpl();
			//wp.saveShareUserinfo(json, state);
		    chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
			
		
	}
	

}
