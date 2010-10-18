package ucm;

import oracle.stellent.ridc.IdcClient;
/*
 * The purpose of this class is to store information specific to UCM.  This can be used
 * in any web application project that talks to the content server.  You should extend
 * this class in your own package to add your web-app specific information there.  For 
 * example we have a web application that does Open WCM items.  We have another web app
 * for a photo gallery.  In the case of the OpenWcm web app we create our own package:
 * openwcm, and create a servlet there to get our application specific parameters such
 * as 'siteId' from the web.xml into the application scoped application object.  We then
 * extend this class and add more fields to it that are specific to openwcm only.
 */
public class UcmInfo {
	public static final String UCM_SERVLET_INFO = "ucmServletInfo";
	public static final String TESTING_USER = "testing_user";
	private String testingUsername;
	private boolean testing;
	private String servletName;
	private String servletContextName;

	
	private IdcClient idcClient;
	public String getTestingUsername() {
		return testingUsername;
	}
	public void setTestingUsername(String testingUsername) {
		this.testingUsername = testingUsername;
	}
	public boolean isTesting() {
		return testing;
	}
	public void setTesting(boolean testing) {
		this.testing = testing;
	}

	public IdcClient getIdcClient() {
		return idcClient;
	}
	public void setIdcClient(IdcClient newIdcClient) {
		idcClient = newIdcClient;
	}

	public String getServletName() {
		return servletName;
	}
	public void setServletName(String servletName) {
		this.servletName = servletName;
	}
	public String getServletContextName() {
		return servletContextName;
	}
	public void setServletContextName(String servletContextName) {
		this.servletContextName = servletContextName;
	}
}
