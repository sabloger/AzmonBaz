package com.kokabi.p.azmonbaz.Components;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.R;

public class CPermissionDeniedDialog extends Dialog implements View.OnClickListener {

    public CPermissionDeniedDialog() {
        super(AppController.getCurrentActivity());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_permission_denied);

        Button ok_btn = (Button) findViewById(R.id.ok_btn);

        ok_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                dismiss();
                break;
        }
    }
}
