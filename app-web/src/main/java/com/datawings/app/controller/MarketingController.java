package com.datawings.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.datawings.app.common.DateUtil;
import com.datawings.app.common.DentalUtils;
import com.datawings.app.common.StringUtilz;
import com.datawings.app.filter.CustomerFiler;
import com.datawings.app.filter.MarketingFilter;
import com.datawings.app.filter.SmsFilter;
import com.datawings.app.filter.ValidationError;
import com.datawings.app.manager.CustomerManager;
import com.datawings.app.model.Branches;
import com.datawings.app.model.Dentist;
import com.datawings.app.model.Marketing;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.IBranchesService;
import com.datawings.app.service.ICustomerService;
import com.datawings.app.service.IDentistService;
import com.datawings.app.service.IMarketingService;
import com.datawings.app.validator.CustomerValidator;
import com.datawings.app.validator.MarketingValidator;

@Controller
@SessionAttributes({ "marketingFilter", "CMFilter" })
public class MarketingController {

	@Autowired
	private IMarketingService marketingService;
	
	@Autowired 
	private MessageSource messageSource;
	
	@Autowired
	private IBranchesService branchesService;
	
	@Autowired
	private MarketingValidator marketingValidator;
	
	@Autowired
	private CustomerValidator customerValidator;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private CustomerManager customerManager;
	
	@Autowired
	private IDentistService dentistService;
	
	@ModelAttribute("marketingFilter")
	public MarketingFilter marketingFilter() {
		MarketingFilter filter = new MarketingFilter();
		return filter;
	}
	
	@ModelAttribute("CMFilter")
	public CustomerFiler CMFilter() {
		CustomerFiler filter = new CustomerFiler();
		filter.setArrivalDateAdd(DateUtil.date2String(new Date(), "dd/MM/yyyy"));
		return filter;
	}
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
	@RequestMapping(value = "/secure/marketing", method = RequestMethod.GET)
	public String getMarketing(@ModelAttribute("smsFilter") SmsFilter smsFilter, @ModelAttribute("marketingFilter") MarketingFilter filter, Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
	
		Integer rowCount = marketingService.getMarketingCount(filter, sysUser);
		filter.setRowCount(rowCount);
		
		List<Branches> branches = branchesService.getBranches();
		model.addAttribute("branches", branches);
		return "marketing";
	}
	
	@RequestMapping(value = "secure/marketing/loadpage", method = RequestMethod.GET)
	public String getDentistLoadPage(@ModelAttribute("marketingFilter") MarketingFilter filter, Integer pageNo, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		filter.setPage(pageNo);
		List<Marketing> marketings = marketingService.getMarketings(filter, sysUser);
		model.addAttribute("marketings", marketings);
		model.addAttribute("row", (filter.getPage()) * filter.getPageSize());
		return "marketingLoadPage";
	}
	
	@RequestMapping(value = "/secure/marketing", method = RequestMethod.POST)
	public String postRdv(@ModelAttribute("marketingFilter") MarketingFilter filter, Model model, HttpServletRequest request) throws ServletRequestBindingException {
		
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		if(StringUtils.equals(action, "CREATE")){
			Marketing marketing = new Marketing();
			marketing.setFullName(filter.getFullNameCreate().trim().toUpperCase());
			marketing.setFullNameEn(StringUtilz.unAccent(filter.getFullNameCreate().trim()).toUpperCase());
			marketing.setPhone(filter.getPhoneCreate());
			marketing.setSex(filter.getSexCreate());
			marketing.setEmail(filter.getEmailCreate());
			marketing.setAddress(filter.getAddressCreate().toUpperCase());
			marketing.setBirthday(DateUtil.string2Date(filter.getBirthdayCreate(), "dd/MM/yyyy"));
			marketing.setArrivalDate(DateUtil.string2Date(filter.getArrivalDateCreate(), "dd/MM/yyyy"));
			marketing.setContent(filter.getContentCreate().toUpperCase());
			marketing.setNote(filter.getNoteCreate());
			marketing.setBranch(filter.getBranchCreate());
			marketing.setStatus(DentalUtils.status_WATTING);
			marketing.setCreatedBy(sysUser.getUsername());
			marketing.setCreatedDate(new Date());
			marketingService.merge(marketing);
			
			filter.init();
			return "redirect:/secure/marketing";
			
		}else if(StringUtils.equals(action, "MODIFY")){
			Integer id = ServletRequestUtils.getIntParameter(request, "id");
			
			Marketing marketing = marketingService.find(id);
			marketing.setFullName(filter.getFullNameEdit().trim().toUpperCase());
			marketing.setFullNameEn(StringUtilz.unAccent(filter.getFullNameEdit().trim()).toUpperCase());
			marketing.setPhone(filter.getPhoneEdit());
			marketing.setSex(filter.getSexEdit());
			marketing.setEmail(filter.getEmailEdit());
			marketing.setAddress(filter.getAddressEdit().toUpperCase());
			marketing.setBirthday(DateUtil.string2Date(filter.getBirthdayEdit(), "dd/MM/yyyy"));
			marketing.setArrivalDate(DateUtil.string2Date(filter.getArrivalDateEdit(), "dd/MM/yyyy"));
			marketing.setContent(filter.getContentEdit().toUpperCase());
			marketing.setNote(filter.getNoteEdit());
			marketing.setBranch(filter.getBranchEdit());
			marketing.setModifiedBy(sysUser.getUsername());
			marketing.setModifiedDate(new Date());
			marketingService.merge(marketing);
			
			filter.init();
			return "redirect:/secure/marketing";
			
		}else if(StringUtils.equals(action, "DELETE")){
			Integer id = ServletRequestUtils.getIntParameter(request, "id");
			marketingService.deleteById(id);
			
		}else if(StringUtils.equals(action, "GO")){
			filter.setPage(0);
		}else if(StringUtils.equals(action, "RESET")){
			filter.init();
		}
		return "redirect:/secure/marketing";
	}
	
