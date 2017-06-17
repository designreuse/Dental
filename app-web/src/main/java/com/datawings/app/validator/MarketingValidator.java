package com.datawings.app.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.datawings.app.common.DateUtil;
import com.datawings.app.filter.MarketingFilter;

@Component
public class MarketingValidator {

	public int checkCreate(Object target, Errors errors) {
		MarketingFilter filter = (MarketingFilter) target;
		Integer erroCode = 0;
		
		if (StringUtils.isBlank(filter.getFullNameCreate())) {
			errors.rejectValue("fullNameCreate", "message.empty", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isNotBlank(filter.getBirthdayCreate()) && !DateUtil.checkDateAsString(filter.getBirthdayCreate(), "dd/MM/yyyy")) {
			errors.rejectValue("birthdayCreate", "message.valid", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isNotBlank(filter.getArrivalDateCreate()) && !DateUtil.checkDateAsString(filter.getArrivalDateCreate(), "dd/MM/yyyy")) {
			errors.rejectValue("arrivalDateCreate", "message.valid", "!");
			erroCode = 1;
		}
		
		return erroCode;
	}

	public int checkEdit(Object target, Errors errors) {
		MarketingFilter filter = (MarketingFilter) target;
		Integer erroCode = 0;
		
		if (StringUtils.isBlank(filter.getFullNameEdit())) {
			errors.rejectValue("fullNameEdit", "message.empty", "!");
			erroCode = 1;
		}
		
		if (StringUtils.isNotBlank(filter.getArrivalDateEdit()) && !DateUtil.checkDateAsString(filter.getArrivalDateEdit(), "dd/MM/yyyy")) {
			errors.rejectValue("arrivalDateEdit", "message.valid", "!");
			erroCode = 1;
		}
		
		return erroCode;
	}
	
}
