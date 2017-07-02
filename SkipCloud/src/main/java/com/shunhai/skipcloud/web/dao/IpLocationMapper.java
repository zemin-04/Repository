package com.shunhai.skipcloud.web.dao;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.web.model.IpLocation;


public interface IpLocationMapper extends GenericDao<IpLocation, Long>{

	IpLocation selectByIp(String ip);
}