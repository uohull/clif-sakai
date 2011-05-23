/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/test/java/org/sakaiproject/content/impl/serialize/impl/test/ProfileSerializerTest.java $
 * $Id: ProfileSerializerTest.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
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

package org.sakaiproject.content.impl.serialize.impl.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.impl.serialize.impl.Type1BaseContentCollectionSerializer;

import junit.framework.TestCase;

/**
 * @author ieb
 */
public class ProfileSerializerTest extends TestCase
{

	private static final Log log = LogFactory.getLog(ProfileSerializerTest.class);

	/**
	 * @param name
	 */
	public ProfileSerializerTest(String name)
	{
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link org.sakaiproject.content.impl.serialize.impl.Type1BaseContentCollectionSerializer#parse(org.sakaiproject.entity.api.serialize.SerializableEntity, java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	public final void testParse() throws Exception
	{
		Type1BaseContentCollectionSerializer t1 = new Type1BaseContentCollectionSerializer();
		t1.setTimeService(new MockTimeService());
		MockSerializableCollectionAcccess sc = new MockSerializableCollectionAcccess();
		byte[] serialized = null;
		Runtime r = Runtime.getRuntime();
		r.gc();
		Thread.sleep(2000);
		{
			long start = System.currentTimeMillis();
			long ms = r.freeMemory();
			for (int i = 0; i < 16000; i++)
			{
				serialized = t1.serialize(sc);
			}
			long me = r.freeMemory();
			long m = ms - me;
			long end = System.currentTimeMillis();
			long t = (end - start);
			log.info("Write 16000 Entities took " + t + "ms ");
			log.info("Write 16000 Entities took " + (t * 1000) / 16000 + " us/entity ");
			log.info("Write 16000 Entities took " + m + " bytes overhead ");
			log.info("Write 16000 Entities took " + (m / 16000) + " bytes/entity overhead ");
		}
		r.gc();
		Thread.sleep(2000);
		{
			long start = System.currentTimeMillis();
			long ms = r.freeMemory();
			for (int i = 0; i < 16000; i++)
			{
				t1.parse(sc, serialized);
			}
			long me = r.freeMemory();
			long m = ms - me;
			long end = System.currentTimeMillis();
			long t = (end - start);
			log.info("Read 16000 Entities took " + t + "ms ");
			log.info("Read 16000 Entities took " + (t * 1000) / 16000 + " us/entity ");
			log.info("Read 16000 Entities took " + m + " bytes overhead ");
			log.info("Read 16000 Entities took " + (m / 16000) + " bytes/entity overhead ");
		}
		sc.check();
	}
}
