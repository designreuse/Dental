package com.datawings.app.filter;

public class SmsFilter extends BaseFilter {
	private String phone;
	private String type;
	private String serial;
	private String fullName;
	private String address;
	private String status;
	private String dateFrom;
	private String dateTo;
	
	private String typeSend;
	private String phoneSend;
	private String messageSend;
	
	private Integer unit;
	private Integer count;
	private Integer gross;

	public SmsFilter() {
		init();
	}

	public void init() {
		super.init();
	}

	public String getTypeSend() {
		return typeSend;
	}

	public void setTypeSend(String typeSend) {
		this.typeSend = typeSend;
	}

	public String getPhoneSend() {
		return phoneSend;
	}

	public void setPhoneSend(String phoneSend) {
		this.phoneSend = phoneSend;
	}

	public String getMessageSend() {
		return messageSend;
	}

	public void setMessageSend(String messageSend) {
		this.messageSend = messageSend;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getGross() {
		return gross;
	}

	public void setGross(Integer gross) {
		this.gross = gross;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

}
