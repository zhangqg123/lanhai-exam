package com.jeecg.signin.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecg.lhs.entity.LhSAccountEntity;
import com.jeecg.lhs.service.LhSAccountService;
import com.jeecg.signin.dao.SigninDateDao;
import com.jeecg.signin.entity.SigninDateEntity;
import com.jeecg.signin.service.SigninDateService;
import com.jeecg.signin.util.CalendarUtil;
import com.jeecg.signin.util.LocationUtils;
import com.jeecg.signin.util.WxApi;


/**
 * 这是小程序-小诗词的rest请求
 */
@Controller
@RequestMapping("/api/signin")
public class RestController {
	
	@Autowired
	private LhSAccountService lhSAccountService;

	@Autowired
	private SigninDateService signinDateService;
	
	@RequestMapping("/getOpenid")
	@ResponseBody
	public String getOpenid(String jscode,String xcxId){
		//微信小程序id
		String appId = xcxId;	
		LhSAccountEntity lhSAccount=lhSAccountService.getByAppId(appId);
		//微信小程序Secret
		String appSecret = lhSAccount.getAppSecret();
		
		//获取请求的url
		String url  = WxApi.getJsCodeSessionUrl(appId, appSecret, jscode);
		JSONObject jsonObject = WxApi.httpsRequest(url, "GET", null);
		
		//获取openid
		String openid = "";
		if (null != jsonObject && !jsonObject.containsKey("errcode")) {
			try {
				openid = jsonObject.getString("openid");
			} catch (JSONException e) {
			}
		}
		return openid;
	}
	
	//获取签到数据
	@RequestMapping("/getSignDates")
//	@RequestMapping(params="getSignDates",method = RequestMethod.GET)
	@ResponseBody
	public String getSignDates(String openid, Integer year, Integer month){
		SigninDateEntity queryEntity = new SigninDateEntity();
		queryEntity.setOpenid(openid);//设置用户的openid
		queryEntity.setYear(year);//设置年
		queryEntity.setMonth(month);//设置月
		//获取已经签到的数据
		MiniDaoPage<SigninDateEntity> signinlist = signinDateService.getAll(queryEntity, 1, 50);
		List<SigninDateEntity> list = signinlist.getResults();
		//将日期返回
		List<Integer> days = new ArrayList<Integer>();
		if(CollectionUtils.isNotEmpty(list)){
			for(SigninDateEntity item : list){
				days.add(item.getDay());
			}
		}
		return JSONArray.fromObject(days).toString();
	}
	
	//实现签到
	@RequestMapping("/doSign")
//	@RequestMapping(params = "doSign",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String doSign(String openid,String lat,String lng,String appId){
		String message="0";
		SigninDateEntity entity = new SigninDateEntity();
		entity.setOpenid(openid);
		entity.setYear(CalendarUtil.getYear());
		entity.setMonth(CalendarUtil.getMonth());
		entity.setDay(CalendarUtil.getDate());
		
		//先判断是否已经签过到
		MiniDaoPage<SigninDateEntity> signinlist = signinDateService.getAll(entity, 1, 50);
		List<SigninDateEntity> list = signinlist.getResults();
		if(CollectionUtils.isNotEmpty(list)){
			//已经签过到，不做任何处理
			message="2";
		}else{
//			LhSAccountEntity lhSAccount=lhSAccountService.getByAppId(appId);
//			double distance = LocationUtils.getDistance(Double.valueOf(lat),Double.valueOf(lng), Double.valueOf(lhSAccount.getLat()), Double.valueOf(lhSAccount.getLng()));
//			if(distance>100){
//				message="3";
//			}else{
				entity.setTime(new Date());
				signinDateService.insert(entity);//加入今日签到数据
				message="1";
//			}
		}
		
		return message;
	}
	
}
