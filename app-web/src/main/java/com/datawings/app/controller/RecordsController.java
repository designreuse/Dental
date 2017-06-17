package com.datawings.app.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.datawings.app.common.DateUtil;
import com.datawings.app.filter.RecordsFilter;
import com.datawings.app.filter.ValidationError;
import com.datawings.app.manager.CustomerManager;
import com.datawings.app.model.Branches;
import com.datawings.app.model.Customer;
import com.datawings.app.model.Dentist;
import com.datawings.app.model.Records;
import com.datawings.app.model.SysUser;
import com.datawings.app.report.InvoicePdfA;
import com.datawings.app.service.IBranchesService;
import com.datawings.app.service.ICustomerService;
import com.datawings.app.service.IDentistService;
import com.datawings.app.service.IRecordsService;
import com.datawings.app.validator.RecordsValidator;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
@SessionAttributes({ "recordsFilter" })
public class RecordsController {

	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IDentistService dentistService;
	
	@Autowired 
	private MessageSource messageSource;
	
	@Autowired
	private IRecordsService recordsService ;
	
	@Autowired
	private RecordsValidator recordsValidator ;
	
	@Autowired
	private CustomerManager customerManager;
	
	@Autowired
	private IBranchesService branchesService;
	
	@ModelAttribute("recordsFilter")
	public RecordsFilter userFilter() {
		RecordsFilter filter = new RecordsFilter();
		return filter;
	}
	
	@RequestMapping(value = "/secure/records", method = RequestMethod.GET)
	public String getDentist(@ModelAttribute("recordsFilter") RecordsFilter filter, Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
		Customer customer = customerService.find(id);
		if(customer == null){
			return "redirect:/secure/customer";
		}
		List<Dentist> dentists = dentistService.findSysUser(sysUser);
		
		model.addAttribute("customer", customer);
		model.addAttribute("dentists", dentists);
		return "records";
	}
	
	@RequestMapping(value = "/secure/records", method = RequestMethod.POST)
	public String postRdv(@ModelAttribute("recordsFilter") RecordsFilter filter, Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		String action = ServletRequestUtils.getStringParameter(request, "action", "");
		Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);

