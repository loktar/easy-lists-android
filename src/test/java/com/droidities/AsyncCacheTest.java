package com.droidities;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AsyncCacheTest {
    private TestCacheCallback callback;
    private TestProvider provider;
    private TestAsyncCache cache;

    @Before
    public void setUp() throws Exception {
        callback = new TestCacheCallback();
        provider = new TestProvider();
        cache = new TestAsyncCache();
    }

    @Test
    public void get_shouldCallTheCallbackWithTheRemoteValueIfNotCached() throws Exception {
        cache.get("key", callback, provider);

        assertThat(callback.value, equalTo("(stored) remote value 0"));
    }

    @Test
    public void get_shouldCallTheCallbackByRetrievingTheStoredValue() throws Exception {
        // This lets us cache read-once types like streams. First read goes into
        // the cache, then call callback with a new stream
        cache.get("key", callback, provider);

        assertThat(callback.value, equalTo("(stored) remote value 0"));
    }

    @Test
    public void get_shouldStoreTheRemoteValueIfNotCached() throws Exception {
        cache.get("key", callback, provider);

        assertThat(cache.storedKey, equalTo("key"));
        assertThat(cache.storedValue, equalTo("remote value 0"));
    }

    @Test
    public void get_shouldCallTheCallbackWithTheStoredValueIfAlreadyCached() throws Exception {
        cache.prepopulate("key", "cached value");

        cache.get("key", callback, provider);

        assertThat(callback.value, equalTo("cached value"));
    }

    @Test
    public void get_shouldUseTheCachedValueForSuccessiveCallsWithTheSameKey() throws Exception {
        cache.get("key", callback, provider);
        assertThat(callback.value, equalTo("(stored) remote value 0"));

        cache.get("key", callback, provider);
        assertThat(callback.value, equalTo("(stored) remote value 0"));
    }

    @Test
    public void get_shouldRetrieveDifferentValuesForDifferentKeys() throws Exception {
        cache.get("key0", callback, provider);
        assertThat(callback.value, equalTo("(stored) remote value 0"));

        cache.get("key1", callback, provider);
        assertThat(callback.value, equalTo("(stored) remote value 1"));
    }

    private class TestAsyncCache extends AsyncCache<String> {
        private Map<String, String> cache = new HashMap<String, String>();

        private String storedKey;
        private String storedValue;

        @Override
        public String getCachedValue(String key) {
            return cache.get(key);
        }

        @Override
        public void storeValue(String key, String value) {
            storedKey = key;
            storedValue = value;

            cache.put(key, "(stored) " + value);
        }

        @Override
        public boolean hasCacheValue(String key) {
            return cache.containsKey(key);
        }

        public void prepopulate(String key, String value) {
            cache.put(key, value);
        }
    }

    private class TestCacheCallback implements AsyncCache.Callback<String> {
        private String value;

        @Override
        public void cachedValueRetrieved(String value) {
            this.value = value;
        }
    }

    private class TestProvider implements AsyncCache.Provider<String> {
        private int count;

        @Override
        public void getValue(AsyncCache.Callback<String> callback) {
            callback.cachedValueRetrieved("remote value " + count++);
        }
    }
}
