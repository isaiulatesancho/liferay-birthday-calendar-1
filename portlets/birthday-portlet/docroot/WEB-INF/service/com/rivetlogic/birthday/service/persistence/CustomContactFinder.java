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

package com.rivetlogic.birthday.service.persistence;

/**
 * @author Brian Wing Shun Chan
 */
public interface CustomContactFinder {
	public java.util.List<com.liferay.portal.model.Contact> getContactsByDate(
		int month, int day)
		throws com.liferay.portal.kernel.exception.SystemException;

	public java.util.Date getUpcomingBirthday(java.util.Date lastdate,
		long tzOffset)
		throws com.liferay.portal.kernel.exception.SystemException;

	public java.util.List<com.liferay.portal.model.Contact> getContactsByWeek(
		int year1, int month1, int day1, int year2, int month2, int day2)
		throws com.liferay.portal.kernel.exception.SystemException;

	public java.util.List<com.liferay.portal.model.Contact> getContactsByMonth(
		int month, int startDay, int endDay)
		throws com.liferay.portal.kernel.exception.SystemException;

	public java.util.Map<java.lang.Integer, java.lang.Integer> getGroupedBirthdaysOfMonth(
		int year, int month)
		throws com.liferay.portal.kernel.exception.SystemException;
}