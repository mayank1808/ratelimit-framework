/**
 * 
 */
package com.phonepe.ratelimit.cache;

/**
 * @author mayank
 *
 */
public interface IMemcachedService {

	public boolean put(String key, Object ob);

	public Object get(String key);

	public boolean delete(String key);

	public boolean replace(String key, Object ob);

	public boolean flushAll();
	
	public boolean addOrPut(String key, Object ob);
}
