package reachingimmortality.com.curriculum.adapters.items;

/**
 * Created by ReachingIm on 7.10.2015..
 */
public class SectionItemWithIcon implements SectionItems {

    int icon;
    String title;
    String subtitle;

    public SectionItemWithIcon(int icon, String title, String firstSubtitle) {

        this.icon = icon;
        this.title = title;
        this.subtitle = firstSubtitle;
    }

    public int getIcon() {
        return icon;
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
        return true;
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