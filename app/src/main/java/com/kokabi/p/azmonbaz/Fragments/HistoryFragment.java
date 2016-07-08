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
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Adapters.TestRVAdapter;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Objects.HistoryObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView historyRV;
    TextView test;

    /*Activity Values*/
    TestRVAdapter testRVAdapter;
    ArrayList<HistoryObj> pageHistory = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_history, container, false);

        context = container.getContext();
        db = new DataBase(context);
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        historyRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        historyRV.setHasFixedSize(true);

        if (db.selectAllHistory().size() > 0) {

            pageHistory.addAll(db.selectAllHistory());

            test.setText(pageHistory.get(0).getTestName() + " , " + pageHistory.get(0).getTestPercentage() + " , " + pageHistory.get(0).getTestTime());

        } else {
            noItem_ly.setVisibility(View.VISIBLE);
            historyRV.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        testRVAdapter.clearTest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        testRVAdapter.clearTest();
    }

    private void findViews(View v) {
        historyRV = (RecyclerView) v.findViewById(R.id.historyRV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);

        test = (TextView) v.findViewById(R.id.test);
    }

}
