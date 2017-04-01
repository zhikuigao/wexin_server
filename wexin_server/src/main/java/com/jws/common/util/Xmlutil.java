package com.jws.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Xmlutil {

	private Xmlutil() {
	}

	private static class JiveGlobeHolder {
		private static Xmlutil instance = new Xmlutil();
	}

	public static Xmlutil getInstance() {
		return JiveGlobeHolder.instance;
	}

	private final Logger logger = Logger.getLogger(this.getClass());

	public Map<String, String> xmlToMaping(String str) {
		Map<String, String> recordMap = new HashMap<String, String>();
		try {
			// 将字符串转化为XML文档对象
			Document document = DocumentHelper.parseText(str);
			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();
			// 遍历所有结点
			while (iter.hasNext()) {
				Element ele = (Element) iter.next();
				// 获取set方法中的参数字段（实体类的属性）
				// 调用set方法
				recordMap.put(ele.getName(), ele.getText());
			}
		} catch (Exception e) {
			logger.error("###dom解析失败:" + str + e);
		}
		return recordMap;
	}

}
