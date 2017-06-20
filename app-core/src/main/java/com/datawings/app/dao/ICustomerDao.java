package com.datawings.app.dao;

import java.util.List;

import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.model.Customer;
import com.datawings.app.model.CustomerId;
import com.datawings.app.model.SysUser;

public interface ICustomerDao extends IBaseDao<Customer, CustomerId>{

	Integer getCustomerRowCount(CustomerFilter filter, SysUser sysUser);

	List<Customer> getCustomers(CustomerFilter filter, SysUser sysUser);

	Customer findByUser(Integer serial, SysUser sysUser);

	List<Customer> getCustomer(String branch);

	Integer maxSerial(SysUser sysUser);

	void updateContent(Customer customer);
	
}
