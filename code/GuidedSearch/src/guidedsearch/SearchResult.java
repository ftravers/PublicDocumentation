package guidedsearch;

public class SearchResult {

    public SearchResult(String title, String url, String filename, 
                        String comments, String subjek) {
        super();
        this.title = title;
        this.url = url;
        this.filename = filename;
        this.comments = comments;
        this.subjek = subjek;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    private String title;
    private String url;
    private String filename;
    private String comments;
    private String subjek;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setSubjek(String subjek) {
        this.subjek = subjek;
    }

    public String getSubjek() {
        return subjek;
    }
}
