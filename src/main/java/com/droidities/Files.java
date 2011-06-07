package com.droidities;

import java.io.*;

public class Files {
    public static final int BUFFER_SIZE = 1024 * 4;

    public static String read(String fileName) throws IOException {
        return read(new File(fileName));
    }

    public static String read(File file) throws IOException {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            StringBuilder sb = new StringBuilder();

            int numRead;
            char[] buffer = new char[BUFFER_SIZE];
            while ((numRead = fileReader.read(buffer, 0, BUFFER_SIZE)) > -1) {
                sb.append(buffer, 0, numRead);
            }
            return sb.toString();
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    public static void write(String fileName, String contents) throws IOException {
        write(new File(fileName), contents);
    }

    public static void write(File file, String contents) throws IOException {
        write(file.getAbsolutePath(), new ByteArrayInputStream(contents.getBytes()));
    }

    public static void write(String fileName, InputStream inputStream) throws IOException {
        write(new File(fileName), inputStream);
    }

    public static void write(File file, InputStream inputStream) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);

            int numRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((numRead = inputStream.read(buffer, 0, BUFFER_SIZE)) > -1) {
                fileOutputStream.write(buffer, 0, numRead);
            }
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
