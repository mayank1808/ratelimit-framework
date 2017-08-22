/**
 * 
 */
package com.phonepe.ratelimit.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * @author mayank
 *
 */
@Component
public class MemcachedServiceImpl implements IMemcachedService {

	private MemCachedClient mcc;

	@Autowired
	public void setMemCachedClient() {
		String[] servers = { "localhost:11211" };
		SockIOPool pool = SockIOPool.getInstance("rateLimitPool");
		pool.setServers(servers);
		pool.setFailover(true);
		pool.setInitConn(10);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setAliveCheck(true);
		pool.initialize();
		this.mcc = new MemCachedClient("rateLimitPool");
	}

	public boolean put(String key, Object ob) {
		return mcc.add(key, ob);
	}

	public Object get(String key) {
		return mcc.get(key);
	}

	public boolean delete(String key) {
		return mcc.delete(key);
	}

	public boolean replace(String key, Object ob) {
		return mcc.replace(key, ob);
	}

	public boolean flushAll() {
		return mcc.flushAll();
	}

	public boolean addOrPut(String key, Object ob) {
		if (!mcc.add(key, ob)) {
			if (!mcc.replace(key, ob)) {
				return false;
			}
		}
		return true;
	}

}
