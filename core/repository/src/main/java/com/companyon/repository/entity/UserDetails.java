/**
 * 
 */
package com.companyon.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author mayank
 *
 */

@Entity
@Table(name = "user_details")
@NamedQuery(name = "UserDetails.findAll", query = "SELECT u FROM UserDetails u")
public class UserDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7687376910178252759L;

	private Integer id;
	private String emailId;
	private String userCode;
	private String phoneNo;
	private String name;

	public UserDetails() {

	}

	public UserDetails(Integer id, String emailId, String userCode, String phoneNo, String name) {
		super();
		this.id = id;
		this.emailId = emailId;
		this.userCode = userCode;
		this.phoneNo = phoneNo;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "email_id", nullable = false, length = 200)
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Column(name = "user_code", nullable = false, length = 200)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "phone_no", nullable = false, length = 200)
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Column(name = "name", nullable = false, length = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserDetails [id=" + id + ", emailId=" + emailId + ", userCode=" + userCode + ", phoneNo=" + phoneNo
				+ ", name=" + name + "]";
	}

}
