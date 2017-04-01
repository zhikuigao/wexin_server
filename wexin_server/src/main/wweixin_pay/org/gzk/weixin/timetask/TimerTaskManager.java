package org.gzk.weixin.timetask;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimerTaskManager  implements  ServletContextListener {
	private  Timer timer;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//执行MyTest1中的run方法，t代表TimerTask的子类,0代表延迟0毫秒执行run方法,1000表示每隔一秒执行一次run方法，
		//后面两个参数可根据自己的需求而定义
		PayMoneyUserTimeTask t=new PayMoneyUserTimeTask();
	    timer=new Timer("开始执行任务",true);//
	    timer.schedule(t, 0, 1000*60*30);
	}

}
