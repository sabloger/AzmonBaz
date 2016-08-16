package com.kokabi.p.azmonbaz.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kokabi.p.azmonbaz.Adapters.FavoredQuestionLVAdapter;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Components.DialogGeneral;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.R;

import de.greenrobot.event.EventBus;

public class FavoredQuestionFragment extends Fragment {

    Context context;
    DataBase db;

    ListView favoredQuestionLV;
    LinearLayout noItem_ly;

    /*Fragment Values*/
    FavoredQuestionLVAdapter favoredAdapter;
    private boolean hasData = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favored_question, container, false);

        context = container.getContext();
        EventBus.getDefault().register(this);
        AppController.setCurrentContext(context);
        db = new DataBase();

        findViews(v);

        if (db.selectAllFavoredQuestion().size() > 0) {
            hasData = true;
            favoredAdapter = new FavoredQuestionLVAdapter(context, db.selectAllFavoredQuestion());
            favoredQuestionLV.setAdapter(favoredAdapter);
        } else {
            hasData = false;
            favoredQuestionLV.setVisibility(View.GONE);
            noItem_ly.setVisibility(View.VISIBLE);
        }

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (hasData) {
            favoredAdapter.clearList();
        }
    }

    private void findViews(View v) {
        favoredQuestionLV = (ListView) v.findViewById(R.id.favoredQuestionLV);

        noItem_ly = (LinearLayout) v.findViewById(R.id.noItem_ly);
    }

    public void onEvent(final GeneralMSB event) {
        switch (event.getMessage()) {
            case "isEmpty":
                favoredQuestionLV.setVisibility(View.GONE);
                noItem_ly.setVisibility(View.VISIBLE);
                break;
            case "isDelete":
                new DialogGeneral(getString(R.string.titleDelete), getString(R.string.confirm), getString(R.string.cancel)) {
                    @Override
                    public void onConfirm() {
                        db.favoredQuestionDelete(event.getId(), event.getBreadCrumb(), event.getTestName());
                        favoredAdapter.updateList(favoredAdapter.getPosition(event.getId()));
                    }
                }.show();
                break;
        }
    }

}
