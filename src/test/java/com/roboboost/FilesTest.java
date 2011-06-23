package com.roboboost;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FilesTest {
    private File testDirectory;

    @Before
    public void setUp() throws Exception {
        testDirectory = new File("__test__");
        testDirectory.mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        for (String fileName : testDirectory.list()) {
            new File(testDirectory, fileName).delete();
        }
        testDirectory.delete();
    }

    @Test
    public void tests_shouldAlwaysCleanupTheTestDirectory() throws Exception {
        assertThat(testDirectory.list().length, equalTo(0));
    }

    @Test
    public void read_shouldReadTheFileIntoAString() throws Exception {
        writeToFile("__test__/f", "foo");

        assertThat(Files.read("__test__/f"), equalTo("foo"));
    }

    @Test
    public void read_shouldReadFilesLongerThanTheBuffer() throws Exception {
        FileWriter fileWriter = new FileWriter("__test__/f");
        for (int i = 0; i < Files.BUFFER_SIZE * 1.5; i++) {
            fileWriter.append("a");
        }
        fileWriter.close();

        assertThat(Files.read("__test__/f").length(), equalTo((int) new File("__test__/f").length()));
    }

    @Test
    public void read_shouldReadFromAFileObject() throws Exception {
        writeToFile("__test__/f", "foo");

        assertThat(Files.read(new File("__test__/f")), equalTo("foo"));
    }

    @Test
    public void read_shouldCloseTheItsOutputStream() throws Exception {
    }


    @Test
    public void write_shouldWriteToTheSpecifiedFileWithAString() throws Exception {
        Files.write("__test__/f", "foo");
        assertThat(new File("__test__/f").exists(), equalTo(true));
    }

    @Test
    public void write_shouldWriteToTheSpecifiedFileWithAStream() throws Exception {
        Files.write("__test__/f", getInputStreamFor("foo"));
        assertThat(new File("__test__/f").length(), equalTo(3L));
    }

    @Test
    public void write_shouldWriteTheSpecifiedContentsTo() throws Exception {
        Files.write("__test__/f", getInputStreamFor("foo"));

        File file = new File("__test__/f");
        assertThat(file.length(), equalTo(3L));

        char[] buffer = new char[3];
        new FileReader(file).read(buffer, 0, buffer.length);
        assertThat(new String(buffer), equalTo("foo"));
    }

    @Test
    public void write_shouldFilesLongerThanTheBufferLength() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Files.BUFFER_SIZE * 1.5; i++) {
            sb.append("a");
        }

        Files.write("__test__/f", getInputStreamFor(sb.toString()));

        File file = new File("__test__/f");
        assertThat(file.length(), equalTo((long) (Files.BUFFER_SIZE * 1.5)));
    }

    @Test
    public void write_shouldWorkWithAFileAndString() throws Exception {
        Files.write(new File("__test__/f"), "foo");
        assertThat(new File("__test__/f").length(), equalTo(3L));
    }

    @Test
    public void write_shouldWorkWithAFileAndStream() throws Exception {
        Files.write(new File("__test__/f"), getInputStreamFor("foo"));
        assertThat(new File("__test__/f").length(), equalTo(3L));
    }

    @Test
    public void write_shouldCloseTheFileOutputStream() throws Exception {
    }

    private ByteArrayInputStream getInputStreamFor(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    private void writeToFile(String fileName, String contents) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(contents);
        fileWriter.close();
    }

}
