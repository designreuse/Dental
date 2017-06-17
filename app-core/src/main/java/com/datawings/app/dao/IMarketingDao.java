package com.datawings.app.dao;

import java.util.List;

import com.datawings.app.filter.MarketingFilter;
import com.datawings.app.model.Marketing;
import com.datawings.app.model.SysUser;

public interface IMarketingDao extends IBaseDao<Marketing, Integer>{

	Integer getMarketingCount(MarketingFilter filter, SysUser sysUser);

	List<Marketing> getMarketings(MarketingFilter filter, SysUser sysUser);

}
