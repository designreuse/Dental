package com.datawings.app.form;

import com.datawings.app.filter.BaseFilter;

public class SmsForm extends BaseFilter {
	public Integer smsType;
	public String toPhone;
	public String message;

	
	public SmsForm() {
		init();
	}

	public void init() {
		super.init();
	}

	public Integer getSmsType() {
		return smsType;
	}

	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}

	public String getToPhone() {
		return toPhone;
	}

	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
