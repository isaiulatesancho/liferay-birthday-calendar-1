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

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * @author Brian Wing Shun Chan
 */
public class CustomContactFinderUtil {
	public static java.util.List<com.liferay.portal.model.Contact> getContactsByDate(
		int month, int day)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getFinder().getContactsByDate(month, day);
	}

	public static java.util.Date getUpcomingBirthday(java.util.Date lastdate,
		long tzOffset)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getFinder().getUpcomingBirthday(lastdate, tzOffset);
	}

	public static java.util.List<com.liferay.portal.model.Contact> getContactsByWeek(
		int year1, int month1, int day1, int year2, int month2, int day2)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getFinder()
				   .getContactsByWeek(year1, month1, day1, year2, month2, day2);
	}

	public static java.util.List<com.liferay.portal.model.Contact> getContactsByMonth(
		int month, int startDay, int endDay)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getFinder().getContactsByMonth(month, startDay, endDay);
	}

	public static java.util.Map<java.lang.Integer, java.lang.Integer> getGroupedBirthdaysOfMonth(
		int year, int month)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getFinder().getGroupedBirthdaysOfMonth(year, month);
	}

	public static CustomContactFinder getFinder() {
		if (_finder == null) {
			_finder = (CustomContactFinder)PortletBeanLocatorUtil.locate(com.rivetlogic.birthday.service.ClpSerializer.getServletContextName(),
					CustomContactFinder.class.getName());

			ReferenceRegistry.registerReference(CustomContactFinderUtil.class,
				"_finder");
		}

		return _finder;
	}

	public void setFinder(CustomContactFinder finder) {
		_finder = finder;

		ReferenceRegistry.registerReference(CustomContactFinderUtil.class,
			"_finder");
	}

	private static CustomContactFinder _finder;
}