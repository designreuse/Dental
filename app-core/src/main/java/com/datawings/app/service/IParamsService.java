package com.datawings.app.service;

import java.io.Serializable;
import java.util.List;

import com.datawings.app.model.Params;

public interface IParamsService {
	public Params find(Serializable paramId);

	public Serializable save(Params model);

	public void update(Params model);

	public void merge(Params model);

	public void saveOrUpdate(Params model);

	public void delete(Params model);

	public void deleteById(Serializable id);
	
	public List<Params> findAll();
}
