package com.droidities;

import android.app.Activity;
import android.content.Context;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AsyncAndroidFileCacheTest {
    @Test
    public void getCacheDir_shouldReturnTheContextCacheDir() throws Exception {
        Activity context = new Activity() {
            @Override
            public File getCacheDir() {
                return new File("/tmp/test/cache");
            }
        };

        File cacheDirectory = new TestAsyncAndroidFileCache(context).getCacheDirectory();
        assertThat(cacheDirectory.getAbsolutePath(), equalTo("/tmp/test/cache"));
    }

    private class TestAsyncAndroidFileCache extends AsyncAndroidFileCache {
        public TestAsyncAndroidFileCache(Context context) {
            super(context);
        }

        @Override
        public void getRemoteValue(Callback<InputStream> inputStreamCallback) {
        }
    }
}
