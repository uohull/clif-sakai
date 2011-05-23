/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/main/java/org/sakaiproject/content/impl/BaseContentService.java $
 * $Id: BaseContentService.java 67675 2009-10-12 17:21:08Z bkirschn@umich.edu $
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

package org.sakaiproject.content.impl;

import java.util.Collection;

import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.content.api.ContentResource;

// CLIF project
public interface FedoraContentStorage 
{
	ContentCollectionEdit putCollectionAllowRecursive(String id);
	void enableStackMarker();
	void disableStackMarker();
	boolean getStackMarkerDisabled();
	Collection<String> getMemberCollectionIds(String collectionId);
	Collection<String> getMemberResourceIds(String collectionId);
	String moveResource(ContentResourceEdit thisResource, String new_id);
	String moveCollection(ContentCollectionEdit thisCollection, String new_id);
        ContentResource getResource(String id);
}