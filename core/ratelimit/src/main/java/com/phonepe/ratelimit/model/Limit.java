/**
 * 
 */
package com.phonepe.ratelimit.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author mayank
 *
 */
public class Limit implements Serializable {

	private static final long serialVersionUID = 6919179352899001401L;
	Map<TimeUnit, Integer> durationMap;

	public Limit() {
		durationMap = new EnumMap<TimeUnit, Integer>(TimeUnit.class);
	}

	public Map<TimeUnit, Integer> getDurationMap() {
		return durationMap;
	}

	public void setDurationMap(Map<TimeUnit, Integer> durationMap) {
		this.durationMap = durationMap;
	}

	@Override
	public String toString() {
		return "Limit [durationMap=" + durationMap + "]";
	}

}
