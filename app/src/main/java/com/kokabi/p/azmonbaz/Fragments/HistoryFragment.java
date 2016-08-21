package com.kokabi.p.azmonbaz.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kokabi.p.azmonbaz.Adapters.HistoryRVAdapter;
import com.kokabi.p.azmonbaz.Components.DialogGeneral;
import com.kokabi.p.azmonbaz.Components.EndlessRecyclerList;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.R;

import de.greenrobot.event.EventBus;

public class HistoryFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView historyRV;

    /*Activity Values*/
    HistoryRVAdapter historyRVAdapter = new HistoryRVAdapter();
    LinearLayoutManager linearLayoutManager;
    boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_history, container, false);

        context = container.getContext();
        EventBus.getDefault().register(this);
        AppController.setCurrentContext(context);
        db = new DataBase();
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(context);
        historyRV.setLayoutManager(linearLayoutManager);
        // in content do not change the layout size of the RecyclerView
        historyRV.setHasFixedSize(true);

        if (db.selectAllHistory(0).size() > 0) {
            historyRVAdapter = new HistoryRVAdapter(db.selectAllHistory(0));
            historyRV.setAdapter(historyRVAdapter);
            loadMore();
        } else {
            historyRV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        }
        isFirstTime = false;
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppController.setCurrentContext(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        historyRVAdapter.clearHistory();
    }

    private void findViews(View v) {
        historyRV = (RecyclerView) v.findViewById(R.id.historyRV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);
    }

    public void onEvent(final GeneralMSB event) {
        switch (event.getMessage()) {
            case "isEmpty":
                historyRV.setVisibility(View.GONE);
                noItem_ly.setVisibility(View.VISIBLE);
                break;
            case "isDelete":
                new DialogGeneral(getString(R.string.titleDelete), getString(R.string.confirm), getString(R.string.cancel)) {
                    @Override
                    public void onConfirm() {
                        db.historyDelete(event.getId());
                        historyRVAdapter.updateHistory(historyRVAdapter.getPosition(event.getId()));
                    }
                }.show();
                break;
        }
    }

    private void loadMore() {
        historyRV.addOnScrollListener(new EndlessRecyclerList(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isFirstTime) {
                    historyRVAdapter.addMoreData(db.selectAllHistory(current_page));
                }
            }
        });
    }
}
