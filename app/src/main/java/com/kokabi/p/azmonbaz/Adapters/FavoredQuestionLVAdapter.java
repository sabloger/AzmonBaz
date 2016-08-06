package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Activities.FavoredQuestionDetailActivity;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class FavoredQuestionLVAdapter extends BaseAdapter {

    Context context;
    ArrayList<TestObj> favoredList = new ArrayList<>();
    ViewHolder holder;

    public FavoredQuestionLVAdapter(Context context, ArrayList<TestObj> dataInput) {
        this.context = context;
        favoredList = dataInput;
    }

    @Override
    public int getCount() {
        return favoredList.size();
    }

    @Override
    public TestObj getItem(int position) {
        return favoredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return favoredList.get(position).getIdQuestion();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tree, null);
            holder = new ViewHolder();

            holder.treeTitle_tv = (TextView) convertView.findViewById(R.id.treeTitle_tv);

            holder.delete_imgbtn = (AppCompatImageButton) convertView.findViewById(R.id.delete_imgbtn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.delete_imgbtn.setVisibility(View.VISIBLE);

        final TestObj testItem = favoredList.get(position);

        holder.treeTitle_tv.setText(String.valueOf(testItem.getTestName() + " سوال " + testItem.getIdQuestion()));

        holder.delete_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new GeneralMSB("isDelete", testItem.getIdQuestion()));
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, FavoredQuestionDetailActivity.class)
                        .putExtra("idQuestion", testItem.getIdQuestion())
                        .putExtra("testName", testItem.getTestName()));
            }
        });

        return convertView;
    }

    public void clearList() {
        favoredList.clear();
        notifyDataSetChanged();
    }

    public void updateList(int position) {
        favoredList.remove(position);
        notifyDataSetChanged();
        if (favoredList.size() == 0) {
            EventBus.getDefault().post(new GeneralMSB("isEmpty"));
        }
    }

    public int getPosition(int id) {
        for (int position = 0; position < favoredList.size(); position++)
            if (favoredList.get(position).getIdQuestion() == id)
                return position;
        return 0;
    }

    private class ViewHolder {
        TextView treeTitle_tv;
        AppCompatImageButton delete_imgbtn;
    }
}
