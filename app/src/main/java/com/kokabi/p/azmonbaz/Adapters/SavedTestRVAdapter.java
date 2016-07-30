package com.kokabi.p.azmonbaz.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBussObj.GeneralMSB;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class SavedTestRVAdapter extends RecyclerView.Adapter<SavedTestRVAdapter.ViewHolder> {

    Context context;
    Dialog dialogDeleteItem;
    DataBase db;
    ArrayList<TestsTitleObj> savedList = new ArrayList<>();
    String decimal = "%02d : %02d";

    public SavedTestRVAdapter() {
    }

    public SavedTestRVAdapter(ArrayList<TestsTitleObj> dataInput) {
        savedList = dataInput;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView testTitle_tv, testTimeElapsed_tv, negativePoint_tv;
        AppCompatImageButton resumeTest_imgbtn, deleteSaved_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            testTitle_tv = (TextView) itemView.findViewById(R.id.testTitle_tv);
            testTimeElapsed_tv = (TextView) itemView.findViewById(R.id.testTimeElapsed_tv);
            negativePoint_tv = (TextView) itemView.findViewById(R.id.negativePoint_tv);

            resumeTest_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.resumeTest_imgbtn);
            deleteSaved_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.deleteSaved_imgbtn);

            db = new DataBase(context);
            
            /*Creating DialogDeleteItem===========================================================*/
            dialogDeleteItem = new Dialog(context);
            dialogDeleteItem.setCancelable(false);
            dialogDeleteItem.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogDeleteItem.setContentView(R.layout.dialog_delete_confirmation);
            /*====================================================================================*/
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_test, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TestsTitleObj testsTitleObj = savedList.get(position);

        holder.testTitle_tv.setText(testsTitleObj.getTestName());
        holder.testTimeElapsed_tv.setText(String.format(decimal,
                TimeUnit.MILLISECONDS.toMinutes(testsTitleObj.getTime() * 1000),
                TimeUnit.MILLISECONDS.toSeconds(testsTitleObj.getTime() * 1000) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(testsTitleObj.getTime() * 1000))));

        if (testsTitleObj.isHasNegativePoint()) {
            holder.negativePoint_tv.setVisibility(View.VISIBLE);
        }

        /*onClickListeners*/
        onClick(holder, position);
    }

    @Override
    public int getItemCount() {
        return savedList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*Clear Method*/
    public void clearSavedTest() {
        savedList.clear();
        notifyDataSetChanged();
    }

    /*Update Method*/
    public void updateTest() {
        notifyDataSetChanged();
    }

    /*DeleteDialog Method*/
    private void showDialogDeleteItem(final int id, final int position) {
        Button confirm_btn = (Button) dialogDeleteItem.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) dialogDeleteItem.findViewById(R.id.cancel_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.savedTestDelete(id);
                savedList.remove(position);
                notifyDataSetChanged();
                if (savedList.size() == 0) {
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

    /*Click Listener Methods*/
    private void onClick(final ViewHolder holder, final int position) {
        final TestsTitleObj testsTitleObj = savedList.get(position);
        holder.deleteSaved_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDeleteItem(testsTitleObj.getIdTest(), position);
            }
        });

        holder.resumeTest_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CourseQuestionsActivity.class)
                        .putExtra("idTest", testsTitleObj.getIdTest())
                        .putExtra("time", testsTitleObj.getTime())
                        .putExtra("testName", testsTitleObj.getTestName())
                        .putExtra("hasNegativePoint", testsTitleObj.isHasNegativePoint())
                        .putExtra("isResumeTest", true));
            }
        });
    }
}
