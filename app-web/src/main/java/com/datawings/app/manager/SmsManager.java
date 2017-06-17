package com.datawings.app.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datawings.app.common.StringUtilz;
import com.datawings.app.dao.ISmsDao;
import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.ISmsService;

@Service
public class SmsManager{
	public static String API_KEY = "45DAD5014D38F45584F55D5E31D3B4";
	public static String SECRECT_KEY = "DF962A5E85B3F1F35C80437429CE34";
	public static String SEND_API_URL = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?";
	
	@Autowired
	public ISmsService smsService;
	
	public String sendSms(SmsFilter smsFilter, SysUser sysUser) {
		
		try {
			API_KEY = URLEncoder.encode(API_KEY, "UTF-8");
			SECRECT_KEY = URLEncoder.encode(SECRECT_KEY, "UTF-8");
			String phone = URLEncoder.encode(smsFilter.getPhoneSend(), "UTF-8");
			String message = URLEncoder.encode(smsFilter.getMessageSend(), "UTF-8");
			String type = URLEncoder.encode(smsFilter.getTypeSend(), "UTF-8");
			String url = SEND_API_URL + "ApiKey=" + API_KEY + "&SecretKey=" + SECRECT_KEY + "&SmsType="+ type +"&Phone=" + phone + "&Content=" + message;
			URL obj;
			
			obj = new URL(url);
		
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//you need to encode ONLY the values of the parameters
	            
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			
			String smsId = "";
			String codeResult = "";
		
			if(responseCode==200)//Đã gọi URL thành công, tuy nhiên bạn phải tự kiểm tra CodeResult xem tin nhắn có gửi thành công không, vì có thể tài khoản bạn không đủ tiền thì sẽ thất bại
			{
				//Đọc Response
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
	 
				//print result
				JSONObject json = new JSONObject(response.toString());
				
				codeResult = (String) json.get("CodeResult");
				if ("100".equals(codeResult)){
					smsId = (String) json.get("SMSID");
				} 
				
			}
			
			Sms sms = new Sms();
			sms.setSmsId(null);
			sms.setPhone(smsFilter.getPhoneSend());
			sms.setMessage(smsFilter.getMessageSend());
			sms.setType(smsFilter.getTypeSend());
			
			sms.setAddress(smsFilter.getAddress());
			sms.setFullName(smsFilter.getFullName());
			sms.setFullNameEn(StringUtilz.unAccent(smsFilter.getFullName()));
			sms.setSerial(smsFilter.getSerial());
			sms.setSmsCode(smsId);
			sms.setStatus(codeResult);
			sms.setCount((StringUtilz.unAccent(message).length()/150) + 1);
			sms.setDateSent(new Date());
			sms.setCreatedDate(new Date());
			sms.setCreatedBy(sysUser.getUsername());
			
			smsService.merge(sms);
		//document.getElementsByTagName("CountRegenerate").item(0).va
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "ERROR:" + e.getMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR:" + e.getMessage();
		}
		return "SUCCESS";
	}
}
