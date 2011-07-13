package com.tjh.util;

import com.tjh.util.Classpath;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClasspathTests {

    @Test
    public void urlsReturnsExistingURLsFromClasspath() throws URISyntaxException {
        for (URL url : Classpath.existingUrls()) {
            assertThat(new File(url.toURI()).exists(), is(true));
        }
    }

    @Test
    public void urlsForPackageWorks(){
        System.out.println("Classpath.urlsForPackage(\"java/lang\") = " + Classpath.urlsForPackage("java/lang"));
    }
}
