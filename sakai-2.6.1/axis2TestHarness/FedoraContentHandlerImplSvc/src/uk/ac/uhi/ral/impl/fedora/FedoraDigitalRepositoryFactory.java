/* CVS Header
   $
   $
*/

package uk.ac.uhi.ral.impl.fedora;

import java.io.FileInputStream;
import java.util.PropertyResourceBundle;
import uk.ac.uhi.ral.DigitalRepositoryFactory;
import uk.ac.uhi.ral.DigitalRepository;
import uk.ac.uhi.ral.DigitalItemInfo;
import java.lang.reflect.Method;

public class FedoraDigitalRepositoryFactory implements DigitalRepositoryFactory {
  private String keystorePath = null;
  private String keystorePassword = null;
  private String truststorePath = null;
  private String truststorePassword = null;
  private boolean debug = false;
  private String debugOutputPath = null;
//CLIF  for AXIS2 test framework  
  private FedoraDigitalRepositoryImpl theRepository = null;
  
  public DigitalRepository create() {
	  theRepository =  new FedoraDigitalRepositoryImpl(keystorePath, keystorePassword, truststorePath, truststorePassword,
                                           debug, debugOutputPath);
	  return theRepository;
  }
//  CLIF  for AXIS2 test framework
  public FedoraDigitalRepositoryFactory(){	  
	  theRepository = new FedoraDigitalRepositoryImpl(keystorePath, keystorePassword, truststorePath, truststorePassword,
              debug, debugOutputPath);
  }
  
  // Spring injection
  public void setKeystorePath(String keystorePath) { this.keystorePath = keystorePath; }
  public void setKeystorePassword(String keystorePassword) { this.keystorePassword = keystorePassword; }
  public void setTruststorePath(String truststorePath) { this.truststorePath = truststorePath; }
  public void setTruststorePassword(String truststorePassword) { this.truststorePassword = truststorePassword; }
  public void setDebug(boolean debug) { this.debug = debug; }
  public void setDebugOutputPath(String debugOutputPath) { this.debugOutputPath = debugOutputPath; }
  public String getDebugOutputPath() { return debugOutputPath; }
//CLIF  for AXIS2 test framework  
  public FedoraDigitalRepositoryImpl getFedoraDigitalRepositoryImpl() { return theRepository; }
  public void setFedoraDigitalRepositoryImpl(FedoraDigitalRepositoryImpl repo) { 
	  theRepository = repo;
  }
  
  public String invokeTestHarnessMethod(String testMethodName) {
  	String retVal = FedoraTestHarness._SUCCESS; 
  	try {
  	  Object fedoraTestHarness =  new FedoraTestHarness();
      Method method = fedoraTestHarness.getClass().getMethod(testMethodName, FedoraDigitalRepositoryImpl.class);
      retVal = (String) method.invoke(fedoraTestHarness,theRepository);
  	}
  	catch(Exception ex) {
		retVal = FedoraTestHarness._FAILURE + ex.toString();
  	}
  	
  	return retVal;
  }  
    
}

class FedoraTestHarness {
	  
	private static String _testFolderName = "SOAPUITestFolder";
	private static String _testResourceName = "SOAPUITestFile";	
	private static String _testUserName1 = "SOAPUITestUser";
	private static PropertyResourceBundle _rootConfig;
	private static String _mountPropertiesFilePath = "C:\\apache-tomcat-5.5.28\\webapps\\axis2\\WEB-INF\\services\\testconfig.properties";
	public static String _SUCCESS = "successful!";
	public static String _FAILURE = "failure!";
	
	private static PropertyResourceBundle getRepoConfig() throws Exception {
				
		if(_rootConfig == null) {
		    try {
		  	  _rootConfig = new PropertyResourceBundle(new FileInputStream(_mountPropertiesFilePath)); 
		    }
		    catch(Exception e) {
		        throw new Exception("Can't load fedora config" +  e.toString());
		    }
		}
	    return _rootConfig;
	}
	
	private static String RootCollection() {
		
		try {
					
			return getRepoConfig().getString(DigitalRepository.CONFIG_KEY_CLIF_FEDORA_ROOT_COLLECTION);
		}
		catch(Exception ex) {;}
		return "";
	}
	
	public static String createAndFindResource(FedoraDigitalRepositoryImpl fedoraImpl) {
		String retVal = _SUCCESS;
		
		try {
		    DigitalItemInfo item = null;	    
		    DigitalRepository repo = (DigitalRepository) fedoraImpl;
		    repo.init(getRepoConfig());	
			
			item = repo.generateItem();
			item.setCreator(_testUserName1);
			item.setModifiedDate("TEST");
			item.setDisplayName(_testResourceName);
			item.setTitle(_testResourceName);
		    item.setIsCollection(false);
		    item.setIsResource(true);
		    item.setIdentifier(RootCollection() + "/::TEMPRY__PID::");
		    item.setBinaryContent(getRepoConfig().toString().getBytes());
		    String pid = fedoraImpl.createObject(item); 
			
		    if(fedoraImpl.getResource(pid) == null) {
		    	retVal = _FAILURE + "unable to verify object " + pid + " was created";	
		    }
		    // now delete ..important if caching implemented
		    fedoraImpl.deleteObject(pid);
			
		}
		catch (Exception ex) {
			
			retVal = _FAILURE + ex.toString();
		}
		return retVal;
	}
	
	public static String createAndFindFolder(FedoraDigitalRepositoryImpl fedoraImpl) {
		String retVal = _SUCCESS;
		
		try {
			
		    DigitalItemInfo item = null;	    
		    DigitalRepository repo = (DigitalRepository) fedoraImpl;
		    repo.init(getRepoConfig());	
			
		    //String pid = fedoraImpl.getNextPid();
			item = repo.generateItem();
			item.setCreator(_testUserName1);
			item.setModifiedDate("TEST");
			item.setDisplayName(_testFolderName);
			item.setTitle(_testFolderName);			
		    item.setIsCollection(true);
		    item.setIsResource(false);
		    item.setIdentifier(RootCollection() + "/::TEMPRY__PID::");	    
		    String pid = fedoraImpl.createFolder(item); 
			
		    if(fedoraImpl.getResource(pid) == null) {
		    	retVal = _FAILURE + "unable to verify object " + pid + " was created";	
		    }
		    // now delete ..important if caching implemented
		    fedoraImpl.deleteObject(pid);
		    
		}
		catch (Exception ex) {
			
			retVal = _FAILURE + ex.toString();
		}
		return retVal;
	}
		
}

