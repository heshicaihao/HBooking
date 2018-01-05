package com.nettactic.hotelbooking.dialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.nettactic.hotelbooking.R;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:自定义对话框
 */
public class FrameProgressDialog extends ProgressDialog {

    public FrameProgressDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_frame_progress);
    }

}
