package com.datawings.app.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.datawings.app.bean.CustomerBean;
import com.datawings.app.common.DentalUtils;
import com.datawings.app.common.SqlUtil;
import com.datawings.app.dao.IRecordsDao;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.filter.RecordsFilter;
import com.datawings.app.model.Records;
import com.datawings.app.model.SysUser;

@Repository
public class RecordsDao extends BaseDao<Records, Integer> implements IRecordsDao{

	@SuppressWarnings({ "unchecked" })
	public List<CustomerBean> getSchedule(RecordsFilter filter, SysUser sysUser) {
		
		StringBuffer hql =new StringBuffer();
		hql.append("select c.customer_id as customerId, c.serial as serial, full_name as fullName, phone as phone, r.date_next as dateExcute");
		hql.append(", c.content as content, r.content_next as contentNext, r.dentist as dentist, r.branch as branch"); 
		hql.append(", c.gross as gross, c.sale as sale, c.payment as payment"); 
		hql.append(" from customer c, records r");
		hql.append(" where c.customer_id = r.customer_id ");
		hql.append(" and date_next is not null");
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			hql.append(" and c.branch = '" + sysUser.getBranch() + "'");
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and c.branch = '" + filter.getBranch() + "'");
			}
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			hql.append(" and c.serial = '" + filter.getSerial() + "'");
		}
		if(StringUtils.isNotBlank(filter.getName())){
			hql.append(" and (full_name like '%" + filter.getName().toUpperCase() + "%' or full_name_en like '%" + filter.getName().toUpperCase() + "%')");
		}
		if(StringUtils.isNotBlank(filter.getTelephone())){
			hql.append(" and phone = '" + filter.getTelephone() + "'");
		}
		if(StringUtils.isNotBlank(filter.getDentist())){
			hql.append(" and r.dentist = '" + filter.getDentist() + "'");
		}
		SqlUtil.buildDatesBetweenLikeHql("date_next", filter.getDateNextFrom(), filter.getDateNextTo(), hql);
		hql.append(" order by serial");
		hql.append(" limit " +  filter.getPageSize() + " offset "+ filter.getOffset());
		
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		
		List<CustomerBean> result = query
				.addScalar("customerId", IntegerType.INSTANCE)
				.addScalar("serial", StringType.INSTANCE)
				.addScalar("fullName", StringType.INSTANCE)
				.addScalar("phone", StringType.INSTANCE)
				.addScalar("dateExcute", DateType.INSTANCE)
				.addScalar("content", StringType.INSTANCE)
				.addScalar("contentNext", StringType.INSTANCE)
				.addScalar("dentist", StringType.INSTANCE)
				.addScalar("branch", StringType.INSTANCE)
				.addScalar("gross", IntegerType.INSTANCE)
				.addScalar("sale", IntegerType.INSTANCE)
				.addScalar("payment", IntegerType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(CustomerBean.class)).list();
		return result;
	}

	public Integer getScheduleRowCount(RecordsFilter filter, SysUser sysUser) {
		StringBuffer hql =new StringBuffer();
		hql.append("select count(*)");
		hql.append(" from customer c, records r");
		hql.append(" where c.customer_id = r.customer_id ");
		hql.append(" and date_next is not null");
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			hql.append(" and c.branch = '" + sysUser.getBranch() + "'");
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and c.branch = '" + filter.getBranch() + "'");
			}
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			hql.append(" and c.serial = '" + filter.getSerial() + "'");
		}
		if(StringUtils.isNotBlank(filter.getName())){
			hql.append(" and (full_name like '%" + filter.getName().toUpperCase() + "%' or full_name_en like '%" + filter.getName().toUpperCase() + "%')");
		}
		if(StringUtils.isNotBlank(filter.getTelephone())){
			hql.append(" and phone = '" + filter.getTelephone() + "'");
		}
		if(StringUtils.isNotBlank(filter.getDentist())){
			hql.append(" and r.dentist = '" + filter.getDentist() + "'");
		}
		SqlUtil.buildDatesBetweenLikeHql("date_next", filter.getDateNextFrom(), filter.getDateNextTo(), hql);
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		return ((BigInteger) query.uniqueResult()).intValue();
	}

	public Integer getCountCustomerReality(CustomerFilter filter, SysUser sysUser) {
		StringBuffer hql =new StringBuffer();
		hql.append("select count(*)");
		hql.append(" from customer c, records r");
		hql.append(" where c.customer_id = r.customer_id ");
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			hql.append(" and c.branch = '" + sysUser.getBranch() + "'");
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and c.branch = '" + filter.getBranch() + "'");
			}
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			hql.append(" and c.serial = '" + filter.getSerial() + "'");
		}
		if(StringUtils.isNotBlank(filter.getFullName())){
			hql.append(" and (full_name like '%" + filter.getFullName().toUpperCase() + "%' or full_name_en like '%" + filter.getFullName().toUpperCase() + "%')");
		}
		if(StringUtils.isNotBlank(filter.getPhone())){
			hql.append(" and phone = '" + filter.getPhone() + "'");
		}
		if(StringUtils.isNotBlank(filter.getDentist())){
			hql.append(" and r.dentist = '" + filter.getDentist() + "'");
		}
		if(StringUtils.isNotBlank(filter.getSource())){
			hql.append(" and c.source = '" + filter.getSource() + "'");
		}
		SqlUtil.buildDatesBetweenLikeHql("date_excute", filter.getArrivalDateFrom(), filter.getArrivalDateTo(), hql);
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		return ((BigInteger) query.uniqueResult()).intValue();
	}

	@SuppressWarnings({ "unchecked" })
	public List<CustomerBean> getCustomerReality(CustomerFilter filter, SysUser sysUser) {
		StringBuffer hql =new StringBuffer();
		hql.append("select c.customer_id as customerId, c.serial as serial, full_name as fullName, phone as phone, r.date_excute as dateExcute");
		hql.append(", r.content as content, r.dentist as dentist, r.branch as branch"); 
		hql.append(", r.payment as payment"); 
		hql.append(" from customer c, records r");
		hql.append(" where c.customer_id = r.customer_id ");
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			hql.append(" and c.branch = '" + sysUser.getBranch() + "'");
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and c.branch = '" + filter.getBranch() + "'");
			}
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			hql.append(" and c.serial = '" + filter.getSerial() + "'");
		}
		if(StringUtils.isNotBlank(filter.getFullName())){
			hql.append(" and (full_name like '%" + filter.getFullName().toUpperCase() + "%' or full_name_en like '%" + filter.getFullName().toUpperCase() + "%')");
		}
		if(StringUtils.isNotBlank(filter.getPhone())){
			hql.append(" and phone = '" + filter.getPhone() + "'");
		}
		if(StringUtils.isNotBlank(filter.getDentist())){
			hql.append(" and r.dentist = '" + filter.getDentist() + "'");
		}
		if(StringUtils.isNotBlank(filter.getSource())){
			hql.append(" and c.source = '" + filter.getSource() + "'");
		}
		SqlUtil.buildDatesBetweenLikeHql("date_excute", filter.getArrivalDateFrom(), filter.getArrivalDateTo(), hql);
		hql.append(" order by date_excute desc");
		hql.append(" limit " +  filter.getPageSize() + " offset "+ filter.getOffset());
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		
		List<CustomerBean> result = query
				.addScalar("customerId", IntegerType.INSTANCE)
				.addScalar("serial", StringType.INSTANCE)
				.addScalar("fullName", StringType.INSTANCE)
				.addScalar("phone", StringType.INSTANCE)
				.addScalar("dateExcute", DateType.INSTANCE)
				.addScalar("content", StringType.INSTANCE)
				.addScalar("dentist", StringType.INSTANCE)
				.addScalar("branch", StringType.INSTANCE)
				.addScalar("payment", IntegerType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(CustomerBean.class)).list();
		return result;
	}

	public CustomerBean getTotalCustomerReality(CustomerFilter filter, SysUser sysUser) {
		StringBuffer hql =new StringBuffer();
		hql.append("select sum(r.payment) as payment");
		hql.append(" from customer c, records r");
		hql.append(" where c.customer_id = r.customer_id ");
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_RECEPTION)){
			hql.append(" and c.branch = '" + sysUser.getBranch() + "'");
		}else {
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and c.branch = '" + filter.getBranch() + "'");
			}
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			hql.append(" and c.serial = '" + filter.getSerial() + "'");
		}
		if(StringUtils.isNotBlank(filter.getFullName())){
			hql.append(" and (full_name like '%" + filter.getFullName().toUpperCase() + "%' or full_name_en like '%" + filter.getFullName().toUpperCase() + "%')");
		}
		if(StringUtils.isNotBlank(filter.getPhone())){
			hql.append(" and phone = '" + filter.getPhone() + "'");
		}
		if(StringUtils.isNotBlank(filter.getDentist())){
			hql.append(" and r.dentist = '" + filter.getDentist() + "'");
		}
		if(StringUtils.isNotBlank(filter.getSource())){
			hql.append(" and c.source = '" + filter.getSource() + "'");
		}
		SqlUtil.buildDatesBetweenLikeHql("date_excute", filter.getArrivalDateFrom(), filter.getArrivalDateTo(), hql);
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		CustomerBean result = (CustomerBean) query
				.addScalar("payment", IntegerType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(CustomerBean.class)).uniqueResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<CustomerBean> getRecordDoctor(CustomerFilter filter, SysUser sysUser) {
		StringBuffer hql =new StringBuffer();
		hql.append("select c.customer_id as customerId, c.serial as serial, full_name as fullName, r.date_excute as dateExcute, c.content as content");
		hql.append(", r.dentist as dentist, r.branch as branch");
		hql.append(" from customer c, records r");
		hql.append(" where c.customer_id = r.customer_id ");
		hql.append(" and r.status = 'W'");
		hql.append(" and date_excute = " + "str_to_date('" + filter.getArrivalDateFrom() + "','%d/%m/%Y') ");
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_ADMIN)){
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and r.dentist = '" + filter.getDentist() + "'");
			}
		}else {
			hql.append(" and r.dentist = '" + sysUser.getName() + "'");
		}
		if(StringUtils.equals(sysUser.getRole(), DentalUtils.ROLE_ADMIN)){
			if(StringUtils.isNotBlank(filter.getBranch())){
				hql.append(" and c.branch = '" + filter.getBranch() + "'");
			}
		}else {
			hql.append(" and c.branch = '" + sysUser.getBranch() + "'");
		}
		if(StringUtils.isNotBlank(filter.getSerial())){
			hql.append(" and c.serial = '" + filter.getSerial() + "'");
		}
		if(StringUtils.isNotBlank(filter.getFullName())){
			hql.append(" and (full_name like '%" + filter.getFullName().toUpperCase() + "%' or full_name_en like '%" + filter.getFullName().toUpperCase() + "%')");
		}
		
		hql.append(" order by serial");
		
		SQLQuery query = getSessionFactory().openSession().createSQLQuery(hql.toString());
		
		List<CustomerBean> result = query
				.addScalar("customerId", IntegerType.INSTANCE)
				.addScalar("serial", StringType.INSTANCE)
				.addScalar("fullName", StringType.INSTANCE)
				.addScalar("dateExcute", DateType.INSTANCE)
				.addScalar("dentist", StringType.INSTANCE)
				.addScalar("branch", StringType.INSTANCE)
				.addScalar("content", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(CustomerBean.class)).list();
		return result;
	}
}
