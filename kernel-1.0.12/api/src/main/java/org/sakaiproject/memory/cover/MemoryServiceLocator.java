/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/api/src/main/java/org/sakaiproject/memory/cover/MemoryServiceLocator.java $
 * $Id: MemoryServiceLocator.java 61770 2009-05-04 13:02:01Z david.horwitz@uct.ac.za $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 Sakai Foundation
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

package org.sakaiproject.memory.cover;

import org.sakaiproject.component.cover.ComponentManager;

/**
 * <p>
 * MemoryService is a static Cover for the {@link org.sakaiproject.memory.api.MemoryService MemoryService}; see that interface for usage details.
 * </p>
 * 
 * @version $Revision: 61770 $
 */
public class MemoryServiceLocator
{
	private static org.sakaiproject.memory.api.MemoryService m_instance = null;

	/**
	 * Access the component instance: special cover only method.
	 * 
	 * @return the component instance.
	 */
	public static org.sakaiproject.memory.api.MemoryService getInstance()
	{
		if (ComponentManager.CACHE_COMPONENTS)
		{
			if (m_instance == null)
				m_instance = (org.sakaiproject.memory.api.MemoryService) ComponentManager
						.get(org.sakaiproject.memory.api.MemoryService.class);
			return m_instance;
		}
		else
		{
			return (org.sakaiproject.memory.api.MemoryService) ComponentManager
					.get(org.sakaiproject.memory.api.MemoryService.class);
		}
	}

}
