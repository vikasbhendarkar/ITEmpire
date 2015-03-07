package com.jobPortal.slidingmenu.model;

public class CheckBoxModel {
	private String name;
	private boolean selected;

	public CheckBoxModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
