package reachingimmortality.com.curriculum.adapters.items;

/**
 * Created by ReachingIm on 8.10.2015..
 */
public class SectionHeaderItem implements SectionItems {
    String headerTitle;

    public SectionHeaderItem(String headerTitle){
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public boolean isSectionWithIcon() {
        return false;
    }

    @Override
    public boolean isSectionNoIcon() {
        return false;
    }

    @Override
    public boolean isSectionEnd() {
        return false;
    }
}
