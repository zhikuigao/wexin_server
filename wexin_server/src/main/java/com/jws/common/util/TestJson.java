package com.jws.common.util;

import java.util.Date;

public class TestJson {
	
	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(JsonUtil.toJSONString(date));;
	}

}
