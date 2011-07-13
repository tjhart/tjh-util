package com.tjh.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class Classpath {
    public static URL[] existingUrls() {
        try {
            List<URL> urls = new ArrayList<URL>(Arrays.asList(allUrls()));

            for (Iterator<URL> urlIterator = urls.iterator(); urlIterator.hasNext();) {
                URL url = urlIterator.next();
                if (!new File(url.toURI()).exists()) urlIterator.remove();
            }

            return urls.toArray(new URL[urls.size()]);
        } catch (URISyntaxException e) { throw new RuntimeException(e); }
    }

    public static URL[] allUrls() {
        try {
            String[] paths = System.getProperty("java.class.path").split(File.pathSeparator);
            List<URL> urls = new ArrayList<URL>(paths.length);
            for (String path : paths) {
                urls.add(new File(path).toURI().toURL());
            }

            return urls.toArray(new URL[urls.size()]);
        } catch (MalformedURLException e) { throw new RuntimeException(e); }
    }

    public static URL[] urlsForPackage(String packageName) {
        try {
            final String packagePath = packageName.replace(".", File.separator);
            final Enumeration<URL> urlEnumeration =
                    Thread.currentThread().getContextClassLoader().getResources(packagePath);
            final Collection<URL> urls = new ArrayList<URL>();
            while (urlEnumeration.hasMoreElements()) {
                final String urlString = urlEnumeration.nextElement().toString();
                urls.add(new URL(urlString.substring(0, urlString.lastIndexOf(packagePath))));
            }

            return urls.toArray(new URL[urls.size()]);

        } catch (IOException e) { throw new RuntimeException(e); }
    }
}

