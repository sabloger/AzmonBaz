package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class CoursesRVAdapter extends RecyclerView.Adapter<CoursesRVAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    Context context;
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    ArrayList<String> coursesTitle = new ArrayList<>();
    ArrayList<Integer> coursesIcon = new ArrayList<>();

    public CoursesRVAdapter(ArrayList<String> dataInput, ArrayList<Integer> iconInput) {
        coursesTitle = dataInput;
        coursesIcon = iconInput;
    }

    public CoursesRVAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout course_fr;
        public TextView courseTitle_tv;
        public AppCompatImageView courseIcon_imgv;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            course_fr = (FrameLayout) itemView.findViewById(R.id.course_fr);

            courseTitle_tv = (TextView) itemView.findViewById(R.id.courseTitle_tv);

            courseIcon_imgv = (AppCompatImageView) itemView.findViewById(R.id.courseIcon_imgv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.course_fr.setBackgroundResource(R.drawable.bg_gradiant_math);
                break;
            case 1:
                holder.course_fr.setBackgroundResource(R.drawable.bg_gradiant_physics);
                break;
            case 2:
                holder.course_fr.setBackgroundResource(R.drawable.bg_gradiant_chemistry);
                holder.courseTitle_tv.setTextColor(Color.parseColor("#777777"));
                break;
        }
        holder.courseTitle_tv.setText(coursesTitle.get(position));
        holder.courseIcon_imgv.setImageResource(coursesIcon.get(position));
    }

    @Override
    public int getItemCount() {
        return coursesTitle.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
