package org.gzk.weixin.timetask;

import java.util.Calendar;
import java.util.TimerTask;

import org.gzk.pay.weixin.control.WeixinJsbridgePayUser;

import com.jws.common.constant.Constants;

public class PayMoneyUserTimeTask extends TimerTask {
	@Override
	public void run() {
		//调起支付
		Integer time = Integer.valueOf(Constants.time);
		 Calendar now = Calendar.getInstance();  
		 int day = now.get(Calendar.HOUR_OF_DAY);
		 WeixinJsbridgePayUser ws = new WeixinJsbridgePayUser();
		 ws.requestPay();
		 if(day==time){
			 //检查支付
			ws.requestPayInfo();
			//检查过期
		 }
		
	}

}
