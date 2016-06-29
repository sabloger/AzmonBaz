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

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;
import com.kokabi.p.azmonbaz.Adapters.CoursesRVAdapter;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;

public class TestFragment extends Fragment {

    Context context;

    CoordinatorLayout mainContent;
    RecyclerView coursesRecycleView;

    /*Fragment Values*/
    CoursesRVAdapter rvAdapter;
    ArrayList<String> coursesTitle = new ArrayList<>();
    ArrayList<Integer> coursesIcon = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_test, container, false);

        context = container.getContext();
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        // use a linear layout manager
        coursesRecycleView.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        coursesRecycleView.setHasFixedSize(true);

        coursesTitle.add("ریاضی");
        coursesTitle.add("فیزیک");
        coursesTitle.add("شیمی");

        coursesIcon.add(R.drawable.icon_math);
        coursesIcon.add(R.drawable.icon_physics);
        coursesIcon.add(R.drawable.ic_chemistry);

        rvAdapter = new CoursesRVAdapter(coursesTitle, coursesIcon);
        coursesRecycleView.setAdapter(rvAdapter);

        coursesRecycleView.addOnItemTouchListener(new CoursesRVAdapter(context, new CoursesRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), CourseQuestionsActivity.class));
            }
        }));

        return v;
    }

    private void findViews(View v) {
        coursesRecycleView = (RecyclerView) v.findViewById(R.id.coursesRecycleView);
    }

}
