package com.datawings.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.datawings.app.dao.IParamsDao;
import com.datawings.app.model.Params;

@Repository
public class ParamsDao extends BaseDao<Params, Integer> implements IParamsDao{

}
