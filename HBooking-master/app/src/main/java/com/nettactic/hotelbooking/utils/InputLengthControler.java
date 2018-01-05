package com.nettactic.hotelbooking.utils;



import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.common.MyApplication;

public class InputLengthControler {
    private EditText mEditText;
    private TextView mHintTextView;
    private final String hint_remain_count = MyApplication.getContext().getString(R.string.hint_remain_count);
    private final String hint_max_length_msg = MyApplication.getContext().getString(R.string.hint_max_length_msg);;
    private int MAX_LENGTH = 140;
 
    public void config(EditText inputBox, int maxLength, TextView lengthHintView) {
        MAX_LENGTH = maxLength;
        mEditText = inputBox;
        mHintTextView = lengthHintView;
        mEditText.addTextChangedListener(watcher);
        updateLengthHint(MAX_LENGTH);
    }
 
    private TextWatcher watcher = new TextWatcher() {
 
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
 
        }
 
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
 
        }
 
        @Override
        public void afterTextChanged(Editable s) {
            if (mEditText.getText().length() > MAX_LENGTH) {
                String str = mEditText.getText().toString();
                if (str != null && str.length() > 10) {
                    str = str.substring(0, MAX_LENGTH);
                    mEditText.setText(str);
                    mEditText.setSelection(str.length());
                }
                toast(String.format(hint_max_length_msg, ""+MAX_LENGTH));
            }
            int enableCount = MAX_LENGTH;
            Editable curContent = mEditText.getText();
            if (curContent != null && curContent.length() > 0) {
                enableCount = MAX_LENGTH - curContent.length();
            }
            updateLengthHint(enableCount);
        }

    };
 
    private void updateLengthHint(int enableCount) {
        if (enableCount < 0) {
            enableCount = 0;
        } else if (enableCount > MAX_LENGTH) {
            enableCount = MAX_LENGTH;
        }
        mHintTextView.setText(String.format(hint_remain_count, ""+enableCount));
    }
 
    private void toast(String msg) {
        @SuppressWarnings("unused")
		Context context;
        if (msg != null && (context = getContext()) != null) {
            ToastUtils.show(msg);
        }
    }
 
    private Context getContext() {
        if (mEditText != null) {
            return mEditText.getContext();
        }
        return null;
    }
}
 
