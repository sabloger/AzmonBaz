package com.kokabi.p.azmonbaz.Fragments;

import android.content.Context;
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
import android.widget.LinearLayout;

import com.kokabi.p.azmonbaz.Adapters.TestRVAdapter;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class FavoritesFragment extends Fragment {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    LinearLayout noItem_ly;
    RecyclerView favoriteTestRV;

    /*Activity Values*/
    TestRVAdapter testRVAdapter;
    ArrayList<TestsTitleObj> favoritesTests = new ArrayList<>();
    ArrayList<Integer> idFavoredTests = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        context = container.getContext();
        db = new DataBase(context);
        mainContent = (CoordinatorLayout) v.findViewById(R.id.mainContent);

        findViews(v);

        idFavoredTests.addAll(db.selectAllFavorites());

        // use a linear layout manager
        favoriteTestRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        favoriteTestRV.setHasFixedSize(true);

        for (int i = 0; i < pageMaker().size(); i++) {
            for (int j = 0; j < idFavoredTests.size(); j++) {
                if (pageMaker().get(i).getIdTest() == idFavoredTests.get(j)) {
                    favoritesTests.add(pageMaker().get(i));
                }
            }
        }

        if (favoritesTests.size() == 0) {
            favoriteTestRV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        }

        Collections.sort(favoritesTests);
        testRVAdapter = new TestRVAdapter(favoritesTests, true);
        favoriteTestRV.setAdapter(testRVAdapter);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        idFavoredTests.clear();
        testRVAdapter.clearTest();
    }

    private void findViews(View v) {
        favoriteTestRV = (RecyclerView) v.findViewById(R.id.favoriteTestRV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);
    }

    private ArrayList<TestsTitleObj> pageMaker() {
        ArrayList<TestsTitleObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.tests_title)).getJSONArray("tests_title");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);

                int idCat = event.getInt("idCat");
                int idTest = event.getInt("idTest");
                String testName = event.getString("testName");
                String description = event.getString("description");
                int time = event.getInt("time");
                boolean hasNegativePoint = event.getBoolean("hasNegativePoint");
                String testOrder = event.getString("testOrder");
                int questionCount = event.getInt("questionCount");

                result.add(new TestsTitleObj(idCat, idTest, testName, description, time, hasNegativePoint, testOrder, questionCount));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

}
