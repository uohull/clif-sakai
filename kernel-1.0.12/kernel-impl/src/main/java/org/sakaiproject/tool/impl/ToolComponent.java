/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/main/java/org/sakaiproject/tool/impl/ToolComponent.java $
 * $Id: ToolComponent.java 67686 2009-10-12 20:01:17Z bkirschn@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2007, 2008 Sakai Foundation
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

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.util.StringUtil;
import org.sakaiproject.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * ToolComponent is the standard implementation of the Sakai Tool API.
 * </p>
 */
public abstract class ToolComponent implements ToolManager
{
	/** Our log (commons). */
	private static Log M_log = LogFactory.getLog(ToolComponent.class);

	/** Key in the ThreadLocalManager for binding our current placement. */
	protected final static String CURRENT_PLACEMENT = "sakai:ToolComponent:current.placement";

	/** Key in the ThreadLocalManager for binding our current tool. */
	protected final static String CURRENT_TOOL = "sakai:ToolComponent:current.tool";

	/** The registered tools. */
	protected Map<String,Tool> m_tools = new ConcurrentHashMap<String,Tool>();

	/** tool ids to be hidden - their catagories don't matter, they don't show up on any catagorized listing. */
	protected String[] m_toolIdsToHide = null;

	/**********************************************************************************************************************************************************************************************************************************************************
	 * Dependencies
	 *********************************************************************************************************************************************************************************************************************************************************/

	/**
	 * @return the ThreadLocalManager collaborator.
	 */
	protected abstract ThreadLocalManager threadLocalManager();

	/**********************************************************************************************************************************************************************************************************************************************************
	 * Configuration
	 *********************************************************************************************************************************************************************************************************************************************************/

	/** tool ids to be stealthed (hidden). */
	protected Collection<String> m_stealthToolIds = null;

	/**
	 * Configuration - set the list of tool ids to be "stealthed". A stealthed tool does not show up in a category list of tools.
	 * 
	 * @param toolIds
	 *        The comma-separated list of tool ids to be stealthed.
	 */
	public void setStealthTools(String toolIds)
	{
		if ((toolIds == null) || (toolIds.length() == 0))
		{
			m_stealthToolIds = null;
		}
		else
		{
			m_stealthToolIds = new Vector<String>();
			String[] items = StringUtil.split(toolIds, ",");
			for (int i = 0; i < items.length; i++)
			{
				m_stealthToolIds.add(items[i]);
			}
		}
	}

	/** tool ids to be visible, not hidden, even if marked hidden or stealthed. */
	protected Collection<String> m_visibleToolIds = null;

	/**
	 * Configuration - set the list of tool ids to be visible, not hidden, even if marked hidden or stealthed.
	 * 
	 * @param toolIds
	 *        The comma-separated list of tool ids to be visible.
	 */
	public void setVisibleTools(String toolIds)
	{
		if ((toolIds == null) || (toolIds.length() == 0))
		{
			m_visibleToolIds = null;
		}
		else
		{
			m_visibleToolIds = new Vector<String>();
			String[] items = StringUtil.split(toolIds, ",");
			for (int i = 0; i < items.length; i++)
			{
				m_visibleToolIds.add(items[i]);
			}
		}
	}

	/** tool ids to be hidden, adding to the stealth list (but not trumping the visible list) */
	protected Collection<String> m_hiddenToolIds = null;

	/**
	 * Configuration - set the list of tool ids to be visible, not hidden, even if marked hidden or stealthed.
	 * 
	 * @param toolIds
	 *        The comma-separated list of tool ids to be visible.
	 */
	public void setHiddenTools(String toolIds)
	{
		if ((toolIds == null) || (toolIds.length() == 0))
		{
			m_hiddenToolIds = null;
		}
		else
		{
			m_hiddenToolIds = new Vector<String>();
			String[] items = StringUtil.split(toolIds, ",");
			for (int i = 0; i < items.length; i++)
			{
				m_hiddenToolIds.add(items[i]);
			}
		}
	}

	/**********************************************************************************************************************************************************************************************************************************************************
	 * Init and Destroy
	 *********************************************************************************************************************************************************************************************************************************************************/

	/**
	 * Final initialization, once all dependencies are set.
	 */
	public void init()
	{
		// compute the tools to hide: these are the stealth tools plus the hidden tools, minus the visible ones
		Collection<String> toHide = new HashSet<String>();

		if (m_stealthToolIds != null)
		{
			toHide.addAll(m_stealthToolIds);
		}
		
		if (m_hiddenToolIds!= null)
		{
			toHide.addAll(m_hiddenToolIds);
		}
		
		if (m_visibleToolIds != null)
		{
			toHide.removeAll(m_visibleToolIds);
		}
		
		// collect the hiddens for logging
		StringBuilder hidden = new StringBuilder();

		if (!toHide.isEmpty())
		{
			m_toolIdsToHide = new String[toHide.size()];
			int pos = 0;
			
			for (String i : toHide) {
				
				m_toolIdsToHide[pos] = i;

				hidden.append(m_toolIdsToHide[pos]);
				hidden.append(" ");

				pos++;
			}
			Arrays.sort(m_toolIdsToHide);
		}

		M_log.info("init(): hidden tools: " + hidden.toString());
	}

	/**
	 * Final cleanup.
	 */
	public void destroy()
	{
		M_log.info("destroy()");
	}

