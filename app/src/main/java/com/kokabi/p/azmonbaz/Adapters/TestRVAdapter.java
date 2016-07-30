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
public class TestRVAdapter extends RecyclerView.Adapter<TestRVAdapter.ViewHolder> {

    Context context;
    DataBase db;
    ArrayList<TestsTitleObj> testList = new ArrayList<>();
    boolean isFavoredFragment = false;
    String decimal = "%02d : %02d";

    public TestRVAdapter(ArrayList<TestsTitleObj> dataInput, boolean isFavoredFragment) {
        testList = dataInput;
        this.isFavoredFragment = isFavoredFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView testTitle_tv, testTime_tv, testDesc_tv, testCount_tv, negativePoint_tv;
        AppCompatImageButton startTest_imgbtn, addToFavorite_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            testTitle_tv = (TextView) itemView.findViewById(R.id.testTitle_tv);
            testTime_tv = (TextView) itemView.findViewById(R.id.testTime_tv);
            testDesc_tv = (TextView) itemView.findViewById(R.id.testDesc_tv);
            testCount_tv = (TextView) itemView.findViewById(R.id.testCount_tv);
            negativePoint_tv = (TextView) itemView.findViewById(R.id.negativePoint_tv);

            startTest_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.startTest_imgbtn);
            addToFavorite_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.addToFavorite_imgbtn);

            db = new DataBase(context);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TestsTitleObj testsTitleObj = testList.get(position);

        holder.testTitle_tv.setText(testsTitleObj.getTestName());
        holder.testTime_tv.setText(String.format(decimal,
                TimeUnit.MILLISECONDS.toMinutes(testsTitleObj.getTime() * 1000),
                TimeUnit.MILLISECONDS.toSeconds(testsTitleObj.getTime() * 1000) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(testsTitleObj.getTime() * 1000))));
        holder.testDesc_tv.setText(testsTitleObj.getDescription());
        holder.testCount_tv.setText(String.valueOf("( " + testsTitleObj.getQuestionCount() + " سوال )"));

        if (testsTitleObj.isHasNegativePoint()) {
            holder.negativePoint_tv.setVisibility(View.VISIBLE);
        }

        if (db.isTestFavored(testsTitleObj.getIdTest())) {
            holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_bookmark);
            holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_bookmark);
        }

        /*onClickListeners*/
        onClick(holder, position);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*Clear Method*/
    public void clearTest() {
        testList.clear();
        notifyDataSetChanged();
    }

    /*Update Method*/
    public void updateTest() {
        notifyDataSetChanged();
    }

    /*Click Listener Methods*/
    private void onClick(final ViewHolder holder, int position) {
        final TestsTitleObj testsTitleObj = testList.get(position);
        holder.addToFavorite_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.isTestFavored(testsTitleObj.getIdTest())) {
                    holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_bookmark_outline);
                    db.favoriteTestDelete(testsTitleObj.getIdTest());
                    if (isFavoredFragment) {
                        testList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        if (testList.size() == 0) {
                            EventBus.getDefault().post(new GeneralMSB("isEmpty"));
                        }
                    }
                } else {
                    holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_bookmark);
                    db.favoriteTestInsert(testsTitleObj);
                }
                db.selectAllFavorites();
            }
        });

        holder.startTest_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CourseQuestionsActivity.class)
                        .putExtra("idTest", testsTitleObj.getIdTest())
                        .putExtra("time", testsTitleObj.getTime())
                        .putExtra("testName", testsTitleObj.getTestName())
                        .putExtra("hasNegativePoint", testsTitleObj.isHasNegativePoint())
                        .putExtra("isResumeTest", false));
            }
        });
    }
}
