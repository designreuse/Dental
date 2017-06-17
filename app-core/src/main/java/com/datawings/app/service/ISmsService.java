package com.datawings.app.service;

import java.io.Serializable;
import java.util.List;

import com.datawings.app.model.Sms;

public interface ISmsService {
	public Sms find(Serializable smsId);

	public Serializable save(Sms model);

	public void update(Sms model);

	public void merge(Sms model);

	public void saveOrUpdate(Sms model);

	public void delete(Sms model);

	public void deleteById(Serializable id);
	
	public List<Sms> findAll();
}
