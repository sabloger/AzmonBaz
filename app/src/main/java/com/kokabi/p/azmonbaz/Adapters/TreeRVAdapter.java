package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class TreeRVAdapter extends RecyclerView.Adapter<TreeRVAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    Context context;
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    ArrayList<CategoryObj> childList = new ArrayList<>();
    String breadCrumb;

    public TreeRVAdapter(Context context, ArrayList<CategoryObj> dataInput, String breadCrumb) {
        this.context = context;
        childList = dataInput;
        this.breadCrumb = breadCrumb;
    }

    public TreeRVAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mainContent;
        public TextView breadCrumb_tv, treeTitle_tv, count;
        public ProgressView progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            mainContent = (CardView) itemView.findViewById(R.id.mainContent);

            breadCrumb_tv = (TextView) itemView.findViewById(R.id.breadCrumb_tv);
            treeTitle_tv = (TextView) itemView.findViewById(R.id.treeTitle_tv);
            count = (TextView) itemView.findViewById(R.id.count);

            progressBar = (ProgressView) itemView.findViewById(R.id.progressBar);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tree, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.treeTitle_tv.setText(childList.get(position).getCatName());
        holder.breadCrumb_tv.setText(breadCrumb);
        holder.count.setText(String.valueOf(Constants.getChildSize(childList.get(position).getIdCat()).getDone()
                + " / " + Constants.getChildSize(childList.get(position).getIdCat()).getSize()));

        float progressValue = (float) Constants.getChildSize(childList.get(position).getIdCat()).getDone()
                / (Constants.getChildSize(childList.get(position).getIdCat()).getSize());

        holder.progressBar.setProgress(progressValue);
        Constants.freeMemory();
    }

    @Override
    public int getItemCount() {
        return childList.size();
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
