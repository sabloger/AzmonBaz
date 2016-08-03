package com.kokabi.p.azmonbaz.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Activities.FavoredQuestionDetailActivity;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class FavoredQuestionLVAdapter extends BaseAdapter {

    Context context;
    Dialog dialogDeleteItem;
    DataBase db;
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

            /*Creating DialogDeleteItem===========================================================*/
            dialogDeleteItem = new Dialog(context);
            dialogDeleteItem.setCancelable(false);
            dialogDeleteItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogDeleteItem.setContentView(R.layout.dialog_delete_confirmation);
            /*====================================================================================*/

            db = new DataBase();

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
                showDialogDeleteItem(testItem, position);
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

    /*DeleteDialog Method*/
    private void showDialogDeleteItem(final TestObj testItem, final int position) {
        Button confirm_btn = (Button) dialogDeleteItem.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) dialogDeleteItem.findViewById(R.id.cancel_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.favoredQuestionDelete(testItem.getIdQuestion());
                favoredList.remove(position);
                notifyDataSetChanged();
                if (favoredList.size() == 0) {
                    EventBus.getDefault().post(new GeneralMSB("isEmpty"));
                }
                dialogDeleteItem.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteItem.dismiss();
            }
        });

        dialogDeleteItem.show();
    }

    private class ViewHolder {
        TextView treeTitle_tv;
        AppCompatImageButton delete_imgbtn;
    }
}
