package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

public class TreeLVAdapter extends BaseAdapter {

    Context context;
    ArrayList<CategoryObj> childList = new ArrayList<>();
    ViewHolder holder;
    String breadCrumb;

    public TreeLVAdapter(Context context, ArrayList<CategoryObj> dataInput,String breadCrumb) {
        this.context = context;
        childList = dataInput;
        this.breadCrumb = breadCrumb;
    }

    @Override
    public int getCount() {
        return childList.size();
    }

    @Override
    public CategoryObj getItem(int position) {
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tree, null);
            holder = new ViewHolder();

            holder.mainContent = (RelativeLayout) convertView.findViewById(R.id.mainContent);

            holder.breadCrumb_tv = (TextView) convertView.findViewById(R.id.breadCrumb_tv);
            holder.treeTitle_tv = (TextView) convertView.findViewById(R.id.treeTitle_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.treeTitle_tv.setText(childList.get(position).getCatName());
        holder.breadCrumb_tv.setText(breadCrumb);

        return convertView;
    }

    private class ViewHolder {
        RelativeLayout mainContent;
        TextView breadCrumb_tv,treeTitle_tv;
    }
}
