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
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;

import java.util.Collections;

import de.greenrobot.event.EventBus;

public class SavedTestFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView savedTestRV;

    /*Activity Values*/
    SavedTestRVAdapter historyRVAdapter = new SavedTestRVAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_saved_test, container, false);

        context = container.getContext();
        EventBus.getDefault().register(this);
        db = new DataBase();
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        savedTestRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        savedTestRV.setHasFixedSize(true);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
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

    public void onEvent(GeneralMSB event) {
        switch (event.getMessage()) {
            case "isEmpty":
                savedTestRV.setVisibility(View.GONE);
                noItem_ly.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadData() {
        if (db.selectAllSavedTest().size() > 0) {
            Collections.sort(db.selectAllSavedTest());
            historyRVAdapter = new SavedTestRVAdapter(db.selectAllSavedTest());
            savedTestRV.setAdapter(historyRVAdapter);
        } else {
            savedTestRV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        }
    }

}
