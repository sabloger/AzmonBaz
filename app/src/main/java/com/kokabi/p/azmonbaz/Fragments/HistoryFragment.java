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
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Components.DialogDelete;
import com.kokabi.p.azmonbaz.R;

import java.util.Collections;

import de.greenrobot.event.EventBus;

public class HistoryFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView historyRV;

    /*Activity Values*/
    HistoryRVAdapter historyRVAdapter = new HistoryRVAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_history, container, false);

        context = container.getContext();
        EventBus.getDefault().register(this);
        db = new DataBase();
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        historyRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        historyRV.setHasFixedSize(true);

        if (db.selectAllHistory().size() > 0) {
            Collections.sort(db.selectAllHistory());
            historyRVAdapter = new HistoryRVAdapter(db.selectAllHistory());
            historyRV.setAdapter(historyRVAdapter);
        } else {
            historyRV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        }
        return v;
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
                new DialogDelete(context) {
                    @Override
                    public void onConfirm() {
                        db.historyDelete(event.getId());
                        historyRVAdapter.updateHistory(historyRVAdapter.getPosition(event.getId()));
                    }
                }.show();
                break;
        }
    }

}
