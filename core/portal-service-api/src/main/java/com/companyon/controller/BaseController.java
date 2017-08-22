package com.companyon.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.companyon.repository.dao.IUserDetailsDao;
import com.phonepe.ratelimit.service.IRateLimitService;

@Controller
public class BaseController {

	@Autowired
	IUserDetailsDao userDao;

	@Autowired
	IRateLimitService rateLimitImpl;

	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(ModelMap model) {

		model.addAttribute("message", "Welcome");
		model.addAttribute("counter", ++counter);
		logger.debug("[welcome] counter : {}", counter);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return VIEW_INDEX;

	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {

		try {
			name = userDao.getName(name);
		} catch (Exception e) {
			logger.error("Some exception occured");
			logger.error(e.getMessage());
		}

		model.addAttribute("message", "Welcome " + name);
		model.addAttribute("counter", ++counter);
		logger.debug("[welcomeName] counter : {}", counter);
		return VIEW_INDEX;

	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public String status(ModelMap model) {

		model.addAttribute("message", "Welcome Guest");
		model.addAttribute("counter", ++counter);
		logger.debug("[welcomeName] counter : {}", counter);
		return VIEW_INDEX;

	}

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public String welcomeName(Object object, ModelMap model) {

		model.addAttribute("message", "Welcome Guest");
		model.addAttribute("counter", ++counter);
		logger.debug("[welcomeName] counter : {}", counter);
		return VIEW_INDEX;

	}

}