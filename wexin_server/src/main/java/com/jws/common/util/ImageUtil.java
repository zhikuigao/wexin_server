package com.jws.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 图片与基于64位编码的二进制流的转换工具类
 * @author wujh
 *
 */
public class ImageUtil {

	public static BASE64Encoder encoder = new BASE64Encoder();   
	public static BASE64Decoder decoder = new BASE64Decoder();   
       
    public static String getImageBinary(){   
        File f = new File("c://logo.jpg");          
        BufferedImage bi;   
        try {   
            bi = ImageIO.read(f);   
            ByteArrayOutputStream baos = new ByteArrayOutputStream();   
            ImageIO.write(bi, "jpg", baos);   
            byte[] bytes = baos.toByteArray();   
               
            return encoder.encodeBuffer(bytes).trim();   
        } catch (IOException e) {   
            e.printStackTrace();   
            return null;   
        }   
    }   
       
    public static boolean base64StringToImage(String base64String){   
        try {   
            byte[] bytes1 = decoder.decodeBuffer(base64String);   
               
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);   
            BufferedImage bi1 =ImageIO.read(bais);   
            File w2 = new File("we1112.jpg");//可以是jpg,png,gif格式   
            return ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动   
        } catch (IOException e) {   
            e.printStackTrace();  
            return false;
        }   
    }
    
    public static void main(String[] args) {   
    	String base64str = getImageBinary();
    	System.out.println(base64str);   
    	base64StringToImage(base64str);   
    }   
    
}
