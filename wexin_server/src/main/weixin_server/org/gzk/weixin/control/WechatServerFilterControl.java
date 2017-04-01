package org.gzk.weixin.control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.gzk.weixin.hand.SignatureCheckKit;

import com.jws.common.constant.Constants;


public class WechatServerFilterControl implements Filter {
	 public Logger logger =  Logger.getLogger(this.getClass());
	public final String isTrue =Constants.isTrue;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain arg2) throws IOException, ServletException {
		if(isTrue.equals("true")){
			//跟微信服务器握手
			try {
				HttpServletRequest request = (HttpServletRequest) req;			
				String echostr = request.getParameter("echostr");
				String signature = request.getParameter("signature");
		        String timestamp = request.getParameter("timestamp");
		        String nonce =  request.getParameter("nonce");
		        logger.info("###echostr:"+echostr+"###signature:"+signature+"###timestamp:"+timestamp);
				boolean isOk = SignatureCheckKit.me.checkSignature(signature, timestamp, nonce);
				logger.info("###返回"+isOk);
				if (isOk)
					res.getWriter().println(echostr);
				else
					logger.info("###验证失败：configServer");
			} catch (Exception e) {
					logger.error("###验证error：configServer"+e);
			}
		}else{
			arg2.doFilter(req, res);
		}
	
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
