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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.datawings.app.common.DateUtil;
import com.datawings.app.common.StringUtilz;
import com.datawings.app.filter.CustomerFilter;
import com.datawings.app.filter.ValidationError;
import com.datawings.app.manager.CustomerManager;
import com.datawings.app.model.Branches;
import com.datawings.app.model.Customer;
import com.datawings.app.model.Dentist;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.IBranchesService;
import com.datawings.app.service.ICustomerService;
import com.datawings.app.service.IDentistService;
import com.datawings.app.validator.CustomerValidator;

@Controller
@SessionAttributes({ "customerFilter", "customerDetailFilter" })
public class CustomerController {

	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IDentistService dentistService;
	
	@Autowired
	private CustomerValidator customerValidator;
	
	@Autowired 
	private MessageSource messageSource;
	
	@Autowired
	private IBranchesService branchesService;
	
	@Autowired
	private CustomerManager customerManager;
	
	@ModelAttribute("customerFilter")
	public CustomerFilter customerFilter() {
		CustomerFilter filter = new CustomerFilter();
		filter.setArrivalDateCreate(DateUtil.date2String(new Date(), "dd/MM/yyyy"));
		return filter;
	}
	
	@ModelAttribute("customerDetailFilter")
	public CustomerFilter customerDetailFilter() {
		CustomerFilter filter = new CustomerFilter();
		return filter;
	}
	
	@RequestMapping(value = "/secure/customer", method = RequestMethod.GET)
	public String getDentist(@ModelAttribute("customerFilter") CustomerFilter filter, Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		request.getSession().setAttribute("FROM_PAGE", "CUSTOMER");
		
		Integer rowCount = customerService.getCustomerRowCount(filter, sysUser);
		filter.setRowCount(rowCount);
		
		List<Dentist> dentists = dentistService.findSysUser(sysUser);
		List<Branches> branches = branchesService.getBranches();
		model.addAttribute("dentists", dentists);
		model.addAttribute("branches", branches);
		return "customer";
	}
	
	@RequestMapping(value = "secure/customer/loadpage", method = RequestMethod.GET)
	public String getDentistLoadPage(@ModelAttribute("customerFilter") CustomerFilter filter, Integer pageNo, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		filter.setPage(pageNo);
		List<Customer> customers = customerService.getCustomers(filter, sysUser);
		model.addAttribute("customers", customers);
		model.addAttribute("row", (filter.getPage()) * filter.getPageSize());
		return "customerLoadPage";
	}
	
	@RequestMapping(value = "/secure/customer", method = RequestMethod.POST)
	public String postRdv(@ModelAttribute("customerFilter") CustomerFilter filter, Model model, HttpServletRequest request) {
		
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		if(StringUtils.equals(action, "CREATE")){
			Integer customerId = customerManager.addCustomerGuest(filter, sysUser);
			filter.init();
			
			return "redirect:/secure/records?id=" + customerId;
			
		}else if(StringUtils.equals(action, "DELETE")){
			Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
			customerService.deleteById(id);
			
		}else if(StringUtils.equals(action, "GO")){
			filter.setPage(0);
		}else if(StringUtils.equals(action, "RESET")){
			filter.init();
		}
		return "redirect:/secure/customer";
	}
	
	@RequestMapping(value = "/secure/customer/detail", method = RequestMethod.GET)
	public String getCustomerDetail(@ModelAttribute("customerDetailFilter") CustomerFilter filter, Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
		Customer customer = customerService.find(id);
		if(customer == null){
			return "redirect:/secure/customer";
		}
			
		List<Dentist> dentists = dentistService.findSysUser(sysUser);
		List<Branches> branches = branchesService.getBranches();
		
		model.addAttribute("branches", branches);
		model.addAttribute("customer", customer);
		model.addAttribute("dentists", dentists);
		return "customerDetail";
	}
	
	@RequestMapping(value = "/secure/customer/detail", method = RequestMethod.POST)
	public String postCustomerDetail(@ModelAttribute("customerDetailFilter") CustomerFilter filter, Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
		
		if(StringUtils.equals(action, "GO")){
			Customer customer = customerService.find(id);
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
			customer.setNote(filter.getNoteCreate().trim());
			customer.setModifiedBy(sysUser.getUsername());
			customer.setModifiedDate(new Date());
			
			customerService.merge(customer);
			filter.init();
		} else if(StringUtils.equals(action, "BACK")){
			String fromPage = (String) request.getSession().getAttribute("FROM_PAGE");
			
			if(StringUtils.equals(fromPage, "CUSTOMER")){
				return "redirect:/secure/customer";
			}else if(StringUtils.equals(fromPage, "SCHEDULE")){
				return "redirect:/secure/schedule";
			}else if(StringUtils.equals(fromPage, "REALITY")){
				return "redirect:/secure/customer/reality";
			}else if(StringUtils.equals(fromPage, "DASHBOARD")){
				return "redirect:/secure/dashboard";
			}
		}
		
		return "redirect:/secure/customer/detail?id=" + id;
	}
	
	@RequestMapping(value = "/secure/customer/errorCreate.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidatorAgent(@ModelAttribute("customerFilter") CustomerFilter filter, BindingResult result, Model model, Locale locale){
		
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = customerValidator.checkCustomerAdd(filter, result);
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
	
	@RequestMapping(value = "/secure/customer/errorEdit.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidatorEdit(@ModelAttribute("customerDetailFilter") CustomerFilter filter, BindingResult result, Model model, Locale locale){
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = customerValidator.checkCustomerEdit(filter, result);
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
}
