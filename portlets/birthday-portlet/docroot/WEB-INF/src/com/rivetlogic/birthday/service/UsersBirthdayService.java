package com.rivetlogic.birthday.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.theme.ThemeDisplay;
import com.rivetlogic.birthday.model.UserBirthday;
import com.rivetlogic.birthday.service.result.PaginatedResult;


public interface UsersBirthdayService {
	public PaginatedResult<UserBirthday> getBirthdaysByDate(int month, int day, int year,
			ThemeDisplay themeDisplay, long timeZoneOffset)
			throws SystemException, PortalException;

	public PaginatedResult<UserBirthday> getBirthdaysByWeek(int minDateYear, int minDateMonth, int minDateDay, 
			int maxDateYear, int maxDateMonth, int maxDateDay, long timeZoneOffset,
			ThemeDisplay themeDisplay)
			throws SystemException, PortalException;

	public PaginatedResult<UserBirthday> getBirthdaysByMonth(
			int year, int month, int day, long timeZoneOffset, int startDay, int endDay, ThemeDisplay themeDisplay)
			throws SystemException, PortalException;
	
	public PaginatedResult<UserBirthday> getGroupedMonthBirthdays(int year,
			int month, long timeZoneOffset)
			throws SystemException, PortalException;
	
	public PaginatedResult<UserBirthday> getGroupedDaysBirthdays(int year,
			int month, int[] days, long timeZoneOffset, ThemeDisplay themeDisplay, Integer start, Integer limit)
			throws SystemException, PortalException;
}
