/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/api/src/main/java/org/sakaiproject/content/api/ContentResourceEdit.java $
 * $Id: ContentResourceEdit.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
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

package org.sakaiproject.content.api;

import java.io.InputStream;
import java.io.OutputStream;

import org.sakaiproject.content.api.GroupAwareEdit;
import org.sakaiproject.entity.api.Edit;
import org.sakaiproject.time.api.Time;

/**
* <p>ContentResource is an editable ContentResource.</p>
*/
public interface ContentResourceEdit
	extends ContentResource, Edit, GroupAwareEdit
{
	/**
	* Set the content byte length.
	* @param length The content byte length.
	*/
	public void setContentLength(int length);

	/**
	* Set the resource MIME type.
	* @param type The resource MIME type.
	*/
	public void setContentType(String type);

	/**
	* Set the resource content.
	* @param content An array containing the bytes of the resource's content.
	*/
	public void setContent(byte[] content);
	
	/**
     * @param stream
     */
    public void setContent(InputStream stream);

}	// ContentResourceEdit



