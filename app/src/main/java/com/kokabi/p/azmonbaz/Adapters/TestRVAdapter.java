package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Objects.TestsObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class TestRVAdapter extends RecyclerView.Adapter<TestRVAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    Context context;
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    ArrayList<TestsObj> testList = new ArrayList<>();
    DataBase db;

    public TestRVAdapter(ArrayList<TestsObj> dataInput) {
        testList = dataInput;
    }

    public TestRVAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView testTitle_tv, testTime_tv, testDesc_tv, testCount_tv, negativePoint_tv;
        AppCompatImageButton addToFavorite_imgbtn;
        Button beginTest_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            testTitle_tv = (TextView) itemView.findViewById(R.id.testTitle_tv);
            testTime_tv = (TextView) itemView.findViewById(R.id.testTime_tv);
            testDesc_tv = (TextView) itemView.findViewById(R.id.testDesc_tv);
            testCount_tv = (TextView) itemView.findViewById(R.id.testCount_tv);
            negativePoint_tv = (TextView) itemView.findViewById(R.id.negativePoint_tv);

            addToFavorite_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.addToFavorite_imgbtn);

            beginTest_btn = (Button) itemView.findViewById(R.id.beginTest_btn);

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
        final TestsObj testsObj = testList.get(position);

        holder.testTitle_tv.setText(testsObj.getTestName());
        holder.testTime_tv.setText(String.valueOf(((float) testsObj.getTime() / 60) + " دقیقه"));
        holder.testDesc_tv.setText(testsObj.getDescription());
        holder.testCount_tv.setText(String.valueOf(testsObj.getQuestionCount() + " سوال"));

        if (testsObj.isHasNegativePoint()) {
            holder.negativePoint_tv.setVisibility(View.VISIBLE);
        }

        if (db.isFavored(testsObj.getIdTest())) {
            holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_favorite);
        }

        holder.addToFavorite_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.isFavored(testsObj.getIdTest())) {
                    holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_favorite_outline);
                    db.favoriteTestDelete(testsObj.getIdTest());
                } else {
                    holder.addToFavorite_imgbtn.setImageResource(R.drawable.ic_favorite);
                    db.favoriteTestInsert(testsObj);
                }
                db.selectAllFavorites();
            }
        });

        holder.beginTest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CourseQuestionsActivity.class)
                        .putExtra("idTest", testsObj.getIdTest())
                        .putExtra("time", testsObj.getTime())
                        .putExtra("testName", testsObj.getTestName())
                        .putExtra("hasNegativePoint", testsObj.isHasNegativePoint()));
            }
        });
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

    /*Click Listener Methods*/
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