	@RequestMapping(value = "/secure/marketing/errorCreate.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidatorCreate(@ModelAttribute("marketingFilter") MarketingFilter filter, BindingResult result, Model model, Locale locale){		
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = marketingValidator.checkCreate(filter, result);
		if(errCode > 0){
			rs.put("errCodeCreate", errCode);
			List<ValidationError> lstErr = new ArrayList<ValidationError>();
			for(int i = 0;i <result.getAllErrors().size();i++){
				ValidationError elm = new ValidationError();
				elm.setMessage(messageSource.getMessage(result.getAllErrors().get(i).getCode(), null, 
						result.getAllErrors().get(i).getDefaultMessage(), locale));
				elm.setPropertyName(StringUtils.substringAfterLast(result.getAllErrors().get(i).getCodes()[0], "."));
				lstErr.add(elm);
			}
			rs.put("lstErr", lstErr);
		}
		return rs;
	}
	
	@RequestMapping(value = "/secure/marketing/errorEdit.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidatorEdit(@ModelAttribute("marketingFilter") MarketingFilter filter, BindingResult result, Model model, Locale locale){		
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = marketingValidator.checkEdit(filter, result);
		if(errCode > 0){
			rs.put("errCodeEdit", errCode);
			List<ValidationError> lstErr = new ArrayList<ValidationError>();
			for(int i = 0;i <result.getAllErrors().size();i++){
				ValidationError elm = new ValidationError();
				elm.setMessage(messageSource.getMessage(result.getAllErrors().get(i).getCode(), null, 
						result.getAllErrors().get(i).getDefaultMessage(), locale));
				elm.setPropertyName(StringUtils.substringAfterLast(result.getAllErrors().get(i).getCodes()[0], "."));
				lstErr.add(elm);
			}
			rs.put("lstErr", lstErr);
		}
		return rs;
	}
	
	@RequestMapping(value = {"/secure/marketing/edit/{id}"}, method = RequestMethod.GET)
	public String getInvoice(@PathVariable Integer id, Model model) {
		Marketing marketing = marketingService.find(id);
		List<Branches> branches = branchesService.getBranches();
		model.addAttribute("branches", branches);
		model.addAttribute("marketing", marketing);
		return "marketingEdit";
	}
	
	@RequestMapping(value = {"/secure/marketing/create/{id}"}, method = RequestMethod.GET)
	public String getCM(@PathVariable Integer id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		Marketing marketing = marketingService.find(id);
		List<Dentist> dentists = dentistService.findSysUser(sysUser);
		model.addAttribute("dentists", dentists);
		model.addAttribute("marketing", marketing);
		return "marketingCreate";
	}
	
	@RequestMapping(value = "/secure/marketingcustomer", method = RequestMethod.POST)
	public String postAddCustomer(@ModelAttribute("CMFilter") CustomerFiler filter, Model model, HttpServletRequest request) throws ServletRequestBindingException {
		
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		if(StringUtils.equals(action, "ADD_CUSTOMER")){
			Integer customerId = customerManager.addCustomer(filter, sysUser, id);
			return "redirect:/secure/records?id=" + customerId;
		}
		return "redirect:/secure/marketing";
	}
	
	@RequestMapping(value = "/secure/marketing/errorCustomer.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidatorCustomer(@ModelAttribute("CMFilter") CustomerFiler filter, BindingResult result, Model model, Locale locale){		
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = customerValidator.checkCustomerFromMarketing(filter, result);
		if(errCode > 0){
			rs.put("errCode", errCode);
			List<ValidationError> lstErr = new ArrayList<ValidationError>();
			for(int i = 0;i <result.getAllErrors().size();i++){
				ValidationError elm = new ValidationError();
				elm.setMessage(messageSource.getMessage(result.getAllErrors().get(i).getCode(), null, 
						result.getAllErrors().get(i).getDefaultMessage(), locale));
				elm.setPropertyName(StringUtils.substringAfterLast(result.getAllErrors().get(i).getCodes()[0], "."));
				lstErr.add(elm);
			}
			rs.put("lstErr", lstErr);
		}
		return rs;
	}
}
