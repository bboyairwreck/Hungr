package edu.washington.echee.swipeviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import edu.washington.echee.swipeviews.R;

/**
 * Created by Steven on 2/16/15.
 */
public class NavDrawerListAdapter extends ArrayAdapter<String> {
    Context mContext;

    public NavDrawerListAdapter(Context context, int resource, List<String> titles) {
        super(context, resource, titles);
        mContext = context;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        if (contextView == null) {
            contextView = LayoutInflater.from(mContext).inflate(R.layout.nav_drawer_list_layout, parent, false);
        }

        TextView topicTitle = (TextView) contextView.findViewById(R.id.tvNavName);
        topicTitle.setText(getItem(position));

        //Get some layout here
        switch(position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }



        return contextView;
    }
}
