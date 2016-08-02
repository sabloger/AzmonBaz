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

import com.google.gson.Gson;
import com.kokabi.p.azmonbaz.Adapters.TestRVAdapter;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

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
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().unregister(this);
        idFavoredTests.clear();
        testRVAdapter.clearTest();
    }

    private void findViews(View v) {
        favoriteTestRV = (RecyclerView) v.findViewById(R.id.favoriteTestRV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);
    }

    public void onEvent(GeneralMSB event) {
        switch (event.getMessage()) {
            case "isEmpty":
                favoriteTestRV.setVisibility(View.GONE);
                noItem_ly.setVisibility(View.VISIBLE);
                break;
        }
    }

    private ArrayList<TestsTitleObj> pageMaker() {
        ArrayList<TestsTitleObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.tests_title)).getJSONArray("test_titles");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);
                result.add(new Gson().fromJson(event.toString(), TestsTitleObj.class));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

}
