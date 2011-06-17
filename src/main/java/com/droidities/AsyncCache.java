package com.droidities;

public abstract class AsyncCache<T> {
    public abstract void getRemoteValue(Callback<T> callback);

    public abstract T getCachedValue(String key);

    public abstract void storeValue(String key, T value);

    public abstract boolean isCached(String key);

    public void get(String key, Callback<T> callback) {
        if (!isCached(key)) {
            getRemoteValue(new StoreRemoteValueCallback(key));
        }
        T cachedValue = getCachedValue(key);
        callback.cachedValueRetrieved(cachedValue);
    }

    public static interface Callback<T> {
        void cachedValueRetrieved(T value);
    }

    private class StoreRemoteValueCallback implements Callback<T> {
        private String key;

        public StoreRemoteValueCallback(String key) {
            this.key = key;
        }

        @Override
        public void cachedValueRetrieved(T remoteValue) {
            storeValue(key, remoteValue);
        }
    }
}
