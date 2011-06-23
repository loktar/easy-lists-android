package com.droidities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.droidities.cache.AsyncAndroidFileCache;
import com.droidities.cache.AsyncCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    ExecutorService pool = Executors.newFixedThreadPool(3);

    public void loadImage(final ImageView imageView, final String url) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                AsyncCache<InputStream> cache = createCache(imageView.getContext());
                cache.get(url, createCallback(imageView, url), createProvider(url));
            }
        });
    }

    protected AsyncCache<InputStream> createCache(Context context) {
        return new AsyncAndroidFileCache(context);
    }

    protected AsyncCache.Callback<InputStream> createCallback(ImageView imageView, String url) {
        return new SetImageDrawableCallback(imageView, url);
    }

    protected AsyncCache.Provider<InputStream> createProvider(String url) {
        return new UrlAsyncCacheProvider(url);
    }

    protected InputStream getInputStreamFromUrl(String url) {
        try {
            return new URL(url).openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class SetImageDrawableCallback implements AsyncCache.Callback<InputStream> {
        private ImageView imageView;
        private String url;

        public SetImageDrawableCallback(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }

        @Override
        public void cachedValueRetrieved(InputStream value) {
            final Drawable drawable = Drawable.createFromStream(value, url);
            // TODO Need to add shadow View#getHandler
//            imageView.getHandler().post(new Runnable() {
//                @Override
//                public void run() {
            imageView.setImageDrawable(drawable);
//                }
//            });
        }
    }

    private class UrlAsyncCacheProvider implements AsyncCache.Provider<InputStream> {
        private String url;

        public UrlAsyncCacheProvider(String url) {
            this.url = url;
        }

        @Override
        public void getValue(AsyncCache.Callback<InputStream> inputStreamCallback) {
            InputStream stream = getInputStreamFromUrl(url);
            inputStreamCallback.cachedValueRetrieved(stream);
        }
    }
}
