package com.datawings.app.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datawings.app.dao.IMarketingDao;
import com.datawings.app.filter.MarketingFilter;
import com.datawings.app.model.Marketing;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.IMarketingService;

@Service
public class MarketingService implements IMarketingService {

	@Autowired
	private IMarketingDao dao;

	public Marketing find(Serializable id) {
		return dao.find(id);
	}

	@Transactional
	public Serializable save(Marketing model) {
		return dao.save(model);
	}

	@Transactional
	public void update(Marketing model) {
		dao.update(model);
	}

	@Transactional
	public void merge(Marketing model) {
		dao.merge(model);
	}

	@Transactional
	public void saveOrUpdate(Marketing model) {
		dao.saveOrUpdate(model);
	}

	@Transactional
	public void delete(Marketing model) {
		dao.delete(model);
	}

	@Transactional
	public void deleteById(Serializable id) {
		dao.deleteById(id);
	}

	@Transactional
	public Integer getMarketingCount(MarketingFilter filter, SysUser sysUser) {
		return dao.getMarketingCount(filter, sysUser);
	}

	@Transactional
	public List<Marketing> getMarketings(MarketingFilter filter, SysUser sysUser) {
		return dao.getMarketings(filter, sysUser);
	}
}
