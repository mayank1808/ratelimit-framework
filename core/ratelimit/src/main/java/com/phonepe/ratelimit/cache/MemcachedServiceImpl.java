/**
 * 
 */
package com.phonepe.ratelimit.cache;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * @author mayank
 *
 */
@Component
public class MemcachedServiceImpl implements IMemcachedService {

	private MemCachedClient client;

	@Autowired
	Environment env;

	private String hosts;

	protected MemCachedClient getClient() {
		return client;
	}

	@PostConstruct
	public void initialize() throws Exception {
		hosts = env.getProperty("memcache.servers.ip");
		SockIOPool pool = SockIOPool.getInstance("rateLimitPool");
		pool.setServers(parseSocketAddresses(hosts));
		pool.setFailover(true);
		pool.setInitConn(10);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setAliveCheck(true);
		pool.initialize();
		client = new MemCachedClient("rateLimitPool");

	}

	public boolean put(String key, Object ob) {
		return client.add(key, ob);
	}

	public Object get(String key) {
		return client.get(key);
	}

	public boolean delete(String key) {
		return client.delete(key);
	}

	public boolean replace(String key, Object ob) {
		return client.replace(key, ob);
	}

	public boolean flushAll() {
		return client.flushAll();
	}

	public boolean addOrPut(String key, Object ob) {
		if (!client.add(key, ob)) {
			if (!client.replace(key, ob)) {
				return false;
			}
		}
		return true;
	}

	private static String[] parseSocketAddresses(String addresses) {
		String[] addressStrings = addresses.split(",");

		return addressStrings;
	}

}
