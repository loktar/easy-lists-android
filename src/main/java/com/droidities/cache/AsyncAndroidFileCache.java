package com.droidities.cache;

import android.content.Context;

import java.io.File;

public abstract class AsyncAndroidFileCache extends AsyncFileCache {
    private Context context;

    public AsyncAndroidFileCache(Context context) {
        this.context = context;
    }

    @Override
    public File getCacheDirectory() {
        return context.getCacheDir();
    }
}
