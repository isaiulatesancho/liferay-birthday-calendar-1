/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.rivetlogic.birthday.service.impl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Contact;
import com.rivetlogic.birthday.service.base.CustomContactLocalServiceBaseImpl;
import com.rivetlogic.birthday.service.persistence.CustomContactFinder;
import com.rivetlogic.birthday.service.persistence.CustomContactFinderUtil;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The implementation of the custom contact local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.rivetlogic.birthday.service.CustomContactLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.rivetlogic.birthday.service.base.CustomContactLocalServiceBaseImpl
 * @see com.rivetlogic.birthday.service.CustomContactLocalServiceUtil
 */
public class CustomContactLocalServiceImpl
	extends CustomContactLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.rivetlogic.birthday.service.CustomContactLocalServiceUtil} to access the custom contact local service.
	 */
	
	public  List<Contact> findUsersByDate(int month, int day) throws SystemException {
		return CustomContactFinderUtil.getContactsByDate(month, day);
	}
	
	public  Date getUpcomingBirthday(Date lastDate, long tzOffset) throws SystemException {
		return CustomContactFinderUtil.getUpcomingBirthday(lastDate, tzOffset);
	}
	
	public List<Contact> getContactsByWeek(int minDateYear, int minDateMonth, int minDateDay,
			int maxDateYear, int maxDateMonth, int maxDateDay) throws SystemException {
		return CustomContactFinderUtil.getContactsByWeek(minDateYear, minDateMonth, minDateDay,
				maxDateYear, maxDateMonth, maxDateDay);
	}
	
	public List<Contact> getContactsByBirthdayMonth(int month, int startDay, int endDay) throws SystemException{
		return CustomContactFinderUtil.getContactsByMonth(month, startDay, endDay);
	}
	
	public Map<Integer, Integer> getGroupedMonthBirthdays(int year, int month) throws SystemException{
		return CustomContactFinderUtil.getGroupedBirthdaysOfMonth(year, month);
	}
	
	
}