package com.kokabi.p.azmonbaz.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;
import com.kokabi.p.azmonbaz.Adapters.CoursesRVAdapter;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestFragment extends Fragment {

    Context context;

    CoordinatorLayout mainContent;
    RecyclerView coursesRecycleView;

    /*Fragment Values*/
    CoursesRVAdapter rvAdapter;

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

        rvAdapter = new CoursesRVAdapter(pageMaker());
        coursesRecycleView.setAdapter(rvAdapter);

        coursesRecycleView.addOnItemTouchListener(new CoursesRVAdapter(context, new CoursesRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), CourseQuestionsActivity.class));
            }
        }));

        return v;
    }

    private ArrayList<CategoryObj> pageMaker() {
        ArrayList<CategoryObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.categories)).getJSONArray("categories");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);

                int idCat = event.getInt("idCat");
                String catName = event.getString("catName");
                String backColor = event.getString("backColor");
                String textColor = event.getString("textColor");
                int backImage = event.getInt("backImage");
                String resIcon = event.getString("resIcon");
                int categoryOrder = event.getInt("categoryOrder");
                int idParent = event.getInt("idParent");

                result.add(new CategoryObj(idCat, catName, backColor, textColor, backImage, resIcon, categoryOrder, idParent));
            }

        } catch (JSONException e) {
            Log.e(TestFragment.class.getName(), e.getMessage());
        }
        return result;
    }

    private void findViews(View v) {
        coursesRecycleView = (RecyclerView) v.findViewById(R.id.coursesRecycleView);
    }

}
