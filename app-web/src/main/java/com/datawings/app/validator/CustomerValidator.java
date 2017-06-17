package com.datawings.app.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.datawings.app.common.DateUtil;
import com.datawings.app.filter.CustomerFiler;
import com.datawings.app.service.ICustomerService;

@Component
public class CustomerValidator {

	@Autowired
	private ICustomerService customerService;
	
	public int checkCustomerAdd(Object target, Errors errors) {
		CustomerFiler filter = (CustomerFiler) target;
		Integer erroCode = 0;
		
		if (StringUtils.isBlank(filter.getFullNameCreate())) {
			errors.rejectValue("fullNameCreate", "message.empty", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isBlank(filter.getBirthdayCreate())) {
			errors.rejectValue("birthdayCreate", "message.empty", "!");
			erroCode = 1;
		}else if (!DateUtil.checkDateAsString(filter.getBirthdayCreate(), "dd/MM/yyyy")) {
			errors.rejectValue("birthdayCreate", "message.valid", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isBlank(filter.getArrivalDateCreate())) {
			errors.rejectValue("arrivalDateCreate", "message.empty", "!");
			erroCode = 1;
		}else if (!DateUtil.checkDateAsString(filter.getArrivalDateCreate(), "dd/MM/yyyy")) {
			errors.rejectValue("arrivalDateCreate", "message.valid", "!");
			erroCode = 1;
		}
		
		return erroCode;
	}
	
	public int checkCustomerFromMarketing(Object target, Errors errors) {
		CustomerFiler filter = (CustomerFiler) target;
		Integer erroCode = 0;
		
		if (StringUtils.isBlank(filter.getFullNameAdd())) {
			errors.rejectValue("fullNameAdd", "message.empty", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isBlank(filter.getBirthdayAdd())) {
			errors.rejectValue("birthdayAdd", "message.empty", "!");
			erroCode = 1;
		}else if (!DateUtil.checkDateAsString(filter.getBirthdayAdd(), "dd/MM/yyyy")) {
			errors.rejectValue("birthdayAdd", "message.valid", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isBlank(filter.getArrivalDateAdd())) {
			errors.rejectValue("arrivalDateAdd", "message.empty", "!");
			erroCode = 1;
		}else if (!DateUtil.checkDateAsString(filter.getArrivalDateAdd(), "dd/MM/yyyy")) {
			errors.rejectValue("arrivalDateAdd", "message.valid", "!");
			erroCode = 1;
		}
		
		return erroCode;
	}


	public int checkCustomerEdit(Object target, Errors errors) {
		CustomerFiler filter = (CustomerFiler) target;
		Integer erroCode = 0;
		
		if (StringUtils.isBlank(filter.getFullNameCreate())) {
			errors.rejectValue("fullNameCreate", "message.empty", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isBlank(filter.getBirthdayCreate())) {
			errors.rejectValue("birthdayCreate", "message.empty", "!");
			erroCode = 1;
		}else if (!DateUtil.checkDateAsString(filter.getBirthdayCreate(), "dd/MM/yyyy")) {
			errors.rejectValue("birthdayCreate", "message.valid", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isBlank(filter.getArrivalDateCreate())) {
			errors.rejectValue("arrivalDateCreate", "message.empty", "!");
			erroCode = 1;
		}else if (!DateUtil.checkDateAsString(filter.getArrivalDateCreate(), "dd/MM/yyyy")) {
			errors.rejectValue("arrivalDateCreate", "message.valid", "!");
			erroCode = 1;
		}
		
		return erroCode;
	}

}
