package org.gzk.weixin.control;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.gzk.weixin.service.impl.addChatMenu;
import org.gzk.weixin.thread.WeixinTokenThreadControl;



public class WechatCacheServiceControl  implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
			//获取token线程
		new Thread(new WeixinTokenThreadControl()).start();
		addChatMenu.getInstance().addMenu();
			
	}

}
