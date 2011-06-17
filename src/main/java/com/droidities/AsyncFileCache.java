package com.droidities;

import java.io.*;

public abstract class AsyncFileCache extends AsyncCache<InputStream> {
    public abstract File getCacheDirectory();

    @Override
    public InputStream getCachedValue(String key) {
        try {
            File cacheFile = getCacheFileForKey(key);
            return new FileInputStream(cacheFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void storeValue(String key, InputStream value) {
        try {
            File cacheFile = getCacheFileForKey(key);
            Files.write(cacheFile, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasCacheValue(String key) {
        return getCacheFileForKey(key).exists();
    }

    public String getFileNameForKey(String key) {
        return key.replaceAll("[^a-zA-Z0-9\\.]", "_");
    }

    public File getCacheFileForKey(String key) {
        String fileName = getFileNameForKey(key);
        return new File(getCacheDirectory(), fileName);
    }
}
