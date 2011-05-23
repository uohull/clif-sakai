/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/kernel/tags/kernel-1.0.12/kernel-impl/src/test/java/org/sakai/memory/impl/test/MemoryServiceTest.java $
 * $Id: MemoryServiceTest.java 51317 2008-08-24 04:38:02Z csev@umich.edu $
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

package org.sakai.memory.impl.test;

import java.util.Random;

import junit.framework.TestCase;
import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.event.api.UsageSessionService;
import org.sakaiproject.memory.api.Cache;
import org.sakaiproject.memory.impl.BasicMemoryService;

/**
 * @author ieb
 *
 */
public class MemoryServiceTest extends TestCase
{

	private static Log log = LogFactory.getLog(MemoryServiceTest.class);
	private EventTrackingService eventTrackingService;
	private UsageSessionService usageSessionService;
	private SecurityService securityService;
	private BasicMemoryService basicMemoryService;
	private CacheManager cacheManager;

	/**
	 * @param name
	 */
	public MemoryServiceTest(String name)
	{
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		
		eventTrackingService = new MockEventTrackingService();
		securityService = new MockSecurityService();
		usageSessionService = new MockUsageSessionService();
		basicMemoryService = new MockBasicMemoryService(eventTrackingService, securityService, usageSessionService );
		cacheManager = new CacheManager(this.getClass().getResourceAsStream("ehcache.xml"));
		basicMemoryService.setCacheManager(cacheManager);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	public void testCreate() {
		Cache cache = basicMemoryService.newCache("org.sakaiproject.alias.api.AliasService.callCache","");
		Random r = new Random();
		int hit = 0;
		int miss = 0;
		for ( int i = 0; i < 10000; i++) {
			int k = r.nextInt(1000);
			if ( cache.containsKey(k)) {
				Object k2 = cache.get(k);
				hit++;
				assertNotNull(k2);
			} else {
				cache.put(k,k);
				miss++;
			}
		}
		log.info("Hits ="+hit+" Misses="+miss);
	}
	public void XtestGetLong() {
		Cache cache = basicMemoryService.newCache("org.sakaiproject.alias.api.AliasService.callCache","");
		int hit = 0;
		int miss = 0;
		for ( int i = 0; i < 100; i++) {
			cache.put(i, i);
		}
		long endTime = System.currentTimeMillis() + 10*60*1000;
		long ncount = 0;
		long errors = 0;
		while(errors < 100 && System.currentTimeMillis() < endTime ) {
		for ( int i = 0; i < 100; i++) {
			ncount++;
			if ( cache.containsKey(i) ) {
				if ( cache.get(i) == null ) {
					log.info("Found Null Key for "+i+" after "+ncount+" attempts ");
					errors++;
				}
			} else {
				log.info("Key missing ");
				cache.put(i, i);
			}
		}
		}
		log.info("All Ok");
	}

}
