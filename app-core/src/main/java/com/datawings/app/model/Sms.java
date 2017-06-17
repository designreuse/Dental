package com.datawings.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.datawings.app.common.BeanUtil;

@Entity
@Table(name = "sms")
public class Sms extends Base {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue()
	@Column(name = "sms_id", unique = true, nullable = false)
	private Integer smsId;

	@Column(name = "phone")
	private String phone;

	@Column(name = "message")
	private String message;

	@Column(name = "type")
	private String type;

	@Column(name = "serial")
	private Integer serial;

	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "full_name_en")
	private String fullNameEn;


	@Column(name = "address")
	private String address;

	@Column(name = "sms_code")
	private String smsCode;

	@Column(name = "status")
	private String status;

	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "date_sent")
	private Date dateSent;
	
	@Column(name = "count")
	private Integer count;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date")
	private Date modifiedDate;

	public Sms() {
		init();
	}

	public void init() {
		BeanUtil.initSimplePropertyBean(this);
	}

	

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
	public Integer getSmsId() {
		return smsId;
	}

	public void setSmsId(Integer smsId) {
		this.smsId = smsId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}

	public String getFullNameEn() {
		return fullNameEn;
	}

	public void setFullNameEn(String fullNameEn) {
		this.fullNameEn = fullNameEn;
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

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if ("100".equals(status)) {
			this.status = "Request thành công";
		}else
		if ("99".equals(status)) {
			this.status = "Lỗi không xác định , thử lại sau";
		}else
		if ("101".equals(status)) {
			this.status = "Đăng nhập thất bại (api key hoặc secrect key không đúng )";
		}else
		if ("102".equals(status)) {
			this.status = "Tài khoản đã bị khóa";
		}else
		if ("103".equals(status)) {
			this.status = "Số dư tài khoản không đủ dể gửi tin";
		}else
		if ("104".equals(status)) {
			this.status = "Mã Brandname không đúng";
		}else {
			this.status = "Error";
		}
		
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