	/**********************************************************************************************************************************************************************************************************************************************************
	 * Work interface
	 *********************************************************************************************************************************************************************************************************************************************************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Set<Tool> findTools(Set categories, Set keywords)
	{
		Set<Tool> rv = new HashSet<Tool>();

		for (Iterator<Tool> i = m_tools.values().iterator(); i.hasNext();)
		{
			Tool tool = (Tool) i.next();
			if (matchCriteria(categories, tool.getCategories()) && matchCriteria(keywords, tool.getKeywords()))
			{
				// add if not hidden (requests for no (null) category include all, even hidden items)
				if ((categories == null) || (m_toolIdsToHide == null) || (Arrays.binarySearch(m_toolIdsToHide, tool.getId()) < 0))
				{
					rv.add(tool);
				}
			}
		}

		return rv;
	}

	/**
	 * {@inheritDoc}
	 */
	public Placement getCurrentPlacement()
	{
		return (Placement) threadLocalManager().get(CURRENT_PLACEMENT);
	}

	/**
	 * {@inheritDoc}
	 */
	public Tool getCurrentTool()
	{
		return (Tool) threadLocalManager().get(CURRENT_TOOL);
	}

	/**
	 * {@inheritDoc}
	 */
	public Tool getTool(String id)
	{
		return (id != null) ? (Tool) m_tools.get(id) : null;
	}

	/**
	 * Check the target values for a match in the criteria. If criteria is empty or null, the target is a match.
	 * 
	 * @param criteria
	 *        The set of String values that is the criteria - any one in the target is a match
	 * @param target
	 *        The set of String values to check against the criteria.
	 * @return true if the target meets the criteria, false if not.
	 */
	@SuppressWarnings("unchecked")
	protected boolean matchCriteria(Set criteria, Set target)
	{
		if ((criteria == null) || (criteria.isEmpty())) return true;

		for (Iterator i = target.iterator(); i.hasNext();)
		{
			String t = (String) i.next();
			if (criteria.contains(t)) return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	public void register(Document toolXml)
	{
		Element root = toolXml.getDocumentElement();
		if (!root.getTagName().equals("registration"))
		{
			M_log.info("register: invalid root element (expecting \"registration\"): " + root.getTagName());
			return;
		}

		// read the children nodes (tools)
		NodeList rootNodes = root.getChildNodes();
		final int rootNodesLength = rootNodes.getLength();
		for (int i = 0; i < rootNodesLength; i++)
		{
			Node rootNode = rootNodes.item(i);
			if (rootNode.getNodeType() != Node.ELEMENT_NODE) continue;
			Element rootElement = (Element) rootNode;

			// for tool
			if (rootElement.getTagName().equals("tool"))
			{
				org.sakaiproject.util.Tool tool = new org.sakaiproject.util.Tool();

				tool.setId(rootElement.getAttribute("id").trim());
				tool.setTitle(rootElement.getAttribute("title").trim());
				tool.setDescription(rootElement.getAttribute("description").trim());
				tool.setHome(StringUtil.trimToNull(rootElement.getAttribute("home")));

				if ("tool".equals(rootElement.getAttribute("accessSecurity")))
				{
					tool.setAccessSecurity(Tool.AccessSecurity.TOOL);
				}
				else
				{
					tool.setAccessSecurity(Tool.AccessSecurity.PORTAL);
				}

				// collect values for these collections
				Properties finalConfig = new Properties();
				Properties mutableConfig = new Properties();
				Set<String> categories = new HashSet<String>();
				Set<String> keywords = new HashSet<String>();

				NodeList kids = rootElement.getChildNodes();
				final int kidsLength = kids.getLength();
				for (int k = 0; k < kidsLength; k++)
				{
					Node kidNode = kids.item(k);
					if (kidNode.getNodeType() != Node.ELEMENT_NODE) continue;
					Element kidElement = (Element) kidNode;

					// for configuration
					if (kidElement.getTagName().equals("configuration"))
					{
						String name = kidElement.getAttribute("name").trim();
						String value = kidElement.getAttribute("value").trim();
						String type = kidElement.getAttribute("type").trim();
						if (name.length() > 0)
						{
							if ("final".equals(type))
							{
								finalConfig.put(name, value);
							}
							else
							{
								mutableConfig.put(name, value);
							}
						}
					}

					// for category
					if (kidElement.getTagName().equals("category"))
					{
						String name = kidElement.getAttribute("name").trim();
						if (name.length() > 0)
						{
							categories.add(name);
						}
					}

					// for keyword
					if (kidElement.getTagName().equals("keyword"))
					{
						String name = kidElement.getAttribute("name").trim();
						if (name.length() > 0)
						{
							keywords.add(name);
						}
					}
				}

				// set the tool's collected values
				tool.setRegisteredConfig(finalConfig, mutableConfig);
				tool.setCategories(categories);
				tool.setKeywords(keywords);

				register(tool);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void register(File toolXmlFile)
	{
		String path = toolXmlFile.getAbsolutePath();
		if (!path.endsWith(".xml"))
		{
			M_log.info("register: skipping non .xml file: " + path);
			return;
		}

		M_log.info("register: file: " + path);

		Document doc = Xml.readDocument(path);
		register(doc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void register(InputStream toolXmlStream)
	{
		Document doc = Xml.readDocumentFromStream(toolXmlStream);
		try
		{
			toolXmlStream.close();
		}
		catch (Exception e)
		{
		}

		register(doc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void register(Tool tool)
	{
		m_tools.put(tool.getId(), tool);
	}

	/**
	 * Establish the Tool associated with the current request / thread
	 * 
	 * @param Tool
	 *        The current Tool, or null if there is none.
	 */
	protected void setCurrentPlacement(Placement placement)
	{
		threadLocalManager().set(CURRENT_PLACEMENT, placement);
	}

	/**
	 * Establish the Tool associated with the current request / thread
	 * 
	 * @param Tool
	 *        The current Tool, or null if there is none.
	 */
	protected void setCurrentTool(Tool tool)
	{
		threadLocalManager().set(CURRENT_TOOL, tool);
	}
}
