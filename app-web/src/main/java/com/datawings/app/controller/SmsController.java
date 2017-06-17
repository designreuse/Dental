package com.datawings.app.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datawings.app.model.Params;
import com.datawings.app.service.IParamsService;

@Controller
@PreAuthorize(value = "hasRole('ADMIN')")
@RequestMapping("/sms/")
public class SmsController {
	@Autowired
	private IParamsService paramsService;
	
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public String sendSms(Model model , HttpServletRequest request) throws IOException {
		List<Params> listParams = paramsService.findAll();
		model.addAttribute("listParams", listParams);
		return "params";
	}
	
}