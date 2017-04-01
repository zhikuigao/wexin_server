package org.gzk.weixin.cache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * ＥＨｃａｃｈｅ工具类　
 * @author Administrator
 *
 */
public class EHCacheConfig {
		
		/**
		 * 当前采用的缓存对象
		 */
		public static String cacheObject = "myCache";
		private static CacheManager cacheManager = null;  
		private static Cache cache = null;  
		
		 
	    static{  
	    	EHCacheConfig.initCacheManager();  
	    	EHCacheConfig.initCache();
	    }  
		
		   /** 
	     *  
	     * 初始化缓存管理容器 
	     */  
	    public static CacheManager initCacheManager() {  
	        try {  
	            if (cacheManager == null)  
	                cacheManager = CacheManager.getInstance();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return cacheManager;  
	    }  
	    
	    
	    
	    
	    /**
	     * 初始化cache
	     * @return
	     */
	    public static Cache initCache() {  
	    	if(cache ==null){
	    		  cache = cacheManager.getCache("myCache");
	    	}
	    	return cache;
	    }
	    /** 
	     *  
	     * 添加缓存 
	     *  
	     * @param key 
	     *            关键字 
	     * @param value 
	     *            值 
	     */  
	    public static void put(Object key, Object value) {  
	        // 创建Element,然后放入Cache对象中  
	        Element element = new Element(key, value);  
	        cache.put(element);  
	    }  
	  
	    /** 
	     * 获取cache 
	     *  
	     * @param key 
	     *            关键字 
	     * @return 
	     */  
	    public static Object get(Object key) {  
	        Element element = cache.get(key);  
	        if (null == element) {  
	            return null;  
	        }  
	        return element.getObjectValue();  
	    }  
	    /** 
	     * 移除所有cache 
	     */  
	    
	    /** 
	     * 释放CacheManage 
	     */  
	  
	    public static void shutdown() {  
	        cacheManager.shutdown();  
	    }  
	    
	    public static void flush(){
	    	cache.flush();
	    }
	  
	    public static void removeAllCache() {  
	        cacheManager.removalAll();  
	    }  
	    /** 
	     *  
	     * 移除所有Element 
	     */  
	    public static void removeAllKey() {   	
	        cache.removeAll();  
	    }  
	    /** 
	     *  
	     * 获取所有的cache名称 
	     * @return 
	     */  
	    public static String[] getAllCaches() {  
	        return cacheManager.getCacheNames();  
	    }  
	    /** 
	     *  
	     * 获取Cache所有的Keys 
	     * @return 
	     */  
	    public static List<Element> getKeys() {  
	        return cache.getKeys();  
	    }  
	    
	
}
