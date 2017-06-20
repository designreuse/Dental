package com.datawings.app.dao;

import java.util.List;

import com.datawings.app.bean.CustomerBean;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.filter.RecordsFilter;
import com.datawings.app.model.Records;
import com.datawings.app.model.SysUser;

public interface IRecordsDao extends IBaseDao<Records, Integer>{

	List<CustomerBean> getSchedule(RecordsFilter filter, SysUser sysUser);

	Integer getScheduleRowCount(RecordsFilter filter, SysUser sysUser);

	Integer getCountCustomerReality(CustomerFilter filter, SysUser sysUser);

	List<CustomerBean> getCustomerReality(CustomerFilter filter, SysUser sysUser);

	CustomerBean getTotalCustomerReality(CustomerFilter filter, SysUser sysUser);

	List<CustomerBean> getRecordDoctor(CustomerFilter filter, SysUser sysUser);

}
