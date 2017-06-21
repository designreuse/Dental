package com.datawings.app.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datawings.app.common.DateUtil;
import com.datawings.app.common.DentalUtils;
import com.datawings.app.common.IntegerUtil;
import com.datawings.app.common.StringUtilz;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.filter.RecordsFilter;
import com.datawings.app.model.Customer;
import com.datawings.app.model.Marketing;
import com.datawings.app.model.Records;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.ICustomerService;
import com.datawings.app.service.IMarketingService;
import com.datawings.app.service.IRecordsService;

@Service
public class CustomerManager {

	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IMarketingService marketingService;
	
	@Autowired
	private IRecordsService recordsService;
	
	@Transactional
	public Integer addCustomer(CustomerFilter filter, SysUser sysUser, Integer id) {
		Integer serial = customerService.maxSerial(sysUser);
		
		Customer customer = new Customer();
		customer.setSerial(serial + 1);
		customer.setFullName(filter.getFullNameAdd().trim().toUpperCase());
		customer.setFullNameEn(StringUtilz.unAccent(filter.getFullNameAdd().trim()).toUpperCase());
		customer.setSex(filter.getSexAdd());
		customer.setType(filter.getTypeAdd());
		customer.setBirthday(DateUtil.string2Date(filter.getBirthdayAdd(), "dd/MM/yyyy"));
		customer.setPhone(filter.getPhoneAdd());
		customer.setEmail(filter.getEmailAdd());
		customer.setAddress(filter.getAddressAdd().toUpperCase());
		customer.setArrivalDate(DateUtil.string2Date(filter.getArrivalDateAdd(), "dd/MM/yyyy"));
		customer.setCause(filter.getCauseAdd().toUpperCase());
		customer.setDentist(filter.getDentistAdd());
		customer.setStatus(filter.getStatusAdd());
		customer.setBranch(sysUser.getBranch());
		customer.setNote(filter.getNoteAdd().trim());
		customer.setSource("MARKETING");
		customer.setCreatedBy(sysUser.getUsername());
		customer.setCreatedDate(new Date());
		
		customerService.save(customer);
		
		//Add records
		
		Records records = new Records();
		records.setCustomerId(customer.getCustomerId());
		records.setSerial(customer.getSerial());
		records.setBranch(customer.getBranch());
		records.setDateExcute(DateUtil.string2Date(filter.getArrivalDateAdd(), "dd/MM/yyyy"));
		records.setDentist(filter.getDentistAdd());
		records.setDateNext(null);
		records.setNote(filter.getNoteAdd().trim());
		records.setStatus("W");
		records.setCreatedBy(sysUser.getUsername());
		records.setCreatedDate(new Date());
		
		recordsService.merge(records);
		
		//Update marketing
		Marketing marketing = marketingService.find(id);
		marketing.setStatus(DentalUtils.status_FINISH);
		marketingService.merge(marketing);
		
		return customer.getCustomerId();
	}
	
	@Transactional
	public Integer addCustomerGuest(CustomerFilter filter, SysUser sysUser) {
		Integer serial = customerService.maxSerial(sysUser);
		
		Customer customer = new Customer();
		customer.setSerial(serial + 1);
		customer.setFullName(filter.getFullNameCreate().trim().toUpperCase());
		customer.setFullNameEn(StringUtilz.unAccent(filter.getFullNameCreate().trim()).toUpperCase());
		customer.setSex(filter.getSexCreate());
		customer.setType(filter.getTypeCreate());
		customer.setBirthday(DateUtil.string2Date(filter.getBirthdayCreate(), "dd/MM/yyyy"));
		customer.setPhone(filter.getPhoneCreate());
		customer.setEmail(filter.getEmailCreate());
		customer.setAddress(filter.getAddressCreate().toUpperCase());
		customer.setArrivalDate(DateUtil.string2Date(filter.getArrivalDateCreate(), "dd/MM/yyyy"));
		customer.setCause(filter.getCauseCreate().toUpperCase());
		customer.setDentist(filter.getDentistCreate());
		customer.setStatus(filter.getStatusCreate());
		customer.setBranch(sysUser.getBranch());
		customer.setSource("GUEST");
		customer.setNote(filter.getNoteCreate().trim());
		customer.setCreatedBy(sysUser.getUsername());
		customer.setCreatedDate(new Date());
		
		customerService.save(customer);
		
		//Add records
		
		Records records = new Records();
		records.setCustomerId(customer.getCustomerId());
		records.setSerial(customer.getSerial());
		records.setBranch(customer.getBranch());
		records.setDateExcute(DateUtil.string2Date(filter.getArrivalDateCreate(), "dd/MM/yyyy"));
		records.setDentist(filter.getDentistCreate());
		records.setDateNext(null);
		records.setStatus("W");
		records.setCreatedBy(sysUser.getUsername());
		records.setCreatedDate(new Date());
		
		recordsService.merge(records);
		
		return customer.getCustomerId();
	}
	
	@Transactional
	public void modifyRecord(Customer customer, Records records, RecordsFilter filter, SysUser sysUser) {
		
		customer.setGross(customer.getGross() + IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getGrossEdit())) - records.getGross());
		customer.setSale(customer.getSale() + IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getSaleEdit())) - records.getSale());
		customer.setPayment(customer.getPayment() + IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getPaymentEdit())) - records.getPayment());
		customerService.merge(customer);
	
		records.setDateExcute(DateUtil.string2Date(filter.getDateExcuteEdit(), "dd/MM/yyyy"));
		records.setTeeth(filter.getTeethEdit());
		records.setContent(filter.getContentEdit().toUpperCase());
		records.setDentist(filter.getDentistEdit());
		records.setGross(IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getGrossEdit())));
		records.setSale(IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getSaleEdit())));
		records.setPayment(IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getPaymentEdit())));
		records.setDateNext(DateUtil.string2Date(filter.getDateNextEdit(), "dd/MM/yyyy"));
		records.setContentNext(filter.getContentNextEdit().toUpperCase());
		records.setCausePayment(filter.getCausePaymentEdit().toUpperCase());
		if(StringUtils.isBlank(filter.getStatusEdit())){
			records.setStatus("W");
		}else{
			records.setStatus(filter.getStatusEdit());
		}
		records.setModifiedBy(sysUser.getUsername());
		records.setModifiedDate(new Date());
		recordsService.merge(records);
	}
	
	@Transactional
	public void deleteRecord(Customer customer, Records records, RecordsFilter filter, SysUser sysUser) {
		customer.setGross(customer.getGross() - records.getGross());
		customer.setSale(customer.getSale() - records.getSale());
		customer.setPayment(customer.getPayment() - records.getPayment());
		
		customer.getRecords().remove(records);
		
		List<Records> listRecords = new ArrayList<Records>(customer.getRecords());
		customer.setContent(genContent(listRecords));
			
		customerService.merge(customer);
	}
	
	public String genContent(List<Records> records) {
		StringBuffer rs = new StringBuffer("");
		for (int i = 0; i< records.size(); i++) {
			Records elm = records.get(i);
			if(i < records.size() - 1){
				rs.append(elm.getContent() + " + ");
			}else{
				rs.append(elm.getContent());
			}
		}
		return rs.toString();
	}
}
