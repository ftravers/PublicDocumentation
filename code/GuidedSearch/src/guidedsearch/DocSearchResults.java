package guidedsearch;

import java.util.ArrayList;
import java.util.Properties;

public class DocSearchResults {

    public DocSearchResults() {
        this.docSearchResults = new ArrayList<SearchResult>();
    }

    ArrayList<SearchResult> docSearchResults;
    int totalRows;

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public ArrayList<SearchResult> getDocSearchResults() {
        return docSearchResults;
    }

    public void setDocSearchResults(ArrayList<SearchResult> docSearchResults) {
        this.docSearchResults = docSearchResults;
    }

}
