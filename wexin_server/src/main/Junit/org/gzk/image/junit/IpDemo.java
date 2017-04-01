package org.gzk.image.junit;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.gzk.pay.weixin.control.WeixinJsbridgePayUser;

public class IpDemo {
  public static void main(String[] args) throws UnknownHostException {
		WeixinJsbridgePayUser ws = new WeixinJsbridgePayUser();
		//ws.requestPay();
		ws.requestPayInfo();
	  
}
} 
