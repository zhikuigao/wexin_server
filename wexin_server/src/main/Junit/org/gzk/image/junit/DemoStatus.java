package org.gzk.image.junit;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.hypertable.FsBroker.hadoop.main;

public class DemoStatus {
			public static void main(String[] args) {
				JSONObject json = new JSONObject();
				String  s = "problemId:2,openid:ceshi";
				//String problem = s.split(",")[0].split(":")[0];
				System.out.println(s.split(",").length);
				if(s.split(",").length>1){
					String problemId = s.split(",")[0].split(":")[1];
					String openid = s.split(",")[1].split(":")[1];
					System.out.println("问题ID："+problemId);
					System.out.println("openID："+openid);
				}

//				
			}
}
