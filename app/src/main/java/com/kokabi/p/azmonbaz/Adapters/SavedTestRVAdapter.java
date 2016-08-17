package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
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
    ArrayList<TestsTitleObj> savedList = new ArrayList<>();
    String decimal = "%02d : %02d";

    public SavedTestRVAdapter() {
    }

    public SavedTestRVAdapter(ArrayList<TestsTitleObj> dataInput) {
        savedList = dataInput;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView breadCrumb_tv, testTitle_tv, testTimeElapsed_tv, negativePoint_tv;
        AppCompatImageButton resumeTest_imgbtn, deleteSaved_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            breadCrumb_tv = (TextView) itemView.findViewById(R.id.breadCrumb_tv);
            testTitle_tv = (TextView) itemView.findViewById(R.id.testTitle_tv);
            testTimeElapsed_tv = (TextView) itemView.findViewById(R.id.testTimeElapsed_tv);
            negativePoint_tv = (TextView) itemView.findViewById(R.id.negativePoint_tv);

            resumeTest_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.resumeTest_imgbtn);
            deleteSaved_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.deleteSaved_imgbtn);

            deleteSaved_imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new GeneralMSB("isDelete", savedList.get(getAdapterPosition()).getIdTest()));
                }
            });

            resumeTest_imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, CourseQuestionsActivity.class)
                            .putExtra("idTest", savedList.get(getAdapterPosition()).getIdTest())
                            .putExtra("time", savedList.get(getAdapterPosition()).getTime())
                            .putExtra("testName", savedList.get(getAdapterPosition()).getTestName())
                            .putExtra("hasNegativePoint", savedList.get(getAdapterPosition()).isHasNegativePoint())
                            .putExtra("isResumeTest", true)
                            .putExtra("initTime", savedList.get(getAdapterPosition()).getInitTime())
                            .putExtra("breadCrumb", savedList.get(getAdapterPosition()).getBreadCrumb()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_test, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TestsTitleObj testsTitleObj = savedList.get(position);

        holder.breadCrumb_tv.setText(testsTitleObj.getBreadCrumb());
        holder.testTitle_tv.setText(testsTitleObj.getTestName());
        holder.testTimeElapsed_tv.setText(String.format(decimal,
                TimeUnit.MILLISECONDS.toSeconds(testsTitleObj.getTime() * 1000) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(testsTitleObj.getTime() * 1000)),
                TimeUnit.MILLISECONDS.toMinutes(testsTitleObj.getTime() * 1000)));

        if (testsTitleObj.isHasNegativePoint()) {
            holder.negativePoint_tv.setVisibility(View.VISIBLE);
        } else {
            holder.negativePoint_tv.setVisibility(View.GONE);
        }
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
    public void updateSavedTest(int position) {
        savedList.remove(position);
        notifyDataSetChanged();
        if (savedList.size() == 0) {
            EventBus.getDefault().post(new GeneralMSB("isEmpty"));
        }
    }

    public int getPosition(int id) {
        for (int position = 0; position < savedList.size(); position++)
            if (savedList.get(position).getIdTest() == id)
                return position;
        return 0;
    }

    public int getSize() {
        return savedList.size();
    }

    public void addMoreData(ArrayList<TestsTitleObj> myDataSet) {
        savedList.addAll(myDataSet);
        notifyDataSetChanged();
    }
}
