package guidedsearch;


import java.util.ArrayList;

public class SearchResults {
    public static final String DEFAULT_SEARCH_ORDER = "Desc";
    // CONSTANTS
    public static String SORT_ORDER = "SortOrder";
    public static String QUERY_TEXT = "QueryText";
    public static String RESULT_COUNT = "ResultCount";
    public static String SORT_FIELD = "SortField";
    public static String PAGE_NUMBER = "PageNumber";
    public static String START_ROW = "StartRow";
    public static String END_ROW = "EndRow";
    public static int defaultPageCount = 
        20; //TODO: This could be placed into the web.xml file

    // FIELDS
    ArrayList<SearchResult> searchResults;
    int totalRows;
    int startRow;
    int endRow;
    int resultCount;
    String queryText;
    int pageNumber;
    String sortOrder;
    String sortField;
    int numPages;


    public SearchResults() {
        searchResults = new ArrayList<SearchResult>();
    }

    public ArrayList<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }


}
