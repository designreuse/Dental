package com.datawings.app.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datawings.app.dao.IMarketingDao;
import com.datawings.app.filter.MarketingFilter;
import com.datawings.app.model.Marketing;
import com.datawings.app.model.SysUser;

@Repository
public class MarketingDao extends BaseDao<Marketing, Integer> implements IMarketingDao{

	@SuppressWarnings("deprecation")
	public Integer getMarketingCount(MarketingFilter filter, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		if(StringUtils.isNotBlank(filter.getBranch())){
			criteria.add(Restrictions.eq("branch", filter.getBranch()));
		}
	
		if(StringUtils.isNotBlank(filter.getFullName())){
			criteria.add(Restrictions.like("fullName", filter.getFullName().toUpperCase().trim(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filter.getAddress())){
			criteria.add(Restrictions.like("address", filter.getAddress().toUpperCase().trim(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filter.getPhone())){
			criteria.add(Restrictions.like("phone", filter.getPhone().trim(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filter.getStatus())){
			criteria.add(Restrictions.eq("status", filter.getStatus().trim()));
		}
	
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Marketing> getMarketings(MarketingFilter filter, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		if(StringUtils.isNotBlank(filter.getBranch())){
			criteria.add(Restrictions.eq("branch", filter.getBranch()));
		}
	
		if(StringUtils.isNotBlank(filter.getFullName())){
			criteria.add(Restrictions.like("fullName", filter.getFullName().toUpperCase().trim(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filter.getAddress())){
			criteria.add(Restrictions.like("address", filter.getAddress().toUpperCase().trim(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filter.getPhone())){
			criteria.add(Restrictions.like("phone", filter.getPhone().trim(), MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(filter.getStatus())){
			criteria.add(Restrictions.eq("status", filter.getStatus().trim()));
		}
		criteria.addOrder(Order.desc("marketingId"));
		criteria.setMaxResults(filter.getPageSize());
		criteria.setFirstResult(filter.getOffset());
		
		return  criteria.list();
	}

}
