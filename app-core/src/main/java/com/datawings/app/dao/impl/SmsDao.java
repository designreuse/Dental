package com.datawings.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.datawings.app.dao.ISmsDao;
import com.datawings.app.model.Sms;

@Repository
public class SmsDao extends BaseDao<Sms, Integer> implements ISmsDao{
	
}
