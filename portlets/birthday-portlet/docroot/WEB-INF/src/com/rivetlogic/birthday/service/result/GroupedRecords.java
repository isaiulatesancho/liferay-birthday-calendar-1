package com.rivetlogic.birthday.service.result;

import com.liferay.portal.kernel.json.JSON;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

@JSON(strict = true)
public class GroupedRecords <T>{
	private Date date;
	private List<T>users;
	private String dateStr;
	private long usersNumber;
	
	public GroupedRecords(Date date, List<T>users, long usersNumber){
		setDate(date);
		setUsers(users);
		setUsersNumber(usersNumber);
	}
	
	public GroupedRecords(Date date, List<T>users){
		setDate(date);
		setUsers(users);
	}
	
	@JSON
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
		if(null != date){
			setDateStr(date);
		}
	}
	@JSON
	public String getDateStr() {
		return dateStr;
	}

	private void setDateStr(Date date) {
		DateTime dateTime = new DateTime(date);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		this.dateStr = fmt.print(dateTime);
	}

	@JSON
	public List<T> getUsers() {
		return users;
	}
	public void setUsers(List<T> users) {
		this.users = users;
		if(null != users){
			setUsersNumber(users.size());
		}
	}
	
	@JSON
	public long getUsersNumber() {
		return usersNumber;
	}

	public void setUsersNumber(long usersNumber) {
		this.usersNumber = usersNumber;
	}
	
	
}
