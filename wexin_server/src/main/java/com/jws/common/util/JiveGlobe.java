package com.jws.common.util;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.json.JSONObject;

/**
 * @author gzkui 工具类
 */
public class JiveGlobe {

	private JiveGlobe() {
	}

	private static class JiveGlobeHolder {
		private static JiveGlobe instance = new JiveGlobe();
	}

	public static JiveGlobe getInstance() {
		return JiveGlobeHolder.instance;
	}

	/**
	 * @author gzkui
	 * @param obj
	 * @return
	 * @see 所有空判断
	 * @serialData 2015/10/16
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;
		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();
		if (obj instanceof Map)
			return ((Map) obj).isEmpty();
		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

	public static boolean isEmpty(String[] str) {
		// str = new String[]{"segs","segs"};
		boolean temp = true;
		if (str == null || (str != null && str.length == 0)) {
		} else {
			temp = false;
		}
		return temp;
	}

	public static <T> boolean isEmpty(List<T> str) {
		boolean temp = true;
		if (str == null || str.isEmpty()) {
		} else {
			temp = false;
		}
		return temp;
	}

	public static boolean isEmpty(String input) {
		return null == input || 0 == input.length()
				|| 0 == input.replaceAll("\\s", "").length();
	}

	public static boolean isEmpty(byte[] bytes) {
		return null == bytes || 0 == bytes.length;
	}

	public static boolean isEmpty(JSONObject json, String str) {
		// 为假的话返回true
		boolean isTrue = true;
		if (json.has(str) && null != json.get(str) && "null" !=json.get(str) && "" !=json.get(str) ) {
			isTrue = false;
		}
		return isTrue;
	}

	public static boolean isEmpty(JSONObject json, String str, String str1,
			String str2) {
		// 为假的话返回true
		boolean isTrue = true;
		if (json.has(str) && null != json.get(str) && json.has(str1)
				&& null != json.get(str1) && json.has(str2)
				&& null != json.get(str2)) {
			isTrue = false;
		}
		return isTrue;
	}

	/**
	 * @param str
	 * @return str 字符创分割,防止内存溢出
	 */
	public static String getFromSpendSubstring(String str, int begin, int end) {
		return new String(str.substring(begin, end));

	}

	/**
	 * @param str
	 * @return str 优化字符创切割
	 */
	@Deprecated
	public static List<String> getFromSpendSpilt(String str, String str1) {
		StringTokenizer st = new StringTokenizer(str, str1);
		List<String> list = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		return list;

	}

