package com.datawings.app.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.datawings.app.common.DentalUtils;
import com.datawings.app.common.SqlUtil;
import com.datawings.app.dao.ICustomerDao;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.model.Customer;
import com.datawings.app.model.CustomerId;
import com.datawings.app.model.SysUser;

@Repository
public class CustomerDao extends BaseDao<Customer, CustomerId> implements ICustomerDao{

	@SuppressWarnings("deprecation")
	public Integer getCustomerRowCount(CustomerFilter filter, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			criteria.add(Restrictions.eq("branch", sysUser.getBranch()));
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				criteria.add(Restrictions.eq("branch", filter.getBranch()));
			}
		}
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_DOCTOR)){
			criteria.add(Restrictions.eq("dentist", sysUser.getName().trim()));
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			criteria.add(Restrictions.eq("serial", filter.getSerial().trim()));
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
		if(StringUtils.isNotBlank(filter.getSource())){
			criteria.add(Restrictions.eq("source", filter.getSource().trim()));
		}
		SqlUtil.buildDatesBetweenLikeCrit("arrival_date", filter.getArrivalDateFrom(), filter.getArrivalDateTo(), criteria);
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Customer> getCustomers(CustomerFilter filter, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			criteria.add(Restrictions.eq("branch", sysUser.getBranch()));
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				criteria.add(Restrictions.eq("branch", filter.getBranch()));
			}
		}
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_DOCTOR)){
			criteria.add(Restrictions.eq("dentist", sysUser.getName().trim()));
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			criteria.add(Restrictions.eq("serial", filter.getSerial().trim()));
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
		if(StringUtils.isNotBlank(filter.getSource())){
			criteria.add(Restrictions.eq("source", filter.getSource().trim()));
		}
		SqlUtil.buildDatesBetweenLikeCrit("arrival_date", filter.getArrivalDateFrom(), filter.getArrivalDateTo(), criteria);
		
		criteria.addOrder(Order.desc("serial"));
		criteria.setMaxResults(filter.getPageSize());
		criteria.setFirstResult(filter.getOffset());
		return  criteria.list();
	}

	@SuppressWarnings("deprecation")
	public Customer findByUser(Integer serial, SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		criteria.add(Restrictions.eq("serial", serial));
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			criteria.add(Restrictions.eq("branch", sysUser.getBranch()));
		}
		return (Customer) criteria.uniqueResult();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Customer> getCustomer(String branch) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		criteria.add(Restrictions.eq("branch", branch));
		
		criteria.addOrder(Order.desc("serial"));
		return  criteria.list();
	}

	@SuppressWarnings("deprecation")
	public Integer maxSerial(SysUser sysUser) {
		Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());
		criteria.setProjection(Projections.max("serial"));
		criteria.add(Restrictions.eq("branch", sysUser.getBranch().trim()));
		
		Integer rs = (Integer) criteria.uniqueResult();
		if(rs == null){
			return 0;
		}
		return rs;
	}

	public void updateContent(Customer customer) {
		StringBuffer hql =new StringBuffer();
		hql.append("update customer set content = '" + customer.getContent() + "'");
		hql.append(" where customer_id =" + customer.getCustomerId());
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		query.executeUpdate();
	}

}
