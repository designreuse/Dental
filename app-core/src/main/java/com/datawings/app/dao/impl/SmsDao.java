package com.datawings.app.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.datawings.app.common.DateUtil;
import com.datawings.app.common.SqlUtil;
import com.datawings.app.dao.ISmsDao;
import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;

@Repository
public class SmsDao extends BaseDao<Sms, Integer> implements ISmsDao{

	@SuppressWarnings("deprecation")
	public Integer getSmsRowCount(SmsFilter filter, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		if(filter.getSerial() != null && filter.getSerial() > 0){
			criteria.add(Restrictions.eq("serial", filter.getSerial()));
		}
		if(StringUtils.isNotBlank(filter.getFullName())){
			criteria.add(Restrictions.or(Restrictions.like("fullName", filter.getFullName().toUpperCase().trim(), MatchMode.ANYWHERE), 
					Restrictions.like("fullNameEn", filter.getFullName().toUpperCase().trim(), MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(filter.getPhone())){
			criteria.add(Restrictions.eq("phone", filter.getPhone().trim()));
		}
		if(StringUtils.isNotBlank(filter.getStatus())){
			criteria.add(Restrictions.eq("status", filter.getStatus().trim()));
		}
		if(StringUtils.isNotBlank(filter.getType())){
			criteria.add(Restrictions.eq("type", filter.getType().trim()));
		}
		SqlUtil.buildDatesBetweenLikeCrit("date_sent", filter.getDateFrom(), filter.getDateTo(), criteria);
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Sms> getlistSms(SmsFilter filter, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		if(filter.getSerial() != null && filter.getSerial() > 0){
			criteria.add(Restrictions.eq("serial", filter.getSerial()));
		}
		if(StringUtils.isNotBlank(filter.getFullName())){
			criteria.add(Restrictions.or(Restrictions.like("fullName", filter.getFullName().toUpperCase().trim(), MatchMode.ANYWHERE), 
					Restrictions.like("fullNameEn", filter.getFullName().toUpperCase().trim(), MatchMode.ANYWHERE)));
		}
		if(StringUtils.isNotBlank(filter.getPhone())){
			criteria.add(Restrictions.eq("phone", filter.getPhone().trim()));
		}
		if(StringUtils.isNotBlank(filter.getStatus())){
			criteria.add(Restrictions.eq("status", filter.getStatus().trim()));
		}
		if(StringUtils.isNotBlank(filter.getType())){
			criteria.add(Restrictions.eq("type", filter.getType().trim()));
		}
		SqlUtil.buildDatesBetweenLikeCrit("date_sent", filter.getDateFrom(), filter.getDateTo(), criteria);
		
		
		criteria.addOrder(Order.desc("createdDate"));
		criteria.setMaxResults(filter.getPageSize());
		criteria.setFirstResult(filter.getOffset());
		return  criteria.list();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<SmsFilter> getSmsStatistic(SmsFilter filter, SysUser sysUser) {
		StringBuffer hql = new StringBuffer();
		hql.append("select type, sum(count) as count");
		hql.append(" from sms");
		hql.append(" where status_code = '5'");
		
		if(StringUtils.isNotBlank(filter.getDateFrom().trim()) && StringUtils.isNotBlank(filter.getDateTo().trim()) ){
			if(DateUtil.checkDateAsString(filter.getDateFrom().trim(), "dd/MM/yyyy") 
					&& DateUtil.checkDateAsString(filter.getDateTo().trim(), "dd/MM/yyyy")){
					hql.append(" and date_sent between str_to_date('" + filter.getDateFrom().trim() + "', '%d/%m/%Y') and str_to_date('" + filter.getDateTo().trim() + "', '%d/%m/%Y')");
			}
		}
		else if(StringUtils.isNotBlank(filter.getDateFrom().trim())){
			if(DateUtil.checkDateAsString(filter.getDateFrom().trim(), "dd/MM/yyyy")){
				hql.append(" and date_sent > str_to_date('" + filter.getDateFrom().trim() + "', '%d/%m/%Y')");
			}
		}
		else if(StringUtils.isNotBlank(filter.getDateTo().trim())){
			if(DateUtil.checkDateAsString(filter.getDateFrom().trim(), "dd/MM/yyyy")){
				hql.append(" and date_sent < str_to_date('" + filter.getDateTo().trim() + "', '%d/%m/%Y')");
			}
		}
		hql.append(" group by type");
		hql.append(" order by date_sent desc");
		
		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
		List<SmsFilter> result = query
			.addScalar("type", new StringType())
			.addScalar("count", new IntegerType())
			.setResultTransformer(Transformers.aliasToBean(SmsFilter.class))
			.list();

		return result;
	}

	@Override
	public List<Sms> getAllWaitingSMS() {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		
		criteria.add(Restrictions.isNotNull("smsCode"));
		criteria.add(Restrictions.or(
						Restrictions.or(
							Restrictions.eq("statusCode","1"), 
							Restrictions.eq("statusCode","2")
						),
						Restrictions.or(
								Restrictions.eq("statusCode","3"), 
								Restrictions.eq("statusCode","100")
						)));

		return  criteria.list();
	}
	
}
