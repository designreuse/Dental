package com.datawings.app.service;

import java.io.Serializable;
import java.util.List;

import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;

public interface ISmsService {
	
	public Sms find(Serializable smsId);

	public Serializable save(Sms model);

	public void update(Sms model);

	public void merge(Sms model);

	public void saveOrUpdate(Sms model);

	public void delete(Sms model);

	public void deleteById(Serializable id);
	
	public List<Sms> findAll();

	public Integer getSmsRowCount(SmsFilter filter, SysUser sysUser);

	public List<Sms> getlistSms(SmsFilter filter, SysUser sysUser);

	public List<SmsFilter> getSmsStatistic(SmsFilter filter, SysUser sysUser);
}
