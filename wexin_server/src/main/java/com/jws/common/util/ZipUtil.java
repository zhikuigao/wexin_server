package com.jws.common.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

public class ZipUtil {
    /**
     * 解压缩zip包
     * 
     * @param zipFilePath
     *            zip文件路径
     * @param targetPath
     *            解压缩到的位置，如果为null或空字符串则默认解压缩到跟zip包同目录跟zip包同名的文件夹下
     * @throws IOException
     * @author 
     * @date 
     */
    @SuppressWarnings({ "rawtypes" })
	public static Boolean unzip(String zipFilePath, String targetPath)
            throws IOException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath);
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath
                        .lastIndexOf("."));
            } else {
                directoryPath = targetPath;
            }
            Boolean  window = true;
            if (StringUtils.equals("/", File.separator)) {
            	window = false;
			}
            Enumeration entryEnum = zipFile.entries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                String newPath = directoryPath;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {
                    	if (window) {
                    		newPath = directoryPath +File.separator
                                    + zipEntry.getName().replace("/", "\\");
						}else {
							newPath = directoryPath +File.separator
	                                + zipEntry.getName();
						}
                    	
                    }else {
                    	if (window) {
                    		newPath = directoryPath +File.separator
                                    + zipEntry.getName().replace("/", "\\");
						}else {
							newPath = directoryPath +File.separator
	                                + zipEntry.getName();
						}
                    	
					}
                    if (zipEntry.getSize() > 0) {
                        // 文件
                        File targetFile = FileUtil.buildFile(newPath, false);
                        os = new BufferedOutputStream(new FileOutputStream(
                                targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }
                        os.flush();
                        os.close();
                    } else {
                        // 空目录
                        FileUtil.buildFile(newPath, true);
                    }
                }
            }
            zipFile.close();
            return true;
        } catch (IOException ex) {
        	File  file = new File(targetPath);
        	if (file.exists()) {
				file.delete();
			}                 
        } finally {
            if(null != zipFile){
                zipFile.close();
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
        return false;
    }
    /**
     * 制作图片缩略图
     * @param filePath 原图片路径
     * @param targetPath 目标图片路径
     * @throws IOException
     */
    public static Boolean makeThumbnail(String filePath, String targetPath)
            throws IOException {
   	 try {
   		 	String picType = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
            File fi = new File(filePath); //大图文件
            File fo = new File(targetPath); //将要转换出的小图文件

            AffineTransform transform = new AffineTransform();
            BufferedImage bis = ImageIO.read(fi);

            int w = bis.getWidth();
            int h = bis.getHeight();
            int nw = 120;
            int nh = (nw * h) / w;
            if(nh>120) {
                nh = 120;
                nw = (nh * w) / h;
            }
            double sx = (double)nw / w;
            double sy = (double)nh / h;
            transform.setToScale(sx,sy);

            AffineTransformOp ato = new AffineTransformOp(transform, null);
            BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
            ato.filter(bis,bid);
            ImageIO.write(bid, picType, fo);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            
        }
   	 	return false;	
    }

}
