package com.roboboost.cache;

import android.content.Context;

import java.io.File;

public class AsyncAndroidFileCache extends AsyncFileCache {
    private Context context;

    public AsyncAndroidFileCache(Context context) {
        this.context = context;
    }

    @Override
    public File getCacheDirectory() {
        return context.getCacheDir();
    }
}
