package com.roboboost.cache;

/**
 * Provides a way to retrieve values through a cache layer in an asynchronous manner using callbacks.
 *
 * @param <T> The type resource being retrieved through the cache.
 */
public abstract class AsyncCache<T> {
    /**
     * Returns a the value from the cache for the specified key. This method will only be called if
     * {@code hasCacheValue} returns true for the key.
     *
     * @param key The key that identifies this cached resource.
     * @return The cached resource for the specified key.
     */
    public abstract T getCachedValue(String key);

    /**
     * Stores a value in the cache for the specified cache key.
     *
     * @param key   The key that identifies this cached resource.
     * @param value The value that should be stored in the cache.
     */
    public abstract void storeValue(String key, T value);

    /**
     * Determines whether this cache has the specified key in the cache. This method should not need
     * to be called by users of the cache, only by the cache itself.
     *
     * @param key The key that identifies this cached resource.
     * @return {@code true} if the cache has stored a value for the key, {@code false} otherwise
     */
    public abstract boolean hasCacheValue(String key);

    /**
     * Gets a value from the cache. This value will be retrieved from the provider if it is
     * not already in the cache. Once the value is retrieved, either from the provider or the
     * cache, it will be passed to the callback.
     *
     * @param key      The key that identifies this cached resource.
     * @param callback Receives the requested value after it is retrieved from either the cache
     *                 or the provider.
     * @param provider Provides the value when the key is not already in the cache.
     */
    public void get(String key, Callback<T> callback, Provider<T> provider) {
        if (!hasCacheValue(key)) {
            provider.getValue(new StoreProviderValueCallback(key));
        }
        T cachedValue = getCachedValue(key);
        callback.cachedValueRetrieved(cachedValue);
    }

    /**
     * Receives cached values after they have been retrieved from either
     * the cache or a {@code Provider}
     *
     * @param <T> The type of resource this callback will receive.
     */
    public static interface Callback<T> {
        /**
         * Receives a value requested from an {@code AsyncCache}
         *
         * @param value The value provided by the cache
         */
        void cachedValueRetrieved(T value);
    }

    /**
     * Provides values to store in an {@code AsyncCache} when the requested cache key
     * is note yet stored in the cache.
     *
     * @param <T> The type of resource to be stored in the cache.
     */
    public static interface Provider<T> {
        /**
         * Gets the value to store in the cache. This method is expected to pass the
         * value to the callback once it has been retrieved.
         *
         * @param callback This callback should be passed the value to be stored
         *                 in the cache.
         */
        void getValue(AsyncCache.Callback<T> callback);
    }

    private class StoreProviderValueCallback implements Callback<T> {
        private String key;

        public StoreProviderValueCallback(String key) {
            this.key = key;
        }

        @Override
        public void cachedValueRetrieved(T value) {
            AsyncCache.this.storeValue(key, value);
        }
    }
}
