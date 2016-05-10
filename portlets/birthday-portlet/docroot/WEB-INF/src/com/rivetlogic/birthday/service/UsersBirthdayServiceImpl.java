package com.rivetlogic.birthday.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.rivetlogic.birthday.model.UserBirthday;
import com.rivetlogic.birthday.service.result.GroupedRecords;
import com.rivetlogic.birthday.service.result.Page;
import com.rivetlogic.birthday.service.result.PaginatedResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class UsersBirthdayServiceImpl implements UsersBirthdayService{
	
	private Date dateWithBeginingOfDay(int year, int month, int day){
		return new DateTime(year, month, day, 0, 0, 0).withTimeAtStartOfDay().toDate();
	}
	
	@Override
	public PaginatedResult<UserBirthday> getBirthdaysByDate(int month, int day, int year, ThemeDisplay themeDisplay, long timeZoneOffset) 
			throws SystemException, PortalException {

		PaginatedResult<UserBirthday> result = null;
		try{
			List<Contact>contacts = ListUtil.copy(CustomContactLocalServiceUtil.findUsersByDate(month, day));
			if(null != contacts && !contacts.isEmpty()){
				for(Contact contact : contacts){
					contact.setBirthday(new DateTime(contact.getBirthday()).withYear(year).toDate());
				}
				result = transformContactsToUserResult(contacts, timeZoneOffset, themeDisplay);
				result.setDates(null);
			}
			if(null == result || (null == result.getUsers() || result.getUsers().isEmpty())){
				result = getUpcomingBirthday(dateWithBeginingOfDay(year, month, day), timeZoneOffset);
			}
		} catch(Exception e){
			LOGGER.error(String.format(ERROR_RETRIEVING_USERS, DATE, e.getMessage()));
			throw e;
		}
		 return result;
	}
	
	@Override
	public PaginatedResult<UserBirthday> getBirthdaysByWeek(int minDateYear, int minDateMonth, int minDateDay, 
			int maxDateYear, int maxDateMonth, int maxDateDay, long timeZoneOffset, ThemeDisplay themeDisplay) 
			throws SystemException, PortalException {
		
		PaginatedResult<UserBirthday> result = null;
		try{
			List<Contact>contacts = ListUtil.copy(CustomContactLocalServiceUtil.getContactsByWeek(minDateYear, minDateMonth, minDateDay,
					maxDateYear, maxDateMonth, maxDateDay));
			if(null != contacts && !contacts.isEmpty()){
				for(Contact contact : contacts){
					DateTime dateTime = new DateTime(contact.getBirthday()).withYear(minDateYear);
					if(dateTime.getDayOfMonth() <= minDateMonth){
						dateTime = dateTime.withYear(maxDateYear);
					}
					else{
						dateTime = dateTime.withYear(minDateYear);
					}
					contact.setBirthday(dateTime.toDate());
				}
				
				result = transformContactsToUserResult(contacts, timeZoneOffset, themeDisplay);
				result.setUsers(null);
			}
			if(null == result || (null == result.getDates() || result.getDates().isEmpty())){
				result = getUpcomingBirthday(dateWithBeginingOfDay(minDateYear, minDateMonth, minDateDay), timeZoneOffset);
			}
		} catch(Exception e){
			LOGGER.error(String.format(ERROR_RETRIEVING_USERS, WEEK, e.getMessage()));
			throw e;
		}
		 return result;
	}
	
	@Override
	public PaginatedResult<UserBirthday> getBirthdaysByMonth(int year, int month, int day, long timeZoneOffset, int startDay, int endDay, ThemeDisplay themeDisplay) 
			throws SystemException, PortalException {
		PaginatedResult<UserBirthday> result = null;
		try{
			List<Contact>contacts = ListUtil.copy(CustomContactLocalServiceUtil.getContactsByBirthdayMonth(month, startDay, endDay));
			if(null != contacts && !contacts.isEmpty()){
				for(Contact contact : contacts){
					contact.setBirthday(new DateTime(contact.getBirthday()).withYear(year).toDate());
				}
				result = transformContactsToUserResult(contacts, timeZoneOffset, themeDisplay);
				result.setUsers(null);
			}
			if(null == result || (null == result.getDates() || result.getDates().isEmpty())){
				result = getUpcomingBirthday(dateWithBeginingOfDay(year, month, day), timeZoneOffset);
			}
		} catch(Exception e){
			LOGGER.error(String.format(ERROR_RETRIEVING_USERS, MONTH, e.getMessage()));
			throw e;
		}
		 return result;
	}
	
	@Override
	public PaginatedResult<UserBirthday> getGroupedMonthBirthdays(int year,
			int month, long timeZoneOffset) throws SystemException, PortalException {
		PaginatedResult<UserBirthday> result = null;
		try{
			
			Map<Integer, Integer> daysMap  = CustomContactLocalServiceUtil.getGroupedMonthBirthdays(year, month);
			if(null != daysMap && !daysMap.isEmpty()){
				List<GroupedRecords<UserBirthday>> groupedList = new ArrayList<GroupedRecords<UserBirthday>>();
				Set<Entry<Integer, Integer>> set = daysMap.entrySet();
				for(Entry<Integer, Integer> entry : set){
					DateTime dateTime = new DateTime(year, month, entry.getKey(), 0, 0);
					dateTime = convertDateToUTCDateTime(dateTime.toDate());
					if(timeZoneOffset > 0){
						dateTime = dateTime.plus(timeZoneOffset);
					}else{
						dateTime = dateTime.minus(timeZoneOffset);
					}
					groupedList.add(new GroupedRecords<UserBirthday>(dateTime.toDate(), null, entry.getValue()));
				}
				//Order the dates
				orderGroupedRecordsByDate(groupedList);
				result = new PaginatedResult<UserBirthday>(null, groupedList);
			}else{
				result = getUpcomingBirthday(dateWithBeginingOfDay(year, month, 1), timeZoneOffset);
			}
		} catch(Exception e){
			LOGGER.error(String.format(ERROR_RETRIEVING_USERS, MONTH, e.getMessage()));
			throw e;
		}
		return result;
	}
	
	
	@Override
	public PaginatedResult<UserBirthday> getGroupedDaysBirthdays(int year,
			int month, int[] days, long timeZoneOffset, ThemeDisplay themeDisplay, Integer start, Integer limit) throws SystemException,
			PortalException {
		
		PaginatedResult<UserBirthday> result = null;
		try{
			List<Contact>contactsList = new ArrayList<>();
			int maxDay = days[0];
			for(int day : days){
				if(maxDay < day){
					maxDay = day;
				}
				List<Contact>contactsResultList = ListUtil.copy(CustomContactLocalServiceUtil.findUsersByDate(month, day));
				if(null != contactsResultList){
					for(Contact contact : contactsResultList){
						contact.setBirthday(new DateTime(contact.getBirthday()).withYear(year).toDate());
					}
					contactsList.addAll(contactsResultList);
				}
			}
			if(null != contactsList && !contactsList.isEmpty()){
				result = transformContactsToUserResult(contactsList, timeZoneOffset, themeDisplay);
				result.setUsers(null);
			}else{
				result = getUpcomingBirthday(dateWithBeginingOfDay(year, month, maxDay), timeZoneOffset);
			}
		} catch(Exception e){
			LOGGER.error(String.format(ERROR_RETRIEVING_USERS, MONTH, e.getMessage()));
			throw e;
		}
		return result;
	}
	
	private List<UserBirthday> transformContactsToUserBirthday(List<Contact> contacts, ThemeDisplay themeDisplay) 
			throws PortalException, SystemException{
		List<UserBirthday> result = null;
		if(null != contacts && !contacts.isEmpty()){
			result = new ArrayList<>();
			for(Contact contact : contacts){
				User user = UserLocalServiceUtil.getUserByContactId(contact.getContactId());
				//Exclude inactive users
				if(!user.isActive()) continue;
				UserBirthday userBirthday = new UserBirthday();
				userBirthday.setUserBirthday(contact.getBirthday());
				userBirthday.setName(contact.getFullName());
				userBirthday.setPortrait(user.getPortraitURL(themeDisplay));
				userBirthday.setProfileUrl(user.getDisplayURL(themeDisplay));
				result.add(userBirthday);
			}
		}
		return result;
	}
	
	private DateTime convertDateToUTCDateTime(Date date){
		DateTime dateTime = new DateTime(DateTimeZone.UTC);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateTime = dateTime.withYear(calendar.get(Calendar.YEAR)).withMonthOfYear(calendar.get(Calendar.MONTH) + 1).
				withDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH)).withTimeAtStartOfDay();
		return dateTime;
	}
	
	private List<GroupedRecords<UserBirthday>> groupUsersByBirthday(List<UserBirthday> birthdaysList, long timezoneOffset){
		List<GroupedRecords<UserBirthday>> result = null;
		if(null != birthdaysList && !birthdaysList.isEmpty()){
			Map<Date, List<UserBirthday>> birthdaysMap = new HashMap<>();
			result = new ArrayList<>();
			for(UserBirthday user : birthdaysList){
				Date userBirthday = user.getUserBirthday();
				if(null == userBirthday)continue;//For leap years
				DateTime userBirthdayDate = convertDateToUTCDateTime(userBirthday);
				if(timezoneOffset > 0){
					userBirthdayDate = userBirthdayDate.plus(timezoneOffset);
				}else{
					userBirthdayDate = userBirthdayDate.minus(timezoneOffset);
				}
				Date dt = userBirthdayDate.toDate();
				if(birthdaysMap.containsKey(dt)){
					birthdaysMap.get(dt).add(user);
				}else{
					birthdaysMap.put(dt, new ArrayList<UserBirthday>(Arrays.asList(new UserBirthday[]{user})));
				}
			}
			for(Entry<Date, List<UserBirthday>> entry : birthdaysMap.entrySet()){
				result.add(new GroupedRecords<UserBirthday>(entry.getKey(), entry.getValue()));
			}
		}
		//Order the dates
		orderGroupedRecordsByDate(result);
		
		return result;
	}
	
	private void orderGroupedRecordsByDate(List<GroupedRecords<UserBirthday>> groupedRecordsList){
		if(null != groupedRecordsList){
			Collections.sort(groupedRecordsList, new Comparator<GroupedRecords<UserBirthday>>() {
	
				@Override
				public int compare(GroupedRecords<UserBirthday> o1,
						GroupedRecords<UserBirthday> o2) {
					return new Long(o1.getDate().getTime()).compareTo(new Long(o2.getDate().getTime()));
				}
				
			});
		}
	}
	
	private PaginatedResult<UserBirthday> transformContactsToUserResult(List<Contact> contactsList, long timezoneOffset,
			ThemeDisplay themeDisplay) throws PortalException, SystemException{
		
		PaginatedResult<UserBirthday> paginatedResult = null;
		List<UserBirthday> birthdaysList = transformContactsToUserBirthday(contactsList, themeDisplay);
		Page<UserBirthday>page = new Page<UserBirthday>(birthdaysList);
		paginatedResult = new PaginatedResult<UserBirthday>(page, groupUsersByBirthday(page, timezoneOffset));
		return paginatedResult;
	}
	
	private String getUpcomingBirthdayStr(Date date, long timezoneOffset) throws SystemException{
		Date upcomingBirthdayDate = CustomContactLocalServiceUtil.getUpcomingBirthday(date, timezoneOffset);
		if(null != upcomingBirthdayDate){
			String nextBirthdayStr = dateToDateTimeWithTZ(upcomingBirthdayDate, timezoneOffset).toString();
			return nextBirthdayStr;
		}else{
			return null;
		}
	}
	
	private PaginatedResult<UserBirthday> getUpcomingBirthday(Date date, long timezoneOffset) throws SystemException{
		PaginatedResult<UserBirthday> paginatedResult = null;
		String nextBirthdayStr = getUpcomingBirthdayStr(date, timezoneOffset);
		if(null != nextBirthdayStr){
			paginatedResult = new PaginatedResult<UserBirthday>();
			paginatedResult.setUpcomingDate(nextBirthdayStr);
		}
		return paginatedResult;
	}

	private DateTime dateToDateTimeWithTZ(Date date, long timezoneOffset) {
		DateTime dateTime1 = new DateTime(date);
		if(timezoneOffset > 0){
			dateTime1 = dateTime1.plus(timezoneOffset);
		}else{
			dateTime1 = dateTime1.minus(timezoneOffset);
		}
		
		return dateTime1;
	}
	

	
	private static final Log LOGGER = LogFactoryUtil.getLog(UsersBirthdayService.class);
	private static final String ERROR_RETRIEVING_USERS = "Error retrieving users birthdays by %S. Error: %s";
	
	private static final String DATE = "date";
	private static final String MONTH = "month";
	private static final String WEEK = "week";

}
