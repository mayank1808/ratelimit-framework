/**
 * 
 */
package com.phonepe.ratelimit.model;

/**
 * @author mayank
 *
 */
public class Host {

	private String protocol;
	private String ip;
	private String port;
	private String address;

	public Host() {

	}

	public Host(String protocol, String ip, String port) {
		super();
		this.protocol = protocol;
		this.ip = ip;
		this.port = port;
		this.address = protocol + "://" + ip + ":" + port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Host [protocol=" + protocol + ", ip=" + ip + ", port=" + port + ", address=" + address + "]";
	}

}
