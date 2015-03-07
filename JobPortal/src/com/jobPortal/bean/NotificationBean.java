package com.jobPortal.bean;

import java.io.Serializable;

public class NotificationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String from_user;
	public String getFrom_user() {
		return from_user;
	}

	public void setFrom_user(String from_user) {
		this.from_user = from_user;
	}

	public String getTo_user() {
		return to_user;
	}

	public void setTo_user(String to_user) {
		this.to_user = to_user;
	}

	private String to_user;
	private String type_user;
	private String message;
	private String created_at;

	public NotificationBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getType_user() {
		return type_user;
	}

	public void setType_user(String type_user) {
		this.type_user = type_user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
