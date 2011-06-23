package com.droidities;

import android.content.Context;
import android.widget.ImageView;
import com.droidities.cache.AsyncAndroidFileCache;
import com.droidities.cache.AsyncCache;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class ImageLoaderTest {
    private ImageLoader imageLoader;

    @Before
    public void setUp() throws Exception {
        imageLoader = spy(new ImageLoader());
        imageLoader.pool = new ImmediatelyExecutingService();
        doReturn(new ByteArrayInputStream("remote value".getBytes())).when(imageLoader).getInputStreamFromUrl(anyString());
        when(imageLoader.createCache(any(Context.class))).thenReturn(new TestCache());
    }

    @Test
    public void loadImage_shouldSetTheImageViewsDrawable() throws Exception {
        ImageView imageView = new ImageView(null);
        imageLoader.loadImage(imageView, "http://image");
        assertThat(imageView.getDrawable(), notNullValue());
    }

    @Test
    public void loadImage_shouldUseAFixedThreadExecutorService() throws Exception {
        ExecutorService pool = new ImageLoader().pool;
        assertThat(pool, instanceOf(ThreadPoolExecutor.class));
        assertThat(((ThreadPoolExecutor) pool).getMaximumPoolSize(), equalTo(3));
    }

    @Test
    public void loadImage_shouldSetTheDrawableUsingTheExecutorService() throws Exception {
        TestExecutorService testExecutorService = new TestExecutorService();
        imageLoader.pool = testExecutorService;
        ImageView imageView = new ImageView(null);
        imageLoader.loadImage(imageView, "http://url");

        assertThat(imageView.getDrawable(), nullValue());
        testExecutorService.latestTask.run();
        assertThat(imageView.getDrawable(), notNullValue());
    }

    @Test
    public void loadImage_shouldSetTheImagesDrawableOnTheUiThread() throws Exception {
        Robolectric.pauseMainLooper();
        ImageView imageView = new ImageView(null);
        imageLoader.loadImage(imageView, "http://url");

        assertThat(imageView.getDrawable(), nullValue());

        Robolectric.unPauseMainLooper();
        assertThat(imageView.getDrawable(), notNullValue());
    }

    @Test
    public void loadImage_shouldUseCachedValues() throws Exception {
        TestProvider testProvider = new TestProvider();
        when(imageLoader.createProvider(anyString())).thenReturn(testProvider);

        ImageView imageView = new ImageView(null);
        imageLoader.loadImage(imageView, "http://image");
        imageLoader.loadImage(imageView, "http://image");

        assertThat(testProvider.callCount, equalTo(1));
    }

    @Test
    public void createCache_shouldUseAndroidFileCache() throws Exception {
        AsyncCache<InputStream> cache = new ImageLoader().createCache(null);
        assertThat(cache, instanceOf(AsyncAndroidFileCache.class));
    }

    @Test
    public void createCallback_shouldSetTheInputStreamAsTheImageViewsDrawable() throws Exception {
        ImageView imageView = new ImageView(null);
        AsyncCache.Callback<InputStream> callback = imageLoader.createCallback(imageView, "http://url");

        callback.cachedValueRetrieved(new ByteArrayInputStream("abc".getBytes()));

        assertThat(imageView.getDrawable(), notNullValue());
    }

    @Test
    public void createProvider_shouldGetTheResourceFromAUrlAndGiveItToTheCallback() throws Exception {
        AsyncCache.Provider<InputStream> provider = imageLoader.createProvider("http://image");

        TestCallback testCallback = new TestCallback();
        provider.getValue(testCallback);

        byte[] bytes = getBytesFromStream(testCallback.value);
        assertThat(new String(bytes), equalTo("remote value"));
    }

    private byte[] getBytesFromStream(InputStream value) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = value.read(buffer);
        byte[] bytes = new byte[bytesRead];
        System.arraycopy(buffer, 0, bytes, 0, bytesRead);
        return bytes;
    }

    private class TestCache extends AsyncCache<InputStream> {
        Map<String, byte[]> cache = new HashMap<String, byte[]>();

        @Override
        public InputStream getCachedValue(String key) {
            return new ByteArrayInputStream(cache.get(key));
        }

        @Override
        public void storeValue(String key, InputStream value) {
            try {
                byte[] bytes;
                bytes = getBytesFromStream(value);
                cache.put(key, bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasCacheValue(String key) {
            return cache.containsKey(key);
        }
    }

    private class TestProvider implements AsyncCache.Provider<InputStream> {
        private int callCount;

        @Override
        public void getValue(AsyncCache.Callback<InputStream> inputStreamCallback) {
            callCount++;
            inputStreamCallback.cachedValueRetrieved(new ByteArrayInputStream("remote value".getBytes()));
        }
    }

    private class TestCallback implements AsyncCache.Callback<InputStream> {
        private InputStream value;

        @Override
        public void cachedValueRetrieved(InputStream value) {
            this.value = value;
        }
    }

    private class TestExecutorService implements ExecutorService {
        private Runnable latestTask;

        @Override
        public void shutdown() {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public List<Runnable> shutdownNow() {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public boolean isShutdown() {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public boolean isTerminated() {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public Future<?> submit(Runnable task) {
            latestTask = task;
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new RuntimeException("Should not be called");
        }

        @Override
        public void execute(Runnable command) {
        }
    }

    private class ImmediatelyExecutingService extends TestExecutorService {
        @Override
        public Future<?> submit(Runnable task) {
            super.submit(task);
            task.run();
            return null;
        }
    }
}
