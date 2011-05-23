/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/main/java/org/sakaiproject/content/impl/CollectionAccessFormatter.java $
 * $Id: CollectionAccessFormatter.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 Sakai Foundation
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

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.Validator;

/**
 * <p>
 * CollectionAccessFormatter is formatter for collection access.
 * </p>
 */
public class CollectionAccessFormatter
{
	/**
	 * Format the collection as an HTML display.
	 */
	public static void format(ContentCollection x, Reference ref, HttpServletRequest req, HttpServletResponse res,
			String accessPointTrue, String accessPointFalse)
	{
		// do not allow directory listings for /attachments and its subfolders  
		if(ContentHostingService.isAttachmentResource(x.getId()))
		{
			try
			{
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} 
			catch ( java.io.IOException e ) 
			{
				return;
			}
		}
		
		PrintWriter out = null;
		// don't set the writer until we verify that
		// getallresources is going to work.
		boolean printedHeader = false;
		boolean printedDiv = false;

		String path = ref.getId();
		String basedir = req.getParameter("sbasedir");
		// path may have been transformed by an alias
		// we need origpath to check whether there was as trailing /
		String origpath = req.getPathInfo();
		String querystring = req.getQueryString();
		// set access to /access/content, must skip http://host
		String access = accessPointFalse;
		int i = access.indexOf("://");
		if (i > 0) i = access.indexOf("/", i + 3);
		if (i > 0) access = access.substring(i);

		boolean sferyx = true;
		if (basedir == null || basedir.equals(""))
		{
			sferyx = false;
			basedir = req.getParameter("basedir");
		}
		String field = req.getParameter("field");

		// System.out.println("basedir " + basedir);

		if (field == null) field = "url";

		try
		{
			List members = x.getMemberResources();
			// we will need resources. getting them once makes the sort a whole lot faster
			// System.out.println("before sort have " + members.size());

			boolean hasCustomSort = false;
			try {
			    hasCustomSort = x.getProperties().getBooleanProperty(ResourceProperties.PROP_HAS_CUSTOM_SORT);
			} catch (Exception e) {
			    // use false that's already there
			}

			if (sferyx || basedir != null)
			    Collections.sort(members, new ContentHostingComparator(ResourceProperties.PROP_DISPLAY_NAME, true));
			else if (hasCustomSort)
			    Collections.sort(members, new ContentHostingComparator(ResourceProperties.PROP_CONTENT_PRIORITY, true));
			else
			    Collections.sort(members, new ContentHostingComparator(ResourceProperties.PROP_DISPLAY_NAME, true));

			// System.out.println("after sort have " + members.size());

			Iterator xi = members.iterator();

			res.setContentType("text/html; charset=UTF-8");

			out = res.getWriter();

			if (sferyx)
			{
				out
						.println("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\"><title>Control Panel - FileBrowser</title></head><body bgcolor=\"#FFFFFF\" topmargin=\"0\" leftmargin=\"0\"><b><font color=\"#000000\" face=\"Arial\" size=\"3\">Path:&nbsp;"
								+ access
								+ Validator.escapeHtml(path)
								+ "</font></b><table border=\"0\" width=\"100%\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\">");
				printedHeader = true;

			}
			else
			{
				ResourceProperties pl = x.getProperties();
				out
						.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
				out.println("<html><head>");
				out.println("<title>" + "Index of " + pl.getProperty(ResourceProperties.PROP_DISPLAY_NAME) + "</title>");
				String webappRoot = ServerConfigurationService.getServerUrl();
				out.println("<link href=\"" + webappRoot
						+ "/library/skin/default/access.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen\" />");
				if (basedir != null)
				{
					out.println("<script type=\"text/javascript\">");
					out.println("function seturl(url) {");
					out.println("window.opener.document.forms[0]." + Validator.escapeJavascript(field) + ".value = url;  window.close();");
					out.println("}");
					out.println("</script>");
				}

				out.println("</head><body>");
				out.println("<div class=\"directoryIndex\">");
				// for content listing it's best to use a real title
				if (basedir != null)
					out.println("<h2>Contents of " + access + path + "</h2>");
				else
				{
					out.println("<h2>" + pl.getProperty(ResourceProperties.PROP_DISPLAY_NAME) + "</h2>");
					String desc = pl.getProperty(ResourceProperties.PROP_DESCRIPTION);
					if (desc != null && !desc.equals("")) out.println("<p>" + desc + "</p>");
				}

				out.println("<table summary=\"Directory index\">");
				printedHeader = true;
				printedDiv = true;
			}

			int slashes = countSlashes(path);

			// basedir will be a full url:
			// http://host:8080/access/content/group/db5a4d0c-3dfd-4d10-8018-41db42ac7c8b/
			// possibly with a file name on the end.
			// xss is just the file name. Compute a prefix

			String filepref = "";
			// /content
			String relaccess = accessPointTrue;
			if (basedir != null && !basedir.equals("none"))
			{
				// start bases after /access/content, since it isn't in path
				String bases = basedir.substring(basedir.indexOf(relaccess) + relaccess.length());
				int lastslash = 0;
				// path is always a directory, so it ends in /
				// do that for base as well
				if (!bases.endsWith("/"))
				{
					lastslash = bases.lastIndexOf("/");
					if (lastslash > 0) bases = bases.substring(0, lastslash + 1);
				}
				// path and bases should now be comparable, starting
				// at /user or /group and ending in /
				// bases: /a/b/c/
				// path: /a/b/d/
				// need ../d
				// this code is used in a context where we know there
				// actually is overlap
				while (bases.length() > path.length() || (!bases.equals("/") && !bases.equals(path.substring(0, bases.length()))))
				{
					lastslash = bases.lastIndexOf("/", bases.length() - 2);
					if (lastslash < 0) break;
					filepref = filepref + "../";
					bases = bases.substring(0, lastslash + 1);
				}
				// bases is now the common part, e.g. /a/b/ /a/b/c
				// add the rest of path
				if (path.length() > bases.length()) filepref = filepref + Validator.escapeUrl(path.substring(bases.length()));
			}
			else if (basedir != null && basedir.equals("none"))
			{
				filepref = access + Validator.escapeUrl(path);
			}

			// for web content format, need to be able to choose main URL

			String baseparam = "";
			if (sferyx)
				baseparam = "?sbasedir=" + Validator.escapeUrl(basedir);
			else if (basedir != null)
				baseparam = "?basedir=" + Validator.escapeUrl(basedir) + "&field=" + Validator.escapeUrl(field);

			if (slashes > 3)
			{
				// go up a level
				String uplev = path.substring(0, path.length() - 1);
				uplev = access + uplev.substring(0, uplev.lastIndexOf('/') + 1);

				if (sferyx)
					out
							.println("<tr><td align=\"center\" left=\"50%\" height=\"20\"><b><a href=\"../"
									+ baseparam
									+ "\">Up one level</a></b></td><td width=\"20%\"></td><td width=\"30%\"></td></tr><form name=\"fileSelections\">");
				else if (basedir != null)
					out.println("<tr><td><a href=\"../" + baseparam + "\">Up one level</a></td><td><b>Folder</b>" + "</td><td>"
							+ "</td><td>" + "</td><td>" + "</td></tr>");
				else
					out.println("<tr><td><a href=\"../\">Up one level</a> [Folder]</td><td></td></tr>");
			}
			else if (sferyx)
				out
						.println("<tr><td align=\"center\" left=\"50%\" height=\"20\">&nbsp;</td><td width=\"20%\"></td><td width=\"30%\"></td></tr><form name=\"fileSelections\">");

			while (xi.hasNext())
			{
				// System.out.println("hasnext");
				Entity nextres = (Entity) xi.next();
				ResourceProperties properties = nextres.getProperties();
				boolean isCollection = properties.getBooleanProperty(ResourceProperties.PROP_IS_COLLECTION);
				String xs = nextres.getId();

				ContentResource content = null;
				if (isCollection)
				{
					xs = xs.substring(0, xs.length() - 1);
					xs = xs.substring(xs.lastIndexOf('/') + 1) + '/';
				}
				else
				{
					content = (ContentResource) nextres;
					xs = xs.substring(xs.lastIndexOf('/') + 1);
				}

				// System.out.println("id " + xs);

				try
				{

					if (isCollection)
					{
						if (sferyx)
							out
									.println("<tr><td bgcolor=\"#FFF678\" align=\"LEFT\"><font face=\"Arial\" size=\"3\">&nbsp;<a href=\""
											+ Validator.escapeUrl(xs)
											+ baseparam
											+ "\">"
											+ Validator.escapeHtml(xs)
											+ "</a></font></td><td bgcolor=\"#FFF678\" align=\"RIGHT\"><font face=\"Arial\" size=\"3\" color=\"#000000\">File Folder</font></td><td>&nbsp;</td></tr>");
						else if (basedir != null)
							out.println("<tr><td><button type=button onclick=\"seturl('" + filepref + Validator.escapeHtml(xs)
									+ "')\">Choose</button>&nbsp;<a href=\"" + Validator.escapeUrl(xs) + baseparam + "\">"
									+ Validator.escapeHtml(xs) + "</a></td><td><b>Folder</b>" + "</td><td>" + "</td><td>"
									+ "</td><td>" + "</td></tr>");
						else
						{
							String desc = properties.getProperty(ResourceProperties.PROP_DESCRIPTION);
							if (desc == null) desc = "";

							out.println("<tr><td><a href=\"" + Validator.escapeUrl(xs) + baseparam + "\">"
									+ Validator.escapeHtml(properties.getProperty(ResourceProperties.PROP_DISPLAY_NAME))
									+ "</a> [Folder]</td><td>" + Validator.escapeHtml(desc) + "</td></tr>");
						}
					}
					else
					{
						long filesize = ((content.getContentLength() - 1) / 1024) + 1;
						String createdBy = getUserProperty(properties, ResourceProperties.PROP_CREATOR).getDisplayName();
						Time modTime = properties.getTimeProperty(ResourceProperties.PROP_MODIFIED_DATE);
						String modifiedTime = modTime.toStringLocalShortDate() + " " + modTime.toStringLocalShort();
						String filetype = content.getContentType();

						if (sferyx)
							out
									.println("<tr><td bgcolor=\"#FFFFFF\" align=\"LEFT\"><font face=\"Arial\" size=\"3\">&nbsp;&nbsp;</font><input type=\"submit\" name=\"selectedFiles\" value=\""
											+ filepref
											+ Validator.escapeUrl(xs)
											+ "\"></td><td bgcolor=\"#FFFFFF\" align=\"RIGHT\"><font face=\"Arial\" size=\"3\">"
											+ filesize
											+ "</font></td><td bgcolor=\"#FFFFFF\" align=\"LEFT\"><font face=\"Arial\" size=\"3\">&nbsp;&nbsp;"
											+ modifiedTime + "</font></td></tr>");
						else if (basedir != null)
							out.println("<tr><td><button type=button onclick=\"seturl('" + filepref + Validator.escapeHtml(xs)
									+ "')\">Choose</button>&nbsp;&nbsp;" + Validator.escapeHtml(xs) + "</td><td>" + filesize
									+ "</td><td>" + createdBy + "</td><td>" + filetype + "</td><td>" + modifiedTime + "</td></tr>");
						else
						{
							String desc = properties.getProperty(ResourceProperties.PROP_DESCRIPTION);
							if (desc == null) desc = "";

							out.println("<tr><td><a href=\"" + Validator.escapeUrl(xs) + "\" target=_blank>"
									+ Validator.escapeHtml(properties.getProperty(ResourceProperties.PROP_DISPLAY_NAME))
									+ "</a></td><td>" + Validator.escapeHtml(desc) + "</td></tr>");
						}
					}
				}
				catch (Throwable ignore)
				{
					if (sferyx)
						out
								.println("<tr><td bgcolor=\"#FFFFFF\" align=\"LEFT\"><font face=\"Arial\" size=\"3\">&nbsp;&nbsp;</font><input type=\"submit\" name=\"selectedFiles\" value=\""
										+ filepref
										+ Validator.escapeHtml(xs)
										+ "\"></td><td bgcolor=\"#FFFFFF\" align=\"RIGHT\"><font face=\"Arial\" size=\"3\">&nbsp</font></td><td bgcolor=\"#FFFFFF\" align=\"LEFT\"><font face=\"Arial\" size=\"3\">&nbsp;&nbsp;</font></td></tr>");
					else if (basedir != null)
						out.println("<tr><td><button type=button onclick=\"seturl('" + filepref + Validator.escapeHtml(xs)
								+ "')\">Choose</button>&nbsp;&nbsp;" + Validator.escapeHtml(xs) + "</td><td>" + "</td><td>"
								+ "</td><td>" + "</td><td>" + "</td></tr>");
					else
						out.println("<tr><td><a href=\"" + Validator.escapeUrl(xs) + "\" target=_blank>" + Validator.escapeHtml(xs)
								+ "</a></td><td></tr>");
				}
			}

		}
		catch (Throwable ignore)
		{
		}

		if (out != null && printedHeader)
		{
			out.println("</table>");
			if (printedDiv) out.println("</div>");
			out.println("</body></html>");
		}
	}

	public static int countSlashes(String s)
	{
		int count = 0;
		int loc = s.indexOf('/');

		while (loc >= 0)
		{
			count++;
			loc++;
			loc = s.indexOf('/', loc);
		}

		return count;
	}

	protected static User getUserProperty(ResourceProperties props, String name)
	{
		String id = props.getProperty(name);
		if (id != null)
		{
			try
			{
				return UserDirectoryService.getUser(id);
			}
			catch (UserNotDefinedException e)
			{
			}
		}
		
		return null;
	}
}
