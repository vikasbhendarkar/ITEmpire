package com.jobPortal.bean;

import java.io.Serializable;

public class Company implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	private String activation;

	private String type;
	private String course;

	private String date_of_placement;
	private String salary_package;
	private String created_at;
	private boolean selected;
	private String registered;

	

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getDate_of_placement() {
		return date_of_placement;
	}

	public void setDate_of_placement(String date_of_placement) {
		this.date_of_placement = date_of_placement;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivation() {
		return activation;
	}

	public void setActivation(String activation) {
		this.activation = activation;
	}

	public String getSalary_package() {
		return salary_package;
	}

	public void setSalary_package(String salary_package) {
		this.salary_package = salary_package;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
