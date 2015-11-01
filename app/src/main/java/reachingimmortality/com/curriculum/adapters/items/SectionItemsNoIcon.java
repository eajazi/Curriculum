package reachingimmortality.com.curriculum.adapters.items;

/**
 * Created by ReachingIm on 9.10.2015..
 */
public class SectionItemsNoIcon implements SectionItems{

    String title;
    String subtitle;

    public SectionItemsNoIcon(String title, String subtitle) {

        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle(){
        return subtitle;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public boolean isSectionWithIcon() {
        return false;
    }

    @Override
    public boolean isSectionNoIcon() {
        return true;
    }

    @Override
    public boolean isSectionEnd() {
        return false;
    }
}
