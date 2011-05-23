/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/main/java/org/sakaiproject/tool/impl/SessionComponentTest.java $
 * $Id: SessionComponentTest.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2008 Sakai Foundation
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

package org.sakaiproject.tool.impl;

import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.thread_local.api.ThreadLocalManager;

/**
 * <p>
 * SessionComponentTest extends the session component providing the dependency injectors for testing.
 * </p>
 */
public class SessionComponentTest extends SessionComponent
{
	/**
	 * @return the ThreadLocalManager collaborator.
	 */
	protected ThreadLocalManager threadLocalManager()
	{
		return null;
	}

	/**
	 * @return the IdManager collaborator.
	 */
	protected IdManager idManager()
	{
		return null;
	}
}
