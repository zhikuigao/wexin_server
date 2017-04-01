package com.jws.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Properties;

/**
 * 文件操作类
 * goidfire的cookie保存文件工具类
 * 亲加通讯云的token保存工具类
 * 
 * @author wujh
 * 
 */
public class FileOperation {

//	public static final File fileName = new File(Constants.FILE_NAME);

	public static File getFileName(String fileName) {
		return new File(fileName);
	}
	
	/**
	 * 保存cookie/token
	 * 
	 * @param cookieStr
	 * @return
	 */
	public static void saveCookie(String cookieStr,String file_Name) {
		try {
			File fileName = getFileName(file_Name);
			if (!fileName.exists()) {
				File parentDir = new File(fileName.getParent());  
                if (!parentDir.exists()){//如果所在目录不存在,则新建.  
                    parentDir.mkdirs();  
                }
				fileName.createNewFile();
			}
			writeTxtFile(cookieStr,file_Name);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * 读cookie/token
	 * 
	 * @return
	 */
	public static String readTxtFile(String file_Name) {
		File fileName = getFileName(file_Name);
		if (fileName.exists()) {
			String result = "";
			FileReader fileReader = null; 	
			BufferedReader bufferedReader = null;
			try {
				fileReader = new FileReader(fileName);
				bufferedReader = new BufferedReader(fileReader);
				String read = new String();
				while ((read = bufferedReader.readLine()) != null) {
					result = result + read;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferedReader != null) {
						bufferedReader.close();
					}
					if (fileReader != null) {
						fileReader.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			// System.out.println("读取出来的文件内容是：" + "\r\n" + result);
			return result;
		} else
			return "";
	}

	/**
	 * 将字符串写入文本文件中
	 * 
	 * @param content
	 * @return
	 */
	public static boolean writeTxtFile(String content,String file_Name) {
		RandomAccessFile mm = null;
		boolean flag = false;
		File fileName = getFileName(file_Name);
		try {
			mm = new RandomAccessFile(fileName, "rw");
			mm.writeBytes(content);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 向原文本文件追加内容
	 * 
	 * @param filePath
	 * @param content
	 */
	public static void contentToTxt(String filePath, String content) {
		String str = new String(); // 原有txt内容
		String s1 = new String();// 内容更新
		try {
			File f = new File(filePath);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((str = input.readLine()) != null) {
				s1 += str + "\n";
			}
			System.out.println(s1);
			input.close();
			s1 += content;

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 读取配置文件中的数据
	 * 
	 * @return
	 */
	public static Properties readConfigProperties(String filename) {
		//filename = "dataConfig.properties";
		FileOperation fie = new FileOperation();
		InputStream inputStream = fie.getClass().getClassLoader()
				.getResourceAsStream(filename);

		Properties p = new Properties();// 属性集合对象

		try {
			p.load(inputStream);// 将属性文件流装载到Properties对象中
			inputStream.close();// 关闭流
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}

	public static void main(String[] args) {

		// Properties p = readConfigProperties();
		//
		// p.setProperty("Goldfire.cookie", "wujiahua");
		//
		// System.out.println("Goldfire.name:"+p.getProperty("Goldfire.name")+"/n");
		// System.out.println("Goldfire.pwd:"+p.getProperty("Goldfire.pwd"));
		// System.out.println("Goldfire.cookie:"+p.getProperty("Goldfire.cookie"));
		try {
			FileOperation fie = new FileOperation();
			Properties p = readConfigProperties("dataConfig.properties");
			String pathString = fie.getClass().getResource("/").getPath();
			// 获取属性值，sitename已在文件中定义
			System.out.println("获取属性值：Goldfire.cookie="
					+ p.getProperty("Goldfire.cookie"));
			// 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件
			// System.out.println("获取属性值：country=" + prop.getProperty("country",
			// "中国"));

			// String path =
			// FileOperation.class.getClassLoader().getResource("").toURI().getPath();
			System.out.println("path: " + pathString);
			// 文件输出流
			// OutputStream fos =
			// fie.getClass().getClassLoader().getResourceAsStream("dataConfig.properties");//new
			// FileOutputStream("dataConfig.properties");
			// // 将Properties集合保存到流中
			// p.store(fos, "test by wujh");
			// fos.close();// 关闭流
			// System.out.println("获取修改后的属性值：cookie=" +
			// p.getProperty("Goldfire.cookie"));

			FileInputStream inputStream = new FileInputStream(pathString
					+ "dataConfig.properties");
			Properties prop = new Properties();
			// 加载
			prop.load(inputStream);
			// 获取
			prop.getProperty("Goldfire.cookie");
			// 设置
			prop.setProperty("Goldfire.cookie", "value1212");
			// 写到配置文件
			FileOutputStream outputStream = new FileOutputStream(pathString
					+ "dataConfig.properties");
			prop.store(outputStream, "update message");
			outputStream.flush();
			outputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
