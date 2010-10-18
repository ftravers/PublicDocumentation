package photoGallery;

public class Photo {
	public Photo(String filename, String contentId, String url, String comments, String title, String createDate, String ID, String author) {
		super();
		this.filename = filename;
		this.contentId = contentId;
		this.url = url;
		this.comments = comments;
		this.title = title;
		this.createDate = createDate;
		this.ID = ID;
		this.author = author;
	}
	private String filename;
	private String contentId;
	private String url;
	private String comments;
	private String title;
	private String createDate;
	private String ID;
	private String author;

	
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
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
}
