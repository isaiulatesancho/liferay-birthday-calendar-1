package com.rivetlogic.birthday.model;

import com.liferay.portal.kernel.json.JSON;

import java.util.Date;

@JSON(strict = true)
public class UserBirthday {
	private String name;
	private Date userBirthday;
	private String portrait;
	private String profileUrl;
	
	@JSON
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JSON
	public Date getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}
	
	@JSON
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	
	@JSON
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
}
