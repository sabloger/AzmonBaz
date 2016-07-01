package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class CoursesRVAdapter extends RecyclerView.Adapter<CoursesRVAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    Context context;
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    ArrayList<CategoryObj> courses = new ArrayList<>();

    public CoursesRVAdapter(ArrayList<CategoryObj> dataInput) {
        courses = dataInput;
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

        public CardView course_cav;
        public FrameLayout course_fr;
        public TextView courseTitle_tv;
        public AppCompatImageView courseIcon_imgv;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            course_cav = (CardView) itemView.findViewById(R.id.course_cav);

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
        holder.course_cav.setCardBackgroundColor(Color.parseColor(courses.get(position).getBackColor()));
        /*get path of saved file to show the backImages*/
        File root = android.os.Environment.getExternalStorageDirectory();
        File imgFile = new File(root.getAbsolutePath() + Constants.appFolder + Constants.catFolder +
                Constants.iconFolder + courses.get(position).getBackImage());
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
            holder.course_fr.setBackground(drawable);
        }
        holder.courseTitle_tv.setText(courses.get(position).getCatName());
        holder.courseTitle_tv.setTextColor(Color.parseColor(courses.get(position).getTextColor()));
        /*set Image Resources*/
        int resIcon = context.getResources().getIdentifier("com.kokabi.p.azmonbaz:drawable/" +
                courses.get(position).getResIcon(), null, null);
        holder.courseIcon_imgv.setImageResource(resIcon);
    }

    @Override
    public int getItemCount() {
        return courses.size();
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
