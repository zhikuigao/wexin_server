package com.jws.app.operater.control;



import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.data.ProblemDataMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.ProblemData;
import com.jws.app.operater.model.User;
import com.jws.app.operater.model.UserInfo;
import com.jws.common.bean.Pagination;
import com.jws.common.constant.Constants;
import com.jws.common.util.CommonConstant;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.JsonUtil;
import com.jws.common.web.response.DataOut;
import com.jws.common.web.response.MessageOut;
import com.jws.common.web.response.Out;

/**
 * 
 * <br>
 * <b>类描述:</b>
 * 
 * <pre>
 *   论坛管理，这部分功能由论坛管理员操作，包括：创建论坛版块、指定论坛版块管理员、
 * 用户锁定/解锁。
 * </pre>
 * 
 * @see
 * @since
 */
@Controller
public class LoginController extends BaseController {
		private final Logger logger = Logger.getLogger(this.getClass());			
		@Resource
		private UserInfoMapper userInfoMapper;
		@Resource
		private ProblemDataMapper problemDataMapper;
	    /**
	     * 用户登陆
	     * @param request
	     * @param user
	     * @return
	     */
		@RequestMapping(value = "/doLogin.do")
		@ResponseBody
		public JSONObject login(HttpServletRequest request,  HttpServletResponse response ) {
//		UserInfo dbUser = userService.getUserByUserName(user.getUserName());
		String account = Constants.account;
		String pass = Constants.password;
		String email = request.getParameter("email");
		String psw = request.getParameter("psw");
//		ModelAndView mav = new ModelAndView();		
		if(account.equals(email) && psw.equals(pass)   ){
			setSessionUser(request, new User(account, pass));
			JSONObject jsonThree = new JSONObject(); 
			jsonThree = JsonUtil.addJsonObject("user", new User(account, pass), MessageOut.LOGIN_OK_MESSAGE);
			//System.out.println("###登录信息"+jsonThree);
			 //"forward:/WEB-INF/jsp/ceshi.html";
			// mav.setViewName("redirect :/main.html");
			return  jsonThree;
			//  return new ModelAndView("redirect:/login.html");
		//	return new ModelAndView("redirect:/WEB-INF/jsp/main.html");
		}else{
			//mav.addObject("message", "用户名不存在");
			String sr = MessageOut.LOGIN_FAIL;
			return null;
		}
	}
		
		/**
		 * 获取角色列表
		 */
		@RequestMapping("/user.do")
		@ResponseBody
		public Out<UserInfo>  user(Pagination pagination) {
			try {
			//	pagination.get
				int pageNumber = 10;
				int page = pagination.getPageNumber();
				if(page>0){
					pageNumber = page*10;
					page = (page-1)*10;
				}
				List<UserInfo> userList = userInfoMapper.queryUserInfoList(page,pageNumber);
				List<UserInfo> newUserList = new ArrayList<>();
				for(int i=0;i<userList.size();i++){
					UserInfo record = new UserInfo();
					record =userList.get(i);
					record.setNickname(java.net.URLDecoder.decode(record.getNickname(),"utf-8"));
					record.setSpareField3(Constants.linuximageSuo+record.getSpareField3());
					newUserList.add(record);
				}
			//	return  ;
				pagination.setTotalRows(userInfoMapper.queryUserInfoListCount());
				return new DataOut<UserInfo>(newUserList, pagination);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * 获取角色列表
		 */
		@RequestMapping("/list.do")
		@ResponseBody
		public Out<UserInfo> list(Pagination pagination) {
			try {
			//	pagination.get
				int pageNumber = 10;
				int page = pagination.getPageNumber();
				if(page>0){
					pageNumber = page*10;
					page = (page-1)*10;
				}
				List<UserInfo> userList = userInfoMapper.queryUserInfo(page,pageNumber);
				List<UserInfo> newUserList = new ArrayList<>();
				for(int i=0;i<userList.size();i++){
					UserInfo record = new UserInfo();
					record =userList.get(i);
					record.setNickname(java.net.URLDecoder.decode(record.getNickname(),"utf-8"));
					record.setSpareField3(Constants.linuximageSuo+record.getSpareField3());
					newUserList.add(record);
				}
				pagination.setTotalRows(userInfoMapper.queryUserInfoCount());
				return new DataOut<UserInfo>(newUserList, pagination);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
		/**
		 * 获取角色列表
		 */
		@RequestMapping("/problem.do")
		@ResponseBody
		public Out<ProblemData> problem(Pagination pagination) {
			try {
			//	pagination.get
				int pageNumber = 10;
				int page = pagination.getPageNumber();
				if(page>0){
					pageNumber = page*10;
					page = (page-1)*10;
				}
				List<ProblemData> userList = problemDataMapper.queryProblemAll(page,pageNumber);
				List<ProblemData> newUserList = new ArrayList<>();
				for(int i=0;i<userList.size();i++){
					ProblemData record = new ProblemData();
					record =userList.get(i);
					newUserList.add(record);
				}
				pagination.setTotalRows(problemDataMapper.queryProblemAllCount());
				return new DataOut<ProblemData>(newUserList, pagination);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * 修改绑定
		 */
		@RequestMapping("/operater/update.do")
		@ResponseBody
		public Out<Object> update(UserInfo obr, String ids) {
			try {
				//System.out.println(obr.toString());
				String name = obr.getNickname();
				if(!JiveGlobe.isEmpty(name)){
					name = URLEncoder.encode(name, "utf-8");
					obr.setNickname(name);
				}
				userInfoMapper.updateByPrimaryKey(obr);
				//this.operaterBindRoleService.updateBindsByOperater(obr.getUserId(), ids, this.getCurrentOperater());
				return MessageOut.UPDATE_OK_MESSAGE;
			} catch (Exception e) {
				System.out.println(e);
				logger.error("###编辑出错"+e);
			}
			return MessageOut.UPDATE_FAIL_MESSAGE;

		}
		
		/**
		 * 修改绑定
		 */
		@RequestMapping("/problem/update.do")
		@ResponseBody
		public Out<Object> updateProblem(ProblemData obr, String ids) {
			try {
				//System.out.println(obr.toString());
				problemDataMapper.updateByPrimaryKey(obr);
				//this.operaterBindRoleService.updateBindsByOperater(obr.getUserId(), ids, this.getCurrentOperater());
				return MessageOut.UPDATE_OK_MESSAGE;
			} catch (Exception e) {
			//	System.out.println(e);
				logger.error("###编辑出错"+e);
			}
			return MessageOut.UPDATE_FAIL_MESSAGE;

		}
		

		/**
		 * 退出
		 * @return String
		 */
		@RequestMapping("/logout.do")
		@ResponseBody
		public Out<Object> logout() {
			try {
				//System.out.println("进来了");
			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().removeAttribute(CommonConstant.USER_CONTEXT);
			} catch (Exception e) {
				logger.error("登录异常"+e);
			}
			return MessageOut.LOGOUT_OK_MESSAGE;
		}
		
}
