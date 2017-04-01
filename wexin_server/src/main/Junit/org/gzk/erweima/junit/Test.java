  package org.gzk.erweima.junit;  
      
    import java.io.File;  
import java.util.Hashtable;  
      


    import com.google.zxing.BarcodeFormat;  
import com.google.zxing.EncodeHintType;  
import com.google.zxing.MultiFormatWriter;  
import com.google.zxing.common.BitMatrix;  
      
    public class Test {  
      
        /** 
         * @param args 
         * @throws Exception  
         */  
        public static void main(String[] args) throws Exception {  
            String text = "http://wx.fsdigital.cn/weiixin/help-detail.html?questId=2&shareId=oKz74wMeEI9OL3WVpASk9AJ4QubQ&from=rqcord";  
            int width = 400;  
            int height = 400;  
            //二维码的图片格式  
            String format = "jpg";  
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
            //内容所使用编码  
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text,  BarcodeFormat.QR_CODE, width, height, hints);  
            //生成二维码  
            File outputFile = new File("d:"+File.separator+"new.jpg");  
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);  
      
        }  
      
    }  