package com.tjh.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Files {
    public static byte[] fileBytes(final File file) throws IOException {
        if (file.length() > Integer.MAX_VALUE) {
            throw new RuntimeException("File length too long");
        }

        final InputStream inputStream = new FileInputStream(file);
        final byte[] result = new byte[(int) file.length()];

        int offset = 0;
        int bytesRead;
        while (offset < result.length
            && (bytesRead = inputStream.read(result, offset, result.length - offset)) >= 0) {
            offset += bytesRead;
        }

        if (offset < result.length) {
            throw new IOException(
                "Could only read " + offset + " bytes out of " + result.length + " from " + file.getName());
        }

        inputStream.close();

        return result;
    }

    public static void writeBytesToFile(final File file, final byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }

    public static Collection<File> find(final File baseDir, final FileFilter filter) {
        if (!baseDir.isDirectory()) {
            throw new IllegalArgumentException("baseDir must be a directory: " + baseDir);
        }

        final Collection<File> result = new HashSet<File>();

        final File[] matchedFiles = baseDir.listFiles(filter);
        
        if (matchedFiles != null) {
            result.addAll(Arrays.asList(matchedFiles));
        }

        final File[] subDirectories = baseDir.listFiles(new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.isDirectory() && pathname.canRead();
            }
        });
        if (subDirectories != null) {
            for (File file : subDirectories)

            {
                result.addAll(find(file, filter));
            }
        }
        return result;
    }

    public static File getTempDir(){ return new File(System.getProperty("java.io.tmpdir")); }
}
