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

package com.rivetlogic.birthday.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableLocalService;

/**
 * Provides the local service utility for CustomContact. This utility wraps
 * {@link com.rivetlogic.birthday.service.impl.CustomContactLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see CustomContactLocalService
 * @see com.rivetlogic.birthday.service.base.CustomContactLocalServiceBaseImpl
 * @see com.rivetlogic.birthday.service.impl.CustomContactLocalServiceImpl
 * @generated
 */
public class CustomContactLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.rivetlogic.birthday.service.impl.CustomContactLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return getService().invokeMethod(name, parameterTypes, arguments);
	}

	public static java.util.List<com.liferay.portal.model.Contact> findUsersByDate(
		int month, int day)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().findUsersByDate(month, day);
	}

	public static java.util.Date getUpcomingBirthday(java.util.Date lastDate,
		long tzOffset)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getUpcomingBirthday(lastDate, tzOffset);
	}

	public static java.util.List<com.liferay.portal.model.Contact> getContactsByWeek(
		int minDateYear, int minDateMonth, int minDateDay, int maxDateYear,
		int maxDateMonth, int maxDateDay)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .getContactsByWeek(minDateYear, minDateMonth, minDateDay,
			maxDateYear, maxDateMonth, maxDateDay);
	}

	public static java.util.List<com.liferay.portal.model.Contact> getContactsByBirthdayMonth(
		int month, int startDay, int endDay)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getContactsByBirthdayMonth(month, startDay, endDay);
	}

	public static java.util.Map<java.lang.Integer, java.lang.Integer> getGroupedMonthBirthdays(
		int year, int month)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getGroupedMonthBirthdays(year, month);
	}

	public static void clearService() {
		_service = null;
	}

	public static CustomContactLocalService getService() {
		if (_service == null) {
			InvokableLocalService invokableLocalService = (InvokableLocalService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					CustomContactLocalService.class.getName());

			if (invokableLocalService instanceof CustomContactLocalService) {
				_service = (CustomContactLocalService)invokableLocalService;
			}
			else {
				_service = new CustomContactLocalServiceClp(invokableLocalService);
			}

			ReferenceRegistry.registerReference(CustomContactLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setService(CustomContactLocalService service) {
	}

	private static CustomContactLocalService _service;
}