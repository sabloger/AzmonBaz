package com.kokabi.p.azmonbaz.Help;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.kokabi.p.azmonbaz.Components.CButton;
import com.kokabi.p.azmonbaz.R;


/**
 * Created by P.kokabi on 8/6/2016.
 */
public abstract class DeleteDialog extends Dialog implements View.OnClickListener {

    private CButton confirm_btn, cancel_btn;

    public DeleteDialog() {
        super(AppController.getCurrentActivity());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_confirmation);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViews();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_btn:
                onConfirm();
                dismiss();
                break;
            case R.id.cancel_btn:
                dismiss();
                break;
        }
    }

    private void findViews() {
        confirm_btn = (CButton) findViewById(R.id.confirm_btn);
        cancel_btn = (CButton) findViewById(R.id.cancel_btn);

        onClickListeners();
    }

    private void onClickListeners() {
        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
    }

    public abstract void onConfirm();
}
