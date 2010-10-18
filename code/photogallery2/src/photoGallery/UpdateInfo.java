package photoGallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import openwcm.OpenWcmServlet;
import openwcm.UcmServletInfo;
import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class UpdateInfo {
	private String collectionId;
	private String parentId;
	private String photoId;
	
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void getUpdateInfo( 
			ServletRequest request,
			ServletContext application, 
			String username,
			String title,
			String comment,
			String ID,
			String contentID) {
		
		IdcContext idcContext = new IdcContext(username);
		IdcClient idcClient = (IdcClient) application.getAttribute(OpenWcmServlet.IDC_CLIENT);
		DataBinder dataBinder = idcClient.createBinder();
		
		dataBinder.putLocal("IdcService", "UPDATE_DOCINFO");
		dataBinder.putLocal("dID", ID);
		dataBinder.putLocal("dDocName", contentID);
		dataBinder.putLocal("dDocTitle", title);
		dataBinder.putLocal("xComments", comment);

		try {
			ServiceResponse idc_response = idcClient.sendRequest(idcContext, dataBinder);
			DataBinder data = idc_response.getResponseAsBinder();
	
			idc_response.close();
			
		} catch (IdcClientException e) {
			System.out.println(e);
		}
	}
}

