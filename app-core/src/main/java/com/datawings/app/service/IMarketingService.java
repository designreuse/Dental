package com.datawings.app.service;

import java.io.Serializable;
import java.util.List;

import com.datawings.app.filter.MarketingFilter;
import com.datawings.app.model.Marketing;
import com.datawings.app.model.SysUser;

public interface IMarketingService {
	
	public Marketing find(Serializable userId);

	public Serializable save(Marketing model);

	public void update(Marketing model);

	public void merge(Marketing model);

	public void saveOrUpdate(Marketing model);

	public void delete(Marketing model);

	public void deleteById(Serializable id);

	public Integer getMarketingCount(MarketingFilter filter, SysUser sysUser);

	public List<Marketing> getMarketings(MarketingFilter filter, SysUser sysUser);
}
