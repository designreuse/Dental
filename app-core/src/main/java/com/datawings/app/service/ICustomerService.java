package com.datawings.app.service;

import java.io.Serializable;
import java.util.List;

import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.model.Customer;
import com.datawings.app.model.SysUser;

public interface ICustomerService {
	
	public Customer find(Serializable userId);

	public Serializable save(Customer model);

	public void update(Customer model);

	public void merge(Customer model);

	public void saveOrUpdate(Customer model);

	public void delete(Customer model);

	public void deleteById(Serializable id);

	public Integer getCustomerRowCount(CustomerFilter filter, SysUser sysUser);

	public List<Customer> getCustomers(CustomerFilter filter, SysUser sysUser);

	public Customer findByUser(Integer serial, SysUser sysUser);

	public List<Customer> getCustomer(String branch);
	
	public Integer maxSerial(SysUser sysUser);

	public void updateContent(Customer customer);

}
