package com.wzq.jz_app.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.wzq.jz_app.R;

public class ProgressUtils {

    private static ProgressDialog dialog = null;

    public static void show(Context context){
        show(context, null);
    }

    public static void show(Context context, String msg){
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg == null ? UiUtils.getString(R.string.load_msg) : msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismiss(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
