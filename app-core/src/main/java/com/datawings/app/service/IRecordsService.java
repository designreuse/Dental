package com.datawings.app.service;

import java.io.Serializable;
import java.util.List;

import com.datawings.app.bean.CustomerBean;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.filter.RecordsFilter;
import com.datawings.app.model.Records;
import com.datawings.app.model.SysUser;

public interface IRecordsService {
	
	public Records find(Serializable userId);

	public Serializable save(Records model);

	public void update(Records model);

	public void merge(Records model);

	public void saveOrUpdate(Records model);

	public void delete(Records model);

	public void deleteById(Serializable id);

	public List<CustomerBean> getSchedule(RecordsFilter filter, SysUser sysUser);

	public Integer getScheduleRowCount(RecordsFilter filter, SysUser sysUser);

	public Integer getCountCustomerReality(CustomerFilter filter, SysUser sysUser);

	public List<CustomerBean> getCustomerReality(CustomerFilter filter,SysUser sysUser);

	public CustomerBean getTotalCustomerReality(CustomerFilter filter, SysUser sysUser);

	public List<CustomerBean> getRecordDoctor(CustomerFilter filter, SysUser sysUser);

}
