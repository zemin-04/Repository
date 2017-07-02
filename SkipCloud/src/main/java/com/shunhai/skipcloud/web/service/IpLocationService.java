package com.shunhai.skipcloud.web.service;

import com.shunhai.skipcloud.core.generic.GenericService;
import com.shunhai.skipcloud.web.model.IpLocation;

/**
 * IP 业务 接口
 **/
public interface IpLocationService extends GenericService<IpLocation, Long> {

    /**
     * 查询ip所属地区信息
     * @param ip
     * @return
     */
	IpLocation selectByIp(String ip);

}
