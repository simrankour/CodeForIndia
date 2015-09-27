package com.ttn.googlehackathon.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ttn.googlehackathon.R;
import com.ttn.googlehackathon.customview.AnimateFirstDisplayListener;
import com.ttn.googlehackathon.models.NavDrawerItem;
import com.ttn.googlehackathon.utility.AppConstant;

import java.util.ArrayList;


public class NavDrawerListAdapter extends BaseAdapter {

    private final DisplayImageOptions options;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private int selectedItem;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(20))
                .build();
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void selectItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.nav_header, null);

            SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstant.PREF_LOGIN_FILE_NAME, Activity.MODE_PRIVATE);
            String userId = sharedPreferences.getString("user_id", "-1");
            String userPic = sharedPreferences.getString("user_pic", "");
            String userName = sharedPreferences.getString("user_name", "");
            if (!userId.equalsIgnoreCase("-1")) {
                ImageLoader.getInstance().displayImage(userPic, (ImageView) convertView.findViewById(R.id.user_pic), options, new AnimateFirstDisplayListener());
                ((TextView) convertView.findViewById(R.id.user_name)).setText(userName);
            }
            return convertView;
        }
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
//        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

//        if (navDrawerItems.get(position).getCounterVisibility()) {
//            txtCount.setText(navDrawerItems.get(position).getCount());
//        } else {
//            txtCount.setVisibility(View.GONE);
//        }

        if (position == selectedItem) {
            txtTitle.setTextColor(context.getResources().getColor(R.color.white));
            txtTitle.setTypeface(Typeface.DEFAULT_BOLD);
            imgIcon.setImageResource(navDrawerItems.get(position).getActiveIcon());
        } else {
            txtTitle.setTextColor(context.getResources().getColor(R.color.black));
            txtTitle.setTypeface(Typeface.DEFAULT_BOLD);
            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        }
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }

}
