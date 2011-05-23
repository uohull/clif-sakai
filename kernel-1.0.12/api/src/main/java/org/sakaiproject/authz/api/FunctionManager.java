/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/api/src/main/java/org/sakaiproject/authz/api/FunctionManager.java $
 * $Id: FunctionManager.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2008 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.authz.api;

import java.util.List;

/**
 * <p>
 * FunctionManager is the API for the service that manages security function registrations from the various Sakai applications.
 * </p>
 */
public interface FunctionManager
{
	/**
	 * Register an authz function
	 * 
	 * param function The function name.
	 */
	void registerFunction(String function);

	/**
	 * Access all the registered functions.
	 * 
	 * @return A List (String) of registered functions.
	 */
	List getRegisteredFunctions();

	/**
	 * Access all the registered functions that begin with the string.
	 * 
	 * @param prefix
	 *        The prefix pattern to find.
	 * @return A List (String) of registered functions that begin with the string.
	 */
	List getRegisteredFunctions(String prefix);
}
