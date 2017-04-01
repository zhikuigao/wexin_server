package com.jfinal.weixin.sdk.msg.in;

import org.hypertable.FsBroker.hadoop.main;

public class Demo {
			 public static void main(String[] args) {
			 InMsg im = 	new InMsg(null, null, null, null) {
				};
				im.setCreateTime(null);
				im.setFromUserName("11");
				System.out.println(im.getFromUserName());
			}
}
