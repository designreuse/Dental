package com.datawings.app.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datawings.app.dao.ICustomerDao;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.model.Customer;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.ICustomerService;

@Service
public class CustomerService implements ICustomerService{

	@Autowired
	private ICustomerDao dao;
	
	//@Transactional
	public Customer find(Serializable id) {
		return dao.find(id);
	}

	@Transactional
	public Serializable save(Customer model) {
		return dao.save(model);
	}

	@Transactional
	public void update(Customer model) {
		dao.update(model);
	}

	@Transactional
	public void merge(Customer model) {
		dao.merge(model);
	}

	@Transactional
	public void saveOrUpdate(Customer model) {
		dao.saveOrUpdate(model);
	}

	@Transactional
	public void delete(Customer model) {
		dao.delete(model);
	}

	@Transactional
	public void deleteById(Serializable id) {
		dao.deleteById(id);
	}

	@Transactional
	public Integer getCustomerRowCount(CustomerFilter filter, SysUser sysUser) {
		return dao.getCustomerRowCount(filter, sysUser);
	}

	public List<Customer> getCustomers(CustomerFilter filter, SysUser sysUser) {
		return dao.getCustomers(filter, sysUser);
	}

	public Customer findByUser(Integer serial, SysUser sysUser) {
		return dao.findByUser(serial, sysUser);
	}

	@Transactional
	public List<Customer> getCustomer(String branch) {
		return dao.getCustomer(branch);
	}

	@Transactional
	public Integer maxSerial(SysUser sysUser) {
		return dao.maxSerial(sysUser);
	}

	@Transactional
	public void updateContent(Customer customer) {
		dao.updateContent(customer);
	}
}
