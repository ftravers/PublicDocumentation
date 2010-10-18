package guidedsearch.resource;

import java.util.ListResourceBundle;

public class GuidedSearchBundle extends ListResourceBundle {
    public static final String PORTLETTITLE_VALUE1 = 
        "javax.portlet.preference.value.portletTitle.value1";
    public static final String PORTLET_INFO_SHORT_TITLE = 
        "javax.portlet.short-title";
    public static final String OK_LABEL = "oklabel";
    public static final String PORTLET_INFO_TITLE = "javax.portlet.title";
    public static final String APPLY_LABEL = "applylabel";
    public static final String PORTLETTITLE = 
        "javax.portlet.preference.name.portletTitle";
    private static final Object[][] sContents = 
    { { PORTLETTITLE_VALUE1, "Search" }, 
      { PORTLET_INFO_SHORT_TITLE, "GuidedSearch" }, { OK_LABEL, "OK" }, 
      { PORTLET_INFO_TITLE, "GuidedSearch" }, { APPLY_LABEL, "Apply" }, 
      { PORTLETTITLE, "Portlet Title" }, };

    public Object[][] getContents() {
        return sContents;
    }
}
