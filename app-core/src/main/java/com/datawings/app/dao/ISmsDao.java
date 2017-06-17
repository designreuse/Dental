package com.datawings.app.dao;

import java.util.List;

import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;

public interface ISmsDao extends IBaseDao<Sms, Integer>{

	Integer getSmsRowCount(SmsFilter filter, SysUser sysUser);

	List<Sms> getlistSms(SmsFilter filter, SysUser sysUser);

	List<SmsFilter> getSmsStatistic(SmsFilter filter, SysUser sysUser);

}
