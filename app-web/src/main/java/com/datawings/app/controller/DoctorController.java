package com.datawings.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.datawings.app.bean.CustomerBean;
import com.datawings.app.common.DateUtil;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.IRecordsService;

@Controller
@SessionAttributes({ "doctorFilter" })
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DOCTOR')")
public class DoctorController {

	@Autowired
	private IRecordsService recordsService;
	
	@ModelAttribute("doctorFilter")
	public CustomerFilter doctorFilter() {
		CustomerFilter filter = new CustomerFilter();
		filter.setArrivalDateFrom(DateUtil.date2String(new Date(), "dd/MM/yyyy"));
		return filter;
	}
	
	@RequestMapping(value = "/secure/doctor", method = RequestMethod.GET)
	public String getDentist(@ModelAttribute("doctorFilter") CustomerFilter filter, Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		List<CustomerBean> records = recordsService.getRecordDoctor(filter, sysUser);
		model.addAttribute("records", records);
		return "doctor";
	}
	
	@RequestMapping(value = "/secure/doctor", method = RequestMethod.POST)
	public String postRdv(@ModelAttribute("doctorFilter") CustomerFilter filter, Model model, HttpServletRequest request) {
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		
		if(StringUtils.equals(action, "RESET")){
			filter.init();
			filter.setArrivalDateFrom(DateUtil.date2String(new Date(), "dd/MM/yyyy"));
		}else if(StringUtils.equals(action, "GO")){
			filter.setPage(0);
		}
		return "redirect:/secure/doctor";	
	}
}
