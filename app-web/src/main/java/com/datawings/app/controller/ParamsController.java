package com.datawings.app.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datawings.app.model.Params;
import com.datawings.app.model.SysUser;
import com.datawings.app.service.IParamsService;

@Controller
@PreAuthorize(value = "hasRole('ADMIN')")
@RequestMapping("/secure/")
public class ParamsController {
	@Autowired
	private IParamsService paramsService;
	
	@ModelAttribute("params")
	public Params params() {
		Params param = new Params();
		return param;
	}
	
	@RequestMapping(value = "params", method = RequestMethod.GET)
	public String loadAllParams(Model model) throws IOException {
		List<Params> listParams = paramsService.findAll();
		model.addAttribute("listParams", listParams);
		return "params";
	}

	@RequestMapping(value = "params/edit/{id}", method = RequestMethod.GET)
	public String getUserModify(@PathVariable Integer id, Model model) {
		Params params = null;
		if (id != null && id > 0){
			params = paramsService.find(id);
		}else{
			params = new Params();
		}
		model.addAttribute("paramsModel", params);
		return "paramsModel";
	}
	
	@RequestMapping(value = "params/update", method = RequestMethod.POST)
	public String postUser(@ModelAttribute("params") Params params, Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SysUser sysUser = (SysUser) auth.getPrincipal();
		
		Params saveParam = new Params();
		saveParam.setKey(params.getKey());
		saveParam.setValue(params.getValue());
		saveParam.setType(params.getType());
		if(params.getParamsId() != null && params.getParamsId() > 0) {
			saveParam.setParamsId(params.getParamsId());
			saveParam.setModifiedBy(sysUser.getUsername());
			saveParam.setModifiedDate(new Date());
			paramsService.update(saveParam);
		}else{
			saveParam.setParamsId(null);
			saveParam.setCreatedBy(sysUser.getUsername());
			saveParam.setCreatedDate(new Date());
			paramsService.merge(saveParam);
		}
		
		List<Params> listParams = paramsService.findAll();
		model.addAttribute("listParams", listParams);
		return "redirect:/secure/params";
	}
	
	@RequestMapping(value = "params/delete", method = RequestMethod.POST)
	public String getUserModify(Model model,  HttpServletRequest request) {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		paramsService.deleteById(Integer.parseInt(id));
		return "redirect:/secure/params";
	}
}