package openwcm;

import ucm.UcmInfo;

public class OpenWcmInfo extends UcmInfo {
	// Constants
	public static final String LINK_PREFIX = "linkPrefix";
	public static final String SITE_ID = "siteId";
	
	// Data Members
	private String linkPrefix;
	private String siteId;

	// Accessors
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getLinkPrefix() {
		return linkPrefix;
	}
	public void setLinkPrefix(String linkPrefix) {
		this.linkPrefix = linkPrefix;
	}
}
