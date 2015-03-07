package com.jobPortal.bean;

import java.io.Serializable;

public class StudentBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String student_id;
	private String roll_no;
	private String section;
	private String firstName;
	private String middleName;
	private String lastName;
	private String date_of_birth;
	private String gender;
	private String branch;
	private String semester;
	private String sscMarks;
	private String hscMarks;
	private String diplomaMarks;
	private String enggMarks;
	private String backlogHistory;
	private String isBacklogLive;
	private String isGapInEducation;
	private String gap_details;
	private String mobile_number;
	private String email;
	private String temp_address;
	private String per_address;
	private String certification;
	private String project_details;
	private String isActive;
	private String created_at;
	private boolean selected;
	
	private String registered;
	
	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public StudentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(String roll_no) {
		this.roll_no = roll_no;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSscMarks() {
		return sscMarks;
	}

	public void setSscMarks(String sscMarks) {
		this.sscMarks = sscMarks;
	}

	public String getHscMarks() {
		return hscMarks;
	}

	public void setHscMarks(String hscMarks) {
		this.hscMarks = hscMarks;
	}

	public String getDiplomaMarks() {
		return diplomaMarks;
	}

	public void setDiplomaMarks(String diplomaMarks) {
		this.diplomaMarks = diplomaMarks;
	}

	public String getEnggMarks() {
		return enggMarks;
	}

	public void setEnggMarks(String enggMarks) {
		this.enggMarks = enggMarks;
	}

	public String getBacklogHistory() {
		return backlogHistory;
	}

	public void setBacklogHistory(String backlogHistory) {
		this.backlogHistory = backlogHistory;
	}

	public String getIsBacklogLive() {
		return isBacklogLive;
	}

	public void setIsBacklogLive(String isBacklogLive) {
		this.isBacklogLive = isBacklogLive;
	}

	public String getIsGapInEducation() {
		return isGapInEducation;
	}

	public void setIsGapInEducation(String isGapInEducation) {
		this.isGapInEducation = isGapInEducation;
	}

	public String getGap_details() {
		return gap_details;
	}

	public void setGap_details(String gap_details) {
		this.gap_details = gap_details;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTemp_address() {
		return temp_address;
	}

	public void setTemp_address(String temp_address) {
		this.temp_address = temp_address;
	}

	public String getPer_address() {
		return per_address;
	}

	public void setPer_address(String per_address) {
		this.per_address = per_address;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getProject_details() {
		return project_details;
	}

	public void setProject_details(String project_details) {
		this.project_details = project_details;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
