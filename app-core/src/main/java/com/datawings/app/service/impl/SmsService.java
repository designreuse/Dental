package com.datawings.app.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datawings.app.dao.ISmsDao;
import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.ISmsService;

@Service
public class SmsService implements ISmsService {

	@Autowired
	private ISmsDao dao;

	public Sms find(Serializable id) {
		return dao.find(id);
	}

	@Transactional
	public Serializable save(Sms model) {
		return dao.save(model);
	}

	@Transactional
	public void update(Sms model) {
		dao.update(model);
	}

	@Transactional
	public void merge(Sms model) {
		dao.merge(model);
	}

	@Transactional
	public void saveOrUpdate(Sms model) {
		dao.saveOrUpdate(model);
	}

	@Transactional
	public void delete(Sms model) {
		dao.delete(model);
	}

	@Transactional
	public void deleteById(Serializable id) {
		dao.deleteById(id);
	}
	
	@Transactional
	public List<Sms> findAll() {
		return dao.findAll();
	}

	@Transactional
	public Integer getSmsRowCount(SmsFilter filter, SysUser sysUser) {
		return dao.getSmsRowCount(filter, sysUser);
	}

	@Transactional
	public List<Sms> getlistSms(SmsFilter filter, SysUser sysUser) {
		return dao.getlistSms(filter, sysUser);
	}

	@Transactional
	public List<SmsFilter> getSmsStatistic(SmsFilter filter, SysUser sysUser) {
		return dao.getSmsStatistic(filter, sysUser);
	}

	@Override
	public List<Sms> getAllWaitingSMS() {
		return dao.getAllWaitingSMS();
	}
	
}
