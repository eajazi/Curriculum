package reachingimmortality.com.curriculum.adapters.items;

/**
 * Created by ReachingIm on 9.10.2015..
 */
public class SectionEnd implements SectionItems {
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
        return false;
    }

    @Override
    public boolean isSectionEnd() {
        return true;
    }
}
