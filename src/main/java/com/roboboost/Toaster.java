package com.roboboost;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Toaster {
    public static void showToast(final Context context, final String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToast(final Context context, final String message, final int duration) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, duration).show();
            }
        });
    }
}
