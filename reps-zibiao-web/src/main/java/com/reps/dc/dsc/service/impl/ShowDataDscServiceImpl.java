package com.reps.dc.dsc.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.dc.dsc.dao.DataDscDao;
import com.reps.dc.dsc.service.IShowDataDscService;
import com.reps.dc.server.StatData;

@Service("com.reps.dc.dsc.service.impl.ShowDataDscServiceImpl")
public class ShowDataDscServiceImpl implements IShowDataDscService {
	
	@Autowired
	DataDscDao dataDscDao;

	@Override
	public Long countByReceiver(Date paramDate1, Date paramDate2, String paramString) {
		return this.dataDscDao.countByReceiver(paramDate1, paramDate2, paramString);
	}

	@Override
	public List<StatData> statByAgentDays(Date paramDate, String paramString) {
		return this.dataDscDao.countByAgentDays(paramDate, paramString);
	}

}
