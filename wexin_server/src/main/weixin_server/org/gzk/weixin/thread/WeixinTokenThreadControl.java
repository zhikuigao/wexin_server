package org.gzk.weixin.thread;

import org.apache.log4j.Logger;
import org.gzk.weixin.accessToken.AccessTokenCommon;
import org.gzk.weixin.accessToken.AcessToken;

import com.jws.common.constant.Constants;
import com.jws.common.util.EHCacheConfig;
import com.jws.common.util.JiveGlobe;

public class WeixinTokenThreadControl implements Runnable {
	private final Logger logger = Logger.getLogger(this.getClass());
	private static String timeout = Constants.tokenTimeout;
	@Override
	public void run() {
		while (true){
			try {
				AcessToken at = AccessTokenCommon.getInstance().getAcessToken();
				if(!JiveGlobe.isEmpty(at)){
					String accessToken = at.getAccess_token();
					EHCacheConfig.put("access_token", accessToken);
				}else{
					logger.info("###获取不到微信token");
				}
				Long  time = null;
				if(!JiveGlobe.isEmpty(timeout)){
					 time = Long.valueOf(timeout)*1000*60;
				}
				Thread.sleep(time,60 * 1000);
			} catch (NumberFormatException | InterruptedException e) {
				logger.error("###获取微信token"+e);
			}
		}
	}

}
