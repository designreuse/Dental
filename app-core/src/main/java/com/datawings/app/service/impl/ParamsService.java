package com.datawings.app.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datawings.app.dao.IParamsDao;
import com.datawings.app.model.Params;
import com.datawings.app.service.IParamsService;

@Service
public class ParamsService implements IParamsService {

	@Autowired
	private IParamsDao dao;

	public Params find(Serializable id) {
		return dao.find(id);
	}

	@Transactional
	public Serializable save(Params model) {
		return dao.save(model);
	}

	@Transactional
	public void update(Params model) {
		dao.update(model);
	}

	@Transactional
	public void merge(Params model) {
		dao.merge(model);
	}

	@Transactional
	public void saveOrUpdate(Params model) {
		dao.saveOrUpdate(model);
	}

	@Transactional
	public void delete(Params model) {
		dao.delete(model);
	}

	@Transactional
	public void deleteById(Serializable id) {
		dao.deleteById(id);
	}
	@Transactional
	public List<Params> findAll() {
		return dao.findAll();
	}
	
}
