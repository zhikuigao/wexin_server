package com.jws.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Spring's bean factory.
 */
public class SpringBeanFactory {
	private static ApplicationContext applicationContext;

	private static SpringBeanFactory beanFactory;

	/**
	 * Instance spring's bean factory.
	 * @param xmlPath
	 * @return
	 */
	public synchronized static SpringBeanFactory instanceBeanFactory(String[] xmlNames) {
		if (beanFactory == null) {
			String xmlAbsPaths[] = new String[xmlNames.length];
			for (int i = 0; i < xmlAbsPaths.length; i++) {
				xmlAbsPaths[i] = getAbsFilePath(xmlNames[i]);
			}
			applicationContext = new FileSystemXmlApplicationContext(xmlAbsPaths);
			beanFactory = new SpringBeanFactory();
		}
		return beanFactory;
	}

	/**
	 * Get bean from ApplicationContext
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	private static String getAbsFilePath(String xmlName) {
		return ClassLoader.getSystemResource(xmlName).toString();

	}
}
