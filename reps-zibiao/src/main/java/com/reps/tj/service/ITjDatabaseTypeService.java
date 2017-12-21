package com.reps.tj.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.tj.entity.DatabaseType;

public interface ITjDatabaseTypeService {

	public List<DatabaseType> findDatatypesById(String id) throws RepsException;
	
}
