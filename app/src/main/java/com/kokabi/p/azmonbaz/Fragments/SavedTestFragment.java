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

import com.kokabi.p.azmonbaz.Adapters.SavedTestRVAdapter;
import com.kokabi.p.azmonbaz.Components.DialogGeneral;
import com.kokabi.p.azmonbaz.Components.EndlessRecyclerList;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;

import de.greenrobot.event.EventBus;

public class SavedTestFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView savedTestRV;

    /*Activity Values*/
    LinearLayoutManager linearLayoutManager;
    SavedTestRVAdapter historyRVAdapter = new SavedTestRVAdapter();
    boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_saved_test, container, false);

        context = container.getContext();
        EventBus.getDefault().register(this);
        AppController.setCurrentContext(context);
        db = new DataBase();
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(context);
        savedTestRV.setLayoutManager(linearLayoutManager);
        // in content do not change the layout size of the RecyclerView
        savedTestRV.setHasFixedSize(true);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppController.setCurrentContext(context);
        loadData(0);
        loadMore();
        isFirstTime = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        historyRVAdapter.clearSavedTest();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Constants.freeMemory();
    }

    private void findViews(View v) {
        savedTestRV = (RecyclerView) v.findViewById(R.id.savedTestRV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);
    }

    public void onEvent(final GeneralMSB event) {
        switch (event.getMessage()) {
            case "isEmpty":
                savedTestRV.setVisibility(View.GONE);
                noItem_ly.setVisibility(View.VISIBLE);
                break;
            case "isDelete":
                new DialogGeneral(getString(R.string.titleDelete), getString(R.string.confirm), getString(R.string.cancel), true) {
                    @Override
                    public void onConfirm() {
                        db.savedTestDelete(event.getId());
                        historyRVAdapter.updateSavedTest(historyRVAdapter.getPosition(event.getId()));
                    }
                }.show();
                break;
        }
    }

    private void loadData(int pageIndex) {
        if (isFirstTime) {
            historyRVAdapter = new SavedTestRVAdapter(db.selectAllSavedTest(pageIndex));
            savedTestRV.setAdapter(historyRVAdapter);
            isFirstTime = false;
        } else {
            historyRVAdapter.addMoreData(db.selectAllSavedTest(pageIndex));
        }
        if (historyRVAdapter.getSize() == 0) {
            savedTestRV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        } else {
            savedTestRV.setVisibility(View.VISIBLE);
            noItem_ly.setVisibility(View.GONE);
        }
    }

    private void loadMore() {
        savedTestRV.addOnScrollListener(new EndlessRecyclerList(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isFirstTime) {
                    loadData(current_page);
                }
            }
        });
    }

}
