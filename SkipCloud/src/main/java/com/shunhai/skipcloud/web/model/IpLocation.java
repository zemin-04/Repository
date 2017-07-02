package com.shunhai.skipcloud.web.model;

public class IpLocation {

    private Long id;

    private String ip;

    private String startIP_S;

    private String endIP_S;

    private String address;

    private String company;

    private Long startIP_L;

    private Long endIP_L;

    public IpLocation(){}

	public IpLocation(String ip, String address, String company) {
		this.ip = ip;
		this.address = address;
		this.company = company;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	public String getStartIP_S() {
		return startIP_S;
	}

	public void setStartIP_S(String startIP_S) {
		this.startIP_S = startIP_S == null ? null : startIP_S.trim();
	}

	public String getEndIP_S() {
		return endIP_S;
	}

	public void setEndIP_S(String endIP_S) {
		this.endIP_S = endIP_S == null ? null : endIP_S.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company == null ? null : company.trim();
	}

	public Long getStartIP_L() {
		return startIP_L;
	}

	public void setStartIP_L(Long startIP_L) {
		this.startIP_L = startIP_L;
	}

	public Long getEndIP_L() {
		return endIP_L;
	}

	public void setEndIP_L(Long endIP_L) {
		this.endIP_L = endIP_L;
	}
}