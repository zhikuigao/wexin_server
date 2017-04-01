package com.jws.common.util;

import java.util.Random;

public  class  RandomUtil{

	 public static final String NUMBERCHAR = "0123456789";
	    /**
	     * 随机生成一个定长的数字，不够的后面补0
	     * @param fixdlenth
	     * @return
	     */
	    public static String toFixdLengthString( int fixdlenth) {
	    	int num = new Random().nextInt(100000);
	    	  StringBuffer sb = new StringBuffer();
	    	  String strNum = String.valueOf(num);
	    	  sb.append(strNum);
	    	  if (fixdlenth - strNum.length() >= 0) {
	    	   sb.append(generateZeroString(fixdlenth - strNum.length()));
	    	  } else {
	    	   throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
	    	     + "的字符串发生异常！");
	    	  }    	 
	    	  return sb.toString();
	     }
	    public static String generateZeroString(int length) {
	    	  StringBuffer sb = new StringBuffer();
	    	  for (int i = 0; i < length; i++) {
	    	   sb.append('0');
	    	  }
	    	  return sb.toString();
	    }
}