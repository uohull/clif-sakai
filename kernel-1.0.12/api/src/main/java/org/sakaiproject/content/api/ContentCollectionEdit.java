/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/api/src/main/java/org/sakaiproject/content/api/ContentCollectionEdit.java $
 * $Id: ContentCollectionEdit.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 Sakai Foundation
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

package org.sakaiproject.content.api;

import java.util.Collection;
import java.util.Map;

import org.sakaiproject.content.api.GroupAwareEdit;
import org.sakaiproject.entity.api.Edit;
import org.sakaiproject.time.api.Time;

/**
* <p>ContentCollectionEdit is an editable ContentCollection.</p>
*/
public interface ContentCollectionEdit
	extends ContentCollection, Edit, GroupAwareEdit
{

	/**
	 * @param priorities
	 */
	void setPriorityMap(Map priorities);
	
}	// ContentCollectionEdit



