package com.kokabi.p.azmonbaz.Components;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by P.kokabi on 7/13/2016.
 */
public abstract class EndlessRecyclerList extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerList.class.getSimpleName();

    private static int previousTotal = 0; // The total number of items in the dataset after the last load
    static boolean loading = true; // True if we are still waiting for the last set of data to load.
    int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private static int current_page = 0;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerList(LinearLayoutManager linearLayoutManager) {
        mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        /*Pagination Conditions*/
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public static void resetPreviousTotal() {
        previousTotal = 10;
        current_page = 1;
        loading = false;
    }

    public abstract void onLoadMore(int current_page);
}