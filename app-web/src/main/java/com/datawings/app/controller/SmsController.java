package com.datawings.app.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.datawings.app.common.DateUtil;
import com.datawings.app.filter.SmsFilter;
import com.datawings.app.model.Params;
import com.datawings.app.model.Sms;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.IParamsService;
import com.datawings.app.service.ISmsService;

@Controller
/*@PreAuthorize(value = "hasRole('ADMIN')")*/
@SessionAttributes({ "smsFilter",  "smsStatisticFilter" })
public class SmsController {
	@Autowired
	private IParamsService paramsService;
	
	@Autowired
	private ISmsService smsService;
	
	@ModelAttribute("smsFilter")
	public SmsFilter smsFilter() {
		SmsFilter filter = new SmsFilter();
		String tmp = DateUtil.date2String(new Date(), "MM/yyyy");
		Date start = DateUtil.string2Date(tmp, "MM/yyyy");
		Date end  = DateUtil.endMonth(start);
		filter.setDateFrom(DateUtil.date2String(start, "dd/MM/yyyy"));
		filter.setDateTo(DateUtil.date2String(end, "dd/MM/yyyy"));
		
		return filter;
	}
	
	@ModelAttribute("smsStatisticFilter")
	public SmsFilter smsStatisticFilter() {
		SmsFilter filter = new SmsFilter();
		String tmp = DateUtil.date2String(new Date(), "MM/yyyy");
		Date start = DateUtil.string2Date(tmp, "MM/yyyy");
		Date end  = DateUtil.endMonth(start);
		filter.setDateFrom(DateUtil.date2String(start, "dd/MM/yyyy"));
		filter.setDateTo(DateUtil.date2String(end, "dd/MM/yyyy"));
		
		return filter;
	}
	
	@RequestMapping(value = "/secure/sms", method = RequestMethod.GET)
	public String getDentist(@ModelAttribute("smsFilter") SmsFilter filter, Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		Integer rowCount = smsService.getSmsRowCount(filter, sysUser);
		filter.setRowCount(rowCount);
		
		return "sms";
	}
	
	@RequestMapping(value = "secure/sms/loadpage", method = RequestMethod.GET)
	public String getDentistLoadPage(@ModelAttribute("smsFilter") SmsFilter filter, Integer pageNo, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		filter.setPage(pageNo);
		List<Sms> listSms = smsService.getlistSms(filter, sysUser);
		model.addAttribute("listSms", listSms);
		model.addAttribute("row", (filter.getPage()) * filter.getPageSize());
		return "smsLoadPage";
	}
	
	@RequestMapping(value = "/secure/sms", method = RequestMethod.POST)
	public String postRdv(@ModelAttribute("smsFilter") SmsFilter filter, Model model, HttpServletRequest request) {
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		
		if(StringUtils.equals(action, "GO")){
			filter.setPage(0);
		}else if(StringUtils.equals(action, "RESET")){
			filter.init();
			String tmp = DateUtil.date2String(new Date(), "MM/yyyy");
			Date start = DateUtil.string2Date(tmp, "MM/yyyy");
			Date end  = DateUtil.endMonth(start);
			filter.setDateFrom(DateUtil.date2String(start, "dd/MM/yyyy"));
			filter.setDateTo(DateUtil.date2String(end, "dd/MM/yyyy"));
		}
		return "redirect:/secure/sms";
	}
	
	@RequestMapping(value = "/secure/sms/statistic", method = RequestMethod.GET)
	public String getSmsStatistic(@ModelAttribute("smsStatisticFilter") SmsFilter filter, Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		List<SmsFilter> smsStatistic = smsService.getSmsStatistic(filter, sysUser);
		
		SmsFilter totalSms = new SmsFilter();
		for (SmsFilter elm : smsStatistic) {
			if(StringUtils.equals(elm.getType(), "1")){
				elm.setUnit(400);
				elm.setGross(elm.getCount() * elm.getUnit());
				totalSms.setCount(totalSms.getCount() + elm.getCount());
				totalSms.setGross(totalSms.getGross() + elm.getGross());
			}else {
				elm.setUnit(600);
				elm.setGross(elm.getCount() * elm.getUnit());
				totalSms.setCount(totalSms.getCount() + elm.getCount());
				totalSms.setGross(totalSms.getGross() + elm.getGross());
			}
		}
		model.addAttribute("smsStatistic", smsStatistic);
		model.addAttribute("totalSms", totalSms);
		return "smsStatistic";
	}
	
	@RequestMapping(value = "/secure/sms/statistic", method = RequestMethod.POST)
	public String postSmsStatistic(@ModelAttribute("smsStatisticFilter") SmsFilter filter, Model model, HttpServletRequest request) {
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		
		if(StringUtils.equals(action, "GO")){
			filter.setPage(0);
		}else if(StringUtils.equals(action, "RESET")){
			filter.init();
			String tmp = DateUtil.date2String(new Date(), "MM/yyyy");
			Date start = DateUtil.string2Date(tmp, "MM/yyyy");
			Date end  = DateUtil.endMonth(start);
			filter.setDateFrom(DateUtil.date2String(start, "dd/MM/yyyy"));
			filter.setDateTo(DateUtil.date2String(end, "dd/MM/yyyy"));
		}
		return "redirect:/secure/sms/statistic";
	}
	
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public String sendSms(Model model , HttpServletRequest request) throws IOException {
		List<Params> listParams = paramsService.findAll();
		model.addAttribute("listParams", listParams);
		return "params";
	}
	
}