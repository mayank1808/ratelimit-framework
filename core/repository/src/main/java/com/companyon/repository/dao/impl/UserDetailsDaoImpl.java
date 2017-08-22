/**
 * 
 */
package com.companyon.repository.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.companyon.repository.dao.IUserDetailsDao;
import com.companyon.repository.repo.IUserRepo;

/**
 * @author mayank
 *
 */

@Service()
@Transactional
public class UserDetailsDaoImpl implements IUserDetailsDao {

	@Autowired
	IUserRepo userRepo;

	@Override
	public String getName(String userCode) throws Exception {

		return userRepo.getName(userCode);
	}

}
