package com.kokabi.p.azmonbaz.Fragments;

import android.content.Context;
import android.content.Intent;
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

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;
import com.kokabi.p.azmonbaz.Adapters.FavoritesRVAdapter;
import com.kokabi.p.azmonbaz.Components.DialogGeneral;
import com.kokabi.p.azmonbaz.Components.EndlessRecyclerList;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class FavoritesFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView favoriteTestRV;

    /*Activity Values*/
    FavoritesRVAdapter favRVAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TestsTitleObj> favoritesTests = new ArrayList<>();
    ArrayList<Integer> idFavoredTests = new ArrayList<>();
    ArrayList<String> breadCrumbFavoredTests = new ArrayList<>();
    boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        context = container.getContext();
        EventBus.getDefault().register(this);
        AppController.setCurrentContext(context);
        db = new DataBase();
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(context);
        favoriteTestRV.setLayoutManager(linearLayoutManager);
        // in content do not change the layout size of the RecyclerView
        favoriteTestRV.setHasFixedSize(true);

        loadData(0);
        loadMore();
        isFirstTime = true;

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        favRVAdapter.updateTest();
        AppController.setCurrentContext(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        idFavoredTests.clear();
        favRVAdapter.clearTest();
        breadCrumbFavoredTests.clear();
    }

    private void findViews(View v) {
        favoriteTestRV = (RecyclerView) v.findViewById(R.id.favoriteTestRV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);
    }

    private void loadData(int pageIndex) {
        idFavoredTests.addAll(db.selectPagingIdFavorites(pageIndex));
        breadCrumbFavoredTests.addAll(db.selectAllBreadCrumbFavorites(pageIndex));

        for (int i = 0; i < Constants.totalTestTitles.size(); i++) {
            for (int j = 0; j < idFavoredTests.size(); j++) {
                if (Constants.totalTestTitles.get(i).getIdTest() == idFavoredTests.get(j)) {
                    TestsTitleObj finalTest = Constants.totalTestTitles.get(i);
                    finalTest.setBreadCrumb(breadCrumbFavoredTests.get(j));
                    favoritesTests.add(finalTest);
                }
            }
        }

        if (favoritesTests.size() == 0) {
            favoriteTestRV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        }

        if (isFirstTime) {
            favRVAdapter = new FavoritesRVAdapter(favoritesTests, true);
            favoriteTestRV.setAdapter(favRVAdapter);
            isFirstTime = false;
        } else {
            favRVAdapter.updateTest();
        }
    }

    public void onEvent(final GeneralMSB event) {
        switch (event.getMessage()) {
            case "isEmpty":
                favoriteTestRV.setVisibility(View.GONE);
                noItem_ly.setVisibility(View.VISIBLE);
                break;
            case "isResume":
                new DialogGeneral(context.getResources().getString(R.string.resumeTestTitle)
                        , context.getResources().getString(R.string.startTest)
                        , context.getResources().getString(R.string.cancel)) {
                    @Override
                    public void onConfirm() {
                        context.startActivity(new Intent(context, CourseQuestionsActivity.class)
                                .putExtra("idTest", event.getTestsTitleObj().getIdTest())
                                .putExtra("time", event.getTestsTitleObj().getTime())
                                .putExtra("testName", event.getTestsTitleObj().getTestName())
                                .putExtra("hasNegativePoint", event.getTestsTitleObj().isHasNegativePoint())
                                .putExtra("isResumeTest", false)
                                .putExtra("initTime", event.getTestsTitleObj().getTime())
                                .putExtra("breadCrumb", event.getBreadCrumb()));

                    }
                }.show();
                break;
        }
    }

    private void loadMore() {
        favoriteTestRV.addOnScrollListener(new EndlessRecyclerList(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isFirstTime) {
                    loadData(current_page);
                }
            }
        });
    }

}
