/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/api/src/main/java/org/sakaiproject/tool/api/ToolManager.java $
 * $Id: ToolManager.java 67686 2009-10-12 20:01:17Z bkirschn@umich.edu $
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

package org.sakaiproject.tool.api;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.w3c.dom.Document;

/**
 * <p>
 * ToolManager holds registration of Tools available in this Sakai installation.
 * </p>
 */
public interface ToolManager
{
	/**
	 * Add this tool to the registry.
	 * @param tool The Tool to register.
	 */
	void register(Tool tool);
	
	/**
	 * Add tools in this XML DOM to the registry, using the Tool XML schema.
	 * @param toolXml The parsed XML DOM in which tools to be added to the registry are to be found.
	 */
	void register(Document toolXml);
	
	/**
	 * Add tools in this file of Tool XML schema to the registry.
	 * @param toolXmlFile The file of Tool schema XML in which tools to be added to the registry are to be found.
	 */
	void register(File toolXmlFile);

	/**
	 * Add tools in this stream of Tool XML schema to the registry.
	 * @param toolXmlFile The file of Tool schema XML in which tools to be added to the registry are to be found.
	 */
	void register(InputStream toolXmlStream);

	/**
	 * Find a tool with this well known id in the registry.
	 * @param id The tool's well known id.
	 * @return The Tool object that has this id, or null if not found.
	 */
	Tool getTool(String id);

	/**
	 * Find a set of tools that meet the critieria.
	 * A tool must have a category in the categories criteria (unless it is empty or null) to be returned.
	 * A tool must have a keyword in the keywords criteria (unless it is empty or null) to be returned.
	 * If both categories and keywords criteria are specified, the tool must meet both criteria to be returned.
	 * If neither criteria are specified, all registered tools are returned.
	 * @param categories A Set (String) of category values; if null or empty no category criteria is specified.
	 * @param keywords A Set (String) of keyword values; if null or empty no keyword criteria is specified.
	 * @return A Set (Tool) of Tool objects that meet the criteria, or an empty set if none found.
	 */
	Set findTools(Set categories, Set keywords);

	/**
	 * Access the Tool associated with the current request / thread
	 * @return The current Tool, or null if there is none.
	 */
	Tool getCurrentTool();
	
	/**
	 * Access the Tool Placement associated with the current request / thread
	 * @return The current Tool Placement, or null if there is none.
	 */
	Placement getCurrentPlacement();

	// TODO: unregister...
}



