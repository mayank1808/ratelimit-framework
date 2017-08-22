/**
 * 
 */
package com.companyon.repository.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.companyon.repository.entity.UserDetails;

/**
 * @author mayank
 *
 */
@Repository
public interface IUserRepo extends JpaRepository<UserDetails, Integer> {
	
	@Query("select u.name from UserDetails u where u.userCode = ?1")
	public String getName(String userCode) throws Exception;
}
