package com.gaocheng.baselibrary.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by 32672 on 2019/1/4.
 */

public class KeyboardUtil {
    public  static void hideKeyBoard(Context mContext, EditText etSearch){
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }
    public  static void showKeyBoard(Context mContext, EditText etSearch){
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch,1);
    }
}
