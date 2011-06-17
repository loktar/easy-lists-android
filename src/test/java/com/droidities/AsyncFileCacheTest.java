package com.droidities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AsyncFileCacheTest {
    private TestAsyncFileCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new TestAsyncFileCache();
    }

    @After
    public void tearDown() throws Exception {
        for (File storedFile : cache.storedFiles) {
            storedFile.delete();
        }
    }

    @Test
    public void storedFiles_shouldBeDeletedAfterEachTest() throws Exception {
        assertThat(cache.getCacheFileForKey("key1").exists(), equalTo(false));
        assertThat(cache.getCacheFileForKey("key2").exists(), equalTo(false));

        cache.storeValue("key1", new ByteArrayInputStream("1".getBytes()));
        cache.storeValue("key2", new ByteArrayInputStream("2".getBytes()));
    }

    @Test
    public void storeValue_shouldSaveTheAFileInTheDirectorySpecifiedByTheCacheDirMethod() throws Exception {
        cache.storeValue("key", new ByteArrayInputStream("abc".getBytes()));

        File expectedFile = new File(cache.getCacheDirectory(), "key");
        assertThat(expectedFile.exists(), equalTo(true));
    }

    @Test
    public void storeValue_shouldStoreFilesWithSpecialCharactersInTheKey() throws Exception {
        cache.storeValue("key/with??special/characters", new ByteArrayInputStream("abc".getBytes()));

        File expectedFile = new File(cache.getCacheDirectory(), "key_with__special_characters");
        assertThat(expectedFile.exists(), equalTo(true));
    }

    @Test
    public void getFileForKey_shouldReturnTheKeyEscaped() throws Exception {
        String fileName = cache.getFileNameForKey("key/with??special/characters");
        assertThat(fileName, equalTo("key_with__special_characters"));
    }

    @Test
    public void hasCacheValue_shouldReturnFalseIfTheKeyHasNotBeenStored() throws Exception {
        assertThat(cache.hasCacheValue("key"), equalTo(false));
    }

    @Test
    public void hasCacheValue_shouldReturnTrueIfTheKeyHasBeenStored() throws Exception {
        cache.storeValue("key", new ByteArrayInputStream("".getBytes()));
        assertThat(cache.hasCacheValue("key"), equalTo(true));
    }

    @Test
    public void getCachedValue_shouldReturnAStreamForTheCachedFile() throws Exception {
        cache.storeValue("key", new ByteArrayInputStream("abc".getBytes()));
        InputStream stream = cache.getCachedValue("key");

        byte[] buffer = new byte[3];
        stream.read(buffer);
        assertThat(new String(buffer), equalTo("abc"));
    }

    private class TestAsyncFileCache extends AsyncFileCache {
        List<File> storedFiles = new ArrayList<File>();

        @Override
        public File getCacheDirectory() {
            return new File("/tmp");
        }

        @Override
        public void getRemoteValue(Callback<InputStream> inputStreamCallback) {
        }

        @Override
        public void storeValue(String key, InputStream value) {
            super.storeValue(key, value);
            storedFiles.add(getCacheFileForKey(key));
        }
    }
}
