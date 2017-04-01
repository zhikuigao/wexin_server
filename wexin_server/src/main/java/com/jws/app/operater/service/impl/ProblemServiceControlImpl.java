package com.jws.app.operater.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;  

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.cloopen.rest.sdk.SDKTestSendTemplateSMS;
import com.jws.app.operater.data.IndusReportMapper;
import com.jws.app.operater.data.ProbelmPayRecordMapper;
import com.jws.app.operater.data.ProblemDataMapper;
import com.jws.app.operater.data.ProblemKillRecordMapper;
import com.jws.app.operater.data.ProblemShareRecordMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.ProbelmPayRecord;
import com.jws.app.operater.model.ProblemCount;
import com.jws.app.operater.model.ProblemData;
import com.jws.app.operater.model.ProblemKillRecord;
import com.jws.app.operater.model.ProblemShareRecord;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.model.UserInfoImage;
import com.jws.app.operater.service.ProblemServiceControl;
import com.jws.common.constant.Constants;
import com.jws.common.constant.ConstantsCode;
import com.jws.common.util.ImageUtirl;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.JsonUtil;
import com.jws.common.util.ResultPackaging;
@Service("problemServiceControlImpl")
public class ProblemServiceControlImpl implements ProblemServiceControl {
	private final Logger logger = Logger.getLogger(this.getClass());
	  public static String tupian = Constants.imageUrlBack + "background.jpg";
	  private static double weixinPayNoHelp = Double.valueOf(Constants.weixinPayNoHelp)/10;
	  private static double weixinPayHelpMan = Double.valueOf(Constants.weixinPayHelpMan)/10;
	  private static double weixinPayKillProblemMan = Double.valueOf(Constants.weixinPayKillProblemMan)/10;
	@Resource
	private IndusReportMapper indusReportMapper;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private ProblemDataMapper problemDataMapper;
	@Resource
	private ProblemKillRecordMapper problemKillRecordMapper;
	@Resource
	private ProblemShareRecordMapper problemShareRecordMapper;
	@Resource
	private ProbelmPayRecordMapper probelmPayRecordMapper;
	@Override
	public JSONObject queryProData() {
		JSONObject returnObject= new JSONObject();
		try {
		//	Properties ps= Constants.problemConfig;
			Properties ps = new Properties();  
			InputStream inputStream = this.getClass().getResourceAsStream("/problemConfig.properties");  
			BufferedReader bf = new BufferedReader(new  InputStreamReader(inputStream,"UTF-8"));
			ps.load(bf);  
			if(JiveGlobe.isEmpty(ps)){
				returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
				return returnObject;			
			}
			Map<String, List<String>> ms = new Hashtable<String, List<String>>();
			 Iterator<Entry<Object, Object>> it = ps.entrySet().iterator();  
		        while (it.hasNext()) {  
		        	List<String> lt = new ArrayList<String>();
		            Entry<Object, Object> entry = it.next();  
		            String value = entry.getValue().toString();  
		            String valueStr = "";
		            if(!JiveGlobe.isEmpty(value)){
		            	String[] str = value.split(",");
		            	for(int i=0;i<str.length;i++){
		            	if( i==0){
		            	 	valueStr = str[0];
		            	}else{
		            		 lt.add(str[i]);
		            	}
		            	}
		            }else{
		            	lt = null;
		            }
		            ms.put(valueStr, lt);
		           // System.out.println("key   :" + key);  
		           // System.out.println("value :" + value);  
		        }  
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, ms);
		} catch (Exception e) {
			logger.error("###获取问题分类error："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

	@Override
	public JSONObject addProblem(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
		if (null==json || !json.has("openid") ||JiveGlobe.isEmpty(json.optString("openid"))|| !json.has("noncestr") ||JiveGlobe.isEmpty(json.optString("noncestr"))|| !json.has("firstClass")||JiveGlobe.isEmpty(json.optString("firstClass")) || !json.has("secondClass") ||JiveGlobe.isEmpty(json.optString("secondClass"))||!json.has("content") ||JiveGlobe.isEmpty(json.optString("content")) || !json.has("money")  ||JiveGlobe.isEmpty(json.optString("money"))|| !json.has("lastTime")) {
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
			return returnObject;			
		}	
		try {
			String openid = json.optString("openid");
			String phone  = userInfoMapper.queryUserPhone(openid);
			if(JiveGlobe.isEmpty(phone)){
				return ResultPackaging.dealJsonObject(ConstantsCode.CODE_UPDATE_ERROR, ConstantsCode.CODE_UPDATE_ERROR, null);	
			}
			
			ProblemData pd = new ProblemData();
			String noncestr = json.optString("noncestr");
			String firstClass = json.optString("firstClass");
			String secondClass = json.optString("secondClass");
			String content = json.optString("content");
			String money = json.optString("money");
			String lastTime = json.optString("lastTime");
			pd.setStatusProblem(1);
			pd.setAmountMoney(Integer.valueOf(money));
			pd.setContentProblem(content);
			pd.setCreateTime(indusReportMapper.selectCurrTime());
			pd.setFirstClassification(firstClass);
			pd.setSecondClassification(secondClass);
			pd.setOpenid(openid);
			pd.setLastTime(JiveGlobe.getSystemToDate(lastTime));
			pd.setMobilePhone(phone);
			pd.setSpareField2(noncestr);
			this.problemDataMapper.insert(pd);
			int primarkeyId = this.problemDataMapper.queryPrimarkeyId(openid, money, content,noncestr);
			//同步支付表信息
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, primarkeyId);
		} catch (Exception e) {
			logger.error("###新增问题失败："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

	@Override
	public JSONObject queryProblemlist(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn ||  !josn.has("page") ||  !josn.has("pageNum") ) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			Integer page = josn.optInt("page");
			Integer pageNum =josn.optInt("pageNum");
			List<ProblemData> newQueryProblemlist = new ArrayList<ProblemData>();
			List<ProblemData> queryProblemlist = problemDataMapper.queryProblemlist(page,pageNum);
			int total = problemDataMapper.queryProblemlistCount();
			for(int i=0;i<queryProblemlist.size();i++){
				ProblemData pd =  queryProblemlist.get(i);
				int count  = problemDataMapper.queryCountShare(pd.getId());
				pd.setSpareField1(count+"");
				newQueryProblemlist.add(pd);
			}
			returnObject = JsonUtil.addJsonObject("total", total, returnObject);
			returnObject = JsonUtil.addJsonObject("problemList", newQueryProblemlist, returnObject);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, returnObject);
		} catch (Exception e) {
			logger.error("###查询问题列表失败："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	
	@Override
	public JSONObject solvProblemRecord(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("probelemId") ||  !josn.has("weixinId") ||  !josn.has("content")) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			int probelemId = josn.optInt("probelemId");
			String weixinId =josn.optString("weixinId");
			String content =josn.optString("content");
			ProblemKillRecord record = new ProblemKillRecord();	
			record.setCreateTime(indusReportMapper.selectCurrTime());
			record.setProbelemid(probelemId);
			record.setProblemWeixinid(weixinId);
			record.setSpareField1(content);
			problemKillRecordMapper.insert(record);
			//推送消息
			ProblemData pd = problemDataMapper.queryProblemDesc(probelemId+"");
			String phone = userInfoMapper.queryUserPhone(pd.getOpenid());
			if(!JiveGlobe.isEmpty(phone)){
				SDKTestSendTemplateSMS.getInstance().pushSmsNotice(phone, "118375");
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
		} catch (Exception e) {
			logger.error("###增加问题解答记录："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	@Override
	public JSONObject updateProStatus(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("id") ||!josn.has("problemStatus")) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			int probelemId = josn.optInt("id");
			int problemStatus = josn.optInt("problemStatus");
			problemDataMapper.updateProStatus(problemStatus, probelemId);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
		} catch (Exception e) {
			logger.error("###更新问题状态："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	@Override
	public JSONObject updateSolvProStatus(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("id") ||!josn.has("problemStatus") || !josn.has("openid") ||JiveGlobe.isEmpty(josn.optString("openid"))) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			int probelemId = josn.optInt("id");
			int problemStatus = josn.optInt("problemStatus");
			String openid = josn.optString("openid");
			if(!JiveGlobe.isEmpty(openid) && problemStatus<1){
				String phone = userInfoMapper.queryUserPhone(openid);
				if(!JiveGlobe.isEmpty(phone)){
					SDKTestSendTemplateSMS.getInstance().pushSmsNotice(phone, "118374");
				}
			}
		//	problemKillRecordMapper.updateSolvProStatus(problemStatus, probelemId);
			
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
		} catch (Exception e) {
			logger.error("###更新问题解决状态："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	@Override
	public JSONObject addShareRecord(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("problemId") ||!josn.has("shareOpenid") ||!josn.has("byShareOpenid")) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			int probelemId = josn.optInt("problemId");
			String shareOpenid = josn.optString("shareOpenid");
			String byShareOpenid = josn.optString("byShareOpenid");
			ProblemShareRecord record = new ProblemShareRecord();
			record.setCreateTime(indusReportMapper.selectCurrTime());
			record.setOpenid(shareOpenid);
			record.setOpenidBy(byShareOpenid);
			record.setProbelemid(probelemId);
			List<ProblemShareRecord> rocord = problemShareRecordMapper.queryProblemShareDetail(shareOpenid,probelemId);
			if(JiveGlobe.isEmpty(rocord)){
				problemShareRecordMapper.insert(record);
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, null);
		} catch (Exception e) {
			logger.error("###增加分享记录："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	
	
	@Override
	public JSONObject generateShareImage(JSONObject josn) {
		JSONObject returnObject= new JSONObject();		
		if (null==josn || !josn.has("money") ||!josn.has("content")  ||!josn.has("openid") || !josn.has("problemId") || JiveGlobe.isEmpty(josn.optString("openid"))|| JiveGlobe.isEmpty(josn.optString("problemId"))  || JiveGlobe.isEmpty(josn.optString("money")) ) {
			return returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);}	
		try {	
			UserInfo  user = userInfoMapper.queryPersonInfo(josn.optString("openid"));
			if(JiveGlobe.isEmpty(user) || JiveGlobe.isEmpty(user.getPhoto()) ||JiveGlobe.isEmpty(user.getNickname())){
				return returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);}
			String money = josn.optString("money");
			String content = josn.optString("content");
			String problemId  =josn.optString("problemId");
			String className=josn.optString("className");
			String photo = user.getPhoto();
			UserInfoImage record = new UserInfoImage();
			record.setContent(content);record.setPost(user.getPost());record.setMoney(money);
			record.setNickName(java.net.URLDecoder.decode(user.getNickname(),"utf-8"));
			record.setClassNmae(className);
			ImageUtirl iu = new ImageUtirl();
			String url1 = iu.pressText(tupian,photo, 1f,record,problemId,josn.optString("openid"));//测试OK
			//String url1 = iu.pressText(tupian,"http://wx.qlogo.cn/mmopen/ajNVdqHZLLCYjvDBJj0p7QcFoEEQzWQBdVh3ZOAvb1Als44aAz92IvdzevQwibW92GJUqiaUNZibJggLyu9uefUxA/0", 1f,record);//测试OK
			if(JiveGlobe.isEmpty(url1)){
				return ResultPackaging.dealJsonObject(ConstantsCode.CODE_REGISTER_FAIL, ConstantsCode.CODE_REGISTER_FAIL, null);
			}
			returnObject =ResultPackaging.dealJsonObjectString(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, url1);
		} catch (Exception e) {
			logger.error("###生成图片ERROR："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

	@Override
	public JSONObject queryProblemDesc(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==json || !json.has("id") ) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			String id = json.optString("id");
			if(!JiveGlobe.isNumeric(id)){
				returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, id);
				return returnObject;		
			}
			ProblemData record = this.problemDataMapper.queryProblemDesc(id);
			int count  = problemDataMapper.queryCountShare(Integer.valueOf(id));
			//查询问解决记录
			List<ProblemKillRecord> recordList = this.problemKillRecordMapper.queryProblemKillRecord(id);			
			returnObject = JsonUtil.addJsonObject("problemKillrecord", recordList, returnObject);
			returnObject = JsonUtil.addJsonObject("problemDesc", record, returnObject);
			returnObject = JsonUtil.addJsonObject("problemHelpCount", count, returnObject);
			
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, returnObject);
		} catch (Exception e) {
			logger.error("###获取问题详情失败："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

	@Override
	public JSONObject queryProblemKillRecord(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==json || !json.has("id") || JiveGlobe.isEmpty(json.optString("id"))) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			String id = json.optString("id");
			List<ProblemKillRecord> record = this.problemKillRecordMapper.queryProblemKillRecord(id);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, record);
		} catch (Exception e) {
			logger.error("###获取问题解决记录："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	public JSONObject problemAdopt(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==json || !json.has("id") || JiveGlobe.isEmpty(json.optString("id"))) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
				String id = json.getString("id");
				ProblemData pd = problemDataMapper.queryProblemDesc(id);
				//更改问题状态为采纳
				if(JiveGlobe.isEmpty(pd) || JiveGlobe.isEmpty(pd.getSpareField2() )){
					return ResultPackaging.dealJsonObject(ConstantsCode.CODE_VERIFY_ERROR, ConstantsCode.CODE_VERIFY_ERROR, null);
				}
				problemDataMapper.updateProStatus(4, Integer.valueOf(id));
				//发送支付信息到发送红包表
				int money = pd.getAmountMoney();
				double proportion = 0.5;
				//查询出解答问题的哥们
				ProblemKillRecord record = this.problemKillRecordMapper.queryProblemKillRecordFindOne(id);
				//查询出帮忙的哥们
				ProblemShareRecord recordBean = problemShareRecordMapper.queryProblemShare(record.getProblemWeixinid(),id);
				List<ProblemShareRecord> recordList = new ArrayList<ProblemShareRecord>();
				if(!JiveGlobe.isEmpty(recordBean)){
					recordList.add(recordBean);
					boolean isTrue  = true;
					while (isTrue){
						recordBean = problemShareRecordMapper.queryProblemShare(	recordBean.getOpenidBy(),id);
						//查询接力的上一层哥们
						if(!JiveGlobe.isEmpty(recordBean)){
							recordList.add(recordBean);
						}else{
							isTrue = false;
						}
					}
				}
				if(JiveGlobe.isEmpty(recordList)){
					//假如没人帮忙
					proportion = weixinPayNoHelp;
				}else{
					int lengthStr = recordList.size();
					double moneysHare = money*weixinPayHelpMan/lengthStr;
					for(int i=0;i<recordList.size();i++){
						ProblemShareRecord shareRecord= recordList.get(i);
						ProbelmPayRecord bean = new ProbelmPayRecord();
						bean.setAmountMoney(moneysHare);
						bean.setCreateTime(indusReportMapper.selectCurrTime());
						bean.setOpenid(shareRecord.getOpenidBy());
						bean.setPaymentType(3);
						bean.setPaymentStatus(2);
						bean.setProbelemid(Integer.valueOf(id));
						bean.setPaymentId(pd.getSpareField2());
						probelmPayRecordMapper.insert(bean);
					}
					proportion = weixinPayKillProblemMan;
				}
				//付钱给解答者
				ProbelmPayRecord bean = new ProbelmPayRecord();
				bean.setAmountMoney(money*proportion);
				bean.setCreateTime(indusReportMapper.selectCurrTime());
				bean.setPaymentType(2);
				bean.setPaymentStatus(2);
				bean.setProbelemid(Integer.valueOf(id));
				bean.setOpenid(record.getProblemWeixinid());
				bean.setPaymentId(pd.getSpareField2());
				probelmPayRecordMapper.insert(bean);
				returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, record);
		} catch (Exception e) {
			logger.error("###获取问题解决记录："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}  
	
	public JSONObject problemApplypay(JSONObject json) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==json || !json.has("openid") || JiveGlobe.isEmpty(json.optString("openid"))) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			String openid = json.optString("openid");
			List<ProblemData>  pdList = problemDataMapper.queryBalanceExtract(openid);
			if(JiveGlobe.isEmpty(pdList)){
				return ResultPackaging.dealJsonObject(ConstantsCode.CODE_VERIFY_INVALID, ConstantsCode.CODE_VERIFY_INVALID, null);
			}
			ProbelmPayRecord record = new ProbelmPayRecord();
			for(int i=0;i<pdList.size();i++){
				ProblemData pd = pdList.get(i);
				//关闭状态
				problemDataMapper.updateProStatus(6, pd.getId());
				//发送下次到提现
				record.setAmountMoney(pd.getAmountMoney()*0.9);
				record.setProbelemid(Integer.valueOf(pd.getId()));
				record.setCreateTime(indusReportMapper.selectCurrTime());
				record.setOpenid(pd.getOpenid());
				record.setPaymentType(1);
				record.setPaymentStatus(2);
				record.setPaymentId(pd.getSpareField2());
				probelmPayRecordMapper.insert(record);
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, record);
		} catch (Exception e) {
			logger.error("###获取问题解决记录："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}

	public JSONObject verificationProblem(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("openid") ||!josn.has("problemId") || JiveGlobe.isEmpty(josn.optString("problemId")) ||JiveGlobe.isEmpty(josn.optString("openid")) ) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			String probelemId = josn.optString("problemId");
			String openid = josn.optString("openid");
			int count = problemKillRecordMapper.queryProblemKillCount(probelemId, openid);
			if(count>2){
				return returnObject =ResultPackaging.dealJsonObject(ConstantsCode.CODE_UPLOAD_FAIL, ConstantsCode.CODE_UPLOAD_FAIL, count);
			}
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, count);
		} catch (Exception e) {
			logger.error("###返回问题解答状态次数："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	public JSONObject queryRespondentMoney(JSONObject josn) {
		JSONObject returnObject= new JSONObject();
		//验证参数
				if (null==josn || !josn.has("openid") ||!josn.has("problemId") || JiveGlobe.isEmpty(josn.optString("problemId")) ||JiveGlobe.isEmpty(josn.optString("openid")) ) {
					returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_LACK_PARAMETER, null);
					return returnObject;			
				}	
		try {
			double count  = 0;
			String probelemId = josn.optString("problemId");
			String openid = josn.optString("openid");
			ProbelmPayRecord pd = probelmPayRecordMapper.queryRespondentMoney(openid,Integer.valueOf(probelemId));
			int problemMan = probelmPayRecordMapper.queryRespondentMoneyCount(Integer.valueOf(probelemId));
			ProblemCount pc = new ProblemCount();
			if(!JiveGlobe.isEmpty(pd)){
				count = pd.getAmountMoney();
			}
			if(problemMan>0){
				problemMan = problemMan-1;
			}
			pc.setCount(count);
			pc.setNumberPartake(problemMan);
			returnObject =ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_SUCCESS, ConstantsCode.RESULT_CODE_SUCCESS, pc);
		} catch (Exception e) {
			logger.error("###获取解答信息："+e);
			return ResultPackaging.dealJsonObject(ConstantsCode.RESULT_CODE_FAIL, ConstantsCode.CODE_EXCEPTION, null);
		}
		return returnObject;
	}
	
	
}
