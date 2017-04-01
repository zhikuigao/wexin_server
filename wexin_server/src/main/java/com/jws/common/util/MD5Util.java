package com.jws.common.util;

import java.security.MessageDigest;

/**
 * MD5转码
 */
public class MD5Util {
	// 用来将字节转换成 16 进制表示的字符
	private static char HexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 根据源字符串获取MD5字符串
	 * @return String
	 * @param src
	 * @return
	 */
	public static final String getMD5String(String src) {
		if (src == null) {
			return null;
		}
		byte[] tmp = src.getBytes();
		String dist = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(tmp);
			// MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			tmp = md.digest();
			// 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			char chars[] = new char[16 * 2];
			int k = 0; // 表示转换结果中对应的字符位置
			// 从第一个字节开始，对 MD5 的每一个字节
			// 转换成 16 进制字符的转换
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i]; // 取第 i 个字节
				chars[k++] = HexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				chars[k++] = HexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			dist = new String(chars); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dist;
	}
	
	/** 
     * 加密解密算法 执行一次加密，两次解密,即加密两次即可将MD5码还原为初始字符串 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    } 
    
    /**
     * des加密算法，goldfire密码加密使用
     * @param pwd
     * @return
     */
    public static String getCryptedPwd(String pwd)
    {
        String sCryptedPwd = "";
        for(int i=0; i<pwd.length(); i++)
        {
            char nChar = pwd.charAt(i);
            nChar >>=8;
            int nChar1 = (int) Math.floor(nChar/26);
            int nChar2 = (nChar%26);
            sCryptedPwd+=String.valueOf((char)(nChar1+65)) + String.valueOf((char)(nChar2+65));
            nChar = pwd.charAt(i);
            nChar = (char) (nChar & 255);
            nChar1 = (char) Math.floor(nChar/26);
            nChar2 = (char) (nChar %26);
            sCryptedPwd+=String.valueOf((char)(nChar1+65)) + String.valueOf((char)(nChar2+65));
        }
        return sCryptedPwd;
    }
    
    /**
     * main方法，测试用
     * @param args
     */
//	public static void main(String[] args) {
//		String str="吴家骅";
//		System.out.println(getMD5String(str));//string2MD5
//		System.out.println(convertMD5(str));//MD5加密
//		System.out.println(convertMD5(convertMD5(str)));//MD5转为string
//	}
    
}
