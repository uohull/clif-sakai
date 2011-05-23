/**
 * 
 */
package org.sakaiproject.content.api;

import org.sakaiproject.entity.api.Entity;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.sakaiproject.exception.ServerOverloadException;

public interface ContentHostingHandlerFedora extends ContentHostingHandler
{
	public ContentEntity commit(ContentCollectionEdit edit, String finalId, ContentEntity realParent);
}
