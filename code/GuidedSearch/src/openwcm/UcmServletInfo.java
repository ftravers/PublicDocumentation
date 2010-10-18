package openwcm;

import oracle.stellent.ridc.IdcClient;

public class UcmServletInfo {
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

    public UcmServletInfo(String testingUsername, boolean testing, 
                          String servletName, String servletContextName, 
                          String photoGalleryRootNodeCollectionId, 
                          IdcClient idcClient, String linkPrefix, String downloadUrlPrefix) {
        super();
        this.testingUsername = testingUsername;
        this.testing = testing;
        this.servletName = servletName;
        this.servletContextName = servletContextName;
        this.photoGalleryRootNodeCollectionId = 
                photoGalleryRootNodeCollectionId;
        this.idcClient = idcClient;
        this.linkPrefix = linkPrefix;
        this.downloadUrlPrefix = downloadUrlPrefix;
    }
    private String testingUsername;
    private boolean testing;
    private String servletName;
    private String servletContextName;
    private String photoGalleryRootNodeCollectionId;
    private IdcClient idcClient;
    private String linkPrefix;
    private String downloadUrlPrefix;

    public IdcClient getIdcClient() {
        return idcClient;
    }

    public void setIdcClient(IdcClient idcClient) {
        this.idcClient = idcClient;
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }

    public void setLinkPrefix(String linkPrefix) {
        this.linkPrefix = linkPrefix;
    }

    public String getPhotoGalleryRootNodeCollectionId() {
        return photoGalleryRootNodeCollectionId;
    }

    public void setPhotoGalleryRootNodeCollectionId(String photoGalleryRootNodeCollectionId) {
        this.photoGalleryRootNodeCollectionId = 
                photoGalleryRootNodeCollectionId;
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

    public void setDownloadUrlPrefix(String downloadUrlPrefix) {
        this.downloadUrlPrefix = downloadUrlPrefix;
    }

    public String getDownloadUrlPrefix() {
        return downloadUrlPrefix;
    }
}