	/**
	 * @param str
	 * @return str 字符串反转
	 */
	public static String getFromReverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}

	/**
	 * 获取Map中的属性
	 * 
	 * @see 由于Map.toString()打印出来的参数值对,是横着一排的...参数多的时候,不便于查看各参数值
	 * @see 故此仿照commons-lang.jar中的ReflectionToStringBuilder.toString()编写了本方法
	 * @return String key11=value11 \n key22=value22 \n key33=value33 \n......
	 */
	public static String getStringFromMap(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		sb.append(map.getClass().getName()).append("@").append(map.hashCode())
				.append("[");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append("\n").append(entry.getKey()).append("=")
					.append(entry.getValue());
		}
		return sb.append("\n]").toString();
	}

	/**
	 * 获取Map中的属性
	 * 
	 * @see 该方法的参数适用于打印Map<String, byte[]>的情况
	 * @return String key11=value11 \n key22=value22 \n key33=value33 \n......
	 */
	public static String getStringFromMapForByte(Map<String, byte[]> map) {
		StringBuilder sb = new StringBuilder();
		sb.append(map.getClass().getName()).append("@").append(map.hashCode())
				.append("[");
		for (Map.Entry<String, byte[]> entry : map.entrySet()) {
			sb.append("\n").append(entry.getKey()).append("=")
					.append(new String(entry.getValue()));
		}
		return sb.append("\n]").toString();
	}

	/**
	 * 获取Map中的属性
	 * 
	 * @see 该方法的参数适用于打印Map<String, Object>的情况
	 * @return String key11=value11 \n key22=value22 \n key33=value33 \n......
	 */
	public static String getStringFromMapForObject(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		sb.append(map.getClass().getName()).append("@").append(map.hashCode())
				.append("[");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append("\n").append(entry.getKey()).append("=")
					.append(entry.getValue().toString());
		}
		return sb.append("\n]").toString();
	}

	/**
	 * 金额元转分
	 * 
	 * @see 注意:该方法可处理贰仟万以内的金额,且若有小数位,则不限小数位的长度
	 * @see 注意:如果你的金额达到了贰仟万以上,则不推荐使用该方法,否则计算出来的结果会令人大吃一惊
	 * @param amount
	 *            金额的元进制字符串
	 * @return String 金额的分进制字符串
	 */
	public static String moneyYuanToFen(String amount) {
		if (isEmpty(amount)) {
			return amount;
		}
		// 传入的金额字符串代表的是一个整数
		if (-1 == amount.indexOf(".")) {
			return Integer.parseInt(amount) * 100 + "";
		}
		// 传入的金额字符串里面含小数点-->取小数点前面的字符串,并将之转换成单位为分的整数表示
		int money_fen = Integer.parseInt(amount.substring(0,
				amount.indexOf("."))) * 100;
		// 取到小数点后面的字符串
		String pointBehind = (amount.substring(amount.indexOf(".") + 1));
		// amount=12.3
		if (pointBehind.length() == 1) {
			return money_fen + Integer.parseInt(pointBehind) * 10 + "";
		}
		// 小数点后面的第一位字符串的整数表示
		int pointString_1 = Integer.parseInt(pointBehind.substring(0, 1));
		// 小数点后面的第二位字符串的整数表示
		int pointString_2 = Integer.parseInt(pointBehind.substring(1, 2));
		// amount==12.03,amount=12.00,amount=12.30
		if (pointString_1 == 0) {
			return money_fen + pointString_2 + "";
		} else {
			return money_fen + pointString_1 * 10 + pointString_2 + "";
		}
	}

	/**
	 * 金额元转分
	 * 
	 * @see 该方法会将金额中小数点后面的数值,四舍五入后只保留两位....如12.345-->12.35
	 * @see 注意:该方法可处理贰仟万以内的金额
	 * @see 注意:如果你的金额达到了贰仟万以上,则非常不建议使用该方法,否则计算出来的结果会令人大吃一惊
	 * @param amount
	 *            金额的元进制字符串
	 * @return String 金额的分进制字符串
	 */
	public static String moneyYuanToFenByRound(String amount) {
		if (isEmpty(amount)) {
			return amount;
		}
		if (-1 == amount.indexOf(".")) {
			return Integer.parseInt(amount) * 100 + "";
		}
		int money_fen = Integer.parseInt(amount.substring(0,
				amount.indexOf("."))) * 100;
		String pointBehind = (amount.substring(amount.indexOf(".") + 1));
		if (pointBehind.length() == 1) {
			return money_fen + Integer.parseInt(pointBehind) * 10 + "";
		}
		int pointString_1 = Integer.parseInt(pointBehind.substring(0, 1));
		int pointString_2 = Integer.parseInt(pointBehind.substring(1, 2));
		// 下面这种方式用于处理pointBehind=245,286,295,298,995,998等需要四舍五入的情况
		if (pointBehind.length() > 2) {
			int pointString_3 = Integer.parseInt(pointBehind.substring(2, 3));
			if (pointString_3 >= 5) {
				if (pointString_2 == 9) {
					if (pointString_1 == 9) {
						money_fen = money_fen + 100;
						pointString_1 = 0;
						pointString_2 = 0;
					} else {
						pointString_1 = pointString_1 + 1;
						pointString_2 = 0;
					}
				} else {
					pointString_2 = pointString_2 + 1;
				}
			}
		}
		if (pointString_1 == 0) {
			return money_fen + pointString_2 + "";
		} else {
			return money_fen + pointString_1 * 10 + pointString_2 + "";
		}
	}

	/**
	 * 金额分转元
	 * 
	 * @see 注意:如果传入的参数中含小数点,则直接原样返回
	 * @see 该方法返回的金额字符串格式为<code>00.00</code>,其整数位有且至少有一个,小数位有且长度固定为2
	 * @param amount
	 *            金额的分进制字符串
	 * @return String 金额的元进制字符串
	 */
	public static String moneyFenToYuan(String amount) {
		if (isEmpty(amount)) {
			return amount;
		}
		if (amount.indexOf(".") > -1) {
			return amount;
		}
		if (amount.length() == 1) {
			return "0.0" + amount;
		} else if (amount.length() == 2) {
			return "0." + amount;
		} else {
			return amount.substring(0, amount.length() - 2) + "."
					+ amount.substring(amount.length() - 2);
		}
	}

	/**
	 * 字节数组转为字符串
	 * 
	 * @see 该方法默认以ISO-8859-1转码
	 * @see 若想自己指定字符集,可以使用<code>getString(byte[] data, String charset)</code>方法
	 */
	public static String getString(byte[] data) {
		return getString(data, "ISO-8859-1");
	}

	/**
	 * 字节数组转为字符串
	 * 
	 * @see 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
	 */
	public static String getString(byte[] data, String charset) {
		if (isEmpty(data)) {
			return "";
		}
		if (isEmpty(charset)) {
			return new String(data);
		}
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			// LogUtil.getLogger().error("将byte数组[" + data +
			// "]转为String时发生异常:系统不支持该字符集[" + charset + "]");
			return new String(data);
		}
	}

	/**
	 * 获取一个字符串的简明效果
	 * 
	 * @return String 返回的字符串格式类似于"abcd***hijk"
	 */
	public static String getStringSimple(String data) {
		return data.substring(0, 4) + "***" + data.substring(data.length() - 4);
	}

	/**
	 * 字符串转为字节数组
	 * 
	 * @see 该方法默认以ISO-8859-1转码
	 * @see 若想自己指定字符集,可以使用<code>getBytes(String str, String charset)</code>方法
	 */
	public static byte[] getBytes(String data) {
		return getBytes(data, "ISO-8859-1");
	}

	/**
	 * 字符串转为字节数组
	 * 
	 * @see 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
	 */
	public static byte[] getBytes(String data, String charset) {
		data = (data == null ? "" : data);
		if (isEmpty(charset)) {
			return data.getBytes();
		}
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			// LogUtil.getLogger().error("将字符串[" + data +
			// "]转为byte[]时发生异常:系统不支持该字符集[" + charset + "]");
			return data.getBytes();
		}
	}

	/**
	 * 根据指定的签名密钥和算法签名Map<String,String>
	 * 
	 * @see 方法内部首先会过滤Map<String,String>参数中的部分键值对
	 * @see 过滤规则:移除键名为"cert","hmac","signMsg"或者键值为null或者键值长度为零的键值对
	 * @see 过滤结果
	 *      :过滤完Map<String,String>后会产生一个字符串,其格式为[key11=value11|key22=value22|
	 *      key33=value33]
	 * @see And the calls
	 *      {@link TradePortalUtil#getHexSign(String,String,String,boolean)}进行签名
	 * @param param
	 *            待签名的Map<String,String>
	 * @param charset
	 *            签名时转码用到的字符集
	 * @param algorithm
	 *            签名时所使用的算法,从业务上看,目前其可传入两个值:MD5,SHA-1
	 * @param signKey
	 *            签名用到的密钥
	 * @return String algorithm digest as a lowerCase hex string
	 */
	public static String getHexSign(Map<String, String> param, String charset,
			String algorithm, String signKey) {
		StringBuilder sb = new StringBuilder();
		List<String> keys = new ArrayList<String>(param.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = param.get(key);
			if (key.equalsIgnoreCase("cert") || key.equalsIgnoreCase("hmac")
					|| key.equalsIgnoreCase("signMsg") || value == null
					|| value.length() == 0) {
				continue;
			}
			sb.append(key).append("=").append(value).append("|");
		}
		sb.append("key=").append(signKey);
		return getHexSign(sb.toString(), charset, algorithm, true);
	}

	/**
	 * 通过指定算法签名字符串
	 * 
	 * @see Calculates the algorithm digest and returns the value as a hex
	 *      string
	 * @see If system dosen't support this <code>algorithm</code>, return "" not
	 *      null
	 * @see It will Calls
	 *      {@link TradePortalUtil#getBytes(String str, String charset)}
	 * @see 若系统不支持<code>charset</code>字符集,则按照系统默认字符集进行转换
	 * @see 若系统不支持<code>algorithm</code>算法,则直接返回""空字符串
	 * @see 另外,commons-codec.jar提供的DigestUtils.md5Hex(String
	 *      data)与本方法getHexSign(data, "UTF-8", "MD5", false)效果相同
	 * @param data
	 *            Data to digest
	 * @param charset
	 *            字符串转码为byte[]时使用的字符集
	 * @param algorithm
	 *            目前其有效值为<code>MD5,SHA,SHA1,SHA-1,SHA-256,SHA-384,SHA-512</code>
	 * @param toLowerCase
	 *            指定是否返回小写形式的十六进制字符串
	 * @return String algorithm digest as a lowerCase hex string
	 */
	public static String getHexSign(String data, String charset,
			String algorithm, boolean toLowerCase) {
		char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// Used to build output as Hex
		char[] DIGITS = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
		// get byte[] from {@link TradePortalUtil#getBytes(String, String)}
		byte[] dataBytes = getBytes(data, charset);
		byte[] algorithmData = null;
		try {
			// get an algorithm digest instance
			algorithmData = MessageDigest.getInstance(algorithm).digest(
					dataBytes);
		} catch (NoSuchAlgorithmException e) {
			// LogUtil.getLogger().error("签名字符串[" + data +
			// "]时发生异常:System doesn't support this algorithm[" + algorithm +
			// "]");
			return "";
		}
		char[] respData = new char[algorithmData.length << 1];
		// two characters form the hex value
		for (int i = 0, j = 0; i < algorithmData.length; i++) {
			respData[j++] = DIGITS[(0xF0 & algorithmData[i]) >>> 4];
			respData[j++] = DIGITS[0x0F & algorithmData[i]];
		}
		return new String(respData);
	}

	/**
	 * 字符编码
	 * 
	 * @see 该方法默认会以UTF-8编码字符串
	 * @see 若想自己指定字符集,可以使用<code>encode(String chinese, String charset)</code>方法
	 */
	public static String encode(String chinese) {
		return encode(chinese, "UTF-8");
	}

	/**
	 * 字符编码
	 * 
	 * @see 该方法通常用于对中文进行编码
	 * @see 若系统不支持指定的编码字符集,则直接将<code>chinese</code>原样返回
	 */
	public static String encode(String chinese, String charset) {
		chinese = (chinese == null ? "" : chinese);
		try {
			return URLEncoder.encode(chinese, charset);
		} catch (UnsupportedEncodingException e) {
			// LogUtil.getLogger().error("编码字符串[" + chinese +
			// "]时发生异常:系统不支持该字符集[" + charset + "]");
			return chinese;
		}
	}

	/**
	 * 字符解码
	 * 
	 * @see 该方法默认会以UTF-8解码字符串
	 * @see 若想自己指定字符集,可以使用<code>decode(String chinese, String charset)</code>方法
	 */
	public static String decode(String chinese) {
		return decode(chinese, "UTF-8");
	}

	/**
	 * 字符解码
	 * 
	 * @see 该方法通常用于对中文进行解码
	 * @see 若系统不支持指定的解码字符集,则直接将<code>chinese</code>原样返回
	 */
	public static String decode(String chinese, String charset) {
		chinese = (chinese == null ? "" : chinese);
		try {
			return URLDecoder.decode(chinese, charset);
		} catch (UnsupportedEncodingException e) {
			// LogUtil.getLogger().error("解码字符串[" + chinese +
			// "]时发生异常:系统不支持该字符集[" + charset + "]");
			return chinese;
		}
	}

	/**
	 * 字符串右补空格
	 * 
	 * @see 该方法默认采用空格(其ASCII码为32)来右补字符
	 * @see 若想自己指定所补字符,可以使用
	 *      <code>rightPadForByte(String str, int size, int padStrByASCII)</code>
	 *      方法
	 */
	public static String rightPadForByte(String str, int size) {
		return rightPadForByte(str, size, 32);
	}

	/**
	 * 字符串右补字符
	 * 
	 * @see 若str对应的byte[]长度不小于size,则按照size截取str对应的byte[],而非原样返回str
	 * @see 所以size参数很关键..事实上之所以这么处理,是由于支付处理系统接口文档规定了字段的最大长度
	 * @see 若对普通字符串进行右补字符,建议org.apache.commons.lang.StringUtils.rightPad(...)
	 * @param size
	 *            该参数指的不是字符串长度,而是字符串所对应的byte[]长度
	 * @param padStrByASCII
	 *            该值为所补字符的ASCII码,如32表示空格,48表示0,64表示@等
	 */
	public static String rightPadForByte(String str, int size, int padStrByASCII) {
		byte[] srcByte = str.getBytes();
		byte[] destByte = null;
		if (srcByte.length >= size) {
			// 由于size不大于原数组长度,故该方法此时会按照size自动截取,它会在数组右侧填充'(byte)0'以使其具有指定的长度
			destByte = Arrays.copyOf(srcByte, size);
		} else {
			destByte = Arrays.copyOf(srcByte, size);
			Arrays.fill(destByte, srcByte.length, size, (byte) padStrByASCII);
		}
		return new String(destByte);
	}

	/**
	 * 字符串左补空格
	 * 
	 * @see 该方法默认采用空格(其ASCII码为32)来左补字符
	 * @see 若想自己指定所补字符,可以使用
	 *      <code>leftPadForByte(String str, int size, int padStrByASCII)</code>
	 *      方法
	 */
	public static String leftPadForByte(String str, int size) {
		return leftPadForByte(str, size, 32);
	}

	/**
	 * 字符串左补字符
	 * 
	 * @see 若str对应的byte[]长度不小于size,则按照size截取str对应的byte[],而非原样返回str
	 * @see 所以size参数很关键..事实上之所以这么处理,是由于支付处理系统接口文档规定了字段的最大长度
	 * @param padStrByASCII
	 *            该值为所补字符的ASCII码,如32表示空格,48表示0,64表示@等
	 */
	public static String leftPadForByte(String str, int size, int padStrByASCII) {
		byte[] srcByte = str.getBytes();
		byte[] destByte = new byte[size];
		Arrays.fill(destByte, (byte) padStrByASCII);
		if (srcByte.length >= size) {
			System.arraycopy(srcByte, 0, destByte, 0, size);
		} else {
			System.arraycopy(srcByte, 0, destByte, size - srcByte.length,
					srcByte.length);
		}
		return new String(destByte);
	}

	/**
	 * 获取前一天日期yyyyMMdd
	 * 
	 * @see 经测试，针对闰年02月份或跨年等情况，该代码仍有效。测试代码如下
	 * @see calendar.set(Calendar.YEAR, 2013);
	 * @see calendar.set(Calendar.MONTH, 0);
	 * @see calendar.set(Calendar.DATE, 1);
	 * @see 测试时，将其放到<code>calendar.add(Calendar.DATE, -1);</code>前面即可
	 * @return 返回的日期格式为yyyyMMdd
	 */
	public static String getYestoday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
	}

	/**
	 * 获取当前的日期yyyyMMdd
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 获取当前的时间yyyyMMddHHmmss
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * HTML字符转义
	 * 
	 * @see 对输入参数中的敏感字符进行过滤替换,防止用户利用JavaScript等方式输入恶意代码
	 * @see String input = <img src='http://t1.baidu.com/it/fm=0&gp=0.jpg'/>
	 * @see HtmlUtils.htmlEscape(input); //from spring.jar
	 * @see StringEscapeUtils.escapeHtml(input); //from commons-lang.jar
	 * @see 尽管Spring和Apache都提供了字符转义的方法,但Apache的StringEscapeUtils功能要更强大一些
	 * @see StringEscapeUtils提供了对HTML,Java,JavaScript,SQL,XML等字符的转义和反转义
	 * @see 但二者在转义HTML字符时,都不会对单引号和空格进行转义,而本方法则提供了对它们的转义
	 * @return String 过滤后的字符串
	 */
	public static String htmlEscape(String input) {
		if (isEmpty(input)) {
			return input;
		}
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll(" ", "&nbsp;");
		input = input.replaceAll("'", "&#39;"); // IE暂不支持单引号的实体名称,而支持单引号的实体编号,故单引号转义成实体编号,其它字符转义成实体名称
		input = input.replaceAll("\"", "&quot;"); // 双引号也需要转义，所以加一个斜线对其进行转义
		input = input.replaceAll("\n", "<br/>"); // 不能把\n的过滤放在前面，因为还要对<和>过滤，这样就会导致<br/>失效了
		return input;
	}

	/**
	 * 
	 * @param 字符创
	 * @return 字符创
	 * @gzkui
	 * @Date 15/10/16 对网页内容、js漏洞输出过滤，
	 */
	public static String getFormEscape(String input) {
		if (isEmpty(input)) {
			return input;
		}
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll(" ", "&nbsp;");
		input = input.replaceAll("'", "&#39;"); // IE�ݲ�֧�ֵ���ŵ�ʵ�����,��֧�ֵ���ŵ�ʵ����,�ʵ����ת���ʵ����,�����ַ�ת���ʵ�����
		input = input.replaceAll("\"", "&quot;"); // ˫���Ҳ��Ҫת�壬���Լ�һ��б�߶������ת��
		input = input.replaceAll("\n", "<br/>"); // ���ܰ�\n�Ĺ��˷���ǰ�棬��Ϊ��Ҫ��<��>���ˣ�����ͻᵼ��<br/>ʧЧ��
		return input;
	}

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return public static String getPingYin(String inputString) {
	 *         HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	 *         format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	 *         format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	 *         format.setVCharType(HanyuPinyinVCharType.WITH_V); char[] input =
	 *         inputString.trim().toCharArray(); String output = ""; try { for
	 *         (int i = 0; i < input.length; i++) { if (java.lang.Character
	 *         .toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) { String[]
	 *         temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
	 *         output += temp[0]; } else output +=
	 *         java.lang.Character.toString(input[i]); } } catch
	 *         (BadHanyuPinyinOutputFormatCombination e) { e.printStackTrace();
	 *         } return output; }
	 */

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音首字母 public static String getFirstSpell(String chinese) {
	 *         StringBuffer pybf = new StringBuffer(); char[] arr =
	 *         chinese.toCharArray(); HanyuPinyinOutputFormat defaultFormat =
	 *         new HanyuPinyinOutputFormat();
	 *         defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	 *         defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); for
	 *         (int i = 0; i < arr.length; i++) { if (arr[i] > 128) { try {
	 *         String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i],
	 *         defaultFormat); if (temp != null) {
	 *         pybf.append(temp[0].charAt(0)); } } catch
	 *         (BadHanyuPinyinOutputFormatCombination e) { e.printStackTrace();
	 *         } } else { pybf.append(arr[i]); } } return
	 *         pybf.toString().replaceAll("\\W", "").trim(); }
	 */

	/**
	 * 获取汉字串拼音，英文字符不变
	 * 
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音 public static String getFullSpell(String chinese) {
	 *         StringBuffer pybf = new StringBuffer(); char[] arr =
	 *         chinese.toCharArray(); HanyuPinyinOutputFormat defaultFormat =
	 *         new HanyuPinyinOutputFormat();
	 *         defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	 *         defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); for
	 *         (int i = 0; i < arr.length; i++) { if (arr[i] > 128) { try {
	 *         pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i],
	 *         defaultFormat)[0]); } catch
	 *         (BadHanyuPinyinOutputFormatCombination e) { e.printStackTrace();
	 *         } } else { pybf.append(arr[i]); } } return pybf.toString(); }
	 */

	/**
	 * @author lx
	 * @Date 15/10/16 随机生成25位字符串
	 */
	public static String getFromRom() {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789QWERTYUIOPASDFGHJKLZXCVBN";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 15; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	
	/**
	 * @author lx
	 * @Date 15/10/16 随机生成6位数组
	 */
	public static String getFromRomSix() {
		String base = "1234567890";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
		
	
	/**
	 * @author lx
	 * @Date 15/10/16 随机生成6位数组
	 */
	public static String getFromRom26() {
		String base = "1234567890";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 26; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	/**
	 * @author lx
	 * @Date 15/10/16 针对JSON解析时间错误修正
	 */
	@Deprecated
	public static String getFromDate2(Date str) {
		String strDate = null;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		if (!JiveGlobe.isEmpty(str)) {
			strDate = gson.toJson(str).toString();
		}
		if (strDate.length() < 2) {
			return strDate;
		}
		return JiveGlobe
				.getFromSpendSubstring(strDate, 1, strDate.length() - 1);
	}

	/**
	 * @author lx
	 * @Date 15/10/16 暴露给注册的枚举验证
	 */
	public static enum typeReturn {
		SUCCESS("返回成功"), FAILURE("程序异常"), FAILURE2("参数为空");
		private final String value;

		typeReturn(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 *  图片格式验证
	 */
	//验证图片格式
		public static boolean isValid(String pref){
			boolean isTrue = true;
			String[] fomat = {"jpg","JPG","png","PNG","jpeg","JPEG"};
			for(int i = 0; i<fomat.length;i++){
				if(fomat[i].equals(pref)){
					isTrue = false;
				}
			}
			return isTrue;
		}
		
		/**
		 * 文件验证
		 */
		public static  void extisFile(File path){
			if(!path.exists()){
				path.mkdirs();
			}
		}
		
		/**
		 * 文件夹生成
		 */
		
		public static void ceateFile(String path){
					 File  filePath =  new File(path);
					 if(!filePath.exists()){
						  filePath.mkdirs();
					 }
		}
		
	    /** 
         * byte(字节)根据长度转成kb(千字节)和mb(兆字节) 
         *  
         * @param bytes 
         * @return 
         */  
        public static String bytes2kb(long bytes) {  
            BigDecimal filesize = new BigDecimal(bytes);  
            BigDecimal megabyte = new BigDecimal(1024 * 1024);  
            float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP) .floatValue();  
            if (returnValue > 1)  
                return (returnValue + "MB");  
            BigDecimal kilobyte = new BigDecimal(1024);  
            returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)  .floatValue();  
            return (returnValue + "KB");  
        }  
        
        public static long bytesKb(int bytem){
        		if(bytem<2){
        			return 4242880;
        		}else{
        			return bytem*1000*1000;
        		}
        }
             
        
        
        /**
         * 生成随机数
         * @return
         */
        public static int getRomdom(){
        	Random rm = new Random();
        	return rm.nextInt(3);
        }
        
        /**
         *time zhuan  date
         * @return 
         * @throws ParseException 
         */
        
        public static Date getSystemToDate(String time) throws ParseException{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	//2016-08-06 11:11:24
        	Date date=sdf.parse(time);
			return date;
        }
        
        public static String getDateToString(Date time) throws ParseException{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        	//2016-08-06 11:11:24
        	//java.util.Date date=new java.util.Date();  
        	String str=sdf.format(time);  
			return str;
        }
        
        public static boolean isNumeric(String str){
  		  for (int i = 0; i < str.length(); i++){
  		   //System.out.println(str.charAt(i));
  		   if (!Character.isDigit(str.charAt(i))){
  		    return false;
  		   }
  		  }
  		  return true;
  		 }
    
}
