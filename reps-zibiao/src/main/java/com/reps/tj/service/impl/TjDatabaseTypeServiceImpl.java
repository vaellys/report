package com.reps.tj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.tj.entity.DatabaseType;
import com.reps.tj.entity.TjZdyzbdyb;
import com.reps.tj.service.ITjDatabaseTypeService;
import com.reps.tj.service.ITjZdyzbdybService;

@Service
public class TjDatabaseTypeServiceImpl implements ITjDatabaseTypeService{
	
	private Logger logger = LoggerFactory.getLogger(TjDatabaseTypeServiceImpl.class);
	
	@Autowired
	ITjZdyzbdybService tjZdyzbdybService;
	
	@Override
	public List<DatabaseType> findDatatypesById(String id) throws RepsException{
		TjZdyzbdyb tjZdyzbdyb = tjZdyzbdybService.get(id);
		return null;
	}
	
}
