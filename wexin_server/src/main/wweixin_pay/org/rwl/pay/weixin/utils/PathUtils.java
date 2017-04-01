package org.rwl.pay.weixin.utils;

import java.io.File;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

public class PathUtils {
	
	private static Logger log = Logger.getLogger(PathUtils.class);
	
	/**
	 * 获取ClassPath
	 * 这里有个注意事项：
	 * 在Tomcat里面如果没有webapps/xxapp/WEB-INF/classes目录，则得到的会是common/classes
	 * 所以要想得到正确的class路径，WEB-INF下的classes目录必须存在才行。
	 * @param _class
	 * @return
	 */
	public static String getClassPath(Class _class) {
		String thename = _class.getClassLoader().getResource("").getFile();
		if(thename != null && thename.indexOf("WEB-INF") < 0) {
			if(_class.getClassLoader().getResource("/") != null) {
				thename = _class.getClassLoader().getResource("/").getFile();
			}
		}
		if(thename != null) {
			thename = URLDecoder.decode(thename);
		}
		return thename;
	}
	
	/**
	 * 获取Class的配置文件路径
	 * @param _class
	 * @param _configName
	 * @return
	 */
	public static String getClassConfigPath(Class _class, String _configName) {
		
		String thename = getClassPath(_class);
		String result = URLDecoder.decode(thename + _configName);
		if(log.isDebugEnabled()) {
			log.debug("获取Class路径:" + _class.getName() + ":" + _configName + "-" + result);
		}
		
		return result;
	}
	
	/**
	 * 获取WEB-INF路径
	 * @param _class
	 * @return
	 */
	public static String getWEBINFPath(Class _class) {
		String urlStr = getClassPath(_class);
		if(urlStr.indexOf("WEB-INF") >= 0) {
            urlStr = urlStr.substring(0,urlStr.indexOf("WEB-INF")+"WEB-INF".length());
        }
		if(log.isDebugEnabled()) {
			log.debug("获取WEB-INF路径:" + _class.getName() + "-" + urlStr);
		}
		return urlStr;
	}
	
	/**
	 * 获取WEB路径
	 * @param _class
	 * @return
	 */
	public static String getWEBAPPPath(Class _class) {
		String urlStr = getClassPath(_class);
		if(urlStr.indexOf("WEB-INF") >= 0) {
            urlStr = urlStr.substring(0,urlStr.indexOf("WEB-INF")+"WEB-INF".length());
            urlStr += File.separator + ".." + File.separator;
        }
		if(log.isDebugEnabled()) {
			log.debug("获取WEB-INF路径:" + _class.getName() + "-" + urlStr);
		}
		return urlStr;
	}
	
}
