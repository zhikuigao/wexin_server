package org.gzk.weixin.accessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jws.common.constant.Constants;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;


/**
 * 
 * 生成签名之前必须先了解一下jsapi_ticket，jsapi_ticket是公众号用于调用微信JS接口的临时票据
 * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
 * 
 * 微信卡券接口签名凭证api_ticket
 * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=wx_card
 */

public class AccessTokenCommon {
	private final Logger logger = Logger.getLogger(this.getClass());
	private static String apiUrl = "https://api.weixin.qq.com/cgi-bin/token";
	private static final String GET  = "GET";
	private static final String POST = "POST";
	private static final String CHARSET = "UTF-8";
	private static boolean using_middle = false; //是否使用了中间件
	private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new AccessTokenCommon().new TrustAnyHostnameVerifier();
	private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
	private AccessTokenCommon() {
	}
	
	private static class JiveGlobeHolder {
		private static AccessTokenCommon instance = new AccessTokenCommon();
	}

	public static AccessTokenCommon getInstance() {
		return JiveGlobeHolder.instance;
	}
	
	public AcessToken getAcessToken(){
		 AcessToken at = new AcessToken();
		 try {
			 String grant_type  = Constants.grant_type;
			 String appid  = Constants.appid;
			 String secret  = Constants.secret;
			 ParaMap pm = ParaMap.create("grant_type", grant_type).put("appid", appid).put("secret", secret);
			 String response = get(apiUrl, pm.getData(), null);
			 JSONObject json = JSONObject.fromObject(response);	
			 if(!JiveGlobe.isEmpty(response)){
				 if(json.has("access_token") && json.has("expires_in")){
					 String access_token = json.getString("access_token");
					 String expires_in = json.getString("expires_in");
					 at.setAccess_token(access_token);
					 at.setExpires_in(expires_in);
				 }else{
					 if(json.has("errcode") && json.has("errmsg")){
						 String errcode = json.getString("access_token");
						 String errmsg = json.getString("errmsg");
						 logger.info("###获取到access_token错误【errcode="+errcode+";errmsg="+errmsg+"】");
						 return null;
					 }
					 logger.info("###获取到access_token错误");
					 return null;
				 }
			 }
			 return at;
		} catch (Exception e) {
			logger.error("###access_token获取erroe："+e);
		}
		return at;
	}
	

	
	public String getAcessTokenBycache(){
			String access_token	="";
			try {
				access_token = EHCacheConfig.get("access_token").toString();
			} catch (Exception e) {
				logger.error("###缓存中没有access_token："+e);
			}
			return access_token;
	}
	
	
	public void refreshToken(){
			AcessToken  at= getAcessToken();
			if(!JiveGlobe.isEmpty(at)){
				String accessToken = at.getAccess_token();
				EHCacheConfig.put("access_token", accessToken);
			}else{
				 logger.info("###获取不到token");
			}
	}
	

	
	/**
	 * Send GET request
	 */
	public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), GET, headers);
			conn.connect();
			return readResponseString(conn);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	/**
	 * Send POST request
	 */
	public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), POST, headers);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			out.write(data.getBytes(CHARSET));
			out.flush();
			out.close();
			
			return readResponseString(conn);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	
	/**
	 * Build queryString of the url
	 */
	public static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
		if (queryParas == null || queryParas.isEmpty())
			return url;
		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf("?") == -1) {
			isFirst = true;
			sb.append("?");
		}
		else {
			isFirst = false;
		}
		
		for (Entry<String, String> entry : queryParas.entrySet()) {
			if (isFirst) isFirst = false;	
			else sb.append("&");
			
			String key = entry.getKey();
			String value = entry.getValue();
			if (value != null && value.length() > 0)
				try {value = URLEncoder.encode(value, CHARSET);} catch (UnsupportedEncodingException e) {throw new RuntimeException(e);}
			sb.append(key).append("=").append(value);
		}
		return sb.toString();
	}
	
	
	private static String readResponseString(HttpURLConnection conn) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		try {
			inputStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
			String line = null;
			while ((line = reader.readLine()) != null){
				sb.append(line).append("\n");
			}
			return sb.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection)conn).setHostnameVerifier(trustAnyHostnameVerifier);
		}
		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setConnectTimeout(19000);
		conn.setReadTimeout(19000);
		
		//修改提交编码，中间调用的时候这个编码会导致问题
		if(using_middle == true) {
			conn.setRequestProperty("Content-Type","text/xml");
		} else {
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		}
		
		
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		
		if (headers != null && !headers.isEmpty())
			for (Entry<String, String> entry : headers.entrySet())
				conn.setRequestProperty(entry.getKey(), entry.getValue());
		
		return conn;
	}
	
	private static SSLSocketFactory initSSLSocketFactory() {
		try {
			TrustManager[] tm = {new AccessTokenCommon().new TrustAnyTrustManager() };  
			SSLContext sslContext = SSLContext.getInstance("TLS", "SunJSSE");  
			sslContext.init(null, tm, new java.security.SecureRandom());  
			return sslContext.getSocketFactory();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * https 证书管理
	 */
	private class TrustAnyTrustManager implements X509TrustManager {
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;  
		}
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}
	
	/**
	 * https 域名校验
	 */
	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	
}
