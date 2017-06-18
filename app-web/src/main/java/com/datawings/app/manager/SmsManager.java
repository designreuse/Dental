package com.datawings.app.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datawings.app.common.StringUtilz;
import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.ISmsService;

@Service
public class SmsManager{
	public static String API_KEY = "45DAD5014D38F45584F55D5E31D3B4";
	public static String SECRECT_KEY = "DF962A5E85B3F1F35C80437429CE34";
	public static String SEND_API_URL = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?";
	public static String CHECK_STATUS_URL = "https://restapi.esms.vn/MainService.svc/xml/GetSendStatus?";
	
	@Autowired
	public ISmsService smsService;
	
	public String sendSms(SmsFilter smsFilter, SysUser sysUser) {
		
		try {
			String phoneString = smsFilter.getPhoneSend();
			String[] phones = phoneString.trim().replaceAll(" ", "").split(";");
			for (String phone : phones) {
				smsFilter.setPhoneSend(phone);
				sendMessage(smsFilter,sysUser);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR:" + e.getMessage();
		}
		return "SUCCESS";
	}

	private void sendMessage(SmsFilter smsFilter, SysUser sysUser) {
		try{
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
			
			sms.setCount((StringUtilz.unAccent(message).length()/150) + 1);
			sms.setDateSent(new Date());
			sms.setCreatedDate(new Date());
			sms.setCreatedBy(sysUser.getUsername());
			
			if (smsId.length() > 1) {
				sms.setSmsCode(smsId);
				sms.setStatus(codeResult);
				sms.setStatusCode(codeResult);
			}else{
				sms.setStatus("Invalid");
			}
			
			
			smsService.merge(sms);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void updateSmsStatus() {
		List<Sms> listSms = smsService.getAllWaitingSMS();
		updateStatus(listSms);
	}

	private void updateStatus(List<Sms> listSms) {
		for (Sms sms : listSms) {
			try {
				String url = CHECK_STATUS_URL+ "RefId="+sms.getSmsCode()+"&ApiKey="+API_KEY+"&SecretKey="+SECRECT_KEY;
				URL obj;
				
				obj = new URL(url);
			
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				//you need to encode ONLY the values of the parameters
		            
				con.setRequestMethod("GET");
				con.setRequestProperty("Accept", "application/json");
				
				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);
				
				if(responseCode==200)//Đã gọi URL thành công, tuy nhiên bạn phải tự kiểm tra CodeResult xem tin nhắn có gửi thành công không, vì có thể tài khoản bạn không đủ tiền thì sẽ thất bại
				{
					String sendfailed = "";
					String sendstatus = "";
					String sendsuccess = "";
					String totalreceiver = "";
					String totalsent = "";
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
					String codeResponse = json.get("CodeResponse").toString();
					sendfailed = json.get("SendFailed").toString();
					sendstatus = json.get("SendStatus").toString();
					sendsuccess =json.get("SendSuccess").toString();
					totalreceiver =json.get("TotalReceiver").toString();
					totalsent = json.get("TotalSent").toString();
					
					if("105".equals(codeResponse)) {
						sms.setStatus("Invalid");
						sms.setStatusCode("0");
						sms.setCount(Integer.parseInt(sendsuccess));
					}else{
						sms.setStatus(sendstatus);
						sms.setStatusCode(sendstatus);
						sms.setCount(Integer.parseInt(sendsuccess));
					}
					
					sms.setModifiedDate(new Date());
					sms.setModifiedBy("System");
					smsService.merge(sms);
				}
				
				
			//document.getElementsByTagName("CountRegenerate").item(0).va
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
