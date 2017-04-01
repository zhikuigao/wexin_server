package com.jws.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUtil {
	/**
     * 生产文件 如果文件所在路径不存在则生成路径
     * 
     * @param fileName
     *            文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     * @author yayagepei
     * @date 2008-8-27
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    } 
    /**
     * 删除文件夹及文件夹下的文件
     * @param oldPath
     */
    public static  void deleteFile(File oldPath) {
        if (oldPath.isDirectory()) {
         File[] files = oldPath.listFiles();
         for (File file : files) {
           deleteFile(file);
         }
        }else{
          oldPath.getAbsoluteFile().delete();
        }
        oldPath.getAbsoluteFile().delete();
      }
    
    public static List<?>  parseRequest(File  repositoryFile, HttpServletRequest request) throws FileUploadException{
		//将请求信息流上传到该路径下
		FileItemFactory factory = new DiskFileItemFactory(1024 * 32, repositoryFile);
		ServletFileUpload upload = new ServletFileUpload(factory); 
		upload.setHeaderEncoding("utf-8");
		upload.setSizeMax(upload.getSizeMax());
		return  upload.parseRequest(request);		
    }
    /**
     * 截取视频第一张图片
     * @param veido_path  视频路径
     * @param img_path  保存成图片的路径
     * @return
     * @throws IOException 
     */
	public static void getVideoFirstImg(String veido_path, String img_path) throws IOException {
		List<String> commands = new java.util.ArrayList<String>();
	//	commands.add(Constants.FFMPEG_EXE);
		commands.add("-i");
		commands.add(veido_path);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-ss");
		commands.add("0.001");// 这个参数是设置截取视频多少秒时的画面
		commands.add("-s");
		commands.add("120x90");
		commands.add(img_path);
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(commands);
		builder.start();
	}

    public static void main(String[] args) throws IOException {
//    	getVedioFirstImg("D:\\VID_20151002_113930.mp4","D:\\firstImg2.jpg");
    }
    /**
     * 将字符串写入到指定文件中
     * @param saveFile  写入文件
     * @param contentTxt 写入内容
     */
    public static void writeToFile(File saveFile, String contentTxt) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "UTF-8"));
            bufferedWriter.write(contentTxt);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
