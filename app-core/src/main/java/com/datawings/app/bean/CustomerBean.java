package com.datawings.app.bean;

import java.util.Date;

import com.datawings.app.common.BeanUtil;

public class CustomerBean {
	public Integer customerId;
	public String serial;
	public String fullName;
	public String phone;
	public Date dateExcute;
	public String content;
	public String contentNext;
	public String dentist;
	public String branch;

	public Integer gross;
	public Integer sale;
	public Integer payment;

	public CustomerBean() {
		init();
	}

	public void init() {
		BeanUtil.initSimplePropertyBean(this);
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getDateExcute() {
		return dateExcute;
	}

	public void setDateExcute(Date dateExcute) {
		this.dateExcute = dateExcute;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentNext() {
		return contentNext;
	}

	public void setContentNext(String contentNext) {
		this.contentNext = contentNext;
	}

	public String getDentist() {
		return dentist;
	}

	public void setDentist(String dentist) {
		this.dentist = dentist;
	}

	public Integer getGross() {
		return gross;
	}

	public void setGross(Integer gross) {
		this.gross = gross;
	}

	public Integer getSale() {
		return sale;
	}

	public void setSale(Integer sale) {
		this.sale = sale;
	}

	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}

}
