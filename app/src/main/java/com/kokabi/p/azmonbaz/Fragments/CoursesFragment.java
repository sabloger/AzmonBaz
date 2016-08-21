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

import com.kokabi.p.azmonbaz.Activities.TestActivity;
import com.kokabi.p.azmonbaz.Activities.TreeActivity;
import com.kokabi.p.azmonbaz.Adapters.CoursesRVAdapter;
import com.kokabi.p.azmonbaz.Components.EndlessRecyclerList;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;
import java.util.Collections;

public class CoursesFragment extends Fragment {

    Context context;

    CoordinatorLayout mainContent;
    RecyclerView coursesRecycleView;

    /*Fragment Values*/
    LinearLayoutManager linearLayoutManager;
    CoursesRVAdapter rvAdapter;
    ArrayList<CategoryObj> rootCategories = new ArrayList<>();
    private boolean isIntent = false, isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_courses, container, false);

        context = container.getContext();
        AppController.setCurrentContext(context);
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(context);
        coursesRecycleView.setLayoutManager(linearLayoutManager);
        // in content do not change the layout size of the RecyclerView
        coursesRecycleView.setHasFixedSize(true);

        rootCategories.clear();

        loadData(0);
        loadMore();
        isFirstTime = false;
        coursesRecycleView.addOnItemTouchListener(new CoursesRVAdapter(context, new CoursesRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isIntent = false;
                for (int i = 0; i < Constants.totalCategories.size(); i++) {
                    if (rootCategories.get(position).getIdCat() == Constants.totalCategories.get(i).getIdParent()) {
                        isIntent = true;
                    }
                }
                if (isIntent) {
                    startActivity(new Intent(getActivity(), TreeActivity.class)
                            .putExtra("idCat", rootCategories.get(position).getIdCat())
                            .putExtra("breadCrumb", rootCategories.get(position).getCatName()));
                } else {
                    startActivity(new Intent(getActivity(), TestActivity.class)
                            .putExtra("idCat", rootCategories.get(position).getIdCat())
                            .putExtra("breadCrumb", rootCategories.get(position).getCatName()));
                }
            }
        }));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppController.setCurrentContext(context);
        isFirstTime = true;
    }

    private void findViews(View v) {
        coursesRecycleView = (RecyclerView) v.findViewById(R.id.coursesRecycleView);
    }

    private void loadData(int pageIndex) {
        if (Constants.totalCategories.size() > 10) {
            for (int i = pageIndex; i < pageIndex + 10; i++) {
                if (Constants.totalCategories.get(i).getIdParent() == 0) {
                    rootCategories.add(Constants.totalCategories.get(i));
                }
            }
        } else {
            for (int i = 0; i < Constants.totalCategories.size(); i++) {
                if (Constants.totalCategories.get(i).getIdParent() == 0) {
                    rootCategories.add(Constants.totalCategories.get(i));
                }
            }
        }

        if (isFirstTime) {
            Collections.sort(rootCategories);
            rvAdapter = new CoursesRVAdapter(rootCategories);
            coursesRecycleView.setAdapter(rvAdapter);
        } else {
            Collections.sort(rootCategories);
            rvAdapter.updateCourses();
        }
    }

    private void loadMore() {
        coursesRecycleView.addOnScrollListener(new EndlessRecyclerList(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isFirstTime) {
                    loadData(current_page * 10);
                }
            }
        });
    }

}
