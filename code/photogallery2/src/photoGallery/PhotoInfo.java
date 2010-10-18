package photoGallery;

public class PhotoInfo {
	public PhotoInfo(String filename, String contentId, String url) {
		super();
		this.filename = filename;
		this.contentId = contentId;
		this.url = url;
	}
	private String filename;
	private String contentId;
	private String url;

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