		if(StringUtils.equals(action, "CREATE")){
			
			Customer customer = customerService.find(id);
			
			Records records = new Records();
			records.setCustomerId(customer.getCustomerId());
			records.setSerial(customer.getSerial());
			records.setBranch(customer.getBranch());
			records.setDateExcute(DateUtil.string2Date(filter.getDateExcute(), "dd/MM/yyyy"));
			records.setDentist(filter.getDentist());
			records.setDateNext(DateUtil.string2Date(filter.getDateNext(), "dd/MM/yyyy"));
			/*records.setTeeth(filter.getTeeth());
			records.setContent(filter.getContent().toUpperCase());
			records.setGross(IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getGross())));
			records.setSale(IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getSale())));
			records.setPayment(IntegerUtil.convertInteger(StringUtilz.replaceMoney(filter.getPayment())));
			records.setDateNext(DateUtil.string2Date(filter.getDateNext(), "dd/MM/yyyy"));
			records.setContentNext(filter.getContentNext().toUpperCase());*/
			records.setNote(filter.getNote());
			records.setCausePayment(filter.getCausePayment());
			records.setStatus("W");
			records.setCreatedBy(sysUser.getUsername());
			records.setCreatedDate(new Date());
			
			/*if(StringUtils.isBlank(customer.getContent())){
				customer.setContent(records.getContent());
			}else {
				customer.setContent(customer.getContent() + " + " + records.getContent());
			}
			
			customer.setGross(customer.getGross() + records.getGross());
			customer.setSale(customer.getSale() + records.getSale());
			customer.setPayment(customer.getPayment() + records.getPayment());*/
			
			recordsService.merge(records);
			filter.init();
			return "redirect:/secure/records?id=" + id;
			
		} else if(StringUtils.equals(action, "DELETE")){
			Integer idRecords = ServletRequestUtils.getIntParameter(request, "idRecords", 0);
			Customer customer = customerService.find(id);
			Records records = recordsService.find(idRecords);
			
			recordsService.deleteById(idRecords);
			customer.setContent("");
			for (Records elm : customer.getRecords()) {
				customer.setContent(customer.getContent() + elm.getContent());
			}
			customer.setGross(customer.getGross() - records.getGross());
			customer.setSale(customer.getSale() - records.getSale());
			customer.setPayment(customer.getPayment() - records.getPayment());
			
			customerService.merge(customer);
			
			return "redirect:/secure/records?id=" + id;
			
		}else if(StringUtils.equals(action, "BACK")){
			String fromPage = (String) request.getSession().getAttribute("FROM_PAGE");
			
			if(StringUtils.equals(fromPage, "CUSTOMER")){
				return "redirect:/secure/customer";
			}else if(StringUtils.equals(fromPage, "SCHEDULE")){
				return "redirect:/secure/schedule";
			}else if(StringUtils.equals(fromPage, "DASHBOARD")){
				return "redirect:/secure/dashboard";
			}else if(StringUtils.equals(fromPage, "REALITY")){
				return "redirect:/secure/customer/reality";
			}
		}else if(StringUtils.equals(action, "MODIFY")){
			Customer customer = customerService.find(filter.getCustomerId());
			Records records = recordsService.find(filter.getRecordId());
			customerManager.modifyRecord(customer, records, filter, sysUser);
			
			return "redirect:/secure/records?id=" + filter.getCustomerId();
		}else if(StringUtils.equals(action, "PRINT")){
			Customer customer = customerService.find(filter.getCustomerId());
			Records records = recordsService.find(filter.getRecordId());
			customerManager.modifyRecord(customer, records, filter, sysUser);
			
			return "redirect:/secure/pdf?id=" + records.getRecordId();
		}
			
			
		return "redirect:/secure/records?id=" + id;
	}
	
	@RequestMapping(value = "secure/pdf", method = RequestMethod.GET)
	public String getInvoicePdf(Model model, HttpServletRequest request){
		int id = ServletRequestUtils.getIntParameter(request, "id", 0);
		Records record = recordsService.find(id);
		Customer customer = customerService.find(record.getCustomerId());
		model.addAttribute("customer", customer);
		model.addAttribute("record", record);
		return "pdfPage";
	}
	
	
	@RequestMapping(value = "secure/print", method = RequestMethod.GET)
	public ModelAndView getInvoicePdfview(Model model, HttpServletRequest request) throws DocumentException, IOException{
		int id = ServletRequestUtils.getIntParameter(request, "id", 0);
		Records record = recordsService.find(id);
		Customer customer = customerService.find(record.getCustomerId());
		Branches branch = branchesService.find(customer.getBranch());
		
		String pathImage = request.getSession().getServletContext().getRealPath("/static/images/logopdf.png"); 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
		
		InvoicePdfA invoicePdf = new InvoicePdfA();
		document.open();
		document = invoicePdf.makePdf(document, request, record, customer, branch, pathImage);
		document.close();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("document", document);
		map.put("baos", baos);

		return new ModelAndView("fileView", "map", map);
	}
	
	
	@RequestMapping(value = {"/secure/records/edit/{id}"}, method = RequestMethod.GET)
	public String getDentist(@PathVariable Integer id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		Records records = recordsService.find(id);
		List<Dentist> dentists = dentistService.findSysUser(sysUser);
		
		model.addAttribute("records", records);
		model.addAttribute("dentists", dentists);
		return "recordsEdit";
	}
	
	
	@RequestMapping(value = "/secure/records/errorCreate.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidator(@ModelAttribute("recordsFilter") RecordsFilter filter, BindingResult result, Locale locale){
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = recordsValidator.checkRecordsAdd(filter, result);
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
	
	@RequestMapping(value = "/secure/records/errorEdit.json", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> doValidatorEdit(@ModelAttribute("recordsFilter") RecordsFilter filter, BindingResult result, Locale locale){
		HashMap<String, Object> rs = new HashMap<String, Object>();
		int errCode = 0;
		errCode = recordsValidator.checkRecordsEdit(filter, result);
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
