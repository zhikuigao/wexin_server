package org.gzk.image.junit;

public class Demo {
  public static void main(String[] args) {
	 // System.out.println("需求一个技术合伙人,公司拓展".length());
	  String s ="123456789012345678901234567890";
	  if(s.length()<15){
		 System.out.println( s.substring(0, 14));
		  System.out.println(s.substring(14,30));
	  }
	  if(s.length()<32){
		  System.out.println( s.substring(0, 14));
		  System.out.println(s.substring(14,30));
	  }
	  if(s.length()<49){
		  System.out.println( s.substring(0, 14));
		  System.out.println(s.substring(14,30));
	  }
}
}
