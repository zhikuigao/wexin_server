package org.gzk.image.junit;

import com.jws.common.util.JiveGlobe;

public class DemoMath {
	 public static void main(String[] args) {
		  //System.out.println(isNumeric("1")); 
		 System.out.println(JiveGlobe.isEmpty("null"));
		
	}
	 
	 public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
		   //System.out.println(str.charAt(i));
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
		 }
}
