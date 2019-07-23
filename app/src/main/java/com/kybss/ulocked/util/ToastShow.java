package com.kybss.ulocked.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017\11\21 0021.
 */

public class ToastShow {
    private static Toast toast;

    public static void show(Context context,
                            String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
