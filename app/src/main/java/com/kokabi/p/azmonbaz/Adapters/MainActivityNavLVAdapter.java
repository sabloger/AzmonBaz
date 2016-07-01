package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.kokabi.p.azmonbaz.Objects.MainActivityNavObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

public class MainActivityNavLVAdapter extends BaseAdapter {

    Context context;
    ArrayList<MainActivityNavObj> listMainActivityNavObj = new ArrayList<>();
    ViewHolder holder;

    public MainActivityNavLVAdapter(Context context, ArrayList<MainActivityNavObj> dataInput) {
        this.context = context;
        listMainActivityNavObj = dataInput;
    }

    @Override
    public int getCount() {
        return listMainActivityNavObj.size();
    }

    @Override
    public Object getItem(int position) {
        return listMainActivityNavObj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_nav_list, null);
            holder = new ViewHolder();

            holder.item_ripple = (MaterialRippleLayout) convertView.findViewById(R.id.item_ripple);

            holder.mainContent = (RelativeLayout) convertView.findViewById(R.id.mainContent);
            holder.underline_rly = (RelativeLayout) convertView.findViewById(R.id.underline_rly);

            holder.navTitle = (TextView) convertView.findViewById(R.id.navTitle_tv);

            holder.navIcon = (ImageView) convertView.findViewById(R.id.navIcon_imgv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MainActivityNavObj navItem = listMainActivityNavObj.get(position);

        if (navItem.isLine()) {

            holder.mainContent.setVisibility(View.GONE);
            holder.underline_rly.setVisibility(View.VISIBLE);
            holder.item_ripple.setEnabled(false);
        } else {
            holder.mainContent.setVisibility(View.VISIBLE);
            holder.underline_rly.setVisibility(View.GONE);

            holder.navTitle.setText(navItem.getTitle());
            holder.navIcon.setImageResource(navItem.getResIcon());
        }

        return convertView;
    }

    private class ViewHolder {
        MaterialRippleLayout item_ripple;
        RelativeLayout mainContent, underline_rly;
        TextView navTitle;
        ImageView navIcon;
    }
}
