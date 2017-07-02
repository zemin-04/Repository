package com.shunhai.skipcloud.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.core.generic.impl.GenericServiceImpl;
import com.shunhai.skipcloud.web.dao.IpLocationMapper;
import com.shunhai.skipcloud.web.model.IpLocation;
import com.shunhai.skipcloud.web.service.IpLocationService;

/**
 * IP Service实现类
 */
@Service
public class IpLocationServiceImpl extends GenericServiceImpl<IpLocation, Long> implements IpLocationService {

    @Resource
    private IpLocationMapper ipLocationMapper;

	@Override
	public IpLocation selectByIp(String ip) {
		if(null == ip || ip.trim().equals("")){
			return new IpLocation("未知IP", "未知IP所在地址", "获取IP失败");
		}else if("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
			return new IpLocation(ip, "本机", "本机访问");
		}else{
			IpLocation ipLocation = ipLocationMapper.selectByIp(ip);
			if(null == ipLocation){
				return new IpLocation("未知IP", "未知IP所在地址", "该IP地址数据库不存在");
			}else{
				return ipLocation;
			}
		}
	}

	@Override
	public GenericDao<IpLocation, Long> getDao() {
		return ipLocationMapper;
	}
}