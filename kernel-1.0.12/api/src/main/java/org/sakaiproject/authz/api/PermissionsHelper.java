/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/api/src/main/java/org/sakaiproject/authz/api/PermissionsHelper.java $
 * $Id: PermissionsHelper.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
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

/**
 * <p>
 * PermissionsHelper describes the contract (API) between the client tool and the permissions helper tool.
 * </p>
 */
public interface PermissionsHelper
{
	/** Set this tool state attribute with the prefix for the permission functions to consider for editing (i.e. "content.") */
	static final String PREFIX = "sakaiproject.permissions.prefix";

	/** Set this tool state attribute with descriptive text for the editor. */
	static final String DESCRIPTION = "sakaiproject.permissions.description";

	/** Set this tool state attribute to the entity reference of the entity whose AuthzGroup is to be edited. */
	static final String TARGET_REF = "sakaiproject.permissions.targetRef";

	/** Set this tool state attribute to the entity reference of the entity which controls the role definitions, if different than the target_ref (leave it unset if target_ref has the roles needed). */
	static final String ROLES_REF = "sakaiproject.permissions.rolesRef";
}
