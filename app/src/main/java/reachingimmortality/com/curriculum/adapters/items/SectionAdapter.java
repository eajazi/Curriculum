package reachingimmortality.com.curriculum.adapters.items;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import reachingimmortality.com.curriculum.R;


public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private ArrayList<SectionItems> items;
    private Context context;

    private static final int SECTION_CONTENT_NO_ICON = 1;
    private static final int SECTION_HEADER = 2;
    private static final int SECTION_END = 3;
    private static final int SECTION_CONTENT_WITH_ICON = 4;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public SectionAdapter(Context context, ArrayList<SectionItems> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        switch (viewType) {

            case SECTION_CONTENT_NO_ICON:
                View viewNoIcon = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_item_section_no_icon, parent, false);

                return new ViewHolder(viewNoIcon);

            case SECTION_CONTENT_WITH_ICON:
                View viewWithIcon = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_item_section_icon_two_lines, parent, false);
                return new ViewHolder(viewWithIcon);

            case SECTION_HEADER:
                View viewHeader = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_item_section_header, parent, false);

                return new ViewHolder(viewHeader);

            case SECTION_END:

        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final SectionItems i = items.get(position);

        switch(holder.getItemViewType()){

            case SECTION_HEADER:

                TextView tvHeaderTitle = (TextView) holder.view.findViewById(R.id.tvSectionHeaderTitle);

                if(i != null){
                    if(i.isHeader()){
                        SectionHeaderItem si = (SectionHeaderItem) i;

                        tvHeaderTitle.setText(si.getHeaderTitle());
                    }
                }
                break;

            case SECTION_CONTENT_NO_ICON:

                TextView titleNoIcon = (TextView) holder.view.findViewById(R.id.sectionNoIconTitle);
                TextView subtitleNoIcon = (TextView) holder.view.findViewById(R.id.sectionNoIconSubtitle);

                if(i != null){
                    if(i.isSectionNoIcon()){
                        SectionItemsNoIcon sectionProfileItem = (SectionItemsNoIcon) i;

                        titleNoIcon.setText(sectionProfileItem.getTitle());
                        subtitleNoIcon.setText(sectionProfileItem.getSubtitle());
                    }
                }
                break;

            case SECTION_CONTENT_WITH_ICON:

                final ImageView icon = (ImageView) holder.view.findViewById(R.id.sectionTwoLinesIcon);
                TextView titleWithIcon = (TextView) holder.view.findViewById(R.id.sectionTwoLinesTitle);
                TextView subtitleWithIcon = (TextView) holder.view.findViewById(R.id.sectionTwoLinesSubtitle);
                if(i != null){
                    if(i.isSectionWithIcon()){
                        SectionItemWithIcon sectionProfileItem = (SectionItemWithIcon) i;

                        icon.setImageResource(sectionProfileItem.getIcon());
                        titleWithIcon.setText(sectionProfileItem.getTitle());
                        subtitleWithIcon.setText(sectionProfileItem.getSubtitle());
                    }
                }
                break;


            case SECTION_END:
                if(i != null){
                    if(i.isSectionEnd()){

                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType ( int position ) {
        int viewType = 0;

        if ( items.get ( position ).isHeader()) {
            viewType = SECTION_HEADER;
        } else if( items.get ( position ).isSectionWithIcon()){
            viewType = SECTION_CONTENT_WITH_ICON;
        } else if( items.get ( position ).isSectionNoIcon()){
            viewType = SECTION_CONTENT_NO_ICON;
        } else if( items.get ( position ).isSectionEnd()){
            viewType = SECTION_END;
        }
        return viewType;
    }

}