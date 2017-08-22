/**
 * 
 */
package com.phonepe.ratelimit.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.phonepe.ratelimit.response.GenericResponse;
import com.phonepe.ratelimit.service.IRateLimitService;

/**
 * @author mayank
 *
 */

@Component
public class RatelimitInterceptor extends HandlerInterceptorAdapter {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(RatelimitInterceptor.class);

	@Autowired
	IRateLimitService rateLimit;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String clientId = request.getHeader("clientId");
		String method = request.getMethod();
		String uri = request.getRequestURI();

		GenericResponse genericResponse = rateLimit.processAPICall(clientId, method, uri);

		if (genericResponse.getCode().equals("429")) {
			response.setStatus(429, "RATE_LIMIT_EXCEEDED");
			response.getWriter().write("RATE_LIMIT_EXCEEDED " + genericResponse.getMessage());
			return false;
		}

		return true;
	}

	// after the handler is executed
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}
}